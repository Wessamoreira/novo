/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoVisaoAgendaEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboAgendaPessoaHorarioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.AgendaPessoaHorarioInterfaceFacade;

/**
 *
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public class AgendaPessoaHorario extends ControleAcesso implements AgendaPessoaHorarioInterfaceFacade {

    protected static String idEntidade;

    public AgendaPessoaHorario() throws Exception {
        super();
        setIdEntidade("AgendaPessoa");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>AgendaPessoaHorarioVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AgendaPessoaHorarioVO obj, UsuarioVO usuarioLogado) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO AgendaPessoaHorario( agendaPessoa, dia, mes, ano, diaSemanaEnum ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getAgendaPessoa().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getAgendaPessoa().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setInt(2, obj.getDia().intValue());
                sqlInserir.setInt(3, obj.getMes().intValue());
                sqlInserir.setInt(4, obj.getAno().intValue());
                sqlInserir.setString(5, obj.getDiaSemanaEnum().name());
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>. Sempre utiliza a chave primária da
     * classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>alterar</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>AgendaPessoaHorarioVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AgendaPessoaHorarioVO obj, UsuarioVO usuarioLogado) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE AgendaPessoaHorario set agendaPessoa=?, dia=?, mes=?, ano=?, diaSemanaEnum=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getAgendaPessoa().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getAgendaPessoa().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, obj.getDia().intValue());
                sqlAlterar.setInt(3, obj.getMes().intValue());
                sqlAlterar.setInt(4, obj.getAno().intValue());
                sqlAlterar.setString(5, obj.getDiaSemanaEnum().name());
                sqlAlterar.setInt(6, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        }));
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>AgendaPessoaHorarioVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public void excluir(AgendaPessoaHorarioVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            AgendaPessoaHorario.excluir(getIdEntidade());
            String sql = "DELETE FROM AgendaPessoaHorario WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getEtapaWorkflowFacade().excluirEtapaWorkflows(obj.getCodigo(), usuarioLogado);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption Se uma inconsistência for encontrada
     * aumaticamente é gerada uma exceção descrevendo o atributo e o erro
     * ocorrido.
     */
    public void validarDados(AgendaPessoaHorarioVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ConsistirException consistir = new ConsistirException();
        if (obj.getDia().intValue() == 0) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_AgendaPessoaHorario_dia"));
        }
        if (obj.getMes().intValue() == 0) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_AgendaPessoaHorario_mes"));
        }
        if (obj.getAno().intValue() == 0) {
            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_AgendaPessoaHorario_ano"));
        }
        if (consistir.existeErroListaMensagemErro()) {
            throw consistir;
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe
     * <code>AgendaPessoaHorarioVO</code>.
     */
    public void validarUnicidade(List<AgendaPessoaHorarioVO> lista, AgendaPessoaHorarioVO obj) throws ConsistirException {
        for (AgendaPessoaHorarioVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados(AgendaPessoaHorarioVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela
     * AgendaPessoaHorarioCons.jsp. Define o tipo de consulta a ser executada,
     * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
     * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
     * sessao da pagina.
     */
    public List<AgendaPessoaHorarioVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaHorarioEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaHorarioEnum.CODIGO_AGENDA_PESSOA.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigoAgendaPessoa(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaHorarioEnum.DIA.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorDia(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaHorarioEnum.MES.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorMes(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaHorarioEnum.ANO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorAno(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>AgendaPessoaHorario</code> através do valor do atributo
     * <code>Integer ano</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM AgendaPessoaHorario WHERE ano >= ?  ORDER BY ano";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>AgendaPessoaHorario</code> através do valor do atributo
     * <code>Integer mes</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM AgendaPessoaHorario WHERE mes >= ?  ORDER BY mes";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>AgendaPessoaHorario</code> através do valor do atributo
     * <code>Integer dia</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorDia(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM AgendaPessoaHorario WHERE dia >= ?  ORDER BY dia";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>AgendaPessoaHorario</code> através do valor do atributo
     * <code>codigo</code> da classe
     * <code>AgendaPessoa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorCodigoAgendaPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT AgendaPessoaHorario.* FROM AgendaPessoaHorario, AgendaPessoa WHERE AgendaPessoaHorario.agendaPessoa = AgendaPessoa.codigo and AgendaPessoa.codigo >= ? ORDER BY AgendaPessoa.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>AgendaPessoaHorario</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM AgendaPessoaHorario WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
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
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>.
     *
     * @return O objeto da classe
     * <code>AgendaPessoaHorarioVO</code> com os dados devidamente montados.
     */
    public static AgendaPessoaHorarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AgendaPessoaHorarioVO obj = new AgendaPessoaHorarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getAgendaPessoa().setCodigo(new Integer(dadosSQL.getInt("agendaPessoa")));
        obj.setDia(new Integer(dadosSQL.getInt("dia")));
        obj.setMes(new Integer(dadosSQL.getInt("mes")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.setDiaSemanaEnum(DiaSemana.valueOf(dadosSQL.getString("diaSemanaEnum")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>.
     *
     * @return O objeto da classe
     * <code>AgendaPessoaHorarioVO</code> com os dados devidamente montados.
     */
    public static AgendaPessoaHorarioVO montarDadosPersonalizado(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AgendaPessoaHorarioVO obj = new AgendaPessoaHorarioVO();
        while (dadosSQL.next()) {
            if (obj.getCodigo() == null || obj.getCodigo().equals(0)) {
                obj.setCodigo(new Integer(dadosSQL.getInt("aph_codigo")));
                obj.getAgendaPessoa().setCodigo(new Integer(dadosSQL.getInt("aph_agendapessoa")));
                obj.getAgendaPessoa().getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
                obj.getAgendaPessoa().getPessoa().setCPF(dadosSQL.getString("pessoa_cpf"));
                obj.getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));
                obj.setDia(new Integer(dadosSQL.getInt("aph_dia")));
                obj.setMes(new Integer(dadosSQL.getInt("aph_mes")));
                obj.setAno(new Integer(dadosSQL.getInt("aph_ano")));
                obj.setDiaSemanaEnum(DiaSemana.valueOf(dadosSQL.getString("aph_diaSemanaEnum")));
                obj.setNovoObj(new Boolean(false));
                if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
                    return obj;
                }
            }
            if (dadosSQL.getInt("cap_codigo") != 0) {
                CompromissoAgendaPessoaHorarioVO compromisso = new CompromissoAgendaPessoaHorarioVO();
                compromisso.setCodigo(new Integer(dadosSQL.getInt("cap_codigo")));
                compromisso.setPossuiMatricula(dadosSQL.getBoolean("possuiMatricula"));
                compromisso.setUltimaInteracao(dadosSQL.getString("ultimaInteracao"));				
                compromisso.setDescricao(dadosSQL.getString("cap_descricao"));
                compromisso.getAgendaPessoaHorario().setCodigo(new Integer(dadosSQL.getInt("aph_codigo")));
                compromisso.setHora(dadosSQL.getString("cap_hora"));
                compromisso.setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipocompromisso")));
                compromisso.setObservacao(dadosSQL.getString("cap_observacao"));
                compromisso.setOrigem(dadosSQL.getString("cap_origem"));
                compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
                compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));

                if (dadosSQL.getString("cap_tipoSituacaoCompromissoEnum") != null && !dadosSQL.getString("cap_tipoSituacaoCompromissoEnum").isEmpty()) {
                    compromisso.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipoSituacaoCompromissoEnum")));
                }
                compromisso.setUrgente(new Boolean(dadosSQL.getBoolean("cap_urgente")));
                compromisso.setDataCadastro(dadosSQL.getDate("cap_datacadastro"));
                compromisso.setDataCompromisso(dadosSQL.getDate("cap_datacompromisso"));
    			compromisso.setDataCompromissoAnterior(dadosSQL.getDate("cap_dataCompromisso"));
    			compromisso.setDataInicialCompromisso(dadosSQL.getDate("cap_dataInicialCompromisso"));
    			compromisso.setHistoricoReagendamentoCompromisso(dadosSQL.getString("cap_historicoReagendamentoCompromisso"));
                compromisso.setTipoContato(TipoContatoEnum.valueOf(dadosSQL.getString("cap_tipocontato")));
                /*
                 * Dados Prospect
                 */
                compromisso.getProspect().setCodigo(new Integer(dadosSQL.getInt("cap_prospect")));
                compromisso.getProspect().setNome((dadosSQL.getString("prospects_nome")));
                compromisso.getProspect().setCpf((dadosSQL.getString("prospects_cpf")));
                compromisso.getProspect().getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("prospects_unidadeensino")));
                compromisso.getProspect().setEmailPrincipal((dadosSQL.getString("prospects_emailprincipal")));
                compromisso.getProspect().setTelefoneResidencial((dadosSQL.getString("prospects_telefoneresidencial")));
                compromisso.getProspect().setTelefoneComercial((dadosSQL.getString("prospects_telefoneComercial")));
                compromisso.getProspect().setTelefoneRecado((dadosSQL.getString("prospects_telefoneRecado")));
                compromisso.getProspect().setCelular((dadosSQL.getString("prospects_celular")));
                getFacadeFactory().getPreInscricaoFacade().montarDadosCursoInteresseCompromissoAgendaPessoaHorarioPreInscricao(compromisso);
                if (!Uteis.isAtributoPreenchido(compromisso.getPreInscricao())) {
                	CursoInteresseVO cursoInteresse = getFacadeFactory().getCursoInteresseFacade().consultarPorCodigoProspect(compromisso.getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
                	compromisso.getCursoInteresseProspect().setCodigo(cursoInteresse.getCurso().getCodigo());
                	compromisso.getCursoInteresseProspect().setNome(cursoInteresse.getCurso().getNome());
                }
                
                /*
                 * Dados Campanha
                 */
                compromisso.getCampanha().setCodigo(new Integer(dadosSQL.getInt("cap_campanha")));
                compromisso.getCampanha().setDescricao((dadosSQL.getString("campanha_descricao")));
                compromisso.getCampanha().getUnidadeEnsino().setNome((dadosSQL.getString("unidadeensino_nome")));
                if (dadosSQL.getString("campanha_tipoCampanha") != null) {
                	compromisso.getCampanha().setTipoCampanha(TipoCampanhaEnum.valueOf(dadosSQL.getString("campanha_tipoCampanha")));
                }
                /*
                 * Dados Etapa
                 */
                compromisso.getEtapaWorkflowVO().setCodigo(new Integer(dadosSQL.getInt("etapaworkflow_codigo")));
                compromisso.getEtapaWorkflowVO().setNome((dadosSQL.getString("etapaworkflow_nome")));
                compromisso.setRealizado(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarSeCompromissoRealizadoDiaAtual(compromisso.getCodigo(), obj.getAno(), obj.getMes(), obj.getDia()));
                //compromisso.setAgendaPessoaHorario(obj);
                compromisso.getAgendaPessoaHorario().getAgendaPessoa().setCodigo(dadosSQL.getInt("aph_agendapessoa"));
                compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
                compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));
                
                compromisso.setDuvida(dadosSQL.getString("cap_duvida"));

                compromisso.setNovoObj(new Boolean(false));
                obj.getListaCompromissoAgendaPessoaHorarioVOs().add(compromisso);
            }

        }

        return obj;
    }

    public static AgendaPessoaHorarioVO montarDadosCodigoAgendaPessoaHorario(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AgendaPessoaHorarioVO obj = new AgendaPessoaHorarioVO();
        while (dadosSQL.next()) {
            if (obj.getCodigo() == null || obj.getCodigo().equals(0)) {
                obj.setCodigo(new Integer(dadosSQL.getInt("aph_codigo")));
            }
        }
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da
     * <code>AgendaPessoaHorarioVO</code> no BD. Faz uso da operação
     * <code>excluir</code> disponível na classe
     * <code>AgendaPessoaHorario</code>.
     *
     * @param <code>agendaPessoa</code> campo chave para exclusão dos objetos no
     * BD.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    public void excluirAgendaPessoaHorarios(Integer agendaPessoa, UsuarioVO usuarioLogado) throws Exception {
        AgendaPessoaHorario.excluir(getIdEntidade());
        String sql = "DELETE FROM AgendaPessoaHorario WHERE (agendaPessoa = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(sql, new Object[]{agendaPessoa});
    }

    /**
     * Operação responsável por alterar todos os objetos da
     * <code>AgendaPessoaHorarioVO</code> contidos em um Hashtable no BD. Faz
     * uso da operação
     * <code>excluirAgendaPessoaHorarios</code> e
     * <code>incluirAgendaPessoaHorarios</code> disponíveis na classe
     * <code>AgendaPessoaHorario</code>.
     *
     * @param objetos List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    public void alterarAgendaPessoaHorarios(Integer agendaPessoa, List objetos, UsuarioVO usuarioLogado) throws Exception {
//        String str = "DELETE FROM AgendaPessoaHorario WHERE agendaPessoa = " + agendaPessoa;
//        Iterator i = objetos.iterator();
//        while (i.hasNext()) {
//            AgendaPessoaHorarioVO objeto = (AgendaPessoaHorarioVO)i.next();
//            str += " AND codigo <> " + objeto.getCodigo().intValue();
//        }
//        PreparedStatement sqlExcluir = con.prepareStatement(str);
//        sqlExcluir.execute();
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            AgendaPessoaHorarioVO objeto = (AgendaPessoaHorarioVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuarioLogado);
            } else {
                alterar(objeto, usuarioLogado);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da
     * <code>AgendaPessoaHorarioVO</code> no BD. Garantindo o relacionamento com
     * a entidade principal
     * <code>crm.AgendaPessoa</code> através do atributo de vínculo.
     *
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    public void incluirAgendaPessoaHorarios(Integer agendaPessoaPrm, List objetos, UsuarioVO usuarioLogado) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            AgendaPessoaHorarioVO obj = (AgendaPessoaHorarioVO) e.next();
            obj.getAgendaPessoa().setCodigo(agendaPessoaPrm);
            incluir(obj, usuarioLogado);
        }
    }

    /**
     * Operação responsável por consultar todos os
     * <code>AgendaPessoaHorarioVO</code> relacionados a um objeto da classe
     * <code>crm.AgendaPessoa</code>.
     *
     * @param agendaPessoa Atributo de
     * <code>crm.AgendaPessoa</code> a ser utilizado para localizar os objetos
     * da classe
     * <code>AgendaPessoaHorarioVO</code>.
     * @return List Contendo todos os objetos da classe
     * <code>AgendaPessoaHorarioVO</code> resultantes da consulta.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    public static List consultarAgendaPessoaHorarios(Integer agendaPessoa, int nivelMontarDados) throws Exception {
        AgendaPessoaHorario.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM AgendaPessoaHorario WHERE agendaPessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{agendaPessoa});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>AgendaPessoaHorarioVO</code> através de sua chave primária.
     *
     * @exception Exception Caso haja problemas de conexão ou localização do
     * objeto procurado.
     */
    public AgendaPessoaHorarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM AgendaPessoaHorario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    public List<AgendaPessoaHorarioVO> consultarUnicidade(AgendaPessoaHorarioVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
        return new ArrayList(0);
    }

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoa(Integer unidadeEnsino, Integer responsavel, Integer codigoCampanha, Date dataFiltro, Boolean prospectsInativo, int nivelMontarDados, TipoCompromissoEnum tipoCompromisso, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where 1 = 1 ");
        
        if (prospectsInativo != null) {
            sql.append(" and prospects.inativo  = ").append(prospectsInativo);
        }
        if (responsavel != null && responsavel != 0) {
        	sql.append(" and pessoa.codigo = ").append(responsavel);
        }
        if (codigoCampanha != null && codigoCampanha != 0) {
            sql.append(" and campanha.codigo = ").append(codigoCampanha);
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
        	sql.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
            sql.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
            sql.append(" or prospects.unidadeensino is null )");
//            sql.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        
        Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(usuarioLogado);
		Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(usuarioLogado);
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
			sql.append(" and (campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA'  or campanha.codigo is null ) ");
		} else {//if (visualizarVendas) {
			sql.append(" and (campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' or campanha.codigo is null) ");
		}
		if (tipoCompromisso != null) {
        	sql.append(" and cap.tipoCompromisso = '").append(tipoCompromisso.toString()).append("' ");
        }
		sql.append(" UNION ");
		sql.append(" SELECT DISTINCT ");
		sql.append(" pessoa.nome as pessoa_nome, ");
		sql.append(" pessoa.cpf as pessoa_cpf, ");
		sql.append(" pessoa.codigo as pessoa_codigo, ");
		sql.append(" aph.codigo as aph_codigo, ");
		sql.append(" aph.dia as aph_dia, ");
		sql.append(" aph.mes as aph_mes,  ");
		sql.append(" aph.ano as aph_ano , ");
		sql.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
		sql.append(" aph.agendapessoa as aph_agendapessoa, ");
		sql.append(" cap.codigo as cap_codigo, ");
		sql.append(" cap.descricao as cap_descricao,  ");
		sql.append(" cap.hora as cap_hora ,  ");
		sql.append(" cap.tipocompromisso as cap_tipocompromisso,  ");
		sql.append(" cap.tipoSituacaoCompromissoEnum as cap_tipoSituacaoCompromissoEnum,  ");
		sql.append(" cap.observacao as cap_observacao,  ");
		sql.append(" cap.origem as cap_origem,  ");
		sql.append(" cap.urgente as cap_urgente,  ");
		sql.append(" cap.datacadastro as cap_datacadastro,   ");
		sql.append(" cap.datacompromisso as cap_datacompromisso,   ");
		sql.append(" cap.dataInicialCompromisso AS cap_dataInicialCompromisso, ");
		sql.append(" cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso, ");
		sql.append(" cap.tipocontato as cap_tipocontato,   ");
        sql.append(" cap.prospect as cap_prospect ,   ");
        sql.append(" cap.campanha as cap_campanha,  ");
        sql.append(" cap.agendapessoahorario as cap_agendapessoahorario, cap.duvida AS cap_duvida, ");
        sql.append(" prospects.nome as prospects_nome,  ");
        sql.append(" prospects.cpf as prospects_cpf,  ");
        sql.append(" prospects.unidadeensino as prospects_unidadeensino,  ");
        sql.append(" prospects.emailprincipal as prospects_emailprincipal,  ");
        sql.append(" prospects.telefoneresidencial as prospects_telefoneresidencial,  ");
        sql.append(" prospects.telefoneComercial as prospects_telefoneComercial,  ");
        sql.append(" prospects.telefoneRecado as prospects_telefoneRecado,  ");
        sql.append(" prospects.celular as prospects_celular,  ");
        sql.append(" campanha.descricao as campanha_descricao, campanha.tipoCampanha AS campanha_tipoCampanha, ");
        sql.append(" etapaworkflow.codigo as etapaworkflow_codigo,  ");
        sql.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        sql.append(" unidadeensino.nome AS unidadeensino_nome,  ");
        sql.append(" (select count(matricula.matricula) from matricula ");
        sql.append(" where aluno = pessoaProspect.codigo ");
        sql.append(" and matricula.situacao = 'AT') > 0 as possuiMatricula, ");
        sql.append(" ( select '<b>Última Interação -> Data:</b> &nbsp; ' || TO_CHAR(dataInicio, 'DD/MM/YYYY') || '<BR/> <b>Responsável: </b> &nbsp; ' || usuario.nome || '<BR/> <b>Observação: </b> &nbsp;' || observacao from interacaoworkflow ");
        sql.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel where interacaoworkflow.prospect = prospects.codigo order by datainicio desc limit 1  ) as ultimaInteracao ");
        sql.append(" FROM agendapessoahorario as aph  ");
        sql.append(" left join reagendamentocompromisso rg  on aph.codigo = rg.agendaPessoaHorario");
        sql.append(" inner join compromissoagendapessoahorario as cap  on cap.codigo = rg.compromissoagendapessoahorario ");
        sql.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        sql.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        sql.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
        sql.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
        sql.append(" left join prospects on cap.prospect = prospects.codigo  ");
        sql.append(" LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
        sql.append(" left join campanha on cap.campanha = campanha.codigo ");
        sql.append(" left join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
        sql.append(" left join unidadeensino on unidadeensino.codigo = campanha.unidadeensino ");
        sql.append(" where 1 = 1  ");
        if (prospectsInativo != null) {
            sql.append(" and prospects.inativo  = ").append(prospectsInativo);
        }
        if (responsavel != null && responsavel != 0) {
        	sql.append(" and pessoa.codigo = ").append(responsavel);
        }
        if (codigoCampanha != null && codigoCampanha != 0) {
            sql.append(" and campanha.codigo = ").append(codigoCampanha);
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
        	sql.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
            sql.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
            sql.append(" or prospects.unidadeensino is null )");
//            sql.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
			sql.append(" and (campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA'  or campanha.codigo is null ) ");
		} else {//if (visualizarVendas) {
			sql.append(" and (campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' or campanha.codigo is null) ");
		}
		if (tipoCompromisso != null) {
        	sql.append(" and cap.tipoCompromisso = '").append(tipoCompromisso.toString()).append("' ");
        }
		sql.append(" order by cap_hora");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }
    
	public Boolean verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaCobranca", usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public Boolean verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaVendas", usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}


    public AgendaPessoaHorarioVO consultarAPartirDiaMesAnoPorCodigoProspect(Integer prospect, Integer unidadeEnsino, Integer responsavel, Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where 1 = 1 and prospects.inativo  = false ");
        if (responsavel != null && responsavel != 0) {
            sql.append(" and pessoa.codigo = ").append(responsavel);
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sql.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
        }
        sql.append(" and prospects.codigo = ").append(prospect);
        
        sql.append(" AND ((cap.hora > '").append(Uteis.getHoraAtual()).append("'");
        sql.append(" AND aph.dia = ").append(Uteis.getDiaMesData(new Date()));
        sql.append(" AND aph.mes = ").append(Uteis.getMesData(new Date()));
        sql.append(" AND aph.ano = ").append(Uteis.getAnoData(new Date()));
        sql.append(") OR  (aph.dia > ").append(Uteis.getDiaMesData(new Date()));
        sql.append(" AND aph.mes = ").append(Uteis.getMesData(new Date()));
        sql.append(" AND aph.ano = ").append(Uteis.getAnoData(new Date()));
        sql.append(") OR (aph.mes > ").append(Uteis.getMesData(new Date()));
        sql.append(" AND aph.ano = ").append(Uteis.getAnoData(new Date()));
        sql.append(") OR aph.ano > ").append(Uteis.getAnoData(new Date()));
        sql.append(") order by cap.hora  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }
    
    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorCodigoAgendaPessoa(Integer unidadeEnsino, Integer codigoAgendaPessoa, Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder str = new StringBuilder();
        str.append(" SELECT DISTINCT ");
        str.append(" pessoa.nome as pessoa_nome, ");
        str.append(" pessoa.cpf as pessoa_cpf, ");
        str.append(" pessoa.codigo as pessoa_codigo, ");
        str.append(" aph.codigo as aph_codigo, ");
        str.append(" aph.dia as aph_dia, ");
        str.append(" aph.mes as aph_mes,  ");
        str.append(" aph.ano as aph_ano , ");
        str.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
        str.append(" aph.agendapessoa as aph_agendapessoa, ");
        str.append(" cap.codigo as cap_codigo, ");
        str.append(" cap.descricao as cap_descricao,  ");
        str.append(" cap.hora as cap_hora ,  ");
        str.append(" cap.tipocompromisso as cap_tipocompromisso,  ");
        str.append(" cap.tipoSituacaoCompromissoEnum as cap_tipoSituacaoCompromissoEnum,  ");
        str.append(" cap.observacao as cap_observacao,  ");
        str.append(" cap.origem as cap_origem,  ");
        str.append(" cap.urgente as cap_urgente,  ");
        str.append(" cap.datacadastro as cap_datacadastro,   ");
        str.append(" cap.datacompromisso as cap_datacompromisso,   ");
		str.append(" cap.dataInicialCompromisso AS cap_dataInicialCompromisso, ");
		str.append(" cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso, ");
        str.append(" cap.tipocontato as cap_tipocontato,   ");
        str.append(" cap.prospect as cap_prospect ,   ");
        str.append(" cap.campanha as cap_campanha,  ");
        str.append(" cap.agendapessoahorario as cap_agendapessoahorario, cap.duvida AS cap_duvida, ");
        str.append(" prospects.nome as prospects_nome,  ");
        str.append(" prospects.cpf as prospects_cpf,  ");
        str.append(" prospects.unidadeensino as prospects_unidadeensino,  ");
        str.append(" prospects.emailprincipal as prospects_emailprincipal,  ");
        str.append(" prospects.telefoneresidencial as prospects_telefoneresidencial,  ");
        str.append(" prospects.telefoneComercial as prospects_telefoneComercial,  ");
        str.append(" prospects.telefoneRecado as prospects_telefoneRecado,  ");
        str.append(" prospects.celular as prospects_celular,  ");
        str.append(" campanha.descricao as campanha_descricao, campanha.tipoCampanha AS campanha_tipoCampanha, ");
        str.append(" etapaworkflow.codigo as etapaworkflow_codigo,  ");
        str.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        str.append(" unidadeensino.nome AS unidadeensino_nome,  ");
        str.append(" (select count(matricula.matricula) from matricula ");
        str.append(" where aluno = pessoaProspect.codigo ");
        str.append(" and matricula.situacao = 'AT') > 0 as possuiMatricula, ");
        str.append(" ( select '<b>Última Interação -> Data:</b> &nbsp; ' || TO_CHAR(dataInicio, 'DD/MM/YYYY') || '<BR/> <b>Responsável: </b> &nbsp; ' || usuario.nome || '<BR/> <b>Observação: </b> &nbsp;' || observacao from interacaoworkflow ");
        str.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel where interacaoworkflow.prospect = prospects.codigo order by datainicio desc limit 1  ) as ultimaInteracao ");
        str.append(" FROM agendapessoahorario as aph  ");
        str.append(" left join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
        str.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        str.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        str.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
        str.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
        str.append(" left join prospects on cap.prospect = prospects.codigo and prospects.inativo  = false ");
        str.append(" LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
        str.append(" left join campanha on cap.campanha = campanha.codigo ");
        str.append(" left join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
        str.append(" left join unidadeensino on unidadeensino.codigo = campanha.unidadeensino ");
        str.append(" where 1=1 ");
        if (codigoAgendaPessoa != null && codigoAgendaPessoa != 0) {
            str.append(" and ap.codigo = ").append(codigoAgendaPessoa);
        }
        if (codigoCampanha != null && codigoCampanha != 0) {
        	str.append(" and campanha.codigo = ").append(codigoCampanha);
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
        	str.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
        }
        str.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        str.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        str.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        
        str.append(" order by cap.hora  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoa(AgendaPessoaHorarioVO agendaPessoaHorario, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where 1 = 1");
        if (agendaPessoaHorario.getAgendaPessoa().getPessoa().getCodigo() != null && agendaPessoaHorario.getAgendaPessoa().getPessoa().getCodigo() != 0) {
            sql.append(" and pessoa.codigo = ").append(agendaPessoaHorario.getAgendaPessoa().getPessoa().getCodigo());
        }
        sql.append(" and aph.dia = ").append(agendaPessoaHorario.getDia());
        sql.append(" and aph.mes = ").append(agendaPessoaHorario.getMes());
        sql.append(" and aph.ano  = ").append(agendaPessoaHorario.getAno());
        
        sql.append(" order by cap.hora  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
    }

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(Integer agendaPessoa, Date dataFiltro, int nivelMontarDados, boolean validarProspects, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where aph.agendapessoa = ").append(agendaPessoa);
        if (validarProspects) {
        	sql.append("and (prospects.inativo  = false OR prospects.codigo is null) ");
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        sql.append("  ");
        sql.append(" order by cap.hora  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);

    }

    public List consultarPorDiaMesAnoAdministradorObterNomeResponsavel(Integer unidadeEnsino, Date dataFiltro, TipoVisaoAgendaEnum tipoVisaoAgendaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" distinct pessoa.codigo as pessoa_codigo, ");
        str.append(" pessoa.nome as pessoa_nome ");
        str.append(" FROM agendapessoahorario as aph  ");
        str.append(" left join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
        str.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        str.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        str.append(" left join campanha on cap.campanha = campanha.codigo ");
        str.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
        str.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
        str.append("  where 1=1 ");
        if (unidadeEnsino != 0 && unidadeEnsino != null) {
        	str.append(" and funcionariocargo.unidadeEnsino  = " + unidadeEnsino);
        }
        if (tipoVisaoAgendaEnum.equals(TipoVisaoAgendaEnum.DIA)) {
            str.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        }
        str.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        str.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        str.append(" order by pessoa.nome  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        return montarResponsavelCombobox(tabelaResultado);
    }

    public List montarResponsavelCombobox(SqlRowSet dadosSQL) throws Exception {
        List objs = new ArrayList(0);
        while (dadosSQL.next()) {
            PessoaVO pessoa = new PessoaVO();
            pessoa.setCodigo(dadosSQL.getInt("pessoa_codigo"));
            pessoa.setNome(dadosSQL.getString("pessoa_nome"));
            objs.add(pessoa);
        }
        return objs;
    }

    public List consultarPorDiaMesAnoObterNomeCampanha(Integer codigoPessoa, Date dataFiltro, TipoVisaoAgendaEnum tipoVisaoAgendaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" distinct campanha.codigo as campanha_codigo, ");
        str.append(" campanha.descricao as campanha_descricao ");
        str.append(" FROM agendapessoahorario as aph  ");
        str.append(" left join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
        str.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        str.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        str.append(" left join campanha on cap.campanha = campanha.codigo ");
        str.append("  where 1=1 ");
        if (codigoPessoa != null && codigoPessoa != 0) {
            str.append(" and pessoa.codigo = ").append(codigoPessoa);
        }
//        if (codigoCampanha != null && codigoCampanha != 0) {
//            str.append(" and campanha.codigo = ").append(codigoCampanha);
//        }
        if (tipoVisaoAgendaEnum.equals(TipoVisaoAgendaEnum.DIA)) {
            str.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        }
        str.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        str.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        str.append(" order by campanha.descricao  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        return montarCampanhaCombobox(tabelaResultado);
    }

    public List montarCampanhaCombobox(SqlRowSet dadosSQL) throws Exception {
        List objs = new ArrayList(0);
        while (dadosSQL.next()) {
            CampanhaVO campanha = new CampanhaVO();
            campanha.setCodigo(dadosSQL.getInt("campanha_codigo"));
            campanha.setDescricao(dadosSQL.getString("campanha_descricao"));
            objs.add(campanha);
        }
        return objs;
    }
    
    public AgendaPessoaHorarioVO consultarPorDiaMesAnoAdministrador(Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        return consultarPorDiaMesAnoAdministrador(codigoCampanha, 0, dataFiltro, nivelMontarDados, null, usuarioLogado);
    }

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoAdministrador(Integer codigoCampanha, Integer unidade, Date dataFiltro, int nivelMontarDados, TipoCompromissoEnum tipoCompromisso, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where 1=1 ");
        if (codigoCampanha != null && codigoCampanha != 0) {
            sql.append(" and campanha.codigo = ").append(codigoCampanha);
        }
        if (unidade != null && unidade != 0) {
        	sql.append(" and (prospects.unidadeensino = ").append(unidade);
            sql.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidade).append(") ");
            sql.append(" or prospects.unidadeensino is null )");
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        if (tipoCompromisso != null) {
        	sql.append(" and cap.tipoCompromisso = '").append(tipoCompromisso).append("' ");
        }
        sql.append(" UNION ");
		sql.append(" SELECT DISTINCT ");
		sql.append(" pessoa.nome as pessoa_nome, ");
		sql.append(" pessoa.cpf as pessoa_cpf, ");
		sql.append(" pessoa.codigo as pessoa_codigo, ");
		sql.append(" aph.codigo as aph_codigo, ");
		sql.append(" aph.dia as aph_dia, ");
		sql.append(" aph.mes as aph_mes,  ");
		sql.append(" aph.ano as aph_ano , ");
		sql.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
		sql.append(" aph.agendapessoa as aph_agendapessoa, ");
		sql.append(" cap.codigo as cap_codigo, ");
		sql.append(" cap.descricao as cap_descricao,  ");
		sql.append(" cap.hora as cap_hora ,  ");
		sql.append(" cap.tipocompromisso as cap_tipocompromisso,  ");
		sql.append(" cap.tipoSituacaoCompromissoEnum as cap_tipoSituacaoCompromissoEnum,  ");
		sql.append(" cap.observacao as cap_observacao,  ");
		sql.append(" cap.origem as cap_origem,  ");
		sql.append(" cap.urgente as cap_urgente,  ");
		sql.append(" cap.datacadastro as cap_datacadastro,   ");
		sql.append(" cap.datacompromisso as cap_datacompromisso,   ");
		sql.append(" cap.dataInicialCompromisso AS cap_dataInicialCompromisso, ");
		sql.append(" cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso, ");
		sql.append(" cap.tipocontato as cap_tipocontato,   ");
        sql.append(" cap.prospect as cap_prospect ,   ");
        sql.append(" cap.campanha as cap_campanha,  ");
        sql.append(" cap.agendapessoahorario as cap_agendapessoahorario, cap.duvida AS cap_duvida, ");
        sql.append(" prospects.nome as prospects_nome,  ");
        sql.append(" prospects.cpf as prospects_cpf,  ");
        sql.append(" prospects.unidadeensino as prospects_unidadeensino,  ");
        sql.append(" prospects.emailprincipal as prospects_emailprincipal,  ");
        sql.append(" prospects.telefoneresidencial as prospects_telefoneresidencial,  ");
        sql.append(" prospects.telefoneComercial as prospects_telefoneComercial,  ");
        sql.append(" prospects.telefoneRecado as prospects_telefoneRecado,  ");
        sql.append(" prospects.celular as prospects_celular,  ");
        sql.append(" campanha.descricao as campanha_descricao, campanha.tipoCampanha AS campanha_tipoCampanha, ");
        sql.append(" etapaworkflow.codigo as etapaworkflow_codigo,  ");
        sql.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        sql.append(" unidadeensino.nome AS unidadeensino_nome,  ");
        sql.append(" (select count(matricula.matricula) from matricula ");
        sql.append(" where aluno = pessoaProspect.codigo ");
        sql.append(" and matricula.situacao = 'AT') > 0 as possuiMatricula, ");
        sql.append(" ( select '<b>Última Interação -> Data:</b> &nbsp; ' || TO_CHAR(dataInicio, 'DD/MM/YYYY') || '<BR/> <b>Responsável: </b> &nbsp; ' || usuario.nome || '<BR/> <b>Observação: </b> &nbsp;' || observacao from interacaoworkflow ");
        sql.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel where interacaoworkflow.prospect = prospects.codigo order by datainicio desc limit 1  ) as ultimaInteracao ");
        sql.append(" FROM agendapessoahorario as aph  ");
        sql.append(" left join reagendamentocompromisso rg  on aph.codigo = rg.agendaPessoaHorario");
        sql.append(" inner join compromissoagendapessoahorario as cap  on cap.codigo = rg.compromissoagendapessoahorario ");
        sql.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        sql.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        sql.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
        sql.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
        sql.append(" left join prospects on cap.prospect = prospects.codigo  ");
        sql.append(" LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
        sql.append(" left join campanha on cap.campanha = campanha.codigo ");
        sql.append(" left join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
        sql.append(" left join unidadeensino on unidadeensino.codigo = campanha.unidadeensino ");
        sql.append(" where 1 = 1  ");
        if (codigoCampanha != null && codigoCampanha != 0) {
            sql.append(" and campanha.codigo = ").append(codigoCampanha);
        }
        if (unidade != null && unidade != 0) {
        	sql.append(" and (prospects.unidadeensino = ").append(unidade);
            sql.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidade).append(") ");
            sql.append(" or prospects.unidadeensino is null )");
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        if (tipoCompromisso != null) {
        	sql.append(" and cap.tipoCompromisso = '").append(tipoCompromisso).append("' ");
        }
        sql.append(" order by cap_hora");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

//    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoaRetornandoCodigo(Integer agendaPessoa, Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
//        StringBuilder sql = getSQLPadraoConsultaRapida().append(" where aph.agendapessoa = ").append(agendaPessoa);
//        if (codigoCampanha != null && codigoCampanha != 0) {
//            sql.append(" and campanha.codigo = ").append(codigoCampanha);
//        }
//        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
//        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
//        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
//        sql.append(" order by cap.hora  ");
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//        return montarDadosCodigoAgendaPessoaHorario(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//    }
    private StringBuilder getSQLPadraoConsultaRapida() {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT DISTINCT ");
        str.append(" pessoa.nome as pessoa_nome, ");
        str.append(" pessoa.cpf as pessoa_cpf, ");
        str.append(" pessoa.codigo as pessoa_codigo, ");
        str.append(" aph.codigo as aph_codigo, ");
        str.append(" aph.dia as aph_dia, ");
        str.append(" aph.mes as aph_mes,  ");
        str.append(" aph.ano as aph_ano , ");
        str.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
        str.append(" aph.agendapessoa as aph_agendapessoa, ");
        str.append(" cap.codigo as cap_codigo, ");
        str.append(" cap.descricao as cap_descricao,  ");
        str.append(" cap.hora as cap_hora ,  ");
        str.append(" cap.tipocompromisso as cap_tipocompromisso,  ");
        str.append(" cap.tipoSituacaoCompromissoEnum as cap_tipoSituacaoCompromissoEnum,  ");
        str.append(" cap.observacao as cap_observacao,  ");
        str.append(" cap.origem as cap_origem,  ");
        str.append(" cap.urgente as cap_urgente,  ");
        str.append(" cap.datacadastro as cap_datacadastro,   ");
        str.append(" cap.datacompromisso as cap_datacompromisso,   ");
		str.append(" cap.dataInicialCompromisso AS cap_dataInicialCompromisso, ");
		str.append(" cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso, ");
        str.append(" cap.tipocontato as cap_tipocontato,   ");
        str.append(" cap.prospect as cap_prospect ,   ");
        str.append(" cap.campanha as cap_campanha,  ");
        str.append(" cap.agendapessoahorario as cap_agendapessoahorario, cap.duvida AS cap_duvida, ");
        str.append(" prospects.nome as prospects_nome,  ");
        str.append(" prospects.cpf as prospects_cpf,  ");
        str.append(" prospects.unidadeensino as prospects_unidadeensino,  ");
        str.append(" prospects.emailprincipal as prospects_emailprincipal,  ");
        str.append(" prospects.telefoneresidencial as prospects_telefoneresidencial,  ");
        str.append(" prospects.telefoneComercial as prospects_telefoneComercial,  ");
        str.append(" prospects.telefoneRecado as prospects_telefoneRecado,  ");
        str.append(" prospects.celular as prospects_celular,  ");
        str.append(" campanha.descricao as campanha_descricao, campanha.tipoCampanha AS campanha_tipoCampanha, ");
        str.append(" etapaworkflow.codigo as etapaworkflow_codigo,  ");
        str.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        str.append(" unidadeensino.nome AS unidadeensino_nome,  ");
        str.append(" (select count(matricula.matricula) from matricula ");
        //str.append(" where aluno = pessoaProspect.codigo and ((matricula.data < cap.datacompromisso) or (matricula.data > current_date and current_date <= cap.datacompromisso)) ");
        str.append(" where aluno = pessoaProspect.codigo ");
        str.append(" and matricula.situacao = 'AT') > 0 as possuiMatricula, ");

//        str.append(" ( CASE WHEN (select count(1) from interacaoworkflow  where compromissoagendapessoahorario =   cap.codigo limit 1 ) = 0   ");
//        str.append(" then ( SELECT distinct etapaworkflow.nome from etapaworkflow   ");
//        str.append(" inner join workflow on workflow.codigo = campanha.workflow    ");
//        str.append(" inner join situacaoprospectworkflow as spw on spw.codigo = etapaworkflow.situacaodefinirprospectfinal  ");
//        str.append(" inner join situacaoprospectpipeline as spp on spp.codigo = spw.situacaoprospectpipeline and spp.controle = '").append(SituacaoProspectPipelineControleEnum.INICIAL).append("'  ");
//        str.append(" ) else (  select etapaworkflow.nome from interacaoworkflow ");
//        str.append(" inner join etapaworkflow on interacaoworkflow.etapaworkflow = etapaworkflow.codigo  ");
//        str.append(" where interacaoworkflow.codigo = (select MAX(codigo) from interacaoworkflow where compromissoagendapessoahorario = cap.codigo)   	  ");
//        str.append(" ) end ) as nome_etapaAtual ");

        str.append(" ( select '<b>Última Interação -> Data:</b> &nbsp; ' || TO_CHAR(dataInicio, 'DD/MM/YYYY') || '<BR/> <b>Responsável: </b> &nbsp; ' || usuario.nome || '<BR/> <b>Observação: </b> &nbsp;' || observacao from interacaoworkflow ");
        str.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel where interacaoworkflow.prospect = prospects.codigo order by datainicio desc limit 1  ) as ultimaInteracao ");

        str.append(" FROM agendapessoahorario as aph  ");
        str.append(" left join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
        str.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        str.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        str.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
        str.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
        str.append(" left join prospects on cap.prospect = prospects.codigo  ");
        str.append(" LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
        str.append(" left join campanha on cap.campanha = campanha.codigo ");
        str.append(" left join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
        str.append(" left join unidadeensino on unidadeensino.codigo = campanha.unidadeensino ");
        
        
        return str;
    }

    public AgendaPessoaHorarioVO consultarPorDiaPorMesPorAnoPorAgendaPessoaEspecificaDoMes(Integer agendaPessoa, Integer codigoCampanha, Integer unidadeEnsino, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapidaMes(codigoCampanha, unidadeEnsino);
        sql.append(" where 1=1  ");
        if (agendaPessoa != null && agendaPessoa != 0) {
            sql.append(" and pessoa.codigo = ").append(agendaPessoa);
        }
        sql.append(" and aph.dia = ").append(Uteis.getDiaMesData(dataFiltro));
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        sql.append(" order by aph.dia  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
         if (!tabelaResultado.next()) {
            return new AgendaPessoaHorarioVO();
        }
        return montarDadoPersonalizadoMes(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    public List<AgendaPessoaHorarioVO> consultarPorMesPorAnoPorAgendaPessoaEspecificaDoMes(Integer agendaPessoa, Integer codigoCampanha, Integer unidadeEnsino, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = new StringBuilder(" select 0 as aph_codigo, 0 as aph_agendapessoa, aph_dia, aph_mes, aph_ano, aph_diaSemanaEnum, sum(qtdContato) as qtdContato, sum(qtdTarefa) as qtdTarefa, ");
        sql.append(" sum(quantidadeNovasAgendasRealizadas) as quantidadeNovasAgendasRealizadas, ");
        sql.append(" sum(quantidadeNovasAgendasRealizadasComInsucesso) as quantidadeNovasAgendasRealizadasComInsucesso, ");
        sql.append(" sum(quantidadeNovasAgendasPendentes) as quantidadeNovasAgendasPendentes, ");
        sql.append(" sum(quantidadeReagendasRealizadas) as quantidadeReagendasRealizadas, ");
        sql.append(" sum(quantidadeReagendasRealizadasComInsucesso) as quantidadeReagendasRealizadasComInsucesso, ");
        sql.append(" sum(quantidadeReagendasPendentes) as quantidadeReagendasPendentes from ( ");
        sql.append(getSQLPadraoConsultaRapidaMes(codigoCampanha, unidadeEnsino));
        sql.append(" where 1=1  ");
        if (agendaPessoa != null && agendaPessoa != 0) {
            sql.append(" and pessoa.codigo = ").append(agendaPessoa);
        }
        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
        sql.append(" order by aph.dia  ");
        sql.append(" ) as t group by aph_dia, aph_mes, aph_ano, aph_diaSemanaEnum ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosPersonalizadoMes(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

//    public List<AgendaPessoaHorarioVO> consultarPorMesPorAnoPorAgendaPessoaAdministrador(Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
//        StringBuilder sql = getSQLPadraoConsultaRapidaMes(codigoCampanha).append(" where 1=1 ");
//        sql.append(" and aph.mes = ").append(Uteis.getMesData(dataFiltro));
//        sql.append(" and aph.ano  = ").append(Uteis.getAnoData(dataFiltro));
//        sql.append(" order by aph.dia  ");
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//        return montarDadosPersonalizadoMes(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//    }
    private StringBuilder getSQLPadraoConsultaRapidaMes(Integer codigoCampanha, Integer unidadeEnsino) {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" aph.codigo as aph_codigo, ");
        str.append(" aph.dia as aph_dia, ");
        str.append(" aph.mes as aph_mes,  ");
        str.append(" aph.ano as aph_ano , ");
        str.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
        str.append(" aph.agendapessoa as aph_agendapessoa, ");
        if (codigoCampanha != null && codigoCampanha != 0) {
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where tipocompromisso  ='CONTATO' and agendapessoahorario  = aph.codigo  ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as qtdContato, ");
            

            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where tipocompromisso  ='TAREFA' and agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as qtdTarefa, ");

            // ============== OBTENDO quantidadeNovasAgendasRealizadas;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeNovasAgendasRealizadas, ");
            // =========================================================
            
            // ============== OBTENDO quantidadeNovasAgendasRealizadasComInsucesso;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO_COM_INSUCESSO_CONTATO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeNovasAgendasRealizadasComInsucesso, ");
            // =========================================================
            
            // ============== OBTENDO quantidadeNovasAgendasPendentes;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO' and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO_COM_INSUCESSO_CONTATO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeNovasAgendasPendentes, ");
            // =========================================================
            		
            // ============== OBTENDO quantidadeReagendasRealizadas;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeReagendasRealizadas, ");
            // =========================================================
            
            // ============== OBTENDO quantidadeReagendasRealizadasComInsucesso;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO_COM_INSUCESSO_CONTATO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeReagendasRealizadasComInsucesso, ");
            // =========================================================      
            
            // ============== OBTENDO quantidadeReagendasPendentes;
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
            str.append(" INNER JOIN campanha  on compromissoagendapessoahorario.campanha = campanha.codigo ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
            str.append(" where agendapessoahorario  = aph.codigo ");
            if(unidadeEnsino != null && unidadeEnsino > 0){
            	str.append(" and campanha.unidadeEnsino = ").append(unidadeEnsino).append("  ");
            }
            // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
        	str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
        	// verificando se a situacao e pendente (diferente de realizada)
        	str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO' and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO_COM_INSUCESSO_CONTATO'"); 
            str.append(" and campanha.codigo = ").append(codigoCampanha).append(" ) as quantidadeReagendasPendentes ");
            // =========================================================            
            
        } else {
            str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario ");
            str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false   ");
            str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
            str.append(" where tipocompromisso  ='CONTATO' and agendapessoahorario  = aph.codigo ");
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           str.append(" ) as qtdContato, ");
           
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where tipocompromisso  ='TAREFA' and agendapessoahorario  = aph.codigo ");
           if (unidadeEnsino != null && unidadeEnsino > 0) {
				str.append(" and prospects.unidadeEnsino = ").append(unidadeEnsino).append("  ");
           }
           str.append(" ) as qtdTarefa, ");
           
           // ============== OBTENDO quantidadeNovasAgendasRealizadas;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO' )as quantidadeNovasAgendasRealizadas, "); 
           // =========================================================
           
           // ============== OBTENDO quantidadeNovasAgendasRealizadasComInsucesso;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO_COM_INSUCESSO_CONTATO' ) as quantidadeNovasAgendasRealizadasComInsucesso, "); 
           // =========================================================
           
           // ============== OBTENDO quantidadeNovasAgendasPendentes;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma nova agenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso = compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO' and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO_COM_INSUCESSO_CONTATO' ) as quantidadeNovasAgendasPendentes, "); 
       	   // =========================================================
           		
           // ============== OBTENDO quantidadeReagendasRealizadas;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO' ) as quantidadeReagendasRealizadas, "); 
           // =========================================================
           
           // ============== OBTENDO quantidadeReagendasRealizadasComInsucesso;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum = 'REALIZADO_COM_INSUCESSO_CONTATO' ) as quantidadeReagendasRealizadasComInsucesso, "); 
           // =========================================================      
           
           // ============== OBTENDO quantidadeReagendasPendentes;
           str.append(" ( SELECT count (compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario  ");
           str.append(" inner join prospects on prospects.codigo = prospect and prospects.inativo = false ");
           str.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
           str.append(" where agendapessoahorario  = aph.codigo ");
           if(unidadeEnsino != null && unidadeEnsino > 0){
        	   str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
				str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
				str.append(" or prospects.unidadeensino is null )");
           }
           // verificando se trata-se de uma REagenda. Quando as datas sao iguais é uma nova agenda, quando diferentes, já refere-se a reagendamento
       	   str.append(" and compromissoagendapessoahorario.datainicialcompromisso != compromissoagendapessoahorario.datacompromisso");
       	   // verificando se a situacao e pendente (diferente de realizada)
       	   str.append(" and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO' and compromissoagendapessoahorario.tipoSituacaoCompromissoEnum != 'REALIZADO_COM_INSUCESSO_CONTATO' ) as quantidadeReagendasPendentes "); 
           // =========================================================            
           
        }
        str.append(" FROM agendapessoahorario as aph  ");
        str.append(" left join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
        str.append(" left join pessoa on ap.pessoa = pessoa.codigo ");
        return str;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>AgendaPessoaHorarioVO</code>.
     *
     * @return O objeto da classe
     * <code>AgendaPessoaHorarioVO</code> com os dados devidamente montados.
     */
    public static List<AgendaPessoaHorarioVO> montarDadosPersonalizadoMes(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        List<AgendaPessoaHorarioVO> lista = new ArrayList<AgendaPessoaHorarioVO>();
        while (dadosSQL.next()) {
            lista.add(montarDadoPersonalizadoMes(dadosSQL, nivelMontarDados));
        }
        return lista;
    }

    public static AgendaPessoaHorarioVO montarDadoPersonalizadoMes(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AgendaPessoaHorarioVO obj = new AgendaPessoaHorarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("aph_codigo")));
        obj.getAgendaPessoa().setCodigo(new Integer(dadosSQL.getInt("aph_agendapessoa")));
        obj.setDia(new Integer(dadosSQL.getInt("aph_dia")));
        obj.setMes(new Integer(dadosSQL.getInt("aph_mes")));
        obj.setAno(new Integer(dadosSQL.getInt("aph_ano")));
        obj.setDiaSemanaEnum(DiaSemana.valueOf(dadosSQL.getString("aph_diaSemanaEnum")));
        obj.setQuantidadeContatos(new Integer(dadosSQL.getInt("qtdcontato")));
        obj.setQuantidadeTarefa(new Integer(dadosSQL.getInt("qtdtarefa")));

        // montando totais novas agendas
        obj.setQuantidadeNovasAgendasRealizadas(new Integer(dadosSQL.getInt("quantidadeNovasAgendasRealizadas")));
        obj.setQuantidadeNovasAgendasRealizadasComInsucesso(new Integer(dadosSQL.getInt("quantidadeNovasAgendasRealizadasComInsucesso")));
        obj.setQuantidadeNovasAgendasPendentes(new Integer(dadosSQL.getInt("quantidadeNovasAgendasPendentes")));
        obj.setQuantidadeNovasAgendas(new Integer(dadosSQL.getInt("quantidadeNovasAgendasRealizadas") + dadosSQL.getInt("quantidadeNovasAgendasRealizadasComInsucesso") + dadosSQL.getInt("quantidadeNovasAgendasPendentes")));
        
        // montando totais das reagendas
        obj.setQuantidadeReagendasRealizadas(new Integer(dadosSQL.getInt("quantidadeReagendasRealizadas")));
        obj.setQuantidadeReagendasRealizadasComInsucesso(new Integer(dadosSQL.getInt("quantidadeReagendasRealizadasComInsucesso")));
        obj.setQuantidadeReagendasPendentes(new Integer(dadosSQL.getInt("quantidadeReagendasPendentes")));
        obj.setQuantidadeReagendas(new Integer(dadosSQL.getInt("quantidadeReagendasRealizadas") + dadosSQL.getInt("quantidadeReagendasRealizadasComInsucesso") + dadosSQL.getInt("quantidadeReagendasPendentes")));
        
        obj.setQuantidadeTotalAgendas(obj.getQuantidadeNovasAgendas() + obj.getQuantidadeReagendas());
        
        
        obj.setIsAtivo(true);
        obj.setNovoObj(false);
        return obj;
    
}
/**
 * Operação reponsável por retornar o identificador desta classe. Este
 * identificar é utilizado para verificar as permissões de acesso as operações
 * desta classe.
 */
public static String getIdEntidade() {
        return AgendaPessoaHorario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AgendaPessoaHorario.idEntidade = idEntidade;
    }
    
    
    @Override
    public List<AgendaPessoaHorarioVO> consultarAgendaPessoaJobNotificacao(){
    	List<AgendaPessoaHorarioVO> listaRetorno = new ArrayList<AgendaPessoaHorarioVO>(0);
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select pessoa.nome, pessoa.email, compromissoagendapessoahorario.datacompromisso, agendapessoa.pessoa, campanha.unidadeensino, count(compromissoagendapessoahorario.codigo) ");
    	sql.append(" from compromissoagendapessoahorario   ");
    	sql.append(" inner join campanha on compromissoagendapessoahorario.campanha = campanha.codigo ");
    	sql.append(" inner join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario");
    	sql.append(" inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
    	sql.append(" inner join pessoa on pessoa.codigo = agendapessoa.pessoa");
    	sql.append(" where compromissoagendapessoahorario.datacompromisso = current_date ");
    	sql.append(" group by pessoa.nome, agendapessoa.codigo, compromissoagendapessoahorario.datacompromisso, agendapessoa.pessoa,pessoa.email, campanha.unidadeensino ");
    	sql.append(" having count(compromissoagendapessoahorario.codigo) > 0 ");
    	sql.append(" order by pessoa.nome,pessoa.email");
    	sql.append(" ");
    	sql.append(" ");
    	SqlRowSet tabelaResultado =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	 while (tabelaResultado.next()) {
    		 AgendaPessoaHorarioVO obj = new AgendaPessoaHorarioVO();
    		 obj.getAgendaPessoa().getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
    		 obj.getAgendaPessoa().getPessoa().setEmail(tabelaResultado.getString("email"));
    		 obj.getAgendaPessoa().getPessoa().setNome(tabelaResultado.getString("nome"));
    		 obj.getAgendaPessoa().setQtdContatosPendentesNaoFinalizados(tabelaResultado.getInt("count"));
    		 obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("campanha.unidadeensino"));
    		 listaRetorno.add(obj);
    	 }
    	 return listaRetorno;
    }
    
    /**
     * Método responsável por atualizar algumas informações sobre o dia de compromissos do consultor.
     * Sao informacoes como quantas agendas o mesmo possui, quantas sao novas, quantas sao reagendas,
     * quantas estao pendentes e quantas já foram finalizadas. Estas informacoes ficam mantidas em
     * agendaPessoaHorarioVO para que possa ser apresenta ao usuario final.
     * @author Edigar A. Diniz Junior - 23 de mai de 2016 
     * @param agendaPessoaHorarioVO
     */
    public void atualizarEstatisticasCompromissosAgendaPessoaHorario(AgendaPessoaHorarioVO agendaPessoaHorarioVO) {
    	agendaPessoaHorarioVO.setQuantidadeTotalAgendas(agendaPessoaHorarioVO.getListaCompromissoAgendaPessoaHorarioVOs().size());
        int quantidadeNovasAgendas = 0;
        int quantidadeNovasAgendasRealizadas = 0;
        int quantidadeNovasAgendasRealizadasComInsucesso = 0;
        int quantidadeNovasAgendasPendentes = 0;
        int quantidadeReagendas = 0;
        int quantidadeReagendasRealizadas = 0;
        int quantidadeReagendasRealizadasComInsucesso = 0;
        int quantidadeReagendasPendentes = 0;
        int quantidadeCancelados = 0;
        for (CompromissoAgendaPessoaHorarioVO compromisso : agendaPessoaHorarioVO.getListaCompromissoAgendaPessoaHorarioVOs()) {
        	compromisso.setReagendado(verificarCompromissoFoiReagendado(agendaPessoaHorarioVO.getDia() , agendaPessoaHorarioVO.getMes() , agendaPessoaHorarioVO.getAno() , compromisso.getCodigo()));
        	if (compromisso.getReagendado()) {
    			quantidadeReagendas++;
    			if (compromisso.getCompromissoRealizado()) {
    		        quantidadeReagendasRealizadas++;
    			} else {
    				if (compromisso.getCompromissoRealizadoComInsucessoContato()) {
    					quantidadeReagendasRealizadasComInsucesso++;
    				} else {
    					quantidadeReagendasPendentes++;
    				}
    	        }
    		} else {
    			quantidadeNovasAgendas++;
    			if (compromisso.getCompromissoParalizado()) {
    				quantidadeNovasAgendasRealizadas++;
    			} else {
    				if (compromisso.getCompromissoRealizadoComInsucessoContato()) {
    					quantidadeReagendasRealizadasComInsucesso++;
    				} else {
    					quantidadeNovasAgendasPendentes++;
    				}
    	        }
    		}
    	}
    	agendaPessoaHorarioVO.setQuantidadeNovasAgendas(quantidadeNovasAgendas);
    	agendaPessoaHorarioVO.setQuantidadeNovasAgendasRealizadas(quantidadeNovasAgendasRealizadas);
    	agendaPessoaHorarioVO.setQuantidadeNovasAgendasRealizadasComInsucesso(quantidadeNovasAgendasRealizadasComInsucesso);
    	agendaPessoaHorarioVO.setQuantidadeNovasAgendasPendentes(quantidadeNovasAgendasPendentes);
    	agendaPessoaHorarioVO.setQuantidadeReagendas(quantidadeReagendas);
    	agendaPessoaHorarioVO.setQuantidadeReagendasRealizadas(quantidadeReagendasRealizadas);
    	agendaPessoaHorarioVO.setQuantidadeReagendasRealizadasComInsucesso(quantidadeReagendasRealizadasComInsucesso);
    	agendaPessoaHorarioVO.setQuantidadeReagendasPendentes(quantidadeReagendasPendentes);
    }
    
    public boolean verificarCompromissoFoiReagendado(int dia , int mes , int ano , int codigoCompromisso) {
    	
    	Integer reagendamentoCompromisso = null;
    	
    	StringBuilder str = new StringBuilder();
    	
    	str.append(" select count(rg.codigo) as quantidade from reagendamentocompromisso rg"); 
    	str.append(" inner join agendapessoahorario ag on rg.agendaPessoaHorario = ag.codigo");
        str.append(" where rg.dataInicioCompromisso  = concat(").append(ano).append(",'-',").append(mes).append(",'-',").append(dia).append(")::date");
        str.append(" and rg.CompromissoAgendaPessoaHorario = '").append(codigoCompromisso).append("'"); 
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        if (tabelaResultado.next()) {
        	reagendamentoCompromisso = tabelaResultado.getInt("quantidade");
        }
        return reagendamentoCompromisso != null && reagendamentoCompromisso != 0;
			
		
    }
    
}
