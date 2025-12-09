package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.UnidadeMedidaVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.ProdutoServicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ProdutoServicoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ProdutoServicoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ProdutoServicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ProdutoServico extends ControleAcesso implements ProdutoServicoInterfaceFacade {

	private static final long serialVersionUID = 1563324033787880011L;

	protected static String idEntidade;

	public ProdutoServico() {
		setIdEntidade("ProdutoServico");
	}

	public void verificarExistenciaProduto(String nomeCat, Integer codigo) throws Exception {
		String sqlStr = "SELECT * FROM ProdutoServico WHERE lower (nome) = ('" + nomeCat.toLowerCase() + "') ";
		if (codigo.intValue() != 0) {
			sqlStr += " and codigo != " + codigo.intValue();
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			throw new Exception("Já existe um ProdutoServico cadastrado com este nome.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ProdutoServicoVO.validarDados(obj);
			ProdutoServico.incluir(getIdEntidade(), true, usuarioVO);
			verificarExistenciaProduto(obj.getNome(), 0);

			incluir(obj, "ProdutoServico",
					new AtributoPersistencia()
							.add("nome", obj.getNome())
							.add("categoriaProduto", obj.getCategoriaProduto())
							.add("descricao", obj.getDescricao())
							.add("unidadeMedida", obj.getUnidadeMedida())
							.add("controlarEstoque", obj.getControlarEstoque())
							.add("exigeCompraCotacao", obj.getExigeCompraCotacao())
							.add("valorUnitario", obj.getValorUnitario())
							.add("valorUltimaCompra", obj.getValorUltimaCompra())
							.add("justificativaRequisicaoObrigatoria", obj.isJustificativaRequisicaoObrigatoria())
							.add("tipoprodutoservico", obj.getTipoProdutoServicoEnum().name())
							.add("situacao",  obj.getSituacao())
							.add("permiteAlterarValorUnitarioRequisicao",  obj.getPermiteAlterarValorUnitarioRequisicao())
							, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);

			if (obj.getControlarEstoque()) {
				atualizarListaEstoque(obj, obj.getCodigo());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarListaEstoque(ProdutoServicoVO obj, Integer produto) throws Exception {
		for (EstoqueVO estoqueAgrupado : obj.getEstoqueVOs()) {
			for (EstoqueVO estoque : estoqueAgrupado.getEstoqueVOs()) {
				if (!Uteis.isAtributoPreenchido(estoque.getCodigo())) {
					estoque.getProduto().setCodigo(produto);
					getFacadeFactory().getEstoqueFacade().incluirSemValidarDados(estoque);
				} else {
					getFacadeFactory().getEstoqueFacade().alterarEstoqueMinimo(estoque);
				}
			}
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ProdutoServicoVO.validarDados(obj);
			ProdutoServico.alterar(getIdEntidade(), true, usuarioVO);
			verificarExistenciaProduto(obj.getNome(), obj.getCodigo());

			alterar(obj, "ProdutoServico",
					new AtributoPersistencia()
							.add("nome", obj.getNome())
							.add("categoriaProduto", obj.getCategoriaProduto())
							.add("descricao", obj.getDescricao())
							.add("unidadeMedida", obj.getUnidadeMedida())
							.add("controlarEstoque", obj.getControlarEstoque())
							.add("exigeCompraCotacao", obj.getExigeCompraCotacao())
							.add("valorUnitario", obj.getValorUnitario())
							.add("valorUltimaCompra", obj.getValorUltimaCompra())
							.add("justificativaRequisicaoObrigatoria", obj.isJustificativaRequisicaoObrigatoria())
							.add("tipoprodutoservico", obj.getTipoProdutoServicoEnum().name())
							.add("situacao",  obj.getSituacao())
							.add("permiteAlterarValorUnitarioRequisicao",  obj.getPermiteAlterarValorUnitarioRequisicao()),
							new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);

			if (obj.getControlarEstoque()) {
				atualizarListaEstoque(obj, obj.getCodigo());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProdutoServicoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProdutoServicoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ProdutoServico.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM ProdutoServico WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProdutoServico</code> através
	 * do valor do atributo <code>String tipoUnidade</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProdutoServicoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProdutoServicoVO> consultarPorTipoUnidade(String valorConsulta, Boolean controlarEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProdutoServico WHERE lower (tipoUnidade) like('" + valorConsulta.toLowerCase() + "%') ORDER BY tipoUnidade";
		if (controlarEstoque != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE lower (tipoUnidade) like('" + valorConsulta.toLowerCase() + "%') and controlarrEstoque = " + controlarEstoque.booleanValue() + " ORDER BY tipoUnidade";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	public List<ProdutoServicoVO> consultarPorNomeCategoriaProduto(String valorConsulta, Boolean controlarEstoque, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNomeCategoriaProduto(valorConsulta, controlarEstoque, null, controlaAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<ProdutoServicoVO> consultarPorNomeCategoriaProduto(String valorConsulta, Boolean controlarEstoque, TipoProdutoServicoEnum tipoProdutoServicoEnum,  boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT ProdutoServico.* FROM ProdutoServico inner join CategoriaProduto on ProdutoServico.categoriaProduto = CategoriaProduto.codigo where sem_acentos(CategoriaProduto.nome) ilike(sem_acentos(?)) ");
		if (controlarEstoque != null) {
			sqlStr.append(" and ProdutoServico.controlarEstoque = ").append(controlarEstoque).append(" ");
		}
		if (Uteis.isAtributoPreenchido(tipoProdutoServicoEnum)) {
			sqlStr.append(" and ProdutoServico.tipoProdutoServico = '").append(tipoProdutoServicoEnum.name()).append("' ");
		}
		sqlStr.append("ORDER BY CategoriaProduto.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ProdutoServicoVO> consultarPorNomeECategoriaProduto(String nomeProduto, Integer codCategoriaProduto, Boolean exigeCotacao, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
		String sqlStr = "";
		if (exigeCotacao != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE sem_acentos(ProdutoServico.nome) ilike(sem_acentos(?)) AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND exigeCompraCotacao = " + exigeCotacao.booleanValue() + " ORDER BY nome";
		} else {
			sqlStr = "SELECT * FROM ProdutoServico WHERE sem_acentos(ProdutoServico.nome) ilike(sem_acentos(?)) AND categoriaproduto = " + codCategoriaProduto.intValue() + " ORDER BY nome";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nomeProduto.toLowerCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ProdutoServicoVO> consultarPorCodigoCategoriaProduto(Integer valorConsulta, Boolean controlarEstoque, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
		String sqlStr = "SELECT ProdutoServico.* FROM ProdutoServico, CategoriaProduto WHERE ProdutoServico.categoriaProduto = CategoriaProduto.codigo and CategoriaProduto.codigo = " + valorConsulta.intValue() + " ORDER BY CategoriaProduto.nome";
		if (controlarEstoque != null) {
			sqlStr = "SELECT ProdutoServico.* FROM ProdutoServico, CategoriaProduto WHERE ProdutoServico.categoriaProduto = CategoriaProduto.codigo and CategoriaProduto.codigo = " + valorConsulta.intValue() + " and ProdutoServico.controlarEstoque = " + controlarEstoque.booleanValue() + " ORDER BY CategoriaProduto.nome";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ProdutoServicoVO> consultarProdutoQueExigemCotacaoPorCategoriaProduto(Integer valorConsulta, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
		String sqlStr = "SELECT ProdutoServico.* FROM ProdutoServico, CategoriaProduto "
		        + " WHERE ProdutoServico.categoriaProduto = CategoriaProduto.codigo and CategoriaProduto.codigo = " + valorConsulta.intValue() + " "
		        + " and ProdutoServico.exigecompracotacao = true ORDER BY ProdutoServico.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNome(valorConsulta, controlarEstoque, null, controlarAcesso, nivelMontarDados, usuario);
	}
	
	public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, TipoProdutoServicoEnum tipoProdutoServico,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM ProdutoServico WHERE sem_acentos(nome) ilike(sem_acentos(?)) ");
		if (controlarEstoque != null) {
			sqlStr.append(" and controlarEstoque = ").append(controlarEstoque).append(" ");
		}
		if (Uteis.isAtributoPreenchido(tipoProdutoServico)) {
			sqlStr.append(" and tipoProdutoServico = '").append(tipoProdutoServico.name()).append("' ");
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, boolean exigeCompraCotacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProdutoServico WHERE exigeCompraCotacao = true AND sem_acentos(nome) ilike(sem_acentos(?)) ORDER BY nome";
		if (controlarEstoque != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE exigeCompraCotacao = true AND sem_acentos(nome) ilike(sem_acentos(?)) and controlarEstoque = " + controlarEstoque.booleanValue() + " ORDER BY nome";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProdutoServico</code> através
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProdutoServicoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProdutoServicoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProdutoServico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ProdutoServicoVO> consultarPorCodigoECategoriaProduto(Integer valorConsulta, Integer codCategoriaProduto, Boolean exigeCotacao, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		if (exigeCotacao != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE codigo >= " + valorConsulta.intValue() + " AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND exigeCompraCotacao = " + exigeCotacao.booleanValue() + " ORDER BY codigo";
		} else {
			sqlStr = "SELECT * FROM ProdutoServico WHERE codigo >= " + valorConsulta.intValue() + " AND categoriaproduto = " + codCategoriaProduto.intValue() + " ORDER BY codigo";
		}
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<ProdutoServicoVO> consultarPorListaRequisicao(List<RequisicaoVO> listaRequisicao, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT distinct ProdutoServico.* FROM ProdutoServico ");
		sb.append(" inner join requisicaoitem on requisicaoitem.produtoservico = produtoservico.codigo ");
		sb.append("where (requisicaoitem.cotacao is null or requisicaoitem.cotacao = 0)   ");
		sb.append(" and  requisicaoitem.quantidadeAutorizada > 0  ");
		if(Uteis.isAtributoPreenchido(listaRequisicao)){
			sb.append("and requisicaoitem.requisicao in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaRequisicao)).append(") ");	
		}
		if (Uteis.isAtributoPreenchido(tipoAutorizacaoRequisicaoEnum)) {
			sb.append(" and  requisicaoitem.tipoautorizacaorequisicao = '").append(tipoAutorizacaoRequisicaoEnum.name()).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProdutoServicoVO</code>
	 *         resultantes da consulta.
	 */
	public static List<ProdutoServicoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ProdutoServicoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>ProdutoServicoVO</code>.
	 * 
	 * @return O objeto da classe <code>ProdutoServicoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ProdutoServicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProdutoServicoVO obj = new ProdutoServicoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setControlarEstoque(dadosSQL.getBoolean("controlarEstoque"));
		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto")));
		obj.setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(dadosSQL.getString("tipoProdutoServico")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getUnidadeMedida().setCodigo(dadosSQL.getInt("unidadeMedida"));
		obj.setExigeCompraCotacao(dadosSQL.getBoolean("exigeCompraCotacao"));
		obj.setValorUnitario(dadosSQL.getDouble("valorUnitario"));
		obj.setValorUltimaCompra(dadosSQL.getDouble("valorUltimaCompra"));
		obj.setJustificativaRequisicaoObrigatoria(dadosSQL.getBoolean("justificativaRequisicaoObrigatoria"));
		obj.setPermiteAlterarValorUnitarioRequisicao(dadosSQL.getBoolean("permiteAlterarValorUnitarioRequisicao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosCategoriaProduto(obj, nivelMontarDados, usuario);
			montarDadosUnidadeMedida(obj, nivelMontarDados, usuario);
			return obj;
		}
		montarDadosCategoriaProduto(obj, nivelMontarDados, usuario);
		montarDadosUnidadeMedida(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CategoriaProdutoVO</code> relacionado ao objeto
	 * <code>ProdutoServicoVO</code>. Faz uso da chave primária da classe
	 * <code>CategoriaProdutoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCategoriaProduto(ProdutoServicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaProduto().getCodigo().intValue() == 0) {
			obj.setCategoriaProduto(new CategoriaProdutoVO());
			return;
		}
		obj.setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCategoriaProduto().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public static void montarDadosUnidadeMedida(ProdutoServicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeMedida().getCodigo().intValue() == 0) {
			obj.setUnidadeMedida(new UnidadeMedidaVO());
			return;
		}
		obj.setUnidadeMedida(getFacadeFactory().getUnidadeMedidaFacade().consultarPorChavePrimaria(obj.getUnidadeMedida().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProdutoServicoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ProdutoServicoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ProdutoServico WHERE codigo = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ProdutoServico.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProdutoServico.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarValorUltimaCompraProdutoServico(final Double valorUltimaCompra, final Integer codigo) throws Exception {
		try {
			final String sql = "UPDATE ProdutoServico set valorUltimaCompra=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDouble(1, valorUltimaCompra);
					sqlAlterar.setInt(2, codigo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public Double consultarTotalQuantidadeAutorizadaPorPrecoUnitarioProduto() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select SUM(total) as total from ( ");
		sb.append(" select SUM(requisicaoitem.quantidadeautorizada) * produtoservico.valorultimacompra AS total from requisicaoitem ");
		sb.append(" inner join requisicao on requisicao.codigo = requisicaoitem.requisicao ");
		sb.append(" inner join detalhamentoPlanoOrcamentario detalhamentoplano on detalhamentoplano.departamento = requisicao.departamento and detalhamentoplano.unidadeensino = requisicao.unidadeensino ");
		sb.append(" inner join planoorcamentario on planoorcamentario.codigo = detalhamentoplano.planoorcamentario ");
		sb.append(" inner join produtoservico on produtoservico.codigo = requisicaoitem.produtoservico ");
		sb.append(" where requisicao.datarequisicao >= planoorcamentario.datainicio and requisicao.datarequisicao <= planoorcamentario.datafinal ");
		sb.append(" and planoorcamentario.situacao = 'AT' ");
		sb.append(" and requisicaoitem.quantidadeautorizada > 0 ");
		sb.append(" group by requisicaoitem.quantidadeautorizada, produtoservico.valorultimacompra  ");
		sb.append(" ) as t  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("total");
		}
		return 0.00;
	}

	public List<ProdutoServicoVO> consultarPorNomeQueExigemCotacaoPorCategoriaProduto(String valorConsulta, Integer categoriaProduto, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select produtoservico.*, ");
		sb.append(" categoriaProduto.codigo AS \"categoriaProduto.codigo\", categoriaProduto.nome AS \"categoriaProduto.nome\", ");
		sb.append(" categoriaProduto.categoriaDespesa AS \"categoriaProduto.categoriaDespesa\" ");
		sb.append(" from produtoservico ");
		sb.append(" INNER JOIN categoriaProduto ON categoriaProduto.codigo = produtoservico.categoriaProduto ");
		sb.append(" WHERE ProdutoServico.exigecompracotacao = true  ");
		if (!categoriaProduto.equals(0)) {
			sb.append(" AND categoriaProduto.codigo = ").append(categoriaProduto);
		}
		sb.append(" AND lower(ProdutoServico.nome) like '").append(valorConsulta.toLowerCase()).append("%' ");
		sb.append(" order by produtoservico.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuarioVO);
	}

	@Override
	public List<ProdutoServicoVO> consultarProdutoServicoExigeCompraCotacao(Integer codigoCategoriaProduto, UsuarioVO usuarioVO) throws Exception {
		Preconditions.checkState(Objects.nonNull(codigoCategoriaProduto) && codigoCategoriaProduto > 0, "Código da Categoria do Produto esta vazio ou zero");
		StringJoiner sql = new StringJoiner(" ");
		sql.add("select * from produtoservico where exigecompracotacao = true and categoriaproduto in (");
		sql.add(String.format("WITH RECURSIVE children AS (SELECT codigo, categoriaprodutopai, nome FROM categoriaproduto where codigo = %d", codigoCategoriaProduto));
		sql.add("UNION SELECT tp.codigo, tp.categoriaprodutopai, tp.nome FROM categoriaproduto tp JOIN children c ON tp.categoriaprodutopai = c.codigo)");
		sql.add("SELECT codigo FROM children);");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	}

	public List<ProdutoServicoVO> consultarPorNomeCategoriaProdutoQueExigemCotacaoPorCategoriaProduto(String valorConsulta, Integer categoriaProduto, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select produtoservico.*, ");
		sb.append(" categoriaProduto.codigo AS \"categoriaProduto.codigo\", categoriaProduto.nome AS \"categoriaProduto.nome\", ");
		sb.append(" categoriaProduto.categoriaDespesa AS \"categoriaProduto.categoriaDespesa\" ");
		sb.append(" from produtoservico ");
		sb.append(" INNER JOIN categoriaProduto ON categoriaProduto.codigo = produtoservico.categoriaProduto ");
		sb.append(" WHERE ProdutoServico.exigecompracotacao = true  ");
		if (!categoriaProduto.equals(0)) {
			sb.append(" AND categoriaProduto.codigo = ").append(categoriaProduto);
		}
		sb.append(" AND lower(categoriaProduto.nome) like '").append(valorConsulta.toLowerCase()).append("%' ");
		sb.append(" order by produtoservico.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		return montarDadosConsultaRapida(tabelaResultado, usuarioVO);
	}

	public List<ProdutoServicoVO> consultarPorCodigoQueExigemCotacaoPorCategoriaProduto(Integer produtoServico, Integer categoriaProduto, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select produtoservico.*, ");
		sb.append(" categoriaProduto.codigo AS \"categoriaProduto.codigo\", categoriaProduto.nome AS \"categoriaProduto.nome\", ");
		sb.append(" categoriaProduto.categoriaDespesa AS \"categoriaProduto.categoriaDespesa\" ");
		sb.append(" from produtoservico ");
		sb.append(" INNER JOIN categoriaProduto ON categoriaProduto.codigo = produtoservico.categoriaProduto ");
		sb.append(" WHERE ProdutoServico.exigecompracotacao = true  ");
		if (!categoriaProduto.equals(0)) {
			sb.append(" AND categoriaProduto.codigo = ").append(categoriaProduto);
		}
		if (!produtoServico.equals(0)) {
			sb.append(" AND ProdutoServico.codigo = ").append(produtoServico);
		}
		sb.append(" order by produtoservico.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuarioVO);
	}

	public List<ProdutoServicoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<ProdutoServicoVO> vetResultado = new ArrayList<ProdutoServicoVO>(0);
		while (tabelaResultado.next()) {
			ProdutoServicoVO obj = new ProdutoServicoVO();
			montarDadosRapida(obj, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void montarDadosRapida(ProdutoServicoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.getUnidadeMedida().setCodigo(dadosSQL.getInt("unidadeMedida"));
		obj.setControlarEstoque(dadosSQL.getBoolean("controlarEstoque"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(dadosSQL.getString("tipoProdutoServico")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setValorUnitario(dadosSQL.getDouble("valorUnitario"));
		obj.setValorUltimaCompra(dadosSQL.getDouble("valorUltimaCompra"));
		obj.setExigeCompraCotacao(dadosSQL.getBoolean("exigeCompraCotacao"));
		obj.setPermiteAlterarValorUnitarioRequisicao(dadosSQL.getBoolean("permiteAlterarValorUnitarioRequisicao"));
		// Dados Categoria de Produto
		obj.getCategoriaProduto().setCodigo(dadosSQL.getInt("categoriaProduto.codigo"));
		obj.getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));
		obj.getCategoriaProduto().getCategoriaDespesa().setCodigo(dadosSQL.getInt("categoriaProduto.categoriaDespesa"));
	}

	@Override
	public List<ProdutoServicoVO> consultarPorFiltros(String nomeProdutoPesquisa, Integer categoriaProdutoPesquisa, String tipoProdutoPesquisa, String situacaoProdutoPesquisa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT * FROM ProdutoServico");
		sql.append(" WHERE 1=1");
		montarFiltrosConsulta(nomeProdutoPesquisa, categoriaProdutoPesquisa, tipoProdutoPesquisa, situacaoProdutoPesquisa, sql);
		sql.append(" ORDER BY codigo");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public Integer consultarTotalPorFiltros(String nomeProdutoPesquisa, Integer categoriaProdutoPesquisa, String tipoProdutoPesquisa, String situacaoProdutoPesquisa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT count(codigo) as qtde FROM ProdutoServico");
		sql.append(" WHERE 1 = 1 ");
		montarFiltrosConsulta(nomeProdutoPesquisa, categoriaProdutoPesquisa, tipoProdutoPesquisa, situacaoProdutoPesquisa, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private void montarFiltrosConsulta(String nomeProdutoPesquisa, Integer categoriaProdutoPesquisa, String tipoProdutoPesquisa, String situacaoProdutoPesquisa, StringBuilder sql) {
		if (!Strings.isNullOrEmpty(nomeProdutoPesquisa)) {
			sql.append(String.format(" AND lower(sem_acentos(nome)) ilike sem_acentos('%%%s%%')", nomeProdutoPesquisa.toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(categoriaProdutoPesquisa)) {
			sql.append(" AND categoriaproduto =  ").append(categoriaProdutoPesquisa);
		}
		if (!Strings.isNullOrEmpty(tipoProdutoPesquisa)) {
			sql.append(String.format(" AND tipoprodutoservico = '%s'", tipoProdutoPesquisa));
		}
		if (!Strings.isNullOrEmpty(situacaoProdutoPesquisa)) {
			sql.append(String.format(" AND produtoservico.situacao = '%s'", situacaoProdutoPesquisa));
		}
	}
	
	public List<ProdutoServicoVO> consultarPorCodigoECategoriaProdutoAtivo(Integer valorConsulta, Integer codCategoriaProduto, Boolean exigeCotacao, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		if (exigeCotacao != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE codigo >= " + valorConsulta.intValue() + " AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND exigeCompraCotacao = " + exigeCotacao.booleanValue() + " AND situacao = 'AT' ORDER BY codigo";
		} else {
			sqlStr = "SELECT * FROM ProdutoServico WHERE codigo >= " + valorConsulta.intValue() + " AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND situacao = 'AT' ORDER BY codigo";
		}
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ProdutoServicoVO> consultarPorNomeECategoriaProdutoAtivo(String nomeProduto, Integer codCategoriaProduto, Boolean exigeCotacao, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
		String sqlStr = "";
		if (exigeCotacao != null) {
			sqlStr = "SELECT * FROM ProdutoServico WHERE sem_acentos (nome) ilike(sem_acentos(?)) AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND exigeCompraCotacao = " + exigeCotacao.booleanValue() + " AND situacao = 'AT' ORDER BY nome";
		} else {
			sqlStr = "SELECT * FROM ProdutoServico WHERE sem_acentos (nome) ilike(sem_acentos(?)) AND categoriaproduto = " + codCategoriaProduto.intValue() + " AND situacao = 'AT' ORDER BY nome";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nomeProduto+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
}
