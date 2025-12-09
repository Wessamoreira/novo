package negocio.facade.jdbc.financeiro;

import java.io.IOException;
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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CartaoCreditoDebitoRecorrenciaPessoaInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class CartaoCreditoDebitoRecorrenciaPessoa extends ControleAcesso implements CartaoCreditoDebitoRecorrenciaPessoaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;
	
	/**
	   * Local da chave privada no sistema de arquivos.
	   */
	  public static final String PATH_CHAVE_PRIVADA = "negocio/comuns/utilitarias/chave/private.key";
	  
	  /**
	   * Local da chave pública no sistema de arquivos.
	   */
	  public static final String PATH_CHAVE_PUBLICA = "negocio/comuns/utilitarias/chave/public.key";
	   

	public CartaoCreditoDebitoRecorrenciaPessoa() throws Exception {
		super();
		setIdEntidade("CartaoCreditoDebitoRecorrenciaPessoa");
	}

	public void setIdEntidade(String idEntidade) {
		CartaoCreditoDebitoRecorrenciaPessoa.idEntidade = idEntidade;
	}
	
	public void persistir(CartaoCreditoDebitoRecorrenciaPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, usuarioVO);
		}else {
			alterar(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final CartaoCreditoDebitoRecorrenciaPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			 * @author Carlos
			 */
			// CartaCobranca.incluir(getIdEntidade());
			validarDados(obj);
			final String sql = "INSERT INTO CartaoCreditoDebitoRecorrenciaPessoa( pessoa, matricula, responsavelFinanceiro, operadoraCartao, numeroCartao, numeroCartaoMascarado, nomeCartao, mesValidade, anoValidade, codigoSeguranca, formaPadraoDataBaseCartaoRecorrente, diaPagamentoPadrao, situacao, dataCadastro ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO().getCodigo())) {
						sqlInserir.setInt(1, obj.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
						sqlInserir.setString(2, obj.getMatriculaVO().getMatricula());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavelFinanceiro().getCodigo())) {
						sqlInserir.setInt(3, obj.getResponsavelFinanceiro().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO().getCodigo())) {
						sqlInserir.setInt(4, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getNumeroCartao());
					sqlInserir.setString(6, obj.getNumeroCartaoMascarado());
					sqlInserir.setString(7, obj.getNomeCartao());
					sqlInserir.setInt(8, obj.getMesValidade());
					sqlInserir.setInt(9, obj.getAnoValidade());
					sqlInserir.setString(10, obj.getCodigoSeguranca());
					sqlInserir.setString(11, obj.getFormaPadraoDataBaseCartaoRecorrente().name());
					sqlInserir.setInt(12, obj.getDiaPagamentoPadrao());
					sqlInserir.setString(13, obj.getSituacao().name());
					sqlInserir.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CartaoCreditoDebitoRecorrenciaPessoaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE CartaoCreditoDebitoRecorrenciaPessoa set pessoa=?, matricula=?, responsavelFinanceiro=?, operadoraCartao=?, numeroCartao=?, numeroCartaoMascarado=?, nomeCartao=?, mesValidade=?, anoValidade=?, codigoSeguranca=?, formaPadraoDataBaseCartaoRecorrente=?, diaPagamentoPadrao=?, situacao=?, dataCadastro=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO().getCodigo())) {
						sqlAlterar.setInt(1, obj.getPessoaVO().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
						sqlAlterar.setString(2, obj.getMatriculaVO().getMatricula());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavelFinanceiro().getCodigo())) {
						sqlAlterar.setInt(3, obj.getResponsavelFinanceiro().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO().getCodigo())) {
						sqlAlterar.setInt(4, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getNumeroCartao());
					sqlAlterar.setString(6, obj.getNumeroCartaoMascarado());
					sqlAlterar.setString(7, obj.getNomeCartao());
					sqlAlterar.setInt(8, obj.getMesValidade());
					sqlAlterar.setInt(9, obj.getAnoValidade());
					sqlAlterar.setString(10, obj.getCodigoSeguranca());
					sqlAlterar.setString(11, obj.getFormaPadraoDataBaseCartaoRecorrente().name());
					sqlAlterar.setInt(12, obj.getDiaPagamentoPadrao());
					sqlAlterar.setString(13, obj.getSituacao().name());
					sqlAlterar.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlAlterar.setInt(15, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	

	public static List<CartaoCreditoDebitoRecorrenciaPessoaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CartaoCreditoDebitoRecorrenciaPessoaVO> vetResultado = new ArrayList<CartaoCreditoDebitoRecorrenciaPessoaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return vetResultado;
	}

	public static CartaoCreditoDebitoRecorrenciaPessoaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavelFinanceiro"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.setNumeroCartao(dadosSQL.getString("numeroCartao"));
		obj.setNumeroCartaoMascarado(dadosSQL.getString("numeroCartaoMascarado"));
		obj.setNomeCartao(dadosSQL.getString("nomeCartao"));
		obj.setMesValidade(dadosSQL.getInt("mesValidade"));
		obj.setAnoValidade(dadosSQL.getInt("anoValidade"));
		obj.setCodigoSeguranca(dadosSQL.getString("codigoSeguranca"));
		if (dadosSQL.getString("situacao") != null) {
			obj.setSituacao(SituacaoEnum.valueOf(dadosSQL.getString("situacao")));
		}
		if (dadosSQL.getString("formapadraodatabasecartaorecorrente") != null) {
			obj.setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.valueOf(dadosSQL.getString("formapadraodatabasecartaorecorrente")));
		}
		obj.setDiaPagamentoPadrao(dadosSQL.getInt("diaPagamentoPadrao"));
		obj.setDataCadastro(dadosSQL.getTimestamp("dataCadastro"));
		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
			
		}
		return obj;
	}
	
	public void validarDadosDiaRecorrencia(FormaPagamentoNegociacaoRecebimentoVO formaCartaoCreditoVO) throws Exception {
		if (formaCartaoCreditoVO.getFormaPadraoPagamentoAutomaticoParcelaRecorrencia().equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
			if (formaCartaoCreditoVO.getDiaPadraoPagamentoRecorrencia().equals(0)) {
				throw new Exception("O campo DIA PADRÃO PAGAMENTO deve ser informado.");
			}
		}
		if (formaCartaoCreditoVO.getDiaPadraoPagamentoRecorrencia() > 31) {
			throw new Exception("DIA PADRÃO PAGAMENTO inválido.");
		}
	}
	
	public void validarDados(CartaoCreditoDebitoRecorrenciaPessoaVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getNomeCartao())) {
			throw new Exception("O campo NOME CARTÃO deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getNumeroCartao())) {
			throw new Exception("O campo NÚMERO CARTÃO deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getCodigoSeguranca())) {
			throw new Exception("O campo CÓDIGO SEGURANÇA deve ser informado.");
		}
		if (obj.getFormaPadraoDataBaseCartaoRecorrente().equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
			if (!Uteis.isAtributoPreenchido(obj.getDiaPagamentoPadrao())) {
				throw new Exception("O campo DIA PADRÃO PAGAMENTO deve ser informado.");
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoRecorrenciaCartaoCreditoDebito(ContaReceberVO contaReceberVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws IOException, Exception {
		
		for (FormaPagamentoNegociacaoRecebimentoVO formaCartaoCreditoVO : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			validarDadosDiaRecorrencia(formaCartaoCreditoVO);
			if (formaCartaoCreditoVO.getUtilizarCartaoComoPagamentoRecorrenteProximaParcela()) {
				String cartao = formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
				CartaoCreditoDebitoRecorrenciaPessoaVO obj = consultarPorMatriculaNumeroMascarado(contaReceberVO.getMatriculaAluno().getMatricula(), "XXXX.XXXX.XXXX."+cartao, usuarioVO);
				if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
					obj.setSituacao(SituacaoEnum.ATIVO);
					obj.setNomeCartao(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito());
					obj.setMesValidade(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade());
					obj.setAnoValidade(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade());
					
					obj.setNumeroCartaoMascarado("XXXX.XXXX.XXXX."+cartao);
					obj.setFormaPadraoDataBaseCartaoRecorrente(formaCartaoCreditoVO.getFormaPadraoPagamentoAutomaticoParcelaRecorrencia());
					if (obj.getFormaPadraoDataBaseCartaoRecorrente().equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
						obj.setDiaPagamentoPadrao(formaCartaoCreditoVO.getDiaPadraoPagamentoRecorrencia());
					} else {
						obj.setDiaPagamentoPadrao(0);
					}
				} else {
					obj.setSituacao(SituacaoEnum.ATIVO);
					obj.setPessoaVO(contaReceberVO.getPessoa());
					obj.setMatriculaVO(contaReceberVO.getMatriculaAluno());
					obj.setResponsavelFinanceiro(contaReceberVO.getResponsavelFinanceiro());
					obj.setOperadoraCartaoVO(formaCartaoCreditoVO.getOperadoraCartaoVO());

					obj.setNumeroCartao(Uteis.criptografarPorAlgoritimoRSA(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), getCaminhoChavePublica()));
					obj.setNumeroCartaoMascarado("XXXX.XXXX.XXXX."+cartao);
					obj.setCodigoSeguranca(Uteis.criptografarPorAlgoritimoRSA(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao(), getCaminhoChavePublica()));
					
					obj.setNomeCartao(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito());
					obj.setMesValidade(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade());
					obj.setAnoValidade(formaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade());

					obj.setFormaPadraoDataBaseCartaoRecorrente(formaCartaoCreditoVO.getFormaPadraoPagamentoAutomaticoParcelaRecorrencia());
					if (obj.getFormaPadraoDataBaseCartaoRecorrente().equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
						obj.setDiaPagamentoPadrao(formaCartaoCreditoVO.getDiaPadraoPagamentoRecorrencia());
					} else {
						obj.setDiaPagamentoPadrao(0);
					}
				}
				persistir(obj, usuarioVO);
			}
		}
	}
	
	@Override
	public CartaoCreditoDebitoRecorrenciaPessoaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;
	}
	
	public CartaoCreditoDebitoRecorrenciaPessoaVO consultarPorMatriculaNumeroMascarado(String matricula, String numeroCartaoMascarado, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		sb.append(" and numeroCartaoMascarado = '").append(numeroCartaoMascarado).append("' ");
		sb.append(" and situacao = 'ATIVO' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;
	}
	
	@Override
	public CartaoCreditoDebitoRecorrenciaPessoaVO consultarPorMatriculaPrimeiroCartaoCadastrado(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where matricula = '").append(matricula).append("' ");
		sb.append(" and situacao = 'ATIVO' ");
		sb.append(" order by cartaocreditodebitorecorrenciapessoa.codigo asc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;
	}
	
	
	@Override
	public List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarContasRecorrenciaAptasPagamento() {
		StringBuilder sb = new StringBuilder();
		Integer ultimoDiaMes = UteisData.getUltimoDiaMes(new Date());
		Integer diaAtual = UteisData.getDiaMesData(new Date());
		sb.append("select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where formapadraodatabasecartaorecorrente = 'DIA_FIXO' ");
		sb.append(" and cartaocreditodebitorecorrenciapessoa.situacao = 'ATIVO' ");
		if (ultimoDiaMes.equals(diaAtual)) {
			sb.append(" and case when diapagamentopadrao > ").append(ultimoDiaMes);
			sb.append(" then true else diapagamentopadrao = ").append(UteisData.getDiaMesData(new Date())).append(" end ");
		} else {
			sb.append(" and diapagamentopadrao = ").append(UteisData.getDiaMesData(new Date()));
		}
		sb.append(" and ").append(getSQLExistsContaReceber(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO));
		sb.append(" union ");
		sb.append(" select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where formapadraodatabasecartaorecorrente = 'VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO' ");
		sb.append(" and ").append(getSQLExistsContaReceber(FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO));
		sb.append(" and cartaocreditodebitorecorrenciapessoa.situacao = 'ATIVO' ");
		sb.append(" order by dataCadastro ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs = new ArrayList<CartaoCreditoDebitoRecorrenciaPessoaVO>(0);
		while (tabelaResultado.next()) {
			CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getResponsavelFinanceiro().setCodigo(tabelaResultado.getInt("responsavelFinanceiro"));
			obj.getOperadoraCartaoVO().setCodigo(tabelaResultado.getInt("operadoraCartao"));
			obj.setNumeroCartao(tabelaResultado.getString("numeroCartao"));
			obj.setNumeroCartaoMascarado(tabelaResultado.getString("numeroCartaoMascarado"));
			obj.setNomeCartao(tabelaResultado.getString("nomeCartao"));
			obj.setMesValidade(tabelaResultado.getInt("mesValidade"));
			obj.setAnoValidade(tabelaResultado.getInt("anoValidade"));
			obj.setCodigoSeguranca(tabelaResultado.getString("codigoSeguranca"));
			if (tabelaResultado.getString("situacao") != null) {
				obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
			}
			if (tabelaResultado.getString("formapadraodatabasecartaorecorrente") != null) {
				obj.setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.valueOf(tabelaResultado.getString("formapadraodatabasecartaorecorrente")));
			}
			obj.setDiaPagamentoPadrao(tabelaResultado.getInt("diaPagamentoPadrao"));
			listaCartaoCreditoDebitoRecorrenciaPessoaVOs.add(obj);
		}
		return listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}
	
	
	public StringBuilder getSQLExistsContaReceber(FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente) {
		StringBuilder sb = new StringBuilder();
		sb.append(" exists (");
		sb.append(" select contareceber.codigo from contareceber ");
		sb.append(" where contareceber.matriculaaluno = cartaocreditodebitorecorrenciapessoa.matricula ");
		if (formaPadraoDataBaseCartaoRecorrente.equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
			sb.append(" and contareceber.datavencimento >= '").append(Uteis.getDataJDBC(Uteis.getDataPrimeiroDiaMes(new Date()))).append("' ");
			sb.append(" and contareceber.datavencimento <= '").append(Uteis.getDataJDBC(Uteis.getDataUltimoDiaMes(new Date()))).append("' ");
		} else {
			sb.append(" and case when contareceber.datalimiteprimeirafaixadescontos is not null and contareceber.datalimiteprimeirafaixadescontos::date <= contareceber.datavencimento::date  ");
			sb.append(" then contareceber.datalimiteprimeirafaixadescontos::date = current_date ");
			sb.append(" else contareceber.datavencimento::date = current_date end ");
		}
		sb.append(" and ((contareceber.tipoorigem = 'MDI') ");
		sb.append(" or  (contareceber.tipoorigem ='MEN' and  exists (select mp.codigo  from matriculaperiodo mp where mp.codigo = contareceber.matriculaperiodo  and mp.situacaomatriculaperiodo <> 'PR' and mp.situacaomatriculaperiodo <> 'PC' )) ");
		sb.append(" or (contareceber.tipoorigem ='NCR' and  exists (select ncr.codigo from negociacaocontareceber ncr  where ncr.codigo = contareceber.codigorenegociacao and ncr.permitirpagamentocartaocreditovisaoaluno ))) ");
		sb.append(" and contareceber.situacao  = 'AR' ");
		sb.append(") ");
		return sb;
	}
	
	@Override
	public List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarContasRecorrenciaPorMatricula(String matricula, Boolean manterCriptografiaCartao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select cartaocreditodebitorecorrenciapessoa.* from cartaocreditodebitorecorrenciapessoa ");
		sb.append(" where cartaocreditodebitorecorrenciapessoa.matricula = '").append(matricula).append("' ");
		sb.append(" and cartaocreditodebitorecorrenciapessoa.situacao = 'ATIVO' ");
		sb.append(" order by cartaocreditodebitorecorrenciapessoa.numeroCartao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs = new ArrayList<CartaoCreditoDebitoRecorrenciaPessoaVO>(0);
		while (tabelaResultado.next()) {
			CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getResponsavelFinanceiro().setCodigo(tabelaResultado.getInt("responsavelFinanceiro"));
			obj.getOperadoraCartaoVO().setCodigo(tabelaResultado.getInt("operadoraCartao"));
			if (!manterCriptografiaCartao) {
				obj.setNumeroCartao(Uteis.decriptografarPorAlgoritimoRSA(tabelaResultado.getString("numeroCartao"), getCaminhoChavePrivada()) );
			} else {
				obj.setNumeroCartao(tabelaResultado.getString("numeroCartao"));
			}
			
			obj.setNumeroCartaoMascarado(tabelaResultado.getString("numeroCartaoMascarado"));
			obj.setNomeCartao(tabelaResultado.getString("nomeCartao"));
			obj.setMesValidade(tabelaResultado.getInt("mesValidade"));
			obj.setAnoValidade(tabelaResultado.getInt("anoValidade"));
			if (!manterCriptografiaCartao) {
				obj.setCodigoSeguranca(Uteis.decriptografarPorAlgoritimoRSA(tabelaResultado.getString("codigoSeguranca"), getCaminhoChavePrivada()));
			} else {
				obj.setCodigoSeguranca(tabelaResultado.getString("codigoSeguranca"));
			}
			if (tabelaResultado.getString("situacao") != null) {
				obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
			}
			if (tabelaResultado.getString("formapadraodatabasecartaorecorrente") != null) {
				obj.setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.valueOf(tabelaResultado.getString("formapadraodatabasecartaorecorrente")));
			}
			obj.setDiaPagamentoPadrao(tabelaResultado.getInt("diaPagamentoPadrao"));
			listaCartaoCreditoDebitoRecorrenciaPessoaVOs.add(obj);
		}
		return listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPorCodigo(final Integer codigo, final SituacaoEnum situacao, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE cartaocreditodebitorecorrenciapessoa set situacao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacao.getName());
					sqlAlterar.setInt(++i, codigo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void adicionarCartaoCreditoDebitoRecorrenciaPessoa(CartaoCreditoDebitoRecorrenciaPessoaVO obj, List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		persistir(obj, usuarioVO);
		int index = 0;
		for (CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO : listaCartaoCreditoDebitoRecorrenciaPessoaVOs) {
			if (cartaoCreditoDebitoRecorrenciaPessoaVO.getNumeroCartao().equals(obj.getNumeroCartao())) {
				listaCartaoCreditoDebitoRecorrenciaPessoaVOs.set(index, obj);
				return;
			}
			index++;
		}
		listaCartaoCreditoDebitoRecorrenciaPessoaVOs.add(obj);
	}

	@Override
	public void removerCartaoCreditoDebitoRecorrenciaPessoa(CartaoCreditoDebitoRecorrenciaPessoaVO obj, List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs) {
		int index = 0;
		for (CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO : listaCartaoCreditoDebitoRecorrenciaPessoaVOs) {
			if (cartaoCreditoDebitoRecorrenciaPessoaVO.getNumeroCartao().equals(obj.getNumeroCartao())) {
				listaCartaoCreditoDebitoRecorrenciaPessoaVOs.remove(index);
				return;
			}
			index++;
		}
	}
	
	@Override
	public void inicializarDadosAlunoCartaoCreditoDebito(CartaoCreditoDebitoRecorrenciaPessoaVO obj, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		String cartao = obj.getNumeroCartao().substring(obj.getNumeroCartao().length() - 4);
		obj.setSituacao(SituacaoEnum.ATIVO);
		obj.setPessoaVO(matriculaVO.getAluno());
		obj.setMatriculaVO(matriculaVO);
		obj.setNumeroCartao(Uteis.criptografarPorAlgoritimoRSA(obj.getNumeroCartao(), getCaminhoChavePublica()));
		obj.setNumeroCartaoMascarado("XXXX.XXXX.XXXX."+cartao);
		obj.setCodigoSeguranca(Uteis.criptografarPorAlgoritimoRSA(obj.getCodigoSeguranca(), getCaminhoChavePublica()));
	}
	
	
	@Override
	public String getCaminhoChavePublica() throws Exception {
		String caminhoPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		String caminhoChavePublica = caminhoPath.substring(1, caminhoPath.lastIndexOf('/') + 1).concat(PATH_CHAVE_PUBLICA);
		return caminhoChavePublica;
	}
	
	@Override
	public String getCaminhoChavePrivada() throws Exception {
		String caminhoPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		String caminhoChavePrivada = caminhoPath.substring(1, caminhoPath.lastIndexOf('/') + 1).concat(PATH_CHAVE_PRIVADA);
		return caminhoChavePrivada;
	}
	
	@Override
	public List<CartaoCreditoDebitoRecorrenciaPessoaVO> consultarPorFiltrosMapaPendenciaCartaoCreditoRecorrenciaDCC(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<OperadoraCartaoVO> operadoraCartaoVOs, String matricula, PessoaVO responsavelFinanceiro, TipoPessoa tipoPessoa, String nomeCartao, Integer mesValidade, String anoValidade, FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente, Integer diaPadraoPagamento, String numeroFinalCartaoCredito, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cartaocreditodebitorecorrenciapessoa.codigo, cartaocreditodebitorecorrenciapessoa.matricula, cartaocreditodebitorecorrenciapessoa.numerocartaomascarado, ");
		sql.append(" cartaocreditodebitorecorrenciapessoa.nomecartao, cartaocreditodebitorecorrenciapessoa.mesvalidade, cartaocreditodebitorecorrenciapessoa.anovalidade, ");
		sql.append(" cartaocreditodebitorecorrenciapessoa.formapadraodatabasecartaorecorrente, cartaocreditodebitorecorrenciapessoa.diapagamentopadrao, ");
		sql.append(" cartaocreditodebitorecorrenciapessoa.situacao , cartaocreditodebitorecorrenciapessoa.dataCadastro, ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sql.append(" operadoracartao.codigo as \"operadoracartao.codigo\", operadoracartao.nome as \"operadoracartao.nome\" ");
		sql.append(" from cartaocreditodebitorecorrenciapessoa  ");
		sql.append(" inner join matricula on matricula.matricula = cartaocreditodebitorecorrenciapessoa.matricula ");
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			sql.append(" inner join pessoa on pessoa.codigo = cartaocreditodebitorecorrenciapessoa.pessoa ");
		} else {
			sql.append(" inner join pessoa on pessoa.codigo = cartaocreditodebitorecorrenciapessoa.responsavelFinanceiro ");
		}
		sql.append(" inner join operadoracartao on operadoracartao.codigo = cartaocreditodebitorecorrenciapessoa.operadoracartao ");
		sql.append(" WHERE cartaocreditodebitorecorrenciapessoa.situacao = '").append(SituacaoEnum.ATIVO).append("' ");
		if (!operadoraCartaoVOs.isEmpty()) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.operadoraCartao in (");
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
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(responsavelFinanceiro.getCodigo())) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.responsavelFinanceiro = ").append(responsavelFinanceiro.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(nomeCartao)) {
			sql.append(" and upper(cartaocreditodebitorecorrenciapessoa.nomeCartao) ilike = '").append(nomeCartao.toUpperCase()).append("%' ");
		}
		if (Uteis.isAtributoPreenchido(mesValidade)) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.mesValidade = ").append(mesValidade);
		}
		if (Uteis.isAtributoPreenchido(anoValidade)) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.anoValidade = ").append(Integer.parseInt(anoValidade));
		}
		if (Uteis.isAtributoPreenchido(formaPadraoDataBaseCartaoRecorrente) && !formaPadraoDataBaseCartaoRecorrente.equals(FormaPadraoDataBaseCartaoRecorrenteEnum.TODOS)) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.formaPadraoDataBaseCartaoRecorrente = '").append(formaPadraoDataBaseCartaoRecorrente).append("' ");
		}
		if (Uteis.isAtributoPreenchido(diaPadraoPagamento) && formaPadraoDataBaseCartaoRecorrente.equals(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO)) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.diaPagamentoPadrao = ").append(diaPadraoPagamento);
		}
		if (Uteis.isAtributoPreenchido(numeroFinalCartaoCredito)) {
			sql.append(" and cartaocreditodebitorecorrenciapessoa.numerocartaomascarado ilike '%").append(numeroFinalCartaoCredito).append("%' ");
		}
		sql.append(" order by pessoa.nome ");
		List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs = new ArrayList<CartaoCreditoDebitoRecorrenciaPessoaVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			CartaoCreditoDebitoRecorrenciaPessoaVO obj = new CartaoCreditoDebitoRecorrenciaPessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.setNumeroCartaoMascarado(tabelaResultado.getString("numerocartaomascarado"));
			obj.setNomeCartao(tabelaResultado.getString("nomecartao"));
			obj.setMesValidade(tabelaResultado.getInt("mesvalidade"));
			obj.setAnoValidade(tabelaResultado.getInt("anoValidade"));
			obj.setDataCadastro(tabelaResultado.getTimestamp("dataCadastro"));
			if (tabelaResultado.getString("formapadraodatabasecartaorecorrente") != null) {
				obj.setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.valueOf(tabelaResultado.getString("formapadraodatabasecartaorecorrente")));
			}
			obj.setDiaPagamentoPadrao(tabelaResultado.getInt("diapagamentopadrao"));
			obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoaVO().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getOperadoraCartaoVO().setCodigo(tabelaResultado.getInt("operadoracartao.codigo"));
			obj.getOperadoraCartaoVO().setNome(tabelaResultado.getString("operadoracartao.nome"));
			listaCartaoCreditoDebitoRecorrenciaPessoaVOs.add(obj);
		}
		return listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}	
	
}
