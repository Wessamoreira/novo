package negocio.facade.jdbc.patrimonio;

/**
 * 
 * @author Leonardo Riciolle
 */
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.FormaEntradaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.patrimonio.PatrimonioInterface;

@Repository
@Scope("singleton")
@Lazy
public class Patrimonio extends ControleAcesso implements PatrimonioInterface {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Patrimonio() throws Exception {
		super();
		setIdEntidade("Patrimonio");
	}

	public Patrimonio novo() throws Exception {
		Patrimonio.incluir(getIdEntidade());
		Patrimonio obj = new Patrimonio();
		return obj;
	}

	public static void validarDados(PatrimonioVO obj) throws Exception {

		if (obj.getDescricao().trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Patrimonio_descricao"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoPatrimonioVO())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Patrimonio_tipoPatrimonio"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getPatrimonioUnidadeVOs())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Patrimonio_patrimonioUnidade"));
		}

	}

	public static void validarDadosConsulta(String valorConsulta) throws Exception {
		if (!Uteis.isAtributoPreenchido(valorConsulta)) {
			throw new Exception("Deve ser informado um valor para a consulta.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PatrimonioVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoPatrimonio.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
			obj.getResponsavel().setNome(usuarioVO.getNome());
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO patrimonio(descricao, modelo, marca, tipopatrimonio, datacadastro, responsavelcadastro, fornecedor, compra, dataentrada, notafiscal, formaentradapatrimonio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int x = 1;
					sqlInserir.setString(x++, obj.getDescricao());
					sqlInserir.setString(x++, obj.getModelo());
					sqlInserir.setString(x++, obj.getMarca());
					sqlInserir.setInt(x++, obj.getTipoPatrimonioVO().getCodigo());
					sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataCadastro()));
					sqlInserir.setInt(x++, obj.getResponsavel().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getFornecedorVO())) {
						sqlInserir.setInt(x++, obj.getFornecedorVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCompraVO())) {
						sqlInserir.setInt(x++, obj.getCompraVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataEntrada())) {
						sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataEntrada()));
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getNotaFiscal());
					sqlInserir.setString(x++, obj.getFormaEntradaPatrimonio().toString());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getPatrimonioUnidadeFacade().persistirPatrimonioUnidadeVOs(obj, verificarAcesso, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			for (PatrimonioUnidadeVO unidade : obj.getPatrimonioUnidadeVOs()) {
				unidade.setNovoObj(true);
				unidade.setCodigo(0);
			}
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PatrimonioVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoPatrimonio.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE patrimonio set descricao = ?, modelo = ?, marca = ?, tipopatrimonio = ?, fornecedor = ?, compra = ?, dataentrada = ?, notafiscal = ?, formaentradapatrimonio = ? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setString(x++, obj.getDescricao());
					sqlAlterar.setString(x++, obj.getModelo());
					sqlAlterar.setString(x++, obj.getMarca());
					sqlAlterar.setInt(x++, obj.getTipoPatrimonioVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getFornecedorVO())) {
						sqlAlterar.setInt(x++, obj.getFornecedorVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCompraVO())) {
						sqlAlterar.setInt(x++, obj.getCompraVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataEntrada())) {
						sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataEntrada()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getNotaFiscal());
					sqlAlterar.setString(x++, obj.getFormaEntradaPatrimonio().toString());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getPatrimonioUnidadeFacade().persistirPatrimonioUnidadeVOs(obj, verificarAcesso, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoPatrimonio.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			getFacadeFactory().getPatrimonioUnidadeFacade().excluirPorPatrimonio(obj.getCodigo(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM patrimonio WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public List<PatrimonioVO> montarDadosConsulta(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<PatrimonioVO> patrimonioVOs = new ArrayList<PatrimonioVO>(0);
		while (tabelaResultado.next()) {
			patrimonioVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return patrimonioVOs;
	}

	public PatrimonioVO montarDados(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PatrimonioVO obj = new PatrimonioVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setModelo(tabelaResultado.getString("modelo"));
		obj.setMarca(tabelaResultado.getString("marca"));
		obj.setNotaFiscal(tabelaResultado.getString("notaFiscal"));
		obj.getTipoPatrimonioVO().setCodigo(tabelaResultado.getInt("tipopatrimonio"));
		obj.getTipoPatrimonioVO().setDescricao(tabelaResultado.getString("tipopatrimonio.descricao"));
		obj.getCompraVO().setCodigo(tabelaResultado.getInt("compra"));
		obj.getFornecedorVO().setCodigo(tabelaResultado.getInt("fornecedor"));
		obj.getFornecedorVO().setNome(tabelaResultado.getString("fornecedor.nome"));
		obj.setDataCadastro(tabelaResultado.getDate("datacadastro"));
		obj.setDataEntrada(tabelaResultado.getDate("dataentrada"));
		obj.getResponsavel().setCodigo(tabelaResultado.getInt("responsavelcadastro"));
		obj.getResponsavel().setNome(tabelaResultado.getString("responsavelcadastro.nome"));
		obj.setFormaEntradaPatrimonio(FormaEntradaPatrimonioEnum.valueOf(tabelaResultado.getString("formaentradapatrimonio")));
		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			obj.setPatrimonioUnidadeVOs(getFacadeFactory().getPatrimonioUnidadeFacade().consultarPorChavePrimariaPatrimonio(obj.getCodigo(), nivelMontarDados, false, usuarioVO));
		}
		return obj;
	}

	private StringBuilder getSqlConsultaBasica() {
		StringBuilder sql = new StringBuilder(" select patrimonio.*,  tipopatrimonio.descricao as \"tipopatrimonio.descricao\", ");
		sql.append(" fornecedor.nome as \"fornecedor.nome\", responsavelcadastro.nome as \"responsavelcadastro.nome\" ");
		sql.append(" from patrimonio ");
		sql.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio ");
		sql.append(" left join fornecedor on fornecedor.codigo = patrimonio.fornecedor ");
		sql.append(" left join usuario as responsavelcadastro on responsavelcadastro.codigo = patrimonio.responsavelcadastro ");
		return sql;
	}

	@Override
	public List<PatrimonioVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		List<PatrimonioVO> objs = new ArrayList<PatrimonioVO>(0);
		validarDadosConsulta(valorConsulta);
		if (campoConsulta.equals("descricao")) {
			objs = consultarPorDescricao(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("modelo")) {
			objs = consultarPorModelo(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("marca")) {
			objs = consultarPorMarca(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("fornecedor")) {
			objs = consultarPorFornecedor(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("codigoBarra")) {
			objs = consultarPorCodigoBarra(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("tipopatrimonio")) {
			objs = consultarPorTipoPatrimonio(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		} else if (campoConsulta.equals("notafiscal")) {
			objs = consultarPorNotaFiscal(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		}else if (campoConsulta.equals("unidadeEnsino")) {
			objs = consultarPorUnidadeEnsino(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		}else if (campoConsulta.equals("local")) {
			objs = consultarPorLocal(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, limite, pagina);
		}
		return objs;
	}

	@Override
	public Integer consultarTotalRegistro(String valorConsulta, String campoConsulta) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select count(patrimonio.codigo) as qtde ");
		sqlStr.append(" from patrimonio ");
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio ");
		if (campoConsulta.equals("descricao")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.descricao) ilike( sem_acentos( ? )) ");
		} else if (campoConsulta.equals("modelo")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.modelo) ilike( sem_acentos( ? ))");
		} else if (campoConsulta.equals("marca")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.marca) ilike( sem_acentos( ? ))  ");
		} else if (campoConsulta.equals("fornecedor")) {
			sqlStr.append(" inner join fornecedor on fornecedor.codigo = patrimonio.fornecedor ");
			sqlStr.append(" WHERE sem_acentos(fornecedor.nome) ilike sem_acentos( ? )  ");
		} else if (campoConsulta.equals("codigoBarra")) {
			sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
			sqlStr.append(" where sem_acentos(patrimoniounidade.codigoBarra) ilike sem_acentos( ? ) ) ");
		} else if (campoConsulta.equals("tipopatrimonio")) {
			sqlStr.append(" WHERE sem_acentos(tipopatrimonio.descricao) ilike sem_acentos( ? ) ");
		} else if (campoConsulta.equals("notafiscal")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.notafiscal) ilike sem_acentos( ? ) ");
		}else if (campoConsulta.equals("unidadeEnsino")) {
			sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
			sqlStr.append(" inner join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento ");
			sqlStr.append(" inner join unidadeensino on localarmazenamento.unidadeensino = unidadeensino.codigo ");
			sqlStr.append(" where sem_acentos(unidadeensino.nome) ilike sem_acentos( ? ) ) ");
		}else if (campoConsulta.equals("local")) {
			sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
			sqlStr.append(" inner join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento ");			
			sqlStr.append(" where sem_acentos(localarmazenamento.localarmazenamento) ilike sem_acentos( ? ) ) ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public List<PatrimonioVO> consultarPorTipoPatrimonio(String tipoPatrimonio, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(tipopatrimonio.descricao) ilike sem_acentos( ? ) ");
		sqlStr.append(" order by tipopatrimonio.descricao, patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), tipoPatrimonio + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<PatrimonioVO> consultarPorFornecedor(String fornecedor, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(fornecedor.nome) ilike sem_acentos( ? )  order by fornecedor.nome, patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), fornecedor + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<PatrimonioVO> consultarPorCodigoBarra(String codigoBarra, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
		sqlStr.append(" where sem_acentos(patrimoniounidade.codigoBarra) ilike sem_acentos( ? ) ) order by patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoBarra + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}


	public List<PatrimonioVO> consultarPorUnidadeEnsino(String codigoBarra, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
		sqlStr.append(" inner join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento ");
		sqlStr.append(" inner join unidadeensino on localarmazenamento.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" where sem_acentos(unidadeensino.nome) ilike sem_acentos( ? ) ) ");
		sqlStr.append(" order by patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoBarra + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}
	
	public List<PatrimonioVO> consultarPorLocal(String codigoBarra, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE patrimonio.codigo in (select distinct patrimoniounidade.patrimonio from patrimoniounidade ");
		sqlStr.append(" inner join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento ");			
		sqlStr.append(" where sem_acentos(localarmazenamento.localarmazenamento) ilike sem_acentos( ? ) ) ");
		sqlStr.append(" order by patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoBarra + "%");		
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}
	
	public List<PatrimonioVO> consultarPorMarca(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(patrimonio.marca) ilike( sem_acentos( ? )) order by patrimonio.marca, patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<PatrimonioVO> consultarPorNotaFiscal(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(patrimonio.notaFiscal) ilike( sem_acentos( ? )) order by patrimonio.notaFiscal, patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<PatrimonioVO> consultarPorModelo(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(patrimonio.modelo) ilike( sem_acentos( ? )) order by patrimonio.modelo, patrimonio.descricao ");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<PatrimonioVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer limite, Integer pagina) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(patrimonio.descricao) ilike(sem_acentos(?)) order by patrimonio.descricao");
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	public PatrimonioVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Patrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaBasica();
		sqlStr.append(" WHERE patrimonio.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		throw new Exception(UteisJSF.internacionalizar("msg_dadosnaoencontrados_patrimonio"));
	}

	public void validarDadosAdicionarUnidadesPatrimonio(LocalArmazenamentoVO localArmazenamentoVO, Integer quantidadePatrimonioUnidadeGerar, String codigoBarraInicial) throws Exception {
		if (!Uteis.isAtributoPreenchido(localArmazenamentoVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_localArmazenamento"));
		} else if (quantidadePatrimonioUnidadeGerar == null || quantidadePatrimonioUnidadeGerar <= 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_numeroUnidade"));
		} else if (codigoBarraInicial == null || codigoBarraInicial.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_codigoBarraInicial"));
		}
	}

	/**
	 * Este método é responsável em adicionar os itens do patrimônio onde irá
	 * gerar um item para a quatidade informado no parâmetro @param
	 * quantidadePatrimonioUnidadeGerar e irá gerar o código de barra sequencial
	 * com base no parâmetro @param codigoBarraInicial
	 * 
	 * @param patrimonioUnidadeVOs
	 * @param obj
	 * @param quantidadePatrimonioUnidadeGerar
	 * @param codigoBarraInicial
	 * @throws Exception
	 */
	@Override
	public void adicionarPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO obj, Integer quantidadePatrimonioUnidadeGerar, String codigoBarraInicial) throws Exception {
		validarDadosAdicionarUnidadesPatrimonio(obj.getLocalArmazenamento(), quantidadePatrimonioUnidadeGerar, codigoBarraInicial);
		for (int i = 1; i <= quantidadePatrimonioUnidadeGerar; i++) {
			PatrimonioUnidadeVO patrimonioUnidadeVO = new PatrimonioUnidadeVO();
			codigoBarraInicial = realizarObtencaoCodigoBarraUtilizarPatrimonioUnidade(patrimonioVO, codigoBarraInicial);
			patrimonioUnidadeVO.setCodigoBarra(codigoBarraInicial);
			patrimonioUnidadeVO.setLocalArmazenamento((LocalArmazenamentoVO)obj.getLocalArmazenamento().clone());
			patrimonioUnidadeVO.getLocalArmazenamento().setUnidadeEnsinoVO((UnidadeEnsinoVO)obj.getLocalArmazenamento().getUnidadeEnsinoVO().clone());
			patrimonioUnidadeVO.setPermiteReserva(obj.getPermiteReserva());
			patrimonioUnidadeVO.setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.DISPONIVEL);
			patrimonioUnidadeVO.setUnidadeLocado(obj.getUnidadeLocado());
			patrimonioUnidadeVO.setValorRecurso(obj.getValorRecurso());
			patrimonioUnidadeVO.setEstadoBem(obj.getEstadoBem());
			patrimonioVO.getPatrimonioUnidadeVOs().add(patrimonioUnidadeVO);
		}
	}

	@Override
	public void removerPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO obj) {
		patrimonioVO.getPatrimonioUnidadeVOs().remove(obj);
	}

	/**
	 * Este método verifica se na lista de patrimonio unidade já possui um
	 * patrimônio com o mesmo codigo de barra ou se este já existe na base de
	 * dados, caso encontre o este é incrementado e chamando este método
	 * recursivamente validando o nosso codigo de barra;
	 * 
	 * @param patrimonioVO
	 * @param codigoBarraBase
	 * @return
	 * @throws Exception
	 */
	public String realizarObtencaoCodigoBarraUtilizarPatrimonioUnidade(PatrimonioVO patrimonioVO, String codigoBarraBase) throws Exception {
		for (PatrimonioUnidadeVO patrimonioUnidadeVO : patrimonioVO.getPatrimonioUnidadeVOs()) {
			if (Long.valueOf(patrimonioUnidadeVO.getCodigoBarra()).equals(Long.valueOf(codigoBarraBase)) || getFacadeFactory().getPatrimonioUnidadeFacade().consultarExistenciaPatrimonioUnidadePorCodigoBarra(codigoBarraBase)) {
				int qtdeCaracteres = codigoBarraBase.length();
				codigoBarraBase = Uteis.getPreencherComZeroEsquerda(String.valueOf(Long.valueOf(codigoBarraBase) + 1), qtdeCaracteres);
				return realizarObtencaoCodigoBarraUtilizarPatrimonioUnidade(patrimonioVO, codigoBarraBase);
			}
		}
		return codigoBarraBase;
	}

	@Override
	public void realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException {
		if (!patrimonioUnidadeVO.getNumeroDeSerie().trim().isEmpty()) {
			for (PatrimonioUnidadeVO obj : patrimonioVO.getPatrimonioUnidadeVOs()) {
				if (!obj.equals(patrimonioUnidadeVO) && obj.getNumeroDeSerie().equals(patrimonioUnidadeVO.getNumeroDeSerie())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_numeroSerieDuplicado").replace("{0}", obj.getCodigoBarra()).replace("{1}", patrimonioUnidadeVO.getNumeroDeSerie()));
				}
			}
			getFacadeFactory().getPatrimonioUnidadeFacade().realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(patrimonioUnidadeVO);
		}
	}

	@Override
	public void realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(PatrimonioVO patrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException {
		int x = 0;
		for (PatrimonioUnidadeVO obj : patrimonioVO.getPatrimonioUnidadeVOs()) {
			if (obj.equals(patrimonioUnidadeVO)) {
				x++;
			}
		}
		if (x > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_unidadePatrimonio").replace("{0}", patrimonioUnidadeVO.getCodigoBarra()));
		}
		getFacadeFactory().getPatrimonioUnidadeFacade().realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(patrimonioUnidadeVO);
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "Patrimonio";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		Patrimonio.idEntidade = idEntidade;
	}
	
	
	@Override
	public List<LocalArmazenamentoVO> realizarSeparacaoPatrimonioUnidadePorLocalArmazenamento(PatrimonioVO patrimonioVO){
		List<LocalArmazenamentoVO> localArmazenamentoVOs = new ArrayList<LocalArmazenamentoVO>(0);
		for(PatrimonioUnidadeVO patrimonioUnidadeVO: patrimonioVO.getPatrimonioUnidadeVOs()){
			if(!localArmazenamentoVOs.contains(patrimonioUnidadeVO.getLocalArmazenamento())){
				localArmazenamentoVOs.add(patrimonioUnidadeVO.getLocalArmazenamento());
			}
			localArmazenamentoVOs.get(localArmazenamentoVOs.indexOf(patrimonioUnidadeVO.getLocalArmazenamento())).getPatrimonioUnidadeVOs().add(patrimonioUnidadeVO);
		}
		return localArmazenamentoVOs;
	}

}
