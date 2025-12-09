package negocio.facade.jdbc.financeiro;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.TotalizadorPorFormaPagamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoConjuntaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.SituacaoConcialiacaoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoTransacaoOFXEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ResultadoConjuntoValoresVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConciliacaoContaCorrenteInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class ConciliacaoContaCorrente extends ControleAcesso implements ConciliacaoContaCorrenteInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2379207927191241827L;
	protected static String idEntidade;

	public ConciliacaoContaCorrente() throws Exception {
		super();
		setIdEntidade("ConciliacaoContaCorrente");
	}

	public void validarDados(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getContaCorrenteArquivo()), "O campo Conta Corrente (Conciliação) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getBanco()), "Não foi encontrado nenhum banco cadastro no sistema para essa conta corrente.");
		/*Optional<ConciliacaoContaCorrenteDiaVO> cccdMin = obj.getListaConciliacaoContaCorrenteDia().stream().min(Comparator.comparing(p-> p.getData()));
		Optional<ConciliacaoContaCorrenteDiaVO> cccdMax = obj.getListaConciliacaoContaCorrenteDia().stream().max(Comparator.comparing(p-> p.getData()));
		Uteis.checkState(!cccdMin.isPresent() || cccdMin.get().getData() == null, "Não foi encontrado a data inicial para conciliação bancaria.");
		Uteis.checkState(!cccdMax.isPresent() || cccdMax.get().getData() == null, "Não foi encontrado a data final para conciliação bancaria.");
		ConciliacaoContaCorrenteVO objExistente = validarUnicidadeConciliacaoContaCorrente(obj, cccdMin.get().getData(), cccdMax.get().getData(), usuario);*/
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataInicioSei()) || !Uteis.isAtributoPreenchido(obj.getDataFimSei()), "O campo Período de Apuração (Conciliação Conta Corrente) deve ser informado.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataInicioSei(), obj.getDataFimSei())>0, "A Data Inicial do Período de Apuração não pode ser maior que a Data Final.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataFimSei(), obj.getDataInicioSei())<0, "A Data Final do Período de Apuração não pode ser menor que a Data Inicial.");
		ConciliacaoContaCorrenteVO objExistente = validarUnicidadeConciliacaoContaCorrente(obj, obj.getDataInicioSei(), obj.getDataFimSei(), usuario);
		Uteis.checkState(Uteis.isAtributoPreenchido(objExistente), "Já foi conciliado o Período " + UteisData.obterDataFormatoTexto_dd_MM_yyyy(obj.getDataInicioSei()) + " até "+ UteisData.obterDataFormatoTexto_dd_MM_yyyy(obj.getDataFimSei()) +" na Conciliação de código: " + objExistente.getCodigo());
		List<ConciliacaoContaCorrenteDiaExtratoVO> listaDuplicidade = validarUnicidadeParaConciliacaoContaCorrenteExtratoDia(obj);
		if(Uteis.isAtributoPreenchido(listaDuplicidade)){
			throw new StreamSeiException("Os lançamentos "+ listaDuplicidade.get(0).getLancamentoOfx() + " e "+ listaDuplicidade.get(1).getLancamentoOfx()+ " do dia " +UteisData.obterDataFormatoTexto_dd_MM_yyyy(listaDuplicidade.get(0).getConciliacaoContaCorrenteDia().getData())+ " estão duplicados por favor verificar.");	
		}
	}
	
	@Override
	public List<ConciliacaoContaCorrenteDiaExtratoVO> validarUnicidadeParaConciliacaoContaCorrenteExtratoDia(ConciliacaoContaCorrenteVO obj)  {
		List<ConciliacaoContaCorrenteDiaExtratoVO> lista = new ArrayList<>();
		try {
			obj.getListaConciliacaoContaCorrenteDia().stream()
			.forEach(ccd->{
				ccd.getListaConciliacaoContaCorrenteExtrato().stream()
				.filter(cccde-> Uteis.isAtributoPreenchido(cccde.getCodigoSei()))
				.forEach(cccde->{
					obj.getListaConciliacaoContaCorrenteDia().stream()
					.filter(p -> UteisData.getCompararDatas(p.getData(), ccd.getData()))
					.flatMap(p-> p.getListaConciliacaoContaCorrenteExtrato().stream())
					.filter(pp-> pp.getCodigoSei().equals(cccde.getCodigoSei())
							&& !cccde.getCodigoOfx().equals(pp.getCodigoOfx())
							&& !cccde.isExisteConciliacaoConjunta())
					.forEach(pp->{			
						lista.add(pp);
						lista.add(cccde);
						throw new StreamSeiException("ENCONTROU");			
					});
				});
			});
			
		} catch (StreamSeiException e) {
			if(!e.getMessage().equals("ENCONTROU")){
				throw e;
			}
		}
		return lista;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, conf, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, conf, usuarioVO);
		}
		getFacadeFactory().getExtratoContaCorrenteFacade().anularExtratoContaCorrentePorConciliacaoContaCorrenteQueNaoFazemMaisParte(obj, usuarioVO);
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConciliacaoContaCorrenteDia(), "ConciliacaoContaCorrenteDia", "ConciliacaoContaCorrente", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getConciliacaoContaCorrenteDiaInterfaceFacade().persistir(obj.getListaConciliacaoContaCorrenteDia(), false, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		try {
			if(!obj.isConciliacaoDepreciadaPorArquivo() &&  !Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
				obj.getArquivoVO().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, conf);	
			}
			ConciliacaoContaCorrente.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO conciliacaoContaCorrente (situacaoConcialiacaoContaCorrenteEnum, nomearquivo, datageracao, contacorrentearquivo, digitocontacorrentearquivo, ");
			sql.append("            nomeContaCorrente, totalValorOfx,valorBalancoOfx, responsavel, banco, arquivo, datainiciosei, datafimsei ) ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setString(++i, obj.getSituacaoConcialiacaoContaCorrenteEnum().name());
					sqlInserir.setString(++i, obj.getNomeArquivo());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataGeracao()));
					sqlInserir.setString(++i, obj.getContaCorrenteArquivo());
					sqlInserir.setString(++i, obj.getDigitoContaCorrenteArquivo());
					sqlInserir.setString(++i, obj.getNomeContaCorrente());
					sqlInserir.setDouble(++i, obj.getTotalValorOfx());
					sqlInserir.setDouble(++i, obj.getValorBalancoOfx());
					sqlInserir.setInt(++i, obj.getResponsavel().getCodigo());
					sqlInserir.setInt(++i, obj.getBanco().getCodigo());
					Uteis.setValuePreparedStatement(obj.getArquivoVO(), ++i, sqlInserir);
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioSei()));
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataFimSei()));
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
			obj.getArquivoVO().setNovoObj(true);
			obj.getArquivoVO().setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrente.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConciliacaoContaCorrente ");
			sql.append("   SET situacaoConcialiacaoContaCorrenteEnum=?, nomearquivo=?, datageracao=?, contacorrentearquivo=?, digitocontacorrentearquivo=?, ");
			sql.append("       nomeContaCorrente=?, totalValorOfx=?, valorBalancoOfx=?, responsavel=?, banco=?, arquivo=?, ");
			sql.append("       datainiciosei=?, datafimsei=?  ");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setString(++i, obj.getSituacaoConcialiacaoContaCorrenteEnum().name());
					sqlAlterar.setString(++i, obj.getNomeArquivo());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataGeracao()));					
					sqlAlterar.setString(++i, obj.getContaCorrenteArquivo());
					sqlAlterar.setString(++i, obj.getDigitoContaCorrenteArquivo());
					sqlAlterar.setString(++i, obj.getNomeContaCorrente());
					sqlAlterar.setDouble(++i, obj.getTotalValorOfx());
					sqlAlterar.setDouble(++i, obj.getValorBalancoOfx());
					sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo());
					sqlAlterar.setInt(++i, obj.getBanco().getCodigo());
					Uteis.setValuePreparedStatement(obj.getArquivoVO(), ++i, sqlAlterar);
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioSei()));
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataFimSei()));
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, conf, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void finalizarConciliacao(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj), "Para finalizar a conciliação antes é necessário gravar.");
		obj.getListaConciliacaoContaCorrenteDia()
				.stream()
				.flatMap(ccd -> ccd.getListaConciliacaoContaCorrenteExtrato().stream())
				.forEach(ccce -> {
					Uteis.checkState(!ccce.isExtratoConciliado() && !ccce.isExtratoTransacaoExistente(), "O lançamento " + ccce.getLancamentoSei() + "  do dia " + UteisData.obterDataFormatoTexto_dd_MM_yyyy(ccce.getConciliacaoContaCorrenteDia().getData()) + " ainda não foi conciliado.");
					Uteis.checkState(!ccce.isExtratoConciliado() && !ccce.isExtratoSeiExistente(), "O lançamento " + ccce.getLancamentoOfx() + "  do dia " + UteisData.obterDataFormatoTexto_dd_MM_yyyy(ccce.getConciliacaoContaCorrenteDia().getData()) + " ainda não foi conciliado.");
					Uteis.checkState(!ccce.isExtratoConciliado() && ccce.isExtratoSeiExistente(), "O lançamento de valor " + ccce.getValorSei() + "  do dia " + UteisData.obterDataFormatoTexto_dd_MM_yyyy(ccce.getConciliacaoContaCorrenteDia().getData()) + " ainda não foi conciliado.");
				});
		obj.setSituacaoConcialiacaoContaCorrenteEnum(SituacaoConcialiacaoContaCorrenteEnum.FINALIZADA);
		atualizarCampoSituacaoConciliacao(obj, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void abrirConciliacao(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		obj.setSituacaoConcialiacaoContaCorrenteEnum(SituacaoConcialiacaoContaCorrenteEnum.ABERTA);
		atualizarCampoSituacaoConciliacao(obj, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void atualizarCampoSituacaoConciliacao(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE ConciliacaoContaCorrente SET situacaoConcialiacaoContaCorrenteEnum = '").append(obj.getSituacaoConcialiacaoContaCorrenteEnum().name()).append("' ");
		sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append("");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConciliacaoContaCorrenteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrente.excluir(getIdEntidade(), verificarAcesso, usuario);			
			getFacadeFactory().getExtratoContaCorrenteFacade().anularExtratoContaCorrentePorConciliacaoContaCorrente(obj, usuario);
			String sql = "DELETE FROM ConciliacaoContaCorrente WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			if(Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoVO(), usuario, conf);	
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void uploadArquivo(ConciliacaoContaCorrenteVO obj, FileUploadEvent upload, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioLogado) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataInicioSei())  || !Uteis.isAtributoPreenchido(obj.getDataFimSei()), "O campo Período de Apuração (Conciliação Conta Corrente) deve ser informado.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataInicioSei(), obj.getDataFimSei())>0, "A Data Inicial do Período de Apuração não pode ser maior que a Data Final.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataFimSei(), obj.getDataInicioSei())<0, "A Data Final do Período de Apuração não pode ser menor que a Data Inicial.");
		getFacadeFactory().getArquivoHelper().upLoad(upload, obj.getArquivoVO(), conf, PastaBaseArquivoEnum.OFX_TMP, usuarioLogado);
		obj.getListaConciliacaoContaCorrenteDia().clear();
		obj.setConciliacaoDiaSelecionada(new ConciliacaoContaCorrenteDiaVO());
		obj.setListaParametrizarEntradaItens(new ArrayList<ParametrizarOperacoesAutomaticasConciliacaoItemVO>());
		obj.setListaParametrizarSaidaItens(new ArrayList<ParametrizarOperacoesAutomaticasConciliacaoItemVO>());	
		obj.getArquivoVO().setResponsavelUpload(usuarioLogado);
		obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
		realizarLeituraArquivoOXF(obj, conf, usuarioLogado);
		realizarLeituraExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoPeriodo(obj, usuarioLogado);
		realizarTentativaDeConciliacaoAutomaticaPorValor(obj, usuarioLogado);
		removerConciliacaoContaCorrenteDiaNaoFazMaisParteDaMovimentacao(obj);
		realizarCalculoTotalizadoresDias(obj, usuarioLogado);
		
		
	}
	
	
	private void realizarLeituraArquivoOXF(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioLogado) throws Exception {
		AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);
		ResponseEnvelope envelope = null;
		BankingResponseMessageSet bankingResponseMessageSet = null;
		BankStatementResponse bankStatementResponse = null;
		File arquivo = isValidarCodificacaoDoArquivo(obj, conf, usuarioLogado);
		try {
			envelope = unmarshaller.unmarshal(FileUtils.openInputStream(arquivo));
			bankingResponseMessageSet = (BankingResponseMessageSet) envelope.getMessageSet(MessageSetType.banking);
			for (BankStatementResponseTransaction bankStatementResponseTransaction : bankingResponseMessageSet.getStatementResponses()) {
				bankStatementResponse = bankStatementResponseTransaction.getMessage();
				realizandoLeituraCabecalhoArquivoOFX(obj, usuarioLogado, bankStatementResponse);
				Integer codigoOFX = 1;
				for (Transaction transaction : bankStatementResponse.getTransactionList().getTransactions()) {
					Date dia = transaction.getDatePosted();
					ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(dia, obj);
					if(UteisData.isDataDentroDoPeriodo(obj.getDataInicioSei(), obj.getDataFimSei(), transaction.getDatePosted())) {
						ConciliacaoContaCorrenteVO objExistente = validarUnicidadeConciliacaoContaCorrente(obj, dia, dia, usuarioLogado);
						Uteis.checkState(Uteis.isAtributoPreenchido(objExistente), "Já foi conciliado o dia " + UteisData.obterDataFormatoTexto_dd_MM_yyyy(dia) + " na Conciliação de código: " + objExistente.getCodigo());
					}
					if (!Uteis.isAtributoPreenchido(objDia.getData())) {
						objDia.setData(transaction.getDatePosted());
						objDia.setConciliacaoContaCorrente(obj);
					}
					preencherExtratoContaCorrenteOfx(obj, objDia, transaction, codigoOFX, usuarioLogado);
					addConciliacaoContaCorrenteDiaVO(objDia, obj);
					codigoOFX++;
				}
			}

		} finally {
			if (!ArquivoHelper.getDetectarCharset(arquivo).equals("UTF-8")) {
				ArquivoHelper.delete(arquivo);
			}
			bankingResponseMessageSet = null;
			bankStatementResponse = null;
			envelope = null;
			unmarshaller = null;
			arquivo = null;
		}
	}
	
	
	private File isValidarCodificacaoDoArquivo(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuarioLogado) throws Exception {
		String caminhoAteDiretorio = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(obj.getArquivoVO(), obj.getArquivoVO().getPastaBaseArquivo() , conf.getLocalUploadArquivoTemp());
		File arquivo = new File(caminhoAteDiretorio + File.separator  + obj.getArquivoVO().getNome());
		String codificacao = ArquivoHelper.getDetectarCharset(arquivo);
		if (!codificacao.equals("UTF-8")) {
			arquivo =  obj.isValidarCodificacaoDoArquivo(arquivo, caminhoAteDiretorio + File.separator  + obj.getArquivoVO().getNome(), codificacao);	
		}
		return arquivo;
	}	

	private void realizandoLeituraCabecalhoArquivoOFX(ConciliacaoContaCorrenteVO obj, UsuarioVO usuarioLogado, BankStatementResponse bankStatementResponse) throws Exception {
		if(!Uteis.isAtributoPreenchido(obj.getBanco())) {
			String numeroConta;
			if (bankStatementResponse.getAccount() != null) {
				if (bankStatementResponse.getAccount().getAccountNumber().contains("-")) {
					numeroConta = bankStatementResponse.getAccount().getAccountNumber().replaceAll("-", "");
					obj.setContaCorrenteArquivo(numeroConta.substring(0, numeroConta.length() - 1));
					obj.setDigitoContaCorrenteArquivo(numeroConta.substring(numeroConta.length() - 1, numeroConta.length()));
				} else {
					obj.setContaCorrenteArquivo(bankStatementResponse.getAccount().getAccountNumber());
					obj.setDigitoContaCorrenteArquivo("");
				}
			}
			obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorNumeroContaCorrentePorDigitoContaCorrente(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0"), obj.getDigitoContaCorrenteArquivo(), usuarioLogado));
			obj.setNomeContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(obj.getBanco().getNrBanco(), StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0"), obj.getDigitoContaCorrenteArquivo(), usuarioLogado));
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getBanco()), "Não foi encontrado nenhum banco ou conta corrente cadastrada no sistema com esses valores:  Conta Corrente = " + obj.getContaCorrenteArquivo() + " e Dígito = " + obj.getDigitoContaCorrenteArquivo());			
			obj.setDataGeracao(new Date());
			obj.setNomeArquivo(obj.getArquivoVO().getDescricao());
			obj.setResponsavel(usuarioLogado);
		}
		if (bankStatementResponse.getLedgerBalance() != null) {
			obj.setValorBalancoOfx(bankStatementResponse.getLedgerBalance().getAmount());
		}
		/*if (bankStatementResponse.getAvailableBalance() != null) {
			obj.setValorBalancoOfx(bankStatementResponse.getAvailableBalance().getAmount());
		}*/
		obj.getListaParametrizarEntradaItens().clear();
		obj.getListaParametrizarSaidaItens().clear();
		getFacadeFactory().getParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade().consultaRapidaPorParametrizarOperacoesAutomaticasConciliacao(obj, usuarioLogado);
	}	
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addConciliacaoContaCorrenteDiaExtratoVO(ConciliacaoContaCorrenteDiaExtratoVO objExtrato, ConciliacaoContaCorrenteDiaVO objDia) {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaExtratoVO objsExistente : objDia.getListaConciliacaoContaCorrenteExtrato()) {
			if (objsExistente.getCodigoOfx().equals(objExtrato.getCodigoOfx())
					&& objsExistente.getCodigoSei().equals(objExtrato.getCodigoSei())) {
				objDia.getListaConciliacaoContaCorrenteExtrato().set(index, objExtrato);
				return;
			}
			index++;
		}
		objDia.getListaConciliacaoContaCorrenteExtrato().add(objExtrato);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addConciliacaoContaCorrenteDiaVO(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteVO obj) {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaVO objsExistente : obj.getListaConciliacaoContaCorrenteDia()) {
			if (UteisData.getCompararDatas(objsExistente.getData(), objDia.getData())) {
				obj.getListaConciliacaoContaCorrenteDia().set(index, objDia);
				return;
			}
			index++;
		}
		obj.getListaConciliacaoContaCorrenteDia().add(objDia);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void removerConciliacaoContaCorrenteDiaVO(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteVO obj) {
		obj.getListaConciliacaoContaCorrenteDia().removeIf(p->UteisData.getCompararDatas(p.getData(), objDia.getData()));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConciliacaoContaCorrenteDiaVO consultarConciliacaoContaCorrenteDiaVO(Date data, ConciliacaoContaCorrenteVO obj) {
		return obj.getListaConciliacaoContaCorrenteDia()
				.stream()
				.filter(p -> UteisData.getCompararDatas(p.getData(), data))
				.findFirst()
				.orElse(new ConciliacaoContaCorrenteDiaVO());
	}	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConciliacaoContaCorrenteDiaExtratoVO consultarConciliacaoContaCorrenteDiaExtratoOFX(Integer codigoOfx, ConciliacaoContaCorrenteDiaVO objDia) {
		return objDia.getListaConciliacaoContaCorrenteExtrato()
				.stream()
				.filter(objsExistente -> objsExistente.isExtratoTransacaoExistente() && objsExistente.getCodigoOfx().equals(codigoOfx))
				.findFirst()
				.orElse(new ConciliacaoContaCorrenteDiaExtratoVO());
	}
	
	private void adicionarConciliacaoContaCorrenteDiaExtratoOfx(ConciliacaoContaCorrenteDiaExtratoVO obj, ConciliacaoContaCorrenteDiaVO objDia) {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaExtratoVO objsExistente : objDia.getListaConciliacaoContaCorrenteExtrato()) {
			if (objsExistente.isExtratoTransacaoExistente() 
					&& obj.isExtratoTransacaoExistente()
					&& UteisData.getCompararDatas(objsExistente.getDataOfx(), obj.getDataOfx())
					&& objsExistente.getTipoTransacaoOFXEnum().equals(obj.getTipoTransacaoOFXEnum())
					&& objsExistente.getLancamentoOfx().equals(obj.getLancamentoOfx()) 
					&& objsExistente.getDocumentoOfx().equals(obj.getDocumentoOfx()) 
					&& objsExistente.getValorOfx().equals(obj.getValorOfx()) 
					&& objsExistente.getCodigoOfx().equals(obj.getCodigoOfx()) 
					) {
				objDia.getListaConciliacaoContaCorrenteExtrato().set(index, obj);
				return;
			}
			index++;
		}
		objDia.getListaConciliacaoContaCorrenteExtrato().add(obj);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConciliacaoContaCorrenteDiaExtratoVO consultarConciliacaoContaCorrenteDiaExtratoSei(TipoMovimentacaoFinanceira tipoMovimentacao, ConciliacaoContaCorrenteDiaVO objDia) {
		return objDia.getListaConciliacaoContaCorrenteExtrato()
				.stream()
				.filter(objsExistente -> !objsExistente.isExtratoTransacaoExistente() && objsExistente.isExtratoSeiExistente() && Uteis.isAtributoPreenchido(objsExistente.getTipoMovimentacaoFinanceiraSei()) && objsExistente.getTipoMovimentacaoFinanceiraSei().equals(tipoMovimentacao))
				.findFirst()
				.orElse(new ConciliacaoContaCorrenteDiaExtratoVO());
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void adicionarConciliacaoContaCorrenteDiaExtratoSeiAgrupado(ConciliacaoContaCorrenteDiaExtratoVO obj, ConciliacaoContaCorrenteDiaVO objDia) {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaExtratoVO objsExistente : objDia.getListaConciliacaoContaCorrenteExtrato()) {
			if (!objsExistente.isExtratoTransacaoExistente()
					&& objsExistente.isExtratoSeiExistente()
					&& Uteis.isAtributoPreenchido(objsExistente.getCodigoSei())
					&& Uteis.isAtributoPreenchido(objsExistente.getTipoMovimentacaoFinanceiraSei())
					&& objsExistente.getCodigoSei().equals(obj.getCodigoSei())
					&& objsExistente.getTipoMovimentacaoFinanceiraSei().equals(obj.getTipoMovimentacaoFinanceiraSei())) {
				objDia.getListaConciliacaoContaCorrenteExtrato().set(index, obj);
				return;
			}
			index++;
		}
		objDia.getListaConciliacaoContaCorrenteExtrato().add(obj);
	}

	
	private void preencherExtratoContaCorrenteOfx(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO objDia, Transaction transaction, Integer codigoOfx, UsuarioVO usuario) throws Exception {
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato = consultarConciliacaoContaCorrenteDiaExtratoOFX(codigoOfx, objDia);
		if(!conciliacaoExtrato.isExtratoTransacaoExistente()) {
			conciliacaoExtrato.setCodigoOfx(codigoOfx);
			conciliacaoExtrato.setDataOfx(transaction.getDatePosted());
			conciliacaoExtrato.setTipoTransacaoOFXEnum(TipoTransacaoOFXEnum.getEnum(transaction.getTransactionType().name()));
			conciliacaoExtrato.setIdentificadorOfx(transaction.getId());
			conciliacaoExtrato.setLancamentoOfx(transaction.getMemo());
			conciliacaoExtrato.setValorOfx(transaction.getAmount());
			conciliacaoExtrato.setDocumentoOfx(transaction.getCheckNumber());
			conciliacaoExtrato.setConciliacaoContaCorrenteDia(objDia);	
		}
		validarSeExisteExtratoContaCorrenteSei(obj, objDia, conciliacaoExtrato, usuario);
		adicionarConciliacaoContaCorrenteDiaExtratoOfx(conciliacaoExtrato, objDia);
	}
	
	
	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarSeExisteExtratoContaCorrenteSei(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, UsuarioVO usuario) throws Exception {
		ExtratoContaCorrenteVO extratoContaCorrente = null;
		if (Uteis.isAtributoPreenchido(obj.getListaParametrizarEntradaItens()) || Uteis.isAtributoPreenchido(obj.getListaParametrizarSaidaItens())) {
			ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro = obj.buscarParametrizacaoOperacaoAutomatica(conciliacaoExtrato.isTipoTransacaoEntrada(), conciliacaoExtrato.getLancamentoOfx());
			if (Uteis.isAtributoPreenchido(parametro.getNomeLancamento())) {
				extratoContaCorrente = getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorValor(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0"), obj.getDigitoContaCorrenteArquivo(), objDia.getData(), parametro.getTipoMovimentacaoFinanceira(), parametro.getOrigemExtratoContaCorrenteEnum(), parametro.getTipoFormaPagamento(), conciliacaoExtrato.getValorOfx(), objDia.getListaCodigoExtratoContaCorrenteExistente(), usuario);
				if (Uteis.isAtributoPreenchido(extratoContaCorrente)) {
					preencherExtratoContaCorrenteSei(objDia, conciliacaoExtrato, extratoContaCorrente);
				}
				if (!conciliacaoExtrato.isExtratoSeiExistente()) {
					validarExtratoContaCorrenteValoresMenoresParaConciliacao(obj, objDia, conciliacaoExtrato, parametro, usuario);
				}
			}
		}
		if (!conciliacaoExtrato.isExtratoSeiExistente()) {
			TipoMovimentacaoFinanceira tipoMov = conciliacaoExtrato.isTipoTransacaoEntrada() ? TipoMovimentacaoFinanceira.ENTRADA : TipoMovimentacaoFinanceira.SAIDA;
			extratoContaCorrente = getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorValor(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0"), obj.getDigitoContaCorrenteArquivo(), objDia.getData(), tipoMov, null, null, conciliacaoExtrato.getValorOfx(), objDia.getListaCodigoExtratoContaCorrenteExistente(),usuario);
			if (Uteis.isAtributoPreenchido(extratoContaCorrente)) {
				preencherExtratoContaCorrenteSei(objDia, conciliacaoExtrato, extratoContaCorrente);
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherExtratoContaCorrenteSei(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ExtratoContaCorrenteVO extratoContaCorrente) {
		if(!validarSeExisteCodigoSei(objDia.getListaCodigoExtratoContaCorrenteExistente(), extratoContaCorrente.getCodigo().toString())){
			extratoContaCorrente.setConciliacaoContaCorrenteDiaExtratoVO(conciliacaoExtrato);
			if (!Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei())) {
				conciliacaoExtrato.setTipoMovimentacaoFinanceiraSei(extratoContaCorrente.getTipoMovimentacaoFinanceira());
				conciliacaoExtrato.setDataSei(objDia.getData());
			}
			conciliacaoExtrato.setValorSei(Uteis.arrendondarForcando2CadasDecimais(conciliacaoExtrato.getValorSei() + extratoContaCorrente.getValor()));
			conciliacaoExtrato.setConciliacaoContaCorrenteDia(objDia);			
			conciliacaoExtrato.getListaExtratoContaCorrente().add(extratoContaCorrente);
			if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei())) {
				conciliacaoExtrato.setCodigoSei(conciliacaoExtrato.getCodigoSei() + "," + extratoContaCorrente.getCodigo());
			} else {
				conciliacaoExtrato.setCodigoSei(extratoContaCorrente.getCodigo().toString());
			}
			if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getLancamentoSei()) && !conciliacaoExtrato.getLancamentoSei().contains(extratoContaCorrente.getFormaPagamentoConciliacao())) {
				conciliacaoExtrato.setLancamentoSei(conciliacaoExtrato.getLancamentoSei() + "," + extratoContaCorrente.getFormaPagamentoConciliacao());
			} else if (!Uteis.isAtributoPreenchido(conciliacaoExtrato.getLancamentoSei())) {
				conciliacaoExtrato.setLancamentoSei(extratoContaCorrente.getFormaPagamentoConciliacao());
			}
			if (Uteis.isAtributoPreenchido(objDia.getListaCodigoExtratoContaCorrenteExistente())) {
				objDia.setListaCodigoExtratoContaCorrenteExistente(objDia.getListaCodigoExtratoContaCorrenteExistente() + "," + extratoContaCorrente.getCodigo());
			} else {
				objDia.setListaCodigoExtratoContaCorrenteExistente(extratoContaCorrente.getCodigo().toString());
			}	
		}
	}
	
	private void validarExtratoContaCorrenteValoresMenoresParaConciliacao(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception {
		List<ExtratoContaCorrenteVO> lista = getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratosContaCorrenteComValorMenores(StringUtils.stripStart(obj.getContaCorrenteArquivo(), "0"), obj.getDigitoContaCorrenteArquivo(), objDia.getData(), parametro.getTipoMovimentacaoFinanceira(), parametro.getOrigemExtratoContaCorrenteEnum(), parametro.getTipoFormaPagamento(), conciliacaoExtrato.getValorOfx(), objDia.getListaCodigoExtratoContaCorrenteExistente(), usuario);
		Double valorOperacao = conciliacaoExtrato.getValorOfx() < 0.0 ? conciliacaoExtrato.getValorOfx() * -1 : conciliacaoExtrato.getValorOfx();
		if (!lista.isEmpty()) {
			//HashMap<String, List<ExtratoContaCorrenteVO>> mapa = Uteis.identificarContasIguaisValorOperacao1(lista, valorOperacao, "codigo", "valor");
			List<ResultadoConjuntoValoresVO<ExtratoContaCorrenteVO>> mapa = Uteis.identificarContasIguaisValorOperacao1(lista, valorOperacao, "codigo", "valor");
			if (mapa.size() == 1) {
				for (ResultadoConjuntoValoresVO<ExtratoContaCorrenteVO> map : mapa) {
					map.getListaDeConjunto().stream().forEach(p -> preencherExtratoContaCorrenteSei(objDia, conciliacaoExtrato, p));
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarCorrecaoParaRegistroDuplicadosConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {		
		obj.getListaConciliacaoContaCorrenteDia().stream()
		.forEach(ccd->{
			ccd.getListaConciliacaoContaCorrenteExtrato().stream()
			.filter(cccde-> Uteis.isAtributoPreenchido(cccde.getCodigoSei()) && cccde.isExtratoConciliado())
			.forEach(cccde->{
				obj.getListaConciliacaoContaCorrenteDia().stream()
				.filter(p -> UteisData.getCompararDatas(p.getData(), ccd.getData()))
				.flatMap(p-> p.getListaConciliacaoContaCorrenteExtrato().stream())
				.filter(pp-> pp.getCodigoSei().equals(cccde.getCodigoSei())
						&& !cccde.getCodigoOfx().equals(pp.getCodigoOfx())
						&& !cccde.isExisteConciliacaoConjunta())
				.forEach(pp->{
					pp.anularDadosSei();
				});
			});
		});
		realizarCalculoTotalizadoresDias(obj, usuario);
		persistir(obj, false, conf, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarAtualizacaoConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataInicioSei()) || !Uteis.isAtributoPreenchido(obj.getDataFimSei()), "O campo Período de Apuração (Conciliação Conta Corrente) deve ser informado.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataInicioSei(), obj.getDataFimSei())>0, "A Data Inicial do Período de Apuração não pode ser maior que a Data Final.");
		Uteis.checkState(UteisData.getCompareData(obj.getDataFimSei(), obj.getDataInicioSei())<0, "A Data Final do Período de Apuração não pode ser menor que a Data Inicial.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getArquivoVO().getNome()) && !obj.isConciliacaoDepreciadaPorArquivo(), "O Upload Arquivo (OFX) dever ser informado.");
		if(!obj.isConciliacaoDepreciadaPorArquivo()) {
			realizarLeituraArquivoOXF(obj, conf, usuario);	
		}
		for (ConciliacaoContaCorrenteDiaVO objDia : obj.getListaConciliacaoContaCorrenteDia()) {
			for (ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato : objDia.getListaConciliacaoContaCorrenteExtrato()) {
				if (conciliacaoExtrato.isExtratoTransacaoExistente() && !conciliacaoExtrato.isExtratoSeiExistente()) {
					validarSeExisteExtratoContaCorrenteSei(obj, objDia, conciliacaoExtrato, usuario);
				}
			}	
		}
		realizarLeituraExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoPeriodo(obj, usuario);
		realizarTentativaDeConciliacaoAutomaticaPorValor(obj, usuario);
		if(!obj.isConciliacaoDepreciadaPorArquivo()) {
			removerConciliacaoContaCorrenteDiaNaoFazMaisParteDaMovimentacao(obj);
		}
		realizarCalculoTotalizadoresDias(obj, usuario);
	}

	
	private void realizarLeituraExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoPeriodo(ConciliacaoContaCorrenteVO obj, UsuarioVO usuarioVO) throws Exception {
		for (ConciliacaoContaCorrenteDiaVO objDia : obj.getListaConciliacaoContaCorrenteDia()) {
			buscarExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoDia(objDia, usuarioVO);
			removerExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoDia(objDia, usuarioVO);	
		}
		adicionarConciliacaoContaCorrenteDiaPeriodoMovimentacaoSei(obj, usuarioVO);
		Collections.sort(obj.getListaConciliacaoContaCorrenteDia(), Comparator.comparing(ConciliacaoContaCorrenteDiaVO::getData));
	}	

	private void adicionarConciliacaoContaCorrenteDiaPeriodoMovimentacaoSei(ConciliacaoContaCorrenteVO obj, UsuarioVO usuarioVO) throws Exception, ParseException {
		Date dataTemp = obj.getDataInicioSei();
		do {
			ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(dataTemp, obj);
			if (!Uteis.isAtributoPreenchido(objDia.getData())) {
				objDia.setData(dataTemp);
				objDia.setConciliacaoContaCorrente(obj);
				buscarExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoDia(objDia, usuarioVO);
				if(Uteis.isAtributoPreenchido(objDia.getListaConciliacaoContaCorrenteExtrato())) {
					addConciliacaoContaCorrenteDiaVO(objDia, obj);	
				}
			}
			dataTemp = UteisData.obterDataFuturaUsandoCalendar(dataTemp, 1);
		} while (UteisData.getCompareData(obj.getDataFimSei(),dataTemp) >= 0);
	}

	private void buscarExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoDia(ConciliacaoContaCorrenteDiaVO objDia, UsuarioVO usuarioVO) throws Exception {
		List<ExtratoContaCorrenteVO> listaExtratoContaCorrente = getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrenteProcessamentoPorContaCorrentePorDataAndCodigoQueNaoEstaoNaLista(objDia, usuarioVO);
		listaExtratoContaCorrente.stream().forEach(extratoContaCorrente -> {
			ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato = consultarConciliacaoContaCorrenteDiaExtratoSei(extratoContaCorrente.getTipoMovimentacaoFinanceira(), objDia);
			preencherExtratoContaCorrenteSei(objDia, conciliacaoExtrato, extratoContaCorrente);
			adicionarConciliacaoContaCorrenteDiaExtratoSeiAgrupado(conciliacaoExtrato, objDia);
		});
	}

	private void removerExtratoContaCorrenteQueNaoEstaNoArquivoPoremEstaNoDia(ConciliacaoContaCorrenteDiaVO objDia, UsuarioVO usuarioVO) throws Exception {
		List<ExtratoContaCorrenteVO> listaExtratoContaCorrente = getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrenteDesconsiderarConciliacaoBancaria(objDia.getListaCodigoExtratoContaCorrenteExistente(), true, usuarioVO);
		listaExtratoContaCorrente.stream().forEach(extratoContaCorrente -> {
			Iterator<ConciliacaoContaCorrenteDiaExtratoVO> i = objDia.getListaConciliacaoContaCorrenteExtrato().iterator();
			while (i.hasNext()) {
				ConciliacaoContaCorrenteDiaExtratoVO cccde = i.next();
				if (!cccde.isExtratoTransacaoExistente()
						&& cccde.isExtratoSeiExistente()
						&& cccde.isExisteExtratoContaCorrente(extratoContaCorrente)) {
					cccde.removerExtratoContaCorrente(extratoContaCorrente);
					cccde.setCodigoSei(removerCodigoOrigemSei(cccde.getCodigoSei(), extratoContaCorrente.getCodigo().toString()));
					cccde.setValorSei(Uteis.arrendondarForcando2CadasDecimais(cccde.getValorSei() - extratoContaCorrente.getValor()));
					objDia.setListaCodigoExtratoContaCorrenteExistente(removerCodigoOrigemSei(objDia.getListaCodigoExtratoContaCorrenteExistente(), extratoContaCorrente.getCodigo().toString()));
					if (cccde.getCodigoSei().isEmpty()) {
						i.remove();
					}
				}
			}
		});
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarCriacaoDeNovoDocumentosParaConciliacaoAutomatica(ConciliacaoContaCorrenteVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		for (ConciliacaoContaCorrenteDiaVO objDia : obj.getListaConciliacaoContaCorrenteDia()) {
			for (ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato : objDia.getListaConciliacaoContaCorrenteExtrato()) {
				if (conciliacaoExtrato.isExtratoTransacaoExistente() && !conciliacaoExtrato.isExtratoSeiExistente()) {
					validarSeExisteExtratoContaCorrenteSei(obj, objDia, conciliacaoExtrato, usuario);
					//Caso ainda a conciliacao continue sem extrato do lado do sei entao tenta se criar os parametros abaixo. Pedro Andrade.
					if(!conciliacaoExtrato.isExtratoSeiExistente() && (Uteis.isAtributoPreenchido(obj.getListaParametrizarEntradaItens())
							|| Uteis.isAtributoPreenchido(obj.getListaParametrizarSaidaItens()))) {
						ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro = obj.buscarParametrizacaoOperacaoAutomatica(conciliacaoExtrato.isTipoTransacaoEntrada(), conciliacaoExtrato.getLancamentoOfx());
						if (Uteis.isAtributoPreenchido(parametro.getNomeLancamento()) && parametro.getOrigemExtratoContaCorrenteEnum().isRecebimento() && conciliacaoExtrato.isTipoTransacaoEntrada()) {
							gerarContaReceberPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, parametro, configuracaoFinanceiroVO, usuario);
						} else if (Uteis.isAtributoPreenchido(parametro.getNomeLancamento()) && parametro.getOrigemExtratoContaCorrenteEnum().isPagamento() && conciliacaoExtrato.isTipoTransacaoSaida()) {
							gerarContaPagarPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, parametro, usuario);
						} else if (Uteis.isAtributoPreenchido(parametro.getNomeLancamento()) && parametro.getOrigemExtratoContaCorrenteEnum().isMovimentacaoFinanceira()) {
							gerarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, parametro, usuario);
						}
					}
				}
			}
		}
		realizarCalculoTotalizadoresDias(obj, usuario);
		persistir(obj, true, conf, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gerarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception {
		MovimentacaoFinanceiraVO mf = new MovimentacaoFinanceiraVO();
		mf.setSomenteContaDestino(false);
		mf.setContaCorrenteDestino(parametro.getContaCorrenteDestinoVO());
		mf.setContaCorrenteOrigem(parametro.getContaCorrenteVO());
		mf.setUnidadeEnsinoVO(parametro.getUnidadeEnsinoVO());
		mf.setData(objDia.getData());
		mf.setDescricao(parametro.getNomeLancamento());
		mf.setResponsavel(usuario);
		mf.setSituacao(SituacaoMovimentacaoFinanceiraEnum.FINALIZADA.getValor());

		MovimentacaoFinanceiraItemVO mfi = new MovimentacaoFinanceiraItemVO();
		mfi.setFormaPagamento(parametro.getFormaPagamentoVO());
		mfi.setMovimentacaoFinanceira(mf.getCodigo());
		if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
			mfi.setValor(conciliacaoExtrato.getValorOfx());
		} else {
			mfi.setValor(conciliacaoExtrato.getValorOfx() * -1);
		}
		mf.getMovimentacaoFinanceiraItemVOs().add(mfi);
		mf.setValor(mfi.getValor());
		gravarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, mf, parametro.getFormaPagamentoVO(), usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, MovimentacaoFinanceiraVO mf, FormaPagamentoVO formaPagamento, UsuarioVO usuario) throws Exception {
		mf.setPularMapaPendenciaMovimentacaoFinanceira(true);
		getFacadeFactory().getMovimentacaoFinanceiraFacade().persistir(mf, true, usuario);

		TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira = null;
		if (conciliacaoExtrato.isTipoTransacaoEntrada()) {
			tipoMovimentacaoFinanceira = TipoMovimentacaoFinanceira.ENTRADA;
		} else {
			tipoMovimentacaoFinanceira = TipoMovimentacaoFinanceira.SAIDA;
		}
		conciliacaoExtrato.setLancamentoSei(formaPagamento.getTipo_Apresentar());
		conciliacaoExtrato.setTipoMovimentacaoFinanceiraSei(tipoMovimentacaoFinanceira);
		conciliacaoExtrato.setSaldoRegistroSei(null);
		conciliacaoExtrato.setDataSei(conciliacaoExtrato.getDataOfx());

		if (tipoMovimentacaoFinanceira.isMovimentacaoEntrada()) {
			conciliacaoExtrato.setValorSei(Uteis.arrendondarForcando2CadasDecimais(mf.getValor()));
		} else {
			conciliacaoExtrato.setValorSei(Uteis.arrendondarForcando2CadasDecimais(mf.getValor() * -1));
		}
		conciliacaoExtrato.setListaExtratoContaCorrente(getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorOrigemPorCodigoOrigem(tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, mf.getCodigo(), usuario));
		conciliacaoExtrato.setCodigoSei(UteisTexto.converteListaEntidadeCampoCodigoParaString(conciliacaoExtrato.getListaExtratoContaCorrente()));
		if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei()) && Uteis.isAtributoPreenchido(objDia.getListaCodigoExtratoContaCorrenteExistente())) {
			objDia.setListaCodigoExtratoContaCorrenteExistente(objDia.getListaCodigoExtratoContaCorrenteExistente() + "," + conciliacaoExtrato.getCodigoSei());
		} else if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei())) {
			objDia.setListaCodigoExtratoContaCorrenteExistente(conciliacaoExtrato.getCodigoSei());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gerarContaReceberPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		
		ContaReceberVO contaReceber = getFacadeFactory().getContaReceberFacade().consultarPorPeriodoVencimentoPorUnidadeEnsinoPorValorPorSituacao(objDia.getData(), objDia.getData(), parametro.getUnidadeEnsinoVO().getCodigo(), conciliacaoExtrato.getValorOfx(), "AR", configuracaoFinanceiroVO, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if(!Uteis.isAtributoPreenchido(contaReceber)) {
			if (parametro.getTipoSacado().isFuncionario()) {
				contaReceber.setTipoPessoa(TipoPessoa.FUNCIONARIO.getValor());
				contaReceber.setFuncionario(parametro.getFuncionarioSacado());
				contaReceber.setPessoa(parametro.getFuncionarioSacado().getPessoa());
			} else if (parametro.getTipoSacado().isParceiro()) {
				contaReceber.setTipoPessoa(TipoPessoa.PARCEIRO.getValor());
				contaReceber.setParceiroVO(parametro.getParceiroSacado());
			} else if (parametro.getTipoSacado().isFornecedor()) {
				contaReceber.setTipoPessoa(TipoPessoa.FORNECEDOR.getValor());
				contaReceber.setFornecedor(parametro.getFornecedorSacado());
			}

			contaReceber.setData(objDia.getData());
			contaReceber.setDataOriginalVencimento(objDia.getData());
			contaReceber.setDataVencimento(objDia.getData());
			contaReceber.setDataCompetencia(objDia.getData());
			contaReceber.setContaCorrente(parametro.getContaCorrenteVO().getCodigo());
			contaReceber.setContaCorrenteVO(parametro.getContaCorrenteVO());
			contaReceber.setUnidadeEnsino(parametro.getUnidadeEnsinoVO());
			contaReceber.setUnidadeEnsinoFinanceira(parametro.getUnidadeEnsinoVO());
			contaReceber.setCentroReceita(parametro.getCentroReceitaVO());
			contaReceber.setValor(conciliacaoExtrato.getValorOfx());
			contaReceber.setValorBaseContaReceber(conciliacaoExtrato.getValorOfx());
			contaReceber.setDescricaoPagamento("Recebimento gerado automático para lançamento da conciliação bancária.");
			contaReceber.setNrDocumento(conciliacaoExtrato.getDocumentoOfx());	
		}
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(parametro.getFormaPagamentoVO());
		formaPagamentoNegociacaoRecebimentoVO.setContaCorrente(parametro.getContaCorrenteVO());
		gravarContaReceberPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, contaReceber, formaPagamentoNegociacaoRecebimentoVO, configuracaoFinanceiroVO, usuario);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarContaReceberPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if(!Uteis.isAtributoPreenchido(contaReceber)) {
			contaReceber.setTipoOrigem(TipoOrigemContaReceber.OUTROS.valor);
			contaReceber.setCodOrigem(conciliacaoExtrato.getCodigo().toString());
			getFacadeFactory().getContaReceberFacade().incluir(contaReceber, false, configuracaoFinanceiroVO, usuario);
		}
		gerarNegociacaoContaReceberPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, contaReceber, formaPagamentoNegociacaoRecebimentoVO, configuracaoFinanceiroVO, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarNegociacaoContaReceberPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		negociacaoRecebimentoVO.setValorTotalRecebimento(0.0);
		negociacaoRecebimentoVO.setData(objDia.getData());

		if (contaReceber.getTipoFuncionario()) {
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.FUNCIONARIO.getValor());
			negociacaoRecebimentoVO.setMatricula(contaReceber.getFuncionario().getMatricula());
			negociacaoRecebimentoVO.setPessoa(contaReceber.getFuncionario().getPessoa());
		} else if (contaReceber.getTipoAluno()) {
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
			negociacaoRecebimentoVO.setMatriculaVO(contaReceber.getMatriculaAluno());
			negociacaoRecebimentoVO.setPessoa(contaReceber.getMatriculaAluno().getAluno());
		} else if (contaReceber.getTipoResponsavelFinanceiro()) {
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
			negociacaoRecebimentoVO.setPessoa(contaReceber.getPessoa());
		} else if (contaReceber.getTipoParceiro()) {
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.PARCEIRO.getValor());
			negociacaoRecebimentoVO.setParceiroVO(contaReceber.getParceiroVO());
		} else if (contaReceber.getTipoFornecedor()) {
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.FORNECEDOR.getValor());
			negociacaoRecebimentoVO.setFornecedor(contaReceber.getFornecedor());
		}

		ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceber);
		contaReceberNegociacaoRecebimentoVO.setNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		negociacaoRecebimentoVO.adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.calcularTotal(configuracaoFinanceiroVO, usuario);

		formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(negociacaoRecebimentoVO.getValorTotal());
		formaPagamentoNegociacaoRecebimentoVO.setDataCredito(objDia.getData());
		negociacaoRecebimentoVO.adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(formaPagamentoNegociacaoRecebimentoVO);

		negociacaoRecebimentoVO.setObservacao("Recebimento gerado pela o lançamento da conciliação bancária.");
		negociacaoRecebimentoVO.setResponsavel(usuario);
		negociacaoRecebimentoVO.setUnidadeEnsino(contaReceber.getUnidadeEnsinoFinanceira());
		negociacaoRecebimentoVO.setContaCorrenteCaixa(formaPagamentoNegociacaoRecebimentoVO.getContaCorrente());
		getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, false, usuario);

		conciliacaoExtrato.setTipoMovimentacaoFinanceiraSei(TipoMovimentacaoFinanceira.ENTRADA);
		conciliacaoExtrato.setSaldoRegistroSei(null);
		conciliacaoExtrato.setDataSei(conciliacaoExtrato.getDataOfx());
		conciliacaoExtrato.setLancamentoSei(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo_Apresentar());
		conciliacaoExtrato.setValorSei(Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotal()));
		conciliacaoExtrato.setListaExtratoContaCorrente(getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorOrigemPorCodigoOrigem(TipoMovimentacaoFinanceira.ENTRADA, OrigemExtratoContaCorrenteEnum.RECEBIMENTO, negociacaoRecebimentoVO.getCodigo(), usuario));

		conciliacaoExtrato.setCodigoSei(UteisTexto.converteListaEntidadeCampoCodigoParaString(conciliacaoExtrato.getListaExtratoContaCorrente()));
		if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei()) && Uteis.isAtributoPreenchido(objDia.getListaCodigoExtratoContaCorrenteExistente())) {
			objDia.setListaCodigoExtratoContaCorrenteExistente(objDia.getListaCodigoExtratoContaCorrenteExistente() + "," + conciliacaoExtrato.getCodigoSei());
		} else if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei())) {
			objDia.setListaCodigoExtratoContaCorrenteExistente(conciliacaoExtrato.getCodigoSei());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gerarContaPagarPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception {
		ContaPagarVO contaPagar = getFacadeFactory().getContaPagarFacade().consultarPorPeriodoVencimentoPorUnidadeEnsinoPorValorPorSituacao(objDia.getData(), objDia.getData(), parametro.getUnidadeEnsinoVO().getCodigo(), conciliacaoExtrato.getValorOfx() * -1, "AP", false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if(!Uteis.isAtributoPreenchido(contaPagar)) {
			if (parametro.getTipoSacado().isFuncionario()) {
				contaPagar.setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
				contaPagar.setFuncionario(parametro.getFuncionarioSacado());
			} else if (parametro.getTipoSacado().isParceiro()) {
				contaPagar.setTipoSacado(TipoSacado.PARCEIRO.getValor());
				contaPagar.setParceiro(parametro.getParceiroSacado());
			} else if (parametro.getTipoSacado().isFornecedor()) {
				contaPagar.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
				contaPagar.setFornecedor(parametro.getFornecedorSacado());
			} else if (parametro.getTipoSacado().isBanco()) {
				contaPagar.setTipoSacado(TipoSacado.BANCO.getValor());
				contaPagar.setBanco(parametro.getBancoSacado());
			}
			contaPagar.setData(objDia.getData());
			contaPagar.setDataFatoGerador(objDia.getData());
			contaPagar.setDataVencimento(objDia.getData());
			contaPagar.setDescricao("Pagamento gerado automático para lançamento da conciliação bancária.");
			contaPagar.setResponsavel(usuario);
			contaPagar.setResponsavelFinanceiro(usuario.getPessoa());
			contaPagar.setSituacao("AP");
			contaPagar.setValor(conciliacaoExtrato.getValorOfx() * -1);
			contaPagar.setJuro(0.0);
			contaPagar.setMulta(0.0);
			contaPagar.setNrDocumento(conciliacaoExtrato.getDocumentoOfx());
			contaPagar.setContaCorrente(parametro.getContaCorrenteVO());
			contaPagar.setUnidadeEnsino(parametro.getUnidadeEnsinoVO());

			CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
			cro.setCentroResultadoAdministrativo(parametro.getCentroResultadoAdministrativo());
			cro.setCategoriaDespesaVO(parametro.getCategoriaDespesaVO());
			cro.setUnidadeEnsinoVO(parametro.getUnidadeEnsinoVO());
			cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
			cro.setQuantidade(1.0);
			cro.setPorcentagem(100.00);
			cro.setValor(contaPagar.getValor());
			contaPagar.getListaCentroResultadoOrigemVOs().add(cro);
		}
		FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
		formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(parametro.getFormaPagamentoVO());
		formaPagamentoNegociacaoPagamentoVO.setContaCorrente(parametro.getContaCorrenteVO());
		gravarContaPagarPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, contaPagar, formaPagamentoNegociacaoPagamentoVO, usuario);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarContaPagarPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
		if(!Uteis.isAtributoPreenchido(contaPagar)) {
			contaPagar.setCodOrigem(conciliacaoExtrato.getCodigo().toString());
			contaPagar.setTipoOrigem(OrigemContaPagar.CONCILIACAO_BANCARIA.getValor());
			getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuario);
		}
		gerarNegociacaoContaPagarPorConciliacaoContaCorrenteDiaExtrato(objDia, conciliacaoExtrato, contaPagar, formaPagamentoNegociacaoPagamentoVO, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarNegociacaoContaPagarPorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
		NegociacaoPagamentoVO negociacaoPagamentoVO = getFacadeFactory().getNegociacaoPagamentoFacade().gerarNegociacaoContaPagarPorContaPagarPorFormaPagamentoNegociacaoPagamentoVO(contaPagar, formaPagamentoNegociacaoPagamentoVO, objDia.getData(), true, usuario);
		conciliacaoExtrato.setTipoMovimentacaoFinanceiraSei(TipoMovimentacaoFinanceira.SAIDA);
		conciliacaoExtrato.setSaldoRegistroSei(null);
		conciliacaoExtrato.setDataSei(conciliacaoExtrato.getDataOfx());
		conciliacaoExtrato.setLancamentoSei(formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo_Apresentar());
		conciliacaoExtrato.setValorSei(Uteis.arrendondarForcando2CadasDecimais(negociacaoPagamentoVO.getValorTotal() * -1));
		conciliacaoExtrato.setListaExtratoContaCorrente(getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorOrigemPorCodigoOrigem(TipoMovimentacaoFinanceira.SAIDA, OrigemExtratoContaCorrenteEnum.PAGAMENTO, negociacaoPagamentoVO.getCodigo(), usuario));

		conciliacaoExtrato.setCodigoSei(UteisTexto.converteListaEntidadeCampoCodigoParaString(conciliacaoExtrato.getListaExtratoContaCorrente()));
		if (Uteis.isAtributoPreenchido(objDia.getListaCodigoExtratoContaCorrenteExistente())) {
			objDia.setListaCodigoExtratoContaCorrenteExistente(objDia.getListaCodigoExtratoContaCorrenteExistente() + "," + conciliacaoExtrato.getCodigoSei());
		} else {
			objDia.setListaCodigoExtratoContaCorrenteExistente(conciliacaoExtrato.getCodigoSei());
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarTentativaDeConciliacaoAutomaticaPorValor(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) {
		obj.getListaConciliacaoContaCorrenteDia()
		.stream()
		.forEach(objDia -> {
			Iterator<ConciliacaoContaCorrenteDiaExtratoVO> j = objDia.getListaConciliacaoContaCorrenteExtrato().iterator();
			while (j.hasNext()) {
				ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei = j.next();
				if (!conciliacaoExtratoSei.isExtratoTransacaoExistente() 
						&& conciliacaoExtratoSei.isExtratoSeiExistente()
						&& realizarBuscaConciliacaoAutomaticaPorValoresIguais(conciliacaoExtratoSei, objDia, obj, usuario)) {
					j.remove();
				}
			}
		});
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private boolean realizarBuscaConciliacaoAutomaticaPorValoresIguais(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei, ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) {
		Optional<ConciliacaoContaCorrenteDiaExtratoVO> findFirst = objDia.getListaConciliacaoContaCorrenteExtrato()
				.stream()
				.filter(p -> (p.isExtratoTransacaoExistente() 
						     && !p.isExtratoSeiExistente() 
						     && p.getValorOfx().equals(conciliacaoExtratoSei.getValorSei()) 
						     && ((p.isTipoTransacaoEntrada() && conciliacaoExtratoSei.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) 
						    		 ||
								(p.isTipoTransacaoSaida() && conciliacaoExtratoSei.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida())))
						)
				.findFirst();
		findFirst.ifPresent(p -> p.preencherDadosSei(conciliacaoExtratoSei));
		return findFirst.isPresent();

	}
	
	private void removerConciliacaoContaCorrenteDiaNaoFazMaisParteDaMovimentacao(ConciliacaoContaCorrenteVO obj) throws Exception {
//		boolean isRemoveuConciliacaoDia = false;
		Iterator<ConciliacaoContaCorrenteDiaVO> i = obj.getListaConciliacaoContaCorrenteDia().iterator();
		while (i.hasNext()) {
			ConciliacaoContaCorrenteDiaVO objDia = (ConciliacaoContaCorrenteDiaVO) i.next();
			if(!UteisData.isDataDentroDoPeriodo(obj.getDataInicioSei(), obj.getDataFimSei(), objDia.getData())) {
				i.remove();
//				isRemoveuConciliacaoDia = true;
			}
		}
//		if(isRemoveuConciliacaoDia) {
//			obj.zeraTotalizadoresCreditoDebitoOFX();
//			obj.zeraTotalizadoresCreditoDebitoSEI();
//			obj.getListaConciliacaoContaCorrenteDia().stream().forEach(cccd->{
//				obj.setTotalValorOfxCredito(obj.getTotalValorOfxCredito() + cccd.getTotalValorOfxCredito());
//				obj.setTotalValorOfxDebito(obj.getTotalValorOfxDebito() + cccd.getTotalValorOfxDebito());
//				obj.setTotalValorSeiCredito(obj.getTotalValorSeiCredito() + cccd.getTotalValorSeiCredito());
//				obj.setTotalValorSeiDebito(obj.getTotalValorSeiDebito() + cccd.getTotalValorSeiDebito());
//			});			
//			obterSaldoOXFAposRemoverConciliacaoContaCorrenteDia(obj);
//			obterSaldoSeiAposRemoverConciliacaoContaCorrenteDia(obj);	
//		}
	}

//	private void obterSaldoSeiAposRemoverConciliacaoContaCorrenteDia(ConciliacaoContaCorrenteVO obj) {
//		Optional<ConciliacaoContaCorrenteDiaVO> objDiaSei = obj.getListaConciliacaoContaCorrenteDia()
//				.stream()
//				.filter(cccd->cccd.getListaConciliacaoContaCorrenteExtrato().stream().anyMatch(cccde->cccde.isExtratoSeiExistente()))
//				.max(Comparator.comparing(p-> p.getData()));
//		if(objDiaSei.isPresent()) {
//			for (int j = objDiaSei.get().getListaConciliacaoContaCorrenteExtrato().size(); j >= 1;) {
//				ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei = objDiaSei.get().getListaConciliacaoContaCorrenteExtrato().get(--j);
//				if(conciliacaoExtratoSei != null && conciliacaoExtratoSei.isExtratoSeiExistente()) {
//					obj.setTotalValorSei(conciliacaoExtratoSei.getSaldoRegistroSei());
//					break;
//				}
//			}			
//		}
//	}
//
//	private void obterSaldoOXFAposRemoverConciliacaoContaCorrenteDia(ConciliacaoContaCorrenteVO obj) {
//		Optional<ConciliacaoContaCorrenteDiaVO> objDiaOfx = obj.getListaConciliacaoContaCorrenteDia()
//				.stream()
//				.filter(cccd-> cccd.getListaConciliacaoContaCorrenteExtrato().stream().anyMatch(cccde->cccde.isExtratoTransacaoExistente()))
//				.max(Comparator.comparing(p-> p.getData()));
//		if(objDiaOfx.isPresent()) {
//			Optional<ConciliacaoContaCorrenteDiaExtratoVO> conciliacaoExtratoOfx = objDiaOfx.get().getListaConciliacaoContaCorrenteExtrato().
//					stream()
//					.max(Comparator.comparing(p-> p.getCodigoOfx()));
//			if(conciliacaoExtratoOfx.isPresent()) {
//				obj.setTotalValorOfx(conciliacaoExtratoOfx.get().getSaldoRegistroOfx());	
//			}	
//		}
//	}
	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarCalculoTotalizadoresDias(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) {
		try {
			if(obj.getBanco().isSafra()) {
				realizarCalculoTotalizadoresDiasOfxSafra(obj);				
			}else {
				realizarCalculoTotalizadoresDiasOfx(obj);				
			}
			realizarCalculoTotalizadoresDiasSei(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void realizarCalculoTotalizadoresDiasOfx(ConciliacaoContaCorrenteVO obj) throws ParseException {
		Optional<ConciliacaoContaCorrenteDiaVO> dataMax = obj.getListaConciliacaoContaCorrenteDia()
				.stream()
				.filter(cccd-> cccd.getListaConciliacaoContaCorrenteExtrato().stream().anyMatch(cccde->cccde.isExtratoTransacaoExistente()))
				.max(Comparator.comparing(p-> p.getData()));
		//Uteis.checkState(!dataMax.isPresent() || dataMax.get().getData() == null, "Não foi encontrado a data Maxima para calcular o sado anterior das movimentações do arquivo OFX.");
		if(dataMax.isPresent() && dataMax.get().getData() != null) {
			obj.setUltimoRegistroDia(true);
			obj.setUltimoExtratoTransacaoDia(true);
			final HashMap<String, Double> mapaDeTotalizadoresSaldos = new HashMap<>();
			obj.setTotalValorOfx(obj.getValorBalancoOfx());
			mapaDeTotalizadoresSaldos.put("valorTotalOfx", obj.getValorBalancoOfx());
			obj.zeraTotalizadoresCreditoDebitoOFX();
			for (int i = obj.getListaConciliacaoContaCorrenteDia().size(); i >= 1;) {
				ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente = obj.getListaConciliacaoContaCorrenteDia().get(--i);
				if (UteisData.getCompareData(dataMax.get().getData(), conciliacaoDiaExistente.getData())>=0) {
					conciliacaoDiaExistente.zeraTotalizadoresCreditoDebitoOFX();
					for (int j = conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato().size(); j >= 1;) {
						ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente = conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato().get(--j);
						mapaDeTotalizadoresSaldos.put("valorTotalOfx", realizarCalculoTotalizadoresDiasOfx(mapaDeTotalizadoresSaldos.get("valorTotalOfx"), obj, conciliacaoDiaExistente, conciliacaoExtratoExistente));
					}			
					conciliacaoDiaExistente.setSaldoAnteriorOfx(mapaDeTotalizadoresSaldos.get("valorTotalOfx") - obj.getSaldoRegistroOfxTemporario());
					// regra feita para ajustar o valores quebrados de zero negativo
					if (conciliacaoDiaExistente.getSaldoAnteriorOfx() < 0.0 && conciliacaoDiaExistente.getSaldoAnteriorOfx() > -0.1) {
						conciliacaoDiaExistente.setSaldoAnteriorOfx(conciliacaoDiaExistente.getSaldoAnteriorOfx() * -1);
					}
					if (obj.isUltimoRegistroDia()) {
						conciliacaoDiaExistente.setSaldoFinalOfx(obj.getValorBalancoOfx());
						mapaDeTotalizadoresSaldos.put("saldoAnteriorOfx", conciliacaoDiaExistente.getSaldoAnteriorOfx());
						obj.setUltimoRegistroDia(false);
					} else {
						conciliacaoDiaExistente.setSaldoFinalOfx(mapaDeTotalizadoresSaldos.get("saldoAnteriorOfx"));
						mapaDeTotalizadoresSaldos.put("saldoAnteriorOfx", conciliacaoDiaExistente.getSaldoAnteriorOfx());
					}
				}
			}
			for (ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente : obj.getListaConciliacaoContaCorrenteDia()) {
				if (UteisData.getCompareData(conciliacaoDiaExistente.getData(), dataMax.get().getData()) > 0) {
					conciliacaoDiaExistente.setSaldoFinalOfx(obj.getValorBalancoOfx());
					conciliacaoDiaExistente.setSaldoAnteriorOfx(obj.getValorBalancoOfx());
				}
			}
		}
	}
	
	private Double realizarCalculoTotalizadoresDiasOfx(Double valorTotalOfx, ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente) {
		if (conciliacaoExtratoExistente.isExtratoTransacaoExistente()) {
			if (obj.isUltimoExtratoTransacaoDia()) {
				conciliacaoExtratoExistente.setSaldoRegistroOfx(valorTotalOfx);
				obj.setUltimoExtratoTransacaoDia(false);
			} else {
				conciliacaoExtratoExistente.setSaldoRegistroOfx(valorTotalOfx - obj.getSaldoRegistroOfxTemporario());
				valorTotalOfx = valorTotalOfx - obj.getSaldoRegistroOfxTemporario();
			}
			if (conciliacaoExtratoExistente.isTipoTransacaoEntrada()) {
				conciliacaoDiaExistente.setTotalValorOfxCredito(conciliacaoDiaExistente.getTotalValorOfxCredito() + conciliacaoExtratoExistente.getValorOfx());
				obj.setTotalValorOfxCredito(obj.getTotalValorOfxCredito() + conciliacaoExtratoExistente.getValorOfx());
			} else if (conciliacaoExtratoExistente.isTipoTransacaoSaida()) {
				conciliacaoDiaExistente.setTotalValorOfxDebito(conciliacaoDiaExistente.getTotalValorOfxDebito() + conciliacaoExtratoExistente.getValorOfx());
				obj.setTotalValorOfxDebito(obj.getTotalValorOfxDebito() + conciliacaoExtratoExistente.getValorOfx());
			}
			obj.setSaldoRegistroOfxTemporario(conciliacaoExtratoExistente.getValorOfx());
		}
		return valorTotalOfx;
	}
	
	private void realizarCalculoTotalizadoresDiasSei(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		Optional<ConciliacaoContaCorrenteDiaVO> cccd = obj.getListaConciliacaoContaCorrenteDia().stream().min(Comparator.comparing(p-> p.getData()));
		//Uteis.checkState(!cccd.isPresent() || cccd.get().getData() == null, "Não foi encontrado a data mínima para calcular o sado anterior das movimentações do Sei.");
		if(cccd.isPresent() && cccd.get().getData() != null) {
			obj.setTotalValorSei(getFacadeFactory().getExtratoContaCorrenteFacade().consultarSaldoAnterior(obj.getContaCorrenteArquivo(), obj.getDigitoContaCorrenteArquivo(), cccd.get().getData(), usuario));
			obj.zeraTotalizadoresCreditoDebitoSEI();
			for (ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente : obj.getListaConciliacaoContaCorrenteDia()) {
				conciliacaoDiaExistente.zeraTotalizadoresCreditoDebitoSEI();
				conciliacaoDiaExistente.setSaldoAnteriorSei(obj.getTotalValorSei());
				ConciliacaoContaCorrenteVO objConciliado = validarUnicidadeConciliacaoContaCorrente(obj, conciliacaoDiaExistente.getData(), conciliacaoDiaExistente.getData(), usuario);
				if(Uteis.isAtributoPreenchido(objConciliado)) {
					obj.setTotalValorSei(Uteis.arrendondarForcando2CadasDecimais(obj.getTotalValorSei() + getFacadeFactory().getExtratoContaCorrenteFacade().consultarSaldoPorConciliacaoContaCorrentePorDataMovimentacao(objConciliado, conciliacaoDiaExistente.getData(), usuario)));
				}else {
					for (ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente : conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato()) {
						realizarCalculoTotalizadoresDiasSei(obj, conciliacaoDiaExistente, conciliacaoExtratoExistente);
					}	
				}
				conciliacaoDiaExistente.setSaldoFinalSei(obj.getTotalValorSei());
			}
		}
	}

	
	private void realizarCalculoTotalizadoresDiasSei(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente) {
		if (conciliacaoExtratoExistente.isExtratoSeiExistente()) {
			conciliacaoExtratoExistente.setSaldoRegistroSei(Uteis.arrendondarForcando2CadasDecimais(obj.getTotalValorSei() + conciliacaoExtratoExistente.getValorSei()));
			obj.setTotalValorSei(Uteis.arrendondarForcando2CadasDecimais(obj.getTotalValorSei() + conciliacaoExtratoExistente.getValorSei()));			
			if (Uteis.isAtributoPreenchido(conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei()) &&  conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) {
				conciliacaoDiaExistente.setTotalValorSeiCredito(Uteis.arrendondarForcando2CadasDecimais(conciliacaoDiaExistente.getTotalValorSeiCredito() + conciliacaoExtratoExistente.getValorSei()));
				obj.setTotalValorSeiCredito(Uteis.arrendondarForcando2CadasDecimais(obj.getTotalValorSeiCredito() + conciliacaoExtratoExistente.getValorSei()));
			} else if (Uteis.isAtributoPreenchido(conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei()) && conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida()) {
				conciliacaoDiaExistente.setTotalValorSeiDebito(Uteis.arrendondarForcando2CadasDecimais(conciliacaoDiaExistente.getTotalValorSeiDebito() + conciliacaoExtratoExistente.getValorSei()));
				obj.setTotalValorSeiDebito(Uteis.arrendondarForcando2CadasDecimais(obj.getTotalValorSeiDebito() + conciliacaoExtratoExistente.getValorSei()));
			}
		}
	}
	
	private void realizarCalculoTotalizadoresDiasOfxSafra(ConciliacaoContaCorrenteVO obj) throws ParseException {
		final HashMap<String, Double> mapaDeTotalizadoresSaldos = new HashMap<>();
		Optional<ConciliacaoContaCorrenteDiaVO> dataMin = obj.getListaConciliacaoContaCorrenteDia()
				.stream()
				.filter(cccd-> cccd.getListaConciliacaoContaCorrenteExtrato().stream().anyMatch(cccde->cccde.isExtratoTransacaoExistente()))
				.min(Comparator.comparing(p-> p.getData()));
		//Uteis.checkState(!dataMin.isPresent() || dataMin.get().getData() == null, "Não foi encontrado a data mínima para calcular o sado anterior das movimentações do arquivo OFX.");
		if(dataMin.isPresent() && dataMin.get().getData() != null) {
			Double saldoTempFinalOfxPrimeiroDia = obj.getValorBalancoOfx();
			mapaDeTotalizadoresSaldos.put("valorTotalOfx", 0.0);
			obj.zeraTotalizadoresCreditoDebitoOFX();
			for (ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente : obj.getListaConciliacaoContaCorrenteDia()) {
				conciliacaoDiaExistente.zeraTotalizadoresCreditoDebitoOFX();
				if (UteisData.getCompararDatas(dataMin.get().getData(), conciliacaoDiaExistente.getData())) {
					conciliacaoDiaExistente.setUltimoExtratoTransacaoDia(true);
					conciliacaoDiaExistente.setSaldoFinalOfx(saldoTempFinalOfxPrimeiroDia);
					conciliacaoDiaExistente.setSaldoAnteriorOfx(0.0);
					for (int j = conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato().size(); j >= 1;) {
						ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente = conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato().get(--j);
						saldoTempFinalOfxPrimeiroDia = realizarCalculoTotalizadoresDiasOfxSafra(saldoTempFinalOfxPrimeiroDia, obj, conciliacaoDiaExistente, conciliacaoExtratoExistente, true);
						obj.setSaldoRegistroOfxTemporario(conciliacaoExtratoExistente.getValorOfx());
					}
					conciliacaoDiaExistente.setSaldoAnteriorOfx(saldoTempFinalOfxPrimeiroDia - obj.getSaldoRegistroOfxTemporario());
					mapaDeTotalizadoresSaldos.put("saldoAnteriorOfxMenorPrimeiroDia", conciliacaoDiaExistente.getSaldoAnteriorOfx());
					// regra feita para ajustar o valores quebrados de zero negativo
					if (conciliacaoDiaExistente.getSaldoAnteriorOfx() < 0.0 && conciliacaoDiaExistente.getSaldoAnteriorOfx() > -0.1) {
						conciliacaoDiaExistente.setSaldoAnteriorOfx(conciliacaoDiaExistente.getSaldoAnteriorOfx() * -1);
					}
					mapaDeTotalizadoresSaldos.put("valorTotalOfx", obj.getValorBalancoOfx());	
				} else {
					conciliacaoDiaExistente.setUltimoExtratoTransacaoDia(false);
					conciliacaoDiaExistente.setSaldoAnteriorOfx(mapaDeTotalizadoresSaldos.get("valorTotalOfx"));
					for (ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente : conciliacaoDiaExistente.getListaConciliacaoContaCorrenteExtrato()) {
						obj.setSaldoRegistroOfxTemporario(conciliacaoExtratoExistente.getValorOfx());
						mapaDeTotalizadoresSaldos.put("valorTotalOfx", realizarCalculoTotalizadoresDiasOfxSafra(mapaDeTotalizadoresSaldos.get("valorTotalOfx"), obj, conciliacaoDiaExistente, conciliacaoExtratoExistente, false));
					}
					conciliacaoDiaExistente.setSaldoFinalOfx(mapaDeTotalizadoresSaldos.get("valorTotalOfx"));
				}
			}
			obj.setTotalValorOfx(mapaDeTotalizadoresSaldos.get("valorTotalOfx"));
			for (ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente : obj.getListaConciliacaoContaCorrenteDia()) {
				if (UteisData.getCompareData(conciliacaoDiaExistente.getData(), dataMin.get().getData()) < 0) {
					conciliacaoDiaExistente.setSaldoFinalOfx(mapaDeTotalizadoresSaldos.get("saldoAnteriorOfxMenorPrimeiroDia"));
					conciliacaoDiaExistente.setSaldoAnteriorOfx(mapaDeTotalizadoresSaldos.get("saldoAnteriorOfxMenorPrimeiroDia"));
				}
			}	
		}
	}
	
	private Double realizarCalculoTotalizadoresDiasOfxSafra(Double valorTotalOfx , ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaVO conciliacaoDiaExistente, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente, boolean isCalculoPrimeiroDia) {
		if (conciliacaoExtratoExistente.isExtratoTransacaoExistente()) {
			if (conciliacaoDiaExistente.isUltimoExtratoTransacaoDia()) {
				conciliacaoExtratoExistente.setSaldoRegistroOfx(valorTotalOfx);
				conciliacaoDiaExistente.setUltimoExtratoTransacaoDia(false);
			} else {
				if(isCalculoPrimeiroDia) {
					conciliacaoExtratoExistente.setSaldoRegistroOfx(valorTotalOfx - obj.getSaldoRegistroOfxTemporario());
				}else {
					conciliacaoExtratoExistente.setSaldoRegistroOfx(valorTotalOfx + obj.getSaldoRegistroOfxTemporario());
				}
				valorTotalOfx = conciliacaoExtratoExistente.getSaldoRegistroOfx();
			}
			
			if (conciliacaoExtratoExistente.isTipoTransacaoEntrada()) {
				conciliacaoDiaExistente.setTotalValorOfxCredito(conciliacaoDiaExistente.getTotalValorOfxCredito() + conciliacaoExtratoExistente.getValorOfx());
				obj.setTotalValorOfxCredito(obj.getTotalValorOfxCredito() + conciliacaoExtratoExistente.getValorOfx());
			} else if (conciliacaoExtratoExistente.isTipoTransacaoSaida()) {
				conciliacaoDiaExistente.setTotalValorOfxDebito(conciliacaoDiaExistente.getTotalValorOfxDebito() + conciliacaoExtratoExistente.getValorOfx());
				obj.setTotalValorOfxDebito(obj.getTotalValorOfxDebito() + conciliacaoExtratoExistente.getValorOfx());
			}			
		}
		return valorTotalOfx;
	}
	
	
	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherDetalheExtratoContaCorrente(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(conciliacaoContaCorrenteDiaExtratoVO.getCodigoSei()) && (conciliacaoContaCorrenteDiaExtratoVO.getListaExtratoContaCorrente().isEmpty() || (StringUtils.countMatches(conciliacaoContaCorrenteDiaExtratoVO.getCodigoSei(), ",") + 1) != conciliacaoContaCorrenteDiaExtratoVO.getListaExtratoContaCorrente().size())) {
			conciliacaoContaCorrenteDiaExtratoVO.setListaExtratoContaCorrente(getFacadeFactory().getExtratoContaCorrenteFacade().consultarExtratoContaCorrentePorListaCodigos(conciliacaoContaCorrenteDiaExtratoVO, usuario));
		}
	}

	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void excluirConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario) {
		Uteis.checkState(conciliacaoExtratoAlterado.isExtratoTransacaoExistente(), "Não é possivel excluir um conciliação extrado dia que tenha dados ofx(Banco).");
		Uteis.checkState(conciliacaoExtratoAlterado.isExisteConciliacaoConjunta(), "Não é possivel excluir um conciliação extrado dia que esta conciliado conjunta.");
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoAlterado.getDataSei(), obj);
		Iterator<ConciliacaoContaCorrenteDiaExtratoVO> i = objDia.getListaConciliacaoContaCorrenteExtrato().iterator();
		while (i.hasNext()) {
			ConciliacaoContaCorrenteDiaExtratoVO cccde =  i.next();
			if(cccde.getCodigoSei().equals(conciliacaoExtratoAlterado.getCodigoSei()) && !cccde.isExtratoConciliado()){
				Splitter.on(',')
				.splitToList(cccde.getCodigoSei())
				.stream()
				.forEach(p->objDia.setListaCodigoExtratoContaCorrenteExistente(removerCodigoOrigemSei(objDia.getListaCodigoExtratoContaCorrenteExistente(), p)));
				if(!Uteis.isAtributoPreenchido(cccde.getCodigo())){
					objDia.getListaConciliacaoContaCorrenteExtratoExcluir().add(cccde);	
				}
				i.remove();
				break;
			}
		}	
		if(!Uteis.isAtributoPreenchido(objDia.getListaConciliacaoContaCorrenteExtrato())) {
			removerConciliacaoContaCorrenteDiaVO(objDia, obj);
		}
		realizarCalculoTotalizadoresDias(obj, usuario);	
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtratoConjunta(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario) {
		boolean existeConciliacaoExtratoDiaNaoConciliado = false;
		String codigoSeiTemp = conciliacaoExtratoAlterado.getCodigoSei();
		Double valorSeiTemp = conciliacaoExtratoAlterado.getTotalValorExtratoContaCorrente();		
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoAlterado.getDataSei(), obj);
		ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
		novoConciliacaoExtrato.preencherDadosSei(conciliacaoExtratoAlterado);
		for (ConciliacaoContaCorrenteDiaExtratoVO cccde : objDia.getListaConciliacaoContaCorrenteExtrato()) {
			if(cccde.getCodigoSei().equals(codigoSeiTemp) && cccde.isExisteConciliacaoConjunta()){
				cccde.getListaConciliacaoContaConjuntaVO().clear();
				if(cccde.isExtratoConciliado()){
					cccde.anularDadosSei();	
				}else{
					existeConciliacaoExtratoDiaNaoConciliado = true;
				}
			}
		}
		if(existeConciliacaoExtratoDiaNaoConciliado){
			objDia.getListaConciliacaoContaCorrenteExtrato().stream()
			.filter(p -> p.getCodigoSei().equals(codigoSeiTemp) && !p.isExtratoConciliado())
			.forEach(p ->p.setValorSei(valorSeiTemp));
		}else{
			novoConciliacaoExtrato.setConciliacaoContaCorrenteDia(objDia);
			novoConciliacaoExtrato.setValorSei(valorSeiTemp);
			objDia.getListaConciliacaoContaCorrenteExtrato().add(novoConciliacaoExtrato);	
		}
		realizarCalculoTotalizadoresDias(obj, usuario);	
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, ExtratoContaCorrenteVO extratoContaCorrente, UsuarioVO usuario) throws Exception {
		ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoAlterado.getDataSei(), obj);
		removerExtratoContaCorrenteParaDesagrupamentoConciliacaoContaCorrenteDiaExtrato(conciliacaoExtratoAlterado, novoConciliacaoExtrato, objDia, extratoContaCorrente);
		conciliacaoExtratoAlterado.removerExtratoContaCorrente(extratoContaCorrente);
		conciliacaoExtratoAlterado.atualizarListaTotalizadores();
		if (conciliacaoExtratoAlterado.getListaExtratoContaCorrente().stream().noneMatch(p -> p.getFormaPagamento().getCodigo().equals(extratoContaCorrente.getFormaPagamento().getCodigo()))) {
			conciliacaoExtratoAlterado.setLancamentoSei(Uteis.removeNomeDentroListaDeString(conciliacaoExtratoAlterado.getLancamentoSei(), extratoContaCorrente.getFormaPagamentoConciliacao()));
		}
		adicionarConciliacaoContaCorrenteDiaExtratoSeiAgrupado(conciliacaoExtratoAlterado, objDia);
		objDia.getListaConciliacaoContaCorrenteExtrato().add(novoConciliacaoExtrato);
		addConciliacaoContaCorrenteDiaVO(objDia, obj);
		realizarCalculoTotalizadoresDias(obj, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDesagrupamentoPorValorAproximadoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, String listaCodigoExtrato, UsuarioVO usuario) throws Exception {
		List<ExtratoContaCorrenteVO> listaExtratoDesagrupadoValor = conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().get(listaCodigoExtrato);
		Uteis.checkState(!Uteis.isAtributoPreenchido(listaExtratoDesagrupadoValor), "Valores nao encontrado para lista.");
		realizarDesagrupamentoPorValorEncontradoConciliacaoContaCorrenteDiaExtrato(obj, conciliacaoExtratoAlterado, listaExtratoDesagrupadoValor , false, usuario );
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDesagrupamentoPorValorConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, UsuarioVO usuario) throws Exception {
		List<ExtratoContaCorrenteVO> listaExtratoTemp = validarDadosParaDesagrupamentoPorValorConciliacaoContaCorrenteExtrato(conciliacaoExtratoAlterado);
		Map<String, Double> mapaExtratoPossiveisValoresTemp = new HashMap<>();
		Double valorOperacao = conciliacaoExtratoAlterado.getDesagruparValor();
		if (conciliacaoExtratoAlterado.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida()) {
			listaExtratoTemp = listaExtratoTemp.stream().filter(p -> p.getValor() > (conciliacaoExtratoAlterado.getDesagruparValor()*-1) && p.getValor() < 0.0).collect(Collectors.toList());
		} else {
			listaExtratoTemp = listaExtratoTemp.stream().filter(p -> p.getValor() < conciliacaoExtratoAlterado.getDesagruparValor() && p.getValor() > 0.0).collect(Collectors.toList());
		}
		conciliacaoExtratoAlterado.getMapaCodigoExtratoPorValor().clear();
		conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().clear();
		List<ResultadoConjuntoValoresVO<ExtratoContaCorrenteVO>> listaResultadoConjunto = Uteis.identificarContasIguaisValorOperacao1(listaExtratoTemp, valorOperacao, "codigo", "valor");
		listaResultadoConjunto.stream().forEach(p->{conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().put(p.getCodigosDoConjunto(), p.getListaDeConjunto());});				
		if (conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().size() == 1) {
			for (Entry<String, List<ExtratoContaCorrenteVO>> map : conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().entrySet()) {
				realizarDesagrupamentoPorValorEncontradoConciliacaoContaCorrenteDiaExtrato(obj, conciliacaoExtratoAlterado, map.getValue(), true, usuario );
			}
		} else if (conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().size() > 1) {
			realizarValidacaoDePossiveisValoresParaDesagrupamentoPorValor(conciliacaoExtratoAlterado, mapaExtratoPossiveisValoresTemp, 5);
			conciliacaoExtratoAlterado.setMapaCodigoExtratoPorValor(mapaExtratoPossiveisValoresTemp.entrySet().stream()
					.sorted(comparingByValue())
					.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new)));
		}
	}

	private void realizarDesagrupamentoPorValorEncontradoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, List<ExtratoContaCorrenteVO> listaExtratoDesagrupadoValor, boolean isValidarIgual, UsuarioVO usuario) {
		Double valorOperacao = 0.0;
		if(isValidarIgual){
			if (conciliacaoExtratoAlterado.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) {
				valorOperacao = listaExtratoDesagrupadoValor.stream().mapToDouble(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
			} else {
				valorOperacao = listaExtratoDesagrupadoValor.stream().mapToDouble(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a - b));
				valorOperacao = valorOperacao * -1;
			}	
		}
		if(!isValidarIgual || (isValidarIgual  && conciliacaoExtratoAlterado.getDesagruparValor() .equals(valorOperacao))){
			ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
			ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoAlterado.getDataSei(), obj);
			Iterator<ExtratoContaCorrenteVO> i = conciliacaoExtratoAlterado.getListaExtratoContaCorrente().iterator();
			for (ExtratoContaCorrenteVO novoExtratoContaCorrente : listaExtratoDesagrupadoValor) {
				while (i.hasNext()) {
					ExtratoContaCorrenteVO objExistente = i.next();
					if (objExistente.getCodigo().equals(novoExtratoContaCorrente.getCodigo())) {
						removerExtratoContaCorrenteParaDesagrupamentoConciliacaoContaCorrenteDiaExtrato(conciliacaoExtratoAlterado, novoConciliacaoExtrato, objDia, objExistente);
						i.remove();
						break;
					}
				}
				if (conciliacaoExtratoAlterado.getListaExtratoContaCorrente().stream().noneMatch(p -> p.getFormaPagamento().getCodigo().equals(novoExtratoContaCorrente.getFormaPagamento().getCodigo()) && p.getOperadoraCartaoVO().getCodigo().equals(novoExtratoContaCorrente.getOperadoraCartaoVO().getCodigo()))) {
					conciliacaoExtratoAlterado.setLancamentoSei(Uteis.removeNomeDentroListaDeString(conciliacaoExtratoAlterado.getLancamentoSei(), novoExtratoContaCorrente.getFormaPagamentoConciliacao()));
				}
			}
			conciliacaoExtratoAlterado.atualizarListaTotalizadores();
			adicionarConciliacaoContaCorrenteDiaExtratoSeiAgrupado(conciliacaoExtratoAlterado, objDia);
			objDia.getListaConciliacaoContaCorrenteExtrato().add(novoConciliacaoExtrato);
			addConciliacaoContaCorrenteDiaVO(objDia, obj);
			realizarCalculoTotalizadoresDias(obj, usuario);	
		}
	}

	private void realizarValidacaoDePossiveisValoresParaDesagrupamentoPorValor(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, Map<String, Double> mapaExtratoTemp, Integer qtdValoresPossiveis) {
		Map<String, Double> mapaExtratoTempOrdenado = null;
		for (Entry<String, List<ExtratoContaCorrenteVO>> map : conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().entrySet()) {
			if (conciliacaoExtratoAlterado.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) {
				mapaExtratoTemp.put(map.getKey(), map.getValue().stream().mapToDouble(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
			} else {
				mapaExtratoTemp.put(map.getKey(), (map.getValue().stream().mapToDouble(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a - b)) * -1));
			}
		}
		if (conciliacaoExtratoAlterado.getMapaExtratoContaCorrentePorValor().size() > qtdValoresPossiveis) {
			mapaExtratoTempOrdenado = mapaExtratoTemp.entrySet().stream()
					.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
					.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			mapaExtratoTemp.clear();
			for (Entry<String, Double> map : mapaExtratoTempOrdenado.entrySet()) {
				mapaExtratoTemp.put(map.getKey(), map.getValue());
				if (mapaExtratoTemp.size() == qtdValoresPossiveis) {
					return;
				}
			}
		}
	}

	private List<ExtratoContaCorrenteVO> validarDadosParaDesagrupamentoPorValorConciliacaoContaCorrenteExtrato(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado) {
		List<ExtratoContaCorrenteVO> listaExtratoTemp;
		Double valorTotalExtratoTemp;
		Uteis.checkState(conciliacaoExtratoAlterado.getDesagruparValor().equals(0.0), "O Valor para ser Desagrupado  deve ser maior que ZERO.");
		if (Uteis.isAtributoPreenchido(conciliacaoExtratoAlterado.getDesagruparFormaPagamento())) {
			valorTotalExtratoTemp = 0.0;
			listaExtratoTemp = conciliacaoExtratoAlterado.getListaExtratoContaCorrente().stream()
					.filter(p -> p.getFormaPagamento().getCodigo().equals(conciliacaoExtratoAlterado.getDesagruparFormaPagamento()))
					.collect(Collectors.toList());
			for (ExtratoContaCorrenteVO objExistente : listaExtratoTemp) {
				if (objExistente.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
					valorTotalExtratoTemp = Uteis.arrendondarForcando2CadasDecimais(valorTotalExtratoTemp + objExistente.getValor());
				} else {
					valorTotalExtratoTemp = Uteis.arrendondarForcando2CadasDecimais(valorTotalExtratoTemp - objExistente.getValor());
				}
			}
			Uteis.checkState(conciliacaoExtratoAlterado.getDesagruparValor() > valorTotalExtratoTemp, "O Valor para ser Desagrupado na Forma de Pagamento escolhido não pode ser maior que o valor total de:" + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(valorTotalExtratoTemp, ","));
		} else {
			valorTotalExtratoTemp = conciliacaoExtratoAlterado.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida() ? conciliacaoExtratoAlterado.getValorSei() * -1 : conciliacaoExtratoAlterado.getValorSei();
			Uteis.checkState(conciliacaoExtratoAlterado.getDesagruparValor() > valorTotalExtratoTemp && !Uteis.isAtributoPreenchido(conciliacaoExtratoAlterado.getDesagruparFormaPagamento()), "O Valor para ser Desagrupado não pode ser maior que o valor total do Lançamento " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(valorTotalExtratoTemp, ","));
			listaExtratoTemp = conciliacaoExtratoAlterado.getListaExtratoContaCorrente();
		}
		return listaExtratoTemp;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarDesagrupamentoPorFormaPagamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, TotalizadorPorFormaPagamentoVO totalizadorPorFormaPagamentoVO, UsuarioVO usuario) throws Exception {
		ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoAlterado.getDataSei(), obj);
		Iterator<ExtratoContaCorrenteVO> i = conciliacaoExtratoAlterado.getListaExtratoContaCorrente().iterator();
		List<String> lancamentoSeiRemovido = new ArrayList<>();
		while (i.hasNext()) {
			ExtratoContaCorrenteVO objExistente = i.next();
			if (objExistente.getFormaPagamento().getCodigo().equals(totalizadorPorFormaPagamentoVO.getFormaPagamentoVO().getCodigo())
					&& objExistente.getOperadoraCartaoVO().getCodigo().equals(totalizadorPorFormaPagamentoVO.getOperadoraCartaoVO().getCodigo())) {
				removerExtratoContaCorrenteParaDesagrupamentoConciliacaoContaCorrenteDiaExtrato(conciliacaoExtratoAlterado, novoConciliacaoExtrato, objDia, objExistente);
				if(!lancamentoSeiRemovido.contains(objExistente.getFormaPagamentoConciliacao())){
					lancamentoSeiRemovido.add(objExistente.getFormaPagamentoConciliacao());	
				}
				i.remove();
			}
		}
		lancamentoSeiRemovido.stream().forEach(p->{
			conciliacaoExtratoAlterado.setLancamentoSei(Uteis.removeNomeDentroListaDeString(conciliacaoExtratoAlterado.getLancamentoSei(), p));	
		});		
		conciliacaoExtratoAlterado.getListaTotalizadorPorFormaPagamento().removeIf(p -> p.getFormaPagamentoVO().getCodigo().equals(totalizadorPorFormaPagamentoVO.getFormaPagamentoVO().getCodigo()) && p.getOperadoraCartaoVO().getCodigo().equals(totalizadorPorFormaPagamentoVO.getOperadoraCartaoVO().getCodigo()));
		adicionarConciliacaoContaCorrenteDiaExtratoSeiAgrupado(conciliacaoExtratoAlterado, objDia);
		objDia.getListaConciliacaoContaCorrenteExtrato().add(novoConciliacaoExtrato);
		addConciliacaoContaCorrenteDiaVO(objDia, obj);
		realizarCalculoTotalizadoresDias(obj, usuario);
	}

	private void removerExtratoContaCorrenteParaDesagrupamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoAlterado, ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato, ConciliacaoContaCorrenteDiaVO objDia, ExtratoContaCorrenteVO extratoRemovido) {
		conciliacaoExtratoAlterado.setValorSei(Uteis.arrendondarForcando2CadasDecimais(conciliacaoExtratoAlterado.getValorSei() - extratoRemovido.getValor()));
		conciliacaoExtratoAlterado.setCodigoSei(removerCodigoOrigemSei(conciliacaoExtratoAlterado.getCodigoSei(), extratoRemovido.getCodigo().toString()));
		objDia.setListaCodigoExtratoContaCorrenteExistente(removerCodigoOrigemSei(objDia.getListaCodigoExtratoContaCorrenteExistente(), extratoRemovido.getCodigo().toString()));
		preencherExtratoContaCorrenteSei(objDia, novoConciliacaoExtrato, extratoRemovido);
	}
	
	private Boolean validarSeExisteCodigoSei(String listaCodigos, String extratoContaCorrenteCodigo) {
		String[] listaCodigosTemp = listaCodigos.split(",");
		for (int i = 0; i < listaCodigosTemp.length; i++) {
			if (listaCodigosTemp[i].equals(extratoContaCorrenteCodigo)) {
				return true;
			}
		}
		return false;
	}
	
	private String removerCodigoOrigemSei(String listaCodigos, String extratoContaCorrenteCodigo) {
		String novoCodigoSei = "";
		String[] listaCodigosTemp = listaCodigos.split(",");
		for (int i = 0; i < listaCodigosTemp.length; i++) {
			if (!listaCodigosTemp[i].equals(extratoContaCorrenteCodigo)) {
				if (novoCodigoSei.isEmpty()) {
					novoCodigoSei += listaCodigosTemp[i];
				} else {
					novoCodigoSei = novoCodigoSei + "," + listaCodigosTemp[i];
				}
			}
		}
		return novoCodigoSei;
	}

	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarAgrupamentoConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrag, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrop, UsuarioVO usuario) throws Exception {
		Uteis.checkState(conciliacaoExtratoDrop.getCodigoSei().equals(conciliacaoExtratoDrag.getCodigoSei()), "Não foi possível agrupar o registro nele mesmo.");
		Uteis.checkState(conciliacaoExtratoDrop.isExtratoConciliado() || conciliacaoExtratoDrag.isExtratoConciliado(), "Não foi possível realizar o agrupamento, pois o registro já esta conciliado.");
		Uteis.checkState(conciliacaoExtratoDrop.isExisteConciliacaoConjunta() || conciliacaoExtratoDrag.isExisteConciliacaoConjunta(), "Não foi possível realizar o agrupamento, pois o registro já esta conciliado conjunta.");
		Uteis.checkState(!UteisData.getCompararDatas(conciliacaoExtratoDrop.getDataSei(), conciliacaoExtratoDrag.getDataSei()), "Não foi possível realizar o agrupamento, pois os registro são de datas diferentes.");
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoDrop.getDataSei(), obj);
		conciliacaoExtratoDrop.setCodigoSei(conciliacaoExtratoDrop.getCodigoSei() + "," + conciliacaoExtratoDrag.getCodigoSei());
		conciliacaoExtratoDrop.setValorSei(Uteis.arrendondarForcando2CadasDecimais(conciliacaoExtratoDrop.getValorSei() + conciliacaoExtratoDrag.getValorSei()));
		if (!conciliacaoExtratoDrop.getLancamentoSei().contains(conciliacaoExtratoDrag.getLancamentoSei())) {
			conciliacaoExtratoDrop.setLancamentoSei(conciliacaoExtratoDrop.getLancamentoSei() + "," + conciliacaoExtratoDrag.getLancamentoSei());
		}
		Iterator<ConciliacaoContaCorrenteDiaExtratoVO> i = objDia.getListaConciliacaoContaCorrenteExtrato().iterator();
		while (i.hasNext()) {
			ConciliacaoContaCorrenteDiaExtratoVO objExistente = i.next();
			if (objExistente.getCodigoSei().equals(conciliacaoExtratoDrag.getCodigoSei())) {
				i.remove();
				break;
			}
		}
		addConciliacaoContaCorrenteDiaExtratoVO(conciliacaoExtratoDrop, objDia);
		addConciliacaoContaCorrenteDiaVO(objDia, obj);
		realizarCalculoTotalizadoresDias(obj, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarRemocaoLinkConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, UsuarioVO usuario) throws Exception {
		preencherDetalheExtratoContaCorrente(conciliacaoExtrato, usuario);
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtrato.getDataSei(), obj);
		objDia.getListaConciliacaoContaCorrenteExtrato().stream()
				.filter(p -> p.getCodigoOfx().equals(conciliacaoExtrato.getCodigoOfx())
						&& p.getCodigoSei().equals(conciliacaoExtrato.getCodigoSei())
						&& p.isExisteConciliacaoConjunta())
				.forEach(p -> {
					//p.removerDadosConciliacaoConjunta();
					p.getListaConciliacaoContaConjuntaVO().clear();
					removerConciliacaoConjuntaParaRegistroOFX(objDia, p.getCodigoOfx(), p.getCodigoSei());
					Optional<ConciliacaoContaCorrenteDiaExtratoVO> findFirst = objDia.getListaConciliacaoContaCorrenteExtrato().stream()
							.filter(pp -> !pp.isExtratoConciliado() && pp.getCodigoSei().equals(p.getCodigoSei()))
							.findFirst();
					if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getCodigoSei())) {
						findFirst.get().setValorSei(Uteis.arrendondarForcando2CadasDecimais(findFirst.get().getValorSei() + p.getValorSei()));
						p.anularDadosSei();
						addConciliacaoContaCorrenteDiaExtratoVO(findFirst.get(), objDia);
						realizarCalculoTotalizadoresDias(obj, usuario);
					}
				});
		if (Uteis.isAtributoPreenchido(conciliacaoExtrato.getCodigoSei())) {
			ConciliacaoContaCorrenteDiaExtratoVO novoConciliacaoExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
			novoConciliacaoExtrato.setConciliacaoContaCorrenteDia(objDia);
			novoConciliacaoExtrato.preencherDadosSei(conciliacaoExtrato);
			if(Uteis.isAtributoPreenchido(conciliacaoExtrato.getListaConciliacaoContaConjuntaVO())) {
				Iterator<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> i = conciliacaoExtrato.getListaConciliacaoContaConjuntaVO().iterator();
				while (i.hasNext()) {
					ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdec = (ConciliacaoContaCorrenteDiaExtratoConjuntaVO) i.next();
					cccdec.setConciliacaoContaCorrenteDiaExtrato(novoConciliacaoExtrato);
					novoConciliacaoExtrato.getListaConciliacaoContaConjuntaVO().add(cccdec);
					i.remove();
				}
			}
			conciliacaoExtrato.anularDadosSei();
			objDia.getListaConciliacaoContaCorrenteExtrato().add(novoConciliacaoExtrato);
			realizarCalculoTotalizadoresDias(obj, usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarLinkConciliacaoExtratoContaCorrente(ConciliacaoContaCorrenteVO obj, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(conciliacaoExtrato.getDataOfx()) || !Uteis.isAtributoPreenchido(conciliacaoExtratoSei.getDataSei()), "Não foi possível realizar a conciliação, pois não existe o lançamento de registro para ambos os lados.");
		Uteis.checkState(conciliacaoExtrato.isExtratoConciliado(), "Não foi possível realizar a conciliação, pois o registro já esta conciliado.");
		Uteis.checkState(!UteisData.getCompararDatas(conciliacaoExtrato.getDataOfx(), conciliacaoExtratoSei.getDataSei()), "Não foi possível realizar a conciliação, pois os registro são de datas diferentes.");
		Uteis.checkState((conciliacaoExtratoSei.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada() && conciliacaoExtrato.getValorOfx() > conciliacaoExtratoSei.getValorSei())
				|| (conciliacaoExtratoSei.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida() && conciliacaoExtrato.getValorOfx() < conciliacaoExtratoSei.getValorSei()), "Não foi possível realizar a conciliação, pois o valor do registro bancário é maior que o valor do SEI.");
		if (conciliacaoExtrato.getCodigoOfx().equals(conciliacaoExtratoSei.getCodigoOfx())
				&& conciliacaoExtrato.getCodigoSei().equals(conciliacaoExtratoSei.getCodigoSei())) {
			return;
		}
		preencherDetalheExtratoContaCorrente(conciliacaoExtratoSei, usuario);
		ConciliacaoContaCorrenteDiaVO objDia = consultarConciliacaoContaCorrenteDiaVO(conciliacaoExtratoSei.getDataSei(), obj);
		Iterator<ConciliacaoContaCorrenteDiaExtratoVO> i = objDia.getListaConciliacaoContaCorrenteExtrato().iterator();
		while (i.hasNext()) {
			ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente = i.next();
			if (!conciliacaoExtratoExistente.isExtratoConciliado() && conciliacaoExtratoExistente.getCodigoSei().equals(conciliacaoExtratoSei.getCodigoSei())) {
				if (conciliacaoExtrato.getValorOfx().equals(conciliacaoExtratoExistente.getValorSei()) && !conciliacaoExtratoSei.isExisteConciliacaoConjunta()) {
					conciliacaoExtrato.preencherDadosSei(conciliacaoExtratoExistente);
					i.remove();
				} else if (conciliacaoExtrato.getValorOfx().equals(conciliacaoExtratoExistente.getValorSei()) && conciliacaoExtratoSei.isExisteConciliacaoConjunta()){
					realizarLinkConciliacaoExtratoContaCorrenteConjunta(objDia, conciliacaoExtrato, conciliacaoExtratoExistente);
				 	i.remove();
				} else if ((conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada() && conciliacaoExtrato.getValorOfx() < conciliacaoExtratoExistente.getValorSei())
						|| (conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida() && conciliacaoExtrato.getValorOfx() > conciliacaoExtratoExistente.getValorSei())) {
					realizarLinkConciliacaoExtratoContaCorrenteConjunta(objDia, conciliacaoExtrato, conciliacaoExtratoExistente);
				}
				break;
			}
		}
		addConciliacaoContaCorrenteDiaExtratoVO(conciliacaoExtrato, objDia);
		addConciliacaoContaCorrenteDiaVO(objDia, obj);
		realizarCalculoTotalizadoresDias(obj, usuario);
	}

	private void realizarLinkConciliacaoExtratoContaCorrenteConjunta(ConciliacaoContaCorrenteDiaVO objDia, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato, ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoExistente) {
		if (conciliacaoExtratoExistente.getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) {
			conciliacaoExtratoExistente.setValorSei(Uteis.arrendondarForcando2CadasDecimais(conciliacaoExtratoExistente.getValorSei() - conciliacaoExtrato.getValorOfx()));
		} else {
			conciliacaoExtratoExistente.setValorSei(Uteis.arrendondarForcando2CadasDecimais((conciliacaoExtratoExistente.getValorSei() * -1) - (conciliacaoExtrato.getValorOfx() * -1)));
			conciliacaoExtratoExistente.setValorSei(conciliacaoExtratoExistente.getValorSei() * -1);
		}
		conciliacaoExtrato.preencherDadosSei(conciliacaoExtratoExistente);
		conciliacaoExtrato.setValorSei(conciliacaoExtrato.getValorOfx());
		//conciliacaoExtrato.preencherDadosConciliacaoConjunta();
		
		//criando conciliacao conjunao para  ofx
		ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdec = new ConciliacaoContaCorrenteDiaExtratoConjuntaVO();
		cccdec.preencherDados(conciliacaoExtrato);
		cccdec.setConciliacaoContaCorrenteDiaExtrato(conciliacaoExtrato);
		conciliacaoExtrato.getListaConciliacaoContaConjuntaVO().add(cccdec);
		
		//copiando conciliacao conjunao existentes do sei para  ofx
		conciliacaoExtratoExistente.getListaConciliacaoContaConjuntaVO()
		.stream().forEach(p -> {
			ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdecClone = p.getClone();
			cccdecClone.setConciliacaoContaCorrenteDiaExtrato(conciliacaoExtrato);
			conciliacaoExtrato.getListaConciliacaoContaConjuntaVO().add(cccdecClone);
		});
		
		//criando conciliacao conjunao para  sei
		ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdecClone = cccdec.getClone();
		cccdecClone.setConciliacaoContaCorrenteDiaExtrato(conciliacaoExtratoExistente);
		conciliacaoExtratoExistente.getListaConciliacaoContaConjuntaVO().add(cccdecClone);
		atualizarConciliacaoConjuntaNosDiasJaConciliado(objDia, conciliacaoExtrato.getCodigoOfx(), conciliacaoExtratoExistente.getCodigoSei(), cccdec);
	}
	
	
	

	private void atualizarConciliacaoConjuntaNosDiasJaConciliado(ConciliacaoContaCorrenteDiaVO objDia, Integer codigoOfx, String codigoSei, ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdec) {
		objDia.getListaConciliacaoContaCorrenteExtrato().stream()
				.filter(p -> p.isExtratoConciliado() 
						&& p.getCodigoSei().equals(codigoSei) 
						&& p.getCodigoOfx() > 0 
						&& !p.getCodigoOfx().equals(codigoOfx))
				.forEach(p -> {
					ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdecClone = cccdec.getClone();
					cccdecClone.setConciliacaoContaCorrenteDiaExtrato(p);
					p.getListaConciliacaoContaConjuntaVO().add(cccdecClone);
				});
	}

	private void removerConciliacaoConjuntaParaRegistroOFX(ConciliacaoContaCorrenteDiaVO objDia, Integer codigoOfx, String codigoSei) {
		objDia.getListaConciliacaoContaCorrenteExtrato().stream()
				.filter(p -> p.getCodigoSei().equals(codigoSei) && !p.getCodigoOfx().equals(codigoOfx))
				.forEach(p -> p.getListaConciliacaoContaConjuntaVO().removeIf(cccdecClone -> cccdecClone.getCodigoOfx().equals(codigoOfx)));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDadosParametrizarOperacoesAutomaticasConciliacao(ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro) throws Exception {
		if (!Uteis.isAtributoPreenchido(parametro.getNomeLancamento())) {
			throw new Exception("O campo Lançamento (Parametrizar Operações Automáticas) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(parametro.getUnidadeEnsinoVO())) {
			throw new Exception("O campo Unidade Ensino (Parametrizar Operações Automáticas) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(parametro.getContaCorrenteVO())) {
			throw new Exception("O campo Conta Corrente (Parametrizar Operações Automáticas) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(parametro.getTipoFormaPagamento())) {
			throw new Exception("O campo Tipo Pagamento (Parametrizar Operações Automáticas) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(parametro.getFormaPagamentoVO())) {
			throw new Exception("O campo Forma Pagamento (Parametrizar Operações Automáticas) deve ser informado.");
		}

		if (parametro.getOrigemExtratoContaCorrenteEnum().isMovimentacaoFinanceira()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(parametro.getContaCorrenteDestinoVO()), "O campo Conta Corrente Destino (Parametrizar Operações Automáticas) deve ser informado.");
			Uteis.checkState(parametro.getContaCorrenteVO().getCodigo().equals(parametro.getContaCorrenteDestinoVO().getCodigo()), "O campo CONTA CORRENTE ORIGEM deve ser diferente de CONTA CORRENTE DESTINO.");
			parametro.setTipoSacado(null);
			parametro.setBancoSacado(new BancoVO());
			parametro.setParceiroSacado(new ParceiroVO());
			parametro.setFornecedorSacado(new FornecedorVO());
			parametro.setFuncionarioSacado(new FuncionarioVO());
			parametro.setOperadoraCartaoSacado(new OperadoraCartaoVO());
			parametro.setCodigoVerificacao("");
			parametro.setNumeroCartao("");
			parametro.setAnoValidade(null);
			parametro.setMesValidade(null);
			parametro.setNomeCartaoCredito("");
			parametro.setQtdeParcelasCartaoCredito(0);
			parametro.setTipoFinanciamentoEnum(null);
			parametro.setOperadoraCartao(new OperadoraCartaoVO());
			parametro.setCategoriaDespesaVO(new CategoriaDespesaVO());
			parametro.setCentroResultadoAdministrativo(new CentroResultadoVO());
			parametro.setCentroReceitaVO(new CentroReceitaVO());
			parametro.setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			parametro.setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
		} else {
			if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada() && !Uteis.isAtributoPreenchido(parametro.getCentroReceitaVO())) {
				throw new Exception("O campo Centro de Receita (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoSaida() && !Uteis.isAtributoPreenchido(parametro.getCategoriaDespesaVO())) {
				throw new Exception("O campo Categoria de Despesas (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoSaida() && !Uteis.isAtributoPreenchido(parametro.getCentroResultadoAdministrativo())) {
				throw new Exception("O campo Centro Resultado Administrativo (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (!Uteis.isAtributoPreenchido(parametro.getTipoSacado())) {
				throw new Exception("O campo Tipo Sacado (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getTipoSacado().isFornecedor() && !Uteis.isAtributoPreenchido(parametro.getFornecedorSacado())) {
				throw new Exception("O campo Fornecedor (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getTipoSacado().isFuncionario() && !Uteis.isAtributoPreenchido(parametro.getFuncionarioSacado())) {
				throw new Exception("O campo Funcionário (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getTipoSacado().isParceiro() && !Uteis.isAtributoPreenchido(parametro.getParceiroSacado())) {
				throw new Exception("O campo Parceiro (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if ((parametro.getFormaPagamentoVO().isCartaoDebito() || parametro.getFormaPagamentoVO().isCartaoCredito()) && !Uteis.isAtributoPreenchido(parametro.getOperadoraCartao())) {
				throw new Exception("O campo Operado (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if ((parametro.getFormaPagamentoVO().isCartaoDebito() || parametro.getFormaPagamentoVO().isCartaoCredito()) && !Uteis.isAtributoPreenchido(parametro.getTipoFinanciamentoEnum())) {
				throw new Exception("O campo Tipo Financiamento (Parametrizar Operações Automáticas) deve ser informado.");
			}
			if (parametro.getFormaPagamentoVO().isCartaoCredito() && !Uteis.isAtributoPreenchido(parametro.getQtdeParcelasCartaoCredito())) {
				throw new Exception("O campo Quantidade de Parcelas (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getFormaPagamentoVO().isCartaoCredito() && parametro.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !Uteis.isAtributoPreenchido(parametro.getNomeCartaoCredito())) {
				throw new Exception("O campo Nome do Titular (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getFormaPagamentoVO().isCartaoCredito() && parametro.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !Uteis.isAtributoPreenchido(parametro.getNumeroCartao())) {
				throw new Exception("O campo Número do Cartão (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getFormaPagamentoVO().isCartaoCredito() && parametro.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !Uteis.isAtributoPreenchido(parametro.getMesValidade())) {
				throw new Exception("O campo Mês Vencimento (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getFormaPagamentoVO().isCartaoCredito() && parametro.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !Uteis.isAtributoPreenchido(parametro.getAnoValidade())) {
				throw new Exception("O campo Ano Vencimento (Parametrizar Operações Automáticas) deve ser informado.");
			}

			if (parametro.getFormaPagamentoVO().isCartaoCredito() && parametro.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !Uteis.isAtributoPreenchido(parametro.getCodigoVerificacao())) {
				throw new Exception("O campo Cód de Seguraça (Parametrizar Operações Automáticas) deve ser informado.");
			}
			parametro.setContaCorrenteDestinoVO(new ContaCorrenteVO());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception {
		int index = 0;
		validarDadosParametrizarOperacoesAutomaticasConciliacao(parametro);
		parametro.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(parametro.getUnidadeEnsinoVO().getCodigo(), false, usuario));
		parametro.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(parametro.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		parametro.setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(parametro.getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
			for (ParametrizarOperacoesAutomaticasConciliacaoItemVO objExistente : obj.getListaParametrizarEntradaItens()) {
				if (objExistente.getNomeLancamento().trim().equals(parametro.getNomeLancamento().trim())) {
					if (Uteis.isAtributoPreenchido(objExistente)) {
						parametro.setCodigo(objExistente.getCodigo());
					}
					obj.getListaParametrizarEntradaItens().set(index, parametro);
					return;
				}
				index++;
			}
			obj.getListaParametrizarEntradaItens().add(parametro);
		} else {
			for (ParametrizarOperacoesAutomaticasConciliacaoItemVO objExistente : obj.getListaParametrizarSaidaItens()) {
				if (objExistente.getNomeLancamento().trim().equals(parametro.getNomeLancamento().trim())) {
					if (Uteis.isAtributoPreenchido(objExistente)) {
						parametro.setCodigo(objExistente.getCodigo());
					}
					obj.getListaParametrizarSaidaItens().set(index, parametro);
					return;
				}
				index++;
			}
			obj.getListaParametrizarSaidaItens().add(parametro);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removeParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro, UsuarioVO usuario) throws Exception {
		Iterator<ParametrizarOperacoesAutomaticasConciliacaoItemVO> i = null;
		if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
			i = obj.getListaParametrizarEntradaItens().iterator();
		} else {
			i = obj.getListaParametrizarSaidaItens().iterator();
		}
		while (i.hasNext()) {
			ParametrizarOperacoesAutomaticasConciliacaoItemVO objExistente = (ParametrizarOperacoesAutomaticasConciliacaoItemVO) i.next();
			if (objExistente.getNomeLancamento().trim().equals(parametro.getNomeLancamento().trim())) {
				obj.getListaParametrizarExcluida().add(objExistente);
				i.remove();
				return;
			}
		}
	}
	
	@Override
	public File realizarValidacaoArquivoRetornoComExtratoContaCorrente(String nrBanco, String  contaCorrenteArquivo, Date dataConciliacao, String urlLogoPadraoRelatorio,  UsuarioVO usuario) throws Exception {
		File arquivo = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Arquivo de Retorno");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, sheet, urlLogoPadraoRelatorio, null , 9, "");
		montarCabecalhoRelatorioExcelConciliacaoBancaria(uteisExcel, workbook, sheet);
		consultarArquivoRetonroValidandoConciliacaoBancaria(uteisExcel, workbook, sheet, nrBanco, contaCorrenteArquivo, dataConciliacao, usuario);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + String.valueOf(new Date().getTime())+".xls");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	
	
	public void consultarArquivoRetonroValidandoConciliacaoBancaria(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet, String nrBanco, String contaCorrenteArquivo, Date dataCredito,  UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select controlecobranca.codigo, count(registrodetalhe.codigo) as qtdecontaarquivo, ");
		sqlStr.append(" datacredito::date as datacreditoarquivoretorno,  ");
		sqlStr.append(" sum(valorpago) as valorRecebidoArquivoRetorno,  ");
		sqlStr.append(" sum(valorrecebido) as valorRecebidoContaReceber,  ");
		sqlStr.append(" cc.codigo as codigocontacorrenteExtrato, "); 
		sqlStr.append(" cc.nomeapresentacaosistema as contacorrenteExtrato, "); 
		sqlStr.append(" extratocontacorrente.data::date as dataExtratoContaCorrente,  ");
		sqlStr.append(" sum(extratocontacorrente.valor) as valorExtratoContaCorrente,  ");
		sqlStr.append(" array_to_string(array_agg(contareceber.nossonumero), ', ') as contas, "); 
		sqlStr.append(" array_to_string(array_agg(extratocontacorrente.codigo), ', ') as extratos ");
		sqlStr.append(" from registrodetalhe  ");
		sqlStr.append(" inner join registroarquivo on registroarquivo.codigo = registrodetalhe.registroarquivo ");
		sqlStr.append(" inner join controlecobranca on registroarquivo.codigo = controlecobranca.registroarquivo ");
		sqlStr.append(" inner join contareceber on contareceber.codigo = registrodetalhe.codigocontareceber ");
		sqlStr.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sqlStr.append(" inner join extratocontacorrente on extratocontacorrente.codigoorigem = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sqlStr.append(" and extratocontacorrente.origemExtratoContaCorrente = 'RECEBIMENTO' ");
		sqlStr.append(" inner join contacorrente as cc on cc.codigo = extratocontacorrente.contacorrente ");
		sqlStr.append(" inner JOIN agencia ON agencia.codigo = cc.agencia ");
    	sqlStr.append(" inner JOIN banco ON agencia.banco = banco.codigo ");
		sqlStr.append(" where registrodetalhe.codigocontareceber is not null and registrodetalhe.valorpago > 0 ");
		sqlStr.append(" and banco.nrbanco = '").append(nrBanco).append("'  ");
    	sqlStr.append(" and ( trim(leading '0' from cc.numero) = '").append(StringUtils.stripStart(contaCorrenteArquivo, "0")).append("' ");
    	sqlStr.append(" or (trim(leading '0' from cc.numero)||cc.digito) = '").append(StringUtils.stripStart(contaCorrenteArquivo, "0")).append("' ");
    	sqlStr.append(" or (trim(leading '0' from agencia.numeroagencia)||cc.numero||cc.digito) = '").append(StringUtils.stripStart(contaCorrenteArquivo, "0")).append("' ");
    	sqlStr.append(" ) ");
		sqlStr.append("  and datacredito::date = '").append(UteisData.getDataFormatoBD(dataCredito)).append("' ");
		sqlStr.append(" group by datacredito::date, cc.codigo, cc.nomeapresentacaosistema, extratocontacorrente.data::date, controlecobranca.codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Row row = null;
		while (rs.next()) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			int cellnum = 0;
			uteisExcel.preencherCelula( row, cellnum++,rs.getInt("codigo"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getInt("qtdecontaarquivo"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getDate("datacreditoarquivoretorno"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getDouble("valorRecebidoArquivoRetorno"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getDouble("valorRecebidoContaReceber"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getString("contacorrenteExtrato"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getDate("dataExtratoContaCorrente"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getDouble("valorExtratoContaCorrente"));
			uteisExcel.preencherCelula( row, cellnum++,rs.getString("contas"));
		}
		Uteis.checkState(sheet.getLastRowNum() == 1, "Não foi localizado o arquivo de retorno para essa conta corrente nesse dia.");
	}
	
	public void montarCabecalhoRelatorioExcelConciliacaoBancaria(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet) {
		int cellnum = 0;	
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000, "Arquivo Retorno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 7000,"Qtd Conta Arquivo Retorno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Data Credito Arquivo Retorno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Valor Recebido Arquivo Retorno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Valor Recebido Contas");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Conta Corrente");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Data Extrato Conta Corrente");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Valor Extrato Conta Corrente");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Contas(Nosso Número)");
		
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConciliacaoContaCorrenteVO validarUnicidadeConciliacaoContaCorrente(ConciliacaoContaCorrenteVO obj, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ccc.codigo as  \"ccc.codigo\" from conciliacaocontacorrentedia  cccd ");
		sql.append(" inner join conciliacaocontacorrente ccc on ccc.codigo = cccd.conciliacaocontacorrente ");
		sql.append(" WHERE cccd.data between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sql.append(" and ccc.contacorrentearquivo = '").append(obj.getContaCorrenteArquivo()).append("' ");
		if (Uteis.isAtributoPreenchido(obj.getDigitoContaCorrenteArquivo())) {
			sql.append(" and ccc.digitocontacorrentearquivo = '").append(obj.getDigitoContaCorrenteArquivo()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(obj)) {
			sql.append(" and ccc.codigo != ").append(obj.getCodigo());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		ConciliacaoContaCorrenteVO objExistente = new ConciliacaoContaCorrenteVO();
		if (tabelaResultado.next()) {
			objExistente.setCodigo(tabelaResultado.getInt("ccc.codigo"));
		}
		return objExistente;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer validarSeExisteConciliacaoContaCorrenteParaEstorno(Integer codigoConciliacaoContaCorrenteExtratoDia, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ccc.codigo from conciliacaocontacorrente ccc   ");
		sql.append(" inner join conciliacaocontacorrentedia cccd on cccd.conciliacaocontacorrente = ccc.codigo ");
		sql.append(" inner join conciliacaocontacorrentediaextrato cccde on cccde.conciliacaocontacorrentedia = cccd.codigo ");
		sql.append(" where (cccde.codigoofx is not null or cccde.codigoofx != 0  ");
		sql.append(" or exists (select * from conciliacaocontacorrentediaextratoconjunta where conciliacaocontacorrentediaextrato = cccde.codigo)) ");
		sql.append(" and cccde.codigosei != '' ");
		sql.append(" and cccde.codigo = ").append(codigoConciliacaoContaCorrenteExtratoDia).append(" ");
		// sql.append(" and ccc.situacaoconcialiacaocontacorrenteenum = '").append(SituacaoConcialiacaoContaCorrenteEnum.FINALIZADA.name()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado.next() ? tabelaResultado.getInt("codigo") : 0;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ccc.codigo as \"ccc.codigo\", ccc.situacaoConcialiacaoContaCorrenteEnum as \"ccc.situacaoConcialiacaoContaCorrenteEnum\", ccc.nomearquivo as \"ccc.nomearquivo\", ccc.datageracao as \"ccc.datageracao\", ");
		sql.append(" ccc.contacorrentearquivo as \"ccc.contacorrentearquivo\", ccc.digitocontacorrentearquivo as \"ccc.digitocontacorrentearquivo\", ccc.nomeContaCorrente as \"ccc.nomeContaCorrente\", ");
		sql.append(" ccc.dataInicioSei as \"ccc.dataInicioSei\", ccc.dataFimSei as \"ccc.dataFimSei\", ");
		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		sql.append(" u.codigo as \"u.codigo\", u.nome as \"u.nome\", ");
		sql.append(" p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.email as \"p.email\", p.email2 as \"p.email2\" ");
		sql.append(" FROM conciliacaocontacorrente ccc");
		sql.append(" Inner JOIN banco  ON banco.codigo = ccc.banco ");
		sql.append(" LEFT JOIN usuario u ON u.codigo = ccc.responsavel ");
		sql.append(" LEFT JOIN pessoa p ON p.codigo = u.pessoa ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ccc.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY ccc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ccc.datageracao >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND ccc.datageracao <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		sqlStr.append(" ORDER BY ccc.datageracao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteVO> consultaRapidaPorResponsavel(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(u.nome) like('%").append(valorConsulta.toLowerCase()).append("%')");
		sqlStr.append(" ORDER BY ccc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteVO> consultaRapidaPorContaCorrente(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ccc.contacorrentearquivo like('%").append(valorConsulta).append("%')");
		sqlStr.append(" ORDER BY ccc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConciliacaoContaCorrenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ConciliacaoContaCorrente WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConciliacaoContaCorrenteVO ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<ConciliacaoContaCorrenteVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ConciliacaoContaCorrenteVO obj = new ConciliacaoContaCorrenteVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDadosBasico(ConciliacaoContaCorrenteVO obj, SqlRowSet dadosSQL) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("ccc.codigo")));
		obj.setDataGeracao(dadosSQL.getTimestamp("ccc.datageracao"));
		obj.setDataInicioSei(dadosSQL.getDate("ccc.dataInicioSei"));
		obj.setDataFimSei(dadosSQL.getDate("ccc.dataFimSei"));
		obj.setSituacaoConcialiacaoContaCorrenteEnum(SituacaoConcialiacaoContaCorrenteEnum.valueOf(dadosSQL.getString("ccc.situacaoConcialiacaoContaCorrenteEnum")));
		obj.setNomeArquivo(dadosSQL.getString("ccc.nomearquivo"));
		obj.setContaCorrenteArquivo(dadosSQL.getString("ccc.contacorrentearquivo"));
		obj.setDigitoContaCorrenteArquivo(dadosSQL.getString("ccc.digitocontacorrentearquivo"));
		obj.setNomeContaCorrente(dadosSQL.getString("ccc.nomeContaCorrente"));

		obj.getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getBanco().setNome(dadosSQL.getString("banco.nome"));

		obj.getResponsavel().setCodigo(dadosSQL.getInt("u.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("u.nome"));
		obj.getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getResponsavel().getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getResponsavel().getPessoa().setEmail(dadosSQL.getString("p.email"));
		obj.getResponsavel().getPessoa().setEmail2(dadosSQL.getString("p.email2"));
	}

	public static ConciliacaoContaCorrenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConciliacaoContaCorrenteVO obj = new ConciliacaoContaCorrenteVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setSituacaoConcialiacaoContaCorrenteEnum(SituacaoConcialiacaoContaCorrenteEnum.valueOf(dadosSQL.getString("situacaoConcialiacaoContaCorrenteEnum")));
		obj.setNomeArquivo(dadosSQL.getString("nomearquivo"));
		obj.setContaCorrenteArquivo(dadosSQL.getString("contacorrentearquivo"));
		obj.setDigitoContaCorrenteArquivo(dadosSQL.getString("digitocontacorrentearquivo"));
		obj.setNomeContaCorrente(dadosSQL.getString("nomeContaCorrente"));
		obj.setDataGeracao(dadosSQL.getTimestamp("dataGeracao"));
		obj.setDataInicioSei(dadosSQL.getDate("dataInicioSei"));
		obj.setDataFimSei(dadosSQL.getDate("dataFimSei"));
		obj.setTotalValorOfx(dadosSQL.getDouble("totalValorOfx"));
		obj.setValorBalancoOfx(dadosSQL.getDouble("valorBalancoOfx"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getBanco().setCodigo(dadosSQL.getInt("banco"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaConciliacaoContaCorrenteDia(getFacadeFactory().getConciliacaoContaCorrenteDiaInterfaceFacade().consultaRapidaPorConciliacaoContaCorrente(obj, usuario));
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavel(obj, nivelMontarDados, usuario);
		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosResponsavel(ConciliacaoContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosBanco(ConciliacaoContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBanco().getCodigo().intValue() == 0) {
			obj.setBanco(new BancoVO());
			return;
		}
		obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados, usuario));
	}
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosArquivo(ConciliacaoContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVO().getCodigo().intValue() == 0) {
			obj.setArquivoVO(new ArquivoVO());
			return;
		}
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(obj.getArquivoVO().getCodigo(), nivelMontarDados, usuario));
	}
	
	public boolean validarConciliacaoContaCorrenteFinalizada(Date data, String contaCorrente, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select conciliacaocontacorrente.codigo from conciliacaocontacorrente ");
		sqlStr.append(" inner join conciliacaocontacorrentedia on conciliacaocontacorrentedia.conciliacaocontacorrente = conciliacaocontacorrente.codigo ");
		sqlStr.append(" inner join conciliacaocontacorrentediaextrato on conciliacaocontacorrentedia.codigo = conciliacaocontacorrentediaextrato.conciliacaocontacorrentedia ");
		sqlStr.append(" inner join extratocontacorrente on extratocontacorrente.conciliacaocontacorrentediaextrato = conciliacaocontacorrentediaextrato.codigo ");
		sqlStr.append(" inner join contacorrente on contacorrente.codigo = extratocontacorrente.contacorrente ");
		
		sqlStr.append(" inner join agencia on agencia.codigo = contacorrente.agencia ");
		sqlStr.append(" WHERE  conciliacaocontacorrentedia.data =  '").append(data).append("'  ");
		sqlStr.append(" and situacaoconcialiacaocontacorrenteenum = 'FINALIZADA' ");
		sqlStr.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(contaCorrente, "0")).append("' ");
		sqlStr.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(StringUtils.stripStart(contaCorrente, "0")).append("' ");
		sqlStr.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(StringUtils.stripStart(contaCorrente, "0")).append("' ");
		sqlStr.append(" ) ");
		sqlStr.append(" limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, "codigo", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConciliacaoContaCorrente.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConciliacaoContaCorrente.idEntidade = idEntidade;
	}

}
