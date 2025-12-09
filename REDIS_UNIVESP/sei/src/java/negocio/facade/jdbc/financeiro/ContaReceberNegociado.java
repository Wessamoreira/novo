package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ContaReceberNegociadoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaReceberNegociadoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaReceberNegociadoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContaReceberNegociadoVO
 * @see ControleAcesso
 * @see NegociacaoContaReceber
 */
@Repository
@Scope("singleton")
@Lazy 
public class ContaReceberNegociado extends ControleAcesso implements ContaReceberNegociadoInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3875210010834308621L;
    protected static String idEntidade;

    public ContaReceberNegociado() throws Exception {
        super();
        setIdEntidade("NegociacaoContaReceber");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberNegociadoVO</code>.
     */
    public ContaReceberNegociadoVO novo() throws Exception {
        ContaReceberNegociado.incluir(getIdEntidade());
        ContaReceberNegociadoVO obj = new ContaReceberNegociadoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberNegociadoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberNegociadoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberNegociadoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {    	
    	final String sql = "INSERT INTO ContaReceberNegociado( negociacaoContaReceber, contaReceber, valor, nrDiasAtraso, desconsiderarDescontoProgressivo, "
        		+ " desconsiderarDescontoAluno, desconsiderarDescontoInstituicaoComValidade, desconsiderarDescontoInstituicaoSemValidade, valorDescontoProgressivoDesconsiderado, "
        		+ " valorDescontoAlunoDesconsiderado, valorDescontoInstituicaoComValidadeDesconsiderado, valorDescontoInstituicaoSemValidadeDesconsiderado, valorOriginalConta  "
        		+ " ) "
        		+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"
        		+ " returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                if (obj.getNegociacaoContaReceber().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getNegociacaoContaReceber().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getContaReceber().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getContaReceber().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setDouble(3, obj.getValor().doubleValue());
                sqlInserir.setLong(4, obj.getNrDiasAtraso().longValue());
                sqlInserir.setBoolean(5, obj.getDesconsiderarDescontoProgressivo());
                sqlInserir.setBoolean(6, obj.getDesconsiderarDescontoAluno());
                sqlInserir.setBoolean(7, obj.getDesconsiderarDescontoInstituicaoComValidade());
                sqlInserir.setBoolean(8, obj.getDesconsiderarDescontoInstituicaoSemValidade());                
                sqlInserir.setDouble(9, obj.getValorDescontoProgressivoDesconsiderado());
                sqlInserir.setDouble(10, obj.getValorDescontoAlunoDesconsiderado());
                sqlInserir.setDouble(11, obj.getValorDescontoInstituicaoComValidadeDesconsiderado());
                sqlInserir.setDouble(12, obj.getValorDescontoInstituicaoSemValidadeDesconsiderado());
                sqlInserir.setDouble(13, obj.getValorOriginalConta());
                /* int i = 13;
                Uteis.setValuePreparedStatement(obj.getItemCondicaoDescontoRenegociacaoVO(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getValorOriginalJuro(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getValorOriginalMulta(), ++i, sqlInserir);*/
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
        alterarSituacaoContaReceber(obj, configuracaoFinanceiro, usuario);
        //getFacadeFactory().getMatriculaPeriodoVencimentoFacade().alterarSituacaoMatriculaPeriodoVencimento(obj.getContaReceber().getCodigo(), SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA.getValor(), usuario);
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoContaReceber(ContaReceberNegociadoVO contaReceberNegociadoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
    	List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber = contaReceberNegociadoVO.getContaReceber().getListaDescontosAplicavesContaReceber(); 
		if(contaReceberNegociadoVO.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao() && contaReceberNegociadoVO.getContaReceber().getRealizandoRecebimento()) {
			contaReceberNegociadoVO.getContaReceber().setRealizandoRecebimento(false);
			listaDescontosAplicavesContaReceber = ContaReceber.gerarListaPlanoFinanceiroAlunoDescricaoDesconto(contaReceberNegociadoVO.getContaReceber(), contaReceberNegociadoVO.getNegociacaoContaReceber().getData(), configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), null, usuario, configuracaoFinanceiro);
			contaReceberNegociadoVO.getContaReceber().setRealizandoRecebimento(true);
		}
		if(contaReceberNegociadoVO.getDesconsiderarDescontoAluno()){
			contaReceberNegociadoVO.getContaReceber().setValorDescontoAlunoJaCalculado(0.0);
		}else if(!contaReceberNegociadoVO.getDesconsiderarDescontoAluno() && contaReceberNegociadoVO.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao() && contaReceberNegociadoVO.getValorDescontoAlunoDesconsiderado() > 0.0){
			contaReceberNegociadoVO.getContaReceber().setValorDescontoAlunoJaCalculado(contaReceberNegociadoVO.getValorDescontoAlunoDesconsiderado());
			//validar com Rodrigo sobre a alteracao do campo valor desconto pois qnd o mesmo e alterado e ao estorna o valor nao consegue voltar a conta para o que era antes. Pedro Andrade
			/*contaReceberNegociadoVO.getContaReceber().setValorDesconto(contaReceberNegociadoVO.getValorDescontoAlunoDesconsiderado());
			if(contaReceberNegociadoVO.getContaReceber().getIsTipoDescontoPorcentagem()){
				contaReceberNegociadoVO.getContaReceber().setValorDesconto(Uteis.arrendondarForcando2CadasDecimais((contaReceberNegociadoVO.getValorDescontoAlunoDesconsiderado()*100)/contaReceberNegociadoVO.getContaReceber().getValor()));
			}*/
		}
    	
		for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: contaReceberNegociadoVO.getContaReceber().getPlanoDescontoInstitucionalContaReceber()){
			if((planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade() && contaReceberNegociadoVO.getDesconsiderarDescontoInstituicaoSemValidade())
					|| (!planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade() && contaReceberNegociadoVO.getDesconsiderarDescontoInstituicaoComValidade())){
				planoDescontoContaReceberVO.setValorUtilizadoRecebimento(0.0);
			} else if(contaReceberNegociadoVO.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao()
					&& !planoDescontoContaReceberVO.getUtilizarDescontoSemLimiteValidade()
					&& !contaReceberNegociadoVO.getDesconsiderarDescontoInstituicaoComValidade()
					&& contaReceberNegociadoVO.getValorDescontoInstituicaoComValidadeDesconsiderado() > 0.0){
				Double valor =  planoDescontoContaReceberVO.getIsPlanoDescontoInstitucional() ? listaDescontosAplicavesContaReceber.get(0).getListaDescontosPlanoDesconto().get(planoDescontoContaReceberVO.getPlanoDescontoVO().getCodigo()) : planoDescontoContaReceberVO.getValorDesconto();
				planoDescontoContaReceberVO.setValorUtilizadoRecebimento(Uteis.arrendondarForcando2CadasDecimais((contaReceberNegociadoVO.getNegociacaoContaReceber().getItemCondicaoDescontoRenegociacaoVO().getPercentualPlanoDesconto().doubleValue() * valor)/100));
			}
		}
    	getFacadeFactory().getMatriculaPeriodoVencimentoFacade().alterarSituacaoMatriculaPeriodoVencimento(contaReceberNegociadoVO.getContaReceber().getCodigo(), SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA.getValor(), usuario);
    	contaReceberNegociadoVO.getContaReceber().setSituacao("NE");
    	Boolean possuiPermissao = Boolean.FALSE;
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RenegociacaoRegistroNegativacaoCobranca", usuario);
    		possuiPermissao = Boolean.TRUE;
    	} catch (Exception e) {
    		possuiPermissao = Boolean.FALSE;
    	}
    	if (!possuiPermissao && getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarContaReceberNegativadaCobrancaContaReceber(contaReceberNegociadoVO.getContaReceber().getCodigo())) {
    		throw new Exception ("Não é possível realizar a negociação da conta a receber de uma conta que possua registro de negativação/cobrança ( Nosso número =  " + contaReceberNegociadoVO.getContaReceber().getNossoNumero() + " )!");
    	}
        getFacadeFactory().getContaReceberFacade().alterar(contaReceberNegociadoVO.getContaReceber(), false, false, configuracaoFinanceiro, usuario);
    }
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberNegociadoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociadoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaReceberNegociadoVO obj, UsuarioVO usuario) throws Exception {
    	/**
    	 * @author Leonardo Riciolle
    	 * Comenatado 23/10/2014
    	 */
        //ContaReceberNegociado.alterar(getIdEntidade());
        final String sql = "UPDATE ContaReceberNegociado set negociacaoContaReceber=?, contaReceber=?, valor=?, nrDiasAtraso=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getNegociacaoContaReceber().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getNegociacaoContaReceber().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getContaReceber().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getContaReceber().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setDouble(3, obj.getValor().doubleValue());
                sqlAlterar.setLong(4, obj.getNrDiasAtraso().longValue());
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberNegociadoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociadoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaReceberNegociadoVO obj, UsuarioVO usuario) throws Exception {
    	/**
    	 * @author Leonardo Riciolle
    	 * Comenatado 23/10/2014
    	 */
        //ContaReceberNegociado.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaReceberNegociado WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

  

  

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociado</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberNegociado WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociadoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ContaReceberNegociadoVO obj = new ContaReceberNegociadoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContaReceberNegociadoVO</code>.
     * @return  O objeto da classe <code>ContaReceberNegociadoVO</code> com os dados devidamente montados.
     */
    public static ContaReceberNegociadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociadoVO obj = new ContaReceberNegociadoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.getNegociacaoContaReceber().setCodigo((dadosSQL.getInt("negociacaoContaReceber")));
        obj.getContaReceber().setCodigo((dadosSQL.getInt("contaReceber")));
        obj.setValor((dadosSQL.getDouble("valor")));
        obj.setNrDiasAtraso((dadosSQL.getLong("nrDiasAtraso")));
        obj.setDesconsiderarDescontoAluno(dadosSQL.getBoolean("desconsiderarDescontoAluno"));
        obj.setDesconsiderarDescontoInstituicaoComValidade(dadosSQL.getBoolean("desconsiderarDescontoInstituicaoComValidade"));
        obj.setDesconsiderarDescontoInstituicaoSemValidade(dadosSQL.getBoolean("desconsiderarDescontoInstituicaoSemValidade"));
        obj.setDesconsiderarDescontoProgressivo(dadosSQL.getBoolean("desconsiderarDescontoProgressivo"));
        obj.setValorDescontoAlunoDesconsiderado(dadosSQL.getDouble("valorDescontoAlunoDesconsiderado"));
        obj.setValorDescontoInstituicaoComValidadeDesconsiderado(dadosSQL.getDouble("valorDescontoInstituicaoComValidadeDesconsiderado"));
        obj.setValorDescontoInstituicaoSemValidadeDesconsiderado(dadosSQL.getDouble("valorDescontoInstituicaoSemValidadeDesconsiderado"));
        obj.setValorDescontoProgressivoDesconsiderado(dadosSQL.getDouble("valorDescontoProgressivoDesconsiderado"));
        obj.setValorOriginalConta(dadosSQL.getDouble("valorOriginalConta"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {        	
        	return obj;
        }        
        montarDadosContaReceber(obj, nivelMontarDados, configuracaoFinanceiro, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaReceberVO</code> relacionado ao objeto <code>ContaReceberNegociadoVO</code>.
     * Faz uso da chave primária da classe <code>ContaReceberVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaReceber(ContaReceberNegociadoVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        if (obj.getContaReceber().getCodigo().intValue() == 0) {
            obj.setContaReceber(new ContaReceberVO());
            return;
        }
        getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceber(), NivelMontarDados.BASICO, configuracaoFinanceiro, usuario);
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberNegociadoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberNegociado</code>.
     * @param <code>negociacaoContaReceber</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaReceberNegociados(NegociacaoContaReceberVO negociacaoContaReceber, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM ContaReceberNegociado WHERE (negociacaoContaReceber = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{negociacaoContaReceber.getCodigo()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberNegociadoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberNegociados</code> e <code>incluirContaReceberNegociados</code> disponíveis na classe <code>ContaReceberNegociado</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarContaReceberNegociados(NegociacaoContaReceberVO negociacaoContaReceber, List objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        excluirContaReceberNegociados(negociacaoContaReceber, usuario);
        incluirContaReceberNegociados(negociacaoContaReceber, objetos, configuracaoFinanceiro, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberNegociadoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoContaReceber</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContaReceberNegociados(NegociacaoContaReceberVO negociacaoContaReceberPrm, List objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContaReceberNegociadoVO obj = (ContaReceberNegociadoVO) e.next();
            obj.setNegociacaoContaReceber(negociacaoContaReceberPrm);
            incluir(obj, configuracaoFinanceiro, usuario);
        }
    }

    
    public List<ContaReceberNegociadoVO> consultarContaReceberNegociados(NegociacaoContaReceberVO negociacaoContaReceber, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociado.consultar(getIdEntidade());
        List<ContaReceberNegociadoVO> objetos = new ArrayList<ContaReceberNegociadoVO>(0);
        String sql = "SELECT ContaReceberNegociado.*, contareceber.datavencimento FROM ContaReceberNegociado inner join contareceber on contareceber.codigo = ContaReceberNegociado.contareceber WHERE negociacaoContaReceber = ? order by contareceber.datavencimento";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{negociacaoContaReceber.getCodigo()});
        while (resultado.next()) {
            ContaReceberNegociadoVO novoObj = new ContaReceberNegociadoVO();
            novoObj = ContaReceberNegociado.montarDados(resultado, nivelMontarDados, configuracaoFinanceiro, usuario);
            novoObj.setNegociacaoContaReceber(negociacaoContaReceber);
            novoObj.getContaReceber().setConfiguracaoFinanceiro(getAplicacaoControle().getConfiguracaoFinanceiroVO(novoObj.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo()));
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberNegociadoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberNegociadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContaReceberNegociado WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaReceberNegociado ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }
    
    
    @Override
    public ContaReceberNegociadoVO consultarPorContaReceber(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	String sql = "SELECT * FROM ContaReceberNegociado WHERE contareceber = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados ( ContaReceberNegociado ).");
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaReceberNegociado.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContaReceberNegociado.idEntidade = idEntidade;
    }
    
    
    @Override
    public void realizarCalculoValorContaReceberAdicionar(ContaReceberNegociadoVO contaReceberNegociadoVO, Boolean desconsiderarDescontoProgressivo, Boolean desconsiderarDescontoAluno, Boolean desconsiderarDescontoInstituicaoComValidade, Boolean desconsiderarDescontoInstituicaoSemValidade) throws Exception{
    		contaReceberNegociadoVO.setValor(contaReceberNegociadoVO.getContaReceber().getValorRecebido());
        	contaReceberNegociadoVO.setValorOriginalConta(contaReceberNegociadoVO.getContaReceber().getValorRecebido());
        	contaReceberNegociadoVO.setValorDescontoAlunoDesconsiderado(contaReceberNegociadoVO.getContaReceber().getValorDescontoAlunoJaCalculado());
        	contaReceberNegociadoVO.setValorDescontoProgressivoDesconsiderado(contaReceberNegociadoVO.getContaReceber().getValorDescontoProgressivo());
        	contaReceberNegociadoVO.setValorDescontoInstituicaoComValidadeDesconsiderado(0.0);
        	contaReceberNegociadoVO.setValorDescontoInstituicaoSemValidadeDesconsiderado(0.0);
    		for(PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO: contaReceberNegociadoVO.getContaReceber().getListaDescontosAplicavesContaReceber()){
    			if(planoFinanceiroAlunoDescricaoDescontosVO.getIsAplicavelDataParaQuitacao(contaReceberNegociadoVO.getContaReceber().getDataVencimento(), new Date())){
    				for(Integer planoDesconto : planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosPlanoDesconto().keySet()){
    					for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: contaReceberNegociadoVO.getContaReceber().getPlanoDescontoContaReceberVOs()){
    						if(Uteis.isAtributoPreenchido(planoDescontoContaReceberVO.getPlanoDescontoVO()) && planoDescontoContaReceberVO.getPlanoDescontoVO().getCodigo().equals(planoDesconto)){
    							if(planoDescontoContaReceberVO.getPlanoDescontoVO().getDescontoValidoAteDataVencimento() 
    								|| ((planoDescontoContaReceberVO.getPlanoDescontoVO().getUtilizarDiaFixo() || planoDescontoContaReceberVO.getPlanoDescontoVO().getUtilizarDiaUtil()) 
    								&& planoDescontoContaReceberVO.getPlanoDescontoVO().getDiasValidadeVencimento() > 0)){
    								contaReceberNegociadoVO.setValorDescontoInstituicaoComValidadeDesconsiderado(contaReceberNegociadoVO.getValorDescontoInstituicaoComValidadeDesconsiderado() + planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosPlanoDesconto().get(planoDesconto));
    							}else{
    								contaReceberNegociadoVO.setValorDescontoInstituicaoSemValidadeDesconsiderado(contaReceberNegociadoVO.getValorDescontoInstituicaoSemValidadeDesconsiderado() + planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosPlanoDesconto().get(planoDesconto));
    							}
    						}
    					}
    				}
    				break;
    			}
    		}
    		contaReceberNegociadoVO.setValorDescontoInstituicaoComValidadeDesconsiderado(Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociadoVO.getValorDescontoInstituicaoComValidadeDesconsiderado()));
    		contaReceberNegociadoVO.setValorDescontoInstituicaoSemValidadeDesconsiderado(Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociadoVO.getValorDescontoInstituicaoSemValidadeDesconsiderado()));
    		contaReceberNegociadoVO.setDesconsiderarDescontoAluno(desconsiderarDescontoAluno);
    		contaReceberNegociadoVO.setDesconsiderarDescontoInstituicaoComValidade(desconsiderarDescontoInstituicaoComValidade);
    		contaReceberNegociadoVO.setDesconsiderarDescontoInstituicaoSemValidade(desconsiderarDescontoInstituicaoSemValidade);
    		contaReceberNegociadoVO.setDesconsiderarDescontoProgressivo(desconsiderarDescontoProgressivo);
    		realizarCalculoValorContaReceberDesconsiderandoDescontos(contaReceberNegociadoVO);
    }
    
    @Override
    public void realizarCalculoValorContaReceberDesconsiderandoDescontos(ContaReceberNegociadoVO contaReceberNegociadoVO) throws Exception{
    	if(contaReceberNegociadoVO.getDesconsiderarDescontoAluno()){
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoAlunoJaCalculado(0.0);
    	}else{
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoAlunoJaCalculado(contaReceberNegociadoVO.getValorDescontoAlunoDesconsiderado());
    	}
    	
    	if(contaReceberNegociadoVO.getDesconsiderarDescontoProgressivo()){
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoProgressivo(0.0);
    	}else{
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoProgressivo(contaReceberNegociadoVO.getValorDescontoProgressivoDesconsiderado());
    	}
    	
    	contaReceberNegociadoVO.getContaReceber().setValorDescontoInstituicao(0.0);
    	if(!contaReceberNegociadoVO.getDesconsiderarDescontoInstituicaoComValidade()){
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoInstituicao(contaReceberNegociadoVO.getValorDescontoInstituicaoComValidadeDesconsiderado());
    	}
    	
    	if(!contaReceberNegociadoVO.getDesconsiderarDescontoInstituicaoSemValidade()){
    		contaReceberNegociadoVO.getContaReceber().setValorDescontoInstituicao(contaReceberNegociadoVO.getContaReceber().getValorDescontoInstituicao() + contaReceberNegociadoVO.getValorDescontoInstituicaoSemValidadeDesconsiderado());
    	}
    	contaReceberNegociadoVO.setValor(Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociadoVO.getContaReceber().getValor() - contaReceberNegociadoVO.getContaReceber().getValorTotalDescontoContaReceber() + (contaReceberNegociadoVO.getContaReceber().getAcrescimo() + contaReceberNegociadoVO.getContaReceber().getJuro() + contaReceberNegociadoVO.getContaReceber().getMulta() + contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue())));
    	contaReceberNegociadoVO.getContaReceber().setValorRecebido(contaReceberNegociadoVO.getValor());
    	contaReceberNegociadoVO.getContaReceber().setValorFinalCalculado(contaReceberNegociadoVO.getValor());
    		
    	
    }
}
