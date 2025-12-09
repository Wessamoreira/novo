package negocio.facade.jdbc.crm;

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
import negocio.comuns.crm.SituacaoProspectPipelineVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboSituacaoProspectPipelineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.SituacaoProspectPipelineInterfaceFacade;

/**
 *
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public class SituacaoProspectPipeline extends ControleAcesso implements SituacaoProspectPipelineInterfaceFacade {

    protected static String idEntidade;

    public SituacaoProspectPipeline() throws Exception {
        super();
        setIdEntidade("SituacaoProspectPipeline");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>SituacaoProspectPipelineVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            SituacaoProspectPipeline.incluir(getIdEntidade(), true, usuarioVO);
            realizarUpperCaseDados(obj);



            final String sql = "INSERT INTO SituacaoProspectPipeline( nome, apresentarPipeLine, controle, corFundo, corTexto ) VALUES ( ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setBoolean(2, obj.getApresentarPipeLine());
                    sqlInserir.setString(3, obj.getControle().toString());
                    sqlInserir.setString(4, obj.getCorFundo());
                    sqlInserir.setString(5, obj.getCorTexto());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>SituacaoProspectPipelineVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            SituacaoProspectPipeline.alterar(getIdEntidade(), true, usuarioVO);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE SituacaoProspectPipeline set nome=?, apresentarPipeLine=?, controle=?, corFundo=?, corTexto=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setBoolean(2, obj.getApresentarPipeLine());
                    sqlAlterar.setString(3, obj.getControle().toString());
                    sqlAlterar.setString(4, obj.getCorFundo());
                    sqlAlterar.setString(5, obj.getCorTexto());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            }));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>SituacaoProspectPipelineVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            SituacaoProspectPipeline.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM SituacaoProspectPipeline WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param SituacaoProspectPipelineVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioVO);
        } else {
            alterar(obj, usuarioVO);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(SituacaoProspectPipelineVO obj) throws ConsistirException, Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_SituacaoProspectPipeline_nome"));
		}
		// Para registros do tipo inicial e finalizado com sucesso este booleano
		// sempre deverá estar marcado.
//		if (obj.getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
//			obj.setApresentarPipeLine(Boolean.TRUE);
//		} else if (obj.getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
//			obj.setApresentarPipeLine(Boolean.TRUE);
//		}
		if (obj.getCorFundo() == null || obj.getCorFundo().trim().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_SituacaoProspectPipeline_corFundo"));
		}
		if (obj.getCorTexto() == null || obj.getCorTexto().trim().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_SituacaoProspectPipeline_corTexto"));
		}
	}

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     */
    public void validarUnicidade(List<SituacaoProspectPipelineVO> lista, SituacaoProspectPipelineVO obj) throws ConsistirException {
        for (SituacaoProspectPipelineVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(SituacaoProspectPipelineVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setNome(obj.getNome().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela SituacaoProspectPipelineCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<SituacaoProspectPipelineVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboSituacaoProspectPipelineEnum.CODIGO.toString())) {

            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals("nome")) {
            return consultarPorNome(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }        
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>SituacaoProspectPipeline</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectPipelineVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM SituacaoProspectPipeline WHERE upper( nome ) like(?) ORDER BY nome";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>SituacaoProspectPipeline</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectPipelineVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM SituacaoProspectPipeline WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectPipelineVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>SituacaoProspectPipelineVO</code>.
     * @return  O objeto da classe <code>SituacaoProspectPipelineVO</code> com os dados devidamente montados.
     */
    public static SituacaoProspectPipelineVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        SituacaoProspectPipelineVO obj = new SituacaoProspectPipelineVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setApresentarPipeLine(dadosSQL.getBoolean("apresentarPipeLine"));
        obj.setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("controle")));
        obj.setCorFundo(dadosSQL.getString("corFundo"));
        obj.setCorTexto(dadosSQL.getString("corTexto"));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>SituacaoProspectPipelineVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public SituacaoProspectPipelineVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM SituacaoProspectPipeline WHERE codigo = ?";
//        PreparedStatement sqlConsultar = con.prepareStatement(sql);
//        sqlConsultar.setInt(1, codigoPrm.intValue() );
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( SituacaoProspectPipeline ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public List consultarSituacaoProspectWorkflowsInicial(int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT spp.* FROM SituacaoProspectPipeline AS spp WHERE spp.controle = 'INICIAL'";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (resultado.next()) {
            if (resultado.isLast()) {
                SituacaoProspectWorkflowVO novoObj = new SituacaoProspectWorkflowVO();
                SituacaoProspectPipelineVO Obj = new SituacaoProspectPipelineVO();
                Obj = montarDados(resultado, nivelMontarDados);
                novoObj.setSituacaoProspectPipeline(Obj);
                objetos.add(novoObj);
            }
        }
        return consultarSituacaoProspectWorkflowsSucesso(objetos, nivelMontarDados, usuario);
    }

    public List consultarSituacaoProspectWorkflowsSucesso(List objetos, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.consultar(getIdEntidade());
        String sql = "SELECT spp.* FROM SituacaoProspectPipeline AS spp WHERE spp.controle = 'FINALIZADO_SUCESSO'";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (resultado.next()) {
            if (resultado.isLast()) {
                SituacaoProspectWorkflowVO novoObj = new SituacaoProspectWorkflowVO();
                SituacaoProspectPipelineVO Obj = new SituacaoProspectPipelineVO();
                Obj = montarDados(resultado, nivelMontarDados);
                novoObj.setSituacaoProspectPipeline(Obj);
                objetos.add(novoObj);
            }
        }
        return consultarSituacaoProspectWorkflowsInsucesso(objetos, nivelMontarDados, usuario);
    }

    public List consultarSituacaoProspectWorkflowsInsucesso(List objetos, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.consultar(getIdEntidade());
        String sql = "SELECT spp.*  FROM SituacaoProspectPipeline AS spp WHERE spp.controle = 'FINALIZADO_INSUCESSO'";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (resultado.next()) {
            if (resultado.isLast()) {
                SituacaoProspectWorkflowVO novoObj = new SituacaoProspectWorkflowVO();
                SituacaoProspectPipelineVO Obj = new SituacaoProspectPipelineVO();
                Obj = montarDados(resultado, nivelMontarDados);
                novoObj.setSituacaoProspectPipeline(Obj);
                objetos.add(novoObj);
            }
        }
        return objetos;
    }

    public List<SituacaoProspectPipelineVO> consultarUnicidade(SituacaoProspectPipelineVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return SituacaoProspectPipeline.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        SituacaoProspectPipeline.idEntidade = idEntidade;
    }
}
