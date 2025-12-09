package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
//import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.DepartamentoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Departamento extends ControleAcesso implements DepartamentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3481908967940445889L;
	protected static String idEntidade = "Departamento";

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			DepartamentoVO.validarDados(obj);
incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO Departamento( nome, departamentoSuperior, responsavel, unidadeEnsino, faleConosco, codigocontabil, nomecontabil, nivelContabil, controlaEstoque ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					if (obj.getDepartamentoSuperior().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getDepartamentoSuperior().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setBoolean(5, obj.getFaleConosco());
					sqlInserir.setString(6, obj.getCodigoContabil());
					sqlInserir.setString(7, obj.getNomeContabil());
					sqlInserir.setString(8, obj.getNivelContabil());
					int i = 9;
					Uteis.setValuePreparedStatement(obj.isControlaEstoque(), i++, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			validarCentroResultadoExistentePorDepartamento(obj, usuarioVO);
			atualizarDepartamentoCentroResultado(obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			DepartamentoVO.validarDados(obj);
			alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE Departamento set nome=?, departamentoSuperior=?, responsavel=?, unidadeEnsino=?, faleConosco=?, codigoContabil=?, nomeContabil=?, nivelContabil=?, controlaEstoque=?, configuracaoLdap=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					if (obj.getDepartamentoSuperior().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getDepartamentoSuperior().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setBoolean(5, obj.getFaleConosco());
					sqlAlterar.setString(6, obj.getCodigoContabil());
					sqlAlterar.setString(7, obj.getNomeContabil());
					sqlAlterar.setString(8, obj.getNivelContabil());
					int i = 9;
					Uteis.setValuePreparedStatement(obj.isControlaEstoque(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getConfiguracaoLdapVO(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), i++, sqlAlterar);
					return sqlAlterar;
				}
			});
			atualizarDepartamentoCentroResultado(obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarCentroResultadoExistentePorDepartamento(DepartamentoVO departamento, UsuarioVO usuarioVO) {
//		if (!Uteis.isAtributoPreenchido(departamento.getCentroResultadoVO())) {
//			CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(departamento.getNome(), departamento.getUnidadeEnsino().getCodigo(), null, null, false, usuarioVO);
//			departamento.setCentroResultadoVO(obj);
//		}

	}

	private void atualizarDepartamentoCentroResultado(DepartamentoVO obj, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" UPDATE Departamento SET ");
//			if (Uteis.isAtributoPreenchido(obj.getCentroResultadoVO())) {
//				sqlStr.append(" centroResultado = ").append(obj.getCentroResultadoVO().getCodigo());
//			} else {
//				sqlStr.append(" centroResultado = null ");
//			}
			sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append(" ");
			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM Departamento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<DepartamentoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Departamento.* FROM Departamento, Pessoa WHERE Departamento.responsavel = Pessoa.codigo and lower (sem_acentos(Pessoa.nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<DepartamentoVO> consultarPorNomeFaleConosco(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') AND faleConosco = 'true' ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE lower (sem_acentos(nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorNomePorUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Departamento.* FROM Departamento, UnidadeEnsino WHERE ((Departamento.unidadeEnsino = UnidadeEnsino.codigo) or (Departamento.unidadeEnsino is null) or (Departamento.unidadeEnsino = 0))");
		sqlStr.append(" and lower (sem_acentos(Departamento.nome)) ilike(sem_acentos('").append(valorConsulta.toLowerCase()).append("%')) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY Departamento.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Departamento.* FROM Departamento, UnidadeEnsino WHERE Departamento.unidadeEnsino =UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Departamento.* FROM Departamento, UnidadeEnsino WHERE ((Departamento.unidadeEnsino = UnidadeEnsino.codigo) or (Departamento.unidadeEnsino is null) or (Departamento.unidadeEnsino = 0)) ");
		if (!valorConsulta.equals(0) && valorConsulta != null) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(valorConsulta);
		}
		sqlStr.append(" ORDER BY Departamento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorCodigoUnidadeEnsinoESemUE(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct (Departamento.*) FROM Departamento " + " WHERE (Departamento.unidadeEnsino = " + valorConsulta.intValue() + " " + "OR departamento.unidadeEnsino is null or departamento.unidadeEnsino = 0)";
		sqlStr += " ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public DepartamentoVO consultarPorCodigoFuncionario(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Departamento.* FROM Departamento ");
		sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append(" and funcionariocargo.funcionario = ").append(valorConsulta);
		if (!unidadeEnsino.equals(0) && unidadeEnsino != null) {
			sqlStr.append(" and funcionariocargo.UnidadeEnsino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new DepartamentoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<DepartamentoVO> consultarPorCodigoPessoaFuncionario(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct departamento.* FROM departamento ");
		sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo  and funcionariocargo.ativo = true ");
		sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where pessoa.codigo = ").append(codigoPessoa);
		sqlStr.append(" and  pessoa.ativo = true ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and funcionariocargo.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorCodigoPorUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Departamento.* FROM Departamento, UnidadeEnsino WHERE ((Departamento.unidadeEnsino = UnidadeEnsino.codigo) or (Departamento.unidadeEnsino is null) or (Departamento.unidadeEnsino = 0))");
		sqlStr.append(" and Departamento.codigo = ").append(valorConsulta);
		if (!unidadeEnsino.equals(0) && unidadeEnsino != null) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorDepartamentoSuperior(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE Departamento.departamentosuperior = " + valorConsulta.intValue() + " ORDER BY Departamento.departamentosuperior";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorDiferenteDepartamentoSuperior(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE (departamentoSuperior is null or departamentoSuperior <> ?) ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.intValue());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorGerenteDpto(Integer codigoGerente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM Departamento WHERE ((responsavel =  ?) ");
		sqlStr.append(" or exists (select funcionariocargo.codigo from funcionariocargo inner join funcionario on  funcionario.codigo = funcionariocargo.funcionario ");
		sqlStr.append(" inner join cargo on  cargo.codigo = funcionariocargo.cargo ");
		sqlStr.append(" where ((funcionariocargo.departamento is not null and funcionariocargo.departamento = departamento.codigo) ");
		sqlStr.append(" or (funcionariocargo.departamento is null and cargo.departamento = departamento.codigo)) and funcionario.pessoa = ? and funcionariocargo.gerente and funcionariocargo.ativo )) ORDER BY Departamento.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoGerente.intValue(), codigoGerente.intValue());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorNivelZero(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Departamento WHERE (departamentoSuperior is null) order by Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<DepartamentoVO> consultarPorNivelUm(Integer codigoDepartamentoPai, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT D1.* FROM Departamento as D1, Departamento as D2" + " WHERE (D1.departamentoSuperior = D2.codigo)" + " and (D2.departamentoSuperior is null or D2.departamentoSuperior = 0)" + " and (D1.departamentoSuperior = ?)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, codigoDepartamentoPai);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public  List<DepartamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DepartamentoVO> vetResultado = new ArrayList<DepartamentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public  DepartamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DepartamentoVO obj = new DepartamentoVO();
		obj.setNovoObj(false);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {

			return obj;
		}

		obj.setCodigoContabil(dadosSQL.getString("codigocontabil"));
		obj.setNomeContabil(dadosSQL.getString("nomecontabil"));
		obj.setNivelContabil(dadosSQL.getString("nivelContabil"));
		obj.setControlaEstoque(dadosSQL.getBoolean("controlaEstoque"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.setFaleConosco(dadosSQL.getBoolean("faleConosco"));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		obj.getDepartamentoSuperior().setCodigo((dadosSQL.getInt("departamentoSuperior")));
//		obj.setCentroResultadoVO(Uteis.montarDadosVO(dadosSQL.getInt("centroResultado"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		obj.setConfiguracaoLdapVO(Uteis.montarDadosVO(dadosSQL.getInt("configuracaoLdap"), ConfiguracaoLdapVO.class, p -> getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosDepartamentoSuperior(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setNovoObj(Boolean.FALSE);

		return obj;
	}

	public  void montarDadosResponsavel(DepartamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new PessoaVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavel().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosUnidadeEnsino(DepartamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosDepartamentoSuperior(DepartamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDepartamentoSuperior().getCodigo().intValue() == 0) {
			obj.setDepartamentoSuperior(new DepartamentoVO());
			return;
		}
		obj.setDepartamentoSuperior(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoSuperior().getCodigo(), true, nivelMontarDados, usuario));
	}

	public DepartamentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Departamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return Departamento.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Departamento.idEntidade = idEntidade;
	}

	@Override
	public DepartamentoVO consultarDepartamentoControlaEstoquePorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select * from ( ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" select departamento.*, 1 as ordem from departamento  ");
			sqlStr.append(" where controlaestoque = true ");
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
			sqlStr.append(" union all ");
		}
		sqlStr.append(" select departamento.* , 2 as ordem from departamento ");
		sqlStr.append(" where  controlaestoque = true and unidadeensino is null ");
		sqlStr.append(" ) as t order by ordem limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new DepartamentoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public DepartamentoVO consultarDepartamentoControlaEstoquePorCentroResultado(Integer centroResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select * from departamento ");
		sqlStr.append(" where controlaestoque = true ");
		sqlStr.append(" and centroresultado = ").append(centroResultado);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new DepartamentoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public DepartamentoVO consultarDepartamentoPorContratoReceitas(Integer contratoReceitas, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select departamento.* from departamento ");
		sqlStr.append(" inner join  contratosreceitas   on contratosreceitas.departamento = departamento.codigo ");
		sqlStr.append(" where contratosreceitas.codigo =  ").append(contratoReceitas);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new DepartamentoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public boolean consultarSePessoaTrabalhaNoDepartamento(Integer codigoPessoa, Integer departamento, Integer centroResultado) {
		StringBuilder sqlStr = new StringBuilder("SELECT count(1) as qtd FROM departamento ");
		sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where pessoa.codigo = ? ");
		sqlStr.append(" and departamento.codigo = ? ");
		sqlStr.append(" and departamento.centroResultado = ?");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPessoa, departamento, centroResultado);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	public boolean consultarSeGerenteTrabalhaNoDepartamento(Integer codigoPessoa, Integer departamento, Integer centroResultado) {
		StringBuilder sqlStr = new StringBuilder("SELECT count(1) as qtd FROM departamento ");
		sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where pessoa.codigo = ? ");
		sqlStr.append(" and departamento.codigo = ? ");
		sqlStr.append(" and departamento.centroResultado = ?");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		sqlStr.append(" and funcionariocargo.gerente = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPessoa, departamento, centroResultado);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	public List<DepartamentoVO> consultarDepartamentoPorDepartamentoTramiteExistente(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct departamento.* from departamentotramite  ");
		sql.append(" inner join departamento on departamento.codigo = departamentotramite.departamento ");
		sql.append(" ORDER BY Departamento.nome");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuario);
	}

	@Override
	public List<DepartamentoVO> consultarDepartamentoContaPagar(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM Departamento ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" WHERE unidadeEnsino is null or unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(" ORDER BY Departamento.nome");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuario);
	}

	@Override
	public List<DepartamentoVO> consultarDepartamentoRequerimento(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM Departamento ");
		sql.append(" WHERE codigo in (SELECT distinct departamentoresponsavel from requerimento ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sql.append(" WHERE unidadeEnsino is null or unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(" ) ORDER BY Departamento.nome");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuario);
	}

	public List<DepartamentoVO> consultarPorCodigoPessoaUnidadeEnsino(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select departamento.codigo, departamento.nome from departamento ");
		sb.append(" inner join cargo on cargo.departamento = departamento.codigo ");
		sb.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
		sb.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		sb.append(" where funcionario.pessoa = ").append(pessoa);
		if (!unidadeEnsino.equals(0)) {
			sb.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
		}
		sb.append(" order by departamento.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<DepartamentoVO> listaDepartamentoVOs = new ArrayList<DepartamentoVO>(0);
		while (tabelaResultado.next()) {
			DepartamentoVO obj = new DepartamentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaDepartamentoVOs.add(obj);
		}
		return listaDepartamentoVOs;
	}

	@Override
	public List<DepartamentoVO> consultarPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidade, int nivelMontarDados, UsuarioVO usuario, Boolean visualizarTodasUnidades, Boolean todosDepartamentosMesmaUnidade, Boolean todosDepartamentosMesmoTramite, Boolean mesmoDepartamentosMesmaUnidade, Boolean realizarTramiteRequerimentoOutroDepartamento, Boolean consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades, Boolean consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		Boolean unidadeEnsino = Boolean.FALSE;
		for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidade) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				unidadeEnsino = Boolean.TRUE;
				continue;
			}

		}
		sqlStr.append("select distinct departamento.codigo, departamento.nome from departamento ");
		sqlStr.append(" left join cargo on cargo.departamento = departamento.codigo ");
		sqlStr.append(" left join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append(" left join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		sqlStr.append(" inner join tiporequerimentodepartamento on tiporequerimentodepartamento.departamento = departamento.codigo ");
		sqlStr.append(" WHERE 1=1 ");
		if (visualizarTodasUnidades) {

		}  
		else if(consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades) {
			sqlStr.append(" and departamento.codigo in(select trd.departamento from tiporequerimentodepartamento trd  ");
			sqlStr.append(" where trd.tiporequerimento in ( ");
			sqlStr.append(" select tiporequerimento from tiporequerimentodepartamento ");
			sqlStr.append(" inner join departamento on tiporequerimentodepartamento.departamento = departamento.codigo ");
//			sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo  ");
			sqlStr.append(" inner join funcionariocargo on funcionariocargo.departamento = departamento.codigo ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
			sqlStr.append(")");
			sqlStr.append(")");
		}
		else if(consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades) {
			sqlStr.append(" and departamento.codigo in (");
			sqlStr.append(" select departamento from funcionariocargo");
			sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(")");
		}
		else if (todosDepartamentosMesmaUnidade || realizarTramiteRequerimentoOutroDepartamento) {
			if (unidadeEnsino) {

				if (listaUnidade != null && !listaUnidade.isEmpty()) {
					sqlStr.append("  and departamento.unidadeensino in (");
					int x = 0;
					for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidade) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							if (x > 0) {
								sqlStr.append(", ");
							}
							sqlStr.append(unidadeEnsinoVO.getCodigo());
							x++;
						}

					}
					sqlStr.append(" ) ");
				}
			} else {
				sqlStr.append("  and departamento.unidadeensino != 0");
			}
		} else if (todosDepartamentosMesmoTramite) {
			if (unidadeEnsino) {
				if (listaUnidade != null && !listaUnidade.isEmpty()) {
					sqlStr.append("  and departamento.unidadeensino in (");
					int x = 0;
					for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidade) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							if (x > 0) {
								sqlStr.append(", ");
							}
							sqlStr.append(unidadeEnsinoVO.getCodigo());
							x++;
						}

					}
					sqlStr.append(" ) ");
				}
			} else {
				sqlStr.append("  and departamento.unidadeensino != 0");
			}
			sqlStr.append(" and departamento.codigo in(select trd.departamento from tiporequerimentodepartamento trd  ");
			sqlStr.append(" where trd.tiporequerimento in ( ");
			sqlStr.append(" select tiporequerimento from tiporequerimentodepartamento ");
			sqlStr.append(" inner join departamento on tiporequerimentodepartamento.departamento = departamento.codigo ");
//			sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo  ");
			sqlStr.append(" inner join funcionariocargo on funcionariocargo.departamento = departamento.codigo ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
			sqlStr.append(")");
			sqlStr.append(")");
		} else if (mesmoDepartamentosMesmaUnidade) {
			if (unidadeEnsino) {

				if (listaUnidade != null && !listaUnidade.isEmpty()) {
					sqlStr.append("  and departamento.unidadeensino in (");
					int x = 0;
					for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidade) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							if (x > 0) {
								sqlStr.append(", ");
							}
							sqlStr.append(unidadeEnsinoVO.getCodigo());
							x++;
						}

					}
					sqlStr.append(" ) ");
				}
			} else {
				sqlStr.append("  and departamento.unidadeensino != 0");
			}
			sqlStr.append(" and departamento.codigo in (");
			sqlStr.append(" select departamento from funcionariocargo");
			sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(")");
		} else {
			sqlStr.append(" and departamento.codigo in (");
			sqlStr.append(" select departamento from funcionariocargo");
			sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(")");
		}

		sqlStr.append(" ORDER BY departamento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public DepartamentoVO consultarPorCodigoFuncionarioCargo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM Departamento ");
		sql.append(" INNER JOIN funcionariocargo ON funcionariocargo.departamento = departamento.codigo");
		sql.append(" WHERE funcionariocargo.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new DepartamentoVO();
	}
	
	public List<DepartamentoVO> consultarDepartamentoObrigatoriamenteComResponsavelPorCoodigoParaEnvioComunicadoInterno (List<DepartamentoVO> departamentoVOs, UsuarioVO usuario) throws Exception {
		List<DepartamentoVO> listaDepartamento = new ArrayList<DepartamentoVO>(0);
		StringBuilder sql = new StringBuilder(" SELECT codigo, nome, responsavel FROM Departamento WHERE codigo in (");
		departamentoVOs.forEach(p -> sql.append(p.getCodigo()).append(", "));
		sql.append("0)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			DepartamentoVO departamentoVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			departamentoVO.getResponsavel().setCodigo((tabelaResultado.getInt("responsavel")));
			if (Uteis.isAtributoPreenchido(departamentoVO.getResponsavel())) {
				montarDadosResponsavel(departamentoVO, 0, usuario);
				listaDepartamento.add(departamentoVO);
			}
		}
		return listaDepartamento;
	}
}
