package negocio.facade.jdbc.secretaria;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoConvocadosEnadeEnum;
import negocio.comuns.secretaria.enumeradores.SituacaoMapaConvocacaoEnadeEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.MapaConvocacaoEnadeInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Repository
@Scope("singleton")
public class MapaConvocacaoEnade extends ControleAcesso implements MapaConvocacaoEnadeInterfaceFacade {

	protected static String idEntidade;
	private static final long serialVersionUID = 1L;
	private PrintWriter pwAluno;
	private PrintWriter pwCurso;
	private ArquivoHelper arquivoHelper = new ArquivoHelper();
	public MapaConvocacaoEnade() {
		super();
		setIdEntidade("MapaConvocacaoEnade");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MapaConvocacaoEnadeVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDados(MapaConvocacaoEnadeVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getEnadeCursoVO().getCursoVO().getCodigo().equals(0)) {
			throw new Exception("O campo (CURSO) deve ser informado!");
		}
		if (obj.getEnadeCursoVO().getEnadeVO().getCodigo().equals(0)) {
			throw new Exception("O campo (ENADE) deve ser informado!");
		}
		if (obj.getResponsavel().getCodigo().equals(0)) {
			throw new Exception("O campo (RESPONSÁVEL) deve ser informado!");
		}
		if (obj.getDataAbertura() == null) {
			throw new Exception("O campo (DATA ABERTURA) deve ser informado!");
		}
		List<MapaConvocacaoEnadeVO> listaMapaConvocacaoPorEnadeCurso = consultaRapidaPorEnadeCurso(obj.getEnadeCursoVO().getEnadeVO().getCodigo(), obj.getEnadeCursoVO().getCodigo(), obj.getCodigo(), false, true, usuario);
		if (Uteis.isAtributoPreenchido(listaMapaConvocacaoPorEnadeCurso) && obj.getNovoObj()) {
			throw new Exception("Já existe um mapa convocação enade para esse enade e curso. ");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaConvocacaoEnadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			final String sql = "INSERT INTO MapaConvocacaoEnade( unidadeEnsino, enadeCurso, responsavel, dataAbertura, dataFechamento, dataPrevisaoConclusao, situacaoMapaConvocacaoEnade ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getUnidadeEnsino());
					if (obj.getEnadeCursoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getEnadeCursoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getDataAbertura() != null) {
						sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataAbertura()));
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getDataFechamento() != null) {
						sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataFechamento()));
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getDataPrevisaoConclusao() != null) {
						sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataPrevisaoConclusao()));
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getSituacaoMapaConvocacaoEnade().name().toString());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().incluirMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(), SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE, usuario);
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().incluirMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs(), SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO, usuario);
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().incluirMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(), SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE, usuario);
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MapaConvocacaoEnadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			final String sql = "UPDATE MapaConvocacaoEnade set unidadeEnsino=?, enadeCurso=?, responsavel=?, dataAbertura=?, dataFechamento=?, dataPrevisaoConclusao=?, situacaoMapaConvocacaoEnade=? WHERE (codigo=?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
						sqlAlterar.setString(1, obj.getUnidadeEnsino());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getEnadeCursoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getEnadeCursoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getDataAbertura() != null) {
						sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataAbertura()));
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getDataFechamento() != null) {
						sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataFechamento()));
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getDataPrevisaoConclusao() != null) {
						sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataPrevisaoConclusao()));
					
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getSituacaoMapaConvocacaoEnade().name().toString());
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().excluirPorMapaConvocacaoEnade(obj.getCodigo(), usuario);
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().alterarMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(), SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE, usuario);
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().alterarMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs(), SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO, usuario);
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().alterarMapaConvocacaoEnadeMatriculaVOs(obj, obj.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(), SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	

	
	
	@Override
	public List<MapaConvocacaoEnadeVO> consultar(String campoConsulta, String valorConsulta, Date dataInicial, Date dataFinal, Integer unidadeEnsino, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados) throws Exception {
		List<MapaConvocacaoEnadeVO> objs = new ArrayList<MapaConvocacaoEnadeVO>(0);
		if (campoConsulta.equals("nome")) {
			objs = consultaRapidaPorNome(valorConsulta, unidadeEnsino, true, usuarioVO);
		}
		if (campoConsulta.equals("matricula")) {
			objs = consultaRapidaPorMatricula(valorConsulta, unidadeEnsino, true, usuarioVO);
		}
		if (campoConsulta.equals("curso")) {
			objs = consultaRapidaPorCurso(valorConsulta, unidadeEnsino, true, usuarioVO);
		}
		if (campoConsulta.equals("enade")) {
			objs = consultaRapidaPorEnade(valorConsulta, unidadeEnsino, true, usuarioVO, nivelMontarDados);
		}
//		if (campoConsulta.equals("unidadeEnsino")) {
//			objs = consultaRapidaPorUnidadeEnsino(valorConsulta, unidadeEnsino, true, usuarioVO);
//		}
		if (campoConsulta.equals("dataAbertura")) {
			objs = consultaRapidaPorDataAbertura(dataInicial, dataFinal, unidadeEnsino, true, usuarioVO);
		}
		if (campoConsulta.equals("dataFechamento")) {
			objs = consultaRapidaPorDataFechamento(dataInicial, dataFinal, unidadeEnsino, true, usuarioVO);
		}
		return objs;
	}

//	public List<MapaConvocacaoEnadeVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
//		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
//		sqlStr.append("WHERE sem_acentos(lower(unidadeEnsino)) like(sem_acentos('");
//		sqlStr.append(valorConsulta.toLowerCase());
//		sqlStr.append("%'))");
//		sqlStr.append(" ORDER BY unidadeEnsino");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		return montarDadosConsultaRapida(tabelaResultado);
//	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(curso.nome)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorEnade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append("WHERE sem_acentos(enade.tituloEnade) ilike(sem_acentos(?)) ");
		sqlStr.append(" ORDER BY enade.tituloEnade");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		List<MapaConvocacaoEnadeVO> vetResultado = new ArrayList<MapaConvocacaoEnadeVO>(0);
		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			while (tabelaResultado.next()) {
				MapaConvocacaoEnadeVO obj = new MapaConvocacaoEnadeVO();
				montarDadosCompleto(obj, tabelaResultado, usuarioVO, false);
				vetResultado.add(obj);
			}
			return vetResultado;	
		} else {
			return montarDadosConsultaRapida(tabelaResultado);
		}
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY mapaconvocacaoenade.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(matricula.matricula)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY mapaconvocacaoenade.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorDataFechamento(Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append(" AND (mapaconvocacaoenade.dataFechamento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		sqlStr.append(" ORDER BY mapaconvocacaoenade.dataFechamento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorDataAbertura(Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append(" AND (mapaconvocacaoenade.dataAbertura BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		sqlStr.append(" ORDER BY mapaconvocacaoenade.dataAbertura");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<MapaConvocacaoEnadeVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<MapaConvocacaoEnadeVO> vetResultado = new ArrayList<MapaConvocacaoEnadeVO>(0);
		while (tabelaResultado.next()) {
			MapaConvocacaoEnadeVO obj = new MapaConvocacaoEnadeVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}

	private void montarDadosBasico(MapaConvocacaoEnadeVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.getEnadeCursoVO().setCodigo(dadosSQL.getInt("enadeCurso.codigo"));
		obj.getEnadeCursoVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getEnadeCursoVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getEnadeCursoVO().getEnadeVO().setCodigo(dadosSQL.getInt("enade.codigo"));
		obj.getEnadeCursoVO().getEnadeVO().setTituloEnade(dadosSQL.getString("enade.tituloEnade"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.setDataPrevisaoConclusao(dadosSQL.getDate("dataPrevisaoConclusao"));
		obj.setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum.valueOf(dadosSQL.getString("situacaoMapaConvocacaoEnade")));

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MapaConvocacaoEnadeVO obj, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception {
		MapaConvocacaoEnade.excluir(getIdEntidade());
		getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().excluirPorMapaConvocacaoEnade(obj.getCodigo(), usuario);
		String sql = "DELETE FROM MapaConvocacaoEnade WHERE (codigo=?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		getFacadeFactory().getMapaConvocacaoEnadeFacade().excluirArquivoTxt(obj, mapaConvocacaoEnadeMatriculaVO, usuario);
	}

	public void carregarDados(MapaConvocacaoEnadeVO obj, UsuarioVO usuario, Boolean arquivoTXT) throws Exception {
		carregarDados((MapaConvocacaoEnadeVO) obj, NivelMontarDados.TODOS, usuario, false);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	public void carregarDados(MapaConvocacaoEnadeVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario, Boolean arquivoTXT) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((MapaConvocacaoEnadeVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((MapaConvocacaoEnadeVO) obj, resultado, usuario, arquivoTXT);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codMapaConvocacaEnade, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (mapaconvocacaoenade.codigo = ").append(codMapaConvocacaEnade).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codMapaConvocacaEnade, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (mapaconvocacaoenade.codigo = ").append(codMapaConvocacaEnade).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder str = new StringBuilder();
		str.append(" select distinct mapaconvocacaoenade.codigo, mapaconvocacaoenade.situacaoMapaConvocacaoEnade, mapaconvocacaoenade.unidadeensino, ");
		str.append(" enadeCurso.codigo AS \"enadeCurso.codigo\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		str.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", ");
		str.append(" dataabertura, datafechamento, dataprevisaoconclusao ");
		str.append(" from mapaconvocacaoenade  ");
		str.append(" inner join mapaconvocacaoenadematricula on mapaconvocacaoenadematricula.mapaConvocacaoEnade = mapaConvocacaoEnade.codigo ");
		str.append(" inner join matricula on matricula.matricula = mapaconvocacaoenadematricula.matricula ");
		str.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		str.append(" inner join enadeCurso on enadeCurso.codigo = mapaconvocacaoenade.enadeCurso ");
		str.append(" inner join enade on enade.codigo = enadeCurso.enade ");
		str.append(" inner join curso on curso.codigo = enadeCurso.curso ");
		str.append(" inner join usuario on usuario.codigo = mapaconvocacaoenade.responsavel ");
		return str;
	}

	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder str = new StringBuilder();
		str.append(" select distinct mapaconvocacaoenade.codigo, mapaconvocacaoenade.situacaoMapaConvocacaoEnade, mapaconvocacaoenade.unidadeensino, ");
		str.append(" enadeCurso.codigo AS \"enadeCurso.codigo\", enadeCurso.PercentualCargaHorariaConcluinte AS \"enadeCurso.PercentualCargaHorariaConcluinte\", enadeCurso.PercentualCargaHorariaIngressante AS \"enadeCurso.PercentualCargaHorariaIngressante\", enadeCurso.anobaseingressante AS \"enadeCurso.anobaseingressante\", enadeCurso.periodoPrevistoTerminoIngressante AS \"enadeCurso.periodoPrevistoTerminoIngressante\", enadeCurso.periodoPrevistoTerminoConcluinte AS \"enadeCurso.periodoPrevistoTerminoConcluinte\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		str.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", ");
		str.append(" dataabertura, datafechamento, dataprevisaoconclusao, dataprova, enade.dataPortaria AS \"enade.dataPortaria\", curso.idcursoinep AS \"curso.idcursoinep\", mapaconvocacaoenade.arquivoalunoingressante AS \"mapaconvocacaoenade.arquivoalunoingressante\", mapaconvocacaoenade.arquivoalunoconcluinte AS \"mapaconvocacaoenade.arquivoalunoconcluinte\", arquivoconcluinte.nome AS \"arquivoconcluinte\", arquivoingressante.nome AS \"arquivoingressante\", enade.codigoprojeto ");
		str.append(" from mapaconvocacaoenade  ");
		str.append(" inner join mapaconvocacaoenadematricula on mapaconvocacaoenadematricula.mapaConvocacaoEnade = mapaConvocacaoEnade.codigo ");
		str.append(" inner join matricula on matricula.matricula = mapaconvocacaoenadematricula.matricula ");
		str.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		str.append(" inner join enadeCurso on enadeCurso.codigo = mapaconvocacaoenade.enadeCurso ");
		str.append(" inner join curso on curso.codigo = enadeCurso.curso ");
		str.append(" inner join enade on enade.codigo = enadeCurso.enade ");
		str.append(" inner join usuario on usuario.codigo = mapaconvocacaoenade.responsavel ");
		str.append(" LEFT join arquivo arquivoconcluinte on arquivoconcluinte.codigo = mapaconvocacaoenade.arquivoalunoconcluinte ");
		str.append(" LEFT join arquivo arquivoingressante on arquivoingressante.codigo = mapaconvocacaoenade.arquivoalunoingressante ");
		return str;
	}

	private void montarDadosBasico(MapaConvocacaoEnadeVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {

	}

	private void montarDadosCompleto(MapaConvocacaoEnadeVO obj, SqlRowSet dadosSQL, UsuarioVO usuario, Boolean arquivoTXT) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.getEnadeCursoVO().setCodigo(dadosSQL.getInt("enadeCurso.codigo"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaIngressante(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaIngressante"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaConcluinte(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaConcluinte"));
		obj.getEnadeCursoVO().setAnoBaseIngressante(dadosSQL.getString("enadeCurso.anobaseingressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoIngressante(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoIngressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoConcluinte(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoConcluinte"));
		obj.getEnadeCursoVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getEnadeCursoVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getEnadeCursoVO().getEnadeVO().setCodigo(dadosSQL.getInt("enade.codigo"));
		obj.getEnadeCursoVO().getEnadeVO().setTituloEnade(dadosSQL.getString("enade.tituloEnade"));
		obj.getEnadeCursoVO().getEnadeVO().setDataPortaria(dadosSQL.getDate("enade.dataPortaria"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.setDataPrevisaoConclusao(dadosSQL.getDate("dataPrevisaoConclusao"));
		obj.getEnadeCursoVO().getEnadeVO().setDataProva(dadosSQL.getDate("dataprova"));
		obj.getEnadeCursoVO().getCursoVO().setIdCursoInep(dadosSQL.getInt("curso.idcursoinep"));
		obj.setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum.valueOf(dadosSQL.getString("situacaoMapaConvocacaoEnade")));
		obj.getArquivoAlunoIngressante().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoingressante"));
		if(Uteis.isAtributoPreenchido(obj.getArquivoAlunoIngressante().getCodigo())){
			obj.getArquivoAlunoIngressante().setNome(dadosSQL.getString("arquivoingressante"));
		}
		obj.getArquivoAlunoConcluinte().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoconcluinte"));
		if(Uteis.isAtributoPreenchido(obj.getArquivoAlunoConcluinte().getCodigo())){
			obj.getArquivoAlunoConcluinte().setNome(dadosSQL.getString("arquivoconcluinte"));
		}
		obj.getEnadeCursoVO().getEnadeVO().setCodigoProjeto(dadosSQL.getString("codigoprojeto"));
		if(!arquivoTXT) {
			getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().carregarDados(obj, usuario, UteisData.getAnoDataString(obj.getEnadeCursoVO().getEnadeVO().getDataProva()));
		}
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDadosConsulta(Integer curso) throws Exception {
		if (curso.equals(0)) {
			throw new Exception("O campo (ENADE) deve ser informado para realização da consulta.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaVO> consultarAlunosMapaConvocacaoEnade(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		validarDadosConsulta(mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getCodigo());
		List<MatriculaVO> listaMatriculaVOs =new ArrayList<MatriculaVO>(0);
		if(mapaConvocacaoEnadeVO.getNovoObj() || (mapaConvocacaoEnadeVO.getSituacaoMapaConvocacaoEnade().equals(SituacaoMapaConvocacaoEnadeEnum.MAPA_EM_CONSTRUCAO) && !mapaConvocacaoEnadeVO.getImprimirPDF())) {
			listaMatriculaVOs = consultarAlunosPorUnidadeEnsinoCurso(unidadeEnsinoVOs, mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getCodigo(), LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(mapaConvocacaoEnadeVO.getEnadeCursoVO().getEnadeVO().getDataProva())).getYear(), usuarioVO);
		} else {
			listaMatriculaVOs = consultarAlunosMapaConvocacaoEnadeMatricula(unidadeEnsinoVOs, mapaConvocacaoEnadeVO,  mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getCodigo(), usuarioVO);
		}
		return listaMatriculaVOs;
	}

	/**
	 * Método responsável por definir se os alunos são INGRESSANTES, DISPENSADOS
	 * OU CONCLUINTES, de acordo com o percentual de integralização já cursado
	 * pelo aluno.
	 * 
	 * @param listaMatriculaVOs
	 * @param mapaConvocacaoEnadeVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @throws Exception
	 * @author CarlosEugenio
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao(List<MatriculaVO> listaMatriculaVOs, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception {
		mapaConvocacaoEnadeVO.setMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0));
		mapaConvocacaoEnadeVO.setMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs(new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0));
		mapaConvocacaoEnadeVO.setMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0));
		if (Uteis.isAtributoPreenchido(listaMatriculaVOs)) {
			final ConsistirException ex = new ConsistirException();
			for (MatriculaVO matriculaVO : listaMatriculaVOs) {
				try {
					progressBar.setProgresso(progressBar.getProgresso() + 1);
					progressBar.setStatus("Processando Aluno: ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " )");
					matriculaVO.getGradeCurricularAtual().setTotalCargaHoraria(getFacadeFactory().getGradeCurricularFacade().consultarTotalCargaHorariaGradeCurricularPorCodigo(matriculaVO.getGradeCurricularAtual().getCodigo(), usuarioVO));
					inicializarDadosAnoIngressoSemestreIngresso(matriculaVO, usuarioVO);
					MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO = inicializarDadosListaMapaConvocacaoEnadeMatricula(matriculaVO, mapaConvocacaoEnadeVO, usuarioVO);
					String anoParametroHistoricosCargaHorariaCursada = UteisData.getAnoDataString(mapaConvocacaoEnadeVO.getDataAbertura());
					if (realizarVerificacaoAlunoIngressante(matriculaVO, matriculaVO.getGradeCurricularAtual(), mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, configuracaoAcademicoVO, usuarioVO, anoParametroHistoricosCargaHorariaCursada)) {
						mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE);
						mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().add(mapaConvocacaoEnadeMatriculaVO);
					} else if (realizarVerificacaoAlunoConcluinte(matriculaVO, matriculaVO.getGradeCurricularAtual(), mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, configuracaoAcademicoVO, usuarioVO, anoParametroHistoricosCargaHorariaCursada)) {
						mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE);
						mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().add(mapaConvocacaoEnadeMatriculaVO);
					} else {
						mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO);
						mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().add(mapaConvocacaoEnadeMatriculaVO);
					}
				} catch (Exception e) {
					ex.adicionarListaMensagemErro(e.getMessage());
				}
			}
			if (Uteis.isAtributoPreenchido(ex.getListaMensagemErro())) {
				throw ex;
			}
			}
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosAnoIngressoSemestreIngresso(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		if (matriculaVO.getAnoIngresso().equals("")) {
			matriculaVO.setAnoIngresso(getFacadeFactory().getMatriculaPeriodoFacade().consultarAnoIngressoMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), usuarioVO));
		}
		if (matriculaVO.getSemestreIngresso().equals("")) {
			matriculaVO.setSemestreIngresso(getFacadeFactory().getMatriculaPeriodoFacade().consultarSemestreIngressoMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), usuarioVO));
		}
	}

	/**
	 * Regra Enade tirado do diário da União no ano de 2014 I - estudantes
	 * ingressantes, aqueles que tenham iniciado o respectivo curso com
	 * matrícula no ano de 2014 e que tenham concluído até 25% (vinte e cinco
	 * por cento) da carga horária mínima do currículo do curso até o término do
	 * período previsto no art. 7º, § 5º desta Portaria Normativa;
	 * 
	 * @param matriculaComHistoricoAlunoVO
	 * @param mapaConvocacaoEnadeVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @author CarlosEugenio
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean realizarVerificacaoAlunoIngressante(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO, String anoParametroHistoricosCargaHorariaCursada) throws Exception {
		String anoIngressoAluno = matriculaVO.getAnoIngresso();
		String anoBaseIngressante = mapaConvocacaoEnadeVO.getEnadeCursoVO().getAnoBaseIngressante();
		Boolean considerarCargaHorariaAtividadeComplementar = mapaConvocacaoEnadeVO.getEnadeCursoVO().getConsiderarCargaHorariaAtividadeComplementar();
		Boolean considerarCargaHorariaEstagio = mapaConvocacaoEnadeVO.getEnadeCursoVO().getConsiderarCargaHorariaEstagio();

		if (!anoIngressoAluno.equals(anoBaseIngressante)) {
			return Boolean.FALSE;
		}
		Integer percentualEvolucaoAcademicaIngressante = mapaConvocacaoEnadeVO.getEnadeCursoVO().getPercentualCargaHorariaIngressante();
		Integer totalCargaHorariaGradeCurricular = gradeCurricularVO.getCargaHoraria();
		Double cargaHorariaAtividadeComplementar = 0.0;
		Integer cargaHorariaEstagio = 0;

		Integer cargaHorariaCursada = getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalenteEDisciplinaPorCorrespondecia(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO, anoParametroHistoricosCargaHorariaCursada);
		Integer cargaHorariaCursandoUltimoPeriodo = getFacadeFactory().getHistoricoFacade().consultarCargaHorariaQueAlunoEstaCursandoDoUltimoPeriodo(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
		if (considerarCargaHorariaAtividadeComplementar) {
			cargaHorariaAtividadeComplementar = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaConsideradaPorMatriculaGradeCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
		}
		if (considerarCargaHorariaEstagio) {
			cargaHorariaEstagio = getFacadeFactory().getEstagioFacade().consultarCargaHorariaCumpridaPorMatriculaEGradeCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
		}


		Double percentualIntegralizacaoPorCargaHoraria = 0.0;
		Double percentualAtual = 0.0;
		Double percentualPossivelCursar = 0.0;
		Double totalCargaHorariaSomandoPeriodoPrevistoIngressante = 0.0;
		Double cargaHorariaAtual = (cargaHorariaCursada + cargaHorariaCursandoUltimoPeriodo + cargaHorariaAtividadeComplementar + cargaHorariaEstagio);

		if (cargaHorariaAtual != 0.0) {
			percentualAtual = Uteis.arrendondarForcando2CadasDecimais(((cargaHorariaAtual) * 100.0) / totalCargaHorariaGradeCurricular);
		}
		if (!cargaHorariaCursandoUltimoPeriodo.equals(0)) {
			percentualPossivelCursar = Uteis.arrendondarForcando2CadasDecimais((cargaHorariaCursandoUltimoPeriodo * 100.0) / totalCargaHorariaGradeCurricular);
		}
		
		totalCargaHorariaSomandoPeriodoPrevistoIngressante = (cargaHorariaAtual + cargaHorariaCursandoUltimoPeriodo);
		if (Uteis.isAtributoPreenchido(totalCargaHorariaSomandoPeriodoPrevistoIngressante)) {
			percentualIntegralizacaoPorCargaHoraria = Uteis.arrendondarForcando2CadasDecimais((totalCargaHorariaSomandoPeriodoPrevistoIngressante * 100) / totalCargaHorariaGradeCurricular);
		}
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoAtual(percentualAtual);
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoPossivelCursar(percentualPossivelCursar);
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoPrevistoDataEnade(percentualIntegralizacaoPorCargaHoraria);
		
		mapaConvocacaoEnadeMatriculaVO.setCargaHorariaAtual(cargaHorariaAtual);
		
		if (percentualAtual <= percentualEvolucaoAcademicaIngressante) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Regra Enade retirado do diário da União do ano de 2014 - Art. 6º II -
	 * estudantes concluintes dos Cursos de Bacharelado, aqueles que tenham
	 * expectativa de conclusão do curso até julho de 2015, assim como aqueles
	 * que tiverem concluído mais de 80% (oitenta por cento) da carga horária
	 * mínima do currículo do curso da IES até o término do período previsto no
	 * art. 7º, § 5º desta Portaria Normativa; e III - estudantes concluintes
	 * dos Cursos Superiores de Tecnologia, aqueles que tenham expectativa de
	 * conclusão do curso até dezembro de 2014, assim como aqueles que tiverem
	 * concluído mais de 75% (setenta e cinco por cento) da carga horária mínima
	 * do currículo do curso da IES até o término do período previsto no art.
	 * 7º, § 5º desta Portaria Normativa.
	 * 
	 * @param matriculaComHistoricoAlunoVO
	 * @param mapaConvocacaoEnadeVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 * @author CarlosEugenio
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean realizarVerificacaoAlunoConcluinte(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO, String anoParametroHistoricosCargaHorariaCursada) throws Exception {

		if (realizarVerificacaoAlunoPossuiCargaHorariaMinimaCurriculoAteDeterminadaDataPrevistaConclusao(matriculaVO, gradeCurricularVO, mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, configuracaoAcademicoVO, usuarioVO, anoParametroHistoricosCargaHorariaCursada)) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}

	/**
	 * Realiza a verificação se o Aluno atingiu a carga horária minima até data
	 * determinada prevista pelo Enade. Caso ele tenha atingido ele já está apto
	 * a ser um concluinte.
	 * 
	 * @param matriculaVO
	 * @param gradeCurricularVO
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean realizarVerificacaoAlunoPossuiCargaHorariaMinimaCurriculoAteDeterminadaDataPrevistaConclusao(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO, String anoParametroHistoricosCargaHorariaCursada) throws Exception {
		Boolean considerarCargaHorariaAtividadeComplementar = mapaConvocacaoEnadeVO.getEnadeCursoVO().getConsiderarCargaHorariaAtividadeComplementar();
		Boolean considerarCargaHorariaEstagio = mapaConvocacaoEnadeVO.getEnadeCursoVO().getConsiderarCargaHorariaEstagio();
		Double cargaHorariaAtividadeComplementar = 0.0;
		Integer cargaHorariaEstagio = 0;
		Integer cargaHorariaCursada = getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalenteEDisciplinaPorCorrespondecia(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO, anoParametroHistoricosCargaHorariaCursada);
		Integer cargaHorariaCursandoUltimoPeriodo = getFacadeFactory().getHistoricoFacade().consultarCargaHorariaQueAlunoEstaCursandoDoUltimoPeriodo(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
		Integer totalCargaHorariaGradeCurricular = gradeCurricularVO.getCargaHoraria();
		if (considerarCargaHorariaAtividadeComplementar) {
			cargaHorariaAtividadeComplementar = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaConsideradaPorMatriculaGradeCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
			totalCargaHorariaGradeCurricular += gradeCurricularVO.getTotalCargaHorariaAtividadeComplementar();
			if(cargaHorariaAtividadeComplementar > gradeCurricularVO.getTotalCargaHorariaAtividadeComplementar()){
				cargaHorariaAtividadeComplementar = Double.valueOf(gradeCurricularVO.getTotalCargaHorariaAtividadeComplementar());
			}
		}
		if (considerarCargaHorariaEstagio) {
			cargaHorariaEstagio = getFacadeFactory().getEstagioFacade().consultarCargaHorariaCumpridaPorMatriculaEGradeCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
			totalCargaHorariaGradeCurricular += gradeCurricularVO.getTotalCargaHorariaEstagio();
			if(cargaHorariaEstagio > gradeCurricularVO.getTotalCargaHorariaEstagio()){
				cargaHorariaEstagio = gradeCurricularVO.getTotalCargaHorariaEstagio();
			}
		}
		Double totalCargaHorariaCumpridaAteDataPrevista = 0.0;
		Double totalCargaHorariaAtual = 0.0;
		Integer percentualCargaHorariaConcluinte = mapaConvocacaoEnadeVO.getEnadeCursoVO().getPercentualCargaHorariaConcluinte();
		Double percentualIntegralizacaoPorCargaHoraria = 0.0;
		Double percentualAtual = 0.0;
		Double percentualPossivelCursar = 0.0;
		totalCargaHorariaAtual = cargaHorariaCursada +  cargaHorariaAtividadeComplementar + cargaHorariaEstagio;
		if ( totalCargaHorariaAtual != 0.0) {
			percentualAtual = Uteis.arrendondarForcando2CadasDecimais(((totalCargaHorariaAtual) * 100) / totalCargaHorariaGradeCurricular);
		}
		totalCargaHorariaCumpridaAteDataPrevista = cargaHorariaCursada + cargaHorariaCursandoUltimoPeriodo +  cargaHorariaAtividadeComplementar + cargaHorariaEstagio;
		if (Uteis.isAtributoPreenchido(totalCargaHorariaCumpridaAteDataPrevista)) {
			percentualIntegralizacaoPorCargaHoraria = Uteis.arrendondarForcando2CadasDecimais((totalCargaHorariaCumpridaAteDataPrevista * 100) / totalCargaHorariaGradeCurricular);
		}
		if (!cargaHorariaCursandoUltimoPeriodo.equals(0)) {
			percentualPossivelCursar = Uteis.arrendondarForcando2CadasDecimais((cargaHorariaCursandoUltimoPeriodo * 100.0) / totalCargaHorariaGradeCurricular);
		}
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoAtual(percentualAtual <= 100.0 ? percentualAtual : 100.0);
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoPossivelCursar(percentualPossivelCursar);
		mapaConvocacaoEnadeMatriculaVO.setPercentualIntegralizacaoPrevistoDataEnade(percentualIntegralizacaoPorCargaHoraria <= 100 ? percentualIntegralizacaoPorCargaHoraria : 100);
		mapaConvocacaoEnadeMatriculaVO.setCargaHorariaAtual(totalCargaHorariaAtual);
		if (percentualAtual < percentualCargaHorariaConcluinte) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Integer obterCargaHorariaCurriculoAlunoEstaraEstudandoDeAcordoComADataPrevistaIngressante(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO) {
		Date periodoPrevistoIngressante = mapaConvocacaoEnadeVO.getEnadeCursoVO().getPeriodoPrevistoTerminoIngressante();
		int anoPrevistoIngressante = Uteis.getAnoData(periodoPrevistoIngressante);
		int semestrePrevistoIngressante = Integer.parseInt(Uteis.getSemestreData(periodoPrevistoIngressante));
		int anoAtual = Integer.parseInt(Uteis.getAnoDataAtual4Digitos());
		int semestreAtual = Integer.parseInt(Uteis.getSemestreAtual());
		Integer cargaHorariaPrevisaoSerCursada = 0;
		Integer periodoLetivoAtual = getFacadeFactory().getPeriodoLetivoFacade().consultarUltimoPeriodoLetivoPorMatriculaGradeCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
		Integer periodoLetivoProgredir = periodoLetivoAtual + 1;

		while (anoAtual <= anoPrevistoIngressante) {
			if (anoAtual == anoPrevistoIngressante && semestreAtual == semestrePrevistoIngressante) {
				break;
			}
			cargaHorariaPrevisaoSerCursada = cargaHorariaPrevisaoSerCursada + getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaPorGradeCurricularPeriodoLetivoDeDisciplinaQueNaoEstaNoHistorico(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), periodoLetivoProgredir, usuarioVO);
			periodoLetivoProgredir++;
			if (semestreAtual == 2) {
				anoAtual++;
			}
			if (semestreAtual == 1) {
				semestreAtual = 2;
			} else {
				semestreAtual = 1;
			}
		}
		return cargaHorariaPrevisaoSerCursada;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMApaConvocacaoPorCodigo(final Integer mapaConvocacaoEnade, final SituacaoMapaConvocacaoEnadeEnum situacao, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE MapaConvocacaoEnade set situacaoMapaConvocacaoEnade=? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacao.name());
					sqlAlterar.setInt(++i, mapaConvocacaoEnade.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public void realizarOrdenacaoMapaConvocacao(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO) {
		Ordenacao.ordenarLista(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(), "nomeAluno");
		Ordenacao.ordenarLista(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs(), "nomeAluno");
		Ordenacao.ordenarLista(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(), "nomeAluno");
	}

	public void inicializarDadosTotalizadorLista(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO) {
		mapaConvocacaoEnadeVO.setTotalAlunosIngressantes(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().size());
		mapaConvocacaoEnadeVO.setTotalAlunosDispensados(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().size());
		mapaConvocacaoEnadeVO.setTotalAlunosConcluintes(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().size());
	}

	public void validarDadosObservacao(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) throws Exception {
		if (mapaConvocacaoEnadeMatriculaVO.getObservacao().equals("")) {
			throw new Exception("O campo OBSERVAÇÃO deve ser informado.");
		}
	}

	/**
	 * INGRESSANTES -> DISPENSADOS
	 * 
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void realizarMudancaAlunoIngressanteParaDispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosObservacao(mapaConvocacaoEnadeMatriculaVO);
		removerAlunoMapaAlunosIngressantes(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		adicionarAlunoMapaAlunosIngressantesParaDispensados(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
	}

	public void adicionarAlunoMapaAlunosIngressantesParaDispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO);
		mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().add(mapaConvocacaoEnadeMatriculaVO);
	}

	public void removerAlunoMapaAlunosIngressantes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		int index = 0;

		for (MapaConvocacaoEnadeMatriculaVO objExistente : mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs()) {
			if (objExistente.getMatriculaVO().getMatricula().equals(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getMatricula())) {
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * DISPENSADO -> INGRESSANTES
	 * 
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void realizarMudancaAlunoDispensadoParaIngressante(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosObservacao(mapaConvocacaoEnadeMatriculaVO);
		removerAlunoMapaAlunosDispensados(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		adicionarAlunoMapaAlunosDispensadosParaIngressantes(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
	}

	public void adicionarAlunoMapaAlunosDispensadosParaIngressantes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE);
		mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().add(mapaConvocacaoEnadeMatriculaVO);
	}

	public void removerAlunoMapaAlunosDispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		int index = 0;

		for (MapaConvocacaoEnadeMatriculaVO objExistente : mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs()) {
			if (objExistente.getMatriculaVO().getMatricula().equals(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getMatricula())) {
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * DISPENSADO -> CONCLUINTE
	 * 
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void realizarMudancaAlunoDispensadoParaConcluinte(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosObservacao(mapaConvocacaoEnadeMatriculaVO);
		removerAlunoMapaAlunosDispensados(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		adicionarAlunoMapaAlunosDispensadosParaConcluinte(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
	}

	public void adicionarAlunoMapaAlunosDispensadosParaConcluinte(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE);
		mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().add(mapaConvocacaoEnadeMatriculaVO);
	}

	/**
	 * CONCLUINTE -> DISPENSADOS
	 * 
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void realizarMudancaAlunoConcluinteParadispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosObservacao(mapaConvocacaoEnadeMatriculaVO);
		removerAlunoMapaAlunosConcluinte(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		adicionarAlunoMapaAlunosConcluinteParaDispensados(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
	}

	public void adicionarAlunoMapaAlunosConcluinteParaDispensados(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_DISPENSADO);
		mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().add(mapaConvocacaoEnadeMatriculaVO);
	}

	public void removerAlunoMapaAlunosConcluinte(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		int index = 0;

		for (MapaConvocacaoEnadeMatriculaVO objExistente : mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs()) {
			if (objExistente.getMatriculaVO().getMatricula().equals(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getMatricula())) {
				mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * DISPENSADO -> CONCLUINTES
	 * 
	 * @param mapaConvocacaoEnadeVO
	 * @param mapaConvocacaoEnadeMatriculaVO
	 * @param usuarioVO
	 */
	public void realizarMudancaAlunoDispensadoParaConcluintes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		removerAlunoMapaAlunosDispensados(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		adicionarAlunoMapaAlunosDispensadosParaConcluintes(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeMatriculaVO, usuarioVO);
		realizarOrdenacaoMapaConvocacao(mapaConvocacaoEnadeVO);
		inicializarDadosTotalizadorLista(mapaConvocacaoEnadeVO);
	}

	public void adicionarAlunoMapaAlunosDispensadosParaConcluintes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuarioVO) {
		mapaConvocacaoEnadeMatriculaVO.setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE);
		mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().add(mapaConvocacaoEnadeMatriculaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MapaConvocacaoEnadeMatriculaVO inicializarDadosListaMapaConvocacaoEnadeMatricula(MatriculaVO matriculaVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuarioVO) {
		MapaConvocacaoEnadeMatriculaVO obj = new MapaConvocacaoEnadeMatriculaVO();
		obj.setMapaConvocacaoEnadeVO(mapaConvocacaoEnadeVO);
		obj.setMatriculaVO(matriculaVO);
		if (Uteis.isAtributoPreenchido(matriculaVO.getUltimoMatriculaPeriodoVO().getPeriodoLetivo())) {
			obj.setPeriodoLetivoAtual(matriculaVO.getUltimoMatriculaPeriodoVO().getPeriodoLetivo());
		} else {
			obj.setPeriodoLetivoAtual(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoAtualPorMatricula(matriculaVO.getMatricula(), usuarioVO));
		}
		obj.setAnoIngresso(matriculaVO.getAnoIngresso());
		obj.setSemestreIngresso(matriculaVO.getSemestreIngresso());
		obj.setAnoSemestreIngresso(obj.getAnoIngresso() + "/"+ obj.getSemestreIngresso());
		obj.setFormacaoAcademicaVO(matriculaVO.getFormacaoAcademica());
		obj.setAnoAtual(matriculaVO.getMatriculaPeriodoVO().getAno());
		obj.setSemestreAtual(matriculaVO.getMatriculaPeriodoVO().getSemestre());
		obj.setUnidadeEnsino(matriculaVO.getUnidadeEnsino().getNome());
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaVO> consultarAlunosPorUnidadeEnsinoCurso(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer anoEnade, UsuarioVO usuarioVO) throws Exception {
		return getFacadeFactory().getMatriculaFacade().consultaRapidaPorUnidadeEnsinoCursoESituacaoMatricula(unidadeEnsinoVOs, curso, SituacaoVinculoMatricula.ATIVA.getValor(), anoEnade, usuarioVO);
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por retornar o identificador desta classe. Este
	 * identificar ï¿½ utilizado para verificar as permissï¿½es de acesso as
	 * operaï¿½ï¿½es desta classe.
	 */
	public static String getIdEntidade() {
		return MapaConvocacaoEnade.idEntidade;
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por definir um novo valor para o identificador
	 * desta classe. Esta alteraï¿½ï¿½o deve ser possï¿½vel, pois, uma mesma
	 * classe de negï¿½cio pode ser utilizada com objetivos distintos. Assim ao
	 * se verificar que Como o controle de acesso ï¿½ realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		MapaConvocacaoEnade.idEntidade = idEntidade;
	}
	
	
	public void montarListaMapaconvocacaoPorEnade(EnadeVO enadeVO, List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOs,MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO,  List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOErros , UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		listaMapaConvocacaoEnadeVOs.clear();
		for( EnadeVO obj  : enadeVO.getEnadeVOs()) {
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
			listaMapaConvocacaoEnadeVOs = consultaRapidaPorEnade(obj.getTituloEnade(),unidadeEnsinoVO.getCodigo(), false,  usuario, NivelMontarDados.BASICO);
		}
		if(mapaConvocacaoEnadeVO.getPossuiErro()) {
			listaMapaConvocacaoEnadeVOErros.add(mapaConvocacaoEnadeVO);
		}else {
			listaMapaConvocacaoEnadeVOs.add(mapaConvocacaoEnadeVO);
		}
	}
	
	public void montarListaMapaconvocacaoPorSituacao(EnadeVO enadeVO, List<MapaConvocacaoEnadeVO> listaSituacaoEnadeVOs,MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO,  List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOErros , UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		listaSituacaoEnadeVOs.clear();
		for( MapaConvocacaoEnadeVO obj  : mapaConvocacaoEnadeVO.getListaSituacaoEnade()) {
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
				
			}
			if(mapaConvocacaoEnadeVO.getPossuiErro()) {
				listaMapaConvocacaoEnadeVOErros.add(mapaConvocacaoEnadeVO);
			}else {
				listaSituacaoEnadeVOs.add(mapaConvocacaoEnadeVO);
			}
		}
	
	public PrintWriter getPwAluno() {
		return pwAluno;
	}
	public void setPwAluno(PrintWriter pwAluno) {
		this.pwAluno = pwAluno;
	}
	public PrintWriter getPwCurso() {
		return pwCurso;
	}
	public void setPwCurso(PrintWriter pwCurso) {
		this.pwCurso = pwCurso;
	}
	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	private void executarCriarRegistroAlunos(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario, EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder linha = new StringBuilder();
		List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaIngressantesConcluintes = new ArrayList<MapaConvocacaoEnadeMatriculaVO>();
		if(mapaConvocacaoEnadeVO.getIngressantes()){
			mapaConvocacaoEnadeMatriculaIngressantesConcluintes.addAll(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs());
		}
		else if(mapaConvocacaoEnadeVO.getConcluintes()){
			mapaConvocacaoEnadeMatriculaIngressantesConcluintes.addAll(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs());
		}
		if(mapaConvocacaoEnadeVO.getIngressantes()){
			getPwAluno().println("CO_PROJETO;TP_ORIGEM;CO_IES;CO_CURSO;NU_CPF;NU_ANO_FIM_ENSINO_MEDIO;CO_TURNO_GRADUACAO;NU_PERCENTUAL_INTEGRALIZACAO;NU_ANO_INICIO_GRADUACAO;NU_SEMESTRE_INICIO_GRADUACAO");
		} else if (mapaConvocacaoEnadeVO.getConcluintes()){
			getPwAluno().println("CO_PROJETO;TP_ORIGEM;CO_IES;CO_CURSO;NU_CPF;NU_ANO_FIM_ENSINO_MEDIO;CO_TURNO_GRADUACAO;NU_PERCENTUAL_INTEGRALIZACAO;NU_ANO_FORMATURA;NU_SEMESTRE_FORMATURA;NU_ANO_INICIO_GRADUACAO;IN_MUNICIPIO_POLO_EXTERIOR;CO_MUNICIPIO_POLO");
		}
		for (MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaItem : mapaConvocacaoEnadeMatriculaIngressantesConcluintes) {
			CursoTurnoVO cursoTurnoVO = getFacadeFactory().getCursoTurnoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getCodigo(), mapaConvocacaoEnadeMatriculaItem.getMatriculaVO().getTurno().getCodigo(), false, usuarioVO);
			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeMatriculaItem.getMatriculaVO().getMatricula());
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuarioVO); 
			FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridade(matriculaVO.getAluno().getCodigo(), NivelFormacaoAcademica.MEDIO, false, usuarioVO);
			matriculaVO.getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(matriculaVO.getUnidadeEnsino().getCidade().getCodigo(), false, usuarioVO));
			validarDadosGeracaoArquivo(mapaConvocacaoEnadeMatriculaItem, mapaConvocacaoEnadeVO, enadeVO, cursoTurnoVO, cursoVO, matriculaVO, matriculaPeriodoVO, usuarioVO,formacaoAcademicaVO);
			Optional<MapaConvocacaoEnadeMatriculaVO> optional = mapaConvocacaoEnadeVO.getListaAlunoErroGeracaoArquivoEnade().stream().filter(o -> o.getMatriculaVO().getMatricula().equals(mapaConvocacaoEnadeMatriculaItem.getMatriculaVO().getMatricula())).findAny();
			if (!optional.isPresent() && !Uteis.isAtributoPreenchido(mapaConvocacaoEnadeVO.getlistaCursoErroGeracaoArquivoEnade())) {
				getPwAluno().println(executarGerarRegistroAluno(linha, Uteis.getAnoDataAtual(), mapaConvocacaoEnadeMatriculaItem, mapaConvocacaoEnadeVO, enadeVO, cursoTurnoVO, cursoVO, matriculaVO, matriculaPeriodoVO, usuarioVO, formacaoAcademicaVO));
				linha.setLength(0);
			}
		}
	}
	
	 public void validarDadosGeracaoArquivo(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, EnadeVO enadeVO, CursoTurnoVO cursoTurnoVO, CursoVO cursoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO, FormacaoAcademicaVO formacaoAcademicaVO) throws Exception {
		if (!mapaConvocacaoEnadeMatriculaVO.isValidarDados().booleanValue()) {
			return;
	    }
		if (enadeVO.getCodigoProjeto().isEmpty()) {
			throw new ConsistirException("Deve ser informado o Código do Projeto. Certifique-se que o código foi gravado antes de realizar a geração.");
	    }
        ConsistirException consistir = new ConsistirException();
	    Uteis.checkStateList(!Uteis.validaCPF(Uteis.removerMascara(matriculaVO.getAluno().getCPF())), "Deve ser informado um CPF válido no cadastro do aluno " + mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno().getNome() +"." , consistir);
	    Uteis.checkStateList(!Uteis.isAtributoPreenchido(formacaoAcademicaVO.getAnoDataFim_Apresentar()), "O aluno " + mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno().getNome()+ " deve possuir a informação da conclusão do ENSINO MÉDIO.", consistir);
	    Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaVO.getAnoIngresso()), "Na matrícula do aluno " + matriculaVO.getMatricula() + " deve ser informado o campo ANO INGRESSO CURSO.", consistir);
        if(matriculaVO.getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
        	Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaVO.getUnidadeEnsino().getCidade().getCodigoIBGE()), "Deve ser informado na cidade " + matriculaVO.getUnidadeEnsino().getCidade().getNome() + "-" + matriculaVO.getUnidadeEnsino().getCidade().getEstado().getSigla() + " o campo código do IBGE.", consistir);
        }
        Uteis.checkStateList(!Uteis.isAtributoPreenchido(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getUnidadeEnsino().getCodigoIES()), "Na matrícula do aluno " + matriculaVO.getMatricula() + " deve ser informado o campo CÓDIGO IES na UNIDADE DE ENSINO.", consistir);
	    Uteis.checkStateList(!Uteis.isAtributoPreenchido(formacaoAcademicaVO.getAnoDataFim_Apresentar()), "O aluno " + mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno().getNome()+ " deve possuir a informação da conclusão do ENSINO MÉDIO.", consistir);
        if (!consistir.getListaMensagemErro().isEmpty()) {
        	mapaConvocacaoEnadeMatriculaVO.setErrosGeracaoArquivo(consistir.getListaMensagemErro().toString().replace("[", "").replace("]", "").replace(",", " "));
        	mapaConvocacaoEnadeVO.getListaAlunoErroGeracaoArquivoEnade().add(mapaConvocacaoEnadeMatriculaVO);
        }
	}
	 
	 public void validarDadosGeracaoArquivoCurso(EnadeCursoVO enadeCurso, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, EnadeVO enadeVO, CursoVO cursoVO, UsuarioVO usuarioVO) throws Exception {
		 mapaConvocacaoEnadeVO.getlistaCursoErroGeracaoArquivoEnade().clear();
		 if (!mapaConvocacaoEnadeVO.isValidarDados().booleanValue()) {
	            return;
	        }
	        ConsistirException consistir = new ConsistirException();
	        @SuppressWarnings("unchecked")
			List<CursoTurnoVO>  listaCursoTurnoVO = getFacadeFactory().getCursoTurnoFacade().consultarPorNomeCurso(enadeCurso.getCursoVO().getNome(), false, usuarioVO);
	        for (CursoTurnoVO ctVO : listaCursoTurnoVO) {
	        	if(enadeCurso.getCursoVO().getCodigo().equals(ctVO.getCurso()) && !Uteis.isAtributoPreenchido(ctVO.getNomeTurnoCenso())) {
	        		Uteis.checkStateList(!Uteis.isAtributoPreenchido(ctVO.getNomeTurnoCenso()), "Deve ser informado no cadastro do curso "+ mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getNome()+" na aba de turno, no turno "+ ctVO.getTurno().getNome() + " o campo NOME TURNO CONSIDERADO CENSO.", consistir);
	        	}
	        }
//	        Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigoIES()), "Deve ser informado o código da IES no cadastro da unidade de Ensino.", consistir);
	        Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getIdCursoInep()), "Deve ser informado o Id do INEP No cadastro do curso.", consistir);
//	        if(cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
//	        	Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidade().getCodigoIBGE()), "Deve ser informado na cidade "+ unidadeEnsinoVO.getCidade().getNome() +"o campo código do IBGE.", consistir);
//	        }
//	        Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidade()), "Deve ser informado na Unidade Ensino "+ unidadeEnsinoVO.getNome() +"o campo Cidade.", consistir);
	        if (!consistir.getListaMensagemErro().isEmpty()) {
	        	mapaConvocacaoEnadeVO.setErrosGeracaoArquivoCurso(consistir.getListaMensagemErro().toString().replace("[", "").replace("]", "").replace(",", " "));
	        	mapaConvocacaoEnadeVO.getlistaCursoErroGeracaoArquivoEnade().add(mapaConvocacaoEnadeVO);
        }
	}
	 
//metodo responsavel por gerar as linhas com os dados dos alunos no txt	
	private String executarGerarRegistroAluno(StringBuilder linha, String anoIngresso, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, EnadeVO enadeVO, CursoTurnoVO cursoTurnoVO, CursoVO cursoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO, FormacaoAcademicaVO formacaoAcademicaVO) throws Exception {
		try {
//			01 - codigo projeto
			linha.append(enadeVO.getCodigoProjeto()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			02 - Tipo origem
			linha.append("E").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			03 - codigo da unidade de ensino
			linha.append(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getUnidadeEnsino().getCodigoIES()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			04 - codigo curso inep
			linha.append(cursoVO.getIdCursoInep()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			05 - CPF
			linha.append(matriculaVO.getAluno().getCPF().replaceAll("[^0-9]", "")).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			06 - Ano formação ensino medio
			linha.append(formacaoAcademicaVO.getAnoDataFim_Apresentar()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//			07 - turno
			if(Uteis.isAtributoPreenchido(cursoTurnoVO.getNomeTurnoCenso()) && cursoTurnoVO.getNomeTurnoCenso().equals(cursoTurnoVO.getNomeTurnoCenso().MATUTINO)) {
				linha.append("1").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}else if(Uteis.isAtributoPreenchido(cursoTurnoVO.getNomeTurnoCenso()) && cursoTurnoVO.getNomeTurnoCenso().equals(cursoTurnoVO.getNomeTurnoCenso().VESPERTINO)) {
				linha.append("2").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}else if(Uteis.isAtributoPreenchido(cursoTurnoVO.getNomeTurnoCenso()) && cursoTurnoVO.getNomeTurnoCenso().equals(cursoTurnoVO.getNomeTurnoCenso().INTEGRAL)) {
				linha.append("3").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}else if(Uteis.isAtributoPreenchido(cursoTurnoVO.getNomeTurnoCenso()) && cursoTurnoVO.getNomeTurnoCenso().equals(cursoTurnoVO.getNomeTurnoCenso().NOTURNO)) {
				linha.append("4").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}
			if (mapaConvocacaoEnadeMatriculaVO.getSituacaoConvocadosEnade().equals(SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE)) {
//				08 - Percentual integralização ingressantes
				linha.append(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoAtual(), 1).replace("," , ".")).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//				09 - ano ingresso dos ingressantes
				linha.append(matriculaVO.getAnoIngresso()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//				10 - semestre ingresso ingressantes
				if(matriculaVO.getCurso().getSemestral()) {
					linha.append(matriculaVO.getSemestreIngresso()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
				}else if(!matriculaVO.getCurso().getIntegral() && !matriculaVO.getCurso().getSemestral()) {
					linha.append("1").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
				}
			}
			if (mapaConvocacaoEnadeMatriculaVO.getSituacaoConvocadosEnade().equals(SituacaoConvocadosEnadeEnum.ALUNO_CONCLUINTE)){
//				08 - Percentual integralização concluintes
				if(mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoAtual() > 100) {
					linha.append("100").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
				} else {
					linha.append(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoAtual(), 1).replace("," , ".")).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
				}
//				09 - ano e semestre conclusão curso
				if(matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
					if (Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) && Uteis.isAtributoPreenchido(matriculaVO.getSemestreConclusao())) {
						linha.append(matriculaVO.getAnoConclusao()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append(matriculaVO.getSemestreConclusao()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					} else if (mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoPrevistoDataEnade() == 100.0 && !Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) && !Uteis.isAtributoPreenchido(matriculaVO.getSemestreConclusao())){
						linha.append(matriculaPeriodoVO.getAno()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append("2").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					} else if(mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoPrevistoDataEnade() < 100.0 && !Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) && !Uteis.isAtributoPreenchido(matriculaVO.getSemestreConclusao())) {
						int AnoFormatura = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(mapaConvocacaoEnadeVO.getEnadeCursoVO().getPeriodoPrevistoTerminoConcluinte())).getYear();
						linha.append(AnoFormatura).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append("1").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					}
				}else if(matriculaVO.getCurso().getPeriodicidade().equals("AN")) {
					if (Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao())){
						linha.append(matriculaVO.getAnoConclusao()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append("2").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					} else if (!Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) && mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoPrevistoDataEnade() == 100.0) {
						linha.append(matriculaPeriodoVO.getAno()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append("2").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					}  else if (!Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) && mapaConvocacaoEnadeMatriculaVO.getPercentualIntegralizacaoPrevistoDataEnade() < 100.0) {
						int AnoFormatura = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(mapaConvocacaoEnadeVO.getEnadeCursoVO().getPeriodoPrevistoTerminoConcluinte())).getYear();
						linha.append(AnoFormatura).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
						linha.append("2").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
					}
				}
//				10 - Ano ingresso curso 
				linha.append(matriculaVO.getAnoIngresso()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
//				11 - codigo municipio polo exterior (para cursos no exterior deve ser 1, no brasil 0)
				linha.append("0").append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}

//			12 - codigo municipio para casos de curso ead
			if(matriculaVO.getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				linha.append(matriculaVO.getUnidadeEnsino().getCidade().getCodigoIBGE()).append(mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().SEPARADOR);
			}
			return linha.toString(); 
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void executarCriarArquivoTexto(String caminhoPasta, String nomeArquivo) throws Exception {
		getFacadeFactory().getArquivoHelper().criarCaminhoPastaDeInclusaoArquivo(caminhoPasta);
		setPwAluno(getFacadeFactory().getArquivoHelper().criarArquivoTexto(caminhoPasta, nomeArquivo, true));
	}
	
	// geração do arquivo 
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarGerarArquivo(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, String caminhoPasta, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, EnadeVO enadeVO,  UsuarioVO usuarioVO) throws Exception {
		String nomeArquivo = "";
		if(Uteis.isAtributoPreenchido(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs()) && mapaConvocacaoEnadeVO.getIngressantes()) {
			nomeArquivo = "ENADE" + enadeVO.getCodigoProjeto() + "_N99_BR_" + Uteis.getData(new Date(), "ddMMyyyy") +"_"+ mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getIdCursoInep() + "_E" +  "001" + ".txt";
		}
		else if(Uteis.isAtributoPreenchido(mapaConvocacaoEnadeVO.getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs()) && mapaConvocacaoEnadeVO.getConcluintes()) {
			nomeArquivo = "ENADE" +enadeVO.getCodigoProjeto() + "_N99_BR_" + Uteis.getData(new Date(), "ddMMyyyy") +"_"+ mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO().getIdCursoInep() + "_E" +  "002" + ".txt";
		}
		try {
			if (!Uteis.isAtributoPreenchido(nomeArquivo)) {
				throw new Exception("Não foi possível realizar a geração do arquivo pois não foram localizados alunos na listagem de " + (mapaConvocacaoEnadeVO.getIngressantes() ? "ingressantes." : "concluintes."));
			}
			executarCriarArquivoTexto(caminhoPasta + File.separator + PastaBaseArquivoEnum.ENADE.getValue(), nomeArquivo);
			executarCriarRegistroAlunos(mapaConvocacaoEnadeVO, usuario, enadeVO, usuarioVO);
			validarDadosGeracaoArquivoCurso(mapaConvocacaoEnadeVO.getEnadeCursoVO(), mapaConvocacaoEnadeVO, enadeVO, mapaConvocacaoEnadeVO.getEnadeCursoVO().getCursoVO(), usuarioVO);
			if (!mapaConvocacaoEnadeVO.getListaAlunoErroGeracaoArquivoEnade().isEmpty()) {
				throw new Exception("Para que o arquivo seja gerado é necessário a correção de alguns dados no cadastro do Aluno. ");
			}
			if (!mapaConvocacaoEnadeVO.getlistaCursoErroGeracaoArquivoEnade().isEmpty()) {
				throw new Exception("Para que o arquivo seja gerado é necessário a correção de alguns dados no cadastro do Curso. ");
			}
			
			EditorOC editorOC = new EditorOC();
			getPwAluno().print(editorOC.getText());
			getPwAluno().close();
			if (!mapaConvocacaoEnadeVO.getListaAlunoEnade().isEmpty()) {
				SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
				superParametroRelVO.setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				superParametroRelVO.setSubReport_Dir(getCaminhoBaseRelatorio());
				superParametroRelVO.setNomeEmpresa(mapaConvocacaoEnadeVO.getUnidadeEnsino());
				superParametroRelVO.setTituloRelatorio("Enade Aluno - " + Uteis.getSemestreAtual() + "/" + Uteis.getAnoDataAtual());
				superParametroRelVO.setListaObjetos(mapaConvocacaoEnadeVO.getListaAlunoEnade());
				GeradorRelatorio.realizarExportacaoEXCEL(superParametroRelVO, caminhoPasta + File.separator + PastaBaseArquivoEnum.ENADE.getValue(), nomeArquivo.replaceAll(".txt", ".xlsx"));
				mapaConvocacaoEnadeVO.getArquivoAlunoExcel().setNome(nomeArquivo.replaceAll(".txt", ".xlsx"));
				mapaConvocacaoEnadeVO.getArquivoAlunoExcel().setOrigem(OrigemArquivo.MATRICULA_ENADE.getValor());
				mapaConvocacaoEnadeVO.getArquivoAlunoExcel().setCodOrigem(mapaConvocacaoEnadeVO.getCodigo());
				mapaConvocacaoEnadeVO.getArquivoAlunoExcel().setPastaBaseArquivo(caminhoPasta + File.separator + PastaBaseArquivoEnum.ENADE.getValue());
				mapaConvocacaoEnadeVO.getArquivoAlunoExcel().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ENADE);
				getFacadeFactory().getArquivoFacade().incluir(mapaConvocacaoEnadeVO.getArquivoAlunoExcel(), usuario, configuracaoGeralSistema);
			}
			if(mapaConvocacaoEnadeVO.getIngressantes()) {
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setNome(nomeArquivo);
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setOrigem(OrigemArquivo.MATRICULA_ENADE.getValor());
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setCodOrigem(mapaConvocacaoEnadeVO.getCodigo());
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setPastaBaseArquivo(PastaBaseArquivoEnum.ENADE.getValue());
				mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ENADE);
				getFacadeFactory().getArquivoFacade().incluir(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante(), usuario, configuracaoGeralSistema);
			}
			else if(mapaConvocacaoEnadeVO.getConcluintes()) {
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setNome(nomeArquivo);
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setOrigem(OrigemArquivo.MATRICULA_ENADE.getValor());
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setCodOrigem(mapaConvocacaoEnadeVO.getCodigo());
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setPastaBaseArquivo(PastaBaseArquivoEnum.ENADE.getValue());
				mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ENADE);
				getFacadeFactory().getArquivoFacade().incluir(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte(), usuario, configuracaoGeralSistema);
			}
			
		} catch (Exception e) {
			ArquivoVO arqExcluir = new ArquivoVO();
			arqExcluir.setNome(nomeArquivo);
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir, caminhoPasta + File.separator + PastaBaseArquivoEnum.ENADE.getValue());
			arqExcluir.setNome(nomeArquivo.replace(".txt", ".xlsx"));
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir, caminhoPasta + File.separator + PastaBaseArquivoEnum.ENADE.getValue());
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoArquivoMapaConvocacaoEnade(final MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario,	ConfiguracaoGeralSistemaVO configuracaoGeralSistema, EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception {
		try {
		if (mapaConvocacaoEnadeVO.getIngressantes()){
			executarGerarArquivo(mapaConvocacaoEnadeVO, configuracaoGeralSistema.getLocalUploadArquivoTemp(),  usuario, configuracaoGeralSistema, enadeVO, usuarioVO);
			alterarArquivoTxt(mapaConvocacaoEnadeVO, mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(),  mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), usuario);
		}
		else if (mapaConvocacaoEnadeVO.getConcluintes()){
			executarGerarArquivo(mapaConvocacaoEnadeVO, configuracaoGeralSistema.getLocalUploadArquivoTemp(),  usuario, configuracaoGeralSistema, enadeVO,  usuarioVO);
			alterarArquivoTxt(mapaConvocacaoEnadeVO,mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(),  mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), usuario);
		}
		
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMapaConvocacaoEnadeArquivoAlunoTxt(final MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, UsuarioVO usuario, Integer arquivoalunoingressante,  Integer arquivoAlunoconcluinte,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) throws Exception {
		try {
			alterarArquivoTxt(mapaConvocacaoEnadeVO, arquivoalunoingressante,  arquivoAlunoconcluinte, usuario);
		} catch (Exception e) {
			mapaConvocacaoEnadeVO.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaVO> consultarAlunosMapaConvocacaoEnadeMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, Integer curso, UsuarioVO usuarioVO) throws Exception {
	MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO = getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().consultaPorMapaConvocacaoEnade(mapaConvocacaoEnadeVO, usuarioVO, UteisData.getAnoDataString(mapaConvocacaoEnadeVO.getDataAbertura()));
	return getFacadeFactory().getMapaConvocacaoEnadeMatriculaFacade().consultaRapidaMapaconvocacaoEnadeMatricula(unidadeEnsinoVOs, mapaConvocacaoEnadeMatriculaVO, curso, SituacaoVinculoMatricula.ATIVA.getValor(), usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarArquivoTxt(final MapaConvocacaoEnadeVO obj, Integer arquivoalunoingressante,  Integer arquivoAlunoconcluinte,  UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE MapaConvocacaoEnade set arquivoalunoingressante= ?, arquivoAlunoconcluinte= ? WHERE (codigo= ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(arquivoalunoingressante)) {
						sqlAlterar.setInt(1, arquivoalunoingressante);
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(arquivoAlunoconcluinte)) {
						sqlAlterar.setInt(2, arquivoAlunoconcluinte);
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
//	@Override
	public MapaConvocacaoEnadeVO consultarEnadeCursoParaTXT(String campoConsulta, Integer codigoEnade, Date dataInicial, Date dataFinal, Integer unidadeEnsino, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer codigoCurso) throws Exception {
		MapaConvocacaoEnadeVO objs = new MapaConvocacaoEnadeVO();
		if (campoConsulta.equals("enade")) {
			objs = consultaRapidaPorEnadeParaTXT(codigoEnade, unidadeEnsino, false, usuarioVO, NivelMontarDados.TODOS,  codigoCurso);
		}
		
		return objs;
	}
	
//consulta responsavel por buscar os dados do enade, e dos arquivos ingressantes e concluintes
	public MapaConvocacaoEnadeVO consultaRapidaPorEnadeParaTXT(Integer codigoEnade, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer codigoCurso) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE enade.codigo = ").append(codigoEnade);
		sqlStr.append(" and curso.codigo = ").append(codigoCurso);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaParaTXT(tabelaResultado, usuarioVO);
	}
	
	public MapaConvocacaoEnadeVO montarDadosConsultaRapidaParaTXT(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		MapaConvocacaoEnadeVO vetResultado = new MapaConvocacaoEnadeVO();
		while (tabelaResultado.next()) {
			montarDadosCompleto(vetResultado, tabelaResultado, usuarioVO, true);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}
	
	public MapaConvocacaoEnadeVO consultaPorChavePrimariaDadosCompletos(Integer codMapaConvocacaEnade, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (mapaconvocacaoenade.codigo = ").append(codMapaConvocacaEnade).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return montarDadosConsultaRapidaParaTXT(tabelaResultado, usuario);
	}

	public List<MapaConvocacaoEnadeVO> consultaRapidaPorEnadeCurso(Integer enade, Integer enadeCurso, Integer mapaConvocacaoEnade,  boolean controlarAcesso, boolean validarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append("WHERE enade.codigo = ").append(enade);
		sqlStr.append(" and enadecurso.codigo = ").append(enadeCurso);
		if(!validarDados) {
			sqlStr.append(" and mapaconvocacaoenade.codigo = ").append(mapaConvocacaoEnade);
		}
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado, usuarioVO);
	}
	
	public List<MapaConvocacaoEnadeVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<MapaConvocacaoEnadeVO> vetResultado = new ArrayList<MapaConvocacaoEnadeVO>(0);
		while (tabelaResultado.next()) {
			MapaConvocacaoEnadeVO obj = new MapaConvocacaoEnadeVO();
			montarDadosCompleto(obj, tabelaResultado, usuarioVO, true);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}
	

	public void excluirArquivoTxt(MapaConvocacaoEnadeVO obj, MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, UsuarioVO usuario) throws Exception {
		try {
//			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(obj.getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario);
			if(obj.getIngressantes() && !obj.getConcluintes()) {
				getFacadeFactory().getMapaConvocacaoEnadeFacade().alterarMapaConvocacaoEnadeArquivoAlunoTxt(obj, usuario, null, obj.getArquivoAlunoConcluinte().getCodigo(), configuracaoGeralSistemaVO, mapaConvocacaoEnadeMatriculaVO);
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoAlunoIngressante(), usuario, configuracaoGeralSistemaVO);
				obj.setArquivoAlunoIngressante(null);
				if(!Uteis.isAtributoPreenchido(obj.getMapaConvocacaoEnadeVORelTxtIngressantes().getCodigoArquivoTxt())){
					obj.setIsMostrarDownloadArquivoIngressante(Boolean.FALSE);
					obj.getArquivoAlunoIngressante().setNome("");
				}
			}
			if(obj.getConcluintes() && !obj.getIngressantes()) {
				getFacadeFactory().getMapaConvocacaoEnadeFacade().alterarMapaConvocacaoEnadeArquivoAlunoTxt(obj,  usuario, obj.getArquivoAlunoIngressante().getCodigo(), null, configuracaoGeralSistemaVO, mapaConvocacaoEnadeMatriculaVO);
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoAlunoConcluinte(), usuario, configuracaoGeralSistemaVO);
				obj.setArquivoAlunoConcluinte(null);
				if(!Uteis.isAtributoPreenchido(obj.getMapaConvocacaoEnadeVORelTxtConcluintes().getCodigoArquivoTxt())){
					obj.setIsMostrarDownloadArquivoConcluinte(Boolean.FALSE);
					obj.getArquivoAlunoConcluinte().setNome("");
				}
			}
			if(obj.getIngressantes() && obj.getConcluintes()) {
				getFacadeFactory().getMapaConvocacaoEnadeFacade().alterarMapaConvocacaoEnadeArquivoAlunoTxt(obj,  usuario, null, null, configuracaoGeralSistemaVO, mapaConvocacaoEnadeMatriculaVO);
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoAlunoConcluinte(), usuario, configuracaoGeralSistemaVO);
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoAlunoIngressante(), usuario, configuracaoGeralSistemaVO);
				obj.setArquivoAlunoConcluinte(null);
				obj.setArquivoAlunoIngressante(null);
				if(!Uteis.isAtributoPreenchido(obj.getMapaConvocacaoEnadeVORelTxtConcluintes().getCodigoArquivoTxt())){
					obj.setIsMostrarDownloadArquivoConcluinte(Boolean.FALSE);
					obj.getArquivoAlunoConcluinte().setNome("");
				}
				if(!Uteis.isAtributoPreenchido(obj.getMapaConvocacaoEnadeVORelTxtIngressantes().getCodigoArquivoTxt())){
					obj.setIsMostrarDownloadArquivoIngressante(Boolean.FALSE);
					obj.getArquivoAlunoIngressante().setNome("");
				}
			}
		} catch (Exception e) {
			throw e;
		}	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao(List<MatriculaVO> listaMatriculaVOs, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception {
		if(mapaConvocacaoEnadeVO.getNovoObj() || (mapaConvocacaoEnadeVO.getSituacaoMapaConvocacaoEnade().equals(SituacaoMapaConvocacaoEnadeEnum.MAPA_EM_CONSTRUCAO) && !mapaConvocacaoEnadeVO.getImprimirPDF())) {
			executarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao(listaMatriculaVOs, mapaConvocacaoEnadeVO, configuracaoAcademicoVO, progressBar, usuarioVO);
		}
	}
}
