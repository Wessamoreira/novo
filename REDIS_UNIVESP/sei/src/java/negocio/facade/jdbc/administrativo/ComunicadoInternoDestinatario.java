package negocio.facade.jdbc.administrativo;

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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ComunicadoInternoDestinatarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ComunicadoInternoDestinatarioVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ComunicadoInternoDestinatarioVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ComunicadoInternoDestinatarioVO
 * @see ControleAcesso
 * @see ComunicacaoInterna
 */
@Repository
@Scope("singleton")
@Lazy
public class ComunicadoInternoDestinatario extends ControleAcesso implements ComunicadoInternoDestinatarioInterfaceFacade {

    protected static String idEntidade;

    public ComunicadoInternoDestinatario() throws Exception {
        super();
        setIdEntidade("ComunicacaoInterna");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
     */
    public ComunicadoInternoDestinatarioVO novo() throws Exception {
        ComunicadoInternoDestinatario.incluir(getIdEntidade());
        ComunicadoInternoDestinatarioVO obj = new ComunicadoInternoDestinatarioVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ComunicadoInternoDestinatarioVO obj) throws Exception {
        ComunicadoInternoDestinatarioVO.validarDados(obj);

        final String sql = "INSERT INTO ComunicadoInternoDestinatario( comunicadoInterno, tipoComunicadoInterno, destinatario, ciJaLida, "
        		+ "ciJaRespondida, dataLeitura, removerCaixaEntrada, mensagemMarketingLida, email, nome ) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                if (obj.getComunicadoInterno().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getComunicadoInterno().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getTipoComunicadoInterno());
                if (obj.getDestinatario().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getDestinatario().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                sqlInserir.setBoolean(4, obj.isCiJaLida().booleanValue());
                sqlInserir.setBoolean(5, obj.isCiJaRespondida().booleanValue());
                sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataLeitura()));
                sqlInserir.setBoolean(7, obj.getRemoverCaixaEntrada().booleanValue());
                sqlInserir.setBoolean(8, obj.getMensagemMarketingLida().booleanValue());
                sqlInserir.setString(9, obj.getEmail());
                sqlInserir.setString(10, obj.getNome());
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ComunicadoInternoDestinatarioVO obj) throws Exception {
        ComunicadoInternoDestinatarioVO.validarDados(obj);

        final String sql = "UPDATE ComunicadoInternoDestinatario set comunicadoInterno=?, tipoComunicadoInterno=?, destinatario=?, ciJaLida=?,"
        		+ " ciJaRespondida=?, dataLeitura=?, removerCaixaEntrada=?, mensagemMarketingLida=?, email=?, nome=? "
        		+ "WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                if (obj.getComunicadoInterno().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getComunicadoInterno().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getTipoComunicadoInterno());
                if (obj.getDestinatario().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getDestinatario().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                sqlAlterar.setBoolean(4, obj.isCiJaLida().booleanValue());
                sqlAlterar.setBoolean(5, obj.isCiJaRespondida().booleanValue());
                sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataLeitura()));
                sqlAlterar.setBoolean(7, obj.getRemoverCaixaEntrada().booleanValue());
                sqlAlterar.setBoolean(8, obj.getMensagemMarketingLida().booleanValue());
                sqlAlterar.setString(9, obj.getEmail());
                sqlAlterar.setString(10, obj.getNome());
                sqlAlterar.setInt(11, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarRemoverCaixaEntrada(final Integer destinatario, final Integer comunicado, final Boolean remover, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE ComunicadoInternoDestinatario set ciJaLida=?, removerCaixaEntrada=? WHERE ((comunicadoInterno = ? ) and (destinatario = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setBoolean(1, remover.booleanValue());
                    sqlAlterar.setBoolean(2, remover.booleanValue());
                    sqlAlterar.setInt(3, comunicado.intValue());
                    sqlAlterar.setInt(4, destinatario.intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarMarcarComoLida(final Integer destinatario, final Integer comunicado, final Boolean remover, UsuarioVO usuarioLogado) throws Exception {
        try {
            final String sql = "UPDATE ComunicadoInternoDestinatario set ciJaLida=?, dataLeitura = ? WHERE ((comunicadoInterno = ? ) and (destinatario = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setBoolean(1, remover.booleanValue());
                    sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlAlterar.setInt(3, comunicado.intValue());                    
                    sqlAlterar.setInt(4, destinatario.intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ComunicadoInternoDestinatarioVO obj) throws Exception {

        String sql = "DELETE FROM ComunicadoInternoDestinatario WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        String sqlStr = "SELECT ComunicadoInternoDestinatario.* FROM ComunicadoInternoDestinatario, Pessoa WHERE ComunicadoInternoDestinatario.destinatario = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
     * <code>String tipoComunicadoInterno</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoComunicadoInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        String sqlStr = "SELECT * FROM ComunicadoInternoDestinatario WHERE upper( tipoComunicadoInterno ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoComunicadoInterno";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>ComunicacaoInterna</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ComunicadoInternoDestinatarioVO> consultarPorCodigoComunicacaoInterna(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
    	StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
    	sql.append(" where ci.codigo = ? ");
    	sql.append(" order by cid.codigo ");
    	if(Uteis.isAtributoPreenchido(limit)) {
    		sql.append(" limit  ").append(limit).append(" offset ").append(offset);
    	}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
        if(Uteis.isAtributoPreenchido(limit)) {
        DataModelo dataModelo = new DataModelo();
        dataModelo.setLimitePorPagina(limit);
        dataModelo.setOffset(offset);
        if (tabelaResultado.next()) {
        	dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
    		}
        }
    	tabelaResultado.beforeFirst();
        return montarDadosCompleto(tabelaResultado);
    }
    
    @Override
    public Integer consultarTotalRegistroPorCodigoComunicacaoInterna(Integer valorConsulta) throws Exception {
    	StringBuilder str = new StringBuilder("");
    	str.append(" SELECT count(cid.codigo) as qtde ");
    	str.append(" FROM comunicadoInternoDestinatario as cid ");
    	str.append(" where cid.comunicadoInterno = ? ");    	
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), valorConsulta);
        if(tabelaResultado.next()) {
        	return tabelaResultado.getInt("qtde");
        }
        return 0;
    }
    
    private StringBuffer getSqlConsultaCompleta(){
    	StringBuffer str = new StringBuffer();
    	str.append(" SELECT ");
    	str.append(" count(cid.*) over() as \"qtde_total_registros\", peComunicadoDestinatario.codigo AS \"pessoaDestinatario.codigo\", peComunicadoDestinatario.nome AS \"pessoaDestinatario.nome\", peComunicadoDestinatario.email AS \"pessoaDestinatario.email\",");
		str.append(" cid.tipoComunicadoInterno AS \"destinatario.tipoComunicadoInterno\", cid.ciJaLida AS \"destinatario.ciJaLida\", cid.ciJaRespondida AS \"destinatario.ciJaRespondida\", ");
		str.append(" cid.dataLeitura AS \"destinatario.dataLeitura\", cid.codigo AS \"destinatario.codigo\", cid.comunicadoInterno AS \"destinatario.comunicadoInterno\", ");
		str.append(" cid.nome AS \"destinatario.nome\", cid.email AS \"destinatario.email\", pei.email AS \"emailinstitucional\" ");
    	str.append(" FROM ComunicacaoInterna ci ");
    	str.append(" LEFT JOIN comunicadoInternoDestinatario cid on ci.codigo = cid.comunicadoInterno ");
    	str.append(" LEFT JOIN pessoa peComunicadoDestinatario on peComunicadoDestinatario.codigo = cid.destinatario ");
    	str.append(" LEFT JOIN pessoaemailinstitucional pei on pei.pessoa = peComunicadoDestinatario.codigo ");
    	return str;
    }
    
    private List<ComunicadoInternoDestinatarioVO> montarDadosCompleto(SqlRowSet dadosSQL){
    	List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
    	ComunicadoInternoDestinatarioVO comunicadoDestinatarioVO = null;
    	while(dadosSQL.next()){
    		comunicadoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			// Dados da Comunicacao Interna Destinatario
			comunicadoDestinatarioVO.setCodigo(new Integer(dadosSQL.getInt("destinatario.codigo")));
			comunicadoDestinatarioVO.setTipoComunicadoInterno(dadosSQL.getString("destinatario.tipoComunicadoInterno"));
			comunicadoDestinatarioVO.setComunicadoInterno(dadosSQL.getInt("destinatario.comunicadoInterno"));
			comunicadoDestinatarioVO.setCiJaLida(dadosSQL.getBoolean("destinatario.ciJaLida"));
			comunicadoDestinatarioVO.setCiJaRespondida(dadosSQL.getBoolean("destinatario.ciJaRespondida"));
			comunicadoDestinatarioVO.setDataLeitura(dadosSQL.getDate("destinatario.dataLeitura"));
			comunicadoDestinatarioVO.setEmailInstitucional(dadosSQL.getString("emailinstitucional"));
			// Dados do Destinatario(Pessoa)
			comunicadoDestinatarioVO.getDestinatario().setCodigo(new Integer(dadosSQL.getInt("pessoaDestinatario.codigo")));
			if(Uteis.isAtributoPreenchido(comunicadoDestinatarioVO.getDestinatario().getCodigo())){
				comunicadoDestinatarioVO.getDestinatario().setNome(dadosSQL.getString("pessoaDestinatario.nome"));
				comunicadoDestinatarioVO.getDestinatario().setEmail(dadosSQL.getString("pessoaDestinatario.email"));	
			}else{
				comunicadoDestinatarioVO.getDestinatario().setNome(dadosSQL.getString("destinatario.nome"));
				comunicadoDestinatarioVO.getDestinatario().setEmail(dadosSQL.getString("destinatario.email"));
			}
			comunicadoInternoDestinatarioVOs.add(comunicadoDestinatarioVO);
    	}
    	return comunicadoInternoDestinatarioVOs;
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        String sqlStr = "SELECT * FROM ComunicadoInternoDestinatario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }   
    

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
     * @return  O objeto da classe <code>ComunicadoInternoDestinatarioVO</code> com os dados devidamente montados.
     */
    public static ComunicadoInternoDestinatarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ComunicadoInternoDestinatarioVO obj = new ComunicadoInternoDestinatarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setComunicadoInterno(new Integer(dadosSQL.getInt("comunicadoInterno")));
        obj.setTipoComunicadoInterno(dadosSQL.getString("tipoComunicadoInterno"));
        obj.getDestinatario().setCodigo(new Integer(dadosSQL.getInt("destinatario")));
        obj.setCiJaLida(new Boolean(dadosSQL.getBoolean("ciJaLida")));
        obj.setCiJaRespondida(new Boolean(dadosSQL.getBoolean("ciJaRespondida")));
        obj.setRemoverCaixaEntrada(new Boolean(dadosSQL.getBoolean("removerCaixaEntrada")));
        obj.setMensagemMarketingLida(new Boolean(dadosSQL.getBoolean("mensagemMarketingLida")));
        obj.setDataLeitura(dadosSQL.getDate("dataLeitura"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosDestinatario(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ComunicadoInternoDestinatarioVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDestinatario(ComunicadoInternoDestinatarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDestinatario().getCodigo().intValue() == 0) {
            obj.setDestinatario(new PessoaVO());
            return;
        }
        obj.setDestinatario(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ComunicadoInternoDestinatarioVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ComunicadoInternoDestinatario</code>.
     * @param <code>comunicadoInterno</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComunicadoInternoDestinatarios(Integer comunicadoInterno) throws Exception {
        String sql = "DELETE FROM ComunicadoInternoDestinatario WHERE (comunicadoInterno = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{comunicadoInterno.intValue()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ComunicadoInternoDestinatarioVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirComunicadoInternoDestinatarios</code> e <code>incluirComunicadoInternoDestinatarios</code> disponíveis na classe <code>ComunicadoInternoDestinatario</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarComunicadoInternoDestinatarios(ComunicacaoInternaVO comunicacaoInternaVO, List objetos, UsuarioVO usuarioVO) throws Exception {
        excluirComunicadoInternoDestinatarios(comunicacaoInternaVO.getCodigo());
        incluirComunicadoInternoDestinatarios(comunicacaoInternaVO, objetos, usuarioVO);
    }

    /**
     * Operação responsável por incluir objetos da <code>ComunicadoInternoDestinatarioVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>administrativo.ComunicacaoInterna</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirComunicadoInternoDestinatarios(ComunicacaoInternaVO comunicacaoInternaVO, List objetos, UsuarioVO usuarioVO) throws Exception {
    	try {
    		final String sql = "INSERT INTO ComunicadoInternoDestinatario( comunicadoInterno, tipoComunicadoInterno, destinatario, ciJaLida, "
            		+ "ciJaRespondida, dataLeitura, removerCaixaEntrada, mensagemMarketingLida, email, nome ) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
    		
    		getConexao().getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement sqlInserir, int arg1) throws SQLException {	
						ComunicadoInternoDestinatarioVO obj = ((List<ComunicadoInternoDestinatarioVO>) objetos).get(arg1);
						if (!obj.getDestinatario().getNome().equals("Email Confirmação Envio Comunicado") && !obj.getDestinatario().getMembroComunidade()) {
						obj.setComunicadoInterno(comunicacaoInternaVO.getCodigo());
	                    if (obj.getComunicadoInterno().intValue() != 0) {
	                        sqlInserir.setInt(1, obj.getComunicadoInterno().intValue());
	                    } else {
	                        sqlInserir.setNull(1, 0);
	                    }
	                    sqlInserir.setString(2, obj.getTipoComunicadoInterno());
	                    if (obj.getDestinatario().getCodigo().intValue() != 0) {
	                        sqlInserir.setInt(3, obj.getDestinatario().getCodigo().intValue());
	                    } else {
	                        sqlInserir.setNull(3, 0);
	                    }
	                    sqlInserir.setBoolean(4, obj.isCiJaLida().booleanValue());
	                    sqlInserir.setBoolean(5, obj.isCiJaRespondida().booleanValue());
	                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataLeitura()));
	                    sqlInserir.setBoolean(7, obj.getRemoverCaixaEntrada().booleanValue());
	                    sqlInserir.setBoolean(8, obj.getMensagemMarketingLida().booleanValue());
	                    sqlInserir.setString(9, obj.getEmail());
	                    sqlInserir.setString(10, obj.getNome());	
						}
					
				}
				
				@Override
				public int getBatchSize() {
					return objetos.size();
				}
			});
    		
//            Iterator e = objetos.iterator();
//            while (e.hasNext()) {
//                ComunicadoInternoDestinatarioVO obj = (ComunicadoInternoDestinatarioVO) e.next();
//                obj.setComunicadoInterno(comunicacaoInternaVO.getCodigo());
//                if (!obj.getDestinatario().getNome().equals("Email Confirmação Envio Comunicado") && !obj.getDestinatario().getMembroComunidade()) {
//                    incluir(obj);
//                    if (comunicacaoInternaVO.getCopiaFollowUp()) {
//                    	getFacadeFactory().getHistoricoFollowUpFacade().incluirHistoricoFollowUpPorComunicadoInterno(obj.getDestinatario(), comunicacaoInternaVO, usuarioVO);
//                    }
//                }
//            }
		} catch (Exception e) {
			throw e;
		}
    }

    /**
     * Operação responsável por consultar todos os <code>ComunicadoInternoDestinatarioVO</code> relacionados a um objeto da classe <code>administrativo.ComunicacaoInterna</code>.
     * @param comunicadoInterno  Atributo de <code>administrativo.ComunicacaoInterna</code> a ser utilizado para localizar os objetos da classe <code>ComunicadoInternoDestinatarioVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarComunicadoInternoDestinatarios(Integer comunicadoInterno, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        List objetos = new ArrayList();
        String sql = "SELECT * FROM ComunicadoInternoDestinatario WHERE comunicadoInterno = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{comunicadoInterno.intValue()});
        while (resultado.next()) {
            ComunicadoInternoDestinatarioVO novoObj = new ComunicadoInternoDestinatarioVO();
            novoObj = ComunicadoInternoDestinatario.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ComunicadoInternoDestinatario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ComunicadoInternoDestinatario.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ComunicadoInternoDestinatarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        String sql = "SELECT * FROM ComunicadoInternoDestinatario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ComunicadoInternoDestinatario ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarComunicadoDestinatarioApensaCampoJaLidaDataLeitura(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception {
        final String sql = "UPDATE ComunicadoInternoDestinatario set ciJaLida='TRUE', dataLeitura=' " + Uteis.getDataJDBC(new Date()) + "' WHERE comunicadoInterno = " + codComunicacaoInterna.intValue() + " and destinatario = " + codUsuarioLogado.intValue() + "";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarComunicadoInternoDestinatarioLeituraObrigatoria(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception {
        alterarComunicadoDestinatarioApensaCampoJaLidaDataLeitura(codComunicacaoInterna, codUsuarioLogado);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarComunicadoDestinatarioMensagemMarketingLida(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception {
        final String sql = "UPDATE ComunicadoInternoDestinatario set mensagemMarketingLida='TRUE' WHERE comunicadoInterno = " + codComunicacaoInterna.intValue() + " and destinatario = " + codUsuarioLogado.intValue() + "";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarComunicadoInternoDestinatarioMarketingLida(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception {
        alterarComunicadoDestinatarioMensagemMarketingLida(codComunicacaoInterna, codUsuarioLogado);
    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void alterarComunicadoDestinatarioMensagemComoLidaERespondida(Integer codComunicacaoInterna, Integer codPessoaLogado) throws Exception {
        final String sql = "UPDATE ComunicadoInternoDestinatario set dataLeitura = current_date, ciJaLida = true, ciJaRespondida = true WHERE comunicadoInterno = " + codComunicacaoInterna.intValue() + " and destinatario = " + codPessoaLogado.intValue() + "";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                return sqlAlterar;
            }
        });
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE ComunicadoInternoDestinatario set destinatario=? WHERE ((destinatario = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}

    @Override
    public ComunicadoInternoDestinatarioVO consultarPorComunicadoInterno(Integer comunicadoInterno, Integer nivelMontarDados, UsuarioVO usuario) throws Exception{
    	final String sql = "select * from comunicadointernodestinatario where comunicadointerno = ? and destinatario = ? ";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{comunicadoInterno, usuario.getPessoa().getCodigo()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ComunicadoInternoDestinatario ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
}
