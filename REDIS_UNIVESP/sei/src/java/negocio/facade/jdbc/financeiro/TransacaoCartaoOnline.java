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

import com.amazonaws.services.dynamodbv2.document.Index;

import jobs.JobTransacaoCartaoOnline;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CieloCodigoRetornoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.TransacaoCartaoOnlineVO;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TransacaoCartaoOnlineInterfaceFacade;

/**
 * @author Victor Hugo 06/04/2016 5.0.4.0
 */
@Repository
@Scope("singleton")
@Lazy
public class TransacaoCartaoOnline extends ControleAcesso implements TransacaoCartaoOnlineInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransacaoCartaoOnlineVO transacaoCartaoOnlineVO, final UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO transacaocartaoonline(");
			sql.append("            requisicao, retornorequisicao, datatransacao, responsaveltransacao, ");
			sql.append("            formapagamentonegociacaorecebimentocartaocredito, chavepedido, ");
			sql.append("            chavetransacao, tipopessoa, pessoa, parceiro, fornecedor, matricula, ");
			sql.append("            mensagem, contareceberrecebimento, situacaotransacao, cartao, ");
			sql.append("			numeroparcela, valorParcela, dataVencimento, tipoCartao, matriculaPeriodo, tipoOrigem, parcelaContaReceber, ");
			sql.append("            tipoOrigemOperadoraCodigoRetornoDCC, codigoOrigemOperadoraCodigoRetornoDCC, codigoContaReceber, nossoNumero, cartaoCreditoDebitoRecorrenciaPessoa, nomeCustomer)"); 
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ");
			sql.append("            ?, ?, ?, ?, ?, ?, ");
			sql.append("            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo;");
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, transacaoCartaoOnlineVO.getRequisicao());
					sqlInserir.setString(2, transacaoCartaoOnlineVO.getRetornoRequisicao());
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(transacaoCartaoOnlineVO.getDataTransacao()));
					if (!transacaoCartaoOnlineVO.getResponsavelTransacao().getCodigo().equals(0)) {
						sqlInserir.setInt(4, transacaoCartaoOnlineVO.getResponsavelTransacao().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (!transacaoCartaoOnlineVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(5, transacaoCartaoOnlineVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, transacaoCartaoOnlineVO.getChavePedido());
					sqlInserir.setString(7, transacaoCartaoOnlineVO.getChaveTransacao());
					sqlInserir.setString(8, transacaoCartaoOnlineVO.getTipoPessoa().getValor());
					if (!transacaoCartaoOnlineVO.getPessoaVO().getCodigo().equals(0)) {
						sqlInserir.setInt(9, transacaoCartaoOnlineVO.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (!transacaoCartaoOnlineVO.getParceiroVO().getCodigo().equals(0)) {
						sqlInserir.setInt(10, transacaoCartaoOnlineVO.getParceiroVO().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (!transacaoCartaoOnlineVO.getFornecedorVO().getCodigo().equals(0)) {
						sqlInserir.setInt(11, transacaoCartaoOnlineVO.getFornecedorVO().getCodigo());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getMatricula())) {
						sqlInserir.setString(12, transacaoCartaoOnlineVO.getMatricula());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, transacaoCartaoOnlineVO.getMensagem());
					sqlInserir.setString(14, transacaoCartaoOnlineVO.getContaReceberRecebimento());
					sqlInserir.setString(15, transacaoCartaoOnlineVO.getSituacaoTransacao().getName());
					sqlInserir.setString(16, transacaoCartaoOnlineVO.getCartao());
					sqlInserir.setString(17, transacaoCartaoOnlineVO.getNumeroParcela());
					sqlInserir.setDouble(18, transacaoCartaoOnlineVO.getValorParcela());
					sqlInserir.setTimestamp(19, Uteis.getDataJDBCTimestamp(transacaoCartaoOnlineVO.getDataVencimento()));
					sqlInserir.setString(20, transacaoCartaoOnlineVO.getTipoCartao().name());
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getMatriculaPeriodoVO().getCodigo())) {
						sqlInserir.setInt(21, transacaoCartaoOnlineVO.getMatriculaPeriodoVO().getCodigo());
					} else {
						sqlInserir.setNull(21, 0);
					}
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getTipoOrigem())) {
						sqlInserir.setString(22, transacaoCartaoOnlineVO.getTipoOrigem().name());
					} else {
						sqlInserir.setNull(22, 0);
					}
					sqlInserir.setString(23, transacaoCartaoOnlineVO.getParcelaContaReceber());
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getTipoOrigemOperadoraCodigoRetornoDCC())) {
						sqlInserir.setString(24, transacaoCartaoOnlineVO.getTipoOrigemOperadoraCodigoRetornoDCC().toString());
					} else {
						sqlInserir.setNull(24, 0);
					}
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getCodigoOrigemOperadoraCodigoRetornoDCC())) {
						sqlInserir.setInt(25, transacaoCartaoOnlineVO.getCodigoOrigemOperadoraCodigoRetornoDCC());
					} else {
						sqlInserir.setNull(25, 0);
					}
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getCodigoContaReceber())) {
						sqlInserir.setInt(26, transacaoCartaoOnlineVO.getCodigoContaReceber());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setString(27, transacaoCartaoOnlineVO.getNossoNumero());
					if (Uteis.isAtributoPreenchido(transacaoCartaoOnlineVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo())) {
						sqlInserir.setInt(28, transacaoCartaoOnlineVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(28, 0);
					}
					sqlInserir.setString(29, transacaoCartaoOnlineVO.getNomeCustomer());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	/**
	 * @param formaPagamentoNegociacaoRecebimentoCartaoCreditoVO
	 * @param usuarioVO
	 */
	public TransacaoCartaoOnlineVO montarTransacaoCartaoOnline(String requisicao, String respostaRequisicao, String chavePedido, String chaveTransacao, String mensagem, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, String contaReceberRecebimento, SituacaoTransacaoEnum situacaoTransacaoEnum, String cartao, String numeroParcela, Double valorParcela, Date dataVencimento, TipoCartaoOperadoraCartaoEnum tipoCartao, ContaReceberVO contaReceberVO, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa, String nomeCustomer, UsuarioVO usuarioVO) throws Exception {
		TransacaoCartaoOnlineVO transacaoCartaoOnlineVO = new TransacaoCartaoOnlineVO();
		transacaoCartaoOnlineVO.setRequisicao(respostaRequisicao.replace(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getNumeroCartao(), cartao).replace(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getCodigoVerificacao(), "***") );
		transacaoCartaoOnlineVO.setRetornoRequisicao(respostaRequisicao.replace(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getNumeroCartao(), cartao).replace(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getCodigoVerificacao(), "***") );
		transacaoCartaoOnlineVO.setChavePedido(chavePedido);
		transacaoCartaoOnlineVO.setChaveTransacao(chaveTransacao);
		transacaoCartaoOnlineVO.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO);
		transacaoCartaoOnlineVO.setDataTransacao(new Date());
		transacaoCartaoOnlineVO.setTipoPessoa(TipoPessoa.getEnum(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoPessoa()));
		transacaoCartaoOnlineVO.setPessoaVO(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa());
		transacaoCartaoOnlineVO.setParceiroVO(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO());
		transacaoCartaoOnlineVO.setFornecedorVO(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor());
		transacaoCartaoOnlineVO.setMatricula(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getMatricula());
		transacaoCartaoOnlineVO.setResponsavelTransacao(usuarioVO);
		transacaoCartaoOnlineVO.setMensagem(mensagem);
		transacaoCartaoOnlineVO.setContaReceberRecebimento(contaReceberRecebimento);
		transacaoCartaoOnlineVO.setSituacaoTransacao(situacaoTransacaoEnum);
		transacaoCartaoOnlineVO.setCartao(cartao);
		transacaoCartaoOnlineVO.setNumeroParcela(numeroParcela);
		transacaoCartaoOnlineVO.setValorParcela(valorParcela);
		transacaoCartaoOnlineVO.setDataVencimento(dataVencimento);
		transacaoCartaoOnlineVO.setTipoCartao(tipoCartao);
		transacaoCartaoOnlineVO.setTransacaoProvenienteRecorrencia(transacaoProvenienteRecorrencia);
		transacaoCartaoOnlineVO.setJobExecutadaManualmente(jobExecutadaManualmente);
		if (Uteis.isAtributoPreenchido(contaReceberVO)) {
			transacaoCartaoOnlineVO.setCodigoContaReceber(contaReceberVO.getCodigo());
			transacaoCartaoOnlineVO.setNossoNumero(contaReceberVO.getNossoNumero());
			transacaoCartaoOnlineVO.getMatriculaPeriodoVO().setCodigo(contaReceberVO.getMatriculaPeriodo());
			transacaoCartaoOnlineVO.setTipoOrigem(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem()));
			transacaoCartaoOnlineVO.setParcelaContaReceber(contaReceberVO.getParcela());
		}
		transacaoCartaoOnlineVO.setTipoOrigemOperadoraCodigoRetornoDCC(tipoOrigemOperadoraCodigoRetornoDCC);
		transacaoCartaoOnlineVO.setCodigoOrigemOperadoraCodigoRetornoDCC(codigoOrigemOperadoraCodigoRetornoDCC);
		transacaoCartaoOnlineVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().setCodigo(cartaoCreditoDebitoRecorrenciaPessoa);
		transacaoCartaoOnlineVO.setNomeCustomer(nomeCustomer);
		return transacaoCartaoOnlineVO;
	}

	@Override
	/**
	 * 
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogTransacaoCartaoOnline(String requisicao, String respostaRequisicao, String chavePedido, String chaveTransacao, String mensagem, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, String contaReceberRecebimento, SituacaoTransacaoEnum situacaoTransacaoEnum, String cartao, String numeroParcela, Double valorParcela, Date dataVencimento, TipoCartaoOperadoraCartaoEnum tipoCartao, ContaReceberVO contaReceberVO, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa, String nomeCustomer, UsuarioVO usuarioVO) throws Exception {
		TransacaoCartaoOnlineVO transacaoCartaoOnlineVO = new TransacaoCartaoOnlineVO();
		transacaoCartaoOnlineVO = montarTransacaoCartaoOnline(requisicao, respostaRequisicao, chavePedido, chaveTransacao, mensagem, formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, contaReceberRecebimento, situacaoTransacaoEnum, cartao, numeroParcela, valorParcela, dataVencimento, tipoCartao, contaReceberVO, tipoOrigemOperadoraCodigoRetornoDCC, codigoOrigemOperadoraCodigoRetornoDCC, transacaoProvenienteRecorrencia, jobExecutadaManualmente, cartaoCreditoDebitoRecorrenciaPessoa, nomeCustomer, usuarioVO);
		Thread trThread = new Thread(new JobTransacaoCartaoOnline(transacaoCartaoOnlineVO, usuarioVO));
		trThread.start();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	
	public List<TransacaoCartaoOnlineVO> consultarPorCodigoFormaPagamentoNegociacaoRecebimento(String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Integer codigoFornecedor, Integer codigoContaReceber, Integer codigoContaReceberRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from transacaocartaoonline");
		sql.append(" where contareceberrecebimento ilike ('%[").append(codigoContaReceberRecebimento).append("]%') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TransacaoCartaoOnlineVO> transacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>(0);
		TransacaoCartaoOnlineVO transacaoCartaoOnlineVO = null;
		while (tabelaResultado.next()) {
			transacaoCartaoOnlineVO = new TransacaoCartaoOnlineVO();
			transacaoCartaoOnlineVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			transacaoCartaoOnlineVOs.add(transacaoCartaoOnlineVO);
		}
		return transacaoCartaoOnlineVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<TransacaoCartaoOnlineVO> consultarPorCodigoFormaPagamentoNegociacaoRecebimentoDCC(Integer codigoFormaPagamentoNegociacaoRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from transacaocartaoonline");
		sql.append(" inner join formapagamentonegociacaorecebimentodcc on formapagamentonegociacaorecebimentodcc.formapagamentonegociacaorecebimentocartaocredito = ");
		sql.append(" transacaocartaoonline.formapagamentonegociacaorecebimentocartaocredito");
		sql.append(" where formapagamentonegociacaorecebimentodcc.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoFormaPagamentoNegociacaoRecebimento });
		List<TransacaoCartaoOnlineVO> transacaocartaoonlineVOs = new ArrayList<TransacaoCartaoOnlineVO>(0);
		TransacaoCartaoOnlineVO transacaoCartaoOnlineVO = null;
		while (tabelaResultado.next()) {
			transacaoCartaoOnlineVO = new TransacaoCartaoOnlineVO();
			transacaoCartaoOnlineVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			transacaocartaoonlineVOs.add(transacaoCartaoOnlineVO);
		}
		return transacaocartaoonlineVOs;
	}

	public List<TransacaoCartaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TransacaoCartaoOnlineVO> vetResultado = new ArrayList<TransacaoCartaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	@Override
	/**
	 * @param resultado
	 * @param usuarioVO
	 */
	public TransacaoCartaoOnlineVO montarDados(SqlRowSet resultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		TransacaoCartaoOnlineVO transacaoCartaoOnlineVO = new TransacaoCartaoOnlineVO();
		transacaoCartaoOnlineVO.setCodigo(resultado.getInt("codigo"));
		transacaoCartaoOnlineVO.setRequisicao(resultado.getString("requisicao"));
		transacaoCartaoOnlineVO.setRetornoRequisicao(resultado.getString("retornorequisicao"));
		transacaoCartaoOnlineVO.setDataTransacao(resultado.getDate("datatransacao"));
		transacaoCartaoOnlineVO.getResponsavelTransacao().setCodigo(resultado.getInt("responsaveltransacao"));
		transacaoCartaoOnlineVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(resultado.getInt("formapagamentonegociacaorecebimentocartaocredito"));
		transacaoCartaoOnlineVO.setChavePedido(resultado.getString("chavepedido"));
		transacaoCartaoOnlineVO.setChaveTransacao(resultado.getString("chavetransacao"));
		transacaoCartaoOnlineVO.setTipoPessoa(TipoPessoa.getEnum(resultado.getString("tipopessoa")));
		transacaoCartaoOnlineVO.getPessoaVO().setCodigo(resultado.getInt("pessoa"));
		transacaoCartaoOnlineVO.getParceiroVO().setCodigo(resultado.getInt("parceiro"));
		transacaoCartaoOnlineVO.getFornecedorVO().setCodigo(resultado.getInt("fornecedor"));
		transacaoCartaoOnlineVO.setMatricula(resultado.getString("matricula"));
		transacaoCartaoOnlineVO.setMensagem(resultado.getString("mensagem"));
		transacaoCartaoOnlineVO.setContaReceberRecebimento(resultado.getString("contareceberrecebimento"));
		transacaoCartaoOnlineVO.setSituacaoTransacao(SituacaoTransacaoEnum.valueOf(resultado.getString("situacaotransacao")));
		transacaoCartaoOnlineVO.setCartao(resultado.getString("cartao"));
		transacaoCartaoOnlineVO.setNumeroParcela(resultado.getString("numeroparcela"));
		transacaoCartaoOnlineVO.setValorParcela(resultado.getDouble("valorparcela"));
		transacaoCartaoOnlineVO.setDataVencimento(resultado.getDate("datavencimento"));
		if (Uteis.NIVELMONTARDADOS_DADOSBASICOS == nivelMontarDados) {
			return transacaoCartaoOnlineVO;
		}
		return transacaoCartaoOnlineVO;
	}
	
	public StringBuilder getSqlFrom(TipoPessoa tipoPessoa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct transacaocartaoonline.codigo, transacaocartaoonline.matricula, transacaocartaoonline.dataTransacao, transacaocartaoonline.situacaoTransacao, transacaocartaoonline.cartao, ");
		sql.append(" transacaocartaoonline.numeroParcela, transacaocartaoonline.valorParcela, transacaocartaoonline.parcelaContaReceber, transacaocartaoonline.tipoOrigem, ");
		if (tipoPessoa.equals(TipoPessoa.PARCEIRO)) {
			sql.append(" parceiro.codigo AS \"parceiro.codigo\", parceiro.nome AS \"parceiro.nome\"");
		} else if (tipoPessoa.equals(TipoPessoa.FORNECEDOR)) {
			sql.append(" fornecedor.codigo AS \"fornecedor.codigo\", fornecedor.nome AS \"fornecedor.nome\"");
		} else {
			sql.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		}
		sql.append(" contareceber.dataVencimento AS \"contareceber.dataVencimento\", contaReceber.nossoNumero AS \"contaReceber.nossoNumero\", ");
		sql.append(" usuario.codigo AS \"usuario.codigo\", usuario.nome as \"usuario.nome\", ");
		sql.append(" cielocodigoretorno.codigo AS \"cielocodigoretorno.codigo\", cielocodigoretorno.codigoResposta AS \"cielocodigoretorno.codigoResposta\", ");
		sql.append(" cielocodigoretorno.definicao AS \"cielocodigoretorno.definicao\", cielocodigoretorno.significado AS \"cielocodigoretorno.significado\",  ");
		sql.append(" cielocodigoretorno.acao AS \"cielocodigoretorno.acao\", cielocodigoretorno.permiteRetentativa AS \"cielocodigoretorno.permiteRetentativa\" ");
		sql.append(" from transacaocartaoonline ");

		return sql;
	}


	public StringBuilder getSqlRelacionamento(TipoPessoa tipoPessoa) {
		StringBuilder sql = new StringBuilder();
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			sql.append(" inner join matricula on matricula.matricula = transacaocartaoonline.matricula ");
			sql.append(" inner join pessoa on pessoa.codigo = transacaocartaoonline.pessoa ");
		} else if (tipoPessoa.equals(TipoPessoa.PARCEIRO)) {
			sql.append(" inner join parceiro on parceiro.codigo = transacaocartaoonline.parceiro ");
		} else if (tipoPessoa.equals(TipoPessoa.FORNECEDOR)) {
			sql.append(" inner join fornecedor on fornecedor.codigo = transacaocartaoonline.fornecedor ");
		} else {
			sql.append(" inner join pessoa on pessoa.codigo = transacaocartaoonline.pessoa ");
		}
		sql.append(" inner join usuario on usuario.codigo = transacaocartaoonline.responsavelTransacao ");
		sql.append(" left join contareceber on contareceber.matriculaaluno = transacaocartaoonline.matricula ");
		sql.append(" and contareceber.parcela = transacaocartaoonline.parcelaContaReceber ");
		sql.append(" and contareceber.nossonumero = transacaocartaoonline.nossonumero ");
		sql.append(" and case when transacaocartaoonline.tipoOrigem = 'MENSALIDADE' then contareceber.tipoOrigem = 'MEN' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'MATRICULA' then contareceber.tipoOrigem = 'MAT'  ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'REQUERIMENTO' then contareceber.tipoOrigem = 'REQ' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'BIBLIOTECA' then contareceber.tipoOrigem = 'BIB'  ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'DEVOLUCAO_CHEQUE' then contareceber.tipoOrigem = 'DCH'  ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'NEGOCIACAO' then contareceber.tipoOrigem = 'NCR' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'BOLSA_CUSTEADA_CONVENIO' then contareceber.tipoOrigem = 'BCC' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'CONTRATO_RECEITA' then contareceber.tipoOrigem = 'CTR' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'OUTROS' then contareceber.tipoOrigem = 'OUT' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'INCLUSAOREPOSICAO' then contareceber.tipoOrigem = 'IRE' ");
		sql.append(" when transacaocartaoonline.tipoOrigem = 'MATERIAL_DIDATICO' then contareceber.tipoOrigem = 'MDI' ");
		sql.append(" end ");
		sql.append(" and case when transacaocartaoonline.tipoOrigem in('MENSALIDADE', 'MATERIAL_DIDATICO', 'MATRICULA') then ");
		sql.append(" transacaocartaoonline.matriculaperiodo = contareceber.matriculaperiodo else true end ");
		
		sql.append(" left join formaPagamentoNegociacaoRecebimentoCartaoCredito on formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo = transacaocartaoonline.formaPagamentoNegociacaoRecebimentoCartaoCredito");
		sql.append(" left join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.formaPagamentoNegociacaoRecebimentoCartaoCredito = formaPagamentoNegociacaoRecebimentoCartaoCredito.codigo ");
		sql.append(" left join cielocodigoretorno on cielocodigoretorno.codigo = transacaocartaoonline.codigoorigemoperadoracodigoretornodcc ");
		sql.append(" and transacaocartaoonline.tipoorigemoperadoracodigoretornodcc = 'CIELO' ");
		return sql;
	}

	public StringBuilder getSqlWhere(TipoPessoa tipoPessoa, String tipoTransacao, String matricula, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<OperadoraCartaoVO> operadoraCartaoVOs, Date dataInicioTransacao, Date dataFimTransacao, String chaveTransacao) {
		StringBuilder sql = new StringBuilder();
		sql.append(" WHERE 1=1 ");
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			sql.append(" and transacaocartaoonline.tipoPessoa = '").append(tipoPessoa.getValor()).append("' ");
			if (Uteis.isAtributoPreenchido(matricula)) {
				sql.append(" and transacaocartaoonline.matricula = '").append(matricula).append("' ");
			}
			if (Uteis.isAtributoPreenchido(pessoaVO.getCodigo())) {
				sql.append(" and transacaocartaoonline.pessoa = ").append(pessoaVO.getCodigo());
			}
		} else if (tipoPessoa.equals(TipoPessoa.PARCEIRO)) {
			sql.append(" and transacaocartaoonline.tipoPessoa = '").append(tipoPessoa.getValor()).append("' ");
			sql.append(" and transacaocartaoonline.parceiro = ").append(parceiroVO.getCodigo());
		} else if (tipoPessoa.equals(TipoPessoa.FORNECEDOR)) {
			sql.append(" and transacaocartaoonline.tipoPessoa = '").append(tipoPessoa.getValor()).append("' ");
			sql.append(" and transacaocartaoonline.fornecedor = ").append(fornecedorVO.getCodigo());
		} else {
			sql.append(" and transacaocartaoonline.tipoPessoa = '").append(tipoPessoa.getValor()).append("' ");
			if (Uteis.isAtributoPreenchido(pessoaVO.getCodigo())) {
				sql.append(" and transacaocartaoonline.pessoa = ").append(pessoaVO.getCodigo());
			}
		}
		if (tipoTransacao.equals("SUCESSO")) {
			sql.append(" and transacaocartaoonline.situacaoTransacao = 'APROVADO' ");
		} else if (tipoTransacao.equals("INSUCESSO")) {
			sql.append(" and transacaocartaoonline.situacaoTransacao <> 'APROVADO' ");
		}
		if (!operadoraCartaoVOs.isEmpty()) {
			sql.append(" and formapagamentonegociacaorecebimento.operadoraCartao in (");
			int x = 0;
			for (OperadoraCartaoVO operadoraCartaoVO : operadoraCartaoVOs) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(operadoraCartaoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" and transacaocartaoonline.dataTransacao::date >= '").append(Uteis.getDataJDBC(dataInicioTransacao)).append("' ");
		sql.append(" and transacaocartaoonline.dataTransacao::date <= '").append(Uteis.getDataJDBC(dataFimTransacao)).append("' ");

		if (!unidadeEnsinoVOs.isEmpty() && tipoPessoa.equals(TipoPessoa.ALUNO)) {

			sql.append(" and matricula.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						sql.append(", ");
					}
					sql.append(unidadeEnsinoVO.getCodigo());
					x++;
				}
			}
			sql.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(chaveTransacao)) {
			sql.append(" and upper(transacaocartaoonline.chaveTransacao) ilike '").append(chaveTransacao.toUpperCase()).append("%' ");
		}
		return sql;
	}
	
	public StringBuilder getSqlOrdenacao(TipoPessoa tipoPessoa) {
		StringBuilder sql = new StringBuilder();
		if (tipoPessoa.equals(TipoPessoa.PARCEIRO)) {
			sql.append(" order by parceiro.nome ");
		} else if (tipoPessoa.equals(TipoPessoa.FORNECEDOR)) {
			sql.append(" order by fornecedor.nome ");
		} else {
			sql.append(" order by pessoa.nome ");
		}
		return sql;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<TransacaoCartaoOnlineVO> consultarPorFiltrosMapaPendenciaCartaoCreditoDCC(String tipoTransacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<OperadoraCartaoVO> listaOperadoraCartaoVOs, String matricula, TipoPessoa tipoPessoa, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO, Date dataInicioTransacao, Date dataFimTransacao, String chaveTransacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append(getSqlFrom(tipoPessoa));
		sql.append(getSqlRelacionamento(tipoPessoa));
		sql.append(getSqlWhere(tipoPessoa, tipoTransacao, matricula, pessoaVO, parceiroVO, fornecedorVO, unidadeEnsinoVOs, listaOperadoraCartaoVOs, dataInicioTransacao, dataFimTransacao, chaveTransacao));
		sql.append(getSqlOrdenacao(tipoPessoa));

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TransacaoCartaoOnlineVO> listaTransacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			TransacaoCartaoOnlineVO obj = new TransacaoCartaoOnlineVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.setDataTransacao(tabelaResultado.getDate("dataTransacao"));
			obj.setParcelaContaReceber(tabelaResultado.getString("parcelaContaReceber"));
			if (tabelaResultado.getString("tipoOrigem") != null) {
				obj.setTipoOrigem(TipoOrigemContaReceber.getEnumPorName(tabelaResultado.getString("tipoOrigem")));
			}
			if (tabelaResultado.getString("situacaoTransacao") != null) {
				obj.setSituacaoTransacao(SituacaoTransacaoEnum.valueOf(tabelaResultado.getString("situacaoTransacao")));
			}
			obj.setCartao(tabelaResultado.getString("cartao"));
			obj.setNumeroParcela(tabelaResultado.getString("numeroParcela"));
			obj.setValorParcela(tabelaResultado.getDouble("valorParcela"));
			if (tipoPessoa.equals(TipoPessoa.PARCEIRO)) {
				obj.getParceiroVO().setCodigo(tabelaResultado.getInt("parceiro.codigo"));
				obj.getParceiroVO().setNome(tabelaResultado.getString("parceiro.nome"));
			} else if (tipoPessoa.equals(TipoPessoa.FORNECEDOR)) {
				obj.getFornecedorVO().setCodigo(tabelaResultado.getInt("fornecedor.codigo"));
				obj.getFornecedorVO().setNome(tabelaResultado.getString("fornecedor.nome"));
			} else {
				obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
				obj.getPessoaVO().setNome(tabelaResultado.getString("pessoa.nome"));
			}
			obj.getResponsavelTransacao().setCodigo(tabelaResultado.getInt("usuario.codigo"));
			obj.getResponsavelTransacao().setNome(tabelaResultado.getString("usuario.nome"));
			obj.getContaReceberVO().setDataVencimento(tabelaResultado.getDate("contaReceber.dataVencimento"));
			obj.getContaReceberVO().setNossoNumero(tabelaResultado.getString("contaReceber.nossoNumero"));
			
			obj.getCieloCodigoRetornoVO().setCodigo(tabelaResultado.getInt("cielocodigoretorno.codigo"));
			obj.getCieloCodigoRetornoVO().setCodigoResposta(tabelaResultado.getString("cielocodigoretorno.codigoResposta"));
			obj.getCieloCodigoRetornoVO().setDefinicao(tabelaResultado.getString("cielocodigoretorno.definicao"));
			obj.getCieloCodigoRetornoVO().setSignificado(tabelaResultado.getString("cielocodigoretorno.significado"));
			obj.getCieloCodigoRetornoVO().setAcao(tabelaResultado.getString("cielocodigoretorno.acao"));
			obj.getCieloCodigoRetornoVO().setPermiteRetentativa(tabelaResultado.getString("cielocodigoretorno.permiteRetentativa"));
			listaTransacaoCartaoOnlineVOs.add(obj);
		}
		return listaTransacaoCartaoOnlineVOs;

	}
	
	@Override
	public List<TransacaoCartaoOnlineVO> consultarPorMatricula(String matricula, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select transacaocartaoonline.*, ");
		sql.append(" contareceber.datavencimento AS datavencimentoContaReceber, contareceber.parcela as parcelaContaReceber, contareceber.nossonumero, ");
		sql.append("(select cielocodigoretorno.definicao from cielocodigoretorno ");
		sql.append(" where cielocodigoretorno.codigo = transacaocartaoonline.codigoorigemoperadoracodigoretornodcc) as definicao ");
		sql.append(" from transacaocartaoonline ");
		sql.append(" left join contareceber on contareceber.matriculaaluno = transacaocartaoonline.matricula ");
		sql.append(" and contareceber.matriculaperiodo = transacaocartaoonline.matriculaperiodo ");
		sql.append(" and contareceber.tipoorigem  in('MEN', 'MDI') ");
		sql.append(" and contareceber.parcela  = transacaocartaoonline.parcelacontareceber ");
		sql.append(" where transacaocartaoonline.matricula = ? ");
		sql.append(" order by transacaocartaoonline.datatransacao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { matricula } );
		List<TransacaoCartaoOnlineVO> listaTransacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			TransacaoCartaoOnlineVO obj = montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
			obj.getContaReceberVO().setDataVencimento(tabelaResultado.getDate("datavencimentoContaReceber"));
			obj.getContaReceberVO().setParcela(tabelaResultado.getString("parcelaContaReceber"));
			obj.getContaReceberVO().setNossoNumero(tabelaResultado.getString("nossoNumero"));
			obj.setMensagemTransacao(tabelaResultado.getString("definicao"));
			listaTransacaoCartaoOnlineVOs.add(obj);
		}
		return listaTransacaoCartaoOnlineVOs;
	}
	
	
	@Override
	public void removerVinculoTransacaoCartaoOnlineMatriculaPeriodo( Integer matriculaPeriodo , UsuarioVO usuarioVO) throws Exception {			
			final String sql = "update transacaocartaoonline  set matriculaperiodo  = null  where  matriculaperiodo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
					sqlAlterar.setInt(1, matriculaPeriodo.intValue());
					return sqlAlterar;
				}
			});
		
		
	}
	
}
