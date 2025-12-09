package negocio.facade.jdbc.academico;

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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DescontoProgressivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DescontoProgressivoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DescontoProgressivoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DescontoProgressivoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DescontoProgressivo extends ControleAcesso implements DescontoProgressivoInterfaceFacade {

    protected static String idEntidade;

    public DescontoProgressivo() throws Exception {
        super();
        setIdEntidade("DescontoProgressivo");
    }

    public DescontoProgressivoVO novo() throws Exception {
        DescontoProgressivo.incluir(getIdEntidade());
        DescontoProgressivoVO obj = new DescontoProgressivoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DescontoProgressivoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DescontoProgressivoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            DescontoProgressivoVO.validarDados(obj);
            DescontoProgressivo.incluir(getIdEntidade(), true, usuario);
            /**
             * ao adicionar alguma informação financeira relevante é necessário alterar as consultas de boleto, remessa para levar em consideração este campos
             */
            final String sql = "INSERT INTO DescontoProgressivo( nome, diaLimite1, percDescontoLimite1, diaLimite2, percDescontoLimite2, diaLimite3, percDescontoLimite3, diaLimite4, percDescontoLimite4, "
                    + "valorDescontoLimite1, valorDescontoLimite2, valorDescontoLimite3, valorDescontoLimite4, ativado, dataAtivacao, responsavelAtivacao, dataInativacao, responsavelInativacao, utilizarDiaFixo, utilizarDiaUtil ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setInt(2, obj.getDiaLimite1().intValue());
                    sqlInserir.setDouble(3, obj.getPercDescontoLimite1().doubleValue());
                    sqlInserir.setInt(4, obj.getDiaLimite2().intValue());
                    sqlInserir.setDouble(5, obj.getPercDescontoLimite2().doubleValue());
                    sqlInserir.setInt(6, obj.getDiaLimite3().intValue());
                    sqlInserir.setDouble(7, obj.getPercDescontoLimite3().doubleValue());
                    sqlInserir.setInt(8, obj.getDiaLimite4().intValue());
                    sqlInserir.setDouble(9, obj.getPercDescontoLimite4().doubleValue());
                    sqlInserir.setDouble(10, obj.getValorDescontoLimite1().doubleValue());
                    sqlInserir.setDouble(11, obj.getValorDescontoLimite2().doubleValue());
                    sqlInserir.setDouble(12, obj.getValorDescontoLimite3().doubleValue());
                    sqlInserir.setDouble(13, obj.getValorDescontoLimite4().doubleValue());
                    sqlInserir.setBoolean(14, obj.getAtivado());
                    sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(16, obj.getResponsavelAtivacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(16, 0);
                    }
                    sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(18, obj.getResponsavelInativacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(18, 0);
                    }
                    sqlInserir.setBoolean(19, obj.getUtilizarDiaFixo());
                    sqlInserir.setBoolean(20, obj.getUtilizarDiaUtil());                    
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

        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DescontoProgressivoVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DescontoProgressivoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            DescontoProgressivoVO.validarDados(obj);
            DescontoProgressivo.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE DescontoProgressivo set nome=?, diaLimite1=?, percDescontoLimite1=?, diaLimite2=?, percDescontoLimite2=?, diaLimite3=?, percDescontoLimite3=?, diaLimite4=?, "
                    + "percDescontoLimite4=?, valorDescontoLimite1=?, valorDescontoLimite2=?, valorDescontoLimite3=?, valorDescontoLimite4=?, ativado=?, dataAtivacao=?, responsavelAtivacao=?, dataInativacao=?, "
                    + "responsavelInativacao=?, utilizarDiaFixo=?, utilizarDiaUtil=?  WHERE ((codigo = ?))"
                    + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setInt(2, obj.getDiaLimite1().intValue());
                    sqlAlterar.setDouble(3, obj.getPercDescontoLimite1().doubleValue());
                    sqlAlterar.setInt(4, obj.getDiaLimite2().intValue());
                    sqlAlterar.setDouble(5, obj.getPercDescontoLimite2().doubleValue());
                    sqlAlterar.setInt(6, obj.getDiaLimite3().intValue());
                    sqlAlterar.setDouble(7, obj.getPercDescontoLimite3().doubleValue());
                    sqlAlterar.setInt(8, obj.getDiaLimite4().intValue());
                    sqlAlterar.setDouble(9, obj.getPercDescontoLimite4().doubleValue());
                    sqlAlterar.setDouble(10, obj.getValorDescontoLimite1().doubleValue());
                    sqlAlterar.setDouble(11, obj.getValorDescontoLimite2().doubleValue());
                    sqlAlterar.setDouble(12, obj.getValorDescontoLimite3().doubleValue());
                    sqlAlterar.setDouble(13, obj.getValorDescontoLimite4().doubleValue());
                    sqlAlterar.setBoolean(14, obj.getAtivado());
                    sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(16, obj.getResponsavelAtivacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(16, 0);
                    }
                    sqlAlterar.setDate(17, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(18, obj.getResponsavelInativacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(18, 0);
                    }
                    sqlAlterar.setBoolean(19, obj.getUtilizarDiaFixo());
                    sqlAlterar.setBoolean(20, obj.getUtilizarDiaUtil());                    
                    sqlAlterar.setInt(21, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DescontoProgressivoVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DescontoProgressivoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            DescontoProgressivo.excluir(getIdEntidade(), true, usuario);
            if(Uteis.isAtributoPreenchido(obj.getCodigo()) && consultarDescontoProgressivoUtilizado(obj.getCodigo())){
            	throw new Exception("Este DESCONTO já está vinculado a CONTA A RECEBER, por isto não pode ser excluído.");
            }
            if(Uteis.isAtributoPreenchido(obj.getCodigo()) && consultarDescontoProgressivoUtilizadoPlanoFinanceiro(obj.getCodigo())){
            	throw new Exception("Este DESCONTO já está vinculado a CONDIÇÃO DE PAGAMENTO DE UM CURSO, por isto não pode ser excluído.");
            }
            String sql = "DELETE FROM DescontoProgressivo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>DescontoProgressivo</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DescontoProgressivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DescontoProgressivo WHERE lower(nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }
    
    public List<DescontoProgressivoVO> consultarPorNomeAtivos(String nome, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DescontoProgressivo WHERE lower(nome) like('" + nome.toLowerCase() + "%') AND ativado = true ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarDescontoProgressivoComboBox(UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT codigo, nome, ativado  FROM DescontoProgressivo ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            DescontoProgressivoVO obj = new DescontoProgressivoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            obj.setAtivado(tabelaResultado.getBoolean("ativado"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }
    public List<DescontoProgressivoVO> consultarDescontoProgressivoAtivosComboBox(UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT codigo, nome, ativado  FROM DescontoProgressivo WHERE ativado = true ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            DescontoProgressivoVO obj = new DescontoProgressivoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            obj.setAtivado(tabelaResultado.getBoolean("ativado"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List consultarDescontoProgressivoPorSituacao(UsuarioVO usuarioVO, Boolean ativo) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT * FROM DescontoProgressivo WHERE ativado = " + ativo.booleanValue() + " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuarioVO));
    }


    /**
     * Responsável por realizar uma consulta de <code>DescontoProgressivo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DescontoProgressivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DescontoProgressivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }
    
    @Override
    public List<DescontoProgressivoVO> consultarPorPlanoFinannceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM DescontoProgressivo WHERE codigo in ( SELECT descontoprogressivo from PlanoFinanceiroAluno  ");
        if(planoFinanceiroAluno != null && planoFinanceiroAluno > 0){
        	sqlStr.append(" where codigo = ").append(planoFinanceiroAluno.intValue());
        }
        sqlStr.append(") ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>DescontoProgressivoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DescontoProgressivoVO</code>.
     *
     * @return O objeto da classe <code>DescontoProgressivoVO</code> com os dados devidamente montados.
     */
    public static DescontoProgressivoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        DescontoProgressivoVO obj = new DescontoProgressivoVO();
        montarDados(dadosSQL, obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DescontoProgressivoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DescontoProgressivoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DescontoProgressivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DescontoProgressivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DescontoProgressivo.idEntidade = idEntidade;
    }

    public void inicializarDadosAtivacao(DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception {
        descontoProgressivoVO.setAtivado(Boolean.TRUE);
        descontoProgressivoVO.setDataAtivacao(new Date());
        descontoProgressivoVO.setResponsavelAtivacao(usuarioVO);
    }

    public void inicializarDadosInativacao(DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception {
        descontoProgressivoVO.setAtivado(Boolean.FALSE);
        descontoProgressivoVO.setDataInativacao(new Date());
        descontoProgressivoVO.setResponsavelInativacao(usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacaoDescontoProgressivo(DescontoProgressivoVO descontoProgressivoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        if(descontoProgressivoVO.getCodigo().equals(0)){
            throw  new ConsistirException("um Desconto Progressivo deve ser selecionado.");
        }
        inicializarDadosAtivacao(descontoProgressivoVO, usuarioVO);
        alterarSituacaoAtivacao(descontoProgressivoVO.getResponsavelAtivacao().getCodigo(), descontoProgressivoVO.getCodigo(), descontoProgressivoVO.getDataAtivacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoAtivacao(Integer responsavelAtivacao, Integer descontoProgressivoVO, Date dataAtivacao) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE DescontoProgressivo set ativado = true, responsavelAtivacao = ?, dataAtivacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelAtivacao, dataAtivacao, descontoProgressivoVO});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacaoDescontoProgressivo(DescontoProgressivoVO descontoProgressivoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        inicializarDadosInativacao(descontoProgressivoVO, usuarioVO);
        alterarSituacaoInativacao(descontoProgressivoVO.getResponsavelInativacao().getCodigo(), descontoProgressivoVO.getCodigo(), descontoProgressivoVO.getDataInativacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoInativacao(Integer responsavelInativacao, Integer descontoProgressivoVO, Date dataInativacao) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE DescontoProgressivo set ativado = false, responsavelInativacao = ?, dataInativacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelInativacao, dataInativacao, descontoProgressivoVO});
    }
    
    
    @Override
    public Boolean consultarDescontoProgressivoUtilizado(Integer codigo) throws Exception {
    	if(Uteis.isAtributoPreenchido(codigo)){
    		StringBuilder sql  = new StringBuilder("(SELECT descontoprogressivo from contareceber where descontoprogressivo = ");
    		sql.append(codigo).append(" limit 1 )");
    		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
    	}
    	return  false;
    }
    
    
    public Boolean consultarDescontoProgressivoUtilizadoPlanoFinanceiro(Integer codigo) throws Exception {
    	if(Uteis.isAtributoPreenchido(codigo)){
    		StringBuilder sql  = new StringBuilder("(SELECT codigo from condicaopagamentoplanofinanceirocurso where (descontoprogressivopadrao = ");
    		sql.append(codigo).append(" or descontoprogressivopadraomatricula = ").append(codigo).append(" or descontoprogressivoprimeiraparcela = ").append(codigo).append(") limit 1 )");
    		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
    	}
    	return  false;
    }
    
    public List<DescontoProgressivoVO> consultarPorNomeSituacao(String nome, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlCons = new StringBuilder();
        sqlCons.append("SELECT * FROM DescontoProgressivo WHERE lower(nome) like('").append(nome.toLowerCase()).append("%')");
        if (!situacao.equals("")) {
        	sqlCons.append(" AND ativado = '").append(situacao).append("' ");
        }
        sqlCons.append(" ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlCons.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }
    
	public List<DescontoProgressivoVO> consultarPorCodigoSituacao(Integer codigo, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlCons = new StringBuilder();
		sqlCons.append("SELECT * FROM DescontoProgressivo WHERE codigo >= ").append(codigo.intValue());
		if (!situacao.equals("")) {
			sqlCons.append(" AND ativado = '").append(situacao).append("' ");
		}
		sqlCons.append(" ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlCons.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	@Override
	public void carregarDados(DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
        String sql = "SELECT * FROM DescontoProgressivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{descontoProgressivoVO.getCodigo()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Desconto Progressivo).");
        }
        montarDados(tabelaResultado, descontoProgressivoVO, usuarioVO);
	}
	
	 /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DescontoProgressivoVO</code>.
     *
     * @return O objeto da classe <code>DescontoProgressivoVO</code> com os dados devidamente montados.
     */
    public static void montarDados(SqlRowSet dadosSQL, DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception {        
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDiaLimite1((dadosSQL.getInt("diaLimite1")));
        obj.setPercDescontoLimite1((dadosSQL.getDouble("percDescontoLimite1")));
        obj.setDiaLimite2((dadosSQL.getInt("diaLimite2")));
        obj.setPercDescontoLimite2((dadosSQL.getDouble("percDescontoLimite2")));
        obj.setDiaLimite3((dadosSQL.getInt("diaLimite3")));
        obj.setPercDescontoLimite3((dadosSQL.getDouble("percDescontoLimite3")));
        obj.setDiaLimite4((dadosSQL.getInt("diaLimite4")));
        obj.setPercDescontoLimite4((dadosSQL.getDouble("percDescontoLimite4")));
        obj.setValorDescontoLimite1((dadosSQL.getDouble("valorDescontoLimite1")));
        obj.setValorDescontoLimite2((dadosSQL.getDouble("valorDescontoLimite2")));
        obj.setValorDescontoLimite3((dadosSQL.getDouble("valorDescontoLimite3")));
        obj.setValorDescontoLimite4((dadosSQL.getDouble("valorDescontoLimite4")));
        obj.setAtivado(dadosSQL.getBoolean("ativado"));
        obj.setUtilizarDiaFixo(dadosSQL.getBoolean("utilizarDiaFixo"));
        obj.setUtilizarDiaUtil(dadosSQL.getBoolean("utilizarDiaUtil"));        
        obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        obj.getResponsavelAtivacao().setCodigo((dadosSQL.getInt("responsavelAtivacao")));
        obj.setDataInativacao(dadosSQL.getDate("dataInativacao"));
        obj.getResponsavelInativacao().setCodigo((dadosSQL.getInt("responsavelInativacao")));
        obj.setNovoObj(Boolean.FALSE);
    }

}
