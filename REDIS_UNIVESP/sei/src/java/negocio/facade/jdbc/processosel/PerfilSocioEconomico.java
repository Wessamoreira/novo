package negocio.facade.jdbc.processosel;

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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerfilSocioEconomicoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PerfilSocioEconomicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerfilSocioEconomicoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerfilSocioEconomicoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PerfilSocioEconomicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PerfilSocioEconomico extends ControleAcesso implements PerfilSocioEconomicoInterfaceFacade {

    protected static String idEntidade;

    public PerfilSocioEconomico() throws Exception {
        super();
        setIdEntidade("PerfilSocioEconomico");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerfilSocioEconomicoVO</code>.
     */
    public PerfilSocioEconomicoVO novo() throws Exception {
        PerfilSocioEconomico.incluir(getIdEntidade());
        PerfilSocioEconomicoVO obj = new PerfilSocioEconomicoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerfilSocioEconomicoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerfilSocioEconomicoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PerfilSocioEconomicoVO obj,UsuarioVO usuarioVO) throws Exception {
        try {
            PerfilSocioEconomicoVO.validarDados(obj);
            PerfilSocioEconomico.incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO PerfilSocioEconomico( pessoa, questionario ) VALUES ( ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getQuestionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getQuestionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
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
        //    getFacadeFactory().getQuestionarioAlunoFacade().incluirQuestionarioAluno(new PerfilSocioEconomicoVO().gerarListaQuestionarioAluno(obj));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PerfilSocioEconomicoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilSocioEconomicoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PerfilSocioEconomicoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            PerfilSocioEconomicoVO.validarDados(obj);
            PerfilSocioEconomico.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE PerfilSocioEconomico set pessoa=?, questionario=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getQuestionario().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getQuestionario().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        //    getFacadeFactory().getQuestionarioAlunoFacade().alterarQuestionarioAluno(obj.getCodigo(), new PerfilSocioEconomicoVO().gerarListaQuestionarioAluno(obj));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PerfilSocioEconomicoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilSocioEconomicoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PerfilSocioEconomicoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            PerfilSocioEconomico.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM PerfilSocioEconomico WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
         //   getFacadeFactory().getQuestionarioAlunoFacade().excluirQuestionarioAluno(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilSocioEconomico</code> através do valor do atributo
     * <code>descricao</code> da classe <code>Questionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PerfilSocioEconomicoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT PerfilSocioEconomico.* FROM PerfilSocioEconomico, Questionario WHERE PerfilSocioEconomico.questionario = Questionario.codigo and upper( Questionario.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Questionario.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilSocioEconomico</code> através do valor do atributo
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PerfilSocioEconomicoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT PerfilSocioEconomico.* FROM PerfilSocioEconomico, Pessoa WHERE PerfilSocioEconomico.pessoa = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilSocioEconomico</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerfilSocioEconomicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM PerfilSocioEconomico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuarioLogado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PerfilSocioEconomicoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuarioLogado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PerfilSocioEconomicoVO</code>.
     * @return  O objeto da classe <code>PerfilSocioEconomicoVO</code> com os dados devidamente montados.
     */
    public static PerfilSocioEconomicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        PerfilSocioEconomicoVO obj = new PerfilSocioEconomicoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getQuestionario().setCodigo(new Integer(dadosSQL.getInt("questionario")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosPessoa(obj, nivelMontarDados,usuarioLogado);
        montarDadosQuestionario(obj, nivelMontarDados,usuarioLogado);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>QuestionarioVO</code> relacionado ao objeto <code>PerfilSocioEconomicoVO</code>.
     * Faz uso da chave primária da classe <code>QuestionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosQuestionario(PerfilSocioEconomicoVO obj, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        if (obj.getQuestionario().getCodigo().intValue() == 0) {
            obj.setQuestionario(new QuestionarioVO());
            return;
        }
        obj.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionario().getCodigo(), nivelMontarDados,usuarioLogado));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PerfilSocioEconomicoVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPessoa(PerfilSocioEconomicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PerfilSocioEconomicoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PerfilSocioEconomicoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuarioLogado);
        String sql = "SELECT * FROM PerfilSocioEconomico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PerfilSocioEconomico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuarioLogado));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PerfilSocioEconomico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PerfilSocioEconomico.idEntidade = idEntidade;
    }

    public void gerarListasRespostaPergunta(PerfilSocioEconomicoVO perfilSocioEconomicoVO, UsuarioVO usuarioVO) throws Exception {
//		List QuestionarioAlunoVOs = new ArrayList(0);
//
//		Iterator i = perfilSocioEconomicoVO.getQuestionario().getPerguntaQuestionarioVOs().iterator();
//		while (i.hasNext()) {
//			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
//			QuestionarioAlunoVOs = getFacadeFactory().getQuestionarioAlunoFacade().consultarQuestionarioAluno(objExistente.getPergunta().getCodigo(), perfilSocioEconomicoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
//			Iterator j = QuestionarioAlunoVOs.iterator();
//			while (j.hasNext()) {
//				QuestionarioAlunoVO objExistenteQuestionarioAluno = (QuestionarioAlunoVO) j.next();
//				if (objExistenteQuestionarioAluno.getTipoResposta().equals("TE")) {
//					perfilSocioEconomicoVO.setTexto(objExistenteQuestionarioAluno.getTexto());
//				} else {
//					if (objExistenteQuestionarioAluno.getPerguntaQuestionario().getCodigo().equals(objExistente.pergunta.getCodigo())) {
//						Iterator k = objExistente.getPergunta().getRespostaPerguntaVOs().iterator();
//						while (k.hasNext()) {
//							RespostaPerguntaVO objExistenteResposta = (RespostaPerguntaVO) k.next();
//							if (objExistenteResposta.getCodigo().equals(objExistenteQuestionarioAluno.getRespostaQuestionarioAluno().getCodigo())) {
//								objExistenteResposta.setSelecionado(true);
//							}
//						}
//					}
//				}
//			}
//		}
	}
}
