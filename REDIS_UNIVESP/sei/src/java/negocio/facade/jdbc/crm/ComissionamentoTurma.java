package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

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
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.comuns.crm.ComissionamentoTurmaVO;
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.crm.RankingTurmaConsultorAlunoVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.crm.ComissionamentoTurmaInterfaceFacade;

/**
 *
 * @author Otimize-04
 */
@Repository
@Scope("singleton")
@Lazy
public class ComissionamentoTurma extends ControleAcesso implements ComissionamentoTurmaInterfaceFacade {

    protected static String idEntidade;

    public ComissionamentoTurma() {
        super();
        setIdEntidade("ComissionamentoTurma");
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return ComissionamentoTurma.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ComissionamentoTurma.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>FuncionarioVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>FuncionarioVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ComissionamentoTurmaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            ComissionamentoTurma.incluir(getIdEntidade(), true, usuario);
            validarDadosUnicidade(obj.getTurmaVO().getCodigo());
            validarDados(obj);
            final String sql = "INSERT INTO ComissionamentoTurma( turma, qtdeParcela, dataPrimeiroPagamento, dataUltimoPagamento, configuracaoRanking, valorTotalAReceberTurma, considerarTicketMedio ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getTurmaVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getTurmaVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getQtdeParcela());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataPrimeiroPagamento()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataUltimoPagamento()));
                    if (obj.getConfiguracaoRankingVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getConfiguracaoRankingVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                	sqlInserir.setDouble(6, obj.getValorTotalAReceberTurma().doubleValue());
                	sqlInserir.setBoolean(7, obj.getConsiderarTicketMedio().booleanValue());
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
            getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().incluirComissionamentoFaixaValorVOs(obj.getCodigo(), obj.getListaComissionamentoPorTurmaFaixaValorVOs(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirComissionamentoTurmaValorMatricula(final String matricula, final Double valor, UsuarioVO usuario) throws Exception {
    	try {
    		final String sql = "INSERT INTO ComissionamentoTurmaValorMatricula( matricula, valorTotalAReceberTicketMedioCRM) VALUES ( ?, ?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
    			
    			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
    				sqlInserir.setString(1, matricula);
    				sqlInserir.setDouble(2, valor);
    				return sqlInserir;
    			}
    		}, new ResultSetExtractor() {
    			
    			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
    				if (arg0.next()) {
    					return arg0.getInt("codigo");
    				}
    				return null;
    			}
    		});
    	} catch (Exception e) {
    		throw e;
    	}
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>FuncionarioVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>FuncionarioVO</code> que será alterada
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ComissionamentoTurmaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            ComissionamentoTurma.alterar(getIdEntidade(), true, usuario);

            final String sql = "UPDATE ComissionamentoTurma set turma=?, qtdeParcela=? , dataPrimeiroPagamento=?, dataUltimoPagamento=?, configuracaoRanking=?, valorTotalAReceberTurma=?, considerarTicketMedio=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    if (obj.getTurmaVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getTurmaVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setInt(2, obj.getQtdeParcela());
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataPrimeiroPagamento()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataUltimoPagamento()));
                    if (obj.getConfiguracaoRankingVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getConfiguracaoRankingVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setDouble(6, obj.getValorTotalAReceberTurma().doubleValue());
                    sqlAlterar.setBoolean(7, obj.getConsiderarTicketMedio().booleanValue());
                    sqlAlterar.setInt(8, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().alterarComissionamentoFaixaValorVOs(obj.getCodigo(), obj.getListaComissionamentoPorTurmaFaixaValorVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorTotalAReceberTicketMedioCRMMatriculaAluno(final String matricula, final Double valor, UsuarioVO usuario) throws Exception {
    	try {
	    	StringBuilder sb = new StringBuilder();
	        sb.append("SELECT codigo FROM ComissionamentoTurmaValorMatricula WHERE matricula = '").append(matricula).append("'");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	        if (tabelaResultado.next()) {
	        	String sqlStr = "UPDATE ComissionamentoTurmaValorMatricula set valorTotalAReceberTicketMedioCRM=? WHERE (matricula = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
	    		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { valor, matricula});    		
	        } else {
	        	this.incluirComissionamentoTurmaValorMatricula(matricula, valor, usuario);
	        }
    	} catch (Exception e) {
            throw e;
        }    	
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarValorETotalPagantes(final Integer turma, final Double valorTotalAReceberTurma, final Integer totalAlunosPagantes, UsuarioVO usuario) throws Exception {
    	try {
    		final String sql = "UPDATE ComissionamentoTurma set valorTotalAReceberTurma=?, totalAlunosPagantes=?  WHERE turma= ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
    			
    			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    				PreparedStatement sqlAlterar = con.prepareStatement(sql);
    				sqlAlterar.setDouble(1, valorTotalAReceberTurma);
    				sqlAlterar.setInt(2, totalAlunosPagantes);
					sqlAlterar.setInt(3, turma);
    				return sqlAlterar;
    			}
    		});
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataProcComissionamento(final Integer codComissionamento, final Boolean comissionamentoProcessado, UsuarioVO usuario) throws Exception {
    	try {
    		String sql = "";
    		if (codComissionamento == 0) {
    			sql = "UPDATE ComissionamentoTurma set comissionamentoAtualizadoMesAtual=?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);    			
    		} else {
    			sql = "UPDATE ComissionamentoTurma set comissionamentoAtualizadoMesAtual=?, dataComissionamentoAtualizadoMesAtual=?  WHERE codigo= ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		}
    		final String sql2 = sql; 
    		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
    			
    			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    				PreparedStatement sqlAlterar = con.prepareStatement(sql2);
    				if (codComissionamento == 0) {
    					sqlAlterar.setBoolean(1, comissionamentoProcessado.booleanValue());
    				} else {
	    				sqlAlterar.setBoolean(1, comissionamentoProcessado.booleanValue());
	    				sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
	    				sqlAlterar.setInt(3, codComissionamento);
    				}
    				return sqlAlterar;
    			}
    		});
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>FuncionarioVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>FuncionarioVO</code> que será removido
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ComissionamentoTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ComissionamentoTurma.excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().excluirComissionamentoFaixaValorVOs(obj.getCodigo(), usuario);
            String sql = "DELETE FROM ComissionamentoTurma WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDados(ComissionamentoTurmaVO obj) throws Exception {
        if (obj.getTurmaVO() == null || obj.getTurmaVO().getCodigo().equals(0)) {
            throw new Exception("O campo TURMA deve ser informado.");
        }
        if (obj.getConfiguracaoRankingVO() == null || obj.getConfiguracaoRankingVO().getCodigo().equals(0)) {
            throw new Exception("O campo CONFIGURAÇÃO RANKING deve ser informado.");
        }
        if (obj == null || obj.getQtdeParcela().equals(0)) {
            throw new Exception("O campo QUANTIDADE PARCELA(S) deve ser informado.");
        }
        if (obj == null || obj.getDataPrimeiroPagamento() == null) {
            throw new Exception("O campo DATA 1º PAGTO deve ser informado.");
        }
        if (obj == null || obj.getDataUltimoPagamento() == null) {
            throw new Exception("O campo DATA ÚLTIMO PAGTO deve ser informado.");
        }
        if (obj.getListaComissionamentoPorTurmaFaixaValorVOs().isEmpty()) {
            throw new Exception("Deve ser informado pelo menos uma FAIXA DE VALOR");
        }
    }

    public void validarDadosUnicidade(Integer turma) throws Exception {
        if (consultarComissionamentoPorTurma(turma)) {
            throw new Exception("Já existe um COMISSIONAMENTO POR TURMA cadastrado para essa turma.");
        }
    }

    public boolean consultarComissionamentoPorTurma(Integer turma) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT codigo FROM comissionamentoTurma WHERE turma = ").append(turma);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public void persistir(ComissionamentoTurmaVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioVO, configuracaoGeralSistemaVO);
        } else {
            alterar(obj, usuarioVO, configuracaoGeralSistemaVO);
        }

    }

    public void consultar(DataModelo controleConsultaOtimizado, String campoConsulta, String valorConsulta, Date valorConsultaData, UsuarioVO usuarioVO) throws ConsistirException, Exception {
        controleConsultaOtimizado.getListaConsulta().clear();
        controleConsultaOtimizado.setLimitePorPagina(10);
        List objs = new ArrayList(0);
        if (campoConsulta.equals("identificadorTurma")) {
            if (valorConsulta.length() < 2) {
                throw new Exception("Informe pelo menos 2 (dois) parâmetros para consulta.");
            }
            objs = consultaRapidaPorTurma(valorConsulta, false, controleConsultaOtimizado.getLimitePorPagina(), controleConsultaOtimizado.getOffset(), true, usuarioVO);
            controleConsultaOtimizado.setTotalRegistrosEncontrados(consultarTotalDeGegistroPorTurma(valorConsulta, true, usuarioVO));
        }
        if (campoConsulta.equals("curso")) {
            if (valorConsulta.length() < 2) {
                throw new Exception("Informe pelo menos 2 (dois) parâmetros para consulta.");
            }
            objs = consultaRapidaPorCurso(valorConsulta, controleConsultaOtimizado.getLimitePorPagina(), controleConsultaOtimizado.getOffset(), true, usuarioVO);
            controleConsultaOtimizado.setTotalRegistrosEncontrados(consultarTotalDeGegistroPorCurso(valorConsulta, true, usuarioVO));
        }

        if (campoConsulta.equals("unidadeEnsino")) {
            if (valorConsulta.length() < 2) {
                throw new ConsistirException("msg_ParametroConsulta_vazio");
            }
            objs = consultaRapidaPorUnidadeEnsino(valorConsulta, controleConsultaOtimizado.getLimitePorPagina(), controleConsultaOtimizado.getOffset(), true, usuarioVO);
            controleConsultaOtimizado.setTotalRegistrosEncontrados(consultarTotalDeGegistroPorUnidadeEnsino(valorConsulta, true, usuarioVO));
        }

        if (campoConsulta.equals("dataPrimeiroPgto")) {
            objs = consultaRapidaPorDataPrimeiroPagamento(valorConsultaData, controleConsultaOtimizado.getLimitePorPagina(), controleConsultaOtimizado.getOffset(), true, usuarioVO);
            controleConsultaOtimizado.setTotalRegistrosEncontrados(consultarTotalDeGegistroPorDataPrimeiroPagamento(valorConsultaData, true, usuarioVO));
        }

        controleConsultaOtimizado.setListaConsulta(objs);
    }

    public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
        if (campoConsulta.equals("identificadorTurma")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsulta, false, usuarioVO);
        }
        return new ArrayList(0);
    }

    public void carregarDados(ComissionamentoTurmaVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((ComissionamentoTurmaVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(ComissionamentoTurmaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((ComissionamentoTurmaVO) obj, resultado, usuario);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((ComissionamentoTurmaVO) obj, resultado, usuario);
            Iterator i = obj.getListaComissionamentoPorTurmaFaixaValorVOs().iterator();
            while (i.hasNext()) {
            	ComissionamentoTurmaFaixaValorVO c = (ComissionamentoTurmaFaixaValorVO)i.next();
//            	c.getComissionamentoTurmaVO().setTicketMedio(obj.getTicketMedio());
//            	c.getComissionamentoTurmaVO().setTotalAlunosPagantes(obj.getTotalAlunosPagantes());
            	if (obj.getConsiderarTicketMedio()) {
            		c.setValor((obj.getTicketMedio().doubleValue() * c.getQtdeInicialAluno()) * (c.getPercComissao().doubleValue() / 100));            		
            	} else {
            		c.setValor(c.getValor());
            	}
            	//c.setValor(valor);
            }
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codComissionamento, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (ComissionamentoTurma.codigo= ").append(codComissionamento).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codComissionamento, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (ComissionamentoTurma.codigo= ").append(codComissionamento).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT comissionamentoTurma.codigo AS \"comissionamentoTurma.codigo\", comissionamentoTurma.qtdeParcela AS \"comissionamentoTurma.qtdeParcela\", comissionamentoTurma.dataPrimeiroPagamento AS \"comissionamentoTurma.dataPrimeiroPagamento\", ");
        sb.append(" comissionamentoTurma.dataUltimoPagamento AS \"comissionamentoTurma.dataUltimoPagamento\", ");
        sb.append(" comissionamentoTurma.valorTotalAReceberTurma AS \"comissionamentoTurma.valorTotalAReceberTurma\", ");
        sb.append(" comissionamentoTurma.totalAlunosPagantes AS \"comissionamentoTurma.totalAlunosPagantes\", ");
        sb.append(" comissionamentoTurma.considerarTicketMedio AS \"comissionamentoTurma.considerarTicketMedio\", ");
        sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\",  ");
        sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
        sb.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");        
        sb.append(" configuracaoRanking.codigo AS \"configuracaoRanking.codigo\", configuracaoRanking.nome AS \"configuracaoRanking.nome\" ");
        sb.append(" FROM comissionamentoTurma ");
        sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sb.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
        sb.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
        return sb;

    }

    private StringBuilder getSQLPadraoConsultaCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT comissionamentoTurma.codigo AS \"comissionamentoTurma.codigo\",  comissionamentoTurma.qtdeParcela AS \"comissionamentoTurma.qtdeParcela\", comissionamentoTurma.dataPrimeiroPagamento AS \"comissionamentoTurma.dataPrimeiroPagamento\", ");
        sb.append(" comissionamentoTurma.dataUltimoPagamento AS \"comissionamentoTurma.dataUltimoPagamento\", ");
        sb.append(" comissionamentoTurma.valorTotalAReceberTurma AS \"comissionamentoTurma.valorTotalAReceberTurma\", ");
        sb.append(" comissionamentoTurma.totalAlunosPagantes AS \"comissionamentoTurma.totalAlunosPagantes\", ");        
        sb.append(" comissionamentoTurma.considerarTicketMedio AS \"comissionamentoTurma.considerarTicketMedio\", ");        
        sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\",  ");
        sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
        sb.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
        sb.append(" configuracaoRanking.codigo AS \"configuracaoRanking.codigo\", configuracaoRanking.nome AS \"configuracaoRanking.nome\" ");
        sb.append(" FROM comissionamentoTurma ");
        sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sb.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
        sb.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
        return sb;
    }

    public List<ComissionamentoTurmaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
        List<ComissionamentoTurmaVO> vetResultado = new ArrayList<ComissionamentoTurmaVO>(0);
        while (tabelaResultado.next()) {
            ComissionamentoTurmaVO obj = new ComissionamentoTurmaVO();
            montarDadosBasico(obj, tabelaResultado, usuarioVO);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosBasico(ComissionamentoTurmaVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        obj.setCodigo(dadosSQL.getInt("comissionamentoTurma.codigo"));
        obj.setQtdeParcela(dadosSQL.getInt("comissionamentoTurma.qtdeParcela"));
        obj.setDataPrimeiroPagamento(dadosSQL.getDate("comissionamentoTurma.dataPrimeiroPagamento"));
        obj.setDataUltimoPagamento(dadosSQL.getDate("comissionamentoTurma.dataUltimoPagamento"));
        obj.setValorTotalAReceberTurma(dadosSQL.getDouble("comissionamentoTurma.valorTotalAReceberTurma"));
        obj.setConsiderarTicketMedio(dadosSQL.getBoolean("comissionamentoTurma.considerarTicketMedio"));
        //Dados Turma
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
        obj.getTurmaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getTurmaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
        //Valor total a receber turma
        //obj.setValorTotalAReceberTurma(consultarTotalAReceberTurma(obj.getTurmaVO().getCodigo(), usuarioVO));
        //total alunos pagantes turma
        //obj.setTotalAlunosPagantes(consultarTotalAlunosPagantes(obj.getTurmaVO().getCodigo()));
        //Dados Configuracao Ranking
        obj.getConfiguracaoRankingVO().setCodigo(dadosSQL.getInt("configuracaoRanking.codigo"));
        obj.getConfiguracaoRankingVO().setNome(dadosSQL.getString("configuracaoRanking.nome"));
    }

    private void montarDadosCompleto(ComissionamentoTurmaVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        obj.setCodigo(dadosSQL.getInt("comissionamentoTurma.codigo"));
        obj.setQtdeParcela(dadosSQL.getInt("comissionamentoTurma.qtdeParcela"));
        obj.setDataPrimeiroPagamento(dadosSQL.getDate("comissionamentoTurma.dataPrimeiroPagamento"));
        obj.setDataUltimoPagamento(dadosSQL.getDate("comissionamentoTurma.dataUltimoPagamento"));
        obj.setValorTotalAReceberTurma(dadosSQL.getDouble("comissionamentoTurma.valorTotalAReceberTurma"));
        obj.setConsiderarTicketMedio(dadosSQL.getBoolean("comissionamentoTurma.considerarTicketMedio"));
        
        //ticketMedio = Uteis.arredondar(((getValorTotalAReceberTurma().doubleValue() / getTotalAlunosPagantes().intValue()) / getQtdeParcela()), 2, 0);
        //Dados Turma
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
        obj.getTurmaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getTurmaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
        //Valor total a receber turma
        obj.setValorTotalAReceberTurma(dadosSQL.getDouble("comissionamentoTurma.valorTotalAReceberTurma"));
        //total alunos pagantes turma
        obj.setTotalAlunosPagantes(dadosSQL.getInt("comissionamentoTurma.totalAlunosPagantes"));
        // Valor do Ticket
        if (obj.getTotalAlunosPagantes().intValue() > 0) {
        	obj.setTicketMedio(Uteis.arredondar(((obj.getValorTotalAReceberTurma().doubleValue() / obj.getTotalAlunosPagantes().intValue()) / obj.getQtdeParcela()), 2, 0));
        } else {
        	obj.setTicketMedio(0.0);
        }
        //Dados Configuracao Ranking
        obj.getConfiguracaoRankingVO().setCodigo(dadosSQL.getInt("configuracaoRanking.codigo"));
        obj.getConfiguracaoRankingVO().setNome(dadosSQL.getString("configuracaoRanking.nome"));
        obj.setListaComissionamentoPorTurmaFaixaValorVOs(getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().consultaRapidaPorComissionamentoTurma(obj.getCodigo(), false, usuarioVO));
    }

    public List<ComissionamentoTurmaVO> consultaRapidaPorTurma(String valorConsulta, Boolean jobComissionamento, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = getSQLPadraoConsultaBasica();
        sb.append("WHERE turma.identificadorTurma ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        if (jobComissionamento) {
        	sb.append(" and current_date >= comissionamentoTurma.dataPrimeiroPagamento and current_date <= comissionamentoTurma.dataUltimoPagamento ");
        	sb.append(" and comissionamentoTurma.comissionamentoAtualizadoMesAtual = false ");
        	sb.append(" ORDER BY comissionamentoTurma.dataComissionamentoAtualizadoMesAtual desc ");
        } else {
        	sb.append(" ORDER BY turma.identificadorTurma");
        }
        if (limite != null) {
            sb.append(" LIMIT ").append(limite);
            if (offset != null) {
                sb.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaRapida(tabelaResultado, usuario));
    }

    public List<ComissionamentoTurmaVO> consultaRapidaPorCurso(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = getSQLPadraoConsultaBasica();
        sb.append("WHERE curso.nome ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        sb.append(" ORDER BY curso.nome ");
        if (limite != null) {
            sb.append(" LIMIT ").append(limite);
            if (offset != null) {
                sb.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaRapida(tabelaResultado, usuario));
    }

    public List<ComissionamentoTurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = getSQLPadraoConsultaBasica();
        sb.append("WHERE unidadeEnsino.nome ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        sb.append(" ORDER BY unidadeEnsino.nome ");
        if (limite != null) {
            sb.append(" LIMIT ").append(limite);
            if (offset != null) {
                sb.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaRapida(tabelaResultado, usuario));
    }

    public List<ComissionamentoTurmaVO> consultaRapidaPorDataPrimeiroPagamento(Date dataPrimeiroPagamento, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = getSQLPadraoConsultaBasica();
        sb.append("WHERE comissionamentoTurma.dataPrimeiroPagamento = '").append(Uteis.getDataJDBC(dataPrimeiroPagamento)).append("' ");
        sb.append(" ORDER BY comissionamentoTurma.dataPrimeiroPagamento ");
        if (limite != null) {
            sb.append(" LIMIT ").append(limite);
            if (offset != null) {
                sb.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaRapida(tabelaResultado, usuario));
    }

    public Integer consultarTotalDeGegistroPorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT DISTINCT COUNT(comissionamentoTurma.codigo) FROM comissionamentoTurma ");
        sqlsb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sqlsb.append(" WHERE turma.identificadorTurma ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultarTotalDeGegistroPorCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT DISTINCT COUNT(comissionamentoTurma.codigo) FROM comissionamentoTurma ");
        sqlsb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sqlsb.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
        sqlsb.append(" WHERE curso.nome ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Double consultarTotalAReceberTurma(Integer turma, UsuarioVO usuario) throws Exception {
//		List<ContaReceberVO> listaContaReceber = getFacadeFactory().getContaReceberFacade().consultarTodasContaAReceberTurma(turma, new ConfiguracaoFinanceiroVO(), usuario);
    	Double valorTotal = 0.0;
//		Iterator i = listaContaReceber.iterator();
//    	while (i.hasNext()) {
//    		ContaReceberVO conta = (ContaReceberVO)i.next();
//    		valorTotal = valorTotal + conta.getValorRecebido(); 
//    	}
//    	StringBuilder sqlsb1 = new StringBuilder();
//    	sqlsb1.append(" select sum(t.totalaluno) as valor from (SELECT DISTINCT ON (pessoa.nome, matricula.matricula) matricula.matricula as matricula, (SELECT count(cr.codigo) FROM matriculaperiodo mp INNER JOIN matricula mt ON (mp.matricula = mt.matricula) INNER JOIN contareceber cr ON ( mp.codigo = cr.matriculaperiodo)  ");
//    	sqlsb1.append(" WHERE mp.matricula = matriculaperiodo.matricula AND matriculaperiodo.turma = mp.turma AND cr.matriculaperiodo = mp.codigo AND cr.parcela <> 'Matrícula' AND mt.curso = turma.curso AND cr.pessoa is not null ) as quantidadeTotalParcelas,  contareceber.datavencimento as dataVencimento, condicaopagamentoplanofinanceirocurso.variavel1 as valorParcela,  ");
//    	sqlsb1.append(" CASE WHEN (matriculaperiodo.bolsista = true) THEN 'S' ELSE 'N' END as bolsa, CASE WHEN (valordescontocalculadoprimeirafaixadescontos is not null) THEN valordescontocalculadoprimeirafaixadescontos ELSE contareceber.valor - (calculadescontoporordem2(contaReceber.codigo,   (CASE WHEN (contaReceber.descontoProgressivo is not null)  ");
//    	sqlsb1.append(" THEN (SELECT dialimite1 FROM descontoProgressivo  WHERE contaReceber.descontoProgressivo = descontoProgressivo.codigo ) ELSE 25 END) ))[5] END as valordescontocalculadoprimeirafaixadescontos, pConsultor.nome as nomeConsultor , (condicaopagamentoplanofinanceirocurso.nrparcelasperiodo * valordescontocalculadoprimeirafaixadescontos) as totalaluno ");
//    	sqlsb1.append(" FROM matriculaperiodo INNER JOIN matricula ON (matriculaperiodo.matricula = matricula.matricula) INNER JOIN curso ON matricula.curso = curso.codigo INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
//    	sqlsb1.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo) INNER JOIN condicaopagamentoplanofinanceirocurso ON (condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso) ");
//    	sqlsb1.append(" LEFT JOIN contareceber ON ( matriculaperiodo.codigo = contareceber.matriculaperiodo) and contareceber.pessoa is not null AND (contareceber.parcela like ('1/%') or contareceber.parcela like ('01%') ) AND (contareceber.parcela not like ('%R%')) ");
//    	sqlsb1.append(" LEFT JOIN funcionario fConsultor ON fConsultor.codigo = matricula.consultor LEFT JOIN pessoa pConsultor ON pConsultor.codigo = fConsultor.pessoa ");
//    	sqlsb1.append(" WHERE 1 = 1 AND matricula.curso = turma.curso AND turma.codigo = ").append(turma).append(" ");
//    	sqlsb1.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT' , 'CO') ORDER BY pessoa.nome, matricula.matricula, turma.identificadorturma ) as t");
//    	SqlRowSet resultado1 = getConexao().getJdbcTemplate().queryForRowSet(sqlsb1.toString());
//    	if (resultado1.next()) {
//    		valorTotal = valorTotal + resultado1.getDouble("valor");
//    	}    	
//    	StringBuilder sqlsb = new StringBuilder();
//    	sqlsb.append(" select distinct matricula.matricula FROM contareceber LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) LEFT JOIN parceiro ON (parceiro.codigo = contareceber.parceiro)  ");
//    	sqlsb.append(" LEFT JOIN pessoa as pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) LEFT JOIN pessoa as pessoamatricula ON (matricula.aluno = pessoamatricula.codigo)  ");
//    	sqlsb.append(" LEFT JOIN pessoa as pessoacandidato ON (contareceber.candidato = pessoacandidato.codigo) LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo) LEFT JOIN unidadeensino ON (unidadeensino.codigo = contareceber.unidadeensino)  ");
//    	sqlsb.append(" LEFT JOIN centroreceita ON (centroreceita.codigo = contareceber.centroreceita) LEFT JOIN descontoprogressivo ON (contareceber.descontoprogressivo = descontoprogressivo.codigo) LEFT JOIN pessoa as responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo)  ");
//    	sqlsb.append(" LEFT JOIN turma as turma ON (contareceber.turma = turma.codigo) LEFT JOIN planofinanceiroaluno on (planofinanceiroaluno.matriculaperiodo = contareceber.matriculaperiodo) LEFT JOIN fornecedor ON (fornecedor.codigo = contareceber.fornecedor) INNER JOIN MATRICULAPERIODO ON MATRICULAPERIODO.CODIGO = CONTARECEBER.matriculaperiodo  ");
//    	sqlsb.append(" where matriculaperiodo.turma = ").append(turma).append(" ");
//    	sqlsb.append(" and (matriculaperiodo.bolsista = false or matriculaperiodo.bolsista is null) and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO') and contareceber.situacao <> 'NE' and contareceber.tipoorigem in ('BCC') and contareceber.parcela not ilike '%M%' and contareceber.codigo not in ( select contareceber.codigo from contareceber inner join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
//    	sqlsb.append(" where contareceber.situacao = 'RE' and valorrecebido = 0 and matriculaperiodo.turma = ").append(turma.intValue()).append(")");
//    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
//    	while (resultado.next()) {
//    		String matricula = resultado.getString("matricula");
//    		Double valor = 0.0;
//    		Integer qtd = 0;
//        	StringBuilder sqlvalor = new StringBuilder();
//        	sqlvalor.append(" select DISTINCT contareceber.codigo, (contareceber.valor - (contareceber.valorDescontoAlunoJaCalculado + contareceber.descontoconvenio +  contareceber.descontoinstituicao + contareceber.valorDescontoProgressivo + contareceber.valorCalculadoDescontoLancadoRecebimento)) as valor ");
//        	sqlvalor.append(" from contareceber  left join contarecebernegociacaorecebimento on (contareceber.codigo = contarecebernegociacaorecebimento.contareceber)  and contarecebernegociacaorecebimento.codigo = (select contarecebernegociacaorecebimento.codigo from contarecebernegociacaorecebimento  ");
//        	sqlvalor.append(" where contarecebernegociacaorecebimento.contareceber = contareceber.codigo order by contarecebernegociacaorecebimento.codigo desc limit 1) left join negociacaorecebimento on (contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo)   ");
//        	sqlvalor.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo left join convenio on convenio.codigo = contareceber.convenio where matriculaaluno = '").append(matricula).append("' ");
//        	sqlvalor.append(" and contareceber.tipopessoa = 'PA'  and contareceber.parcela not ilike '%M%'and contareceber.situacao = 'AR' order by contareceber.codigo desc limit 1 ");
//        	SqlRowSet resultadoValor = getConexao().getJdbcTemplate().queryForRowSet(sqlvalor.toString());
//        	if (resultadoValor.next()) {
//        		valor = resultadoValor.getDouble("valor");
//        	}
//        	StringBuilder sqlqtd = new StringBuilder();
//        	sqlqtd.append(" select DISTINCT count(contareceber.codigo) as quantidade from contareceber  left join contarecebernegociacaorecebimento on (contareceber.codigo = contarecebernegociacaorecebimento.contareceber)   ");
//        	sqlqtd.append(" and contarecebernegociacaorecebimento.codigo = (select contarecebernegociacaorecebimento.codigo from contarecebernegociacaorecebimento where contarecebernegociacaorecebimento.contareceber = contareceber.codigo order by contarecebernegociacaorecebimento.codigo desc limit 1)  ");
//        	sqlqtd.append(" left join negociacaorecebimento on (contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo)  left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
//        	sqlqtd.append(" left join convenio on convenio.codigo = contareceber.convenio where matriculaaluno = '").append(matricula).append("' and contareceber.tipopessoa = 'PA'  and contareceber.parcela not ilike '%M%'");
//        	SqlRowSet resultadoQtd = getConexao().getJdbcTemplate().queryForRowSet(sqlqtd.toString());
//        	if (resultadoQtd.next()) {
//        		qtd = resultadoQtd.getInt("quantidade");
//        	}
//        	valorTotal = valorTotal + (qtd * valor);
//    	}    	
    	return valorTotal;
    }    
    
    public Integer consultarTotalAlunosPagantes(Integer turma) throws Exception {
    	StringBuilder sqlsb = new StringBuilder();
    	sqlsb.append(" select count(*) as ticketMedio from (select distinct contareceber.matriculaaluno from matriculaperiodovencimento ");
    	sqlsb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo inner join contareceber on contareceber.codigo = matriculaperiodovencimento.contareceber ");
    	sqlsb.append(" where matriculaperiodo.turma = ").append(turma).append(" ");
    	sqlsb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO')");
    	sqlsb.append(" and contareceber.situacao <> 'NE' and contareceber.tipoorigem in ('MEN', 'BCC') ");
    	sqlsb.append(" and (matriculaperiodo.bolsista = false or matriculaperiodo.bolsista is null) ");
    	
    	//sqlsb.append(" and contareceber.codigo not in ( select contareceber.codigo from contareceber inner join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		//sqlsb.append(" where contareceber.situacao = 'RE' and valorrecebido = 0 and matriculaperiodo.turma = ").append(turma.intValue()).append(")");
    	//sqlsb.append(" and (matriculaperiodo.bolsista = false or matriculaperiodo.bolsista is null)) as t ");
    	sqlsb.append(" ) as t ");
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
    	if (resultado.next()) {
    		return resultado.getInt("ticketMedio");
    	}
    	return 0;
    }
    
    public Integer consultarTotalDeGegistroPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT DISTINCT COUNT(comissionamentoTurma.codigo) FROM comissionamentoTurma ");
        sqlsb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sqlsb.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
        sqlsb.append(" WHERE unidadeEnsino.nome ilike'").append(valorConsulta.toLowerCase()).append("%' ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultarTotalDeGegistroPorDataPrimeiroPagamento(Date dataPrimeiroPagamento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT DISTINCT COUNT(comissionamentoTurma.codigo) FROM comissionamentoTurma ");
        sqlsb.append(" WHERE dataPrimeiroPagamento = '").append(Uteis.getDataJDBC(dataPrimeiroPagamento)).append("' ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public void adicionarObjComissionamentoFaixaValorVOs(ComissionamentoTurmaVO comissionamentoTurmaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception {
        getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().validarDados(obj);
        getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().validarDadosIntervalorQtdeAluno(comissionamentoTurmaVO.getListaComissionamentoPorTurmaFaixaValorVOs(), obj);
        if (comissionamentoTurmaVO.getConsiderarTicketMedio()) {
        	obj.setValor((comissionamentoTurmaVO.getTicketMedio().doubleValue() * obj.getQtdeInicialAluno()) * (obj.getPercComissao().doubleValue() / 100));        	
        }         
        comissionamentoTurmaVO.getListaComissionamentoPorTurmaFaixaValorVOs().add(obj);
    }

    public void excluirObjComissionamentoFaixaValorVOs(ComissionamentoTurmaVO comissionamentoTurmaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception {
        int index = 0;
        Iterator i = comissionamentoTurmaVO.getListaComissionamentoPorTurmaFaixaValorVOs().iterator();
        while (i.hasNext()) {
            ComissionamentoTurmaFaixaValorVO objExistente = (ComissionamentoTurmaFaixaValorVO) i.next();
            if (objExistente.getQtdeInicialAluno().equals(obj.getQtdeInicialAluno())
                    && objExistente.getQtdeFinalAluno().equals(obj.getQtdeFinalAluno())
                    && objExistente.getValor().equals(obj.getValor())) {
                comissionamentoTurmaVO.getListaComissionamentoPorTurmaFaixaValorVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public void realizarInicializacaoDataUltimoPagamento(ComissionamentoTurmaVO obj) throws Exception {
        obj.setDataUltimoPagamento(Uteis.getDataFutura(obj.getDataPrimeiroPagamento(), GregorianCalendar.MONTH, obj.getQtdeParcela()));
    }

    public List<ConfiguracaoRankingVO> montarListaSelectItemConfiguracaoRanking(Integer unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        resultadoConsulta = getFacadeFactory().getConfiguracaoRankingFacade().consultaRapidaNivelComboBox(unidadeEnsinoLogado, "ATIVO", usuarioVO);
        i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        while (i.hasNext()) {
            ConfiguracaoRankingVO obj = (ConfiguracaoRankingVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        return objs;
    }
 
    public Double consultarValorTotalAReceberTicketMedioCRM(String matricula) {
		String sqlStr = "SELECT valorTotalAReceberTicketMedioCRM FROM ComissionamentoTurmaValorMatricula WHERE matricula = '" + matricula + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valorTotalAReceberTicketMedioCRM");
			
		}
		return 0.0;
	}
    
    public void atualizarComissionamentoTurma(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
		try {	    	
        	List<RankingTurmaVO> lista = getFacadeFactory().getRankingFacade().consultarRankingTurma(unidadeEnsino, curso, turma, valorConsultaMes, usuarioVO);
        	Iterator i = lista.iterator();
        	while (i.hasNext()) {
        		RankingTurmaVO ranking = (RankingTurmaVO)i.next();
        		Iterator j = ranking.getRankingVOs().iterator();
        		Double valorTotal = 0.0;
        		Integer qtdAluno = 0;
        		while (j.hasNext()) {
        			RankingVO r = (RankingVO)j.next();
        			Iterator o = r.getRankingTurmaConsultorAlunoAptoVOs().iterator();
//        			// System.out.println("COMISSIONAMENTO =>>>>>>>>>>>  ");
        			while (o.hasNext()) {
        				RankingTurmaConsultorAlunoVO rankingTurmaConsultorAlunoVO = (RankingTurmaConsultorAlunoVO)o.next();
        				qtdAluno = qtdAluno + 1;
        				String matricula = rankingTurmaConsultorAlunoVO.getMatriculaVO().getMatricula();
//        				if (matricula.equals("001049000763") ||
//        						matricula.equals("001049000708") ||
//        						matricula.equals("001049000714") ||
//        						matricula.equals("001049000720") ||
//        						matricula.equals("001049000778") ||
//        						matricula.equals("001049000715") ||
//        						matricula.equals("001049000752") ||
//        						matricula.equals("001049000735") ||
//        						matricula.equals("001049000754")) {
//        					// System.out.println("MATRICULA = " + matricula);
            				List<ContaReceberVO> listaContaReceber = getFacadeFactory().getContaReceberFacade().consultarTodasContaAReceberTurma(matricula, new ConfiguracaoFinanceiroVO(), null);
            				Iterator k = listaContaReceber.iterator();        				
            				Double valorAluno = 0.0;
            		    	while (k.hasNext()) {
            		    		ContaReceberVO conta = (ContaReceberVO)k.next();
            		    		valorTotal = valorTotal + conta.getValorRecebido();
            		    		valorAluno = valorAluno + conta.getValorRecebido();
            		    	}
            		    	this.alterarValorTotalAReceberTicketMedioCRMMatriculaAluno(matricula, valorAluno, usuarioVO);
            		    	// System.out.println("VALOR TOTAL ALUNO = " + valorAluno);
            		    	// System.out.println("VALOR TOTAL COMISSAO = " + valorTotal);
//        				}

        			}
        		}
        		getFacadeFactory().getComissionamentoTurmaFacade().alterarValorETotalPagantes(ranking.getTurmaVO().getCodigo(), valorTotal, qtdAluno, usuarioVO);
        		// System.out.println(valorTotal);
        		// System.out.println(qtdAluno);
        	}        	
        } catch (Exception e) {
            e.getMessage();
        }
	}
}