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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.EnadeCursoInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class EnadeCurso extends ControleAcesso implements EnadeCursoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public EnadeCurso() {
		super();
		setIdEntidade("EnadeCurso");
	}

	public static String getIdEntidade() {
		return EnadeCurso.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		EnadeCurso.idEntidade = idEntidade;
	}

	public DisciplinaVO novo() throws Exception {
		Disciplina.incluir(getIdEntidade());
		DisciplinaVO obj = new DisciplinaVO();
		return obj;
	}

	public void validarDados(EnadeCursoVO enadeCursoVO) throws Exception {
		if (enadeCursoVO.getCursoVO() == null || enadeCursoVO.getCursoVO().getCodigo().equals(0)) {
			throw new Exception("O campo CURSO deve ser informado.");
		}
		if (enadeCursoVO.getAnoBaseIngressante().equals("")) {
			throw new Exception("O campo ANO BASE INGRESSANTE deve ser informado.");
		}
//		if (enadeCursoVO.getPercentualCargaHorariaIngressante().equals(0)) {
//			throw new Exception("O campo PERCENTUAL CARGA HORÁRIA INGRESSANTE deve ser informado.");
//		}
		if (enadeCursoVO.getPercentualCargaHorariaConcluinte().equals(0)) {
			throw new Exception("O campo PERCENTUAL CARGA HORÁRIA CONCLUINTE deve ser informado.");
		}
		if (enadeCursoVO.getPeriodoPrevistoTerminoIngressante() == null) {
			throw new Exception("O campo PERÍODO PREVISTO TÉRMINO INGRESSANTE deve ser informado.");
		}
		if (enadeCursoVO.getPeriodoPrevistoTerminoConcluinte() == null) {
			throw new Exception("O campo PERÍODO PREVISTO TÉRMINO CONCLUINTE deve ser informado.");
		}
		

	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EnadeCursoVO obj, UsuarioVO usuario) throws Exception {
		try {

			validarDados(obj);
			final String sql = "INSERT INTO EnadeCurso( enade, curso, anoBaseIngressante, percentualCargaHorariaIngressante, percentualCargaHorariaConcluinte, periodoPrevistoTerminoIngressante, periodoPrevistoTerminoConcluinte, considerarCargaHorariaAtividadeComplementar, considerarCargaHorariaEstagio ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);

					if (obj.getEnadeVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getEnadeVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getCursoVO().getCodigo() != 0) {
						sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getAnoBaseIngressante());
					sqlInserir.setInt(4, obj.getPercentualCargaHorariaIngressante());
					sqlInserir.setInt(5, obj.getPercentualCargaHorariaConcluinte());
					sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getPeriodoPrevistoTerminoIngressante()));
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getPeriodoPrevistoTerminoConcluinte()));
					sqlInserir.setBoolean(8, obj.getConsiderarCargaHorariaAtividadeComplementar());
					sqlInserir.setBoolean(9, obj.getConsiderarCargaHorariaEstagio());

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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final EnadeCursoVO obj, UsuarioVO usuario) throws Exception {
		try {

			validarDados(obj);
			final String sql = "UPDATE EnadeCurso set enade=?, curso = ?, anoBaseIngressante=?, percentualCargaHorariaIngressante=?, percentualCargaHorariaConcluinte=?, periodoPrevistoTerminoIngressante=?, periodoPrevistoTerminoConcluinte=?, considerarCargaHorariaAtividadeComplementar=?, considerarCargaHorariaEstagio=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getEnadeVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getEnadeVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getCursoVO().getCodigo() != 0) {
						sqlAlterar.setInt(2, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getAnoBaseIngressante());
					sqlAlterar.setInt(4, obj.getPercentualCargaHorariaIngressante());
					sqlAlterar.setInt(5, obj.getPercentualCargaHorariaConcluinte());
					sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getPeriodoPrevistoTerminoIngressante()));
					sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getPeriodoPrevistoTerminoConcluinte()));
					sqlAlterar.setBoolean(8, obj.getConsiderarCargaHorariaAtividadeComplementar());
					sqlAlterar.setBoolean(9, obj.getConsiderarCargaHorariaEstagio());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EnadeCursoVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM EnadeCurso WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static EnadeCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		EnadeCursoVO obj = new EnadeCursoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getEnadeVO().setCodigo(dadosSQL.getInt("enade"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		obj.setAnoBaseIngressante(dadosSQL.getString("anoBaseIngressante"));
		obj.setPercentualCargaHorariaIngressante(dadosSQL.getInt("percentualCargaHorariaIngressante"));
		obj.setPercentualCargaHorariaConcluinte(dadosSQL.getInt("percentualCargaHorariaConcluinte"));
		obj.setPeriodoPrevistoTerminoIngressante(dadosSQL.getDate("periodoPrevistoTerminoIngressante"));
		obj.setPeriodoPrevistoTerminoConcluinte(dadosSQL.getDate("periodoPrevistoTerminoConcluinte"));
		obj.setConsiderarCargaHorariaAtividadeComplementar(dadosSQL.getBoolean("considerarCargaHorariaAtividadeComplementar"));
		obj.setConsiderarCargaHorariaEstagio(dadosSQL.getBoolean("considerarCargaHorariaEstagio"));
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static void montarDadosCurso(EnadeCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), nivelMontarDados, false, usuario));
	}
	
	@Override
	public void incluirEnadeCursoVOs(EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception {
		for (EnadeCursoVO enadeCursoVO : enadeVO.getEnadeCursoVOs()) {
			enadeCursoVO.setEnadeVO(enadeVO);
			incluir(enadeCursoVO, usuarioVO);
		}

	}
	
	@Override
	public void alterarEnadeCursoVOs(EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception {
		excluirEnadeCursoVOs(enadeVO);
		for (EnadeCursoVO enadeCursoVO : enadeVO.getEnadeCursoVOs()) {
			enadeCursoVO.setEnadeVO(enadeVO);
			if (enadeCursoVO.getNovoObj()) {
				incluir(enadeCursoVO, usuarioVO);
			} else {
				alterar(enadeCursoVO, usuarioVO);
			}
		}

	}
	
	@Override
	public void excluirEnadeCursoVOs(EnadeVO enadeVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM EnadeCurso where enade = " + enadeVO.getCodigo() + " and codigo not in (0");
		for (EnadeCursoVO enadeCursoVO : enadeVO.getEnadeCursoVOs()) {
			sql.append(", ").append(enadeCursoVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());

	}
	
	@Override
	public void excluirEnadeCursoPorEnade(EnadeVO enadeVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM EnadeCurso where enade = " + enadeVO.getCodigo() + " ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Override
	public List<EnadeCursoVO> consultarPorEnade(Integer enade, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select * from enadeCurso where enade = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), enade);
		return montarDadosConsulta(rs, usuarioVO);
	}
	
	public List<EnadeCursoVO> consultarPorCodigoEnade(Integer valorConsulta, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct enadecurso.codigo AS \"enadecurso.codigo\",  enadecurso.anobaseingressante, enadecurso.percentualcargahorariaingressante, ");
		sb.append(" enadecurso.percentualcargahorariaconcluinte, enadecurso.periodoprevistoterminoingressante, enadecurso.periodoprevistoterminoconcluinte, ");
		sb.append(" enadecurso.considerarCargaHorariaAtividadeComplementar, enadecurso.considerarCargaHorariaEstagio, ");
		sb.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", enade.dataprova AS \"enade.dataprova\", enade.dataportaria AS \"enade.dataportaria\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.titulo AS \"curso.titulo\", curso.regime AS \"curso.regime\", ");
		sb.append(" curso.periodicidade AS \"curso.periodicidade\", curso.idcursoinep AS \"curso.idcursoinep\" ");
		sb.append(" from enadecurso ");
		sb.append(" inner join enade on enadecurso.enade = enade.codigo ");
		sb.append(" inner join curso on curso.codigo = enadecurso.curso ");
		sb.append(" where enade.codigo = ").append(valorConsulta);
		sb.append(" order by enade.codigo, curso.nome ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsultaRapida(tabelaResultado, usuario));
	}
	
	public List<EnadeCursoVO> consultarPorTituloEnade(String valorConsulta, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct enadecurso.codigo AS \"enadecurso.codigo\",  enadecurso.anobaseingressante, enadecurso.percentualcargahorariaingressante, ");
		sb.append(" enadecurso.percentualcargahorariaconcluinte, enadecurso.periodoprevistoterminoingressante, enadecurso.periodoprevistoterminoconcluinte, ");
		sb.append(" enadecurso.considerarCargaHorariaAtividadeComplementar, enadecurso.considerarCargaHorariaEstagio, ");
		sb.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", enade.dataprova AS \"enade.dataprova\", enade.dataportaria AS \"enade.dataportaria\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.titulo AS \"curso.titulo\", curso.regime AS \"curso.regime\", ");
		sb.append(" curso.periodicidade AS \"curso.periodicidade\", curso.idcursoinep AS \"curso.idcursoinep\", enade.codigoprojeto AS \"enade.codigoprojeto\" ");
		sb.append(" from enadecurso ");
		sb.append(" inner join enade on enadecurso.enade = enade.codigo ");
		sb.append(" inner join curso on curso.codigo = enadecurso.curso ");
		sb.append(" where ( enade.tituloEnade ) ilike(?) ");
		sb.append(" order by enade.tituloEnade, curso.nome ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		return (montarDadosConsultaRapida(tabelaResultado, usuario));
	}
	
 	public List<EnadeCursoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<EnadeCursoVO> vetResultado = new ArrayList<EnadeCursoVO>(0);
		while (tabelaResultado.next()) {
			EnadeCursoVO obj = new EnadeCursoVO();
			montarDadosBasico(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}
	
	private void montarDadosBasico(EnadeCursoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.setCodigo(dadosSQL.getInt("enadeCurso.codigo"));
		obj.setAnoBaseIngressante(dadosSQL.getString("anoBaseIngressante"));
		obj.setPercentualCargaHorariaIngressante(dadosSQL.getInt("percentualCargaHorariaIngressante"));
		obj.setPercentualCargaHorariaConcluinte(dadosSQL.getInt("percentualCargaHorariaConcluinte"));
		obj.setPeriodoPrevistoTerminoIngressante(dadosSQL.getDate("periodoPrevistoTerminoIngressante"));
		obj.setPeriodoPrevistoTerminoConcluinte(dadosSQL.getDate("periodoPrevistoTerminoConcluinte"));
		obj.getEnadeVO().setCodigo(dadosSQL.getInt("enade.codigo"));
		obj.getEnadeVO().setTituloEnade(dadosSQL.getString("enade.tituloEnade"));
		obj.getEnadeVO().setDataProva(dadosSQL.getDate("enade.dataProva"));
		obj.getEnadeVO().setDataPortaria(dadosSQL.getDate("enade.dataportaria"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getCursoVO().setTitulo(dadosSQL.getString("curso.titulo"));
		obj.getCursoVO().setRegime(dadosSQL.getString("curso.regime"));
		obj.getCursoVO().setPeriodicidade(dadosSQL.getString("curso.periodicidade"));
		obj.setConsiderarCargaHorariaAtividadeComplementar(dadosSQL.getBoolean("considerarCargaHorariaAtividadeComplementar"));
		obj.setConsiderarCargaHorariaEstagio(dadosSQL.getBoolean("considerarCargaHorariaEstagio"));
		obj.getCursoVO().setIdCursoInep(dadosSQL.getInt("curso.idcursoinep"));
		obj.getEnadeVO().setCodigoProjeto(dadosSQL.getString("enade.codigoprojeto"));
	}

}
