package negocio.facade.jdbc.administrativo;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoLdapInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ConfiguracaoLDAPVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ConfiguracaoLDAPVO</code>.
 * Encapsula toda a interação com o banco de dados.
 *
 * @see ConfiguracaoLdapVO
 * @see ControleAcesso
 * @see configuracaoGeralSistema
 */
@Lazy
@Repository
@Scope
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConfiguracaoLdap extends ControleAcesso implements ConfiguracaoLdapInterfaceFacade {

    private static final long serialVersionUID = -5943128458938271132L;

    protected static String idEntidade;

    public ConfiguracaoLdap() throws Exception {
        super();
        setIdEntidade("ConfiguracaoGeralSistema");
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ConfiguracaoLdap.idEntidade;
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ConfiguracaoLDAPVO</code>.
     */
    public ConfiguracaoLdapVO novo() throws Exception {
        ConfiguracaoLdap.incluir(getIdEntidade());
        return new ConfiguracaoLdapVO();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConfiguracaoLdapVO obj) throws Exception {
        this.validarDados(obj);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                final StringBuilder sql = new StringBuilder("INSERT INTO configuracaoldap ")
                        .append("(configuracaoGeralSistema, hostnameLdap, portaLdap, dnLdap, senhaLdap, dcLdap, formatoSenhaLdap, dominio, diretorio, grupo, prefixoSenha, urlLoginAD, urlLogoutAD, urlIdentificadorAD, diretorioinativacao) ")
                        .append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) ")
                        .append(" returning codigo ");

                int i = 0;
                PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                Uteis.setValuePreparedStatement(obj.getConfiguracaoGeralSistemaVO().getCodigo(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getHostnameLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getPortaLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDnLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getSenhaLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDcLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getFormatoSenhaLdap(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDominio(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getPrefixoSenha(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDiretorio(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getUrlLoginAD(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getUrlLogoutAD(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getUrlIdentificadorAD(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDiretorioInativacao(), ++i, sqlInserir);
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException {
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracaoLDAPVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj Objeto da classe <code>ConfiguracaoLDAPVO</code> que será alterada no banco de dados.
     * @throws Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConfiguracaoLdapVO obj) throws Exception {
        this.validarDados(obj);
        ConfiguracaoLdap.alterar(getIdEntidade());

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                StringBuilder sql = new StringBuilder("UPDATE configuracaoldap set ")
                        .append(" configuracaoGeralSistema = ?, hostnameLdap = ?, portaLdap = ?, dnLdap = ?, senhaLdap = ?, dcLdap = ?, formatoSenhaLdap = ?, dominio = ?, diretorio = ?, grupo = ?, prefixoSenha=?, urlLoginAD = ?, urlLogoutAD = ?, urlIdentificadorAD = ?, diretorioinativacao = ? ")
                        .append(" WHERE codigo = ? ");

                int i = 0;
                PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
                Uteis.setValuePreparedStatement(obj.getConfiguracaoGeralSistemaVO().getCodigo(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getHostnameLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getPortaLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDnLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getSenhaLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDcLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getFormatoSenhaLdap(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDominio(), ++i, sqlAlterar);                
                Uteis.setValuePreparedStatement(obj.getDiretorio(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getPrefixoSenha(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getUrlLoginAD(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getUrlLogoutAD(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getUrlIdentificadorAD(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDiretorioInativacao(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                return sqlAlterar;
            }
        });
    }

    @Override
    public void validarDados(ConfiguracaoLdapVO obj) throws ConsistirException {
        if (!obj.isValidarDados()) {
            return;
        }
        if (obj.getHostnameLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo HOSTNAME (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getPortaLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo PORTA (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getDnLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo DN (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getSenhaLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo SENHA (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getFormatoSenhaLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo FORMATO SENHA (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getDcLdap().trim().isEmpty()) {
            throw new ConsistirException("O campo DC (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getDiretorio().trim().isEmpty()) {
            throw new ConsistirException("O campo DIRETÓRIO (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getDominio().trim().isEmpty()) {
            throw new ConsistirException("O campo DOMÍNIO (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getUrlLoginAD().trim().isEmpty()) {
            throw new ConsistirException("O campo URL DE LOGON (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getUrlLogoutAD().trim().isEmpty()) {
            throw new ConsistirException("O campo URL DE LOGOUT (aba Integração LDAP) deve ser informado.");
        }
        if (obj.getUrlIdentificadorAD().trim().isEmpty()) {
            throw new ConsistirException("O campo ID DA ENTIDADE (aba Integração LDAP) deve ser informado.");
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoLDAPVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe <code>ConfiguracaoLDAPVO</code> que será removido no banco de dados.
     * @throws Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ConfiguracaoLdapVO obj) throws Exception {
        ConfiguracaoLdap.excluir(getIdEntidade());
        String sql = "DELETE FROM configuracaoldap WHERE codigo = ?";
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

    public List consultarPorConfiguracaoGeralSistema(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM configuracaoldap WHERE configuracaoGeralSistema = ? ORDER BY codigo desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ConfiguracaoLDAP</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ConfiguracaoLDAPVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM configuracaoldap WHERE codigo = ? ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>ConfiguracaoLDAPVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsavel por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ConfiguracaoLDAPVO</code>.
     *
     * @return O objeto da classe <code>ConfiguracaoLDAPVO</code> com os dados devidamente montados.
     */
    public static ConfiguracaoLdapVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ConfiguracaoLdapVO obj = new ConfiguracaoLdapVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setConfiguracaoGeralSistemaVO(new ConfiguracaoGeralSistemaVO());
        obj.getConfiguracaoGeralSistemaVO().setCodigo(dadosSQL.getInt("configuracaoGeralSistema"));
        obj.setHostnameLdap(dadosSQL.getString("hostnameLdap"));
        obj.setPortaLdap(dadosSQL.getString("portaLdap"));
        obj.setDnLdap(dadosSQL.getString("dnLdap"));
        obj.setSenhaLdap(dadosSQL.getString("senhaLdap"));
        obj.setDcLdap(dadosSQL.getString("dcLdap"));
        obj.setFormatoSenhaLdap(dadosSQL.getString("formatoSenhaLdap"));
        obj.setDominio(dadosSQL.getString("dominio"));
        obj.setGrupo(dadosSQL.getString("grupo"));
        obj.setPrefixoSenha(dadosSQL.getString("prefixoSenha"));
        obj.setDiretorio(dadosSQL.getString("diretorio"));
        obj.setUrlLoginAD(dadosSQL.getString("urlLoginAD"));
        obj.setUrlLogoutAD(dadosSQL.getString("urlLogoutAD"));
        obj.setUrlIdentificadorAD(dadosSQL.getString("urlIdentificadorAD"));
        obj.setDiretorioInativacao(dadosSQL.getString("diretorioinativacao"));
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ConfiguracaoLDAPVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ConfiguracaoLDAP</code>.
     *
     * @param <code>configuracaoGeralSistema</code> campo chave para exclusão dos objetos no BD.
     * @throws Exception Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirConfiguracaoLdap(Integer configuracaoGeralSistema) throws Exception {
        String sql = "DELETE FROM configuracaoldap WHERE configuracaoGeralSistema = ?";
        getConexao().getJdbcTemplate().update(sql, configuracaoGeralSistema);
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ConfiguracaoLDAPVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirConfiguracaoLDAPs</code> e <code>incluirConfiguracaoLDAPs</code> disponíveis na classe <code>ConfiguracaoLDAP</code>.
     *
     * @param objetos List com os objetos a serem alterados ou incluídos no BD.
     * @throws Exception Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarConfiguracaoLdaps(Integer configuracaoGeralSistema, List objetos) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM configuracaoldap WHERE configuracaoGeralSistema = ?");
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ConfiguracaoLdapVO objeto = (ConfiguracaoLdapVO) i.next();
            sql.append(" AND codigo <> ").append(objeto.getCodigo());
        }
        getConexao().getJdbcTemplate().update(sql.toString(), configuracaoGeralSistema);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ConfiguracaoLdapVO objeto = (ConfiguracaoLdapVO) e.next();
            if (objeto.getConfiguracaoGeralSistemaVO().getCodigo().equals(0)) {
                objeto.getConfiguracaoGeralSistemaVO().setCodigo(configuracaoGeralSistema);
            }
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ConfiguracaoLDAPVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>administrativo.configuracaoGeralSistema</code> através do atributo de vínculo.
     *
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @throws Exception Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirConfiguracaoLdaps(Integer configuracaoGeralSistemaPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ConfiguracaoLdapVO obj = (ConfiguracaoLdapVO) e.next();
            obj.getConfiguracaoGeralSistemaVO().setCodigo(configuracaoGeralSistemaPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ConfiguracaoLDAPVO</code>
     * através de sua chave primária.
     *
     * @throws Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ConfiguracaoLdapVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM configuracaoldap WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public ConfiguracaoLdapVO consultarPorCodigoDepartamento(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT configuracaoldap.* FROM configuracaoldap inner join departamento on departamento.configuracaoldap = configuracaoldap.codigo  WHERE departamento.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public ConfiguracaoLdapVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
    	String sql = "SELECT * FROM configuracaoldap WHERE codigo = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
    	if (!tabelaResultado.next()) {
    		return null;
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ConfiguracaoLdap.idEntidade = idEntidade;
    }

    @Override
    public List<ConfiguracaoLdapVO> consultarConfiguracaoLdaps() throws Exception {
        List<ConfiguracaoLdapVO> lista = new ArrayList<>();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(" SELECT * FROM ConfiguracaoLDAP");
        while (tabelaResultado.next()) {
            lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
        }
        return lista;
    }
    
    @Override
    public ConfiguracaoLdapVO consultarConfiguracaoLdapPorPessoa(Integer pessoa) throws Exception {
    	StringBuilder sql = new StringBuilder("");
    	sql.append(" select configuracaoldap.* from pessoaemailinstitucional "); 
    	sql.append(" inner join configuracaoldap on pessoaemailinstitucional.email ilike ('%@%') and configuracaoldap.dominio = substring(pessoaemailinstitucional.email, position('@' in pessoaemailinstitucional.email)) ");
    	sql.append(" where pessoa = ? and statusativoinativoenum = 'ATIVO'  ");
    	
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), pessoa);
    	if(rs.next()) {
    		return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
    	}
    	return new ConfiguracaoLdapVO();
    }
    
    
    @Override
    public ConfiguracaoLdapVO consultarConfiguracaoLdapPorCurso(Integer curso) throws Exception {
    	StringBuilder sql = new StringBuilder("");
    	sql.append(" select configuracaoldap.* from curso "); 
    	sql.append(" inner join configuracaoldap on configuracaoldap.codigo = curso.configuracaoldap ");
    	sql.append(" where curso.codigo = ?  ");
    	
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), curso);
    	if(rs.next()) {
    		return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
    	}
    	return new ConfiguracaoLdapVO();
    }
    
    @Override
    public ConfiguracaoLdapVO consultarConfiguracaoLdapPorPessoaEmailInstitucional(Integer pessoaEmailInstitucional) throws Exception {
    	StringBuilder sql = new StringBuilder("");
    	sql.append(" select configuracaoldap.* from pessoaemailinstitucional "); 
    	sql.append(" inner join configuracaoldap on pessoaemailinstitucional.email ilike ('%@%') and configuracaoldap.dominio = substring(pessoaemailinstitucional.email, position('@' in pessoaemailinstitucional.email)) ");
    	sql.append(" where pessoaemailinstitucional.codigo = ?   ");
    	
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), pessoaEmailInstitucional);
    	if(rs.next()) {
    		return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
    	}
    	return new ConfiguracaoLdapVO();
    }

}
