package negocio.facade.jdbc.compras;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.auth.RSA;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CategoriaProdutoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CategoriaProduto extends ControleAcesso implements CategoriaProdutoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6394158845362978161L;
	protected static String idEntidade;

	public CategoriaProduto() throws Exception {
		super();
		setIdEntidade("CategoriaProduto");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>CategoriaProdutoVO</code>.
	 */
	public CategoriaProdutoVO novo() throws Exception {
		CategoriaProduto.incluir(getIdEntidade());
		CategoriaProdutoVO obj = new CategoriaProdutoVO();
		return obj;
	}

	public void verificarExistenciaCategoriaProduto(String nomeCat, Integer codigo) throws Exception {
		String sqlStr = String.format("SELECT * FROM CategoriaProduto WHERE lower (sem_acentos(nome)) = (sem_acentos('%s')) ", nomeCat.toLowerCase());
		if (codigo.intValue() != 0) {
			sqlStr += " and codigo != " + codigo.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			throw new Exception("Já existe uma Categoria de Produto cadastrada com este nome.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void incluir(final CategoriaProdutoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			CategoriaProdutoVO.validarDados(obj);
			CategoriaProduto.incluir(getIdEntidade(), true, usuarioVO);
			verificarExistenciaCategoriaProduto(obj.getNome(), 0);

			incluir(obj, "CategoriaProduto",
					new AtributoPersistencia()
							.add("nome", obj.getNome())
							.add("categoriaDespesa", obj.getCategoriaDespesa())
							.add("tramiteCotacaoCompra", obj.getTramiteCotacaoCompra())
							.add("categoriaprodutopai", obj.getCategoriaProdutoPai())
							.add("questionarioaberturarequisicao", obj.getQuestionarioAberturaRequisicao())
							.add("questionarioentregarequisicao", obj.getQuestionarioEntregaRequisicao())
							.add("obrigarDataNecessidadeRequisicao", obj.getObrigarDataNecessidadeRequisicao()), usuarioVO);
			obj.setNovoObj(Boolean.TRUE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void alterar(final CategoriaProdutoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			CategoriaProdutoVO.validarDados(obj);
			CategoriaProduto.alterar(getIdEntidade(), true, usuarioVO);
			verificarExistenciaCategoriaProduto(obj.getNome(), obj.getCodigo());

			alterar(obj, "CategoriaProduto",
					new AtributoPersistencia()
							.add("nome", obj.getNome())
							.add("categoriaDespesa", obj.getCategoriaDespesa())
							.add("tramiteCotacaoCompra", obj.getTramiteCotacaoCompra())
							.add("categoriaprodutopai", obj.getCategoriaProdutoPai())
							.add("questionarioaberturarequisicao", obj.getQuestionarioAberturaRequisicao())
							.add("questionarioentregarequisicao", obj.getQuestionarioEntregaRequisicao())
							.add("obrigarDataNecessidadeRequisicao", obj.getObrigarDataNecessidadeRequisicao()),
							new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void excluir(CategoriaProdutoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			CategoriaProduto.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM CategoriaProduto WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<CategoriaProdutoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaProduto WHERE lower (nome) like('%" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CategoriaProdutoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaProduto WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public CategoriaProdutoVO consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM CategoriaProduto WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new CategoriaProdutoVO();
	}

	@Override
	public List<CategoriaProdutoVO> consultaComHierarquia(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringJoiner sql = new StringJoiner(" ");

		if (Objects.isNull(valorConsulta) || valorConsulta <= 0) {
			sql.add("SELECT * FROM CategoriaProduto ORDER BY codigo");
		} else {
			sql.add(String.format("select * from categoriaproduto where codigo not in (WITH RECURSIVE children AS (SELECT codigo, categoriaprodutopai, nome FROM categoriaproduto where codigo = %d", valorConsulta));
			sql.add("UNION");
			sql.add("SELECT tp.codigo, tp.categoriaprodutopai, tp.nome FROM categoriaproduto tp JOIN children c ON tp.categoriaprodutopai = c.codigo)");
			sql.add("SELECT codigo FROM children);");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<CategoriaProdutoVO> consultarCategoriaProdutoPassandoDescricaoCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " select categoriaproduto.codigo, categoriaproduto.nome as nome, " + " categoriadespesa.codigo as categoriaDespesa from categoriaproduto inner join categoriadespesa on categoriaproduto.categoriadespesa = categoriadespesa.codigo " + " where upper(sem_acentos(categoriadespesa.descricao)) like (sem_acentos('%" + valorConsulta.toUpperCase() + "%'))";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	
	public static List<CategoriaProdutoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CategoriaProdutoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	public static CategoriaProdutoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CategoriaProdutoVO obj = new CategoriaProdutoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getTramiteCotacaoCompra().setCodigo((dadosSQL.getInt("tramiteCotacaoCompra")));
		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa")));
		obj.setObrigarDataNecessidadeRequisicao(dadosSQL.getBoolean("obrigardatanecessidaderequisicao"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.setCategoriaProdutoPai(Uteis.montarDadosVO(dadosSQL.getInt("categoriaprodutopai"), CategoriaProdutoVO.class, p -> getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_TODOS, usuario)));
		obj.setTramiteCotacaoCompra(Uteis.montarDadosVO(dadosSQL.getInt("tramiteCotacaoCompra"), TramiteCotacaoCompraVO.class, p -> getFacadeFactory().getTramiteFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		montarDadosCategoriaDespesa(obj, nivelMontarDados, usuario);
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("questionarioaberturarequisicao"))) {
			obj.setQuestionarioAberturaRequisicao(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("questionarioaberturarequisicao"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("questionarioentregarequisicao"))) {
			obj.setQuestionarioEntregaRequisicao(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("questionarioentregarequisicao"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		return obj;
	}

	public static void montarDadosCategoriaDespesa(CategoriaProdutoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
			obj.setCategoriaDespesa(new CategoriaDespesaVO());
			return;
		}
		obj.setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public CategoriaProdutoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM CategoriaProduto WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List consultarCategoriaProdutoPassandoCodigoFornecedor(Integer fornecedor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT categoriaProduto.codigo, categoriaProduto.* FROM CategoriaProduto LEFT JOIN FornecedorCategoriaProduto ON CategoriaProduto.codigo = FornecedorCategoriaProduto.CategoriaProduto ";
		if(fornecedor > 0) {
			sqlStr += " WHERE FornecedorCategoriaProduto.fornecedor = " + fornecedor; 	
		}
		sqlStr +=  " ORDER BY CategoriaProduto.codigo";	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	
	public static String getIdEntidade() {
		return CategoriaProduto.idEntidade;
	}
	
	public void setIdEntidade(String idEntidade) {
		CategoriaProduto.idEntidade = idEntidade;
	}
}
