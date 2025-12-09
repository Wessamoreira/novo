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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConvenioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConvenioVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ConvenioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ConvenioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Convenio extends ControleAcesso implements ConvenioInterfaceFacade {

	protected static String idEntidade;

	public Convenio() throws Exception {
		super();
		setIdEntidade("Convenio");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ConvenioVO</code>.
	 */
	public ConvenioVO novo() throws Exception {
		Convenio.incluir(getIdEntidade());
		ConvenioVO obj = new ConvenioVO();
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConvenioVO obj, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getPossuiDescontoAntecipacao()) {
			validarPorcentagensDescontosProgressivos(obj, usuarioLogado);
		}
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, usuarioLogado);
		} else {
			alterar(obj, usuarioLogado);
		}

	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConvenioVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConvenioVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConvenioVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConvenioVO.validarDados(obj);
			validarUnicidadeConvenio(obj, usuario);
			validarSeJaExisteConvenioTipoPadraoParceiro(obj, usuario);
			Convenio.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO Convenio( descricao, dataAssinatura, cobertura, preRequisitos, dataInicioVigencia, dataFinalVigencia, " + "descontoMatricula, tipoDescontoMatricula, descontoParcela, tipoDescontoParcela, bolsaCusteadaParceiroMatricula, " + "tipoBolsaCusteadaParceiroMatricula, bolsaCusteadaParceiroParcela, tipoBolsaCusteadaParceiroParcela, formaRecebimentoParceiro, " + "diaBaseRecebimentoParceiro, parceiro, requisitante, dataRequisicao, responsavelAutorizacao, dataAutorizacao, " + "responsavelFinalizacao, dataFinalizacao, situacao, validoParaTodoCurso, validoParaTodaUnidadeEnsino, validoParaTodoTurno, " + "periodoIndeterminado, possuiDescontoAntecipacao, descontoProgressivoParceiro, descontoProgressivoAluno, calculadoEmCimaValorLiquido, " + "aplicardescontoprogressivomatricula, aplicardescontoprogressivomatriculaparceiro, abaterDescontoNoValorMatricula, abaterDescontoNoValorParcela, "
					+ "aplicarSobreValorBaseDeduzidoValorOutrosConvenios, ordemPrioridadeParaCalculo, convenioInadimplenteBloqueaMatricula," + "gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual, gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio, categoriaDespesaRestituicaoConvenio, " + "centroReceitaMatricula, centroReceitaMensalidade, tipoFinanciamentoEstudantil, removerDescontoRenovacao, " + "convenioTipoPadrao ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataAssinatura()));
					sqlInserir.setString(3, obj.getCobertura());
					sqlInserir.setString(4, obj.getPreRequisitos());
					if (obj.getPeriodoIndeterminado().booleanValue() == false) {
						sqlInserir.setDate(5, Uteis.getDataJDBC(Uteis.getDateTime(obj.getDataInicioVigencia(), 00, 00, 00)));
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getPeriodoIndeterminado().booleanValue() == false) {
						sqlInserir.setDate(6, Uteis.getDataJDBC(Uteis.getDateTime(obj.getDataFinalVigencia(), 23, 59, 59)));
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setDouble(7, obj.getDescontoMatricula().doubleValue());
					sqlInserir.setString(8, obj.getTipoDescontoMatricula());
					sqlInserir.setDouble(9, obj.getDescontoParcela().doubleValue());
					sqlInserir.setString(10, obj.getTipoDescontoParcela());
					sqlInserir.setDouble(11, obj.getBolsaCusteadaParceiroMatricula().doubleValue());
					sqlInserir.setString(12, obj.getTipoBolsaCusteadaParceiroMatricula());
					sqlInserir.setDouble(13, obj.getBolsaCusteadaParceiroParcela().doubleValue());
					sqlInserir.setString(14, obj.getTipoBolsaCusteadaParceiroParcela());
					if(Uteis.isAtributoPreenchido(obj.getFormaRecebimentoParceiro().getCodigo().intValue())) {
						sqlInserir.setInt(15, obj.getFormaRecebimentoParceiro().getCodigo());
					}else {
						sqlInserir.setNull(15, 0);
					}
					
					sqlInserir.setInt(16, obj.getDiaBaseRecebimentoParceiro().intValue());
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(17, 0);
					}
					if (obj.getRequisitante().getCodigo().intValue() != 0) {
						sqlInserir.setInt(18, obj.getRequisitante().getCodigo().intValue());
					} else {
						sqlInserir.setNull(18, 0);
					}
					sqlInserir.setDate(19, Uteis.getDataJDBC(obj.getDataRequisicao()));
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(20, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataAutorizacao()));
					if (obj.getResponsavelFinalizacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(22, obj.getResponsavelFinalizacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(22, 0);
					}
					sqlInserir.setDate(23, Uteis.getDataJDBC(obj.getDataFinalizacao()));
					sqlInserir.setString(24, obj.getSituacao());
					sqlInserir.setBoolean(25, obj.isValidoParaTodoCurso().booleanValue());
					sqlInserir.setBoolean(26, obj.isValidoParaTodaUnidadeEnsino().booleanValue());
					sqlInserir.setBoolean(27, obj.isValidoParaTodoTurno().booleanValue());
					sqlInserir.setBoolean(28, obj.getPeriodoIndeterminado().booleanValue());
					sqlInserir.setBoolean(29, obj.getPossuiDescontoAntecipacao());
					if (obj.getDescontoProgressivoParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(30, obj.getDescontoProgressivoParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(30, 0);
					}
					if (obj.getDescontoProgressivoAluno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(31, obj.getDescontoProgressivoAluno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(31, 0);
					}
					sqlInserir.setBoolean(32, obj.getCalculadoEmCimaValorLiquido());
					if (obj.getPossuiDescontoAntecipacao()) {
						sqlInserir.setBoolean(33, obj.getAplicarDescontoProgressivoMatricula());
						sqlInserir.setBoolean(34, obj.getAplicarDescontoProgressivoMatriculaParceiro());
					} else {
						sqlInserir.setNull(33, 0);
						sqlInserir.setNull(34, 0);
					}
					sqlInserir.setBoolean(35, obj.getAbaterDescontoNoValorMatricula());
					sqlInserir.setBoolean(36, obj.getAbaterDescontoNoValorParcela());
					sqlInserir.setBoolean(37, obj.getAplicarSobreValorBaseDeduzidoValorOutrosConvenios());
					sqlInserir.setInt(38, obj.getOrdemPrioridadeParaCalculo());
					sqlInserir.setBoolean(39, obj.getConvenioInadimplenteBloqueaMatricula());

					sqlInserir.setBoolean(40, obj.getGerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual());
					sqlInserir.setBoolean(41, obj.getGerarRestituicaoAlunoValorJaPagoRelativoAoConvenio());
					if (obj.getCategoriaDespesaRestituicaoConvenio().getCodigo().intValue() != 0) {
						sqlInserir.setInt(42, obj.getCategoriaDespesaRestituicaoConvenio().getCodigo().intValue());
					} else {
						sqlInserir.setNull(42, 0);
					}
					if (obj.getCentroReceitaMatricula().getCodigo().intValue() != 0) {
						sqlInserir.setInt(43, obj.getCentroReceitaMatricula().getCodigo().intValue());
					} else {
						sqlInserir.setNull(43, 0);
					}
					if (obj.getCentroReceitaMensalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(44, obj.getCentroReceitaMensalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(44, 0);
					}
					sqlInserir.setString(45, obj.getTipoFinanciamentoEstudantil());
					sqlInserir.setBoolean(46, obj.getRemoverDescontoRenovacao());
					sqlInserir.setBoolean(47, obj.getConvenioTipoPadrao().booleanValue());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getConvenioUnidadeEnsinoFacade().incluirConvenioUnidadeEnsinos(obj.getCodigo(), obj.getConvenioUnidadeEnsinoVOs());
			getFacadeFactory().getConvenioTurnoFacade().incluirConvenioTurnos(obj.getCodigo(), obj.getConvenioTurnoVOs());
			getFacadeFactory().getConvenioCursoFacade().incluirConvenioCursos(obj.getCodigo(), obj.getConvenioCursoVOs());
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException && e.getMessage().contains("ck_convenio")) {
				throw new ConsistirException("Já existe um convênio cadastrado com este NOME e estes PERCENTUAIS DE DESCONTO, favor renomear o mesmo.");
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ConvenioVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConvenioVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConvenioVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConvenioVO.validarDados(obj);
			validarUnicidadeConvenio(obj, usuario);
			validarSeJaExisteConvenioTipoPadraoParceiro(obj, usuario);
			Convenio.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE Convenio set descricao=?, dataAssinatura=?, cobertura=?, preRequisitos=?, dataInicioVigencia=?, " + "dataFinalVigencia=?, descontoMatricula=?, tipoDescontoMatricula=?, descontoParcela=?, tipoDescontoParcela=?, " + "bolsaCusteadaParceiroMatricula=?, tipoBolsaCusteadaParceiroMatricula=?, bolsaCusteadaParceiroParcela=?, " + "tipoBolsaCusteadaParceiroParcela=?, formaRecebimentoParceiro=?, diaBaseRecebimentoParceiro=?, parceiro=?, requisitante=?, " + "dataRequisicao=?, responsavelAutorizacao=?, dataAutorizacao=?, responsavelFinalizacao=?, dataFinalizacao=?, situacao=?, " + "validoParaTodoCurso=?, validoParaTodaUnidadeEnsino=?, validoParaTodoTurno=?, periodoIndeterminado=?, possuiDescontoAntecipacao=?, " + "descontoProgressivoParceiro=?, descontoProgressivoAluno=?, calculadoEmCimaValorLiquido=?, aplicarDescontoProgressivoMatricula=?, " + "aplicardescontoprogressivomatriculaparceiro=?, abaterDescontoNoValorMatricula=?, abaterDescontoNoValorParcela=?, "
					+ "aplicarSobreValorBaseDeduzidoValorOutrosConvenios=?, ordemPrioridadeParaCalculo=?, convenioInadimplenteBloqueaMatricula=?, " + "gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual=?, gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio=?, categoriaDespesaRestituicaoConvenio=?, " + "centroReceitaMatricula=?, centroReceitaMensalidade=? , tipoFinanciamentoEstudantil=?, removerDescontoRenovacao=?, " + "convenioTipoPadrao=? " + "WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataAssinatura()));
					sqlAlterar.setString(3, obj.getCobertura());
					sqlAlterar.setString(4, obj.getPreRequisitos());
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataInicioVigencia()));
					sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataFinalVigencia()));
					sqlAlterar.setDouble(7, obj.getDescontoMatricula().doubleValue());
					sqlAlterar.setString(8, obj.getTipoDescontoMatricula());
					sqlAlterar.setDouble(9, obj.getDescontoParcela().doubleValue());
					sqlAlterar.setString(10, obj.getTipoDescontoParcela());
					sqlAlterar.setDouble(11, obj.getBolsaCusteadaParceiroMatricula().doubleValue());
					sqlAlterar.setString(12, obj.getTipoBolsaCusteadaParceiroMatricula());
					sqlAlterar.setDouble(13, obj.getBolsaCusteadaParceiroParcela().doubleValue());
					sqlAlterar.setString(14, obj.getTipoBolsaCusteadaParceiroParcela());
					if(Uteis.isAtributoPreenchido(obj.getFormaRecebimentoParceiro().getCodigo().intValue())) {
						sqlAlterar.setInt(15, obj.getFormaRecebimentoParceiro().getCodigo());
					}else {
						sqlAlterar.setNull(15, 0);
					}
					
					
					sqlAlterar.setInt(16, obj.getDiaBaseRecebimentoParceiro().intValue());
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(17, 0);
					}
					if (obj.getRequisitante().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getRequisitante().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setDate(19, Uteis.getDataJDBC(obj.getDataRequisicao()));
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(20, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(20, 0);
					}
					sqlAlterar.setDate(21, Uteis.getDataJDBC(obj.getDataAutorizacao()));
					if (obj.getResponsavelFinalizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(22, obj.getResponsavelFinalizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					sqlAlterar.setDate(23, Uteis.getDataJDBC(obj.getDataFinalizacao()));
					sqlAlterar.setString(24, obj.getSituacao());
					sqlAlterar.setBoolean(25, obj.isValidoParaTodoCurso().booleanValue());
					sqlAlterar.setBoolean(26, obj.isValidoParaTodaUnidadeEnsino().booleanValue());
					sqlAlterar.setBoolean(27, obj.isValidoParaTodoTurno().booleanValue());
					sqlAlterar.setBoolean(28, obj.getPeriodoIndeterminado().booleanValue());
					sqlAlterar.setBoolean(29, obj.getPossuiDescontoAntecipacao());
					if (obj.getDescontoProgressivoParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(30, obj.getDescontoProgressivoParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(30, 0);
					}
					if (obj.getDescontoProgressivoAluno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(31, obj.getDescontoProgressivoAluno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(31, 0);
					}
					sqlAlterar.setBoolean(32, obj.getCalculadoEmCimaValorLiquido());
					if (obj.getPossuiDescontoAntecipacao()) {
						sqlAlterar.setBoolean(33, obj.getAplicarDescontoProgressivoMatricula());
						sqlAlterar.setBoolean(34, obj.getAplicarDescontoProgressivoMatriculaParceiro());
					} else {
						sqlAlterar.setNull(33, 0);
						sqlAlterar.setNull(34, 0);
					}
					sqlAlterar.setBoolean(35, obj.getAbaterDescontoNoValorMatricula());
					sqlAlterar.setBoolean(36, obj.getAbaterDescontoNoValorParcela());

					sqlAlterar.setBoolean(37, obj.getAplicarSobreValorBaseDeduzidoValorOutrosConvenios());
					sqlAlterar.setInt(38, obj.getOrdemPrioridadeParaCalculo());
					sqlAlterar.setBoolean(39, obj.getConvenioInadimplenteBloqueaMatricula());

					sqlAlterar.setBoolean(40, obj.getGerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual());
					sqlAlterar.setBoolean(41, obj.getGerarRestituicaoAlunoValorJaPagoRelativoAoConvenio());
					if (obj.getCategoriaDespesaRestituicaoConvenio().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(42, obj.getCategoriaDespesaRestituicaoConvenio().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(42, 0);
					}
					if (obj.getCentroReceitaMatricula().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(43, obj.getCentroReceitaMatricula().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(43, 0);
					}
					if (obj.getCentroReceitaMensalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(44, obj.getCentroReceitaMensalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(44, 0);
					}
					sqlAlterar.setString(45, obj.getTipoFinanciamentoEstudantil());

					sqlAlterar.setBoolean(46, obj.getRemoverDescontoRenovacao());
					sqlAlterar.setBoolean(47, obj.getConvenioTipoPadrao().booleanValue());
					sqlAlterar.setInt(48, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getConvenioUnidadeEnsinoFacade().alterarConvenioUnidadeEnsinos(obj.getCodigo(), obj.getConvenioUnidadeEnsinoVOs());
			getFacadeFactory().getConvenioTurnoFacade().alterarConvenioTurnos(obj.getCodigo(), obj.getConvenioTurnoVOs());
			getFacadeFactory().getConvenioCursoFacade().alterarConvenioCursos(obj.getCodigo(), obj.getConvenioCursoVOs());
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException && e.getMessage().contains("ck_convenio")) {
				throw new ConsistirException("Já existe um convênio cadastrado com este NOME e estes PERCENTUAIS DE DESCONTO, favor renomear o mesmo.");
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final ConvenioVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Convenio set " + "responsavelAutorizacao=?, dataAutorizacao=?, responsavelFinalizacao=?, dataFinalizacao=?, situacao=? " + " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataAutorizacao()));
					if (obj.getResponsavelFinalizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getResponsavelFinalizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataFinalizacao()));
					sqlAlterar.setString(5, obj.getSituacao());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ConvenioVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConvenioVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConvenioVO obj, UsuarioVO usuario) throws Exception {
		try {
			Convenio.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM Convenio WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getConvenioUnidadeEnsinoFacade().excluirConvenioUnidadeEnsinos(obj.getCodigo());
			getFacadeFactory().getConvenioTurnoFacade().excluirConvenioTurnos(obj.getCodigo());
			getFacadeFactory().getConvenioCursoFacade().excluirConvenioCursos(obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Convenio</code> através do
	 * valor do atributo <code>nome</code> da classe <code>Parceiro</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ConvenioVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeParceiro(String valorConsulta, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Convenio.* FROM Convenio, Parceiro WHERE Convenio.parceiro = Parceiro.codigo and upper( Parceiro.nome ) like('" + valorConsulta.toUpperCase() + "%') ");
		if (!tipoOrigem.equals("")) {
			sqlStr.append(" and tipoorigem = ").append(tipoOrigem);
		}
		sqlStr.append(" ORDER BY Parceiro.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeParceiroESituacao(String valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = " SELECT Convenio.* FROM Convenio INNER JOIN Parceiro ON Convenio.parceiro = Parceiro.codigo" + " WHERE upper( Parceiro.nome ) like('" + valorConsulta.toUpperCase() + "%') " + " AND Convenio.ativo = " + ativo + " ORDER BY Parceiro.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Convenio</code> através do
	 * valor do atributo <code>Date dataAssinatura</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConvenioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Convenio WHERE ((dataAssinatura >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataAssinatura <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataAssinatura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Convenio</code> através do
	 * valor do atributo <code>String descricao</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConvenioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Convenio WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ConvenioVO> consultarConvenioAptoUsarNaMatricula(Integer unidadeEnsino, Integer curso, Integer turno, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("select * from convenio  where   ativo = true and ");
		sb.append("  (validoparatodaunidadeensino =  true or convenio.codigo in (select convenio from conveniounidadeensino where unidadeensino = ").append(unidadeEnsino).append(")) ");
		sb.append("  and (validoparatodocurso =  true or convenio.codigo in (select convenio from conveniocurso where curso  =  ").append(curso).append(" )) ");
		sb.append("  and (validoparatodoturno =  true or convenio.codigo in (select convenio from convenioturno  where turno =  ").append(turno).append(" )) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<ConvenioVO> consultarConvenioAptoUsarNaMatriculaPorParceiro(Integer unidadeEnsino, Integer curso, Integer turno, Integer parceiro, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("select * from convenio  where   ativo = true and ");
		sb.append("  (validoparatodaunidadeensino =  true or convenio.codigo in (select convenio from conveniounidadeensino where unidadeensino = ").append(unidadeEnsino).append(")) ");
		sb.append("  and (validoparatodocurso =  true or convenio.codigo in (select convenio from conveniocurso where curso  =  ").append(curso).append(" )) ");
		sb.append("  and (validoparatodoturno =  true or convenio.codigo in (select convenio from convenioturno  where turno =  ").append(turno).append(" )) ");
		sb.append("  and parceiro = ").append(parceiro);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorDescricaoESituacao(String valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Convenio" + " WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%')" + " AND ativo = " + ativo + " ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorAtivoVigente(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Convenio WHERE (dataInicioVigencia<='" + Uteis.getDataJDBC(new Date()) + "' and dataFinalVigencia >= '" + Uteis.getDataJDBC(Uteis.getDateTime(new Date(), 23, 59, 59)) + "' or periodoIndeterminado=true) and responsavelAutorizacao > 0 ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorAtivoVigenteESituacao(boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Convenio" + " WHERE (dataInicioVigencia<='" + Uteis.getDataJDBC(new Date()) + "' and dataFinalVigencia >= '" + Uteis.getDataJDBC(Uteis.getDateTime(new Date(), 23, 59, 59)) + "' or periodoIndeterminado=true)" + " AND responsavelAutorizacao > 0 AND ativo = " + ativo + " ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Convenio</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConvenioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Convenio WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean consultarPorCodigoConvenioAbateValorCusteadoContaAReceber(Integer valorConsulta, boolean contaAReceberDeMensalidade, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT abaterDescontoNoValorParcela FROM Convenio WHERE codigo = " + valorConsulta.intValue();
		if (!contaAReceberDeMensalidade) {
			sqlStr = "SELECT abaterDescontoNoValorMatricula FROM Convenio WHERE codigo = " + valorConsulta.intValue();
		}
		Boolean deveAbaterValor = Boolean.FALSE;
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			while (tabelaResultado.next()) {
				if (!contaAReceberDeMensalidade) {
					deveAbaterValor = tabelaResultado.getBoolean("abaterDescontoNoValorMatricula");
				} else {
					deveAbaterValor = tabelaResultado.getBoolean("abaterDescontoNoValorParcela");
				}
				break;
			}
		} catch (Exception e) {
		}
		return deveAbaterValor;
	}

	public List consultarPorCodigoESituacao(Integer valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Convenio" + " WHERE codigo >= " + valorConsulta.intValue() + "" + " AND ativo = " + ativo + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorSituacao(boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Convenio" + " WHERE ativo = " + ativo + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ConvenioVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConvenioVO</code>.
	 * 
	 * @return O objeto da classe <code>ConvenioVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ConvenioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConvenioVO obj = new ConvenioVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setConvenioTipoPadrao(dadosSQL.getBoolean("convenioTipoPadrao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setDataAssinatura(dadosSQL.getDate("dataAssinatura"));
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		obj.setCobertura(dadosSQL.getString("cobertura"));
		obj.setPreRequisitos(dadosSQL.getString("preRequisitos"));
		obj.setDataInicioVigencia(dadosSQL.getDate("dataInicioVigencia"));
		obj.setDataFinalVigencia(dadosSQL.getDate("dataFinalVigencia"));
		obj.setDescontoMatricula(dadosSQL.getDouble("descontoMatricula"));
		obj.setTipoDescontoMatricula(dadosSQL.getString("tipoDescontoMatricula"));
		obj.setAbaterDescontoNoValorMatricula(dadosSQL.getBoolean("abaterDescontoNoValorMatricula"));
		obj.setDescontoParcela(dadosSQL.getDouble("descontoParcela"));
		obj.setTipoDescontoParcela(dadosSQL.getString("tipoDescontoParcela"));
		obj.setAbaterDescontoNoValorParcela(dadosSQL.getBoolean("abaterDescontoNoValorParcela"));
		obj.setBolsaCusteadaParceiroMatricula(dadosSQL.getDouble("bolsaCusteadaParceiroMatricula"));
		obj.setTipoBolsaCusteadaParceiroMatricula(dadosSQL.getString("tipoBolsaCusteadaParceiroMatricula"));
		obj.setBolsaCusteadaParceiroParcela(dadosSQL.getDouble("bolsaCusteadaParceiroParcela"));
		obj.setTipoBolsaCusteadaParceiroParcela(dadosSQL.getString("tipoBolsaCusteadaParceiroParcela"));
		obj.getFormaRecebimentoParceiro().setCodigo(dadosSQL.getInt("formaRecebimentoParceiro"));
		obj.setDiaBaseRecebimentoParceiro(dadosSQL.getInt("diaBaseRecebimentoParceiro"));
		obj.getRequisitante().setCodigo(dadosSQL.getInt("requisitante"));
		obj.setDataRequisicao(dadosSQL.getDate("dataRequisicao"));
		obj.getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("responsavelAutorizacao"));
		obj.setDataAutorizacao(dadosSQL.getDate("dataAutorizacao"));
		obj.getResponsavelFinalizacao().setCodigo(dadosSQL.getInt("responsavelFinalizacao"));
		obj.setDataFinalizacao(dadosSQL.getDate("dataFinalizacao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setValidoParaTodoCurso(dadosSQL.getBoolean("validoParaTodoCurso"));
		obj.setValidoParaTodaUnidadeEnsino(dadosSQL.getBoolean("validoParaTodaUnidadeEnsino"));
		obj.setValidoParaTodoTurno(dadosSQL.getBoolean("validoParaTodoTurno"));
		obj.setPeriodoIndeterminado(dadosSQL.getBoolean("periodoIndeterminado"));
		obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
		obj.setDataInativacao(dadosSQL.getDate("dataInativacao"));
		obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelAtivacao"));
		obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelInativacao"));
		obj.getDescontoProgressivoParceiro().setCodigo(dadosSQL.getInt("descontoProgressivoParceiro"));
		obj.getDescontoProgressivoAluno().setCodigo(dadosSQL.getInt("descontoProgressivoAluno"));
		obj.setPossuiDescontoAntecipacao(dadosSQL.getBoolean("possuiDescontoAntecipacao"));
		obj.setCalculadoEmCimaValorLiquido(dadosSQL.getBoolean("calculadoEmCimaValorLiquido"));
		obj.setAplicarDescontoProgressivoMatricula(dadosSQL.getBoolean("aplicarDescontoProgressivoMatricula"));
		obj.setAplicarDescontoProgressivoMatriculaParceiro(dadosSQL.getBoolean("aplicarDescontoProgressivoMatriculaParceiro"));
		obj.setAplicarSobreValorBaseDeduzidoValorOutrosConvenios(dadosSQL.getBoolean("aplicarSobreValorBaseDeduzidoValorOutrosConvenios"));
		obj.setOrdemPrioridadeParaCalculo(dadosSQL.getInt("ordemPrioridadeParaCalculo"));
		obj.setConvenioInadimplenteBloqueaMatricula(dadosSQL.getBoolean("convenioInadimplenteBloqueaMatricula"));

		obj.setGerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual(dadosSQL.getBoolean("gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual"));
		obj.setGerarRestituicaoAlunoValorJaPagoRelativoAoConvenio(dadosSQL.getBoolean("gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio"));
		obj.getCategoriaDespesaRestituicaoConvenio().setCodigo(dadosSQL.getInt("categoriaDespesaRestituicaoConvenio"));
		obj.getCentroReceitaMatricula().setCodigo(dadosSQL.getInt("centroReceitaMatricula"));
		obj.getCentroReceitaMensalidade().setCodigo(dadosSQL.getInt("centroReceitaMensalidade"));
		obj.setTipoFinanciamentoEstudantil(dadosSQL.getString("tipoFinanciamentoEstudantil"));
		obj.setRemoverDescontoRenovacao(dadosSQL.getBoolean("removerDescontoRenovacao"));
		montarDadosDescontoProgressivoParceiro(obj, nivelMontarDados, usuario);
		montarDadosDescontoProgressivoAluno(obj, nivelMontarDados, usuario);
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			obj.setParceiro(Uteis.montarDadosVO(dadosSQL.getInt("parceiro"), ParceiroVO.class, p -> getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
			return obj;
		}
		
		obj.setConvenioUnidadeEnsinoVOs(ConvenioUnidadeEnsino.consultarConvenioUnidadeEnsinos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		obj.setParceiro(Uteis.montarDadosVO(dadosSQL.getInt("parceiro"), ParceiroVO.class, p -> getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {			
			return obj;
		}
		obj.setConvenioTurnoVOs(ConvenioTurno.consultarConvenioTurnos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		obj.setConvenioCursoVOs(ConvenioCurso.consultarConvenioCursos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		montarResponsavelAtivacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarResponsavelInativacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosCategoriaDespesaRestituicaoConvenio(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCentroReceitaMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCentroReceitaMensalidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);		
		montarDadosRequisitante(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelFinalizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFormaRecebimentoParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static void montarDadosDescontoProgressivoParceiro(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDescontoProgressivoParceiro().getCodigo().intValue() == 0) {
			obj.setDescontoProgressivoParceiro(new DescontoProgressivoVO());
			return;
		}
		obj.setDescontoProgressivoParceiro(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoParceiro().getCodigo(), usuario));
	}

	public static void montarDadosDescontoProgressivoAluno(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDescontoProgressivoAluno().getCodigo().intValue() == 0) {
			obj.setDescontoProgressivoAluno(new DescontoProgressivoVO());
			return;
		}
		obj.setDescontoProgressivoAluno(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoAluno().getCodigo(), usuario));
	}

	public static void montarResponsavelAtivacao(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!obj.getResponsavelAtivacao().getCodigo().equals(0)) {
			obj.setResponsavelAtivacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAtivacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
	}

	public static void montarResponsavelInativacao(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!obj.getResponsavelInativacao().getCodigo().equals(0)) {
			obj.setResponsavelInativacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelInativacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>ConvenioVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelFinalizacao(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelFinalizacao().getCodigo().intValue() == 0) {
			obj.setResponsavelFinalizacao(new PessoaVO());
			return;
		}
		obj.setResponsavelFinalizacao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelFinalizacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>ConvenioVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAutorizacao(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAutorizacao(new PessoaVO());
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>ConvenioVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosRequisitante(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRequisitante().getCodigo().intValue() == 0) {
			obj.setRequisitante(new FuncionarioVO());
			return;
		}
		obj.setRequisitante(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getRequisitante().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosCategoriaDespesaRestituicaoConvenio(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesaRestituicaoConvenio().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCategoriaDespesaRestituicaoConvenio(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesaRestituicaoConvenio().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	public static void montarDadosCentroReceitaMatricula(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCentroReceitaMatricula().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCentroReceitaMatricula(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaMatricula().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosCentroReceitaMensalidade(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCentroReceitaMensalidade().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCentroReceitaMensalidade(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaMensalidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ParceiroVO</code> relacionado ao objeto <code>ConvenioVO</code>.
	 * Faz uso da chave primária da classe <code>ParceiroVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosParceiro(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FormaPagamentoVO</code> relacionado ao objeto
	 * <code>ConvenioVO</code>. Faz uso da chave primária da classe
	 * <code>FormaPagamentoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFormaRecebimentoParceiro(ConvenioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaRecebimentoParceiro().getCodigo().intValue() == 0) {
			obj.setFormaRecebimentoParceiro(new FormaPagamentoVO());
			return;
		}
		obj.setFormaRecebimentoParceiro(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaRecebimentoParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ConvenioVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ConvenioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Convenio WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Convenio ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Convenio.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Convenio.idEntidade = idEntidade;
	}

	public String realizarAtivacaoInativacao(ConvenioVO convenioVO, UsuarioVO usuario) throws Exception {

		if (convenioVO.getAtivo()) {
			convenioVO.setAtivo(false);
			convenioVO.setDataInativacao(new Date());
			convenioVO.setResponsavelInativacao(usuario);
			realizarAlterarConvenioParaInativo(convenioVO, usuario);
			return "msg_dados_inativados";
		} else {
			convenioVO.setAtivo(true);
			convenioVO.setDataAtivacao(new Date());
			convenioVO.setResponsavelAtivacao(usuario);
			realizarAlterarConvenioParaAtivo(convenioVO, usuario);
			return "msg_dados_ativados";
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlterarConvenioParaAtivo(final ConvenioVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Convenio set ativo=?, dataAtivacao=?, responsavelAtivacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getAtivo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAtivacao()));
				sqlAlterar.setInt(3, obj.getResponsavelAtivacao().getCodigo());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlterarConvenioParaInativo(final ConvenioVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Convenio set ativo=?, dataInativacao=?, responsavelInativacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getAtivo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataInativacao()));
				sqlAlterar.setInt(3, obj.getResponsavelInativacao().getCodigo());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	public void validarPorcentagensDescontosProgressivos(ConvenioVO convenioVO, UsuarioVO usuarioLogado) throws Exception {
		DescontoProgressivoVO descontoProgressivoParceiro = new DescontoProgressivoVO();
		DescontoProgressivoVO descontoProgressivoAluno = new DescontoProgressivoVO();
		try {
			if (!convenioVO.getDescontoProgressivoParceiro().getCodigo().equals(0)) {
				descontoProgressivoParceiro = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(convenioVO.getDescontoProgressivoParceiro().getCodigo(), usuarioLogado);
			}
			if (!convenioVO.getDescontoProgressivoAluno().getCodigo().equals(0)) {
				descontoProgressivoAluno = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(convenioVO.getDescontoProgressivoAluno().getCodigo(), usuarioLogado);
			}
			if (descontoProgressivoAluno.getPercDescontoLimite1() + descontoProgressivoParceiro.getPercDescontoLimite1() > 100) {
				throw new Exception("A soma dos percentuais de desconto de antecipação está acima de 100%.");
			}
		} finally {
			descontoProgressivoAluno = null;
			descontoProgressivoParceiro = null;
		}
	}

	public List consultarPorPlanoFinanceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct convenio.* from convenio ");
		sb.append(" inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.convenio = convenio.codigo ");
		if (planoFinanceiroAluno != null && planoFinanceiroAluno > 0) {
			sb.append(" where itemplanofinanceiroaluno.planoFinanceiroAluno = ");
			sb.append(planoFinanceiroAluno);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ConvenioVO> consultarPorParceiro(Integer parceiro, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Convenio.* FROM Convenio, Parceiro WHERE Convenio.parceiro = Parceiro.codigo and Parceiro.codigo = ").append(parceiro);
		if (!tipoOrigem.equals("")) {
			sqlStr.append(" and tipoorigem = ").append(tipoOrigem);
		}
		sqlStr.append(" ORDER BY Parceiro.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<ConvenioVO> consultarConvenioFiltrarRenovacaoTurmaNivelCombobox(Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct convenio.codigo, convenio.descricao from Convenio ");
		sql.append(" inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.convenio = convenio.codigo ");
		sql.append(" inner join planofinanceiroaluno on planofinanceiroaluno.codigo = itemplanofinanceiroaluno.planofinanceiroaluno ");
		sql.append(" inner join matriculaperiodo on planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sql.append(" where matriculaperiodo.turma = ").append(turma);
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			if (semestre != null && !semestre.trim().isEmpty()) {
				sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
		}
		if (gradeCurricular != null && gradeCurricular > 0) {
			sql.append(" and matricula.gradeCurricularAtual = ").append(gradeCurricular);
		}
		sql.append(" order by Convenio.descricao ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ConvenioVO> convenioVOs = new ArrayList<ConvenioVO>(0);
		ConvenioVO obj = null;
		while (rs.next()) {
			obj = new ConvenioVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDescricao(rs.getString("descricao"));
			convenioVOs.add(obj);
		}
		return convenioVOs;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ConvenioVO consultarConvenioPadrao(ParceiroVO parceiro, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Convenio WHERE convenioTipoPadrao = ? and parceiro = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { true, parceiro.getCodigo() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Não foi encontrado as informações padrões adicionais para o cadastro do convênio deste parceiro.");			
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarUnicidadeConvenio(ConvenioVO obj, UsuarioVO usuario) throws Exception {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("SELECT descricao FROM Convenio WHERE ");
			sqlStr.append(" descricao =  '").append(obj.getDescricao()).append("' ");
			sqlStr.append(" and parceiro =  ").append(obj.getParceiro().getCodigo());
			sqlStr.append(" and bolsaCusteadaParceiroMatricula =  ").append(obj.getBolsaCusteadaParceiroMatricula());
			sqlStr.append(" and tipoBolsaCusteadaParceiroMatricula =  '").append(obj.getTipoBolsaCusteadaParceiroMatricula()).append("' ");
			sqlStr.append(" and bolsaCusteadaParceiroParcela =  ").append(obj.getBolsaCusteadaParceiroParcela());
			sqlStr.append(" and tipoBolsaCusteadaParceiroParcela =  '").append(obj.getTipoBolsaCusteadaParceiroParcela()).append("' ");
			
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
				sqlStr.append(" and codigo !=  ").append(obj.getCodigo());
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (rs.next() && Uteis.isAtributoPreenchido(rs.getString("descricao"))) {
				throw new Exception("O Convênio de Descrição: \"" + rs.getString("descricao") + "\" já está cadastrado para esse parceiro com essas valores de custeo.");
			}
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarSeJaExisteConvenioTipoPadraoParceiro(ConvenioVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getConvenioTipoPadrao()) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("SELECT descricao FROM Convenio WHERE ");
			sqlStr.append(" convenioTipoPadrao = true ");
			sqlStr.append(" and parceiro =  ").append(obj.getParceiro().getCodigo());
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
				sqlStr.append(" and codigo !=  ").append(obj.getCodigo());
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (rs.next() && Uteis.isAtributoPreenchido(rs.getString("descricao"))) {
				throw new Exception("O Convênio de Descrição: \"" + rs.getString("descricao") + "\" já está cadastrado como Padrão para esse parceiro.");
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarPersistenciaPorRenovacaoMatricula(ConvenioVO obj, ParceiroVO parceiro, UsuarioVO usuario) throws Exception {
		obj.setValidoParaTodaUnidadeEnsino(true);
		obj.setValidoParaTodoCurso(true);
		obj.setValidoParaTodoTurno(true);
		obj.setSituacao("AT");
		obj.getResponsavelAutorizacao().setCodigo(usuario.getPessoa().getCodigo());
		obj.setParceiro(parceiro);
		obj.setDataAutorizacao(new Date());
		obj.setResponsavelFinalizacao(new PessoaVO());
		obj.setDataFinalizacao(null);
		persistir(obj, usuario);
		obj.setAtivo(false);
		realizarAtivacaoInativacao(obj, usuario);

	}
	
	@Override
	public List<ConvenioVO> consultarConvenioPorSituacaoNivelCombobox(String situacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct convenio.codigo, convenio.descricao from Convenio WHERE 1=1 ");
		if (situacao != null && !situacao.equals("TO")) {
			sql.append(" AND convenio.situacao = '").append(situacao).append("' ");
		}
		sql.append(" order by Convenio.descricao ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ConvenioVO> convenioVOs = new ArrayList<ConvenioVO>(0);
		ConvenioVO obj = null;
		while (rs.next()) {
			obj = new ConvenioVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDescricao(rs.getString("descricao"));
			convenioVOs.add(obj);
		}
		return convenioVOs;

	}

}
