package negocio.facade.jdbc.faturamento.nfe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalSaidaServicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>NotaFiscalSaidaServicoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>NotaFiscalSaidaServicoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see NotaFiscalSaidaServicoVO
 * @see SuperEntidade
 * @see NotaFiscalSaida
*/
@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalSaidaServico extends ControleAcesso implements NotaFiscalSaidaServicoInterfaceFacade {
	
	private static final long serialVersionUID = 1L;
    protected static String idEntidade;
	
    public NotaFiscalSaidaServico() throws Exception {
        super();
        setIdEntidade("NotaFiscalSaida");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
    */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,  rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public NotaFiscalSaidaServicoVO novo(UsuarioVO usuarioLogado) throws Exception {
        incluir(getIdEntidade(), usuarioLogado);
        NotaFiscalSaidaServicoVO obj = new NotaFiscalSaidaServicoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operação na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>NotaFiscalSaidaServicoVO</code> que serão gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception {
		obj.realizarUpperCaseDados();
		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO notafiscalsaidaservico(");
		sql.append("            notafiscalsaida, precounitario, aliquotaissqn, baseissqn, ");
		sql.append("            totalissqn, descricao, codigonaturezaoperacao, nomenaturezaoperacao, ");
		sql.append("            ncm, aliquotapis, aliquotacofins, totalpis, totalcofins, codigoProduto)");
		sql.append("    VALUES (?, ?, ?, ?, ");
		sql.append("            ?, ?, ?, ?, ");
		sql.append("            ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				if (obj.getNotaFiscalSaida().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getNotaFiscalSaida().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setDouble(2, obj.getPrecoUnitario().doubleValue());
				sqlInserir.setDouble(3, obj.getAliquotaIssqn());
				sqlInserir.setDouble(4, obj.getBaseIssqn());
				sqlInserir.setDouble(5, obj.getTotalIssqn());
				sqlInserir.setString(6, obj.getDescricao());
				sqlInserir.setString(7, obj.getCodigoNaturezaOperacao());
				sqlInserir.setString(8, obj.getNomeNaturezaOperacao());
				sqlInserir.setString(9, obj.getCodigoNCM());
				sqlInserir.setDouble(10, obj.getAliquotaPIS());
				sqlInserir.setDouble(11, obj.getAliquotaCOFINS());
				sqlInserir.setDouble(12, obj.getTotalPIS());
				sqlInserir.setDouble(13, obj.getTotalCOFINS());
				sqlInserir.setInt(14, obj.getCodigoProduto());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return 0;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
		Map<Integer, Integer> mapContaReceberVOs = new HashMap<Integer, Integer>(0);
		for (Integer crr : obj.getListaCodigoContaReceberRecebimento()) {

			getFacadeFactory().getContaReceberRecebimentoFacade().alterarCodigoNotaFiscalSaidaServico(obj.getCodigo(), crr, usuarioLogado);
			ContaReceberVO contaReceberAlterarVO = getFacadeFactory().getContaReceberFacade().consultarPorContaReceberRecebimento(crr, usuarioLogado);
			if (!mapContaReceberVOs.containsKey(contaReceberAlterarVO.getCodigo())) {
				mapContaReceberVOs.put(contaReceberAlterarVO.getCodigo(), contaReceberAlterarVO.getCodigo());
			}

		}
		for (ContaReceberVO crr : obj.getListaCodigoContaReceber()) {
			if (!mapContaReceberVOs.containsKey(crr.getCodigo())) {
				getFacadeFactory().getContaReceberFacade().alterarNotaFiscalSaidaServico(crr, obj.getCodigo(),  usuarioLogado);
			}
		}
	}


    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operação na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>NotaFiscalSaidaServicoVO</code> que serão alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception {
		obj.realizarUpperCaseDados();
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE notafiscalsaidaservico");
		sql.append("   SET notafiscalsaida=?, precounitario=?, aliquotaissqn=?, ");
		sql.append("       baseissqn=?, totalissqn=?, descricao=?, codigonaturezaoperacao=?, ");
		sql.append("       nomenaturezaoperacao=?, ncm=?, aliquotapis=?, aliquotacofins=?, ");
		sql.append("       totalpis=?, totalcofins=?, codigoProduto=?");
		sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				if (obj.getNotaFiscalSaida().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getNotaFiscalSaida().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setDouble(2, obj.getPrecoUnitario().doubleValue());
				sqlAlterar.setDouble(3, obj.getAliquotaIssqn());
				sqlAlterar.setDouble(4, obj.getBaseIssqn());
				sqlAlterar.setDouble(5, obj.getTotalIssqn());
				sqlAlterar.setString(6, obj.getDescricao());
				sqlAlterar.setString(7, obj.getCodigoNaturezaOperacao());
				sqlAlterar.setString(8, obj.getNomeNaturezaOperacao());
				sqlAlterar.setString(9, obj.getCodigoNCM());
				sqlAlterar.setDouble(10, obj.getAliquotaPIS());
				sqlAlterar.setDouble(11, obj.getAliquotaCOFINS());
				sqlAlterar.setDouble(12, obj.getTotalPIS());
				sqlAlterar.setDouble(13, obj.getTotalCOFINS());
				sqlAlterar.setInt(14, obj.getCodigoProduto());
				sqlAlterar.setInt(15, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuarioLogado);
		}
	}

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operação na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>NotaFiscalSaidaServicoVO</code> que serão removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(NotaFiscalSaidaServicoVO obj, UsuarioVO usuarioLogado) throws Exception {
        //NotaFiscalSaidaServico.excluir(getIdEntidade(), usuarioLogado);
        String sql = "DELETE FROM NotaFiscalSaidaServico WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>NotaFiscalSaidaServico</code> através do valor do atributo 
     * <code>codigoNaturezaOperacao</code> da classe <code>NaturezaOperacao</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public List<NotaFiscalSaidaServicoVO> consultarPorCodigoNaturezaOperacaoNaturezaOperacao(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        String sqlStr = "SELECT NotaFiscalSaidaServico.* FROM NotaFiscalSaidaServico, NaturezaOperacao WHERE NotaFiscalSaidaServico.naturezaOperacao = NaturezaOperacao.codigo and NaturezaOperacao.codigoNaturezaOperacao >= " + valorConsulta.intValue() + " ORDER BY NaturezaOperacao.codigoNaturezaOperacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>NotaFiscalSaidaServico</code> através do valor do atributo 
     * <code>abreviatura</code> da classe <code>Servico</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public List<NotaFiscalSaidaServicoVO> consultarPorAbreviaturaServico(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        String sqlStr = "SELECT NotaFiscalSaidaServico.* FROM NotaFiscalSaidaServico, Servico WHERE NotaFiscalSaidaServico.servico = Servico.codigo and upper( Servico.abreviatura ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Servico.abreviatura";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>NotaFiscalSaidaServico</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>NotaFiscalSaida</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public List<NotaFiscalSaidaServicoVO> consultarPorCodigoNotaFiscalSaida(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        String sqlStr = "SELECT NotaFiscalSaidaServico.* FROM NotaFiscalSaidaServico, NotaFiscalSaida WHERE NotaFiscalSaidaServico.notaFiscalSaida = NotaFiscalSaida.codigo and NotaFiscalSaida.codigo >= " + valorConsulta.intValue() + " ORDER BY NotaFiscalSaida.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
    }
    
    
   
    /**
     * Responsável por realizar uma consulta de <code>NotaFiscalSaidaServico</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public List<NotaFiscalSaidaServicoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM NotaFiscalSaidaServico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
    */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class})
    public List<NotaFiscalSaidaServicoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        List<NotaFiscalSaidaServicoVO> vetResultado = new ArrayList<NotaFiscalSaidaServicoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>NotaFiscalSaidaServicoVO</code>.
     * @return  O objeto da classe <code>NotaFiscalSaidaServicoVO</code> com os dados devidamente montados.
    */
    @Transactional(readOnly = false, rollbackFor = {Throwable.class})
    public NotaFiscalSaidaServicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        NotaFiscalSaidaServicoVO obj = new NotaFiscalSaidaServicoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setCodigoProduto(dadosSQL.getInt("codigoProduto"));
        obj.getNotaFiscalSaida().setCodigo(dadosSQL.getInt("notaFiscalSaida"));
        obj.setPrecoUnitario(dadosSQL.getDouble("precoUnitario"));
        obj.setAliquotaIssqn( ( dadosSQL.getDouble("aliquotaIssqn")));
        obj.setBaseIssqn( ( dadosSQL.getDouble("baseIssqn")));
        obj.setTotalIssqn( ( dadosSQL.getDouble("totalIssqn")));
        obj.setCodigoNaturezaOperacao(dadosSQL.getString("codigoNaturezaOperacao"));
        obj.setNomeNaturezaOperacao(dadosSQL.getString("nomeNaturezaOperacao"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setCodigoNCM(dadosSQL.getString("ncm"));
        obj.setAliquotaPIS(dadosSQL.getDouble("aliquotaPIS"));
        obj.setAliquotaCOFINS(dadosSQL.getDouble("aliquotaCOFINS"));
        obj.setTotalPIS(dadosSQL.getDouble("totalPIS"));
        obj.setTotalCOFINS(dadosSQL.getDouble("totalCOFINS"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }


    /**
     * Operação responsável por excluir todos os objetos da <code>NotaFiscalSaidaServicoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>NotaFiscalSaidaServico</code>.
     * @param <code>notaFiscalSaida</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirNotaFiscalSaidaServicos( Integer notaFiscalSaida ) throws Exception {
        String sql = "DELETE FROM NotaFiscalSaidaServico WHERE (notaFiscalSaida = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{notaFiscalSaida});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>NotaFiscalSaidaServicoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirNotaFiscalSaidaServicos</code> e <code>incluirNotaFiscalSaidaServicos</code> disponíveis na classe <code>NotaFiscalSaidaServico</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarNotaFiscalSaidaServicos( Integer notaFiscalSaida, List<NotaFiscalSaidaServicoVO> objetos, UsuarioVO usuarioLogado) throws Exception {
        String str = "DELETE FROM NotaFiscalSaidaServico WHERE notaFiscalSaida = " + notaFiscalSaida;
        Iterator<NotaFiscalSaidaServicoVO> i = objetos.iterator();
        while (i.hasNext()) {
            NotaFiscalSaidaServicoVO objeto = (NotaFiscalSaidaServicoVO)i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);
        
        Iterator<NotaFiscalSaidaServicoVO> e = objetos.iterator();
        while (e.hasNext()) {
            NotaFiscalSaidaServicoVO objeto = (NotaFiscalSaidaServicoVO)e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getNotaFiscalSaida().setCodigo(notaFiscalSaida);
                incluir(objeto, usuarioLogado);
            } else {
                alterar(objeto, usuarioLogado);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>NotaFiscalSaidaServicoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NotaFiscalSaida</code> através do atributo de vï¿½nculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirNotaFiscalSaidaServicos( Integer notaFiscalSaidaPrm, List<NotaFiscalSaidaServicoVO> objetos, UsuarioVO usuarioLogado) throws Exception {
		Iterator<NotaFiscalSaidaServicoVO> e = objetos.iterator();
		while (e.hasNext()) {
			NotaFiscalSaidaServicoVO obj = (NotaFiscalSaidaServicoVO) e.next();
			obj.getNotaFiscalSaida().setCodigo(notaFiscalSaidaPrm);
			incluir(obj, usuarioLogado);
		}
    }

    /**
     * Operação responsável por consultar todos os <code>NotaFiscalSaidaServicoVO</code> relacionados a um objeto da classe <code>financeiro.NotaFiscalSaida</code>.
     * @param notaFiscalSaida  Atributo de <code>financeiro.NotaFiscalSaida</code> a ser utilizado para localizar os objetos da classe <code>NotaFiscalSaidaServicoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>NotaFiscalSaidaServicoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Override
    @Transactional(readOnly = false, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public  List<NotaFiscalSaidaServicoVO> consultarNotaFiscalSaidaServicos(Integer notaFiscalSaida, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {                
        String sql = "SELECT * FROM NotaFiscalSaidaServico WHERE notaFiscalSaida = ?";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{notaFiscalSaida}), nivelMontarDados, usuarioLogado);
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>NotaFiscalSaidaServicoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public NotaFiscalSaidaServicoVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
     //   //consultar(getIdEntidade(), false);
        String sql = "SELECT * FROM NotaFiscalSaidaServico WHERE codigo = ?";
        
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( NotaFiscalSaidaServico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
    }
	

    /**
     * Operação responsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return NotaFiscalSaidaServico.idEntidade;
    }
     
    /**
     * Operação responsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        NotaFiscalSaidaServico.idEntidade = idEntidade;
    }
    
    public List<NotaFiscalSaidaServicoVO> consultarListaServicoNotaFiscaSimplesRemessa(Integer valorConsulta,int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception{
    	List<NotaFiscalSaidaServicoVO> listaNotaFiscalSaidaServico = new ArrayList<NotaFiscalSaidaServicoVO>(0);
		listaNotaFiscalSaidaServico = consultarPorCodigoNotaFiscalSaida(valorConsulta, nivelMontarDados, usuarioLogado);
		return listaNotaFiscalSaidaServico;
    }
}