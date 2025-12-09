package negocio.facade.jdbc.processosel;

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

import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcSeletivoUnidadeEnsinoEixoCursoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoUnidadeEnsinoEixoCurso extends ControleAcesso
		implements ProcSeletivoUnidadeEnsinoEixoCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ProcSeletivoUnidadeEnsinoEixoCurso() throws Exception {
		super();
		setIdEntidade("ProcSeletivoEixoCurso");
	}

	@Override
	public ProcSeletivoUnidadeEnsinoEixoCursoVO novo() throws Exception {
		ProcSeletivoUnidadeEnsinoEixoCurso.incluir(getIdEntidade());
		ProcSeletivoUnidadeEnsinoEixoCursoVO obj = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO procseletivounidadeensinoeixocurso( procseletivounidadeensino, eixocurso, nrvagaseixocurso) VALUES ( ?, ?, ?) returning codigo"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getProcSeletivoUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setInt(2, obj.getEixoCurso().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getNrVagasEixoCurso());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}

	}

	@Override
	public void alterar(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE procseletivounidadeensinoeixocurso set procSeletivounidadeensino = ?, eixocurso = ?, nrvagaseixocurso=?  WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getProcSeletivoUnidadeEnsino().getCodigo().intValue());
				sqlAlterar.setInt(2, obj.getEixoCurso().getCodigo().intValue());
				sqlAlterar.setInt(3, obj.getNrVagasEixoCurso());
				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuarioVO);
			return;
		};

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProcSeletivoUnidadeEnsinoEixoCurso(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ProcSeletivounidadeensinoeixocurso WHERE procSeletivoUnidadeEnsino = ").append(procSeletivoUnidadeEnsinoVO.getCodigo()).append(" and codigo not in (0 ");
		for (ProcSeletivoUnidadeEnsinoEixoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs()) {
			if (obj.getCodigo() > 0) {
				sql.append(", ").append(obj.getCodigo());
			}
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		for (ProcSeletivoUnidadeEnsinoEixoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs()) {
			obj.getProcSeletivoUnidadeEnsino().setCodigo(procSeletivoUnidadeEnsinoVO.getCodigo());
			obj.getProcSeletivoUnidadeEnsino().setProcSeletivo(procSeletivoUnidadeEnsinoVO.getProcSeletivo());
			if (obj.getCodigo() > 0) {
				alterar(obj, usuarioVO);
			} else {
				incluir(obj, usuarioVO);
			}
		}
	}

	@Override
	public void excluir(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
		ProcSeletivoUnidadeEnsino.excluir(getIdEntidade());
		String sql = "DELETE FROM ProcSeletivounidadeensinoeixocurso WHERE  (procSeletivoUnidadeEnsino = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());

	}


	@Override
	public ProcSeletivoUnidadeEnsinoEixoCursoVO consultarPorChavePrimaria(Integer procSeletivoUnidadeEnsino,
			Integer eixoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM procseletivounidadeEnsinoeixocurso WHERE procSeletivounidadeensino = ? and eixocurso = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, procSeletivoUnidadeEnsino.intValue(),
				eixoCurso.intValue());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados));
		}
		return  new ProcSeletivoUnidadeEnsinoEixoCursoVO();
	}

	public void validarDados(ProcSeletivoUnidadeEnsinoEixoCursoVO obj) throws Exception {
		if ((obj.getEixoCurso() == null) || (obj.getProcSeletivoUnidadeEnsino().getCodigo().intValue() == 0)) {
			throw new Exception("O campo UNIDADE ENSINO (Unidade de Ensino Processo Seletivo) deve ser informado.");
		}
	}

	public static List<ProcSeletivoUnidadeEnsinoEixoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados)
			throws Exception {
		List<ProcSeletivoUnidadeEnsinoEixoCursoVO> vetResultado = new ArrayList<ProcSeletivoUnidadeEnsinoEixoCursoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	public static ProcSeletivoUnidadeEnsinoEixoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		ProcSeletivoUnidadeEnsinoEixoCursoVO obj = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getProcSeletivoUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("procSeletivounidadeensino")));
		obj.getEixoCurso().setCodigo(new Integer(dadosSQL.getInt("eixocurso")));
		obj.setNrVagasEixoCurso(new Integer(dadosSQL.getInt("nrvagaseixocurso")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	public void incluirProcSeletivoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) {
		try {
			for (ProcSeletivoUnidadeEnsinoEixoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs()) {
	            obj.getProcSeletivoUnidadeEnsino().setCodigo(procSeletivoUnidadeEnsinoVO.getCodigo());
	            incluir(obj, usuarioVO);
	        }
		} catch (Exception e) {
			
		}
	}

	public static String getIdEntidade() {
		return ProcSeletivoUnidadeEnsinoEixoCurso.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ProcSeletivoUnidadeEnsinoEixoCurso.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcSeletivoEixoCurso(Integer procSeletivoUnidadeEnsino) throws Exception {
        String sql = "DELETE FROM ProcSeletivounidadeensinoeixocurso WHERE (procSeletivoUnidadeEnsino = ?)";
        getConexao().getJdbcTemplate().update(sql, procSeletivoUnidadeEnsino.intValue());
    }
	
	public static List consultarProcSeletivoEixoCursoVOs(Integer procSeletivo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoUnidadeEnsinoEixoCurso.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM procseletivounidadeensinoeixocurso WHERE procSeletivoUnidadeEnsino = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, procSeletivo.intValue());
        while (resultado.next()) {
            ProcSeletivoUnidadeEnsinoEixoCursoVO novoObj = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
            novoObj = ProcSeletivoUnidadeEnsinoEixoCurso.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }
	
	public static ProcSeletivoUnidadeEnsinoEixoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoUnidadeEnsinoEixoCursoVO obj = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getProcSeletivoUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("procSeletivoUnidadeEnsino")));
        obj.getEixoCurso().setCodigo(new Integer(dadosSQL.getInt("eixocurso")));
        obj.setNrVagasEixoCurso(dadosSQL.getInt("nrvagaseixocurso"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosEixoCurso(obj, nivelMontarDados);
        return obj;
    }
	
	public static void montarDadosEixoCurso(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, int nivelMontarDados) throws Exception {
        if (obj.getEixoCurso().getCodigo().intValue() == 0) {
            obj.setEixoCurso(new EixoCursoVO());
            return;
        }
        obj.setEixoCurso(getFacadeFactory().getEixoCursoFacade().consultarPorChavePrimaria(obj.getEixoCurso().getCodigo(), nivelMontarDados));
    }
	
	
	 @Override
	    public ProcSeletivoUnidadeEnsinoEixoCursoVO consultarPorInscricaoUnidadeEnsinoEixoCurso(Integer inscricao, Integer unidadeEnsino, Integer eixoCurso,  int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" select psuec.codigo as \"psuec.codigo\" , psuec.procSeletivounidadeensino as \"psuec.procSeletivounidadeensino\" ,  psuec.nrvagaseixocurso as \"psuec.nrvagaseixocurso\"  , ps.codigo as \"procseletivo.codigo\" , exc.codigo as \"eixocurso.codigo\" ,  exc.nome as \"eixocurso.nome\" from procseletivounidadeensino psu ");
	    	sb.append(" inner join procseletivo  ps on ps.codigo = psu.procseletivo ");  	
	    	sb.append(" inner join inscricao i on i.procseletivo = ps.codigo ");  	
	    	sb.append(" inner join procseletivounidadeensinoeixocurso psuec  on  psuec.procseletivounidadeensino =  psu.codigo  ");  	
	    	sb.append(" inner join eixocurso exc on exc.codigo = psuec.eixocurso  ");  	
	    	sb.append(" where  i.codigo = ? "); 
	    	sb.append(" and exc.codigo = ? ");
	        sb.append(" and psu.unidadeensino = ? ");
	    			   
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(),  new Object[] {inscricao,eixoCurso ,unidadeEnsino});	
    		ProcSeletivoUnidadeEnsinoEixoCursoVO obj = new ProcSeletivoUnidadeEnsinoEixoCursoVO();

	    	if(tabelaResultado.next()) {
	    		obj.setCodigo(new Integer(tabelaResultado.getInt("psuec.codigo")));
	    		obj.getProcSeletivoUnidadeEnsino().setCodigo(new Integer(tabelaResultado.getInt("psuec.procSeletivounidadeensino")));
	    		obj.setNrVagasEixoCurso(new Integer(tabelaResultado.getInt("psuec.nrvagaseixocurso")));
	    		obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().setCodigo(new Integer(tabelaResultado.getInt("procseletivo.codigo")));
	    		obj.getEixoCurso().setCodigo(new Integer(tabelaResultado.getInt("eixocurso.codigo")));
	    		obj.getEixoCurso().setNome(tabelaResultado.getString("eixocurso.nome"));
	    		obj.setNovoObj(Boolean.FALSE);	    		
	    	}
	    	return obj;
	    	
	    	
	    }
	
	
	

}
