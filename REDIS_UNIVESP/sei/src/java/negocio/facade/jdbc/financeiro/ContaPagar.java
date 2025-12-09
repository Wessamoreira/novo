 package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.SituacaoLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoValorLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarPagamentoVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContratosDespesasVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.GrupoContaPagarVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadePixEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.sad.DespesaDW;
import negocio.interfaces.financeiro.ContaPagarInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaPagarVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaPagarVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ContaPagarVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaPagar extends ControleAcesso implements ContaPagarInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = 8739340707485357662L;
	protected static String idEntidade = "ContaPagar";

	public static String getIdEntidade() {
		return ContaPagar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ContaPagar.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final ContaPagarVO obj, Boolean validarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception {

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			this.incluir(obj, validarAcesso, validarCentroResultadoAposAlteracao, usuario);
		} else {
			this.alterar(obj, validarAcesso, validarCentroResultadoAposAlteracao, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContaPagarVO obj, Boolean validarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception {
		try {
			ContaPagarVO.validarDados(obj);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().validarDadosTotalizadoresAposAlteracao(obj.getListaCentroResultadoOrigemVOs(), obj.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), validarCentroResultadoAposAlteracao, usuario);
			ContaPagar.incluir(getIdEntidade(), validarAcesso, usuario);
			if (!Uteis.isAtributoPreenchido(obj.getResponsavel())) {
				obj.getResponsavel().setCodigo(usuario.getCodigo());
			}
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario);
			realizarVinculoContaReceberComResponsavelFinanceiro(obj, null);
			final String sql = "INSERT INTO ContaPagar( data, codOrigem, tipoOrigem, situacao, dataVencimento, valor, "
					+ " juro, multa, nrDocumento, parcela, fornecedor, "
					+ " origemRenegociacaoPagar, codigoBarra, contaCorrente, valorPago, "
					+ " unidadeEnsino, desconto, funcionario, tipoSacado, banco,"
					+ " descricao, responsavel, dataFatoGerador, "
					+ " pessoa, grupoContaPagar, matricula, parceiro, responsavelFinanceiro, updated, operadoracartao, "
					+ " convenio, bancoremessapagar, bancoRecebimento, numeroAgenciaRecebimento, digitoAgenciaRecebimento, contaCorrenteRecebimento, "
					+ " digitoCorrenteRecebimento, tipoServicoContaPagar, tipoLancamentoContaPagar, modalidadeTransferenciaBancariaEnum , finalidadedocenum, "
					+ " finalidadetedenum, codigoReceitaTributo, identificacaoContribuinte, tipoIdentificacaoContribuinte, numeroReferencia, "
					+ " valorReceitaBrutaAcumulada, percentualReceitaBrutaAcumulada,  "
					+ " linhadigitavel1, linhadigitavel2, linhadigitavel3, linhadigitavel4, linhadigitavel5, linhadigitavel6, linhadigitavel7,"
					+ " linhadigitavel8, observacao, formaPagamento, numeronotafiscalentrada, tipoContaEnum, descontoPorUsoAdiantamento, valorUtilizadoAdiantamento, codigonotafiscalentrada  , finalidadePixEnum  , tipoIdentificacaoChavePixEnum , chaveEnderecamentoPix ) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ,? , ? ) "
					+ "returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(++i, obj.getCodOrigem());
					sqlInserir.setString(++i, obj.getTipoOrigem());
					sqlInserir.setString(++i, obj.getSituacao());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(++i, obj.getValor().doubleValue());
					sqlInserir.setDouble(++i, obj.getJuro().doubleValue());
					sqlInserir.setDouble(++i, obj.getMulta().doubleValue());
					sqlInserir.setString(++i, obj.getNrDocumento());
					sqlInserir.setString(++i, obj.getParcela());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setInt(++i, obj.getOrigemRenegociacaoPagar().intValue());
					sqlInserir.setString(++i, obj.getCodigoBarra());
					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getContaCorrente().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDouble(++i, obj.getValorPago().doubleValue());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDouble(++i, obj.getDesconto().doubleValue());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoSacado());
					if (obj.getBanco().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getBanco().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getDescricao());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataFatoGerador()));
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getGrupoContaPagar().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getGrupoContaPagar().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (!Strings.isNullOrEmpty(obj.getMatricula())) {
						sqlInserir.setString(++i, obj.getMatricula());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getResponsavelFinanceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					obj.setUpdated(new Date());
					sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getUpdated()));
					if (obj.getOperadoraCartao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getOperadoraCartao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getConvenio().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getConvenio().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getBancoRemessaPagar().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getBancoRemessaPagar().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getBancoRecebimento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getBancoRecebimento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroAgenciaRecebimento());
					sqlInserir.setString(++i, obj.getDigitoAgenciaRecebimento());
					sqlInserir.setString(++i, obj.getContaCorrenteRecebimento());
					sqlInserir.setString(++i, obj.getDigitoCorrenteRecebimento());
					if (Uteis.isAtributoPreenchido(obj.getTipoServicoContaPagar())) {
						sqlInserir.setString(++i, obj.getTipoServicoContaPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
						sqlInserir.setString(++i, obj.getTipoLancamentoContaPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getModalidadeTransferenciaBancariaEnum())) {
						sqlInserir.setString(++i, obj.getModalidadeTransferenciaBancariaEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeDocEnum())) {
						sqlInserir.setString(++i, obj.getFinalidadeDocEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeTedEnum())) {
						sqlInserir.setString(++i, obj.getFinalidadeTedEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoReceitaTributo())) {
						sqlInserir.setString(++i, obj.getCodigoReceitaTributo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getIdentificacaoContribuinte())) {
						sqlInserir.setString(++i, obj.getIdentificacaoContribuinte());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoContribuinte())) {
						sqlInserir.setString(++i, obj.getTipoIdentificacaoContribuinte().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNumeroReferencia())) {
						sqlInserir.setString(++i, obj.getNumeroReferencia());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getValorReceitaBrutaAcumulada())) {
						sqlInserir.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPercentualReceitaBrutaAcumulada())) {
						sqlInserir.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getLinhaDigitavel1());
					sqlInserir.setString(++i, obj.getLinhaDigitavel2());
					sqlInserir.setString(++i, obj.getLinhaDigitavel3());
					sqlInserir.setString(++i, obj.getLinhaDigitavel4());
					sqlInserir.setString(++i, obj.getLinhaDigitavel5());
					sqlInserir.setString(++i, obj.getLinhaDigitavel6());
					sqlInserir.setString(++i, obj.getLinhaDigitavel7());
					sqlInserir.setString(++i, obj.getLinhaDigitavel8());
					sqlInserir.setString(++i, obj.getObservacao());
					Uteis.setValuePreparedStatement(obj.getFormaPagamentoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroNotaFiscalEntrada(), ++i, sqlInserir);
					if (Uteis.isAtributoPreenchido(obj.getTipoContaEnum())) {
						sqlInserir.setString(++i, obj.getTipoContaEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDouble(++i, obj.getDescontoPorUsoAdiantamento().doubleValue());
					sqlInserir.setDouble(++i, obj.getValorUtilizadoAdiantamento().doubleValue());
					Uteis.setValuePreparedStatement(obj.getCodigoNotaFiscalEntrada(), ++i, sqlInserir);
					if (Uteis.isAtributoPreenchido(obj.getFinalidadePixEnum())){
						sqlInserir.setString(++i, obj.getFinalidadePixEnum().name());
					}else {
						sqlInserir.setNull(++i, 0);
					}					
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())){
						sqlInserir.setString(++i, obj.getTipoIdentificacaoChavePixEnum().name());
					}else {
						sqlInserir.setNull(++i, 0);
					}			
					sqlInserir.setString(++i, obj.getChaveEnderecamentoPix());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			
			if (!obj.getArquivoVO().getNome().equals("")) {
				obj.getArquivoVO().setCodOrigem(obj.getCodigo());
				obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getServidorArquivoOnline()));
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario));
				alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
			}
		
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, false, usuario, false);
			getFacadeFactory().getContaPagarAdiantamentoFacade().incluirContaPagarAdiantamentos(obj, obj.getListaContaPagarAdiantamentoVO(), usuario);
			obj.setNovoObj(Boolean.FALSE);
			obj.setNossoNumero(new Long(obj.getCodigo()));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			if (!TipoSacado.FORNECEDOR.getValor().equals(obj.getTipoSacado())) {
				obj.setNrDocumento("");
			}
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirArquivoContaPagar(ContaPagarVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		removerVinculoArquivoContaReceber(obj, usuario);
		getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVO(), false, "Upload", usuario, configuracaoGeralSistemaVO);
		obj.setArquivoVO(new ArquivoVO());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoArquivoContaReceber(ContaPagarVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE contapagar set arquivo = null WHERE ((codigo = ?)) "  + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo()});
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoArquivo(ContaPagarVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE contapagar set arquivo=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { Uteis.isAtributoPreenchido(codArquivo) ? codArquivo : null, obj.getCodigo() });
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ContaPagarVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContaPagarVO obj, Boolean verificarAcesso, boolean validarCentroResultadoAposAlteracao, UsuarioVO usuario) throws Exception {
		final Date update = new Date();
		try {
			ContaPagar.alterar(getIdEntidade(), verificarAcesso, usuario);
			ContaPagarVO.validarDados(obj);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().validarDadosTotalizadoresAposAlteracao(obj.getListaCentroResultadoOrigemVOs(), obj.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), validarCentroResultadoAposAlteracao, usuario);
			if (!Uteis.isAtributoPreenchido(obj.getResponsavel())) {
				obj.getResponsavel().setCodigo(usuario.getCodigo());
			}
			if(!obj.getSituacao().equals("NE")) {
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataVencimentoAntesAlteracao(), obj.getDataFatoGerador(), obj.getDataCompetenciaAntesAlteracao(), obj.getUnidadeEnsino().getCodigo(), null, TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
			}
			
			validarSeContaPagarExisteVinculoComArquivoRemessa(obj);
			final String sql = "UPDATE ContaPagar set data=?, codOrigem=?, tipoOrigem=?, situacao=?, dataVencimento=?, "
					+ " valor=?, juro=?, multa=?, nrDocumento=?, parcela=?, "
					+ " fornecedor=?, origemRenegociacaoPagar=?, codigoBarra = ?, contaCorrente = ?, "
					+ " valorPago=?, unidadeEnsino = ?, desconto = ?, funcionario = ?, "
					+ "tipoSacado = ? , banco=?, "
					+ "descricao = ?, responsavel=?, dataFatoGerador=?, "
					+ " pessoa=?, grupoContaPagar=?, matricula=?, parceiro=?, responsavelFinanceiro = ?, "
					+ " updated = ?, operadoracartao=?, convenio=?, bancoremessapagar=?, bancorecebimento=?, numeroAgenciaRecebimento=?,"
					+ " digitoAgenciaRecebimento=?, contaCorrenteRecebimento=?, digitoCorrenteRecebimento=?, tipoServicoContaPagar=?, "
					+ " tipoLancamentoContaPagar=?, modalidadeTransferenciaBancariaEnum=?, finalidadedocenum=?, finalidadetedenum=?,"
					+ " codigoReceitaTributo=?, identificacaoContribuinte=?, tipoIdentificacaoContribuinte=?, numeroReferencia=?, "
					+ " valorReceitaBrutaAcumulada=?, percentualReceitaBrutaAcumulada=?, " + " linhadigitavel1=?, linhadigitavel2=?, "
					+ " linhadigitavel3=?, linhadigitavel4=?, linhadigitavel5=?, linhadigitavel6=?, linhadigitavel7=?, "
					+ " linhadigitavel8=?, nossonumero=?, observacao=?, formapagamento=?, numeronotafiscalentrada=?, tipoContaEnum=?, descontoPorUsoAdiantamento=?, valorUtilizadoAdiantamento=?, codigonotafiscalentrada=?  ,"
					+ " finalidadePixEnum=?  , tipoIdentificacaoChavePixEnum=? , chaveEnderecamentoPix=?  "
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(++i, obj.getCodOrigem());
					sqlAlterar.setString(++i, obj.getTipoOrigem());
					sqlAlterar.setString(++i, obj.getSituacao());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlAlterar.setDouble(++i, obj.getValor().doubleValue());
					sqlAlterar.setDouble(++i, obj.getJuro().doubleValue());
					sqlAlterar.setDouble(++i, obj.getMulta().doubleValue());
					sqlAlterar.setString(++i, obj.getNrDocumento());
					sqlAlterar.setString(++i, obj.getParcela());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setInt(++i, obj.getOrigemRenegociacaoPagar().intValue());
					sqlAlterar.setString(++i, obj.getCodigoBarra());
					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrente().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setDouble(++i, obj.getValorPago().doubleValue());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setDouble(++i, obj.getDesconto());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoSacado());
					if (obj.getBanco().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getBanco().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getDescricao());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataFatoGerador()));
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getGrupoContaPagar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getGrupoContaPagar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getTipoAluno() && obj.getMatricula() != null && !obj.getMatricula().trim().isEmpty()) {
						sqlAlterar.setString(++i, obj.getMatricula());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getResponsavelFinanceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}

					sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(update));
					if (obj.getOperadoraCartao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getOperadoraCartao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}

					if (obj.getConvenio().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getConvenio().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getBancoRemessaPagar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getBancoRemessaPagar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getBancoRecebimento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getBancoRecebimento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroAgenciaRecebimento());
					sqlAlterar.setString(++i, obj.getDigitoAgenciaRecebimento());
					sqlAlterar.setString(++i, obj.getContaCorrenteRecebimento());
					sqlAlterar.setString(++i, obj.getDigitoCorrenteRecebimento());
					if (Uteis.isAtributoPreenchido(obj.getTipoServicoContaPagar())) {
						sqlAlterar.setString(++i, obj.getTipoServicoContaPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
						sqlAlterar.setString(++i, obj.getTipoLancamentoContaPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getModalidadeTransferenciaBancariaEnum())) {
						sqlAlterar.setString(++i, obj.getModalidadeTransferenciaBancariaEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeDocEnum())) {
						sqlAlterar.setString(++i, obj.getFinalidadeDocEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeTedEnum())) {
						sqlAlterar.setString(++i, obj.getFinalidadeTedEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoReceitaTributo())) {
						sqlAlterar.setString(++i, obj.getCodigoReceitaTributo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getIdentificacaoContribuinte())) {
						sqlAlterar.setString(++i, obj.getIdentificacaoContribuinte());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoContribuinte())) {
						sqlAlterar.setString(++i, obj.getTipoIdentificacaoContribuinte().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNumeroReferencia())) {
						sqlAlterar.setString(++i, obj.getNumeroReferencia());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getValorReceitaBrutaAcumulada())) {
						sqlAlterar.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPercentualReceitaBrutaAcumulada())) {
						sqlAlterar.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getLinhaDigitavel1());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel2());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel3());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel4());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel5());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel6());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel7());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel8());
					sqlAlterar.setLong(++i, obj.getNossoNumero());
					sqlAlterar.setString(++i, obj.getObservacao());
					Uteis.setValuePreparedStatement(obj.getFormaPagamentoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNumeroNotaFiscalEntrada(), ++i, sqlAlterar);
					if (Uteis.isAtributoPreenchido(obj.getTipoContaEnum())) {
						sqlAlterar.setString(++i, obj.getTipoContaEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setDouble(++i, obj.getDescontoPorUsoAdiantamento().doubleValue());
					sqlAlterar.setDouble(++i, obj.getValorUtilizadoAdiantamento().doubleValue());
					Uteis.setValuePreparedStatement(obj.getCodigoNotaFiscalEntrada(), ++i, sqlAlterar);
					if (Uteis.isAtributoPreenchido(obj.getFinalidadePixEnum())){
						sqlAlterar.setString(++i, obj.getFinalidadePixEnum().name());
					}else {
						sqlAlterar.setNull(++i, 0);
					}					
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())){
						sqlAlterar.setString(++i, obj.getTipoIdentificacaoChavePixEnum().name());
					}else {
						sqlAlterar.setNull(++i, 0);
					}			
					sqlAlterar.setString(++i, obj.getChaveEnderecamentoPix());
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
			
			if (!Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo()) && !obj.getArquivoVO().getNome().equals("")) {
				obj.getArquivoVO().setCodOrigem(obj.getCodigo());
				obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getServidorArquivoOnline()));
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario));
				alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
			}
			getFacadeFactory().getContaPagarPagamentoFacade().incluirContaPagarPagamentos(obj.getCodigo(), obj.getContaPagarPagamentoVOs(), usuario);
			Boolean permitirGravarContaPagarIsenta = (obj.getSituacao().equals("PA") && obj.getValorPagamento().equals(0.0)) ? true : false;
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, false, usuario, permitirGravarContaPagarIsenta);
			getFacadeFactory().getContaPagarAdiantamentoFacade().incluirContaPagarAdiantamentos(obj, obj.getListaContaPagarAdiantamentoVO(), usuario);
			alterarOrigemContaPagar(obj, usuario);
			if (obj.getSituacao().equals(SituacaoFinanceira.PAGO.getValor())) {
				incluirPagamentoEmDespesaDW(obj, usuario);
			}
			obj.setUpdated(Uteis.getDataJDBCTimestamp(update));
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("check_validar_situacao_conta_pagar")) {
				throw new ConsistirException("A Situação da Conta a Pagar está desatualizada. Atualize os dados.");
			}
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorUtilizadoAdiantamento(final ContaPagarVO obj,  final Double valorUtilizado, final UsuarioVO usuario,  final boolean isOperacaoInclusao) throws Exception {
		synchronized (this) {
			final Date update = new Date();
			try {
				StringBuilder sqlUpdate = new StringBuilder(" UPDATE ContaPagar set updated = '").append(Uteis.getDataJDBCTimestamp(update)).append("', ");
				sqlUpdate.append(" valorUtilizadoAdiantamento = valorUtilizadoAdiantamento ");
				if(isOperacaoInclusao){
					sqlUpdate.append(" + ").append(valorUtilizado);
					sqlUpdate.append(" WHERE ContaPagar.codigo in (select cpu.codigo from contapagar  as cpu ");
					sqlUpdate.append(" inner join contapagaradiantamento on cpu.codigo = contapagaradiantamento.contapagarutilizada ");
					sqlUpdate.append(" where cpu.codigo = ").append(obj.getCodigo());
					sqlUpdate.append(" and cpu.valorpago > cpu.valorutilizadoadiantamento ) ");
				}else{
					sqlUpdate.append(" - ").append(valorUtilizado);
					sqlUpdate.append(" WHERE codigo =  ").append(obj.getCodigo());
				}
				sqlUpdate.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sqlUpdate.toString());	
				obj.setUpdated(update);
			} catch (Exception e) {
				throw e;
			}	
		}
	}
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorDescontoPorUsoAdiantamento(final ContaPagarVO obj,  final Double valorUtilizado, final UsuarioVO usuario) throws Exception {
		synchronized (this) {
			final Date update = new Date();
			try {
				StringBuilder sqlUpdate = new StringBuilder(" UPDATE ContaPagar set updated = '").append(Uteis.getDataJDBCTimestamp(update)).append("', ");
				sqlUpdate.append(" descontoporusoadiantamento = descontoporusoadiantamento  - ").append(valorUtilizado).append(" WHERE codigo =  ").append(obj.getCodigo());
				sqlUpdate.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sqlUpdate.toString());	
				obj.setUpdated(update);
			} catch (Exception e) {
				throw e;
			}	
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosRemessa(final ContaPagarVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		final Date update = new Date();
		try {
			final String sql = "UPDATE ContaPagar set "
					+ " updated = ?, bancoremessapagar=?, bancorecebimento=?, numeroAgenciaRecebimento=?,"
					+ " digitoAgenciaRecebimento=?, contaCorrenteRecebimento=?, digitoCorrenteRecebimento=?, tipoServicoContaPagar=?, "
					+ " tipoLancamentoContaPagar=?, modalidadeTransferenciaBancariaEnum=?, finalidadedocenum=?, finalidadetedenum=?,"
					+ " codigoReceitaTributo=?, identificacaoContribuinte=?, tipoIdentificacaoContribuinte=?, numeroReferencia=?, "
					+ " valorReceitaBrutaAcumulada=?, percentualReceitaBrutaAcumulada=?, " + " linhadigitavel1=?, linhadigitavel2=?, "
					+ " linhadigitavel3=?, linhadigitavel4=?, linhadigitavel5=?, linhadigitavel6=?, linhadigitavel7=?, "
					+ " linhadigitavel8=?, formapagamento=?,  tipoContaEnum=? "
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;					
					sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(update));
					if (obj.getBancoRemessaPagar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getBancoRemessaPagar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getBancoRecebimento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getBancoRecebimento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroAgenciaRecebimento());
					sqlAlterar.setString(++i, obj.getDigitoAgenciaRecebimento());
					sqlAlterar.setString(++i, obj.getContaCorrenteRecebimento());
					sqlAlterar.setString(++i, obj.getDigitoCorrenteRecebimento());
					if (Uteis.isAtributoPreenchido(obj.getTipoServicoContaPagar())) {
						sqlAlterar.setString(++i, obj.getTipoServicoContaPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
						sqlAlterar.setString(++i, obj.getTipoLancamentoContaPagar().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getModalidadeTransferenciaBancariaEnum())) {
						sqlAlterar.setString(++i, obj.getModalidadeTransferenciaBancariaEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeDocEnum())) {
						sqlAlterar.setString(++i, obj.getFinalidadeDocEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeTedEnum())) {
						sqlAlterar.setString(++i, obj.getFinalidadeTedEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoReceitaTributo())) {
						sqlAlterar.setString(++i, obj.getCodigoReceitaTributo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getIdentificacaoContribuinte())) {
						sqlAlterar.setString(++i, obj.getIdentificacaoContribuinte());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoContribuinte())) {
						sqlAlterar.setString(++i, obj.getTipoIdentificacaoContribuinte().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNumeroReferencia())) {
						sqlAlterar.setString(++i, obj.getNumeroReferencia());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getValorReceitaBrutaAcumulada())) {
						sqlAlterar.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPercentualReceitaBrutaAcumulada())) {
						sqlAlterar.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getLinhaDigitavel1());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel2());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel3());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel4());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel5());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel6());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel7());
					sqlAlterar.setString(++i, obj.getLinhaDigitavel8());
					Uteis.setValuePreparedStatement(obj.getFormaPagamentoVO(), ++i, sqlAlterar);
					if (Uteis.isAtributoPreenchido(obj.getTipoContaEnum())) {
						sqlAlterar.setString(++i, obj.getTipoContaEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});			
			obj.setUpdated(Uteis.getDataJDBCTimestamp(update));
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrigemContaPagar(ContaPagarVO contaPagar, UsuarioVO usuario) throws Exception {
		if (contaPagar.getTipoOrigemEnum().isCompra() || contaPagar.getTipoOrigemEnum().isRecebimentoCompra() || contaPagar.getTipoOrigemEnum().isNotaFiscalEntrada()) {
			validarAtualizacaoDoCampoSituacaoFinanceiraCompra(contaPagar, usuario);
		}
		if (contaPagar.getTipoOrigem().equals("CD") && contaPagar.isQuitada()) {
			alteraOrigemContratoDespesa(contaPagar, usuario);
		}
	}

	public void alteraOrigemContratoDespesa(ContaPagarVO contaPagar, UsuarioVO usuario) throws Exception {
		ContratosDespesasVO contratosDespesas = getFacadeFactory().getContratosDespesasFacade().consultarPorChavePrimaria(Integer.parseInt(contaPagar.getCodOrigem()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (contratosDespesas.getContratoIndeterminado()) {
			ContaPagarVO novaContaPagar = contaPagar.getClone();			
			novaContaPagar.setSituacao(SituacaoFinanceira.A_PAGAR.getValor());
			novaContaPagar.getDataVencimento().setYear(contaPagar.getDataVencimento().getYear() + 1);
			novaContaPagar.setValor(contratosDespesas.getValorParcela());
			novaContaPagar.setValorPago(0.0);
			novaContaPagar.setValorPagamento(0.0);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(novaContaPagar.getListaCentroResultadoOrigemVOs(), novaContaPagar.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), usuario);
			incluir(novaContaPagar, true, true, usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarAtualizacaoDoCampoSituacaoFinanceiraCompra(ContaPagarVO contaPagar, UsuarioVO usuario) {
		try {
			if (contaPagar.getTipoOrigemEnum().isRecebimentoCompra()) {
				validarSituacaoFinanceiraCompraPorRecebimentoCompra(contaPagar, usuario);
			} else if (contaPagar.getTipoOrigemEnum().isCompra()) {
				validarSituacaoFinanceiraCompraPorCompra(contaPagar, usuario);
			} else if (contaPagar.getTipoOrigemEnum().isNotaFiscalEntrada()) {
				validarSituacaoFinanceiraCompraPorNotaFiscalEntrada(contaPagar, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarSituacaoFinanceiraCompraPorNotaFiscalEntrada(ContaPagarVO contaPagar, UsuarioVO usuario) {
		try {
			CompraVO compra = getFacadeFactory().getCompraFacade().consultarPorCompraPorCodOrigemContaPagar(contaPagar.getCodOrigem(), contaPagar.getTipoOrigemEnum(), usuario);
			boolean existeRecebimentoSemVinculoComNotaFiscalEntrada = getFacadeFactory().getRecebimentoCompraFacade().consultarSeExisteRecebimentoCompraPorCompraSemVinculoComNotaFiscalEntrada(compra.getCodigo(), usuario);
			boolean existeContaPagarCompra = consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(contaPagar.getCodOrigem(), SituacaoFinanceira.A_PAGAR.getValor(), contaPagar.getCodigo(), contaPagar.getTipoOrigemEnum(), usuario);
			if (!existeContaPagarCompra && !existeRecebimentoSemVinculoComNotaFiscalEntrada && contaPagar.isQuitada()) {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(compra.getCodigo(), SituacaoFinanceira.PAGO.getValor());
			} else {
				validarAtualizacaoDoCampoSituacaoFinanceiraCompraPendenteOuPagoParcial(compra.getCodigo(), contaPagar, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarSituacaoFinanceiraCompraPorCompra(ContaPagarVO contaPagar, UsuarioVO usuario) {
		try {
			boolean existeContaPagarCompra = consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(contaPagar.getCodOrigem(), SituacaoFinanceira.A_PAGAR.getValor(), contaPagar.getCodigo(), contaPagar.getTipoOrigemEnum(), usuario);
			if (!existeContaPagarCompra && contaPagar.isQuitada()) {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(Integer.parseInt(contaPagar.getCodOrigem()), SituacaoFinanceira.PAGO.getValor());
			} else {
				validarAtualizacaoDoCampoSituacaoFinanceiraCompraPendenteOuPagoParcial(Integer.parseInt(contaPagar.getCodOrigem()), contaPagar, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarSituacaoFinanceiraCompraPorRecebimentoCompra(ContaPagarVO contaPagar, UsuarioVO usuario) {
		try {
			CompraVO compra = getFacadeFactory().getCompraFacade().consultarPorCompraPorCodOrigemContaPagar(contaPagar.getCodOrigem(), contaPagar.getTipoOrigemEnum(), usuario);
			boolean existeContaPagarCompra = consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(contaPagar.getCodOrigem(), SituacaoFinanceira.A_PAGAR.getValor(), contaPagar.getCodigo(), contaPagar.getTipoOrigemEnum(), usuario);
			if (!existeContaPagarCompra && contaPagar.isQuitada() && compra.isSituacaoRecebimentoFinalizado()) {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(compra.getCodigo(), SituacaoFinanceira.PAGO.getValor());
			} else {
				validarAtualizacaoDoCampoSituacaoFinanceiraCompraPendenteOuPagoParcial(compra.getCodigo(), contaPagar, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarAtualizacaoDoCampoSituacaoFinanceiraCompraPendenteOuPagoParcial(Integer compra, ContaPagarVO contaPagar, UsuarioVO usuario) {
		try {
			boolean existeContaPaga = consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(contaPagar.getCodOrigem(), SituacaoFinanceira.PAGO.getValor(), contaPagar.getCodigo(), contaPagar.getTipoOrigemEnum(), usuario);
			if (existeContaPaga || contaPagar.isQuitada()) {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(compra, SituacaoFinanceira.PAGO_PARCIAL.getValor());
			} else {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(compra, SituacaoFinanceira.A_PAGAR.getValor());
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPagamentoEmDespesaDW(ContaPagarVO obj, UsuarioVO usuario) throws Exception {
		new DespesaDW().incluir(obj.getDespesaDWVO(obj.getValorPago(), "PA"), usuario);
		if (!obj.getTipoOrigem().equals(OrigemContaPagar.COMPRA.getValor())) {
			new DespesaDW().incluir(obj.getDespesaDWVO(obj.getValorPago(), "RE"), usuario);
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ContaPagarVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ContaPagarVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContaPagarVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		ContaPagar.excluir(getIdEntidade(), validarAcesso, usuario);			
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "EXCLUIR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario);		
		validarSeContaPagarExisteVinculoComArquivoRemessa(obj);
		getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().cancelarContaPagarPorCodigo(obj.getCodigo(), usuario);		validarSeContaPagarExisteVinculoComArquivoRemessa(obj);		getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuario);
		getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuario);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, usuario);
		if (Uteis.isAtributoPreenchido(obj.getListaContaPagarAdiantamentoVO()) && (obj.getJuro() > 0 || obj.getMulta() > 0)) {
			for (ContaPagarAdiantamentoVO cpa : obj.getListaContaPagarAdiantamentoVO()) {
				getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(cpa.getContaPagarUtilizada(), cpa.getValorUtilizado(),  usuario , false);	
			}
		}
		getFacadeFactory().getContaPagarAdiantamentoFacade().excluirContaPagarAdiantamentos(obj.getCodigo(), usuario);
		String sql = "DELETE FROM ContaPagar WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, UsuarioVO usuario) throws Exception {
		excluirContasPagarTipoOrigemCodigoOrigem(tipoOrigem, codigoOrigem, situacao, Boolean.TRUE, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirListaContasPagar(String listaCodigoExcluir, Boolean validarBloqueioFechamento, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" Select codigo, dataVencimento, dataFatoGerador, unidadeEnsino FROM ContaPagar WHERE codigo in ( ").append(listaCodigoExcluir).append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeContaPagarAptaExclusao(usuario, validarBloqueioFechamento, rs);
		String sql = "DELETE FROM ContaPagar WHERE codigo in ( " + listaCodigoExcluir + ") ";
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Boolean validarBloqueioFechamento, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" Select codigo, dataVencimento, dataFatoGerador, unidadeEnsino FROM ContaPagar WHERE tipoOrigem ='").append(tipoOrigem).append("' ");
		sb.append(" and codOrigem = '").append(codigoOrigem).append("' ");
		if (!situacao.equals("")) {
			sb.append(" and situacao = '").append(situacao).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeContaPagarAptaExclusao(usuario, validarBloqueioFechamento, rs);
		String sql = "DELETE FROM ContaPagar WHERE ((tipoOrigem = ?) and (codOrigem = ?))";
		if (!situacao.equals("")) {
			sql += " and (situacao) = '" + situacao.toUpperCase() + "' ";
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), tipoOrigem, codigoOrigem);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Date dataBase, UsuarioVO usuario) throws Exception {
		excluirContasPagarTipoOrigemCodigoOrigem(tipoOrigem, codigoOrigem, situacao, dataBase, Boolean.TRUE, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasPagarTipoOrigemCodigoOrigem(String tipoOrigem, String codigoOrigem, String situacao, Date dataBase, Boolean validarBloqueioFechamentoMes, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" Select codigo, dataVencimento, dataFatoGerador, unidadeEnsino FROM ContaPagar WHERE tipoOrigem ='").append(tipoOrigem).append("' ");
		sb.append(" and codOrigem = '").append(codigoOrigem).append("' ");
		sb.append(" and dataVencimento >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
		if (!situacao.equals("")) {
			sb.append(" and situacao = '").append(situacao).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeContaPagarAptaExclusao(usuario, validarBloqueioFechamentoMes, rs);
		String sql = "DELETE FROM ContaPagar WHERE ((tipoOrigem = ?) and (codOrigem = ?)) and (dataVencimento >= ?) ";
		if (!situacao.equals("")) {
			sql += " and (situacao) = '" + situacao.toUpperCase() + "' ";
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), tipoOrigem, codigoOrigem, dataBase);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaPagarPorTipoOrigemPorCodigoOrigem(String tipoOrigem, String codigoOrigem, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" Select codigo, dataVencimento, dataFatoGerador, unidadeEnsino FROM ContaPagar WHERE tipoOrigem ='").append(tipoOrigem).append("' ");
		sb.append(" and codOrigem = '").append(codigoOrigem).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeContaPagarAptaExclusao(usuario, Boolean.TRUE, rs);
		String sql = "DELETE FROM ContaPagar WHERE tipoOrigem = ? and codOrigem = ?  ";
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), tipoOrigem, codigoOrigem);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaFilho(ContaPagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			ContaPagar.excluir(getIdEntidade());
			StringBuilder sb = new StringBuilder("");
			sb.append(" Select codigo, dataVencimento, dataFatoGerador, unidadeEnsino FROM ContaPagar WHERE origemRenegociacaoPagar = ").append(obj.getOrigemRenegociacaoPagar()).append(" ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			validarSeContaPagarAptaExclusao(usuario, Boolean.TRUE, rs);
			String sql = "DELETE FROM ContaPagar WHERE ((origemRenegociacaoPagar = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getOrigemRenegociacaoPagar());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarSeContaPagarAptaExclusao(UsuarioVO usuario, Boolean validarBloqueioFechamentoMes, SqlRowSet rs) throws Exception {
			while (rs.next()) {
				ContaPagarVO obj = new ContaPagarVO();
				obj.setCodigo(rs.getInt("codigo"));
				obj.setDataVencimento(rs.getDate("dataVencimento"));
				obj.setDataFatoGerador(rs.getDate("dataFatoGerador"));
				obj.getUnidadeEnsino().setCodigo((rs.getInt("unidadeEnsino")));
				
				if (validarBloqueioFechamentoMes) {
					verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "EXCLUIR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
				}
				
				validarSeContaPagarExisteVinculoComArquivoRemessa(obj);
				getFacadeFactory().getContaPagarAdiantamentoFacade().excluirContaPagarAdiantamentos(rs.getInt("codigo"), usuario);
				getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(String.valueOf(rs.getInt("codigo")), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuario);
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, usuario);
			}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarSeContaPagarExisteVinculoComArquivoRemessa(ContaPagarVO obj) {
		try {
			Integer arquivoRemessa = getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().verificaContaPagarExistenteEmControleRemessa(obj);
			if (Uteis.isAtributoPreenchido(arquivoRemessa)) {
				throw new StreamSeiException("Essa Conta pagar de número " + obj.getCodigo() + " está vinculada ao arquivo de remessa pagar de número " + arquivoRemessa + " por isso não é possível realizar sua alteração.");
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	public static void incluirContaFilho(List<ContaPagarVO> objs, UsuarioVO usuario) throws Exception {
		Iterator<ContaPagarVO> i = objs.iterator();
		while (i.hasNext()) {
			ContaPagarVO obj = (ContaPagarVO) i.next();
			getFacadeFactory().getContaPagarFacade().incluir(obj, true, true, usuario);
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo <code>identificadorCentroDespesa</code> da classe <code>CentroDespesa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>ContaPagarVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContaPagarVO> consultarPorCodigoCompra(Integer compra, TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		if (Uteis.isAtributoPreenchido(tipoCriacaoContaPagarEnum)) {
			StringBuilder sqlStr = new StringBuilder("SELECT ContaPagar.* FROM ContaPagar ");
			if (tipoCriacaoContaPagarEnum.isCompra()) {
				sqlStr.append(" WHERE situacao = 'AP' AND tipoorigem = '").append(OrigemContaPagar.COMPRA.getValor()).append("' AND codorigem::int = ").append(compra);
			} else if (tipoCriacaoContaPagarEnum.isRecebimentoCompra()) {
				sqlStr.append(" WHERE situacao = 'AP' AND tipoorigem = '").append(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor()).append("' AND codorigem::int IN (SELECT codigo FROM recebimentocompra WHERE recebimentocompra.compra = ").append(compra).append(") ");
			} else if (tipoCriacaoContaPagarEnum.isNotaFiscalEntrada()) {
				sqlStr.append(" WHERE situacao = 'AP' AND tipoorigem = '").append(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor()).append("' AND codorigem::int IN (SELECT notafiscalentradarecebimentocompra.notafiscalentrada FROM recebimentocompra INNER JOIN notafiscalentradarecebimentocompra ON ");
				sqlStr.append(" recebimentocompra.codigo = notafiscalentradarecebimentocompra.recebimentocompra INNER JOIN compra ON recebimentocompra.compra = compra.codigo WHERE compra.codigo = ").append(compra).append(") ");
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		}
		return new ArrayList<>();
	}

	public List consultarVencidosEmPeriodo(Integer codigoSacado, String tipoSacado, Date dataInicio, Date dataTermino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM ContaPagar ";

		if (tipoSacado.equals("FO")) {
			sqlStr += " , fornecedor ";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " , funcionario, pessoa ";
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " , banco ";
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " , parceiro ";
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " , operadoraCartao ";
		} else if (tipoSacado.equals("AL") || tipoSacado.equals("RF")) {
			sqlStr += " , pessoa ";
		}

		sqlStr += " WHERE 1 = 1 ";
		if (dataInicio != null) {
			sqlStr += " AND dataVencimento >= '" + Uteis.getDataJDBC(dataInicio) + "' ";
		}
		if (dataTermino != null) {
			sqlStr += " AND dataVencimento <= '" + Uteis.getDataJDBC(dataTermino) + "' ";
		}

		if (tipoSacado.equals("FO")) {
			sqlStr += " AND fornecedor.codigo = " + codigoSacado;
			sqlStr += " AND fornecedor.codigo = contapagar.fornecedor";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " AND funcionario.codigo = contapagar.funcionario";
			sqlStr += " AND pessoa.codigo = funcionario.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " AND ContaPagar.banco = banco.codigo";
			sqlStr += " AND banco.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("AL")) {
			sqlStr += " AND pessoa.codigo = contapagar.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("RF")) {
			sqlStr += " AND pessoa.codigo = contapagar.responsavelFinanceiro";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " AND parceiro.codigo = contapagar.parceiro";
			sqlStr += " AND parceiro.codigo = " + codigoSacado;
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " AND operadoraCartao.codigo = contapagar.operadoraCartao";
			sqlStr += " AND operadoraCartao.codigo = " + codigoSacado;
		}

		sqlStr += " AND contapagar.situacao = 'AP' ";

		sqlStr += " ORDER BY dataVencimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarAVencerPorTipoSacado(Integer codigoSacado, String tipoSacado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM ContaPagar ";

		if (tipoSacado.equals("FO")) {
			sqlStr += " , fornecedor ";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " , funcionario, pessoa ";
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " , banco ";
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " , parceiro ";
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " , operadoraCartao ";
		} else if (tipoSacado.equals("AL") || tipoSacado.equals("RF")) {
			sqlStr += " , pessoa ";
		}

		sqlStr += " WHERE dataVencimento >= '" + Uteis.getDataJDBC(new Date()) + "' ";

		if (tipoSacado.equals("FO")) {
			sqlStr += " AND fornecedor.codigo = " + codigoSacado;
			sqlStr += " AND fornecedor.codigo = contapagar.fornecedor";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " AND funcionario.codigo = contapagar.funcionario";
			sqlStr += " AND pessoa.codigo = funcionario.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " AND ContaPagar.banco = banco.codigo";
			sqlStr += " AND banco.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("AL")) {
			sqlStr += " AND pessoa.codigo = contapagar.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("RF")) {
			sqlStr += " AND pessoa.codigo = contapagar.responsavelFinanceiro";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " AND parceiro.codigo = contapagar.parceiro";
			sqlStr += " AND parceiro.codigo = " + codigoSacado;
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " AND operadoraCartao.codigo = contapagar.operadoraCartao";
			sqlStr += " AND operadoraCartao.codigo = " + codigoSacado;
		}

		sqlStr += " AND contapagar.situacao = 'AP' ";

		sqlStr += " ORDER BY dataVencimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarVencidosPorTipoSacado(Integer codigoSacado, String tipoSacado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM ContaPagar ";

		if (tipoSacado.equals("FO")) {
			sqlStr += " , fornecedor ";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " , funcionario, pessoa ";
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " , banco ";
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " , parceiro ";
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " , operadoraCartao ";
		} else if (tipoSacado.equals("AL") || tipoSacado.equals("RF")) {
			sqlStr += " , pessoa ";
		}

		sqlStr += " WHERE dataVencimento < '" + Uteis.getDataJDBC(new Date()) + "' ";

		if (tipoSacado.equals("FO")) {
			sqlStr += " AND fornecedor.codigo = " + codigoSacado;
			sqlStr += " AND fornecedor.codigo = contapagar.fornecedor";
		} else if (tipoSacado.equals("FU")) {
			sqlStr += " AND funcionario.codigo = contapagar.funcionario";
			sqlStr += " AND pessoa.codigo = funcionario.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("BA")) {
			sqlStr += " AND ContaPagar.banco = banco.codigo";
			sqlStr += " AND banco.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("AL")) {
			sqlStr += " AND pessoa.codigo = contapagar.pessoa";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("RF")) {
			sqlStr += " AND pessoa.codigo = contapagar.responsavelFinanceiro";
			sqlStr += " AND pessoa.codigo = " + codigoSacado;
		} else if (tipoSacado.equals("PA")) {
			sqlStr += " AND parceiro.codigo = contapagar.parceiro";
			sqlStr += " AND parceiro.codigo = " + codigoSacado;
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sqlStr += " AND operadoraCartao.codigo = contapagar.operadoraCartao";
			sqlStr += " AND operadoraCartao.codigo = " + codigoSacado;
		}

		sqlStr += " AND contapagar.situacao = 'AP' ";

		sqlStr += " ORDER BY dataVencimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ContaPagarVO> consultarPorNotaFiscalEntrada(NotaFiscalEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder("select * from contapagar ");
		List<Object> listaFiltros = new ArrayList<>();
		sqlStr.append(" where tipoorigem = ? ").append(" and codorigem = ?  ");
		sqlStr.append(" order by datavencimento ");
		listaFiltros.add(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
		listaFiltros.add(obj.getCodigo().toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<ContaPagarVO> consultarPorNotaFiscalEntradaOutrasOrigem(NotaFiscalEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		List<ContaPagarVO> lista = new ArrayList<>();
		Map<TipoCriacaoContaPagarEnum, List<NotaFiscalEntradaRecebimentoCompraVO>> mapaTipoCriacaoContaPagar = obj.getMapaTipoCriacaoContaPagarNotaFiscal();
		if (mapaTipoCriacaoContaPagar != null && (mapaTipoCriacaoContaPagar.containsKey(TipoCriacaoContaPagarEnum.COMPRA) || mapaTipoCriacaoContaPagar.containsKey(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA))) {
			StringBuilder sqlStr = new StringBuilder("select * from contapagar ");
			List<Object> listaFiltros = new ArrayList<>();
			String condicaoOr = "";
			sqlStr.append(" where 1=1 and ");
			sqlStr.append(" ( ");
			for (Map.Entry<TipoCriacaoContaPagarEnum, List<NotaFiscalEntradaRecebimentoCompraVO>> mapaTipoCriacaoContaPagarEnum : mapaTipoCriacaoContaPagar.entrySet()) {
				if (mapaTipoCriacaoContaPagarEnum.getKey().isCompra()) {
					sqlStr.append(condicaoOr);
					sqlStr.append(" (tipoorigem = ? ").append(" and codorigem in(").append(obj.getListaCodigoCompra(mapaTipoCriacaoContaPagarEnum.getValue())).append(")) ");
					listaFiltros.add(OrigemContaPagar.COMPRA.getValor());
					condicaoOr = " or ";
				} else if (mapaTipoCriacaoContaPagarEnum.getKey().isRecebimentoCompra()) {
					sqlStr.append(condicaoOr);
					sqlStr.append(" (tipoorigem = ? ").append(" and codorigem in(").append(obj.getListaCodigoRecebimentoCompra(mapaTipoCriacaoContaPagarEnum.getValue())).append("))");
					listaFiltros.add(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor());
					condicaoOr = " or ";
				}
			}
			sqlStr.append(" ) ");
			sqlStr.append(" order by tipoorigem, datavencimento ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
			lista = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		}
		return lista;
	}

	public List consultarPorCodigoFornecedor(Integer valorConsulta, Boolean pago, Boolean pagoParcialmente, Boolean apagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContaPagar.* FROM ContaPagar, Fornecedor WHERE ContaPagar.fornecedor = Fornecedor.codigo and Fornecedor.codigo  = " + valorConsulta.intValue() + " ";
		String or = "";
		if (pago || pagoParcialmente || apagar) {
			or = " and ( ";
		}
		if (pago) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.PAGO.getValor() + "') ";
			or = " or ";
		}
		if (pagoParcialmente) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.PAGO_PARCIAL.getValor() + "') ";
			or = " or ";
		}
		if (apagar) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.A_PAGAR.getValor() + "') ";
			or = " or ";
		}
		if (pago || pagoParcialmente || apagar) {
			sqlStr += " ) ORDER BY dataVencimento";
		} else {
			sqlStr += " ORDER BY dataVencimento";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoFuncionario(Integer valorConsulta, Boolean pago, Boolean pagoParcialmente, Boolean apagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContaPagar.* FROM ContaPagar, Funcionario WHERE ContaPagar.funcionario = Funcionario.codigo and Funcionario.codigo  = " + valorConsulta.intValue() + " ";
		String or = "";
		if (pago || pagoParcialmente || apagar) {
			or = " and ( ";
		}
		if (pago) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.PAGO.getValor() + "') ";
			or = " or ";
		}
		if (pagoParcialmente) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.PAGO_PARCIAL.getValor() + "') ";
			or = " or ";
		}
		if (apagar) {
			sqlStr += or + " (ContaPagar.situacao = '" + SituacaoFinanceira.A_PAGAR.getValor() + "') ";
			or = " or ";
		}
		if (pago || pagoParcialmente || apagar) {
			sqlStr += " ) ORDER BY dataVencimento";
		} else {
			sqlStr += " ORDER BY dataVencimento";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public void consultar(String situacao, Integer unidadeEnsino, boolean filtrarDataFatorGerador, boolean consultaPainelGestorFinanceiro, DataModelo dataModelo) {
		try {
			List<ContaPagarVO> objs = new ArrayList<>();
			ContaPagarVO.enumCampoConsultaContaPagar enumCampoConsulta = ContaPagarVO.enumCampoConsultaContaPagar.valueOf(dataModelo.getCampoConsulta());
			if (ContaPagarVO.enumCampoConsultaContaPagar.CODIGO.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if(dataModelo.getValorConsulta().equals("")) {
					throw new StreamSeiException("O código da conta a pagar deve ser informado!");
				}
				int valorInt = Uteis.getValorInteiro(dataModelo.getValorConsulta());
				objs = consultarPorCodigo(valorInt, unidadeEnsino, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorCodigoTotalRegistros(valorInt, unidadeEnsino, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().length() < 3) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio3"));
				}
				objs = consultarPorNomeFavorecido(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorNomeFavorecidoTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.CATEGORIA_DESPESA.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				objs = consultarPorDescricaoCentroDespesaCentroDespesa(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorDescricaoCentroDespesaCentroDespesaTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_PAGAMENTO.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if(dataModelo.getValorConsulta().equals("")) {
					throw new StreamSeiException("O código do pagamento deve ser informado!");
				}
				int valorInt = Uteis.getValorInteiro(dataModelo.getValorConsulta());
				objs = consultarPorCodigoPagamento(valorInt, unidadeEnsino, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorCodigoPagamentoTotalRegistros(valorInt, unidadeEnsino, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.TURMA.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				objs = consultarPorIdentificadorTurma(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorIdentificadorTurmaTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.DEPARTAMENTO.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				objs = consultarPorDepartamento(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorDepartamentoTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.NR_DOCUMENTO.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				objs = consultarPorNrDocumento(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorNrDocumentoTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.NUMERO_NOTA_FISCAL_ENTRADA.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().length() < 1) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
				}
				if(!Uteis.getIsValorNumerico(dataModelo.getValorConsulta())) {
					throw new StreamSeiException("Infome apenas números.");
				}
				int valorInt = Uteis.getValorInteiro(dataModelo.getValorConsulta());
				objs = consultarPorNumeroNotaFiscalEntrada(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorNumeroNotaFiscalEntradaTotalRegistros(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador,  dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.CNPJ_FORNECEDOR.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().isEmpty()) {
					throw new StreamSeiException("Informe o CNPJ");
				}
				objs = consultarPorCnpjFornecedor(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorCnpjFornecedorTotalRegistros(situacao, unidadeEnsino, filtrarDataFatorGerador, dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_CONTRATO_DESPESA.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().length() < 1) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
				}
				if(!Uteis.getIsValorNumerico(dataModelo.getValorConsulta())) {
					throw new StreamSeiException("Infome apenas números.");
				}
				int valorInt = Uteis.getValorInteiro(dataModelo.getValorConsulta());
				objs = consultarPorCodigoContratoDespesa(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorCodigoContratoDespesaTotalRegistros(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador,  dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_NOTA_FISCAL_ENTRADA.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().length() < 1) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
				}
				if(!Uteis.getIsValorNumerico(dataModelo.getValorConsulta())) {
					throw new StreamSeiException("Infome apenas números.");
				}
				int valorInt = Uteis.getValorInteiro(dataModelo.getValorConsulta());
				objs = consultarPorCodigoNotaFiscalEntrada(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorCodigoNotaFiscalEntradaTotalRegistros(valorInt, unidadeEnsino, situacao, filtrarDataFatorGerador,  dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.VALOR.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				if (dataModelo.getValorConsulta().length() < 1) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
				}				
				
				objs = consultarValor( unidadeEnsino, situacao, filtrarDataFatorGerador, dataModelo);
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarValorTotalRegistros( unidadeEnsino, situacao, filtrarDataFatorGerador,  dataModelo));
			}
			if (ContaPagarVO.enumCampoConsultaContaPagar.FAIXA_VALOR.equals(enumCampoConsulta) && !consultaPainelGestorFinanceiro) {
				/*if (dataModelo.getListaConsulta().isEmpty()) {
					throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
				}	*/		
				objs = consultarPorFaixaValor(unidadeEnsino, situacao, filtrarDataFatorGerador, dataModelo);				
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarPorFaixaValorTotalRegistros(unidadeEnsino, situacao, filtrarDataFatorGerador,  dataModelo));
			}
			dataModelo.setListaConsulta(objs);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	

	private List<ContaPagarVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ContaPagar where codigo = ?");
		dataModelo.getListaFiltros().add(valorConsulta);
		preencherDadosParaConsultaFiltroUnidadeEnsino(unidadeEnsino, sql);
		sql.append(" ORDER BY codigo ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	private Integer consultarPorCodigoTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar where codigo = ? ");
		dataModelo.getListaFiltros().add(valorConsulta);
		preencherDadosParaConsultaFiltroUnidadeEnsino(unidadeEnsino, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorNomeFavorecido(String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		preencherDadosParaConsultaPorNomeFavorecido(dataModelo.getValorConsulta(), situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append("order by contapagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet resultado = null;
		if(Uteis.isAtributoPreenchido(dataModelo.getValorConsulta()) && !dataModelo.getValorConsulta().trim().equals("%%")) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT);
		}else {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		return montarDadosConsulta(resultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorNomeFavorecidoTotalRegistros(String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		preencherDadosParaConsultaPorNomeFavorecido(dataModelo.getValorConsulta(), situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet resultado = null;
		if(Uteis.isAtributoPreenchido(dataModelo.getValorConsulta()) && !dataModelo.getValorConsulta().trim().equals("%%")) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT, dataModelo.getValorConsulta()+PERCENT);
		}else {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}		
		return (Integer) Uteis.getSqlRowSetTotalizador(resultado, "qtde", TipoCampoEnum.INTEIRO);

	}

	private List<ContaPagarVO> consultarPorDescricaoCentroDespesaCentroDespesa(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT contapagar.* FROM contapagar ");
		sql.append("  INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("'  ");	
		sql.append("  INNER JOIN categoriadespesa 	   ON categoriadespesa.codigo 		  = centroresultadoorigem.categoriadespesa  ");
		sql.append(" WHERE 1 = 1  ");	
		if(dataModelo.getValorConsulta().equals("0")) {
			dataModelo.setValorConsulta("");
		}
		sql.append(" and (sem_acentos (categoriadespesa.descricao)) ilike (sem_acentos (?))");
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append(" ORDER BY ContaPagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorDescricaoCentroDespesaCentroDespesaTotalRegistros(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(distinct contapagar.codigo) AS qtde FROM contapagar ");
		sql.append("  INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("'  ");	
		sql.append("  INNER JOIN categoriadespesa 	   ON categoriadespesa.codigo 		  = centroresultadoorigem.categoriadespesa  ");
		sql.append(" WHERE 1 = 1  ");	
		if(dataModelo.getValorConsulta().equals("0")) {
			dataModelo.setValorConsulta("");
		}
		sql.append(" and (sem_acentos (categoriadespesa.descricao)) ilike (sem_acentos (?))");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorCodigoPagamento(Integer valorConsulta, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		sql.append(" where contapagar.codigo in (select contapagar from contapagarnegociacaopagamento where negociacaocontapagar = ");
		sql.append(valorConsulta);
		sql.append(") ");
		preencherDadosParaConsultaFiltroUnidadeEnsino(unidadeEnsino, sql);
		sql.append("ORDER BY contapagar.datavencimento ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorCodigoPagamentoTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		sql.append(" where contapagar.codigo in (select contapagar from contapagarnegociacaopagamento where negociacaocontapagar = ");
		sql.append(valorConsulta);
		sql.append(") ");
		preencherDadosParaConsultaFiltroUnidadeEnsino(unidadeEnsino, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorIdentificadorTurma(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		sql.append(" WHERE exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join turma on turma.codigo = centroresultadoorigem.turma ");
		sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
		sql.append(" and (sem_acentos(turma.identificadorTurma)) ilike(sem_acentos(?))) ");		
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append("ORDER BY contapagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorIdentificadorTurmaTotalRegistros(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		sql.append(" WHERE exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join turma on turma.codigo = centroresultadoorigem.turma ");
		sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
		sql.append(" and (sem_acentos(turma.identificadorTurma)) ilike(sem_acentos(?))) ");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorDepartamento(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		sql.append(" WHERE exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento ");
		sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
		sql.append(" and (sem_acentos(departamento.nome)) ilike(sem_acentos(?))) ");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append("ORDER BY contapagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorDepartamentoTotalRegistros(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		sql.append(" WHERE exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento ");
		sql.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
		sql.append(" and (sem_acentos(departamento.nome)) ilike(sem_acentos(?))) ");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorNumeroNotaFiscalEntrada(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM ContaPagar  ");
		sql.append(" inner join notafiscalentrada on notafiscalentrada.codigo = contapagar.codorigem::integer and contapagar.tipoorigem = 'NE' ");		
		sql.append(" where notafiscalentrada.numero = '").append(valorConsulta).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append(" ORDER BY datavencimento ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	private Integer consultarPorNumeroNotaFiscalEntradaTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar ");
		sql.append("  inner join notafiscalentrada on notafiscalentrada.codigo = contapagar.codorigem::integer and contapagar.tipoorigem = 'NE' ");
		sql.append(" where notafiscalentrada.numero = '").append(valorConsulta).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private void preencherDadosParaConsultaPorNomeFavorecido(String nomeFavorecido, String situacao, Date dataIni, Date dataFim, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, StringBuilder sql) {
		sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario  ");
		sql.append(" left join parceiro on parceiro.codigo = contapagar.parceiro  ");
		sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
		sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sql.append(" left join banco on banco.codigo = contapagar.banco ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
		if(Uteis.isAtributoPreenchido(nomeFavorecido) && !nomeFavorecido.trim().equals("%%")) {
			sql.append(" where (upper(sem_acentos(fornecedor.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(parceiro.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(banco.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(pessoa.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(operadoracartao.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) ilike(sem_acentos(?)))");
		}else {
			sql.append(" where 1 = 1 ");
		}
		preencherDadosParaConsultaFiltros(situacao, dataIni, dataFim, unidadeEnsino, filtrarDataFatorGerador, sql);
	}

	private void preencherDadosParaConsultaFiltros(String situacao, Date dataIni, Date dataFim, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, StringBuilder sql) {
		if (Uteis.isAtributoPreenchido(situacao) && !situacao.equals("0")) {
			if(situacao.equals("PAAP")) {
				sql.append("  and (contapagar.situacao) in ('AP', 'PP', 'PA') ");
			}else {
				sql.append("  and (contapagar.situacao) like('").append(situacao.toUpperCase()).append("%') ");
		}
		}
		if (Uteis.isAtributoPreenchido(dataIni) && Uteis.isAtributoPreenchido(dataFim)) {
			if (!filtrarDataFatorGerador) {
				sql.append("and contapagar.datavencimento ");
			} else {
				sql.append("and contapagar.dataFatoGerador ");
			}
			sql.append("between '");
			sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)));
			sql.append("' and '");
			sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
			sql.append("'");
		}
		preencherDadosParaConsultaFiltroUnidadeEnsino(unidadeEnsino, sql);
	}

	private void preencherDadosParaConsultaFiltroUnidadeEnsino(Integer unidadeEnsino, StringBuilder sql) {
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" AND contapagar.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
	}

	public List<ContaPagarVO> consultaRapidaContaPagarPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("Select * from contaPagar ");
		sql.append("where unidadeEnsino = ").append(unidadeEnsino).append(" and situacao <> 'PA' ");
		preencherDadosParaConsultaFiltroPorSituacaoUnidadeEnsino(situacao, dataBase, sql);
		sql.append("order by dataVencimento ");
		UteisTexto.addLimitAndOffset(sql, limite, offset);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}

	public Integer consultaRapidaContaPagarPorSituacaoUnidadeEnsinoTotalRegistros(Integer unidadeEnsino, String situacao, Date dataBase) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(contapagar.codigo) as qtde  from contaPagar ");
		sql.append("where unidadeEnsino = ").append(unidadeEnsino).append(" and situacao <> 'PA' ");
		preencherDadosParaConsultaFiltroPorSituacaoUnidadeEnsino(situacao, dataBase, sql);
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(resultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private void preencherDadosParaConsultaFiltroPorSituacaoUnidadeEnsino(String situacao, Date dataBase, StringBuilder sql) {
		if (situacao.equals("VE")) {
			sql.append("and dataVencimento < '").append(Uteis.getDataJDBC(dataBase)).append(" 00:00:00' ");
		} else if (situacao.equals("VH")) {
			sql.append("and ( dataVencimento >= '").append(Uteis.getDataJDBC(dataBase)).append(" 00:00:00' and dataVencimento <= '").append(Uteis.getDataJDBC(dataBase)).append(" 23:59:59') ");
		} else if (situacao.equals("AV")) {
			sql.append("and (dataVencimento > '").append(Uteis.getDataJDBC(dataBase)).append(" 23:59:59' and dataVencimento <= '").append(Uteis.getDataJDBC(Uteis.getDataUltimoDiaMes(dataBase))).append(" 23:59:59' ) ");
		}
	}

	public Map<String, Double> consultarPorCodigoTotalPagarTotalPago(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		sql.append(getSQLPadraoConsultaTotalPagar());
		sql.append("WHERE contapagar.codigo = ").append(valorConsulta.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and contapagar.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sql.append(" and contapagar.situacao in ('AP', 'PP') ");
		sql.append(") UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalPago());
		sql.append("WHERE contapagar.codigo = ").append(valorConsulta.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and contapagar.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sql.append(" and contapagar.situacao in ('PA', 'PP') ");
		
		sql.append(") UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalNegociado());
		sql.append("WHERE contapagar.codigo = ").append(valorConsulta.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and contapagar.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sql.append(" and contapagar.situacao = 'NE' ");
		
		sql.append(")) AS contapagar");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaTotalPagarTotalPago(resultado);
	}

	public Map<String, Double> consultaRapidaPorNomeSacadoTotalPagarTotalPago(String nomeFavorecido, String situacao, int unidadeEnsino, Date dataInicio, Date dataFim, Boolean filtrarDataFatorGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = null;
		if (situacao == null || (!situacao.equals("PA") && !situacao.equals("AP") && !situacao.equals("NE"))) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		} else if (situacao.equals("PAAP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("AP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, 0 AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("PA")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("NE")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, 0 AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		}
		sql.append(getSQLPadraoConsultaTotalPagar());		
		if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
			sql.append(" where 1 = 1  AND ");
		}else {
			sql.append(" where (upper(sem_acentos(fornecedor.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(parceiro.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(banco.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(pessoa.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(operadoracartao.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(responsavelFinanceiro.nome)) ilike(sem_acentos(?)))  AND ");
		}
		if (unidadeEnsino != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && (!situacao.equals("PAAP"))) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('AP', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.datafatogerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(") UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalPago());
		if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
			sql.append(" where 1 = 1 AND ");
		}else {
			sql.append(" where (upper(sem_acentos(fornecedor.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(parceiro.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(banco.nome)) like(sem_acentos(?)) or upper(sem_acentos(pessoa.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(operadoracartao.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(responsavelFinanceiro.nome)) ilike(sem_acentos(?)))  AND ");
		}
		if (unidadeEnsino != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && (!situacao.equals("PAAP"))) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('PA', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(") UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalNegociado());
		if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
			sql.append(" where 1 = 1 AND ");
		}else {
			sql.append(" where (upper(sem_acentos(fornecedor.nome)) ilike(sem_acentos(?))");
			sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(parceiro.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(banco.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(pessoa.nome)) ilike(sem_acentos(?)) ");
			sql.append(" or upper(sem_acentos(operadoracartao.nome)) ilike(sem_acentos(?)) or upper(sem_acentos(responsavelFinanceiro.nome)) ilike(sem_acentos(?)))  AND ");
		}
		if (unidadeEnsino != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null &&  (!situacao.equals("")) && (!situacao.equals("0"))) {
			sql.append("((contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao = 'NE' AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}			
		sql.append(")) AS contapagar");

		SqlRowSet resultado = null;
		
		if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {
			nomeFavorecido = nomeFavorecido.toUpperCase()+"%";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, 
					nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido,
					nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido);
		}
		return montarDadosConsultaTotalPagarTotalPago(resultado);	
	}

	public Map<String, Double> consultaRapidaPorIdentificadorCentroReceitaTotalPagarTotalPago(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = null;
		if (situacao == null || (!situacao.equals("PA") && !situacao.equals("AP") && !situacao.equals("NE") && !situacao.equals("PAAP"))) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		} else if (situacao.equals("PAAP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("AP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, 0 AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("PA")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("NE")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, 0 AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		}
		sql.append(getSQLPadraoConsultaTotalPagar());
		sql.append("  INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("'  ");	
		sql.append("  INNER JOIN categoriadespesa 	   ON categoriadespesa.codigo 		  = centroresultadoorigem.categoriadespesa  ");
		sql.append(" WHERE (sem_acentos (categoriadespesa.descricao)) ilike (sem_acentos (?)) AND ");		
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('AP', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" )UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalPago());
		sql.append("  INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("'  ");	
		sql.append("  INNER JOIN categoriadespesa 	   ON categoriadespesa.codigo 		  = centroresultadoorigem.categoriadespesa  ");
		sql.append(" WHERE (sem_acentos (categoriadespesa.descricao)) ilike (sem_acentos (?)) AND ");		
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('PA', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.datafatogerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" ) UNION ALL ( ");
		sql.append(getSQLPadraoConsultaTotalNegociado());
		sql.append("  INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("'  ");	
		sql.append("  INNER JOIN categoriadespesa 	   ON categoriadespesa.codigo 		  = centroresultadoorigem.categoriadespesa  ");
		sql.append(" WHERE (sem_acentos (categoriadespesa.descricao)) ilike (sem_acentos (?)) AND ");		
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0"))) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao = 'NE' AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.datafatogerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" )) AS contapagar");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%", valorConsulta+"%", valorConsulta+"%");
		return montarDadosConsultaTotalPagarTotalPago(resultado);
	}

	public Map<String, Double> consultaRapidaPorSituacaoUnidadeEnsinoTotalPagarTotalPago(Integer unidadeEnsino, String situacao, Date dataBase, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaTotalPagar());
		sql.append("where contapagar.unidadeEnsino = ").append(unidadeEnsino).append(" and contapagar.situacao in ('AP', 'PP') ");
		if (situacao.equals("VE")) {
			sql.append("and contapagar.dataVencimento < '").append(Uteis.getDataJDBC(dataBase)).append(" 00:00:00' ");
		} else if (situacao.equals("VH")) {
			sql.append("and (contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataBase)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataBase)).append(" 23:59:59' ) ");
		} else if (situacao.equals("AV")) {
			sql.append("and (contapagar.dataVencimento > '").append(Uteis.getDataJDBC(dataBase)).append(" 23:59:59' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(Uteis.getDataUltimoDiaMes(dataBase))).append(" 23:59:59' ) ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaTotalPagarTotalPago(resultado);
	}

	private Map<String, Double> montarDadosConsultaTotalPagarTotalPago(SqlRowSet resultado) {
		HashMap<String, Double> totalPagarTotalPago = new HashMap<>(0);
		if (resultado.next()) {
			totalPagarTotalPago.put("valorAPagar", resultado.getDouble("valorAPagar"));
			totalPagarTotalPago.put("valorPago", resultado.getDouble("valorPago"));
			totalPagarTotalPago.put("valorNegociado", resultado.getDouble("valorNegociado"));
			return totalPagarTotalPago;
		} else {
			totalPagarTotalPago.put("valorAPagar", 0.0);
			totalPagarTotalPago.put("valorPago", 0.0);
			totalPagarTotalPago.put("valorNegociado", 0.0);
			return totalPagarTotalPago;
		}
	}

	@Override
	public Double consultarValorParaAtualizarContaPagar(Integer codigo) throws Exception {
		String sqlStr = "select valor from ContaPagar where codigo = " + codigo.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return 0.0;
		}
		return tabelaResultado.getDouble("valor");
	}

	public Double consultarValorContaPagarCompraGeradoNotaFiscal(Integer codigoCompra) throws Exception {
		String sqlStr = "select sum(ContaPagar.valor) as valor from ContaPagar, RecebimentoCompra, NotaFiscalEntrada,Compra" + " where ContaPagar.recebimentoCompra = RecebimentoCompra.codigo and RecebimentoCompra.compra = Compra.codigo " + " and NotaFiscalEntrada.recebimentoCompra = RecebimentoCompra.codigo and Compra.codigo = " + codigoCompra.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return 0.0;
		}
		return tabelaResultado.getDouble("valor");
	}

	public List consultarContaPagarPorSituacaoUnidadeEnsino(Integer unidadeEnsino, String situacao, Date dataBase, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sql = "Select * from contaPagar where unidadeEnsino = " + unidadeEnsino + " and situacao <> 'PA'";
		if (situacao.equals("VE")) {
			sql += " and dataVencimento < '" + Uteis.getDataJDBC(dataBase) + " 00:00:00'";
		} else if (situacao.equals("VH")) {
			sql += " and ( dataVencimento >= '" + Uteis.getDataJDBC(dataBase) + " 00:00:00' and dataVencimento <= '" + Uteis.getDataJDBC(dataBase) + " 23:59:59') ";
		} else if (situacao.equals("AV")) {
			sql += " and (dataVencimento > '" + Uteis.getDataJDBC(dataBase) + " 23:59:59' and dataVencimento <= '" + Uteis.getDataJDBC(Uteis.getDataUltimoDiaMes(dataBase)) + " 23:59:59' )";
		}
		sql += " order by dataVencimento ";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorNotaFiscalEntrada(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			if (getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(obj.getCodigo().toString(), OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor(), usuario)) {
				throw new StreamSeiException("Existe uma conta pagar para essa Nota Fiscal de Entrada e a mesma esta paga, para que seja feita a operação é necessário primeiramente realizar o estorno da conta pagar.");
			}
			Iterator<ContaPagarVO> i = obj.getListaContaPagar().iterator();
			while (i.hasNext()) {
				ContaPagarVO contaPagarVO = i.next();
				excluir(contaPagarVO, false, usuario);
				i.remove();
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasPagarMatriculaPeriodoConvenio(String matricula, Integer matriculaPeriodo, Integer convenio, UsuarioVO usuario) throws Exception {

		StringBuilder whereStr = new StringBuilder();
		whereStr.append(" WHERE (matricula='").append(matricula).append("') ");
		whereStr.append(" AND (tipoorigem='").append(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.getValor()).append("') ");
		whereStr.append(" AND (codorigem='").append(matriculaPeriodo).append("') ");
		whereStr.append(" AND (situacao='").append(SituacaoFinanceira.A_PAGAR.getValor()).append("') ");
		whereStr.append(" AND (convenio=").append(convenio).append(") ");
		
		List<ContaPagarVO> contasVerificar = consultaRapidaContaPagarClausulaWhereParametro(whereStr.toString(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		for (ContaPagarVO obj : contasVerificar) {
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
		}
			
		StringBuilder sql = new StringBuilder("DELETE FROM ContaPagar ");
		sql.append(whereStr.toString());
		getConexao().getJdbcTemplate().update(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}

	public List<ContaPagarVO> consultaRapidaContaPagarPorMatriculaPeriodoConvenio(String matricula, Integer matriculaPeriodo, Integer convenio, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("Select * from contaPagar ");
		sql.append("WHERE (matricula='").append(matricula).append("') ");
		sql.append(" AND (tipoorigem='").append(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.getValor()).append("') ");
		sql.append(" AND (codorigem='").append(matriculaPeriodo).append("') ");
		sql.append(" AND (convenio=").append(convenio).append(") ");
		sql.append("order by dataVencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagar</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ContaPagarVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagar WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoGrupoContaPagar(Integer grupoContaPagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagar WHERE grupoContaPagar = " + grupoContaPagar.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public ContaPagarVO consultarPorChavePrimariaSituacao(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sql = "SELECT * FROM ContaPagar WHERE codigo = ? and (situacao = 'PA' OR situacao = 'PP')";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List<ContaPagarVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ContaPagarVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static List<ContaPagarVO> montarDadosConsultaResumida(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ContaPagarVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosResumido(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static ContaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaPagarVO obj = new ContaPagarVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setDataVencimentoAntesAlteracao(dadosSQL.getTimestamp("dataVencimento"));
		obj.setDataFatoGerador(dadosSQL.getDate("dataFatoGerador"));
		obj.setDataCompetenciaAntesAlteracao(dadosSQL.getDate("dataFatoGerador"));
		obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
		obj.setNossoNumero(dadosSQL.getLong("nossoNumero"));
		obj.getFornecedor().setCodigo((dadosSQL.getInt("fornecedor")));
		obj.getParceiro().setCodigo((dadosSQL.getInt("parceiro")));
		obj.getFuncionario().setCodigo((dadosSQL.getInt("funcionario")));
		obj.getBanco().setCodigo((dadosSQL.getInt("banco")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.getResponsavelFinanceiro().setCodigo((dadosSQL.getInt("responsavelFinanceiro")));
		obj.setValor((dadosSQL.getDouble("valor")));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setCodOrigem(dadosSQL.getString("codOrigem"));
		obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.setJuro((dadosSQL.getDouble("juro")));
		obj.setValorPago((dadosSQL.getDouble("valorPago")));
		obj.setMulta((dadosSQL.getDouble("multa")));
		obj.setDescontoPorUsoAdiantamento(dadosSQL.getDouble("descontoPorUsoAdiantamento"));
		obj.setValorUtilizadoAdiantamento(dadosSQL.getDouble("valorUtilizadoAdiantamento"));
		obj.setValorUtilizadoAdiantamentoBackup(dadosSQL.getDouble("valorUtilizadoAdiantamento"));
		obj.setNumeroNotaFiscalEntrada(dadosSQL.getString("numeroNotaFiscalEntrada"));
		obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
		obj.setLinhaDigitavel1(dadosSQL.getString("linhaDigitavel1"));
		obj.setLinhaDigitavel2(dadosSQL.getString("linhaDigitavel2"));
		obj.setLinhaDigitavel3(dadosSQL.getString("linhaDigitavel3"));
		obj.setLinhaDigitavel4(dadosSQL.getString("linhaDigitavel4"));
		obj.setLinhaDigitavel5(dadosSQL.getString("linhaDigitavel5"));
		obj.setLinhaDigitavel6(dadosSQL.getString("linhaDigitavel6"));
		obj.setLinhaDigitavel7(dadosSQL.getString("linhaDigitavel7"));
		obj.setLinhaDigitavel8(dadosSQL.getString("linhaDigitavel8"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setOrigemRenegociacaoPagar((dadosSQL.getInt("origemRenegociacaoPagar")));
		obj.getGrupoContaPagar().setCodigo((dadosSQL.getInt("grupoContaPagar")));
		obj.getPessoa().setCodigo((dadosSQL.getInt("pessoa")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDesconto((dadosSQL.getDouble("desconto")));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente"));
		obj.getOperadoraCartao().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.getConvenio().setCodigo(dadosSQL.getInt("convenio"));
		obj.getFormaPagamentoVO().setCodigo(dadosSQL.getInt("formapagamento"));
		obj.getBancoRemessaPagar().setCodigo(dadosSQL.getInt("bancoremessapagar"));
		obj.getBancoRecebimento().setCodigo(dadosSQL.getInt("bancorecebimento"));
		obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroAgenciaRecebimento"));
		obj.setContaCorrenteRecebimento(dadosSQL.getString("contaCorrenteRecebimento"));
		obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoAgenciaRecebimento"));
		obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitoCorrenteRecebimento"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoServicoContaPagar"))) {
			obj.setTipoServicoContaPagar(TipoServicoContaPagarEnum.valueOf(dadosSQL.getString("tipoServicoContaPagar")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoLancamentoContaPagar"))) {
			obj.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.valueOf(dadosSQL.getString("tipoLancamentoContaPagar")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("modalidadeTransferenciaBancariaEnum"))) {
			obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.valueOf(dadosSQL.getString("modalidadeTransferenciaBancariaEnum")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoContaEnum"))) {
			obj.setTipoContaEnum(TipoContaEnum.valueOf(dadosSQL.getString("tipoContaEnum")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadeDocEnum"))) {
			obj.setFinalidadeDocEnum(FinalidadeDocEnum.valueOf(dadosSQL.getString("finalidadeDocEnum")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadeTedEnum"))) {
			obj.setFinalidadeTedEnum(FinalidadeTedEnum.valueOf(dadosSQL.getString("finalidadeTedEnum")));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoContribuinte"))) {
			obj.setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.valueOf(dadosSQL.getString("tipoIdentificacaoContribuinte")));
		}
		obj.setCodigoReceitaTributo(dadosSQL.getString("codigoReceitaTributo"));
		obj.setIdentificacaoContribuinte(dadosSQL.getString("identificacaoContribuinte"));
		obj.setNumeroReferencia(dadosSQL.getString("numeroReferencia"));
		obj.setValorReceitaBrutaAcumulada(dadosSQL.getDouble("valorReceitaBrutaAcumulada"));
		obj.setPercentualReceitaBrutaAcumulada(dadosSQL.getDouble("percentualReceitaBrutaAcumulada"));
		obj.setCodigoNotaFiscalEntrada(dadosSQL.getString("codigoNotaFiscalEntrada"));

		obj.setDataCancelamento(dadosSQL.getTimestamp("dataCancelamento"));
		obj.setMotivoCancelamento(dadosSQL.getString("motivoCancelamento"));

		
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("arquivo"))) {
			obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadePixEnum"))) {
			obj.setFinalidadePixEnum(FinalidadePixEnum.valueOf(dadosSQL.getString("finalidadePixEnum")));
		}
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))) {
			obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));
		}
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("chaveEnderecamentoPix"))) {
	       obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix"));
        }

		// obj.setFormaPagamentoContaPagarRemessa(FormaPagamentoContaPagarRemessaEnum.getEnum(dadosSQL.getString("formapagamentocontapagar")));
		if (!obj.getSituacao().equals("AP")) {
			obj.setPagamentos(consultarCodigoPagamentos(obj.getCodigo()));
		}
		obj.setNovoObj(Boolean.FALSE);
		obj.setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosGrupoContaPagar(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosBancoRemessaPagar(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		obj.setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo(), usuario));
		obj.setContaPagarPagamentoVOs(ContaPagarPagamento.consultarContaPagarPagamentos(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setListaContaPagarAdiantamentoVO(getFacadeFactory().getContaPagarAdiantamentoFacade().consultarContaPagarUtilizadaPorCodigoContaPagar(obj.getCodigo(), nivelMontarDados, usuario));		
		obj.setListaLancamentoContabeisCredito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, TipoPlanoContaEnum.CREDITO, false, nivelMontarDados, usuario));
		obj.setListaLancamentoContabeisDebito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, TipoPlanoContaEnum.DEBITO, false, nivelMontarDados, usuario));
		obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		
		if(obj.getTipoOrigemEnum().isAdiantamento()){
			obj.setListaContaPagarAdiantamentoUtilizado(getFacadeFactory().getContaPagarAdiantamentoFacade().consultarContaPagarPorCodigoContaPagarUtilizada(obj.getCodigo(), nivelMontarDados, usuario));				
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosGrupoContaPagar(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosBancoRemessaPagar(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosGrupoContaPagar(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosBancoRemessaPagar(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosBancoRecebimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosConvenio(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	private static void montarDadosArquivo(ContaPagarVO obj, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVO().getCodigo() == 0) {
			obj.setArquivoVO(new ArquivoVO());
			return;
		}
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), nivelmontardadosDadosbasicos, usuario));
	}

	public static String consultarCodigoPagamentos(Integer codigo) {
		return getConexao().getJdbcTemplate().queryForObject("select array_to_string(array(select negociacaocontapagar from contapagarnegociacaopagamento where contapagarnegociacaopagamento.contapagar = contapagar.codigo), ', ') from contapagar where codigo = " + codigo, String.class);
	}

	public static ContaPagarVO montarDadosResumido(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaPagarVO obj = new ContaPagarVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setDataVencimentoAntesAlteracao(dadosSQL.getDate("dataVencimento"));
		obj.setDataFatoGerador(dadosSQL.getDate("dataFatoGerador"));
		obj.setDataCompetenciaAntesAlteracao(dadosSQL.getDate("dataFatoGerador"));
		obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
		obj.setNossoNumero(dadosSQL.getLong("nossoNumero"));
		obj.getFornecedor().setCodigo((dadosSQL.getInt("fornecedor")));
		obj.getFuncionario().setCodigo((dadosSQL.getInt("funcionario")));
		obj.getBanco().setCodigo((dadosSQL.getInt("banco")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.setValor((dadosSQL.getDouble("valor")));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setCodOrigem(dadosSQL.getString("codOrigem"));
		obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.setJuro((dadosSQL.getDouble("juro")));
		obj.setValorPago((dadosSQL.getDouble("valorPago")));
		obj.setMulta((dadosSQL.getDouble("multa")));
		obj.setDescontoPorUsoAdiantamento((dadosSQL.getDouble("descontoPorUsoAdiantamento")));
		obj.setValorUtilizadoAdiantamento((dadosSQL.getDouble("valorUtilizadoAdiantamento")));
		obj.setNumeroNotaFiscalEntrada(dadosSQL.getString("numeroNotaFiscalEntrada"));
		obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setOrigemRenegociacaoPagar((dadosSQL.getInt("origemRenegociacaoPagar")));
		obj.getGrupoContaPagar().setCodigo((dadosSQL.getInt("grupoContaPagar")));
		obj.getPessoa().setCodigo((dadosSQL.getInt("pessoa")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDesconto((dadosSQL.getDouble("desconto")));
		obj.setUpdated(dadosSQL.getDate("updated"));

		obj.getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));
		obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("pessoafuncionario.nome"));
		obj.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavelFinanceiro"));
		obj.getResponsavelFinanceiro().setNome(dadosSQL.getString("responsavelFinanceiro.nome"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getBanco().setNome(dadosSQL.getString("banco.nome"));
		obj.getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
		obj.getOperadoraCartao().setNome(dadosSQL.getString("operadoracartao.nome"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.setCodigoNotaFiscalEntrada(dadosSQL.getString("codigoNotaFiscalEntrada"));
		obj.setDataCancelamento(dadosSQL.getTimestamp("dataCancelamento"));
		obj.setMotivoCancelamento(dadosSQL.getString("motivoCancelamento"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static void montarDadosResponsavelFinanceiro(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelFinanceiro().getCodigo().intValue() == 0) {
			return;
		}
		getFacadeFactory().getPessoaFacade().carregarDadosPessoaRemessaContaPagar(obj.getResponsavelFinanceiro(), nivelMontarDados, usuario);
	}

	public static void montarDadosUnidadeEnsino(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosConvenio(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getConvenio().getCodigo().intValue() == 0) {
			return;
		}
		obj.setConvenio(getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(obj.getConvenio().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	public static void montarDadosBanco(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBanco().getCodigo().intValue() == 0) {
			obj.setBanco(new BancoVO());
			return;
		}
		obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosBancoRemessaPagar(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBancoRemessaPagar().getCodigo().intValue() == 0) {
			obj.setBancoRemessaPagar(new BancoVO());
			return;
		}
		obj.setBancoRemessaPagar(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoRemessaPagar().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosBancoRecebimento(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBancoRecebimento().getCodigo().intValue() == 0) {
			obj.setBancoRecebimento(new BancoVO());
			return;
		}
		obj.setBancoRecebimento(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoRecebimento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavel(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosContaCorrente(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrente().getCodigo().intValue() == 0) {
			obj.setContaCorrente(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFormaPagamento(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamentoVO().getCodigo().intValue() == 0) {
			obj.setFormaPagamentoVO(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>FornecedorVO</code> relacionado ao objeto <code>ContaPagarVO</code> . Faz uso da chave primária da classe <code>FornecedorVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFornecedor(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosOperadoraCartao(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOperadoraCartao().getCodigo().intValue() == 0) {
			obj.setOperadoraCartao(new OperadoraCartaoVO());
			return;
		}
		obj.setOperadoraCartao(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartao().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosParceiro(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosGrupoContaPagar(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getGrupoContaPagar().getCodigo().intValue() == 0) {
			obj.setGrupoContaPagar(new GrupoContaPagarVO());
			return;
		}
		obj.setGrupoContaPagar(getFacadeFactory().getGrupoContaPagarFacade().consultarPorChavePrimaria(obj.getGrupoContaPagar().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>ContaPagarVO</code>. Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFuncionario(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	public static void montarDadosPessoa(ContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		getFacadeFactory().getPessoaFacade().carregarDadosPessoaRemessaContaPagar(obj.getPessoa(), nivelMontarDados, usuario);
	}

	public ContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ContaPagar WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaPagar ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public ContaPagarVO consultarPorNossoNumero(Long codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ContaPagar WHERE nossonumero = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;
	}

	public List<ContaPagarVO> consultarPorOrigemContaPagar(String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select * from contapagar where codorigem = ? and tipoorigem = ? ");
		if (situacaoFinanceira != null && !situacaoFinanceira.trim().isEmpty()) {
			sql.append(" and situacao =  '").append(situacaoFinanceira).append("' ");
		}
		sql.append(" order by dataVencimento");
		SqlRowSet result = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoOrigem, tipoOrigem);
		return montarDadosConsulta(result, nivelMontarDados, usuario);
	}

	// Metodo para fazer consulta de contas a pagar vencidas e não pagas
	public List consultarPorContaPagarVencida(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		Date data = new Date();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT Fornecedor.nome, ContaPagar.nrdocumento, ContaPagar.valor, ContaPagar.datavencimento FROM ContaPagar, Fornecedor WHERE ContaPagar.fornecedor = Fornecedor.codigo";
		sql += " AND ContaPagar.situacao = 'AP' AND ContaPagar.datavencimento < '" + data + "' GROUP BY Fornecedor.nome, ContaPagar.nrdocumento, ContaPagar.valor, ContaPagar.datavencimento";
		sql += " ORDER BY ContaPagar.datavencimento";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeFavorecido(String nomeFavorecido, String situacao, Date dataIni, Date dataFim, Integer unidadeEnsino, Boolean filtrarDataFatoGerador, boolean controlarAcesso, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(" SELECT contapagar.* FROM contapagar ");
			sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
			sql.append(" left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
			sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario  ");
			sql.append(" left join parceiro on parceiro.codigo = contapagar.parceiro  ");
			sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
			sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
			sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
			sql.append(" left join banco on banco.codigo = contapagar.banco ");
			if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
				sql.append(" where 1 = 1 ");
			}else {
				sql.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos(?))");
				sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos(?)) or upper(sem_acentos(parceiro.nome)) like(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(banco.nome)) like(sem_acentos(?)) or upper(sem_acentos(pessoa.nome)) like(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(operadoracartao.nome)) like(sem_acentos(?)) or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos(?)))");
			}
			if(Uteis.isAtributoPreenchido(situacao)) {
			sql.append("  and contapagar.situacao = ('");
			sql.append(situacao.toUpperCase());
			sql.append("') ");
			}
			if (!filtrarDataFatoGerador) {
				sql.append("and contapagar.datavencimento between '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)));
				sql.append("' and '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
				sql.append("'");
			} else {
				sql.append("and contapagar.dataFatoGerador between '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)));
				sql.append("' and '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
				sql.append("'");
			}

			if (unidadeEnsino != 0) {
				sql.append("AND contapagar.unidadeensino = ");
				sql.append(unidadeEnsino);
			}
			sql.append("order by contapagar.datavencimento");
			if (limite != null) {
				sql.append(" LIMIT ").append(limite);
				if (offset != null) {
					sql.append(" OFFSET ").append(offset);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		SqlRowSet resultado = null;
		
		if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {
			nomeFavorecido = nomeFavorecido.toUpperCase()+"%";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido);
		}
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);
	}

	public Integer consultarPorNomeFavorecidoTotalRegistros(String nomeFavorecido, String situacao, Date dataIni, Date dataFim, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(" SELECT count(contapagar.codigo) FROM contapagar ");
			sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
			sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario  ");
			sql.append(" left join parceiro on parceiro.codigo = contapagar.parceiro ");
			sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
			sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
			sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
			sql.append(" left join banco on banco.codigo = contapagar.banco ");
			sql.append(" left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
			if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
				sql.append(" where 1 = 1 ");
			}else {
				sql.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos(?))");
				sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos(?)) or upper(sem_acentos(parceiro.nome)) like(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(banco.nome)) like(sem_acentos(?)) or upper(sem_acentos(pessoa.nome)) like(sem_acentos(?)) ");
				sql.append(" or upper(sem_acentos(operadoracartao.nome)) like(sem_acentos(?)) or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos(?)))");
			}
			if(Uteis.isAtributoPreenchido(situacao)) {
				sql.append("  and contapagar.situacao =('");
				sql.append(situacao.toUpperCase());
				sql.append("') ");
				}
			if (!filtrarDataFatorGerador) {
				sql.append(" and contapagar.datavencimento between '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)));
				sql.append("' and '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
				sql.append("' ");
			} else {
				sql.append(" and contapagar.dataFatoGerador between '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)));
				sql.append("' and '");
				sql.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
				sql.append("' ");
			}
			if (unidadeEnsino != 0) {
				sql.append("AND contapagar.unidadeensino = ").append(unidadeEnsino);
			}
			SqlRowSet resultado = null;
			
			if(!Uteis.isAtributoPreenchido(nomeFavorecido)) {
				resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			}else {
				nomeFavorecido = nomeFavorecido.toUpperCase()+"%";
				resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido, nomeFavorecido);
			}
			if (resultado.next()) {
				return resultado.getInt("count");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List consultarPorNomeFavorecidoDataVencimento(String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select contapagar.* from contapagar ");
		sqlStr.append("left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sqlStr.append("left join funcionario on funcionario.codigo = contapagar.funcionario ");
		sqlStr.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sqlStr.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contaPagar.responsavelFinanceiro ");
		sqlStr.append("left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sqlStr.append("left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
		sqlStr.append("left join banco on banco.codigo = contapagar.banco ");
		sqlStr.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))");
		sqlStr.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))");
		sqlStr.append(" or upper(sem_acentos(banco.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))");
		sqlStr.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))");
		sqlStr.append(" or upper(sem_acentos(operadoracartao.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))");
		sqlStr.append(" or upper(sem_acentos(pessoa.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%'))) AND ");
		sqlStr.append(" contapagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		if (unidadeEnsino != 0) {
			sqlStr.append("AND contapagar.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append("order by contapagar.datavencimento");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoNomeFavorecidoDataVencimento(Integer codigoFavorecido, String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select contapagar.* from contapagar ");
		sqlStr.append("left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sqlStr.append("left join funcionario on funcionario.codigo = contapagar.funcionario ");
		sqlStr.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sqlStr.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contaPagar.responsavelFinanceiro ");
		sqlStr.append("left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sqlStr.append("left join banco on banco.codigo = contapagar.banco ");
		sqlStr.append("where ((upper(sem_acentos(fornecedor.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%')) and fornecedor.codigo = ").append(codigoFavorecido).append(") ");
		sqlStr.append("or (upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%')) and pessoafuncionario.codigo = ").append(codigoFavorecido).append(") ");
		sqlStr.append("or (upper(sem_acentos(banco.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%')) and banco.codigo = ").append(codigoFavorecido).append(") ");
		sqlStr.append("or (upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%')) and responsavelFinanceiro.codigo = ").append(codigoFavorecido).append(") ");
		sqlStr.append("or (upper(sem_acentos(pessoa.nome)) like(sem_acentos('").append(nomeFavorecido.toUpperCase()).append("%')) and pessoa.codigo = ").append(codigoFavorecido).append(")) ");
		sqlStr.append("and contapagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		if (unidadeEnsino != 0) {
			sqlStr.append("AND contapagar.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append("order by contapagar.datavencimento");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoNomeFavorecidoDataVencimentoEdicaoFuncionario(Integer codigoFavorecido, String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select contapagar.*, fornecedor.nome as \"fornecedor.nome\", pessoafuncionario.nome as \"pessoafuncionario.nome\", ");
		sqlStr.append("banco.nome as \"banco.nome\", pessoa.nome as \"pessoa.nome\",  ");
		sqlStr.append("parceiro.nome as \"parceiro.nome\", operadoracartao.nome as \"operadoracartao.nome\", ");
		sqlStr.append("unidadeensino.nome as \"unidadeensino.nome\", responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\" from contapagar ");
		sqlStr.append("left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sqlStr.append("left join funcionario on funcionario.codigo = contapagar.funcionario ");
		sqlStr.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sqlStr.append("left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sqlStr.append("left join banco on banco.codigo = contapagar.banco ");
		sqlStr.append("left join parceiro on parceiro.codigo = contapagar.parceiro ");
		sqlStr.append("left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
		sqlStr.append("left join unidadeensino on unidadeensino.codigo = contapagar.unidadeensino ");
		sqlStr.append("LEFT JOIN Pessoa as responsavelFinanceiro ON contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo ");
		sqlStr.append("where (( ");
		if (nomeFavorecido != null && !nomeFavorecido.equals("")) {
			sqlStr.append("upper(pessoafuncionario.nome) like('").append(nomeFavorecido.toUpperCase()).append("%') and ");
		}
		sqlStr.append("pessoafuncionario.codigo = ").append(codigoFavorecido).append(") ");
		sqlStr.append("or ( ");
		if (nomeFavorecido != null && !nomeFavorecido.equals("")) {
			sqlStr.append("upper(pessoa.nome) like('").append(nomeFavorecido.toUpperCase()).append("%') and ");
		}
		sqlStr.append("pessoa.codigo = ").append(codigoFavorecido).append(")) ");
		sqlStr.append("and contapagar.datavencimento between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		if (unidadeEnsino != 0) {
			sqlStr.append("AND contapagar.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append("order by contapagar.datavencimento");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaResumida(resultado, nivelMontarDados, usuario);
	}

	public void executarCriacaoGraficoLinhaContaPagar(ContaPagarVO contaPagarVO, boolean filtroFavorecido, Date dataInicio, Date dataTermino) throws Exception {
		contaPagarVO.setGraficoContaPagarLinhaTempo(new TimeSeriesCollection());
		SqlRowSet rs = null;
		// TimeSeries valor = null;
		Boolean montarGraficoPorDia = false;
		rs = consultarQtdeMesesContaPagarGraficoLinhaTempo(filtroFavorecido, contaPagarVO, dataInicio, dataTermino);
		if (rs.next()) {
			if (rs.getInt("qtde") < 2) {
				montarGraficoPorDia = true;
			}
		}
		rs = null;
		if (montarGraficoPorDia) {
			rs = consultarValorContaPagarGraficoLinhaTempo(filtroFavorecido, contaPagarVO, dataInicio, dataTermino, montarGraficoPorDia);
		} else {
			rs = consultarValorContaPagarGraficoLinhaTempo(filtroFavorecido, contaPagarVO, dataInicio, dataTermino, montarGraficoPorDia);
		}
		contaPagarVO.setListaGraficoContaPagar("");
		while (rs.next()) {
			if (!contaPagarVO.getListaGraficoContaPagar().isEmpty()) {
				contaPagarVO.setListaGraficoContaPagar(contaPagarVO.getListaGraficoContaPagar() + ",");
			}
			if (montarGraficoPorDia) {
				contaPagarVO.setListaGraficoContaPagar(contaPagarVO.getListaGraficoContaPagar() + "[Date.UTC(" + rs.getInt("ano") + ", " + rs.getInt("mes") + "," + rs.getInt("dia") + "), " + rs.getDouble("valor") + "]");
			} else {
				contaPagarVO.setListaGraficoContaPagar(contaPagarVO.getListaGraficoContaPagar() + "[Date.UTC(" + rs.getInt("ano") + ", " + rs.getInt("mes") + ",1), " + rs.getDouble("valor") + "]");
			}
		}

	}

	public SqlRowSet consultarQtdeMesesContaPagarGraficoLinhaTempo(boolean filtroFavorecido, ContaPagarVO contaPagarVO, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sb = new StringBuilder("select count(distinct (extract(month from (cp.datavencimento))::INT)) qtde ");
		sb.append("from contapagar cp ");
		sb.append("where cp.datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and cp.datavencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		if (filtroFavorecido) {
			if (contaPagarVO.getTipoSacado().equals("FO")) {
				sb.append("AND cp.fornecedor = ").append(contaPagarVO.getFornecedor().getCodigo()).append(" ");
			} else if (contaPagarVO.getTipoSacado().equals("BA")) {
				sb.append("AND cp.banco = ").append(contaPagarVO.getBanco().getCodigo()).append(" ");
			} else if (contaPagarVO.getTipoSacado().equals("AL") || contaPagarVO.getTipoSacado().equals("FU")) {
				sb.append("AND cp.pessoa = ").append(contaPagarVO.getPessoa().getCodigo()).append(" ");
			}
			//PEDROsb.append("AND cp.centroDespesa = ").append(contaPagarVO.getCentroDespesa().getCodigo()).append(" ");
		} else {
			//PEDROsb.append("AND cp.centroDespesa = ").append(contaPagarVO.getCentroDespesa().getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	public SqlRowSet consultarValorContaPagarGraficoLinhaTempo(boolean filtroFavorecido, ContaPagarVO contaPagarVO, Date dataInicio, Date dataTermino, Boolean montarGraficoPorDia) throws Exception {
		StringBuilder sb = new StringBuilder("select sum(trunc(valor::NUMERIC,2)) as valor, ano, mes ");
		if (montarGraficoPorDia) {
			sb.append(", dia ");
		}
		sb.append("from( ");
		sb.append("select distinct cp.codigo, valor, extract(year from max(cp.datavencimento))::INT as ano, extract(month from max(cp.datavencimento))::INT as mes ");
		if (montarGraficoPorDia) {
			sb.append(", extract(day from max(cp.datavencimento))::INT as dia ");
		}
		sb.append("from contapagar cp ");
		sb.append("where cp.datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and cp.datavencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		if (filtroFavorecido) {
			if (contaPagarVO.getTipoSacado().equals("FO")) {
				sb.append("AND cp.fornecedor = ").append(contaPagarVO.getFornecedor().getCodigo()).append(" ");
			} else if (contaPagarVO.getTipoSacado().equals("BA")) {
				sb.append("AND cp.banco = ").append(contaPagarVO.getBanco().getCodigo()).append(" ");
			} else if (contaPagarVO.getTipoSacado().equals("AL") || contaPagarVO.getTipoSacado().equals("FU")) {
				sb.append("AND cp.pessoa = ").append(contaPagarVO.getPessoa().getCodigo()).append(" ");
			}
			//PEDROsb.append("AND cp.centroDespesa = ").append(contaPagarVO.getCentroDespesa().getCodigo()).append(" ");
		} else {
			//PEDROsb.append("AND cp.centroDespesa = ").append(contaPagarVO.getCentroDespesa().getCodigo()).append(" ");
		}
		sb.append("group by cp.codigo, valor order by ano, mes");
		if (montarGraficoPorDia) {
			sb.append(", dia ");
		}
		sb.append(") as t group by ano, mes ");
		if (montarGraficoPorDia) {
			sb.append(", dia ");
		}
		sb.append("order by ano, mes");
		if (montarGraficoPorDia) {
			sb.append(", dia ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	private StringBuilder getSQLPadraoConsultaTotalPagar() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(contapagar.valor - (case when contapagar.desconto is null then 0 else contapagar.desconto end) + (case when contapagar.juro is null then 0 else contapagar.juro end) + (case when contapagar.multa is null then 0 else contapagar.multa end)) AS valorAPagar, 0 AS valorPago, 0 AS valorNegociado ");
		sql.append("FROM contapagar ");
		sql.append("LEFT JOIN fornecedor ON fornecedor.codigo = contapagar.fornecedor ");
		sql.append("LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
		sql.append("LEFT JOIN funcionario ON funcionario.codigo = contapagar.funcionario ");
		sql.append("LEFT JOIN pessoa AS pessoafuncionario ON pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN pessoa ON pessoa.codigo = contapagar.pessoa ");
		sql.append("LEFT JOIN banco ON banco.codigo = contapagar.banco ");
		sql.append("LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
		sql.append("LEFT JOIN Pessoa as responsavelFinanceiro ON contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo ");		
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaTotalPago() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 0 AS valorAPagar, sum(contapagar.valorPago) AS valorPago, 0 AS valorNegociado ");
		sql.append("FROM contapagar ");
		sql.append("LEFT JOIN fornecedor ON fornecedor.codigo = contapagar.fornecedor ");
		sql.append("LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
		sql.append("LEFT JOIN funcionario ON funcionario.codigo = contapagar.funcionario ");
		sql.append("LEFT JOIN pessoa AS pessoafuncionario ON pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN pessoa ON pessoa.codigo = contapagar.pessoa ");
		sql.append("LEFT JOIN banco ON banco.codigo = contapagar.banco ");
		sql.append("LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
		sql.append("LEFT JOIN Pessoa as responsavelFinanceiro ON contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo ");
		return sql;
	}

	
	private StringBuilder getSQLPadraoConsultaTotalNegociado() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 0 AS valorAPagar, 0 AS valorPago, sum(contapagar.valor - (case when contapagar.desconto is null then 0 else contapagar.desconto end) + (case when contapagar.juro is null then 0 else contapagar.juro end) + (case when contapagar.multa is null then 0 else contapagar.multa end)) as valorNegociado ");
		sql.append("FROM contapagar ");
		sql.append("LEFT JOIN fornecedor ON fornecedor.codigo = contapagar.fornecedor ");
		sql.append("LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
		sql.append("LEFT JOIN funcionario ON funcionario.codigo = contapagar.funcionario ");
		sql.append("LEFT JOIN pessoa AS pessoafuncionario ON pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN pessoa ON pessoa.codigo = contapagar.pessoa ");
		sql.append("LEFT JOIN banco ON banco.codigo = contapagar.banco ");
		sql.append("LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
		sql.append("LEFT JOIN Pessoa as responsavelFinanceiro ON contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo ");

		return sql;
	}

	@Override
	public void realizarVinculoContaReceberComResponsavelFinanceiro(ContaPagarVO contaPagarVO, UsuarioVO usuarioLogado) throws Exception {
		if (contaPagarVO.getTipoSacado().equals("AL")) {
			PessoaVO responsavelFinanceiro = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(contaPagarVO.getPessoa().getCodigo(), usuarioLogado);
			if (responsavelFinanceiro != null) {
				contaPagarVO.setResponsavelFinanceiro(responsavelFinanceiro);
				contaPagarVO.setTipoSacado(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
			}
		}
	}

	@Override
	public List<ContaPagarVO> consultarContaPagarPorUnidadesCategoriaDespesaPeriodo(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer limit, Integer offset, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder("");
		List<ContaPagarVO> contaPagarVOs = new ArrayList<ContaPagarVO>(0);
		if (trazerContasAPagar || trazerContasPagas) {
			sb.append(" select distinct contapagar.codigo, contapagar.dataVencimento, contapagar.dataFatoGerador, contaPagar.valor, contaPagar.valorPago, contaPagar.situacao, contaPagar.tipoSacado, ");
			sb.append(" contapagar.fornecedor, fornecedor.nome as \"fornecedor.nome\",   ");
			sb.append(" contapagar.funcionario, pessoaFunc.codigo as \"pessoaFunc.codigo\", pessoaFunc.nome as \"pessoaFunc.nome\", ");
			sb.append(" contapagar.banco, banco.nome as \"banco.nome\", ");
			sb.append(" contapagar.pessoa, pessoa.nome as \"pessoa.nome\",  ");
			sb.append(" contapagar.parceiro, parceiro.nome as \"parceiro.nome\",  ");
			sb.append(" contapagar.operadoracartao, operadoracartao.nome as \"operadoracartao.nome\",  ");
			sb.append(" contapagar.responsavelFinanceiro, responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", ");
			sb.append(" contapagar.UnidadeEnsino, UnidadeEnsino.nome as \"UnidadeEnsino.nome\", ");
			sb.append(" trim(case when fornecedor.nome is null then '' else fornecedor.nome end ) || ");
			sb.append(" trim(case when pessoaFunc.nome is null then '' else pessoaFunc.nome end) || ");
			sb.append(" trim(case when banco.nome is null then '' else banco.nome end) ||  ");
			sb.append(" trim(case when pessoa.nome is null then '' else pessoa.nome end) ||  ");
			sb.append(" trim(case when parceiro.nome is null then '' else parceiro.nome end) ||  ");
			sb.append(" trim(case when operadoracartao.nome is null then '' else operadoracartao.nome end) ||  ");
			sb.append(" trim(case when responsavelFinanceiro.nome is null then '' else responsavelFinanceiro.nome end) as favorecido ");
			sb.append(" from contapagar ");
			sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = contapagar.UnidadeEnsino ");
			sb.append(" LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
			sb.append(" LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
			sb.append(" left join departamento on departamento.codigo = contapagar.departamento ");
			sb.append(" left join turma on turma.codigo = contapagar.turma ");
			sb.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
			sb.append(" left join funcionario on funcionario.codigo = contapagar.funcionario ");
			sb.append(" left join pessoa as pessoaFunc on pessoaFunc.codigo = funcionario.pessoa ");
			sb.append(" left join banco on banco.codigo = contapagar.banco ");
			sb.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
			sb.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
			
			sb.append(" where 1 = 1");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
				if (identificadorCategoriaPrincipal.equals("-1")) {
					sb.append(" and not exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar) ");				
				} else {
					sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
					sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
					sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
					sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
					sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					sb.append(" ) ");
				}
			}
			if (!unidadeEnsinoVOs.isEmpty()) {
				sb.append(" and contapagar.unidadeensino in (");
				int x = 0;
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						if (x > 0) {
							sb.append(", ");
						}
						sb.append(unidadeEnsinoVO.getCodigo());
						x++;
					}
				}
				sb.append(") ");
			}
			sb.append(" and ( ");
			if (trazerContasAPagar) {
				sb.append(" ( contapagar.situacao = 'AP' ");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			if (trazerContasAPagar && trazerContasPagas) {
				sb.append(" or ");
			}

			if (trazerContasPagas) {
				sb.append(" ( contapagar.situacao in ('PA', 'PP') ");
				sb.append(" and contapagar.codigo in (select contapagar ");
				sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
				sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
				sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
				sb.append(" )");
				sb.append(" ) ");
			}

			sb.append(" ) ");
			sb.append(" order by contapagar.dataVencimento ");
			if (limit != null && limit > 0) {
				sb.append(" limit ").append(limit).append(" offset ").append(offset);
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			ContaPagarVO obj = null;
			while (rs.next()) {
				obj = new ContaPagarVO();
				obj.setCodigo(rs.getInt("codigo"));
				obj.setDataVencimento(rs.getDate("dataVencimento"));
				obj.setDataFatoGerador(rs.getDate("dataFatoGerador"));
				obj.setValor(rs.getDouble("valor"));
				obj.setValorPago(rs.getDouble("valorPago"));
				obj.setSituacao(rs.getString("situacao"));
				obj.setTipoSacado(rs.getString("tipoSacado"));
				obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				//PEDROobj.getCentroDespesa().setCodigo(rs.getInt("centroDespesa"));
				//PEDROobj.getCentroDespesa().setIdentificadorCategoriaDespesa(rs.getString("identificadorCategoriaDespesa"));
				//PEDROobj.getCentroDespesa().setDescricao(rs.getString("descricao"));
				obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
				obj.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
				//PEDROobj.getDepartamento().setCodigo(rs.getInt("departamento"));
				//PEDROobj.getDepartamento().setNome(rs.getString("departamento.nome"));
				//PEDROobj.getTurma().setCodigo(rs.getInt("turma"));
				//PEDROobj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
				obj.getFornecedor().setCodigo(rs.getInt("fornecedor"));
				obj.getFornecedor().setNome(rs.getString("fornecedor.nome"));
				obj.getBanco().setCodigo(rs.getInt("banco"));
				obj.getBanco().setNome(rs.getString("banco.nome"));
				obj.getPessoa().setCodigo(rs.getInt("pessoa"));
				obj.getPessoa().setNome(rs.getString("pessoa.nome"));
				obj.getResponsavelFinanceiro().setCodigo(rs.getInt("responsavelFinanceiro"));
				obj.getResponsavelFinanceiro().setNome(rs.getString("responsavelFinanceiro.nome"));
				obj.getFuncionario().setCodigo(rs.getInt("funcionario"));
				obj.getFuncionario().getPessoa().setCodigo(rs.getInt("pessoaFunc.codigo"));
				obj.getFuncionario().getPessoa().setNome(rs.getString("pessoaFunc.nome"));
				obj.getParceiro().setCodigo(rs.getInt("parceiro"));
				obj.getParceiro().setNome(rs.getString("parceiro.nome"));
				obj.getOperadoraCartao().setCodigo(rs.getInt("operadoracartao"));
				obj.getOperadoraCartao().setNome(rs.getString("operadoracartao.nome"));
				contaPagarVOs.add(obj);
			}
		}
		return contaPagarVOs;

	}

	@Override
	public List<ContaPagarVO> consultarContaPagarPorUnidadesCategoriaDespesaPorDepartamento(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer codigoDepartamento, Integer limit, Integer offset, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder("");
		List<ContaPagarVO> contaPagarVOs = new ArrayList<ContaPagarVO>(0);
		if (trazerContasAPagar || trazerContasPagas) {
			sb.append(" select distinct contapagar.codigo, contapagar.dataVencimento, contapagar.dataFatoGerador, contaPagar.valor, contaPagar.valorPago, contaPagar.situacao, contaPagar.tipoSacado, ");
			sb.append(" contapagar.fornecedor, fornecedor.nome as \"fornecedor.nome\",   ");
			sb.append(" contapagar.funcionario, pessoaFunc.codigo as \"pessoaFunc.codigo\", pessoaFunc.nome as \"pessoaFunc.nome\", ");
			sb.append(" contapagar.banco, banco.nome as \"banco.nome\", ");
			sb.append(" contapagar.pessoa, pessoa.nome as \"pessoa.nome\",  ");
			sb.append(" contapagar.parceiro, parceiro.nome as \"parceiro.nome\",  ");
			sb.append(" contapagar.operadoracartao, operadoracartao.nome as \"operadoracartao.nome\",  ");
			sb.append(" contapagar.responsavelFinanceiro, responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", ");
			sb.append(" contapagar.UnidadeEnsino, UnidadeEnsino.nome as \"UnidadeEnsino.nome\", ");
			sb.append(" trim(case when fornecedor.nome is null then '' else fornecedor.nome end ) || ");
			sb.append(" trim(case when pessoaFunc.nome is null then '' else pessoaFunc.nome end) || ");
			sb.append(" trim(case when banco.nome is null then '' else banco.nome end) ||  ");
			sb.append(" trim(case when pessoa.nome is null then '' else pessoa.nome end) ||  ");
			sb.append(" trim(case when responsavelFinanceiro.nome is null then '' else responsavelFinanceiro.nome end) as favorecido ");
			sb.append(" from contapagar ");
			sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = contapagar.UnidadeEnsino ");
			sb.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
			sb.append(" LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
			sb.append(" LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
			sb.append(" left join funcionario on funcionario.codigo = contapagar.funcionario ");
			sb.append(" left join pessoa as pessoaFunc on pessoaFunc.codigo = funcionario.pessoa ");
			sb.append(" left join banco on banco.codigo = contapagar.banco ");
			sb.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
			sb.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");

			sb.append(" where 1 = 1");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
				if (identificadorCategoriaPrincipal.equals("-1")) {
					sb.append(" and not exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar) ");				
				} else {
					sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
					sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
					sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
					sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
					sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
					sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					sb.append(" ) ");
				}
			}
			if (!unidadeEnsinoVOs.isEmpty()) {
				sb.append(" and contapagar.unidadeensino in (0");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sb.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sb.append(") ");
			}
			sb.append(" and ( ");
			if (trazerContasAPagar) {
				sb.append(" ( contapagar.situacao = 'AP' ");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			if (trazerContasAPagar && trazerContasPagas) {
				sb.append(" or ");
			}

			if (trazerContasPagas) {
				sb.append(" ( contapagar.situacao in ('PA', 'PP') ");
//				sb.append(" and contapagar.codigo in (select contapagar ");
//				sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
//				sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
//				sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
//				sb.append(" )");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			sb.append(" ) ");
			if (Uteis.isAtributoPreenchido(codigoDepartamento)) {
				sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
				sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
				sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
				sb.append(" and centroresultadoorigem.departamento = ").append(codigoDepartamento);
				if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
					if (!identificadorCategoriaPrincipal.equals("-1")) {
						sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
						sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					}
				}
				sb.append(" ) ");
			} else {
				sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
				sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
				sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
				sb.append(" and centroresultadoorigem.departamento is null ");
				if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
					if (!identificadorCategoriaPrincipal.equals("-1")) {
						sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
						sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
			}
				}
				sb.append(" ) ");
			}
			if (Uteis.isAtributoPreenchido(dataInicioCompetencia) && Uteis.isAtributoPreenchido(dataFimCompetencia)) {
				sb.append(" and contapagar.datafatogerador >= '").append(Uteis.getDataJDBC(dataInicioCompetencia)).append("  00:00:00' ");
				sb.append(" and contapagar.datafatogerador <= '").append(Uteis.getDataJDBC(dataFimCompetencia)).append("  23:59:59' ");				
			}
			sb.append(" order by contapagar.dataVencimento ");
			if (limit != null && limit > 0) {
				sb.append(" limit ").append(limit).append(" offset ").append(offset);
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			ContaPagarVO obj = null;
			while (rs.next()) {
				obj = new ContaPagarVO();
				obj.setCodigo(rs.getInt("codigo"));
				obj.setDataVencimento(rs.getDate("dataVencimento"));
				obj.setDataFatoGerador(rs.getDate("dataFatoGerador"));
				obj.setValor(rs.getDouble("valor"));
				obj.setValorPago(rs.getDouble("valorPago"));
				obj.setSituacao(rs.getString("situacao"));
				obj.setTipoSacado(rs.getString("tipoSacado"));
				obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				//PEDROobj.getCentroDespesa().setCodigo(rs.getInt("centroDespesa"));
				//PEDROobj.getCentroDespesa().setIdentificadorCategoriaDespesa(rs.getString("identificadorCategoriaDespesa"));
				//PEDROobj.getCentroDespesa().setDescricao(rs.getString("descricao"));
				obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
				obj.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
				//PEDROobj.getDepartamento().setCodigo(rs.getInt("departamento"));
				//PEDROobj.getDepartamento().setNome(rs.getString("departamento.nome"));
				//PEDROobj.getTurma().setCodigo(rs.getInt("turma"));
				//PEDROobj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
				obj.getFornecedor().setCodigo(rs.getInt("fornecedor"));
				obj.getFornecedor().setNome(rs.getString("fornecedor.nome"));
				obj.getBanco().setCodigo(rs.getInt("banco"));
				obj.getBanco().setNome(rs.getString("banco.nome"));
				obj.getPessoa().setCodigo(rs.getInt("pessoa"));
				obj.getPessoa().setNome(rs.getString("pessoa.nome"));
				obj.getResponsavelFinanceiro().setCodigo(rs.getInt("responsavelFinanceiro"));
				obj.getResponsavelFinanceiro().setNome(rs.getString("responsavelFinanceiro.nome"));
				obj.getFuncionario().setCodigo(rs.getInt("funcionario"));
				obj.getFuncionario().getPessoa().setCodigo(rs.getInt("pessoaFunc.codigo"));
				obj.getFuncionario().getPessoa().setNome(rs.getString("pessoaFunc.nome"));
				obj.getParceiro().setCodigo(rs.getInt("parceiro"));
				obj.getParceiro().setNome(rs.getString("parceiro.nome"));
				obj.getOperadoraCartao().setCodigo(rs.getInt("operadoracartao"));
				obj.getOperadoraCartao().setNome(rs.getString("operadoracartao.nome"));
				contaPagarVOs.add(obj);
			}
		}
		return contaPagarVOs;

	}

	@Override
	public Integer consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodo(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean trazerContasPagas, Boolean trazerContasAPagar) throws Exception {
		StringBuilder sb = new StringBuilder("");
		if (trazerContasAPagar || trazerContasPagas) {
			sb.append(" select count(contapagar.codigo) as qtde ");
			sb.append(" from contapagar ");
			sb.append(" where 1 = 1");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
				if (identificadorCategoriaPrincipal.equals("-1")) {
					sb.append(" and not exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar) ");				
				} else {
					sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
					sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
					sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
					sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
					sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					sb.append(" ) ");
				}
			}
			if (!unidadeEnsinoVOs.isEmpty()) {
				sb.append(" and contapagar.unidadeensino in (");
				int x = 0;
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						if (x > 0) {
							sb.append(", ");
						}
						sb.append(unidadeEnsinoVO.getCodigo());
						x++;
					}
				}
				sb.append(") ");
			}
			sb.append(" and ( ");
			if (trazerContasAPagar) {
				sb.append(" ( contapagar.situacao = 'AP' ");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			if (trazerContasAPagar && trazerContasPagas) {
				sb.append(" or ");
			}

			if (trazerContasPagas) {
				sb.append(" ( situacao in ('PA', 'PP') ");
				sb.append(" and contapagar.codigo in (select contapagar ");
				sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
				sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
				sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
				sb.append(" )");
				sb.append(" ) ");
			}

			sb.append(" ) ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (rs.next()) {
				return rs.getInt("qtde");
			}
		}
		return 0;

	}

	@Override
	public Integer consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodoDepartamento(String identificadorCategoriaPrincipal, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer codigoDepartamento) throws Exception {
		StringBuilder sb = new StringBuilder("");
		if (trazerContasAPagar || trazerContasPagas) {
			sb.append(" select count(contapagar.codigo) as qtde ");
			sb.append(" from contapagar ");
			sb.append(" where 1 = 1");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
				if (identificadorCategoriaPrincipal.equals("-1")) {
					sb.append(" and not exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar) ");				
				} else {
					sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
					sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
					sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
					sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
					sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
					sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					sb.append(" ) ");
				}
			}
			if (!unidadeEnsinoVOs.isEmpty()) {
				sb.append(" and contapagar.unidadeensino in (0");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sb.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sb.append(") ");
			}
			sb.append(" and ( ");
			if (trazerContasAPagar) {
				sb.append(" ( contapagar.situacao = 'AP' ");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			if (trazerContasAPagar && trazerContasPagas) {
				sb.append(" or ");
			}

			if (trazerContasPagas) {
				sb.append(" ( situacao in ('PA', 'PP') ");
//				sb.append(" and contapagar.codigo in (select contapagar ");
//				sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
//				sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
//				sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
//				sb.append(" )");
				sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
				sb.append(" ) ");
			}
			sb.append(" ) ");
			if (Uteis.isAtributoPreenchido(codigoDepartamento)) {
				sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
				sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
				sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
				sb.append(" and centroresultadoorigem.departamento = ").append(codigoDepartamento);
				if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
					if (!identificadorCategoriaPrincipal.equals("-1")) {
						sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
						sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
					}
				}
				sb.append(" ) ");
			} else {
				sb.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem ");
				sb.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
				sb.append(" where centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
				sb.append(" and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
				sb.append(" and centroresultadoorigem.departamento is null ");
				if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
					if (!identificadorCategoriaPrincipal.equals("-1")) {
						sb.append(" and  (categoriaDespesa.identificadorCategoriaDespesa = '").append(identificadorCategoriaPrincipal).append("' ");
						sb.append(" or  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%') )");
			}
				}
				sb.append(" ) ");
			}
			if (Uteis.isAtributoPreenchido(dataInicioCompetencia) && Uteis.isAtributoPreenchido(dataFimCompetencia)) {
				sb.append(" and contapagar.datafatogerador >= '").append(Uteis.getDataJDBC(dataInicioCompetencia)).append("  00:00:00' ");
				sb.append(" and contapagar.datafatogerador <= '").append(Uteis.getDataJDBC(dataFimCompetencia)).append("  23:59:59' ");
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (rs.next()) {
				return rs.getInt("qtde");
			}
		}
		return 0;
	}

	/**
	 * Método responsavel por gerar uma conta a pagar para restituir o aluno com relação a uma parcela já paga.
	 */
	public ContaPagarVO criarContaPagarRestituirValorJaPagoPorAlunoConvenio(Date dataVcto, Double valorRestituir, String parcelaCorrespondente, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, OrigemContaPagar origemContaPagar, String descricaoPagamento, ConvenioVO convenio, CategoriaDespesaVO categoriaDespesaPadraoRestituicaoAluno, UsuarioVO usuario) throws Exception {
		ContaPagarVO contaPagar = new ContaPagarVO();
		contaPagar.setTipoOrigem(origemContaPagar.getValor());
		contaPagar.setCodOrigem(matriculaPeriodo.getCodigo().toString());
		contaPagar.setData(dataVcto);
		contaPagar.setDataVencimento(dataVcto);
		contaPagar.setDataFatoGerador(dataVcto);
		if (origemContaPagar.equals(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO)) {
			contaPagar.setConvenio(convenio);
		}
		contaPagar.setValor(valorRestituir);
		contaPagar.setTipoSacado("AL");
		contaPagar.setPessoa(matricula.getAluno());
		contaPagar.setSituacao("AP");
		contaPagar.setJuro(0.0);
		contaPagar.setMulta(0.0);
		contaPagar.setNrDocumento("");
		contaPagar.setDescricao(descricaoPagamento);
		contaPagar.setParcela(parcelaCorrespondente);
		contaPagar.setMatricula(matriculaPeriodo.getMatricula());
		contaPagar.setUnidadeEnsino(matricula.getUnidadeEnsino());
		contaPagar.setResponsavel(usuario);

		CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
		if (Uteis.isAtributoPreenchido(convenio) && origemContaPagar.equals(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO) && Uteis.isAtributoPreenchido(convenio.getCategoriaDespesaRestituicaoConvenio())) {
			cro.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(convenio.getCategoriaDespesaRestituicaoConvenio().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		} else {
			Uteis.checkState(!Uteis.isAtributoPreenchido(categoriaDespesaPadraoRestituicaoAluno), "Não foi localizado a Categoria Despesa Padrão Restituição Aluno na configuração financeira.");
			cro.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(categoriaDespesaPadraoRestituicaoAluno.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		cro.setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		cro.setQuantidade(1.0);
		cro.setPorcentagem(100.00);
		if (cro.getCategoriaDespesaVO().isInformaNivelAcademicoTurma() || cro.getCategoriaDespesaVO().isNivelCategoriaDespesaDepartamento()) {
			cro.setTurmaVO(matriculaPeriodo.getTurma());
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.TURMA);
		} else if (cro.getCategoriaDespesaVO().isInformaNivelAcademicoCursoTurno()) {
			cro.setTurnoVO(matricula.getTurno());
			cro.setCursoVO(matricula.getCurso());
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.CURSO_TURNO);
		} else if (cro.getCategoriaDespesaVO().isInformaNivelAcademicoCurso()) {
			cro.setCursoVO(matricula.getCurso());
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.CURSO);
		} else if (cro.getCategoriaDespesaVO().isNivelCategoriaDespesaUnidadeEnsino()) {			
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		}
		
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(cro, usuario);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(contaPagar.getListaCentroResultadoOrigemVOs(), cro, contaPagar.getPrevisaoValorPago(), true, usuario);
		return contaPagar;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(Double valorNovaParcela, String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(valorNovaParcela, codigoOrigem, tipoOrigem, situacaoFinanceira, controlarAcesso, Boolean.TRUE, usuario);
	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(Double valorNovaParcela, String codigoOrigem, String tipoOrigem, String situacaoFinanceira, boolean controlarAcesso, Boolean verificarBloqueioCompetenciaFechada, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuilder whereStr = new StringBuilder();
		whereStr.append(" WHERE contapagar.codorigem = '" + codigoOrigem + "' and contapagar.tipoorigem = '" + tipoOrigem + "' ");
		if (situacaoFinanceira != null && !situacaoFinanceira.trim().isEmpty()) {
			whereStr.append(" and contapagar.situacao =  '").append(situacaoFinanceira).append("' ");
		}
		
		if (verificarBloqueioCompetenciaFechada) {
			List<ContaPagarVO> contasVerificar = consultaRapidaContaPagarClausulaWhereParametro(whereStr.toString(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			for (ContaPagarVO obj : contasVerificar) {
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
			}
		}
		
		StringBuilder sql = new StringBuilder("update contapagar set valor = " + valorNovaParcela + whereStr.toString());
		getConexao().getJdbcTemplate().update(sql.toString());
		StringBuilder sqlCentroResultado = new StringBuilder(" update centroresultadoorigem set valor = "); 
		sqlCentroResultado.append(" ((").append(valorNovaParcela).append("*porcentagem)/100.00) from contapagar where centroresultadoorigem.tipocentroresultadoorigem = 'CONTA_PAGAR' ");
		sqlCentroResultado.append(" and centroresultadoorigem.codorigem = contapagar.codigo::varchar ");
		sqlCentroResultado.append(" and contapagar.codorigem = '").append(codigoOrigem).append("' and contapagar.tipoorigem = '").append(tipoOrigem).append("'" );
		if (situacaoFinanceira != null && !situacaoFinanceira.trim().isEmpty()) {
			sqlCentroResultado.append(" and contapagar.situacao =  '").append(situacaoFinanceira).append("' ");
		}
		getConexao().getJdbcTemplate().update(sqlCentroResultado.toString());
	}

	public List consultarPorNumeroCheque(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario, Date dataInicio, Date dataFim, Integer unidadeEnsino, Boolean filtarTodoPeriodo, Integer limite, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(" SELECT contapagar.origemrenegociacaopagar, contapagar.centrodespesa, contapagar.fornecedor, contapagar.parcela, ");
			sql.append(" contapagar.nrdocumento, contapagar.multa, contapagar.juro, contapagar.valor, contapagar.datavencimento, contapagar.situacao, contapagar.tipoorigem,");
			sql.append(" contapagar.codorigem, contapagar.data, contapagar.codigobarra, contapagar.codigo, contapagar.contacorrente, contapagar.valorpago,");
			sql.append(" contapagar.unidadeensino, contapagar.desconto, contapagar.tiposacado, contapagar.banco, contapagar.turma, contapagar.departamento,");
			sql.append(" contapagar.funcionario, contapagar.funcionariocentrocusto, contapagar.descricao, contapagar.curso, contapagar.turno,");
			sql.append(" contapagar.responsavel, contapagar.datafatogerador, contapagar.pessoa, contapagar.grupocontapagar, contapagar.matricula,");
			sql.append(" contapagar.parceiro, contapagar.responsavelfinanceiro, contapagar.updated,contapagar.convenio,contapagar.operadoraCartao ");
			sql.append(" FROM contapagar");
			sql.append(" INNER JOIN  contapagarnegociacaopagamento               ON contapagar.codigo = contapagarnegociacaopagamento.contapagar");
			sql.append(" INNER JOIN  negociacaopagamento                         ON contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
			sql.append(" INNER JOIN  formapagamentonegociacaopagamento           ON formapagamentonegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
			sql.append(" INNER JOIN  cheque ON formapagamentonegociacaopagamento.cheque = cheque.codigo");
			sql.append(" INNER JOIN contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo  ");
			sql.append(" and formapagamentonegociacaopagamento.codigo = contapagarpagamento.formapagamentonegociacaopagamento ");
			sql.append(" WHERE cheque.pagamento is not null AND cheque.pagamento > 0 AND contapagar.situacao = 'PA' AND cheque.numero LIKE ('");
			sql.append(valorConsulta);
			sql.append("%') ");
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sql.append(" AND contapagar.unidadeEnsino = ");
				sql.append(unidadeEnsino);
			}
			if (!filtarTodoPeriodo) {
				sql.append(" AND cheque.dataemissao::date BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("'");
				sql.append(" AND '").append(Uteis.getDataJDBC(dataFim)).append("'");

			}
			if (limite != null) {
				sql.append(" LIMIT ").append(limite);
				if (offset != null) {
					sql.append(" OFFSET ").append(offset);
				}
			}

		} catch (Exception e) {
			throw e;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public Integer consultarPorNumeroChequeTotalRegistros(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, Date dataInicio, Date dataFim, Integer unidadeEnsino, Boolean filtarTodoPeriodo) throws Exception {

		StringBuilder sql = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		try {
			sql.append(" SELECT count(contapagar.codigo) ");
			sql.append(" FROM contapagar");
			sql.append(" INNER JOIN  contapagarnegociacaopagamento               ON contapagar.codigo = contapagarnegociacaopagamento.contapagar");
			sql.append(" INNER JOIN  negociacaopagamento                         ON contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
			sql.append(" INNER JOIN  formapagamentonegociacaopagamento           ON formapagamentonegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
			sql.append(" INNER JOIN  cheque ON formapagamentonegociacaopagamento.cheque = cheque.codigo");
			sql.append(" INNER JOIN contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo  ");
			sql.append(" and formapagamentonegociacaopagamento.codigo = contapagarpagamento.formapagamentonegociacaopagamento ");
			sql.append(" WHERE cheque.pagamento IS NOT NULL AND cheque.pagamento > 0 AND  contapagar.situacao = 'PA' AND cheque.numero LIKE ('");
			sql.append(valorConsulta);
			sql.append("%') ");
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sql.append(" AND contapagar.unidadeEnsino = ");
				sql.append(unidadeEnsino);
			}
			if (!filtarTodoPeriodo) {
				sql.append(" AND cheque.dataemissao::date BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("'");
				sql.append(" AND '").append(Uteis.getDataJDBC(dataFim)).append("'");

			}

		} catch (Exception e) {
			throw e;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}

	public List<ContaPagarVO> consultarPorCodigoCheque(Integer cheque, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from (");
		sb.append(" select distinct ");
		sb.append(" case ");
		sb.append(" when contapagar.tiposacado = 'FO' then fornecedor.nome ");
		sb.append(" when contapagar.tiposacado = 'PA' then parceiro.nome ");
		sb.append(" when contapagar.tiposacado = 'FU' then pessoafuncionario.nome ");
		sb.append(" when contapagar.tiposacado = 'RF' then responsavelFinanceiro.nome ");
		sb.append(" when contapagar.tiposacado = 'AL' then pessoa.nome ");
		sb.append(" when contapagar.tiposacado = 'BA' then banco.nome ");
		sb.append(" when contapagar.tiposacado = 'OC' then operadoracartao.nome ");
		sb.append(" end AS favorecido, ");
		sb.append(" contapagar.codigo, contapagar.valor, contapagar.data, contapagar.datavencimento, contapagar.parcela, contapagar.situacao, contapagar.descricao, contapagar.tiposacado ");
		sb.append(" from cheque ");
		sb.append(" inner join formapagamentonegociacaopagamento on formapagamentonegociacaopagamento.cheque = cheque.codigo ");
		sb.append(" inner join negociacaopagamento on negociacaopagamento.codigo = formapagamentonegociacaopagamento.negociacaocontapagar ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join contapagar on contapagar.codigo = contapagarnegociacaopagamento.contapagar ");
		sb.append(" inner join contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo ");
		sb.append(" and formapagamentonegociacaopagamento.codigo = contapagarpagamento.formapagamentonegociacaopagamento ");
		sb.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sb.append(" left join funcionario on funcionario.codigo = contapagar.funcionario ");
		sb.append(" left join parceiro on parceiro.codigo = contapagar.parceiro ");
		sb.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sb.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
		sb.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
		sb.append(" left join banco on banco.codigo = contapagar.banco ");
		sb.append(" LEFT JOIN operadoracartao ON operadoracartao.codigo = contapagar.operadoracartao ");
		sb.append(" where cheque.codigo = ").append(cheque);
		sb.append(") as t ");
		sb.append(" order by favorecido, datavencimento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ContaPagarVO> listaContaPagarVOs = new ArrayList<ContaPagarVO>(0);
		;
		while (tabelaResultado.next()) {
			ContaPagarVO obj = new ContaPagarVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setValor(tabelaResultado.getDouble("valor"));
			obj.setData(tabelaResultado.getDate("data"));
			obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
			obj.setParcela(tabelaResultado.getString("parcela"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setTipoSacado(tabelaResultado.getString("tipoSacado"));
			if (obj.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
				obj.getFornecedor().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
				obj.getParceiro().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
				obj.getFuncionario().getPessoa().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
				obj.getResponsavelFinanceiro().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
				obj.getPessoa().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
				obj.getBanco().setNome(tabelaResultado.getString("favorecido"));
			} else if (obj.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
				obj.getOperadoraCartao().setNome(tabelaResultado.getString("favorecido"));
			}
			listaContaPagarVOs.add(obj);

		}
		if (!listaContaPagarVOs.isEmpty()) {
			return listaContaPagarVOs;
		}
		listaContaPagarVOs = null;
		return listaContaPagarVOs;
	}

	public List<ContaPagarVO> consultaRapidaContaPagarClausulaWhereParametro(String clausulaWhere, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("Select * from contaPagar ");
		sql.append(clausulaWhere);
		sql.append("order by codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}
	

	@Override
	public ContaPagarVO consultaRapidaContaPagarRestituicaoValorPorMatriculaPeriodo(String matricula, Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("Select * from contaPagar ");
		sql.append("WHERE (matricula='").append(matricula).append("') ");
		sql.append(" AND (tipoorigem='").append(OrigemContaPagar.RESTITUICAO_VALOR_MATRICULA_PERIODO.getValor()).append("') ");
		sql.append(" AND (codorigem='").append(matriculaPeriodo).append("') ");
		sql.append("order by dataVencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!rs.next()) {
			return null;
		}
		return montarDados(rs, nivelMontarDados, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ContaPagarControleRemessaContaPagarVO> consultaRapidaContasArquivoRemessaEntreDatas(ControleRemessaContaPagarVO controleRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<FormaPagamentoVO> formaPagamentoVOs, Boolean outroBanco, Boolean semBanco, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT cpcrcp.situacaoControleRemessaContaReceber, contapagar.codigo as \"contapagar.codigo\", contapagar.situacao as \"contapagar.situacao\", contapagar.datavencimento as \"contapagar.datavencimento\", contapagar.datafatogerador as \"contapagar.datafatogerador\",  ");
		sqlStr.append(" contapagar.nrdocumento as \"contapagar.nrdocumento\", contapagar.nossonumero as \"contapagar.nossonumero\",  contapagar.fornecedor as \"contapagar.fornecedor\", contapagar.parceiro as \"contapagar.parceiro\", contapagar.funcionario as \"contapagar.funcionario\", contapagar.banco as \"contapagar.banco\", ");
		sqlStr.append(" contapagar.unidadeensino as \"contapagar.unidadeensino\", contapagar.responsavelfinanceiro as \"contapagar.responsavelfinanceiro\", contapagar.valor as \"contapagar.valor\", ");
		sqlStr.append(" contapagar.tiposacado as \"contapagar.tiposacado\", contapagar.data as \"contapagar.data\", contapagar.descricao as \"contapagar.descricao\", contapagar.codorigem as \"contapagar.codorigem\", contapagar.tipoorigem as \"contapagar.tipoorigem\",  ");
		sqlStr.append(" contapagar.responsavel as \"contapagar.responsavel\", contapagar.juro as \"contapagar.juro\", contapagar.valorpago as \"contapagar.valorpago\", contapagar.multa as \"contapagar.multa\", contapagar.codigobarra as \"contapagar.codigobarra\", ");
		sqlStr.append(" contapagar.parcela as \"contapagar.parcela\", contapagar.origemrenegociacaopagar as \"contapagar.origemrenegociacaopagar\", contapagar.grupocontapagar as \"contapagar.grupocontapagar\", contapagar.pessoa as \"contapagar.pessoa\", contapagar.matricula as \"contapagar.matricula\", contapagar.desconto as \"contapagar.desconto\", ");
		sqlStr.append(" contapagar.contacorrente as \"contapagar.contacorrente\", contapagar.operadoracartao as \"contapagar.operadoracartao\", contapagar.convenio as \"contapagar.convenio\", ");
		sqlStr.append(" contapagar.bancoRemessaPagar as \"contapagar.bancoRemessaPagar\", contapagar.tipoServicoContaPagar as \"contapagar.tipoServicoContaPagar\", contapagar.tipoLancamentoContaPagar as \"contapagar.tipoLancamentoContaPagar\", ");
		sqlStr.append(" contapagar.finalidadeDocEnum as \"contapagar.finalidadeDocEnum\", contapagar.finalidadeTedEnum as \"contapagar.finalidadeTedEnum\", contapagar.modalidadeTransferenciaBancariaEnum as \"contapagar.modalidadeTransferenciaBancariaEnum\", contapagar.tipoContaEnum as \"contapagar.tipoContaEnum\", ");
		sqlStr.append(" contapagar.bancoRecebimento as \"contapagar.bancoRecebimento\", contapagar.numeroAgenciaRecebimento as \"contapagar.numeroAgenciaRecebimento\", contapagar.digitoAgenciaRecebimento as \"contapagar.digitoAgenciaRecebimento\", ");
		sqlStr.append(" contapagar.contaCorrenteRecebimento as \"contapagar.contaCorrenteRecebimento\", contapagar.digitoCorrenteRecebimento as \"contapagar.digitoCorrenteRecebimento\", ");
		sqlStr.append(" contapagar.codigoReceitaTributo as \"contapagar.codigoReceitaTributo\", contapagar.identificacaoContribuinte as \"contapagar.identificacaoContribuinte\", ");
		sqlStr.append(" contapagar.tipoIdentificacaoContribuinte as \"contapagar.tipoIdentificacaoContribuinte\", contapagar.numeroReferencia as \"contapagar.numeroReferencia\", ");
		sqlStr.append(" contapagar.valorReceitaBrutaAcumulada as \"contapagar.valorReceitaBrutaAcumulada\", contapagar.percentualReceitaBrutaAcumulada as \"contapagar.percentualReceitaBrutaAcumulada\",   ");
		sqlStr.append(" contapagar.linhadigitavel1 as \"contapagar.linhadigitavel1\", contapagar.linhadigitavel2 as \"contapagar.linhadigitavel2\", contapagar.linhadigitavel3 as \"contapagar.linhadigitavel3\",");
		sqlStr.append(" contapagar.linhadigitavel4 as \"contapagar.linhadigitavel4\", contapagar.linhadigitavel5 as \"contapagar.linhadigitavel5\", contapagar.linhadigitavel6 as \"contapagar.linhadigitavel6\",");
		sqlStr.append(" contapagar.linhadigitavel7 as \"contapagar.linhadigitavel7\", contapagar.linhadigitavel8 as \"contapagar.linhadigitavel8\", ");
		sqlStr.append(" contapagar.formapagamento as \"contapagar.formapagamento\", ");
		sqlStr.append(" contapagar.chaveEnderecamentoPix as \"contapagar.chaveEnderecamentoPix\" , contapagar.tipoIdentificacaoChavePixEnum as \"contapagar.tipoIdentificacaoChavePixEnum\" ,  contapagar.finalidadePixEnum as \"contapagar.finalidadePixEnum\" ");
		sqlStr.append(" from contapagar ");
		sqlStr.append(" LEFT JOIN fornecedor ON fornecedor.codigo = contapagar.fornecedor  ");
		sqlStr.append(" LEFT JOIN parceiro on parceiro.codigo = contapagar.parceiro ");
		sqlStr.append(" LEFT JOIN funcionario ON funcionario.codigo = contapagar.funcionario  ");
		sqlStr.append(" LEFT JOIN pessoa AS pessoafuncionario ON pessoafuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" left join contapagarcontroleremessacontapagar cpcrcp on cpcrcp.contapagar = contapagar.codigo ");
		sqlStr.append(" WHERE 1=1 ");
		if (Uteis.isAtributoPreenchido(controleRemessaContaPagarVO.getBancoVO().getCodigo()) && !outroBanco && !semBanco) {
			sqlStr.append(" and contaPagar.bancoRemessaPagar = ").append(controleRemessaContaPagarVO.getBancoVO().getCodigo());
		}
		if (outroBanco) {
			sqlStr.append(" and contaPagar.bancoRemessaPagar <> ").append(controleRemessaContaPagarVO.getBancoVO().getCodigo());
		}
		if (semBanco) {
			sqlStr.append(" and (contaPagar.bancoRemessaPagar is null or contaPagar.bancoRemessaPagar = 0 ");
			sqlStr.append(" or contaPagar.formaPagamento is null or contaPagar.formaPagamento = 0) ");
		}
//		if (Uteis.isAtributoPreenchido(controleRemessaContaPagarVO.getContaCorrenteVO().getCodigo())) {
//			sqlStr.append(" and contaPagar.contaCorrente = ").append(controleRemessaContaPagarVO.getContaCorrenteVO().getCodigo());
//		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				sqlStr.append(" and contaPagar.unidadeensino in (");
			
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(formaPagamentoVOs) && !semBanco) {
			sqlStr.append(" and contaPagar.formaPagamento in (");
			
			for (FormaPagamentoVO ue : formaPagamentoVOs) {
				if (ue.getFiltrarFormaPagamento()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" AND contapagar.situacao = 'AP' ");
		sqlStr.append(" and (cpcrcp.codigo IS NULL or (cpcrcp.codigo is not null and cpcrcp.situacaoControleRemessaContaReceber in ('REMETIDA_TITULO_CANCELADO', 'ESTORNADO'))) ");
		sqlStr.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(controleRemessaContaPagarVO.getDataInicio())).append("' ");
		sqlStr.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(controleRemessaContaPagarVO.getDataFim())).append("' ");
		sqlStr.append(" and contapagar.tiposacado not in(");
		sqlStr.append("'").append(TipoSacado.BANCO.getValor()).append("',");
		sqlStr.append("'").append(TipoSacado.OPERADORA_CARTAO.getValor()).append("')");		
		sqlStr.append(adicionarFiltroTipoOrigemContaPagar(filtroRelatorioFinanceiro, "contapagar"));
		sqlStr.append(" and (fornecedor.permiteenviarremessa = true  or parceiro.permiteenviarremessa = true   or pessoafuncionario.permiteenviarremessa = true or contapagar.tiposacado in ('AL','RF')  ) ");
		sqlStr.append(" order by contapagar.datavencimento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosArquivoRemessa(tabelaResultado, controleRemessaContaPagarVO, outroBanco, semBanco, configuracaoFinanceiro, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ContaPagarControleRemessaContaPagarVO> montarDadosArquivoRemessa(SqlRowSet dadosSQL, ControleRemessaContaPagarVO controleRemessaContaPagarVO, Boolean outroBanco, Boolean semBanco, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List vetResultado = new ArrayList(0);
		while (dadosSQL.next()) {
			ContaPagarControleRemessaContaPagarVO obj = new ContaPagarControleRemessaContaPagarVO();
			obj.getContaPagar().setCodigo(dadosSQL.getInt("contapagar.codigo"));
			obj.getContaPagar().setDescricao(dadosSQL.getString("contapagar.descricao"));
			obj.getContaPagar().setData(dadosSQL.getDate("contapagar.data"));
			obj.getContaPagar().setDataVencimento(dadosSQL.getDate("contapagar.dataVencimento"));
			obj.getContaPagar().setDataFatoGerador(dadosSQL.getDate("contapagar.dataFatoGerador"));
			obj.getContaPagar().setSituacao(dadosSQL.getString("contapagar.situacao"));
			obj.getContaPagar().setNrDocumento(dadosSQL.getString("contapagar.nrDocumento"));
			obj.getContaPagar().setNossoNumero(dadosSQL.getLong("contapagar.nossoNumero"));
			obj.getContaPagar().setValor(dadosSQL.getDouble("contapagar.valor"));
			obj.getContaPagar().setTipoSacado(dadosSQL.getString("contapagar.tipoSacado"));
			obj.getContaPagar().getFornecedor().setCodigo((dadosSQL.getInt("contapagar.fornecedor")));
			obj.getContaPagar().getParceiro().setCodigo((dadosSQL.getInt("contapagar.parceiro")));
			obj.getContaPagar().getFuncionario().setCodigo((dadosSQL.getInt("contapagar.funcionario")));
			obj.getContaPagar().getBanco().setCodigo((dadosSQL.getInt("contapagar.banco")));
			obj.getContaPagar().getUnidadeEnsino().setCodigo((dadosSQL.getInt("contapagar.unidadeEnsino")));
			obj.getContaPagar().getResponsavelFinanceiro().setCodigo((dadosSQL.getInt("contapagar.responsavelFinanceiro")));
			obj.getContaPagar().getPessoa().setCodigo((dadosSQL.getInt("contapagar.pessoa")));
			obj.getContaPagar().getResponsavel().setCodigo((dadosSQL.getInt("contapagar.responsavel")));
			obj.getContaPagar().getBancoRemessaPagar().setCodigo(dadosSQL.getInt("contapagar.bancoremessapagar"));
			obj.getContaPagar().getBancoRecebimento().setCodigo(dadosSQL.getInt("contapagar.bancorecebimento"));
			obj.getContaPagar().getFormaPagamentoVO().setCodigo(dadosSQL.getInt("contapagar.formapagamento"));
			obj.getContaPagar().getContaCorrente().setCodigo(dadosSQL.getInt("contapagar.contaCorrente"));
			obj.getContaPagar().getOperadoraCartao().setCodigo(dadosSQL.getInt("contapagar.operadoraCartao"));
			obj.getContaPagar().setCodOrigem(dadosSQL.getString("contapagar.codOrigem"));
			obj.getContaPagar().setTipoOrigem(dadosSQL.getString("contapagar.tipoOrigem"));
			obj.getContaPagar().setJuro((dadosSQL.getDouble("contapagar.juro")));
			obj.getContaPagar().setMulta((dadosSQL.getDouble("contapagar.multa")));
			obj.getContaPagar().setCodigoBarra(dadosSQL.getString("contapagar.codigoBarra"));
			obj.getContaPagar().setLinhaDigitavel1(dadosSQL.getString("contapagar.linhaDigitavel1"));
			obj.getContaPagar().setLinhaDigitavel2(dadosSQL.getString("contapagar.linhaDigitavel2"));
			obj.getContaPagar().setLinhaDigitavel3(dadosSQL.getString("contapagar.linhaDigitavel3"));
			obj.getContaPagar().setLinhaDigitavel4(dadosSQL.getString("contapagar.linhaDigitavel4"));
			obj.getContaPagar().setLinhaDigitavel5(dadosSQL.getString("contapagar.linhaDigitavel5"));
			obj.getContaPagar().setLinhaDigitavel6(dadosSQL.getString("contapagar.linhaDigitavel6"));
			obj.getContaPagar().setLinhaDigitavel7(dadosSQL.getString("contapagar.linhaDigitavel7"));
			obj.getContaPagar().setLinhaDigitavel8(dadosSQL.getString("contapagar.linhaDigitavel8"));
			obj.getContaPagar().setDesconto((dadosSQL.getDouble("contapagar.desconto")));
			obj.getContaPagar().setNumeroAgenciaRecebimento(dadosSQL.getString("contapagar.numeroAgenciaRecebimento"));
			obj.getContaPagar().setContaCorrenteRecebimento(dadosSQL.getString("contapagar.contaCorrenteRecebimento"));
			obj.getContaPagar().setDigitoAgenciaRecebimento(dadosSQL.getString("contapagar.digitoAgenciaRecebimento"));
			obj.getContaPagar().setDigitoCorrenteRecebimento(dadosSQL.getString("contapagar.digitoCorrenteRecebimento"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoServicoContaPagar"))) {
				obj.getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.valueOf(dadosSQL.getString("contapagar.tipoServicoContaPagar")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoLancamentoContaPagar"))) {
				obj.getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.valueOf(dadosSQL.getString("contapagar.tipoLancamentoContaPagar")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.modalidadeTransferenciaBancariaEnum"))) {
				obj.getContaPagar().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.valueOf(dadosSQL.getString("contapagar.modalidadeTransferenciaBancariaEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoContaEnum"))) {
				obj.getContaPagar().setTipoContaEnum(TipoContaEnum.valueOf(dadosSQL.getString("contapagar.tipoContaEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.finalidadeDocEnum"))) {
				obj.getContaPagar().setFinalidadeDocEnum(FinalidadeDocEnum.valueOf(dadosSQL.getString("contapagar.finalidadeDocEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.finalidadeTedEnum"))) {
				obj.getContaPagar().setFinalidadeTedEnum(FinalidadeTedEnum.valueOf(dadosSQL.getString("contapagar.finalidadeTedEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.finalidadePixEnum"))) {
				obj.getContaPagar().setFinalidadePixEnum(FinalidadePixEnum.valueOf(dadosSQL.getString("contapagar.finalidadePixEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoIdentificacaoContribuinte"))) {
				obj.getContaPagar().setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.valueOf(dadosSQL.getString("contapagar.tipoIdentificacaoContribuinte")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoIdentificacaoChavePixEnum"))) {
				obj.getContaPagar().setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("contapagar.tipoIdentificacaoChavePixEnum")));
			}
			obj.getContaPagar().setCodigoReceitaTributo(dadosSQL.getString("contapagar.codigoReceitaTributo"));
			obj.getContaPagar().setIdentificacaoContribuinte(dadosSQL.getString("contapagar.identificacaoContribuinte"));
			obj.getContaPagar().setNumeroReferencia(dadosSQL.getString("contapagar.numeroReferencia"));
			obj.getContaPagar().setValorReceitaBrutaAcumulada(dadosSQL.getDouble("contapagar.valorReceitaBrutaAcumulada"));
			obj.getContaPagar().setPercentualReceitaBrutaAcumulada(dadosSQL.getDouble("contapagar.percentualReceitaBrutaAcumulada"));
			obj.getContaPagar().setChaveEnderecamentoPix(dadosSQL.getString("contapagar.chaveEnderecamentoPix"));
			obj.setDataVencimento(dadosSQL.getDate("contapagar.dataVencimento"));
			obj.setDataFatoGerador(dadosSQL.getDate("contapagar.dataFatoGerador"));
			obj.setNrDocumento(dadosSQL.getString("contapagar.nrDocumento"));
			obj.setNossoNumero(dadosSQL.getLong("contapagar.nossoNumero"));
			obj.setValor((dadosSQL.getDouble("contapagar.valor")));
			obj.setTipoSacado(obj.getContaPagar().getTipoSacado());
			obj.setJuro((dadosSQL.getDouble("contapagar.juro")));
			obj.setMulta((dadosSQL.getDouble("contapagar.multa")));
			obj.setCodigoBarra(dadosSQL.getString("contapagar.codigoBarra"));
			obj.setLinhaDigitavel1(dadosSQL.getString("contapagar.linhaDigitavel1"));
			obj.setLinhaDigitavel2(dadosSQL.getString("contapagar.linhaDigitavel2"));
			obj.setLinhaDigitavel3(dadosSQL.getString("contapagar.linhaDigitavel3"));
			obj.setLinhaDigitavel4(dadosSQL.getString("contapagar.linhaDigitavel4"));
			obj.setLinhaDigitavel5(dadosSQL.getString("contapagar.linhaDigitavel5"));
			obj.setLinhaDigitavel6(dadosSQL.getString("contapagar.linhaDigitavel6"));
			obj.setLinhaDigitavel7(dadosSQL.getString("contapagar.linhaDigitavel7"));
			obj.setLinhaDigitavel8(dadosSQL.getString("contapagar.linhaDigitavel8"));
			obj.setDesconto((dadosSQL.getDouble("contapagar.desconto")));
			obj.setNumeroAgenciaRecebimento(dadosSQL.getString("contapagar.numeroAgenciaRecebimento"));
			obj.setContaCorrenteRecebimento(dadosSQL.getString("contapagar.contaCorrenteRecebimento"));
			obj.setDigitoAgenciaRecebimento(dadosSQL.getString("contapagar.digitoAgenciaRecebimento"));
			obj.setDigitoCorrenteRecebimento(dadosSQL.getString("contapagar.digitoCorrenteRecebimento"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("situacaoControleRemessaContaReceber"))) {
				obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.getEnum(dadosSQL.getString("situacaoControleRemessaContaReceber")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoServicoContaPagar"))) {
				obj.setTipoServicoContaPagar(TipoServicoContaPagarEnum.valueOf(dadosSQL.getString("contapagar.tipoServicoContaPagar")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoLancamentoContaPagar"))) {
				obj.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.valueOf(dadosSQL.getString("contapagar.tipoLancamentoContaPagar")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.modalidadeTransferenciaBancariaEnum"))) {
				obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.valueOf(dadosSQL.getString("contapagar.modalidadeTransferenciaBancariaEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoContaEnum"))) {
				obj.setTipoContaEnum(TipoContaEnum.valueOf(dadosSQL.getString("contapagar.tipoContaEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.finalidadeDocEnum"))) {
				obj.setFinalidadeDocEnum(FinalidadeDocEnum.valueOf(dadosSQL.getString("contapagar.finalidadeDocEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.finalidadeTedEnum"))) {
				obj.setFinalidadeTedEnum(FinalidadeTedEnum.valueOf(dadosSQL.getString("contapagar.finalidadeTedEnum")));
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contapagar.tipoIdentificacaoContribuinte"))) {
				obj.setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.valueOf(dadosSQL.getString("contapagar.tipoIdentificacaoContribuinte")));
			}
			obj.setCodigoReceitaTributo(dadosSQL.getString("contapagar.codigoReceitaTributo"));
			obj.setIdentificacaoContribuinte(dadosSQL.getString("contapagar.identificacaoContribuinte"));
			obj.setNumeroReferencia(dadosSQL.getString("contapagar.numeroReferencia"));
			obj.setValorReceitaBrutaAcumulada(dadosSQL.getDouble("contapagar.valorReceitaBrutaAcumulada"));
			obj.setPercentualReceitaBrutaAcumulada(dadosSQL.getDouble("contapagar.percentualReceitaBrutaAcumulada"));
			montarDadosOperadoraCartao(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosFornecedor(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosResponsavelFinanceiro(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosParceiro(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosPessoa(obj.getContaPagar(),Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosFuncionario(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			montarDadosBanco(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			montarDadosFormaPagamento(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			montarDadosBancoRemessaPagar(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			montarDadosBancoRecebimento(obj.getContaPagar(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			
			obj.setNomeFavorecido(obj.getContaPagar().getFavorecido());
			if (obj.getContaPagar().getNrDocumento().equals(11)) {
				obj.setCnpjOuCpfFavorecido(obj.getContaPagar().getCnpjOuCpfFavorecido());
			}
			obj.setCnpjOuCpfFavorecido(obj.getContaPagar().getCnpjOuCpfFavorecido());
			obj.setInscricaoEstadualFavorecido(obj.getContaPagar().getInscricaoEstadualFavorecido());
			obj.setInscricaoMunicipalFavorecido(obj.getContaPagar().getInscricaoMunicipalFavorecido());
			obj.setTipoInscricaoFavorecido(obj.getContaPagar().getTipoInscricaoFavorecido());
			obj.setLogradouroFavorecido(obj.getContaPagar().getLogadouroFavorecido());
			obj.setBairroFavorecido(obj.getContaPagar().getBairroFavorecido());
			obj.setCepFavorecido(obj.getContaPagar().getCepFavorecido());
			obj.setCidadeFavorecido(obj.getContaPagar().getCidadeFavorecido());
			obj.setEstadoFavorecido(obj.getContaPagar().getEstadoFavorecido());
			obj.setComplementoFavorecido(obj.getContaPagar().getComplementoFavorecido());
			if (Uteis.isAtributoPreenchido(obj.getContaPagar().getNumeroEnderecoFavorecido()) && obj.getContaPagar().getNumeroEnderecoFavorecido().length() > 10) {
				obj.setNumeroEnderecoFavorecido(obj.getContaPagar().getNumeroEnderecoFavorecido().substring(0,10));
			}else {
				obj.setNumeroEnderecoFavorecido(obj.getContaPagar().getNumeroEnderecoFavorecido());
			}
			obj.setBancoRemessaPagar(obj.getContaPagar().getBancoRemessaPagar());
			obj.setBancoRecebimento(obj.getContaPagar().getBancoRecebimento());
			if (!semBanco && !outroBanco) {
				verificarPossiveisErrosRemessa(obj, controleRemessaContaPagarVO);
			}
			if(Uteis.isAtributoPreenchido(obj.getContaPagar().getTipoLancamentoContaPagar())){
				obj.getContaPagar().validarSubistituirTipoLancamentoDepreciado();
			    obj.setTipoLancamentoContaPagar(obj.getContaPagar().getTipoLancamentoContaPagar());
			}
			vetResultado.add(obj);
		}
		return vetResultado;

	}
	
	public void verificarPossiveisErrosRemessa(ContaPagarControleRemessaContaPagarVO obj, ControleRemessaContaPagarVO controleRemessa) {
		try {
			String msgErro = "";
			if (obj.getNomeFavorecido().replaceAll(" ", "").length() <= 0) {
				msgErro += "Nome favorecido em branco (" + obj.getNomeFavorecido() + "); ";
			}
			if (Uteis.removerMascara(obj.getCnpjOuCpfFavorecido()).equals("")) {
				msgErro += "CPF/CNPJ favorecido em branco (" + obj.getCnpjOuCpfFavorecido() + "); ";
			}
			if (!Uteis.removerMascara(obj.getCnpjOuCpfFavorecido()).equals("")) {
				if (Uteis.removerMascara(obj.getCnpjOuCpfFavorecido()).length() <= 11) {
					if (!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(obj.getCnpjOuCpfFavorecido())).replaceAll(" ", ""))) {
						msgErro += "CPF inválido (" + obj.getCnpjOuCpfFavorecido() + "); ";
					}
				} else {
					if (Uteis.removerMascara(Uteis.removerAcentos(Uteis.removeCaractersEspeciais2(obj.getCnpjOuCpfFavorecido()))).length() == 14) {
						boolean validouCNPJ = false;
						validouCNPJ = Uteis.validaCNPJ(Uteis.removerMascara(Uteis.removerAcentos(Uteis.removeCaractersEspeciais2(obj.getCnpjOuCpfFavorecido()))));
						if (!validouCNPJ) {
							msgErro += "CNPJ inválido (" + obj.getCnpjOuCpfFavorecido() + "); ";
						}
					} else {
						msgErro += "CNPJ inválido (" + obj.getCnpjOuCpfFavorecido() + "); ";
					}
				}
			}
			String cep = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(obj.getCepFavorecido()).replaceAll(" ", ""));
			if (cep.length() != 8) {
				msgErro += "CEP inválido (" + obj.getCepFavorecido() + "); ";
			}			
			String cidade = obj.getCidadeFavorecido();
			if (cidade.trim().equals("")) {
				msgErro += "Cidade inválida (" + obj.getCidadeFavorecido() + "); ";
			}			
			String estado = obj.getEstadoFavorecido();
			if (estado.trim().equals("")) {
				msgErro += "Estado inválido (" + obj.getEstadoFavorecido() + "); ";
			}						
			String endereco = obj.getLogradouroFavorecido();
			if (endereco.trim().equals("")) {
				msgErro += "Endereço inválido (" + obj.getLogradouroFavorecido() + "); ";
			}
			if (obj.getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO)) {
				if (Uteis.isAtributoPreenchido(obj.getContaPagar().getTipoLancamentoContaPagar()) && obj.getContaPagar().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()) {
					if (obj.getContaPagar().getLinhaDigitavel1().equals("") || 
							obj.getContaPagar().getLinhaDigitavel2().equals("") ||
							obj.getContaPagar().getLinhaDigitavel3().equals("") ||
							obj.getContaPagar().getLinhaDigitavel4().equals("")) {
						msgErro += "Linha Digitável inválida!";
					}
				} else {
					if (obj.getContaPagar().getLinhaDigitavel1().equals("") || 
							obj.getContaPagar().getLinhaDigitavel2().equals("") ||
							obj.getContaPagar().getLinhaDigitavel3().equals("") ||
							obj.getContaPagar().getLinhaDigitavel4().equals("") ||
							obj.getContaPagar().getLinhaDigitavel5().equals("") ||
							obj.getContaPagar().getLinhaDigitavel6().equals("") ||
							obj.getContaPagar().getLinhaDigitavel7().equals("") ||
							obj.getContaPagar().getLinhaDigitavel8().equals("")) {
						msgErro += "Linha Digitável inválida!";
					}
				}
			}
			if(!obj.getContaPagar().getFormaPagamentoVO().getCodigo().equals(0)) {
				if (obj.getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE) 
						|| obj.getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEPOSITO)) {
					/*if (obj.getContaPagar().getNumeroAgenciaRecebimento().equals("")) {
						msgErro += "Número Agência Favorecido inválido (" + obj.getContaPagar().getNumeroAgenciaRecebimento() + "); ";
					}
					if (obj.getContaPagar().getDigitoAgenciaRecebimento().equals("")) {
						msgErro += "Dígito Agência Favorecido inválido (" + obj.getContaPagar().getDigitoAgenciaRecebimento() + "); ";					
					}*/
					if(obj.getContaPagar().getTipoLancamentoContaPagar().isPixTransferencia()   &&    Uteis.isAtributoPreenchido(obj.getContaPagar().getModalidadeTransferenciaBancariaEnum()) &&  obj.getContaPagar().getModalidadeTransferenciaBancariaEnum().isPix()) {						
						if (!Uteis.isAtributoPreenchido(obj.getContaPagar().getChaveEnderecamentoPix())) {
							msgErro += "Chave Endereço Pix  Favorecido inválido (" + obj.getContaPagar().getChaveEnderecamentoPix() + "); ";
						}
					}else {
						if (obj.getContaPagar().getContaCorrenteRecebimento().equals("")) {
							msgErro += "Conta Corrente Favorecido inválido (" + obj.getContaPagar().getContaCorrenteRecebimento() + "); ";
						}
					}
				}else {
					if(obj.getContaPagar().getTipoLancamentoContaPagar().isPixTransferencia()   && 
							Uteis.isAtributoPreenchido(obj.getContaPagar().getModalidadeTransferenciaBancariaEnum()) &&  
							obj.getContaPagar().getModalidadeTransferenciaBancariaEnum().isPix() && 
							!Uteis.isAtributoPreenchido(obj.getContaPagar().getChaveEnderecamentoPix())) {
							msgErro += "Chave Endereço Pix  Favorecido inválido (" + obj.getContaPagar().getChaveEnderecamentoPix() + "); ";
						}
				}
				
			}else {
				msgErro += msgErro += "Forma de Pagamento inválido (" + obj.getContaPagar().getFormaPagamentoVO() + "); ";;
				
			}
			
			if (!msgErro.equals("")) {
				obj.setMotivoErro(msgErro);
				obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO);
			}else{				
				obj.setMotivoErro("");
				obj.setSituacaoControleRemessaContaReceber(null);
			}
		} catch (Exception e) {
			obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarContaPagarPaga(Double valorConta, Integer banco, FormaPagamentoVO formaPagamento, CategoriaDespesaVO categoriaDespesa, UnidadeEnsinoVO unidadeEnsino, ContaCorrenteVO contaCorrente, Boolean utilizaAbatimentoNoRepasseRemessaBanco, UsuarioVO usuario) throws Exception {
		try {
			ContaPagarVO obj = new ContaPagarVO();
			obj.setValor(Uteis.arrendondarForcando2CadasDecimais(valorConta));
			obj.setValorPagamento(Uteis.arrendondarForcando2CadasDecimais(valorConta));
			obj.setValorPago(Uteis.arrendondarForcando2CadasDecimais(valorConta));
			obj.setData(new Date());
			obj.setDataVencimento(new Date());
			obj.setUnidadeEnsino(unidadeEnsino);
			obj.setSituacao(SituacaoFinanceira.A_PAGAR.getValor());
			obj.getBanco().setCodigo(banco);
			obj.getContaCorrente().setCodigo(contaCorrente.getCodigo());
			obj.setCodOrigem("");
			obj.setTipoOrigem("");
			obj.setTipoSacado("BA");
			CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
			cro.setCategoriaDespesaVO(categoriaDespesa);
			cro.setUnidadeEnsinoVO(obj.getUnidadeEnsino());
			cro.setQuantidade(1.00);
			cro.setPorcentagem(100.00);
			cro.setValor(obj.getValor());	
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);	
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(cro, usuario);
			obj.getListaCentroResultadoOrigemVOs().add(cro);
			getFacadeFactory().getContaPagarFacade().incluir(obj, false, true, usuario);

			FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();

			NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
			ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
			contaPagarNegociacaoPagamento.setContaPagar(obj);
			contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
			contaPagarNegociacaoPagamento.setValorContaPagar(obj.getValor());

			formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(formaPagamento);
			formaPagamentoNegociacaoPagamentoVO.getContaCorrente().setCodigo(contaCorrente.getCodigo());
			formaPagamentoNegociacaoPagamentoVO.setValor(obj.getValor());

			formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());

			negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
			negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);
			negociacaoPagamentoVO.setData(new Date());
			negociacaoPagamentoVO.setUnidadeEnsino(unidadeEnsino);
			negociacaoPagamentoVO.setTipoSacado("BA");
			negociacaoPagamentoVO.setBanco(obj.getBanco());
			negociacaoPagamentoVO.setValorTotal(obj.getValor());
			negociacaoPagamentoVO.setValorTotalPagamento(obj.getValor());
			negociacaoPagamentoVO.setResponsavel(usuario);
			getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, false, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLancamentoContabilPadrao(ContaPagarVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_PAGAR.getValor(), usuarioVO);
		List<LancamentoContabilVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(conta.getListaLancamentoContabeisCredito());
		listaTemp.addAll(conta.getListaLancamentoContabeisDebito());
		for (LancamentoContabilVO lc : listaTemp) {
			getFacadeFactory().getLancamentoContabilFacade().validarDados(lc);
		}
		listaTemp.clear();
		conta.getListaLancamentoContabeisCredito().clear();
		conta.getListaLancamentoContabeisDebito().clear();
		List<ContaPagarPagamentoVO> lista = getFacadeFactory().getContaPagarPagamentoFacade().consultarPorContaPagarParaGeracaoLancamentoContabil(conta.getCodigo(), false, usuarioVO);
		for (ContaPagarPagamentoVO contaPagarPagamentoVO : lista) {
			getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaPagar(listaTemp, contaPagarPagamentoVO.getData(), conta, contaPagarPagamentoVO.getFormaPagamentoNegociacaoPagamentoVO(), contaPagarPagamentoVO.getValorTotalPagamento(),  true, usuarioVO);	
		}
		if (Uteis.isAtributoPreenchido(listaTemp)) {
			for (LancamentoContabilVO objExistente : listaTemp) {
				getFacadeFactory().getLancamentoContabilFacade().persistir(objExistente, false, usuarioVO);
				if (objExistente.getTipoPlanoConta().isCredito()) {
					conta.getListaLancamentoContabeisCredito().add(objExistente);
				} else {
					conta.getListaLancamentoContabeisDebito().add(objExistente);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLancamentoContabilVO(ContaPagarVO conta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_PAGAR.getValor(), usuarioVO);
		if (!conta.getTotalLancamentoContabeisCreditoTipoValorContaPagar().equals(conta.getTotalLancamentoContabeisDebitoTipoValorContaPagar())) {
			throw new Exception("Os valores dos lançamentos contábeis do tipo valor Conta Pagar não podem ser diferentes.");
		}
		if (conta.getTotalLancamentoContabeisCreditoTipoValorContaPagar() > conta.getPrevisaoValorPago()) {
			throw new Exception("O Total do Lançamento de Crédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(conta.getTotalLancamentoContabeisCreditoTipoValorContaPagar(), ",") + " do tipo valor Conta Pagar não podem ser maior que o valor da conta pagar " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(conta.getPrevisaoValorPago(), ",") + ".");
		}
		if (conta.getTotalLancamentoContabeisDebitoTipoValorContaPagar() > conta.getPrevisaoValorPago()) {
			throw new Exception("O Total do Lançamento de Dédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(conta.getTotalLancamentoContabeisDebitoTipoValorContaPagar(), ",") + " do tipo valor Conta Pagar não podem ser maior que o valor da conta pagar " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(conta.getPrevisaoValorPago(), ",") + ".");
		}
		List<LancamentoContabilVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(conta.getListaLancamentoContabeisCredito());
		listaTemp.addAll(conta.getListaLancamentoContabeisDebito());
		EnumMap<TipoOrigemLancamentoContabilEnum, String> mapaDeletar = new EnumMap(TipoOrigemLancamentoContabilEnum.class);
		mapaDeletar.put(TipoOrigemLancamentoContabilEnum.PAGAR, "'" + conta.getCodigo().toString() + "'");
		getFacadeFactory().getLancamentoContabilFacade().validarSeLancamentoContabilFoiExcluido(listaTemp, mapaDeletar, usuarioVO);
		for (LancamentoContabilVO lc : listaTemp) {
			getFacadeFactory().getLancamentoContabilFacade().persistir(lc, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void addLancamentoContabilVO(ContaPagarVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(lancamento)) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(lancamento.getPlanoContaVO()), "O campo Plano de Conta deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(lancamento.getValor()), "O campo Valor deve ser informado.");
			lancamento.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
			Date dataNegocicaoPagamento = getFacadeFactory().getNegociacaoPagamentoFacade().consultarDataPagamentoNegociacaoPagamentoPorContaPagar(conta.getCodigo(), usuario);
			ContaCorrenteVO contaCorrentePagamento = null;
			if(!Uteis.isAtributoPreenchido(lancamento.getContaCorrenteVO())){
				contaCorrentePagamento = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteUsadaNaNegociacaoPagamentoPorContaPagar(conta.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				Uteis.checkState(!Uteis.isAtributoPreenchido(contaCorrentePagamento), "Não foi encontrado uma conta corrente na Negociação pagamento para essa Conta.");	
			}else{
				contaCorrentePagamento = lancamento.getContaCorrenteVO();
			}
			getFacadeFactory().getLancamentoContabilFacade().preencherLancamentoContabilPorContaPagar(lancamento, lancamento.getPlanoContaVO(), dataNegocicaoPagamento, conta, TipoValorLancamentoContabilEnum.CONTA_PAGAR, lancamento.getValor(), contaCorrentePagamento, false, lancamento.getTipoPlanoConta(), false, usuario);
		}
		if (Uteis.isAtributoPreenchido(lancamento.getListaCentroNegocioAcademico()) && !lancamento.getTotalCentroNegocioAcademico().equals(lancamento.getValor())) {
			throw new Exception("O Total do Rateio Acadêmico " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lancamento.getTotalCentroNegocioAcademico(), ",") + " não podem ser diferente do valor do lançamento contábil " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lancamento.getValor(), ",") + ".");
		} else if (Uteis.isAtributoPreenchido(lancamento.getListaCentroNegocioAdministrativo()) && !lancamento.getTotalCentroNegocioAdministrativo().equals(lancamento.getValor())) {
			throw new Exception("O Total do Rateio Administrativo " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lancamento.getTotalCentroNegocioAdministrativo(), ",") + " não podem ser diferente do valor do lançamento contábil " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lancamento.getValor(), ",") + ".");
		}
		if (lancamento.getTipoPlanoConta().isCredito()) {
			getFacadeFactory().getLancamentoContabilFacade().preencherListaLancamentoContabilVO(conta.getListaLancamentoContabeisCredito(), lancamento);
		} else {
			getFacadeFactory().getLancamentoContabilFacade().preencherListaLancamentoContabilVO(conta.getListaLancamentoContabeisDebito(), lancamento);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removeLancamentoContabilVO(ContaPagarVO conta, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception {
		if (lancamento.getTipoPlanoConta().isCredito()) {
			getFacadeFactory().getLancamentoContabilFacade().removeLancamentoContabilVO(conta.getListaLancamentoContabeisCredito(), lancamento, usuario);
		} else {
			getFacadeFactory().getLancamentoContabilFacade().removeLancamentoContabilVO(conta.getListaLancamentoContabeisDebito(), lancamento, usuario);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarContaPagarPorCentroResultadoOrigem(List<ContaPagarVO> listaContaPagar, List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, ContaPagarVO contaPagar,  UsuarioVO usuario) {
		int index = 0;
		for (ContaPagarVO objExistente : listaContaPagar) {
			if (objExistente.equalsCampoSelecaoListaPorNotaFiscalEntrada(contaPagar)) {
				if (!contaPagar.isEdicaoManual()) {
					objExistente.setValor(objExistente.getValor() + contaPagar.getValor());	
					objExistente.setNrDocumento(contaPagar.getNrDocumento());
				}
				listaContaPagar.set(index, objExistente);
				atualizarCentroResultadoOrigemContaPagar(listaCentroResultadoOrigem, objExistente, usuario);
				return;
			}
			index++;
		}
		if(contaPagar.getNrDocumento().contains(".")) {
			contaPagar.setNrDocumento(contaPagar.getParcela().substring(0, 1) + contaPagar.getNrDocumento());
		}
		
		atualizarCentroResultadoOrigemContaPagar(listaCentroResultadoOrigem, contaPagar,  usuario);
		listaContaPagar.add(contaPagar);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void atualizarCentroResultadoOrigemContaPagar(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, ContaPagarVO contaPagar, UsuarioVO usuario)  {
		try {
			Double valorTotalCentroResultados = Uteis.arrendondarForcando2CadasDecimais(listaCentroResultadoOrigem.stream().mapToDouble(CentroResultadoOrigemVO::getValor).sum());
			Map<String, Double> mapPorcentagemCentroResultado = new HashMap<String, Double>(0);
			listaCentroResultadoOrigem.forEach(cr -> {
				mapPorcentagemCentroResultado.put(cr.toString(), Uteis.arrendondarForcandoCadasDecimais((cr.getValor() * 100.0000) / valorTotalCentroResultados, 8));
			});
			// aqui soma o percentual para ver se deu 100%, caso não tenha dado é jogado a
			// diferença no 1 centro de resultado
			Optional<BigDecimal> somaPercentualOptionalBigDecimal = mapPorcentagemCentroResultado.values().stream().map(String::valueOf).map(BigDecimal::new).reduce(BigDecimal::add);
			Double somaPercentual = Uteis.arrendondarForcandoCadasDecimais(somaPercentualOptionalBigDecimal.orElse(BigDecimal.ZERO).doubleValue(), 8);
			if (somaPercentual < 100.0000000 && Uteis.isAtributoPreenchido(listaCentroResultadoOrigem)) {
				mapPorcentagemCentroResultado.put(listaCentroResultadoOrigem.get(0).toString(), Uteis.arrendondarForcandoCadasDecimais(mapPorcentagemCentroResultado.get(listaCentroResultadoOrigem.get(0).toString()) + (100 - somaPercentual), 8));
			} else if(somaPercentual > 100.00000000  && Uteis.isAtributoPreenchido(listaCentroResultadoOrigem)) {
				mapPorcentagemCentroResultado.put(listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size()-1).toString(), Uteis.arrendondarForcandoCadasDecimais(mapPorcentagemCentroResultado.get(listaCentroResultadoOrigem.get(listaCentroResultadoOrigem.size()-1).toString()) - (somaPercentual - 100.00000000), 8));
			}
			contaPagar.getListaCentroResultadoOrigemVOs().clear();
			for (CentroResultadoOrigemVO centroResultadoOrigemVO : listaCentroResultadoOrigem) {
				CentroResultadoOrigemVO centroResultadoOrigemVO2 = (CentroResultadoOrigemVO) centroResultadoOrigemVO.clone();
				centroResultadoOrigemVO2.setCodigo(0);
				centroResultadoOrigemVO2.setNovoObj(true);
				centroResultadoOrigemVO2.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
				centroResultadoOrigemVO2.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_PAGAR);
				centroResultadoOrigemVO2.setQuantidade(1.0);
				centroResultadoOrigemVO2.setCodOrigem("");
				centroResultadoOrigemVO2.setValor(0.0);
				centroResultadoOrigemVO2.setPorcentagem(mapPorcentagemCentroResultado.get(centroResultadoOrigemVO.toString()));
				centroResultadoOrigemVO2.calcularValor(contaPagar.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento());
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(contaPagar.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO2, contaPagar.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), false, usuario);
			}
			
//			List<CentroResultadoOrigemVO> novaLista = new ArrayList<>();
//			for (CentroResultadoOrigemVO cro : listaCentroResultadoOrigem) {
//				CentroResultadoOrigemVO clone = cro.getClone();
//				clone.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
//				clone.setQuantidade(1.0);
//				clone.calcularValor(contaPagar.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento());
//				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(clone, novaLista);
//			}	
//			
//			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().atualizarListasDeCentroResultadoOrigem(contaPagar.getListaCentroResultadoOrigemVOs(), novaLista);
//			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(contaPagar.getListaCentroResultadoOrigemVOs(), contaPagar.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), usuario);
		} catch (Exception e) {
			throw new  StreamSeiException(e.getMessage());
		}
	}
	
	/**
     * INICIO MERGE EDIGAR 24/05/2018
     */
    
    /**
     * Metodo responsavel por, data uma conta a pagar, distribuir e utilizar o maximo de adiantamentos
     * dispniveis possíveis (do sacado em questao) para fazer o abatimento destes adiantamento no valor
     * a ser pago para esta conta.
     * @param contaPagar
     * @param listaAdiantamentosDisponiveisUsarAbatimento
     * @return
     */
	@Override
    public Double realizarDistribuicaoAdiantamentosDisponiveisParaAbaterContaPagar(ContaPagarVO contaPagar, Double valorResidualContaPagarParaAbatimento,  List<ContaPagarVO> listaAdiantamentosDisponiveisUsarAbatimento, boolean isConsiderarContaPagarParcialmentePaga) {
    	
    	double valorTotalAdiantamentoUtilizado = 0.0;
    	int j = 0;
    	while (j < listaAdiantamentosDisponiveisUsarAbatimento.size()) {
    		ContaPagarVO adiantamento = listaAdiantamentosDisponiveisUsarAbatimento.get(j);
    		if (adiantamento.getUtilizarAdiantamentoNegociacaoPagamento()) {
    			double valorPagoAdiantamentoDisponivelUso = adiantamento.getValorPago() - adiantamento.getValorUtilizadoAdiantamento();
    			if (valorPagoAdiantamentoDisponivelUso > 0) {
    			   if ((valorPagoAdiantamentoDisponivelUso ==  valorResidualContaPagarParaAbatimento) ||
    					   ((valorPagoAdiantamentoDisponivelUso < valorResidualContaPagarParaAbatimento) && isConsiderarContaPagarParcialmentePaga)) {
    				   // SITUACAO 1 - Neste caso podemos utilizar todo o adiantamento para descontar da valor pago da conta a pagar.          				
    				   // entao vamos descontar o valor do adiantamento do residual.
    				   valorResidualContaPagarParaAbatimento = valorResidualContaPagarParaAbatimento - valorPagoAdiantamentoDisponivelUso;
    				
    				   // vamos lancar o valor do adiantamento no descontoPorAbatimento
    				   contaPagar.setDescontoPorUsoAdiantamento(contaPagar.getDescontoPorUsoAdiantamento() + valorPagoAdiantamentoDisponivelUso);
    				
    				   // vamos registrar no adiantamento, que o todo o seu valor foi utilizado
    				   adiantamento.setValorUtilizadoAdiantamento(adiantamento.getValorUtilizadoAdiantamento() + valorPagoAdiantamentoDisponivelUso);
    				
    				   // acumulando o total dos adiantamentos utilizados
    				   valorTotalAdiantamentoUtilizado = valorTotalAdiantamentoUtilizado + valorPagoAdiantamentoDisponivelUso;
    				   
    				   // vamos gerar o rastro de uso do adiantamento na conta a pagar correspondente
    				   ContaPagarAdiantamentoVO contaPagarAdiantamentoVO = new ContaPagarAdiantamentoVO();
    				   contaPagarAdiantamentoVO.setContaPagar(contaPagar);
    				   contaPagarAdiantamentoVO.setContaPagarUtilizada(adiantamento);
    				   contaPagarAdiantamentoVO.setDataUsoAdiantamento(new Date());
    				   contaPagarAdiantamentoVO.setValorUtilizado(valorPagoAdiantamentoDisponivelUso);
    				   contaPagarAdiantamentoVO.setPercenutalContaPagarUtilizada(100.0);
    				   contaPagar.getListaContaPagarAdiantamentoVO().add(contaPagarAdiantamentoVO); 
    				    				    
    			   } else if(valorPagoAdiantamentoDisponivelUso > valorResidualContaPagarParaAbatimento) {
    				   // SITUACAO 2 - Neste caso o adiantamento tem um valor superior ao da conta que esta sendo paga.
    				   //              Sendo assim, iremos considerar somente parte do valor do adiantamento para quitar
    				   //              a conta a pagar em questao.

    				   // assim o primeiro item a definir é o valor do adiantamento que será utiliza
    				   double valorParcialAdiantamentoSeraUtilizado = valorResidualContaPagarParaAbatimento;
    				   
    				   // entao vamos descontar o valor parcial do adiantamento do residual.
    				   valorResidualContaPagarParaAbatimento = valorResidualContaPagarParaAbatimento - valorParcialAdiantamentoSeraUtilizado;

    				   // vamos lancar o valor parcial do adiantamento no descontoPorAbatimento
    				   contaPagar.setDescontoPorUsoAdiantamento(contaPagar.getDescontoPorUsoAdiantamento() + valorParcialAdiantamentoSeraUtilizado);
    				   
    				   // vamos registrar no adiantamento, o valor parcial do mesmo que foi utilizado
    				   adiantamento.setValorUtilizadoAdiantamento(adiantamento.getValorUtilizadoAdiantamento() + valorParcialAdiantamentoSeraUtilizado);
    				   
    				   // calcular o % do adiantamento que está sendo utilizado nesta conta a pagar em questão
    				   double totalAdiantamento = adiantamento.getValorPago();
    				   double percentualAdiantamentoUtilizado = Uteis.arredondar((valorParcialAdiantamentoSeraUtilizado * 100 / totalAdiantamento), 2 ,0);
    				   
    				   // acumulando o total dos adiantamentos utilizados
    				   valorTotalAdiantamentoUtilizado = valorTotalAdiantamentoUtilizado + valorParcialAdiantamentoSeraUtilizado;
    				   
    				   // vamos gerar o rastro de uso do adiantamento na conta a pagar correspondente
    				   ContaPagarAdiantamentoVO contaPagarAdiantamentoVO = new ContaPagarAdiantamentoVO();
    				   contaPagarAdiantamentoVO.setContaPagar(contaPagar);
    				   contaPagarAdiantamentoVO.setContaPagarUtilizada(adiantamento);
    				   contaPagarAdiantamentoVO.setDataUsoAdiantamento(new Date());
    				   contaPagarAdiantamentoVO.setValorUtilizado(valorParcialAdiantamentoSeraUtilizado);
    				   contaPagarAdiantamentoVO.setPercenutalContaPagarUtilizada(percentualAdiantamentoUtilizado);
    				   contaPagar.getListaContaPagarAdiantamentoVO().add(contaPagarAdiantamentoVO);
    			   }
    			}
    			if (valorResidualContaPagarParaAbatimento <= 0) {
    				// se nao ha mais valor residual para abater, entao podemos sair do metodo. pois a conta a pagar
    				// ja foi resolvida.
    				break;
    			}
    		}
    		j++;
    	}
    	return valorTotalAdiantamentoUtilizado;
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void montarContaPagarPorRegistroDetalhe(ContaPagarRegistroArquivoVO cpra, UsuarioVO usuarioVO) throws Exception {
		cpra.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorNossoNumero(cpra.getRegistroDetalhePagarVO().getNossoNumero(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
		if (Uteis.isAtributoPreenchido(cpra.getContaPagarVO())) {
			cpra.getContaPagarVO().setObservacao("Conta Localizada!");
			if (!cpra.isContaPagarEfetivado()) {
				cpra.getContaPagarVO().setObservacao(cpra.getMotivoRejeicao_Apresentar());
			} else if (cpra.getRegistroDetalhePagarVO().getValorPagamento().equals(0.0)) {
				cpra.getContaPagarVO().setObservacao("Conta com problemas no valor pago!");
			} else if (cpra.getContaPagarVO().getQuitada()) {
				cpra.getContaPagarVO().setObservacao("Conta já Paga!");
			}
		} else if (!cpra.isContaPagarEfetivado()) {
			cpra.getContaPagarVO().setObservacao(cpra.getMotivoRejeicao_Apresentar());
		} else {
			cpra.getContaPagarVO().setObservacao("Conta Não Localizada!");
		}
	}

	@Override
	public Boolean verificarExistenciaContaPagarRecebidaEmDuplicidade(Integer contaPagar) throws Exception {
		StringBuilder sql = new StringBuilder("select codigo from contapagar where codigo = ? and situacao = ? ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contaPagar, "PA").next();
	}

	@Override
	public Boolean consultarSeExisteContaPagarPorCodOrigemPorTipoOrigem(String codigoOrigem, String tipoOrigem, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from contapagar where codorigem = ? and tipoorigem = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoOrigem, tipoOrigem);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);

	}

	@Override
	public Boolean consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(String codigoOrigem, String tipoOrigem, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from contapagar where codorigem = ? and tipoorigem = ?  ");
		sql.append(" and situacao in ('").append(SituacaoFinanceira.PAGO.getValor()).append("','").append(SituacaoFinanceira.PAGO_PARCIAL.getValor()).append("') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoOrigem, tipoOrigem);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(String codOrigem, String situacao, Integer contaPagarDiferente, OrigemContaPagar origemContaPagar, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(distinct contapagar.codigo) as qtd  ");
		sql.append(" from compra ");
		sql.append(" inner join recebimentocompra on recebimentocompra.compra = compra.codigo ");
		sql.append(" left join notafiscalentradarecebimentocompra on notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo ");
		sql.append(" left join notafiscalentrada on notafiscalentradarecebimentocompra.notafiscalentrada = notafiscalentrada.codigo ");
		sql.append(" inner join contapagar on  ");
		sql.append(" codorigem::text =  (case when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA.name()).append("' then recebimentocompra.codigo::text when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.COMPRA.name()).append("' then compra.codigo::text else notafiscalentrada.codigo::text end) ");
		sql.append(" and tipoorigem  =  (case when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA.name()).append("' then '").append(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor()).append("' when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.COMPRA.name()).append("' then '").append(OrigemContaPagar.COMPRA.getValor()).append("' else '").append(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor()).append("' end) ");
		sql.append(" where  contapagar.situacao = '").append(situacao).append("' and contapagar.codigo != ").append(contaPagarDiferente).append(" ");
		if (origemContaPagar.isRecebimentoCompra()) {
			sql.append(" and  recebimentocompra.codigo = ").append(codOrigem);
		} else if (origemContaPagar.isCompra()) {
			sql.append(" and  compra.codigo = ").append(codOrigem);
		} else if (origemContaPagar.isNotaFiscalEntrada()) {
			sql.append(" and  notafiscalentrada.codigo = ").append(codOrigem);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void preencherCamposRemessaPorTipoLancamento(ContaPagarVO obj) {
		if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar()) && obj.getTipoLancamentoContaPagar().isTransferencia()) {			
			if (obj.getTipoLancamentoContaPagar().isTransferenciaTed()) {
				obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.TED);
			} else if (obj.getTipoLancamentoContaPagar().isTransferenciaDoc()) {
				obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.DOC);
			}else if(obj.getTipoLancamentoContaPagar().isPixTransferencia()) {
				obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.PIX);
			}
			obj.setFinalidadeTedEnum(FinalidadeTedEnum.OUTROS);
			obj.setFinalidadeDocEnum(null);
		} else if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar()) && (obj.getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra() || obj.getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra() || obj.getTipoLancamentoContaPagar().isGpsSemCodigoBarra())) {
				obj.setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.CPF);
		} else {
			obj.setModalidadeTransferenciaBancariaEnum(null);
			obj.setFinalidadeDocEnum(null);
			obj.setFinalidadeTedEnum(null);
			/*obj.setLinhaDigitavel1("");
			obj.setLinhaDigitavel2("");
			obj.setLinhaDigitavel3("");
			obj.setLinhaDigitavel4("");
			obj.setLinhaDigitavel5("");
			obj.setLinhaDigitavel6("");
			obj.setLinhaDigitavel7("");
			obj.setLinhaDigitavel8("");
			obj.setCodigoBarra("");
			obj.setBancoRecebimento(null);
			obj.setNumeroAgenciaRecebimento("");
			obj.setContaCorrenteRecebimento("");
			obj.setDigitoAgenciaRecebimento("");
			obj.setDigitoCorrenteRecebimento("");*/
			obj.setTipoIdentificacaoContribuinte(null);
			obj.setCodigoReceitaTributo("");
			obj.setIdentificacaoContribuinte("");
			obj.setNumeroReferencia("");
			obj.setValorReceitaBrutaAcumulada(0.0);
			obj.setPercentualReceitaBrutaAcumulada(0.0);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void preencherDadosRemessaContaPagar(ContaPagarVO obj, String bancoRecebimento, String agenciaRecebimento, String digitoAgenciaRecebimento, String contaRecebimento, String digitoContaRecebimento,String chaveEnderecamentoPix , TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(bancoRecebimento)) {
			List<BancoVO> objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(bancoRecebimento, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (!objs.isEmpty()) {
				obj.setBancoRecebimento(objs.get(0));
			}
		}
		obj.setNumeroAgenciaRecebimento(agenciaRecebimento);
		obj.setDigitoAgenciaRecebimento(digitoAgenciaRecebimento);
		obj.setContaCorrenteRecebimento(contaRecebimento);
		obj.setDigitoCorrenteRecebimento(digitoContaRecebimento);
		obj.setTipoIdentificacaoChavePixEnum(tipoIdentificacaoChavePixEnum);
		obj.setChaveEnderecamentoPix(chaveEnderecamentoPix);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final Integer codigo, SituacaoFinanceira situacaoFinanceira, UsuarioVO usuario) throws Exception {
		try {
			ContaPagarVO obj = consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			if(!situacaoFinanceira.getValor().equals("NE")) {
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
			}
			
			final String sql = "UPDATE ContaPagar set situacao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacaoFinanceira.getValor());
					sqlAlterar.setInt(++i, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("check_validar_situacao_conta_pagar")) {
				throw new ConsistirException("A Situação da Conta a Pagar está desatualizada. Atualize os dados.");
			}
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarContaPagar(ContaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoFinanceira situacaoAnterior = SituacaoFinanceira.getEnum(obj.getSituacao());
		try {
			ContaPagar.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			Uteis.checkState(obj.isQuitada(), "Não é possivel alterar a situação da conta pagar pois, a mesma esta quitada.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getMotivoCancelamento()), "O campo Motivo Cancelamento deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataCancelamento()), "O campo Data Cancelamento deve ser informado.");
			obj.setSituacao(SituacaoFinanceira.CANCELADO_FINANCEIRO.getValor());
			alterarCamposSitacaoAndDataCancelamentoAndMotivoCancelamento(obj, usuarioVO);
		} catch (Exception e) {
			obj.setSituacao(situacaoAnterior.getValor());
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void reativarContaPagarCancelada(ContaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoFinanceira situacaoAnterior = SituacaoFinanceira.getEnum(obj.getSituacao());
		try {
			ContaPagar.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			Uteis.checkState(!obj.isCanceladoFinanceiro(), "Não é possivel reativar a situação da conta pagar pois, a mesma não esta cancelada.");
			obj.setSituacao(SituacaoFinanceira.A_PAGAR.getValor());
			obj.setMotivoCancelamento("");
			obj.setDataCancelamento(null);
			alterarCamposSitacaoAndDataCancelamentoAndMotivoCancelamento(obj, usuarioVO);
		} catch (Exception e) {
			obj.setSituacao(situacaoAnterior.getValor());
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCamposSitacaoAndDataCancelamentoAndMotivoCancelamento(ContaPagarVO obj, UsuarioVO usuario) throws Exception {
		if(!obj.getSituacao().equals("NE")) {
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
		}
		alterar(obj, "contaPagar", new AtributoPersistencia()
				.add("situacao", obj.getSituacao())
				.add("dataCancelamento", Uteis.getDataJDBCTimestamp(obj.getDataCancelamento()))
				.add("motivoCancelamento", obj.getMotivoCancelamento())
				, new AtributoPersistencia().add("codigo", obj.getCodigo())
				, usuario);		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarContaPagarPorGestaoContasPagar(ContaPagarVO obj, boolean isAtualizarCentroResultado , UsuarioVO usuario) throws Exception {
		if(!obj.getSituacao().equals("NE")) {
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataVencimento(), obj.getDataFatoGerador(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.APAGAR, usuario );
		}
		alterar(obj, "contaPagar", new AtributoPersistencia()
				.add("dataVencimento", obj.getDataVencimento())
				.add("valor", obj.getValor())
				, new AtributoPersistencia().add("codigo", obj.getCodigo())
				, usuario);
		if(isAtualizarCentroResultado) {
			Boolean permitirGravarContaPagarIsenta = (obj.getSituacao().equals("PA") && obj.getValorPagamento().equals(0.0)) ? true : false;
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, false, usuario, permitirGravarContaPagarIsenta);	
		}
	}
	
	
	@Override
	public List<ContaPagarVO> consultarPorPeriodoTipoSacadoCodigoSacadoAPagar(String campoConsulta, String valorConsulta, Date dataInicio, Date dataFim, TipoSacado tipoSacado, Integer codigoSacado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql  = new StringBuilder("select contapagar.* from contapagar ");
		if(tipoSacado.equals(TipoSacado.FORNECEDOR)) {
			sql.append(" inner join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
			sql.append(" where fornecedor.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.FUNCIONARIO_PROFESSOR)) {
			sql.append(" inner join funcionario on funcionario.codigo = contapagar.funcionario  ");
			sql.append(" inner join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
			sql.append(" where  pessoafuncionario.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.PARCEIRO)) {
			sql.append(" inner join parceiro on parceiro.codigo = contapagar.parceiro  ");
			sql.append(" where  parceiro.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.RESPONSAVEL_FINANCEIRO)) {
			sql.append(" inner join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
			sql.append(" where  responsavelFinanceiro.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.ALUNO)) {
			sql.append(" inner join pessoa on pessoa.codigo = contapagar.pessoa ");
			sql.append(" where  pessoa.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.BANCO)) {
			sql.append(" inner join banco on banco.codigo = contapagar.banco ");
			sql.append(" where  banco.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.OPERADORA_CARTAO)) {
			sql.append(" inner join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
			sql.append(" where  operadoracartao.codigo = ").append(codigoSacado);
		}else if(tipoSacado.equals(TipoSacado.CANDIDATO)) {
			sql.append(" inner join pessoa on pessoa.codigo = contapagar.pessoa ");
			sql.append(" where  pessoa.codigo = ").append(codigoSacado);
		}
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			if(campoConsulta.equals("codigo")) {
				if(!Uteis.getIsValorNumerico(valorConsulta)) {
					throw new Exception("Informe apenas números no filtro da consulta.");
				}
				sql.append(" and  contapagar.codigo = (?)::int ");
			}else if(campoConsulta.equals("notaFiscal")) {
				if(!Uteis.getIsValorNumerico(valorConsulta)) {
					throw new Exception("Informe apenas números no filtro da consulta.");
				}
				sql.append(" and (?)::int =  ANY(string_to_array(numeroNotaFiscalEntrada, ',')::int[]) ");
			}else if(campoConsulta.equals("nrDocumento")) {
				valorConsulta += PERCENT;
				sql.append(" and  sem_acentos(contapagar.nrDocumento) ilike ( sem_acentos(?)) ");
			}else if(campoConsulta.equals("turma")) {
				valorConsulta += PERCENT;
				sql.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join turma on turma.codigo = centroresultadoorigem.turma where  ((sem_acentos(turma.identificadorturma)) ilike (sem_acentos(?))) and centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar)  ");
			}else if(campoConsulta.equals("departamento")) {
				valorConsulta += PERCENT;
				sql.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento where  ((sem_acentos(departamento.nome)) ilike (sem_acentos(?))) and centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar)  ");
			}else if(campoConsulta.equals("categoriaDespesa")) {
				valorConsulta += PERCENT;
				sql.append(" and exists(select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where  ((sem_acentos(categoriadespesa.descricao)) ilike (sem_acentos(?))) and centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar) ");
			}
		}
		sql.append(" and ").append(super.realizarGeracaoWherePeriodo(dataInicio, dataFim, "contapagar.datavencimento", false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and  contapagar.unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(" and contapagar.situacao = 'AP' ");
		sql.append(" order by contapagar.datavencimento, contapagar.parcela, contapagar.codigo ");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
	}

	@Override
	public Map<String, Double> consultarPorNumeroNotaFiscalEntradaTotalPagarTotalPago(Integer valorConsulta, Integer unidadeEnsino, Date dataIni, Date dataFim, String situacao, Boolean filtrarDataFatorGerador) throws Exception {
		StringBuilder sql = null;
		if (situacao == null || (!situacao.equals("PA") && !situacao.equals("AP") && !situacao.equals("NE") && !situacao.equals("PAAP"))) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		} else if (situacao.equals("PAAP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("AP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, 0 AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("PA")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("NE")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, 0 AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
}
		sql.append(getSQLPadraoConsultaTotalPagar());
		sql.append(" where ? =   ANY(string_to_array(numeroNotaFiscalEntrada, ',')::int[]) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and (contapagar.unidadeensino = ").append(unidadeEnsino).append(")  ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append(" and (contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append(" and (contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append(" and  (UPPER(contapagar.situacao) = '").append(situacao).append("') ");
		} else {
			sql.append(" and  contapagar.situacao in ('AP', 'PP') ");
		}
		
		sql.append(" ) UNION ALL ( ");
		sql.append(getSQLPadraoConsultaTotalPago());
		sql.append(" where ? =   ANY(string_to_array(numeroNotaFiscalEntrada, ',')::int[]) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and (contapagar.unidadeensino = ").append(unidadeEnsino).append(")  ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append(" and  (contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append(" and  (contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		if (situacao != null &&  (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append(" and (UPPER(contapagar.situacao) = '").append(situacao).append("') ");
		} else {
			sql.append(" and contapagar.situacao in ('PA', 'PP') ");
		}
		
		sql.append(" ) UNION ALL ( ");
		sql.append(getSQLPadraoConsultaTotalNegociado());
		sql.append(" where ? =   ANY(string_to_array(numeroNotaFiscalEntrada, ',')::int[]) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and (contapagar.unidadeensino = ").append(unidadeEnsino).append(")  ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append(" and (contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append(" and (contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0"))) {
			sql.append(" and (UPPER(contapagar.situacao) = '").append(situacao).append("')  ");
		} else {
			sql.append(" and contapagar.situacao = 'NE'  ");
		}
		
		sql.append(" )) AS contapagar");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta, valorConsulta, valorConsulta);
		return montarDadosConsultaTotalPagarTotalPago(resultado);
	}
	
	private List<ContaPagarVO> consultarPorNrDocumento(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		sql.append(" WHERE (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?)) ");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append("ORDER BY contapagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	private Integer consultarPorNrDocumentoTotalRegistros(String situacaoDePagamento, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		sql.append(" WHERE (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?)) ");	
		dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		preencherDadosParaConsultaFiltros(situacaoDePagamento, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Map<String, Double> consultaRapidaPorNrDocumentoTotalPagarTotalPago(String valorConsulta, Date dataIni, Date dataFim, String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = null;
		if (situacao == null || (!situacao.equals("PA") && !situacao.equals("AP") && !situacao.equals("NE") && !situacao.equals("PAAP"))) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		} else if (situacao.equals("PAAP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("AP")) {
			sql = new StringBuilder("SELECT sum(valorAPagar) AS valorAPagar, 0 AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("PA")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, sum(valorPago) AS valorPago, 0 as valorNegociado FROM(( ");
		} else if (situacao.equals("NE")){
			sql = new StringBuilder("SELECT 0 AS valorAPagar, 0 AS valorPago, sum(valorNegociado) as valorNegociado FROM(( ");
		}
		sql.append(getSQLPadraoConsultaTotalPagar());
		sql.append("WHERE (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?))  and ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('AP', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.dataFatoGerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" )UNION ALL( ");
		sql.append(getSQLPadraoConsultaTotalPago());
		sql.append("WHERE (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?))  and ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null && (!situacao.equals("")) && (!situacao.equals("0")) && !situacao.equals("PAAP")) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao in ('PA', 'PP') AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.datafatogerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" ) UNION ALL ( ");
		sql.append(getSQLPadraoConsultaTotalNegociado());
		sql.append("WHERE (sem_acentos(contapagar.nrdocumento)) ilike(sem_acentos(?))  and ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("(contapagar.unidadeensino = ").append(unidadeEnsino).append(") AND ");
		}
		if (situacao != null &&  (!situacao.equals("")) && (!situacao.equals("0"))) {
			sql.append("(UPPER(contapagar.situacao) = '").append(situacao).append("') AND ");
		} else {
			sql.append("contapagar.situacao = 'NE' AND ");
		}
		if (!filtrarDataFatorGerador) {
			sql.append("(contapagar.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		} else {
			sql.append("(contapagar.datafatogerador BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)).append("') ");
		}
		sql.append(" )) AS contapagar");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%", valorConsulta+"%", valorConsulta+"%");
		return montarDadosConsultaTotalPagarTotalPago(resultado);
	}
	
	/**
	 * INICIO MERGE EDIGAR 24/05/18
	 */
	public List<ContaPagarVO> consultaRapidaContaPagarAdiantamentoPodemSerUtilizadasParaAbatimentoNegociacaoPagamento(
			Integer unidadeEnsino, String tipoSacado,  Integer codigoSacado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("Select * from contaPagar where 1=1 ");
		if ((unidadeEnsino != null) && (unidadeEnsino != 0)) {
			sql.append(" AND unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(" AND situacao = 'PA' "); // titulo precisa estar quitado
		sql.append(" AND tipoOrigem = 'AD' "); // tipoOtidem precisar se AD - Adiantmaneto
		sql.append(" AND valorUtilizadoAdiantamento < valorPago "); // valor Ja Utilizado do Adiantamento precisa ser menor que o valorpago - caso contrario todo o adiantamento ja foi utilizado
		sql.append(" AND tipoSacado = '").append(tipoSacado).append("' "); // tem q ser o mesmo tipo de sacado
		if (tipoSacado.equals(TipoSacado.FORNECEDOR.getValor())) {
			sql.append(" AND fornecedor = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			sql.append(" AND funcionario = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.BANCO.getValor())) {
			sql.append(" AND banco = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.ALUNO.getValor())) {
			sql.append(" AND pessoa = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
			sql.append(" AND responsavelFinanceiro = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.PARCEIRO.getValor())) {
			sql.append(" AND parceiro = ").append(codigoSacado);
		} else if (tipoSacado.equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			sql.append(" AND operadoraCartao = ").append(codigoSacado);
		}
		sql.append(" order by dataVencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}
	
	public List<ContaPagarVO> consultarPorCnpjFornecedor(String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM contapagar ");
		preencherDadosParaConsultaPorCnpjFornecedor(dataModelo.getValorConsulta(), situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append("order by contapagar.datavencimento");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+PERCENT);
		
		return montarDadosConsulta(resultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}
	
	private void preencherDadosParaConsultaPorCnpjFornecedor(String cnpjFornecedor, String situacao, Date dataIni, Date dataFim, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, StringBuilder sql) {
		sql.append(" inner join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sql.append(" where fornecedor.CNPJ like(?) and fornecedor.tipoempresa = 'JU'");

		preencherDadosParaConsultaFiltros(situacao, dataIni, dataFim, unidadeEnsino, filtrarDataFatorGerador, sql);
	}
	
	public Integer consultarPorCnpjFornecedorTotalRegistros(String situacao, Integer unidadeEnsino, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM contapagar ");
		preencherDadosParaConsultaPorCnpjFornecedor(dataModelo.getValorConsulta(), situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+PERCENT);
	
		return (Integer) Uteis.getSqlRowSetTotalizador(resultado, "qtde", TipoCampoEnum.INTEIRO);

	}
	
	@Override
	public ContaPagarVO consultarPorPeriodoVencimentoPorUnidadeEnsinoPorValorPorSituacao( Date dataIni, Date dataFim, Integer unidadeEnsino, Double valor, String situacao, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM ContaPagar  ");	
		sql.append(" where valor = ").append(valor).append(" ");
		preencherDadosParaConsultaFiltros(situacao, dataIni, dataFim, unidadeEnsino, false, sql);
		sql.append(" ORDER BY codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new ContaPagarVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	private List<ContaPagarVO> consultarPorCodigoNotaFiscalEntrada(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM ContaPagar  ");	
		sql.append(" where codigonotafiscalentrada like '%").append(valorConsulta).append("%' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append(" ORDER BY datavencimento ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	private Integer consultarPorCodigoNotaFiscalEntradaTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar ");
		sql.append(" where codigonotafiscalentrada like '%").append(valorConsulta).append("%' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContaPagarVO> consultarPorCodigoContratoDespesa(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ContaPagar where tipoorigem = 'CD' and codorigem = ? ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		sql.append(" ORDER BY datavencimento ");
		UteisTexto.addLimitAndOffset(sql,  dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta.toString());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}
	
	private Integer consultarPorCodigoContratoDespesaTotalRegistros(Integer valorConsulta, Integer unidadeEnsino, String situacao, Boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar where contapagar.tipoorigem = 'CD' and contapagar.codorigem = ? ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);				
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	@Override
	public void validarDados(ContaPagarVO contaPagar) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(contaPagar.getFormaPagamentoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("fdaf"));
		}
		
	}
    
	

	private List<ContaPagarVO> consultarPorFaixaValor( Integer unidadeEnsino,String situacao, boolean filtrarDataFatorGerador, DataModelo dataModelo)  throws Exception {
		String   valorInicial = String.valueOf(dataModelo.getListaConsulta().get(0));
		String  valorFinal =   String.valueOf(dataModelo.getListaConsulta().get(1));
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM ContaPagar  ");		
		sql.append(" where valor >= '").append(valorInicial).append("' ");
		sql.append(" and  valor <= '").append(valorFinal).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);		
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}
	
	private Integer consultarPorFaixaValorTotalRegistros( Integer unidadeEnsino,	String situacao, boolean filtrarDataFatorGerador, DataModelo dataModelo)  throws Exception  {
		String   valorInicial = String.valueOf(dataModelo.getListaConsulta().get(0));
		String  valorFinal =   String.valueOf(dataModelo.getListaConsulta().get(1));
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar ");
		sql.append(" where valor >= '").append(valorInicial).append("' ");
		sql.append(" and  valor <= '").append(valorFinal).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	private List<ContaPagarVO> consultarValor( Integer unidadeEnsino, String situacao,	boolean filtrarDataFatorGerador, DataModelo dataModelo) throws Exception  {		
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contapagar.* FROM ContaPagar  ");		
		sql.append(" where valor= '").append(dataModelo.getValorConsulta()).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);		
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	private Integer consultarValorTotalRegistros( Integer unidadeEnsino, String situacao,boolean filtrarDataFatorGerador, DataModelo dataModelo)throws Exception {		
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(contapagar.codigo) as qtde FROM ContaPagar ");
		sql.append(" where valor= '").append(dataModelo.getValorConsulta()).append("' ");
		preencherDadosParaConsultaFiltros(situacao, dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, filtrarDataFatorGerador, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	
	
}
