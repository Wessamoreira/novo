package negocio.facade.jdbc.administrativo;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CargoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CargoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CargoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CargoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Cargo extends ControleAcesso implements CargoInterfaceFacade {

    private static final long serialVersionUID = 1L;

	protected static String idEntidade;

    public Cargo() throws Exception {
        super();
        setIdEntidade("Cargo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CargoVO</code>.
     */
    public CargoVO novo() throws Exception {
        Cargo.incluir(getIdEntidade());
        CargoVO obj = new CargoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CargoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CargoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CargoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            CargoVO.validarDados(obj);
            incluir(getIdEntidade(), true, usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                	
                	final StringBuilder sql = new StringBuilder("INSERT INTO Cargo ( ")
                			.append(" nome, descricao, responsabilidades, departamento, controlaNivelExperiencia, ")
                			.append(" consultorVendas, portariaCargo, cbo, progressaosalarial, legislacao ) ")
                			.append(" VALUES ( ?, ?, ?, ?, ?, ")
                			.append(" ?, ?, ?, ?, ? ) returning codigo");
                	
                    PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                    int i = 0;
                    Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getResponsabilidades(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getDepartamento(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getControlaNivelExperiencia(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getConsultorVendas(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getPortariaCargo(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getCbo(), ++i, sqlInserir);
//                    Uteis.setValuePreparedStatement(obj.getProgressaoSalarial(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getLegislacao(), ++i, sqlInserir);

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
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CargoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CargoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CargoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            CargoVO.validarDados(obj);
            alterar(getIdEntidade(), true, usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                	
                	final StringBuilder sql = new StringBuilder("UPDATE Cargo set nome=?, descricao=?, responsabilidades=?, departamento=?, controlaNivelExperiencia=?, ")
                			.append(" consultorVendas=?, portariaCargo=?, cbo=?, progressaosalarial=?, legislacao=? WHERE ((codigo = ?))");
                	
                    PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
                    int i = 0;
                    
                    Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getResponsabilidades(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getDepartamento(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getControlaNivelExperiencia(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getConsultorVendas(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getPortariaCargo(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCbo(), ++i, sqlAlterar);
//                    Uteis.setValuePreparedStatement(obj.getProgressaoSalarial(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getLegislacao(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                    
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CargoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CargoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CargoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Cargo WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
    	dataModelo.setListaConsulta(this.consultarPorNome(dataModelo, false, dataModelo.getUsuario()));
		dataModelo.setTotalRegistrosEncontrados(this.consultarTotalPorNome(dataModelo));
	}

	private List<CargoVO> consultarPorNome(DataModelo dataModelo, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM Cargo WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%" +dataModelo.getValorConsulta()+"%");
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    private Integer consultarTotalPorNome(DataModelo dataModelo) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append("SELECT count(codigo) as qtde FROM Cargo WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?))");

    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%" +dataModelo.getValorConsulta()+"%");
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getInt("qtde");
    	} else {
    		return 0;
    	}
    }

    /**
     * Responsável por realizar uma consulta de <code>Cargo</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CargoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CargoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE lower (sem_acentos(nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
    
    public List<CargoVO> consultarPorCbo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE lower (sem_acentos(cbo)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY cbo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List<CargoVO> consultarPorNomeDuploPercent(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE lower (nome) like('%" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List<CargoVO> consultarPorDepartamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE departamento = " + valorConsulta.intValue() + " ORDER BY departamento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
    
    public List<CargoVO> consultarPorDepartamentoENome(String nome, DepartamentoVO departamento, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE lower (nome) like('%" + nome.toLowerCase() + "%') AND departamento = " + departamento.getCodigo() + " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Cargo</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CargoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CargoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Cargo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CargoVO</code> resultantes da consulta.
     */
    public  List<CargoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<CargoVO> vetResultado = new ArrayList<>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CargoVO</code>.
     * @return  O objeto da classe <code>CargoVO</code> com os dados devidamente montados.
     */
    public  CargoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CargoVO obj = new CargoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setPortariaCargo(dadosSQL.getString("portariaCargo"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setResponsabilidades(dadosSQL.getString("responsabilidades"));
        obj.setControlaNivelExperiencia(dadosSQL.getBoolean("controlaNivelExperiencia"));
        obj.setConsultorVendas(dadosSQL.getBoolean("consultorVendas"));
        obj.setCbo(dadosSQL.getString("cbo"));
        obj.setLegislacao(dadosSQL.getString("legislacao"));

//        if (Uteis.isAtributoPreenchido(dadosSQL.getLong("progressaosalarial"))) {
//        	obj.setProgressaoSalarial(getFacadeFactory().getProgressaoSalarialInterfaceFacade().consultarPorChavePrimaria(dadosSQL.getLong("progressaosalarial")));
//        }

        montarDadosDepartamento(obj, nivelMontarDados,usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public  void montarDadosDepartamento(CargoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), true, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CargoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CargoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM Cargo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }
    
    
    /**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("select cargo.codigo, cargo.nome, cargo.portariaCargo, departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\", ")
		.append(" cargo.cbo, progressaosalarial from cargo  ");
		str.append("left join departamento on departamento.codigo = cargo.departamento ");
		return str;
	}

    /**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará 
	 * apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica)
	 * e apenas adicionar as cláusulas de condições e ordenação.
	 * @author Carlos
	 */
	public List<CargoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(cargo.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" ORDER BY cargo.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	
	public List<CargoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<CargoVO> vetResultado = new ArrayList<CargoVO>(0);
		while (tabelaResultado.next()) {
			CargoVO obj = new CargoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	
	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(CargoVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do Cargo
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setPortariaCargo(dadosSQL.getString("portariaCargo"));
		// Dados do Departamento
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		
	}


    /** Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Cargo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Cargo.idEntidade = idEntidade;
    }

    public List<CargoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
            throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
        }
        if (campoConsulta.equals("nome")) {
            return getFacadeFactory().getCargoFacade().consultarPorNome(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        return new ArrayList<>(0);
    }
    
    @Override
    public List<CargoVO> consultarCargoPorFuncionario(Integer codigoFuncionario, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuffer sql = getSQLPadraoConsultaBasica();
    	sql.append("WHERE EXISTS (SELECT FROM funcionariocargo f WHERE f.cargo = cargo.codigo AND f.funcionario = ?) ORDER BY cargo.nome");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionario);
    	return montarDadosConsultaRapida(tabelaResultado);
    }
}
