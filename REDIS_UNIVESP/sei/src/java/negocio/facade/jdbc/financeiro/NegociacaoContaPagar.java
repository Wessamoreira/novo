package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaPagarNegociadoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.NegociacaoContaPagarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>NegociacaoContaReceberVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>NegociacaoContaReceberVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see NegociacaoContaReceberVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class NegociacaoContaPagar extends ControleAcesso implements NegociacaoContaPagarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1178007385713883824L;
	protected static String idEntidade;

	public NegociacaoContaPagar() throws Exception {
		super();
		setIdEntidade("NegociacaoContaPagar");
	}
	
	@Override
	public void validarDados(NegociacaoContaPagarVO obj) throws ConsistirException {		
		validarDadosBasicos(obj);
		if (obj.getNrParcela().intValue() < 0 || (obj.getNrParcela().equals(0) && !obj.getValorEntrada().equals(obj.getValorTotal()))) {
			throw new ConsistirException("O campo  NÚMERO PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoIntervaloParcelaEnum().isIntervaloEntreDias() &&  obj.getIntervaloParcela().intValue() < 0) {
			throw new ConsistirException("O campo INTERVALO PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
		
		if (obj.getTipoIntervaloParcelaEnum().isIntervaloDataBase() &&  obj.getDataBaseParcela() == null) {
			throw new ConsistirException("O campo DATA BASE PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
				
		for(ContaPagarVO conta: obj.getContaPagarGeradaVOs()){
			if(conta.getValorFinalTemp().equals(0.0)){
				throw new ConsistirException("Não é possível gerar uma CONTA PAGAR RENEGOCIADA com valor 0,00.");
			}
			if(conta.getValorFinalTemp() < 0){
				throw new ConsistirException("Não é possível gerar uma CONTA PAGAR RENEGOCIADA com valor menor ou igual a 0,00.");
			}
		}		
	}

	@Override
	public void validarDadosBasicos(NegociacaoContaPagarVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Renegociação Conta Pagar) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoSacado())) {
			throw new ConsistirException("O campo TIPO SACADO (Renegociação Conta Pagar) deve ser informado.");
		}
		if (obj.getTipoAluno()) {
			if (obj.getMatriculaAluno() == null || obj.getMatriculaAluno().getMatricula().equals("")) {
				throw new ConsistirException("O campo ALUNO (Renegociação Conta Pagar) deve ser informado.");
			}
		} else if (obj.getTipoFuncionario()) {
			if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FUNCIONARIO (Renegociação Conta Pagar) deve ser informado.");
			}
		}else if (obj.getTipoFornecedor()) {
			if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FORNECEDOR (Renegociação Conta Pagar) deve ser informado.");
			}
		}else if (obj.getTipoParceiro()) {
			if (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PARCEIRO (Renegociação Conta Pagar) deve ser informado.");
			}
		}else if (obj.getTipoBanco()) {
			if (obj.getBancoVO() == null || obj.getBancoVO().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo BANCO (Renegociação Conta Pagar) deve ser informado.");
			}
		}else if (obj.getTipoOperadoraCartao()) {
			if (obj.getOperadoraCartaoVO() == null || obj.getOperadoraCartaoVO().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo OPERADORA CARTÃO (Renegociação Conta Pagar) deve ser informado.");
			}
		}else if (obj.getTipoResponsavelFinanceiro()) {
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo RESPONSÁVEL FINANCEIRO (Renegociação Conta Pagar) deve ser informado.");
			}
		}	
		if (obj.getJustificativa().equals("")) {
			throw new ConsistirException("O campo JUSTIFICATIVA (Renegociação Conta Pagar) deve ser informado.");
		}
		
		if (obj.getContaPagarNegociadoVOs().isEmpty()) {
			throw new ConsistirException("O campo CONTA PAGAR (Renegociação Conta Pagar) deve ser informado.");
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoContaPagar.incluir(getIdEntidade(), verificarPermissao, usuario);
			calcularValorTotal(obj);
			validarDados(obj);
			validarDadosCalculoValorTotalConfirmacaoNegociacao(obj);			
			
			final StringBuilder sql = new StringBuilder("INSERT INTO NegociacaoContaPagar( data, responsavel, unidadeEnsino, tipoSacado, pessoa, matriculaAluno, ");
			sql.append(" funcionario, valor, juro, multa, desconto, valorTotal, valorEntrada, nrParcela, ");
			sql.append(" intervaloParcela, justificativa, parceiro, fornecedor, ");
			sql.append(" dataBaseParcela, tipoIntervaloParcelaEnum, banco, operadoracartao ) ");
			sql.append(" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");
			sql.append(" returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int x = 1;
					sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getTipoSacado().getValor());
					if ((obj.getTipoAluno() || obj.getTipoResponsavelFinanceiro() || obj.getTipoFuncionario() || obj.getTipoCandidato()) && obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getTipoAluno() && !obj.getMatriculaAluno().getMatricula().equals("")) {
						sqlInserir.setString(x++, obj.getMatriculaAluno().getMatricula());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setDouble(x++, obj.getValor());
					sqlInserir.setDouble(x++, obj.getJuro());
					sqlInserir.setDouble(x++, obj.getMulta());
					sqlInserir.setDouble(x++, obj.getDesconto());
					sqlInserir.setDouble(x++, obj.getValorTotal());					
					sqlInserir.setDouble(x++, obj.getValorEntrada());
					sqlInserir.setInt(x++, obj.getNrParcela().intValue());
					sqlInserir.setInt(x++, obj.getIntervaloParcela().intValue());
					sqlInserir.setString(x++, obj.getJustificativa());					
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}									
					if (obj.getTipoFornecedor() && obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}					
					if(obj.getTipoIntervaloParcelaEnum().isIntervaloDataBase()){
						sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataBaseParcela()));	
					}else{
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getTipoIntervaloParcelaEnum().name());
					if (obj.getTipoBanco() && obj.getBancoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getBancoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}					
					if (obj.getOperadoraCartaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getOperadoraCartaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
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
			getFacadeFactory().getContaPagarNegociadoFacade().incluirContaPagarNegociados(obj, usuario);
			criarNovaContasPagar(obj, usuario);
			for(LancamentoContabilVO lancamentoContabilVO: obj.getLancamentoContabilCreditoVOs()) {
				lancamentoContabilVO.setCodOrigem(obj.getCodigo().toString());
				getFacadeFactory().getLancamentoContabilFacade().persistir(lancamentoContabilVO, false, usuario);
			}
			for(LancamentoContabilVO lancamentoContabilVO: obj.getLancamentoContabilDebitoVOs()) {
				lancamentoContabilVO.setCodOrigem(obj.getCodigo().toString());
				getFacadeFactory().getLancamentoContabilFacade().persistir(lancamentoContabilVO, false, usuario);
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			for(ContaPagarNegociadoVO contaPagarNegociadoVO: obj.getContaPagarNegociadoVOs()){
				contaPagarNegociadoVO.getContaPagarVO().setSituacao("AP");
				contaPagarNegociadoVO.setCodigo(0);
				contaPagarNegociadoVO.setNovoObj(true);
			}
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarJustificativa(final NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoContaPagar.alterar(getIdEntidade(), verificarPermissao, usuario);
			validarDados(obj);								
			final StringBuilder sql = new StringBuilder("UPDATE NegociacaoContaPagar set  justificativa = ? where codigo = ? ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getJustificativa());					
					sqlInserir.setInt(2, obj.getCodigo());										
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarNovaContasPagar(NegociacaoContaPagarVO obj, UsuarioVO usuario) throws Exception {				
		for(ContaPagarVO contaPagar : obj.getContaPagarGeradaVOs()) {			
			contaPagar.setTipoOrigem(OrigemContaPagar.NEGOCICACAO_CONTA_PAGAR.getValor());
			contaPagar.setSituacao("AP");			
			contaPagar.setCodOrigem(obj.getCodigo().toString());
			contaPagar.setTipoSacado(obj.getTipoSacado().getValor());
			contaPagar.setUnidadeEnsino(obj.getUnidadeEnsino());
			contaPagar.setDataFatoGerador(contaPagar.getDataVencimento());
			contaPagar.setOrigemRenegociacaoPagar(obj.getCodigo());
			if (obj.getTipoFornecedor()) {				
				contaPagar.setFornecedor(obj.getFornecedor());
			} else if (obj.getTipoParceiro()) {
				contaPagar.setParceiro(obj.getParceiro());
			} else if (obj.getTipoBanco()) {
				contaPagar.setBanco(obj.getBancoVO());
			} else if (obj.getTipoOperadoraCartao()) {
				contaPagar.setOperadoraCartao(obj.getOperadoraCartaoVO());
			} else if (obj.getTipoResponsavelFinanceiro()) {
				contaPagar.setResponsavelFinanceiro(obj.getPessoa());
			} else if (obj.getTipoFuncionario()) {
				contaPagar.setFuncionario(obj.getFuncionario());
				contaPagar.setPessoa(obj.getPessoa());
			} else if (obj.getTipoAluno()) {
				contaPagar.setMatricula(obj.getMatriculaAluno().getMatricula());
				contaPagar.setPessoa(obj.getPessoa());
			}			
			contaPagar.setValor(Uteis.arrendondarForcando2CadasDecimais(contaPagar.getValorFinalTemp()));
			getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuario);			
		}
	}
	

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>NegociacaoContaReceberVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoContaReceberVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoContaPagar.excluir(getIdEntidade(), verificarPermissao, usuario);
			for (ContaPagarVO contaPagarVO : obj.getContaPagarGeradaVOs()) {
				if(contaPagarVO.getSituacao().equals(SituacaoFinanceira.A_PAGAR.getValor())) {
					getFacadeFactory().getContaPagarFacade().excluir(contaPagarVO, false, usuario);
				}else {
					throw new Exception("Não é possível excluir esta negociação pois a conta a pagar com vencimento "+contaPagarVO.getDataVencimento_Apresentar()+" está com a situação "+contaPagarVO.getSituacao_Apresentar());
				}
			}

			getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR, false, usuario);
			String sql = "DELETE FROM NegociacaoContaPagar WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });		
			for (ContaPagarNegociadoVO contaPagarNegociadoVO : obj.getContaPagarNegociadoVOs()) {								
				getFacadeFactory().getContaPagarFacade().alterarSituacao(contaPagarNegociadoVO.getContaPagarVO().getCodigo(), SituacaoFinanceira.A_PAGAR, usuario);				
			}
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoContaReceberVO</code> resultantes da consulta.
	 */
	public static List<NegociacaoContaPagarVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<NegociacaoContaPagarVO> vetResultado = new ArrayList<NegociacaoContaPagarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>NegociacaoContaReceberVO</code>.
	 * 
	 * @return O objeto da classe <code>NegociacaoContaReceberVO</code> com os
	 *         dados devidamente montados.
	 */
	public static NegociacaoContaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		NegociacaoContaPagarVO obj = new NegociacaoContaPagarVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setTipoSacado(TipoSacado.getEnum(dadosSQL.getString("tipoSacado")));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setJuro(dadosSQL.getDouble("juro"));		
		obj.setNrParcela(dadosSQL.getInt("nrParcela"));
		obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
		obj.setValorEntrada(dadosSQL.getDouble("valorEntrada"));
		obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaAluno"));
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
		obj.getFornecedor().setCodigo(dadosSQL.getInt("fornecedor"));
		obj.getBancoVO().setCodigo(dadosSQL.getInt("banco"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
		obj.setMulta(dadosSQL.getDouble("multa"));		
		obj.setIntervaloParcela(dadosSQL.getInt("intervaloParcela"));
		obj.setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum.valueOf(dadosSQL.getString("tipoIntervaloParcelaenum")));
		obj.setDataBaseParcela(dadosSQL.getDate("dataBaseParcela"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosParceiro(obj,  Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);		
		montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);		
		montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX || nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS || nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}		
		obj.setContaPagarNegociadoVOs(getFacadeFactory().getContaPagarNegociadoFacade().consultarPorCodigoNegociacaoContaPagar(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setContaPagarGeradaVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(obj.getCodigo().toString(), OrigemContaPagar.NEGOCICACAO_CONTA_PAGAR.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		obj.setLancamentoContabilCreditoVOs(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR, TipoPlanoContaEnum.CREDITO, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
		obj.setLancamentoContabilDebitoVOs(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR, TipoPlanoContaEnum.DEBITO, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));

		return obj;
	}

	public static void montarDadosFornecedor(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public static void montarDadosBanco(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getBancoVO().getCodigo())) {			
			obj.setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoVO().getCodigo(), false, nivelMontarDados, usuario));
		}
	}
	
	public static void montarDadosOperadoraCartao(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getOperadoraCartaoVO().getCodigo())) {			
			obj.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartaoVO().getCodigo(), nivelMontarDados, usuario));
		}
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FuncionarioVO</code> relacionado ao objeto
	 * <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da
	 * classe <code>FuncionarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFuncionario(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da
	 * classe <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatriculaAluno(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatriculaAluno().getMatricula() == null) || (obj.getMatriculaAluno().getMatricula().equals(""))) {
			obj.setMatriculaAluno(new MatriculaVO());
			return;
		}
		obj.setMatriculaAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaAluno().getMatricula(), 0, NivelMontarDados.BASICO, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da
	 * classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoa(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosParceiro(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UnidadeEnsinoVO</code> relacionado ao objeto
	 * <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da
	 * classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto
	 * <code>NegociacaoContaReceberVO</code>. Faz uso da chave primária da
	 * classe <code>UsuarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(NegociacaoContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>NegociacaoContaReceberVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public NegociacaoContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM NegociacaoContaPagar WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoContaPagar ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return NegociacaoContaPagar.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NegociacaoContaPagar.idEntidade = idEntidade;
	}

	public Double calcularValorTotalConfirmacaoNegociacao(NegociacaoContaPagarVO obj) {
		Double valorTotalTemp = 0.0;		
		for (ContaPagarVO contaPagarVO : obj.getContaPagarGeradaVOs()) {						
			valorTotalTemp = valorTotalTemp + contaPagarVO.getValorFinalTemp();			
		}
		calcularValorTotal(obj);
		return Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp);
	}

	public void validarDadosCalculoValorTotalConfirmacaoNegociacao(NegociacaoContaPagarVO obj) throws Exception {
		Double valorTotalTemp = calcularValorTotalConfirmacaoNegociacao(obj);		
		if (!obj.getValorTotal().equals(Uteis.arrendondarForcando2CadasDecimais(valorTotalTemp))) {
			throw new Exception("A soma das parcelas negociadas estão diferentes do valor total.");
		}		
	}		
	
	@Override
	public void gerarParcelas(NegociacaoContaPagarVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getValorTotalPrevisto() < 0.0) {
			throw new ConsistirException("O campo VALOR TOTAL PREVISTO (Renegociação Conta Pagar) deve ser MAIOR que ZERO.");
		}
		validarDados(obj);
		
		StringBuilder nrDoc = new StringBuilder();
		StringBuilder nrNota = new StringBuilder();
		StringBuilder descricao = new StringBuilder();
		for(ContaPagarNegociadoVO contaPagarNegociadoVO: obj.getContaPagarNegociadoVOs()) {
			if(!contaPagarNegociadoVO.getContaPagarVO().getNrDocumento().trim().isEmpty() 
				&& !nrDoc.toString().contains(contaPagarNegociadoVO.getContaPagarVO().getNrDocumento().trim())
				&& nrDoc.length() < 50) {
				if(nrDoc.length() > 0) {
					nrDoc.append(", ");
				}
				nrDoc.append(contaPagarNegociadoVO.getContaPagarVO().getNrDocumento().trim());
				if(nrDoc.length() > 50 ) {
					nrDoc = new StringBuilder(nrDoc.substring(0, 50));
				}	
			}
			
			if(!contaPagarNegociadoVO.getContaPagarVO().getNumeroNotaFiscalEntrada().trim().isEmpty() 
					&& !nrNota.toString().contains(contaPagarNegociadoVO.getContaPagarVO().getNumeroNotaFiscalEntrada().trim())
					&& nrNota.length() < 250) {
					if(nrNota.length() > 0) {
						nrNota.append(", ");
					}
					nrNota.append(contaPagarNegociadoVO.getContaPagarVO().getNumeroNotaFiscalEntrada().trim());
					if(nrNota.length() > 250 ) {
						nrNota = new StringBuilder(nrNota.substring(0, 250));
					}
			}
			
			if(!contaPagarNegociadoVO.getContaPagarVO().getDescricao().trim().isEmpty() 
					&& !descricao.toString().contains(contaPagarNegociadoVO.getContaPagarVO().getDescricao().trim())
					&& descricao.length() < 255) {
					if(descricao.length() > 0) {
						descricao.append(", ");
					}					
					descricao.append(contaPagarNegociadoVO.getContaPagarVO().getDescricao().trim());
					if(descricao.length() > 255 ) {
						descricao = new StringBuilder(descricao.substring(0, 255));
					}										
			}
			
		}
		
		int nrParcela = 1;		
		obj.getContaPagarGeradaVOs().clear();
		
		
		if (obj.getValorEntrada() > 0) {
			int tam = obj.getNrParcela() + 1;
			obj.getContaPagarGeradaVOs().add(crirContaPagar("1N" + tam, obj.getValorEntrada(), new Date(), descricao.toString(), nrNota.toString(), nrDoc.toString(), obj, usuarioVO));
		}
		
		if (obj.getValorEntrada() < obj.getValorTotalPrevisto()) {
			Double valor = Uteis.arrendondarForcando2CadasDecimais(((obj.getValor() - obj.getValorEntrada()) / obj.getNrParcela().intValue()));
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
					Double resto = calcularRestoUltimaParcelaNegociacao(valor, obj.getValor(), obj.getValorEntrada(), nrParcela);					
					if (!resto.equals(0.0)) {
						Boolean somar = verificarAcaoUltimaParcelaNegociacao(valor, obj.getValor(), obj.getValorEntrada(), nrParcela);
						if (somar) {
							valor = valor + resto;
						} else {
							valor = valor - resto;
						}
					}
				}

				if (obj.getValorEntrada() > 0) {
					int tam = obj.getNrParcela() + 1;
					obj.getContaPagarGeradaVOs().add(crirContaPagar(nrParcela + 1 + "N" + tam, valor, dataPrevisao, descricao.toString(), nrNota.toString(), nrDoc.toString(), obj, usuarioVO));
				} else {
					obj.getContaPagarGeradaVOs().add(crirContaPagar(nrParcela + "N" + obj.getNrParcela(), valor, dataPrevisao, descricao.toString(), nrNota.toString(), nrDoc.toString(), obj, usuarioVO));
				}
				nrParcela++;
			}			
		}		
		realizarGeracaoCentroResultadoComBaseNasContasNegociadas(obj, usuarioVO);				
	}
	
	@Override
	public Double realizarCalculoValorBaseParcela(NegociacaoContaPagarVO negociacaoContaPagarVO) throws Exception  {
		Double valor = 0.0;
		negociacaoContaPagarVO.setValorTotalPrevisto(negociacaoContaPagarVO.getValor());
		negociacaoContaPagarVO.setJuro(0.0);
		negociacaoContaPagarVO.setMulta(0.0);
		negociacaoContaPagarVO.setDesconto(0.0);
		if(negociacaoContaPagarVO.getNrParcela() > 0){
			valor = (Uteis.arrendondarForcando2CadasDecimais(((negociacaoContaPagarVO.getValor() - negociacaoContaPagarVO.getValorEntrada()) / negociacaoContaPagarVO.getNrParcela().intValue()))+negociacaoContaPagarVO.getJuroPorParcela()+negociacaoContaPagarVO.getMultaPorParcela()-negociacaoContaPagarVO.getDescontoPorParcela());
			if (valor < 0.0) {
				negociacaoContaPagarVO.setDescontoPorParcela(0.0);
				throw new Exception("O Valor Total Não Pode Ser Menor Que Zero.");
			}
			negociacaoContaPagarVO.setJuro(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getNrParcela()*negociacaoContaPagarVO.getJuroPorParcela()));
			negociacaoContaPagarVO.setMulta(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getNrParcela()*negociacaoContaPagarVO.getMultaPorParcela()));
			negociacaoContaPagarVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getNrParcela()*negociacaoContaPagarVO.getDescontoPorParcela()));
			negociacaoContaPagarVO.setValorTotalPrevisto(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getValor()+negociacaoContaPagarVO.getJuro()+negociacaoContaPagarVO.getMulta()-negociacaoContaPagarVO.getDesconto()));
		}
		if(negociacaoContaPagarVO.getValorEntrada() > 0) {
			if (negociacaoContaPagarVO.getValorTotalPrevisto()+negociacaoContaPagarVO.getJuroPorParcela()+negociacaoContaPagarVO.getMultaPorParcela()-negociacaoContaPagarVO.getDescontoPorParcela() < 0.0) {
				negociacaoContaPagarVO.setDescontoPorParcela(0.0);
				throw new Exception("O Valor Total Não Pode Ser Menor Que Zero.");
			}
			negociacaoContaPagarVO.setValorTotalPrevisto(negociacaoContaPagarVO.getValorTotalPrevisto()+negociacaoContaPagarVO.getJuroPorParcela()+negociacaoContaPagarVO.getMultaPorParcela()-negociacaoContaPagarVO.getDescontoPorParcela());
			negociacaoContaPagarVO.setJuro(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getJuro() + negociacaoContaPagarVO.getJuroPorParcela()));
			negociacaoContaPagarVO.setMulta(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getMulta() + negociacaoContaPagarVO.getMultaPorParcela()));
			negociacaoContaPagarVO.setDesconto(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getDesconto() + negociacaoContaPagarVO.getDescontoPorParcela()));
		}
		return valor;
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

	public ContaPagarVO crirContaPagar(String nrParcela, Double valor, Date dataVencimento, String descricao, String nrNota, String nrDoc, NegociacaoContaPagarVO obj, UsuarioVO usuarioVO) throws Exception {
		ContaPagarVO contaPagar = new ContaPagarVO();
		contaPagar.setParcela(nrParcela);
		contaPagar.setValorTemp(valor);		
		contaPagar.setDescricao(descricao);
		contaPagar.setNumeroNotaFiscalEntrada(nrNota);
		contaPagar.setNrDocumento(nrDoc);
		contaPagar.setDataVencimento(dataVencimento);		
		contaPagar.setTipoSacado(obj.getTipoSacado().getValor());
		contaPagar.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
		contaPagar.setDescricao(obj.getJustificativa());
		contaPagar.setJuroTemp(obj.getJuroPorParcela());
		contaPagar.setMultaTemp(obj.getMultaPorParcela());
		contaPagar.setDescontoTemp(obj.getDescontoPorParcela());
		obj.setData(new Date());			
		return contaPagar;
	}	

	@Override
	public List<NegociacaoContaPagarVO> consultar(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select negociacaocontapagar.* from negociacaocontapagar ");
		sql.append(" left join fornecedor on fornecedor.codigo = negociacaocontapagar.fornecedor ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = negociacaocontapagar.operadoracartao ");
		sql.append(" left join parceiro on parceiro.codigo = negociacaocontapagar.parceiro  ");
		sql.append(" left join pessoa on pessoa.codigo = negociacaocontapagar.pessoa ");
		sql.append(" left join banco on banco.codigo = negociacaocontapagar.banco ");
		if(!Uteis.isAtributoPreenchido(valorConsulta)) {
			sql.append(" where 1 = 1 ");
		}else {
			sql.append(" where (sem_acentos(fornecedor.nome) ilike(sem_acentos(?))");
			sql.append(" or sem_acentos(parceiro.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(banco.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(operadoracartao.nome) ilike(sem_acentos(?)))");
		}
		sql.append(" and ").append(super.realizarGeracaoWherePeriodo(dataIni, dataFim, "negociacaocontapagar.data", false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and negociacaocontapagar.unidadeensino = ").append(unidadeEnsino);
		}
		sql.append(" order by negociacaocontapagar.data, pessoa.nome, fornecedor.nome, operadoracartao.nome, banco.nome, parceiro.nome  ");
		if(Uteis.isAtributoPreenchido(limite)) {
			sql.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		if(!Uteis.isAtributoPreenchido(valorConsulta)) {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString() ), nivelMontarDados, usuario);
		}
		valorConsulta = valorConsulta+"%";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta, valorConsulta, valorConsulta, valorConsulta, valorConsulta), nivelMontarDados, usuario);
	}

	@Override
	public Integer consultarTotalRegistro(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("select count(negociacaocontapagar.codigo) as qtde from negociacaocontapagar ");
		sql.append(" left join fornecedor on fornecedor.codigo = negociacaocontapagar.fornecedor ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = negociacaocontapagar.operadoracartao ");
		sql.append(" left join parceiro on parceiro.codigo = negociacaocontapagar.parceiro  ");
		sql.append(" left join pessoa on pessoa.codigo = negociacaocontapagar.pessoa ");
		sql.append(" left join banco on banco.codigo = negociacaocontapagar.banco ");
		if(!Uteis.isAtributoPreenchido(valorConsulta)) {
			sql.append(" where 1 = 1 ");
		}else {
			sql.append(" where (sem_acentos(fornecedor.nome) ilike(sem_acentos(?))");
			sql.append(" or sem_acentos(parceiro.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(banco.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
			sql.append(" or sem_acentos(operadoracartao.nome) ilike(sem_acentos(?)))");
		}
		sql.append(" and ").append(super.realizarGeracaoWherePeriodo(dataIni, dataFim, "negociacaocontapagar.data", false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and negociacaocontapagar.unidadeensino = ").append(unidadeEnsino);
		}		
		SqlRowSet rs = null;
		if(!Uteis.isAtributoPreenchido(valorConsulta)) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta, valorConsulta, valorConsulta, valorConsulta, valorConsulta);
		}
		if(rs.next()) {
			return rs.getInt("qtde");
		}
		return 0; 
	}
	
	@Override
	public void adicionarObjContaPagarNegociadoVOs(NegociacaoContaPagarVO negociacaoContaPagarVO, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getContaPagarFacade().validarSeContaPagarExisteVinculoComArquivoRemessa(contaPagarVO);
		ContaPagarNegociadoVO contaPagarNegociadoVO =  new ContaPagarNegociadoVO();
		contaPagarNegociadoVO.setContaPagarVO(contaPagarVO);
		contaPagarNegociadoVO.setValor(contaPagarVO.getPrevisaoValorPago());		
		if(!negociacaoContaPagarVO.getContaPagarNegociadoVOs().contains(contaPagarNegociadoVO)) {
			contaPagarNegociadoVO.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			negociacaoContaPagarVO.getContaPagarNegociadoVOs().add(contaPagarNegociadoVO);			
			negociacaoContaPagarVO.setValor(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getValor() + contaPagarNegociadoVO.getValor()));			
			calcularValorTotal(negociacaoContaPagarVO);
		}
	}

	@Override
	public void removerObjContaPagarNegociadoVOs(NegociacaoContaPagarVO negociacaoContaPagarVO, ContaPagarNegociadoVO obj) {	
		if(negociacaoContaPagarVO.getContaPagarNegociadoVOs().contains(obj)) {
			negociacaoContaPagarVO.getContaPagarNegociadoVOs().remove(obj);
			negociacaoContaPagarVO.setValor(Uteis.arrendondarForcando2CadasDecimais(negociacaoContaPagarVO.getValor() - obj.getValor()));			
			calcularValorTotal(negociacaoContaPagarVO);
		}
	}

	@Override
	public void calcularValorTotal(NegociacaoContaPagarVO negociacaoContaPagarVO) {
        Double valor = negociacaoContaPagarVO.getValor();           
        realizarCalculoTotalJuroMultaDescontoAplicadoNasContas(negociacaoContaPagarVO);
        valor += negociacaoContaPagarVO.getJuro()+negociacaoContaPagarVO.getMulta();
        valor -= negociacaoContaPagarVO.getDesconto();
    	negociacaoContaPagarVO.setValorTotal(Uteis.arrendondarForcando2CadasDecimais(valor));
    }
	
	public void realizarCalculoTotalJuroMultaDescontoAplicadoNasContas(NegociacaoContaPagarVO negociacaoContaPagarVO) {
		negociacaoContaPagarVO.setJuro(0.0);
        negociacaoContaPagarVO.setMulta(0.0);
        negociacaoContaPagarVO.setDesconto(0.0);
		for(ContaPagarVO contaPagarVO: negociacaoContaPagarVO.getContaPagarGeradaVOs()) {
			negociacaoContaPagarVO.setJuro(negociacaoContaPagarVO.getJuro()+contaPagarVO.getJuroTemp());
	        negociacaoContaPagarVO.setMulta(negociacaoContaPagarVO.getMulta()+contaPagarVO.getMultaTemp());
	        negociacaoContaPagarVO.setDesconto(negociacaoContaPagarVO.getDesconto()+contaPagarVO.getDescontoTemp());
		}
	}
	
	public void realizarGeracaoCentroResultadoComBaseNasContasNegociadas(NegociacaoContaPagarVO negociacaoContaPagarVO, UsuarioVO usuario) throws Exception {
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = realizarGeracaoCentroResultaParaBaseCalculo(negociacaoContaPagarVO);
		if (!centroResultadoOrigemVOs.isEmpty()) {
			Double valorTotalCentroResultados = centroResultadoOrigemVOs.stream().mapToDouble(CentroResultadoOrigemVO::getValor).sum();
			Map<String, Double> mapPorcentagemCentroResultado = new HashMap<String, Double>(0);
			centroResultadoOrigemVOs.forEach(cr -> {
				mapPorcentagemCentroResultado.put(cr.toString(), Uteis.arrendondarForcandoCadasDecimais((cr.getValor() * 100) / valorTotalCentroResultados, 8));
			});
			// aqui soma o percentual para ver se deu 100%, caso não tenha dado é jogado a
			// diferença no 1 centro de resultado
			Double somaPercentual = mapPorcentagemCentroResultado.values().stream().mapToDouble(p -> p).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
			if (somaPercentual < 100.00000000) {
				mapPorcentagemCentroResultado.put(centroResultadoOrigemVOs.get(0).toString(), Uteis.arrendondarForcandoCadasDecimais(mapPorcentagemCentroResultado.get(centroResultadoOrigemVOs.get(0).toString()) + (100 - somaPercentual), 8));
			}
			for (ContaPagarVO contaPagarVO : negociacaoContaPagarVO.getContaPagarGeradaVOs()) {
				for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemVOs) {
					CentroResultadoOrigemVO centroResultadoOrigemVO2 = (CentroResultadoOrigemVO) centroResultadoOrigemVO.clone();
					centroResultadoOrigemVO2.setCodigo(0);
					centroResultadoOrigemVO2.setNovoObj(true);
					centroResultadoOrigemVO2.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_PAGAR);
					centroResultadoOrigemVO2.setCodOrigem("");
					centroResultadoOrigemVO2.setValor(0.0);
					centroResultadoOrigemVO2.setPorcentagem(mapPorcentagemCentroResultado.get(centroResultadoOrigemVO.toString()));
					centroResultadoOrigemVO2.calcularValor(contaPagarVO.getValorFinalTemp());
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(contaPagarVO.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO2, contaPagarVO.getValorFinalTemp(), false, usuario);
				}
			}
		}
	}
	
	public List<CentroResultadoOrigemVO> realizarGeracaoCentroResultaParaBaseCalculo(NegociacaoContaPagarVO negociacaoContaPagarVO) throws Exception{
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = new ArrayList<CentroResultadoOrigemVO>();
		for(ContaPagarNegociadoVO contaPagarNegociadoVO: negociacaoContaPagarVO.getContaPagarNegociadoVOs()) {
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarAgrupamentoCentroResultadoOrigemVOSomandoValor(centroResultadoOrigemVOs, contaPagarNegociadoVO.getContaPagarVO().getListaCentroResultadoOrigemVOs(), true);
		}
		return centroResultadoOrigemVOs;
	}	
	
	@Override
	public void realizarGeracaoLancamentoContabilJuroMultaDesconto(NegociacaoContaPagarVO negociacaoContaPagarVO, Boolean forcarRecarregamentoConfiguracaoContabil, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorNegociacaoContaPagar(negociacaoContaPagarVO, forcarRecarregamentoConfiguracaoContabil, usuarioVO);
	}
	
}


//alter table negociacaocontapagar  drop column acrescimo;
//alter table negociacaocontapagar  drop column acrescimoGeral;
//alter table negociacaocontapagar  drop column totalAcrescimoPorParcela;
//alter table negociacaocontapagar  drop column acrescimoPorParcela;
//alter table negociacaocontapagar  drop column tipoAcrescimoPorParcela;