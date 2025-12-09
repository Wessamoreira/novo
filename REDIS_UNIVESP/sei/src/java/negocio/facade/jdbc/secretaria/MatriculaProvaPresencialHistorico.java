/**
 * 
 */
package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialHistoricoVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.MatriculaProvaPresencialHistoricoInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaProvaPresencialHistorico extends ControleAcesso implements MatriculaProvaPresencialHistoricoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaProvaPresencialHistorico() {
		super();
	}

	public void validarDados(MatriculaProvaPresencialHistoricoVO obj) throws Exception {
		if (obj.getHistoricoVO().getCodigo().equals(0)) {
			throw new Exception("");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MatriculaProvaPresencialHistorico( matriculaProvaPresencialDisciplina, historico, notaAtualizada, nota, realizarCalculoMediaLancamentoNota, disciplina, configuracaoAcademico, ano, semestre ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getMatriculaProvaPresencialDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getMatriculaProvaPresencialDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getHistoricoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getHistoricoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getNotaAtualizada());
					if (obj.getNota() != null) {
						sqlInserir.setDouble(4, obj.getNota());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setBoolean(5, obj.getRealizarCalculoMediaLancamentoNota());
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getAno());
					sqlInserir.setString(9, obj.getSemestre());
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
	public void alterar(final MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE MatriculaProvaPresencialHistorico set matriculaProvaPresencialDisciplina=?, historico=?, notaAtualizada=?, nota=?, realizarCalculoMediaLancamentoNota=?, disciplina=?, configuracaoAcademico=?, ano=?, semestre=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getMatriculaProvaPresencialDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getMatriculaProvaPresencialDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getHistoricoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getHistoricoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getNotaAtualizada());
					sqlAlterar.setDouble(4, obj.getNota());
					sqlAlterar.setBoolean(5, obj.getRealizarCalculoMediaLancamentoNota());
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getAno());
					sqlAlterar.setString(9, obj.getSemestre());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialHistorico WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaProvaPresencialDisciplina(MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialHistorico WHERE (MatriculaProvaPresencialDisciplina = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public void excluirPorListaMatriculaProvaPresencialDisciplina(List<MatriculaProvaPresencialVO> listaMatriculaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : listaMatriculaProvaPresencialVOs) {
			for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
				if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {
					excluirPorMatriculaProvaPresencialDisciplina(matriculaProvaPresencialDisciplinaVO, usuarioVO);
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaProvaPresencialHistorico(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, Integer configuracaoAcademico, String variavelNota, Boolean realizarCalculoMediaLancamentoNota, String ano, String semestre, UsuarioVO usuario) throws Exception {
		for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : objetos) {

			if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {
				MatriculaProvaPresencialHistoricoVO obj = inicializarDadosMatriculaProvaPresencialHistorico(matriculaProvaPresencialDisciplinaVO, configuracaoAcademico, variavelNota, realizarCalculoMediaLancamentoNota, ano, semestre, usuario);
				incluir(obj, usuario);
			}
		}
	}

	public MatriculaProvaPresencialHistoricoVO inicializarDadosMatriculaProvaPresencialHistorico(MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO, Integer configuracaoAcademico, String variavelNota, Boolean realizarCalculoMediaLancamentoNota, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		MatriculaProvaPresencialHistoricoVO obj = new MatriculaProvaPresencialHistoricoVO();

		if (matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo().equals(0)) {
			throw new Exception("Não foi encontrado o Histórico referente para a disciplina ");
		}
		matriculaProvaPresencialDisciplinaVO.setHistoricoVO(getFacadeFactory().getHistoricoFacade().consultaRapidaPorChavePrimariaDadosCompletosSemExcessao(matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo(), usuarioVO));
		obj.getHistoricoVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo());

		String notaUtilizar = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(configuracaoAcademico, variavelNota, usuarioVO);
		Double nota = verificarNotaHistoricoDeAcordoComTituloNota(matriculaProvaPresencialDisciplinaVO.getHistoricoVO(), notaUtilizar);

		obj.getMatriculaProvaPresencialDisciplinaVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getCodigo());

		obj.setNotaAtualizada(notaUtilizar);
		obj.setNota(nota);
		obj.setRealizarCalculoMediaLancamentoNota(realizarCalculoMediaLancamentoNota);
		obj.getDisciplinaVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getCodigo());
		obj.getConfiguracaoAcademicoVO().setCodigo(configuracaoAcademico);
		obj.setAno(ano);
		obj.setSemestre(semestre);
		return obj;
	}
	
	public MatriculaProvaPresencialHistoricoVO inicializarDadosMatriculaProvaPresencialHistoricoPorHistorico(MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO, HistoricoVO historicoVO, Integer configuracaoAcademico, String variavelNota, Boolean realizarCalculoMediaLancamentoNota, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		MatriculaProvaPresencialHistoricoVO obj = new MatriculaProvaPresencialHistoricoVO();

		if (matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo().equals(0)) {
			throw new Exception("Não foi encontrado o Histórico referente para a disciplina ");
		}
		matriculaProvaPresencialDisciplinaVO.setHistoricoVO(historicoVO);
		obj.getHistoricoVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo());

		String notaUtilizar = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(configuracaoAcademico, variavelNota, usuarioVO);
		Double nota = verificarNotaHistoricoDeAcordoComTituloNota(matriculaProvaPresencialDisciplinaVO.getHistoricoVO(), notaUtilizar);

		obj.getMatriculaProvaPresencialDisciplinaVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getCodigo());

		obj.setNotaAtualizada(notaUtilizar);
		obj.setNota(nota);
		obj.setRealizarCalculoMediaLancamentoNota(realizarCalculoMediaLancamentoNota);
		obj.getDisciplinaVO().setCodigo(matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getCodigo());
		obj.getConfiguracaoAcademicoVO().setCodigo(configuracaoAcademico);
		obj.setAno(ano);
		obj.setSemestre(semestre);
		return obj;
	}

	public Double verificarNotaHistoricoDeAcordoComTituloNota(HistoricoVO historicoVO, String tituloNota) {
		if (tituloNota.equals("nota1")) {
			return historicoVO.getNota1();
		}
		if (tituloNota.equals("nota2")) {
			return historicoVO.getNota2();
		}
		if (tituloNota.equals("nota3")) {
			return historicoVO.getNota3();
		}
		if (tituloNota.equals("nota4")) {
			return historicoVO.getNota4();
		}
		if (tituloNota.equals("nota5")) {
			return historicoVO.getNota5();
		}
		if (tituloNota.equals("nota6")) {
			return historicoVO.getNota6();
		}
		if (tituloNota.equals("nota7")) {
			return historicoVO.getNota7();
		}
		if (tituloNota.equals("nota8")) {
			return historicoVO.getNota8();
		}
		if (tituloNota.equals("nota9")) {
			return historicoVO.getNota9();
		}
		if (tituloNota.equals("nota10")) {
			return historicoVO.getNota10();
		}
		if (tituloNota.equals("nota11")) {
			return historicoVO.getNota11();
		}
		if (tituloNota.equals("nota12")) {
			return historicoVO.getNota12();
		}
		if (tituloNota.equals("nota13")) {
			return historicoVO.getNota13();
		}
		if (tituloNota.equals("nota14")) {
			return historicoVO.getNota14();
		}
		if (tituloNota.equals("nota15")) {
			return historicoVO.getNota15();
		}
		if (tituloNota.equals("nota16")) {
			return historicoVO.getNota16();
		}
		if (tituloNota.equals("nota17")) {
			return historicoVO.getNota17();
		}
		if (tituloNota.equals("nota18")) {
			return historicoVO.getNota18();
		}
		if (tituloNota.equals("nota19")) {
			return historicoVO.getNota19();
		}
		if (tituloNota.equals("nota20")) {
			return historicoVO.getNota20();
		}
		if (tituloNota.equals("nota21")) {
			return historicoVO.getNota21();
		}
		if (tituloNota.equals("nota22")) {
			return historicoVO.getNota22();
		}
		if (tituloNota.equals("nota23")) {
			return historicoVO.getNota23();
		}
		if (tituloNota.equals("nota24")) {
			return historicoVO.getNota24();
		}
		if (tituloNota.equals("nota25")) {
			return historicoVO.getNota25();
		}
		if (tituloNota.equals("nota26")) {
			return historicoVO.getNota26();
		}
		if (tituloNota.equals("nota27")) {
			return historicoVO.getNota27();
		}
		if (tituloNota.equals("nota28")) {
			return historicoVO.getNota28();
		}
		if (tituloNota.equals("nota29")) {
			return historicoVO.getNota29();
		}
		if (tituloNota.equals("nota30")) {
			return historicoVO.getNota30();
		}
		return null;
	}

	/**
	 * Método responsável por recuperar a nota original do histórico caso seja
	 * excluído um processamento de arquivo.
	 * 
	 * @author Carlos Eugênio - 25/06/2015
	 * @param obj
	 * @param usuario
	 * @throws Exception
	 */
	public void executarAtualizacaoHistoricoNotaAtualizadaParaHistoricoOriginal(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : obj.getMatriculaProvaPresencialVOs()) {
			for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
				if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {

					MatriculaProvaPresencialHistoricoVO matriculaProvaPresencialHistoricoVO = consultarPorMatriculaProvaPresencialDisciplina(matriculaProvaPresencialDisciplinaVO.getCodigo(), usuario);
					if (!matriculaProvaPresencialHistoricoVO.getHistoricoVO().getCodigo().equals(0)) {
						HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorChavePrimariaDadosCompletosSemExcessao(matriculaProvaPresencialHistoricoVO.getHistoricoVO().getCodigo(), usuario);
						if (historicoVO != null && !historicoVO.getCodigo().equals(0)) {
							String notaAtualizar = matriculaProvaPresencialHistoricoVO.getNotaAtualizada();
							getFacadeFactory().getMatriculaProvaPresencialFacade().adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, notaAtualizar, matriculaProvaPresencialHistoricoVO.getNota());
							if (matriculaProvaPresencialHistoricoVO.getRealizarCalculoMediaLancamentoNota()) {
								matriculaProvaPresencialHistoricoVO.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matriculaProvaPresencialHistoricoVO.getConfiguracaoAcademicoVO().getCodigo(), usuario));
								if(historicoVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
									historicoVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(historicoVO.getMatricula().getMatricula(), 0, historicoVO.getMatriculaPeriodo().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getGradeDisciplinaVO().getCodigo(), historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, null));
									Map<Integer, ConfiguracaoAcademicoVO> mapConf =  new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
									if(!mapConf.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())){
										mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
									}
									mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
									for (HistoricoVO historicoVO2 : historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs()) {
										if(!historicoVO2.getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
											if (!mapConf.containsKey(historicoVO2.getConfiguracaoAcademico().getCodigo())) {			
												mapConf.put(historicoVO2.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO2.getConfiguracaoAcademico().getCodigo(), usuario));
											}
											historicoVO2.setConfiguracaoAcademico(mapConf.get(historicoVO2.getConfiguracaoAcademico().getCodigo()));
										}
									}										
								}
								getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), matriculaProvaPresencialHistoricoVO.getConfiguracaoAcademicoVO(), null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, usuario);
								if (historicoVO.getHistoricoDisciplinaFazParteComposicao()) {
									getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(historicoVO, null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, false, usuario);
								}else if(historicoVO.getHistoricoDisciplinaComposta()) { 
									getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(historicoVO,  matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), false, usuario);
								}
								getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuario);
							} else {
								if (historicoVO.getHistoricoDisciplinaFazParteComposicao()) {
									getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(historicoVO, null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, false, usuario);
								}
								getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuario);
							}
						}
					}
				}
			}
		}
	}

	public MatriculaProvaPresencialHistoricoVO consultarPorMatriculaProvaPresencialDisciplina(Integer matriculaProvaPresencialDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencialhistorico.codigo, matriculaprovapresencialhistorico.matriculaprovapresencialdisciplina, matriculaprovapresencialhistorico.historico, ");
		sb.append(" matriculaprovapresencialhistorico.notaatualizada, matriculaprovapresencialhistorico.nota, matriculaprovapresencialhistorico.realizarCalculoMediaLancamentoNota, ");
		sb.append(" matriculaprovapresencialhistorico.disciplina, matriculaprovapresencialhistorico.configuracaoacademico, matriculaprovapresencialhistorico.ano, matriculaprovapresencialhistorico.semestre ");
		sb.append(" from matriculaprovapresencialhistorico where matriculaprovapresencialdisciplina = ").append(matriculaProvaPresencialDisciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		MatriculaProvaPresencialHistoricoVO obj = new MatriculaProvaPresencialHistoricoVO();
		if (tabelaResultado.next()) {
			montarDados(obj, tabelaResultado, usuarioVO);
		}
		return obj;
	}

	public void montarDados(MatriculaProvaPresencialHistoricoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatriculaProvaPresencialDisciplinaVO().setCodigo(dadosSQL.getInt("matriculaprovapresencialdisciplina"));
		obj.getHistoricoVO().setCodigo(dadosSQL.getInt("historico"));
		obj.setNotaAtualizada(dadosSQL.getString("notaAtualizada"));
		if (dadosSQL.getObject("nota") == null) {
			obj.setNota((Double) dadosSQL.getObject("nota"));
		} else {
			obj.setNota(dadosSQL.getDouble("nota"));
		}
		obj.setRealizarCalculoMediaLancamentoNota(dadosSQL.getBoolean("realizarCalculoMediaLancamentoNota"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
		obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));

	}

	public static String getIdEntidade() {
		return MatriculaProvaPresencialHistorico.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MatriculaProvaPresencialHistorico.idEntidade = idEntidade;
	}

}
