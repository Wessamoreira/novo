package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.ItemPlanoFinanceiroAluno;
import negocio.facade.jdbc.academico.PlanoFinanceiroAluno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.UnidadeEnsinoInterfaceFacade;
import negocio.interfaces.financeiro.MatriculaPeriodoVencimentoInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaPeriodoVencimentoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MatriculaPeriodoVencimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaPeriodoVencimentoVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class MatriculaPeriodoVencimento extends ControleAcesso implements MatriculaPeriodoVencimentoInterfaceFacade {

	protected static String idEntidade;
	protected UnidadeEnsinoInterfaceFacade unidadeEnsinoFacade = null;
	public static final long serialVersionUID = 1L;

	public MatriculaPeriodoVencimento() throws Exception {
		super();
		setIdEntidade("ContaReceber");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
	 */
	public MatriculaPeriodoVencimentoVO novo() throws Exception {
		MatriculaPeriodoVencimento.incluir(getIdEntidade());
		MatriculaPeriodoVencimentoVO obj = new MatriculaPeriodoVencimentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			MatriculaPeriodoVencimentoVO.validarDados(obj);
			final String sql = "INSERT INTO MatriculaPeriodoVencimento( data, matriculaPeriodo, situacao, contaReceber, " + "vencimentoReferenteMatricula, dataVencimento, valor, valorDesconto,  " + "valorDescontoInstituicao, valorDescontoConvenio, parcela, tipoDesconto, tipoBoleto, " + "vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina, descricaoPagamento, diasVariacaoDataVencimento, " + "valorDescontoCalculadoPrimeiraFaixaDescontos, valorDescontoCalculadoSegundaFaixaDescontos, " + "valorDescontoCalculadoTerceiraFaixaDescontos, valorDescontoCalculadoQuartaFaixaDescontos, dataCompetencia, financeiroManual, acrescimoExtra, tipoOrigemMatriculaPeriodoVencimento, usaValorPrimeiraParcela  )" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setInt(2, obj.getMatriculaPeriodo());
					sqlInserir.setString(3, obj.getSituacao().getValor());
					if (!obj.getContaReceber().getCodigo().equals(0)) {
						sqlInserir.setInt(4, obj.getContaReceber().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setBoolean(5, obj.getVencimentoReferenteMatricula());
					sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(7, obj.getValor().doubleValue());
					sqlInserir.setDouble(8, obj.getValorDesconto().doubleValue());
					sqlInserir.setDouble(9, obj.getValorDescontoInstituicao().doubleValue());
					sqlInserir.setDouble(10, obj.getValorDescontoConvenio().doubleValue());
					sqlInserir.setString(11, obj.getParcela());
					sqlInserir.setString(12, obj.getTipoDesconto());
					sqlInserir.setString(13, obj.getTipoBoleto());
					sqlInserir.setBoolean(14, obj.getVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina());
					sqlInserir.setString(15, obj.getDescricaoPagamento());
					sqlInserir.setInt(16, obj.getDiasVariacaoDataVencimento());
					sqlInserir.setDouble(17, obj.getValorDescontoCalculadoPrimeiraFaixaDescontos());
					sqlInserir.setDouble(18, obj.getValorDescontoCalculadoSegundaFaixaDescontos());
					sqlInserir.setDouble(19, obj.getValorDescontoCalculadoTerceiraFaixaDescontos());
					sqlInserir.setDouble(20, obj.getValorDescontoCalculadoQuartaFaixaDescontos());

					sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataCompetencia()));
					sqlInserir.setBoolean(22, obj.getFinanceiroManual());
					sqlInserir.setDouble(23, obj.getAcrescimoExtra());
					int i = 23;
					Uteis.setValuePreparedStatement(obj.getTipoOrigemMatriculaPeriodoVencimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isUsaValorPrimeiraParcela(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void alterar(MatriculaPeriodoVencimentoVO obj) throws Exception {
	// try {
	// MatriculaPeriodoVencimento.alterar(getIdEntidade());
	// alterar(obj, false);
	// } catch (Exception e) {
	// throw e;
	// }
	// }
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaPeriodoVencimentoVO obj, boolean modificarSituacaoContaReceber, UsuarioVO usuario) throws SQLException, Exception {
		try {
			MatriculaPeriodoVencimentoVO.validarDados(obj);
			final String sql = "UPDATE MatriculaPeriodoVencimento SET data = ?, matriculaPeriodo = ?, situacao =?, contaReceber= ?, " +
					"vencimentoReferenteMatricula = ?, dataVencimento = ?, valor = ?, valorDesconto = ?,  "
					+ "valorDescontoInstituicao = ?, valorDescontoConvenio = ?, parcela = ?, tipoDesconto = ?, tipoBoleto =?, "
					+ "vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina=?, descricaoPagamento=?, diasVariacaoDataVencimento=?, "
					+ "valorDescontoCalculadoPrimeiraFaixaDescontos=?, valorDescontoCalculadoSegundaFaixaDescontos=?, "
					+ "valorDescontoCalculadoTerceiraFaixaDescontos=?, valorDescontoCalculadoQuartaFaixaDescontos=?, dataCompetencia=?, "
					+ "financeiroManual=?, geracaomanualparcela = ?, erroGeracaoParcela = ?, mensagemErroGeracaoParcela = ?, "
					+ " acrescimoExtra = ?, tipoOrigemMatriculaPeriodoVencimento=?, usaValorPrimeiraParcela=?  WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (!obj.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA) && !obj.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_DEVE_SER_GERADA)) {
				obj.setErroGeracaoParcela(false);
				obj.setMensagemErroGeracaoParcela("");
			}
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlPS = cnctn.prepareStatement(sql);
					sqlPS.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlPS.setInt(2, obj.getMatriculaPeriodo());
					sqlPS.setString(3, obj.getSituacao().getValor());
					if (!obj.getContaReceber().getCodigo().equals(0)) {
						sqlPS.setInt(4, obj.getContaReceber().getCodigo());
					} else {
						sqlPS.setNull(4, 0);
					}
					sqlPS.setBoolean(5, obj.getVencimentoReferenteMatricula());
					sqlPS.setDate(6, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlPS.setDouble(7, obj.getValor().doubleValue());
					sqlPS.setDouble(8, obj.getValorDesconto().doubleValue());
					sqlPS.setDouble(9, obj.getValorDescontoInstituicao().doubleValue());
					sqlPS.setDouble(10, obj.getValorDescontoConvenio().doubleValue());
					sqlPS.setString(11, obj.getParcela());
					sqlPS.setString(12, obj.getTipoDesconto());
					sqlPS.setString(13, obj.getTipoBoleto());
					sqlPS.setBoolean(14, obj.getVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina());
					sqlPS.setString(15, obj.getDescricaoPagamento());
					sqlPS.setInt(16, obj.getDiasVariacaoDataVencimento());
					sqlPS.setDouble(17, obj.getValorDescontoCalculadoPrimeiraFaixaDescontos());
					sqlPS.setDouble(18, obj.getValorDescontoCalculadoSegundaFaixaDescontos());
					sqlPS.setDouble(19, obj.getValorDescontoCalculadoTerceiraFaixaDescontos());
					sqlPS.setDouble(20, obj.getValorDescontoCalculadoQuartaFaixaDescontos());

					sqlPS.setDate(21, Uteis.getDataJDBC(obj.getDataCompetencia()));
					sqlPS.setBoolean(22, obj.getFinanceiroManual());
					if (obj.getGeracaoManualParcela() != null && obj.getGeracaoManualParcela() > 0) {
						sqlPS.setInt(23, obj.getGeracaoManualParcela());
					} else {
						sqlPS.setNull(23, 0);
					}
					sqlPS.setBoolean(24, obj.getErroGeracaoParcela());
					sqlPS.setString(25, obj.getMensagemErroGeracaoParcela());
					sqlPS.setDouble(26, obj.getAcrescimoExtra());
					int i = 26;
					Uteis.setValuePreparedStatement(obj.getTipoOrigemMatriculaPeriodoVencimento(), ++i, sqlPS);
					Uteis.setValuePreparedStatement(obj.isUsaValorPrimeiraParcela(), ++i, sqlPS);
					sqlPS.setInt(++i, obj.getCodigo());
					return sqlPS;
				}
			});
		} catch (Exception e) {
			e.getMessage();
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>. Sempre localiza o registro a ser excluído através do codigo da ContaReceberVO. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaReceber(Integer contaReceber, UsuarioVO usuario) throws Exception {
		try {
			MatriculaPeriodoVencimento.excluir(getIdEntidade());
			String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((contaReceber = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { contaReceber });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaPeriodoContaReceberNaoPagaNaoNegociada(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		try {
			MatriculaPeriodoVencimento.excluir(getIdEntidade());
			String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE (matriculaPeriodo = ?) and situacao <> 'GP' and contareceber not in(" + " select codigo from contareceber where matriculaPeriodo = ? and (situacao = 'RE' OR situacao = 'NE'))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaPeriodo, matriculaPeriodo });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * possuiValorRateio = quando existe valor a ser restituido ao aluno e o mesmo possui contas vencidas e na condicao de pagamento do curso informa que não deve ser regerado contas vencidas, esta regra é desconsiderada para que o valor a ser rateado possa ser aplicado nas contas vencidas, fazendo assim o abatimento das contas do aluno.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSemCommitVctoNaoPagos(MatriculaPeriodoVO matriculaPeriodoVO, Boolean possuiValorRateio, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoVencimentoVO> listaRegerar = new ArrayList<MatriculaPeriodoVencimentoVO>(1);
		int posicao = matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs().size() - 1;
		while (posicao >= 0) {
			MatriculaPeriodoVencimentoVO vctoExcluir = (MatriculaPeriodoVencimentoVO) matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs().get(posicao);
			boolean contaImportadaSistemaLegado = getFacadeFactory().getContaReceberFacade().executarVerificarContaReceberImportadaSistemaLegada(vctoExcluir.getContaReceber().getCodigo(), usuario);
			if ((!contaImportadaSistemaLegado) && (!vctoExcluir.getVencimentoReferenteMatricula())
					&& (!vctoExcluir.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA)
							&& !vctoExcluir.getContaReceber().getSituacao().equals("NE")
							&& !vctoExcluir.getContaReceber().getContaEditadaManualmente()
							&& (!matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoRegerarParcelaVencida()
									|| (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoRegerarParcelaVencida()
											&& vctoExcluir.getContaReceber().getCodigo() == 0)
									|| (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoRegerarParcelaVencida()
											&& vctoExcluir.getContaReceber().getCodigo() > 0
											&& vctoExcluir.getContaReceber().getSituacao().equals("AR")
											&& (Uteis.getDataJDBC(vctoExcluir.getContaReceber().getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) > 0 || possuiValorRateio))))) {
				if (vctoExcluir.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA)) {
					MatriculaPeriodoVencimentoVO mpvRegerar = new MatriculaPeriodoVencimentoVO();
					mpvRegerar.setTipoOrigemMatriculaPeriodoVencimento(vctoExcluir.getTipoOrigemMatriculaPeriodoVencimento());
					mpvRegerar.setParcela(vctoExcluir.getParcela());
					if (mpvRegerar.getParcela().contains("/")) {
						mpvRegerar.setParcela(mpvRegerar.getParcela().substring(0, mpvRegerar.getParcela().indexOf("/")));
					}
					listaRegerar.add(mpvRegerar);
				}
				matriculaPeriodoVO.excluirObjMatriculaPeriodoVencimentoVOs(vctoExcluir);
				excluirMatriculaPeriodoVencimentoSemCommit(matriculaPeriodoVO, usuario, vctoExcluir);
			} else if (!matriculaPeriodoVO.getPlanoFinanceiroCurso().getModeloGeracaoParcelas().equals("FC") && (vctoExcluir.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA) || vctoExcluir.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_RENEGOCIADA))) {
				getFacadeFactory().getContaReceberFacade().excluirContasReceberTipoOrigemCodigoOrigem("BCC", matriculaPeriodoVO.getCodigo(), "AR", vctoExcluir.getParcelaCusteadaConvenio(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo(),matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().intValue()), !matriculaPeriodoVO.getBloqueioPorFechamentoMesLiberado(),  usuario);
			}
			posicao--;
		}
		matriculaPeriodoVO.setListaParcelasGeradasManualmenteAnteriormenteAptasParaRegeracao(listaRegerar);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSemCommitVctoMatricula(MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception {
		Iterator<MatriculaPeriodoVencimentoVO> i = matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO =  i.next();
			if(matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMatricula()) {
				getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirMatriculaPeriodoVencimentoSemCommit(matriculaPeriodoVO, usuario, matriculaPeriodoVencimentoVO);
				i.remove();
				break;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoVencimentoSemCommit(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, MatriculaPeriodoVencimentoVO vctoExcluir) throws Exception {
		getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluir(vctoExcluir.getCodigo(), usuario);
		getFacadeFactory().getContaReceberFacade().excluirContasReceberTipoOrigemCodigoOrigem("BCC", matriculaPeriodoVO.getCodigo(), "AR", vctoExcluir.getParcelaCusteadaConvenio(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo(),matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().intValue()), !matriculaPeriodoVO.getBloqueioPorFechamentoMesLiberado(),  usuario);
		if (vctoExcluir.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA)) {
			Integer notaFiscal = getFacadeFactory().getContaReceberFacade().consultarNumeroNotaFiscalSaidaServicoPorContaReceber(vctoExcluir.getContaReceber().getCodigo());
			getFacadeFactory().getContaReceberFacade().consultarContasReceberTipoOrigemCodigoOrigemSituacaoParcela(vctoExcluir.getParcela(), matriculaPeriodoVO, usuario);
			if(matriculaPeriodoVO.getBloqueioPorFechamentoMesLiberado()) {
				vctoExcluir.getContaReceber().liberarVerificacaoBloqueioFechamentoMes();
			}	
			getFacadeFactory().getControleCobrancaFacade().excluirControleRemessaContaReceber(vctoExcluir.getContaReceber(), usuario);
			getFacadeFactory().getContaReceberFacade().excluir(vctoExcluir.getContaReceber(), usuario);			
			if (Uteis.isAtributoPreenchido(notaFiscal)) {
				vctoExcluir.getContaReceber().getNotaFiscalSaidaServicoVO().setCodigo(notaFiscal);
				matriculaPeriodoVO.getContaReceberNotaFiscalEmitidaVOs().add(vctoExcluir.getContaReceber());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodos(Integer codigoMatriculaPeriodo, UsuarioVO usuario) throws Exception {
		// MatriculaPeriodoVencimento.excluir(getIdEntidade());
		String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((matriculaPeriodo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { codigoMatriculaPeriodo });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoExcluir, UsuarioVO usuario) throws Exception {

		String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { codigoExcluir });
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>MatriculaPeriodoVencimentoVO</code> resultantes da consulta.
	 */
	public static List<MatriculaPeriodoVencimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoVencimentoVO> vetResultado = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
	 * 
	 * @return O objeto da classe <code>MatriculaPeriodoVencimentoVO</code> com os dados devidamente montados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MatriculaPeriodoVencimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVencimentoVO obj = new MatriculaPeriodoVencimentoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setGeracaoManualParcela(dadosSQL.getInt("geracaoManualParcela"));
		obj.setErroGeracaoParcela(dadosSQL.getBoolean("erroGeracaoParcela"));
		obj.setMensagemErroGeracaoParcela(dadosSQL.getString("mensagemErroGeracaoParcela"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setMatriculaPeriodo(dadosSQL.getInt("matriculaPeriodo"));
		obj.setSituacao(SituacaoVencimentoMatriculaPeriodo.getEnum(dadosSQL.getString("situacao")));
		obj.getContaReceber().setCodigo(dadosSQL.getInt("contaReceber"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoOrigemMatriculaPeriodoVencimento"))) {
			obj.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.valueOf(dadosSQL.getString("tipoOrigemMatriculaPeriodoVencimento")));
		}
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
		obj.setValorDescontoInstituicao(dadosSQL.getDouble("valorDescontoInstituicao"));
		obj.setValorDescontoConvenio(dadosSQL.getDouble("valorDescontoConvenio"));
		obj.setAcrescimoExtra(dadosSQL.getDouble("acrescimoExtra"));
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setTipoDesconto(dadosSQL.getString("tipoDesconto"));
		obj.setTipoBoleto(dadosSQL.getString("tipoboleto"));
		obj.setDescricaoPagamento(dadosSQL.getString("descricaoPagamento"));
		obj.setUsaValorPrimeiraParcela(dadosSQL.getBoolean("usaValorPrimeiraParcela"));
		obj.setVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina(dadosSQL.getBoolean("vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina"));
		obj.setDiasVariacaoDataVencimento(dadosSQL.getInt("diasVariacaoDataVencimento"));

		obj.setDataCompetencia(dadosSQL.getDate("dataCompetencia"));
		obj.setFinanceiroManual(dadosSQL.getBoolean("financeiroManual"));

		obj.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
		obj.setValorDescontoCalculadoSegundaFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoSegundaFaixaDescontos"));
		obj.setValorDescontoCalculadoTerceiraFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoTerceiraFaixaDescontos"));
		obj.setValorDescontoCalculadoQuartaFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoQuartaFaixaDescontos"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosBasicosContaReceberAtualizarSituacaoMatriculaPeriodoVencimento(obj, configuracaoFinanceiroVO, usuario);
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>MatriculaPeriodoVencimentoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public MatriculaPeriodoVencimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {

		String sql = "SELECT * FROM MatriculaPeriodoVencimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Matrícula Período Vencimento).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return MatriculaPeriodoVencimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		MatriculaPeriodoVencimento.idEntidade = idEntidade;
	}

	public static MatriculaPeriodoVencimentoVO gerarMatriculaPeriodoVencimentoEquivalenteContaReceberVO(ContaReceberVO contaReceberBase, String parcela) {
		MatriculaPeriodoVencimentoVO matPerVO = new MatriculaPeriodoVencimentoVO();
		matPerVO.setContaReceber(contaReceberBase);
		matPerVO.setData(contaReceberBase.getData());
		matPerVO.setDataVencimento(contaReceberBase.getDataVencimento());
		matPerVO.setDescricaoPagamento(contaReceberBase.getDescricaoPagamento());
		matPerVO.setParcela(parcela);
		matPerVO.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA);
		if (contaReceberBase.getSituacaoEQuitada()) {
			matPerVO.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA);
		}
		if (contaReceberBase.getTipoOrigem().equals("MAT")) {
			matPerVO.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MATRICULA);
			matPerVO.setTipoBoleto("MA");
		} else if (contaReceberBase.getTipoOrigem().equals("MEN")) {
			matPerVO.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MENSALIDADE);
			matPerVO.setTipoBoleto("ME");
		} else if (contaReceberBase.getTipoOrigem().equals("MDI")) {
			matPerVO.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MATERIAL_DIDATICO);
			matPerVO.setTipoBoleto(TipoBoletoBancario.MATERIAL_DIDATICO.getValor());
		}
		matPerVO.setTipoDesconto(contaReceberBase.getTipoDesconto());
		matPerVO.setValor(contaReceberBase.getValor());
		matPerVO.setValorDesconto(contaReceberBase.getValorDesconto());
		matPerVO.setValorDescontoConvenio(contaReceberBase.getValorDescontoConvenio());
		matPerVO.setValorDescontoInstituicao(contaReceberBase.getValorDescontoInstituicao());
		return matPerVO;
	}

	public static List<MatriculaPeriodoVencimentoVO> verificarContasReceberPreExistentesEGerarMatriculaPeriodoVencimentoVOCorrespondente(MatriculaPeriodoVO matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoVencimentoVO> objetosResultadoFinal = new ArrayList(0);
		// getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(matriculaPeriodo,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		List<ContaReceberVO> obsMatricula = getFacadeFactory().getContaReceberFacade().consultarPorCodOrigemTipoOrigem("MAT", matriculaPeriodo.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
		if (obsMatricula.size() > 0) {
			ContaReceberVO contaReceberMatricula = (ContaReceberVO) obsMatricula.get(0);
			MatriculaPeriodoVencimentoVO vctoReferenteMatricula = gerarMatriculaPeriodoVencimentoEquivalenteContaReceberVO(contaReceberMatricula, "MA");
			objetosResultadoFinal.add(vctoReferenteMatricula);
		}
		List<ContaReceberVO> obsMensalidade = getFacadeFactory().getContaReceberFacade().consultarPorCodOrigemTipoOrigem("MEN", matriculaPeriodo.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
		if (obsMensalidade.size() > 0) {
			int nrParcelas = obsMensalidade.size();
			if (matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
				nrParcelas = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario).getNrParcelasPeriodo();
			}
			// int nrParcelas = obsMensalidade.size();
			int contador = 1;
			Iterator i = obsMensalidade.iterator();
			while (i.hasNext()) {
				ContaReceberVO contaReceberMensalidade = (ContaReceberVO) i.next();
				MatriculaPeriodoVencimentoVO vctoReferenteVencimento = gerarMatriculaPeriodoVencimentoEquivalenteContaReceberVO(contaReceberMensalidade, contador + "/" + nrParcelas);
				objetosResultadoFinal.add(vctoReferenteVencimento);
				contador++;
			}
		}
		return objetosResultadoFinal;
	}

	public static List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoVOs(MatriculaPeriodoVO matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVencimento.consultar(getIdEntidade(), false, usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM MatriculaPeriodoVencimento WHERE matriculaPeriodo = ? ORDER BY dataVencimento";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matriculaPeriodo.getCodigo() });
		while (resultado.next()) {
			MatriculaPeriodoVencimentoVO novoObj = new MatriculaPeriodoVencimentoVO();
			novoObj = MatriculaPeriodoVencimento.montarDados(resultado, nivelMontarDados, configuracaoFinanceiroVO, usuario);
			objetos.add(novoObj);
		}
		if ((objetos.isEmpty()) && ((nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS) || (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS))) {
			return verificarContasReceberPreExistentesEGerarMatriculaPeriodoVencimentoVOCorrespondente(matriculaPeriodo, configuracaoFinanceiroVO, usuario);
		}
		return objetos;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaPeriodoVencimentoVO consultarPorContaReceber(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM matriculaPeriodoVencimento WHERE matriculaPeriodoVencimento.contareceber = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Matrícula Período Vencimento).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaPeriodoVencimentoVO consultarMatriculaVencimentoPorContaReceber(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM matriculaPeriodoVencimento WHERE matriculaPeriodoVencimento.contareceber = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new MatriculaPeriodoVencimentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public List<MatriculaPeriodoVencimentoVO> consultarPorMatricula(String matricula, String parcela, String ano, String semestre, Boolean contaVencida, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT mpv.* FROM matriculaperiodovencimento mpv ");
		sqlStr.append("INNER JOIN matriculaperiodo mp ON mp.codigo = mpv.matriculaperiodo ");
		sqlStr.append("WHERE mp.matricula = '").append(matricula).append("' ");
		if (Uteis.isAtributoPreenchido(parcela)) {
			sqlStr.append("AND mpv.parcela = '").append(parcela).append("' ");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append("AND mp.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append("AND mp.semestre = '").append(semestre).append("' ");
		}
		if (!contaVencida) {
			sqlStr.append("AND mpv.datavencimento >= current_date ");
		}
		sqlStr.append("AND mpv.situacao NOT IN('NG', 'GP', 'PP') ");
		filtroRelatorioFinanceiroVO.ifPresent(filtro -> {
			sqlStr.append(" AND mpv.tipoorigemmatriculaperiodovencimento IN (''");
			sqlStr.append(filtro.getTipoOrigemMensalidade() ? ", 'MENSALIDADE'" : "");
			sqlStr.append(filtro.getTipoOrigemMaterialDidatico() ? ", 'MATERIAL_DIDATICO'" : "");
			sqlStr.append(filtro.getTipoOrigemMatricula() ? ", 'MATRICULA'" : "");
			sqlStr.append(" )");
		});
		sqlStr.append("ORDER BY mpv.dataVencimento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario);
	}

	public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoNaoPagaPorMatricula(String matricula, Date dataInicial, Date dataFinal, Boolean consultarContaVencida, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("SELECT * FROM matriculaperiodovencimento mpv INNER JOIN matriculaperiodo mp ON mpv.matriculaperiodo = mp.codigo inner join contareceber on mpv.contareceber = contareceber.codigo WHERE mp.matricula = ? AND mpv.situacao <> 'GP' and mpv.situacao <> 'NA' ");
		sql.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contaReceber"));
		if (consultarContaVencida && dataInicial != null && dataFinal != null) {
			sql.append(" AND (  mpv.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicial)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' ) ");
		}else if (!consultarContaVencida && dataInicial != null && dataFinal != null) {
			if(UteisData.getCompareData(new Date(), dataInicial) > 0){
				dataInicial = new Date();
			}
			sql.append(" AND (  mpv.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicial)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' ) ");			
		}else if (!consultarContaVencida) {
			sql.append(" AND mpv.datavencimento >= current_date ");
		}
		sql.append(" ORDER BY mpv.datavencimento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { matricula });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoVOsSituacao(Integer matriculaPeriodo, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVencimento.consultar(getIdEntidade(), false, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM MatriculaPeriodoVencimento WHERE matriculaPeriodo = ?");
		if (!situacao.equals("")) {
			sql.append(" AND matriculaperiodovencimento.situacao = '" + situacao + "'");
		}
		sql.append("ORDER BY dataVencimento");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { matriculaPeriodo });
		while (resultado.next()) {
			MatriculaPeriodoVencimentoVO novoObj = new MatriculaPeriodoVencimentoVO();
			novoObj = MatriculaPeriodoVencimento.montarDados(resultado, nivelMontarDados, configuracaoFinanceiroVO, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	private static void montarDadosBasicosContaReceberAtualizarSituacaoMatriculaPeriodoVencimento(MatriculaPeriodoVencimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (obj.getContaReceber().getCodigo().intValue() == 0) {
			obj.setContaReceber(new ContaReceberVO());
			return;
		}
		getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceber(), NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuario);
		obj.atualizarSituacaoMatriculaPeriodoVencimentoDeAcordoContaReceber();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoVencimentoVOsNaoUtilizadosMais(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		// MatriculaPeriodoVencimento.excluir(getIdEntidade());
		String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((matriculaPeriodo = ?))";

		Iterator i = matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVencimentoVO vcto = (MatriculaPeriodoVencimentoVO) i.next();
			sql = sql + " AND (codigo != " + vcto.getCodigo() + ")";
		}
		getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaPeriodoVO.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarMatriculaPeriodoVencimentoVOs(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		excluirMatriculaPeriodoVencimentoVOsNaoUtilizadosMais(matriculaPeriodoVO, usuario);
		Iterator i = matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVencimentoVO vcto = (MatriculaPeriodoVencimentoVO) i.next();
			vcto.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
			if (vcto.getCodigo().equals(0)) {
				incluir(vcto, usuario);
			} else {
				alterar(vcto, false, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarMatriculaPeriodoVencimentoPelaContaReceberVO(ContaReceberVO contaReceberBase, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, UsuarioVO usuario) throws Exception {
		matriculaPeriodoVencimentoVo.setData(contaReceberBase.getData());
		matriculaPeriodoVencimentoVo.setDataVencimento(contaReceberBase.getDataVencimento());
		matriculaPeriodoVencimentoVo.setDescricaoPagamento(contaReceberBase.getDescricaoPagamento());
		if (contaReceberBase.getSituacaoEQuitada()) {
			matriculaPeriodoVencimentoVo.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA);
		} else {
			matriculaPeriodoVencimentoVo.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA);
		}
		if (contaReceberBase.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())) {
			matriculaPeriodoVencimentoVo.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MATRICULA);
			matriculaPeriodoVencimentoVo.setTipoBoleto(TipoBoletoBancario.MATRICULA.getValor());
		} else if (contaReceberBase.getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor())) {
			matriculaPeriodoVencimentoVo.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MATERIAL_DIDATICO);
			matriculaPeriodoVencimentoVo.setTipoBoleto(TipoBoletoBancario.MATERIAL_DIDATICO.getValor());
		} else if (contaReceberBase.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor())) {
			matriculaPeriodoVencimentoVo.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MENSALIDADE);
			matriculaPeriodoVencimentoVo.setTipoBoleto(TipoBoletoBancario.MENSALIDADE.getValor());
		}
		matriculaPeriodoVencimentoVo.setTipoDesconto(contaReceberBase.getTipoDesconto());
		matriculaPeriodoVencimentoVo.setValor(contaReceberBase.getValor());
		matriculaPeriodoVencimentoVo.setValorDesconto(contaReceberBase.getValorDescontoAlunoJaCalculado());
		matriculaPeriodoVencimentoVo.setValorDescontoConvenio(contaReceberBase.getValorDescontoConvenio());
		matriculaPeriodoVencimentoVo.setValorDescontoInstituicao(contaReceberBase.getValorDescontoInstituicao());
		alterar(matriculaPeriodoVencimentoVo, false, usuario);
	}

	public MatriculaPeriodoVencimentoVO preencherNovaMatriculaPeriodoVencimentoVo(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, MatriculaPeriodoVO matriculaPeriodoVo, UsuarioVO usuario) {
		matriculaPeriodoVencimentoVo.setMatriculaPeriodo(matriculaPeriodoVo.getCodigo().intValue());
		matriculaPeriodoVencimentoVo.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MENSALIDADE);
		matriculaPeriodoVencimentoVo.setTipoBoleto(TipoBoletoBancario.MENSALIDADE.getValor());
		matriculaPeriodoVencimentoVo.setTipoDesconto("VA");
		matriculaPeriodoVencimentoVo.setValidarParcela(true);
		return matriculaPeriodoVencimentoVo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarMatriculaPeriodoVencimento(MatriculaPeriodoVO matriculaPeriodoVo, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, boolean novaMatriculaPeriodoVencimento, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			if (novaMatriculaPeriodoVencimento) {
				matriculaPeriodoVencimentoVo = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().preencherNovaMatriculaPeriodoVencimentoVo(matriculaPeriodoVencimentoVo, matriculaPeriodoVo, usuario);
				getFacadeFactory().getMatriculaPeriodoVencimentoFacade().incluir(matriculaPeriodoVencimentoVo, usuario);
				matriculaPeriodoVo.getMatriculaPeriodoVencimentoVOs().add(matriculaPeriodoVencimentoVo);
			} else {
				matriculaPeriodoVencimentoVo.setValidarParcela(true);
				matriculaPeriodoVencimentoVo.setTipoDesconto("VA");
				getFacadeFactory().getMatriculaPeriodoVencimentoFacade().alterar(matriculaPeriodoVencimentoVo, false, usuario);
				if (matriculaPeriodoVencimentoVo.getSituacao().getValor().equals("GE")) {
					matriculaPeriodoVencimentoVo.setContaReceber(getFacadeFactory().getContaReceberFacade().alterarContaReceberPelaMatriculaPeriodoVencimento(matriculaPeriodoVencimentoVo, configuracaoFinanceiro, usuario));
					matriculaPeriodoVencimentoVo.getContaReceber().setTelaEdicaoContaReceber(true);
					getFacadeFactory().getContaReceberFacade().alterar(matriculaPeriodoVencimentoVo.getContaReceber(), false, configuracaoFinanceiro, usuario);
				}
				for (int i = 0; i < matriculaPeriodoVo.getMatriculaPeriodoVencimentoVOs().size(); i++) {
					if (matriculaPeriodoVencimentoVo.getCodigo().intValue() == matriculaPeriodoVo.getMatriculaPeriodoVencimentoVOs().get(i).getCodigo().intValue()) {
						matriculaPeriodoVo.getMatriculaPeriodoVencimentoVOs().set(i, matriculaPeriodoVencimentoVo);
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT MatriculaPeriodoVencimento.*, " + " MatriculaPeriodo.codigo as \"MatriculaPeriodo.codigo\", " + " MatriculaPeriodo.data as \"MatriculaPeriodo.data\", " + " MatriculaPeriodo.situacao as \"MatriculaPeriodo.situacao\", " + " MatriculaPeriodo.matricula as \"MatriculaPeriodo.matricula\", " + " MatriculaPeriodo.unidadeEnsinoCurso as \"MatriculaPeriodo.unidadeEnsinoCurso\", " + " MatriculaPeriodo.processoMatricula as \"MatriculaPeriodo.processoMatricula\", " + " MatriculaPeriodo.situacaoMatriculaPeriodo as \"MatriculaPeriodo.situacaoMatriculaPeriodo\", " + " MatriculaPeriodo.semestre as \"MatriculaPeriodo.semestre\", " + " MatriculaPeriodo.ano as \"MatriculaPeriodo.ano\",  " + " MatriculaPeriodo.unidadeEnsinoCurso as \"MatriculaPeriodo.unidadeEnsinoCurso\", " + " MatriculaPeriodo.processoMatricula as \"MatriculaPeriodo.processoMatricula\", " + " MatriculaPeriodo.periodoLetivoMatricula as \"MatriculaPeriodo.periodoLetivoMatricula\", "
				+ " MatriculaPeriodo.planoFinanceiroCurso as \"MatriculaPeriodo.planoFinanceiroCurso\", " + " MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso as \"MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso\", ");
		str.append("       PlanoFinanceiroAluno.descontoProgressivo as \"PlanoFinanceiroAluno.descontoProgressivo\", PlanoFinanceiroAluno.codigo as \"PlanoFinanceiroAluno.codigo\",  ");
		str.append("       PlanoFinanceiroAluno.descontoProgressivoPrimeiraParcela as \"PlanoFinanceiroAluno.descontoProgressivoPrimeiraParcela\", ");
		str.append("       PlanoFinanceiroAluno.tipoDescontoMatricula as \"PlanoFinanceiroAluno.tipoDescontoMatricula\", ");
		str.append("       PlanoFinanceiroAluno.percDescontoMatricula as \"PlanoFinanceiroAluno.percDescontoMatricula\", ");
		str.append("       PlanoFinanceiroAluno.valorDescontoMatricula as \"PlanoFinanceiroAluno.valorDescontoMatricula\", ");
		str.append("       PlanoFinanceiroAluno.tipoDescontoParcela as \"PlanoFinanceiroAluno.tipoDescontoParcela\", ");
		str.append("       PlanoFinanceiroAluno.percDescontoParcela as \"PlanoFinanceiroAluno.percDescontoParcela\", ");
		str.append("       PlanoFinanceiroAluno.valorDescontoParcela as \"PlanoFinanceiroAluno.valorDescontoParcela\", ");

		str.append("       PlanoFinanceiroAluno.ordemDescontoAluno as \"PlanoFinanceiroAluno.ordemDescontoAluno\", ");
		str.append("       PlanoFinanceiroAluno.ordemPlanoDesconto as \"PlanoFinanceiroAluno.ordemPlanoDesconto\", ");
		str.append("       PlanoFinanceiroAluno.ordemConvenio as \"PlanoFinanceiroAluno.ordemConvenio\", ");
		str.append("       PlanoFinanceiroAluno.ordemDescontoProgressivo as \"PlanoFinanceiroAluno.ordemDescontoProgressivo\", ");
		str.append("       PlanoFinanceiroAluno.ordemDescontoAlunoValorCheio as \"PlanoFinanceiroAluno.ordemDescontoAlunoValorCheio\", ");
		str.append("       PlanoFinanceiroAluno.ordemPlanoDescontoValorCheio as \"PlanoFinanceiroAluno.ordemPlanoDescontoValorCheio\", ");
		str.append("       PlanoFinanceiroAluno.ordemConvenioValorCheio as \"PlanoFinanceiroAluno.ordemConvenioValorCheio\", ");
		str.append("       PlanoFinanceiroAluno.ordemDescontoProgressivoValorCheio as \"PlanoFinanceiroAluno.ordemDescontoProgressivoValorCheio\", ");

		str.append("       PeriodoLetivo.descricao as \"PeriodoLetivo.descricao\", PeriodoLetivo.periodoLetivo as \"PeriodoLetivo.periodoLetivo\",  ");
		str.append("       Matricula.matricula as \"Matricula.matricula\", Matricula.data as \"Matricula.data\", ");
		str.append("       Matricula.situacao as \"Matricula.situacao\", Matricula.situacaoFinanceira as \"Matricula.situacaoFinanceira\", ");
		str.append("       Matricula.turno as \"Matricula.turno\", ");
		str.append("       Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", ");
		str.append("       Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
		str.append("       UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\" ");
		str.append(" FROM MatriculaPeriodoVencimento ");
		str.append("      INNER JOIN MatriculaPeriodo ON (MatriculaPeriodoVencimento.matriculaPeriodo = matriculaPeriodo.codigo) ");
		str.append("      INNER JOIN Matricula ON (MatriculaPeriodo.matricula = Matricula.matricula) ");
		str.append("      INNER JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append("      INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append("      INNER JOIN PeriodoLetivo ON (MatriculaPeriodo.periodoLetivoMatricula = PeriodoLetivo.codigo) ");
		// TODO (SEI-RN48.1)
		str.append("      INNER JOIN PlanoFinanceiroAluno ON (PlanoFinanceiroAluno.matriculaPeriodo = MatriculaPeriodo.codigo) ");
		// TODO (SEI-RN48.1)
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaTotalRegistroETotalValor() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT count(MatriculaPeriodoVencimento.codigo) as qtde, sum(MatriculaPeriodoVencimento.valor) as valor ");
		str.append(" FROM MatriculaPeriodoVencimento ");
		str.append("      INNER JOIN MatriculaPeriodo ON (MatriculaPeriodoVencimento.matriculaPeriodo = matriculaPeriodo.codigo) ");
		str.append("      INNER JOIN Matricula ON (MatriculaPeriodo.matricula = Matricula.matricula) ");
		str.append("      INNER JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append("      INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append("      INNER JOIN PeriodoLetivo ON (MatriculaPeriodo.periodoLetivoMatricula = PeriodoLetivo.codigo) ");
		str.append("      INNER JOIN PlanoFinanceiroAluno ON (PlanoFinanceiroAluno.matriculaPeriodo = MatriculaPeriodo.codigo) ");
		return str;
	}

	public void montarDadosBasicoMatriculaPeriodo(MatriculaPeriodoVencimentoVO vcto, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		vcto.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		MatriculaPeriodoVO obj = vcto.getMatriculaPeriodoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("MatriculaPeriodo.codigo")));
		obj.setData(dadosSQL.getDate("MatriculaPeriodo.data"));
		obj.setSituacao(dadosSQL.getString("MatriculaPeriodo.situacao"));
		obj.setSituacaoMatriculaPeriodo(dadosSQL.getString("MatriculaPeriodo.situacaoMatriculaPeriodo"));
		obj.setSemestre(dadosSQL.getString("MatriculaPeriodo.semestre"));
		obj.setAno(dadosSQL.getString("MatriculaPeriodo.ano"));
		obj.setMatricula(dadosSQL.getString("MatriculaPeriodo.matricula"));
		obj.setUnidadeEnsinoCurso(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
		obj.setProcessoMatricula(dadosSQL.getInt("MatriculaPeriodo.processoMatricula"));
		obj.getProcessoMatriculaVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.processoMatricula"));
		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("MatriculaPeriodo.planoFinanceiroCurso"));
		obj.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso"));

		obj.setNovoObj(false);
		obj.setNivelMontarDados(NivelMontarDados.BASICO);

		// Periodo Letivo
		obj.getPeridoLetivo().setCodigo(dadosSQL.getInt("MatriculaPeriodo.periodoLetivoMatricula"));
		obj.getPeridoLetivo().setDescricao(dadosSQL.getString("PeriodoLetivo.descricao"));

		// Dados da Matrícula
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("Matricula.matricula"));
		obj.getMatriculaVO().setData(dadosSQL.getDate("Matricula.data"));
		obj.getMatriculaVO().setSituacao(dadosSQL.getString("Matricula.situacao"));
		obj.getMatriculaVO().setSituacaoFinanceira(dadosSQL.getString("Matricula.situacaoFinanceira"));
		obj.getMatriculaVO().getTurno().setCodigo(dadosSQL.getInt("Matricula.turno"));

		// Plano Financeiro Aluno
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setTipoDescontoMatricula(dadosSQL.getString("PlanoFinanceiroAluno.tipoDescontoMatricula"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(dadosSQL.getDouble("PlanoFinanceiroAluno.percDescontoMatricula"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(dadosSQL.getDouble("PlanoFinanceiroAluno.valorDescontoMatricula"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setTipoDescontoParcela(dadosSQL.getString("PlanoFinanceiroAluno.tipoDescontoParcela"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(dadosSQL.getDouble("PlanoFinanceiroAluno.percDescontoParcela"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(dadosSQL.getDouble("PlanoFinanceiroAluno.valorDescontoParcela"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoAluno(new Integer(dadosSQL.getInt("PlanoFinanceiroAluno.ordemDescontoAluno")));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemPlanoDesconto(new Integer(dadosSQL.getInt("PlanoFinanceiroAluno.ordemPlanoDesconto")));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemConvenio(new Integer(dadosSQL.getInt("PlanoFinanceiroAluno.ordemConvenio")));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoProgressivo(new Integer(dadosSQL.getInt("PlanoFinanceiroAluno.ordemDescontoProgressivo")));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("PlanoFinanceiroAluno.ordemDescontoAlunoValorCheio"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("PlanoFinanceiroAluno.ordemPlanoDescontoValorCheio"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemConvenioValorCheio(dadosSQL.getBoolean("PlanoFinanceiroAluno.ordemConvenioValorCheio"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("PlanoFinanceiroAluno.ordemDescontoProgressivoValorCheio"));

		obj.getMatriculaVO().getPlanoFinanceiroAluno().setCodigo(dadosSQL.getInt("PlanoFinanceiroAluno.codigo"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().getDescontoProgressivoPrimeiraParcela().setCodigo(dadosSQL.getInt("PlanoFinanceiroAluno.descontoProgressivoPrimeiraParcela"));
		obj.getMatriculaVO().getPlanoFinanceiroAluno().getDescontoProgressivo().setCodigo(dadosSQL.getInt("PlanoFinanceiroAluno.descontoProgressivo"));
		PlanoFinanceiroAluno.montarDadosDescontoProgressivo(obj.getMatriculaVO().getPlanoFinanceiroAluno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		obj.getMatriculaVO().getPlanoFinanceiroAluno().setItemPlanoFinanceiroAlunoVOs(ItemPlanoFinanceiroAluno.consultarItemPlanoFinanceiroAlunos(obj.getMatriculaVO().getPlanoFinanceiroAluno().getCodigo(), false, usuario));
		Ordenacao.ordenarLista(obj.getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs(), "ordenacao");

		// Dados do Aluno
		obj.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getMatriculaVO().getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getMatriculaVO().getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Curso
		obj.getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getMatriculaVO().getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getMatriculaVO().getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getMatriculaVO().getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
	}

	public List<MatriculaPeriodoVencimentoVO> consultarPorMesReferenciaSituacaoUnidadeAluno(String mesReferencia, String anoReferencia, String situacao, String matriculaAluno, Integer unidadeEnsino, Boolean permitirGerarParcelaPreMatricula, boolean trazerMatriculasComCanceladoFinanceiro, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVencimento.consultar(getIdEntidade(), false, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE MatriculaPeriodo.matricula = ? ");
		if (!permitirGerarParcelaPreMatricula) {
			sql.append(" AND (MatriculaPeriodo.situacaoMatriculaPeriodo <> 'PR' or (MatriculaPeriodo.situacaoMatriculaPeriodo = 'PR' and MatriculaPeriodoVencimento.parcela ilike '%M%'))");
		}
		if (!mesReferencia.equals("")) {
			sql.append(" AND (date_part('MONTH', MatriculaPeriodoVencimento.datavencimento) = '" + mesReferencia + "') ");
		} else {
			// sql.append(" AND (date_part('MONTH', MatriculaPeriodoVencimento.datavencimento) >= '" + Uteis.getMesData(new Date()) + "') ");
		}
		if (!anoReferencia.equals("")) {
			sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datavencimento) = '" + anoReferencia + "') ");
		} else {
			sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datavencimento) >= '" + Uteis.getAno(new Date()) + "') ");
		}
		if (!situacao.equals("")) {
			sql.append(" AND (MatriculaPeriodoVencimento.situacao = '");
			sql.append(situacao);
			sql.append("')");
		}
		if ((unidadeEnsino != null) && (!unidadeEnsino.equals(0))) {
			sql.append(" AND (UnidadeEnsino.codigo = ");
			sql.append(String.valueOf(unidadeEnsino));
			sql.append(")");
		}
		if (!trazerMatriculasComCanceladoFinanceiro) {
			sql.append(" AND (Matricula.canceladoFinanceiro  is null or Matricula.canceladoFinanceiro = false) ");
		}
		sql.append(" AND (Matricula.situacao <> 'CA') AND (Matricula.situacao <> 'TR') AND (Matricula.situacao <> 'JU') AND (Matricula.situacao <> 'CF') AND (Matricula.situacao <> 'TS')");
		sql.append(" AND (Matricula.situacao <> 'ER') AND (Matricula.situacao <> 'IN') AND (Matricula.situacao <> 'DE') AND (Matricula.situacao <> 'AC') ");
		sql.append(" AND matriculaperiodo.codigo = (");
		sql.append(" select mp.codigo from matriculaperiodo mp ");
		sql.append(" where mp.matricula = matricula.matricula and mp.ano = matriculaperiodo.ano ");
		sql.append(" and mp.semestre = matriculaperiodo.semestre  ");
		sql.append(" order by mp.codigo desc limit 1 )");
		sql.append(" ORDER BY MatriculaPeriodo.unidadeEnsinoCurso, MatriculaPeriodo.processoMatricula, dataVencimento");

		// matriculaVO.getCurso().getCodigo(),
		// matriculaVO.getUnidadeEnsino().getCodigo(),
		// matriculaVO.getTurno().getCodigo()

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaAluno);
		List objetos = new ArrayList(0);
		while (resultado.next()) {
			MatriculaPeriodoVencimentoVO novoObj = new MatriculaPeriodoVencimentoVO();
			novoObj = MatriculaPeriodoVencimento.montarDados(resultado, nivelMontarDados, configuracaoFinanceiroVO, usuario);
			montarDadosBasicoMatriculaPeriodo(novoObj, resultado, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public List<MatriculaPeriodoVencimentoVO> consultarPorMesReferenciaSituacaoUnidade(String mesReferencia, String anoReferencia, String situacao, Integer unidadeEnsino, Integer curso, TurmaVO turma, Boolean permitirGerarParcelaPreMatricula, boolean trazerMatriculasComCanceladoFinanceiro, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, Integer limite, Integer geracaoManulaParcela, Boolean utilizarDataCompetencia) throws Exception {
		MatriculaPeriodoVencimento.consultar(getIdEntidade(), false, usuario);
		StringBuffer sql = new StringBuffer(getSQLPadraoConsultaBasica());
		sql.append(" WHERE (matriculaperiodo.financeiromanual is null or matriculaperiodo.financeiromanual = false) ");
		sql.append(" and (MatriculaPeriodoVencimento.tipoOrigemMatriculaPeriodoVencimento in ('").append(TipoOrigemContaReceber.MATERIAL_DIDATICO.name()).append("', '").append(TipoOrigemContaReceber.MENSALIDADE.name()).append("')) ");
		sql.append(" and ((MatriculaPeriodoVencimento.erroGeracaoParcela is null or MatriculaPeriodoVencimento.erroGeracaoParcela = false) or ( ");
		sql.append(" MatriculaPeriodoVencimento.erroGeracaoParcela = true ");
		sql.append(" and (MatriculaPeriodoVencimento.geracaoManualParcela is null or MatriculaPeriodoVencimento.geracaoManualParcela != ").append(geracaoManulaParcela).append(" ))) ");
		if (!mesReferencia.equals("") && !anoReferencia.equals("")) {
			if (utilizarDataCompetencia != null && utilizarDataCompetencia) {
				sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datacompetencia) = ?) AND ");
				sql.append("(date_part('MONTH', MatriculaPeriodoVencimento.datacompetencia) = ?) ");
			} else {
				sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datavencimento) = ?) AND ");
				sql.append("(date_part('MONTH', MatriculaPeriodoVencimento.datavencimento) = ?) ");
			}
		}
		if (!situacao.equals("")) {
			sql.append(" AND (MatriculaPeriodoVencimento.situacao = '");
			sql.append(situacao);
			sql.append("')");
		}
		if (!permitirGerarParcelaPreMatricula) {
			sql.append(" AND ((MatriculaPeriodo.situacaoMatriculaPeriodo <> 'PR')");
			sql.append(" OR (MatriculaPeriodo.situacaoMatriculaPeriodo = 'PR'");
			sql.append(" AND MatriculaPeriodoVencimento.parcela ilike '%M%')");
			sql.append(" )");
		} else {
			sql.append(" AND (MatriculaPeriodo.situacaoMatriculaPeriodo IN ('PR', 'AT', 'CO'))");
		}
		if ((unidadeEnsino != null) && (!unidadeEnsino.equals(0))) {
			if (!Uteis.isAtributoPreenchido(turma) || (Uteis.isAtributoPreenchido(turma) && !turma.getTurmaAgrupada())) {
				sql.append(" AND (UnidadeEnsino.codigo = ");
				sql.append(String.valueOf(unidadeEnsino));
				sql.append(")");

			}
		}
		if (curso != null && curso > 0) {
			sql.append(" AND (matricula.curso = ");
			sql.append(curso);
			sql.append(")");
		}
		
		if (Uteis.isAtributoPreenchido(turma)) {
			if(turma.getTurmaAgrupada()) {				
				if (!turma.getTurmaAgrupadaVOs().isEmpty()) {
					boolean virgula = false;
					sql.append(" AND matriculaPeriodo.turma IN(");
					for (TurmaAgrupadaVO turmaAgrupadaVO : turma.getTurmaAgrupadaVOs()) {

							if (!virgula) {
								sql.append(turmaAgrupadaVO.getTurma().getCodigo());
							} else {
								sql.append(", ").append(turmaAgrupadaVO.getTurma().getCodigo());
							}
							virgula = true;
						
					}
					sql.append(") ");
				}else {
					sql.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
				}
				
			}
			else {
				sql.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
			}			
		}		

		if (!permitirGerarParcelaPreMatricula) {
			sql.append(" AND Matricula.situacao in ('AT', 'CO') ");
		} else {
			sql.append(" AND Matricula.situacao in ('AT', 'PR', 'CO') ");
		}
		if (!trazerMatriculasComCanceladoFinanceiro) {
			sql.append(" AND (Matricula.canceladoFinanceiro is null or Matricula.canceladoFinanceiro = false) ");
		}
		sql.append(" AND matricula.bloqueioPorSolicitacaoLiberacaoMatricula = false");
		sql.append(" AND matriculaperiodo.codigo = (");
		sql.append(" select mp.codigo from matriculaperiodo mp ");
		sql.append(" where mp.matricula = matricula.matricula and mp.ano = matriculaperiodo.ano ");
		sql.append(" and mp.semestre = matriculaperiodo.semestre  ");
		sql.append(" order by mp.codigo desc limit 1 )");
		
		sql.append(" ORDER BY MatriculaPeriodoVencimento.codigo, MatriculaPeriodo.unidadeEnsinoCurso, MatriculaPeriodo.processoMatricula, dataVencimento ");
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite);
		}
		SqlRowSet resultado;
		if (!mesReferencia.equals("") && !anoReferencia.equals("")) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Integer(anoReferencia), new Integer(mesReferencia));
		} else {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		List<MatriculaPeriodoVencimentoVO> objetos = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
		while (resultado.next()) {
			MatriculaPeriodoVencimentoVO novoObj = new MatriculaPeriodoVencimentoVO();
			novoObj = MatriculaPeriodoVencimento.montarDados(resultado, nivelMontarDados, configuracaoFinanceiroVO, usuario);
			montarDadosBasicoMatriculaPeriodo(novoObj, resultado, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarDataVencimentoMatriculaPeriodoVencimentoAposRegerarBoleto(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE matriculaperiodovencimento SET datavencimento = ? WHERE contareceber = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { Uteis.getDataJDBC(contaReceberVO.getDataVencimento()), contaReceberVO.getCodigo() });
	}

	public Integer consultarDiasVariacaoDataVencimentoPeloCodigoContaReceber(Integer codigoContaReceber, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT distinct diasVariacaoDataVencimento FROM matriculaperiodovencimento WHERE contareceber = ? limit 1";
		try {
			return getConexao().getJdbcTemplate().queryForInt(sqlStr, new Object[] { codigoContaReceber });
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * Consulta pelo código da Matrícula Período a matrícula Período Vencimento.
	 * 
	 * 
	 * @param codigoMatriculaPeriodo
	 *            Integer
	 * 
	 * @return List<MatriculaPeriodoVencimentoVO>
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodo(Integer codigoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select mpv.* from matriculaperiodovencimento mpv ");
		sql.append("inner join matriculaperiodo mp on mpv.matriculaperiodo = mp.codigo ");
		sql.append("where mp.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoMatriculaPeriodo });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodoESituacaoParcelaExtra(Integer codigoMatriculaPeriodo, String situacaoPeriodoVencimento, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select matriculaperiodovencimento.* from matriculaperiodovencimento ");
		sql.append("inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sql.append("where matriculaperiodo.codigo = ");
		sql.append(codigoMatriculaPeriodo);
		sql.append(" and matriculaperiodovencimento.parcela ilike 'EX%'");
		sql.append(" and matriculaperiodovencimento.situacao = '");
		sql.append(situacaoPeriodoVencimento);
		sql.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarPorMatriculaPeriodoParcelaExtraSituacao(Integer codigoMatriculaPeriodo, String situacaoPeriodoVencimento, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(matriculaperiodovencimento.codigo) as qtde from matriculaperiodovencimento ");
		sql.append("inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sql.append("where matriculaperiodo.codigo = ");
		sql.append(codigoMatriculaPeriodo);
		sql.append(" and matriculaperiodovencimento.parcela ilike 'EX%'");
		sql.append(" and matriculaperiodovencimento.situacao = '");
		sql.append(situacaoPeriodoVencimento);
		sql.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer qtde = 0;
		if (tabelaResultado.next()) {
			return qtde = tabelaResultado.getInt("qtde");
		}
		return qtde;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatriculaPeriodoVencimento(Integer codigoContaReceber, String situacao, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE matriculaPeriodoVencimento SET situacao = ? WHERE contareceber = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		try {
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, codigoContaReceber });
		} finally {
			sqlStr = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoDataVencimentoPorDataBaseGeracaoTurma(final TurmaVO turma, Date dataBaseGeracaoParcelas, List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase, UsuarioVO usuario) throws Exception {
		try {
			String diaVencimento = "";
			for (MatriculaVO matriculaVO : listaMatriculaComControleGeracaoParcelaDataBase) {
				final StringBuilder sql = new StringBuilder(" select * from ( ");
				sql.append(" select matriculaperiodovencimento.codigo,  ");
				sql.append(" matriculaperiodovencimento.dataVencimento, ");
				sql.append(" matriculaperiodovencimento.matriculaperiodo, ");
				sql.append(" matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento, ");
				sql.append(" condicaopagamentoplanofinanceirocurso.controlaDiaBaseVencimentoParcelaMaterialDidatico,  ");
				sql.append(" cast (substring(matriculaperiodovencimento.parcela, 0, position('/'in matriculaperiodovencimento.parcela)) as int) as parcelanumero ");
				sql.append(" from matriculaperiodovencimento ");
				sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
				sql.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo  = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
				sql.append(" Where matriculaperiodo.turma =  ").append(turma.getCodigo());
				sql.append(" and matriculaperiodovencimento.situacao = 'NG' ");
				sql.append(" and matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO') ");
				sql.append(" and matriculaperiodo.matricula = '").append(matriculaVO.getMatricula()).append("'");
				sql.append(" ) as t");
				sql.append(" order by matriculaperiodo, tipoorigemmatriculaperiodovencimento, parcelanumero ");
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				int nrMensalidade=1;
				while(tabelaResultado.next()) {
					MatriculaPeriodoVencimentoVO mpv = new MatriculaPeriodoVencimentoVO();
					mpv.setCodigo(tabelaResultado.getInt("codigo"));
					mpv.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.valueOf(tabelaResultado.getString("tipoorigemmatriculaperiodovencimento")));
					if(mpv.getTipoOrigemMatriculaPeriodoVencimento().isMensalidade()
							||(mpv.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() && !
									tabelaResultado.getBoolean("controlaDiaBaseVencimentoParcelaMaterialDidatico"))){
						diaVencimento = String.valueOf(Uteis.getDiaMesData(dataBaseGeracaoParcelas));					
					}else{
						diaVencimento = String.valueOf(Uteis.getDiaMesData(tabelaResultado.getDate("dataVencimento")));
					}
					Date novaDataVencimento = getFacadeFactory().getMatriculaPeriodoFacade().montarDadosDataVencimentoDataBaseGeracaoParcelas(nrMensalidade, diaVencimento, dataBaseGeracaoParcelas, new ArrayList<>());
					mpv.setDataVencimento(novaDataVencimento);
					mpv.setDataCompetencia(Uteis.getDataPrimeiroDiaMes(novaDataVencimento));
					alterarDataVencimentoAndDataCompetencia(mpv);
					nrMensalidade++;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoDiaVencimento(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoVOs, Integer novoDia, UsuarioVO usuario) throws Exception {
		for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimento : listaMatriculaPeriodoVencimentoVOs) {
			matriculaPeriodoVencimento.setDataVencimento(Uteis.alterarSomenteDia(novoDia, matriculaPeriodoVencimento.getDataVencimento()));
			matriculaPeriodoVencimento.getContaReceber().setDataVencimento(matriculaPeriodoVencimento.getDataVencimento());
			alterarDataVencimento(matriculaPeriodoVencimento);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataVencimentoAndDataCompetencia(MatriculaPeriodoVencimentoVO obj) throws Exception {
		String sqlStr = "UPDATE matriculaPeriodoVencimento set dataVencimento=?, dataCompetencia=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getDataVencimento(), obj.getDataCompetencia(), obj.getCodigo()});
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataVencimento(MatriculaPeriodoVencimentoVO obj) throws Exception {
		String sqlStr = "UPDATE matriculaPeriodoVencimento set dataVencimento=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getContaReceber().getDataVencimento(), obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodovencimento WHERE matriculaperiodo IN ( ");
		sqlStr.append("SELECT codigo FROM matriculaperiodo WHERE matricula = '" + matricula + "' ) ");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodovencimento WHERE matriculaperiodo = ");
		sqlStr.append(codMatriculaPeriodo);
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		} finally {
			sqlStr = null;
		}
	}

	public void alterarDescontoMatriculaPeriodoVencimentoConformeMapaPendenciaControleConbranca(Integer matriculaPeriodo, Integer descontoProgressivo, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs = consultarPorMatriculaPeriodo(matriculaPeriodo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuarioVO);
		for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVencimentoVOs) {
			if (!matriculaPeriodoVencimentoVO.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA)) {
				matriculaPeriodoVencimentoVO.setTipoDesconto("VA");
				matriculaPeriodoVencimentoVO.setValorDesconto(0.0);
				alterar(matriculaPeriodoVencimentoVO, false, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaContaReceberDataVencimentoSituacaoMatricula(String matricula, String situacao, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaPeriodoVencimento ");
		sqlStr.append(" WHERE contaReceber IN (SELECT codigo FROM contaReceber WHERE matriculaAluno = '").append(matricula).append("' ");
		sqlStr.append(" AND dataVencimento >= '").append(Uteis.getDataJDBC(Uteis.getDateTime(new Date(), 0, 0, 0))).append("' AND situacao = '").append(situacao).append("')");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarExisteMatriculaPeriodoVencimentoPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(" SELECT codigo FROM matriculaPeriodoVencimento mpv ");
		sql.append(" WHERE mpv.matriculaPeriodo = ").append(matriculaPeriodo).append(" AND mpv.situacao = '").append(situacao).append("' ");
		sql.append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (tabelaResultado.next());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoValorDescontosMatriculaPeriodoVencimento(final MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception {
		final String sqlStr = "UPDATE matriculaPeriodoVencimento SET situacao=?, valorDesconto=?, valorDescontoInstituicao=?, valorDescontoConvenio=? WHERE contareceber = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlPS = cnctn.prepareStatement(sqlStr.toString());
					sqlPS.setString(1, obj.getSituacao().getValor());
					sqlPS.setDouble(2, obj.getValorDesconto().doubleValue());
					sqlPS.setDouble(3, obj.getValorDescontoInstituicao().doubleValue());
					sqlPS.setDouble(4, obj.getValorDescontoConvenio().doubleValue());
					sqlPS.setInt(5, obj.getContaReceber().getCodigo());
					return sqlPS;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public MatriculaPeriodoVencimentoVO consultarPorMatriculaPeriodoParcela(Integer codigoMatriculaPeriodo, String parcela, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select matriculaperiodovencimento.* from matriculaperiodovencimento ");
		sql.append("inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sql.append("where matriculaperiodo.codigo = ");
		sql.append(codigoMatriculaPeriodo);
		sql.append(" and matriculaperiodovencimento.parcela = '");
		sql.append(parcela);
		sql.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public Boolean consultarPorMatriculaPeriodoParcelaSituacaoExistente(Integer matriculaPeriodo, String parcela, String situacao, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("select codigo from matriculaPeriodoVencimento ");
		sql.append(" where matriculaPeriodo = ").append(matriculaPeriodo);
		sql.append(" and parcela = '").append(parcela).append("' ");
		sql.append(" and situacao = '").append(situacao).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;

	}

	@Override
	public Map<String, Object> consultarPorMesReferenciaSituacaoUnidadeTotalRegistroTotalValor(String mesReferencia, String anoReferencia, String situacao, Integer unidadeEnsino, Integer curso, TurmaVO turma, Boolean permitirGerarParcelaPreMatricula, Boolean utilizarDataCompetencia) throws Exception {

		StringBuffer sql = new StringBuffer(getSQLPadraoConsultaBasicaTotalRegistroETotalValor());
		sql.append(" WHERE (matriculaperiodo.financeiromanual is null or matriculaperiodo.financeiromanual = false)  ");
		sql.append(" and (MatriculaPeriodoVencimento.tipoOrigemMatriculaPeriodoVencimento in ('").append(TipoOrigemContaReceber.MATERIAL_DIDATICO.name()).append("', '").append(TipoOrigemContaReceber.MENSALIDADE.name()).append("')) ");
		if (!mesReferencia.equals("") && !anoReferencia.equals("")) {
			if (utilizarDataCompetencia != null && utilizarDataCompetencia) {
				sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datacompetencia) = ?) AND ");
				sql.append("(date_part('MONTH', MatriculaPeriodoVencimento.datacompetencia) = ?) ");
			} else {
				sql.append(" AND (date_part('YEAR', MatriculaPeriodoVencimento.datavencimento) = ?) AND ");
				sql.append("(date_part('MONTH', MatriculaPeriodoVencimento.datavencimento) = ?) ");
			}
		}
		if (!situacao.equals("")) {
			sql.append(" AND (MatriculaPeriodoVencimento.situacao = '");
			sql.append(situacao);
			sql.append("')");
		}
		if (!permitirGerarParcelaPreMatricula) {
			sql.append(" AND ((MatriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'CO'))");
			sql.append(" OR (MatriculaPeriodo.situacaoMatriculaPeriodo = 'PR'");
			sql.append(" AND MatriculaPeriodoVencimento.parcela ilike '%M%')");
			sql.append(" )");
		} else {
			sql.append(" AND (MatriculaPeriodo.situacaoMatriculaPeriodo IN ('PR', 'AT', 'CO'))");
		}
		if ((unidadeEnsino != null) && (!unidadeEnsino.equals(0))) {
			if (!Uteis.isAtributoPreenchido(turma) || (Uteis.isAtributoPreenchido(turma) && !turma.getTurmaAgrupada())) {
				sql.append(" AND (UnidadeEnsino.codigo = ");
				sql.append(String.valueOf(unidadeEnsino));
				sql.append(")");

			}
		}
		if (curso != null && curso > 0) {
			sql.append(" AND (matricula.curso = ");
			sql.append(curso);
			sql.append(")");
		}
		
		if (Uteis.isAtributoPreenchido(turma)) {
			if(turma.getTurmaAgrupada()) {				
				if (!turma.getTurmaAgrupadaVOs().isEmpty()) {
					boolean virgula = false;
					sql.append(" AND matriculaPeriodo.turma IN(");
					for (TurmaAgrupadaVO turmaAgrupadaVO : turma.getTurmaAgrupadaVOs()) {

							if (!virgula) {
								sql.append(turmaAgrupadaVO.getTurma().getCodigo());
							} else {
								sql.append(", ").append(turmaAgrupadaVO.getTurma().getCodigo());
							}
							virgula = true;
						
					}
					sql.append(") ");
				}else {
					sql.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
				}
				
			}
			else {
				sql.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
			}			
		}		

		if (!permitirGerarParcelaPreMatricula) {
			sql.append(" AND Matricula.situacao in ('AT', 'CO') ");
		} else {
			sql.append(" AND Matricula.situacao in ('AT', 'PR', 'CO') ");
		}
		sql.append(" AND matricula.bloqueioPorSolicitacaoLiberacaoMatricula = false");

		SqlRowSet resultado;
		if (!mesReferencia.equals("") && !anoReferencia.equals("")) {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Integer(anoReferencia), new Integer(mesReferencia));
		} else {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		Map<String, Object> resultadoConsulta = new HashMap<String, Object>(0);
		if (resultado.next()) {
			resultadoConsulta.put("QTDE", resultado.getInt("qtde"));
			resultadoConsulta.put("VALOR", resultado.getDouble("valor"));
			return resultadoConsulta;
		} else {
			return null;
		}
	}

	@Override
	public void realizarRegistroErroGeracaoContaReceber(final Integer matriculaPeriodoVencimento, final Integer geracaoManualParcela, final String erro) throws Exception {
		final String sql = "UPDATE matriculaperiodovencimento set geracaoManualParcela=?, erroGeracaoParcela = ?, mensagemErroGeracaoParcela=?  WHERE codigo = ? ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, geracaoManualParcela);
				sqlAlterar.setBoolean(2, true);
				sqlAlterar.setString(3, erro);
				sqlAlterar.setInt(4, matriculaPeriodoVencimento);
				return sqlAlterar;
			}
		});
	}

	@Override
	public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoPorGeracaoManualParcela(Integer geracaoManualParcela, String matricula, String aluno, String curso, Date dataVencimento, String parcela, Boolean processadaComErro, Integer limite, Integer offset) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT matriculaperiodovencimento.codigo, ");
		sql.append(" matricula.matricula, pessoa.nome as aluno, curso.nome as curso, dataVencimento, ");
		sql.append(" parcela, valor, mensagemErroGeracaoParcela, tipoOrigemMatriculaPeriodoVencimento ");
		sql.append(" from matriculaperiodovencimento ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sql.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" where geracaoManualParcela = ").append(geracaoManualParcela);
		if (processadaComErro != null && processadaComErro) {
			sql.append(" and  erroGeracaoParcela = true");
		} else {
			sql.append(" and  erroGeracaoParcela = false");
		}
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and  sem_acentos(matricula.matricula) ilike (sem_acentos('").append(matricula).append("%'))");
		}
		if (aluno != null && !aluno.trim().isEmpty()) {
			sql.append(" and  sem_acentos(pessoa.nome) ilike (sem_acentos('").append(aluno).append("%'))");
		}
		if (curso != null && !curso.trim().isEmpty()) {
			sql.append(" and  sem_acentos(curso.nome) ilike (sem_acentos('").append(curso).append("%'))");
		}
		if (parcela != null && !parcela.trim().isEmpty()) {
			sql.append(" and  sem_acentos(matriculaperiodovencimento.parcela) ilike (sem_acentos('").append(parcela).append("%'))");
		}
		if (dataVencimento != null) {
			sql.append(" and  matriculaperiodovencimento.dataVencimento::DATE = '").append(Uteis.getDataJDBC(dataVencimento)).append("'");
		}
		sql.append(" order by matricula.matricula, dataVencimento ");

		sql.append(" limit ").append(limite).append(" offset ").append(offset);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
		MatriculaPeriodoVencimentoVO obj = null;
		while (rs.next()) {
			obj = new MatriculaPeriodoVencimentoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getMatriculaPeriodoVO().setMatricula(rs.getString("matricula"));
			obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(rs.getString("matricula"));
			obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
			obj.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setNome(rs.getString("curso"));
			obj.setDataVencimento(rs.getDate("dataVencimento"));
			obj.setValor(rs.getDouble("valor"));
			obj.setParcela(rs.getString("parcela"));
			obj.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.valueOf(rs.getString("tipoOrigemMatriculaPeriodoVencimento")));
			obj.setMensagemErroGeracaoParcela(rs.getString("mensagemErroGeracaoParcela"));
			obj.setNovoObj(false);
			matriculaPeriodoVencimentoVOs.add(obj);
		}
		return matriculaPeriodoVencimentoVOs;
	}

	@Override
	public Integer consultarMatriculaPeriodoVencimentoPorGeracaoManualParcelaTotalRegistro(Integer geracaoManualParcela, String matricula, String aluno, String curso, Date dataVencimento, String parcela, Boolean processadaComErro) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(matriculaperiodovencimento.codigo) as qtde ");
		sql.append(" from matriculaperiodovencimento ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sql.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" where geracaoManualParcela = ").append(geracaoManualParcela);
		if (processadaComErro != null && processadaComErro) {
			sql.append(" and  erroGeracaoParcela = true");
		} else {
			sql.append(" and  erroGeracaoParcela = false");
		}
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and  sem_acentos(matricula.matricula) ilike (sem_acentos('").append(matricula).append("%'))");
		}
		if (aluno != null && !aluno.trim().isEmpty()) {
			sql.append(" and  sem_acentos(pessoa.nome) ilike (sem_acentos('").append(aluno).append("%'))");
		}
		if (curso != null && !curso.trim().isEmpty()) {
			sql.append(" and  sem_acentos(curso.nome) ilike (sem_acentos('").append(curso).append("%'))");
		}
		if (parcela != null && !parcela.trim().isEmpty()) {
			sql.append(" and  sem_acentos(matriculaperiodovencimento.parcela) ilike (sem_acentos('").append(parcela).append("%'))");
		}
		if (dataVencimento != null) {
			sql.append(" and  matriculaperiodovencimento.dataVencimento::DATE = '").append(Uteis.getDataJDBC(dataVencimento)).append("'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoParcelaReajusteAReceberAptasParaAplicacaoReajuste(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo, Boolean considerarDescontoSemValidadeCalculoIndiceReajuste,  Boolean limit1, UsuarioVO usuarioVO) throws Exception{
		Date dataInicioMes = Uteis.getDate("01/"+mes+"/"+ano);		
		Date dataFimMes = Uteis.getDataUltimoDiaMes(dataInicioMes);
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaperiodovencimento.codigo, matriculaperiodo.matricula, matriculaperiodovencimento.parcela, matriculaperiodovencimento.matriculaPeriodo, ");
		sb.append(" matriculaperiodovencimento.datavencimento, matriculaperiodovencimento.valor, matriculaperiodovencimento.situacao, ");
		sb.append(" contareceber.codigo AS \"contareceber.codigo\", contareceber.matriculaaluno AS \"contareceber.matriculaaluno\", contareceber.parcela AS \"contareceber.parcela\", ");
		sb.append(" contaReceber.matriculaPeriodo AS \"contaReceber.matriculaPeriodo\", contareceber.datavencimento AS \"contareceber.datavencimento\", contareceber.tipoorigem as  \"contareceber.tipoorigem\",  ");
		sb.append(" contareceber.valor AS \"contareceber.valor\", contareceber.valorbasecontareceber AS \"contareceber.valorbasecontareceber\", contaReceber.valorIndiceReajuste AS \"contaReceber.valorIndiceReajuste\", ");
		sb.append(" contareceber.situacao AS \"contareceber.situacao\", contareceber.contacorrente as \"contareceber.contacorrente\", matricula.permiteExecucaoReajustePreco, ");
		sb.append(" matriculaperiodovencimento.tipoOrigemMatriculaPeriodoVencimento, ");
		
		sb.append(" ( select case when count(controleRM.contareceber) > 0 then true else false end ");
		sb.append("  from controleremessacontareceber as controleRM where controleRM.contareceber = matriculaperiodovencimento.contareceber ");
		sb.append(" ) as possuiRemessa ");
		
		if(considerarDescontoSemValidadeCalculoIndiceReajuste) {
			sb.append(" , case when contareceber.codigo is null then matriculaperiodovencimento.valor - "); 
			sb.append(" coalesce((case when matriculaperiodovencimento.valordescontocalculadoquartafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoquartafaixadescontos else ");
			sb.append(" case when matriculaperiodovencimento.valordescontocalculadoterceirafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoterceirafaixadescontos else ");
			sb.append(" case when matriculaperiodovencimento.valordescontocalculadosegundafaixadescontos  > 0 then matriculaperiodovencimento.valordescontocalculadosegundafaixadescontos  else ");
			sb.append(" case when matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos else 0.0 end end end end), 0.0) ");
			sb.append(" else coalesce((select sum(valor) from detalhamentovalorconta where origemdetalhamentoconta = 'CONTA_RECEBER' and codigoorigem = contareceber.codigo ");
			sb.append(" and tipocentroresultadoorigemdetalhe in ('DESCONTO_ALUNO', 'DESCONTO_CONVENIO', 'DESCONTO_INSTITUICAO', 'DESCONTO_MANUAL', 'DESCONTO_RATEIO', 'DESCONTO_CUSTEADO_CONTA_RECEBER') ");
			sb.append(" and utilizado and datalimiteaplicacaodesconto is null), 0.0) end as \"contareceber.valordescontosemvalidade\" ");
		}
		
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		sb.append(" inner join matricula on matricula.matricula = matriculaPeriodo.matricula ");
		sb.append(" left join contareceber on contareceber.codigo = matriculaperiodovencimento.contareceber ");
		sb.append(" where matricula.matricula in(");

		sb.append(" select distinct matricula.matricula ");
		sb.append(" from indiceReajuste ");
		sb.append(" inner join turma on turma.indiceReajuste = indiceReajuste.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		
		sb.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo   ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where tipoorigemmatriculaperiodovencimento = 'MENSALIDADE' and condicaopagamentoplanofinanceirocurso.nrparcelasperiodo > 12 ");
		sb.append(" and turma.indiceReajuste = ").append(indiceReajuste);
		if (matriculaPeriodo > 0) {
			sb.append(" and matriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		}
		sb.append(" and curso.periodicidade = 'IN' ");
		
		sb.append(" and ( ");
		sb.append(" (matricula.situacao = 'CA' and exists (select matriculaperiodovencimento.codigo from matriculaperiodovencimento where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodovencimento.situacao = 'GE')) ");
		sb.append("  or matricula.situacao <> 'CA' ) ");
		
		sb.append(" and (matriculaperiodovencimento.situacao in('NG', 'GE', 'NCR', 'CEM') or matriculaperiodovencimento.contareceber in(");

		// regra serve para trazer contas recebidas em datas anteriores e possui a data de vencimento a mesma do reajuste
		sb.append(" select distinct contarecebernegociacaorecebimento.contareceber from negociacaorecebimento ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" where contarecebernegociacaorecebimento.contareceber = matriculaperiodovencimento.contareceber ");
//		sb.append(" and negociacaorecebimento.data::date < '").append(Uteis.getDataJDBC(dataInicioMes)).append("' ");
		sb.append(")) ");
		sb.append(" and matriculaperiodovencimento.parcela ilike ('%/%') ");
		sb.append(" and (position('/' in matriculaperiodovencimento.parcela) - 1) > 0");
		sb.append(" and isnumeric(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1)) ");
		
		sb.append(" and ");
		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and matricula.considerarParcelasMaterialDidaticoReajustePreco ");
		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO') ");
		sb.append(" then ((substring(matriculaperiodovencimento.parcela, 0, position('/' in matriculaperiodovencimento.parcela))::int) = ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico ").append(") ");
		sb.append(" else ");
		sb.append("(substring(matriculaperiodovencimento.parcela, 0, position('/' in matriculaperiodovencimento.parcela)) = '").append(parcelaInicialReajuste).append("') ");
		sb.append(" end ");
		
		sb.append(" and matriculaperiodovencimento.datavencimento >= '").append(Uteis.getDataJDBC(dataInicioMes)).append("'");
		sb.append(" and matriculaperiodovencimento.datavencimento <= '").append(Uteis.getDataJDBC(dataFimMes)).append("'");
//		sb.append(" and matriculaperiodovencimento.contareceber not in(select distinct controleremessacontareceber.contareceber from controleremessacontareceber  where controleremessacontareceber.contareceber = matriculaperiodovencimento.contareceber and controleremessacontareceber.situacao = 'REMETIDA' ) ");
		sb.append(" and not exists (");
		sb.append(" select distinct indicePeriodo.codigo from indicereajusteperiodomatriculaperiodovencimento indicePeriodoVencimento ");
		sb.append(" inner join indicereajustePeriodo indicePeriodo on indicePeriodo.codigo = indicePeriodoVencimento.indiceReajustePeriodo ");
		sb.append(" where indicePeriodoVencimento.matriculaperiodo = matriculaPeriodo.codigo ");
		sb.append(" and ");
		
		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and matricula.considerarParcelasMaterialDidaticoReajustePreco ");
		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO') ");
		sb.append(" then indicePeriodoVencimento.parcela::int = ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico ");
		sb.append(" else ");
		sb.append(" indicePeriodoVencimento.parcela = '").append(parcelaInicialReajuste).append("' ");
		sb.append(" end ");
		
		sb.append(" and indicePeriodo.mes = '").append(MesAnoEnum.getMesData(dataInicioMes).name()).append("' "); 
		sb.append(" and indicePeriodo.ano = '").append(ano).append("' "); 
		sb.append(" and indicePeriodoVencimento.situacao = 'PROCESSADO' ) ");		
		sb.append(" ) ");
		sb.append(" and matriculaperiodovencimento.parcela ilike ('%/%') ");
		sb.append(" and (position('/' in matriculaperiodovencimento.parcela) - 1) > 0");
		sb.append(" and isnumeric(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1)) ");
		
		sb.append(" and ");
		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and matricula.considerarParcelasMaterialDidaticoReajustePreco  ");
		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO') ");
		sb.append(" then (cast(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) as integer) >= ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico) ");
		sb.append(" else ");
		sb.append(" (cast(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) as integer) >= ").append(parcelaInicialReajuste).append(") ");
		sb.append(" end ");
		
		sb.append(" and tipoorigemmatriculaperiodovencimento = 'MENSALIDADE' ");
		sb.append(" and matriculaperiodovencimento.situacao in('NG', 'GE', 'NCR', 'CEM') ");
		if (matriculaPeriodo > 0) {
			sb.append(" and matriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		}
//		sb.append(" and (matriculaperiodovencimento.contareceber is null or (matriculaperiodovencimento.contareceber is not null and not exists(");
//		sb.append(" select distinct controleremessacontareceber.contareceber from controleremessacontareceber ");
//		sb.append(" where controleremessacontareceber.contareceber = matriculaperiodovencimento.contareceber ");		
//		sb.append("))) ");
		sb.append(" and not exists ( ");
		sb.append(" select distinct indicePeriodo.codigo from indicereajusteperiodomatriculaperiodovencimento indicePeriodoVencimento "); 
		sb.append(" inner join indicereajustePeriodo indicePeriodo on indicePeriodo.codigo = indicePeriodoVencimento.indiceReajustePeriodo "); 
		sb.append(" where indicePeriodoVencimento.matriculaperiodo = matriculaPeriodo.codigo  ");
		sb.append(" and indicePeriodoVencimento.parcela = substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) ");
		sb.append(" and indicePeriodoVencimento.tipoorigem = 'MENSALIDADE' ");
		sb.append(" and indicePeriodo.mes = '").append(MesAnoEnum.getMesData(dataInicioMes).name()).append("' ");
		sb.append(" and indicePeriodo.ano = '").append(ano).append("' ");
		sb.append(" and indicePeriodoVencimento.situacao = 'PROCESSADO') ");
		
		sb.append(" and ");
		sb.append(" case when exists (");
		sb.append("  select cr.codigo from contareceber cr ");
		sb.append("  where cr.codigo = matriculaperiodovencimento.contareceber ");
		sb.append("  ) then contareceber.situacao <> 'RE' else true end ");
		
		sb.append(" order by matriculaperiodo.matricula, matriculaperiodovencimento.parcela  ");
		if (limit1) {
			sb.append(" limit 1");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoVencimentoVO obj = new MatriculaPeriodoVencimentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setMatriculaPeriodo(tabelaResultado.getInt("matriculaPeriodo"));
			obj.getMatriculaPeriodoVO().setCodigo(tabelaResultado.getInt("matriculaPeriodo"));
			obj.getMatriculaPeriodoVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoVO().getMatriculaVO().setPermiteExecucaoReajustePreco(tabelaResultado.getBoolean("permiteExecucaoReajustePreco"));
			obj.setParcela(tabelaResultado.getString("parcela"));
			obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
			obj.setValor(tabelaResultado.getDouble("valor"));
			obj.setSituacao(SituacaoVencimentoMatriculaPeriodo.getEnum(tabelaResultado.getString("situacao")));
			obj.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.valueOf(tabelaResultado.getString("tipoOrigemMatriculaPeriodoVencimento")));

			// DADOS CONTA A RECEBER
			obj.getContaReceber().setCodigo(tabelaResultado.getInt("contaReceber.codigo"));
			obj.getContaReceber().getMatriculaAluno().setMatricula(tabelaResultado.getString("contaReceber.matriculaAluno"));
			obj.getContaReceber().setParcela(tabelaResultado.getString("contaReceber.parcela"));
			obj.getContaReceber().setMatriculaPeriodo(tabelaResultado.getInt("contaReceber.matriculaPeriodo"));
			obj.getContaReceber().setDataVencimento(tabelaResultado.getDate("contaReceber.dataVencimento"));
			obj.getContaReceber().setValor(tabelaResultado.getDouble("contaReceber.valor"));
			obj.getContaReceber().setValorBaseContaReceber(tabelaResultado.getDouble("contaReceber.valorBaseContaReceber"));
			obj.getContaReceber().setValorIndiceReajuste(tabelaResultado.getBigDecimal("contaReceber.valorIndiceReajuste"));
			obj.getContaReceber().setSituacao(tabelaResultado.getString("contaReceber.situacao"));
			obj.getContaReceber().getContaCorrenteVO().setCodigo((tabelaResultado.getInt("contareceber.contacorrente")));
			obj.getContaReceber().setTipoOrigem(tabelaResultado.getString("contareceber.tipoorigem"));
			obj.getContaReceber().setPossuiRemessa(tabelaResultado.getBoolean("possuiRemessa"));
			if(considerarDescontoSemValidadeCalculoIndiceReajuste) {
				obj.getContaReceber().setValorDescontoSemValidade(tabelaResultado.getDouble("contareceber.valordescontosemvalidade"));
			}			
			listaMatriculaPeriodoVencimentoVOs.add(obj);
		}

		return listaMatriculaPeriodoVencimentoVOs;
	}

//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoParcelaReajusteARecebidaAptasParaAplicacaoReajuste(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo, Boolean considerarDescontoSemValidadeCalculoIndiceReajuste, Boolean limit1, UsuarioVO usuarioVO)  throws Exception{		
//		Date dataInicioMes = Uteis.getDate("01/"+mes+"/"+ano);		
//		Date dataFimMes = Uteis.getDataUltimoDiaMes(dataInicioMes);
//		StringBuilder sb = new StringBuilder();
//		sb.append(" select matriculaperiodovencimento.codigo, matriculaperiodo.matricula, matriculaperiodovencimento.parcela, matriculaperiodovencimento.matriculaPeriodo, ");
//		sb.append(" matriculaperiodovencimento.datavencimento, matriculaperiodovencimento.valor, matriculaperiodovencimento.situacao, ");
//		sb.append(" contareceber.codigo AS \"contareceber.codigo\", contareceber.matriculaaluno AS \"contareceber.matriculaaluno\", contareceber.parcela AS \"contareceber.parcela\", ");
//		sb.append(" contaReceber.matriculaPeriodo AS \"contaReceber.matriculaPeriodo\", contareceber.datavencimento AS \"contareceber.datavencimento\", contareceber.tipoorigem as \"contareceber.tipoorigem\",  ");
//		sb.append(" contareceber.valor AS \"contareceber.valor\", contareceber.valorbasecontareceber AS \"contareceber.valorbasecontareceber\", contaReceber.valorIndiceReajuste AS \"contaReceber.valorIndiceReajuste\", ");
//		sb.append(" contareceber.situacao AS \"contareceber.situacao\", ");
//		sb.append(" contareceber.contacorrente AS \"contareceber.contacorrente\", matriculaperiodovencimento.tipoOrigemMatriculaPeriodoVencimento, ");
//		
//		sb.append(" ( select case when count(controleRM.contareceber) > 0 then true else false end   ");
//		sb.append(" from controleremessacontareceber as controleRM where controleRM.contareceber = matriculaperiodovencimento.contareceber ");
//		sb.append(") as possuiRemessa ");
//		
//		if(considerarDescontoSemValidadeCalculoIndiceReajuste) {
//			sb.append(" , case when contareceber.codigo is null then matriculaperiodovencimento.valor - "); 
//			sb.append(" coalesce((case when matriculaperiodovencimento.valordescontocalculadoquartafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoquartafaixadescontos else ");
//			sb.append(" case when matriculaperiodovencimento.valordescontocalculadoterceirafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoterceirafaixadescontos else ");
//			sb.append(" case when matriculaperiodovencimento.valordescontocalculadosegundafaixadescontos  > 0 then matriculaperiodovencimento.valordescontocalculadosegundafaixadescontos  else ");
//			sb.append(" case when matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos > 0 then matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos else 0.0 end end end end), 0.0) ");
//			sb.append(" else coalesce((select sum(valor) from detalhamentovalorconta where origemdetalhamentoconta = 'CONTA_RECEBER' and codigoorigem = contareceber.codigo ");
//			sb.append(" and tipocentroresultadoorigemdetalhe in ('DESCONTO_ALUNO', 'DESCONTO_CONVENIO', 'DESCONTO_INSTITUICAO', 'DESCONTO_MANUAL', 'DESCONTO_RATEIO', 'DESCONTO_CUSTEADO_CONTA_RECEBER') ");
//			sb.append(" and utilizado and datalimiteaplicacaodesconto is null), 0.0) end as \"contareceber.valordescontosemvalidade\" ");
//		}
//		
//		sb.append(" from matriculaperiodovencimento ");
//		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
//		sb.append(" inner join matricula as m on  m.matricula = matriculaperiodo.matricula ");
//		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
//		sb.append(" left join contareceber on contareceber.codigo = matriculaperiodovencimento.contareceber ");
//		sb.append(" left join controleremessacontareceber on controleremessacontareceber.contareceber = contareceber.codigo ");
//		sb.append(" where matriculaperiodo.matricula in(");
//
//		sb.append(" select distinct matricula.matricula ");
//		sb.append(" from indiceReajuste ");
//		sb.append(" inner join turma on turma.indiceReajuste = indiceReajuste.codigo ");
//		sb.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
//		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
//		sb.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo   ");
//		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
//		sb.append(" inner join curso on curso.codigo = matricula.curso ");
//		sb.append(" where tipoorigemmatriculaperiodovencimento = 'MENSALIDADE' and condicaopagamentoplanofinanceirocurso.nrparcelasperiodo > 12 ");
//		sb.append(" and matricula.matricula = m.matricula ");
//		sb.append(" and turma.indiceReajuste = ").append(indiceReajuste);
//		if (matriculaPeriodo > 0) {
//			sb.append(" and matriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
//		}
//		
//		sb.append(" and curso.periodicidade = 'IN' ");
//		
//		sb.append(" and ( ");
//		sb.append(" (matricula.situacao = 'CA' and exists (select matriculaperiodovencimento.codigo from  matriculaperiodovencimento where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodovencimento.situacao = 'GE')) ");
//		sb.append("  or matricula.situacao <> 'CA' ) ");
//		
//		sb.append(" and (matriculaperiodovencimento.situacao = ('GP') or (matriculaperiodovencimento.contareceber is not null and exists(");
//
//		// regra serve para trazer contas recebidas em datas anteriores e possui a data de vencimento a mesma do reajuste
//		sb.append(" select distinct contarecebernegociacaorecebimento.contareceber from negociacaorecebimento ");
//		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
//		sb.append(" where contarecebernegociacaorecebimento.contareceber = matriculaperiodovencimento.contareceber ");
//		sb.append(" and negociacaorecebimento.data::date >= '").append(Uteis.getDataJDBC(dataInicioMes)).append("')) ");
//
//		sb.append(" or matriculaperiodovencimento.situacao in('NG', 'GE', 'NCR', 'CEM')   ");
//		//sb.append(" and exists (select distinct controleremessacontareceber.contareceber from controleremessacontareceber where controleremessacontareceber.contareceber = matriculaperiodovencimento.contareceber and controleremessacontareceber.situacao != 'REMETIDA') ");
//		sb.append(") ");
//		sb.append(" and matriculaperiodovencimento.datavencimento >= '").append(Uteis.getDataJDBC(dataInicioMes)).append("'");
//		sb.append(" and matriculaperiodovencimento.datavencimento <= '").append(Uteis.getDataJDBC(dataFimMes)).append("'");
//		sb.append(" and matriculaperiodovencimento.parcela ilike ('%/%')             ");
//		sb.append(" and (position('/' in matriculaperiodovencimento.parcela) -1) > 0 ");
//		sb.append(" and isnumeric(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1)) ");
//		
//		sb.append(" and ");
//		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and matricula.considerarParcelasMaterialDidaticoReajustePreco   ");
//		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO') ");
//		sb.append(" then ((substring(matriculaperiodovencimento.parcela, 0, position('/' in matriculaperiodovencimento.parcela))::int) = ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico ").append(") ");
//		sb.append(" else ");
//		sb.append(" (substring(matriculaperiodovencimento.parcela, 0, position('/' in matriculaperiodovencimento.parcela)) = '").append(parcelaInicialReajuste).append("') ");
//		sb.append(" end ");
//
//		sb.append(" and not exists (");
//		sb.append(" select distinct indicePeriodo.codigo from indicereajusteperiodomatriculaperiodovencimento indicePeriodoVencimento ");
//		sb.append(" inner join indicereajustePeriodo indicePeriodo on indicePeriodo.codigo = indicePeriodoVencimento.indiceReajustePeriodo ");
//		sb.append(" where indicePeriodoVencimento.matriculaperiodo = matriculaPeriodo.codigo ");
//		
//		sb.append(" and ");
//		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and matricula.considerarParcelasMaterialDidaticoReajustePreco ");
//		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO') ");
//		sb.append(" then indicePeriodoVencimento.parcela::int = ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico ");
//		sb.append(" else ");
//		sb.append(" indicePeriodoVencimento.parcela = '").append(parcelaInicialReajuste).append("' ");
//		sb.append(" end ");
//		
//		
//		sb.append("  and indicePeriodo.mes = '").append(MesAnoEnum.getMesData(dataInicioMes).name()).append("' ");
//		sb.append("  and indicePeriodo.ano = '").append(ano).append("' ");
////		sb.append("  and indicePeriodo.situacaoExecucao = 'PROCESSADO') ");
//		sb.append(" and indicePeriodoVencimento.situacao = 'PROCESSADO') ");
//		sb.append(") ");
//		sb.append(" and matriculaperiodovencimento.parcela ilike ('%/%')             ");
//		sb.append(" and (position('/' in matriculaperiodovencimento.parcela) -1) > 0 ");
//		sb.append(" and isnumeric(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1)) ");
//		sb.append(" and ");
//		sb.append(" case when condicaopagamentoplanofinanceirocurso.considerarParcelasMaterialDidaticoReajustePreco and m.considerarParcelasMaterialDidaticoReajustePreco  ");
//		sb.append(" and exists (select mpv.codigo from matriculaPeriodoVencimento mpv where mpv.matriculaPeriodo = matriculaperiodo.codigo and mpv.tipoorigemmatriculaperiodovencimento = 'MATERIAL_DIDATICO')  ");
//		sb.append(" then (cast(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) as integer) > ").append(parcelaInicialReajuste).append(" - condicaopagamentoplanofinanceirocurso.quantidadeparcelasmaterialdidatico) ");
//		sb.append(" else ");
//		sb.append("  (cast(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) as integer) > ").append(parcelaInicialReajuste).append(") ");
//		sb.append(" end ");
//		
//		sb.append(" and tipoorigemmatriculaperiodovencimento = 'MENSALIDADE' ");
//		
//		sb.append(" and matriculaperiodovencimento.situacao in('NG', 'GE', 'NCR', 'CEM') ");
//		if (matriculaPeriodo > 0) {
//			sb.append(" and matriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
//		}
////		sb.append(" and (matriculaperiodovencimento.contareceber is null or (matriculaperiodovencimento.contareceber is not null and not exists(");
////		sb.append(" select distinct controleremessacontareceber.contareceber from controleremessacontareceber ");
////		sb.append(" where controleremessacontareceber.contareceber = matriculaperiodovencimento.contareceber ");
////		sb.append(" and controleremessacontareceber.situacao = 'REMETIDA' ))) ");
//		
//		sb.append("  and not exists ( ");
//		sb.append("  select distinct indicePeriodo.codigo from indicereajusteperiodomatriculaperiodovencimento indicePeriodoVencimento "); 
//		sb.append("  inner join indicereajustePeriodo indicePeriodo on indicePeriodo.codigo = indicePeriodoVencimento.indiceReajustePeriodo "); 
//		sb.append("  where indicePeriodoVencimento.matriculaperiodo = matriculaPeriodo.codigo  ");
//		sb.append("  and indicePeriodoVencimento.parcela = substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) ");
//		sb.append("  and indicePeriodoVencimento.tipoorigem = 'MENSALIDADE' ");
//		sb.append("  and indicePeriodo.mes = '").append(MesAnoEnum.getMesData(dataInicioMes).name()).append("' ");
//		sb.append("  and indicePeriodo.ano = '").append(ano).append("' ");
//		sb.append("  and indicePeriodoVencimento.situacao = 'PROCESSADO' ) ");
//		sb.append("  and matriculaperiodovencimento.datavencimento >= '").append(Uteis.getDataJDBC(dataInicioMes)).append("'");
//		
//		sb.append(" order by matriculaperiodo.matricula, matriculaperiodovencimento.parcela  ");
//		if (limit1) {
//			sb.append(" limit 1");
//		}
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
//		List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//		while (tabelaResultado.next()) {
//			MatriculaPeriodoVencimentoVO obj = new MatriculaPeriodoVencimentoVO();
//			obj.setCodigo(tabelaResultado.getInt("codigo"));
//			obj.setMatriculaPeriodo(tabelaResultado.getInt("matriculaPeriodo"));
//			obj.getMatriculaPeriodoVO().setCodigo(tabelaResultado.getInt("matriculaPeriodo"));
//			obj.getMatriculaPeriodoVO().setMatricula(tabelaResultado.getString("matricula"));
//			obj.setParcela(tabelaResultado.getString("parcela"));
//			obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
//			obj.setValor(tabelaResultado.getDouble("valor"));
//			obj.setSituacao(SituacaoVencimentoMatriculaPeriodo.getEnum(tabelaResultado.getString("situacao")));
//			obj.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.valueOf(tabelaResultado.getString("tipoOrigemMatriculaPeriodoVencimento")));
//
//			// DADOS CONTA A RECEBER
//			obj.getContaReceber().setCodigo(tabelaResultado.getInt("contaReceber.codigo"));
//			obj.getContaReceber().getMatriculaAluno().setMatricula(tabelaResultado.getString("contaReceber.matriculaAluno"));
//			obj.getContaReceber().setParcela(tabelaResultado.getString("contaReceber.parcela"));
//			obj.getContaReceber().setMatriculaPeriodo(tabelaResultado.getInt("contaReceber.matriculaPeriodo"));
//			obj.getContaReceber().setDataVencimento(tabelaResultado.getDate("contaReceber.dataVencimento"));
//			obj.getContaReceber().setValor(tabelaResultado.getDouble("contaReceber.valor"));
//			obj.getContaReceber().setValorBaseContaReceber(tabelaResultado.getDouble("contaReceber.valorBaseContaReceber"));
//			obj.getContaReceber().setValorIndiceReajuste(tabelaResultado.getBigDecimal("contaReceber.valorIndiceReajuste"));
//			obj.getContaReceber().setSituacao(tabelaResultado.getString("contaReceber.situacao"));
//			obj.getContaReceber().setTipoOrigem(tabelaResultado.getString("contareceber.tipoorigem"));
//			obj.getContaReceber().getContaCorrenteVO().setCodigo(tabelaResultado.getInt("contaReceber.contacorrente"));
//			obj.getContaReceber().setPossuiRemessa(tabelaResultado.getBoolean("possuiRemessa"));
//			if(considerarDescontoSemValidadeCalculoIndiceReajuste) {
//				obj.getContaReceber().setValorDescontoSemValidade(tabelaResultado.getDouble("contareceber.valordescontosemvalidade"));
//			}
//			listaMatriculaPeriodoVencimentoVOs.add(obj);
//		}
//
//		return listaMatriculaPeriodoVencimentoVOs;
//	}

	@Override
	public void realizarGeracaoDescricaoDescontoMatriculaPeriodoVencimento(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs()) {
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber().getCodigo())) {
				if (matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MENSALIDADE) && !matriculaPeriodoVencimentoVO.getParcela().contains("EX")) {
					realizarGeracaoDescricaoParcelaMensalidade(matriculaPeriodoVencimentoVO, matriculaVO, matriculaPeriodoVO, ordemDesconto, configuracaoFinanceiro);
				} else if (matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MENSALIDADE) && matriculaPeriodoVencimentoVO.getParcela().contains("EX")) {
					realizarGeracaoDescricaoParcelaExtra(matriculaPeriodoVencimentoVO, matriculaVO, matriculaPeriodoVO, ordemDesconto, configuracaoFinanceiro);
				} else if (matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO)) {
					realizarGeracaoDescricaoParcelaMaterialDidatico(matriculaPeriodoVencimentoVO, matriculaVO, matriculaPeriodoVO, ordemDesconto, configuracaoFinanceiro);
				} else if (matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATRICULA)) {
					realizarGeracaoDescricaoParcelaMatricula(matriculaPeriodoVencimentoVO, matriculaVO, matriculaPeriodoVO, ordemDesconto, configuracaoFinanceiro, usuario);
				}
			} else if (Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber().getCodigo())) {

				if(!Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber().getListaDescontosAplicavesContaReceber())) {
					String situacaoCr = matriculaPeriodoVencimentoVO.getContaReceber().getSituacao();
					matriculaPeriodoVencimentoVO.getContaReceber().setSituacao("AR");
					ContaReceber.montarListaDescontosAplicaveisContaReceber(matriculaPeriodoVencimentoVO.getContaReceber(), Uteis.obterDataAntiga(matriculaPeriodoVencimentoVO.getContaReceber().getDataVencimento(), 360), configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, usuario, 0);
					matriculaPeriodoVencimentoVO.getContaReceber().setSituacao(situacaoCr);
				}				
				matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getContaReceber().getDescricaoDescontos());
			}
			preencherDescricaoParcelaPorConfiguracaoFinanceira(configuracaoFinanceiro, matriculaPeriodoVencimentoVO);
		}
	}

	private void preencherDescricaoParcelaPorConfiguracaoFinanceira(ConfiguracaoFinanceiroVO configuracaoFinanceiro, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO) {
		matriculaPeriodoVencimentoVO.setDescricaoParcela(matriculaPeriodoVencimentoVO.getParcela() +" - "+matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().getDescricao());
		if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMatricula()
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMatriculaApresentarAluno())
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMatriculaApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getSiglaParcelaMatriculaApresentarAluno()+" - "+configuracaoFinanceiro.getNomeParcelaMatriculaApresentarAluno());
		}else if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMatricula()
				&& !Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMatriculaApresentarAluno())
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMatriculaApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getNomeParcelaMatriculaApresentarAluno());
		}else if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMatricula()
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMatriculaApresentarAluno())
				&& !Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMatriculaApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getSiglaParcelaMatriculaApresentarAluno());
		}else if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico()
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMaterialDidaticoApresentarAluno())
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getSiglaParcelaMaterialDidaticoApresentarAluno()+" - "+configuracaoFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno());					
		}else if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico()
				&& !Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMaterialDidaticoApresentarAluno())
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno());					
		}else if(configuracaoFinanceiro != null
				&& matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico()
				&& Uteis.isAtributoPreenchido(configuracaoFinanceiro.getSiglaParcelaMaterialDidaticoApresentarAluno())
				&& !Uteis.isAtributoPreenchido(configuracaoFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno())
				) {
			matriculaPeriodoVencimentoVO.setDescricaoParcela(configuracaoFinanceiro.getSiglaParcelaMaterialDidaticoApresentarAluno());					
		}
		
	}

	@Override
	public void realizarGeracaoDescricaoParcelaMensalidade(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
		if (matriculaPeriodoVencimentoVO.isUsaValorPrimeiraParcela() && matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MENSALIDADE)) {
			matriculaPeriodoVencimentoVO.setDescricaoPagamento("");
			boolean valoresPreenchidos = Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber());
			if(!valoresPreenchidos) {
				matriculaPeriodoVencimentoVO.getContaReceber().setValorBaseContaReceber(matriculaPeriodoVencimentoVO.getValor());
				matriculaPeriodoVencimentoVO.getContaReceber().setValor(matriculaPeriodoVencimentoVO.getValor());
				matriculaPeriodoVencimentoVO.getContaReceber().setValorCusteadoContaReceber(0.0);
				matriculaPeriodoVencimentoVO.getContaReceber().getPlanoDescontoContaReceberVOs().clear();
				matriculaPeriodoVencimentoVO.getContaReceber().getListaDescontosAplicavesContaReceber().clear();
				valoresPreenchidos =  true;
			}
			matriculaPeriodoVencimentoVO.setDescricaoDesconto("Desconto de R$0,00 (0) [Descontos=] - Valor a Pagar: "+Uteis.arrendondarForcando2CadasDecimaisStrComSepador(matriculaPeriodoVencimentoVO.getValor(),",")+"");			
		}else if (!matriculaPeriodoVencimentoVO.isUsaValorPrimeiraParcela() && matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MENSALIDADE)) {
			matriculaPeriodoVencimentoVO.setDescricaoPagamento("");
			Integer nrParcela = Integer.valueOf(matriculaPeriodoVencimentoVO.getParcela().substring(0, matriculaPeriodoVencimentoVO.getParcela().indexOf("/")));

			List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
			for (PlanoDescontoVO planoDescontoVO : matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs()) {
				if (planoDescontoVO.getAplicarDescontoApartirParcela() <= 1 || (planoDescontoVO.getAplicarDescontoApartirParcela() > 1 && planoDescontoVO.getAplicarDescontoApartirParcela() <= nrParcela)) {
					planoDescontoVOs.add(planoDescontoVO);
				}
			}
			List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosMensalidade = (getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVencimentoVO.getValor(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), matriculaPeriodoVencimentoVO.getDataVencimento(), matriculaPeriodoVencimentoVO.getDataVencimento(), ordemDesconto, matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo(), planoDescontoVOs, matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
			/**
			 * Criado por Rodrigo Wind 30/04/15 Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
			 */
			if (!listaDescontosMensalidade.isEmpty() && matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && !listaDescontosMensalidade.get(listaDescontosMensalidade.size() - 1).getReferentePlanoFinanceiroAposVcto() && listaDescontosMensalidade.get(listaDescontosMensalidade.size() - 1).getValorDescontoAluno() > 0) {
				// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir
				// que gere
				// a condição de pagamento que será aplicada para o aluno após o vencimento do
				// boleto
				// Esta descrição é útil quando existem descontos que vencem na data de
				// vencimento. Ou seja,
				// nestes casos é importante que o SEI imprima os valores a serem adotados após
				// o pagamento
				// do boleto.
				Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(matriculaPeriodoVencimentoVO.getDataVencimento(), Calendar.DAY_OF_MONTH, 1);
				List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, matriculaPeriodoVencimentoVO.getValor(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), matriculaPeriodoVencimentoVO.getDataVencimento(), matriculaPeriodoVencimentoVO.getDataVencimento(), ordemDesconto, null, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto, matriculaVO.getMatricula(),
						matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo());
				if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
					if (!listaDescontosMensalidade.isEmpty() && listaDescontosMensalidade.get(listaDescontosMensalidade.size() - 1).getDiaNrAntesVencimento().equals(0)) {
						listaDescontosMensalidade.get(listaDescontosMensalidade.size() - 1).setReferentePlanoFinanceiroAteVencimento(true);
					}
					PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
					planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
					listaDescontosMensalidade.add(planoAposVcto);
				}
			}
			boolean valoresPreenchidos = Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber());
			for (PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO : listaDescontosMensalidade) {
				if(!valoresPreenchidos) {
					matriculaPeriodoVencimentoVO.getContaReceber().setValorBaseContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase());
					matriculaPeriodoVencimentoVO.getContaReceber().setValor(Uteis.arrendondarForcando2CadasDecimais(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase() - planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber()));
					matriculaPeriodoVencimentoVO.getContaReceber().setValorCusteadoContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber());
					matriculaPeriodoVencimentoVO.getContaReceber().setListaDescontosAplicavesContaReceber(listaDescontosMensalidade);
					valoresPreenchidos =  true;
				}
				if (matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimento() == 0 && planoFinanceiroAlunoDescricaoDescontosVO.getReferentePlanoFinanceiroAposVcto()) {
					planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(0.0);
				}
				if (!(planoFinanceiroAlunoDescricaoDescontosVO.executarCalculoValorTotalDesconto().equals(0.0) && planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimentoDescontoComecaASerAplicado() != 0)) {					
					if (!matriculaPeriodoVencimentoVO.getDescricaoDesconto().isEmpty()) {
						matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + "</br>");
					}
					matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + planoFinanceiroAlunoDescricaoDescontosVO.getDescricaoDetalhadaComDataLimitesEDescontosValidos());
				}
			}
		}
	}
	
	public void realizarGeracaoDescricaoParcelaExtra(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {		
		matriculaPeriodoVencimentoVO.setDescricaoPagamento("");
		matriculaPeriodoVencimentoVO.getContaReceber().setValorBaseContaReceber(matriculaPeriodoVencimentoVO.getValor());
		matriculaPeriodoVencimentoVO.getContaReceber().setValor(matriculaPeriodoVencimentoVO.getValor());
		matriculaPeriodoVencimentoVO.getContaReceber().setValorCusteadoContaReceber(0.0);
		matriculaPeriodoVencimentoVO.getContaReceber().getListaDescontosAplicavesContaReceber().clear();;
		matriculaPeriodoVencimentoVO.setDescricaoDesconto("Desconto de R$0,00 (0) [Descontos=] - Valor a Pagar: "+Uteis.getDoubleFormatado(matriculaPeriodoVencimentoVO.getValor()));
		
	}

	@Override
	public void realizarGeracaoDescricaoParcelaMaterialDidatico(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
		if (matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO)) {
			List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontos = null;
			Date dataVencimentoParcela = matriculaPeriodoVencimentoVO.getDataVencimento();
			if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontosParcelasNoMaterialDidatico()) {
				List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
				List<ConvenioVO> convenioVOs = new ArrayList<ConvenioVO>();
				DescontoProgressivoVO descontoProgressivo = new DescontoProgressivoVO();
				String tipoDescontoParcela = "";
				Double percDescontoParcela = 0.0;
				Double valorDescontoParcela = 0.0;

				if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoInstitucional()) {
					List<PlanoDescontoVO> planoDescontoBaseVOs = matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs();
					Ordenacao.ordenarLista(planoDescontoBaseVOs, "aplicarDescontoApartirParcela");
					for (PlanoDescontoVO planoDescontoVO : planoDescontoBaseVOs) {
						if (planoDescontoVO.getAplicarDescontoApartirParcela() <= 1 ||
								(planoDescontoVO.getAplicarDescontoApartirParcela() > 1 && planoDescontoVO.getAplicarDescontoApartirParcela() <= 1)) {
							planoDescontoVOs.add(planoDescontoVO);
						}
					}
				}

				if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoConvenio()) {
					for (ConvenioVO convenioVO : matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs()) {
						if (convenioVO.getParceiro().isCusteaParcelasMaterialDidatico()) {
							convenioVOs.add(convenioVO);
						}
					}
				}

				if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoProgressivo()) {
					descontoProgressivo = matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo();
				}

				if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoAluno()) {
					tipoDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela();
					percDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela();
					valorDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela();
				}

				listaDescontos = (getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), tipoDescontoParcela, percDescontoParcela, valorDescontoParcela, matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto, descontoProgressivo, planoDescontoVOs, convenioVOs, 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));

				if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontosDesconsiderandosVencimento()) {
					for (PlanoFinanceiroAlunoDescricaoDescontosVO pfadd : listaDescontos) {
						planoDescontoVOs.clear();
						descontoProgressivo = new DescontoProgressivoVO();
						tipoDescontoParcela = TipoDescontoAluno.VALOR.getValor();
						percDescontoParcela = 0.0;
						valorDescontoParcela = pfadd.getValorDescontoAluno() + pfadd.getValorDescontoProgressivo() + pfadd.getValorDescontoInstituicao();
						listaDescontos = (getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), tipoDescontoParcela, percDescontoParcela, valorDescontoParcela, matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto, descontoProgressivo, planoDescontoVOs, convenioVOs, 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
						break;
					}
				}

				/**
				 * Criado por Rodrigo Wind 30/04/15 Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
				 */
				if (!listaDescontos.isEmpty()
						&& matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela()
						&& !listaDescontos.get(listaDescontos.size() - 1).getReferentePlanoFinanceiroAposVcto()
						&& listaDescontos.get(listaDescontos.size() - 1).getValorDescontoAluno() > 0) {
					// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir que gere
					// a condição de pagamento que será aplicada para o aluno após o vencimento do boleto
					// Esta descrição é útil quando existem descontos que vencem na data de vencimento. Ou seja,
					// nestes casos é importante que o SEI imprima os valores a serem adotados após o pagamento
					// do boleto.
					Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(dataVencimentoParcela, Calendar.DAY_OF_MONTH, 1);
					List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(),
							matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto,
							null, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto,
							matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo());
					if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
						if (!listaDescontos.isEmpty() && listaDescontos.get(listaDescontos.size() - 1).getDiaNrAntesVencimento().equals(0)) {
							listaDescontos.get(listaDescontos.size() - 1).setReferentePlanoFinanceiroAteVencimento(true);
						}
						PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
						planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
						listaDescontos.add(planoAposVcto);
					}
				}
				
			} else {
				listaDescontos = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
				PlanoFinanceiroAlunoDescricaoDescontosVO pfadd = new PlanoFinanceiroAlunoDescricaoDescontosVO();
				pfadd.setDataLimiteAplicacaoDesconto(dataVencimentoParcela);
				pfadd.setValorBase(matriculaPeriodoVO.getValorMaterialDidaticoCheio());
				pfadd.setValorBaseComDescontosJaCalculadosAplicados(matriculaPeriodoVO.getValorMaterialDidaticoCheio());
				pfadd.setValorParaPagamentoDentroDataLimiteDesconto(matriculaPeriodoVO.getValorMaterialDidaticoCheio());
				pfadd.setTipoOrigemDesconto(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO);
				listaDescontos.add(pfadd);
			}
			
			
			boolean valoresPreenchidos = Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber());
			for (PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO : listaDescontos) {
				if(!valoresPreenchidos) {
					matriculaPeriodoVencimentoVO.getContaReceber().setValorBaseContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase());
					matriculaPeriodoVencimentoVO.getContaReceber().setValor(Uteis.arrendondarForcando2CadasDecimais(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase() - planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber()));
					matriculaPeriodoVencimentoVO.getContaReceber().setValorCusteadoContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber());
					matriculaPeriodoVencimentoVO.getContaReceber().setListaDescontosAplicavesContaReceber(listaDescontos);
					valoresPreenchidos =  true;
				}
				if (matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimento() == 0 && planoFinanceiroAlunoDescricaoDescontosVO.getReferentePlanoFinanceiroAposVcto()) {
					planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(0.0);
				}
				if (!(planoFinanceiroAlunoDescricaoDescontosVO.executarCalculoValorTotalDesconto().equals(0.0) && planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimentoDescontoComecaASerAplicado() != 0)) {					
					if (!matriculaPeriodoVencimentoVO.getDescricaoDesconto().isEmpty()) {
						matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + "</br>");
					}
					matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + planoFinanceiroAlunoDescricaoDescontosVO.getDescricaoDetalhadaComDataLimitesEDescontosValidos());
				}
			}


		}
	}

	private void realizarGeracaoDescricaoParcelaMatricula(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		DescontoProgressivoVO descontoprogressivoVO = new DescontoProgressivoVO();
		if (!matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadraoMatricula().getCodigo().equals(0)) {
			descontoprogressivoVO = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadraoMatricula().getCodigo(), usuario);
		}

		Date dataVencimentoMatricula = matriculaPeriodoVencimentoVO.getDataVencimento();
		/**
		 * Responsável por definir a data base de vencimento da parcela
		 */
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontos = (getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(true, matriculaPeriodoVO.getValorMatriculaCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getPercDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoMatricula, dataVencimentoMatricula, ordemDesconto, descontoprogressivoVO, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
		boolean valoresPreenchidos = Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber());
		for (PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO : listaDescontos) {
			if(!valoresPreenchidos) {
				matriculaPeriodoVencimentoVO.getContaReceber().setValorBaseContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase());
				matriculaPeriodoVencimentoVO.getContaReceber().setValor(Uteis.arrendondarForcando2CadasDecimais(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase() - planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber()));
				matriculaPeriodoVencimentoVO.getContaReceber().setValorCusteadoContaReceber(planoFinanceiroAlunoDescricaoDescontosVO.getValorCusteadoContaReceber());
				matriculaPeriodoVencimentoVO.getContaReceber().setListaDescontosAplicavesContaReceber(listaDescontos);
				valoresPreenchidos =  true;
			}
			if (!(planoFinanceiroAlunoDescricaoDescontosVO.executarCalculoValorTotalDesconto().equals(0.0) && planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimentoDescontoComecaASerAplicado() != 0)) {
				if (!matriculaPeriodoVencimentoVO.getDescricaoDesconto().isEmpty()) {
					matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + "</br>");
				}
				matriculaPeriodoVencimentoVO.setDescricaoDesconto(matriculaPeriodoVencimentoVO.getDescricaoDesconto() + planoFinanceiroAlunoDescricaoDescontosVO.getDescricaoDetalhadaComDataLimitesEDescontosValidos());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarExisteMatriculaPeriodoVencimentoGeradaPagaOuNegociada(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(" SELECT codigo FROM matriculaPeriodoVencimento mpv ");
		sql.append(" WHERE mpv.matriculaPeriodo = ").append(matriculaPeriodo).append(" AND mpv.situacao in ('NCR','GP')");
		sql.append(" AND mpv.tipoOrigemMatriculaPeriodoVencimento in ('MATERIAL_DIDATICO','MENSALIDADE')");
		sql.append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (tabelaResultado.next());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarDataVencimentoEDataCompetencia(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE matriculaperiodovencimento SET datavencimento = ?, datacompetencia = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { Uteis.getDataJDBC(matriculaPeriodoVencimentoVO.getDataVencimento()), Uteis.getDataJDBC(matriculaPeriodoVencimentoVO.getDataCompetencia()), matriculaPeriodoVencimentoVO.getCodigo() });
	}

	 public void consultarValorMatriculaMensalidadeMatriculaPeriodoVencimento(MatriculaPeriodoVO matriculaPeriodoVO, boolean controlarAcesso, int nivelMontarDados, String tipoBoleto, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select distinct case when tipoboleto = 'MA' then valor  end as valormatricula,  case when tipoboleto = 'ME' then valor end as valormensalidade,  tipoboleto  from matriculaperiodovencimento ");
			sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
			sqlStr.append(" where matriculaperiodo.matricula = '").append(matriculaPeriodoVO.getMatricula()).append("' ");
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getAno())) {
				sqlStr.append(" and matriculaperiodo.ano  = '").append(matriculaPeriodoVO.getAno()).append("' ");
				}
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getSemestre())) {
				sqlStr.append(" and matriculaperiodo.semestre  = '").append(matriculaPeriodoVO.getSemestre()).append("' ");
				}
			sqlStr.append(" and tipoboleto =  '").append(tipoBoleto).append("' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				if(tipoBoleto.equals("MA")) {
					matriculaPeriodoVO.setValorMatriculaCheio(tabelaResultado.getDouble("valormatricula"));
				}
				if(tipoBoleto.equals("ME")) {
					matriculaPeriodoVO.setValorMensalidadeCheio(tabelaResultado.getDouble("valormensalidade"));
				}
			}
		}
}
