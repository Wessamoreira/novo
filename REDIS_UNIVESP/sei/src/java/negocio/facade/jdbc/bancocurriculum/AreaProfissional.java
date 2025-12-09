package negocio.facade.jdbc.bancocurriculum;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.AreaProfissionalInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AreaProfissional extends ControleAcesso implements AreaProfissionalInterfaceFacade {

    private static String idEntidade;

    public AreaProfissional() throws Exception {
        super();
        setIdEntidade("AreaProfissional");
    }

    public static void validarDados(AreaProfissionalVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDescricaoAreaProfissional() == null || obj.getDescricaoAreaProfissional().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO deve ser informado.");
        }
    }

    public AreaProfissionalVO novo() throws Exception {
        AreaProfissional.incluir(getIdEntidade());
        AreaProfissionalVO obj = new AreaProfissionalVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AreaProfissionalVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO AreaProfissional( descricaoAreaProfissional, situacao) VALUES (?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricaoAreaProfissional());
                    sqlInserir.setString(2, obj.getSituacao());
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
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AreaProfissionalVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaProfissionalVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void inativar(final AreaProfissionalVO obj, UsuarioVO usuario) throws Exception {
        obj.setSituacao("IN");
        this.alterar(obj, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AreaProfissionalVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            AreaProfissional.alterar(getIdEntidade());
            final String sql = "UPDATE AreaProfissional set descricaoAreaProfissional=?, situacao = ? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricaoAreaProfissional());
                    sqlAlterar.setString(2, obj.getSituacao());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AreaProfissionalVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaProfissionalVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AreaProfissionalVO obj, UsuarioVO usuario) throws Exception {
        try {
            AreaProfissional.excluir(getIdEntidade());
            String sql = "DELETE FROM AreaProfissional WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>AreaProfissional</code> através do valor do atributo
     * <code>descricao</code> da classe <code>Bairro</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>AreaProfissionalVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoAreaProfissional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AreaProfissional.* FROM AreaProfissional WHERE upper(descricaoAreaProfissional) like('" + valorConsulta.toUpperCase() + "%')  ESCAPE '' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorDescricaoAreaProfissionalAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AreaProfissional.* FROM AreaProfissional WHERE upper(descricaoAreaProfissional) like('" + valorConsulta.toUpperCase() + "%') and situacao = 'AT' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorSituacaoAreaProfissional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AreaProfissional.* FROM AreaProfissional WHERE situacao ilike('" + valorConsulta + "') ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>AreaProfissional</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AreaProfissionalVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AreaProfissional WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>AreaProfissionalVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            AreaProfissionalVO obj = new AreaProfissionalVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>AreaProfissionalVO</code>.
     * @return  O objeto da classe <code>AreaProfissionalVO</code> com os dados devidamente montados.
     */
    public static AreaProfissionalVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        AreaProfissionalVO obj = new AreaProfissionalVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricaoAreaProfissional(dadosSQL.getString("descricaoAreaProfissional"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public AreaProfissionalVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM AreaProfissional WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( AreaProfissional ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public List<AreaProfissionalVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            if (campoConsulta.equals("codigo")) {
//            if (valorConsulta.trim().equals("")) {
//                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
//            }
                if (valorConsulta.equals("")) {
                    valorConsulta = "0";
                }
                int valorInt = Integer.parseInt(valorConsulta);
                return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            if (campoConsulta.equals("descricaoAreaProfissional")) {
//            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
//                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
//            }
                return consultarPorDescricaoAreaProfissional(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            if (campoConsulta.equals("situacao")) {
//            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
//                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
//            }
                return consultarPorSituacaoAreaProfissional(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            return new ArrayList(0);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public List<AreaProfissionalVO> consultarPorCodigoEmpresaNivelComboBox(Integer parceiro, Integer estagio, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select areaprofissional.codigo, areaprofissional.descricaoareaprofissional from areaprofissional ");
    	sb.append(" inner join areaprofissionalparceiro on areaprofissionalparceiro.areaprofissional = areaprofissional.codigo ");
    	sb.append(" where areaprofissionalparceiro.parceiro = ").append(parceiro);
    	
    	sb.append(" union ");
    	//ESSA CONSULTA IRÁ TRAZER A ÁREA PROFISSIONAL MESMO SE O USUÁRIO TENHA REMOVIDO A MESMA DO CADASTRO DO PARCEIRO
    	//AO EDITAR O ESTÁGIO
    	sb.append(" select areaprofissional.codigo, areaprofissional.descricaoareaprofissional from areaprofissional ");
    	sb.append(" inner join estagio on estagio.areaprofissional = areaprofissional.codigo ");
    	sb.append(" where estagio.codigo = ").append(estagio);
    	sb.append(" and areaprofissional.codigo not in(");
    	sb.append(" select areaprofissionalparceiro.areaprofissional from areaprofissionalparceiro ");
    	sb.append(" where areaprofissionalparceiro.parceiro = ").append(parceiro);
    	sb.append(") ");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<AreaProfissionalVO> listaAreaProfissionalVOs = new ArrayList<AreaProfissionalVO>(0);
    	while (tabelaResultado.next()) {
    		AreaProfissionalVO obj = new AreaProfissionalVO();
    		obj.setCodigo(tabelaResultado.getInt("codigo"));
    		obj.setDescricaoAreaProfissional(tabelaResultado.getString("descricaoareaprofissional"));
    		listaAreaProfissionalVOs.add(obj);
    	}
    	return listaAreaProfissionalVOs;
    }
}
