package negocio.facade.jdbc.academico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
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

import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ControleLivroRegistroDiplomaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ControleLivroRegistroDiplomaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ControleLivroRegistroDiplomaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ControleLivroRegistroDiplomaVO
 * @see SuperEntidade
*/
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ControleLivroRegistroDiploma extends ControleAcesso implements ControleLivroRegistroDiplomaInterfaceFacade {
	
	private static final long serialVersionUID = -8902357054344793625L;

	protected static String idEntidade;
	
    public ControleLivroRegistroDiploma() throws Exception {
        super();
        setIdEntidade("ControleLivroRegistroDiploma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ControleLivroRegistroDiplomaVO</code>.
    */
    public ControleLivroRegistroDiplomaVO novo() throws Exception {
        ControleLivroRegistroDiploma.incluir(getIdEntidade());
        ControleLivroRegistroDiplomaVO obj = new ControleLivroRegistroDiplomaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ControleLivroRegistroDiplomaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleLivroRegistroDiplomaVO obj, List listaControleLivroFolhaReciboVOs, UsuarioVO usuario) throws Exception {
        try {
            ControleLivroRegistroDiplomaVO.validarDados(obj);
            ControleLivroRegistroDiploma.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO ControleLivroRegistroDiploma( nrLivro, nrFolhaRecibo, situacaoFechadoAberto,"
            		+ " nrMaximoFolhasLivro,numeroRegistro, tipolivroregistrodiplomaenum, niveleducacional, quantidadeRegistroPorFolha, curso )"
            		+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getNrLivro().intValue() );
                    sqlInserir.setInt(2, obj.getNrFolhaRecibo().intValue() );
                    sqlInserir.setString(3, obj.getSituacaoFechadoAberto());
                    sqlInserir.setInt(4, obj.getNrMaximoFolhasLivro().intValue() );                    
                    sqlInserir.setInt(5, obj.getNumeroRegistro());
                    sqlInserir.setString(6, obj.getTipoLivroRegistroDiplomaEnum().name());
                    sqlInserir.setString(7, obj.getNivelEducacional());
                    sqlInserir.setInt(8, obj.getQuantidadeRegistroPorFolha());
                    if (obj.getCurso().getCodigo().intValue() != 0 ) {
                        sqlInserir.setInt(9, obj.getCurso().getCodigo().intValue() );
                    }else {
                        sqlInserir.setNull(9, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));            
            getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().persistir(obj, usuario);
            getFacadeFactory().getControleLivroFolhaReciboFacade().alterarListaControleLivroFolhaRecibo(obj.getCodigo(), listaControleLivroFolhaReciboVOs, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleLivroRegistroDiplomaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ControleLivroRegistroDiplomaVO obj, List listaControleLivroFolhaReciboVOs, UsuarioVO usuario) throws Exception {
        try {
            ControleLivroRegistroDiplomaVO.validarDados(obj);
            ControleLivroRegistroDiploma.alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE ControleLivroRegistroDiploma set nrLivro=?, nrFolhaRecibo=?, "
            		+ "situacaoFechadoAberto=?, nrMaximoFolhasLivro=?, numeroRegistro=?, "
            		+ "tipolivroregistrodiplomaenum=?, niveleducacional=?, quantidadeRegistroPorFolha=?, curso=? "
            		+ "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getNrLivro().intValue() );
                    sqlAlterar.setInt(2, obj.getNrFolhaRecibo().intValue() );
                    sqlAlterar.setString(3, obj.getSituacaoFechadoAberto() );
                    sqlAlterar.setInt(4, obj.getNrMaximoFolhasLivro().intValue() );
                    sqlAlterar.setInt(5, obj.getNumeroRegistro());
                    sqlAlterar.setString(6, obj.getTipoLivroRegistroDiplomaEnum().name() );
                    sqlAlterar.setString(7, obj.getNivelEducacional() );
                    sqlAlterar.setInt(8, obj.getQuantidadeRegistroPorFolha());
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getCurso().getCodigo().intValue() );
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setInt(10, obj.getCodigo().intValue() );
                 return sqlAlterar;
                }
            });
            getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().persistir(obj, usuario);
            getFacadeFactory().getControleLivroFolhaReciboFacade().alterarListaControleLivroFolhaRecibo(obj.getCodigo(), listaControleLivroFolhaReciboVOs, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleLivroRegistroDiplomaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final ControleLivroRegistroDiplomaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleLivroRegistroDiploma.excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().excluir(obj.getCodigo(), usuario);
            final String sql = "DELETE FROM ControleLivroRegistroDiploma WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlExcluir = con.prepareStatement(sql);
                    sqlExcluir.setInt( 1, obj.getCodigo().intValue() );
                    
                    return sqlExcluir;
                }
            });
            
        } catch (Exception e) {
            throw e;
        }
    }

