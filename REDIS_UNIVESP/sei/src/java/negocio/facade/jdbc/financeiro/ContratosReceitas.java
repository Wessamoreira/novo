package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ContratoReceitaAlteracaoValorVO;
import negocio.comuns.financeiro.ContratoReceitaEspecificoVO;
import negocio.comuns.financeiro.ContratosDespesasVO;
import negocio.comuns.financeiro.ContratosReceitasVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.ContratoReceitaSituacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContratosReceitasInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ContratosDespesasVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ContratosDespesasVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ContratosDespesasVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ContratosReceitas extends ControleAcesso implements ContratosReceitasInterfaceFacade {

	protected static String idEntidade;

	public ContratosReceitas() throws Exception {
		super();
		setIdEntidade("ContratosReceitas");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ContratosDespesasVO</code>.
	 */
	public ContratosReceitasVO novo() throws Exception {
		ContratosReceitas.incluir(getIdEntidade());
		ContratosReceitasVO obj = new ContratosReceitasVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ContratosDespesasVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContratosReceitasVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			ContratosReceitasVO.validarDados(obj);
			ContratosReceitas.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO ContratosReceitas(valorParcela, diaVencimento, mesVencimento, situacao, contratoIndeterminado, dataTermino, dataInicio, descricao, tipoContrato, sacado, dataPrimeiraParcela, unidadeEnsino, categoriaReceita, tipoSacado, contaCorrente, dataInicioVigencia, dataTerminoVigencia, quantidadeParcela, matricula, departamento ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setDouble(1, obj.getValorParcela().doubleValue());
					sqlInserir.setString(2, obj.getDiaVencimento());
					sqlInserir.setString(3, obj.getMesVencimento());
					sqlInserir.setString(4, obj.getSituacao().getValor());
					sqlInserir.setBoolean(5, obj.isContratoIndeterminado().booleanValue());
					if (obj.getContratoIndeterminado().booleanValue() == false) {
						sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataTermino()));
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlInserir.setString(8, obj.getDescricao());
					sqlInserir.setString(9, obj.getTipoContrato());
					sqlInserir.setInt(10, obj.getSacado().intValue());
					sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataPrimeiraParcela()));
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					if (obj.getCentroReceitaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getCentroReceitaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setString(14, obj.getTipoSacado());
					if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(15, obj.getContaCorrenteVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataInicioVigencia()));
					sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDataTerminoVigencia()));
					sqlInserir.setInt(18, obj.getQuantidadeParcelas().intValue());
					sqlInserir.setString(19, obj.getMatriculaVO().getMatricula());
					int i=19;
					Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getContratoReceitaEspecificoFacade().incluirContratoReceitaEspecificos(obj.getCodigo().intValue(), obj.getContratoReceitaEspecificoVOs());
			if (obj.getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO)) {
				criarContaReceber(obj, configuracaoFinanceiro, usuario);
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}				
			
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Método responsavel para criar as Contas a pagar pertinentes ao contrato
	 * de despesa já autorizado
	 * 
	 * @param obj
	 *            = Contrato de Despesa Autorizado
	 */
	public void criarContaReceber(ContratosReceitasVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		if (obj.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
			obj.getPessoa().setAluno(true);
			obj.getMatriculaPeriodoVO().setCodigo(getFacadeFactory().getMatriculaPeriodoFacade().consultarCodigoMatriculaPeriodoUltimoPeriodoPorMatricula(obj.getMatriculaVO().getMatricula()));
		} else if (obj.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			obj.getPessoa().setAluno(false);
			obj.getPessoa().setFuncionario(true);
		} else {
			obj.getPessoa().setAluno(false);
		}
		
		if (obj.getTipoContrato().equals("ME")) {
			criarContaReceberContratoReceitaMensal(obj, configuracaoFinanceiro, usuario);
		} else if (obj.getTipoContrato().equals("AN")) {
			criarContaReceberContratoReceitaAnual(obj, configuracaoFinanceiro, usuario);
		} else if (obj.getTipoContrato().equals("ES")) {
			criarContaReceberContratoReceitaEspecifico(obj, configuracaoFinanceiro, usuario);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarContaReceberContratoReceitaMensal(ContratosReceitasVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		int qtdeContaReceberContrato = 0;
		int qtDeMesesPeriodoVigencia = 0;
		obj.setDataInicioVigencia(UteisData.getPrimeiroDataMes(obj.getDataInicioVigencia()));
		obj.setDataTerminoVigencia(UteisData.getUltimaDataMes(obj.getDataTerminoVigencia()));
		if (obj.getContratoIndeterminado()) {
			qtdeContaReceberContrato = 12;
		} else {
			// qtdeContaReceberContrato =
			// Uteis.getCalculaQuantidadeMesesEntreDatas(obj.getDataInicio(),
			// obj.getDataTermino());
			qtdeContaReceberContrato = obj.getQuantidadeParcelas();
			qtDeMesesPeriodoVigencia = UteisData.obterQuantidadeMesesPeriodo(obj.getDataInicioVigencia(), obj.getDataTerminoVigencia());
		}
		if (qtDeMesesPeriodoVigencia < qtdeContaReceberContrato && !obj.getContratoIndeterminado()) {
			throw new Exception("Não é Possível Gerar a Quantia de Parcelas Informadas Para Este Período.");
		}
		int nrParcelas = 1;
		Date dataVencimento = Uteis.getDateSemHora(obj.getDataInicioVigencia());
		if (Integer.parseInt(obj.getDiaVencimento()) < UteisData.getDiaMesData(obj.getDataInicioVigencia())) {
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
		} else {
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 0);
		}

		usuario.setUsuarioJaLiberouRegistroCompetenciaFechada(obj.verificarBloqueioCompetenciaFoiLiberado());
		while (nrParcelas <= qtdeContaReceberContrato) {
			if (dataVencimento.before(obj.getDataTerminoVigencia()) || obj.getDataTerminoVigencia().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado().booleanValue()) {
				PessoaVO pessoaVO = obj.getPessoa().getClone();
				getFacadeFactory().getContaReceberFacade().criarContaReceber(obj.getMatriculaVO(), obj.getParceiro(), pessoaVO, obj.getUnidadeEnsino(), obj.getUnidadeEnsino(), obj.getContaCorrenteVO(), obj.getCodigo(), "CTR", dataVencimento, dataVencimento, obj.getValorParcela(), obj.getCentroReceitaVO().getCodigo().intValue(), nrParcelas, qtdeContaReceberContrato, "OU", null, configuracaoFinanceiro, usuario, obj.getFornecedor(), obj.getMatriculaPeriodoVO().getCodigo(), "", obj.getDepartamentoVO());
			}
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
			nrParcelas++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarContaReceberContratoReceitaAnual(ContratosReceitasVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		int qtdeContaReceberContrato = 0;
		int qtdDeAnosPeriodo = 0;
		usuario.setUsuarioJaLiberouRegistroCompetenciaFechada(obj.verificarBloqueioCompetenciaFoiLiberado());
		
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(obj.getDataInicioVigencia());
		startCalendar.set(Calendar.YEAR, UteisData.getAnoData(obj.getDataInicioVigencia()));
		startCalendar.set(Calendar.MONTH, 0);
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		obj.setDataInicioVigencia(startCalendar.getTime());
		
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(obj.getDataInicioVigencia());
		endCalendar.set(Calendar.YEAR, UteisData.getAnoData(obj.getDataTerminoVigencia()));
		endCalendar.set(Calendar.MONTH, 11);
		endCalendar.set(Calendar.DAY_OF_MONTH, 31);
		obj.setDataTerminoVigencia(endCalendar.getTime());
		
		qtdDeAnosPeriodo = UteisData.obterQuantidadeAnosPeriodo(obj.getDataInicioVigencia(), obj.getDataTerminoVigencia());
		
		if (obj.getContratoIndeterminado()) {
			qtdeContaReceberContrato = 1;
		} else {
			// qtdeContaReceberContrato =
			// Uteis.getCalculaQuantidadeAnosEntreDatas(obj.getDataInicio(),
			// obj.getDataTermino());
			qtdeContaReceberContrato = obj.getQuantidadeParcelas();
			
			if (qtdDeAnosPeriodo < qtdeContaReceberContrato){
				throw new Exception("Não é Possível Gerar a Quantia de Parcelas Informadas Para Este Período.");
			}
		}
		int nrParcelas = 1;
		while (nrParcelas <= qtdeContaReceberContrato) {
			int ano = Uteis.getAnoData(obj.getDataPrimeiraParcela()) + nrParcelas - 1;
			Date dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			if (dataVencimento.before(obj.getDataInicioVigencia())) {
				ano++;
				dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			}
			if (dataVencimento.before(obj.getDataTerminoVigencia()) || obj.getDataTerminoVigencia().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado()) {
				PessoaVO pessoaVO = obj.getPessoa().getClone();
				getFacadeFactory().getContaReceberFacade().criarContaReceber(obj.getMatriculaVO(), obj.getParceiro(), pessoaVO, obj.getUnidadeEnsino(), obj.getUnidadeEnsino(), obj.getContaCorrenteVO(), obj.getCodigo(), "CTR", dataVencimento, dataVencimento, obj.getValorParcela(), obj.getCentroReceitaVO().getCodigo().intValue(), nrParcelas, qtdeContaReceberContrato, "OU", null, configuracaoFinanceiro, usuario, obj.getFornecedor(), obj.getMatriculaPeriodoVO().getCodigo(), "", obj.getDepartamentoVO());
			}
			nrParcelas++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarContaReceberContratoReceitaEspecifico(ContratosReceitasVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		usuario.setUsuarioJaLiberouRegistroCompetenciaFechada(obj.verificarBloqueioCompetenciaFoiLiberado());
		int totalParcela = obj.getContratoReceitaEspecificoVOs().size();
		Iterator i = obj.getContratoReceitaEspecificoVOs().iterator();
		int x = 1;
		while (i.hasNext()) {
			ContratoReceitaEspecificoVO especifico = (ContratoReceitaEspecificoVO) i.next();
			PessoaVO pessoaVO = obj.getPessoa().getClone();
			getFacadeFactory().getContaReceberFacade().criarContaReceber(obj.getMatriculaVO(), obj.getParceiro(), pessoaVO, obj.getUnidadeEnsino(), obj.getUnidadeEnsino(), obj.getContaCorrenteVO(), obj.getCodigo(), "CTR", especifico.getDataVencimento(), especifico.getDataVencimento(), especifico.getValorParcela(), obj.getCentroReceitaVO().getCodigo().intValue(), x, totalParcela, "OU", null, configuracaoFinanceiro, usuario, obj.getFornecedor(), obj.getMatriculaPeriodoVO().getCodigo(), "", obj.getDepartamentoVO());
			x++;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ContratosDespesasVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContratosReceitasVO obj, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getSituacao().equals(ContratoReceitaSituacaoEnum.FINALIZADO)) {
				ContratosReceitasVO.validarDados(obj);
				ContratosReceitas.alterar(getIdEntidade(), true, usuario);
				final String sql = "UPDATE ContratosReceitas set valorParcela=?, diaVencimento=?, mesVencimento=?, situacao=?, contratoIndeterminado=?, dataTermino=?, dataInicio=?, descricao=?, tipoContrato=?, sacado=?, dataPrimeiraParcela=?, unidadeEnsino=?, categoriaReceita=?, tipoSacado=?, contaCorrente=?, dataInicioVigencia=?, dataTerminoVigencia=?, quantidadeParcela=?, matricula=?, departemento=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
						PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
						sqlAlterar.setDouble(1, obj.getValorParcela().doubleValue());
						sqlAlterar.setString(2, obj.getDiaVencimento());
						sqlAlterar.setString(3, obj.getMesVencimento());
						sqlAlterar.setString(4, obj.getSituacao().getValor());
						sqlAlterar.setBoolean(5, obj.isContratoIndeterminado().booleanValue());
						if (obj.getContratoIndeterminado().booleanValue() == false) {
							sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataTermino()));
						} else {
							sqlAlterar.setNull(6, 0);
						}
						sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataInicio()));
						sqlAlterar.setString(8, obj.getDescricao());
						sqlAlterar.setString(9, obj.getTipoContrato());
						sqlAlterar.setInt(10, obj.getSacado().intValue());
						sqlAlterar.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataPrimeiraParcela()));
						if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(12, obj.getUnidadeEnsino().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(12, 0);
						}
						if (obj.getCentroReceitaVO().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(13, obj.getCentroReceitaVO().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(13, 0);
						}
						sqlAlterar.setString(14, obj.getTipoSacado());
						if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(15, obj.getContaCorrenteVO().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(15, 0);
						}
						sqlAlterar.setDate(16, Uteis.getDataJDBC(obj.getDataInicioVigencia()));
						sqlAlterar.setDate(17, Uteis.getDataJDBC(obj.getDataTerminoVigencia()));
						sqlAlterar.setInt(18, obj.getQuantidadeParcelas().intValue());
						sqlAlterar.setString(19, obj.getMatriculaVO().getMatricula());
						int i = 19;
						Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlAlterar);
						Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
						return sqlAlterar;
					}
				});
				getFacadeFactory().getContratoReceitaEspecificoFacade().alterarContratoReceitaEspecificos(obj.getCodigo(), obj.getContratoReceitaEspecificoVOs());
				getFacadeFactory().getContaReceberFacade().excluirContasReceberTipoOrigemCodigoOrigem("CTR", obj.getCodigo(), "AR", usuario);
			} else {
				throw new Exception("O único campo que pode ser alterado é a situaçao do contrato");
			}
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}	
			
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ContratosDespesasVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContratosReceitasVO obj, UsuarioVO usuario) throws Exception {
		try {
			ContratosReceitas.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM ContratosReceitas WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getContratoReceitaEspecificoFacade().excluirContratoReceitaEspecificos(obj.getCodigo());
			getFacadeFactory().getContaReceberFacade().excluirContasReceberTipoOrigemCodigoOrigem("CTR", obj.getCodigo(), "AR", !obj.verificarBloqueioCompetenciaFoiLiberado(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratosDespesas</code>
	 * através do valor do atributo <code>identificadorCentroDespesa</code> da
	 * classe <code>CentroDespesa</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContratosReceitas.* FROM ContratosReceitas, CentroReceita WHERE ContratosReceitas.categoriaReceita = CentroReceita.codigo and upper( CentroReceita.identificadorCentroReceita ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CentroReceita.identificadorCentroReceita";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT ContratosReceitas.* FROM ContratosReceitas, CentroReceita WHERE ContratosReceitas.categoriaReceita = CentroReceita.codigo and upper( CentroReceita.identificadorCentroReceita ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "'ORDER BY CentroReceita.identificadorCentroReceita";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosReceitas WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosReceitas WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY situacao";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>Date dataTermino</code>. Retorna os
	 * objetos com valores pertecentes ao período informado por parâmetro. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorDataTermino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosReceitas WHERE ((dataTermino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataTermino <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataTermino";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosReceitas WHERE ((dataTermino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataTermino <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY dataTermino";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>Date dataInicio</code>. Retorna os
	 * objetos com valores pertecentes ao período informado por parâmetro. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosReceitas WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicio";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosReceitas WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeensino = '" + unidadeEnsino.intValue() + "' ORDER BY dataInicio";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>String tipoContrato</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorTipoContrato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosReceitas WHERE upper( tipoContrato ) like upper(?) ORDER BY tipoContrato";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr = "SELECT * FROM ContratosReceitas WHERE upper( tipoContrato ) like upper(?) AND unidadeEnsino = " + unidadeEnsino + " ORDER BY tipoContrato";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Fornecedor</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorNomeSacado(String valorConsulta, String tipoSacado, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT ContratosReceitas.* FROM ContratosReceitas ");
		sqlStr.append(" left join Pessoa on ContratosReceitas.sacado = Pessoa.codigo and  tipoSacado in ('AL', 'FU')");
		sqlStr.append(" left join Parceiro on ContratosReceitas.sacado = Parceiro.codigo and  tipoSacado in ('PA')");
		sqlStr.append(" left join Fornecedor on ContratosReceitas.sacado = Fornecedor.codigo  and tipoSacado in ('FO')");
		
		if (tipoSacado.equals("PA")) {
			sqlStr.append(" where upper(sem_acentos(Parceiro.nome)) like upper(sem_acentos(?)) ");
			parametros.add(valorConsulta + PERCENT);
		} else if (tipoSacado.equals("FO")) {
			sqlStr.append(" where upper(sem_acentos(Fornecedor.nome)) like upper(sem_acentos(?)) ");
			parametros.add(valorConsulta + PERCENT);
		} else if (tipoSacado.equals("AL") || tipoSacado.equals("FU")) {
			sqlStr.append(" where upper(sem_acentos(Pessoa.nome)) like upper(sem_acentos(?)) ");
			parametros.add(valorConsulta + PERCENT);
		}else{
			sqlStr.append(" where ( upper(sem_acentos(Pessoa.nome)) like upper(sem_acentos(?)) OR ");
			parametros.add(valorConsulta + PERCENT);
			sqlStr.append(" upper(sem_acentos(Parceiro.nome)) like upper(sem_acentos(?)) OR ");
			parametros.add(valorConsulta + PERCENT);
			sqlStr.append(" upper(sem_acentos(Fornecedor.nome)) like upper(sem_acentos(?))) ");
			parametros.add(valorConsulta + PERCENT);
		}
		if(unidadeEnsino != null && unidadeEnsino>0){
			sqlStr.append(" and ContratosReceitas.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" order by trim(Pessoa.nome)||trim(Parceiro.nome)||trim(Fornecedor.nome) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratosReceitas</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContratosReceitasVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosReceitas WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosReceitas WHERE codigo = " + valorConsulta.intValue() + " AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 */
	public static List<ContratosReceitasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ContratosReceitasVO> vetResultado = new ArrayList<ContratosReceitasVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ContratosDespesasVO</code>.
	 * 
	 * @return O objeto da classe <code>ContratosDespesasVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ContratosReceitasVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContratosReceitasVO obj = new ContratosReceitasVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setValorParcela(new Double(dadosSQL.getDouble("valorParcela")));
		obj.setDiaVencimento(dadosSQL.getString("diaVencimento"));
		obj.setMesVencimento(dadosSQL.getString("mesVencimento"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("situacao"))){
			obj.setSituacao(ContratoReceitaSituacaoEnum.getEnumPorValor(dadosSQL.getString("situacao")));
		}
		obj.setContratoIndeterminado(new Boolean(dadosSQL.getBoolean("contratoIndeterminado")));
		obj.setDataTermino(dadosSQL.getDate("dataTermino"));
		obj.setDataInicio(dadosSQL.getDate("dataInicio"));

		obj.setDataTerminoVigencia(dadosSQL.getDate("dataTerminoVigencia"));
		obj.setDataInicioVigencia(dadosSQL.getDate("dataInicioVigencia"));

		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoContrato(dadosSQL.getString("tipoContrato"));
		obj.setSacado(dadosSQL.getInt("sacado"));
		obj.setDataPrimeiraParcela(dadosSQL.getDate("dataPrimeiraParcela"));
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getCentroReceitaVO().setCodigo(new Integer(dadosSQL.getInt("categoriaReceita")));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
		obj.setQuantidadeParcelas(dadosSQL.getInt("quantidadeParcela"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));		
		if (obj.getTipoSacado().equals("FO")) {
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		} else if (obj.getTipoSacado().equals("PA")) {
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		} else {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosMatricula(obj, usuario);
		montarDadosCentroReceita(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setDepartamentoVO(Uteis.montarDadosVO(dadosSQL.getInt("departamento"), DepartamentoVO.class, p -> getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		if (obj.getTipoContrato().equals("ES")) {
			obj.setContratoReceitaEspecificoVOs(getFacadeFactory().getContratoReceitaEspecificoFacade().consultarPorCodigoContratoReceita(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setContratoReceitaAlteracaoValorVOs(getFacadeFactory().getContratoReceitaAlteracaoValorFacade().consultarPorContratoReceita(obj.getCodigo(), usuario));
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FornecedorVO</code> relacionado ao objeto
	 * <code>ContratoDespesaVO</code>. Faz uso da chave primária da classe
	 * <code>FornecedorVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosParceiro(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSacado().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getSacado(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSacado().intValue() == 0) {
			obj.setFornecedor(null);
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getSacado(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosPessoa(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSacado().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getSacado(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCentroReceita(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCentroReceitaVO().getCodigo().intValue() == 0) {
			obj.setCentroReceitaVO(new CentroReceitaVO());
			return;
		}
		obj.setCentroReceitaVO(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosContaCorrente(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
			obj.setContaCorrenteVO(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(ContratosReceitasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ContratosDespesasVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ContratosReceitasVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContratosReceitas WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContratosReceitas ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContratosDespesas.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContratosDespesas.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFinalizacaoContratoReceitasPorCodigoReceita(final ContratosReceitasVO contratosReceitasVO, UsuarioVO usuarioVO) throws Exception{
		final String sql = "UPDATE ContratosReceitas SET situacao=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				sqlAlterar.setString(1, contratosReceitasVO.getSituacao().getValor());
				sqlAlterar.setInt(2, contratosReceitasVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorDasParcelasPendentes(ContratosReceitasVO contratosReceitasVO, Double novoValor,  String motivoAlteracao, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		try {
			ControleAcesso.alterar(getIdEntidade(), true, usuarioVO);
			List<ContaReceberVO> listaDeContas = getFacadeFactory().getContaReceberFacade().consultarPorOrigemContaReceber(String.valueOf(contratosReceitasVO.getCodigo()), "CTR", "AR", false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
			if(listaDeContas.isEmpty()){
				throw new Exception("Não existe nenhuma conta a receber vinculado a este contrato de receita apto a alteração de valores.");
			}
			ContratoReceitaAlteracaoValorVO contratoReceitaAlteracaoValorVO = new ContratoReceitaAlteracaoValorVO();
			contratoReceitaAlteracaoValorVO.setContratosReceitasVO(contratosReceitasVO);
			contratoReceitaAlteracaoValorVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
			contratoReceitaAlteracaoValorVO.getResponsavel().setNome(usuarioVO.getNome());
			contratoReceitaAlteracaoValorVO.setMotivoAlteracao(motivoAlteracao);
			if(contratosReceitasVO.getContratoReceitaAlteracaoValorVOs().isEmpty()){
				contratoReceitaAlteracaoValorVO.setValorAnterior(contratosReceitasVO.getValorParcela());
			}else{
				contratoReceitaAlteracaoValorVO.setValorAnterior(contratosReceitasVO.getContratoReceitaAlteracaoValorVOs().get(0).getValorNovo());
			}
			contratoReceitaAlteracaoValorVO.setValorNovo(novoValor);
			for (ContaReceberVO obj : listaDeContas) {
				obj.setValor(novoValor);
				obj.verificarEReplicarLiberacaoBloqueioEntidadePrincipal(contratosReceitasVO);

				getFacadeFactory().getContaReceberFacade().alterar(obj, configuracaoFinanceiroVO, false, usuarioVO);
				if(contratoReceitaAlteracaoValorVO.getContasAlteradas().trim().isEmpty()){
					contratoReceitaAlteracaoValorVO.setContasAlteradas(obj.getNossoNumero());
				}else{
					contratoReceitaAlteracaoValorVO.setContasAlteradas(contratoReceitaAlteracaoValorVO.getContasAlteradas()+ ", "+obj.getNossoNumero());
				}
			}
			getFacadeFactory().getContratoReceitaAlteracaoValorFacade().incluir(contratoReceitaAlteracaoValorVO, usuarioVO);	
			contratosReceitasVO.getContratoReceitaAlteracaoValorVOs().add(0, contratoReceitaAlteracaoValorVO);
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				contratosReceitasVO.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}
			
			throw e;
		}		
	}
	
	public List<ContratosReceitasVO> consultarPorMatriculaAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT * FROM ContratosReceitas WHERE matricula like ? ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public static void montarDadosMatricula(ContratosReceitasVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			obj.setMatriculaVO(new MatriculaVO());
			return;
		}
		getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatriculaVO(), usuario);
	}
}
