package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoControleGrupoOptativaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.PeriodoLetivoInterfaceFacade;
import webservice.servicos.PeriodoLetivoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PeriodoLetivoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PeriodoLetivoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see PeriodoLetivoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PeriodoLetivo extends ControleAcesso implements PeriodoLetivoInterfaceFacade {

	protected static String idEntidade;

	public PeriodoLetivo() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#novo()
	 */
	public PeriodoLetivoVO novo() throws Exception {
		PeriodoLetivo.incluir(getIdEntidade());
		PeriodoLetivoVO obj = new PeriodoLetivoVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#incluir(negocio
	 * .comuns.academico.PeriodoLetivoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PeriodoLetivoVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		try{
		PeriodoLetivoVO.validarDados(obj);
		final String sql = "INSERT INTO PeriodoLetivo( descricao, gradeCurricular, periodoLetivo, "
                        + "totalCargaHoraria, totalCreditos, nomeCertificacao, controleOptativaGrupo, "
                        + "numeroCreditoOptativa, numeroCargaHorariaOptativa, tipoControleGrupoOptativa, "
                        + "gradeCurricularGrupoOptativa, "
                        // novos
                        + "numeroMaximoCreditoAlunoPodeCursar, numeroMaximoCargaHorariaAlunoPodeCursar, "
                        + "numeroMinimoCreditoAlunoPodeCursar, numeroMinimoCargaHorariaAlunoPodeCursar, configuracaoAcademico "
                        + "  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getDescricao());
				sqlInserir.setInt(2, obj.getGradeCurricular().intValue());
				sqlInserir.setInt(3, obj.getPeriodoLetivo().intValue());
				sqlInserir.setInt(4, obj.getTotalCargaHoraria().intValue());
				sqlInserir.setInt(5, obj.getTotalCreditos().intValue());
				sqlInserir.setString(6, obj.getNomeCertificacao());
				sqlInserir.setBoolean(7, obj.getControleOptativaGrupo());
				sqlInserir.setInt(8, obj.getNumeroCreditoOptativa());
				sqlInserir.setInt(9, obj.getNumeroCargaHorariaOptativa());
				sqlInserir.setString(10, obj.getTipoControleGrupoOptativa().name());
				if(obj.getControleOptativaGrupo() && obj.getGradeCurricularGrupoOptativa().getCodigo() != null && obj.getGradeCurricularGrupoOptativa().getCodigo() > 0){
					sqlInserir.setInt(11, obj.getGradeCurricularGrupoOptativa().getCodigo());
				}else{
					sqlInserir.setNull(11, 0);
				}
                sqlInserir.setInt(12, obj.getNumeroMaximoCreditoAlunoPodeCursar());
				sqlInserir.setInt(13, obj.getNumeroMaximoCargaHorariaAlunoPodeCursar());
				int i = 13;
				Uteis.setValuePreparedStatement(obj.getNumeroMinimoCreditoAlunoPodeCursar(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getNumeroMinimoCargaHorariaAlunoPodeCursar(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getConfiguracaoAcademicoVO(), ++i, sqlInserir);
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
		getFacadeFactory().getGradeDisciplinaFacade().incluirGradeDisciplinas(obj.getCodigo(), obj.getGradeDisciplinaVOs(), situacaoGradeCurricular, usuario);
		} catch (Exception e) {
			obj.setCodigo(0);
            throw e;
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#alterar(negocio
	 * .comuns.academico.PeriodoLetivoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PeriodoLetivoVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		try {
			PeriodoLetivoVO.validarDados(obj);
			final String sql = "UPDATE PeriodoLetivo set descricao=?, gradeCurricular=?, periodoLetivo=?, " 
			+ "totalCargaHoraria=?, totalCreditos=?, nomeCertificacao=?, controleOptativaGrupo = ?, " 
			+ "numeroCreditoOptativa = ?, numeroCargaHorariaOptativa = ?, " 
			+ "tipoControleGrupoOptativa = ?, " 
			+ "gradeCurricularGrupoOptativa=?, "
			// novos
			+ "numeroMaximoCreditoAlunoPodeCursar=?, numeroMaximoCargaHorariaAlunoPodeCursar=?, "
			+ "numeroMinimoCreditoAlunoPodeCursar=?, numeroMinimoCargaHorariaAlunoPodeCursar=?, configuracaoAcademico = ? "
			+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getGradeCurricular().intValue());
					sqlAlterar.setInt(3, obj.getPeriodoLetivo().intValue());
					sqlAlterar.setInt(4, obj.getTotalCargaHoraria().intValue());
					sqlAlterar.setInt(5, obj.getTotalCreditos().intValue());
					sqlAlterar.setString(6, obj.getNomeCertificacao());
					sqlAlterar.setBoolean(7, obj.getControleOptativaGrupo());
					sqlAlterar.setInt(8, obj.getNumeroCreditoOptativa());
					sqlAlterar.setInt(9, obj.getNumeroCargaHorariaOptativa());
					sqlAlterar.setString(10, obj.getTipoControleGrupoOptativa().name());
					if (obj.getControleOptativaGrupo() && obj.getGradeCurricularGrupoOptativa().getCodigo() != null && obj.getGradeCurricularGrupoOptativa().getCodigo() > 0) {
						sqlAlterar.setInt(11, obj.getGradeCurricularGrupoOptativa().getCodigo());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setInt(12, obj.getNumeroMaximoCreditoAlunoPodeCursar());
					sqlAlterar.setInt(13, obj.getNumeroMaximoCargaHorariaAlunoPodeCursar());
					int i = 13;
					Uteis.setValuePreparedStatement(obj.getNumeroMinimoCreditoAlunoPodeCursar(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNumeroMinimoCargaHorariaAlunoPodeCursar(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getConfiguracaoAcademicoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
			getFacadeFactory().getGradeDisciplinaFacade().alterarGradeDisciplinas(obj.getCodigo(), obj.getGradeDisciplinaVOs(), situacaoGradeCurricular, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#excluir(negocio
	 * .comuns.academico.PeriodoLetivoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PeriodoLetivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			PeriodoLetivo.excluir(getIdEntidade());
			String sql = "DELETE FROM PeriodoLetivo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getGradeDisciplinaFacade().excluirGradeDisciplinas(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarPorGradeCurricularDisciplina(java.lang.Integer,
	 * java.lang.Integer, int)
	 */
	// public List consultarPorSiglaPeriodoLetivo(String valorConsulta) throws
	// Exception {
	// ControleAcesso.consultar(getIdEntidade(), true);
	// String sqlStr =
	// "SELECT GradeCurricular.* FROM GradeCurricular, PeriodoLetivo WHERE GradeCurricular.periodoLetivo = PeriodoLetivo.codigo and lower (PeriodoLetivo.sigla) like('"
	// + valorConsulta.toLowerCase() + "%') ORDER BY PeriodoLetivo.sigla";
	// Statement stm = con.createStatement();
	// ResultSet tabelaResultado = stm.executeQuery(sqlStr);
	// return montarDadosConsulta(tabelaResultado);
	// }
	public PeriodoLetivoVO consultarPorGradeCurricularDisciplina(Integer disciplina, Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(disciplina) && Uteis.isAtributoPreenchido(gradeCurricular)) {
			String sqlStr = "select periodoletivo.* " + "from gradedisciplina " + "inner join disciplina on disciplina.codigo = gradedisciplina.disciplina " + "inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo " + "where periodoletivo.gradecurricular = " + gradeCurricular + " and disciplina.codigo = " + disciplina;
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (tabelaResultado.next()) {
				return montarDados(tabelaResultado, nivelMontarDados, usuario);
			}
		}
		return new PeriodoLetivoVO();
	}

	public PeriodoLetivoVO consultarPorPeriodoLetivoGradeCurricular(Integer periodoLetivo, Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select distinct periodoletivo.* " + "from gradedisciplina " + "inner join disciplina on disciplina.codigo = gradedisciplina.disciplina " + "inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo " + "where periodoletivo.gradecurricular = " + gradeCurricular + " and periodoletivo.periodoLetivo = " + periodoLetivo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new PeriodoLetivoVO();
	}

	public PeriodoLetivoVO consultarUltimoPeriodoLetivoGradeCurricular(Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select distinct periodoletivo.* " + "from gradedisciplina " + "inner join disciplina on disciplina.codigo = gradedisciplina.disciplina " + "inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo " + "where periodoletivo.gradecurricular = " + gradeCurricular + " order by periodoletivo.periodoLetivo desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new PeriodoLetivoVO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarPorDescricao(java.lang.String, boolean, int)
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivo WHERE lower (descricao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#consultarPorCodigo
	 * (java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PeriodoLetivoVO</code> resultantes da consulta.
	 */
	public static List<PeriodoLetivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PeriodoLetivoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PeriodoLetivoVO</code>.
	 * 
	 * @return O objeto da classe <code>PeriodoLetivoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static PeriodoLetivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// //System.out.println(">> Montar dados(PeriodoLetivo) - " + new Date());
		PeriodoLetivoVO obj = new PeriodoLetivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoLetivo")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setGradeCurricular(new Integer(dadosSQL.getInt("gradeCurricular")));
		
		obj.setTotalCargaHoraria(new Integer(dadosSQL.getInt("totalCargaHoraria")));
		obj.setTotalCreditos(new Integer(dadosSQL.getInt("totalCreditos")));
		obj.setControleOptativaGrupo(dadosSQL.getBoolean("controleOptativaGrupo"));
		if(dadosSQL.getBoolean("controleOptativaGrupo")){
			obj.setNumeroCreditoOptativa(dadosSQL.getInt("numeroCreditoOptativa"));
			obj.setNumeroCargaHorariaOptativa(dadosSQL.getInt("numeroCargaHorariaOptativa"));
		}
                
        obj.setNumeroMaximoCreditoAlunoPodeCursar(dadosSQL.getInt("numeroMaximoCreditoAlunoPodeCursar"));
		obj.setNumeroMaximoCargaHorariaAlunoPodeCursar(dadosSQL.getInt("numeroMaximoCargaHorariaAlunoPodeCursar"));
		obj.setNumeroMinimoCreditoAlunoPodeCursar(dadosSQL.getInt("numeroMinimoCreditoAlunoPodeCursar"));
		obj.setNumeroMinimoCargaHorariaAlunoPodeCursar(dadosSQL.getInt("numeroMinimoCargaHorariaAlunoPodeCursar"));
                
		if(dadosSQL.getString("tipoControleGrupoOptativa") != null && !dadosSQL.getString("tipoControleGrupoOptativa").trim().isEmpty()){
			obj.setTipoControleGrupoOptativa(TipoControleGrupoOptativaEnum.valueOf(dadosSQL.getString("tipoControleGrupoOptativa")));
		}
		obj.getGradeCurricularGrupoOptativa().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativa"));
		
		 obj.setNomeCertificacao(dadosSQL.getString("nomeCertificacao"));
		 obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico"));		 
		obj.setNovoObj(Boolean.FALSE);
		if ((nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) || (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS)) {
			return obj;
		}
		obj.setGradeDisciplinaVOs(getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(obj.getCodigo(), false, usuario,  obj.getConfiguracaoAcademicoVO()));
		obj.setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(obj.getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, usuario));        		
		//Ordenacao.ordenarLista(obj.getGradeDisciplinaVOs(), "ordenacao");
		
		return obj;
	}
	
	public void montarDadosGradeCurricularGrupoOptativa(PeriodoLetivoVO periodoLetivoVO, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
		if(periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo() == null || periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo() == 0){
			return;
		}
		periodoLetivoVO.setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo(), nivelMontarDados, usuarioVO));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * excluirPeriodoLetivo(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPeriodoLetivo(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		CursoTurno.excluir(getIdEntidade());
		String sql = "DELETE FROM PeriodoLetivo WHERE (gradeCurricular = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { gradeCurricular });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * alterarPeriodoLetivo(java.lang.Integer, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPeriodoLetivo(Integer gradeCurricularPrm, List objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		incluirPeriodoLetivo(gradeCurricularPrm, objetos, situacaoGradeCurricular, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * incluirPeriodoLetivo(java.lang.Integer, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPeriodoLetivo(Integer gradeCurricularPrm, List<PeriodoLetivoVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		boolean encontrado = false;
		List<PeriodoLetivoVO> periodoLetivoAntigoVOs = consultarPorGradeCurricular(gradeCurricularPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		for (PeriodoLetivoVO periodoLetivoAntigoVO : periodoLetivoAntigoVOs) {
			encontrado = false;
			for (PeriodoLetivoVO periodoLetivoVO : objetos) {
				if (periodoLetivoAntigoVO.getCodigo().equals(periodoLetivoVO.getCodigo())) {
					encontrado = true;
				}
			}
			if (!encontrado) {
				excluir(periodoLetivoAntigoVO, usuario);
			}
		}

		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) e.next();
			obj.setGradeCurricular(gradeCurricularPrm);
			obj.atualizarTotalCargaHoraria();
			if (obj.getCodigo().equals(0)) {
				incluir(obj, situacaoGradeCurricular, usuario);
			} else {
				alterar(obj, situacaoGradeCurricular, usuario);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public PeriodoLetivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<PeriodoLetivoVO> consultarPeriodoLetivos(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<PeriodoLetivoVO> objetos = new ArrayList<PeriodoLetivoVO>(0);
		String sqlStr = "SELECT * FROM PeriodoLetivo WHERE gradeCurricular = ? ORDER BY periodoletivo;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { gradeCurricular });
		while (tabelaResultado.next()) {
			objetos.add(PeriodoLetivo.montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return objetos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarProximoPeriodoLetivoCurso(java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, boolean, int)
	 */
	public PeriodoLetivoVO consultarProximoPeriodoLetivoCurso(Integer gradeCurricular, Integer curso, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PeriodoLetivo.* FROM PeriodoLetivo, GradeCurricular, Curso " + " WHERE PeriodoLetivo.gradeCurricular = gradeCurricular.codigo and PeriodoLetivo.gradeCurricular = ? and" + " GradeCurricular.curso = Curso.codigo and Curso.codigo = ? and PeriodoLetivo.periodoLetivo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { gradeCurricular, curso, periodoLetivo });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<PeriodoLetivoVO> consultarPorGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PeriodoLetivo.* FROM PeriodoLetivo WHERE gradeCurricular = ? ORDER BY periodoLetivo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { gradeCurricular });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>GradeDisciplinaVO</code> relacionados a um objeto da classe
	 * <code>academico.PeriodoLetivo</code>.
	 * 
	 * @param gradeCurricular
	 *            Atributo de <code>academico.PeriodoLetivo</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>GradeDisciplinaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>GradeDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static PeriodoLetivoVO consultarPorCursoPorPerioLetivo(Integer periodoLetivo, Integer curso, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from periodoLetivo where periodoletivo = ? and curso = ? and gradeCurrucular=?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { periodoLetivo, curso, gradeCurricular });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarPorMatricula(java.lang.String, boolean, int)
	 */
	
	public List<PeriodoLetivoVO> consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct periodoletivo.* FROM periodoletivo " + " LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo " + " WHERE matriculaperiodo.matricula = '" + matricula + "' ORDER BY periodoletivo.periodoletivo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<PeriodoLetivoVO> consultarTodosPeriodosPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT periodoletivo.* FROM matricula INNER JOIN periodoletivo ON periodoletivo.gradecurricular = matricula.gradeCurricularAtual WHERE matricula.matricula = ? ORDER BY periodoletivo.periodoletivo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, matricula);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	
	public List<PeriodoLetivoVO> consultarPorMatriculaPeriodoLetivo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct periodoletivo.periodoletivo, periodoletivo.codigo FROM periodoletivo LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo " + "WHERE matriculaperiodo.matricula = '" + matricula + "' ORDER BY periodoletivo.periodoletivo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<PeriodoLetivoVO> vetResultado = new ArrayList<PeriodoLetivoVO>(0);
		while (tabelaResultado.next()) {
			PeriodoLetivoVO obj = new PeriodoLetivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setPeriodoLetivo(new Integer(tabelaResultado.getInt("periodoLetivo")));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List consultarPorMatriculaPorMatriculaPeriodoSituacaoDiferenteAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM periodoletivo ");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append(" WHERE matriculaperiodo.matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("' and matriculaPeriodo.situacaoMatriculaPeriodo <> 'AT' ");
		sqlStr.append(" ORDER BY periodoletivo.periodoletivo ");
		// String sqlStr = "SELECT * FROM periodoletivo " +
		// "LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo "
		// + "WHERE matriculaperiodo.matricula = '" + matricula +
		// "' ORDER BY periodoletivo.periodoletivo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#
	 * consultarPorMatricula(java.lang.String, boolean, int)
	 */
	public List consultarPorMatriculaCurso(String matricula, Integer curso, String periodicidadeCurso,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from ( ");
		sqlStr.append("select distinct (periodoletivo.codigo), periodoletivo.*, matriculaPeriodo.situacaomatriculaperiodo from periodoLetivo  ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.periodoLetivomatricula = periodoletivo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaPeriodo.matricula ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" where  matricula.matricula = '");
		sqlStr.append(matricula.toString());
		sqlStr.append("'");
		sqlStr.append(" and curso.codigo = ");
		sqlStr.append(curso.intValue());
		sqlStr.append(" ) as t ");
		
		if(Uteis.isAtributoPreenchido(periodicidadeCurso) && periodicidadeCurso.equals("IN")){
			sqlStr.append(" order by case when t.situacaomatriculaperiodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end ");	
		}else{
			sqlStr.append(" order by t.descricao");	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorTurma(TurmaVO turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct periodoletivo.* FROM periodoletivo ");
		sqlStr.append("LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma AND turma.periodoletivo = periodoletivo.codigo ");
		if (turma.getTurmaAgrupada()) {
			if (turma.getTurmaAgrupadaVOs().size() > 0) {
				sqlStr.append(" WHERE ");
				String condicao = "";
				for (TurmaAgrupadaVO turmaAgrupada : turma.getTurmaAgrupadaVOs()) {
					sqlStr.append(condicao).append(" matriculaperiodoturmadisciplina.turma = ").append(turmaAgrupada.getTurma().getCodigo());
					condicao = " OR ";
				}
			}
		} else {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo());
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}

		sqlStr.append(" ORDER BY periodoletivo.periodoletivo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return PeriodoLetivo.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#setIdEntidade
	 * (java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		PeriodoLetivo.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>GradeDisciplinaVO</code> no List <code>gradeDisciplinaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>GradeDisciplina</code> - getDisciplina().getCodigo() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param disciplina
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirObjGradeDisciplinaVOs(GradeDisciplinaVO grade, PeriodoLetivoVO periodoLetivoVO, Boolean validarAlteracaoMatrizCurricularAtivaInativa, UsuarioVO usuario) throws Exception {
		int index = 0;
		Boolean deveAtualizarCargaHoraria = false;
		Iterator i = periodoLetivoVO.getGradeDisciplinaVOs().iterator();
		while (i.hasNext()) {
			GradeDisciplinaVO objExistente = (GradeDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(grade.getDisciplina().getCodigo())) {
				if (!grade.getCodigo().equals(0)) {
					if (validarAlteracaoMatrizCurricularAtivaInativa) {
						getFacadeFactory().getGradeDisciplinaFacade().excluirImpactoExclusaoGradeDisciplina(grade, usuario);
						getFacadeFactory().getLogGradeCurricularInterfaceFacade().realizarCriacaoLogExclusaoGradeDisciplina(periodoLetivoVO.getGradeCurricular(), periodoLetivoVO, grade, usuario);
						deveAtualizarCargaHoraria = true;
					}
					getFacadeFactory().getGradeDisciplinaFacade().excluir(grade, usuario);
				}
				periodoLetivoVO.setTotalCargaHoraria(periodoLetivoVO.getTotalCargaHoraria() - grade.getCargaHoraria());
				periodoLetivoVO.setTotalCreditos(periodoLetivoVO.getTotalCreditos() - grade.getNrCreditos());
				if (deveAtualizarCargaHoraria) {
					alterarCargaHorariaENrCredito(periodoLetivoVO.getCodigo(), periodoLetivoVO.getTotalCargaHoraria(), periodoLetivoVO.getTotalCreditos(), usuario);
				}
				periodoLetivoVO.getGradeDisciplinaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public List consultarPorMatriculaPorMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM periodoletivo ");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append(" WHERE matriculaperiodo.matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("' ORDER BY periodoletivo.periodoletivo ");
		// String sqlStr = "SELECT * FROM periodoletivo " +
		// "LEFT JOIN matriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivo.codigo "
		// + "WHERE matriculaperiodo.matricula = '" + matricula +
		// "' ORDER BY periodoletivo.periodoletivo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<PeriodoLetivoVO> consultarPorCursoGradeCurricularAtiva(Integer codigoCurso, Integer gradecurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select periodoLetivo.codigo, periodoLetivo.periodoLetivo, periodoLetivo.descricao from periodoLetivo "); 
		sqlStr.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular "); 
		sqlStr.append(" where gradecurricular.curso = ").append(codigoCurso);
		sqlStr.append(" and gradecurricular.situacao = 'AT' ");
		sqlStr.append(" and gradecurricular.codigo = ").append(gradecurricular);
		sqlStr.append(" order by periodoLetivo.periodoLetivo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public PeriodoLetivoVO consultarPorAnoSemestreMatriculaPeriodoEMatricula(String matricula, String ano, String semestre, String periodicidadeCurso, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select periodoletivo.codigo, periodoletivo.periodoletivo, periodoletivo.descricao from periodoletivo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		if (!periodicidadeCurso.equals("IN")) {
			if (periodicidadeCurso.equals("AN")) {
				if (!ano.equals("")) {
					sb.append(" and ano = '").append(ano).append("' ");
				}
			} else {
				if (!ano.equals("")) {
					sb.append(" and ano = '").append(ano).append("' ");
				}
				if (!semestre.equals("")) {
					sb.append(" and semestre = '").append(semestre).append("' ");
				}
			}
		}
		sb.append(" order by case when matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end asc, matriculaperiodo.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PeriodoLetivoVO obj = null;
		if (tabelaResultado.next()) {
			obj = new PeriodoLetivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
		}
		return obj;
	}
	
	public void removerGradeDisciplinaForaGradeInclusaoExclusaoDisciplinaVOs(GradeDisciplinaVO grade, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, UsuarioVO usuario) throws Exception {
		for (PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivoVOs) {
			int index = 0;
			Iterator i = periodoLetivoVO.getGradeDisciplinaVOs().iterator();
			while (i.hasNext()) {
				GradeDisciplinaVO objExistente = (GradeDisciplinaVO) i.next();
				if (objExistente.getDisciplina().getCodigo().equals(grade.getDisciplina().getCodigo())) {
					periodoLetivoVO.getGradeDisciplinaVOs().remove(index);
					return;
				}
				index++;
			}
		}
	}
	
	public Integer consultarQuantidadePeriodoLetivoACursar(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ((SELECT max(PeriodoLetivo.periodoletivo) FROM PeriodoLetivo WHERE gradeCurricular = matriculaperiodo.gradecurricular) - periodoletivo.periodoletivo) as qtdePeriodoFaltaCursar ");
		sb.append(" from matriculaperiodo ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" WHERE MatriculaPeriodo.matricula = '").append(matricula).append("' ");
		sb.append(" order by (MatriculaPeriodo.ano ||'/' || MatriculaPeriodo.semestre) desc limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getInt("qtdePeriodoFaltaCursar");
		}
		return 0;
	}
	
	public Integer consultarPeriodoLetivoAnoBaseIngressoAluno(String matricula, String anoIngressoAluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select periodoletivo from ( ");
		sb.append(" select distinct matriculaperiodo.codigo, ano, semestre, periodoletivo.periodoletivo from periodoletivo ");
		sb.append(" inner join matriculaperiodo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		sb.append(" and ano = '").append(anoIngressoAluno).append("' ");
		sb.append(" order by matriculaperiodo.codigo, semestre, periodoletivo.periodoletivo ");
		sb.append(" limit 1 ");
		sb.append(" ) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("periodoLetivo");
		}
		return 0;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarUltimoPeriodoLetivoPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select periodoletivo from (");
		sb.append(" select periodoletivo.periodoletivo, matriculaperiodo.codigo from periodoletivo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		sb.append(" and periodoletivo.gradecurricular = ").append(gradeCurricular);
		sb.append(" order by matriculaperiodo.codigo desc, periodoletivo.periodoletivo desc limit 1 ");
		sb.append(" ) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("periodoLetivo");
		}
		return 0;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PeriodoLetivoVO consultarPeriodoLetivoAtualPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select codigoPeriodoLetivo, descricao, periodoletivo from (");
		sb.append(" select periodoletivo.periodoletivo, periodoletivo.descricao, periodoletivo.codigo AS codigoPeriodoLetivo, matriculaperiodo.codigo from periodoletivo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		sb.append(" and periodoletivo.gradecurricular = ").append(gradeCurricular);
		sb.append(" order by matriculaperiodo.codigo desc, periodoletivo.periodoletivo desc limit 1 ");
		sb.append(" ) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PeriodoLetivoVO obj = new PeriodoLetivoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigoPeriodoLetivo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo"));
		}
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarTotalCargaHorariaPorGradeCurricularPeriodoLetivo(Integer periodoLetivo, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select totalCargaHoraria from periodoletivo where gradecurricular = ").append(gradeCurricular);
		sb.append(" and periodoletivo = ").append(periodoLetivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalCargaHoraria");
		}
		return 0;
	}
	
	@Override
	public List<PeriodoLetivoVO> consultarPorCursoGradeCurricular(Integer codigoCurso, Integer gradecurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select periodoLetivo.* from periodoLetivo ");
		sqlStr.append("inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
		sqlStr.append("where gradecurricular.curso = ").append(codigoCurso);
		sqlStr.append(" and gradecurricular.codigo = ").append(gradecurricular);
		sqlStr.append(" order by periodoLetivo.periodoLetivo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public Integer consultarMaiorNumeroPeriodoLetivoPorCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select max(periodoLetivo.periodoLetivo) as periodoLetivo from periodoLetivo ");
		sqlStr.append("inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
		sqlStr.append("where gradecurricular.curso = ").append(codigoCurso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "periodoLetivo", TipoCampoEnum.INTEIRO);
	}
	
	/**
	 * Responsável por executar a consulta de período letivo com base na
	 * matrícula, levando em consideração a última matrícula período.
	 * 
	 * @author Wellington Rodrigues - 4 de ago de 2015
	 * @param matricula
	 * @param usuarioVO
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PeriodoLetivoVO consultarPeriodoLetivoAtualPorMatricula(String matricula, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select periodoletivo.codigo, periodoletivo.descricao, periodoletivo.periodoletivo from matricula ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append("inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sqlStr.append("where matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append("and matriculaperiodo.codigo = (");
		sqlStr.append("select mp.codigo from matriculaperiodo mp ");
		sqlStr.append("where mp.matricula = matricula.matricula ");
		sqlStr.append("order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		PeriodoLetivoVO obj = new PeriodoLetivoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo"));
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarPeriodoLetivoPorGradeCurricularDisciplina(Integer gradeCurricular, Integer disciplina, Integer mapaEquivalenciaDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct periodoletivo.periodoletivo from periodoletivo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" where gradecurricular = ").append(gradeCurricular);
		if (!mapaEquivalenciaDisciplina.equals(0)) {
			sb.append(" and disciplina in ( ");
			sb.append("(select distinct mapaequivalenciadisciplinamatrizcurricular.disciplina from mapaequivalenciadisciplinamatrizcurricular ");
			sb.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.codigo = mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina ");
			sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo ");
			sb.append(" where mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = ").append(mapaEquivalenciaDisciplina);
			sb.append(" and mapaequivalenciadisciplinacursada.disciplina = ").append(disciplina);
			sb.append(")) ");
		} else {
			sb.append(" and disciplina = ").append(disciplina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("periodoLetivo");
		}
		return 0;
	}
	
	@Override
	public PeriodoLetivoVO consultarPeriodoLetivoPorGradeCurricularGrupoOptativaDisciplina(Integer gradeCurricularGrupoOptativaDisciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct periodoletivo.* from periodoletivo ");
		sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa ");
		sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sb.append(" where gradecurriculargrupooptativadisciplina.codigo = ").append(gradeCurricularGrupoOptativaDisciplina);
		sb.append(" order by periodoletivo.periodoletivo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return new PeriodoLetivoVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCargaHorariaENrCredito(final Integer periodoLetivo, final Integer totalCargaHoraria, Integer totalCreditos, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE PeriodoLetivo set totalCargaHoraria=?, totalCreditos=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setInt(++i, totalCargaHoraria);
					sqlAlterar.setInt(++i, totalCreditos);
					sqlAlterar.setInt(++i, periodoLetivo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	public PeriodoLetivoVO consultarPeriodoLetivoIngressoPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder().append(" SELECT periodoletivo.* FROM periodoletivo ")
				.append(" INNER JOIN matriculaperiodo ON periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ")
				.append(" INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula ")
				.append(" WHERE matricula.matricula = ? ")
				.append(" ORDER BY (matriculaperiodo.ano || '/' || matriculaperiodo.semestre), periodoletivo.periodoletivo, matriculaperiodo.codigo LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), matricula);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return null;
	}
	
	@Override
	public PeriodoLetivoVO realizarDefinicaoPeriodoLetivoCursarBaseadoDisciplinasAprovadasMatriculasAntigas(PessoaVO aluno, Integer gradeCurricular ,
            MatriculaPeriodoVO matriculaPeriodoVO,ConfiguracaoFinanceiroVO configFinanceiroVO, UsuarioVO usuario) throws Exception {
				
				PeriodoLetivoVO periodoLetivoAux = null;				
				// historicos aprovados do aluno.
				List<HistoricoVO> historicosMapa  = getFacadeFactory().getMatriculaFacade().realizarCriacaoHistoricoApartirDisciplinasAprovadasMesmaGradeCurricularMatriculaOnline("",aluno.getCodigo() ,gradeCurricular,  matriculaPeriodoVO.getTurma()  ,configFinanceiroVO  , NivelMontarDados.TODOS ,usuario);
				// periodo letivos da grade.
				List<PeriodoLetivoVO> periodoLetivosVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
				
				for(PeriodoLetivoVO  periodoLetivo : periodoLetivosVOs){	
					// pegando proximo periodo letivo para iteração 
					// caso nao encaixe na regra do prox for 
					// sera retornado o ultimo perido atribuido 
					periodoLetivoAux = periodoLetivo;			
					for(GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
						Boolean disciplinaAprovada = historicosMapa.stream()
								                    .anyMatch(h -> h.getDisciplina().getCodigo()
								                    .equals(gradeDisciplina.getDisciplina().getCodigo())
						                             && (SituacaoHistorico.getEnum(h.getSituacao()).isAprovadaAproveitamento() 
						                            		 || SituacaoHistorico.getEnum(h.getSituacao()).isAprovada())) 
								                   						 
 							  ||   historicosMapa.stream()
 							        .filter(HistoricoVO::getHistoricoEquivalente) // Filtra apenas os históricos equivalentes
 							        .map(HistoricoVO::getMapaEquivalenciaDisciplina) // Obtém o objeto MapaEquivalenciaDisciplina
 							        .filter(Objects::nonNull) // Garante que não seja nulo
 							        .map(MapaEquivalenciaDisciplinaVO::getMapaEquivalenciaDisciplinaMatrizCurricularVOs) // Obtém a lista
 							        .filter(Objects::nonNull) // Garante que a lista não seja nula
 							        .flatMap(List::stream) // Achata a lista
 							        .map(MapaEquivalenciaDisciplinaMatrizCurricularVO::getDisciplinaVO) // Obtém o objeto Disciplina
 							        .filter(Objects::nonNull) // Garante que não seja nulo
 							        .anyMatch(disciplina -> gradeDisciplina.getDisciplina().getCodigo().equals(disciplina.getCodigo())); // Verifica se algum código bate
 								
						if(!disciplinaAprovada &&  (periodoLetivo.getPrimeiro() || verificarOfertaDisciplinaParaDisciplinasDoPeriodoLetivo(periodoLetivo.getGradeDisciplinaVOs(), matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre()))){
					       return periodoLetivo;
					    }else if(!disciplinaAprovada ) {
					    	break;
					    }			  
					}				
				}
	return periodoLetivoAux;		
	}
	
	
	private boolean verificarOfertaDisciplinaParaDisciplinasDoPeriodoLetivo(List<GradeDisciplinaVO> gradeDisciplinaVOs, String ano, String semestre) throws Exception {
	   for(GradeDisciplinaVO grade : gradeDisciplinaVOs) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(grade.getDisciplina().getCodigo(),ano, semestre);
			if(Uteis.isAtributoPreenchido(ofertaDisciplinaVO)) {
				return true ;
			}				
		}
		return false;
	}

	@Override
	public PeriodoLetivoRSVO consultarPeriodoLetivoMatriculaOnline(PessoaVO aluno, Integer gradeCurricular, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) {
		PeriodoLetivoRSVO  periodoLetivoCursar =  new PeriodoLetivoRSVO();
		try {
			PeriodoLetivoVO periodoLetivo = getFacadeFactory().getPeriodoLetivoFacade()
					.realizarDefinicaoPeriodoLetivoCursarBaseadoDisciplinasAprovadasMatriculasAntigas(aluno,
							gradeCurricular, matriculaPeriodoVO, configuracaoFinanceiroVO, usuarioVO);
			
			periodoLetivoCursar.setCodigo(periodoLetivo.getCodigo());
			periodoLetivoCursar.setNome(periodoLetivo.getDescricao());
			periodoLetivoCursar.setPeriodoLetivo(periodoLetivo.getPeriodoLetivo());
		} catch (Exception e) {			
			e.printStackTrace();
		}		
		return periodoLetivoCursar;
		
	}
}