//    public ControleLivroRegistroDiplomaVO consultarPorUnidadeEnsinoCurso(int unidadeEnsino, int curso, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//        String sqlStr = "SELECT ControleLivroRegistroDiploma.* FROM ControleLivroRegistroDiploma, Curso , UnidadeEnsino WHERE ControleLivroRegistroDiploma.situacaofechadoaberto like '"+situacao+"%' and ControleLivroRegistroDiploma.curso = Curso.codigo and  (Curso.codigo = " + curso + ") and  ControleLivroRegistroDiploma.unidadeEnsino = UnidadeEnsino.codigo and ( UnidadeEnsino.codigo = " + unidadeEnsino + ") ORDER BY ControleLivroRegistroDiploma.nrlivro";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		if (!tabelaResultado.next()){
//			return null;
//		}
//    	return montarDados(tabelaResultado, nivelMontarDados, usuario);
//    }
    
    public ControleLivroRegistroDiplomaVO consultarPorChavePrimaria(int valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleLivroRegistroDiploma.* FROM ControleLivroRegistroDiploma WHERE ControleLivroRegistroDiploma.codigo = "+valorConsulta+"";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()){
			return null;
		}
    	return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }
    
    
    /**
     * Responsável por realizar uma consulta de <code>ControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleLivroRegistroDiploma.* FROM ControleLivroRegistroDiploma left join Curso on ControleLivroRegistroDiploma.curso = Curso.codigo";
        if(Uteis.isAtributoPreenchido(valorConsulta.replaceAll("%", ""))) {
        	sqlStr += " WHERE upper( Curso.nome ) like ? "; 
        }
        sqlStr += " ORDER BY Curso.nome, ControleLivroRegistroDiploma.nrLivro ";
        if(Uteis.isAtributoPreenchido(valorConsulta.replaceAll("%", ""))) {
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleLivroRegistroDiploma.* FROM ControleLivroRegistroDiploma WHERE ";
		sqlStr += "EXISTS(SELECT clu.controleLivroRegistroDiploma FROM controlelivroregistrodiplomaunidadeensino clu ";
		sqlStr += "INNER JOIN unidadeensino un ON un.codigo = clu.unidadeensino ";
		sqlStr += "INNER JOIN controleLivroRegistroDiploma clr ON ControleLivroRegistroDiploma.codigo = clu.controleLivroRegistroDiploma ";
		sqlStr += "WHERE un.nome ilike(sem_acentos('%"+valorConsulta+"%')))";
		sqlStr += "ORDER BY ControleLivroRegistroDiploma.codigo, ControleLivroRegistroDiploma.nrLivro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleLivroRegistroDiploma WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public ControleLivroRegistroDiplomaVO consultarPorCodigoCurso(Integer valorConsulta, String niveEducacional,  TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM ControleLivroRegistroDiploma");
        sqlStr.append(" WHERE curso = ").append(valorConsulta);
        if(Uteis.isAtributoPreenchido(niveEducacional)) {
        	sqlStr.append(" and niveleducacional='").append(niveEducacional).append("' ");
        }
        if(Uteis.isAtributoPreenchido(tipoLivroRegistroDiplomaEnum)) {
        	sqlStr.append(" and tipolivroregistrodiplomaenum='").append(tipoLivroRegistroDiplomaEnum.name()).append("' ");
        }
        sqlStr.append(" and situacaofechadoaberto='AB' ");
        sqlStr.append(" ORDER BY nrLivro desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()){
			return null;
		}
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }
    
    @Override
    public Integer consultarMaxNumeroRegistro(Integer valorConsulta, Boolean zerarPorCurso) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso);
    	String sqlStr = "";
    	if (zerarPorCurso) {
    		sqlStr = "select max(numeroregistro) as maximonumeroregistro from controlelivroregistrodiploma WHERE curso = " + valorConsulta.intValue() + " ";	
    	} else {
    		sqlStr = "select max(numeroregistro) as maximonumeroregistro from controlelivroregistrodiploma ";
    	}
        
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if(tabelaResultado.next()){
        	return tabelaResultado.getInt("maximonumeroregistro");
        }else{
        	return 0;
        }
    }
    
    
    

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ControleLivroRegistroDiplomaVO</code> resultantes da consulta.
    */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>.
     * @return  O objeto da classe <code>ControleLivroRegistroDiplomaVO</code> com os dados devidamente montados.
    */
    public static ControleLivroRegistroDiplomaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleLivroRegistroDiplomaVO obj = new ControleLivroRegistroDiplomaVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setNrLivro( new Integer( dadosSQL.getInt("nrLivro")));
        obj.setNrFolhaRecibo( new Integer( dadosSQL.getInt("nrFolhaRecibo")));
        obj.setSituacaoFechadoAberto( dadosSQL.getString("situacaoFechadoAberto"));
        obj.setNrMaximoFolhasLivro( new Integer( dadosSQL.getInt("nrMaximoFolhasLivro")));
        obj.setNumeroRegistro(dadosSQL.getInt("numeroRegistro"));
        obj.setTipoLivroRegistroDiplomaEnum(TipoLivroRegistroDiplomaEnum.valueOf(dadosSQL.getString("tipolivroregistrodiplomaenum")));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setQuantidadeRegistroPorFolha(dadosSQL.getInt("quantidadeRegistroPorFolha"));
        obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
        obj.setUnidadeConsultaApresentar(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarUnidadesVinculadas(obj.getCodigo()));
        obj.setControleLivroRegistroDiplomaUnidadeEnsinoVOs(getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().consultarPorControleLivroRegistroDiploma(obj.getCodigo()));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

