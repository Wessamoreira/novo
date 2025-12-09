package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaCobrancaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CartaCobrancaInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class CartaCobranca extends ControleAcesso implements CartaCobrancaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CartaCobranca() throws Exception {
		super();
		setIdEntidade("CartaCobranca");
	}

	public void setIdEntidade(String idEntidade) {
		CartaCobranca.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final CartaCobrancaVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle Comentado 23/10/2014 Não precisa ser
			 *         validaddo o UsuarioVO
			 */
			// CartaCobranca.incluir(getIdEntidade());
			final String sql = "INSERT INTO cartacobranca( aluno, matricula, curso, turma, usuario, unidadeEnsino, dataGeracao, dataInicioFiltro, dataFimFiltro, tipoOrigemInscricaoProcessoSeletivo, tipoOrigemMatricula, tipoOrigemRequerimento, "+
			" tipoOrigemBiblioteca, tipoOrigemMensalidade, tipoOrigemDevolucaoCheque, tipoOrigemNegociacao, tipoOrigemBolsaCusteadaConvenio, tipoOrigemContratoReceita, tipoOrigemInclusaoReposicao, tipoOrigemOutros, "+ 
			" ativo, preMatricula, preMatriculaCancelada, trancado, abandonado, transferenciaInterna, transferenciaExterna, cancelado, concluido, formado, centroReceitaApresentar) " + 
			" VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getAluno());
					if (obj.getMatricula().isEmpty()) {
						sqlInserir.setNull(2, 0);
					} else {
						sqlInserir.setString(2, obj.getMatricula());
					}
					if (obj.getCursoVO().getCodigo().intValue() > 0) {
						sqlInserir.setInt(3, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getTurmaVO().getCodigo());
					sqlInserir.setInt(5, obj.getUsuarioVO().getCodigo());
					sqlInserir.setInt(6, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataGeracao()));
					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataInicioFiltro()));
					sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataFimFiltro()));
					sqlInserir.setBoolean(10, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemInscricaoProcessoSeletivo());
					sqlInserir.setBoolean(11, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemMatricula());
					sqlInserir.setBoolean(12, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemRequerimento());
					sqlInserir.setBoolean(13, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemBiblioteca());
					sqlInserir.setBoolean(14, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemMensalidade());
					sqlInserir.setBoolean(15, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemDevolucaoCheque());
					sqlInserir.setBoolean(16, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemNegociacao());
					sqlInserir.setBoolean(17, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemBolsaCusteadaConvenio());
					sqlInserir.setBoolean(18, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemContratoReceita());
					sqlInserir.setBoolean(19, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemInclusaoReposicao());
					sqlInserir.setBoolean(20, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemOutros());
					sqlInserir.setBoolean(21, obj.getFiltroRelatorioAcademicoVO().getAtivo());
					sqlInserir.setBoolean(22, obj.getFiltroRelatorioAcademicoVO().getPreMatricula());
					sqlInserir.setBoolean(23, obj.getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
					sqlInserir.setBoolean(24, obj.getFiltroRelatorioAcademicoVO().getTrancado());
					sqlInserir.setBoolean(25, obj.getFiltroRelatorioAcademicoVO().getAbandonado());
					sqlInserir.setBoolean(26, obj.getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
					sqlInserir.setBoolean(27, obj.getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
					sqlInserir.setBoolean(28, obj.getFiltroRelatorioAcademicoVO().getCancelado());
					sqlInserir.setBoolean(29, obj.getFiltroRelatorioAcademicoVO().getConcluido());
					sqlInserir.setBoolean(30, obj.getFiltroRelatorioAcademicoVO().getFormado());
					sqlInserir.setString(31, obj.getCentroReceitaApresentar());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
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
			obj.setNovoObj(true);
			throw e;
		}
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT cartacobranca.* from cartacobranca ");
        return sqlStr;
    }
	
	public List<CartaCobrancaVO> consultarPorUsuario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN usuario ON usuario.codigo = cartacobranca.usuario ");
		sqlStr.append(" WHERE upper(sem_acentos(usuario.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<CartaCobrancaVO> consultarPorMatricula(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE cartacobranca.matricula = '").append(valorConsulta.toUpperCase()).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<CartaCobrancaVO> consultarPorAluno(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper(sem_acentos(cartacobranca.aluno)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<CartaCobrancaVO> consultarPorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN turma ON turma.codigo = cartacobranca.turma ");
		sqlStr.append(" WHERE upper(sem_acentos(turma.identificadorTurma)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<CartaCobrancaVO> consultarPorCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN curso ON curso.codigo = cartacobranca.curso ");
		sqlStr.append(" WHERE upper(sem_acentos(curso.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<CartaCobrancaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = cartacobranca.unidadeensino ");
		sqlStr.append(" WHERE upper(sem_acentos(unidadeensino.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	@Override
	public List<CartaCobrancaVO> consultarPorDataGeracao(Date dataGeracaoInicio, Date dataGeracaoFim, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	public static List<CartaCobrancaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CartaCobrancaVO> vetResultado = new ArrayList<CartaCobrancaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return vetResultado;
	}

	public static CartaCobrancaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CartaCobrancaVO obj = new CartaCobrancaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setAluno(dadosSQL.getString("aluno"));
		obj.setmatricula(dadosSQL.getString("matricula"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		montarDadosCurso(obj, nivelMontarDados, usuario);
		montarDadosTurma(obj, nivelMontarDados, usuario);
		montarDadosUsuario(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
		obj.setDataInicioFiltro(dadosSQL.getDate("dataInicioFiltro"));
		obj.setDataFimFiltro(dadosSQL.getDate("dataFimFiltro"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(dadosSQL.getBoolean("tipoOrigemInscricaoProcessoSeletivo"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(dadosSQL.getBoolean("tipoOrigemMatricula"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(dadosSQL.getBoolean("tipoOrigemRequerimento"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(dadosSQL.getBoolean("tipoOrigemBiblioteca"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(dadosSQL.getBoolean("tipoOrigemMensalidade"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(dadosSQL.getBoolean("tipoOrigemDevolucaoCheque"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(dadosSQL.getBoolean("tipoOrigemNegociacao"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(dadosSQL.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(dadosSQL.getBoolean("tipoOrigemContratoReceita"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(dadosSQL.getBoolean("tipoOrigemInclusaoReposicao"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(dadosSQL.getBoolean("tipoOrigemOutros"));
		obj.getFiltroRelatorioAcademicoVO().setAtivo(dadosSQL.getBoolean("ativo"));
		obj.getFiltroRelatorioAcademicoVO().setPreMatricula(dadosSQL.getBoolean("preMatricula"));
		obj.getFiltroRelatorioAcademicoVO().setPreMatriculaCancelada(dadosSQL.getBoolean("preMatriculaCancelada"));
		obj.getFiltroRelatorioAcademicoVO().setTrancado(dadosSQL.getBoolean("trancado"));
		obj.getFiltroRelatorioAcademicoVO().setAbandonado(dadosSQL.getBoolean("abandonado"));
		obj.getFiltroRelatorioAcademicoVO().setTransferenciaInterna(dadosSQL.getBoolean("transferenciaInterna"));
		obj.getFiltroRelatorioAcademicoVO().setTransferenciaExterna(dadosSQL.getBoolean("transferenciaExterna"));
		obj.getFiltroRelatorioAcademicoVO().setCancelado(dadosSQL.getBoolean("cancelado"));
		obj.getFiltroRelatorioAcademicoVO().setConcluido(dadosSQL.getBoolean("concluido"));
		obj.getFiltroRelatorioAcademicoVO().setFormado(dadosSQL.getBoolean("formado"));
		obj.setCentroReceitaApresentar(dadosSQL.getString("centroReceitaApresentar"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}
	
	public static void montarDadosCurso(CartaCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoVO().getCodigo().intValue() == 0) {
			obj.setCursoVO(new CursoVO());
			return;
		}
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
	}
	
	public static void montarDadosTurma(CartaCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurmaVO().getCodigo().intValue() == 0) {
			obj.setTurmaVO(new TurmaVO());
			return;
		}
		obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	public static void montarDadosUsuario(CartaCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUsuarioVO().getCodigo().equals(0)) {
			obj.setUsuarioVO(new UsuarioVO());
			return;
		}
		obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	public static void montarDadosUnidadeEnsino(CartaCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
}
