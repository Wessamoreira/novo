package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DetalhamentoValorContaVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.enumerador.OrigemDetalhamentoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.DetalhamentoValorContaInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class DetalhamentoValorConta extends ControleAcesso implements DetalhamentoValorContaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4724572396798317549L;
	protected static String idEntidade = "DetalhamentoValorConta";

	private void validarDados(DetalhamentoValorContaVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigoOrigem()), "O campo Código Origem (DetalhamentoValorConta) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoCentroResultadoOrigemDetalheEnum()), "O campo Tipo Centro Resultado Origem (DetalhamentoValorConta) deve ser informado.");
		// Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValor()), "O campo Valor (DetalhamentoValorConta) deve ser informado.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<DetalhamentoValorContaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (DetalhamentoValorContaVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}

	private void persistir(DetalhamentoValorContaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	private void incluir(final DetalhamentoValorContaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoOrigem.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO DetalhamentoValorConta (origemDetalhamentoConta, codigoorigem, valor, tipoCentroResultadoOrigemDetalhe, ");
			sql.append("    dataLimiteAplicacaoDesconto, utilizado, faixaDescontoProgressivo, codOrigemDoTipoDetalhe, nomeOrigemDoTipoDetalhe, ordemApresentacao, tipoValor, valortipovalor )  ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getOrigemDetalhamentoConta(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoOrigem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoCentroResultadoOrigemDetalheEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataLimiteAplicacaoDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isUtilizado(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFaixaDescontoProgressivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodOrigemDoTipoDetalhe(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeOrigemDoTipoDetalhe(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOrdemApresentacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorTipoValor(), ++i, sqlInserir);
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
			throw new StreamSeiException(e);
		}
	}

	private void alterar(final DetalhamentoValorContaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoOrigem.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE DetalhamentoValorConta ");
			sql.append(" SET origemDetalhamentoConta=?, codigoorigem = ?, valor=?, tipoCentroResultadoOrigemDetalhe=?, ");
			sql.append(" dataLimiteAplicacaoDesconto=?, utilizado=?, faixaDescontoProgressivo=?, codOrigemDoTipoDetalhe=?, ");
			sql.append(" nomeOrigemDoTipoDetalhe=?, ordemApresentacao=?, tipoValor = ?, valortipovalor = ? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getOrigemDetalhamentoConta(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigoOrigem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoCentroResultadoOrigemDetalheEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataLimiteAplicacaoDesconto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isUtilizado(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFaixaDescontoProgressivo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodOrigemDoTipoDetalhe(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNomeOrigemDoTipoDetalhe(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOrdemApresentacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorTipoValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorTipoCentroResultadoOrigemDetalhePorOrigemDetalhamentoConta(Integer codigoContaReceber, TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalhe,  OrigemDetalhamentoContaEnum origemDetalhamentoConta,  UsuarioVO usuario) throws Exception {
		final String sql = "DELETE FROM  DetalhamentoValorConta  WHERE tipoCentroResultadoOrigemDetalhe=? and origemDetalhamentoConta =? and codigoorigem = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { tipoCentroResultadoOrigemDetalhe.name(), origemDetalhamentoConta.name(), codigoContaReceber });
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCentroResultadoOrigemDetalhe(final DetalhamentoValorContaVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE DetalhamentoValorConta set valor=?,  dataLimiteAplicacaoDesconto=?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataLimiteAplicacaoDesconto(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}

	private PlanoFinanceiroAlunoDescricaoDescontosVO obterPlanoFinanceiroAlunoDescricaoAtual(ContaReceberVO contaReceberVO, Date dataRecebimentoContaAReceber, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		if (contaReceberVO.getRealizandoRecebimento() || listaDescontosAplicavesContaReceber.isEmpty()) {
			contaReceberVO.setRealizandoRecebimento(false);
			listaDescontosAplicavesContaReceber.clear();
			listaDescontosAplicavesContaReceber.addAll(ContaReceber.gerarListaPlanoFinanceiroAlunoDescricaoDesconto(contaReceberVO, dataRecebimentoContaAReceber, conf.getUsaDescontoCompostoPlanoDesconto(), null, usuario, conf));
			contaReceberVO.setRealizandoRecebimento(true);
		}
		PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual = null;
		for (PlanoFinanceiroAlunoDescricaoDescontosVO pfadd : listaDescontosAplicavesContaReceber) {
			if (//Regra criado para matriculas geradas com calendario de mensalidade retroativo e ja vem com 100% desconto    
				(contaReceberVO.getSituacaoEQuitada() && contaReceberVO.getValorRecebido().equals(0.0) && contaReceberVO.getValorDescontoProgressivo() > 0 &&  contaReceberVO.getValorDescontoProgressivo().equals(pfadd.getValorDescontoProgressivo())) 
				|| (contaReceberVO.getSituacaoEQuitada() && contaReceberVO.getValorRecebido().equals(0.0) && contaReceberVO.getValorDescontoInstituicao() > 0 &&  contaReceberVO.getValorDescontoInstituicao().equals(pfadd.getValorDescontoInstituicao()) && contaReceberVO.isContaQuitadaPossuiPlanoDescontoComValidade() )
				|| pfadd.getIsAplicavelDataParaQuitacao(null, dataRecebimentoContaAReceber)) {
				pfaddAtual = pfadd;
				break;
			}
		}
		if (pfaddAtual == null && Uteis.isAtributoPreenchido(listaDescontosAplicavesContaReceber)) {
			pfaddAtual = listaDescontosAplicavesContaReceber.get(listaDescontosAplicavesContaReceber.size() - 1);
		}
		return pfaddAtual;
	}
	
	private PlanoFinanceiroAlunoDescricaoDescontosVO obterPlanoFinanceiroAlunoDescricaoPlanoDescontoValido(Integer keyPlanoDesconto, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber) throws Exception {		
		for (int i = listaDescontosAplicavesContaReceber.size()-1; i >= 0 ; i--) {
			PlanoFinanceiroAlunoDescricaoDescontosVO pfadd = listaDescontosAplicavesContaReceber.get(i);
			if(pfadd.getListaDescontosPlanoDesconto().containsKey(keyPlanoDesconto)){
				return pfadd;
			}
		}
//		throw new StreamSeiException("Não foi encontrado o plano de desconto de código "+ keyPlanoDesconto + " para o detalhamento do valor da conta");
		return null;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoDeAtualizacaoDetalhamentoValorContaReceber(ContaReceberVO contaReceberVO, Date dataBaixaContaAReceber, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber = new ArrayList<>();
		List<DetalhamentoValorContaVO> detalhamentoValorContaVOs = new ArrayList<>();
		listaDescontosAplicavesContaReceber.addAll(contaReceberVO.getListaDescontosAplicavesContaReceber());
		PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual = obterPlanoFinanceiroAlunoDescricaoAtual(contaReceberVO, dataBaixaContaAReceber, listaDescontosAplicavesContaReceber, conf, usuario);
		geracaoCentroResultadoOrigemDetalhePadraoContaReceber(detalhamentoValorContaVOs, contaReceberVO, dataBaixaContaAReceber, pfaddAtual, listaDescontosAplicavesContaReceber, usuario);
		geracaoCentroResultadoOrigemDetalhePadraoPorPlanoFinanceiroAlunoDescricaoDesconto(contaReceberVO, pfaddAtual, dataBaixaContaAReceber, listaDescontosAplicavesContaReceber, detalhamentoValorContaVOs, conf, usuario);
		geracaoCentroResultadoOrigemDetalhePadraoPorContaReceberNegociada(contaReceberVO, dataBaixaContaAReceber, usuario, detalhamentoValorContaVOs, listaDescontosAplicavesContaReceber.get(0), conf);

		
		for (DetalhamentoValorContaVO crod : contaReceberVO.getDetalhamentoValorContaVOs()) {
			if (crod.getTipoCentroResultadoOrigemDetalheEnum().isAcrescimo() && !crod.getValor().equals(contaReceberVO.getAcrescimo())) {
				crod.setValor(contaReceberVO.getAcrescimo());
			}
			if (crod.getTipoCentroResultadoOrigemDetalheEnum().isMulta() && !crod.getValor().equals(contaReceberVO.getMulta())) {
				crod.setValor(contaReceberVO.getMulta());
			}
			if (crod.getTipoCentroResultadoOrigemDetalheEnum().isJuro() && !crod.getValor().equals(contaReceberVO.getJuro())) {
				crod.setValor(contaReceberVO.getJuro());
			}
			if ((crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoProgressivo()
					|| crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoInstituicao()
					|| crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoManual())
					&& Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto())
					&& UteisData.getCompareData(dataBaixaContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0) {
				crod.setUtilizado(false);
			}
		}
		
		if (!Uteis.isAtributoPreenchido(contaReceberVO.getDetalhamentoValorContaVOs())) {
			contaReceberVO.getDetalhamentoValorContaVOs().addAll(detalhamentoValorContaVOs);
		}else {
			removerDetalhamentoValorNaoExistenteJob(contaReceberVO, detalhamentoValorContaVOs, usuario);
			for (DetalhamentoValorContaVO detalhamentoValorContaVO : detalhamentoValorContaVOs) {
				adicionarCentroResultadoOrigemDetalhe(contaReceberVO.getDetalhamentoValorContaVOs(), detalhamentoValorContaVO, usuario);
			}
		}
		
		if (Uteis.isAtributoPreenchido(contaReceberVO)) {
			persistir(contaReceberVO.getDetalhamentoValorContaVOs(), false, usuario);
		}
	}
	
	
	private void removerDetalhamentoValorNaoExistenteJob(ContaReceberVO contaReceberVO, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, UsuarioVO usuario) {
		Iterator<DetalhamentoValorContaVO> i = contaReceberVO.getDetalhamentoValorContaVOs().iterator();
		while (i.hasNext()) {
			DetalhamentoValorContaVO objExistente = i.next();
			if (!listaTempCentroResultadoOrigemDetalheVO.stream().anyMatch(p -> p.equalsCentroResultadoOrigemDetalhe(objExistente))) {
				i.remove();
				excluir(objExistente,usuario);
			}
		}
	}

	private void excluir(DetalhamentoValorContaVO obj, UsuarioVO usuario) {
		final String sql = "DELETE FROM  DetalhamentoValorConta  WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarGeracaoCentroResultadoOrigemDetalhePadrao(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		List<DetalhamentoValorContaVO> detalhamentoValorContaVOs = new ArrayList<>();
		Date dataRecebimentoContaAReceber = null;
		if (contaReceberVO.getSituacao().equals("NE")) {
			dataRecebimentoContaAReceber = getFacadeFactory().getNegociacaoContaReceberFacade().consultaDataNegociacaoContaReceberPorContaReceberUnica(contaReceberVO.getCodigo(), usuario);
		} else if (contaReceberVO.getSituacao().equals("RE")) {
			dataRecebimentoContaAReceber = getFacadeFactory().getNegociacaoRecebimentoFacade().consultaDataRecebimentoPorContaReceberUnica(contaReceberVO.getCodigo(), usuario);
		}
		if (dataRecebimentoContaAReceber == null) {
			dataRecebimentoContaAReceber = new Date();
		}
		
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber = new ArrayList<>();
		listaDescontosAplicavesContaReceber.addAll(contaReceberVO.getListaDescontosAplicavesContaReceber());
		PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual = obterPlanoFinanceiroAlunoDescricaoAtual(contaReceberVO, dataRecebimentoContaAReceber, listaDescontosAplicavesContaReceber, conf, usuario);
		//Regra criado para atender os casos onde a conta receber esta recebida com o valor 0 e existe desconto com validade 
		//que nao foram atendidos por serem retroativos Pedro Andrade 05/03/2020. 
		if((contaReceberVO.getSituacaoEQuitada() 
				&& contaReceberVO.getValorRecebido().equals(0.0) 
				&& ((contaReceberVO.getValorDescontoProgressivo() > 0) || (contaReceberVO.getValorDescontoInstituicao() > 0 && contaReceberVO.isContaQuitadaPossuiPlanoDescontoComValidade())))
				&& pfaddAtual != null
				&& pfaddAtual.getDataLimiteAplicacaoDesconto() != null
				) {
			dataRecebimentoContaAReceber = pfaddAtual.getDataLimiteAplicacaoDesconto() ;
		}
		geracaoCentroResultadoOrigemDetalhePadraoContaReceber(detalhamentoValorContaVOs, contaReceberVO, dataRecebimentoContaAReceber, pfaddAtual, listaDescontosAplicavesContaReceber, usuario);
		geracaoCentroResultadoOrigemDetalhePadraoPorPlanoFinanceiroAlunoDescricaoDesconto(contaReceberVO, pfaddAtual, dataRecebimentoContaAReceber, listaDescontosAplicavesContaReceber, detalhamentoValorContaVOs, conf, usuario);
		geracaoCentroResultadoOrigemDetalhePadraoPorContaReceberNegociada(contaReceberVO, dataRecebimentoContaAReceber, usuario, detalhamentoValorContaVOs, listaDescontosAplicavesContaReceber.get(0), conf);
		if (!Uteis.isAtributoPreenchido(contaReceberVO.getDetalhamentoValorContaVOs())) {
			contaReceberVO.getDetalhamentoValorContaVOs().addAll(detalhamentoValorContaVOs);
		} else {
			removerDetalhamentoValorNaoExistente(contaReceberVO, detalhamentoValorContaVOs);
			for (DetalhamentoValorContaVO detalhamentoValorContaVO : detalhamentoValorContaVOs) {
				adicionarCentroResultadoOrigemDetalhe(contaReceberVO.getDetalhamentoValorContaVOs(), detalhamentoValorContaVO, usuario);
			}
		}
	}

	private void removerDetalhamentoValorNaoExistente(ContaReceberVO contaReceberVO, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO) {
		Iterator<DetalhamentoValorContaVO> i = contaReceberVO.getDetalhamentoValorContaVOs().iterator();
		while (i.hasNext()) {
			DetalhamentoValorContaVO objExistente = i.next();
			if (!listaTempCentroResultadoOrigemDetalheVO.stream().anyMatch(p -> p.equalsCentroResultadoOrigemDetalhe(objExistente))) {
				i.remove();
			}
		}
	}

	private void geracaoCentroResultadoOrigemDetalhePadraoContaReceber(List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, ContaReceberVO contaReceberVO, Date dataRecebimentoContaAReceber, PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber,UsuarioVO usuario) throws Exception {

		DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorBaseContaReceber())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorBaseContaReceber(), TipoCentroResultadoOrigemDetalheEnum.VALOR_BASE, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -3, TipoDescontoAluno.VALOR, contaReceberVO.getValorBaseContaReceber());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorJuroCalculado())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorJuroCalculado(), TipoCentroResultadoOrigemDetalheEnum.JURO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -2, TipoDescontoAluno.PORCENTO, contaReceberVO.getJuroPorcentagem());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorMultaCalculado())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorMultaCalculado(), TipoCentroResultadoOrigemDetalheEnum.MULTA, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -2, TipoDescontoAluno.PORCENTO, contaReceberVO.getMultaPorcentagem());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceberVO.getAcrescimo())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getAcrescimo(), TipoCentroResultadoOrigemDetalheEnum.ACRESCIMO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -2, TipoDescontoAluno.VALOR, contaReceberVO.getAcrescimo());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue(), TipoCentroResultadoOrigemDetalheEnum.REAJUSTE_PRECO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -2, TipoDescontoAluno.VALOR, contaReceberVO.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}		
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorIndiceReajustePorAtraso().doubleValue())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorIndiceReajustePorAtraso().doubleValue(), TipoCentroResultadoOrigemDetalheEnum.REAJUSTE_POR_ATRASO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), -2, TipoDescontoAluno.VALOR, contaReceberVO.getValorIndiceReajustePorAtraso().doubleValue());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}		

		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorCalculadoDescontoLancadoRecebimento())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorCalculadoDescontoLancadoRecebimento(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_RECEBIMENTO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), 5, TipoDescontoAluno.getEnum(contaReceberVO.getTipoDescontoLancadoRecebimento()), contaReceberVO.getValorDescontoLancadoRecebimento());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceberVO.getValorDescontoRateio())) {
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceberVO.getValorDescontoRateio(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_RATEIO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), 5, TipoDescontoAluno.VALOR, contaReceberVO.getValorDescontoRateio());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}

		geracaoCentroResultadoOrigemDetalhePorConvenioCusteado(contaReceberVO, listaTempCentroResultadoOrigemDetalheVO, listaDescontosAplicavesContaReceber, usuario);
		geracaoCentroResultadoOrigemDetalhePorConvenio(contaReceberVO, listaTempCentroResultadoOrigemDetalheVO, listaDescontosAplicavesContaReceber, usuario);
		if (Uteis.isAtributoPreenchido(contaReceberVO.getDescontoProgressivo())) {
			geracaoCentroResultadoOrigemDetalhePorDescontoProgressivo(contaReceberVO, dataRecebimentoContaAReceber, listaTempCentroResultadoOrigemDetalheVO, usuario);
		}

	}

	private void geracaoCentroResultadoOrigemDetalhePadraoPorPlanoFinanceiroAlunoDescricaoDesconto(ContaReceberVO contaReceberVO, PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual, Date dataRecebimentoContaAReceber, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(pfaddAtual.getValorDescontoRateio())) {
			DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(pfaddAtual.getValorDescontoRateio(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_RATEIO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), 100, TipoDescontoAluno.VALOR, pfaddAtual.getValorDescontoRateio());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(pfaddAtual.getValorDescontoAluno())) {
			DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(pfaddAtual.getValorDescontoAluno(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_ALUNO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), contaReceberVO.getOrdemDescontoAluno(), TipoDescontoAluno.getEnum(contaReceberVO.getTipoDesconto()), contaReceberVO.getValorDesconto());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		
		for (PlanoDescontoContaReceberVO pdcr : contaReceberVO.getPlanoDescontoInstitucionalContaReceber()) {
			if((pdcr.getPlanoDescontoVO().getPercDescontoMatricula() > 0.0 && contaReceberVO.getTipoOrigem().equals("MAT"))
					|| (pdcr.getPlanoDescontoVO().getPercDescontoParcela() > 0.0 && !contaReceberVO.getTipoOrigem().equals("MAT"))){
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				PlanoFinanceiroAlunoDescricaoDescontosVO pfaddPlanoDescontoValido = pfaddAtual.getListaDescontosPlanoDesconto().containsKey(pdcr.getPlanoDescontoVO().getCodigo()) ? pfaddAtual : obterPlanoFinanceiroAlunoDescricaoPlanoDescontoValido(pdcr.getPlanoDescontoVO().getCodigo(), listaDescontosAplicavesContaReceber);
				if (pfaddPlanoDescontoValido != null) {
					TipoCentroResultadoOrigemDetalheEnum tipo = pdcr.getIsPlanoDescontoManual() ? TipoCentroResultadoOrigemDetalheEnum.DESCONTO_MANUAL : TipoCentroResultadoOrigemDetalheEnum.DESCONTO_INSTITUICAO;
					TipoDescontoAluno tipoValor = pdcr.getIsPlanoDescontoManual() ? pdcr.getTipoDesconto() : contaReceberVO.getTipoOrigem().equals("MAT") ? TipoDescontoAluno.getEnum(pdcr.getPlanoDescontoVO().getTipoDescontoMatricula()) : TipoDescontoAluno.getEnum(pdcr.getPlanoDescontoVO().getTipoDescontoParcela());
					Double valorTipoValor = pdcr.getIsPlanoDescontoManual() ? pdcr.getValorDesconto() : contaReceberVO.getTipoOrigem().equals("MAT") ? pdcr.getPlanoDescontoVO().getPercDescontoMatricula() : pdcr.getPlanoDescontoVO().getPercDescontoParcela();
					crod.preencherCentroResultadoOrigemDetalheVO(pfaddPlanoDescontoValido.getListaDescontosPlanoDesconto().get(pdcr.getPlanoDescontoVO().getCodigo()), tipo, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), contaReceberVO.getOrdemPlanoDesconto(), tipoValor, valorTipoValor);
					crod.setCodOrigemDoTipoDetalhe(pdcr.getIsPlanoDescontoManual() ? pdcr.getCodigo() : pdcr.getPlanoDescontoVO().getCodigo());
					crod.setNomeOrigemDoTipoDetalhe(pdcr.getIsPlanoDescontoManual() ? pdcr.getNome() : pdcr.getPlanoDescontoVO().getNome());
					if (!pdcr.getUtilizarDescontoSemLimiteValidade()) {
						crod.setDataLimiteAplicacaoDesconto(pfaddPlanoDescontoValido.getDataLimiteAplicacaoDesconto());
					}
					if (Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto()) && UteisData.getCompareData(dataRecebimentoContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0) {
						crod.setUtilizado(false);
					}
					adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
				}
			}
		}

	}

	private void geracaoCentroResultadoOrigemDetalhePadraoPorContaReceberNegociada(ContaReceberVO contaReceberVO, Date dataRecebimentoContaAReceber, UsuarioVO usuario, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, PlanoFinanceiroAlunoDescricaoDescontosVO pfaddMelhorDesconto, ConfiguracaoFinanceiroVO conf) throws Exception {
		/*if (contaReceberVO.getSituacao().equals("NE")) {
			ContaReceberNegociadoVO crn = getFacadeFactory().getContaReceberNegociadoFacade().consultarPorContaReceber(contaReceberVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, conf, usuario);
			crn.getNegociacaoContaReceber().setItemCondicaoDescontoRenegociacaoVO(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().consultarItemCondicaoDescontoRenegociacaoPorNegociacaoContaReceber(crn.getNegociacaoContaReceber().getCodigo(),usuario));
			if (Uteis.isAtributoPreenchido(crn)
					&& crn.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao()
					&& ((!crn.getDesconsiderarDescontoInstituicaoComValidade() && crn.getValorDescontoInstituicaoComValidadeDesconsiderado() > 0.0))
					|| (!crn.getDesconsiderarDescontoInstituicaoSemValidade() && crn.getValorDescontoInstituicaoSemValidadeDesconsiderado() > 0.0)) {
				geracaoCentroResultadoOrigemDetalhePorNegociacaoContaReceberPorInstitucional(contaReceberVO, dataRecebimentoContaAReceber, pfaddMelhorDesconto, listaTempCentroResultadoOrigemDetalheVO, crn, usuario);
			}
			if (Uteis.isAtributoPreenchido(crn) && !crn.getDesconsiderarDescontoProgressivo() && crn.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao() && crn.getValorDescontoProgressivoDesconsiderado() > 0.0) {
				geracaoCentroResultadoOrigemDetalhePorNegociacaoContaReceberPorDescontoProgressivo(contaReceberVO, dataRecebimentoContaAReceber, listaTempCentroResultadoOrigemDetalheVO, crn, usuario);
			}
			if (Uteis.isAtributoPreenchido(crn) && !crn.getDesconsiderarDescontoAluno() && crn.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao() && crn.getValorDescontoAlunoDesconsiderado() > 0.0) {
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				crod = new DetalhamentoValorContaVO();
				crod.preencherCentroResultadoOrigemDetalheVO(crn.getValorDescontoAlunoDesconsiderado(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_ALUNO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceberVO.getCodigo(), contaReceberVO.getOrdemDescontoAluno(), TipoDescontoAluno.getEnum(contaReceberVO.getTipoDesconto()), contaReceberVO.getValorDesconto());
				adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
			}
			atualizarValoresParaContasReceberNegociadasQueDesconsideramDescontos(listaTempCentroResultadoOrigemDetalheVO, crn);
		}*/
	}

	private void atualizarValoresParaContasReceberNegociadasQueDesconsideramDescontos(List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, ContaReceberNegociadoVO crn) {
		if (Uteis.isAtributoPreenchido(crn) && crn.getDesconsiderarDescontoInstituicaoSemValidade()) {
			for (DetalhamentoValorContaVO crod : listaTempCentroResultadoOrigemDetalheVO) {
				if ((crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoInstituicao()
						|| crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoManual())
						&& !Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto())) {
					crod.setUtilizado(false);
				}
			}
		}
		if (Uteis.isAtributoPreenchido(crn) && crn.getDesconsiderarDescontoInstituicaoComValidade()) {
			for (DetalhamentoValorContaVO crod : listaTempCentroResultadoOrigemDetalheVO) {
				if ((crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoInstituicao()
						|| crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoManual())
						&& Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto())) {
					crod.setUtilizado(false);
				}
			}
		}
		if (Uteis.isAtributoPreenchido(crn) && crn.getDesconsiderarDescontoProgressivo()) {
			for (DetalhamentoValorContaVO crod : listaTempCentroResultadoOrigemDetalheVO) {
				if (crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoProgressivo()
						&& crod.isUtilizado()) {
					crod.setUtilizado(false);
				}
			}
		}
		if (Uteis.isAtributoPreenchido(crn) && crn.getDesconsiderarDescontoAluno()) {
			for (DetalhamentoValorContaVO crod : listaTempCentroResultadoOrigemDetalheVO) {
				if (crod.getTipoCentroResultadoOrigemDetalheEnum().isDescontoAluno()
						&& crod.isUtilizado()) {
					crod.setUtilizado(false);
				}
			}
		}
	}

	private void geracaoCentroResultadoOrigemDetalhePorDescontoProgressivo(ContaReceberVO contaReceber, Date dataRecebimentoContaAReceber, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, UsuarioVO usuario) throws Exception {
		DetalhamentoValorContaVO crod;
		if (Uteis.isAtributoPreenchido(contaReceber.getDescontoProgressivo().getValorDescontoCalculado1())) {
			TipoDescontoAluno tipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite1() > 0 ? TipoDescontoAluno.PORCENTO : TipoDescontoAluno.VALOR;
			Double valorTipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite1() > 0 ? contaReceber.getDescontoProgressivo().getPercDescontoLimite1() : contaReceber.getDescontoProgressivo().getValorDescontoCalculado1();
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceber.getDescontoProgressivo().getValorDescontoCalculado1(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemDescontoProgressivo(), tipoValor, valorTipoValor);
			crod.setDataLimiteAplicacaoDesconto(contaReceber.getDescontoProgressivo().getDataBaseValidade1());
			if (Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto()) && UteisData.getCompareData(dataRecebimentoContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0) {
				crod.setUtilizado(false);
			}
			crod.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.PRIMEIRO);
			crod.setCodOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getCodigo());
			crod.setNomeOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getNome());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceber.getDescontoProgressivo().getValorDescontoCalculado2())) {
			TipoDescontoAluno tipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite2() > 0 ? TipoDescontoAluno.PORCENTO : TipoDescontoAluno.VALOR;
			Double valorTipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite2() > 0 ? contaReceber.getDescontoProgressivo().getPercDescontoLimite2() : contaReceber.getDescontoProgressivo().getValorDescontoCalculado2();
			crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceber.getDescontoProgressivo().getValorDescontoCalculado2(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemDescontoProgressivo(), tipoValor, valorTipoValor);
			crod.setDataLimiteAplicacaoDesconto(contaReceber.getDescontoProgressivo().getDataBaseValidade2());
			if ((Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto()) && UteisData.getCompareData(dataRecebimentoContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0)
					|| listaTempCentroResultadoOrigemDetalheVO.stream().anyMatch(p -> Uteis.isAtributoPreenchido(p.getFaixaDescontoProgressivo()) && p.getFaixaDescontoProgressivo().isPrimeiraFaixa() && p.isUtilizado())) {
				crod.setUtilizado(false);
			}
			crod.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.SEGUNDO);
			crod.setCodOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getCodigo());
			crod.setNomeOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getNome());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceber.getDescontoProgressivo().getValorDescontoCalculado3())) {
			crod = new DetalhamentoValorContaVO();
			TipoDescontoAluno tipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite3() > 0 ? TipoDescontoAluno.PORCENTO : TipoDescontoAluno.VALOR;
			Double valorTipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite3() > 0 ? contaReceber.getDescontoProgressivo().getPercDescontoLimite3() : contaReceber.getDescontoProgressivo().getValorDescontoCalculado3();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceber.getDescontoProgressivo().getValorDescontoCalculado3(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemDescontoProgressivo(), tipoValor, valorTipoValor);
			crod.setDataLimiteAplicacaoDesconto(contaReceber.getDescontoProgressivo().getDataBaseValidade3());
			if ((Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto()) && UteisData.getCompareData(dataRecebimentoContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0)
					|| listaTempCentroResultadoOrigemDetalheVO.stream().anyMatch(p -> Uteis.isAtributoPreenchido(p.getFaixaDescontoProgressivo()) && (p.getFaixaDescontoProgressivo().isPrimeiraFaixa() || p.getFaixaDescontoProgressivo().isSegundaFaixa()) && p.isUtilizado())) {
				crod.setUtilizado(false);
			}
			crod.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.TERCEIRO);
			crod.setCodOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getCodigo());
			crod.setNomeOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getNome());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
		if (Uteis.isAtributoPreenchido(contaReceber.getDescontoProgressivo().getValorDescontoCalculado4())) {
			crod = new DetalhamentoValorContaVO();
			TipoDescontoAluno tipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite4() > 0 ? TipoDescontoAluno.PORCENTO : TipoDescontoAluno.VALOR;
			Double valorTipoValor = contaReceber.getDescontoProgressivo().getPercDescontoLimite4() > 0 ? contaReceber.getDescontoProgressivo().getPercDescontoLimite4() : contaReceber.getDescontoProgressivo().getValorDescontoCalculado4();
			crod.preencherCentroResultadoOrigemDetalheVO(contaReceber.getDescontoProgressivo().getValorDescontoCalculado4(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemDescontoProgressivo(), tipoValor, valorTipoValor);
			crod.setDataLimiteAplicacaoDesconto(contaReceber.getDescontoProgressivo().getDataBaseValidade4());
			if ((Uteis.isAtributoPreenchido(crod.getDataLimiteAplicacaoDesconto()) && UteisData.getCompareData(dataRecebimentoContaAReceber, crod.getDataLimiteAplicacaoDesconto()) > 0)
					|| listaTempCentroResultadoOrigemDetalheVO.stream().anyMatch(p -> Uteis.isAtributoPreenchido(p.getFaixaDescontoProgressivo()) && (p.getFaixaDescontoProgressivo().isPrimeiraFaixa() || p.getFaixaDescontoProgressivo().isSegundaFaixa() || p.getFaixaDescontoProgressivo().isTerceiraFaixa()) && p.isUtilizado())) {
				crod.setUtilizado(false);
			}
			crod.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.QUARTO);
			crod.setCodOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getCodigo());
			crod.setNomeOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getNome());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
	}
	
	private Double obterValorPlanoFinanceiroAlunoDescricaoParaConvenio(List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber, PlanoDescontoContaReceberVO convenio) {
		PlanoFinanceiroAlunoDescricaoDescontosVO pfaddConvenio = Uteis.isAtributoPreenchido(listaDescontosAplicavesContaReceber) ? listaDescontosAplicavesContaReceber.get(0) : null;
		if(pfaddConvenio != null && pfaddConvenio.getListaDescontosConvenio().containsKey(convenio.getConvenio().getCodigo())) {
			return Uteis.arrendondarForcando2CadasDecimais(pfaddConvenio.getListaDescontosConvenio().get(convenio.getConvenio().getCodigo())); 
		}
		return 0.0;
	}

	private void geracaoCentroResultadoOrigemDetalhePorConvenioCusteado(ContaReceberVO contaReceber, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber, UsuarioVO usuario) {		
		for (PlanoDescontoContaReceberVO convenio : contaReceber.getPlanoDescontoConvenioCusteadoContaReceber()) {
			if(Uteis.isAtributoPreenchido(convenio.getValorUtilizadoRecebimento()) &&
					(convenio.getConvenio().getDescontoMatricula() > 0.0 && contaReceber.getTipoOrigem().equals("MAT"))
					|| (convenio.getConvenio().getDescontoParcela() > 0.0 && !contaReceber.getTipoOrigem().equals("MAT"))){
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				TipoDescontoAluno tipoValor = contaReceber.getTipoOrigem().equals("MAT") ? TipoDescontoAluno.getEnum(convenio.getConvenio().getTipoDescontoMatricula()) : TipoDescontoAluno.getEnum(convenio.getConvenio().getTipoDescontoParcela());
				Double valorTipoValor = contaReceber.getTipoOrigem().equals("MAT") ? convenio.getConvenio().getDescontoMatricula() : convenio.getConvenio().getDescontoParcela();
					crod.preencherCentroResultadoOrigemDetalheVO(obterValorPlanoFinanceiroAlunoDescricaoParaConvenio(listaDescontosAplicavesContaReceber, convenio), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_CUSTEADO_CONTA_RECEBER, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), -1, tipoValor, valorTipoValor);
				crod.setCodOrigemDoTipoDetalhe(convenio.getConvenio().getCodigo());
				crod.setNomeOrigemDoTipoDetalhe(convenio.getConvenio().getDescricao());
					crod.setUtilizado(!convenio.isSuspensoFinanciamentoProprio());
				adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);				
			}
		}
	}

	private void geracaoCentroResultadoOrigemDetalhePorConvenio(ContaReceberVO contaReceber, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO,  List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber, UsuarioVO usuario) {
		for (PlanoDescontoContaReceberVO convenio : contaReceber.getPlanoDescontoConvenioContaReceber()) {
			if((convenio.getConvenio().getDescontoMatricula() > 0.0 && contaReceber.getTipoOrigem().equals("MAT"))
					|| (convenio.getConvenio().getDescontoParcela() > 0.0 && !contaReceber.getTipoOrigem().equals("MAT"))){
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				TipoDescontoAluno tipoValor = contaReceber.getTipoOrigem().equals("MAT") ? TipoDescontoAluno.getEnum(convenio.getConvenio().getTipoDescontoMatricula()) : TipoDescontoAluno.getEnum(convenio.getConvenio().getTipoDescontoParcela());
				Double valorTipoValor = contaReceber.getTipoOrigem().equals("MAT") ? convenio.getConvenio().getDescontoMatricula() : convenio.getConvenio().getDescontoParcela();
				crod.preencherCentroResultadoOrigemDetalheVO(obterValorPlanoFinanceiroAlunoDescricaoParaConvenio(listaDescontosAplicavesContaReceber, convenio), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_CONVENIO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemConvenio(), tipoValor, valorTipoValor);
				crod.setCodOrigemDoTipoDetalhe(convenio.getConvenio().getCodigo());
				crod.setNomeOrigemDoTipoDetalhe(convenio.getConvenio().getDescricao());
				crod.setUtilizado(!convenio.isSuspensoFinanciamentoProprio());	
				adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
			}
		}
	}

	

	private void geracaoCentroResultadoOrigemDetalhePorNegociacaoContaReceberPorInstitucional(ContaReceberVO contaReceber, Date dataRecebimentoContaAReceber, PlanoFinanceiroAlunoDescricaoDescontosVO pfadd, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, ContaReceberNegociadoVO crn, UsuarioVO usuario) throws Exception {
		for (PlanoDescontoContaReceberVO pdcr : contaReceber.getPlanoDescontoInstitucionalContaReceber()) {
			if((pdcr.getPlanoDescontoVO().getPercDescontoMatricula() > 0.0 && contaReceber.getTipoOrigem().equals("MAT"))
					|| (pdcr.getPlanoDescontoVO().getPercDescontoParcela() > 0.0 && !contaReceber.getTipoOrigem().equals("MAT"))){
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				TipoCentroResultadoOrigemDetalheEnum tipo = pdcr.getIsPlanoDescontoManual() ? TipoCentroResultadoOrigemDetalheEnum.DESCONTO_MANUAL : TipoCentroResultadoOrigemDetalheEnum.DESCONTO_INSTITUICAO;
				TipoDescontoAluno tipoValor = pdcr.getIsPlanoDescontoManual() ? pdcr.getTipoDesconto() : contaReceber.getTipoOrigem().equals("MAT") ? TipoDescontoAluno.getEnum(pdcr.getPlanoDescontoVO().getTipoDescontoMatricula()) : TipoDescontoAluno.getEnum(pdcr.getPlanoDescontoVO().getTipoDescontoParcela());
				Double valorTipoValor = pdcr.getIsPlanoDescontoManual() ? pdcr.getValorDesconto() : contaReceber.getTipoOrigem().equals("MAT") ? pdcr.getPlanoDescontoVO().getPercDescontoMatricula() : pdcr.getPlanoDescontoVO().getPercDescontoParcela();
				crod.preencherCentroResultadoOrigemDetalheVO(pdcr.getValorUtilizadoRecebimento(), tipo, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemPlanoDesconto(), tipoValor, valorTipoValor);
				crod.setCodOrigemDoTipoDetalhe(pdcr.getIsPlanoDescontoManual() ? pdcr.getCodigo() : pdcr.getPlanoDescontoVO().getCodigo());
				crod.setNomeOrigemDoTipoDetalhe(pdcr.getIsPlanoDescontoManual() ? pdcr.getNome() : pdcr.getPlanoDescontoVO().getNome());
				if (!pdcr.getUtilizarDescontoSemLimiteValidade()) {
					crod.setDataLimiteAplicacaoDesconto(dataRecebimentoContaAReceber);
				}
				adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}
	}
	}

	private void geracaoCentroResultadoOrigemDetalhePorNegociacaoContaReceberPorDescontoProgressivo(ContaReceberVO contaReceber, Date dataRecebimentoContaAReceber, List<DetalhamentoValorContaVO> listaTempCentroResultadoOrigemDetalheVO, ContaReceberNegociadoVO crn, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(contaReceber.getDescontoProgressivo())) {
			DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
			crod.preencherCentroResultadoOrigemDetalheVO(crn.getValorDescontoProgressivoDesconsiderado(), TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, OrigemDetalhamentoContaEnum.CONTA_RECEBER, contaReceber.getCodigo(), contaReceber.getOrdemDescontoProgressivo(), TipoDescontoAluno.VALOR, crn.getValorDescontoProgressivoDesconsiderado());
			crod.setDataLimiteAplicacaoDesconto(dataRecebimentoContaAReceber);
			crod.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.PRIMEIRO);
			crod.setCodOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getCodigo());
			crod.setNomeOrigemDoTipoDetalhe(contaReceber.getDescontoProgressivo().getNome());
			adicionarCentroResultadoOrigemDetalhe(listaTempCentroResultadoOrigemDetalheVO, crod, usuario);
		}

	}

	public void adicionarCentroResultadoOrigemDetalhe(List<DetalhamentoValorContaVO> listaCentroResultadoOrigemDetalheVOs, DetalhamentoValorContaVO obj, UsuarioVO usuario) {
		try {
			Optional<DetalhamentoValorContaVO> findFirst = listaCentroResultadoOrigemDetalheVOs.stream().filter(p -> p.equalsCentroResultadoOrigemDetalhe(obj)).findFirst();
			if (findFirst.isPresent()) {
				findFirst.get().setUtilizado(obj.isUtilizado());
				findFirst.get().setDataLimiteAplicacaoDesconto(obj.getDataLimiteAplicacaoDesconto());
				if (findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isAcrescimo()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isJuro()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isMulta()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isReajustePreco()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isReajustePorAtraso()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isValorBase()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isDescontoRateio()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isDescontoCusteadoContaReceber()
						|| findFirst.get().getTipoCentroResultadoOrigemDetalheEnum().isDescontoAluno()) {
					findFirst.get().setValor(obj.getValor());
				}
				return;
			}
			listaCentroResultadoOrigemDetalheVOs.add(obj);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT dvc.codigo as \"dvc.codigo\",  ");
		sql.append(" dvc.origemDetalhamentoConta as \"dvc.origemDetalhamentoConta\", ");
		sql.append(" dvc.codigoorigem as \"dvc.codigoorigem\", ");
		sql.append(" dvc.valor as \"dvc.valor\", dvc.codOrigemDoTipoDetalhe as \"dvc.codOrigemDoTipoDetalhe\", ");
		sql.append(" dvc.dataLimiteAplicacaoDesconto as \"dvc.dataLimiteAplicacaoDesconto\", ");
		sql.append(" dvc.utilizado as \"dvc.utilizado\",  dvc.ordemApresentacao as \"dvc.ordemApresentacao\", ");
		sql.append(" dvc.tipoCentroResultadoOrigemDetalhe as \"dvc.tipoCentroResultadoOrigemDetalhe\", ");
		sql.append(" dvc.faixaDescontoProgressivo as \"dvc.faixaDescontoProgressivo\", ");
		sql.append(" dvc.tipoValor as \"dvc.tipovalor\", ");
		sql.append(" dvc.valortipovalor as \"dvc.valortipovalor\", ");
		sql.append(" case when tipocentroresultadoorigemdetalhe = 'DESCONTO_INSTITUICAO' then planodesconto.nome  ");
		sql.append("      when tipocentroresultadoorigemdetalhe in ('DESCONTO_CONVENIO', 'DESCONTO_CUSTEADO_CONTA_RECEBER') then convenio.descricao ");
		sql.append("      else dvc.nomeOrigemDoTipoDetalhe end  as \"dvc.nomeOrigemDoTipoDetalhe\"   ");
		sql.append(" FROM DetalhamentoValorConta dvc ");
		sql.append(" left join planodesconto on  planodesconto.codigo =  case when tipocentroresultadoorigemdetalhe = 'DESCONTO_INSTITUICAO' then dvc.codorigemdotipodetalhe else 0 end ");
		sql.append(" left join convenio on  convenio.codigo =  case when tipocentroresultadoorigemdetalhe in ('DESCONTO_CONVENIO', 'DESCONTO_CUSTEADO_CONTA_RECEBER') then dvc.codorigemdotipodetalhe else 0 end ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DetalhamentoValorContaVO> consultaRapidaPorOrigemCodigoOrigemConta(OrigemDetalhamentoContaEnum origemDetalhamentoContaEnum, Integer codigoConta, int nivelMontarDados, UsuarioVO usuario) {
		List<DetalhamentoValorContaVO> vetResultado = new ArrayList<>();
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE dvc.origemDetalhamentoConta = '").append(origemDetalhamentoContaEnum.name()).append("' ");
			sqlStr.append(" and dvc.codigoOrigem = ").append(codigoConta).append(" ");
			sqlStr.append(" order by dvc.ordemApresentacao, dvc.dataLimiteAplicacaoDesconto  ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (tabelaResultado.next()) {
				DetalhamentoValorContaVO crod = new DetalhamentoValorContaVO();
				montarDadosBasico(crod, tabelaResultado, nivelMontarDados);
				vetResultado.add(crod);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double somarDescontoIncondicionalContaReceber(Integer idContaReceber) throws Exception {
		Double soma = 0.0;
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select sum(valor) as soma from detalhamentovalorconta where origemdetalhamentoconta = 'CONTA_RECEBER' and tipocentroresultadoorigemdetalhe in ('DESCONTO_ALUNO', 'DESCONTO_CONVENIO', 'DESCONTO_INSTITUICAO', 'DESCONTO_MANUAL', 'DESCONTO_RATEIO', 'DESCONTO_CUSTEADO_CONTA_RECEBER') ");
		sqlStr.append(" and utilizado and datalimiteaplicacaodesconto is null and codigoorigem = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { idContaReceber });
		
		if(tabelaResultado.next()) {
			soma = tabelaResultado.getDouble("soma");
		}

		return soma;	
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public DetalhamentoValorContaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE dvc.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( DetalhamentoValorContaVO ).");
			}
			DetalhamentoValorContaVO obj = new DetalhamentoValorContaVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosBasico(DetalhamentoValorContaVO obj, SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("dvc.codigo"));
		obj.setCodOrigemDoTipoDetalhe(dadosSQL.getInt("dvc.codOrigemDoTipoDetalhe"));
		obj.setNomeOrigemDoTipoDetalhe(dadosSQL.getString("dvc.nomeOrigemDoTipoDetalhe"));
		obj.setOrdemApresentacao(dadosSQL.getInt("dvc.ordemApresentacao"));
		obj.setValor(dadosSQL.getDouble("dvc.valor"));
		obj.setUtilizado(dadosSQL.getBoolean("dvc.utilizado"));
		obj.setDataLimiteAplicacaoDesconto(dadosSQL.getDate("dvc.dataLimiteAplicacaoDesconto"));
		obj.setTipoCentroResultadoOrigemDetalheEnum(TipoCentroResultadoOrigemDetalheEnum.valueOf(dadosSQL.getString("dvc.tipoCentroResultadoOrigemDetalhe")));
		obj.setFaixaDescontoProgressivo(FaixaDescontoProgressivo.valueOf(dadosSQL.getString("dvc.faixaDescontoProgressivo")));
		obj.setOrigemDetalhamentoConta(OrigemDetalhamentoContaEnum.valueOf(dadosSQL.getString("dvc.origemDetalhamentoConta")));
		obj.setCodigoOrigem(dadosSQL.getInt("dvc.codigoOrigem"));
		obj.setTipoValor(TipoDescontoAluno.valueOf(dadosSQL.getString("dvc.tipovalor")));
		obj.setValorTipoValor(dadosSQL.getDouble("dvc.valortipovalor"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DetalhamentoValorConta.idEntidade;
	}

}
