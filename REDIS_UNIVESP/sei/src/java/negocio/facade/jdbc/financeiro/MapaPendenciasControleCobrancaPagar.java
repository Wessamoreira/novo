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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.MapaPendenciasControleCobrancaPagarInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaPendenciasControleCobrancaPagar extends ControleAcesso implements MapaPendenciasControleCobrancaPagarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -720609076716428531L;
	protected static String idEntidade;

	public MapaPendenciasControleCobrancaPagar() throws Exception {
		super();
		setIdEntidade("MapaPendenciasControleCobrancaPagar");
	}

	private void validarDados(MapaPendenciasControleCobrancaPagarVO mapaPendenciasControleCobrancaPagarVO) throws Exception {
		if (mapaPendenciasControleCobrancaPagarVO.getContaPagarVO().getCodigo().equals(0)) {
			throw new ConsistirException("Informe a Conta Pagar.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaPendenciasControleCobrancaPagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO mapaPendenciasControleCobrancaPagar (matricula, contaPagar, controleCobrancaPagar, valorDiferenca, selecionado, dataPagamento, dataProcessamento) VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (!obj.getMatriculaVO().getMatricula().equals("")) {
						sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setInt(2, obj.getContaPagarVO().getCodigo());
					sqlInserir.setInt(3, obj.getControleCobrancaPagarVO().getCodigo());
					sqlInserir.setDouble(4, obj.getValorDiferenca());
					sqlInserir.setBoolean(5, obj.getSelecionado());
					sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataPagamento()));
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataProcessamento()));
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
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MapaPendenciasControleCobrancaPagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			MapaPendenciasControleCobrancaPagar.alterar(getIdEntidade());
			final String sql = "UPDATE MapaPendenciasControleCobrancaPagar set matricula=?, contaPagar=?, controleCobrancaPagar=?, selecionado=?, dataPagamento=?, dataProcessamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getMatriculaVO().getMatricula());
					sqlAlterar.setInt(2, obj.getContaPagarVO().getCodigo());
					sqlAlterar.setInt(3, obj.getControleCobrancaPagarVO().getCodigo());
					sqlAlterar.setBoolean(4, obj.getSelecionado());
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataPagamento()));
					sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataProcessamento()));
					sqlAlterar.setInt(7, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MapaPendenciasControleCobrancaPagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM MapaPendenciasControleCobrancaPagar WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorContaPagar(ContaPagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM MapaPendenciasControleCobrancaPagar WHERE ((contaPagar = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSelecionado(Integer codigo, Boolean selecionado, UsuarioVO usuario) throws Exception {
		try {
			String sql = "UPDATE MapaPendenciasControleCobrancaPagar SET selecionado = ? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { selecionado, codigo });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public void alterarSelecionadoPorControleCobrancaPagar(Integer controleCobrancaPagar, Boolean selecionado, UsuarioVO usuario) throws Exception {
		 try {
	          String sql = "UPDATE MapaPendenciasControleCobrancaPagar SET selecionado = ? WHERE controleCobrancaPagar = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
	          getConexao().getJdbcTemplate().update(sql, new Object[]{selecionado, controleCobrancaPagar});
	     } catch (Exception e) {
	    	 throw e;
	     }
	}

	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT * FROM MapaPendenciasControleCobrancaPagar WHERE UPPER(matricula) = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toUpperCase() });
		try {
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		} finally {
			sqlStr = null;
		}
	}

	/*public List<MapaPendenciasControleCobrancaPagarVO> consultarPorListaContaPagar(List<ContaPagarVO> contaPagarVOs, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contareceber, cr.situacao, cr.valor, cr.matriculaperiodo, ");
		sqlStr.append("cr.valorrecebido, cr.valordescontorecebido, cr.valorDescontoAlunoJaCalculado, cr.descontoConvenio, cr.descontoInstituicao, cr.valorDescontoProgressivo, ");
		sqlStr.append("cr.datavencimento, cr.tipopessoa, p.codigo as \"p.codigo\", p.nome, cr.parcela, cr.parceiro, parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
		sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
		sqlStr.append("cr.codorigem, m.unidadeensino, mpcc.selecionado, ");
		sqlStr.append("resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
		sqlStr.append("FROM MapaPendenciasControleCobrancaPagar mpcc ");
		sqlStr.append("LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
		sqlStr.append("LEFT JOIN pessoa p ON m.aluno = p.codigo ");
		sqlStr.append("LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
		sqlStr.append("LEFT JOIN parceiro parc ON parc.codigo = cr.parceiro ");
		sqlStr.append("LEFT JOIN pessoa resp ON cr.responsavelFinanceiro = resp.codigo ");
		sqlStr.append("WHERE 1 = 1 ");
		if (!contaPagarVOs.isEmpty()) {
			sqlStr.append("AND contareceber in(");
			for (ContaPagarVO contaPagarVO : contaPagarVOs) {
				if (contaPagarVO.getCodigo() != 0) {
					sqlStr.append(contaPagarVO.getCodigo());
					if (!contaPagarVO.getCodigo().equals(contaPagarVOs.get(contaPagarVOs.size() - 1).getCodigo())) {
						sqlStr.append(", ");
					}
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY p.nome, parc.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		try {
			return montarDadosConsultaTelaControleCobranca(tabelaResultado);
		} finally {
			sqlStr = null;
		}
	}*/

	@Override
	public List<MapaPendenciasControleCobrancaPagarVO> consultarPorControleCobrancaPagarSelecionado(Integer controleCobranca, Integer qtde, Integer inicio, Boolean selecionado, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contapagar, ");
		sqlStr.append(" cp.situacao, cp.valor, cp.valorpago, cp.datavencimento, cp.tiposacado, cp.parcela, ");
		sqlStr.append(" p.codigo as \"p.codigo\", p.nome, p.aluno, ");
		sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
		sqlStr.append(" cp.codorigem, m.unidadeensino, mpcc.selecionado, ");
		sqlStr.append(" parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
		sqlStr.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
		sqlStr.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
		sqlStr.append(" FROM MapaPendenciasControleCobrancaPagar mpcc ");
		sqlStr.append(" LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
		sqlStr.append(" LEFT JOIN pessoa p ON m.aluno = p.codigo ");
		sqlStr.append(" LEFT JOIN contapagar cp ON mpcc.contapagar = cp.codigo ");
		sqlStr.append(" LEFT JOIN pessoa resp ON cp.responsavelFinanceiro = resp.codigo ");
		sqlStr.append(" LEFT JOIN parceiro parc ON parc.codigo = cp.parceiro ");
		sqlStr.append(" LEFT JOIN fornecedor ON fornecedor.codigo = cp.fornecedor ");

		sqlStr.append("WHERE mpcc.controleCobrancaPagar = ").append(controleCobranca);
		if (selecionado != null) {
			sqlStr.append(" AND mpcc.selecionado = ").append(selecionado).append(" ");
		}
		sqlStr.append(" ORDER BY p.nome, parc.nome ");
		if (qtde != null) {
			sqlStr.append(" LIMIT ").append(qtde);
			if (inicio != null) {
				sqlStr.append(" OFFSET ").append(inicio);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		try {
			return montarDadosConsultaTelaControleCobranca(tabelaResultado);
		} finally {
			sqlStr = null;
		}
	}

	@Override
	public Integer consultarQtdeMapaPendenciaPorControleCobranca(Integer controleCobranca, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT count(distinct mpcc.codigo) as qtde ");
		sqlStr.append("FROM MapaPendenciasControleCobrancaPagar mpcc ");
		sqlStr.append("WHERE  mpcc.controleCobrancaPagar = ").append(controleCobranca);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public static List<MapaPendenciasControleCobrancaPagarVO> montarDadosConsultaTelaControleCobranca(SqlRowSet tabelaResultado) throws Exception {
		List<MapaPendenciasControleCobrancaPagarVO> vetResultado = new ArrayList<MapaPendenciasControleCobrancaPagarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosTelaControleCobranca(tabelaResultado));
		}
		return vetResultado;
	}

	public static MapaPendenciasControleCobrancaPagarVO montarDadosTelaControleCobranca(SqlRowSet dadosSQL) throws Exception {
		MapaPendenciasControleCobrancaPagarVO obj = new MapaPendenciasControleCobrancaPagarVO();

		obj.setDataPagamento(dadosSQL.getDate("dataPagamento"));
		obj.setDataProcessamento(dadosSQL.getDate("dataProcessamento"));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setValorDiferenca(dadosSQL.getDouble("valorDiferenca"));
		obj.setSelecionado(dadosSQL.getBoolean("selecionado"));

		obj.getContaPagarVO().setCodigo(dadosSQL.getInt("contaPagar"));
		obj.getContaPagarVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getContaPagarVO().setDataVencimento(dadosSQL.getTimestamp("datavencimento"));
		obj.getContaPagarVO().setSituacao(dadosSQL.getString("situacao"));
		obj.getContaPagarVO().setValor(dadosSQL.getDouble("valor"));
		obj.getContaPagarVO().setValorPago(dadosSQL.getDouble("valorpago"));
		obj.getContaPagarVO().setCodOrigem(dadosSQL.getString("codorigem"));
		obj.getContaPagarVO().setParcela(dadosSQL.getString("parcela"));
		obj.getContaPagarVO().setTipoSacado(dadosSQL.getString("tiposacado"));

		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));

		if (obj.getContaPagarVO().isTipoSacadoParceiro()) {
			obj.getContaPagarVO().getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
			obj.getContaPagarVO().getParceiro().setNome(dadosSQL.getString("parc.nome"));
			obj.getContaPagarVO().getParceiro().setCPF(dadosSQL.getString("parc.cpf"));
			obj.getContaPagarVO().getParceiro().setCNPJ(dadosSQL.getString("parc.cnpj"));
		} else if (obj.getContaPagarVO().isTipoSacadoFornecedor()) {
			obj.getContaPagarVO().getFornecedor().setCodigo(dadosSQL.getInt("for.codigo"));
			obj.getContaPagarVO().getFornecedor().setNome(dadosSQL.getString("for.nome"));
			obj.getContaPagarVO().getFornecedor().setCPF(dadosSQL.getString("for.cpf"));
			obj.getContaPagarVO().getFornecedor().setCNPJ(dadosSQL.getString("for.cnpj"));
			obj.getContaPagarVO().getFornecedor().setTipoEmpresa(dadosSQL.getString("for.tipoEmpresa"));
		} else if (obj.getContaPagarVO().isTipoSacadoAluno()) {
			// DADOS DO RESPONSÁVEL FINANCEIRO
			obj.getContaPagarVO().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("resp.codigo"));
			obj.getContaPagarVO().getResponsavelFinanceiro().setNome(dadosSQL.getString("resp.nome"));
			obj.getContaPagarVO().getResponsavelFinanceiro().setCPF(dadosSQL.getString("resp.cpf"));
			// DADOS DO ALUNO
			obj.getContaPagarVO().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
			obj.getContaPagarVO().getPessoa().setNome(dadosSQL.getString("nome"));
			obj.getContaPagarVO().getPessoa().setAluno(dadosSQL.getBoolean("aluno"));
			obj.getContaPagarVO().getPessoa().setFuncionario(dadosSQL.getBoolean("funcionario"));
			obj.getContaPagarVO().getPessoa().setProfessor(dadosSQL.getBoolean("professor"));
			obj.getContaPagarVO().getPessoa().setCandidato(dadosSQL.getBoolean("candidato"));
			obj.getContaPagarVO().getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiacessovisaopais"));
			obj.getContaPagarVO().getPessoa().setMembroComunidade(dadosSQL.getBoolean("membrocomunidade"));
			obj.getContaPagarVO().getPessoa().setRequisitante(dadosSQL.getBoolean("requisitante"));
		} else {
			obj.getContaPagarVO().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
			obj.getContaPagarVO().getPessoa().setNome(dadosSQL.getString("nome"));
			obj.getContaPagarVO().getPessoa().setAluno(dadosSQL.getBoolean("aluno"));
			obj.getContaPagarVO().getPessoa().setFuncionario(dadosSQL.getBoolean("funcionario"));
			obj.getContaPagarVO().getPessoa().setProfessor(dadosSQL.getBoolean("professor"));
			obj.getContaPagarVO().getPessoa().setCandidato(dadosSQL.getBoolean("candidato"));
			obj.getContaPagarVO().getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiacessovisaopais"));
			obj.getContaPagarVO().getPessoa().setMembroComunidade(dadosSQL.getBoolean("membrocomunidade"));
			obj.getContaPagarVO().getPessoa().setRequisitante(dadosSQL.getBoolean("requisitante"));
		}

		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public List consultarPorContaPagar(Integer codigoContaPagar, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MapaPendenciasControleCobrancaPagar WHERE contaPagar = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoContaPagar.intValue() });
		try {
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		} finally {
			sqlStr = null;
		}
	}

	public List consultarPorCodigo(Integer codigoMapa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MapaPendenciasControleCobrancaPagar WHERE codigo >= ? ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoMapa.intValue() });
		try {
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		} finally {
			sqlStr = null;
		}
	}

	public List<MapaPendenciasControleCobrancaPagarVO> consultarTodasPendencias(String matricula, String nome, Boolean todoPeriodo, Date dataInicio, Date dataFim, String ano, String semestre, String campoConsultaPeriodo, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contareceber, cr.situacao, cr.valor, cr.matriculaperiodo, ");
		sqlStr.append("cr.valorrecebido, cr.valordescontorecebido, cr.valorDescontoAlunoJaCalculado, cr.descontoConvenio, cr.descontoInstituicao, cr.valorDescontoProgressivo, ");
		sqlStr.append("cr.datavencimento, cr.tipopessoa, p.codigo as \"p.codigo\", p.nome, p.aluno, cr.parcela, cr.parceiro, parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
		sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
		sqlStr.append("cr.codorigem, m.unidadeensino, mpcc.selecionado, ");
		sqlStr.append("contacorrente.numero as contacorrente_numero, agencia.numeroagencia as agencia_numero, agencia.digito as agencia_digito, banco.nome as banco_nome, ");
		sqlStr.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
		sqlStr.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
		sqlStr.append("FROM MapaPendenciasControleCobrancaPagar mpcc ");
		sqlStr.append("LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
		sqlStr.append("LEFT JOIN pessoa p ON m.aluno = p.codigo ");
		sqlStr.append("LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
		sqlStr.append("LEFT JOIN contacorrente ON contacorrente.codigo = cr.contacorrente ");
		sqlStr.append("LEFT JOIN agencia ON contacorrente.agencia = agencia.codigo ");
		sqlStr.append("LEFT JOIN banco ON agencia.banco = banco.codigo ");
		sqlStr.append("LEFT JOIN parceiro parc ON parc.codigo = cr.parceiro ");
		sqlStr.append("LEFT JOIN fornecedor ON fornecedor.codigo = cr.fornecedor ");
		sqlStr.append("LEFT JOIN matriculaPeriodo ON matriculaPeriodo.codigo = cr.matriculaPeriodo ");
		sqlStr.append("LEFT JOIN pessoa resp ON cr.responsavelFinanceiro = resp.codigo ");
		sqlStr.append("WHERE 1=1 ");
		if (unidadeEnsino != 0) {
			sqlStr.append("AND m.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (!matricula.equals("")) {
			sqlStr.append(" AND TRIM(m.matricula) ilike('").append(matricula.trim()).append("%') ");
		}
		if (!nome.equals("")) {
			sqlStr.append(" AND TRIM(p.nome) ilike('").append(nome.trim()).append("%') ");
		}
		if (!todoPeriodo) {
			if (campoConsultaPeriodo.equals("ANO_SEMESTRE")) {
				if (!ano.equals("")) {
					sqlStr.append(" AND ano = '").append(ano).append("' ");
				}
				if (!semestre.equals("")) {
					sqlStr.append(" AND semestre = '").append(semestre).append("' ");
				}
			}
			if (campoConsultaPeriodo.equals("DATA_VENCIMENTO")) {
				sqlStr.append(" AND datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
				sqlStr.append(" AND datavencimento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			}
			if (campoConsultaPeriodo.equals("DATA_PROCESSAMENTO")) {
				sqlStr.append(" AND dataprocessamento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
				sqlStr.append(" AND dataprocessamento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			}
			if (campoConsultaPeriodo.equals("DATA_PAGAMENTO")) {
				sqlStr.append(" AND datapagamento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
				sqlStr.append(" AND datapagamento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		try {
			List<MapaPendenciasControleCobrancaPagarVO> vetResultado = new ArrayList<MapaPendenciasControleCobrancaPagarVO>(0);
			while (tabelaResultado.next()) {
				MapaPendenciasControleCobrancaPagarVO mapaPendenciasControleCobrancaPagarVO = montarDadosTelaControleCobranca(tabelaResultado);
				vetResultado.add(mapaPendenciasControleCobrancaPagarVO);
			}
			return vetResultado;
		} finally {
			sqlStr = null;
		}
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		}
		return vetResultado;
	}

	public static MapaPendenciasControleCobrancaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MapaPendenciasControleCobrancaPagarVO obj = new MapaPendenciasControleCobrancaPagarVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getContaPagarVO().setCodigo(dadosSQL.getInt("contaPagar"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosContaPagar(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
		return obj;
	}

	public static void montarDadosMatricula(MapaPendenciasControleCobrancaPagarVO obj, Integer nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!obj.getMatriculaVO().getMatricula().equals("")) {
			obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaVO().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
		}
	}

	public static void montarDadosContaPagar(MapaPendenciasControleCobrancaPagarVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (obj.getContaPagarVO().getCodigo().intValue() == 0) {
			obj.setContaPagarVO(new ContaPagarVO());
			return;
		}
		obj.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagarVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public MapaPendenciasControleCobrancaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM MapaPendenciasControleCobrancaPagar WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( MapaPendenciasControleCobrancaPagar ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public static String getIdEntidade() {
		return MapaPendenciasControleCobrancaPagar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MapaPendenciasControleCobrancaPagar.idEntidade = idEntidade;
	}

}
