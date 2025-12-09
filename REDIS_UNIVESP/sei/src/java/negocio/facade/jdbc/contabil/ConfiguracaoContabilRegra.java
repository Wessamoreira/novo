package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.ConfiguracaoContabilRegraInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoContabilRegra extends ControleAcesso implements ConfiguracaoContabilRegraInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2802256453179043666L;
	protected static String idEntidade;

	public ConfiguracaoContabilRegra() throws Exception {
		super();
		setIdEntidade("ConfiguracaoContabilRegra");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConfiguracaoContabilRegraVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConfiguracaoContabilRegraVO obj : lista) {
			Uteis.checkState(validarUnicidade(obj, usuarioVO), "Existe uma "+obj.getTipoRegraContabilEnumApresentar()+" da configuração contábil em Duplicidade.");
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConfiguracaoContabilRegraPlanoContaVO(), "ConfiguracaoContabilRegraPlanoConta", "ConfiguracaoContabilRegra", obj.getCodigo(), usuarioVO);
			getFacadeFactory().getConfiguracaoContabilRegraPlanoContaFacade().persistir(obj.getListaConfiguracaoContabilRegraPlanoContaVO(), false, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoContabilRegraVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegra.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ConfiguracaoContabilRegra (configuracaoContabil, tipoRegraContabil, contaCorrenteOrigem, contaCorrenteDestino, ");
			sql.append("    formaPagamento, operadoraCartao, curso, turno, tipoOrigemContaReceber, ");
			sql.append("    tipoSacadoReceber, centroReceita, considerarValorDataCompensacao, origemContaPagar, tipoSacadoPagar,  categoriaDespesa, ");
			sql.append("    codigosacado, tipodesconto, categoriaProduto, imposto, planoConta ) ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ? , ?, ? ) ");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getConfiguracaoContabilVO().getCodigo());
					sqlInserir.setString(++i, obj.getTipoRegraContabilEnum().name());

					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())) {
						sqlInserir.setInt(++i, obj.getContaCorrenteOrigemVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteDestinoVO())) {
						sqlInserir.setInt(++i, obj.getContaCorrenteDestinoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())) {
						sqlInserir.setInt(++i, obj.getFormaPagamentoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())) {
						sqlInserir.setInt(++i, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlInserir.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getTurnoVO())) {
						sqlInserir.setInt(++i, obj.getTurnoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getTipoOrigemContaReceber())) {
						sqlInserir.setString(++i, obj.getTipoOrigemContaReceber().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber())) {
						sqlInserir.setString(++i, obj.getTipoSacadoReceber().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getCentroReceitaVO())) {
						sqlInserir.setInt(++i, obj.getCentroReceitaVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					sqlInserir.setBoolean(++i, obj.isConsiderarValorDataCompensacao());

					if (Uteis.isAtributoPreenchido(obj.getOrigemContaPagar())) {
						sqlInserir.setString(++i, obj.getOrigemContaPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar())) {
						sqlInserir.setString(++i, obj.getTipoSacadoPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())) {
						sqlInserir.setInt(++i, obj.getCategoriaDespesaVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getCodigoSacado())) {
						sqlInserir.setInt(++i, obj.getCodigoSacado());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())) {
						sqlInserir.setString(++i, obj.getTipoDescontoEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCategoriaProdutoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPlanoContaVO(), ++i, sqlInserir);
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
	public void alterar(final ConfiguracaoContabilRegraVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegra.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConfiguracaoContabilRegra ");
			sql.append("   SET configuracaoContabil=?, tipoRegraContabil=?, contaCorrenteOrigem=?, contaCorrenteDestino=?,");
			sql.append("    formaPagamento=?, operadoraCartao=?, curso=?, turno=?, tipoOrigemContaReceber=?, ");
			sql.append("    tipoSacadoReceber=?, centroReceita=?, considerarValorDataCompensacao=?, origemContaPagar=?, tipoSacadoPagar=?,  categoriaDespesa=?, ");
			sql.append("    codigosacado=?, tipodesconto=?, categoriaProduto=?, imposto=?, planoConta=? ");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getConfiguracaoContabilVO().getCodigo());
					sqlAlterar.setString(++i, obj.getTipoRegraContabilEnum().name());

					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())) {
						sqlAlterar.setInt(++i, obj.getContaCorrenteOrigemVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteDestinoVO())) {
						sqlAlterar.setInt(++i, obj.getContaCorrenteDestinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())) {
						sqlAlterar.setInt(++i, obj.getFormaPagamentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())) {
						sqlAlterar.setInt(++i, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlAlterar.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTurnoVO())) {
						sqlAlterar.setInt(++i, obj.getTurnoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoOrigemContaReceber())) {
						sqlAlterar.setString(++i, obj.getTipoOrigemContaReceber().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber())) {
						sqlAlterar.setString(++i, obj.getTipoSacadoReceber().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCentroReceitaVO())) {
						sqlAlterar.setInt(++i, obj.getCentroReceitaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.isConsiderarValorDataCompensacao());
					if (Uteis.isAtributoPreenchido(obj.getOrigemContaPagar())) {
						sqlAlterar.setString(++i, obj.getOrigemContaPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar())) {
						sqlAlterar.setString(++i, obj.getTipoSacadoPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())) {
						sqlAlterar.setInt(++i, obj.getCategoriaDespesaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoSacado())) {
						sqlAlterar.setInt(++i, obj.getCodigoSacado());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())) {
						sqlAlterar.setString(++i, obj.getTipoDescontoEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCategoriaProdutoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPlanoContaVO(), ++i, sqlAlterar);
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
	public void excluir(ConfiguracaoContabilRegraVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegra.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM ConfiguracaoContabilRegra WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	private ConfiguracaoContabilRegraVO consultarConfiguracaoContabilRegraPorTipoRegra(ConfiguracaoContabilVO obj, Integer codigo, TipoRegraContabilEnum tipoRegraContabilEnum) throws Exception {
		if (tipoRegraContabilEnum.isDesconto()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasDesconto(), codigo);
		} else if (tipoRegraContabilEnum.isDescontoPagar()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasDescontoPagar(), codigo);
		} else if (tipoRegraContabilEnum.isJuroMultaAcrescimo()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasJuroMultaAcrescimo(), codigo);
		} else if (tipoRegraContabilEnum.isJuroMultaPagar()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasJuroMultaPagar(), codigo);
		} else if (tipoRegraContabilEnum.isMovimentacaoFinanceira()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasMovimentacaoFinanceira(), codigo);
		} else if (tipoRegraContabilEnum.isPagamento()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasPagamento(), codigo);
		} else if (tipoRegraContabilEnum.isRecebimento()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasRecebimento(), codigo);
		} else if (tipoRegraContabilEnum.isNotaFiscaEntradaCategoriaProduto()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), codigo);
		} else if (tipoRegraContabilEnum.isSacado()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasSacado(), codigo);
		} else if (tipoRegraContabilEnum.isNotaFiscaEntradaImposto()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), codigo);
		} else if (tipoRegraContabilEnum.isTaxaCartoes()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasTaxaCartoes(), codigo);
		} else if (tipoRegraContabilEnum.isCartaoCredito()) {
			return consultarConfiguracaoContabilRegraVO(obj.getListaContabilRegrasCartaoCredito(), codigo);
		}
		return new ConfiguracaoContabilRegraVO();
	}

	private ConfiguracaoContabilRegraVO consultarConfiguracaoContabilRegraVO(List<ConfiguracaoContabilRegraVO> lista, Integer codigo)  {
		return lista.stream().filter(p -> p.getCodigo().equals(codigo)).findFirst().orElse(new ConfiguracaoContabilRegraVO());
	}

	private void addConfiguracaoContabilRegraPorTipoRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO ccr)  {
		if (ccr.getTipoRegraContabilEnum().isDesconto()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasDesconto(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isDescontoPagar()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasDescontoPagar(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isJuroMultaAcrescimo()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasJuroMultaAcrescimo(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isJuroMultaPagar()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasJuroMultaPagar(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isMovimentacaoFinanceira()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasMovimentacaoFinanceira(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isPagamento()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasPagamento(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isRecebimento()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasRecebimento(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isNotaFiscaEntradaCategoriaProduto()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isSacado()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasSacado(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isNotaFiscaEntradaImposto()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasNotaFiscalEntradaImposto(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isTaxaCartoes()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasTaxaCartoes(), ccr);
		} else if (ccr.getTipoRegraContabilEnum().isCartaoCredito()) {
			addConfiguracaoContabilRegraVO(obj.getListaContabilRegrasCartaoCredito(), ccr);
		}
		
	}

	private void addConfiguracaoContabilRegraVO(List<ConfiguracaoContabilRegraVO> lista, ConfiguracaoContabilRegraVO ccr)  {
		int index = 0;
		for (ConfiguracaoContabilRegraVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(ccr)) {
				lista.set(index, ccr);
				return;
			}
			index++;
		}
		lista.add(ccr);
	}

	
	private Boolean validarUnicidade(ConfiguracaoContabilRegraVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM ConfiguracaoContabilRegra ");
		sql.append(" WHERE tipoRegraContabil = '").append(obj.getTipoRegraContabilEnum().name()).append("' ");
		switch (obj.getTipoRegraContabilEnum()) {
		case RECEBIMENTO:
			if(Uteis.isAtributoPreenchido(obj.getTipoOrigemContaReceber())){
				sql.append(" and  tipoOrigemContaReceber = '").append(obj.getTipoOrigemContaReceber().name()).append("' ");	
			}else{
				sql.append(" and  tipoOrigemContaReceber is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber())){
				sql.append(" and  tipoSacadoReceber = '").append(obj.getTipoSacadoReceber().name()).append("' ");	
			}else{
				sql.append(" and  tipoSacadoReceber is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCodigoSacado())){
				sql.append(" and  codigosacado = ").append(obj.getCodigoSacado()).append(" ");	
			}else{
				sql.append(" and  codigosacado is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())){
				sql.append(" and  contaCorrenteOrigem = ").append(obj.getContaCorrenteOrigemVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  contaCorrenteOrigem is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.isConsiderarValorDataCompensacao())){
				sql.append(" and  considerarValorDataCompensacao = true ");	
			}else{
				sql.append(" and  considerarValorDataCompensacao = false ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCursoVO())){
				sql.append(" and  curso = ").append(obj.getCursoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  curso is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTurnoVO())){
				sql.append(" and  turno = ").append(obj.getTurnoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  turno is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())){
				sql.append(" and  formaPagamento = ").append(obj.getFormaPagamentoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  formaPagamento is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())){
				sql.append(" and  operadoraCartao = ").append(obj.getOperadoraCartaoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  operadoraCartao is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCentroReceitaVO())){
				sql.append(" and  centroReceita = ").append(obj.getCentroReceitaVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  centroReceita is null ");
			}
			break;
		case TAXA_CARTAOES:
			if(Uteis.isAtributoPreenchido(obj.getCursoVO())){
				sql.append(" and  curso = ").append(obj.getCursoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  curso is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTurnoVO())){
				sql.append(" and  turno = ").append(obj.getTurnoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  turno is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())){
				sql.append(" and  formaPagamento = ").append(obj.getFormaPagamentoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  formaPagamento is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())){
				sql.append(" and  operadoraCartao = ").append(obj.getOperadoraCartaoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  operadoraCartao is null ");
			}			
			break;
		case CARTAO_CREDITO:
			if(Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())){
				sql.append(" and  contaCorrenteOrigem = ").append(obj.getContaCorrenteOrigemVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  contaCorrenteOrigem is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())){
				sql.append(" and  formaPagamento = ").append(obj.getFormaPagamentoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  formaPagamento is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())){
				sql.append(" and  operadoraCartao = ").append(obj.getOperadoraCartaoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  operadoraCartao is null ");
			}			
			break;
		case JURO_MULTA_ACRESCIMO:
			if(Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())){
				sql.append(" and  tipodesconto = '").append(obj.getTipoDescontoEnum().name()).append("' ");	
			}else{
				sql.append(" and  tipodesconto is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCursoVO())){
				sql.append(" and  curso = ").append(obj.getCursoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  curso is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTurnoVO())){
				sql.append(" and  turno = ").append(obj.getTurnoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  turno is null ");
			}
			break;
		case DESCONTO:
			if(Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())){
				sql.append(" and  tipodesconto = '").append(obj.getTipoDescontoEnum().name()).append("' ");	
			}else{
				sql.append(" and  tipodesconto is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTipoSacadoReceber())){
				sql.append(" and  tipoSacadoReceber = '").append(obj.getTipoSacadoReceber().name()).append("' ");	
			}else{
				sql.append(" and  tipoSacadoReceber is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCodigoSacado())){
				sql.append(" and  codigosacado = ").append(obj.getCodigoSacado()).append(" ");	
			}else{
				sql.append(" and  codigosacado is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCursoVO())){
				sql.append(" and  curso = ").append(obj.getCursoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  curso is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTurnoVO())){
				sql.append(" and  turno = ").append(obj.getTurnoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  turno is null ");
			}
			break;
		case PAGAMENTO:
			if(Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())){
				sql.append(" and  contaCorrenteOrigem = ").append(obj.getContaCorrenteOrigemVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  contaCorrenteOrigem is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getOrigemContaPagar())){
				sql.append(" and  origemContaPagar = '").append(obj.getOrigemContaPagar().name()).append("' ");	
			}else{
				sql.append(" and  origemContaPagar is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())){
				sql.append(" and  formaPagamento = ").append(obj.getFormaPagamentoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  formaPagamento is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO())){
				sql.append(" and  operadoraCartao = ").append(obj.getOperadoraCartaoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  operadoraCartao is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())){
				sql.append(" and  categoriaDespesa = ").append(obj.getCategoriaDespesaVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  categoriaDespesa is null ");
			}
			break;
		case MOVIMENTACAO_FINANCEIRA:
			if(Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigemVO())){
				sql.append(" and  contaCorrenteOrigem = ").append(obj.getContaCorrenteOrigemVO().getCodigo()).append(" ");	
			}
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())){
				sql.append(" and  formaPagamento = ").append(obj.getFormaPagamentoVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  formaPagamento  is null");
			}
			break;
		case NOTA_FISCAL_ENTRADA_IMPOSTO:
			if(Uteis.isAtributoPreenchido(obj.getImpostoVO())){
				sql.append(" and  imposto = ").append(obj.getImpostoVO().getCodigo()).append(" ");	
			}
			break;
		case NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO:
			if(Uteis.isAtributoPreenchido(obj.getCategoriaProdutoVO())){
				sql.append(" and  categoriaProduto = ").append(obj.getCategoriaProdutoVO().getCodigo()).append(" ");	
			}
			break;
		case SACADO:
			if(Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())){
				sql.append(" and  categoriaDespesa = ").append(obj.getCategoriaDespesaVO().getCodigo()).append(" ");	
			}else{
				sql.append(" and  categoriaDespesa is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar())){
				sql.append(" and  tipoSacadoPagar = '").append(obj.getTipoSacadoPagar().name()).append("' ");	
			}else{
				sql.append(" and  tipoSacadoPagar is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCodigoSacado())){
				sql.append(" and  codigosacado = ").append(obj.getCodigoSacado()).append(" ");	
			}else{
				sql.append(" and  codigosacado is null ");
			}
			break;
		case JURO_MULTA_PAGAR:
			if(Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())){
				sql.append(" and  tipodesconto = '").append(obj.getTipoDescontoEnum().name()).append("' ");	
			}
			break;
		case DESCONTO_PAGAR:
			if(Uteis.isAtributoPreenchido(obj.getTipoDescontoEnum())){
				sql.append(" and  tipodesconto = '").append(obj.getTipoDescontoEnum().name()).append("' ");	
			}
			if(Uteis.isAtributoPreenchido(obj.getTipoSacadoPagar())){
				sql.append(" and  tipoSacadoPagar = '").append(obj.getTipoSacadoPagar().name()).append("' ");	
			}else{
				sql.append(" and  tipoSacadoPagar is null ");
			}
			if(Uteis.isAtributoPreenchido(obj.getCodigoSacado())){
				sql.append(" and  codigosacado = ").append(obj.getCodigoSacado()).append(" ");	
			}else{
				sql.append(" and  codigosacado is null ");
			}
			break;
		default:
			break;
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append(" ccr.codigo as \"ccr.codigo\", ccr.tipoRegraContabil as \"ccr.tipoRegraContabil\", ccr.tipoOrigemContaReceber as \"ccr.tipoOrigemContaReceber\",  ");
		sql.append(" ccr.tipoSacadoReceber as \"ccr.tipoSacadoReceber\", ccr.considerarValorDataCompensacao as \"ccr.considerarValorDataCompensacao\", ccr.origemContaPagar as \"ccr.origemContaPagar\",  ");
		sql.append(" ccr.tipoSacadoPagar as \"ccr.tipoSacadoPagar\", ccr.configuracaoContabil as \"ccr.configuracaoContabil\", ccr.codigosacado as \"ccr.codigosacado\", ");
		sql.append(" ccr.tipodesconto as \"ccr.tipodesconto\", ");

		sql.append(" cco.codigo as \"cco.codigo\", cco.numero as \"cco.numero\", cco.digito as \"cco.digito\",  cco.contacaixa as \"cco.contacaixa\",  cco.tipoContaCorrente as \"cco.tipoContaCorrente\", cco.nomeapresentacaosistema as \"cco.nomeapresentacaosistema\", ");
		sql.append(" ccd.codigo as \"ccd.codigo\", ccd.numero as \"ccd.numero\", ccd.digito as \"ccd.digito\",  ccd.contacaixa as \"ccd.contacaixa\",  ccd.tipoContaCorrente as \"ccd.tipoContaCorrente\", ccd.nomeapresentacaosistema as \"ccd.nomeapresentacaosistema\", ");

		sql.append(" fp.codigo as \"fp.codigo\", fp.nome as \"fp.nome\", fp.tipo as \"fp.tipo\", ");
		sql.append(" oc.codigo as \"oc.codigo\", oc.nome as \"oc.nome\",  ");

		sql.append(" planoconta.codigo as \"planoconta.codigo\", planoconta.identificadorplanoconta as \"planoconta.identificadorplanoconta\", planoconta.descricao as \"planoconta.descricao\",   ");

		sql.append(" curso.codigo as \"curso.codigo\", curso.descricao as \"curso.descricao\", curso.nome as \"curso.nome\", curso.abreviatura as \"curso.abreviatura\", ");

		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");

		sql.append(" centroreceita.codigo as \"centroreceita.codigo\", centroreceita.identificadorcentroreceita as \"centroreceita.identificadorcentroreceita\", centroreceita.descricao as \"centroreceita.descricao\",  ");

		sql.append(" categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.identificadorcategoriadespesa as \"categoriadespesa.identificadorcategoriadespesa\", categoriadespesa.descricao as \"categoriadespesa.descricao\",  ");

		sql.append(" parceiroreceber.codigo as \"parceiroreceber.codigo\", parceiroreceber.nome as \"parceiroreceber.nome\", ");

		sql.append(" fornecedorreceber.codigo as \"fornecedorreceber.codigo\", fornecedorreceber.nome as \"fornecedorreceber.nome\", ");

		sql.append(" parceiropagar.codigo as \"parceiropagar.codigo\", parceiropagar.nome as \"parceiropagar.nome\", ");

		sql.append(" fornecedorpagar.codigo as \"fornecedorpagar.codigo\", fornecedorpagar.nome as \"fornecedorpagar.nome\", ");

		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");

		sql.append(" funcionario.codigo as \"funcionario.codigo\", funcionario.matricula as \"funcionario.matricula\",  ");

		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");

		sql.append(" ccrpc.codigo as \"ccrpc.codigo\",  ");

		sql.append(" pcc.codigo as \"pcc.codigo\", pcc.identificadorplanoconta as \"pcc.identificadorplanoconta\", pcc.descricao as \"pcc.descricao\",   ");
		sql.append(" pcd.codigo as \"pcd.codigo\", pcd.identificadorplanoconta as \"pcd.identificadorplanoconta\", pcd.descricao as \"pcd.descricao\",  ");

		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\", ");
		sql.append(" imposto.codigo as \"imposto.codigo\", imposto.nome as \"imposto.nome\" ");

		sql.append(" FROM ConfiguracaoContabilRegra ccr  ");
		sql.append(" left join planoconta on planoconta.codigo = ccr.planoconta  ");
		sql.append(" left join contacorrente cco on cco.codigo = ccr.contacorrenteorigem  ");
		sql.append(" left join contacorrente ccd on ccd.codigo = ccr.contacorrentedestino  ");
		sql.append(" left join formapagamento fp on fp.codigo = ccr.formapagamento  ");
		sql.append(" left join operadoraCartao oc on oc.codigo = ccr.operadoracartao  ");
		sql.append(" left join curso on curso.codigo = ccr.curso  ");
		sql.append(" left join turno on turno.codigo = ccr.turno  ");
		sql.append(" left join centroreceita on centroreceita.codigo = ccr.centroreceita  ");
		sql.append(" left join categoriadespesa on categoriadespesa.codigo = ccr.categoriadespesa  ");
		sql.append(" left join categoriaproduto on categoriaproduto.codigo = ccr.categoriaproduto  ");
		sql.append(" left join imposto on imposto.codigo = ccr.imposto  ");
		sql.append(" left join parceiro as parceiroreceber on (case when tiposacadoreceber = 'PARCEIRO' then parceiroreceber.codigo = codigosacado else  parceiroreceber.codigo = 0 end) ");
		sql.append(" left join fornecedor as fornecedorreceber on (case when tiposacadoreceber = 'FORNECEDOR' then fornecedorreceber.codigo = codigosacado else  fornecedorreceber.codigo = 0 end) ");
		sql.append(" left join parceiro as parceiropagar on (case when tipoSacadoPagar = 'PARCEIRO' then parceiropagar.codigo = codigosacado else  parceiropagar.codigo = 0 end) ");
		sql.append(" left join fornecedor as fornecedorpagar on (case when tipoSacadoPagar = 'FORNECEDOR' then fornecedorpagar.codigo = codigosacado else  fornecedorpagar.codigo = 0 end) ");
		sql.append(" left join banco on (case when tipoSacadoPagar = 'BANCO' then banco.codigo = codigosacado else  banco.codigo = 0 end) ");
		sql.append(" left join funcionario on (case when tipoSacadoPagar = 'FUNCIONARIO_PROFESSOR' then funcionario.codigo = codigosacado else  funcionario.codigo = 0 end) ");
		sql.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sql.append(" left join ConfiguracaoContabilRegraPlanoConta ccrpc on ccrpc.configuracaoContabilRegra = ccr.codigo ");
		sql.append(" left join planoconta pcc on pcc.codigo = ccrpc.planoContaCredito  ");
		sql.append(" left join planoconta pcd on pcd.codigo = ccrpc.planoContaDebito  ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaRapidaPorConfiguracaoContabil(ConfiguracaoContabilVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ccr.configuracaoContabil = ").append(obj.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			ConfiguracaoContabilRegraVO ccr = consultarConfiguracaoContabilRegraPorTipoRegra(obj, tabelaResultado.getInt("ccr.codigo"), TipoRegraContabilEnum.valueOf(tabelaResultado.getString("ccr.tipoRegraContabil")));
			if (!Uteis.isAtributoPreenchido(ccr)) {
				ccr.setConfiguracaoContabilVO(obj);
				montarDadosBasico(tabelaResultado, ccr);
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("ccrpc.codigo"))) {

				getFacadeFactory().getConfiguracaoContabilRegraPlanoContaFacade().montarDadosBasicoConfiguracaoContabilRegraPlanoConta(tabelaResultado, ccr);
			}
			addConfiguracaoContabilRegraPorTipoRegra(obj, ccr);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(ConfiguracaoContabilVO obj, List<TipoRegraContabilEnum> listaTipoRegraContabil, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ccr.configuracaoContabil = ").append(obj.getCodigo());
		if (Uteis.isAtributoPreenchido(listaTipoRegraContabil)) {
			sql.append(" and ccr.tipoRegraContabil in ( ");
			StringBuilder sbIn = new StringBuilder();
			for (TipoRegraContabilEnum tipoRegraContabilEnum : listaTipoRegraContabil) {
				UteisTexto.addCampoParaClausaIn(sbIn, "'" + tipoRegraContabilEnum.name() + "'");
			}
			sql.append(sbIn).append(") ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			ConfiguracaoContabilRegraVO ccr = consultarConfiguracaoContabilRegraPorTipoRegra(obj, tabelaResultado.getInt("ccr.codigo"), TipoRegraContabilEnum.valueOf(tabelaResultado.getString("ccr.tipoRegraContabil")));
			if (!Uteis.isAtributoPreenchido(ccr)) {
				ccr.setConfiguracaoContabilVO(obj);
				montarDadosBasico(tabelaResultado, ccr);
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("ccrpc.codigo"))) {

				getFacadeFactory().getConfiguracaoContabilRegraPlanoContaFacade().montarDadosBasicoConfiguracaoContabilRegraPlanoConta(tabelaResultado, ccr);
			}
			addConfiguracaoContabilRegraPorTipoRegra(obj, ccr);
		}
	}

	private void montarDadosBasico(SqlRowSet dadosSQL, ConfiguracaoContabilRegraVO ccr) throws Exception {
		ccr.setNovoObj(Boolean.FALSE);
		ccr.setCodigo(dadosSQL.getInt("ccr.codigo"));
		ccr.setTipoRegraContabilEnum(TipoRegraContabilEnum.valueOf(dadosSQL.getString("ccr.tipoRegraContabil")));

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccr.tipodesconto"))) {
			ccr.setTipoDescontoEnum(TipoDesconto.valueOf(dadosSQL.getString("ccr.tipodesconto")));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccr.origemContaPagar"))) {
			ccr.setOrigemContaPagar(OrigemContaPagar.valueOf(dadosSQL.getString("ccr.origemContaPagar")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccr.tipoSacadoPagar"))) {
			ccr.setTipoSacadoPagar(TipoSacado.valueOf(dadosSQL.getString("ccr.tipoSacadoPagar")));
		}
		ccr.getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("categoriadespesa.codigo"));
		ccr.getCategoriaDespesaVO().setIdentificadorCategoriaDespesa(dadosSQL.getString("categoriadespesa.identificadorcategoriadespesa"));
		ccr.getCategoriaDespesaVO().setDescricao(dadosSQL.getString("categoriadespesa.descricao"));

		ccr.setConsiderarValorDataCompensacao(dadosSQL.getBoolean("ccr.considerarValorDataCompensacao"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccr.tipoOrigemContaReceber"))) {
			ccr.setTipoOrigemContaReceber(TipoOrigemContaReceber.valueOf(dadosSQL.getString("ccr.tipoOrigemContaReceber")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccr.tipoSacadoReceber"))) {
			ccr.setTipoSacadoReceber(TipoPessoa.valueOf(dadosSQL.getString("ccr.tipoSacadoReceber")));
		}

		ccr.getPlanoContaVO().setCodigo(dadosSQL.getInt("planoconta.codigo"));
		ccr.getPlanoContaVO().setIdentificadorPlanoConta(dadosSQL.getString("planoconta.identificadorplanoconta"));
		ccr.getPlanoContaVO().setDescricao(dadosSQL.getString("planoconta.descricao"));

		ccr.getCentroReceitaVO().setCodigo(dadosSQL.getInt("centroreceita.codigo"));
		ccr.getCentroReceitaVO().setIdentificadorCentroReceita(dadosSQL.getString("centroreceita.identificadorcentroreceita"));
		ccr.getCentroReceitaVO().setDescricao(dadosSQL.getString("centroreceita.descricao"));
		ccr.getImpostoVO().setCodigo(dadosSQL.getInt("imposto.codigo"));
		ccr.getImpostoVO().setNome(dadosSQL.getString("imposto.nome"));
		ccr.getCategoriaProdutoVO().setCodigo(dadosSQL.getInt("categoriaProduto.codigo"));
		ccr.getCategoriaProdutoVO().setNome(dadosSQL.getString("categoriaProduto.nome"));

		ccr.getContaCorrenteOrigemVO().setCodigo(dadosSQL.getInt("cco.codigo"));
		ccr.getContaCorrenteOrigemVO().setNumero(dadosSQL.getString("cco.numero"));
		ccr.getContaCorrenteOrigemVO().setDigito(dadosSQL.getString("cco.digito"));
		ccr.getContaCorrenteOrigemVO().setContaCaixa(dadosSQL.getBoolean("cco.contacaixa"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("cco.tipoContaCorrente"))) {
			ccr.getContaCorrenteOrigemVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("cco.tipoContaCorrente")));
		}

		ccr.getContaCorrenteOrigemVO().setNomeApresentacaoSistema(dadosSQL.getString("cco.nomeApresentacaoSistema"));

		ccr.getContaCorrenteDestinoVO().setCodigo(dadosSQL.getInt("ccd.codigo"));
		ccr.getContaCorrenteDestinoVO().setNumero(dadosSQL.getString("ccd.numero"));
		ccr.getContaCorrenteDestinoVO().setDigito(dadosSQL.getString("ccd.digito"));
		ccr.getContaCorrenteDestinoVO().setContaCaixa(dadosSQL.getBoolean("ccd.contacaixa"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccd.tipoContaCorrente"))) {
			ccr.getContaCorrenteDestinoVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("ccd.tipoContaCorrente")));
		}
		ccr.getContaCorrenteDestinoVO().setNomeApresentacaoSistema(dadosSQL.getString("ccd.nomeApresentacaoSistema"));

		ccr.getFormaPagamentoVO().setCodigo(dadosSQL.getInt("fp.codigo"));
		ccr.getFormaPagamentoVO().setNome(dadosSQL.getString("fp.nome"));
		ccr.getFormaPagamentoVO().setTipo(dadosSQL.getString("fp.tipo"));

		ccr.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("oc.codigo"));
		ccr.getOperadoraCartaoVO().setNome(dadosSQL.getString("oc.nome"));

		ccr.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		ccr.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		ccr.getCursoVO().setDescricao(dadosSQL.getString("curso.descricao"));
		ccr.getCursoVO().setAbreviatura(dadosSQL.getString("curso.abreviatura"));

		ccr.getTurnoVO().setCodigo(dadosSQL.getInt("turno.codigo"));
		ccr.getTurnoVO().setNome(dadosSQL.getString("turno.nome"));

		ccr.setCodigoSacado(dadosSQL.getInt("ccr.codigosacado"));
		if (Uteis.isAtributoPreenchido(ccr.getCodigoSacado())) {
			if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoReceber()) && ccr.getTipoSacadoReceber().isParceiro()) {
				ccr.getParceiroVO().setCodigo(dadosSQL.getInt("parceiroreceber.codigo"));
				ccr.getParceiroVO().setNome(dadosSQL.getString("parceiroreceber.nome"));
			} else if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoReceber()) && ccr.getTipoSacadoReceber().isFornecedor()) {
				ccr.getFornecedorVO().setCodigo(dadosSQL.getInt("fornecedorreceber.codigo"));
				ccr.getFornecedorVO().setNome(dadosSQL.getString("fornecedorreceber.nome"));
			} else if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoPagar()) && ccr.getTipoSacadoPagar().isBanco()) {
				ccr.getBancoVO().setCodigo(dadosSQL.getInt("banco.codigo"));
				ccr.getBancoVO().setNome(dadosSQL.getString("banco.nome"));
			} else if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoPagar()) && ccr.getTipoSacadoPagar().isFuncionario()) {
				ccr.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
				ccr.getFuncionarioVO().setMatricula(dadosSQL.getString("funcionario.matricula"));
				ccr.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
				ccr.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
			} else if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoPagar()) && ccr.getTipoSacadoPagar().isFornecedor()) {
				ccr.getFornecedorVO().setCodigo(dadosSQL.getInt("fornecedorpagar.codigo"));
				ccr.getFornecedorVO().setNome(dadosSQL.getString("fornecedorpagar.nome"));
			} else if (Uteis.isAtributoPreenchido(ccr.getTipoSacadoPagar()) && ccr.getTipoSacadoPagar().isParceiro()) {
				ccr.getParceiroVO().setCodigo(dadosSQL.getInt("parceiropagar.codigo"));
				ccr.getParceiroVO().setNome(dadosSQL.getString("parceiropagar.nome"));
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDadosConfiguracaoContabilRegraPlanoConta(ConfiguracaoContabilRegraPlanoContaVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getPlanoContaCreditoVO())) {
			throw new Exception("O campo Plano de Conta Crédito (Configuração Contábil) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getPlanoContaDebitoVO())) {
			throw new Exception("O campo Plano de Conta Dédito (Configuração Contábil) deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addConfiguracaoContabilRegraPlanoConta(ConfiguracaoContabilRegraVO obj, ConfiguracaoContabilRegraPlanoContaVO regraPlanoConta, UsuarioVO usuario) throws Exception {
		validarDadosConfiguracaoContabilRegraPlanoConta(regraPlanoConta);
		regraPlanoConta.setConfiguracaoContabilRegraVO(obj);
		int index = 0;
		for (ConfiguracaoContabilRegraPlanoContaVO objExistente : obj.getListaConfiguracaoContabilRegraPlanoContaVO()) {
			if (objExistente.equalsCampoSelecaoLista(regraPlanoConta)) {
				obj.getListaConfiguracaoContabilRegraPlanoContaVO().set(index, regraPlanoConta);
				return;
			}
			index++;
		}
		obj.getListaConfiguracaoContabilRegraPlanoContaVO().add(regraPlanoConta);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removeConfiguracaoContabilRegraPlanoConta(ConfiguracaoContabilRegraVO obj, ConfiguracaoContabilRegraPlanoContaVO regraPlanoConta, UsuarioVO usuario) throws Exception {
		Iterator<ConfiguracaoContabilRegraPlanoContaVO> i = obj.getListaConfiguracaoContabilRegraPlanoContaVO().iterator();
		while (i.hasNext()) {
			ConfiguracaoContabilRegraPlanoContaVO objExistente =  i.next();
			if (objExistente.equalsCampoSelecaoLista(regraPlanoConta)) {
				i.remove();
				return;
			}
		}
	}

	public static String getIdEntidade() {
		return ConfiguracaoContabilRegra.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoContabilRegra.idEntidade = idEntidade;
	}

}
