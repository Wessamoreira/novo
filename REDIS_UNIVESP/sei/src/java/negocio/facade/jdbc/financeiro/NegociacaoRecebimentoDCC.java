package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
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
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.NegociacaoRecebimentoDCCInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class NegociacaoRecebimentoDCC extends ControleAcesso implements NegociacaoRecebimentoDCCInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NegociacaoRecebimentoDCC() throws Exception {
		super();
		setIdEntidade("NegociacaoRecebimento");
	}
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return NegociacaoRecebimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NegociacaoRecebimentoDCC.idEntidade = idEntidade;
	}

	@Override
	/**
	 * Não poderá ser recebimento juntamente com outra forma de recebimento que não seja cartão com DCC, ou seja, não poderá receber juntamente com
	 * dinheiro, cheque, cartão que não seja com dcc e outras formas de recebimento pois a conta ficará com uma previsão de recebimento e o sistema
	 * não trabalha com recebimento parcial.
	 * 
	 * @param negociacaoRecebimentoVO
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @throws Exception
	 */
	public void validarRecebimentoPossuiMaisDeUmaFormaQuandoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException {
		if (!(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().size() == 0)) {
			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					if (formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
						continue;
					} else {
						ConsistirException ce = new ConsistirException();
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NegociacaoRecebimento_recebimentoNaoAutorizadoQuandoHouverDCCEOutraFormaDePagamento"));
						throw ce;
					}
				}
			}
		}
	}

	/**
	 * Em um recebimento em DCC não poderá ser realizado com contas vencidas, as mesmas deverão ser negociação e depois recebidas.
	 * 
	 * @param negociacaoRecebimentoVO
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @throws Exception
	 */
	public void validarRecebimentoContaVencidaFormaPagamentoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException {
		if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
			for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
				if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getDataVencimento().compareTo(new Date()) < 0) {
					ConsistirException ce = new ConsistirException();
					ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NegociacaoRecebimento_mensagemContaVencidaDCC"));
					throw ce;
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoRecebimentoDCC.incluir(getIdEntidade(), verificarAcesso, usuario);
			NegociacaoRecebimentoVO.validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO negociacaoRecebimentodcc( data, valorTotalRecebimento, valorTotal, responsavel, contaCorrenteCaixa, pessoa, tipoPessoa, matricula, unidadeEnsino, parceiro, observacao, fornecedor, pagamentocomdcc, dataregistro) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setDouble(2, obj.getValorTotalRecebimento().doubleValue());
					sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
					if (!obj.getResponsavel().getCodigo().equals(0)) {
						sqlInserir.setInt(4, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getContaCorrenteCaixa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getContaCorrenteCaixa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getTipoPessoa());
					if (obj.getTipoAluno().booleanValue() || obj.getTipoFuncionario().booleanValue()) {
						sqlInserir.setString(8, obj.getMatricula());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setDouble(9, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getParceiroVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getParceiroVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setString(11, obj.getObservacao());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setBoolean(13, obj.getPagamentoComDCC());
					sqlInserir.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().incluirFormaPagamentoNegociacaoRecebimentos(obj, verificarAcesso, usuario);
			getFacadeFactory().getContaReceberNegociacaoRecebimentoDCCFacade().incluirContaReceberNegociacaoRecebimentos(obj.getCodigo(), obj.getContaReceberNegociacaoRecebimentoVOs(), configuracaoFinanceiro, usuario);
			atribuirPagamentoDCCContaReceber(obj.getContaReceberNegociacaoRecebimentoVOs());
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	public void calcularValorQuandoTipoRecebimentoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException {
		validarValorRecebimentoTipoDCC(negociacaoRecebimentoVO, formaPagamentoNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.setValorTotalRecebimento(0.00);
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
				negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() + formaPagamentoNegociacaoRecebimentoVO2.getValorRecebimento());
			}
		}
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
	}
	
	public void validarValorRecebimentoTipoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException {
		ConsistirException ce = new ConsistirException();
		if(Uteis.arrendondarForcando2CasasDecimais(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberNegociacaoRecebimentoVO().getValorTotal()) < Uteis.arrendondarForcando2CasasDecimais(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento())) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimento_valorRecebimentoMaiorQueOValorContaReceberRecebimento"));
		}
		Double valorTotalRecebimentoFormasPagamento = 0.00;
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
			if(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberVO().getCodigo().equals(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo())
					&& contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().size() > 1) {
				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					valorTotalRecebimentoFormasPagamento += formaPagamentoNegociacaoRecebimentoVO2.getValorRecebimento();
				}				
				if(Uteis.arrendondarForcando2CasasDecimais(valorTotalRecebimentoFormasPagamento) > Uteis.arrendondarForcando2CasasDecimais(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberNegociacaoRecebimentoVO().getValorTotal())) {
					ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimento_valorSomadoDiferenteDaContaReceber").replace("{0}", formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberNegociacaoRecebimentoVO().getContaReceber().getNrDocumento()));
					break;
				}
			}
		}
		if(!ce.getListaMensagemErro().isEmpty()) {
			throw ce;
		}
	}
	
	public void validarFormasPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO) throws ConsistirException {
		for (Iterator iterator = negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().iterator(); iterator.hasNext();) {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) iterator.next();
			validarValorRecebimentoTipoDCC(negociacaoRecebimentoVO, formaPagamentoNegociacaoRecebimentoVO);
			if(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento().equals(0.00)) {
				iterator.remove();
			}
		}
	}
	
