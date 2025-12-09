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

import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoAtualizacaoCadastralInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ConfiguracaoAtualizacaoCadastralVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ConfiguracaoAtualizacaoCadastral extends ControleAcesso implements ConfiguracaoAtualizacaoCadastralInterfaceFacade {

    protected static String idEntidade;

    public ConfiguracaoAtualizacaoCadastral() throws Exception {
        super();
        setIdEntidade("ConfiguracaoAtualizacaoCadastral");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
     */
    public ConfiguracaoAtualizacaoCadastralVO novo() throws Exception {
        ConfiguracaoAtualizacaoCadastralVO obj = new ConfiguracaoAtualizacaoCadastralVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            final String sql = "INSERT INTO ConfiguracaoAtualizacaoCadastral(configuracaoGeralSistema,  habilitarRecursoAtualizacaoCadastral,  apresentarCampoEndereco,  trazerDadoEnderecoEmBranco,  enderecoObrigatorio,  apresentarCampoContato,  trazerDadoContatoEmBranco," //7
            		+ " celularObrigatorio,  umTelefoneFixoObrigatorio,  emailObrigatorio,  apresentarCampoDataNascimento,  dataNascimentoObrigatorio,  apresentarCampoNaturalidadeNacionalidade,  naturalidadeNacionalidadeObrigatorio, " //7
            		+ " apresentarCampoCorRaca,  corRacaObrigatorio,  apresentarCampoEstadoCivil,  estadoCivilObrigatorio,  apresentarCampoRG,  rgObrigatorio,  apresentarCampoRegistroMilitar,  registroMilitarObrigatorio, " //8
            		+ " apresentarCampoDadoEleitoral,  dadoEleitoralObrigatorio,   apresentarCampoEnderecoFiliacao,  apresentarCampoCpfFiliacao,  apresentarCampoRgFiliacao,  apresentarCampoContatoFiliacao, " //6
            		+ " apresentarCampoDataNascFiliacao,  apresentarCampoNacionalidadeFiliacao,  apresentarCampoEstadoCivilFiliacao,  apresentarCampoDadoComercial,  apresentarCampoFormacaoAcademica,  apresentarCampoFormacaoExtraCurricular," //6
            		+ " apresentarCampoQualificacaoComplementar,  apresentarCampoAreaProfissional,  solicitarAtualizacaoACada,   apresentarCampoBancoCurriculo, apresentarCampoUploadCurriculo, permitirAtualizarCpf, apresentarCampoCpf , apresentarCampoFiliacao, apresentarCampoCertidaoNasc,  trazerDadoCertidaoNascEmBranco,  certidaoNascObrigatorio, permitirAlterarEndereco," //12
            		+ " editarcamporg, editarnaturalidadenacionalidade, editarcampodatanascimento, apresentarcampotranstornosneurodivergentes, permitiralterartranstornosneurodivergentes )"//5
            		+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    int i = 1;
                    sqlInserir.setInt(i++, obj.getConfiguracaoGeralSistema());
                    sqlInserir.setBoolean(i++, obj.getHabilitarRecursoAtualizacaoCadastral());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoEndereco());
                    sqlInserir.setBoolean(i++, obj.getTrazerDadoEnderecoEmBranco());
                    sqlInserir.setBoolean(i++, obj.getEnderecoObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoContato());
                    sqlInserir.setBoolean(i++, obj.getTrazerDadoContatoEmBranco());
                    sqlInserir.setBoolean(i++, obj.getCelularObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getUmTelefoneFixoObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getEmailObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoDataNascimento());
                    sqlInserir.setBoolean(i++, obj.getDataNascimentoObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoNaturalidadeNacionalidade());
                    sqlInserir.setBoolean(i++, obj.getNaturalidadeNacionalidadeObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoCorRaca());
                    sqlInserir.setBoolean(i++, obj.getCorRacaObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoEstadoCivil());
                    sqlInserir.setBoolean(i++, obj.getEstadoCivilObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoRG());
                    sqlInserir.setBoolean(i++, obj.getRgObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoRegistroMilitar());
                    sqlInserir.setBoolean(i++, obj.getRegistroMilitarObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoDadoEleitoral());
                    sqlInserir.setBoolean(i++, obj.getDadoEleitoralObrigatorio());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoEnderecoFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoCpfFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoRgFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoContatoFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoDataNascFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoNacionalidadeFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoEstadoCivilFiliacao());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoDadoComercial());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoFormacaoAcademica());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoFormacaoExtraCurricular());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoQualificacaoComplementar());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoAreaProfissional());
                    sqlInserir.setInt(i++, obj.getSolicitarAtualizacaoACada()); 
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoBancoCurriculo());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoUploadCurriculo());
                    sqlInserir.setBoolean(i++, obj.getPermitirAtualizarCpf());
                    sqlInserir.setBoolean(i++, obj.getApresentarCampoCpf());
					sqlInserir.setBoolean(i++, obj.getApresentarCampoFiliacao());
					sqlInserir.setBoolean(i++, obj.getApresentarCampoCertidaoNasc());                    
					sqlInserir.setBoolean(i++, obj.getTrazerDadoCertidaoNascEmBranco());                    
					sqlInserir.setBoolean(i++, obj.getCertidaoNascObrigatorio()); 
                    sqlInserir.setBoolean(i++, obj.getPermitirAlterarEndereco());
					sqlInserir.setBoolean(i++, obj.getEditarCampoRG());
					sqlInserir.setBoolean(i++, obj.getEditarNaturalidadeNacionalidade());
					sqlInserir.setBoolean(i++, obj.getEditarCampoDataNascimento());
					sqlInserir.setBoolean(i++, obj.getApresentarCampoTranstornosNeurodivergentes());
					sqlInserir.setBoolean(i++, obj.getPermitirAlterarTranstornosNeurodivergentes());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            final String sql = "UPDATE ConfiguracaoAtualizacaoCadastral set configuracaoGeralSistema=?,  habilitarRecursoAtualizacaoCadastral=?,  apresentarCampoEndereco=?,  trazerDadoEnderecoEmBranco=?,  enderecoObrigatorio=?,  apresentarCampoContato=?,  trazerDadoContatoEmBranco=?,"
            		+ " celularObrigatorio=?,  umTelefoneFixoObrigatorio=?,  emailObrigatorio=?,  apresentarCampoDataNascimento=?,  dataNascimentoObrigatorio=?,  apresentarCampoNaturalidadeNacionalidade=?,  naturalidadeNacionalidadeObrigatorio=?, "
            		+ " apresentarCampoCorRaca=?,  corRacaObrigatorio=?,  apresentarCampoEstadoCivil=?,  estadoCivilObrigatorio=?,  apresentarCampoRG=?,  rgObrigatorio=?,  apresentarCampoRegistroMilitar=?,  registroMilitarObrigatorio=?, "
            		+ " apresentarCampoDadoEleitoral=?,  dadoEleitoralObrigatorio=?,   apresentarCampoEnderecoFiliacao=?,  apresentarCampoCpfFiliacao=?,  apresentarCampoRgFiliacao=?,  apresentarCampoContatoFiliacao=?, "
            		+ " apresentarCampoDataNascFiliacao=?,  apresentarCampoNacionalidadeFiliacao=?,  apresentarCampoEstadoCivilFiliacao=?,  apresentarCampoDadoComercial=?,  apresentarCampoFormacaoAcademica=?, "
            		+ " apresentarCampoFormacaoExtraCurricular=?, apresentarCampoQualificacaoComplementar=?,  apresentarCampoAreaProfissional=?,  solicitarAtualizacaoACada=?, apresentarCampoBancoCurriculo=?, apresentarCampoUploadCurriculo=?, permitirAtualizarCpf=?, apresentarCampoCpf=?, apresentarCampoFiliacao=?, apresentarCampoCertidaoNasc=?,  trazerDadoCertidaoNascEmBranco=?,  certidaoNascObrigatorio=?,"
            		+ " permitirAlterarEndereco=?, editarcamporg=?, editarnaturalidadenacionalidade=?, editarcampodatanascimento=?, apresentarcampotranstornosneurodivergentes=?, permitiralterartranstornosneurodivergentes=? "
            		+ " WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    int i = 1;
                    sqlAlterar.setInt(i++, obj.getConfiguracaoGeralSistema());
                    sqlAlterar.setBoolean(i++, obj.getHabilitarRecursoAtualizacaoCadastral());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoEndereco());
                    sqlAlterar.setBoolean(i++, obj.getTrazerDadoEnderecoEmBranco());
                    sqlAlterar.setBoolean(i++, obj.getEnderecoObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoContato());
                    sqlAlterar.setBoolean(i++, obj.getTrazerDadoContatoEmBranco());
                    sqlAlterar.setBoolean(i++, obj.getCelularObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getUmTelefoneFixoObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getEmailObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoDataNascimento());
                    sqlAlterar.setBoolean(i++, obj.getDataNascimentoObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoNaturalidadeNacionalidade());
                    sqlAlterar.setBoolean(i++, obj.getNaturalidadeNacionalidadeObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoCorRaca());
                    sqlAlterar.setBoolean(i++, obj.getCorRacaObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoEstadoCivil());
                    sqlAlterar.setBoolean(i++, obj.getEstadoCivilObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoRG());
                    sqlAlterar.setBoolean(i++, obj.getRgObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoRegistroMilitar());
                    sqlAlterar.setBoolean(i++, obj.getRegistroMilitarObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoDadoEleitoral());
                    sqlAlterar.setBoolean(i++, obj.getDadoEleitoralObrigatorio());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoEnderecoFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoCpfFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoRgFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoContatoFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoDataNascFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoNacionalidadeFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoEstadoCivilFiliacao());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoDadoComercial());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoFormacaoAcademica());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoFormacaoExtraCurricular());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoQualificacaoComplementar());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoAreaProfissional());
                    sqlAlterar.setInt(i++, obj.getSolicitarAtualizacaoACada());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoBancoCurriculo());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoUploadCurriculo());
                    sqlAlterar.setBoolean(i++, obj.getPermitirAtualizarCpf());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoCpf());
					sqlAlterar.setBoolean(i++, obj.getApresentarCampoFiliacao());
					sqlAlterar.setBoolean(i++, obj.getApresentarCampoCertidaoNasc());
					sqlAlterar.setBoolean(i++, obj.getTrazerDadoCertidaoNascEmBranco());
					sqlAlterar.setBoolean(i++, obj.getCertidaoNascObrigatorio());
					sqlAlterar.setBoolean(i++, obj.getPermitirAlterarEndereco());
                    sqlAlterar.setBoolean(i++, obj.getEditarCampoRG());
                    sqlAlterar.setBoolean(i++, obj.getEditarNaturalidadeNacionalidade());
                    sqlAlterar.setBoolean(i++, obj.getEditarCampoDataNascimento());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCampoTranstornosNeurodivergentes());
                    sqlAlterar.setBoolean(i++, obj.getPermitirAlterarTranstornosNeurodivergentes());

					sqlAlterar.setInt(i++, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            String sql = "DELETE FROM ConfiguracaoAtualizacaoCadastral WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ConfiguracaoAtualizacaoCadastral</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ConfiguracaoAtualizacaoCadastral WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>.
     * @return  O objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code> com os dados devidamente montados.
     */
    public static ConfiguracaoAtualizacaoCadastralVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ConfiguracaoAtualizacaoCadastralVO obj = new ConfiguracaoAtualizacaoCadastralVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setConfiguracaoGeralSistema(new Integer(dadosSQL.getInt("configuracaoGeralSistema")));
        obj.setHabilitarRecursoAtualizacaoCadastral(dadosSQL.getBoolean("habilitarRecursoAtualizacaoCadastral"));
        obj.setApresentarCampoEndereco(dadosSQL.getBoolean("apresentarCampoEndereco"));
        obj.setTrazerDadoEnderecoEmBranco(dadosSQL.getBoolean("trazerDadoEnderecoEmBranco"));
        obj.setEnderecoObrigatorio(dadosSQL.getBoolean("enderecoObrigatorio"));
        obj.setApresentarCampoCertidaoNasc(dadosSQL.getBoolean("apresentarCampoCertidaoNasc"));
        obj.setTrazerDadoCertidaoNascEmBranco(dadosSQL.getBoolean("trazerDadoCertidaoNascEmBranco"));
        obj.setCertidaoNascObrigatorio(dadosSQL.getBoolean("certidaoNascObrigatorio"));
        obj.setApresentarCampoContato(dadosSQL.getBoolean("apresentarCampoContato"));
        obj.setTrazerDadoContatoEmBranco(dadosSQL.getBoolean("trazerDadoContatoEmBranco"));
        obj.setCelularObrigatorio(dadosSQL.getBoolean("celularObrigatorio"));
        obj.setUmTelefoneFixoObrigatorio(dadosSQL.getBoolean("umTelefoneFixoObrigatorio"));
        obj.setEmailObrigatorio(dadosSQL.getBoolean("emailObrigatorio"));
        obj.setApresentarCampoDataNascimento(dadosSQL.getBoolean("apresentarCampoDataNascimento"));
        obj.setEditarCampoDataNascimento(dadosSQL.getBoolean("editarCampoDataNascimento"));
        obj.setDataNascimentoObrigatorio(dadosSQL.getBoolean("dataNascimentoObrigatorio"));
        obj.setApresentarCampoNaturalidadeNacionalidade(dadosSQL.getBoolean("apresentarCampoNaturalidadeNacionalidade"));
        obj.setEditarNaturalidadeNacionalidade(dadosSQL.getBoolean("editarNaturalidadeNacionalidade"));
        obj.setNaturalidadeNacionalidadeObrigatorio(dadosSQL.getBoolean("naturalidadeNacionalidadeObrigatorio"));
        obj.setApresentarCampoCorRaca(dadosSQL.getBoolean("apresentarCampoCorRaca"));
        obj.setCorRacaObrigatorio(dadosSQL.getBoolean("corRacaObrigatorio"));
        obj.setApresentarCampoEstadoCivil(dadosSQL.getBoolean("apresentarCampoEstadoCivil"));
        obj.setEstadoCivilObrigatorio(dadosSQL.getBoolean("estadoCivilObrigatorio"));
        obj.setApresentarCampoRG(dadosSQL.getBoolean("apresentarCampoRG"));
        obj.setEditarCampoRG(dadosSQL.getBoolean("editarCampoRG"));
        obj.setRgObrigatorio(dadosSQL.getBoolean("rgObrigatorio"));
        obj.setApresentarCampoRegistroMilitar(dadosSQL.getBoolean("apresentarCampoRegistroMilitar"));
        obj.setRegistroMilitarObrigatorio(dadosSQL.getBoolean("registroMilitarObrigatorio"));
        obj.setApresentarCampoDadoEleitoral(dadosSQL.getBoolean("apresentarCampoDadoEleitoral"));
        obj.setDadoEleitoralObrigatorio(dadosSQL.getBoolean("dadoEleitoralObrigatorio"));
        obj.setApresentarCampoEnderecoFiliacao(dadosSQL.getBoolean("apresentarCampoEnderecoFiliacao"));
        obj.setApresentarCampoCpf(dadosSQL.getBoolean("apresentarCampoCpf"));
		obj.setApresentarCampoFiliacao(dadosSQL.getBoolean("apresentarCampoFiliacao"));        
        obj.setApresentarCampoCpfFiliacao(dadosSQL.getBoolean("apresentarCampoCpfFiliacao"));
        obj.setApresentarCampoRgFiliacao(dadosSQL.getBoolean("apresentarCampoRgFiliacao"));
        obj.setApresentarCampoContatoFiliacao(dadosSQL.getBoolean("apresentarCampoContatoFiliacao"));
        obj.setApresentarCampoDataNascFiliacao(dadosSQL.getBoolean("apresentarCampoDataNascFiliacao"));
        obj.setApresentarCampoNacionalidadeFiliacao(dadosSQL.getBoolean("apresentarCampoNacionalidadeFiliacao"));
        obj.setApresentarCampoEstadoCivilFiliacao(dadosSQL.getBoolean("apresentarCampoEstadoCivilFiliacao"));
        obj.setApresentarCampoDadoComercial(dadosSQL.getBoolean("apresentarCampoDadoComercial"));
        obj.setApresentarCampoFormacaoAcademica(dadosSQL.getBoolean("apresentarCampoFormacaoAcademica"));
        obj.setApresentarCampoFormacaoExtraCurricular(dadosSQL.getBoolean("apresentarCampoFormacaoExtraCurricular"));
        obj.setApresentarCampoQualificacaoComplementar(dadosSQL.getBoolean("apresentarCampoQualificacaoComplementar"));
        obj.setApresentarCampoAreaProfissional(dadosSQL.getBoolean("apresentarCampoAreaProfissional"));
        obj.setApresentarCampoBancoCurriculo(dadosSQL.getBoolean("apresentarCampoBancoCurriculo"));
        obj.setApresentarCampoUploadCurriculo(dadosSQL.getBoolean("apresentarCampoUploadCurriculo"));
        obj.setSolicitarAtualizacaoACada(dadosSQL.getInt("solicitarAtualizacaoACada"));
        obj.setPermitirAtualizarCpf(dadosSQL.getBoolean("permitirAtualizarCpf"));
        obj.setPermitirAlterarEndereco(dadosSQL.getBoolean("permitirAlterarEndereco"));
        obj.setApresentarCampoTranstornosNeurodivergentes(dadosSQL.getBoolean("apresentarcampotranstornosneurodivergentes"));
        obj.setPermitirAlterarTranstornosNeurodivergentes(dadosSQL.getBoolean("permitiralterartranstornosneurodivergentes"));
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ConfiguracaoAtualizacaoCadastralVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ConfiguracaoAtualizacaoCadastralVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ConfiguracaoAtualizacaoCadastral WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    public ConfiguracaoAtualizacaoCadastralVO consultarPorConfiguracaoGeralSistema(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sql = "SELECT * FROM ConfiguracaoAtualizacaoCadastral WHERE configuracaoGeralSistema = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
    	if (!tabelaResultado.next()) {
    		return new ConfiguracaoAtualizacaoCadastralVO();
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }
    
    
	
    /** Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ConfiguracaoAtualizacaoCadastral.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ConfiguracaoAtualizacaoCadastral.idEntidade = idEntidade;
    }

    public List<ConfiguracaoAtualizacaoCadastralVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
            throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
        }
//        if (campoConsulta.equals("nome")) {
//            return getFacadeFactory().getConfiguracaoAtualizacaoCadastralFacade().consultarPorNome(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
//        }
        return new ArrayList(0);
    }

    @Override
    public void aplicarRegraRenderizacaoCamposAtualizacaoCadastral(ConfiguracaoAtualizacaoCadastralVO configuracaoAtualizacaoCadastralVO) {
    	if(Uteis.isAtributoPreenchido(configuracaoAtualizacaoCadastralVO)) {
    	
    		if (!configuracaoAtualizacaoCadastralVO.getPermitirAlterarEndereco()) {
				configuracaoAtualizacaoCadastralVO.setTrazerDadoEnderecoEmBranco(false);
				configuracaoAtualizacaoCadastralVO.setEnderecoObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoEndereco()) {
				configuracaoAtualizacaoCadastralVO.setPermitirAlterarEndereco(false);
				configuracaoAtualizacaoCadastralVO.setTrazerDadoEnderecoEmBranco(false);
				configuracaoAtualizacaoCadastralVO.setEnderecoObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoFiliacao()) {
				configuracaoAtualizacaoCadastralVO.setApresentarCampoContatoFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoCpfFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoDataNascFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoEnderecoFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoEstadoCivilFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoNacionalidadeFiliacao(false);
				configuracaoAtualizacaoCadastralVO.setApresentarCampoRgFiliacao(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoRegistroMilitar()) {
				configuracaoAtualizacaoCadastralVO.setRegistroMilitarObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoRG()) {
				configuracaoAtualizacaoCadastralVO.setEditarCampoRG(false);
				configuracaoAtualizacaoCadastralVO.setRgObrigatorio(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getEditarCampoRG()) {
				configuracaoAtualizacaoCadastralVO.setRgObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoEstadoCivil()) {
				configuracaoAtualizacaoCadastralVO.setEstadoCivilObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoCorRaca()) {
				configuracaoAtualizacaoCadastralVO.setCorRacaObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoNaturalidadeNacionalidade()) {
				configuracaoAtualizacaoCadastralVO.setEditarNaturalidadeNacionalidade(false);
				configuracaoAtualizacaoCadastralVO.setNaturalidadeNacionalidadeObrigatorio(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getEditarNaturalidadeNacionalidade()) {
				configuracaoAtualizacaoCadastralVO.setNaturalidadeNacionalidadeObrigatorio(false);				
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoDataNascimento()) {
				configuracaoAtualizacaoCadastralVO.setEditarCampoDataNascimento(false);
				configuracaoAtualizacaoCadastralVO.setDataNascimentoObrigatorio(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getEditarCampoDataNascimento()) {
				configuracaoAtualizacaoCadastralVO.setDataNascimentoObrigatorio(false);
			}
			if (!configuracaoAtualizacaoCadastralVO.getApresentarCampoContato()) {
				configuracaoAtualizacaoCadastralVO.setUmTelefoneFixoObrigatorio(false);
				configuracaoAtualizacaoCadastralVO.setCelularObrigatorio(false);
				configuracaoAtualizacaoCadastralVO.setTrazerDadoContatoEmBranco(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getApresentarCampoDadoEleitoral()) {
				configuracaoAtualizacaoCadastralVO.setDadoEleitoralObrigatorio(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getApresentarCampoCpf()) {
				configuracaoAtualizacaoCadastralVO.setPermitirAtualizarCpf(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getApresentarCampoCertidaoNasc()) {
				configuracaoAtualizacaoCadastralVO.setCertidaoNascObrigatorio(false);
				configuracaoAtualizacaoCadastralVO.setTrazerDadoCertidaoNascEmBranco(false);
			}
			if(!configuracaoAtualizacaoCadastralVO.getApresentarCampoTranstornosNeurodivergentes()) {
				configuracaoAtualizacaoCadastralVO.setApresentarCampoTranstornosNeurodivergentes(false);
				configuracaoAtualizacaoCadastralVO.setPermitirAlterarTranstornosNeurodivergentes(false);
			}
    	}
    }
}
