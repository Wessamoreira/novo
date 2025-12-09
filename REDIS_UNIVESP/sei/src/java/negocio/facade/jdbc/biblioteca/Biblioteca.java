package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.TipoImpressaoComprovanteBibliotecaEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.BibliotecaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>BibliotecaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>BibliotecaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see BibliotecaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Biblioteca extends ControleAcesso implements BibliotecaInterfaceFacade {

    protected static String idEntidade;

    public Biblioteca() throws Exception {
        super();
        setIdEntidade("Biblioteca");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>BibliotecaVO</code>.
     */
    public BibliotecaVO novo() throws Exception {
        Biblioteca.incluir(getIdEntidade());
        BibliotecaVO obj = new BibliotecaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>BibliotecaVO</code>. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>BibliotecaVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final BibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {
            BibliotecaVO.validarDados(obj);
            Biblioteca.incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder("INSERT INTO Biblioteca( nome, endereco, setor, numero, CEP, complemento, cidade, telefone1, telefone2, telefone3, configuracaoBiblioteca, bibliotecaria, ");
            sql.append(" email, turno, tipoImpressaoComprovanteBiblioteca, centroResultado  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo");


            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getEndereco());
                    sqlInserir.setString(3, obj.getSetor());
                    sqlInserir.setString(4, obj.getNumero());
                    sqlInserir.setString(5, obj.getCEP());
                    sqlInserir.setString(6, obj.getComplemento());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setString(8, obj.getTelefone1());
                    sqlInserir.setString(9, obj.getTelefone2());
                    sqlInserir.setString(10, obj.getTelefone3());
                    if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getConfiguracaoBiblioteca().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    if (obj.getBibliotecaria().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(12, obj.getBibliotecaria().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(12, 0);
                    }
                    sqlInserir.setString(13, obj.getEmail());
                    sqlInserir.setInt(14, obj.getTurno().getCodigo());
                    sqlInserir.setString(15, obj.getTipoImpressaoComprovanteBiblioteca().name());
                    int i = 15;
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlInserir);
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
            getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().incluirUnidadeEnsinoBiblioteca(obj.getCodigo(), obj.getUnidadeEnsinoBibliotecaVOs());
            getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().incluirConfiguracaoBibliotecaNivelEducacionalVOs(obj, obj.getConfiguracaoBibliotecaNivelEducacionalVOs(), usuarioVO);
            obj.setNovoObj(Boolean.FALSE);
        } catch (DuplicateKeyException e) {
            obj.setNovoObj(true);
            throw new Exception("Um registro com esse nome já está cadastrado no sistema.");
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>BibliotecaVO</code>. Sempre utiliza
     * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
     * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>BibliotecaVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final BibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {

            BibliotecaVO.validarDados(obj);
            Biblioteca.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Biblioteca set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, cidade=?, telefone1=?, telefone2=?, telefone3=?, configuracaoBiblioteca=?, "
            		+ "bibliotecaria=?, email=?, turno=?, tipoImpressaoComprovanteBiblioteca = ?, centroResultado =?  WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getEndereco());
                    sqlAlterar.setString(3, obj.getSetor());
                    sqlAlterar.setString(4, obj.getNumero());
                    sqlAlterar.setString(5, obj.getCEP());
                    sqlAlterar.setString(6, obj.getComplemento());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setString(8, obj.getTelefone1());
                    sqlAlterar.setString(9, obj.getTelefone2());
                    sqlAlterar.setString(10, obj.getTelefone3());
                    if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(11, obj.getConfiguracaoBiblioteca().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }
                    if (obj.getBibliotecaria().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(12, obj.getBibliotecaria().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    sqlAlterar.setString(13, obj.getEmail());
                    sqlAlterar.setInt(14, obj.getTurno().getCodigo());
                    sqlAlterar.setString(15, obj.getTipoImpressaoComprovanteBiblioteca().name());
                    int i = 15;
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().alterarUnidadeEnsinoBiblioteca(obj.getCodigo(), obj.getUnidadeEnsinoBibliotecaVOs());
            getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().alterarConfiguracaoBibliotecaNivelEducacionalVOs(obj, obj.getConfiguracaoBibliotecaNivelEducacionalVOs(), usuarioVO);
        } catch (DuplicateKeyException e) {
            obj.setNovoObj(true);
            throw new Exception("Um registro com esse nome já está cadastrado no sistema.");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>BibliotecaVO</code>. Sempre localiza o registro
     * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>BibliotecaVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(BibliotecaVO obj,final UsuarioVO usuarioVO) throws Exception {
        try {
            Biblioteca.excluir(getIdEntidade(), true, usuarioVO);
            getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().excluirUnidadeEnsinoBiblioteca(obj.getUnidadeEnsinoBibliotecaVOs());
            getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().excluirConfiguracaoBibliotecaNivelEducacionalVOs(obj, usuarioVO);
            String sql = "DELETE FROM Biblioteca WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Biblioteca</code> através do valor do atributo <code>nome</code>
     * da classe <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct Biblioteca.* FROM Biblioteca ");
        sqlStr.append(" inner join Funcionario on funcionario.codigo = biblioteca.bibliotecaria ");
        sqlStr.append(" inner join Pessoa on pessoa.codigo = funcionario.pessoa ");
        sqlStr.append(" left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo ");
        sqlStr.append(" WHERE upper( Pessoa.nome ) ilike('?')");
		if (unidadeEnsino > 0) {
			sqlStr.append(" and unidadeensinobiblioteca.unidadeensino = ?");
		}
		        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%",unidadeEnsino);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Biblioteca</code> através do valor do atributo <code>codigo</code>
     * da classe <code>ConfiguracaoBiblioteca</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoConfiguracaoBiblioteca(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Biblioteca.* FROM Biblioteca inner join configuracaoBiblioteca on Biblioteca.configuracaoBiblioteca = ConfiguracaoBiblioteca.codigo "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
        		+ " WHERE ConfiguracaoBiblioteca.codigo >= " + valorConsulta.intValue() ;
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Biblioteca</code> através do valor do atributo <code>nome</code>
     * da classe <code>Cidade</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Biblioteca.* FROM Biblioteca inner join Cidade on cidade.codigo = biblioteca.cidade "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
        		+ " WHERE upper(sem_acentos( Cidade.nome )) ilike(sem_acentos('" + valorConsulta.toUpperCase()
                + "%')) ";
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Biblioteca</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct biblioteca.* FROM Biblioteca "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
        		+ " WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino;
        }
		sqlStr += " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Biblioteca</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Biblioteca.* FROM Biblioteca "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
        		+ "WHERE biblioteca.codigo >= " + valorConsulta.intValue() + " ";
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino;
        }
		sqlStr += "ORDER BY biblioteca.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct biblioteca.* FROM biblioteca ");
        sqlStr.append(" inner join unidadeEnsinoBiblioteca ueb on ueb.biblioteca = biblioteca.codigo ");
        if (unidadeEnsino != 0) {
            sqlStr.append("WHERE ueb.unidadeensino = " + unidadeEnsino);
        }
        sqlStr.append(" order by biblioteca.nome ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<BibliotecaVO> consultarPorUnidadeEnsinoNivelComboBox(Integer unidadeEnsino, boolean apenasBibliotecaComExemplar, UsuarioVO usuario) throws Exception {        
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT codigo, nome FROM biblioteca ");
        sqlStr.append("inner join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo ");
        sqlStr.append(" where 1 = 1 ");
        if (unidadeEnsino != 0) {
            sqlStr.append(" and unidadeensinobiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        if(apenasBibliotecaComExemplar) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where bibliotecaatual = biblioteca.codigo and exemplar.situacaoatual in ('DI', 'CO', 'EM') limit 1 ) ");
        }
        sqlStr.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            BibliotecaVO obj = new BibliotecaVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     * 
     * @return List Contendo vários objetos da classe <code>BibliotecaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    private List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>BibliotecaVO</code>.
     * 
     * @return O objeto da classe <code>BibliotecaVO</code> com os dados devidamente montados.
     */
    public static BibliotecaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        BibliotecaVO obj = new BibliotecaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setEndereco(dadosSQL.getString("endereco"));
        obj.setSetor(dadosSQL.getString("setor"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setCEP(dadosSQL.getString("CEP"));
        obj.setComplemento(dadosSQL.getString("complemento"));
        obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
        obj.setTelefone1(dadosSQL.getString("telefone1"));
        obj.setTelefone2(dadosSQL.getString("telefone2"));
        obj.setTelefone3(dadosSQL.getString("telefone3"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.getConfiguracaoBiblioteca().setCodigo(dadosSQL.getInt("configuracaoBiblioteca"));
        obj.getBibliotecaria().setCodigo(dadosSQL.getInt("bibliotecaria"));
        obj.setNovoObj(Boolean.FALSE);
        obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
        obj.setTipoImpressaoComprovanteBiblioteca(TipoImpressaoComprovanteBibliotecaEnum.valueOf(dadosSQL.getString("tipoImpressaoComprovanteBiblioteca")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().consultarPorBiblioteca(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        obj.setConfiguracaoBibliotecaNivelEducacionalVOs(getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().consultarPorBiblioteca(obj.getCodigo(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setCentroResultadoVO(Uteis.montarDadosVO(dadosSQL.getInt("centroresultado"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario)));
        montarDadosTurno(obj, nivelMontarDados, usuario);        
        montarDadosCidade(obj, nivelMontarDados, usuario);
        montarDadosConfiguracaoBiblioteca(obj, nivelMontarDados, usuario);
        montarDadosBibliotecaria(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosTurno(BibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getTurno().getCodigo().intValue() == 0) {
    		obj.setTurno(new TurnoVO());
    		return;
    	}
    	obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
    }
    
    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>BibliotecaVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBibliotecaria(BibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBibliotecaria().getCodigo().intValue() == 0) {
            obj.setBibliotecaria(new FuncionarioVO());
            return;
        }
        obj.setBibliotecaria(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getBibliotecaria().getCodigo(), usuario.getUnidadeEnsinoLogado().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ConfiguracaoBibliotecaVO</code> relacionado
     * ao objeto <code>BibliotecaVO</code>. Faz uso da chave primária da classe <code>ConfiguracaoBibliotecaVO</code>
     * para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosConfiguracaoBiblioteca(BibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() == 0) {
            obj.setConfiguracaoBiblioteca(new ConfiguracaoBibliotecaVO());
            return;
        }
        obj.setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(obj.getConfiguracaoBiblioteca().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto
     * <code>BibliotecaVO</code>. Faz uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCidade(BibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>BibliotecaVO</code> através de sua chave primária.
     * 
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public BibliotecaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM Biblioteca WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Biblioteca ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Biblioteca.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Biblioteca.idEntidade = idEntidade;
    }
   
    public List consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Biblioteca WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public BibliotecaVO consultarPorCodigoCatalogo(Integer codigoCatalogo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select biblioteca from exemplar ");
    	sqlStr.append(" where catalogo = ? "); 
    	sqlStr.append(" limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoCatalogo});
    	if (tabelaResultado.next()) {
    		if (tabelaResultado.getInt("biblioteca") > 0) {
    			return consultarPorChavePrimaria(tabelaResultado.getInt("biblioteca"), nivelMontarDados, usuario);
    		}
    	}
    	return new BibliotecaVO();
    }
}
