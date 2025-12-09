package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.ConfiguracaoContabilInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoContabil extends ControleAcesso implements ConfiguracaoContabilInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6720565966524342596L;
	protected static String idEntidade;

	public ConfiguracaoContabil() {
		super();
		setIdEntidade("ConfiguracaoContabil");
	}

	public void validarDados(ConfiguracaoContabilVO obj, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), "O campo Nome (Configuração Contábil) não foi informado.");
		Uteis.checkState(validarUnicidade(obj, usuario), "Já existe uma configuração contábil com esse nome: " + obj.getNome());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		List<ConfiguracaoContabilRegraVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(obj.getListaContabilRegrasDesconto());
		listaTemp.addAll(obj.getListaContabilRegrasDescontoPagar());
		listaTemp.addAll(obj.getListaContabilRegrasJuroMultaAcrescimo());
		listaTemp.addAll(obj.getListaContabilRegrasJuroMultaPagar());
		listaTemp.addAll(obj.getListaContabilRegrasMovimentacaoFinanceira());
		listaTemp.addAll(obj.getListaContabilRegrasPagamento());
		listaTemp.addAll(obj.getListaContabilRegrasRecebimento());
		listaTemp.addAll(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto());
		listaTemp.addAll(obj.getListaContabilRegrasSacado());
		listaTemp.addAll(obj.getListaContabilRegrasNotaFiscalEntradaImposto());
		listaTemp.addAll(obj.getListaContabilRegrasTaxaCartoes());
		listaTemp.addAll(obj.getListaContabilRegrasCartaoCredito());
		validarSeRegistroForamExcluidoDasListaSubordinadas(listaTemp, "ConfiguracaoContabilRegra", "configuracaoContabil", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasDesconto(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasDescontoPagar(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasJuroMultaAcrescimo(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasJuroMultaPagar(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasMovimentacaoFinanceira(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasPagamento(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasRecebimento(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasSacado(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasTaxaCartoes(), false, usuarioVO);
		getFacadeFactory().getConfiguracaoContabilRegraFacade().persistir(obj.getListaContabilRegrasCartaoCredito(), false, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabil.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ConfiguracaoContabil (layoutintegracao, nome ) ");
			sql.append("    VALUES ( ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getLayoutIntegracaoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getUnidadeEnsinoFacade().atualizarUnidadeEnsinoDadosConfiguracaoContabil(obj, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabil.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConfiguracaoContabil ");
			sql.append("   SET layoutintegracao=?, nome=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getLayoutIntegracaoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
			getFacadeFactory().getUnidadeEnsinoFacade().atualizarUnidadeEnsinoDadosConfiguracaoContabil(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabil.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM ConfiguracaoContabil WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDadosConfiguracaoContabilRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
//		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar()) && obj.getTipoSacadoPagar().isBanco() && !Uteis.isAtributoPreenchido(obj.getBancoVO()), "O Sacado Banco deve ser informado.");
//		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar()) && obj.getTipoSacadoPagar().isFuncionario() && !Uteis.isAtributoPreenchido(obj.getFuncionarioVO()), "O Sacado Funcionário deve ser informado.");
//		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar()) && obj.getTipoSacadoPagar().isFornecedor() && !Uteis.isAtributoPreenchido(obj.getFornecedorVO()), "O Sacado Fornecedor deve ser informado.");
//		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getTipoSacdoaPagar()) && obj.getTipoSacadoPagar().isParceiro() && !Uteis.isAtributoPreenchido(obj.getParceiroVO()), "O Sacado Parceiro deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(configuracaoRegra.getPlanoContaVO()) && !Uteis.isAtributoPreenchido(configuracaoRegra.getListaConfiguracaoContabilRegraPlanoContaVO()), "O campo Plano de Conta (Configuração Contábil) deve ser informado.");		
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasRecebimento(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDesconto(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasJuroMultaAcrescimo(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasTaxaCartoes(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasCartaoCredito(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDescontoPagar(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasJuroMultaPagar(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasMovimentacaoFinanceira(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasPagamento(), configuracaoRegra);
		obj.validarDadosListaConfiguracaoContabilRegra(obj.getListaContabilRegrasSacado(), configuracaoRegra);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addConfiguracaoContabilRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra, UsuarioVO usuario) throws Exception {
		configuracaoRegra.setConfiguracaoContabilVO(obj);
		validarDadosConfiguracaoContabilRegra(obj, configuracaoRegra);
		if (Uteis.isAtributoPreenchido(configuracaoRegra.getContaCorrenteOrigemVO())) {
			configuracaoRegra.setContaCorrenteOrigemVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(configuracaoRegra.getContaCorrenteOrigemVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		if (Uteis.isAtributoPreenchido(configuracaoRegra.getFormaPagamentoVO())) {
			configuracaoRegra.setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(configuracaoRegra.getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(configuracaoRegra.getOperadoraCartaoVO())) {
			configuracaoRegra.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(configuracaoRegra.getOperadoraCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(configuracaoRegra.getImpostoVO())) {
			configuracaoRegra.setImpostoVO(getFacadeFactory().getImpostoFacade().consultarPorChavePrimaria(configuracaoRegra.getImpostoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}

		if (configuracaoRegra.getTipoRegraContabilEnum().isRecebimento()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasRecebimento(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isPagamento()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasPagamento(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isDesconto()) {
			preencherListaconfiguracaoContabilPorDesconto(obj, configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isDescontoPagar()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDescontoPagar(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaAcrescimo()) {
			preencherListaContabilRegrasJuroMultaAcrescimo(obj.getListaContabilRegrasJuroMultaAcrescimo(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaPagar()) {
			preencherListaContabilRegrasJuroMultaPagar(obj.getListaContabilRegrasJuroMultaPagar(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isMovimentacaoFinanceira()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasMovimentacaoFinanceira(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isSacado()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasSacado(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isNotaFiscaEntradaImposto()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isTaxaCartoes()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasTaxaCartoes(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isCartaoCredito()) {
			preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasCartaoCredito(), configuracaoRegra);
		}
	}

	private void preencherListaconfiguracaoContabilPorDesconto(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		if (Uteis.isAtributoPreenchido(configuracaoRegra.getParceiroVO()) && (configuracaoRegra.getTipoDescontoEnum().isBolsaConvenio() || configuracaoRegra.getTipoDescontoEnum().isConvenio())) {
			configuracaoRegra.setTipoSacadoReceber(TipoPessoa.PARCEIRO);
			configuracaoRegra.setCodigoSacado(configuracaoRegra.getParceiroVO().getCodigo());
		} else {
			configuracaoRegra.setTipoSacadoReceber(null);
			configuracaoRegra.setCodigoSacado(null);
		}
		preencherListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDesconto(), configuracaoRegra);
	}

	
	private void preencherListaConfiguracaoContabilRegra(List<ConfiguracaoContabilRegraVO> lista, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		int index = 0;
		for (ConfiguracaoContabilRegraVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(configuracaoRegra)) {
				lista.set(index, configuracaoRegra);
				return;
			}
			index++;
		}
		lista.add(configuracaoRegra);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removeConfiguracaoContabilRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra, UsuarioVO usuario) throws Exception {
		if (configuracaoRegra.getTipoRegraContabilEnum().isRecebimento()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasRecebimento(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isPagamento()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasPagamento(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isDesconto()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDesconto(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isDescontoPagar()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasDescontoPagar(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaAcrescimo()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasJuroMultaAcrescimo(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaPagar()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasJuroMultaPagar(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isMovimentacaoFinanceira()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasMovimentacaoFinanceira(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isSacado()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasSacado(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isNotaFiscaEntradaImposto()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isTaxaCartoes()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasTaxaCartoes(), configuracaoRegra);
		} else if (configuracaoRegra.getTipoRegraContabilEnum().isCartaoCredito()) {
			removerListaConfiguracaoContabilRegra(obj.getListaContabilRegrasCartaoCredito(), configuracaoRegra);
		}
		
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void removerListaConfiguracaoContabilRegra(List<ConfiguracaoContabilRegraVO> lista, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		Iterator<ConfiguracaoContabilRegraVO> i = lista.iterator();
		while (i.hasNext()) {
			ConfiguracaoContabilRegraVO objExistente =  i.next();
			if (objExistente.equalsCampoSelecaoLista(configuracaoRegra)) {
				i.remove();
				return;
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasicaDistinct() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cc.codigo as \"cc.codigo\", cc.nome as \"cc.nome\", ");
		sql.append(" layoutintegracao.codigo as \"layoutintegracao.codigo\", layoutintegracao.descricao as \"layoutintegracao.descricao\" ");
		sql.append(" FROM ConfiguracaoContabil cc");
		sql.append(" LEFT JOIN layoutintegracao ON layoutintegracao.codigo = cc.layoutintegracao ");
		return sql;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cc.codigo as \"cc.codigo\", cc.nome as \"cc.nome\", ");
		sql.append(" layoutintegracao.codigo as \"layoutintegracao.codigo\", layoutintegracao.descricao as \"layoutintegracao.descricao\" ");
		sql.append(" FROM ConfiguracaoContabil cc");
		sql.append(" LEFT JOIN layoutintegracao ON layoutintegracao.codigo = cc.layoutintegracao ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE cc.codigo >= ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY cc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoContabilVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lower(sem_acentos(cc.nome)) like(lower(sem_acentos(?)))");
		sqlStr.append(" ORDER BY cc.nome desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta.toLowerCase() + "%");
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoContabilVO> consultaRapidaPorLayoutIntegracaoContabil(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lower(sem_acentos(layoutintegracao.descricao)) like(lower(sem_acentos(?)))");
		sqlStr.append(" ORDER BY layoutintegracao.descricao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta.toLowerCase() + "%");
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoContabilVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ConfiguracaoContabil WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoContabilVO ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoContabilVO consultaRapidaPorIntegracaoContabil(IntegracaoContabilVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.configuracaoContabil = cc.codigo ");
		if(obj.getTipoGeracaoIntegracaoContabilEnum().isUnidadeEnsino()){
			sqlStr.append(" WHERE unidadeensino.codigo = ").append(obj.getUnidadeEnsinoVO().getCodigo());
		}else if(obj.getTipoGeracaoIntegracaoContabilEnum().isCodigoIntegracao()){
			sqlStr.append(" WHERE unidadeensino.codigointegracaocontabil = '").append(obj.getCodigoIntegracaoContabil()).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoContabilVO cc = new ConfiguracaoContabilVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(cc, tabelaResultado, nivelMontarDados, usuario);
		}
		return cc;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoContabilVO consultaRapidaPorCodigoUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" LEFT JOIN unidadeEnsino ON unidadeEnsino.configuracaoContabil = cc.codigo ");
		sqlStr.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoContabilVO obj = new ConfiguracaoContabilVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
		}
		return obj;
	}
	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct ConfiguracaoContabil.codigo FROM ConfiguracaoContabil ");
		sql.append(" LEFT JOIN unidadeEnsino ON unidadeEnsino.configuracaoContabil = configuracaoContabil.codigo ");
		sql.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsinoPorTipoRegraContabil(Integer unidadeEnsino, TipoRegraContabilEnum tipoRegraContabilEnum, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct ConfiguracaoContabil.codigo FROM ConfiguracaoContabil ");
		sql.append(" INNER JOIN ConfiguracaoContabilRegra ON ConfiguracaoContabilRegra.ConfiguracaoContabil = ConfiguracaoContabil.codigo ");
		sql.append(" LEFT JOIN unidadeEnsino ON unidadeEnsino.configuracaoContabil = configuracaoContabil.codigo ");
		sql.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
		sql.append(" and ConfiguracaoContabilRegra.TipoRegraContabil = '").append(tipoRegraContabilEnum.name()).append("' ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private Boolean validarUnicidade(ConfiguracaoContabilVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM ConfiguracaoContabil ");
		sql.append(" WHERE lower(sem_acentos(nome) ) = (lower(sem_acentos(?)))");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNome()).next();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoContabilVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConfiguracaoContabilVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			ConfiguracaoContabilVO obj = new ConfiguracaoContabilVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(ConfiguracaoContabilVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("cc.codigo")));
		obj.setNome(dadosSQL.getString("cc.nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return;
		}
		obj.getLayoutIntegracaoVO().setCodigo(dadosSQL.getInt("layoutintegracao.codigo"));
		obj.getLayoutIntegracaoVO().setDescricao(dadosSQL.getString("layoutintegracao.descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
		
		obj.setLayoutIntegracaoVO(Uteis.montarDadosVO(dadosSQL.getInt("layoutintegracao.codigo"), LayoutIntegracaoVO.class, p -> getFacadeFactory().getLayoutIntegracaoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_TODOS, usuario)));
		obj.setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarRapidaPorConfiguracaoContabil(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabil(obj, usuario);

	}

	public static ConfiguracaoContabilVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoContabilVO obj = new ConfiguracaoContabilVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.getLayoutIntegracaoVO().setCodigo(dadosSQL.getInt("layoutintegracao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarRapidaPorConfiguracaoContabil(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabil(obj, usuario);
		obj.setLayoutIntegracaoVO(Uteis.montarDadosVO(dadosSQL.getInt("layoutintegracao"), LayoutIntegracaoVO.class, p -> getFacadeFactory().getLayoutIntegracaoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_TODOS, usuario)));
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoContabil.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoContabil.idEntidade = idEntidade;
	}

	private void preencherListaContabilRegrasJuroMultaAcrescimo(List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaAcrescimo, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaAcrescimo() && !Uteis.isAtributoPreenchido(configuracaoRegra.getTipoDescontoEnum())) {
			List<TipoDesconto> listaTipoDescontoEnum = new ArrayList<>(EnumSet.of(TipoDesconto.ACRESCIMO, TipoDesconto.JURO, TipoDesconto.MULTA));
			Ordenacao.ordenarLista(listaTipoDescontoEnum, "descricao");
			for (TipoDesconto tipoDescontoEnum : listaTipoDescontoEnum) {
				ConfiguracaoContabilRegraVO configuracaoContabilRegraVO = configuracaoRegra.getClone();
				configuracaoContabilRegraVO.setTipoDescontoEnum(tipoDescontoEnum);
				preencherListaConfiguracaoContabilRegra(listaContabilRegrasJuroMultaAcrescimo, configuracaoContabilRegraVO);
			}
		} else {
			preencherListaConfiguracaoContabilRegra(listaContabilRegrasJuroMultaAcrescimo, configuracaoRegra);
		}
	}

	private void preencherListaContabilRegrasJuroMultaPagar(List<ConfiguracaoContabilRegraVO> listaContabilRegrasJuroMultaPagar, ConfiguracaoContabilRegraVO configuracaoRegra) throws Exception {
		if (configuracaoRegra.getTipoRegraContabilEnum().isJuroMultaAcrescimo() && !Uteis.isAtributoPreenchido(configuracaoRegra.getTipoDescontoEnum())) {
			List<TipoDesconto> listaTipoDescontoEnum = new ArrayList<>(EnumSet.of(TipoDesconto.JURO, TipoDesconto.MULTA));
			Ordenacao.ordenarLista(listaTipoDescontoEnum, "descricao");
			for (TipoDesconto tipoDescontoEnum : listaTipoDescontoEnum) {
				ConfiguracaoContabilRegraVO configuracaoContabilRegraVO = configuracaoRegra.getClone();
				configuracaoContabilRegraVO.setTipoDescontoEnum(tipoDescontoEnum);
				preencherListaConfiguracaoContabilRegra(listaContabilRegrasJuroMultaPagar, configuracaoContabilRegraVO);
			}
		} else {
			preencherListaConfiguracaoContabilRegra(listaContabilRegrasJuroMultaPagar, configuracaoRegra);
		}
	}

}