//        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ControleLivroRegistroDiplomaVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCurso(ControleLivroRegistroDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }


    /**
     * Operação responsável por localizar um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public ControleLivroRegistroDiplomaVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ControleLivroRegistroDiploma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ControleLivroRegistroDiploma ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    
    /**
     * Operação responsável por localizar um objeto da classe <code>ControleLivroRegistroDiplomaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
//    public Integer obterNumeroLivroRegistro(int unidadeEnsino, int curso, UsuarioVO usuario) throws Exception {
//    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
//        String sqlStr = "SELECT MAX(nrlivro) as ultimoNrlivro FROM ControleLivroRegistroDiploma, Curso , UnidadeEnsino WHERE ControleLivroRegistroDiploma.curso = Curso.codigo and  (Curso.codigo = " + curso + ") and  ControleLivroRegistroDiploma.unidadeEnsino = UnidadeEnsino.codigo and ( UnidadeEnsino.codigo = " + unidadeEnsino + ")";
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//        int ultimoLivro = (0);
//        if (tabelaResultado.next()) {
//            ultimoLivro = tabelaResultado.getInt("ultimoNrlivro");
//        }
//        return (ultimoLivro);
//    }

    public String obterNumeroRegistroMatricula(String matricula) throws Exception {
    	String sqlStr = "select numeroRegistro from controlelivrofolharecibo WHERE matricula = '" + matricula + "' order by numeroRegistro desc limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	String numeroRegistro = "";
    	if (tabelaResultado.next()) {
    		numeroRegistro = tabelaResultado.getString("numeroRegistro");
    	}
    	return (numeroRegistro);
    }

    public String obterNumeroRegistroMatriculaPrimeiraVia(String matricula) throws Exception {
    	String sqlStr = "select numeroRegistro from controlelivrofolharecibo WHERE matricula = '" + matricula + "'order by numeroRegistro limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	String numeroRegistro = "";
    	if (tabelaResultado.next()) {
    		numeroRegistro = tabelaResultado.getString("numeroRegistro");
    	}
    	return (numeroRegistro);
    }
    
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return ControleLivroRegistroDiploma.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        ControleLivroRegistroDiploma.idEntidade = idEntidade;
    }
    
    @Override
	public List<ControleLivroRegistroDiplomaVO> consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ControleLivroRegistroDiploma.* ");
		sqlStr.append("FROM ControleLivroRegistroDiploma ");
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
			sqlStr.append("left JOIN ControleLivroFolhaRecibo on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
			sqlStr.append("left JOIN Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
			sqlStr.append("left JOIN Pessoa aluno on aluno.codigo = Matricula.aluno ");
		sqlStr.append("WHERE sem_acentos(aluno.nome) ilike sem_acentos(?) ");
		}
		sqlStr.append(" ORDER BY ControleLivroRegistroDiploma.nrLivro, ControleLivroRegistroDiploma.nrFolhaRecibo");
		SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta.toLowerCase() + "%");
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}		
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
    
    @Override
    public List<ControleLivroRegistroDiplomaVO> consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("SELECT DISTINCT ControleLivroRegistroDiploma.* ");
    	sqlStr.append("FROM ControleLivroRegistroDiploma ");
    	if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
    		sqlStr.append("left JOIN ControleLivroFolhaRecibo on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
    		sqlStr.append("left JOIN Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
    		sqlStr.append("left JOIN Pessoa aluno on aluno.codigo = Matricula.aluno ");
    		sqlStr.append("WHERE Matricula.matricula ilike ? ");
    	}
    	sqlStr.append(" ORDER BY ControleLivroRegistroDiploma.nrLivro, ControleLivroRegistroDiploma.nrFolhaRecibo");
    	SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}	
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    @Override
	public ControleLivroRegistroDiplomaVO consultarPorUnidadeEnsinoCursoLivro(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, int curso, int livro, String situacao, String tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ControleLivroRegistroDiploma.* ");
		sqlStr.append("FROM ControleLivroRegistroDiploma ");
		sqlStr.append("INNER JOIN controlelivroregistrodiplomaunidadeensino on ControleLivroRegistroDiploma.codigo = controlelivroregistrodiplomaunidadeensino.ControleLivroRegistroDiploma ");
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr.append("WHERE controlelivroregistrodiplomaunidadeensino.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr.append(unidade.getUnidadeEnsino().getCodigo() + ")");
				} else {
					sqlStr.append(unidade.getUnidadeEnsino().getCodigo() + ",");
				}
			}
		} else {
			sqlStr.append("WHERE controlelivroregistrodiplomaunidadeensino.unidadeEnsino = 0 ");
		}
		if(curso != 0){
			sqlStr.append(" AND curso = ").append(curso);
		}
		sqlStr.append(" AND nrLivro = ").append(livro);
		if (Uteis.isAtributoPreenchido(tipoLivro)) {
			sqlStr.append(" AND tipolivroregistrodiplomaenum = '").append(tipoLivro).append("'");
		}
		if (Uteis.isAtributoPreenchido(situacao)) {
			sqlStr.append(" AND situacaoFechadoAberto = '").append(situacao).append("'");
		}
		sqlStr.append("ORDER BY codigo desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new ControleLivroRegistroDiplomaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
    
	@Override
	public Integer consultarPorUnidadeEnsinoCursoUltimoLivroCadastrado(int unidadeEnsino, int curso, String situacao, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ControleLivroRegistroDiploma.nrLivro ");
		sqlStr.append("FROM ControleLivroRegistroDiploma ");
		sqlStr.append("WHERE unidadeEnsino = ").append(unidadeEnsino);
		if(Uteis.isAtributoPreenchido(curso)) {
		sqlStr.append(" AND curso = ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
		sqlStr.append(" AND niveleducacional = '").append(nivelEducacional).append("'");
		sqlStr.append(" AND codigo = (select max(codigo) from controlelivroregistrodiploma) ");
		}
		if (Uteis.isAtributoPreenchido(situacao)) {
			sqlStr.append(" AND situacaoFechadoAberto = '").append(situacao).append("'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getInt("nrLivro");
		}
		return 0;
	}
	
	@Override
    public List<ControleLivroRegistroDiplomaVO> consultarPorTipoLivroRegistro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("SELECT DISTINCT ControleLivroRegistroDiploma.* ");
    	sqlStr.append("FROM ControleLivroRegistroDiploma ");
    	sqlStr.append("WHERE ControleLivroRegistroDiploma.tipolivroregistrodiplomaenum = '").append(valorConsulta).append("' ");
    	sqlStr.append("ORDER BY ControleLivroRegistroDiploma.nrLivro, ControleLivroRegistroDiploma.nrFolhaRecibo");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
	public List<ControleLivroRegistroDiplomaVO> consultarPorNumeroLivroRegistro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ControleLivroRegistroDiploma.* ");
		sqlStr.append("FROM ControleLivroRegistroDiploma ");
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
		sqlStr.append("INNER JOIN ControleLivroFolhaRecibo on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append("WHERE ControleLivroFolhaRecibo.numeroregistro::text like ? ");
		}
		sqlStr.append(" ORDER BY ControleLivroRegistroDiploma.nrLivro, ControleLivroRegistroDiploma.nrFolhaRecibo");
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public void validarFolhaReciboAtualMenorUltimo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(listaControleLivroFolhaRecibo)) {
			Comparator<ControleLivroFolhaReciboVO> comparator = Comparator.comparing( ControleLivroFolhaReciboVO::getFolhaReciboAtual );
			Integer maiorFolhaRecibo = listaControleLivroFolhaRecibo.stream().max(comparator).get().getFolhaReciboAtual();
			if (controleLivroRegistroDiplomaVO.getNrFolhaRecibo() < maiorFolhaRecibo) {
				throw new ConsistirException("O campo FOLHA/RECIBO ATUAL não pode ter um valor menor e/ou mesmo valor do maior FOLHA/RECIBO ATUAL lista de Aluno(s).");
			}
		}
		
	}

	@Override
	public void validarNumeroRegistroMenorUltimo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(listaControleLivroFolhaRecibo)) {
			Comparator<ControleLivroFolhaReciboVO> comparator = Comparator.comparing( ControleLivroFolhaReciboVO::getNumeroRegistro );
			Integer maiorNumeroRegistro = listaControleLivroFolhaRecibo.stream().max(comparator).get().getNumeroRegistro();
			if (controleLivroRegistroDiplomaVO.getNumeroRegistro() < maiorNumeroRegistro) {
				throw new ConsistirException("O campo ÚLTIMO REGISTRO não pode ter um valor menor e/ou mesmo valor do maior ÚLTIMO REGISTRO lista de Aluno(s).");
			}
		}
	}
	
	
	
	@Override
    public List<ControleLivroRegistroDiplomaVO> consultarPorRegistroAcademico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("SELECT DISTINCT ControleLivroRegistroDiploma.* ");
    	sqlStr.append("FROM ControleLivroRegistroDiploma ");
    	if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
    		sqlStr.append("left JOIN ControleLivroFolhaRecibo on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
    		sqlStr.append("left JOIN Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
    		sqlStr.append("left JOIN Pessoa aluno on aluno.codigo = Matricula.aluno ");
    		sqlStr.append("WHERE aluno.registroAcademico ilike ? ");
    	}
    	sqlStr.append(" ORDER BY ControleLivroRegistroDiploma.nrLivro, ControleLivroRegistroDiploma.nrFolhaRecibo");
    	SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(valorConsulta.replace("%", ""))) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}	
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

	@Override
	public String consultarUnidadesVinculadas(Integer codigo) throws Exception {
		String unidades = "";
		List<String> lista = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder("SELECT unidadeensino.nome as \"unidadeensino.nome\" FROM controlelivroregistrodiploma ");
		sql.append("INNER JOIN controlelivroregistrodiplomaunidadeensino ON controlelivroregistrodiploma.codigo = controlelivroregistrodiplomaunidadeensino.controlelivroregistrodiploma ");
		sql.append("INNER JOIN unidadeensino ON unidadeensino.codigo =  controlelivroregistrodiplomaunidadeensino.unidadeensino ");
		sql.append("WHERE controlelivroregistrodiploma.codigo = "+codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			lista.add(tabelaResultado.getString("unidadeensino.nome"));
		}
		for (String unidadeVO : lista) {
			if (!lista.get(lista.size() -1).equals(unidadeVO)) {
				unidades += unidadeVO+"; ";
			} else {
				unidades += unidadeVO;
			}
		}
		if (unidades.length() >= 50) {
			unidades.substring(0, 49);
			return unidades += "...";
		}
		return unidades;
	}
	
	@Override
	public Map<String, String> obterDadosLivroPorMatricula(String matricula, String via) throws Exception {
    	StringBuilder sqlStr = new StringBuilder(); 
    	sqlStr.append("SELECT controlelivrofolharecibo.folhaReciboAtual, controlelivrofolharecibo.numeroregistro, controlelivroregistrodiploma.nrlivro ");
    	sqlStr.append("FROM controlelivrofolharecibo ");
    	sqlStr.append("INNER JOIN controlelivroregistrodiploma ON controlelivroregistrodiploma.codigo = controlelivrofolharecibo.controlelivroregistrodiploma ");
    	sqlStr.append("WHERE controlelivrofolharecibo.matricula = ? ");
    	if (Uteis.isAtributoPreenchido(via)) {
    		sqlStr.append("AND controlelivrofolharecibo.via = '").append(via).append("' ");
    	}
    	sqlStr.append("ORDER BY controlelivrofolharecibo.codigo desc limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
    	Map<String, String> map = new HashMap<>(0);
    	if (tabelaResultado.next()) {
    		map.put(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor(), Uteis.isAtributoPreenchido(tabelaResultado.getString("numeroregistro")) ? tabelaResultado.getString("numeroregistro") : Constantes.EMPTY);
    		map.put(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor(), Uteis.isAtributoPreenchido(tabelaResultado.getString("folhaReciboAtual")) ? tabelaResultado.getString("folhaReciboAtual") : Constantes.EMPTY);
    		map.put(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor(), Uteis.isAtributoPreenchido(tabelaResultado.getString("nrlivro")) ? tabelaResultado.getString("nrlivro") : Constantes.EMPTY);
    	}
    	return map;
    }
	
}