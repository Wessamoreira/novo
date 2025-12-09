package negocio.facade.jdbc.avaliacaoinst;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see AvaliacaoInstitucionalPresencialRespostaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoInstitucionalPresencialResposta extends ControleAcesso implements AvaliacaoInstitucionalPresencialRespostaInterfaceFacade {

    protected static String idEntidade;

    public AvaliacaoInstitucionalPresencialResposta() throws Exception {
        super();
        setIdEntidade("AvaliacaoInstitucionalPresencialResposta");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     */
    public AvaliacaoInstitucionalPresencialRespostaVO novo() throws Exception {
        AvaliacaoInstitucionalPresencialResposta.incluir(getIdEntidade());
        AvaliacaoInstitucionalPresencialRespostaVO obj = new AvaliacaoInstitucionalPresencialRespostaVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AvaliacaoInstitucionalPresencialRespostaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDataCriacao() == null) {
            throw new ConsistirException("O campo DATA CRIAÇÃO (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getDataAlteracao() == null) {
            throw new ConsistirException("O campo DATA ALTERAÇÃO (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getAvaliacaoInstitucional().getCodigo().equals(0)) {
            throw new ConsistirException("O campo AVALIAÇÃO INSTITUCIONAL (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getUnidadeEnsino().getCodigo().equals(0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getCurso().getCodigo().equals(0)) {
            throw new ConsistirException("O campo CURSO (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getTurma().getCodigo().equals(0)) {
            throw new ConsistirException("O campo TURMA (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getDisciplina().getCodigo().equals(0)) {
            throw new ConsistirException("O campo DISCIPLINA (Avaliação Institucional Resposta) deve ser informado.");
        }
        if (obj.getProfessor().getCodigo().equals(0)) {
            throw new ConsistirException("O campo PROFESSOR (Avaliação Institucional Resposta) deve ser informado.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AvaliacaoInstitucionalPresencialRespostaVO obj,UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            if(getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().verificarAvaliacaoInstitucionalPresencialGravada(obj.getAvaliacaoInstitucional().getCodigo(), obj.getUnidadeEnsino().getCodigo(), obj.getCurso().getCodigo()
                    , obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getProfessor().getCodigo())){
                    throw new ConsistirException("Já existe um cadastro com os mesmos dados informados.");
            }
            AvaliacaoInstitucionalPresencialResposta.incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO AvaliacaoInstitucionalPresencialResposta( dataCriacao, dataAlteracao, avaliacaoInstitucional, unidadeEnsino, curso, turma, " // 1 - 6
                    + "disciplina, professor, responsavel ) " // 7 - 9
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"; // 9
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
                    sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
                    sqlInserir.setInt(3, obj.getAvaliacaoInstitucional().getCodigo().intValue());
                    sqlInserir.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlInserir.setInt(5, obj.getCurso().getCodigo().intValue());
                    sqlInserir.setInt(6, obj.getTurma().getCodigo().intValue());
                    sqlInserir.setInt(7, obj.getDisciplina().getCodigo().intValue());
                    sqlInserir.setInt(8, obj.getProfessor().getCodigo().intValue());
                    sqlInserir.setInt(9, obj.getResponsavel().getCodigo().intValue());

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
            persistirAvaliacaoInstitucionalPresencialItemResposta(obj);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            AvaliacaoInstitucionalPresencialResposta.alterar(getIdEntidade(),true , usuario);
            final String sql = "UPDATE AvaliacaoInstitucionalPresencialResposta set dataAlteracao=?, avaliacaoInstitucional=?, unidadeEnsino=?, curso=?, turma=?, " // 1 - 5
                    + "disciplina=?, professor=?, responsavel=? " // 6 - 8
                    + "WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
//                    sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
                    sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
                    sqlAlterar.setInt(2, obj.getAvaliacaoInstitucional().getCodigo().intValue());
                    sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlAlterar.setInt(4, obj.getCurso().getCodigo().intValue());
                    sqlAlterar.setInt(5, obj.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(6, obj.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setInt(7, obj.getProfessor().getCodigo().intValue());
                    sqlAlterar.setInt(8, obj.getResponsavel().getCodigo().intValue());
                    sqlAlterar.setInt(9, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            persistirAvaliacaoInstitucionalPresencialItemResposta(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirAvaliacaoInstitucionalPresencialItemResposta(AvaliacaoInstitucionalPresencialRespostaVO obj) throws Exception {
        for (PerguntaQuestionarioVO perguntaQuestionario : obj.getAvaliacaoInstitucional().getQuestionarioVO().getPerguntaQuestionarioVOs()) {
        	if (perguntaQuestionario.getPergunta().getTipoRespostaTextual()) {
        	
        		RespostaPerguntaVO respostaPergunta = new RespostaPerguntaVO();
        		respostaPergunta.setTexto(perguntaQuestionario.getPergunta().getRespostaTextual());
        		respostaPergunta.setPergunta(perguntaQuestionario.getPergunta().getCodigo());
        		
        		if (!respostaPergunta.getAvaliacaoInstitucionalPresencialItemResposta().equals(0)) {
    				getFacadeFactory().getAvaliacaoInstitucionalPresencialItemRespostaFacade().alterar(obj, respostaPergunta);
    			} else {
    				getFacadeFactory().getAvaliacaoInstitucionalPresencialItemRespostaFacade().incluir(obj, respostaPergunta);
    			}
        		
        	} else {
        		for (RespostaPerguntaVO respostaPergunta : perguntaQuestionario.getPergunta().getRespostaPerguntaVOs()) {
        			if (!respostaPergunta.getAvaliacaoInstitucionalPresencialItemResposta().equals(0)) {
        				getFacadeFactory().getAvaliacaoInstitucionalPresencialItemRespostaFacade().alterar(obj, respostaPergunta);
        			} else {
        				getFacadeFactory().getAvaliacaoInstitucionalPresencialItemRespostaFacade().incluir(obj, respostaPergunta);
        			}
        		}
        	}
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
        try {
			AvaliacaoInstitucionalPresencialResposta.excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getAvaliacaoInstitucionalPresencialItemRespostaFacade().excluirPorAvaliacaoInstitucionalPresencialResposta(obj);
            String sql = "DELETE FROM AvaliacaoInstitucionalPresencialResposta WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>Questionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AvaliacaoInstitucional.* FROM AvaliacaoInstitucional, Questionario WHERE AvaliacaoInstitucional.questionariounidadeensino = Questionario.codigo and upper( Questionario.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Questionario.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AvaliacaoInstitucional.* FROM AvaliacaoInstitucional, UnidadeEnsino WHERE AvaliacaoInstitucional.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>Date dataFinal</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinal(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE ((dataFinal >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinal <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>Date dataInicio</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicio(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicio";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorAvaliacaoAtiva(Date prmIni, Integer unidadeEnsino, String publicoAlvo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE ((dataInicio <= '" + Uteis.getDataJDBCTimestamp(prmIni) + " ') "
                + "and (dataFinal >= '" + Uteis.getDataJDBCTimestamp(prmIni) + "')) and";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " (unidadeEnsino is null or unidadeEnsino = " + unidadeEnsino.intValue() + ") and ";
        }
        sqlStr += "(upper (AvaliacaoInstitucional.publicoAlvo) = 'TO' or upper (AvaliacaoInstitucional.publicoAlvo) = '" + publicoAlvo.toUpperCase() + "') ";
        sqlStr += "ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>String publicoAlvo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorPublicoAlvo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE upper (AvaliacaoInstitucional.publicoAlvo )  = '" + valorConsulta.toUpperCase() + "' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * @return  O objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> com os dados devidamente montados.
     */
    public static AvaliacaoInstitucionalPresencialRespostaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        AvaliacaoInstitucionalPresencialRespostaVO obj = new AvaliacaoInstitucionalPresencialRespostaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDataCriacao(dadosSQL.getDate("data"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            return obj;
        }
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>QuestionarioVO</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>QuestionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
//    public static void montarDadosQuestionario(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        if (obj.getQuestionarioVO().getCodigo().intValue() == 0) {
//            obj.setQuestionarioVO(new QuestionarioVO());
//            return;
//        }
//        obj.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionarioVO().getCodigo(), nivelMontarDados, usuario));
//    }
    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>Curso</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>Turma</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>TurmaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurma(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>Turma</code> relacionado ao objeto <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Faz uso da chave primária da classe <code>TurmaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDisciplina(AvaliacaoInstitucionalPresencialRespostaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            obj.setDisciplina(new DisciplinaVO());
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public AvaliacaoInstitucionalPresencialRespostaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPresencialResposta WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( AvaliacaoInstitucionalPresencialResposta ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AvaliacaoInstitucionalPresencialResposta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        AvaliacaoInstitucionalPresencialResposta.idEntidade = idEntidade;
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorAvaliacaoInstitucional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(avaliacaoinstitucional.nome)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorUnidadeEnsinoDataSituacaoPublicoAlvo(Integer unidadeEnsino, Date data, String situacao, String publicoAlvo, Boolean avaliacaoPresencial, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataInicio <= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' AND avaliacaoinstitucional.dataFinal >= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
        }
        if (!situacao.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.situacao = '").append(situacao).append("'");
        }
        if (!publicoAlvo.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.publicoAlvo = '").append(publicoAlvo).append("'");
        }
        if (avaliacaoPresencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(avaliacaoPresencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorPublicoAlvo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(avaliacaoinstitucional.publicoAlvo)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.publicoAlvo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorCurso(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(curso.nome) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY curso.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorTurma(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(turma.identificadorTurma) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY turma.identificadorTurma");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDisciplina(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(disciplina.nome) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorProfessor(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(pessoa.nome) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataCriacao(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucionalPresencialResposta.dataCriacao between'");
        sqlStr.append(Uteis.getDataJDBC(dataInicio));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        sqlStr.append(" ORDER BY avaliacaoinstitucionalPresencialResposta.dataCriacao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataInicio(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataInicio between'");
        sqlStr.append(Uteis.getDataJDBC(dataInicio));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.dataInicio");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorDataFinal(Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataFinal between'");
        sqlStr.append(Uteis.getDataJDBC(dataInicio));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.dataFinal");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorUnidadeEnsino(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(unidadeEnsino.nome) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY unidadeEnsino.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorQuestionario(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(questionario.descricao) like('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY questionario.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> consultaRapidaPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(avaliacaoinstitucional.situacao) like('");
        sqlStr.append(situacao.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.situacao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public Boolean verificarAvaliacaoInstitucionalPresencialGravada(Integer avaliacaoInstitucional, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Integer professor) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM AvaliacaoInstitucionalPresencialResposta ");
        sqlStr.append("WHERE avaliacaoInstitucional = ? AND unidadeEnsino = ? AND curso = ? AND turma = ? AND disciplina = ? AND professor = ? ");
        sqlStr.append("LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{avaliacaoInstitucional,unidadeEnsino, curso, turma, disciplina, professor});
        if (!tabelaResultado.next()) {
            return false;
        }
        return true;
    }

    public List<AvaliacaoInstitucionalPresencialRespostaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<AvaliacaoInstitucionalPresencialRespostaVO> vetResultado = new ArrayList<AvaliacaoInstitucionalPresencialRespostaVO>(0);
        while (tabelaResultado.next()) {
            AvaliacaoInstitucionalPresencialRespostaVO obj = new AvaliacaoInstitucionalPresencialRespostaVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public void carregarDados(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((AvaliacaoInstitucionalPresencialRespostaVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Alberto
     */
    public void carregarDados(AvaliacaoInstitucionalPresencialRespostaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((AvaliacaoInstitucionalPresencialRespostaVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((AvaliacaoInstitucionalPresencialRespostaVO) obj, resultado);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codAvaliacaoinstitucionalpresencialresposta, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (avaliacaoinstitucionalpresencialresposta.codigo= ").append(codAvaliacaoinstitucionalpresencialresposta).append(")");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codAvaliacaoinstitucionalpresencialresposta, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (avaliacaoinstitucionalpresencialresposta.codigo = ").append(codAvaliacaoinstitucionalpresencialresposta).append(")");
        sqlStr.append(" AND (respostapergunta.codigo = avaliacaoinstitucionalpresencialitemresposta.respostapergunta AND pergunta.tiporesposta != 'TE' OR ");
        sqlStr.append(" pergunta.tiporesposta = 'TE' AND avaliacaoInstitucionalPresencialItemResposta.texto != '') ");
        sqlStr.append(" ORDER BY avaliacaoinstitucionalpresencialresposta.codigo, pergunta.codigo, respostapergunta.codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append(" SELECT DISTINCT avaliacaoinstitucionalpresencialresposta.codigo as \"avaliacaoinstitucionalpresencialresposta.codigo\", ");
        str.append(" avaliacaoinstitucionalpresencialresposta.dataCriacao as \"avaliacaoinstitucionalpresencialresposta.dataCriacao\", ");
        str.append(" avaliacaoinstitucionalpresencialresposta.dataAlteracao as \"avaliacaoinstitucionalpresencialresposta.dataAlteracao\", ");
        str.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", curso.codigo as \"curso.codigo\",  ");
        str.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
        str.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
        str.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", avaliacaoinstitucional.codigo as \"avaliacaoinstitucional.codigo\", ");
        str.append(" avaliacaoinstitucional.nome as \"avaliacaoinstitucional.nome\", avaliacaoinstitucional.situacao as \"avaliacaoinstitucional.situacao\", ");
        str.append(" avaliacaoinstitucional.publicoAlvo as \"avaliacaoinstitucional.publicoAlvo\", questionario.codigo as \"questionario.codigo\", questionario.descricao as \"questionario.descricao\", ");
        str.append(" pergunta.codigo as \"pergunta.codigo\", pergunta.descricao as \"pergunta.descricao\", pergunta.tipoResposta as \"pergunta.tipoResposta\", respostapergunta.codigo as \"respostapergunta.codigo\", ");
        str.append(" respostapergunta.descricao as \"respostapergunta.descricao\", ");
        str.append(" avaliacaoinstitucionalpresencialitemresposta.codigo as \"avaliacaoinstitucionalpresencialitemresposta.codigo\", ");
        str.append(" avaliacaoinstitucionalpresencialitemresposta.qtderespostas as \"avaliacaoinstitucionalpresencialitemresposta.qtderespostas\", ");
        str.append(" avaliacaoInstitucionalPresencialItemResposta.texto as \"avaliacaoInstitucionalPresencialItemResposta.texto\" ");
        str.append(" FROM avaliacaoinstitucionalpresencialresposta ");
        str.append(" INNER JOIN avaliacaoinstitucionalpresencialitemresposta ON avaliacaoinstitucionalpresencialresposta.codigo = avaliacaoinstitucionalpresencialitemresposta.avaliacaoinstitucionalpresencialresposta ");
        str.append(" INNER JOIN avaliacaoinstitucional ON avaliacaoinstitucional.codigo = avaliacaoinstitucionalpresencialresposta.avaliacaoinstitucional ");
        str.append(" INNER JOIN questionario ON questionario.codigo = avaliacaoinstitucional.questionario ");
        str.append(" INNER JOIN perguntaquestionario ON perguntaquestionario.questionario = questionario.codigo ");
        str.append(" INNER JOIN pergunta ON pergunta.codigo = perguntaquestionario.pergunta ");
        str.append(" LEFT JOIN respostapergunta ON respostapergunta.pergunta = pergunta.codigo ");
        str.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = avaliacaoinstitucionalpresencialresposta.unidadeensino ");
        str.append(" INNER JOIN curso ON curso.codigo = avaliacaoinstitucionalpresencialresposta.curso ");
        str.append(" INNER JOIN turma ON turma.codigo = avaliacaoinstitucionalpresencialresposta.turma ");
        str.append(" INNER JOIN disciplina ON disciplina.codigo = avaliacaoinstitucionalpresencialresposta.disciplina ");
        str.append(" INNER JOIN pessoa ON pessoa.codigo = avaliacaoinstitucionalpresencialresposta.professor ");
        str.append(" INNER JOIN usuario ON usuario.codigo = avaliacaoinstitucionalpresencialresposta.responsavel ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT DISTINCT avaliacaoinstitucionalpresencialresposta.codigo as \"avaliacaoinstitucionalpresencialresposta.codigo\", ");
        str.append("avaliacaoinstitucionalpresencialresposta.dataCriacao as \"avaliacaoinstitucionalpresencialresposta.dataCriacao\", ");
        str.append("avaliacaoinstitucionalpresencialresposta.dataAlteracao as \"avaliacaoinstitucionalpresencialresposta.dataAlteracao\", ");
        str.append("unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
        str.append("curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
        str.append("disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
        str.append("usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", avaliacaoinstitucional.codigo as \"avaliacaoinstitucional.codigo\", ");
        str.append("avaliacaoinstitucional.nome as \"avaliacaoinstitucional.nome\", avaliacaoinstitucional.situacao as \"avaliacaoinstitucional.situacao\", ");
        str.append("avaliacaoinstitucional.publicoAlvo as \"avaliacaoinstitucional.publicoAlvo\", questionario.codigo as \"questionario.codigo\", ");
        str.append("questionario.descricao as \"questionario.descricao\" ");
        str.append("FROM avaliacaoinstitucionalpresencialresposta ");
        str.append("INNER JOIN avaliacaoinstitucional ON avaliacaoinstitucional.codigo = avaliacaoinstitucionalpresencialresposta.avaliacaoinstitucional ");
        str.append("INNER JOIN questionario ON questionario.codigo = avaliacaoinstitucional.questionario ");
        str.append("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = avaliacaoinstitucionalpresencialresposta.unidadeensino ");
        str.append("INNER JOIN curso ON curso.codigo = avaliacaoinstitucionalpresencialresposta.curso ");
        str.append("INNER JOIN turma ON turma.codigo = avaliacaoinstitucionalpresencialresposta.turma ");
        str.append("INNER JOIN disciplina ON disciplina.codigo = avaliacaoinstitucionalpresencialresposta.disciplina ");
        str.append("INNER JOIN pessoa ON pessoa.codigo = avaliacaoinstitucionalpresencialresposta.professor ");
        str.append("INNER JOIN usuario ON usuario.codigo = avaliacaoinstitucionalpresencialresposta.responsavel ");
        return str;
    }

    private void montarDadosCompleto(AvaliacaoInstitucionalPresencialRespostaVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da AvaliacaoInstitucionalPresencialResposta
        obj.setCodigo(dadosSQL.getInt("avaliacaoInstitucionalPresencialResposta.codigo"));
        obj.setDataCriacao(dadosSQL.getTimestamp("avaliacaoInstitucionalPresencialResposta.dataCriacao"));
        obj.setDataAlteracao(dadosSQL.getTimestamp("avaliacaoInstitucionalPresencialResposta.dataAlteracao"));
        obj.setNivelMontarDados(NivelMontarDados.TODOS);

        //Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
        obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados do Curso
        obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados da Turma
        obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
        obj.getTurma().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados da Disciplina
        obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
        obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
        obj.getDisciplina().setNivelMontarDados(NivelMontarDados.BASICO);
        ;

        //Dados do Professor
        obj.getProfessor().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getProfessor().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getProfessor().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados do Responsável
        obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
        obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);

        // Dados da Avaliação
        obj.getAvaliacaoInstitucional().setCodigo(dadosSQL.getInt("avaliacaoinstitucional.codigo"));
        obj.getAvaliacaoInstitucional().setNome(dadosSQL.getString("avaliacaoinstitucional.nome"));
        obj.getAvaliacaoInstitucional().setSituacao(dadosSQL.getString("avaliacaoinstitucional.situacao"));
        obj.getAvaliacaoInstitucional().setPublicoAlvo(dadosSQL.getString("avaliacaoinstitucional.publicoAlvo"));
        obj.getAvaliacaoInstitucional().setNivelMontarDados(NivelMontarDados.BASICO);

        // Dados do Questionário
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario.codigo"));
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setDescricao(dadosSQL.getString("questionario.descricao"));
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setNivelMontarDados(NivelMontarDados.BASICO);

        PerguntaQuestionarioVO perguntaQuestionarioVO = null;
        Integer avaliacaoInstitucionalPresencialResposta = 0;
        RespostaPerguntaVO respostaPerguntaVO = null;
        Integer pergunta = 0;
        do {
            if ((avaliacaoInstitucionalPresencialResposta != null) && (!avaliacaoInstitucionalPresencialResposta.equals(0)) && (!avaliacaoInstitucionalPresencialResposta.equals(dadosSQL.getInt("avaliacaoInstitucionalPresencialResposta.codigo")))) {
                dadosSQL.previous();
                break;
            }
            perguntaQuestionarioVO = new PerguntaQuestionarioVO();
            perguntaQuestionarioVO.getPergunta().setCodigo(dadosSQL.getInt("pergunta.codigo"));
            perguntaQuestionarioVO.getPergunta().setDescricao(dadosSQL.getString("pergunta.descricao"));
            perguntaQuestionarioVO.getPergunta().setTipoResposta(dadosSQL.getString("pergunta.tipoResposta"));
            
            if(perguntaQuestionarioVO.getPergunta().getTipoResposta().equals("TE")) {
            	perguntaQuestionarioVO.getPergunta().setApresentarRespostaTextual(true);
            	perguntaQuestionarioVO.getPergunta().setRespostaTextual(dadosSQL.getString("avaliacaoInstitucionalPresencialItemResposta.texto"));
            }
            
            perguntaQuestionarioVO.setQuestionario(obj.getAvaliacaoInstitucional().getQuestionarioVO().getCodigo());
            avaliacaoInstitucionalPresencialResposta = dadosSQL.getInt("avaliacaoInstitucionalPresencialResposta.codigo");
            do {
                if ((pergunta != null) && (!pergunta.equals(0)) && (!pergunta.equals(dadosSQL.getInt("pergunta.codigo")))) {
                    dadosSQL.previous();
                    break;
                }
                respostaPerguntaVO = new RespostaPerguntaVO();
                respostaPerguntaVO.setCodigo(dadosSQL.getInt("respostapergunta.codigo"));
                respostaPerguntaVO.setDescricao(dadosSQL.getString("respostapergunta.descricao"));
                respostaPerguntaVO.setAvaliacaoInstitucionalPresencialItemResposta(dadosSQL.getInt("avaliacaoinstitucionalpresencialitemresposta.codigo"));
                respostaPerguntaVO.setQtdeRespostas(dadosSQL.getInt("avaliacaoinstitucionalpresencialitemresposta.qtderespostas"));
                respostaPerguntaVO.setPergunta(perguntaQuestionarioVO.getPergunta().getCodigo());
                pergunta = dadosSQL.getInt("pergunta.codigo");
                perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs().add(respostaPerguntaVO);
                if (dadosSQL.isLast()) {
                    perguntaQuestionarioVO.getPergunta().calcularMediaAvaliacaoInstitucionalResposta();
                    obj.getAvaliacaoInstitucional().getQuestionarioVO().getPerguntaQuestionarioVOs().add(perguntaQuestionarioVO);
                    return;
                }
            } while (dadosSQL.next());
            pergunta = 0;
            perguntaQuestionarioVO.getPergunta().calcularMediaAvaliacaoInstitucionalResposta();
            obj.getAvaliacaoInstitucional().getQuestionarioVO().getPerguntaQuestionarioVOs().add(perguntaQuestionarioVO);
            if (dadosSQL.isLast()) {
                return;
            }
        } while (dadosSQL.next());
    }

    private void montarDadosBasico(AvaliacaoInstitucionalPresencialRespostaVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da AvaliacaoInstitucionalPresencialResposta
        obj.setCodigo(dadosSQL.getInt("avaliacaoInstitucionalPresencialResposta.codigo"));
        obj.setDataCriacao(dadosSQL.getTimestamp("avaliacaoInstitucionalPresencialResposta.dataCriacao"));
        obj.setDataAlteracao(dadosSQL.getTimestamp("avaliacaoInstitucionalPresencialResposta.dataAlteracao"));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
        obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados do Curso
        obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
        obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados da Turma
        obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
        obj.getTurma().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados da Disciplina
        obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
        obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
        obj.getDisciplina().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados do Professor
        obj.getProfessor().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getProfessor().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getProfessor().setNivelMontarDados(NivelMontarDados.BASICO);

        //Dados do Responsável
        obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
        obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);

        // Dados da Avaliação
        obj.getAvaliacaoInstitucional().setCodigo(dadosSQL.getInt("avaliacaoinstitucional.codigo"));
        obj.getAvaliacaoInstitucional().setNome(dadosSQL.getString("avaliacaoinstitucional.nome"));
        obj.getAvaliacaoInstitucional().setSituacao(dadosSQL.getString("avaliacaoinstitucional.situacao"));
        obj.getAvaliacaoInstitucional().setPublicoAlvo(dadosSQL.getString("avaliacaoinstitucional.publicoAlvo"));
        obj.getAvaliacaoInstitucional().setNivelMontarDados(NivelMontarDados.BASICO);

        // Dados do Questionário
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario.codigo"));
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setDescricao(dadosSQL.getString("questionario.descricao"));
        obj.getAvaliacaoInstitucional().getQuestionarioVO().setNivelMontarDados(NivelMontarDados.BASICO);
    }
}
