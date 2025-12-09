package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.administrativo.enumeradores.TipoTCCEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoTCCInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ConfiguracaoTCC extends ControleAcesso implements ConfiguracaoTCCInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4388758043770029974L;
	private static String idEntidade = "ConfiguracaoTCC";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ConfiguracaoTCCVO configuracaoTCCVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(configuracaoTCCVO);
		if (configuracaoTCCVO.isNovoObj()) {
			incluir(configuracaoTCCVO, usuarioVO);
		} else {
			alterar(configuracaoTCCVO, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final ConfiguracaoTCCVO configuracaoTCCVO, UsuarioVO usuarioVO) throws Exception {
		incluir(getIdEntidade(), true, usuarioVO);
		try {
			configuracaoTCCVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoTCC (");
					sql.append(" controlaPlanoTCC, prazoExecucaoPlanoTCC, prazoRespostaOrientadorPlanoTCC, permiteInteracaoOrientadorPlanoTCC, ");
					sql.append(" permiteSolicitarOrientador, valorPagamentoOrientadorPlanoTCC, valorPagamentoCoordenadorPlanoTCC, controlaElaboracaoTCC,");
					sql.append(" prazoExecucaoElaboracaoTCC, prazoRespostaOrientadorElaboracaoTCC, permiteInteracaoOrientadorElaboracaoTCC, permiteArquivoApoioElaboracaoTCC,");
					sql.append(" controlarHistoricoPostagemArquivoElaboracaoTCC, valorPagamentoOrientadorElaboracaoTCC, valorPagamentoCoordenadorElaboracaoTCC, prazoExecucaoApresentacaoTCC, ");
					sql.append(" exigePostagemArquivoFinalAvaliacaoTCC, permiteArquivoApresentacaoTCC, permiteArquivoApoioApresentacaoTCC, permiteInteracaoOrientadorApresentacaoTCC,");
					sql.append(" valorPagamentoOrientadorApresentacaoTCC, valorPagamentoCoordenadorApresentacaoTCC, controlarHistoricoPostagemArquivoApresentacaoTCC, descricao, ");
					sql.append(" tipoTCC, orientacaoGeral, orientacaoExtensaoPrazo, numeroDiaAntesPrimeiraAulaLiberarAcessoTCC,");
					sql.append(" apagarHistoricoPostagemArquivoAposFinalizacaoTCC, dataBaseVencimentoPagamentoOrientacao, dataBaseProcessamentoPagamentoOrientacao, ");
					sql.append(" controlaFrequencia, numeroDiaAntesEncerramentoEtapaPrimeiroAviso, numeroDiaAntesEncerramentoEtapaSegundoAviso, numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica, ");
					sql.append(" politicaApresentarTCCAluno, ocultarOrientadoVisaoAluno, validarPendenciaFinanceira, validarPendenciaDocumento, extensoesPermitidasParaArquivos, ");
					sql.append(" orientadorPadrao, nomenclaturaUtilizarParaAvaliador, nomenclaturaUtilizarParaComissao, textoOrientacaoPendenciaDoc, textoOrientacaoPendenciaFin, apresentarContasVencidas, utilizarProfMinistCoordeConfTurma, composicaoNotaQuest, mediaAprovacao, notaMaximaMediaFormatacao, notaMaximaMediaConteudo, textoOrientacaoAlunoAprov, textoOrientacaoAlunoReprov ");
					sql.append(") VALUES (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setBoolean(x++, configuracaoTCCVO.getControlaPlanoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoPlanoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoRespostaOrientadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteSolicitarOrientador());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorPlanoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlaElaboracaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoElaboracaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoRespostaOrientadorElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApoioElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlarHistoricoPostagemArquivoElaboracaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorElaboracaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorApresentacaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getExigePostagemArquivoFinalAvaliacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApoioApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorApresentacaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorApresentacaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlarHistoricoPostagemArquivoApresentacaoTCC());
					ps.setString(x++, configuracaoTCCVO.getDescricao());
					ps.setString(x++, configuracaoTCCVO.getTipoTCC().toString());
					ps.setString(x++, configuracaoTCCVO.getOrientacaoGeral());
					ps.setString(x++, configuracaoTCCVO.getOrientacaoExtensaoPrazo());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getApagarHistoricoPostagemArquivoAposFinalizacaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getDataBaseVencimentoPagamentoOrientacao());
					ps.setInt(x++, configuracaoTCCVO.getDataBaseProcessamentoPagamentoOrientacao());
					ps.setBoolean(x++, configuracaoTCCVO.getControlaFrequencia());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesEncerramentoEtapaPrimeiroAviso());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesEncerramentoEtapaSegundoAviso());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica());
					
					ps.setString(x++, configuracaoTCCVO.getPoliticaApresentarTCCAluno());
					ps.setBoolean(x++, configuracaoTCCVO.getOcultarOrientadoVisaoAluno());
					ps.setBoolean(x++, configuracaoTCCVO.getValidarPendenciaFinanceira());
					ps.setBoolean(x++, configuracaoTCCVO.getValidarPendenciaDocumento());
					ps.setString(x++, configuracaoTCCVO.getExtensoesPermitidasParaArquivos());
					ps.setInt(x++, configuracaoTCCVO.getOrientadorPadrao().getCodigo());
					ps.setString(x++, configuracaoTCCVO.getNomenclaturaUtilizarParaAvaliador());
					ps.setString(x++, configuracaoTCCVO.getNomenclaturaUtilizarParaComissao());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoPendenciaDoc());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoPendenciaFin());
					ps.setBoolean(x++, configuracaoTCCVO.getApresentarContasVencidas());
					ps.setBoolean(x++, configuracaoTCCVO.getUtilizarProfMinistCoordeConfTurma());
					ps.setBoolean(x++, configuracaoTCCVO.getComposicaoNotaQuest());
					ps.setDouble(x++, configuracaoTCCVO.getMediaAprovacao());
					ps.setDouble(x++, configuracaoTCCVO.getNotaMaximaMediaFormatacao());
					ps.setDouble(x++, configuracaoTCCVO.getNotaMaximaMediaConteudo());	
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoAlunoAprov());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoAlunoReprov());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getConfiguracaoTCCArtefatoFacade().incluirConfiguracaoTCCArtefatoVOs(configuracaoTCCVO);
			getFacadeFactory().getConfiguracaoTCCMembroBancaFacade().incluirMembrosBanca(configuracaoTCCVO.getCodigo(), configuracaoTCCVO.getMembroBancaPadraoVOs());
			getFacadeFactory().getQuestaoTCCFacade().incluirQuestaoFormatacao(configuracaoTCCVO, usuarioVO);
			getFacadeFactory().getQuestaoTCCFacade().incluirQuestaoConteudo(configuracaoTCCVO, usuarioVO);			
			configuracaoTCCVO.setNovoObj(false);
		} catch (Exception e) {
			configuracaoTCCVO.setNovoObj(true);
			configuracaoTCCVO.setCodigo(0);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final ConfiguracaoTCCVO configuracaoTCCVO, UsuarioVO usuarioVO) throws Exception {
		alterar(getIdEntidade(), true, usuarioVO);
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoTCC SET ");
					sql.append(" controlaPlanoTCC = ?, prazoExecucaoPlanoTCC = ?, prazoRespostaOrientadorPlanoTCC = ?, permiteInteracaoOrientadorPlanoTCC = ?, ");
					sql.append(" permiteSolicitarOrientador = ?, valorPagamentoOrientadorPlanoTCC = ?, valorPagamentoCoordenadorPlanoTCC = ?, controlaElaboracaoTCC = ?,");
					sql.append(" prazoExecucaoElaboracaoTCC = ?, prazoRespostaOrientadorElaboracaoTCC = ?, permiteInteracaoOrientadorElaboracaoTCC = ?, permiteArquivoApoioElaboracaoTCC = ?,");
					sql.append(" controlarHistoricoPostagemArquivoElaboracaoTCC = ?, valorPagamentoOrientadorElaboracaoTCC = ?, valorPagamentoCoordenadorElaboracaoTCC = ?, prazoExecucaoApresentacaoTCC = ?, ");
					sql.append(" exigePostagemArquivoFinalAvaliacaoTCC = ?, permiteArquivoApresentacaoTCC = ?, permiteArquivoApoioApresentacaoTCC = ?, permiteInteracaoOrientadorApresentacaoTCC = ?,");
					sql.append(" valorPagamentoOrientadorApresentacaoTCC = ?, valorPagamentoCoordenadorApresentacaoTCC = ?, controlarHistoricoPostagemArquivoApresentacaoTCC = ?, descricao = ?, ");
					sql.append(" tipoTCC = ?, orientacaoGeral = ?, orientacaoExtensaoPrazo = ?, numeroDiaAntesPrimeiraAulaLiberarAcessoTCC = ?,");
					sql.append(" apagarHistoricoPostagemArquivoAposFinalizacaoTCC = ?, dataBaseVencimentoPagamentoOrientacao = ?, dataBaseProcessamentoPagamentoOrientacao = ?,");
					sql.append(" controlaFrequencia = ?, numeroDiaAntesEncerramentoEtapaPrimeiroAviso = ?, numeroDiaAntesEncerramentoEtapaSegundoAviso = ?, numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica = ?, ");
					sql.append(" politicaApresentarTCCAluno=?, ocultarOrientadoVisaoAluno=?, validarPendenciaFinanceira=?, validarPendenciaDocumento=?, extensoesPermitidasParaArquivos=?, ");
					sql.append(" orientadorPadrao=?, nomenclaturaUtilizarParaAvaliador=?, nomenclaturaUtilizarParaComissao=?, textoOrientacaoPendenciaDoc=?, textoOrientacaoPendenciaFin=?, apresentarContasVencidas=?, utilizarProfMinistCoordeConfTurma=?, composicaoNotaQuest=?, mediaAprovacao=?, notaMaximaMediaFormatacao=?, notaMaximaMediaConteudo=?, textoOrientacaoAlunoAprov=?, textoOrientacaoAlunoReprov=? ");
					sql.append(" WHERE  codigo = ? ");
					
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setBoolean(x++, configuracaoTCCVO.getControlaPlanoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoPlanoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoRespostaOrientadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteSolicitarOrientador());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorPlanoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorPlanoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlaElaboracaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoElaboracaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoRespostaOrientadorElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApoioElaboracaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlarHistoricoPostagemArquivoElaboracaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorElaboracaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorApresentacaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getPrazoExecucaoApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getExigePostagemArquivoFinalAvaliacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteArquivoApoioApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getPermiteInteracaoOrientadorApresentacaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoOrientadorApresentacaoTCC());
					ps.setDouble(x++, configuracaoTCCVO.getValorPagamentoCoordenadorApresentacaoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getControlarHistoricoPostagemArquivoApresentacaoTCC());
					ps.setString(x++, configuracaoTCCVO.getDescricao());
					ps.setString(x++, configuracaoTCCVO.getTipoTCC().toString());
					ps.setString(x++, configuracaoTCCVO.getOrientacaoGeral());
					ps.setString(x++, configuracaoTCCVO.getOrientacaoExtensaoPrazo());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC());
					ps.setBoolean(x++, configuracaoTCCVO.getApagarHistoricoPostagemArquivoAposFinalizacaoTCC());
					ps.setInt(x++, configuracaoTCCVO.getDataBaseVencimentoPagamentoOrientacao());
					ps.setInt(x++, configuracaoTCCVO.getDataBaseProcessamentoPagamentoOrientacao());
					ps.setBoolean(x++, configuracaoTCCVO.getControlaFrequencia());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesEncerramentoEtapaPrimeiroAviso());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaAntesEncerramentoEtapaSegundoAviso());
					ps.setInt(x++, configuracaoTCCVO.getNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica());

					ps.setString(x++, configuracaoTCCVO.getPoliticaApresentarTCCAluno());
					ps.setBoolean(x++, configuracaoTCCVO.getOcultarOrientadoVisaoAluno());
					ps.setBoolean(x++, configuracaoTCCVO.getValidarPendenciaFinanceira());
					ps.setBoolean(x++, configuracaoTCCVO.getValidarPendenciaFinanceira());
					ps.setString(x++, configuracaoTCCVO.getExtensoesPermitidasParaArquivos());
					ps.setInt(x++, configuracaoTCCVO.getOrientadorPadrao().getCodigo());
					ps.setString(x++, configuracaoTCCVO.getNomenclaturaUtilizarParaAvaliador());
					ps.setString(x++, configuracaoTCCVO.getNomenclaturaUtilizarParaComissao());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoPendenciaDoc());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoPendenciaFin());
					ps.setBoolean(x++, configuracaoTCCVO.getApresentarContasVencidas());
					ps.setBoolean(x++, configuracaoTCCVO.getUtilizarProfMinistCoordeConfTurma());
					ps.setBoolean(x++, configuracaoTCCVO.getComposicaoNotaQuest());
					ps.setDouble(x++, configuracaoTCCVO.getMediaAprovacao());
					ps.setDouble(x++, configuracaoTCCVO.getNotaMaximaMediaFormatacao());
					ps.setDouble(x++, configuracaoTCCVO.getNotaMaximaMediaConteudo());					
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoAlunoAprov());
					ps.setString(x++, configuracaoTCCVO.getTextoOrientacaoAlunoReprov());
					
					ps.setInt(x++, configuracaoTCCVO.getCodigo());
					return ps;
				}
			});
			configuracaoTCCVO.setNovoObj(false);
			getFacadeFactory().getConfiguracaoTCCArtefatoFacade().alterarConfiguracaoTCCArtefatoVOs(configuracaoTCCVO);
			getFacadeFactory().getConfiguracaoTCCMembroBancaFacade().alterarMembrosBanca(configuracaoTCCVO.getCodigo(), configuracaoTCCVO.getMembroBancaPadraoVOs());
			getFacadeFactory().getQuestaoTCCFacade().alterarQuestaoFormatacao(configuracaoTCCVO, usuarioVO);
			getFacadeFactory().getQuestaoTCCFacade().alterarQuestaoConteudo(configuracaoTCCVO, usuarioVO);			
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public List<ConfiguracaoTCCVO> consultarPorDescricao(String descricao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM ConfiguracaoTCC  ");
		sql.append(" where upper(sem_acentos(descricao)) like upper(sem_acentos('").append(descricao).append("%')) order by descricao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
	}

	private List<ConfiguracaoTCCVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<ConfiguracaoTCCVO> configuracaoTCCVOs = new ArrayList<ConfiguracaoTCCVO>(0);
		while (rs.next()) {
			configuracaoTCCVOs.add(montarDados(rs, nivelMontarDados));
		}
		return configuracaoTCCVOs;
	}

	private ConfiguracaoTCCVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		ConfiguracaoTCCVO obj = new ConfiguracaoTCCVO();
		obj.setNovoObj(false);
		obj.setApagarHistoricoPostagemArquivoAposFinalizacaoTCC(rs.getBoolean("apagarHistoricoPostagemArquivoAposFinalizacaoTCC"));
		obj.setCodigo(rs.getInt("codigo"));
		obj.setControlaElaboracaoTCC(rs.getBoolean("controlaElaboracaoTCC"));
		obj.setControlaPlanoTCC(rs.getBoolean("controlaPlanoTCC"));
		obj.setControlarHistoricoPostagemArquivoApresentacaoTCC(rs.getBoolean("controlarHistoricoPostagemArquivoApresentacaoTCC"));
		obj.setControlarHistoricoPostagemArquivoElaboracaoTCC(rs.getBoolean("controlarHistoricoPostagemArquivoElaboracaoTCC"));
		obj.setDataBaseProcessamentoPagamentoOrientacao(rs.getInt("dataBaseProcessamentoPagamentoOrientacao"));
		obj.setDataBaseVencimentoPagamentoOrientacao(rs.getInt("dataBaseVencimentoPagamentoOrientacao"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setExigePostagemArquivoFinalAvaliacaoTCC(rs.getBoolean("exigePostagemArquivoFinalAvaliacaoTCC"));
		obj.setNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC(rs.getInt("numeroDiaAntesPrimeiraAulaLiberarAcessoTCC"));
		obj.setOrientacaoExtensaoPrazo(rs.getString("orientacaoExtensaoPrazo"));
		obj.setOrientacaoGeral(rs.getString("orientacaoGeral"));
		obj.setPermiteArquivoApoioApresentacaoTCC(rs.getBoolean("permiteArquivoApoioApresentacaoTCC"));
		obj.setPermiteArquivoApoioElaboracaoTCC(rs.getBoolean("permiteArquivoApoioElaboracaoTCC"));
		obj.setPermiteArquivoApresentacaoTCC(rs.getBoolean("permiteArquivoApresentacaoTCC"));
		obj.setPermiteInteracaoOrientadorApresentacaoTCC(rs.getBoolean("permiteInteracaoOrientadorApresentacaoTCC"));
		obj.setPermiteInteracaoOrientadorElaboracaoTCC(rs.getBoolean("permiteInteracaoOrientadorElaboracaoTCC"));
		obj.setPermiteInteracaoOrientadorPlanoTCC(rs.getBoolean("permiteInteracaoOrientadorPlanoTCC"));
		obj.setPermiteSolicitarOrientador(rs.getBoolean("permiteSolicitarOrientador"));
		obj.setPrazoExecucaoApresentacaoTCC(rs.getInt("prazoExecucaoApresentacaoTCC"));
		obj.setPrazoExecucaoElaboracaoTCC(rs.getInt("prazoExecucaoElaboracaoTCC"));
		obj.setPrazoExecucaoPlanoTCC(rs.getInt("prazoExecucaoPlanoTCC"));
		obj.setPrazoRespostaOrientadorElaboracaoTCC(rs.getInt("prazoRespostaOrientadorElaboracaoTCC"));
		obj.setPrazoRespostaOrientadorPlanoTCC(rs.getInt("prazoRespostaOrientadorPlanoTCC"));
		obj.setValorPagamentoCoordenadorApresentacaoTCC(rs.getDouble("valorPagamentoCoordenadorApresentacaoTCC"));
		obj.setValorPagamentoCoordenadorElaboracaoTCC(rs.getDouble("valorPagamentoCoordenadorElaboracaoTCC"));
		obj.setValorPagamentoCoordenadorPlanoTCC(rs.getDouble("valorPagamentoCoordenadorPlanoTCC"));
		obj.setValorPagamentoOrientadorApresentacaoTCC(rs.getDouble("valorPagamentoOrientadorApresentacaoTCC"));
		obj.setValorPagamentoOrientadorElaboracaoTCC(rs.getDouble("valorPagamentoOrientadorElaboracaoTCC"));
		obj.setValorPagamentoOrientadorPlanoTCC(rs.getDouble("valorPagamentoOrientadorPlanoTCC"));
		obj.setTipoTCC(TipoTCCEnum.valueOf(rs.getString("tipoTCC")));
		obj.setControlaFrequencia(rs.getBoolean("controlaFrequencia"));
		obj.setNumeroDiaAntesEncerramentoEtapaPrimeiroAviso(rs.getInt("numeroDiaAntesEncerramentoEtapaPrimeiroAviso"));
		obj.setNumeroDiaAntesEncerramentoEtapaSegundoAviso(rs.getInt("numeroDiaAntesEncerramentoEtapaSegundoAviso"));
		obj.setNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica(rs.getInt("numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica"));

		obj.setPoliticaApresentarTCCAluno(rs.getString("politicaApresentarTCCAluno"));
		obj.setOcultarOrientadoVisaoAluno(rs.getBoolean("ocultarOrientadoVisaoAluno"));
		obj.setValidarPendenciaFinanceira(rs.getBoolean("validarPendenciaFinanceira"));
		obj.setValidarPendenciaDocumento(rs.getBoolean("validarPendenciaDocumento"));
		obj.setExtensoesPermitidasParaArquivos(rs.getString("extensoesPermitidasParaArquivos"));
		obj.getOrientadorPadrao().setCodigo(rs.getInt("orientadorPadrao"));
		obj.setNomenclaturaUtilizarParaAvaliador(rs.getString("nomenclaturaUtilizarParaAvaliador"));
		obj.setNomenclaturaUtilizarParaComissao(rs.getString("nomenclaturaUtilizarParaComissao"));
		obj.setTextoOrientacaoPendenciaDoc(rs.getString("textoOrientacaoPendenciaDoc"));
		obj.setTextoOrientacaoPendenciaFin(rs.getString("textoOrientacaoPendenciaFin"));
		obj.setApresentarContasVencidas(rs.getBoolean("apresentarContasVencidas"));
		obj.setUtilizarProfMinistCoordeConfTurma(rs.getBoolean("utilizarProfMinistCoordeConfTurma"));
		obj.setComposicaoNotaQuest(rs.getBoolean("composicaoNotaQuest"));
		obj.setMediaAprovacao(rs.getDouble("mediaAprovacao"));
		obj.setNotaMaximaMediaFormatacao(rs.getDouble("notaMaximaMediaFormatacao"));
		obj.setNotaMaximaMediaConteudo(rs.getDouble("notaMaximaMediaConteudo"));
		
		obj.setTextoOrientacaoAlunoAprov(rs.getString("textoOrientacaoAlunoAprov"));
		obj.setTextoOrientacaoAlunoReprov(rs.getString("textoOrientacaoAlunoReprov"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setConfiguracaoTCCArtefatoVOs(getFacadeFactory().getConfiguracaoTCCArtefatoFacade().consultarPorConfiguracaoTCC(obj.getCodigo()));
		obj.setMembroBancaPadraoVOs(getFacadeFactory().getConfiguracaoTCCMembroBancaFacade().consultarPorTCC(obj.getCodigo()));
		obj.setQuestaoConteudoVOs(getFacadeFactory().getQuestaoTCCFacade().consultarPorConfiguracao(obj.getCodigo(), "conteudo"));
		obj.setQuestaoFormatacaoVOs(getFacadeFactory().getQuestaoTCCFacade().consultarPorConfiguracao(obj.getCodigo(), "formatacao"));
		return obj;
	}

	@Override
	public void adicionarConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO, ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws Exception {
		getFacadeFactory().getConfiguracaoTCCArtefatoFacade().validarDados(configuracaoTCCArtefatoVO);
		for (ConfiguracaoTCCArtefatoVO objExist : configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs()) {
			if (objExist.getArtefato().trim().equalsIgnoreCase(configuracaoTCCArtefatoVO.getArtefato().trim())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoTCCArtefato_jaAdicionado"));
			}
		}
		configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs().add(configuracaoTCCArtefatoVO);
	}

	@Override
	public void removerConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO, ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws Exception {
		int index = 0;
		for (ConfiguracaoTCCArtefatoVO objExist : configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs()) {
			if (objExist.getArtefato().trim().equalsIgnoreCase(configuracaoTCCArtefatoVO.getArtefato().trim())) {
				configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs().remove(index);
				return;
			}
			index++;
		}

	}

    public void alterarOrdemQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO, QuestaoTCCVO questaoTCCVO2) throws Exception {
        Integer ordem1 = questaoTCCVO.getOrdemApresentacao();
        questaoTCCVO.setOrdemApresentacao(questaoTCCVO2.getOrdemApresentacao());
        questaoTCCVO2.setOrdemApresentacao(ordem1);
        Ordenacao.ordenarLista(configuracaoVO.getQuestaoConteudoVOs(), "ordemApresentacao");
    }
    
    public void adicionarQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO) throws Exception {
        int index = 0;
        Iterator i = configuracaoVO.getQuestaoFormatacaoVOs().iterator();
        while (i.hasNext()) {
        	QuestaoTCCVO objExistente = (QuestaoTCCVO) i.next();
            if (objExistente.getEnunciado().equals(questaoTCCVO.getEnunciado())) {
            	configuracaoVO.getQuestaoFormatacaoVOs().set(index, questaoTCCVO);
                return;
            }
            index++;
        }
        configuracaoVO.getQuestaoFormatacaoVOs().add(questaoTCCVO);    	
    }

    public void removerQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoVO) throws Exception {
    	int index = 0;
        Iterator i = configuracaoVO.getQuestaoFormatacaoVOs().iterator();
        while (i.hasNext()) {
        	QuestaoTCCVO objExistente = (QuestaoTCCVO) i.next();
            if (objExistente.getEnunciado().equals(questaoVO.getEnunciado())) {
            	configuracaoVO.getQuestaoFormatacaoVOs().remove(index);
                return;
            }
            index++;
        }
    }
	
    public void adicionarQuestaoConteudo(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO) throws Exception {
        int index = 0;
        Iterator i = configuracaoVO.getQuestaoConteudoVOs().iterator();
        while (i.hasNext()) {
        	QuestaoTCCVO objExistente = (QuestaoTCCVO) i.next();
            if (objExistente.getEnunciado().equals(questaoTCCVO.getEnunciado())) {
            	configuracaoVO.getQuestaoConteudoVOs().set(index, questaoTCCVO);
                return;
            }
            index++;
        }
        configuracaoVO.getQuestaoConteudoVOs().add(questaoTCCVO);    	
    }

    public void removerQuestaoConteudo(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoVO) throws Exception {
    	int index = 0;
        Iterator i = configuracaoVO.getQuestaoConteudoVOs().iterator();
        while (i.hasNext()) {
        	QuestaoTCCVO objExistente = (QuestaoTCCVO) i.next();
            if (objExistente.getEnunciado().equals(questaoVO.getEnunciado())) {
            	configuracaoVO.getQuestaoConteudoVOs().remove(index);
                return;
            }
            index++;
        }
    }
	
	@Override
	public void validarDados(ConfiguracaoTCCVO configuracaoTCCVO) throws ConsistirException {
		if (configuracaoTCCVO.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_descricao"));
		}
//		if (configuracaoTCCVO.getDataBaseVencimentoPagamentoOrientacao() == null || configuracaoTCCVO.getDataBaseVencimentoPagamentoOrientacao() == 0) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_dataBaseVencimentoPagamentoOrientacao"));
//		}
//		if (configuracaoTCCVO.getDataBaseProcessamentoPagamentoOrientacao() == null || configuracaoTCCVO.getDataBaseProcessamentoPagamentoOrientacao() == 0) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_dataBaseProcessamentoPagamentoOrientacao"));
//		}
		if (configuracaoTCCVO.getControlaPlanoTCC() && (configuracaoTCCVO.getPrazoExecucaoPlanoTCC() == null || configuracaoTCCVO.getPrazoExecucaoPlanoTCC() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_prazoExecucaoPlanoTCC"));
		}
		if (configuracaoTCCVO.getControlaPlanoTCC() && (configuracaoTCCVO.getPrazoRespostaOrientadorPlanoTCC() == null || configuracaoTCCVO.getPrazoRespostaOrientadorPlanoTCC() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_prazoRespostaOrientadorPlanoTCC"));
		}
		if (configuracaoTCCVO.getControlaElaboracaoTCC() && (configuracaoTCCVO.getPrazoExecucaoElaboracaoTCC() == null || configuracaoTCCVO.getPrazoExecucaoElaboracaoTCC() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_prazoExecucaoElaboracaoTCC"));
		}
		if (configuracaoTCCVO.getControlaElaboracaoTCC() && (configuracaoTCCVO.getPrazoRespostaOrientadorElaboracaoTCC() == null || configuracaoTCCVO.getPrazoRespostaOrientadorElaboracaoTCC() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_prazoRespostaOrientadorElaboracaoTCC"));
		}
		if (configuracaoTCCVO.getPrazoExecucaoApresentacaoTCC() == null || configuracaoTCCVO.getPrazoExecucaoApresentacaoTCC() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCC_prazoExecucaoApresentacaoTCC"));
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoTCC.idEntidade = idEntidade;
	}
	
	@Override
	public ConfiguracaoTCCVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception{
		StringBuilder sql = new StringBuilder("SELECT * FROM ConfiguracaoTCC WHERE codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs, nivelMontarDados);
		}
		throw new Exception("Dados n?o encontrados(Configura??o TCC).");
	}
	
	@Override
	public ConfiguracaoTCCVO consultarPorCurso(int curso, int nivelMontarDados) throws Exception{
		StringBuilder sql = new StringBuilder("SELECT ConfiguracaoTCC.* FROM ConfiguracaoTCC ");
		sql.append(" inner join curso on curso.ConfiguracaoTCC = ConfiguracaoTCC.codigo ");
		sql.append(" WHERE curso.codigo = ").append(curso);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs, nivelMontarDados);
		}
		return null;
	}

}