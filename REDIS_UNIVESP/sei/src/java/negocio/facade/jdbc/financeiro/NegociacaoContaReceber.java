package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

import javax.faces.model.SelectItem;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoFuncionarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberPlanoDescontoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.OpcaoAlunoCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.enumerador.TipoAcrescimoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.NegociacaoContaReceberInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>NegociacaoContaReceberVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>NegociacaoContaReceberVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see NegociacaoContaReceberVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class NegociacaoContaReceber extends ControleAcesso implements NegociacaoContaReceberInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1873322744633392666L;
	protected static String idEntidade;

	public NegociacaoContaReceber() throws Exception {
		super();
		setIdEntidade("NegociacaoContaReceber");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>NegociacaoContaReceberVO</code>.
	 */
	public NegociacaoContaReceberVO novo() throws Exception {
		NegociacaoContaReceber.incluir(getIdEntidade());
		NegociacaoContaReceberVO obj = new NegociacaoContaReceberVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>NegociacaoContaReceberVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoContaReceberVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, Boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoContaReceber.incluir(getIdEntidade(), verificarPermissao, usuario);
			NegociacaoContaReceberVO.validarDados(obj);
			validarDadosCalculoValorTotalConfirmacaoNegociacao(obj);
			validarDadosContasNegociadasAReceberVencidas(obj, usuario);
			verificarSituacaoTipoRenegociacao(obj, usuario);
			if (obj.getDescontoProgressivoVO().getCodigo() == null) {
				obj.getDescontoProgressivoVO().setCodigo(0);
			}
			final StringBuilder sql = new StringBuilder("INSERT INTO NegociacaoContaReceber( data, responsavel, unidadeEnsino, tipoPessoa, pessoa, matriculaAluno, ");
			sql.append("funcionario, valor, juro, multa, desconto, valorTotal, centroReceita, contaCorrente, valorEntrada, nrParcela, ");
			sql.append("intervaloParcela, justificativa, descontoProgressivo, parceiro, tipoDesconto, tipoRenegociacao, condicaoRenegociacao, ");
			sql.append("itemCondicaoRenegociacao, liberarRenovacaoAposPagamentoPrimeiraParcela, liberarRenovacaoAposPagamentoTodasParcelas, fornecedor, acrescimo, ");
			sql.append(" dataBaseParcela, tipoIntervaloParcelaEnum, acrescimoGeral, totalAcrescimoPorParcela, acrescimoPorParcela, tipoAcrescimoPorParcela, ");
			sql.append(" permitirRenegociacaoApenasComCondicaoRenegociacao, ");
			sql.append(" responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao, dataLiberacaoRenegociarDesativandoCondicaoRenegociacao, liberarRenegociarDesativandoCondicaoRenegociacao, ");
			sql.append(" respLibUsuarioNaoVinculadoCondRenegRealizarNegociacao, dataLibUsuarioNaoVinculadoCondRenegRealizarNegociacao, liberarUsuarioNaoVinculadoCondRenegRealizarNegociacao, ");
			sql.append(" itemCondicaoDescontoRenegociacao, valorTotalJuro, valorIsencaoTotalJuro, valorIsencaoTotalJuroMaximo, ");
			sql.append(" valorTotalMulta, valorIsencaoTotalMulta, valorIsencaoTotalMultaMaximo, ");
			sql.append(" valorTotalDescontoPerdido, valorConcecaoDescontoPerdido, valorConcecaoDescontoPerdidoMaximo, ");
			sql.append(" pessoaComissionada, permitirPagamentoCartaoCreditoVisaoAluno, valorJuroDesconto, valorMultaDesconto, valorIndiceReajusteDesconto, agenteNegativacaoCobrancaContaReceber ");
			sql.append(" ) VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sql.append("  ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )  ");
			sql.append(" returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getTipoPessoa());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (!obj.getMatriculaAluno().getMatricula().equals("")) {
						sqlInserir.setString(6, obj.getMatriculaAluno().getMatricula());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setDouble(8, obj.getValor().doubleValue());
					sqlInserir.setDouble(9, obj.getJuro().doubleValue());
					sqlInserir.setDouble(10, obj.getMulta().doubleValue());
					sqlInserir.setDouble(11, obj.getDesconto());					
					sqlInserir.setDouble(12, obj.getValorTotal().doubleValue());
					if (obj.getCentroReceita().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getCentroReceita().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
						sqlInserir.setInt(14, obj.getContaCorrente().getCodigo().intValue());
					} else {
						sqlInserir.setNull(14, 0);
					}
					sqlInserir.setDouble(15, obj.getValorEntrada().doubleValue());
					sqlInserir.setInt(16, obj.getNrParcela().intValue());
					sqlInserir.setInt(17, obj.getIntervaloParcela().intValue());
					sqlInserir.setString(18, obj.getJustificativa());
					if (obj.getDescontoProgressivoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(19, obj.getDescontoProgressivoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(19, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(20, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setString(21, obj.getTipoDesconto());
					sqlInserir.setString(22, obj.getTipoRenegociacao());
					if (obj.getCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(23, obj.getCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(23, 0);
					}
					if (obj.getItemCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getItemCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setBoolean(25, obj.getLiberarRenovacaoAposPagamentoPrimeiraParcela());
					sqlInserir.setBoolean(26, obj.getLiberarRenovacaoAposPagamentoTodasParcelas());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(27, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(27, 0);
					}
					sqlInserir.setDouble(28, obj.getAcrescimo());
					if(obj.getTipoIntervaloParcelaEnum().isIntervaloDataBase()){
						sqlInserir.setDate(29, Uteis.getDataJDBC(obj.getDataBaseParcela()));	
					}else{
						sqlInserir.setNull(29, 0);
					}
					sqlInserir.setString(30, obj.getTipoIntervaloParcelaEnum().name());
					sqlInserir.setDouble(31, obj.getAcrescimoGeral());
					sqlInserir.setDouble(32, obj.getTotalAcrescimoPorParcela());
					sqlInserir.setDouble(33, obj.getAcrescimoPorParcela());
					sqlInserir.setString(34, obj.getTipoAcrescimoPorParcela().name());

					sqlInserir.setBoolean(35, obj.getPermitirRenegociacaoApenasComCondicaoRenegociacao());
					if (obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(36, obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(36, 0);
					}
					if (obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setTimestamp(37, Uteis.getDataJDBCTimestamp(new Date()));
					} else {
						sqlInserir.setNull(37, 0);
					}
					sqlInserir.setBoolean(38, obj.getLiberarRenegociarDesativandoCondicaoRenegociacao());
					if (obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(39, obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(39, 0);
					}
					if (obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue() != 0) {
						sqlInserir.setTimestamp(40, Uteis.getDataJDBCTimestamp(new Date()));
					} else {
						sqlInserir.setNull(40, 0);
					}
					sqlInserir.setBoolean(41, obj.getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao());
					int i = 41;
		            Uteis.setValuePreparedStatement(obj.getItemCondicaoDescontoRenegociacaoVO(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorTotalJuro(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalJuro(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalJuroMaximo(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorTotalMulta(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalMulta(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalMultaMaximo(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorTotalDescontoPerdido(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorConcecaoDescontoPerdido(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorConcecaoDescontoPerdidoMaximo(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getPessoaComissionada(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getPermitirPagamentoCartaoCreditoVisaoAluno(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorJuroDesconto(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorMultaDesconto(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getValorIndiceReajusteDesconto(), ++i, sqlInserir);
		            Uteis.setValuePreparedStatement(obj.getAgenteNegativacaoCobrancaContaReceberVO(), ++i, sqlInserir);
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
			getFacadeFactory().getContaReceberNegociadoFacade().incluirContaReceberNegociados(obj, obj.getContaReceberNegociadoVOs(), configuracaoFinanceiro, usuario);
			getFacadeFactory().getNegociacaoContaReceberPlanoDescontoFacade().incluirNegociacaoContaReceberPlanoDesconto(obj, usuario);
			criarNovaContasReceber(obj, configuracaoFinanceiro, usuario);
			verificarParcelaContareceberNegociada(obj, configuracaoFinanceiro, obj.getContaReceberNegociadoVOs(), usuario);
			if(obj.isLiberarIsencaoJuroMultaDescontoAcimaMaximo()){
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.NEGOCIACAO_CONTA_RECEBER, obj.getCodigo().toString(), OperacaoFuncionalidadeEnum.NEGOCIACAO_CONTA_RECEBER_LIBERAR_ISENCAO_JURO_MULTA_DESCONTO, obj.getUsuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo(), ""));	
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			for (ContaReceberNegociadoVO contaReceberNegociadoVO : obj.getContaReceberNegociadoVOs()) {
				contaReceberNegociadoVO.getContaReceber().setSituacao("AR");
				contaReceberNegociadoVO.setCodigo(0);
				contaReceberNegociadoVO.setNovoObj(true);
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarNovaContasReceber(NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		Iterator<ContaReceberVO> i = obj.getNovaContaReceber().iterator();
		int cont = 1;
		String matricula = obj.getMatricula();
		List<String> matriculasAlunosContaReceber = new ArrayList<String>();
		if (matricula.equals("")) {
			matricula = obj.getMatriculaAluno().getMatricula();
		}
		
		if (!Uteis.isAtributoPreenchido(matricula) && obj.getTipoResponsavelFinanceiro()) {
			for (ContaReceberNegociadoVO contaReceberNegociadoVO : obj.getContaReceberNegociadoVOs()) {
				if (Uteis.isAtributoPreenchido(contaReceberNegociadoVO.getContaReceber().getMatriculaAluno().getMatricula()) && !matriculasAlunosContaReceber.contains(contaReceberNegociadoVO.getContaReceber().getMatriculaAluno().getMatricula())) {
					matriculasAlunosContaReceber.add(contaReceberNegociadoVO.getContaReceber().getMatriculaAluno().getMatricula());
				}
			}
			if (matriculasAlunosContaReceber.size() == 1) {
				matricula = matriculasAlunosContaReceber.get(0);
			}
		}
		
		MatriculaPeriodoVO mp = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		
		realizarGeracaoCentroResultadoComBaseNasContasNegociadas(obj, usuario);
		while (i.hasNext()) {
			ContaReceberVO contaReceber = (ContaReceberVO) i.next();
			contaReceber.setTipoBoleto("NCR");
			contaReceber.setTipoOrigem("NCR");
			contaReceber.setSituacao("AR");
			contaReceber.setRenegociacaoContaReceberOrigem(obj);
			contaReceber.setCodOrigem(obj.getCodigo().toString());
			contaReceber.setDescontoProgressivo(obj.getDescontoProgressivoVO());
			if (obj.getTipoFornecedor()) {
				contaReceber.setNrDocumento(obj.getFornecedor().getCodigo().toString() + obj.getCodigo().toString() + cont);
				contaReceber.setFornecedor(obj.getFornecedor());
			} else if (obj.getTipoParceiro()) {
				contaReceber.setNrDocumento(obj.getParceiro().getCodigo().toString() + obj.getCodigo().toString() + cont);
				contaReceber.setParceiroVO(obj.getParceiro());
				contaReceber.setPessoa(obj.getMatriculaAluno().getAluno());
				contaReceber.setMatriculaAluno(obj.getMatriculaAluno());
				contaReceber.setMatriculaPeriodo(mp.getCodigo());
				contaReceber.getTurma().setCodigo(mp.getTurma().getCodigo());
			} else if (obj.getTipoFuncionario()) {
				contaReceber.setNrDocumento(obj.getFuncionario().getPessoa().getCodigo().toString() + obj.getCodigo().toString() + cont);
				contaReceber.setFuncionario(obj.getFuncionario());
			} else if (obj.getTipoResponsavelFinanceiro()) {
				contaReceber.setRenegociacaoContaReceberOrigem(obj);
				contaReceber.setNrDocumento(obj.getPessoa().getCodigo().toString() + obj.getCodigo().toString() + cont);
				contaReceber.setMatriculaAluno(mp.getMatriculaVO());
				contaReceber.setPessoa(mp.getMatriculaVO().getAluno());
				contaReceber.setMatriculaPeriodo(mp.getCodigo());
				contaReceber.getTurma().setCodigo(mp.getTurma().getCodigo());
			} else {
				contaReceber.setRenegociacaoContaReceberOrigem(obj);
				contaReceber.setNrDocumento(obj.getPessoa().getCodigo().toString() + obj.getCodigo().toString() + cont);
				contaReceber.setMatriculaAluno(obj.getMatriculaAluno());
				contaReceber.setMatriculaPeriodo(mp.getCodigo());
				
				contaReceber.getTurma().setCodigo(mp.getTurma().getCodigo());
			}
			
			getFacadeFactory().getContaReceberFacade().incluir(contaReceber, false, configuracaoFinanceiro, usuario);
			contaReceber.atualizarSituacaoContaReceberDeAcordoComDescontos(configuracaoFinanceiro, mp.getData(), usuario);
			if (contaReceber.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				getFacadeFactory().getContaReceberFacade().baixarContaReceberVOConcedendoDescontoTotalAMesma(contaReceber, 
						mp.getData(), "Desconto 100% na parcela.", false, usuario, configuracaoFinanceiro, usuario);
			}
			cont++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarParcelaContareceberNegociada(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<ContaReceberNegociadoVO> listaContaReceberNegociadoVO, UsuarioVO usuarioLogado) throws Exception {

		for (ContaReceberNegociadoVO contaReceberNegociadoVO : listaContaReceberNegociadoVO) {
			if (contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo() > 0 && (contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("MAT") || contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("MEN") || contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("NCR"))) {
				MatriculaPeriodoVO mp = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiro, null);
				Boolean permiteAlterarSituacao = false;
				if (mp.getSituacaoMatriculaPeriodo().equals("PR")) {
					Boolean controlaParcelaMatricula = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarCondicaoPlanoAlunoControlaMatriculaPorMatriculaPeriodo(contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo());
					if (controlaParcelaMatricula == null) {
						permiteAlterarSituacao = true;
					} else if (controlaParcelaMatricula && contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("MAT") && !negociacaoContaReceberVO.getLiberarRenovacaoAposPagamentoPrimeiraParcela() && !negociacaoContaReceberVO.getLiberarRenovacaoAposPagamentoTodasParcelas()) {
						permiteAlterarSituacao = true;
					} else if (!controlaParcelaMatricula && !contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("NCR") && !negociacaoContaReceberVO.getLiberarRenovacaoAposPagamentoPrimeiraParcela() && !negociacaoContaReceberVO.getLiberarRenovacaoAposPagamentoTodasParcelas()) {
						permiteAlterarSituacao = true;
					} else if (contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("NCR")) {
						permiteAlterarSituacao = getFacadeFactory().getNegociacaoContaReceberFacade().realizarValidacaoAtivacaoMatriculaPeriodoParcelaRenegociada(contaReceberNegociadoVO.getContaReceber().getCodigo(), contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo(), controlaParcelaMatricula);
					}

					if (configuracaoFinanceiro.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula().booleanValue() && permiteAlterarSituacao) {
						getFacadeFactory().getMatriculaFacade().alterarSituacaoFinanceiraMatricula(negociacaoContaReceberVO.getMatricula(), "QU");
						mp.setSituacao("AT");
						mp.setSituacaoMatriculaPeriodo("AT");
						getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoFinanceiraMatriculaPeriodoProcessandoAtivacaoDaMesma(negociacaoContaReceberVO.getMatriculaAluno(), mp);

					}
				}
				if (!permiteAlterarSituacao && !mp.getSituacaoMatriculaPeriodo().equals("PR") && !mp.getSituacao().equals("AT") && !mp.getSituacao().equals("CO")) {
					getFacadeFactory().getMatriculaFacade().alterarSituacaoFinanceiraMatricula(negociacaoContaReceberVO.getMatricula(), "QU");
					getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoFinanceiraMatriculaPeriodo(contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo(), "AT");
				}
				break;
			} else if (contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())) {
				getFacadeFactory().getMapaLancamentoFuturoFacade().baixarPendenciaAtravezDeRenegociacaoRecebimento(negociacaoContaReceberVO, contaReceberNegociadoVO, usuarioLogado);
			}
		}

	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>NegociacaoContaReceberVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoContaReceberVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {

			NegociacaoContaReceber.alterar(getIdEntidade(), true, usuario);
			NegociacaoContaReceberVO.validarDados(obj);
			validarDadosCalculoValorTotalConfirmacaoNegociacao(obj);
			validarDadosContasNegociadasAReceberVencidas(obj, usuario);
			verificarSituacaoTipoRenegociacao(obj, usuario);
			final String sql = "UPDATE NegociacaoContaReceber set data=?, responsavel=?, unidadeEnsino=?, tipoPessoa=?, pessoa=?, " + "matriculaAluno=?, funcionario=?, valor=?, juro=?, multa=?, desconto=?, valorTotal=?, centroReceita=?, contaCorrente=?, " + "valorEntrada=?, nrParcela=?, intervaloParcela=?, justificativa=?, descontoProgressivo=?, parceiro=?, tipoDesconto = ?, " + " tipoRenegociacao=?, condicaoRenegociacao = ?, itemCondicaoRenegociacao = ?, liberarRenovacaoAposPagamentoPrimeiraParcela = ?, " + " liberarRenovacaoAposPagamentoTodasParcelas = ?, fornecedor = ?, acrescimo=?, dataBaseParcela=?, tipoIntervaloParcelaEnum=?,"
					+ " permitirRenegociacaoApenasComCondicaoRenegociacao, "
					+ " responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao=?, dataLiberacaoRenegociarDesativandoCondicaoRenegociacao=?, liberarRenegociarDesativandoCondicaoRenegociacao=? "
					+ " respLibUsuarioNaoVinculadoCondRenegRealizarNegociacao=?, dataLibUsuarioNaoVinculadoCondRenegRealizarNegociacao=?, liberarUsuarioNaoVinculadoCondRenegRealizarNegociacao=?, "
					+ " itemCondicaoDescontoRenegociacao=?, valorTotalJuro=?, valorIsencaoTotalJuro=?, valorIsencaoTotalJuroMaximo=?, "
					+ " valorTotalMulta=?, valorIsencaoTotalMulta=?, valorIsencaoTotalMultaMaximo=?, "
					+ " valorTotalDescontoPerdido=?, valorConcecaoDescontoPerdido=?, valorConcecaoDescontoPerdidoMaximo=?, "
					+ " pessoaComissionada=?, permitirPagamentoCartaoCreditoVisaoAluno = ?, valorJuroDesconto = ?, valorMultaDesconto= ?, valorIndiceReajusteDesconto= ? "
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getTipoPessoa());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (!obj.getMatriculaAluno().getMatricula().equals("")) {
						sqlAlterar.setString(6, obj.getMatriculaAluno().getMatricula());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setDouble(8, obj.getValor().doubleValue());
					sqlAlterar.setDouble(9, obj.getJuro().doubleValue());
					sqlAlterar.setDouble(10, obj.getMulta().doubleValue());
					sqlAlterar.setDouble(11, obj.getDesconto().doubleValue());
					sqlAlterar.setDouble(12, obj.getValorTotal().doubleValue());
					if (obj.getCentroReceita().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(13, obj.getCentroReceita().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(13, 0);
					}
					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(14, obj.getContaCorrente().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(14, 0);
					}
					sqlAlterar.setDouble(15, obj.getValorEntrada().doubleValue());
					sqlAlterar.setInt(16, obj.getNrParcela().intValue());
					sqlAlterar.setInt(17, obj.getIntervaloParcela().intValue());
					sqlAlterar.setString(18, obj.getJustificativa());
					if (obj.getDescontoProgressivoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(19, obj.getDescontoProgressivoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(19, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(20, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(20, 0);
					}
					sqlAlterar.setString(21, obj.getTipoDesconto());
					sqlAlterar.setString(22, obj.getTipoRenegociacao());
					if (obj.getCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(23, obj.getCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					if (obj.getItemCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(24, obj.getItemCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(24, 0);
					}
					sqlAlterar.setBoolean(25, obj.getLiberarRenovacaoAposPagamentoPrimeiraParcela());
					sqlAlterar.setBoolean(26, obj.getLiberarRenovacaoAposPagamentoTodasParcelas());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(27, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(27, 0);
					}
					sqlAlterar.setDouble(28, obj.getAcrescimo());
					if(obj.getTipoIntervaloParcelaEnum().isIntervaloDataBase()){
						sqlAlterar.setDate(29, Uteis.getDataJDBC(obj.getDataBaseParcela()));	
					}else{
						sqlAlterar.setNull(29, 0);
					}
					sqlAlterar.setString(30, obj.getTipoIntervaloParcelaEnum().name());
					
					sqlAlterar.setBoolean(35, obj.getPermitirRenegociacaoApenasComCondicaoRenegociacao());
					if (obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(36, obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(36, 0);
					}
					if (obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setTimestamp(37, Uteis.getDataJDBCTimestamp(new Date()));
					} else {
						sqlAlterar.setNull(37, 0);
					}
					sqlAlterar.setBoolean(38, obj.getLiberarRenegociarDesativandoCondicaoRenegociacao());
					if (obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(39, obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(39, 0);
					}
					if (obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().getCodigo().intValue() != 0) {
						sqlAlterar.setTimestamp(40, Uteis.getDataJDBCTimestamp(new Date()));
					} else {
						sqlAlterar.setNull(40, 0);
					}
					sqlAlterar.setBoolean(41, obj.getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao());
					int i = 41;
		            Uteis.setValuePreparedStatement(obj.getItemCondicaoDescontoRenegociacaoVO(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorTotalJuro(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalJuro(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalJuroMaximo(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorTotalMulta(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalMulta(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorIsencaoTotalMultaMaximo(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorTotalDescontoPerdido(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorConcecaoDescontoPerdido(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorConcecaoDescontoPerdidoMaximo(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getPessoaComissionada(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getPermitirPagamentoCartaoCreditoVisaoAluno(), ++i, sqlAlterar);		            
		            Uteis.setValuePreparedStatement(obj.getValorJuroDesconto(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorMultaDesconto(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getValorIndiceReajusteDesconto(), ++i, sqlAlterar);
		            Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
			new ContaReceberNegociado().alterarContaReceberNegociados(obj, obj.getContaReceberNegociadoVOs(), configuracaoFinanceiro, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>NegociacaoContaReceberVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoContaReceberVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiro, Boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoContaReceber.excluir(getIdEntidade(), verificarPermissao, usuario);
			String sql = "DELETE FROM NegociacaoContaReceber WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			for (ContaReceberVO contaReceberVO : contaReceberVOs) {
				getFacadeFactory().getContaReceberFacade().excluir(contaReceberVO, configuracaoFinanceiro, verificarPermissao, usuario);
			}
			for (ContaReceberNegociadoVO contaReceberNegociadoVO : obj.getContaReceberNegociadoVOs()) {
				if (!contaReceberNegociadoVO.getContaReceber().getSituacao().equals("RE")) {
					getFacadeFactory().getContaReceberFacade().alterarValoresDaContaReceberPorEstorno(contaReceberNegociadoVO.getContaReceber().getCodigo(), "NE", configuracaoFinanceiro, obj.getBloqueioPorFechamentoMesLiberado(), usuario);
					if (contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())) {
						executarCriacaoPendenciaChequeDevolvidoEstornoRenegociacaoReceber(obj, contaReceberNegociadoVO.getContaReceber().getCodOrigem(), usuario);
					}
					getFacadeFactory().getMatriculaPeriodoFacade().realizarEstornoSituacaoMatriculaPeriodoAoEstornarContaReceber(contaReceberNegociadoVO.getContaReceber(), configuracaoFinanceiro, usuario);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarCriacaoPendenciaChequeDevolvidoEstornoRenegociacaoReceber(NegociacaoContaReceberVO obj, String codOrigem, UsuarioVO usuario) throws Exception {
		Integer devolucaoCheque = Integer.valueOf(codOrigem);
		DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(devolucaoCheque, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		devolucaoChequeVO.setContaCaixa(obj.getContaCorrenteCaixaEstorno());
		devolucaoChequeVO.getCheque().setPago(false);
		getFacadeFactory().getDevolucaoChequeFacade().criarPendenciaChequeDevolvido(devolucaoChequeVO, usuario);
		getFacadeFactory().getDevolucaoChequeFacade().movimentacaoCaixa(devolucaoChequeVO, usuario);
	}

	public void verificarPermissaoExcluirNegociacao(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs, UsuarioVO usuario) throws Exception {
		// Comentado para atender o chamado da PROCESSUS de N° 5686, onde não
		// conseguia estornar uma renegociação onde as contas a receber havia
		// sido
		// excluidas.
		// if (contaReceberVOs.isEmpty()) {
		// throw new
		// Exception("Não existem contas a receber vinculadas a essa negociação, portanto não é permitido exclui-la.");
		// }
		if (obj.isExisteContaReceberTipoOrigemCheque() && !Uteis.isAtributoPreenchido(obj.getContaCorrenteCaixaEstorno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ContaReceber_estornoRenegociacaoContaCaixa"));
		}
		if (obj.isExisteContaReceberTipoOrigemCheque() && Uteis.isAtributoPreenchido(obj.getContaCorrenteCaixaEstorno())) {
			obj.setContaCorrenteCaixaEstorno(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteCaixaEstorno().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			FluxoCaixaVO fluxoCaixa = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), obj.getContaCorrenteCaixaEstorno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (fluxoCaixa == null) {
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + obj.getContaCorrenteCaixaEstorno().getNumero() + "-" + obj.getContaCorrenteCaixaEstorno().getDigito() + ")");
			}
		}
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor()) || contaReceberVO.getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())) {
				throw new Exception("Já existem contas pagas/negociadas dessa negociação, portanto não é permitido exclui-la.");
			}
		}
	}

	@Override
	public Boolean verificarPermissaoExcluirNegociacaoThread(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs) throws Exception {
		Boolean retorno = Boolean.FALSE;
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor()) || contaReceberVO.getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor()) || contaReceberVO.getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
				retorno = Boolean.TRUE;
			}
		}
		return retorno;
	}

	/**
	 * Responsável por realizar uma consulta de <code>NegociacaoContaReceber</code> através do valor do atributo <code>matricula</code> da classe <code>Matricula</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>NegociacaoContaReceberVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<NegociacaoContaReceberVO> consultarPorMatriculaMatricula(String valorConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoContaReceber.* FROM NegociacaoContaReceber, Matricula WHERE NegociacaoContaReceber.matriculaAluno = Matricula.matricula and upper( Matricula.matricula ) like('" + valorConsulta.toUpperCase() + "%')";
		if (dataIni != null) {
			sqlStr += " AND NegociacaoContaReceber.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		}
		if (dataFim != null) {
			sqlStr += " AND NegociacaoContaReceber.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		}
		sqlStr += "ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	@Override
	public List<NegociacaoContaReceberVO> consultarPorCodigoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoContaReceber.* FROM NegociacaoContaReceber, Pessoa WHERE NegociacaoContaReceber.pessoa = Pessoa.codigo and Pessoa.codigo = " + codigoResponsavelFinanceiro;
		if (dataIni != null) {
			sqlStr += " AND NegociacaoContaReceber.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		}
		if (dataFim != null) {
			sqlStr += " AND NegociacaoContaReceber.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		}
		sqlStr += "ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Date consultaDataNegociacaoContaReceberPorContaReceberUnica(Integer contaReceber,  UsuarioVO usuario) throws Exception {
		String sql = "";
		sql = "select negociacaocontareceber.data from negociacaocontareceber " 
		+ " inner join contarecebernegociado  on contarecebernegociado.negociacaocontareceber = negociacaocontareceber.codigo "
		+ " where contarecebernegociado.contareceber = " + contaReceber + "  ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return tabelaResultado.getDate("data");
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>NegociacaoContaReceberVO</code> resultantes da consulta.
	 */
	public static List<NegociacaoContaReceberVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<NegociacaoContaReceberVO> vetResultado = new ArrayList<NegociacaoContaReceberVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>NegociacaoContaReceberVO</code>.
	 * 
	 * @return O objeto da classe <code>NegociacaoContaReceberVO</code> com os dados devidamente montados.
	 */
	public static NegociacaoContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		NegociacaoContaReceberVO obj = new NegociacaoContaReceberVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setTipoRenegociacao(dadosSQL.getString("tipoRenegociacao"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setJuro(dadosSQL.getDouble("juro"));
		obj.setMulta(dadosSQL.getDouble("multa"));
		obj.setValorMultaDesconto(dadosSQL.getDouble("valorMultaDesconto"));
		obj.setValorJuroDesconto(dadosSQL.getDouble("valorJuroDesconto"));
		obj.setValorIndiceReajusteDesconto(dadosSQL.getDouble("valorIndiceReajusteDesconto"));
		obj.setNrParcela(dadosSQL.getInt("nrParcela"));
		obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
		obj.setValorEntrada(dadosSQL.getDouble("valorEntrada"));
		obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaAluno"));
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
		obj.getFornecedor().setCodigo(dadosSQL.getInt("fornecedor"));
		obj.setTipoDesconto(dadosSQL.getString("tipoDesconto"));
		obj.setAcrescimoGeral(dadosSQL.getDouble("acrescimoGeral"));
		obj.setTotalAcrescimoPorParcela(dadosSQL.getDouble("totalAcrescimoPorParcela"));
		obj.setAcrescimoPorParcela(dadosSQL.getDouble("acrescimoPorParcela"));
		obj.setTipoAcrescimoPorParcela(TipoAcrescimoEnum.valueOf(dadosSQL.getString("tipoAcrescimoPorParcela")));
		obj.setPermitirPagamentoCartaoCreditoVisaoAluno(dadosSQL.getBoolean("permitirPagamentoCartaoCreditoVisaoAluno"));
		obj.getAgenteNegativacaoCobrancaContaReceberVO().setCodigo(dadosSQL.getInt("agenteNegativacaoCobrancaContaReceber"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			// montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
			montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
			montarDadosPessoa(obj, nivelMontarDados, usuario);
			montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosParceiro(obj, nivelMontarDados, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			obj.setPessoaComissionada(Uteis.montarDadosVO(dadosSQL.getInt("pessoacomissionada"), PessoaVO.class, p -> getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario)));
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosParceiro(obj, nivelMontarDados, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosParceiro(obj, nivelMontarDados, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		obj.getCondicaoRenegociacao().setCodigo(dadosSQL.getInt("condicaoRenegociacao"));
		obj.getItemCondicaoRenegociacao().setCodigo(dadosSQL.getInt("itemCondicaoRenegociacao"));
		obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));				
		obj.getCentroReceita().setCodigo(dadosSQL.getInt("centroReceita"));
		obj.getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente"));
		obj.getDescontoProgressivoVO().setCodigo(dadosSQL.getInt("descontoProgressivo"));
		obj.setIntervaloParcela(dadosSQL.getInt("intervaloParcela"));
		obj.setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum.valueOf(dadosSQL.getString("tipoIntervaloParcelaenum")));
		obj.setDataBaseParcela(dadosSQL.getDate("dataBaseParcela"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setLiberarRenovacaoAposPagamentoPrimeiraParcela(dadosSQL.getBoolean("liberarRenovacaoAposPagamentoPrimeiraParcela"));
		obj.setLiberarRenovacaoAposPagamentoTodasParcelas(dadosSQL.getBoolean("liberarRenovacaoAposPagamentoTodasParcelas"));
		obj.setPermitirRenegociacaoApenasComCondicaoRenegociacao(dadosSQL.getBoolean("permitirRenegociacaoApenasComCondicaoRenegociacao"));
		obj.getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao().setCodigo(dadosSQL.getInt("responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao"));
		obj.setDataLiberacaoRenegociarDesativandoCondicaoRenegociacao(dadosSQL.getTimestamp("dataLiberacaoRenegociarDesativandoCondicaoRenegociacao"));
		obj.setLiberarRenegociarDesativandoCondicaoRenegociacao(dadosSQL.getBoolean("liberarRenegociarDesativandoCondicaoRenegociacao"));
		obj.getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao().setCodigo(dadosSQL.getInt("respLibUsuarioNaoVinculadoCondRenegRealizarNegociacao"));
		obj.setDataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(dadosSQL.getTimestamp("dataLibUsuarioNaoVinculadoCondRenegRealizarNegociacao"));
		obj.setLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(dadosSQL.getBoolean("liberarUsuarioNaoVinculadoCondRenegRealizarNegociacao"));
		obj.getPessoaComissionada().setCodigo((dadosSQL.getInt("pessoacomissionada")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}		
		obj.getItemCondicaoDescontoRenegociacaoVO().setCodigo((dadosSQL.getInt("itemCondicaoDescontoRenegociacao")));
		obj.setValorTotalJuro(dadosSQL.getDouble("valortotaljuro"));
		obj.setValorIsencaoTotalJuro(dadosSQL.getDouble("valorisencaototaljuro"));
		obj.setValorIsencaoTotalJuroMaximo(dadosSQL.getDouble("valorisencaototaljuromaximo"));
		obj.setValorTotalMulta(dadosSQL.getDouble("valortotalmulta"));
		obj.setValorIsencaoTotalMulta(dadosSQL.getDouble("valorisencaototalmulta"));
		obj.setValorIsencaoTotalMultaMaximo(dadosSQL.getDouble("valorisencaototalmultamaximo"));
		obj.setValorTotalDescontoPerdido(dadosSQL.getDouble("valortotaldescontoperdido"));
		obj.setValorConcecaoDescontoPerdido(dadosSQL.getDouble("valorconcecaodescontoperdido"));
		obj.setValorConcecaoDescontoPerdidoMaximo(dadosSQL.getDouble("valorconcecaodescontoperdidomaximo"));
		obj.setNegociacaoContaReceberPlanoDescontoVOs(getFacadeFactory().getNegociacaoContaReceberPlanoDescontoFacade().consultarPorNegociacaoContaReceber(obj.getCodigo(), usuario));
		obj.setItemCondicaoDescontoRenegociacaoVO(Uteis.montarDadosVO(dadosSQL.getInt("itemCondicaoDescontoRenegociacao"), ItemCondicaoDescontoRenegociacaoVO.class, p -> getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		obj.setPessoaComissionada(Uteis.montarDadosVO(dadosSQL.getInt("pessoacomissionada"), PessoaVO.class, p -> getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		obj.setContaReceberNegociadoVOs(getFacadeFactory().getContaReceberNegociadoFacade().consultarContaReceberNegociados(obj, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosFuncionario(obj, nivelMontarDados, usuario);
		montarDadosCentroReceita(obj, nivelMontarDados, usuario);
		montarDadosContaCorrente(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaCorrente(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrente().getCodigo().intValue() == 0) {
			obj.setContaCorrente(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CentroReceitaVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>CentroReceitaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCentroReceita(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCentroReceita().getCodigo().intValue() == 0) {
			obj.setCentroReceita(new CentroReceitaVO());
			return;
		}
		obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFuncionario(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatriculaAluno(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatriculaAluno().getMatricula() == null) || (obj.getMatriculaAluno().getMatricula().equals(""))) {
			obj.setMatriculaAluno(new MatriculaVO());
			return;
		}
		obj.setMatriculaAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaAluno().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoa(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosParceiro(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(NegociacaoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>NegociacaoContaReceberVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public NegociacaoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM NegociacaoContaReceber WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoContaReceber ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}
	
	
	public Boolean permiteReceberCartaoCreditoVisaoAluno(String codOrigem, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT permitirPagamentoCartaoCreditoVisaoAluno FROM NegociacaoContaReceber WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Integer.valueOf(codOrigem) });
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("permitirPagamentoCartaoCreditoVisaoAluno");
		}
		return false;
	}
	
	public void alterarPermitirPagamentoCartaoCredito(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuarioLogado) throws Exception {
		alterar(negociacaoContaReceberVO, "negociacaoContaReceber",
				new AtributoPersistencia().add("permitirPagamentoCartaoCreditoVisaoAluno", negociacaoContaReceberVO.getPermitirPagamentoCartaoCreditoVisaoAluno()),
				new AtributoPersistencia().add("codigo", negociacaoContaReceberVO.getCodigo()), usuarioLogado);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return NegociacaoContaReceber.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NegociacaoContaReceber.idEntidade = idEntidade;
	}

	public Integer consultarNumeroDeRecebimentosParaUmaMatricula(String matricula) throws Exception {
		String sqlStr = "SELECT count(codigo) FROM negociacaocontareceber WHERE matriculaaluno = ?";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matricula });
			if (!tabelaResultado.next()) {
				return 0;
			} else {
				return tabelaResultado.getInt("count");
			}
		} finally {
			sqlStr = null;
		}
	}
	
	public Boolean validarPermissaoNegociacarParcelaNegociadaNaoCumprida(ContaReceberVO obj, UsuarioVO usuarioVO) throws Exception {
		boolean existeParecelaPaga =  getFacadeFactory().getContaReceberFacade().verificarExisteContaReceberPorCodOrigemTipoOrigemSituacaoNegociacaoContaReceber(obj.getCodOrigem(), TipoOrigemContaReceber.NEGOCIACAO.getValor(), SituacaoContaReceber.RECEBIDO.getValor());
		boolean isPermissaoNegociacaoParcelasNegociasNaoCumprida = verificarPermissaoNegociacaoParcelasNegociacaoNaoCumprida(usuarioVO);
		if(!existeParecelaPaga && !isPermissaoNegociacaoParcelasNegociasNaoCumprida) {
			return false;
		}else {
			return true;
		}
	}
	
	 private boolean verificarPermissaoNegociacaoParcelasNegociacaoNaoCumprida(UsuarioVO usuarioLogado) {
	    	try {
	    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_NEGOCIAR_PARCELAS_NEGOCIACAO_NAO_CUMPRIDA, usuarioLogado);
	    		return true;
			} catch (Exception e) {
				return false;
			}
	  }
		
	public Boolean consultarExistenciaParcelasPaga(String codigoOrigem) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT negociacaocontareceber.codigo from ");
		sqlStr.append(" negociacaocontareceber ");
		sqlStr.append(" inner join contareceber on  negociacaocontareceber.codigo = contareceber.codorigem::int  ");
		sqlStr.append("	WHERE contareceber.situacao = 'RE' ");
		sqlStr.append("	and contareceber.tipoorigem = 'NCR' ");
		sqlStr.append(" and negociacaocontareceber.codigo = ? ");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Integer.valueOf(codigoOrigem) });
			if (!tabelaResultado.next()) {
				return false;
			} else {
				return true;
			}
		} finally {
			sqlStr = null;
		}
	}


	public Double calcularValorTotalConfirmacaoNegociacao(NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		Double valorTotalTemp = 0.0;
		//tem que ser o campo valor da conta receber pois quando o mesmo tem algum tipo de pano de desconto nao pode considerar o valor
		for (ContaReceberVO contaReceberVO : obj.getNovaContaReceber()) {
			contaReceberVO.setValorBaseContaReceber(contaReceberVO.getValor());
			contaReceberVO.getCalcularValorFinal(new Date(), configuracaoFinanceiroVO, false, new Date(), usuarioVO);			
			valorTotalTemp = valorTotalTemp + contaReceberVO.getValor();
		}
		return Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp);
	}

	public void validarDadosCalculoValorTotalConfirmacaoNegociacao(NegociacaoContaReceberVO obj) throws Exception {
		Double valorTotalTemp = 0.0;
		obj.calcularValorTotal();
		for (ContaReceberVO contaReceberVO : obj.getNovaContaReceber()) {
			valorTotalTemp = valorTotalTemp + contaReceberVO.getValor();
		}
		if (!Double.valueOf(Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotal())).equals(Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp))) {
			throw new Exception("A soma das parcelas negociadas estão diferentes do valor total.");
		}
	}

	public void validarDadosContasNegociadasAReceberVencidas(NegociacaoContaReceberVO obj, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RealizarNegociacaoContaReceberAReceberVencida", usuario);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (obj.getContaReceberNegociadoVOs().isEmpty()) {
			return;
		}
		boolean contavencida = false;
		boolean contaavencer = false;
		for (ContaReceberNegociadoVO contaReceberVO : obj.getContaReceberNegociadoVOs()) {
			if (contaReceberVO.getContaReceber().getDataVencimento().after(new Date())) {
				contaavencer = true;
			} else {
				contavencida = true;
			}
		}
		if (contaavencer && contavencida) {
			throw new Exception("Não é permitido realizar para um mesma renegociação de conta a receber, renegociação de contas A RECEBER (Contas com data de vencimento posterior a data atual)e contas VENCIDAS (Contas com data de vencimento anterior a data atual). Caso seja necessário realize duas renegociações!");
		}
	}

	public void verificarSituacaoTipoRenegociacao(NegociacaoContaReceberVO obj, UsuarioVO usuario) throws Exception {
		boolean contavencida = false;
		boolean contaavencer = false;
		for (ContaReceberNegociadoVO contaReceberVO : obj.getContaReceberNegociadoVOs()) {
			if (contaReceberVO.getContaReceber().getDataVencimento().after(new Date())) {
				contaavencer = true;
			} else {
				contavencida = true;
			}
		}
		if (contaavencer && contavencida) {
			obj.setTipoRenegociacao("");
		} else if (contaavencer) {
			obj.setTipoRenegociacao("AV");
		} else if (contavencida) {
			obj.setTipoRenegociacao("VE");
		}
	}

	@Override
	public void realizarInicializacaoDadosRenegociacaoContaReceber(NegociacaoContaReceberVO negociacaoContaReceberVO, MatriculaVO matriculaVO, TipoPessoa tipoPessoa, UsuarioVO usuarioLogado) throws Exception {
		negociacaoContaReceberVO.setData(new Date());
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			negociacaoContaReceberVO.setMatriculaAluno(matriculaVO);
			negociacaoContaReceberVO.setPessoa(matriculaVO.getAluno());
			negociacaoContaReceberVO.setTipoPessoa("AL");
		} else {
			negociacaoContaReceberVO.setTipoPessoa("RF");
			negociacaoContaReceberVO.setPessoa(usuarioLogado.getPessoa());
		}
		negociacaoContaReceberVO.setTipoRenegociacao("VE");
		negociacaoContaReceberVO.setResponsavel(usuarioLogado);
		negociacaoContaReceberVO.setTipoDesconto("VA");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realizarInicializacaoDadosOpcaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, List<ContaReceberVO> contaReceberVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception {

		Integer perfilEconomico = negociacaoContaReceberVO.getPessoa().getPerfilEconomico().getCodigo();
		try {
			if (perfilEconomico == null || perfilEconomico == 0) {
				perfilEconomico = configuracaoGeralSistemaVO.getPerfilEconomicoPadrao().getCodigo();
			}
			if (!visaoAdministrativa) {
				adicionarContaReceberRenegociacaoAluno(negociacaoContaReceberVO, contaReceberVOs);
			}
			if (negociacaoContaReceberVO.getContaReceberNegociadoVOs().isEmpty()) {
				throw new Exception("Não foi encontrada nenhuma conta a receber para negociação. Favor selecione ao menos uma conta para prosseguir com a operação.");
			}
			negociacaoContaReceberVO.setDesconto(0.0);
			negociacaoContaReceberVO.setAcrescimoGeral(0.0);
			negociacaoContaReceberVO.setValorAcrescimoCondicaoRenegociacao(0.0);
			negociacaoContaReceberVO.setValorDescontoCondicaoRenegociacao(0.0);
			negociacaoContaReceberVO.setValorMultaDesconto(0.0);
			negociacaoContaReceberVO.setValorJuroDesconto(0.0);
			negociacaoContaReceberVO.setValorIndiceReajusteDesconto(0.0);
			negociacaoContaReceberVO.setNrParcela(0);
			negociacaoContaReceberVO.setValorEntrada(0.0);
			Long nrDiasAtraso =  0L;
			OptionalLong max = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream().mapToLong(p -> p.getNrDiasAtraso()).max();
			if (max.isPresent() && max.getAsLong() > 0L) {
				nrDiasAtraso =max.getAsLong();
			}
			if (negociacaoContaReceberVO.getTipoAluno() && !Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getCondicaoRenegociacao())) {
				negociacaoContaReceberVO.setCondicaoRenegociacao(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarCondicaoRenegociacaoDisponivelAluno(perfilEconomico, negociacaoContaReceberVO.getMatriculaAluno().getUltimoMatriculaPeriodoVO().getTurma().getCodigo(), negociacaoContaReceberVO.getMatriculaAluno().getCurso().getCodigo(), negociacaoContaReceberVO.getMatriculaAluno().getTurno().getCodigo(), negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), negociacaoContaReceberVO.getValor(), nrDiasAtraso, visaoAdministrativa, usuarioLogado));
			} else if(!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getCondicaoRenegociacao())){
				negociacaoContaReceberVO.setCondicaoRenegociacao(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarCondicaoRenegociacaoDisponivelAluno(perfilEconomico, null, null, null, negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), negociacaoContaReceberVO.getValor(), nrDiasAtraso, visaoAdministrativa, usuarioLogado));
			}else if(Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getCondicaoRenegociacao())) {
				negociacaoContaReceberVO.getCondicaoRenegociacao().setItemCondicaoRenegociacaoVOs(ItemCondicaoRenegociacao.consultarItemCondicaoRenegociacaos(negociacaoContaReceberVO.getCondicaoRenegociacao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
				negociacaoContaReceberVO.getCondicaoRenegociacao().setListaCondicaoRenegociacaoFuncionarioVOs(getFacadeFactory().getCondicaoRenegociacaoFuncionarioCargoFacade().consultarCondicaoRenegociacaoFuncionarioPorCondicaoRenegociacao(negociacaoContaReceberVO.getCondicaoRenegociacao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			}
			montarOpcaoAlunoCondicaoRenegociacaoComBaseItemCondicaoRenegociacao(negociacaoContaReceberVO, visaoAdministrativa, usuarioLogado, nrDiasAtraso);
			if (!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getCondicaoRenegociacao())) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				throw new Exception(UteisJSF.internacionalizar("msg_nao_existe_condicao_renegociacao").replace("NOME_ALUNO", negociacaoContaReceberVO.getPessoa().getNome().toUpperCase()));
			}
			negociacaoContaReceberVO.setLiberarRenovacaoAposPagamentoPrimeiraParcela(negociacaoContaReceberVO.getCondicaoRenegociacao().getLiberarRenovacaoAposPagamentoPrimeiraParcela());
			negociacaoContaReceberVO.setLiberarRenovacaoAposPagamentoTodasParcelas(negociacaoContaReceberVO.getCondicaoRenegociacao().getLiberarRenovacaoAposPagamentoTodasParcelas());
			negociacaoContaReceberVO.setPermitirPagamentoCartaoCreditoVisaoAluno(negociacaoContaReceberVO.getCondicaoRenegociacao().getPermitirPagamentoCartaoCreditoVisaoAluno());

			montarContaCorrente(negociacaoContaReceberVO, visaoAdministrativa, usuarioLogado);

			if (negociacaoContaReceberVO.getCondicaoRenegociacao().getDescontoProgressivo().getCodigo() > 0) {
				negociacaoContaReceberVO.setDescontoProgressivoVO(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getCondicaoRenegociacao().getDescontoProgressivo().getCodigo(), usuarioLogado));
			}
			if (negociacaoContaReceberVO.getCondicaoRenegociacao().getGrupoDestinatario().getCodigo() > 0) {
				negociacaoContaReceberVO.setGrupoDestinatariosVO(getFacadeFactory().getGrupoDestinatariosFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getCondicaoRenegociacao().getGrupoDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			}
			
			if(negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_nao_existe_condicao_renegociacao").replace("NOME_ALUNO", negociacaoContaReceberVO.getPessoa().getNome().toUpperCase()));
			}
			realizarAtualizacaoOpcoesPagamentoConformeItemCondicaoNegociacao(negociacaoContaReceberVO);
			} catch (Exception e) {
				throw e;
			}
		}
		
		@Override
		public void realizarAtualizacaoOpcoesPagamentoConformeItemCondicaoNegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO) throws Exception {
			try {
				if(Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getItemCondicaoRenegociacao())) {
					ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoSubstituir = new ItemCondicaoRenegociacaoVO();			
					for(ItemCondicaoRenegociacaoVO item : negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs()) {
						if (item.getCodigo().equals(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getCodigo())) {
							itemCondicaoRenegociacaoSubstituir = (ItemCondicaoRenegociacaoVO) Uteis.clonar(item);
							break;
						}
					}
					negociacaoContaReceberVO.setItemCondicaoRenegociacao(itemCondicaoRenegociacaoSubstituir);
			}else {
				negociacaoContaReceberVO.setItemCondicaoRenegociacao((ItemCondicaoRenegociacaoVO) Uteis.clonar(negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs().get(0)));
			}			
			realizarCalculoAcrecimoDescontoCondicaoNegociacao(negociacaoContaReceberVO);
			if(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()) < negociacaoContaReceberVO.getValorMinimoEntrada()) {
				negociacaoContaReceberVO.setValorEntrada(negociacaoContaReceberVO.getValorMinimoEntrada());
			}else if(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()) > negociacaoContaReceberVO.getValorMaximoEntrada()) {
				negociacaoContaReceberVO.setValorEntrada(negociacaoContaReceberVO.getValorMaximoEntrada());
			}
			realizarDefinicaoPrimeiroVencimentoParcela(negociacaoContaReceberVO);			
			negociacaoContaReceberVO.getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs().clear();
	    	negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().clear();
	    	negociacaoContaReceberVO.getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs().addAll(realizarCriacaoOpcaoAlunoCondicaoRenegociacaoComBaseItemCondicaoRenegociacao(negociacaoContaReceberVO.getValorTotal(), negociacaoContaReceberVO.getValorEntrada(), negociacaoContaReceberVO.getDataBaseParcela(), negociacaoContaReceberVO.getCondicaoRenegociacao(), negociacaoContaReceberVO.getItemCondicaoRenegociacao()));
	    	negociacaoContaReceberVO.setOpcaoAlunoCondicaoRenegociacaoVOs(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs());	    		    	
	    	if(!negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().isEmpty() && Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal()) != (Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()))) {
	    		if(!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getNrParcela()) || !negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().stream().anyMatch(t -> t.getNumeroParcela().equals(negociacaoContaReceberVO.getNrParcela()))) {	    			
	    			negociacaoContaReceberVO.setNrParcela(negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().get(0).getNumeroParcela());
	    		}
	    	}else if(negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().isEmpty() && negociacaoContaReceberVO.getItemCondicaoRenegociacao().getValorMinimoPorParcela().doubleValue() > 0.0 && Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal()) != (Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()))) {	    		
	    		if(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getFaixaEntradaFinal() > 0.0 && negociacaoContaReceberVO.getValorEntrada() > 0.0) {
	    			throw new Exception("O valor mínimo para a parcela é de R$ "+Uteis.getDoubleFormatado(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getValorMinimoPorParcela().doubleValue())+", portanto informe um valor diferente para a entrada.");
	    		}else {
	    			throw new Exception("O valor mínimo para a parcela é de R$ "+Uteis.getDoubleFormatado(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getValorMinimoPorParcela().doubleValue())+", altere a condição de negociação que atenda as regras da sua negociação, caso não encontre procure o departamento financeiro.");
	    		}
	    	}else {
	    		negociacaoContaReceberVO.setNrParcela(0);				
	    	}						
	    	Ordenacao.ordenarLista(negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs(), "numeroParcela");
		} catch (Exception e) {
			throw e;
		}
	}
		
	private void realizarCalculoAcrecimoDescontoCondicaoNegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		negociacaoContaReceberVO.setDesconto(0.0);
		negociacaoContaReceberVO.setAcrescimoGeral(0.0);						
		negociacaoContaReceberVO.setValorDescontoCondicaoRenegociacao(0.0);
		negociacaoContaReceberVO.setValorAcrescimoCondicaoRenegociacao(0.0);
		
		realizarIsencoesJurosMultaIndiceReajustePorItemCondicaoNegociacao(negociacaoContaReceberVO);
		negociacaoContaReceberVO.setTipoDesconto("VA");
		negociacaoContaReceberVO.setDesconto(negociacaoContaReceberVO.getValorDescontoIsencaoJuroMultaIndiceReajuste());
		negociacaoContaReceberVO.calcularValorTotal();
		Double valorBaseCalculo = negociacaoContaReceberVO.getValorTotal();
		valorBaseCalculo = Uteis.arrendondarForcando2CadasDecimais((valorBaseCalculo + ((valorBaseCalculo * negociacaoContaReceberVO.getCondicaoRenegociacao().getJuroEspecifico()) / 100) 
				- ((valorBaseCalculo * negociacaoContaReceberVO.getCondicaoRenegociacao().getDescontoEspecifico()) / 100)) + 
				((valorBaseCalculo * negociacaoContaReceberVO.getItemCondicaoRenegociacao().getJuro()) / 100) - ((valorBaseCalculo * negociacaoContaReceberVO.getItemCondicaoRenegociacao().getDesconto()) / 100));
		if (negociacaoContaReceberVO.getValorTotal() > valorBaseCalculo) {
			negociacaoContaReceberVO.setValorDescontoCondicaoRenegociacao(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal() - valorBaseCalculo));							
		} else if (negociacaoContaReceberVO.getValorTotal() < valorBaseCalculo) {
			negociacaoContaReceberVO.setValorAcrescimoCondicaoRenegociacao(Uteis.arrendondarForcando2CadasDecimais(valorBaseCalculo - negociacaoContaReceberVO.getValorTotal()));			
		}
		negociacaoContaReceberVO.setDesconto(negociacaoContaReceberVO.getDesconto() + negociacaoContaReceberVO.getValorDescontoCondicaoRenegociacao());
		negociacaoContaReceberVO.setAcrescimoGeral(negociacaoContaReceberVO.getAcrescimoGeral() + negociacaoContaReceberVO.getValorAcrescimoCondicaoRenegociacao());
		negociacaoContaReceberVO.calcularValorTotal();
	}
	private void realizarDefinicaoPrimeiroVencimentoParcela(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		negociacaoContaReceberVO.setDataVencimentoEntrada(null);
		if(negociacaoContaReceberVO.getValorEntrada() > 0.0 
				&& negociacaoContaReceberVO.getItemCondicaoRenegociacao().getDefinirNumeroDiasVencimentoPrimeiraParcela()) {
				negociacaoContaReceberVO.setDataBaseParcela(Uteis.obterDataAvancada(negociacaoContaReceberVO.getDataVencimentoEntrada(), negociacaoContaReceberVO.getItemCondicaoRenegociacao().getNumeroDiasAposVencimentoEntrada()));
			}else if(negociacaoContaReceberVO.getValorEntrada() > 0.0 
					&& negociacaoContaReceberVO.getItemCondicaoRenegociacao().getGerarParcelas30DiasAposDataEntrada()) {
				negociacaoContaReceberVO.setDataBaseParcela(Uteis.obterDataAvancada(negociacaoContaReceberVO.getDataVencimentoEntrada(), 30));
			}else  {
				negociacaoContaReceberVO.setDataBaseParcela(Uteis.obterDataAvancada(new Date(), 30));
			}
	}

	private void montarOpcaoAlunoCondicaoRenegociacaoComBaseItemCondicaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO,
			Boolean visaoAdministrativa, UsuarioVO usuarioLogado, Long nrDiasAtraso) throws Exception {
		negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs().clear();
		negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs().clear();
		for (ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO : negociacaoContaReceberVO.getCondicaoRenegociacao().getItemCondicaoRenegociacaoVOs()) {
			if (itemCondicaoRenegociacaoVO.getIsAtivo()
					&& ((visaoAdministrativa && itemCondicaoRenegociacaoVO.getUtilizarVisaoAdministrativa()) 
							|| (!visaoAdministrativa && itemCondicaoRenegociacaoVO.getUtilizarVisaoAluno())) 
					&& itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() <= nrDiasAtraso 
					&& itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() >= nrDiasAtraso) {
				try {
					validarDadosUsuarioLogadoPossuiPermissaoRealizarRenegociacao(negociacaoContaReceberVO, negociacaoContaReceberVO.getCondicaoRenegociacao(), itemCondicaoRenegociacaoVO, usuarioLogado);
					boolean adicionarItemCondicaoNegociacao = validarDadosCondicaoRenegociacaoTipoOrigemETipoParcelaNegociar(negociacaoContaReceberVO, itemCondicaoRenegociacaoVO, usuarioLogado);
					// Valida se o valor total negociado não é menor que o valor mínimo que pode cada parcela
					negociacaoContaReceberVO.setItemCondicaoRenegociacao(itemCondicaoRenegociacaoVO);
					realizarIsencoesJurosMultaIndiceReajustePorItemCondicaoNegociacao(negociacaoContaReceberVO);
					negociacaoContaReceberVO.calcularValorTotal();
					if(adicionarItemCondicaoNegociacao && itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue() > 0.0 
							&& ((negociacaoContaReceberVO.getItemCondicaoRenegociacao().getFaixaEntradaInicial() < 100.0 &&  Uteis.arrendondarForcando2CadasDecimais((negociacaoContaReceberVO.getValorTotal().doubleValue() - negociacaoContaReceberVO.getValorMinimoEntrada())) < Uteis.arrendondarForcando2CadasDecimais(itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue()))
							|| (negociacaoContaReceberVO.getItemCondicaoRenegociacao().getFaixaEntradaInicial().equals(100.0) && Uteis.arrendondarForcando2CadasDecimais(itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue()) >  Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorMinimoEntrada())))){
						adicionarItemCondicaoNegociacao = false;
					}
					
					if(adicionarItemCondicaoNegociacao && ((itemCondicaoRenegociacaoVO.getValorInicial() > negociacaoContaReceberVO.getValorTotal().doubleValue()) 
							||  (itemCondicaoRenegociacaoVO.getValorFinal() < negociacaoContaReceberVO.getValorTotal().doubleValue()))) {
						adicionarItemCondicaoNegociacao = false;
					}
					
					if (adicionarItemCondicaoNegociacao) {								
						negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs().add(itemCondicaoRenegociacaoVO);
					}
					negociacaoContaReceberVO.setItemCondicaoRenegociacao(new ItemCondicaoRenegociacaoVO());
				}catch (Exception e) {
					throw e;
				}
			}
		}
		if (!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getItemCondicaoRenegociacaoVOs())) {
			throw new Exception("Não existe nenhuma condição de negociação dísponivel.");
		}
	}

	private void montarContaCorrente(NegociacaoContaReceberVO negociacaoContaReceberVO, Boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception {
		if (!visaoAdministrativa) {
			if (negociacaoContaReceberVO.getCondicaoRenegociacao().getUtilizarContaCorrenteEspecifica()) {
				negociacaoContaReceberVO.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getCondicaoRenegociacao().getContaCorrentePadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			} else {
				negociacaoContaReceberVO.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorCondicaoRenegociacaoEUnidadeEnsino(negociacaoContaReceberVO.getCondicaoRenegociacao().getCodigo(), negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				if (!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getContaCorrente()) && Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getUnidadeEnsino().getContaCorrentePadraoNegociacao())) {
					negociacaoContaReceberVO.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getUnidadeEnsino().getContaCorrentePadraoNegociacao(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				} else if (!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getContaCorrente())){
					throw new Exception("Não foi encontrada Conta Corrente na Condição de Renegociação para a Unidade de Ensino: " + negociacaoContaReceberVO.getUnidadeEnsino().getNome().toUpperCase() + ".");
				}
			}
		}
	}

	public List<OpcaoAlunoCondicaoRenegociacaoVO> realizarCriacaoOpcaoAlunoCondicaoRenegociacaoComBaseItemCondicaoRenegociacao(Double valorInicial, Double valorEntrada, Date dataBaseVencimentoParcela, CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO) throws Exception {
		List<OpcaoAlunoCondicaoRenegociacaoVO> opcaoAlunoCondicaoRenegociacaoVOs = new ArrayList<OpcaoAlunoCondicaoRenegociacaoVO>(0);
		OpcaoAlunoCondicaoRenegociacaoVO opcao = null;
		Integer parcelaInicial = itemCondicaoRenegociacaoVO.getParcelaInicial();
		//Double valorBase = Uteis.arrendondarForcando2CadasDecimais((valorInicial + ((valorInicial * condicaoRenegociacaoVO.getJuroEspecifico()) / 100) - ((valorInicial * condicaoRenegociacaoVO.getDescontoEspecifico()) / 100)) + ((valorInicial * itemCondicaoRenegociacaoVO.getJuro()) / 100) - ((valorInicial * itemCondicaoRenegociacaoVO.getDesconto()) / 100));
		while (parcelaInicial <= itemCondicaoRenegociacaoVO.getParcelaFinal()) {
			opcao = new OpcaoAlunoCondicaoRenegociacaoVO();
			opcao.setValorInicial(valorInicial);
			opcao.setValorFinal(valorInicial);
			opcao.setNumeroParcela(parcelaInicial);
			opcao.setDataBaseVencimentoParcela(dataBaseVencimentoParcela);
			if (parcelaInicial > 0 && valorEntrada > 0.0) {
				opcao.setPossuiEntrada(true);
				opcao.setPorcentagemEntrada(Uteis.arrendondarForcando2CadasDecimais((valorEntrada*100)/valorInicial));
				opcao.setValorEntrada(valorEntrada);
			} else if (parcelaInicial == 0) {
				opcao.setPossuiEntrada(true);
				opcao.setValorEntrada(valorInicial);
				opcao.setPorcentagemEntrada(100.0);
			} else {
				opcao.setPossuiEntrada(false);
				opcao.setPorcentagemEntrada(0.0);
				opcao.setValorEntrada(0.0);
			}
			if (parcelaInicial > 0) {
				opcao.setValorParcela(Uteis.arrendondarForcando2CadasDecimais((valorInicial - opcao.getValorEntrada()) / parcelaInicial));
			} else {
				opcao.setValorParcela(0.0);
			}
			opcao.setDescontoEspecifico(itemCondicaoRenegociacaoVO.getDesconto());
			opcao.setDescontoGeral(condicaoRenegociacaoVO.getDescontoEspecifico());
			opcao.setJuroEspecifico(itemCondicaoRenegociacaoVO.getJuro());
			opcao.setJuroGeral(condicaoRenegociacaoVO.getJuroEspecifico());
			opcao.setValorFinal(Uteis.arrendondarForcando2CadasDecimais(opcao.getValorEntrada() + (opcao.getNumeroParcela() * opcao.getValorParcela())));
			opcao.setItemCondicaoRenegociacaoVO(itemCondicaoRenegociacaoVO);
			parcelaInicial++;
			if (opcao.getValorParcela() >= itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue() && opcao.getValorParcela() > 0.0) {    		
				opcaoAlunoCondicaoRenegociacaoVOs.add(opcao);
    		}
		}
		return opcaoAlunoCondicaoRenegociacaoVOs;
	}

	public void adicionarContaReceberRenegociacaoAluno(NegociacaoContaReceberVO negociacaoContaReceberVO, List<ContaReceberVO> contaReceberVOs) throws Exception {
		ContaReceberNegociadoVO contaReceberNegociadoVO = null;
		negociacaoContaReceberVO.setValor(0.0);
		negociacaoContaReceberVO.getContaReceberNegociadoVOs().clear();
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			if (contaReceberVO.getSelecionado()) {
				contaReceberNegociadoVO = new ContaReceberNegociadoVO();
				contaReceberNegociadoVO.setContaReceber(contaReceberVO);
				contaReceberNegociadoVO.setNrDiasAtraso(contaReceberVO.getNrDiasAtraso());
				getFacadeFactory().getContaReceberNegociadoFacade().realizarCalculoValorContaReceberAdicionar(contaReceberNegociadoVO, false, false, false, false);
				negociacaoContaReceberVO.getContaReceberNegociadoVOs().add(contaReceberNegociadoVO);
				negociacaoContaReceberVO.setValor(negociacaoContaReceberVO.getValor() + contaReceberNegociadoVO.getValor());
			}
		}
		negociacaoContaReceberVO.calcularValorTotal();
		if (negociacaoContaReceberVO.getContaReceberNegociadoVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_selecione_contas_renegociacao"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRenegociacaoAluno(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception {		
		Double descGeral = negociacaoContaReceberVO.getDesconto();
		Double acresGeral = negociacaoContaReceberVO.getAcrescimoGeral();
		try {
		negociacaoContaReceberVO.getNovaContaReceber().clear();
		OpcaoAlunoCondicaoRenegociacaoVO opcaoAlunoCondicaoRenegociacaoSelecionado = null;
		negociacaoContaReceberVO.setCentroReceita(configuracaoFinanceiroVO.getCentroReceitaNegociacaoPadrao());
		negociacaoContaReceberVO.setJustificativa(UteisJSF.internacionalizar("msg_renegociacao_justificativa_padrao").replace("{0}", usuarioLogado.getVisaoLogar()));
		negociacaoContaReceberVO.setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum.ENTRE_DIAS);
		negociacaoContaReceberVO.setIntervaloParcela(30);
		for (OpcaoAlunoCondicaoRenegociacaoVO opcaoAlunoCondicaoRenegociacaoVO : negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs()) {
			if (opcaoAlunoCondicaoRenegociacaoVO.getNumeroParcela().equals(negociacaoContaReceberVO.getNrParcela())) {
				opcaoAlunoCondicaoRenegociacaoSelecionado = opcaoAlunoCondicaoRenegociacaoVO;
				opcaoAlunoCondicaoRenegociacaoSelecionado.setValorEntrada(negociacaoContaReceberVO.getValorEntrada());
				if(opcaoAlunoCondicaoRenegociacaoSelecionado.getValorFinal() > opcaoAlunoCondicaoRenegociacaoSelecionado.getValorInicial()) {
					negociacaoContaReceberVO.setAcrescimoGeral(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getAcrescimoGeral()+ (opcaoAlunoCondicaoRenegociacaoSelecionado.getValorFinal() - opcaoAlunoCondicaoRenegociacaoSelecionado.getValorInicial())));
				} else if(opcaoAlunoCondicaoRenegociacaoSelecionado.getValorFinal() < opcaoAlunoCondicaoRenegociacaoSelecionado.getValorInicial()) {
					if(negociacaoContaReceberVO.getTipoDesconto().equals("PO")) {
						Double desconto = Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getDescontoCalculado() + (opcaoAlunoCondicaoRenegociacaoSelecionado.getValorInicial() - opcaoAlunoCondicaoRenegociacaoSelecionado.getValorFinal()));
						negociacaoContaReceberVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais( (desconto * 100) / negociacaoContaReceberVO.getValor()));
					}else {
						negociacaoContaReceberVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getDesconto() + (opcaoAlunoCondicaoRenegociacaoSelecionado.getValorInicial() - opcaoAlunoCondicaoRenegociacaoSelecionado.getValorFinal())));
					}
				}
				opcaoAlunoCondicaoRenegociacaoVO.setPossuiEntrada(negociacaoContaReceberVO.getValorEntrada() > 0.0);				
				break;
			}
		}		
		
		if(opcaoAlunoCondicaoRenegociacaoSelecionado == null && Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal()) == Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada())) {
			opcaoAlunoCondicaoRenegociacaoSelecionado =  new OpcaoAlunoCondicaoRenegociacaoVO();			
			opcaoAlunoCondicaoRenegociacaoSelecionado.setDescontoEspecifico(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getDesconto());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setDescontoGeral(negociacaoContaReceberVO.getCondicaoRenegociacao().getDescontoEspecifico());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setJuroEspecifico(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getJuro());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setJuroGeral(negociacaoContaReceberVO.getCondicaoRenegociacao().getJuroEspecifico());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setValorEntrada(negociacaoContaReceberVO.getValorEntrada());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setValorFinal(negociacaoContaReceberVO.getValorEntrada());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setItemCondicaoRenegociacaoVO(negociacaoContaReceberVO.getItemCondicaoRenegociacao());
			opcaoAlunoCondicaoRenegociacaoSelecionado.setPossuiEntrada(true);
			opcaoAlunoCondicaoRenegociacaoSelecionado.setNumeroParcela(0);
		}
		if (opcaoAlunoCondicaoRenegociacaoSelecionado != null) {
			gerarParcelasComBaseOpcaoAlunoCondicaoRenegociacao(negociacaoContaReceberVO, opcaoAlunoCondicaoRenegociacaoSelecionado, configuracaoFinanceiroVO, usuarioLogado);
			incluir(negociacaoContaReceberVO, configuracaoFinanceiroVO, true, usuarioLogado);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRenegociacaoContaReceberAluno(negociacaoContaReceberVO, usuarioLogado);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRenegociacaoContaReceberGrupoDestinatario(negociacaoContaReceberVO, negociacaoContaReceberVO.getGrupoDestinatariosVO(), usuarioLogado);
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_selecione_opcaoRenegociacao"));
		}
		}catch (Exception e) {
			negociacaoContaReceberVO.setDesconto(descGeral);
			negociacaoContaReceberVO.setAcrescimoGeral(acresGeral);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ContaReceberVO incluirRenegociacaoApartirContaReceber(ContaReceberVO contaReceberSimulacao, ContaReceberVO contaReceber, Date dataVencimento, Boolean validarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean removerDescontoVencido, UsuarioVO usuarioLogado) throws Exception {
		if (validarDados) {
			if (dataVencimento == null) {
				throw new ConsistirException("O campo NOVA DATA VENCIMENTO deve ser informado.");
			}
			// if (Uteis.getDateSemHora(dataVencimento).compareTo(Uteis.getDateSemHora(contaReceber.getDataVencimento())) <= 0) {
			// throw new ConsistirException("O campo NOVA DATA VENCIMENTO deve ser maior que a DATA DE VENCIMENTO da conta a receber atual.");
			// }
			if (Uteis.getDateSemHora(dataVencimento).compareTo(Uteis.getDateSemHora(new Date())) < 0) {
				throw new ConsistirException("O campo NOVA DATA VENCIMENTO deve ser maior ou igual a data atual.");
			}
		}
		contaReceber.setContaVencida(Uteis.nrDiasEntreDatas(contaReceber.getDataVencimento(), new Date()) < 0);
		NegociacaoContaReceberVO negociacaoContaReceberVO = new NegociacaoContaReceberVO();
		negociacaoContaReceberVO.setCentroReceita(contaReceber.getCentroReceita());
		negociacaoContaReceberVO.getContaCorrente().setCodigo(contaReceber.getContaCorrente());
		negociacaoContaReceberVO.setData(new Date());
		negociacaoContaReceberVO.setDesconto(0.0);
		negociacaoContaReceberVO.setIntervaloParcela(Uteis.getIntervaloEntreDatas(new Date(), dataVencimento));
		negociacaoContaReceberVO.setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum.ENTRE_DIAS);
		negociacaoContaReceberVO.setResponsavel(usuarioLogado);
		negociacaoContaReceberVO.setLiberarRenovacaoAposPagamentoTodasParcelas(true);
		if (contaReceber.getTipoFuncionario()) {
			negociacaoContaReceberVO.setFuncionario(contaReceber.getFuncionario());
			negociacaoContaReceberVO.setPessoa(contaReceber.getPessoa());
		}
		negociacaoContaReceberVO.setJustificativa("Alteração na DATA DE VENCIMENTO.");
		if (contaReceber.getTipoAluno() || contaReceber.getTipoResponsavelFinanceiro()) {
			negociacaoContaReceberVO.setMatriculaAluno(contaReceber.getMatriculaAluno());
			negociacaoContaReceberVO.setPessoa(contaReceber.getPessoa());
		}
		negociacaoContaReceberVO.setNrParcela(1);
		if (contaReceber.getTipoParceiro()) {
			negociacaoContaReceberVO.setParceiro(contaReceber.getParceiroVO());
		} else {
			negociacaoContaReceberVO.setPessoa(contaReceber.getPessoa());
		}
		negociacaoContaReceberVO.setTipoPessoa(contaReceber.getTipoPessoa());
		negociacaoContaReceberVO.setTipoRenegociacao("VE");
		negociacaoContaReceberVO.setUnidadeEnsino(contaReceber.getUnidadeEnsinoFinanceira());
		negociacaoContaReceberVO.setValor(contaReceberSimulacao.getValorBaseContaReceber());
		if(removerDescontoVencido && contaReceber.getContaVencida()) {
			negociacaoContaReceberVO.setDescontoProgressivoVO(null);
		}else {
		negociacaoContaReceberVO.setDescontoProgressivoVO(contaReceber.getDescontoProgressivo());
		}
		contaReceber.setRealizandoRecebimento(Boolean.TRUE);
		contaReceber.setValorFinalCalculado(contaReceber.getCalcularValorFinal(new Date(), configuracaoFinanceiroVO, false, new Date(), usuarioLogado));
		
		contaReceber.setRealizandoRecebimento(Boolean.FALSE);
//		negociacaoContaReceberVO.setAcrescimoPorParcela(contaReceberSimulacao.getJuro() + contaReceberSimulacao.getMulta());		
//		if (contaReceberSimulacao.getValor() > Uteis.arrendondarForcando2CadasDecimais(contaReceber.getValorBaseContaReceber() + contaReceberSimulacao.getJuro() + contaReceberSimulacao.getMulta())) {
//			negociacaoContaReceberVO.setAcrescimoGeral(negociacaoContaReceberVO.getAcrescimoGeral() + Uteis.arrendondarForcando2CadasDecimais(contaReceberSimulacao.getValor() - contaReceber.getValorBaseContaReceber() + contaReceber.getJuro() + contaReceber.getMulta()));
//		}
//		negociacaoContaReceberVO.setValorTotal(contaReceberSimulacao.getValor());

		ContaReceberVO novaContaReceberVO = new ContaReceberVO();
		novaContaReceberVO.setBeneficiario(contaReceberSimulacao.getBeneficiario());
		novaContaReceberVO.setCentroReceita(contaReceberSimulacao.getCentroReceita());
		novaContaReceberVO.setConfiguracaoFinanceiro(configuracaoFinanceiroVO);
		novaContaReceberVO.setContaCorrente(contaReceberSimulacao.getContaCorrente());
		novaContaReceberVO.setContaCorrenteVO(contaReceberSimulacao.getContaCorrenteVO());
		novaContaReceberVO.setConvenio(contaReceberSimulacao.getConvenio());
		novaContaReceberVO.setData(new Date());
		novaContaReceberVO.setDataVencimento(dataVencimento);
		novaContaReceberVO.setFuncionario(contaReceberSimulacao.getFuncionario());
		novaContaReceberVO.setMatriculaAluno(contaReceberSimulacao.getMatriculaAluno());
		novaContaReceberVO.setMatriculaPeriodo(contaReceberSimulacao.getMatriculaPeriodo());
		novaContaReceberVO.setOrdemConvenio(contaReceberSimulacao.getOrdemConvenio());
		novaContaReceberVO.setOrdemConvenioValorCheio(contaReceberSimulacao.getOrdemConvenioValorCheio());
		novaContaReceberVO.setOrdemDescontoAluno(contaReceberSimulacao.getOrdemDescontoAluno());
		novaContaReceberVO.setOrdemDescontoAlunoValorCheio(contaReceberSimulacao.getOrdemDescontoAlunoValorCheio());
		novaContaReceberVO.setOrdemDescontoProgressivo(contaReceberSimulacao.getOrdemDescontoProgressivo());
		novaContaReceberVO.setOrdemDescontoProgressivoValorCheio(contaReceberSimulacao.getOrdemDescontoProgressivoValorCheio());
		novaContaReceberVO.setOrdemPlanoDesconto(contaReceberSimulacao.getOrdemPlanoDesconto());
		novaContaReceberVO.setOrdemPlanoDescontoValorCheio(contaReceberSimulacao.getOrdemPlanoDescontoValorCheio());
		novaContaReceberVO.setParceiroVO(contaReceberSimulacao.getParceiroVO());
		novaContaReceberVO.setParcela(contaReceberSimulacao.getParcela());
		novaContaReceberVO.setPessoa(contaReceberSimulacao.getPessoa());
		novaContaReceberVO.setResponsavelFinanceiro(contaReceberSimulacao.getResponsavelFinanceiro());
		novaContaReceberVO.setSituacao("AR");
		novaContaReceberVO.setTipoBoleto(contaReceberSimulacao.getTipoBoleto());
		novaContaReceberVO.setTipoPessoa(contaReceberSimulacao.getTipoPessoa());
		novaContaReceberVO.setValor(contaReceberSimulacao.getValor());
		novaContaReceberVO.setValorBaseContaReceber(contaReceberSimulacao.getValorBaseContaReceber());
		novaContaReceberVO.setValorCusteadoContaReceber(contaReceberSimulacao.getValorCusteadoContaReceber());
		novaContaReceberVO.setAcrescimo(contaReceberSimulacao.getAcrescimo());
		novaContaReceberVO.setValorDescontoRateio(contaReceberSimulacao.getValorDescontoRateio());
		novaContaReceberVO.setValorDescontoRecebido(contaReceberSimulacao.getValorDescontoRecebido());
		novaContaReceberVO.setValorDesconto(contaReceberSimulacao.getValorDesconto());
		novaContaReceberVO.setTipoDesconto(contaReceberSimulacao.getTipoDesconto());
		if(removerDescontoVencido && contaReceber.getContaVencida()) {
			novaContaReceberVO.setDescontoProgressivo(null);
			novaContaReceberVO.setValorDescontoProgressivo(0.0);
		}else {
		novaContaReceberVO.setDescontoProgressivo(contaReceberSimulacao.getDescontoProgressivo());
		novaContaReceberVO.setValorDescontoProgressivo(contaReceberSimulacao.getValorDescontoProgressivo());
		}
		for (PlanoDescontoContaReceberVO planoDescontoContaReceberVO : contaReceberSimulacao.getPlanoDescontoContaReceberVOs()) {
			if(!removerDescontoVencido || (removerDescontoVencido && planoDescontoContaReceberVO.getValorUtilizadoRecebimento() > 0)) {
			PlanoDescontoContaReceberVO planoDescontoContaReceberVO2 = (PlanoDescontoContaReceberVO) planoDescontoContaReceberVO.clone();
			planoDescontoContaReceberVO2.setCodigo(0);
			planoDescontoContaReceberVO2.setContaReceber(0);
			novaContaReceberVO.getPlanoDescontoContaReceberVOs().add(planoDescontoContaReceberVO2);
		}
		}
		novaContaReceberVO.setRealizandoRecebimento(true);
		novaContaReceberVO.setValorFinalCalculado(novaContaReceberVO.getCalcularValorFinal(Uteis.obterDataAntiga(novaContaReceberVO.getDataVencimento(), 100), configuracaoFinanceiroVO, false, Uteis.obterDataAntiga(novaContaReceberVO.getDataVencimento(), 100), usuarioLogado));
		
		novaContaReceberVO.setRealizandoRecebimento(false);
		if (!novaContaReceberVO.getListaDescontosAplicavesContaReceber().isEmpty()) {
			novaContaReceberVO.setValorCusteadoContaReceber(novaContaReceberVO.getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
			novaContaReceberVO.setValor(novaContaReceberVO.getValorBaseContaReceber() - novaContaReceberVO.getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
			novaContaReceberVO.setValorReceberCalculado(novaContaReceberVO.getValorReceberCalculado() - novaContaReceberVO.getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
			novaContaReceberVO.setValorFinalCalculado(novaContaReceberVO.getValorFinalCalculado() - novaContaReceberVO.getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
		}
		getFacadeFactory().getContaReceberFacade().realizarAtualizacaoValorDescontoFaixaContaReceber(novaContaReceberVO, configuracaoFinanceiroVO, false);
		novaContaReceberVO.setUnidadeEnsino(contaReceber.getUnidadeEnsino());
		novaContaReceberVO.setUnidadeEnsinoFinanceira(contaReceber.getUnidadeEnsinoFinanceira());
		ContaReceberNegociadoVO contaReceberNegociadoVO = new ContaReceberNegociadoVO();
		contaReceberNegociadoVO.setContaReceber(contaReceber);
		contaReceberNegociadoVO.setValorOriginalConta(contaReceber.getValorRecebido());
		contaReceberNegociadoVO.setValor(contaReceber.getValorRecebido());
		contaReceberNegociadoVO.setNrDiasAtraso(Uteis.getIntervaloEntreDatas(contaReceber.getDataVencimento(), new Date()).longValue());
		if (contaReceberNegociadoVO.getNrDiasAtraso() < 0) {
			contaReceberNegociadoVO.setNrDiasAtraso(0l);
		}
		negociacaoContaReceberVO.getNovaContaReceber().add(novaContaReceberVO);
		negociacaoContaReceberVO.getContaReceberNegociadoVOs().add(contaReceberNegociadoVO);
		if(novaContaReceberVO.getValor() > contaReceberNegociadoVO.getValor()) {
			negociacaoContaReceberVO.setAcrescimoGeral(Uteis.arrendondarForcando2CadasDecimais(novaContaReceberVO.getValor() - contaReceberNegociadoVO.getValor()));
		}else if (novaContaReceberVO.getValor() < contaReceberNegociadoVO.getValor()) {
			negociacaoContaReceberVO.setTipoDesconto(TipoDescontoAluno.VALOR.getValor());
			negociacaoContaReceberVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociadoVO.getValor() - novaContaReceberVO.getValor()));
		}
		realizarCalculoTotal(negociacaoContaReceberVO);
		realizarGeracaoCentroResultadoComBaseNasContasNegociadas(negociacaoContaReceberVO, usuarioLogado);
		
		incluir(negociacaoContaReceberVO, configuracaoFinanceiroVO, false, usuarioLogado);
		return novaContaReceberVO;
	}

	@Override
	public Boolean realizarValidacaoAtivacaoMatriculaPeriodoParcelaRenegociada(Integer contaReceber, Integer matriculaPeriodo, Boolean utilizaMatricula) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select negociacaocontareceber.codigo, liberarrenovacaoapospagamentoprimeiraparcela, liberarrenovacaoapospagamentotodasparcelas,  ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('RE', 'NE') and crr.tipoOrigem = 'NCR' and  crr.codorigem::INT = negociacaocontareceber.codigo ) as recebido, ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('AR') and crr.tipoOrigem = 'NCR' and  crr.codorigem::INT = negociacaocontareceber.codigo ) as areceber, ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('RE') and crr.tipoOrigem in ('MEN') and  crr.codorigem = '" + matriculaPeriodo.toString() + "' ) as recebidoNaoNegociado ");
		sql.append(" from negociacaocontareceber   ");
		sql.append(" inner join contareceber on  contareceber.tipoOrigem = 'NCR' and contareceber.codorigem::INT = negociacaocontareceber.codigo ");
		sql.append(" where contareceber.codigo =  ").append(contaReceber);
		sql.append(" and (select count(contarecebernegociado.codigo) from contarecebernegociado   ");
		sql.append(" inner join contareceber cr2 on cr2.codigo = contarecebernegociado.contareceber ");
		sql.append(" where contarecebernegociado.negociacaocontareceber = negociacaocontareceber.codigo ");
		if (utilizaMatricula) {
			sql.append(" and cr2.tipoorigem in ('MAT')) > 0 ");
		} else {
			sql.append(" and cr2.tipoorigem in ('MAT', 'MEN')) > 0 ");
		}
		sql.append(" group by negociacaocontareceber.codigo, liberarrenovacaoapospagamentoprimeiraparcela, liberarrenovacaoapospagamentotodasparcelas ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			if (!utilizaMatricula && rs.getInt("recebidoNaoNegociado") > 0) {
				return true;
			}
			if (rs.getBoolean("liberarrenovacaoapospagamentotodasparcelas") && rs.getInt("areceber") > 0) {
				return false;
			}
			if (rs.getBoolean("liberarrenovacaoapospagamentoprimeiraparcela") && rs.getInt("recebido") == 0) {
				return false;
			}

			return true;
		}
		return false;
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoConfiguracoes() {
		List<UnidadeEnsinoVO> listaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select codigo ,configuracoes from unidadeensino");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getConfiguracoes().setCodigo(rs.getInt("configuracoes"));
			listaUnidadeEnsino.add(obj);
		}

		return listaUnidadeEnsino;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void excluirNegociacaoRecebimentoVencida(RegistroExecucaoJobVO registroExecucaoJobVO) throws Exception {
		try {
			List<UnidadeEnsinoVO> listaUnidade = new ArrayList<UnidadeEnsinoVO>(0);
			listaUnidade.addAll(consultarUnidadeEnsinoConfiguracoes());
			for (UnidadeEnsinoVO obj : listaUnidade) {
				executarExclusaoNegociacaoRecebimentoVencida(obj, registroExecucaoJobVO);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void executarExclusaoNegociacaoRecebimentoVencida(UnidadeEnsinoVO unidadeEnsino, RegistroExecucaoJobVO registroExecucaoJobVO) throws Exception {
		try {
			List<NegociacaoContaReceberVO> lista = consultarListaNegociacaoRecebimento(unidadeEnsino);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoGED();
			UsuarioVO usuResp =  null;
			if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo())) {
				usuResp = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			}else{
				usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			}
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getAplicacaoControle().getConfiguracaoFinanceiroVO(unidadeEnsino.getCodigo());
			for (NegociacaoContaReceberVO obj : lista) {
				List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>(0);
				contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContasVinculadasNegociacao(obj.getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuResp);
				if (!getFacadeFactory().getNegociacaoContaReceberFacade().verificarPermissaoExcluirNegociacaoThread(obj, contaReceberVOs)) {
					obj = getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, configuracaoFinanceiroVO, usuResp);
					getFacadeFactory().getNegociacaoContaReceberFacade().excluir(obj, contaReceberVOs, configuracaoFinanceiroVO, false, usuResp);
					registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"\nExclusão de Negociação Conta Receber de Código "+obj.getCodigo());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<NegociacaoContaReceberVO> consultarListaNegociacaoRecebimento(UnidadeEnsinoVO unidadeEnsino) {
		List<NegociacaoContaReceberVO> listaNegociacao = new ArrayList<NegociacaoContaReceberVO>(0);
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct negociacaocontareceber.codigo from negociacaocontareceber   ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = negociacaocontareceber.unidadeensino   ");
		sql.append(" inner join configuracaoFinanceiro on configuracaoFinanceiro.configuracoes =   ");
		sql.append(" (case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes  ");
		sql.append(" else (select c.codigo from configuracoes c where c.padrao = true ) end)  ");
		sql.append(" where   ");
		sql.append(" configuracaoFinanceiro.excluirNegociacaoRecebimentoVencida = 'true'   ");
		sql.append(" and not exists ( ");
		sql.append(" 		select cr.codigorenegociacao from contareceber cr ");
		sql.append(" 		where cr.codorigem = negociacaocontareceber.codigo::varchar and cr.situacao in ('RE', 'NE', 'CF')  ");
		sql.append(" 		and cr.tipoorigem = 'NCR' limit 1 ");
		sql.append(" 		) ");
		sql.append(" and exists ( ");
		sql.append(" 		select cr.codigorenegociacao from contareceber cr ");
		sql.append(" 		where cr.codorigem = negociacaocontareceber.codigo::varchar and cr.situacao = 'AR'  ");
		sql.append(" 		and cr.tipoorigem = 'NCR' and (datavencimento + (case when configuracaoFinanceiro.qtdeDiasExcluirNegociacaoContaReceberVencida is null or configuracaoFinanceiro.qtdeDiasExcluirNegociacaoContaReceberVencida = 0 then 4 else configuracaoFinanceiro.qtdeDiasExcluirNegociacaoContaReceberVencida end ||' day')::interval) <= current_date limit 1 ");
		sql.append(" 		) ");
		sql.append(" ");
		sql.append(" and unidadeensino.codigo =");
		sql.append(unidadeEnsino.getCodigo());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (dadosSQL.next()) {
			NegociacaoContaReceberVO obj = new NegociacaoContaReceberVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			listaNegociacao.add(obj);
		}
		return listaNegociacao;
	}

	@Override
	public Boolean realizarValidacaoEstornoAtivacaoMatriculaPeriodoParcelaRenegociada(Integer contaReceber, Integer matriculaPeriodo, Boolean utilizaMatricula) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select negociacaocontareceber.codigo, liberarrenovacaoapospagamentoprimeiraparcela, liberarrenovacaoapospagamentotodasparcelas,  ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('RE', 'NE') and crr.tipoOrigem = 'NCR' and  crr.codorigem::INT = negociacaocontareceber.codigo ) as recebido, ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('AR') and crr.tipoOrigem = 'NCR' and  crr.codorigem::INT = negociacaocontareceber.codigo ) as areceber, ");
		sql.append(" (select count(codigo) from contareceber crr where crr.situacao in ('RE') and crr.tipoOrigem in ('MEN') and  crr.codorigem = '" + matriculaPeriodo.toString() + "' ) as recebidoNaoNegociado ");
		sql.append(" from negociacaocontareceber   ");
		sql.append(" inner join contareceber on  contareceber.tipoOrigem = 'NCR' and contareceber.codorigem::INT = negociacaocontareceber.codigo ");
		sql.append(" where contareceber.codigo =  ").append(contaReceber);
		sql.append(" and (select count(contarecebernegociado.codigo) from contarecebernegociado   ");
		sql.append(" inner join contareceber cr2 on cr2.codigo = contarecebernegociado.contareceber ");
		sql.append(" where contarecebernegociado.negociacaocontareceber = negociacaocontareceber.codigo ");
		if (utilizaMatricula) {
			sql.append(" and cr2.tipoorigem in ('MAT')) > 0 ");
		} else {
			sql.append(" and cr2.tipoorigem in ('MAT', 'MEN')) > 0 ");
		}
		sql.append(" group by negociacaocontareceber.codigo, liberarrenovacaoapospagamentoprimeiraparcela, liberarrenovacaoapospagamentotodasparcelas ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			if (!utilizaMatricula && rs.getInt("recebidoNaoNegociado") > 0) {
				return false;
			}
			if (rs.getBoolean("liberarrenovacaoapospagamentotodasparcelas") && rs.getInt("areceber") == 0) {
				return true;
			}
			if (rs.getBoolean("liberarrenovacaoapospagamentoprimeiraparcela") && rs.getInt("recebido") == 0) {
				return true;
			}

			return false;
		}
		return false;
	}

	@Override
	public void realizarCalculoTotal(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		negociacaoContaReceberVO.setValor(0.0);
		for (ContaReceberNegociadoVO contaReceberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
			negociacaoContaReceberVO.setValor(contaReceberNegociadoVO.getValor() + negociacaoContaReceberVO.getValor());
		}
		negociacaoContaReceberVO.calcularValorTotal();
	}

	@Override
	public void realizarCalculoValorTodasContaReceberAdicionar(NegociacaoContaReceberVO negociacaoContaReceberVO, Boolean desconsiderarDescontoProgressivo, Boolean desconsiderarDescontoAluno, Boolean desconsiderarDescontoInstituicaoComValidade, Boolean desconsiderarDescontoInstituicaoSemValidade) throws Exception {

		for (ContaReceberNegociadoVO contaReceberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
			contaReceberNegociadoVO.setDesconsiderarDescontoAluno(desconsiderarDescontoAluno);
			contaReceberNegociadoVO.setDesconsiderarDescontoInstituicaoComValidade(desconsiderarDescontoInstituicaoComValidade);
			contaReceberNegociadoVO.setDesconsiderarDescontoInstituicaoSemValidade(desconsiderarDescontoInstituicaoSemValidade);
			contaReceberNegociadoVO.setDesconsiderarDescontoProgressivo(desconsiderarDescontoProgressivo);
			getFacadeFactory().getContaReceberNegociadoFacade().realizarCalculoValorContaReceberDesconsiderandoDescontos(contaReceberNegociadoVO);
		}
		getFacadeFactory().getNegociacaoContaReceberFacade().realizarCalculoTotal(negociacaoContaReceberVO);

	}

	@Override
	public void gerarParcelas(NegociacaoContaReceberVO obj, TipoAcrescimoEnum tipoAcrescimo, Double acrescimoPorParcela, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		int nrParcela = 1;
		if (obj.getPermitirRenegociacaoApenasComCondicaoRenegociacao()) {
			if (!usuarioVO.getIsApresentarVisaoAdministrativa()) {				
				inicializarDadosNegociacaoContaReceberBaseadoCondicaoRenegociacao(obj, configuracaoFinanceiroVO, usuarioVO);
			}
		} else {
			NegociacaoContaReceberVO.validarDados(obj);
			obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			obj.setNovaContaReceber(new ArrayList<ContaReceberVO>());
			obj.setTipoAcrescimoPorParcela(tipoAcrescimo);
			obj.setAcrescimoPorParcela(acrescimoPorParcela);
			obj.setTotalAcrescimoPorParcela(0.0);
		}
		if (obj.getValorEntrada() > 0) {
			String parcela = "1N" + (obj.getNrParcela()+1);
			if (obj.getValorEntrada().equals(obj.getValor())) {
				parcela = "1N1";
		}
			Date dataBase = new Date();
			if(obj.getPermitirRenegociacaoApenasComCondicaoRenegociacao() ) {
				dataBase = obj.getDataVencimentoEntrada();
			}
			obj.getNovaContaReceber().add(criarContaReceber(parcela, obj.getValorEntrada(), dataBase, obj, configuracaoFinanceiroVO, usuarioVO));
		}
		if(Uteis.isAtributoPreenchido(obj.getDescontoProgressivoVO())){
			obj.setDescontoProgressivoVO(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoVO().getCodigo(), usuarioVO));
		}
		
		if (obj.getValorEntrada() < obj.getValor()) {
			Double valor =realizarCalculoValorBaseParcela(obj, tipoAcrescimo, acrescimoPorParcela);
			Double valorFinal = obj.getValorEntrada();
			while (obj.getNrParcela().intValue() >= nrParcela) {
				valorFinal += valor;
				Date dataPrevisao = new Date();
				if(obj.getTipoIntervaloParcelaEnum().isIntervaloEntreDias()){
					dataPrevisao = (Uteis.obterDataFutura(dataPrevisao, (obj.getIntervaloParcela().intValue() * nrParcela)));	
				}else{
					dataPrevisao = (Uteis.obterDataAvancadaPorMes(obj.getDataBaseParcela(), (nrParcela-1)));
				}
				
				if (obj.getNrParcela() == nrParcela) {
					Double resto = 0.0;
					if(obj.getAcrescimoPorParcela()>0.0){
						resto = calcularRestoUltimaParcelaNegociacao(valor, valorFinal, obj.getValorEntrada(), nrParcela);
					}else{
						resto = calcularRestoUltimaParcelaNegociacao(valor, obj.getValorTotal(), obj.getValorEntrada(), nrParcela);
					}
					if (!resto.equals(0.0)) {
						Boolean somar = verificarAcaoUltimaParcelaNegociacao(valor, obj.getValorTotal(), obj.getValorEntrada(), nrParcela);
						if (somar) {
							valor = valor + resto;
						} else {
							valor = valor - resto;
						}
					}
				}

				if (obj.getValorEntrada() > 0) {
					int tam = obj.getNrParcela() + 1;
					obj.getNovaContaReceber().add(criarContaReceber(nrParcela + 1 + "N" + tam, valor, dataPrevisao, obj, configuracaoFinanceiroVO, usuarioVO));
				} else {
					obj.getNovaContaReceber().add(criarContaReceber(nrParcela + "N" + obj.getNrParcela(), valor, dataPrevisao, obj, configuracaoFinanceiroVO, usuarioVO));
				}
				nrParcela++;
			}
			if(valorFinal - obj.getValorTotal() > 0 && obj.getAcrescimoPorParcela()>0.0){
				obj.setTotalAcrescimoPorParcela(valorFinal - obj.getValorTotal());
			}
			obj.calcularValorTotal();
		}
	}

	@Override
	public Double realizarCalculoValorBaseParcela(NegociacaoContaReceberVO negociacaoContaReceberVO, TipoAcrescimoEnum tipoAcrescimoEnum, Double acrescimoPorParcela) {
		Double valor = 0.0;
		if (negociacaoContaReceberVO.getNrParcela() > 0) {
			valor = (Uteis.arrendondarForcando2CadasDecimais((negociacaoContaReceberVO.getValorTotal() - negociacaoContaReceberVO.getValorEntrada()) / negociacaoContaReceberVO.getNrParcela().intValue()));
			if (acrescimoPorParcela > 0.0) {
				if (tipoAcrescimoEnum.equals(TipoAcrescimoEnum.PORCENTAGEM)) {
					valor += Uteis.arrendondarForcando2CasasDecimais((valor * acrescimoPorParcela) / 100.0);
				} else {
					valor += acrescimoPorParcela;
				}
				negociacaoContaReceberVO.setTotalAcrescimoPorParcela(Uteis.arrendondarForcando2CadasDecimais((valor * negociacaoContaReceberVO.getNrParcela()) - (negociacaoContaReceberVO.getValorTotal() - negociacaoContaReceberVO.getValorEntrada())));
			} else {
				negociacaoContaReceberVO.setTotalAcrescimoPorParcela(0.0);
			}
		}
		return valor;
	}

	public void gerarParcelasComBaseOpcaoAlunoCondicaoRenegociacao(NegociacaoContaReceberVO obj, OpcaoAlunoCondicaoRenegociacaoVO opcao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		NegociacaoContaReceberVO.validarDados(obj);
//		Date dataPrevisao = new Date();
//		if (opcao.getPossuiEntrada() && Uteis.isAtributoPreenchido(opcao.getItemCondicaoRenegociacaoVO().getQtdeDiasEntrada())) {
//			dataPrevisao =  Uteis.obterDataFutura(dataPrevisao, opcao.getItemCondicaoRenegociacaoVO().getQtdeDiasEntrada());
//		}
		if (opcao.getPossuiEntrada()) {
			String parcela = "1N" + (obj.getNrParcela()+1);
			if (obj.getValorEntrada().equals(obj.getValor())) {
				parcela = "1N1";
			}
			obj.getNovaContaReceber().add(criarContaReceber(parcela, obj.getValorEntrada(), obj.getDataVencimentoEntrada(), obj, configuracaoFinanceiroVO, usuarioVO));
		}
//		if(opcao.getPossuiEntrada() && (Uteis.isAtributoPreenchido(opcao.getItemCondicaoRenegociacaoVO().getQtdeDiasEntrada()) && !opcao.getItemCondicaoRenegociacaoVO().getGerarParcelas30DiasAposDataEntrada())
//				|| (!opcao.getItemCondicaoRenegociacaoVO().getGerarParcelas30DiasAposDataEntrada() && !opcao.getItemCondicaoRenegociacaoVO().getDefinirNumeroDiasVencimentoPrimeiraParcela()) ) {
//			 dataPrevisao = new Date();
//		}
		if (obj.getValorEntrada() < obj.getValorTotal() && opcao.getNumeroParcela() > 0) {
			int nrParcela = 1;
			while (opcao.getNumeroParcela() >= nrParcela) {				
				if (opcao.getPossuiEntrada()) {
					obj.getNovaContaReceber().add(criarContaReceber(nrParcela + 1 + "N" + (opcao.getNumeroParcela() + 1), opcao.getValorParcela(), 
							Uteis.obterDataFutura(obj.getDataBaseParcela(), (obj.getIntervaloParcela().intValue() * (nrParcela - 1))), 
									obj, configuracaoFinanceiroVO, usuarioVO));
				} else {
					obj.getNovaContaReceber().add(criarContaReceber(nrParcela + "N" + opcao.getNumeroParcela(), opcao.getValorParcela(),
					Uteis.obterDataFutura(obj.getDataBaseParcela(), (obj.getIntervaloParcela().intValue() * (nrParcela - 1))), obj, configuracaoFinanceiroVO, usuarioVO));
				}
				nrParcela++;
			}
		}

	}

	public Double calcularRestoUltimaParcelaNegociacao(Double valorParcela, Double valorTotal, Double valorEntrada, Integer nrParcela) {
		Double valorTotalTemp1 = valorTotal - valorEntrada;
		Double valorTotalTemp = valorParcela * nrParcela;
		Double resto = 0.0;
		if (valorTotalTemp < valorTotalTemp1) {
			resto = Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp1 - valorTotalTemp);
		} else if (valorTotalTemp > valorTotalTemp1) {
			resto = Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp - valorTotalTemp1);
		}
		return resto;
	}

	public Boolean verificarAcaoUltimaParcelaNegociacao(Double valorParcela, Double valorTotal, Double valorEntrada, Integer nrParcela) {
		Double valorTotalTemp1 = valorTotal - valorEntrada;
		Double valorTotalTemp = valorParcela * nrParcela;
		if (valorTotalTemp < valorTotalTemp1) {
			return Boolean.TRUE;
		} else if (valorTotalTemp > valorTotalTemp1) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public ContaReceberVO criarContaReceber(String nrParcela, Double valor, Date dataVencimento, NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		ContaReceberVO contaReceber = new ContaReceberVO();

		contaReceber.setParcela(nrParcela);
		contaReceber.setValor(valor);
		contaReceber.setValorBaseContaReceber(valor);
		contaReceber.setDataVencimento(dataVencimento);
		contaReceber.setDataCompetencia(dataVencimento);
		contaReceber.setTipoPessoa(obj.getTipoPessoa());
		contaReceber.getCentroReceita().setCodigo(obj.getCentroReceita().getCodigo());
		contaReceber.setContaCorrente(obj.getContaCorrente().getCodigo());
		contaReceber.setContaCorrenteVO(obj.getContaCorrente());// Obrigatorio para a validação da rotina verificaBloqueioEmissaoBoleto
		contaReceber.setEmissaoBloqueada(getFacadeFactory().getContaReceberFacade().verificaBloqueioEmissaoBoleto(contaReceber, usuarioVO));
		if (obj.getTipoAluno()) {
			contaReceber.getMatriculaAluno().setMatricula(obj.getMatriculaAluno().getMatricula());
			contaReceber.getMatriculaAluno().setPlanoFinanceiroAluno(obj.getMatriculaAluno().getPlanoFinanceiroAluno());
		}
		if (obj.getTipoFuncionario()) {
			contaReceber.getFuncionario().setCodigo(obj.getFuncionario().getCodigo());
		}

		// contaReceber.getCandidato().setCodigo(obj.getPessoa().getCodigo());
		if (obj.getTipoParceiro()) {
			contaReceber.getParceiroVO().setCodigo(obj.getParceiro().getCodigo());
			if(Uteis.isAtributoPreenchido(obj.getMatriculaAluno().getAluno())) {
				contaReceber.setMatriculaAluno(obj.getMatriculaAluno());
				contaReceber.setPessoa(obj.getMatriculaAluno().getAluno());
			}
		} else if (!obj.getTipoResponsavelFinanceiro()) {
			contaReceber.getPessoa().setCodigo(obj.getPessoa().getCodigo());
		} else if (obj.getTipoResponsavelFinanceiro()) {
			contaReceber.getResponsavelFinanceiro().setCodigo(obj.getPessoa().getCodigo());
		} else if (obj.getTipoFornecedor()) {
			contaReceber.getFornecedor().setCodigo(obj.getFornecedor().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getMatriculaAluno().getUnidadeEnsino().getCodigo())) {
			contaReceber.getUnidadeEnsino().setCodigo(obj.getMatriculaAluno().getUnidadeEnsino().getCodigo());
		} else {
			contaReceber.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
		}
		contaReceber.getUnidadeEnsinoFinanceira().setCodigo(obj.getUnidadeEnsino().getCodigo());

		contaReceber.setDescricaoPagamento(obj.getJustificativa());
		contaReceber.setTipoOrigem("NCR");
		if (Uteis.isAtributoPreenchido(obj.getDescontoProgressivoVO())) {
			contaReceber.setDescontoProgressivo(obj.getDescontoProgressivoVO());
		}
		obj.setData(new Date());
		for (NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO : obj.getNegociacaoContaReceberPlanoDescontoVOs()) {
			PlanoDescontoContaReceberVO planoDescontoContaReceberVO = new PlanoDescontoContaReceberVO();
			planoDescontoContaReceberVO.setTipoItemPlanoFinanceiro("PD");
			planoDescontoContaReceberVO.setPlanoDescontoVO(negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO());
			contaReceber.getPlanoDescontoContaReceberVOs().add(planoDescontoContaReceberVO);
		}
		contaReceber.setOrdemConvenio(obj.getOrdemConvenio());
		contaReceber.setOrdemConvenioValorCheio(obj.getOrdemConvenioValorCheio());
		contaReceber.setOrdemDescontoAluno(obj.getOrdemDescontoAluno());
		contaReceber.setOrdemDescontoAlunoValorCheio(obj.getOrdemDescontoAlunoValorCheio());
		contaReceber.setOrdemDescontoProgressivo(obj.getOrdemDescontoProgressivo());
		contaReceber.setOrdemDescontoProgressivoValorCheio(obj.getOrdemDescontoProgressivoValorCheio());
		contaReceber.setOrdemPlanoDesconto(obj.getOrdemPlanoDesconto());
		contaReceber.setOrdemPlanoDescontoValorCheio(obj.getOrdemPlanoDescontoValorCheio());
		contaReceber.setRealizandoRecebimento(true);
		contaReceber.getCalcularValorFinal(new Date(), configuracaoFinanceiroVO, false, new Date(), usuarioVO);
		//ContaReceber.montarListaDescontosAplicaveisContaReceber(contaReceber, Uteis.obterDataAntiga(contaReceber.getDataVencimento(), 100), configuracaoFinanceiroVO.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiroVO, usuarioVO, 0);
		return contaReceber;
	}

	@Override
	public void adicionarPlanoDesconto(NegociacaoContaReceberVO negociacaoContaReceberVO, Integer planoDesconto, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(planoDesconto)) {
			throw new Exception("O campo DESCONTO INSTITUIÇÃO deve ser informado.");
		}
		for (NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO : negociacaoContaReceberVO.getNegociacaoContaReceberPlanoDescontoVOs()) {
			if (negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO().getCodigo().equals(planoDesconto)) {
				return;
			}
		}
		PlanoDescontoVO planoDescontoVO = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(planoDesconto, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO = new NegociacaoContaReceberPlanoDescontoVO();
		negociacaoContaReceberPlanoDescontoVO.setNegociacaoContaReceberVO(negociacaoContaReceberVO);
		negociacaoContaReceberPlanoDescontoVO.setPlanoDescontoVO(planoDescontoVO);
		negociacaoContaReceberVO.getNegociacaoContaReceberPlanoDescontoVOs().add(negociacaoContaReceberPlanoDescontoVO);
	}

	@Override
	public void removerPlanoDesconto(NegociacaoContaReceberVO negociacaoContaReceberVO, NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO) throws Exception {
		negociacaoContaReceberVO.getNegociacaoContaReceberPlanoDescontoVOs().remove(negociacaoContaReceberPlanoDescontoVO);
	}

	@Override
	public void realizarInicializacaoDadosOpcaoRenegociacaoBaseadoCondicaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, List<ContaReceberVO> listaContaReceberVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean visaoAdministrativa, UsuarioVO usuarioVO) throws Exception {
		realizarInicializacaoDadosOpcaoRenegociacao(negociacaoContaReceberVO, listaContaReceberVOs, configuracaoGeralSistemaVO, visaoAdministrativa, usuarioVO);
	}

	public void inicializarDadosNegociacaoContaReceberBaseadoCondicaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		OpcaoAlunoCondicaoRenegociacaoVO opcaoAlunoCondicaoRenegociacaoSelecionado = null;
		for (OpcaoAlunoCondicaoRenegociacaoVO opcaoAlunoCondicaoRenegociacaoVO : negociacaoContaReceberVO.getOpcaoAlunoCondicaoRenegociacaoVOs()) {
			if (opcaoAlunoCondicaoRenegociacaoVO.getNumeroParcela().equals(negociacaoContaReceberVO.getNrParcela())) {
				validarDadosValorMinimoPorParcela(negociacaoContaReceberVO, opcaoAlunoCondicaoRenegociacaoVO, usuarioVO);
				opcaoAlunoCondicaoRenegociacaoSelecionado = opcaoAlunoCondicaoRenegociacaoVO;
				negociacaoContaReceberVO.setCentroReceita(configuracaoFinanceiroVO.getCentroReceitaNegociacaoPadrao());
				negociacaoContaReceberVO.setValorEntrada(opcaoAlunoCondicaoRenegociacaoVO.getValorEntrada());
				negociacaoContaReceberVO.setJustificativa(UteisJSF.internacionalizar("msg_renegociacao_justificativa_padrao").replace("{0}", usuarioVO.getVisaoLogar()));
				if (negociacaoContaReceberVO.getValorTotal() > opcaoAlunoCondicaoRenegociacaoVO.getValorFinal()) {
					if(negociacaoContaReceberVO.getTipoDesconto().equals("PO")){
						negociacaoContaReceberVO.setValorDescontoCondicaoRenegociacao(Uteis.arrendondarForcando2CadasDecimais((Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal() - opcaoAlunoCondicaoRenegociacaoVO.getValorFinal()) * 100) / negociacaoContaReceberVO.getValorTotal()));	
					}else{
						negociacaoContaReceberVO.setValorDescontoCondicaoRenegociacao(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotal() - opcaoAlunoCondicaoRenegociacaoVO.getValorFinal()));
				}					
				} else if (negociacaoContaReceberVO.getValorTotal() < opcaoAlunoCondicaoRenegociacaoVO.getValorFinal()) {
					negociacaoContaReceberVO.setValorAcrescimoCondicaoRenegociacao(Uteis.arrendondarForcando2CadasDecimais(opcaoAlunoCondicaoRenegociacaoVO.getValorFinal() - negociacaoContaReceberVO.getValorTotal()));
					negociacaoContaReceberVO.setAcrescimoGeral(negociacaoContaReceberVO.getAcrescimoGeral() + negociacaoContaReceberVO.getValorAcrescimoCondicaoRenegociacao());
				}
				negociacaoContaReceberVO.calcularValorTotal();
				negociacaoContaReceberVO.setItemCondicaoRenegociacao(opcaoAlunoCondicaoRenegociacaoSelecionado.getItemCondicaoRenegociacaoVO());
				break;
			}
		}
	}

	public boolean validarDadosCondicaoRenegociacaoTipoOrigemETipoParcelaNegociar(NegociacaoContaReceberVO negociacaoContaReceberVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioVO) throws Exception {
		boolean adicionarItemCondicaoNegociacao = true;
		for (ContaReceberNegociadoVO contaReceberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
			ContaReceberVO contaReceberVO = contaReceberNegociadoVO.getContaReceber();
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemMatricula()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.MATRICULA.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemMensalidade()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.MENSALIDADE.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemBiblioteca()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.BIBLIOTECA.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemDevolucaoCheque()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemNegociacao()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.NEGOCIACAO.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemBolsaCusteadaConvenio()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemContratoReceita()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.OUTROS.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemOutros()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.OUTROS.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor()) && !itemCondicaoRenegociacaoVO.getTipoOrigemInclusaoReposicao()) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				adicionarItemCondicaoNegociacao = false;
				//throw new Exception("Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + "). A Condição de Renegociação " + itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getDescricao() + " não está habilitada para o Tipo de Origem " + TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao().toUpperCase() + ". Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}

			if (contaReceberNegociadoVO.getNrDiasAtraso() > 0 && itemCondicaoRenegociacaoVO.getTipoParcelaNegociar().equals(TipoParcelaNegociarEnum.A_VENCER)) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				throw new Exception("A Condição de Renegociação está configurada para permitir negociar apenas parcelas que estão A Vencer. Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
			if (contaReceberNegociadoVO.getNrDiasAtraso().equals(0L) && itemCondicaoRenegociacaoVO.getTipoParcelaNegociar().equals(TipoParcelaNegociarEnum.VENCIDAS)) {
				negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(true);
				throw new Exception("A Condição de Renegociação está configurada para permitir negociar apenas parcelas que estão Vencidas. Será necessário realizar a exclusão da Conta Receber de Nosso Número: " + contaReceberVO.getNossoNumero() + ", Parcela: " + contaReceberVO.getParcela() + " e Valor igual a R$" + Uteis.formatarDecimalDuasCasas(contaReceberVO.getValor()) + " para prosseguir com a operação, ou Desativar a Condição de Renegociação.");
			}
		}
		return adicionarItemCondicaoNegociacao;
	}

	public void validarDadosUsuarioLogadoPossuiPermissaoRealizarRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioVO) throws Exception {
		if (condicaoRenegociacaoVO.getListaCondicaoRenegociacaoFuncionarioVOs().isEmpty() || negociacaoContaReceberVO.getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao()) {
			return;
		}
		Boolean possuiPermissao = false;
		for (CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO : condicaoRenegociacaoVO.getListaCondicaoRenegociacaoFuncionarioVOs()) {
			if (usuarioVO.getPessoa().getCodigo().equals(condicaoRenegociacaoFuncionarioVO.getFuncionarioVO().getPessoa().getCodigo())) {
				possuiPermissao = true;
				break;
			}
		}
		if (!possuiPermissao) {
			negociacaoContaReceberVO.setApresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(true);
			throw new Exception("O Usuário " + usuarioVO.getNome().toUpperCase() + " não possui permissão na Condição de Renegociação " + condicaoRenegociacaoVO.getDescricao().toUpperCase() + " para realizar uma Negociação. Item Condição Renegociação Utilizada: (Faixa de Valor de R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorInicial()) + " à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorFinal()) + " - Qtde. Dias Atraso de " + itemCondicaoRenegociacaoVO.getQtdeInicialDiasAtraso() + " à " + itemCondicaoRenegociacaoVO.getQtdeFinalDiasAtraso() + ") ");
		}
	}

	

	public void validarDadosValorMinimoPorParcela(NegociacaoContaReceberVO negociacaoContaReceberVO, OpcaoAlunoCondicaoRenegociacaoVO opcaoAlunoCondicaoRenegociacaoVO, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO = opcaoAlunoCondicaoRenegociacaoVO.getItemCondicaoRenegociacaoVO();
		if (itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().equals(BigDecimal.ZERO)) {
			return;
		}
		if (Uteis.arrendondarForcando2CadasDecimais(opcaoAlunoCondicaoRenegociacaoVO.getValorParcela()) < Uteis.arrendondarForcando2CadasDecimais(itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue())) {
			throw new Exception("Valor Mínimo por parcela menor do que o que está configurado na Condição de Renegociação. O Valor por parcela deve ser igual ou superior à R$" + Uteis.formatarDecimalDuasCasas(itemCondicaoRenegociacaoVO.getValorMinimoPorParcela().doubleValue()) + ".");
		}
	}

	@Override
	public void validarExistenciaItemCondicaoDescontoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		negociacaoContaReceberVO.setValorTotalDescontoPerdido(0.0);
		negociacaoContaReceberVO.setValorConcecaoDescontoPerdidoMaximo(0.0);
		OptionalLong max = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream().mapToLong(p -> p.getNrDiasAtraso()).max();
		if (max.isPresent() && max.getAsLong() > 0L) {
			negociacaoContaReceberVO.setItemCondicaoDescontoRenegociacaoVO(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().consultarItemCondicaoDescontoRenegociacaoDisponivel(max.getAsLong(), negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), usuario));
		}
		if (Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO())) {				
				for (ContaReceberNegociadoVO crn : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
				if (crn.getContaReceber().getNrDiasAtraso() > 0L){
						preencherItemCondicaoDescontoRenegociacao(negociacaoContaReceberVO, conf, crn, usuario);
					}
				}
			if (Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getJuroIsencao())) {
				negociacaoContaReceberVO.setValorIsencaoTotalJuroMaximo(Uteis.arrendondarForcando2CadasDecimais((negociacaoContaReceberVO.getValorTotalJuro() * negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getJuroIsencao().doubleValue())/ 100));
					}
			if (Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getMultaIsencao())) {
				negociacaoContaReceberVO.setValorIsencaoTotalMultaMaximo(Uteis.arrendondarForcando2CadasDecimais((negociacaoContaReceberVO.getValorTotalMulta() * negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getMultaIsencao().doubleValue())/ 100));
			}
			if(!negociacaoContaReceberVO.isLiberarIsencaoJuroMultaDescontoAcimaMaximo()){
				negociacaoContaReceberVO.setTipoDesconto("VA");
				negociacaoContaReceberVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorIsencaoTotalJuro() + negociacaoContaReceberVO.getValorIsencaoTotalMulta() + negociacaoContaReceberVO.getValorConcecaoDescontoPerdido()));
		}
		}else{
			negociacaoContaReceberVO.setDesconto(0.0);
			negociacaoContaReceberVO.setValorIsencaoTotalJuroMaximo(0.0);
			negociacaoContaReceberVO.setValorIsencaoTotalMultaMaximo(0.0);
	}
		negociacaoContaReceberVO.calcularValorTotal();
	}

	private void preencherItemCondicaoDescontoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO conf, ContaReceberNegociadoVO crn, UsuarioVO usuario) throws Exception {
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber = crn.getContaReceber().getListaDescontosAplicavesContaReceber(); 
		if(crn.getContaReceber().getRealizandoRecebimento() || listaDescontosAplicavesContaReceber.isEmpty()) {
			crn.getContaReceber().setRealizandoRecebimento(false);
			listaDescontosAplicavesContaReceber = ContaReceber.gerarListaPlanoFinanceiroAlunoDescricaoDesconto(crn.getContaReceber(), negociacaoContaReceberVO.getData(), conf.getUsaDescontoCompostoPlanoDesconto(), null, usuario, conf);
			crn.getContaReceber().setRealizandoRecebimento(true);
		}
		if (!listaDescontosAplicavesContaReceber.isEmpty()) {
			aplicarDescontoDevidoDataAntecipacao(negociacaoContaReceberVO, crn, listaDescontosAplicavesContaReceber);
			aplicarDescontoProgressivoDevidoDataAntecipacao(negociacaoContaReceberVO, crn, listaDescontosAplicavesContaReceber);
			aplicarDescontoAlunoDevidoDataAntecipacao(negociacaoContaReceberVO, crn, listaDescontosAplicavesContaReceber);
		}
		getFacadeFactory().getContaReceberNegociadoFacade().realizarCalculoValorContaReceberDesconsiderandoDescontos(crn);		
	}

	private void aplicarDescontoAlunoDevidoDataAntecipacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ContaReceberNegociadoVO crn, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber) throws Exception {
		if (negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao()
				&& !listaDescontosAplicavesContaReceber.get(0).getIsAplicavelDataParaQuitacao(crn.getContaReceber().getDataVencimento(), negociacaoContaReceberVO.getData()) 
				&& listaDescontosAplicavesContaReceber.get(0).getValorDescontoAluno() > 0.0) {
			negociacaoContaReceberVO.setValorTotalDescontoPerdido(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotalDescontoPerdido() + listaDescontosAplicavesContaReceber.get(0).getValorDescontoAluno()));	
			negociacaoContaReceberVO.setValorConcecaoDescontoPerdidoMaximo(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorConcecaoDescontoPerdidoMaximo() + ((negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getPercentualDescontoAluno().doubleValue() * listaDescontosAplicavesContaReceber.get(0).getValorDescontoAluno())/100)));	
		}
	}

	private void aplicarDescontoProgressivoDevidoDataAntecipacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ContaReceberNegociadoVO crn, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber) throws Exception {
		if (negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao()
				&& !listaDescontosAplicavesContaReceber.get(0).getIsAplicavelDataParaQuitacao(crn.getContaReceber().getDataVencimento(), negociacaoContaReceberVO.getData())
				&& listaDescontosAplicavesContaReceber.get(0).getValorDescontoProgressivo() > 0.0) {
			negociacaoContaReceberVO.setValorTotalDescontoPerdido(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotalDescontoPerdido() + listaDescontosAplicavesContaReceber.get(0).getValorDescontoProgressivo()));
			negociacaoContaReceberVO.setValorConcecaoDescontoPerdidoMaximo(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorConcecaoDescontoPerdidoMaximo() + ((negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getPercentualDescontoProgressivo().doubleValue() * listaDescontosAplicavesContaReceber.get(0).getValorDescontoProgressivo())/100)));
		} 
	}

	private void aplicarDescontoDevidoDataAntecipacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ContaReceberNegociadoVO crn, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber) throws Exception {
		if (negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao()
				&& !listaDescontosAplicavesContaReceber.get(0).getIsAplicavelDataParaQuitacao(crn.getContaReceber().getDataVencimento(), negociacaoContaReceberVO.getData())) {
			crn.setValorDescontoInstituicaoComValidadeDesconsiderado(0.0);
			for (PlanoDescontoContaReceberVO planoDescontoContaReceberVO : crn.getContaReceber().getPlanoDescontoInstitucionalContaReceber()) {
				 if(!planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade()&& !crn.getDesconsiderarDescontoInstituicaoComValidade()){
					Double valor =  planoDescontoContaReceberVO.getIsPlanoDescontoInstitucional() ? listaDescontosAplicavesContaReceber.get(0).getListaDescontosPlanoDesconto().get(planoDescontoContaReceberVO.getPlanoDescontoVO().getCodigo()) : planoDescontoContaReceberVO.getValorDesconto();
					negociacaoContaReceberVO.setValorTotalDescontoPerdido(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorTotalDescontoPerdido() + valor)); 
					negociacaoContaReceberVO.setValorConcecaoDescontoPerdidoMaximo(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorConcecaoDescontoPerdidoMaximo() + ((negociacaoContaReceberVO.getItemCondicaoDescontoRenegociacaoVO().getPercentualPlanoDesconto().doubleValue() * valor)/100)));
				 }
			}
		}
	}	

	public void realizarGeracaoCentroResultadoComBaseNasContasNegociadas(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuario) throws Exception {
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = realizarGeracaoCentroResultaParaBaseCalculo(negociacaoContaReceberVO);
		if (!centroResultadoOrigemVOs.isEmpty()) {
			Double valorTotalCentroResultados = centroResultadoOrigemVOs.stream().mapToDouble(CentroResultadoOrigemVO::getValor).sum();
			Map<String, Double> mapPorcentagemCentroResultado = new HashMap<String, Double>(0);
			centroResultadoOrigemVOs.forEach(cr -> {
				mapPorcentagemCentroResultado.put(cr.toString(), Uteis.arrendondarForcandoCadasDecimais(((cr.getValor() * 100) / valorTotalCentroResultados), 8));
			});
			// aqui soma o percentual para ver se deu 100%, caso não tenha dado é jogado a
			// diferença no 1 centro de resultado
			Optional<BigDecimal> somaPercentualOptionalBigDecimal = mapPorcentagemCentroResultado.values().stream().map(String::valueOf).map(BigDecimal::new).reduce(BigDecimal::add);
			Double somaPercentual = Uteis.arrendondarForcandoCadasDecimais(somaPercentualOptionalBigDecimal.orElse(BigDecimal.ZERO).doubleValue(), 8);
			if (somaPercentual < 100.0000000 && Uteis.isAtributoPreenchido(centroResultadoOrigemVOs)) {
				mapPorcentagemCentroResultado.put(centroResultadoOrigemVOs.get(0).toString(), Uteis.arrendondarForcandoCadasDecimais(mapPorcentagemCentroResultado.get(centroResultadoOrigemVOs.get(0).toString()) + (100 - somaPercentual), 8));
			} else if(somaPercentual > 100.00000000  && Uteis.isAtributoPreenchido(centroResultadoOrigemVOs)) {
				mapPorcentagemCentroResultado.put(centroResultadoOrigemVOs.get(centroResultadoOrigemVOs.size()-1).toString(), Uteis.arrendondarForcandoCadasDecimais(mapPorcentagemCentroResultado.get(centroResultadoOrigemVOs.get(centroResultadoOrigemVOs.size()-1).toString()) - (somaPercentual - 100.00000000), 8));
			}
			for (ContaReceberVO contaReceberVO : negociacaoContaReceberVO.getNovaContaReceber()) {
				contaReceberVO.getListaCentroResultadoOrigem().clear();
				for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemVOs) {
					CentroResultadoOrigemVO centroResultadoOrigemVO2 = (CentroResultadoOrigemVO) centroResultadoOrigemVO.clone();
					centroResultadoOrigemVO2.setCodigo(0);
					centroResultadoOrigemVO2.setNovoObj(true);
					centroResultadoOrigemVO2.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_PAGAR);
					centroResultadoOrigemVO2.setCodOrigem("");
					centroResultadoOrigemVO2.setValor(0.0);
					centroResultadoOrigemVO2.setPorcentagem(mapPorcentagemCentroResultado.get(centroResultadoOrigemVO.toString()));
					centroResultadoOrigemVO2.calcularValor(contaReceberVO.getValorReceberCalculado());
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(contaReceberVO.getListaCentroResultadoOrigem(), centroResultadoOrigemVO2, contaReceberVO.getValorReceberCalculado(), false, usuario);
				}
			}
		}
	}

	public List<CentroResultadoOrigemVO> realizarGeracaoCentroResultaParaBaseCalculo(NegociacaoContaReceberVO negociacaoContaReceberVO) throws Exception{
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = new ArrayList<CentroResultadoOrigemVO>();
		for(ContaReceberNegociadoVO contaReceberNegociadoVO: negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarAgrupamentoCentroResultadoOrigemVOSomandoValor(centroResultadoOrigemVOs, 
					contaReceberNegociadoVO.getContaReceber().getListaCentroResultadoOrigem(), true);
		}
		return centroResultadoOrigemVOs;
	}	
	
	@Override
	public void consultar(DataModelo dataModelo, String responsavelCadastro, String comissionado, boolean validarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception{
		dataModelo.setListaConsulta(new ArrayList<>());
		dataModelo.setTotalRegistrosEncontrados(0);
		dataModelo.setLimitePorPagina(10);
		StringBuilder sql = new StringBuilder("select negociacaocontareceber.* ");
		sql.append(" from negociacaocontareceber ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = negociacaocontareceber.unidadeensino  ");
		sql.append(" left join pessoa as comissionado on comissionado.codigo = negociacaocontareceber.pessoaComissionada  ");
		sql.append(" left join usuario on usuario.codigo = negociacaocontareceber.responsavel  ");
		sql.append(" left join matricula on matricula.matricula = negociacaocontareceber.matriculaAluno  ");
		sql.append(" left join pessoa as aluno on matricula.aluno = aluno.codigo  ");
		sql.append(" left join parceiro on negociacaocontareceber.parceiro = parceiro.codigo  ");
		sql.append(" left join fornecedor on negociacaocontareceber.fornecedor = fornecedor.codigo  ");
		sql.append(" left join funcionario on negociacaocontareceber.funcionario = funcionario.codigo  ");
		sql.append(" left join pessoa as pessoafuncionario on funcionario.pessoa = pessoafuncionario.codigo  ");
		sql.append(" left join pessoa on negociacaocontareceber.pessoa = pessoa.codigo  ");
		sql.append(" left join centroReceita on negociacaocontareceber.centroReceita = centroReceita.codigo  ");
		sql.append(" where 1=1 ");
		List<Object> nomeSacados = new ArrayList<Object>(0);
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and exists(select contarecebernegociado.codigo from contarecebernegociado inner join contareceber on contareceber.codigo = contarecebernegociado.contareceber  ");
			sql.append(" where  contarecebernegociado.negociacaocontareceber = negociacaocontareceber.codigo ");
			if(Uteis.isAtributoPreenchido(usuarioVO.getUnidadeEnsinoLogado())){
				sql.append(" and contareceber.unidadeensino = ").append(usuarioVO.getUnidadeEnsinoLogado().getCodigo()).append(" ");				
				sql.append(" and contareceber.unidadeensinofinanceira = ").append(unidadeEnsino).append(" limit 1) ");
			}else {
				sql.append(" and (contareceber.unidadeensino = ").append(unidadeEnsino).append(" ");
				sql.append(" or contareceber.unidadeensinofinanceira = ").append(unidadeEnsino).append(") limit 1) ");
}
		}
		if(dataModelo.getCampoConsulta().equals("codigo")) {
			if(Uteis.getIsValorNumerico(dataModelo.getValorConsulta()) && !dataModelo.getValorConsulta().trim().isEmpty()) {
			sql.append(" and negociacaocontareceber.codigo = ? ");
				nomeSacados.add(Integer.valueOf(dataModelo.getValorConsulta()));
		}else {			
				sql.append(" and negociacaocontareceber.codigo > ? ");
				nomeSacados.add(0);
			}
		}else {			
			nomeSacados.add(dataModelo.getValorConsulta() + "%");
			if(dataModelo.getCampoConsulta().equals("matriculaMatricula")) {
				sql.append(" and sem_acentos(matricula.matricula) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("nomeMatricula")) {
				sql.append(" and sem_acentos(aluno.nome) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("nomeParceiro")) {
				sql.append(" and sem_acentos(parceiro.nome) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("responsavelFinanceiro")) {
				sql.append(" and sem_acentos(pessoa.nome) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("matriculaFuncionario")) {
				sql.append(" and sem_acentos(funcionario.matricula) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("nomeFuncionario")) {
				sql.append(" and sem_acentos(pessoafuncionario.nome) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("nomeFornecedor")) {
				sql.append(" and sem_acentos(fornecedor.nome) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("identificadorCentroReceitaCentroReceita")) {
				sql.append(" and sem_acentos(centroReceita.identificadorcentroreceita) ilike sem_acentos(?) ");
			}else if(dataModelo.getCampoConsulta().equals("nossoNumeroContaAntiga")) {
				sql.append(" and exists (select contarecebernegociado.codigo from contarecebernegociado inner join contareceber on contareceber.codigo = contarecebernegociado.contareceber where  contarecebernegociado.negociacaocontareceber = negociacaocontareceber.codigo and contareceber.nossoNumero ilike ? ) ");
			}else if(dataModelo.getCampoConsulta().equals("nossoNumeroNovaConta")) {
				sql.append(" and exists (select contareceber.codigo from contareceber where contareceber.tipoorigem = 'NCR' and  contareceber.codorigem = negociacaocontareceber.codigo::varchar and contareceber.nossoNumero ilike ? ) ");
			}
			if(Uteis.isAtributoPreenchido(comissionado)) {
				sql.append(" and sem_acentos(comissionado.nome) ilike sem_acentos(?) ");
				nomeSacados.add(comissionado+"%");
			}
			if(Uteis.isAtributoPreenchido(responsavelCadastro)) {
				sql.append(" and sem_acentos(usuario.nome) ilike sem_acentos(?) ");
				nomeSacados.add(responsavelCadastro+"%");
			}
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), " negociacaocontareceber.data ", false));
		}
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select count(codigo) as qtde from ("+sql.toString()+") as t", nomeSacados.toArray());
		if(rs.next()) {
			dataModelo.setTotalRegistrosEncontrados(rs.getInt("qtde"));
			sql.append(" order by ");
			if(dataModelo.getCampoConsulta().equals("matriculaMatricula")) {
				sql.append(" negociacaocontareceber.matriculaAluno, ");
			}else if(dataModelo.getCampoConsulta().equals("nomeMatricula")) {
				sql.append(" aluno.nome, ");
			}else if(dataModelo.getCampoConsulta().equals("nomeParceiro")) {
				sql.append(" parceiro.nome, ");
			}else if(dataModelo.getCampoConsulta().equals("responsavelFinanceiro")) {
				sql.append(" pessoa.nome, ");
			}else if(dataModelo.getCampoConsulta().equals("matriculaFuncionario")) {
				sql.append(" funcionario.matricula, ");
			}else if(dataModelo.getCampoConsulta().equals("matriculaFuncionario")) {
				sql.append(" pessoafuncionario.nome, ");
			}else if(dataModelo.getCampoConsulta().equals("nomeFornecedor")) {
				sql.append(" fornecedor.nome, ");
			}else if(dataModelo.getCampoConsulta().equals("identificadorCentroReceitaCentroReceita")) {
				sql.append(" centroReceita.identificadorcentroreceita, ");
			}
			sql.append(" negociacaocontareceber.data ");
			sql.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
			dataModelo.setListaConsulta(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeSacados.toArray()), nivelMontarDados, getAplicacaoControle().getConfiguracaoFinanceiroVO(unidadeEnsino), usuarioVO));
		}
	}

	@Override
	public void validarValorEntrada(NegociacaoContaReceberVO negociacaoContaReceberVO) throws ConsistirException {
		if ((Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()) < negociacaoContaReceberVO.getValorMinimoEntrada() || 
			Uteis.arrendondarForcando2CadasDecimais(negociacaoContaReceberVO.getValorEntrada()) > negociacaoContaReceberVO.getValorMaximoEntrada())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ItemRenociacaoContaReceber_informeUmValorEntre")
					.replace("{0}", Uteis.formatarDoubleParaMoeda((negociacaoContaReceberVO.getValorMinimoEntrada())))
					.replace("{1}", Uteis.formatarDoubleParaMoeda((negociacaoContaReceberVO.getValorMaximoEntrada()))));
		}
	}

	
	public void realizarIsencoesJurosMultaIndiceReajustePorItemCondicaoNegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		negociacaoContaReceberVO.setValorJuroDesconto(0.0);
		negociacaoContaReceberVO.setValorMultaDesconto(0.0);
		negociacaoContaReceberVO.setValorIndiceReajusteDesconto(0.0);
		if(!negociacaoContaReceberVO.getContaReceberNegociadoVOs().isEmpty()) {
			for (ContaReceberNegociadoVO contaReceberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {				
					if(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getIsentarJuroParcela()) {
						negociacaoContaReceberVO.setValorJuroDesconto(negociacaoContaReceberVO.getValorJuroDesconto() + contaReceberNegociadoVO.getContaReceber().getJuro());
					}
					if(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getIsentarMultaParcela()) {
						negociacaoContaReceberVO.setValorMultaDesconto(negociacaoContaReceberVO.getValorMultaDesconto() + contaReceberNegociadoVO.getContaReceber().getMulta());
					}
					if(negociacaoContaReceberVO.getItemCondicaoRenegociacao().getIsentarIndiceReajustePorAtrasoParcela()) {
						negociacaoContaReceberVO.setValorIndiceReajusteDesconto(negociacaoContaReceberVO.getValorIndiceReajusteDesconto() + contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
					}				
			}	
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirAgenteNegativacao(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuarioVO) {
		if(Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getAgenteNegativacaoCobrancaContaReceberVO())) {
			getConexao().getJdbcTemplate().update("update NegociacaoContaReceber set  agenteNegativacaoCobrancaContaReceber = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), negociacaoContaReceberVO.getAgenteNegativacaoCobrancaContaReceberVO().getCodigo(), negociacaoContaReceberVO.getCodigo() );
		}else {
			getConexao().getJdbcTemplate().update("update NegociacaoContaReceber set  agenteNegativacaoCobrancaContaReceber = null where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), negociacaoContaReceberVO.getCodigo() );
}
	}
	
	
	@Override
	 public void adicionarContaReceberNegociado(NegociacaoContaReceberVO negociacaoContaReceberVO,  ContaReceberNegociadoVO contaReceberNegociadoVO, 
			 Boolean desconsiderarDescontoProgressivo, Boolean desconsiderarDescontoAluno, 
			 Boolean desconsiderarDescontoInstituicaoComValidade, Boolean desconsiderarDescontoInstituicaoSemValidade, ConfiguracaoFinanceiroVO conf, UsuarioVO usuarioVO) throws Exception {
	    	
	      	if(contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("REQ") || contaReceberNegociadoVO.getContaReceber().getTipoOrigem().equals("IPS")){
	        	throw new Exception(UteisJSF.internacionalizar("msg_NegociacaoContaReceber_negarRenegociacaoContaReceberProcessoSeletivoRequerimento") + " " + contaReceberNegociadoVO.getContaReceber().getTipoOrigem_apresentar() );
	        } 

	        if (!negociacaoContaReceberVO.getCodigo().equals(0)) {
	            contaReceberNegociadoVO.setNegociacaoContaReceber(negociacaoContaReceberVO);
	        }
	        for (ContaReceberNegociadoVO receberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
	        	if (contaReceberNegociadoVO.getContaReceber().getCodigo().equals(receberNegociadoVO.getContaReceber().getCodigo())) {
	        		throw new ConsistirException("Não é possível adicionar a Conta de código: " + contaReceberNegociadoVO.getContaReceber().getCodigo()  + ", pois a mesma já se encontra adicionada à lista de Conta Receber Negociadas.");
	        	}
	        }
	        getFacadeFactory().getContaReceberNegociadoFacade().realizarCalculoValorContaReceberAdicionar(contaReceberNegociadoVO, desconsiderarDescontoProgressivo, desconsiderarDescontoAluno, 
	        		desconsiderarDescontoInstituicaoComValidade,desconsiderarDescontoInstituicaoSemValidade);
	        negociacaoContaReceberVO.adicionarObjContaReceberNegociadoVOs(contaReceberNegociadoVO);
	        
	        Ordenacao.ordenarLista(negociacaoContaReceberVO.getContaReceberNegociadoVOs(), "ordenacao");
	        getFacadeFactory().getNegociacaoContaReceberFacade().validarExistenciaItemCondicaoDescontoRenegociacao(negociacaoContaReceberVO, conf, usuarioVO);
	        if(!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getAgenteNegativacaoCobrancaContaReceberVO())) {
	        	List<RegistroNegativacaoCobrancaContaReceberItemVO> registros = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().carregarHistoricoNegativacaoContaReceber(contaReceberNegociadoVO.getContaReceber().getCodigo());
	        	if(Uteis.isAtributoPreenchido(registros) && registros.size() > 0) {
	        		negociacaoContaReceberVO.getAgenteNegativacaoCobrancaContaReceberVO().setCodigo(registros.get(0).getCodigoAgente());
	        	}	        	
	        }
	        

	    }   

}