//	@Override
//	public void adicionarFormaPagamentoNegociacaoRecebimentoCartaoCreditoQuandoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
//		validarRecebimentoPossuiMaisDeUmaFormaQuandoDCC(negociacaoRecebimentoVO, formaPagamentoNegociacaoRecebimentoVO);
//		validarRecebimentoContaVencidaFormaPagamentoDCC(negociacaoRecebimentoVO, formaPagamentoNegociacaoRecebimentoVO);
//		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
//			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 = new FormaPagamentoNegociacaoRecebimentoVO();
//			formaPagamentoNegociacaoRecebimentoVO2 = formaPagamentoNegociacaoRecebimentoVO.clone();
//			if (contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
//				formaPagamentoNegociacaoRecebimentoVO2.setValorRecebimento(contaReceberNegociacaoRecebimentoVO.getValorTotal());
//			} 
//			negociacaoRecebimentoVO.setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotalRecebimento() + formaPagamentoNegociacaoRecebimentoVO2.getValorRecebimento()));
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberVO(new ContaReceberVO());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberVO().setCodigo(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberNegociacaoRecebimentoVO(new ContaReceberNegociacaoRecebimentoVO());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberNegociacaoRecebimentoVO(contaReceberNegociacaoRecebimentoVO);
//			formaPagamentoNegociacaoRecebimentoVO2.setConfiguracaoRecebimentoCartaoOnlineVO(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO());
//			formaPagamentoNegociacaoRecebimentoVO2.setOperadoraCartaoVO(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
//			formaPagamentoNegociacaoRecebimentoVO2.setDataPrevisaCartao(contaReceberNegociacaoRecebimentoVO.getContaReceber().getDataLimiteAplicacaoDesconto());
//			formaPagamentoNegociacaoRecebimentoVO2.setQtdeParcelasCartaoCredito(1);
//			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroParcela(1 + "/" + formaPagamentoNegociacaoRecebimentoVO2.getQtdeParcelasCartaoCredito());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(formaPagamentoNegociacaoRecebimentoVO2.getValorRecebimento());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataEmissao(new Date());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataVencimento(contaReceberNegociacaoRecebimentoVO.getContaReceber().getDataLimiteAplicacaoDesconto());
//			formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(formaPagamentoNegociacaoRecebimentoVO2);
//			contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO2);
//			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO2);
//		}
//	}
	
	private NegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception {
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getPessoa().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getPessoa().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getFornecedor().setCodigo(dadosSQL.getInt("Fornecedor.codigo"));
		obj.getFornecedor().setNome(dadosSQL.getString("Fornecedor.nome"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("Parceiro.codigo"));
		obj.getParceiroVO().setNome(dadosSQL.getString("Parceiro.nome"));
		obj.getParceiroVO().setCNPJ(dadosSQL.getString("Parceiro.cnpj"));
		obj.getParceiroVO().setCPF(dadosSQL.getString("Parceiro.cpf"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getResponsavel().setNome(dadosSQL.getString("Responsavel.nome"));
		obj.setValorTotalRecebimento(new Double(dadosSQL.getDouble("valorTotalRecebimento")));
		obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		obj.getContaCorrenteCaixa().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteCaixa")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, usuarioLogado));
		obj.setPagamentoComDCC(dadosSQL.getBoolean("pagamentocomdcc"));
		if (obj.getTipoPessoa().trim().equals("") && !obj.getMatricula().trim().isEmpty()) {
			obj.setTipoPessoa("AL");
		}
		obj.setNovoObj(Boolean.FALSE);	
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.getContaReceberNegociacaoRecebimentoVOs().addAll(getFacadeFactory().getContaReceberNegociacaoRecebimentoDCCFacade().consultarPorNegociacaoRecebimentoDCC(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioLogado));
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : obj.getContaReceberNegociacaoRecebimentoVOs()) {
			obj.getFormaPagamentoNegociacaoRecebimentoVOs().addAll(contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs());
		}
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT negociacaorecebimentodcc.*, parceiro.codigo as \"Parceiro.codigo\", parceiro.nome as \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\", pessoa.nome AS \"Pessoa.nome\", pessoa.cpf AS \"Pessoa.cpf\", usuario.nome AS \"Responsavel.nome\",");
		sql.append(" fornecedor.nome as \"fornecedor.nome\", fornecedor.codigo as \"fornecedor.codigo\" FROM negociacaorecebimentodcc");
		sql.append(" LEFT JOIN fornecedor ON (fornecedor.codigo = negociacaorecebimentodcc.fornecedor) ");
		sql.append(" LEFT JOIN pessoa ON (pessoa.codigo = negociacaorecebimentodcc.pessoa) ");
		sql.append(" LEFT JOIN parceiro ON (parceiro.codigo = negociacaorecebimentodcc.parceiro) ");
		sql.append(" LEFT JOIN usuario ON (usuario.codigo = negociacaorecebimentodcc.responsavel) ");
		sql.append(" WHERE negociacaorecebimentodcc.codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, configuracaoFinanceiroVO, usuarioLogado));
		}
		return new NegociacaoRecebimentoVO();
	}
	
	public void atribuirPagamentoDCCContaReceber(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs) throws Exception {
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : contaReceberNegociacaoRecebimentoVOs) {
			contaReceberNegociacaoRecebimentoVO.getContaReceber().setPagoComDCC(true);
			getFacadeFactory().getContaReceberFacade().alterarSituacaoPagoDCCTrue(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo());
		}
	}
}
