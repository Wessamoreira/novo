package negocio.facade.jdbc.patrimonio;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.patrimonio.LocalArmazenamentoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class LocalArmazenamento extends ControleAcesso implements LocalArmazenamentoInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4986052840639241374L;
	private static String idEntidade = "LocalArmazenamento";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (localArmazenamentoVO.isNovoObj()) {
			incluir(localArmazenamentoVO, controlarAcesso, usuarioVO);
		} else {
			alterar(localArmazenamentoVO, controlarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {		
		LocalArmazenamento.incluir(getIdEntidade(), controlarAcesso, usuarioVO);
		validarDados(localArmazenamentoVO);

		
		try {
			final StringBuilder sql = new StringBuilder("INSERT INTO localArmazenamento (localArmazenamento, unidadeEnsino, localArmazenamentoSuperior, quantidadediaslimitereserva, permitereserva) values (?, ?, ?, ?, ?) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));							
			localArmazenamentoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, localArmazenamentoVO.getLocalArmazenamento());
					sqlInserir.setInt(2, localArmazenamentoVO.getUnidadeEnsinoVO().getCodigo());
					if(Uteis.isAtributoPreenchido(localArmazenamentoVO.getLocalArmazenamentoSuperiorVO())){
						sqlInserir.setInt(3, localArmazenamentoVO.getLocalArmazenamentoSuperiorVO().getCodigo());
					}else{
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, localArmazenamentoVO.getQuantidadeDiasLimiteReserva());
					sqlInserir.setBoolean(5, localArmazenamentoVO.getPermiteReserva());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						localArmazenamentoVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			
			localArmazenamentoVO.setNovoObj(false);
		} catch (DuplicateKeyException e) {
			throw new Exception(UteisJSF.internacionalizar("msg_LocalArmazenamento_unicidade"));
		} catch (Exception e) {
			throw e;
		} finally {
			
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		LocalArmazenamento.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
		validarDados(localArmazenamentoVO);
		final StringBuilder sql = new StringBuilder("UPDATE localArmazenamento set localArmazenamento = ?, unidadeEnsino = ?, localArmazenamentoSuperior = ?,  quantidadediaslimitereserva = ?, permitereserva = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));		
		
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setString(1, localArmazenamentoVO.getLocalArmazenamento());
				sqlAlterar.setInt(2, localArmazenamentoVO.getUnidadeEnsinoVO().getCodigo());
				if(Uteis.isAtributoPreenchido(localArmazenamentoVO.getLocalArmazenamentoSuperiorVO())){
					sqlAlterar.setInt(3, localArmazenamentoVO.getLocalArmazenamentoSuperiorVO().getCodigo());
				}else{
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, localArmazenamentoVO.getQuantidadeDiasLimiteReserva());
				sqlAlterar.setBoolean(5, localArmazenamentoVO.getPermiteReserva());
				sqlAlterar.setInt(6, localArmazenamentoVO.getCodigo());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(localArmazenamentoVO, controlarAcesso, usuarioVO);
		}

	}
	
	public void validarUnicidadeLocalArmazenamento(LocalArmazenamentoVO localArmazenamentoVO) throws ConsistirException{
		StringBuilder sql = new StringBuilder();
		sql.append(" select codigo from localArmazenamento ");		
		sql.append(" where unidadeensino = ? ");
		sql.append(" and trim(sem_acentos(localArmazenamento.localArmazenamento)) ilike (trim(sem_acentos(?)))");
		sql.append(" and localArmazenamento.codigo != ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), localArmazenamentoVO.getUnidadeEnsinoVO().getCodigo(), localArmazenamentoVO.getLocalArmazenamento(), localArmazenamentoVO.getCodigo());
		if(rs.next()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_unicidade"));
		}
		
	}
	
	@Override
	public List<LocalArmazenamentoVO> consultarPorLocalDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorLocal(Integer local, UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, FuncionarioVO solicitante){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from localArmazenamento ");
		sql.append(" where 1=1 ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and unidadeensino = ").append(unidadeEnsino.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(local)){
			sql.append(" and codigo = ").append(local);
		}
		sql.append(" order by codigo"); 
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<LocalArmazenamentoVO> objs = new ArrayList<LocalArmazenamentoVO>();
		while (tabelaResultado.next()) {
			LocalArmazenamentoVO obj = new LocalArmazenamentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setLocalArmazenamento(tabelaResultado.getString("localarmazenamento"));
			obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
			obj.getListaOcorrencias().addAll(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarOcorrenciaLocalParaGestao(obj.getCodigo(), dataInicial,dataFinal,solicitante));
			objs.add(obj);
		}
		return objs;
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		LocalArmazenamento.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM localArmazenamento where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), localArmazenamentoVO.getCodigo());
	}

	public StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT localArmazenamento.codigo, localArmazenamento.localarmazenamento, localArmazenamento.unidadeensino, localArmazenamento.localarmazenamentosuperior,   ");
		sql.append(" localArmazenamentoSuperior.localArmazenamento as \"localArmazenamentoSuperior.localArmazenamento\", ");
		sql.append(" localArmazenamentoSuperior.unidadeensino as \"localArmazenamentoSuperior.unidadeEnsino\", ");
		sql.append(" localArmazenamento.quantidadediaslimitereserva as \"localArmazenamento.quantidadediaslimitereserva\", ");
		sql.append(" localArmazenamento.permitereserva as \"localArmazenamento.permitereserva\", ");
		sql.append(" unidadeEnsino.nome as \"unidadeEnsino.nome\" ");
		sql.append(" FROM localArmazenamento ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = localArmazenamento.unidadeEnsino  ");
		sql.append(" left join localArmazenamento as localArmazenamentoSuperior on localArmazenamentoSuperior.codigo = localArmazenamento.localArmazenamentoSuperior  ");
		return sql;
	}

	@Override
	public List<LocalArmazenamentoVO> consultar(TipoConsultaLocalArmazenamentoEnum consultarPor, String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoLogado, Integer limit, Integer pagina, Boolean apenasLocalPermiteReserva) throws Exception {
		LocalArmazenamento.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where 1 = 1 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoLogado.getCodigo()).append(" ");
		}
		if(apenasLocalPermiteReserva){
			sql.append(" and localArmazenamento.permitereserva = true ");
		}
		if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.LOCAL)) {
			sql.append(" and sem_acentos(localArmazenamento.localArmazenamento) ilike (sem_acentos('").append(valorConsulta).append("%')) ");
			sql.append(" order by localArmazenamento.localArmazenamento ");
		} else if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR)) {
			sql.append(" and sem_acentos(localArmazenamentoSuperior.localArmazenamento) ilike (sem_acentos('").append(valorConsulta).append("%')) ");
			sql.append(" order by localArmazenamentoSuperior.localArmazenamento, localArmazenamento.localArmazenamento ");
		} else if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.UNIDADE_ENSINO)) {
			sql.append(" and sem_acentos(unidadeEnsino.nome) ilike (sem_acentos('").append(valorConsulta).append("%')) ");
			sql.append(" order by unidadeEnsino.nome, localArmazenamento.localArmazenamento ");
		}
		if (limit != null && limit.compareTo(0) > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}


	@Override
	public Integer consultarTotalRegistro(TipoConsultaLocalArmazenamentoEnum consultarPor, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoLogado, Boolean apenasLocalPermiteReserva) throws Exception {
	    
		StringBuilder sql = new StringBuilder("select count(localArmazenamento.codigo) as qtde ");
		sql.append(" FROM localArmazenamento ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = localArmazenamento.unidadeEnsino  ");
		sql.append(" left join localArmazenamento as localArmazenamentoSuperior on localArmazenamentoSuperior.codigo = localArmazenamento.localArmazenamentoSuperior  ");
		sql.append(" where 1 = 1 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoLogado.getCodigo()).append(" ");
		}
		if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.LOCAL)) {
			sql.append(" and sem_acentos(localArmazenamento.localArmazenamento) ilike (sem_acentos('").append(valorConsulta).append("%')) ");

		} else if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR)) {
			sql.append(" and sem_acentos(localArmazenamentoSuperior.localArmazenamento) ilike (sem_acentos('").append(valorConsulta).append("%')) ");

		} else if (consultarPor != null && consultarPor.equals(TipoConsultaLocalArmazenamentoEnum.UNIDADE_ENSINO)) {
			sql.append(" and sem_acentos(unidadeEnsino.nome) ilike (sem_acentos('").append(valorConsulta).append("%')) ");

		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<LocalArmazenamentoVO> consultarPorChavePrimaria(Integer codigo, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where localArmazenamento.codigo = "+ codigo +"");
		return montarDadosConsulta((getConexao().getJdbcTemplate().queryForRowSet(sql.toString())));
	}

	@Override
	public TreeNodeCustomizado consultarArvoreLocalArmazenamentoSuperior(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();		
		sql.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior,   ");
		sql.append(" \"localArmazenamentoSuperior.localArmazenamento\", ");
		sql.append(" \"localArmazenamentoSuperior.unidadeEnsino\", ");	
		sql.append(" \"localArmazenamento.quantidadediaslimitereserva\", ");
		sql.append(" \"localArmazenamento.permitereserva\", ");
		sql.append(" \"unidadeEnsino.nome\" ) as ( ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" where localarmazenamento.codigo = ").append(localArmazenamentoVO.getLocalArmazenamentoSuperiorVO().getCodigo());
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" inner join local on localarmazenamento.codigo = local.localarmazenamentosuperior ");
		sql.append(" ) select * from local order by local.codigo ");
				
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			LocalArmazenamentoVO obj = montarDados(rs);
//			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj, obj.getLocalArmazenamento(), "");
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);
			if (!nodes.containsKey(obj.getLocalArmazenamentoSuperiorVO().getCodigo())) {
				nodes.put(obj.getLocalArmazenamentoSuperiorVO().getCodigo(), TreeNodeCustomizadoRaiz);
			}
			nodeImpl.setData(obj);
			nodeImpl.setMaximizarTree(true);
			nodes.get(obj.getLocalArmazenamentoSuperiorVO().getCodigo()).addChild(obj, nodeImpl);
			nodes.put(obj.getCodigo(), nodeImpl);
		}
		return TreeNodeCustomizadoRaiz;
	}

	@Override
	public TreeNodeCustomizado consultarArvoreLocalArmazenamentoInferior(LocalArmazenamentoVO localArmazenamentoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();		
		sql.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior,   ");
		sql.append(" \"localArmazenamentoSuperior.localArmazenamento\", ");
		sql.append(" \"localArmazenamentoSuperior.unidadeEnsino\", ");		
		sql.append(" \"localArmazenamento.quantidadediaslimitereserva\", ");
		sql.append(" \"localArmazenamento.permitereserva\", ");		
		sql.append(" \"unidadeEnsino.nome\" ) as ( ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" where localarmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo ");
		sql.append(" ) select * from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			LocalArmazenamentoVO obj = montarDados(rs);
//			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj, obj.getLocalArmazenamento(), "");
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);
			if (!nodes.containsKey(obj.getLocalArmazenamentoSuperiorVO().getCodigo())) {
				
				nodes.put(obj.getLocalArmazenamentoSuperiorVO().getCodigo(), TreeNodeCustomizadoRaiz);
			}
			nodeImpl.setData(obj);
			nodeImpl.setMaximizarTree(true);
			nodes.get(obj.getLocalArmazenamentoSuperiorVO().getCodigo()).addChild(obj, nodeImpl);
			nodes.put(obj.getCodigo(), nodeImpl);
		}
		return TreeNodeCustomizadoRaiz;
	}

	@Override
	public void validarDados(LocalArmazenamentoVO localArmazenamentoVO) throws ConsistirException, Exception {
		validarUnicidadeLocalArmazenamento(localArmazenamentoVO);
		if (!Uteis.isAtributoPreenchido(localArmazenamentoVO.getUnidadeEnsinoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_unidadeEnsino"));
		}
		if (localArmazenamentoVO.getLocalArmazenamento().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_LocalArmazenamento"));
		}
		if (!Uteis.isAtributoPreenchido(localArmazenamentoVO.getQuantidadeDiasLimiteReserva()) || localArmazenamentoVO.getQuantidadeDiasLimiteReserva() < 1) {
			throw new Exception("O campo QUANTIDADE DIAS LIMITE PARA RESERVA deve ser informado e ser MAIOR QUE ZERO.");
		}
		validarDadosLocalSuperior(localArmazenamentoVO, localArmazenamentoVO.getLocalArmazenamentoSuperiorVO());
	}

	@Override
	public void validarDadosLocalSuperior(LocalArmazenamentoVO localArmazenamentoVO, LocalArmazenamentoVO localArmazenamentoSuperiorVO) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(localArmazenamentoSuperiorVO) && Uteis.isAtributoPreenchido(localArmazenamentoVO)) {
			if (localArmazenamentoVO.equals(localArmazenamentoSuperiorVO)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_localArmazenamentoSuperiorMesmo"));
			}
			if (Uteis.isAtributoPreenchido(localArmazenamentoSuperiorVO.getLocalArmazenamentoSuperiorVO()) && localArmazenamentoVO.equals(localArmazenamentoSuperiorVO.getLocalArmazenamentoSuperiorVO())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_localArmazenamentoSuperiorAutoRelacionamento"));
			}
			if (!localArmazenamentoSuperiorVO.getUnidadeEnsinoVO().getCodigo().equals(localArmazenamentoVO.getUnidadeEnsinoVO().getCodigo())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_LocalArmazenamento_localArmazenamentoSuperiorUnidadeDiferente"));
			}
		}
	}

	public List<LocalArmazenamentoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<LocalArmazenamentoVO> list = new ArrayList<LocalArmazenamentoVO>();
		while (rs.next()) {
			list.add(montarDados(rs));
		}
		return list;
	}

	public LocalArmazenamentoVO montarDados(SqlRowSet rs) throws Exception {
		LocalArmazenamentoVO obj = new LocalArmazenamentoVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNovoObj(false);
		obj.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsino"));
		obj.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino.nome"));
		obj.getLocalArmazenamentoSuperiorVO().setCodigo(rs.getInt("localArmazenamentoSuperior"));
		obj.getLocalArmazenamentoSuperiorVO().getUnidadeEnsinoVO().setCodigo(rs.getInt("localArmazenamentoSuperior.unidadeEnsino"));		
		obj.getLocalArmazenamentoSuperiorVO().setLocalArmazenamento(rs.getString("localArmazenamentoSuperior.localArmazenamento"));
		obj.setLocalArmazenamento(rs.getString("localArmazenamento"));
		obj.setQuantidadeDiasLimiteReserva(rs.getInt("localArmazenamento.quantidadediaslimitereserva"));
		obj.setPermiteReserva(rs.getBoolean("localArmazenamento.permitereserva"));
		 
		return obj;
	}

	public static String getIdEntidade() {
		if (LocalArmazenamento.idEntidade == null) {
			LocalArmazenamento.idEntidade = "LocalArmazenamento";
		}
		return LocalArmazenamento.idEntidade;
	}
	
	@Override
	public List<LocalArmazenamentoVO> consultarPorLocalArmazenamento(String localArmazenamento, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where localArmazenamento.localarmazenamento ilike '"+localArmazenamento+"%'");
		return montarDadosConsulta((getConexao().getJdbcTemplate().queryForRowSet(sql.toString())));
	}
	
	@Override
	public LocalArmazenamentoVO consultarPorCodigo(Integer codigo, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where localArmazenamento.codigo = "+ codigo +"");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado);
		}
		throw new Exception("");
	}

}
