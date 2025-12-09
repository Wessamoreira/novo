package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ParametrizarOperacoesAutomaticasConciliacaoItem extends ControleAcesso implements ParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990764925798003741L;
	protected static String idEntidade;

	public ParametrizarOperacoesAutomaticasConciliacaoItem() throws Exception {
		super();
		setIdEntidade("ParametrizarOperacoesAutomaticasConciliacaoItem");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConciliacaoContaCorrenteVO conciliacao, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		persistir(conciliacao.getListaParametrizarEntradaItens(), verificarAcesso, usuarioVO);
		persistir(conciliacao.getListaParametrizarSaidaItens(), verificarAcesso, usuarioVO);
		for (ParametrizarOperacoesAutomaticasConciliacaoItemVO obj : conciliacao.getListaParametrizarExcluida()) {
			excluir(obj, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ParametrizarOperacoesAutomaticasConciliacaoItemVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ParametrizarOperacoesAutomaticasConciliacaoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ParametrizarOperacoesAutomaticasConciliacaoItem.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ParametrizarOperacoesAutomaticasConciliacaoItem (nomeLancamento, unidadeEnsino,  ");
			sql.append("    tipoFormaPagamento, centroReceita, contaCorrente, formaPagamento, tipoSacado, funcionarioSacado,  ");
			sql.append("    fornecedorSacado, parceiroSacado, bancoSacado, operadoraCartaoSacado, tipoMovimentacaoFinanceira , ");
			sql.append("    origemExtratoContaCorrenteEnum, contaCorrenteDestino, categoriaDespesa, centroResultadoAdministrativo ) ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setString(++i, obj.getNomeLancamento());
					sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setString(++i, obj.getTipoFormaPagamento().name());
					if (Uteis.isAtributoPreenchido(obj.getCentroReceitaVO())) {
						sqlInserir.setInt(++i, obj.getCentroReceitaVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					sqlInserir.setInt(++i, obj.getFormaPagamentoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTipoSacado())) {
						sqlInserir.setString(++i, obj.getTipoSacado().name());	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioSacado())) {
						sqlInserir.setInt(++i, obj.getFuncionarioSacado().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFornecedorSacado())) {
						sqlInserir.setInt(++i, obj.getFornecedorSacado().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getParceiroSacado())) {
						sqlInserir.setInt(++i, obj.getParceiroSacado().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getBancoSacado())) {
						sqlInserir.setInt(++i, obj.getBancoSacado().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoSacado())) {
						sqlInserir.setInt(++i, obj.getOperadoraCartaoSacado().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoMovimentacaoFinanceira().name());
					sqlInserir.setString(++i, obj.getOrigemExtratoContaCorrenteEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteDestinoVO())) {
						sqlInserir.setInt(++i, obj.getContaCorrenteDestinoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlInserir);
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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ParametrizarOperacoesAutomaticasConciliacaoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ParametrizarOperacoesAutomaticasConciliacaoItem.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ParametrizarOperacoesAutomaticasConciliacaoItem ");
			sql.append("   SET nomeLancamento=?, unidadeEnsino=?,  ");
			sql.append("    tipoFormaPagamento=?, centroReceita=?, contaCorrente=?,  ");
			sql.append("    formaPagamento=?, tipoSacado=?, funcionarioSacado=?, fornecedorSacado=?, ");
			sql.append("    parceiroSacado=?, bancoSacado=?, operadoraCartaoSacado=?, tipoMovimentacaoFinanceira=?, ");
			sql.append("    origemExtratoContaCorrenteEnum=?, contaCorrenteDestino=?, categoriaDespesa=?, centroResultadoAdministrativo=? ");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setString(++i, obj.getNomeLancamento());
					sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlAlterar.setString(++i, obj.getTipoFormaPagamento().name());
					if (Uteis.isAtributoPreenchido(obj.getCentroReceitaVO())) {
						sqlAlterar.setInt(++i, obj.getCentroReceitaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getFormaPagamentoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTipoSacado())) {
						sqlAlterar.setString(++i, obj.getTipoSacado().name());	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioSacado())) {
						sqlAlterar.setInt(++i, obj.getFuncionarioSacado().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFornecedorSacado())) {
						sqlAlterar.setInt(++i, obj.getFornecedorSacado().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getParceiroSacado())) {
						sqlAlterar.setInt(++i, obj.getParceiroSacado().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getBancoSacado())) {
						sqlAlterar.setInt(++i, obj.getBancoSacado().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoSacado())) {
						sqlAlterar.setInt(++i, obj.getOperadoraCartaoSacado().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoMovimentacaoFinanceira().name());
					sqlAlterar.setString(++i, obj.getOrigemExtratoContaCorrenteEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteDestinoVO())) {
						sqlAlterar.setInt(++i, obj.getContaCorrenteDestinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConciliacaoContaCorrenteVO conciliacao, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			for (ParametrizarOperacoesAutomaticasConciliacaoItemVO obj : conciliacao.getListaParametrizarEntradaItens()) {
				excluir(obj, verificarAcesso, usuario);
			}
			for (ParametrizarOperacoesAutomaticasConciliacaoItemVO obj : conciliacao.getListaParametrizarSaidaItens()) {
				excluir(obj, verificarAcesso, usuario);
			}
			for (ParametrizarOperacoesAutomaticasConciliacaoItemVO obj : conciliacao.getListaParametrizarExcluida()) {
				excluir(obj, verificarAcesso, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ParametrizarOperacoesAutomaticasConciliacaoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if(Uteis.isAtributoPreenchido(obj)){
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.PARAMETRIZAR_OPERACOES_AUTOMATICAS_CONCILIACAO_ITEM, usuario);
				ParametrizarOperacoesAutomaticasConciliacaoItem.excluir(getIdEntidade(), verificarAcesso, usuario);
				String sql = "DELETE FROM ParametrizarOperacoesAutomaticasConciliacaoItem WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });	
			}
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" poaci.codigo as \"poaci.codigo\", poaci.nomelancamento as \"poaci.nomelancamento\",  ");
		sql.append(" poaci.tipoformapagamento as \"poaci.tipoformapagamento\", poaci.tiposacado as \"poaci.tiposacado\", poaci.tipomovimentacaofinanceira as \"poaci.tipomovimentacaofinanceira\", ");
		sql.append(" poaci.origemExtratoContaCorrenteEnum as \"poaci.origemExtratoContaCorrenteEnum\",  ");
		
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\",  unidadeensino.nome as \"unidadeensino.nome\", ");
		
		sql.append(" contacorrente.codigo as \"contacorrente.codigo\", contacorrente.numero as \"contacorrente.numero\",  ");
		sql.append(" contacorrente.digito as \"contacorrente.digito\", contacorrente.carteira as \"contacorrente.carteira\", ");
		sql.append(" contacorrente.contaCaixa as \"contacorrente.contaCaixa\", contacorrente.tipoContaCorrente as \"contacorrente.tipoContaCorrente\", ");
		sql.append(" agencia.codigo as \"agencia.codigo\", agencia.numeroagencia as \"agencia.numeroagencia\", agencia.digito as \"agencia.digito\", ");
		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		
		
		sql.append(" contadestino.codigo as \"contadestino.codigo\", contadestino.numero as \"contadestino.numero\",  ");
		sql.append(" contadestino.digito as \"contadestino.digito\", contadestino.carteira as \"contadestino.carteira\", ");
		sql.append(" contadestino.contaCaixa as \"contadestino.contaCaixa\",  contadestino.tipoContaCorrente as \"contadestino.tipoContaCorrente\", ");
		sql.append(" agenciadestino.codigo as \"agenciadestino.codigo\", agenciadestino.numeroagencia as \"agenciadestino.numeroagencia\", agenciadestino.digito as \"agenciadestino.digito\", ");
		sql.append(" bancodestino.codigo as \"bancodestino.codigo\", bancodestino.nome as \"bancodestino.nome\", ");
		
		
		
		sql.append(" formapagamento.codigo as \"formapagamento.codigo\", formapagamento.nome as \"formapagamento.nome\", ");
		sql.append(" centroresultado.codigo as \"centroresultado.codigo\", centroresultado.descricao as \"centroresultado.descricao\", ");
		sql.append(" categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.descricao as \"categoriadespesa.descricao\", ");
		sql.append(" centroreceita.codigo as \"centroreceita.codigo\", centroreceita.descricao as \"centroreceita.descricao\", ");
		sql.append(" funcionario.codigo as \"funcionario.codigo\",  funcionario.matricula as \"funcionario.matricula\", pessoafunc.codigo as \"pessoafunc.codigo\", pessoafunc.nome as \"pessoafunc.nome\", ");
		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\", ");
		sql.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sql.append(" bancosacado.codigo as \"bancosacado.codigo\",  bancosacado.nome as \"bancosacado.nome\", ");
		sql.append(" operadoracartao.codigo as \"operadoracartao.codigo\", operadoracartao.nome as \"operadoracartao.nome\" ");

		sql.append(" FROM parametrizaroperacoesautomaticasconciliacaoitem poaci ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = poaci.unidadeensino ");
		sql.append(" inner join formapagamento on formapagamento.codigo = poaci.formapagamento ");
		sql.append(" left join contacorrente on contacorrente.codigo = poaci.contacorrente ");
		sql.append(" left join agencia on agencia.codigo = contacorrente.agencia ");
		sql.append(" left join banco on banco.codigo = agencia.banco ");
		sql.append(" left join contacorrente contadestino on contadestino.codigo = poaci.contacorrentedestino ");
		sql.append(" left join agencia agenciadestino on agenciadestino.codigo = contadestino.agencia ");
		sql.append(" left join banco bancodestino on bancodestino.codigo = agenciadestino.banco ");
		sql.append(" left join centroresultado on centroresultado.codigo = poaci.centroresultadoAdministrativo ");
		sql.append(" left join categoriadespesa on categoriadespesa.codigo = poaci.categoriadespesa ");
		sql.append(" left join centroreceita on centroreceita.codigo = poaci.centroreceita ");
		sql.append(" left join funcionario on funcionario.codigo = poaci.funcionariosacado ");
		sql.append(" left join pessoa pessoafunc on pessoafunc.codigo = funcionario.pessoa ");
		sql.append(" left join fornecedor on fornecedor.codigo = poaci.fornecedorsacado ");
		sql.append(" left join parceiro on parceiro.codigo = poaci.parceirosacado ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = poaci.operadoracartaosacado ");
		sql.append(" left join banco bancosacado on bancosacado.codigo = poaci.bancosacado ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaRapidaPorParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE case when (poaci.tipomovimentacaofinanceira  = '").append(TipoMovimentacaoFinanceira.ENTRADA.name()).append("' and  poaci.origemExtratoContaCorrenteEnum = '").append(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA.name()).append("') ");
		sql.append(" then ( ");
		sql.append(" ( trim(leading '0' from contadestino.numero) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" or (trim(leading '0' from contadestino.numero)||contadestino.digito) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" or (trim(leading '0' from agenciadestino.numeroagencia)||contadestino.numero||contadestino.digito) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" ) ");
		if (Uteis.isAtributoPreenchido(obj.getDigitoContaCorrenteArquivo())) {
			sql.append(" and contadestino.digito = '").append(obj.getDigitoContaCorrenteArquivo()).append("' ");
		}
		sql.append(" ) else ( ");
		sql.append(" ( trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0")).append("' ");
		sql.append(" ) ");
		if (Uteis.isAtributoPreenchido(obj.getDigitoContaCorrenteArquivo())) {
			sql.append(" and contacorrente.digito = '").append(obj.getDigitoContaCorrenteArquivo()).append("' ");
		}
		sql.append(" ) end  ");
		sql.append(" ORDER BY tipomovimentacaofinanceira, nomelancamento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			ParametrizarOperacoesAutomaticasConciliacaoItemVO poaci = consultarConciliacaoContaCorrenteDiaVO(obj, tabelaResultado.getString("poaci.nomelancamento"), TipoMovimentacaoFinanceira.valueOf(tabelaResultado.getString("poaci.tipomovimentacaofinanceira"))); 
			if(!Uteis.isAtributoPreenchido(poaci)){
				montarDadosBasico(poaci, tabelaResultado);
				if(poaci.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()){
					obj.getListaParametrizarEntradaItens().add(poaci);
				}else{
					obj.getListaParametrizarSaidaItens().add(poaci);
				}	
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ParametrizarOperacoesAutomaticasConciliacaoItemVO consultarConciliacaoContaCorrenteDiaVO(ConciliacaoContaCorrenteVO obj, String nomeLancamento, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira) throws Exception {
	
		if(tipoMovimentacaoFinanceira.isMovimentacaoEntrada()){
 			return obj.getListaParametrizarEntradaItens()
 			.stream()
 			.filter(p-> p.getNomeLancamento().equals(nomeLancamento))
 			.findFirst()
 			.orElse(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
		}else{
			return obj.getListaParametrizarSaidaItens()
 			.stream()
 			.filter(p-> p.getNomeLancamento().equals(nomeLancamento))
 			.findFirst()
 			.orElse(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(ParametrizarOperacoesAutomaticasConciliacaoItemVO poaci, SqlRowSet dadosSQL) throws Exception {
		poaci.setCodigo(dadosSQL.getInt("poaci.codigo"));
		poaci.setNomeLancamento(dadosSQL.getString("poaci.nomelancamento"));
		poaci.setOrigemExtratoContaCorrenteEnum(OrigemExtratoContaCorrenteEnum.valueOf(dadosSQL.getString("poaci.origemExtratoContaCorrenteEnum")));
		poaci.setTipoFormaPagamento(TipoFormaPagamento.valueOf(dadosSQL.getString("poaci.tipoformapagamento")));		
		poaci.setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.valueOf(dadosSQL.getString("poaci.tipomovimentacaofinanceira")));
		
		poaci.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		poaci.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		poaci.getFormaPagamentoVO().setCodigo(dadosSQL.getInt("formapagamento.codigo"));
		poaci.getFormaPagamentoVO().setNome(dadosSQL.getString("formapagamento.nome"));
		poaci.getFormaPagamentoVO().setTipo(poaci.getTipoFormaPagamento().getValor());
		
		poaci.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contacorrente.codigo"));
		poaci.getContaCorrenteVO().setNumero(dadosSQL.getString("contacorrente.numero"));
		poaci.getContaCorrenteVO().setDigito(dadosSQL.getString("contacorrente.digito"));
		poaci.getContaCorrenteVO().setCarteira(dadosSQL.getString("contacorrente.carteira"));
		poaci.getContaCorrenteVO().setContaCaixa(dadosSQL.getBoolean("contacorrente.contacaixa"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("contacorrente.tipoContaCorrente"))) {
			poaci.getContaCorrenteVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contacorrente.tipoContaCorrente")));
		}
		
		poaci.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("agencia.codigo"));
		poaci.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroagencia"));
		poaci.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		
		poaci.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		poaci.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));		
		
		if(poaci.getOrigemExtratoContaCorrenteEnum().isMovimentacaoFinanceira()){
			poaci.getContaCorrenteDestinoVO().setCodigo(dadosSQL.getInt("contadestino.codigo"));
			poaci.getContaCorrenteDestinoVO().setNumero(dadosSQL.getString("contadestino.numero"));
			poaci.getContaCorrenteDestinoVO().setDigito(dadosSQL.getString("contadestino.digito"));
			poaci.getContaCorrenteDestinoVO().setCarteira(dadosSQL.getString("contadestino.carteira"));
			poaci.getContaCorrenteDestinoVO().setContaCaixa(dadosSQL.getBoolean("contadestino.contacaixa"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contadestino.tipoContaCorrente"))) {
				poaci.getContaCorrenteDestinoVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contadestino.tipoContaCorrente")));
			}
			
			poaci.getContaCorrenteDestinoVO().getAgencia().setCodigo(dadosSQL.getInt("agenciadestino.codigo"));
			poaci.getContaCorrenteDestinoVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agenciadestino.numeroagencia"));
			poaci.getContaCorrenteDestinoVO().getAgencia().setDigito(dadosSQL.getString("agenciadestino.digito"));
			
			poaci.getContaCorrenteDestinoVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancodestino.codigo"));
			poaci.getContaCorrenteDestinoVO().getAgencia().getBanco().setNome(dadosSQL.getString("bancodestino.nome"));
		}else{
			poaci.setTipoSacado(TipoSacadoExtratoContaCorrenteEnum.valueOf(dadosSQL.getString("poaci.tiposacado")));			
			
			poaci.getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("categoriadespesa.codigo"));
			poaci.getCategoriaDespesaVO().setDescricao(dadosSQL.getString("categoriadespesa.descricao"));
			
			poaci.getCentroResultadoAdministrativo().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
			poaci.getCentroResultadoAdministrativo().setDescricao(dadosSQL.getString("centroResultado.descricao"));
			
			poaci.getCentroReceitaVO().setCodigo(dadosSQL.getInt("centroreceita.codigo"));
			poaci.getCentroReceitaVO().setDescricao(dadosSQL.getString("centroreceita.descricao"));
			
			poaci.getFuncionarioSacado().setCodigo(dadosSQL.getInt("funcionario.codigo"));
			poaci.getFuncionarioSacado().setMatricula(dadosSQL.getString("funcionario.matricula"));
			poaci.getFuncionarioSacado().getPessoa().setCodigo(dadosSQL.getInt("pessoafunc.codigo"));
			poaci.getFuncionarioSacado().getPessoa().setNome(dadosSQL.getString("pessoafunc.nome"));
			
			poaci.getFornecedorSacado().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
			poaci.getFornecedorSacado().setNome(dadosSQL.getString("fornecedor.nome"));
			
			poaci.getParceiroSacado().setCodigo(dadosSQL.getInt("parceiro.codigo"));
			poaci.getParceiroSacado().setNome(dadosSQL.getString("parceiro.nome"));
			
			poaci.getBancoSacado().setCodigo(dadosSQL.getInt("bancosacado.codigo"));
			poaci.getBancoSacado().setNome(dadosSQL.getString("bancosacado.nome"));
			
			poaci.getOperadoraCartaoSacado().setCodigo(dadosSQL.getInt("operadoracartao.codigo"));
			poaci.getOperadoraCartaoSacado().setNome(dadosSQL.getString("operadoracartao.nome"));
			
			
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ParametrizarOperacoesAutomaticasConciliacaoItem.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ParametrizarOperacoesAutomaticasConciliacaoItem.idEntidade = idEntidade;
	}

}
