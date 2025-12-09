package negocio.facade.jdbc.pesquisa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.pesquisa.ProjetoPesquisaVO;
import negocio.comuns.pesquisa.PublicacaoPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.pesquisa.PublicacaoPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PublicacaoPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PublicacaoPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PublicacaoPesquisaVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class PublicacaoPesquisa extends ControleAcesso implements PublicacaoPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public PublicacaoPesquisa() throws Exception {
        super();
        setIdEntidade("PublicacaoPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>PublicacaoPesquisaVO</code>.
    */
    public PublicacaoPesquisaVO novo() throws Exception {
        PublicacaoPesquisa.incluir(getIdEntidade());
        PublicacaoPesquisaVO obj = new PublicacaoPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PublicacaoPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PublicacaoPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PublicacaoPesquisaVO obj) throws Exception {
        try {
            
            PublicacaoPesquisaVO.validarDados(obj);
            PublicacaoPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO PublicacaoPesquisa( autorProfessor, autorAluno, orientador, tituloPublicacao, eventoRevistaPublicacao, localPublicacao, anoPublicacao, semestrePublicacao, complementoPublicacao, tipoPesquisador, tipoPublicacao, palavraChave, resumo, curso, unidadeEnsinso, projetoPesquisa, linhaPesquisa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setInt( 1, obj.getAutorProfessor().getCodigo().intValue() );
            sqlInserir.setString( 2, obj.getAutorAluno().getMatricula() );
            sqlInserir.setInt( 3, obj.getOrientador().getCodigo().intValue() );
            sqlInserir.setString( 4, obj.getTituloPublicacao() );
            sqlInserir.setString( 5, obj.getEventoRevistaPublicacao() );
            sqlInserir.setString( 6, obj.getLocalPublicacao() );
            sqlInserir.setInt( 7, obj.getAnoPublicacao().intValue() );
            sqlInserir.setString( 8, obj.getSemestrePublicacao() );
            sqlInserir.setString( 9, obj.getComplementoPublicacao() );
            sqlInserir.setString( 10, obj.getTipoPesquisador() );
            sqlInserir.setString( 11, obj.getTipoPublicacao() );
            sqlInserir.setString( 12, obj.getPalavraChave() );
            sqlInserir.setString( 13, obj.getResumo() );
            sqlInserir.setInt( 14, obj.getCurso().getCodigo().intValue() );
            sqlInserir.setInt( 15, obj.getUnidadeEnsinso().getCodigo().intValue() );
            sqlInserir.setInt( 16, obj.getProjetoPesquisa().getCodigo().intValue() );
            sqlInserir.setInt( 17, obj.getLinhaPesquisa().getCodigo().intValue() );
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
        	obj.setNovoObj(true);
        	throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PublicacaoPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PublicacaoPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PublicacaoPesquisaVO obj) throws Exception {
        try {
           
            PublicacaoPesquisaVO.validarDados(obj);
            PublicacaoPesquisa.alterar(getIdEntidade());
            final String sql = "UPDATE PublicacaoPesquisa set autorProfessor=?, autorAluno=?, orientador=?, tituloPublicacao=?, eventoRevistaPublicacao=?, localPublicacao=?, anoPublicacao=?, semestrePublicacao=?, complementoPublicacao=?, tipoPesquisador=?, tipoPublicacao=?, palavraChave=?, resumo=?, curso=?, unidadeEnsinso=?, projetoPesquisa=?, linhaPesquisa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setInt( 1, obj.getAutorProfessor().getCodigo().intValue() );
            sqlAlterar.setString( 2, obj.getAutorAluno().getMatricula() );
            sqlAlterar.setInt( 3, obj.getOrientador().getCodigo().intValue() );
            sqlAlterar.setString( 4, obj.getTituloPublicacao() );
            sqlAlterar.setString( 5, obj.getEventoRevistaPublicacao() );
            sqlAlterar.setString( 6, obj.getLocalPublicacao() );
            sqlAlterar.setInt( 7, obj.getAnoPublicacao().intValue() );
            sqlAlterar.setString( 8, obj.getSemestrePublicacao() );
            sqlAlterar.setString( 9, obj.getComplementoPublicacao() );
            sqlAlterar.setString( 10, obj.getTipoPesquisador() );
            sqlAlterar.setString( 11, obj.getTipoPublicacao() );
            sqlAlterar.setString( 12, obj.getPalavraChave() );
            sqlAlterar.setString( 13, obj.getResumo() );
            sqlAlterar.setInt( 14, obj.getCurso().getCodigo().intValue() );
            sqlAlterar.setInt( 15, obj.getUnidadeEnsinso().getCodigo().intValue() );
            sqlAlterar.setInt( 16, obj.getProjetoPesquisa().getCodigo().intValue() );
            sqlAlterar.setInt( 17, obj.getLinhaPesquisa().getCodigo().intValue() );
            sqlAlterar.setInt( 18, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PublicacaoPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PublicacaoPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PublicacaoPesquisaVO obj) throws Exception {
        try {
            
            PublicacaoPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM PublicacaoPesquisa WHERE ((codigo = ?))";
           getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>LinhaPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeLinhaPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, LinhaPesquisa WHERE PublicacaoPesquisa.linhaPesquisa = LinhaPesquisa.codigo and LinhaPesquisa.nome like('" + valorConsulta + "%') ORDER BY LinhaPesquisa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>ProjetoPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeProjetoPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, ProjetoPesquisa WHERE PublicacaoPesquisa.projetoPesquisa = ProjetoPesquisa.codigo and ProjetoPesquisa.nome like('" + valorConsulta + "%') ORDER BY ProjetoPesquisa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, UnidadeEnsino WHERE PublicacaoPesquisa.unidadeEnsinso = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, Curso WHERE PublicacaoPesquisa.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>String tipoPublicacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoPublicacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PublicacaoPesquisa WHERE tipoPublicacao like('" + valorConsulta + "%') ORDER BY tipoPublicacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>String tipoPesquisador</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoPesquisador(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PublicacaoPesquisa WHERE tipoPesquisador like('" + valorConsulta + "%') ORDER BY tipoPesquisador";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>String tituloPublicacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTituloPublicacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PublicacaoPesquisa WHERE tituloPublicacao like('" + valorConsulta + "%') ORDER BY tituloPublicacao";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, Matricula WHERE PublicacaoPesquisa.autorAluno = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ORDER BY Matricula.matricula";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PublicacaoPesquisa.* FROM PublicacaoPesquisa, Pessoa WHERE PublicacaoPesquisa.autorProfessor = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PublicacaoPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PublicacaoPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PublicacaoPesquisaVO</code> resultantes da consulta.
    */
       public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,Uteis.NIVELMONTARDADOS_TODOS,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PublicacaoPesquisaVO</code>.
     * @return  O objeto da classe <code>PublicacaoPesquisaVO</code> com os dados devidamente montados.
    */
    public static PublicacaoPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PublicacaoPesquisaVO obj = new PublicacaoPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.getAutorProfessor().setCodigo( new Integer( dadosSQL.getInt("autorProfessor")));
        obj.getAutorAluno().setMatricula( dadosSQL.getString("autorAluno"));
        obj.getOrientador().setCodigo( new Integer( dadosSQL.getInt("orientador")));
        obj.setTituloPublicacao( dadosSQL.getString("tituloPublicacao"));
        obj.setEventoRevistaPublicacao( dadosSQL.getString("eventoRevistaPublicacao"));
        obj.setLocalPublicacao( dadosSQL.getString("localPublicacao"));
        obj.setAnoPublicacao( new Integer( dadosSQL.getInt("anoPublicacao")));
        obj.setSemestrePublicacao( dadosSQL.getString("semestrePublicacao"));
        obj.setComplementoPublicacao( dadosSQL.getString("complementoPublicacao"));
        obj.setTipoPesquisador( dadosSQL.getString("tipoPesquisador"));
        obj.setTipoPublicacao( dadosSQL.getString("tipoPublicacao"));
        obj.setPalavraChave( dadosSQL.getString("palavraChave"));
        obj.setResumo( dadosSQL.getString("resumo"));
        obj.getCurso().setCodigo( new Integer( dadosSQL.getInt("curso")));
        obj.getUnidadeEnsinso().setCodigo( new Integer( dadosSQL.getInt("unidadeEnsinso")));
        obj.getProjetoPesquisa().setCodigo( new Integer( dadosSQL.getInt("projetoPesquisa")));
        obj.getLinhaPesquisa().setCodigo( new Integer( dadosSQL.getInt("linhaPesquisa")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return obj;
        }
        montarDadosAutorProfessor(obj, nivelMontarDados,usuario);
        montarDadosAutorAluno(obj, nivelMontarDados,usuario);
        montarDadosOrientador(obj, nivelMontarDados,usuario);
        montarDadosCurso(obj, nivelMontarDados,usuario);
        montarDadosUnidadeEnsinso(obj, nivelMontarDados, usuario);
        montarDadosProjetoPesquisa(obj, nivelMontarDados,usuario);
        montarDadosLinhaPesquisa(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>LinhaPesquisaVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>LinhaPesquisaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosLinhaPesquisa(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getLinhaPesquisa().getCodigo().intValue() == 0) {
            obj.setLinhaPesquisa(new LinhaPesquisaVO());
            return;
        }
        obj.setLinhaPesquisa(getFacadeFactory().getLinhaPesquisaFacade().consultarPorChavePrimaria( obj.getLinhaPesquisa().getCodigo(), nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ProjetoPesquisaVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>ProjetoPesquisaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosProjetoPesquisa(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getProjetoPesquisa().getCodigo().intValue() == 0) {
            obj.setProjetoPesquisa(new ProjetoPesquisaVO());
            return;
        }
        obj.setProjetoPesquisa(getFacadeFactory().getProjetoPesquisaFacade().consultarPorChavePrimaria( obj.getProjetoPesquisa().getCodigo() ,nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosUnidadeEnsinso(PublicacaoPesquisaVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinso().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinso(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsinso(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria( obj.getUnidadeEnsinso().getCodigo(), false, nivelMontarDados, usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCurso(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria( obj.getCurso().getCodigo(), nivelMontarDados, false, usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosOrientador(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getOrientador().getCodigo().intValue() == 0) {
            obj.setOrientador(new PessoaVO());
            return;
        }
        obj.setOrientador(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria( obj.getOrientador().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAutorAluno(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if ((obj.getAutorAluno().getMatricula() == null) || (obj.getAutorAluno().getMatricula().equals(""))) {
            obj.setAutorAluno(new MatriculaVO());
            return;
        }
        obj.setAutorAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getAutorAluno().getMatricula(),0, NivelMontarDados.getEnum(nivelMontarDados),usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PublicacaoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAutorProfessor(PublicacaoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getAutorProfessor().getCodigo().intValue() == 0) {
            obj.setAutorProfessor(new PessoaVO());
            return;
        }
        obj.setAutorProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria( obj.getAutorProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PublicacaoPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public PublicacaoPesquisaVO consultarPorChavePrimaria( Integer codigoPrm ,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM PublicacaoPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return PublicacaoPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        PublicacaoPesquisa.idEntidade = idEntidade;
    }
}