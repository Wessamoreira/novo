/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CalendarioAgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboAgendaPessoaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.AgendaPessoaInterfaceFacade;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;
import webservice.servicos.objetos.enumeradores.OrigemAgendaAlunoEnum;

/**
 * 
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public class AgendaPessoa extends ControleAcesso implements AgendaPessoaInterfaceFacade {

    protected static String idEntidade;
    private Hashtable agendaPessoaHorarios;

    public AgendaPessoa() throws Exception {
        super();
        setIdEntidade("AgendaPessoa");
        setAgendaPessoaHorarios(new Hashtable(0));
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AgendaPessoaVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>AgendaPessoaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AgendaPessoaVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            /**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AgendaPessoa.incluir(getIdEntidade());
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO AgendaPessoa( pessoa ) VALUES ( ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getAgendaPessoaHorarioFacade().incluirAgendaPessoaHorarios(obj.getCodigo(), obj.getAgendaPessoaHorarioVOs(), usuarioVO);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AgendaPessoaVO</code>. Sempre utiliza a chave primária da classe como atributo para localização
     * do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>AgendaPessoaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AgendaPessoaVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            /**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AgendaPessoa.alterar(getIdEntidade());
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE AgendaPessoa set pessoa=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);;
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            }));
            getFacadeFactory().getAgendaPessoaHorarioFacade().alterarAgendaPessoaHorarios(obj.getCodigo(), obj.getAgendaPessoaHorarioVOs(), usuarioVO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AgendaPessoaVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     * 
     * @param obj Objeto da classe <code>AgendaPessoaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AgendaPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
        	/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AgendaPessoa.excluir(getIdEntidade());
            String sql = "DELETE FROM AgendaPessoa WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);;
            getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
            getFacadeFactory().getAgendaPessoaHorarioFacade().excluirAgendaPessoaHorarios(obj.getCodigo(), usuarioVO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * 
     * @param AgendaPessoaVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(AgendaPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioVO);
        } else {
            alterar(obj, usuarioVO);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>AgendaPessoaVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste
     * método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public void validarDados(AgendaPessoaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0)) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_AgendaPessoa_pessoa"));
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>AgendaPessoaVO</code>.
     */
    public void validarUnicidade(List<AgendaPessoaVO> lista, AgendaPessoaVO obj) throws ConsistirException {
        for (AgendaPessoaVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(AgendaPessoaVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela AgendaPessoaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<AgendaPessoaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
		if (campoConsulta.equals(TipoConsultaComboAgendaPessoaEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaEnum.NOME_PESSOA.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorNomePessoa(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals(TipoConsultaComboAgendaPessoaEnum.NOME_UNIDADE_ENSINO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorNomeUnidadeEnsino(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>AgendaPessoa</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>AgendaPessoaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        valorConsulta += "%";
        String sqlStr = "SELECT AgendaPessoa.* FROM AgendaPessoa, UnidadeEnsino WHERE AgendaPessoa.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like(?) ORDER BY UnidadeEnsino.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado);
    }

    public AgendaPessoaVO consultarPorCodigoPessoa(Integer codigoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);

        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" agendapessoa.codigo as agendapessoa_codigo, ");
        str.append(" agendapessoa.pessoa as agendapessoa_pessoa ");
        str.append(" FROM AgendaPessoa ");
        str.append(" WHERE AgendaPessoa.pessoa = ").append(codigoPessoa);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuarioLogado);
        }
        return new AgendaPessoaVO();
    }

    /**
     * Responsável por realizar uma consulta de <code>AgendaPessoa</code> através do valor do atributo <code>nome</code> da classe <code>Pessoa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>AgendaPessoaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        valorConsulta += "%";
        String sqlStr = "SELECT AgendaPessoa.* FROM AgendaPessoa, Pessoa WHERE AgendaPessoa.pessoa = Pessoa.codigo and upper( Pessoa.nome ) like(?) ORDER BY Pessoa.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>AgendaPessoa</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AgendaPessoaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM AgendaPessoa WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que
     * realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>AgendaPessoaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>AgendaPessoaVO</code>.
     * 
     * @return O objeto da classe <code>AgendaPessoaVO</code> com os dados devidamente montados.
     */
    public static AgendaPessoaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        AgendaPessoaVO obj = new AgendaPessoaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("agendapessoa_codigo")));
        /* Dados de pessoa */
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("agendapessoa_pessoa")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.getPessoa().setNome((dadosSQL.getString("pessoa_nome")));
        obj.getPessoa().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("arquivo_pastaBaseArquivo"));
        obj.getPessoa().getArquivoImagem().setCodigo(dadosSQL.getInt("arquivo_codigo"));
        obj.getPessoa().getArquivoImagem().setNome(dadosSQL.getString("arquivo_nome"));
        obj.setQtdContatosPendentesNaoFinalizados(dadosSQL.getInt("qtdContatosPendentesNaoFinalizados"));
        obj.setQtdContatosPendentesNaoIniciados(dadosSQL.getInt("qtdContatosPendentesNaoIniciados"));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setAgendaPessoaHorarioVOs(AgendaPessoaHorario.consultarAgendaPessoaHorarios(obj.getCodigo(), nivelMontarDados));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosPessoa(obj, nivelMontarDados, usuarioLogado);
        return obj;
    }

    private StringBuilder getSQLPadraoConsultaRapida() {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" agendapessoa.codigo as agendapessoa_codigo, ");
        str.append(" agendapessoa.pessoa as agendapessoa_pessoa, ");
        str.append(" pessoa.nome as pessoa_nome, ");
        str.append(" arquivo.codigo as arquivo_codigo, ");
        str.append(" arquivo.pastaBaseArquivo as arquivo_pastaBaseArquivo, ");
        str.append(" arquivo.nome as arquivo_nome, ");
        str.append(" (select count (1) from compromissoagendapessoahorario as cap ");
        str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo");
        str.append(" where cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
        str.append(" AND aph.dia < ").append(Uteis.getDiaMesData(new Date()));
        str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
        str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
        str.append(" ) as qtdContatosPendentesNaoIniciados, ");
        str.append(" (select count (1) from compromissoagendapessoahorario as cap  ");
        str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo ");
        str.append(" where cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("'");
        str.append(" AND aph.dia <= ").append(Uteis.getDiaMesData(new Date()));
        str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
        str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
        str.append(" ) as qtdContatosPendentesNaoFinalizados ");
        str.append(" from agendapessoa   ");
        str.append(" inner join pessoa on agendapessoa.pessoa = pessoa.codigo ");
        str.append(" left join arquivo on arquivo.codigo = pessoa.arquivoimagem");
        return str;
    }

    public void executarAtualizacaoContatosPendentes(AgendaPessoaVO agendaPessoa, Integer codigoResponsavel, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        controlarAcesso = false;
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        StringBuilder str = new StringBuilder();
        str.append(" SELECT ");
        str.append(" (select count (1) from compromissoagendapessoahorario as cap ");
        str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo");
        str.append(" inner join prospects ON prospects.codigo = cap.prospect and prospects.inativo = false ");
        str.append(" left join campanha ON campanha.codigo = cap.campanha  ");
        str.append(" where cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
        str.append(" AND aph.dia < ").append(Uteis.getDiaMesData(new Date()));
        str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
        str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
        if(unidadeEnsino != null && unidadeEnsino > 0){        	
        	str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
        	str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
        	str.append(" or prospects.unidadeensino is null )");
        }
        str.append(" ) as qtdContatosPendentesNaoIniciados, ");
        str.append(" (select count (1) from compromissoagendapessoahorario as cap  ");
        str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo ");
        str.append(" inner join prospects ON prospects.codigo = cap.prospect and prospects.inativo = false ");
        str.append(" left join campanha ON campanha.codigo = cap.campanha  ");
        str.append(" where cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("'");
        str.append(" AND aph.dia <= ").append(Uteis.getDiaMesData(new Date()));
        str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
        str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
        if(unidadeEnsino != null && unidadeEnsino > 0){
        	str.append(" and (prospects.unidadeensino = ").append(unidadeEnsino);
        	str.append(" or (campanha.codigo is not null and campanha.unidadeensino = ").append(unidadeEnsino).append(") ");
        	str.append(" or prospects.unidadeensino is null )");
        }
        str.append(" ) as qtdContatosPendentesNaoFinalizados ");
        str.append(" from agendapessoa  ");        
        str.append(" where  agendapessoa.pessoa = ").append(codigoResponsavel);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        if (tabelaResultado.next()) {
            agendaPessoa.setQtdContatosPendentesNaoFinalizados(tabelaResultado.getInt("qtdContatosPendentesNaoFinalizados"));
            agendaPessoa.setQtdContatosPendentesNaoIniciados(tabelaResultado.getInt("qtdContatosPendentesNaoIniciados"));
        }
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>AgendaPessoaHorarioVO</code> ao List <code>agendaPessoaHorarioVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>AgendaPessoaHorario</code> - getDia() - como identificador (key) do objeto no List.
     * 
     * @param obj Objeto da classe <code>AgendaPessoaHorarioVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjAgendaPessoaHorarioVOs(AgendaPessoaVO objAgendaPessoaVO, AgendaPessoaHorarioVO obj) throws Exception {
        getFacadeFactory().getAgendaPessoaHorarioFacade().validarDados(obj);
        obj.setAgendaPessoa(objAgendaPessoaVO);
        int index = 0;
        for (AgendaPessoaHorarioVO objExistente : objAgendaPessoaVO.getAgendaPessoaHorarioVOs()) {
            if (objExistente.getDia().equals(obj.getDia())) {
                objAgendaPessoaVO.getAgendaPessoaHorarioVOs().set(index, obj);
                return;
            }
            index++;
        }
        objAgendaPessoaVO.getAgendaPessoaHorarioVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>AgendaPessoaHorarioVO</code> no List <code>agendaPessoaHorarioVOs</code>. Utiliza o atributo padrão de consulta da
     * classe <code>AgendaPessoaHorario</code> - getDia() - como identificador (key) do objeto no List.
     * 
     * @param dia Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjAgendaPessoaHorarioVOs(AgendaPessoaVO objAgendaPessoaVO, Integer dia) throws Exception {
        int index = 0;
        for (AgendaPessoaHorarioVO objExistente : objAgendaPessoaVO.getAgendaPessoaHorarioVOs()) {
            if (objExistente.getDia().equals(dia)) {
                objAgendaPessoaVO.getAgendaPessoaHorarioVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>AgendaPessoaHorarioVO</code> no List <code>agendaPessoaHorarioVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>AgendaPessoaHorario</code> - getDia() - como identificador (key) do objeto no List.
     * 
     * @param dia Parâmetro para localizar o objeto do List.
     */
    public AgendaPessoaHorarioVO consultarObjAgendaPessoaHorarioVO(AgendaPessoaVO objAgendaPessoaVO, Integer dia) throws Exception {
        for (AgendaPessoaHorarioVO objExistente : objAgendaPessoaVO.getAgendaPessoaHorarioVOs()) {
            if (objExistente.getDia().equals(dia)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>AgendaPessoaVO</code>. Faz uso da chave primária da classe
     * <code>PessoaVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPessoa(AgendaPessoaVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuarioLogado));
    }

    /**
     * Operação responsável por adicionar um objeto da <code>AgendaPessoaHorarioVO</code> no Hashtable <code>AgendaPessoaHorarios</code>. Neste Hashtable são mantidos todos os
     * objetos de AgendaPessoaHorario de uma determinada AgendaPessoa.
     * 
     * @param obj Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjAgendaPessoaHorarios(AgendaPessoaHorarioVO obj) throws Exception {
        getAgendaPessoaHorarios().put(obj.getDia() + "", obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por remover um objeto da classe <code>AgendaPessoaHorarioVO</code> do Hashtable <code>AgendaPessoaHorarios</code>. Neste Hashtable são mantidos todos os
     * objetos de AgendaPessoaHorario de uma determinada AgendaPessoa.
     * 
     * @param Dia Atributo da classe <code>AgendaPessoaHorarioVO</code> utilizado como apelido (key) no Hashtable.
     */
    public void excluirObjAgendaPessoaHorarios(Integer Dia) throws Exception {
        getAgendaPessoaHorarios().remove(Dia + "");
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AgendaPessoaVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public AgendaPessoaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM AgendaPessoa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
    }

    public List<AgendaPessoaVO> consultarUnicidade(AgendaPessoaVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
        return new ArrayList(0);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public AgendaPessoaVO realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaRapida().append(" WHERE AgendaPessoa.pessoa = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { pessoa.getCodigo() });
        if (!tabelaResultado.next()) {
            return new AgendaPessoaVO();
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
    }

    public void executarCriacaoAgendaPessoaHorarioDoDia(AgendaPessoaVO agendaPessoa, Date data, UsuarioVO usuarioVO) throws Exception {
        AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(data), Uteis.getMesData(data), Uteis.getAnoData(data), Uteis.getDiaSemanaEnum(data), true);
        agendaPessoaHorarioVO.setAgendaPessoa(agendaPessoa);
        agendaPessoa.getAgendaPessoaHorarioVOs().add(agendaPessoaHorarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO compromisso, AgendaPessoaVO agendaPessoa, Boolean isAdicionar, UsuarioVO usuarioVO) throws Exception {

        for (AgendaPessoaHorarioVO agendaHorario : agendaPessoa.getAgendaPessoaHorarioVOs()) {
            if (agendaHorario.getDia().equals(Uteis.getDiaMesData(compromisso.getDataCompromisso()))
                    && agendaHorario.getMes().equals(Uteis.getMesData(compromisso.getDataCompromisso()))
                    && agendaHorario.getAno().equals(Uteis.getAnoData(compromisso.getDataCompromisso()))) {
                if (isAdicionar) {
                    compromisso.setAgendaPessoaHorario(agendaHorario);
                    CompromissoAgendaPessoaHorarioVO.validarDados(compromisso);
                    compromisso.setDataInicialCompromisso(compromisso.getDataCompromisso());
                    compromisso.reagendarCompromissoParaDataFutura(compromisso.getDataCompromisso(), usuarioVO);
                    adicionarCompromissoAgendaPessoaHorario(agendaHorario, compromisso);
                    getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(compromisso, usuarioVO);
                    if (agendaPessoa.getIsVisaoAgendaMes() && compromisso.getIsTipoCompromissoContato()) {
                        agendaHorario.setQuantidadeContatos(agendaHorario.getQuantidadeContatos() + 1);
                    } else if (agendaPessoa.getIsVisaoAgendaMes() && compromisso.getIsTipoCompromissoTarefa()) {
                        agendaHorario.setQuantidadeTarefa(agendaHorario.getQuantidadeTarefa() + 1);
                    }
                    getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().realizarAlteracaoConsultorAgendaCampanhaPublicoAlvoProspect(compromisso, usuarioVO);
                } else {
                    removerCompromissoAgendaPessoaHorario(compromisso, agendaHorario);
                }
                return;
            }
        }
        if (isAdicionar) {
            adicionarCompromissoAgendaPessoaHorarioRealizandoValidacaoSeExisteAgendaHorario(compromisso, agendaPessoa, usuarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarCompromissoAgendaPessoaHorarioRealizandoValidacaoSeExisteAgendaHorario(CompromissoAgendaPessoaHorarioVO compromisso, AgendaPessoaVO agendaPessoa, UsuarioVO usuarioVO) throws Exception {
        AgendaPessoaHorarioVO agendaHorario = null;
        agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(0, agendaPessoa.getPessoa().getCodigo(), 0, compromisso.getDataCompromisso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuarioVO);
        if (agendaHorario.getNovoObj()) {
            agendaHorario = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(compromisso.getDataCompromisso()), Uteis.getMesData(compromisso.getDataCompromisso()), Uteis.getAnoData(compromisso.getDataCompromisso()), Uteis.getDiaSemanaEnum(compromisso.getDataCompromisso()), true);
            agendaHorario.setAgendaPessoa(agendaPessoa);
        }

        agendaHorario.setAgendaPessoa(agendaPessoa);
        compromisso.setAgendaPessoaHorario(agendaHorario);
        if(!Uteis.isAtributoPreenchido(compromisso.getDataInicialCompromisso())){
        	compromisso.setDataInicialCompromisso(compromisso.getDataCompromisso());
        }
        compromisso.reagendarCompromissoParaDataFutura(compromisso.getDataCompromisso(), usuarioVO);
        CompromissoAgendaPessoaHorarioVO.validarDados(compromisso);
        getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(compromisso, usuarioVO);
        getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().realizarAlteracaoConsultorAgendaCampanhaPublicoAlvoProspect(compromisso, usuarioVO);
        agendaHorario = null;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarCompromissoAgendaPessoaHorario(AgendaPessoaHorarioVO agendaHorario, CompromissoAgendaPessoaHorarioVO compromisso) throws Exception {
        try {
            int index = 0;
            for (CompromissoAgendaPessoaHorarioVO compromissoExistente : agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs()) {
                if (compromissoExistente.getHora().equals(compromisso.getHora())) {
                    agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs().set(index, compromisso);
                    return;
                }
                index++;
            }
            agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs().add(compromisso);
        } finally {
            Ordenacao.ordenarLista(agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs(), "hora");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO compromisso, AgendaPessoaHorarioVO agendaHorario) throws Exception {
        for (CompromissoAgendaPessoaHorarioVO compromissoExistente : agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs()) {
            if (compromissoExistente.getHora().equals(compromisso.getHora())) {
                agendaHorario.getListaCompromissoAgendaPessoaHorarioVOs().remove(compromisso);
                return;
            }
        }
    }

    public void realizarGeracaoCalendarioMes(Boolean administrador, AgendaPessoaVO agendaPessoa, Integer codigoResponsavel, Integer codigoCampanha, Integer unidadeEnsino, Date filtroMes, UsuarioVO usuarioVO) throws Exception {
        List<AgendaPessoaHorarioVO> listaTemporariaAgendaHorario = new ArrayList<AgendaPessoaHorarioVO>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(filtroMes);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        if (!administrador) {
            agendaPessoa.setAgendaPessoaHorarioVOs(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorMesPorAnoPorAgendaPessoaEspecificaDoMes(agendaPessoa.getPessoa().getCodigo(), codigoCampanha, unidadeEnsino, cal.getTime(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        } else {
            agendaPessoa.setAgendaPessoaHorarioVOs(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorMesPorAnoPorAgendaPessoaEspecificaDoMes(codigoResponsavel, codigoCampanha, unidadeEnsino, cal.getTime(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        }
        int ultimoDiaMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if (diaSemana != 1) {
            for (int i = 1; i < diaSemana; i++) {
                Date dataRetroativa = Uteis.obterDataAntiga(cal.getTime(), i);
                realizarGeracaoMesPorData(listaTemporariaAgendaHorario, agendaPessoa, dataRetroativa, false);
            }
        }
        for (int i = 0; i < ultimoDiaMes; i++) {
            Date dataAtual = Uteis.obterDataAvancada(cal.getTime(), i);
            boolean existeData = false;
            for (int k = 0; k < agendaPessoa.getAgendaPessoaHorarioVOs().size(); k++) {
                AgendaPessoaHorarioVO agendaHorario = agendaPessoa.getAgendaPessoaHorarioVOs().get(k);
                if (agendaHorario.getDia().equals(Uteis.getDiaMesData(dataAtual))
                        && agendaHorario.getMes().equals(Uteis.getMesData(dataAtual))
                        && agendaHorario.getAno().equals(Uteis.getAnoData(dataAtual))) {
                    existeData = true;
                    break;
                }
            }
            if (!existeData) {
                realizarGeracaoMesPorData(listaTemporariaAgendaHorario, agendaPessoa, dataAtual, true);
            }
        }
        agendaPessoa.getAgendaPessoaHorarioVOs().addAll(listaTemporariaAgendaHorario);
        listaTemporariaAgendaHorario = null;
    }
    
    public void realizarGeracaoMesPorData(List<AgendaPessoaHorarioVO> listaTemporariaAgendaHorario, AgendaPessoaVO agendaPessoa, Date data, Boolean ativo) {
        AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(data), Uteis.getMesData(data), Uteis.getAnoData(data), Uteis.getDiaSemanaEnum(data), ativo);
        agendaPessoaHorarioVO.setAgendaPessoa(agendaPessoa);
        listaTemporariaAgendaHorario.add(agendaPessoaHorarioVO);
    }

    public void executarCriacaoCompromissoPorColaborador(AgendaPessoaVO agenda) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Hashtable getAgendaPessoaHorarios() {
        if (agendaPessoaHorarios == null) {
            agendaPessoaHorarios = new Hashtable(0);
        }
        return (agendaPessoaHorarios);
    }

    public void setAgendaPessoaHorarios(Hashtable agendaPessoaHorarios) {
        this.agendaPessoaHorarios = agendaPessoaHorarios;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AgendaPessoa.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AgendaPessoa.idEntidade = idEntidade;
    }

    @Override
    public CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> realizarGeracaoCalendarioAgendaPessoa(Integer pessoa, Integer unidadeEnsino, Boolean visualizaAgendaGeral, MesAnoEnum mesAno, Integer ano, TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum) throws Exception {

        CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> calendarioAgendaPessoa = new CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO>();
        calendarioAgendaPessoa.setAno(ano.toString());
        calendarioAgendaPessoa.setMesAno(mesAno);
        Map<Integer, CalendarioAgendaPessoaVO> calendarioAgendaPessoaVOs = null;
        if (visualizaAgendaGeral) {
        	calendarioAgendaPessoaVOs = consultarDadosGeracaoCalendario(0, unidadeEnsino, mesAno, ano, tipoSituacaoCompromissoEnum);
        } else {
        	calendarioAgendaPessoaVOs = consultarDadosGeracaoCalendario(pessoa, 0, mesAno, ano, tipoSituacaoCompromissoEnum);        	
        }
        Date data = Uteis.getData("01/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy");
        Integer ultimoDia = Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(data));
        String dataComp = "";
        CalendarioAgendaPessoaVO calendarioAgendaPessoaVO = null;
        DiaSemana diaSemanaInicial = Uteis.getDiaSemanaEnum(data);
        DiaSemana diaSemanaFinal = Uteis.getDiaSemanaEnum(Uteis.getDataUltimoDiaMes(data));
        /*
         * adiciona dias anteriores
         */
        switch (diaSemanaInicial) {
            case DOMINGO:
                break;
            case SEGUNGA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case TERCA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 2), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case QUARTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 2), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 3), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case QUINTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 2), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 3), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 4), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;                
            case SEXTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 2), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 3), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 4), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 5), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case SABADO:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 1), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 2), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 3), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 4), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 5), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAntiga(data, 6), DiaSemana.DOMINGO, pessoa, "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            default:
                break;
        }
        for (int x = 1; x <= ultimoDia; x++) {         
            if (calendarioAgendaPessoaVOs.containsKey(x)) {
                calendarioAgendaPessoaVO = calendarioAgendaPessoaVOs.get(x);
                data = calendarioAgendaPessoaVO.getDataCompromisso();
            } else {
                calendarioAgendaPessoaVO = new CalendarioAgendaPessoaVO();
                if (x <= 9) {
                    dataComp = "0" + x + "/" + mesAno.getKey() + "/" + ano;
                } else {
                    dataComp = x + "/" + mesAno.getKey() + "/" + ano;
                }
                data = Uteis.getData(dataComp, "dd/MM/yyyy");
                calendarioAgendaPessoaVO.setConsultor(pessoa);
                calendarioAgendaPessoaVO.setDataCompromisso(data);
                calendarioAgendaPessoaVO.setDiaSemana(Uteis.getDiaSemanaEnum(data));
            }
            calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(calendarioAgendaPessoaVO, calendarioAgendaPessoaVO.getDiaSemana());
        }
        /*
         * adiciona dias posteriores
         */
        switch (diaSemanaFinal) {
            case DOMINGO:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.SEGUNGA, pessoa, "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 2), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 3), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 4), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 5), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 6), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case SEGUNGA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.TERCA, pessoa, "colunaOutroMes"), DiaSemana.TERCA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 2), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 3), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 4), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 5), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case TERCA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.QUARTA, pessoa, "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 2), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 3), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 4), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case QUARTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.QUINTA, pessoa, "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 2), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 3), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case QUINTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.SEXTA, pessoa, "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 2), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;                
            case SEXTA:
                calendarioAgendaPessoa.adicionarItemListaCalendarioPorDiaSemana(new CalendarioAgendaPessoaVO(Uteis.obterDataAvancada(data, 1), DiaSemana.SABADO, pessoa, "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case SABADO:                
                break;
            default:
                break;
        }
        return calendarioAgendaPessoa;
    }
    
    public Map<Integer, CalendarioAgendaPessoaVO> consultarDadosGeracaoCalendario(Integer pessoa, Integer unidadeEnsino, MesAnoEnum mesAno, Integer ano, TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum) throws Exception {
        Map<Integer, CalendarioAgendaPessoaVO> calendarioAgendaPessoaVOs = new HashMap<Integer, CalendarioAgendaPessoaVO>(0);
        Date dataInicio = Uteis.getData("01/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy");
        Date dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
        StringBuilder sql = new StringBuilder("select consultor, diasemana, datacompromisso, extract(day from datacompromisso)::INT as dia, sum(qtdeCompromissoNaoRealizado) as qtdeCompromissoNaoRealizado, sum(qtdeCompromissoARealizar) as qtdeCompromissoARealizar from ( ");
        sql.append(" select agendapessoa.pessoa as consultor, diasemanaenum as diasemana,  compromissoagendapessoahorario.datacompromisso::DATE as datacompromisso, ");
        sql.append(" case when tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and datacompromisso::DATE < current_date then count(compromissoagendapessoahorario.codigo) else  0 end as qtdeCompromissoNaoRealizado, ");
        sql.append(" case when tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and datacompromisso::DATE >= current_date then count(compromissoagendapessoahorario.codigo) else 0 end as qtdeCompromissoARealizar ");
        sql.append(" from compromissoagendapessoahorario ");
        sql.append(" inner join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");        
        sql.append(" inner join agendapessoa on agendapessoahorario.agendapessoa = agendapessoa.codigo ");        
        sql.append(" inner join prospects on prospects.codigo = compromissoagendapessoahorario.prospect and prospects.inativo = false ");        
        sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datacompromisso", true));
        if (tipoSituacaoCompromissoEnum != null) {
            sql.append(" and tiposituacaocompromissoenum = '").append(tipoSituacaoCompromissoEnum.name()).append("' ");
        }
        if (pessoa != null && pessoa > 0) {
            sql.append(" and agendapessoa.pessoa =").append(pessoa);
        }
        if (unidadeEnsino != null && unidadeEnsino > 0) {
        	sql.append(" and prospects.unidadeEnsino =").append(unidadeEnsino);
        }
        sql.append(" group by agendapessoa.pessoa, diasemanaenum, compromissoagendapessoahorario.datacompromisso::DATE, tiposituacaocompromissoenum)");
        sql.append(" as t group by consultor, diasemana, datacompromisso order by datacompromisso");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        CalendarioAgendaPessoaVO calendarioAgendaPessoaVO = null;
        while (rs.next()) {
            calendarioAgendaPessoaVO = new CalendarioAgendaPessoaVO();
            calendarioAgendaPessoaVO.setConsultor(rs.getInt("consultor"));
            calendarioAgendaPessoaVO.setDataCompromisso(rs.getDate("datacompromisso"));
            calendarioAgendaPessoaVO.setDiaSemana(Uteis.getDiaSemanaEnum(rs.getDate("datacompromisso")));
            calendarioAgendaPessoaVO.setQtdeCompromissoARealizar(rs.getInt("qtdeCompromissoARealizar"));
            if(UteisData.getCompararDatas(calendarioAgendaPessoaVO.getDataCompromisso(), new Date())){
            	calendarioAgendaPessoaVO.setStyleClass("bg-info text-white");
            }else if(calendarioAgendaPessoaVO.getQtdeCompromissoARealizar()>0){
                calendarioAgendaPessoaVO.setStyleClass("bg-gray text-dark");               
            }
            
            calendarioAgendaPessoaVO.setQtdeCompromissoNaoRealizado(rs.getInt("qtdeCompromissoNaoRealizado"));
            if(calendarioAgendaPessoaVO.getQtdeCompromissoNaoRealizado() > 0){
                calendarioAgendaPessoaVO.setStyleClass("bg-danger  text-white");
            }
            
            calendarioAgendaPessoaVOs.put(rs.getInt("dia"), calendarioAgendaPessoaVO);
        }

        return calendarioAgendaPessoaVOs;
    }
    
    public void atualizarEstatisticasCompromissosAgendaPessoa(AgendaPessoaVO agendaPessoa) {
    	atualizarEstatisticasCompromissosAgendaPessoa(agendaPessoa, true);
    }
    
    public void atualizarEstatisticasCompromissosAgendaPessoa(AgendaPessoaVO agendaPessoa, boolean forcarAtualizarAgendaPessoaHorario) {
    	agendaPessoa.setQuantidadeTotalAgendas(0);
    	agendaPessoa.setQuantidadeNovasAgendas(0);
    	agendaPessoa.setQuantidadeNovasAgendasRealizadas(0);
    	agendaPessoa.setQuantidadeNovasAgendasRealizadasComInsucesso(0);
    	agendaPessoa.setQuantidadeNovasAgendasPendentes(0);
    	agendaPessoa.setQuantidadeReagendas(0);
    	agendaPessoa.setQuantidadeReagendasRealizadas(0);
    	agendaPessoa.setQuantidadeReagendasRealizadasComInsucesso(0);
    	agendaPessoa.setQuantidadeReagendasPendentes(0);
    	for (AgendaPessoaHorarioVO agendaPessoaHorarioVO : agendaPessoa.getAgendaPessoaHorarioVOs()) {
    		if (forcarAtualizarAgendaPessoaHorario) {
    			getFacadeFactory().getAgendaPessoaHorarioFacade().atualizarEstatisticasCompromissosAgendaPessoaHorario(agendaPessoaHorarioVO);
    		}
        	agendaPessoa.setQuantidadeTotalAgendas(agendaPessoa.getQuantidadeTotalAgendas() + agendaPessoaHorarioVO.getQuantidadeTotalAgendas());
        	agendaPessoa.setQuantidadeNovasAgendas(agendaPessoa.getQuantidadeNovasAgendas() + agendaPessoaHorarioVO.getQuantidadeNovasAgendas());
        	agendaPessoa.setQuantidadeNovasAgendasRealizadas(agendaPessoa.getQuantidadeNovasAgendasRealizadas() + agendaPessoaHorarioVO.getQuantidadeNovasAgendasRealizadas());
        	agendaPessoa.setQuantidadeNovasAgendasRealizadasComInsucesso(agendaPessoa.getQuantidadeNovasAgendasRealizadasComInsucesso() + agendaPessoaHorarioVO.getQuantidadeNovasAgendasRealizadasComInsucesso());
        	agendaPessoa.setQuantidadeNovasAgendasPendentes(agendaPessoa.getQuantidadeNovasAgendasPendentes() + agendaPessoaHorarioVO.getQuantidadeNovasAgendasPendentes());
        	agendaPessoa.setQuantidadeReagendas(agendaPessoa.getQuantidadeReagendas() + agendaPessoaHorarioVO.getQuantidadeReagendas());
        	agendaPessoa.setQuantidadeReagendasRealizadas(agendaPessoa.getQuantidadeReagendasRealizadas() + agendaPessoaHorarioVO.getQuantidadeReagendasRealizadas());
        	agendaPessoa.setQuantidadeReagendasRealizadasComInsucesso(agendaPessoa.getQuantidadeReagendasRealizadasComInsucesso() + agendaPessoaHorarioVO.getQuantidadeReagendasRealizadasComInsucesso());
        	agendaPessoa.setQuantidadeReagendasPendentes(agendaPessoa.getQuantidadeReagendasPendentes() + agendaPessoaHorarioVO.getQuantidadeReagendasPendentes());
    	}
    }
    
}
