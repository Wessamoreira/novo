package negocio.facade.jdbc.planoorcamentario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see SolicitacaoOrcamentoPlanoOrcamentarioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class SolicitacaoOrcamentoPlanoOrcamentario extends ControleAcesso implements SolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade {

	private static final long serialVersionUID = 8663954465337329288L;

	protected static String idEntidade;

    public SolicitacaoOrcamentoPlanoOrcamentario() throws Exception {
        super();
        setIdEntidade("SolicitacaoOrcamentoPlanoOrcamentario");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     */
    public SolicitacaoOrcamentoPlanoOrcamentarioVO novo() throws Exception {
        SolicitacaoOrcamentoPlanoOrcamentario.incluir(getIdEntidade());
        SolicitacaoOrcamentoPlanoOrcamentarioVO obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        try {
            SolicitacaoOrcamentoPlanoOrcamentarioVO.validarDados(obj);
            SolicitacaoOrcamentoPlanoOrcamentario.incluir(getIdEntidade(), true, usuario);
            validarDadosUnicidade(obj.getCodigo(), obj.getPlanoOrcamentario().getCodigo(), obj.getDepartamento().getCodigo(), obj.getUnidadeEnsino().getCodigo());

            incluir(obj, "SolicitacaoOrcamentoPlanoOrcamentario",
					new AtributoPersistencia()
							.add("pessoa", obj.getPessoa())
							.add("valorTotalSolicitado", obj.getValorTotalSolicitado())
							.add("situacao", obj.getSituacao().getValor())
							.add("departamento", obj.getDepartamento())
							.add("planoOrcamentario", obj.getPlanoOrcamentario())
							.add("unidadeensino", obj.getUnidadeEnsino())
							.add("valorTotalAprovado", obj.getValorTotalAprovado())
							, usuario);

            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().incluirItemSolicitacaoOrcamentoPlanoOrcamentarios(obj.getCodigo(), obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(), usuario);
        } catch (Exception e) {
             obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        try {
            SolicitacaoOrcamentoPlanoOrcamentarioVO.validarDados(obj);
            validarDadosUnicidade(obj.getCodigo(), obj.getPlanoOrcamentario().getCodigo(), obj.getDepartamento().getCodigo(), obj.getUnidadeEnsino().getCodigo());
            SolicitacaoOrcamentoPlanoOrcamentario.alterar(getIdEntidade(), true, usuario);

            alterar(obj, "SolicitacaoOrcamentoPlanoOrcamentario",
					new AtributoPersistencia()
							.add("pessoa", obj.getPessoa())
							.add("valorTotalSolicitado", obj.getValorTotalSolicitado())
							.add("situacao", obj.getSituacao().getValor())
							.add("departamento", obj.getDepartamento())
							.add("planoOrcamentario", obj.getPlanoOrcamentario())
							.add("unidadeensino", obj.getUnidadeEnsino())
							.add("valorTotalAprovado", obj.getValorTotalAprovado()),
							 
							new AtributoPersistencia().add("codigo", obj.getCodigo()) , usuario);
            getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarItemSolicitacaoOrcamentoPlanoOrcamentarios(obj.getCodigo(), obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void finalizar(final Integer codigo, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE SolicitacaoOrcamentoPlanoOrcamentario set situacao='FI' WHERE (planoOrcamentario = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, codigo.intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        } 
    }

    public void validarDadosUnicidade(Integer solicitacao, Integer planoOrcamentario, Integer departamento, Integer unidadeEnsino) throws Exception {
        String sql = "";
        if (solicitacao == 0) {
            sql = "SELECT * FROM  solicitacaoOrcamentoPlanoOrcamentario WHERE planoOrcamentario = " + planoOrcamentario.intValue() + " AND departamento = " + departamento.intValue() + " AND unidadeEnsino = " + unidadeEnsino.intValue() + "";
        } else {
            sql = "SELECT * FROM  solicitacaoOrcamentoPlanoOrcamentario WHERE codigo <> " + solicitacao + " AND planoOrcamentario = " + planoOrcamentario.intValue() + " AND departamento = " + departamento.intValue() + " AND unidadeEnsino = " + unidadeEnsino.intValue() + "";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (tabelaResultado.next()) {
            throw new Exception("Já existe uma Solicitação cadastrada para o Plano Orçamentário, Departamento e Unidade de Ensino informado.");
        }

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        try {
            SolicitacaoOrcamentoPlanoOrcamentario.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM SolicitacaoOrcamentoPlanoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>SolicitacaoOrcamentoPlanoOrcamentario</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoOrcamentoPlanoOrcamentario WHERE nome ilike '" + valorConsulta.toUpperCase() + "%' and pessoa = " + usuario.getPessoa().getCodigo() + " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoOrcamentoPlanoOrcamentario WHERE situacao = '" + valorConsulta.toUpperCase() + "' and pessoa = " + usuario.getPessoa().getCodigo() + " ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorPessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoOrcamentoPlanoOrcamentario "
                + " inner join pessoa on pessoa.codigo = solicitacaoOrcamentoPlanoOrcamentario.pessoa "
                + " WHERE pessoa.nome ilike '" + valorConsulta + "%' and pessoa.codigo = " + usuario.getPessoa().getCodigo() + " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>SolicitacaoOrcamentoPlanoOrcamentario</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoOrcamentoPlanoOrcamentario WHERE codigo >= " + valorConsulta.intValue() + " and pessoa = " + usuario.getPessoa().getCodigo() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> resultantes da consulta.
     */
    public static List<SolicitacaoOrcamentoPlanoOrcamentarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<SolicitacaoOrcamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     * @return  O objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code> com os dados devidamente montados.
     */
	public static SolicitacaoOrcamentoPlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SolicitacaoOrcamentoPlanoOrcamentarioVO obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPlanoOrcamentario().setCodigo(new Integer(dadosSQL.getInt("planoOrcamentario")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
        obj.setValorTotalSolicitado(new Double(dadosSQL.getDouble("valorTotalSolicitado")));
        obj.setValorTotalAprovado(new Double(dadosSQL.getDouble("valorTotalAprovado")));
        obj.setValorConsumido(getFacadeFactory().getRequisicaoFacade().consultarValorConsumidoPlanoOrcamentario(null, obj.getCodigo(), null, null, null, null, null, usuario, null));
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("situacao"))) {
        	obj.setSituacao(SituacaoPlanoOrcamentarioEnum.getSituacaoPlanoOrcamentarioEnum(dadosSQL.getString("situacao")));
        }
        obj.setNovoObj(Boolean.FALSE);
        montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosPlanoOrcamentario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(ItemSolicitacaoOrcamentoPlanoOrcamentario.consultarItemSolicitacaoOrcamentoPlanoOrcamentarios(obj.getCodigo(), false, usuario));
        return obj;
    }

    public static void montarDadosDepartamento(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosPessoa(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosPlanoOrcamentario(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoOrcamentario().getCodigo().intValue() == 0) {
            obj.setPlanoOrcamentario(new PlanoOrcamentarioVO());
            return;
        }
        obj.setPlanoOrcamentario(getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorChavePrimaria(obj.getPlanoOrcamentario().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosUnidadeEnsino(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>SolicitacaoOrcamentoPlanoOrcamentarioVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public SolicitacaoOrcamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM SolicitacaoOrcamentoPlanoOrcamentario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( SolicitacaoOrcamentoPlanoOrcamentario ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return SolicitacaoOrcamentoPlanoOrcamentario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        SolicitacaoOrcamentoPlanoOrcamentario.idEntidade = idEntidade;
    }

    public void carregarDados(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((SolicitacaoOrcamentoPlanoOrcamentarioVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((SolicitacaoOrcamentoPlanoOrcamentarioVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((SolicitacaoOrcamentoPlanoOrcamentarioVO) obj, resultado, usuario);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codSolicitacao, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (solicitacao.codigo = " + codSolicitacao + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codSolicitacao, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (solicitacao.codigo = " + codSolicitacao + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("select solicitacao.codigo, solicitacao.situacao, solicitacao.valortotalsolicitado, solicitacao.valorTotalAprovado as \"solicitacao.valorTotalAprovado\", solicitacao.valorConsumido as \"solicitacao.valorConsumido\",  ");
        //Dados da Pessoa
        str.append("pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
        //Dados do PlanoOrçamentario
        str.append("planoOrcamentario.codigo AS \"planoOrcamentario.codigo\", planoOrcamentario.nome AS \"planoOrcamentario.nome\",  planoOrcamentario.dataInicio AS \"planoOrcamentario.dataInicio\",   planoOrcamentario.dataFinal AS \"planoOrcamentario.dataFinal\", ");
        //Dados do Departamento
        str.append("departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\", ");
        //Dados da Unidade de Ensino
        str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\" ");
        
        str.append("from solicitacaoorcamentoplanoorcamentario solicitacao ");
        str.append(" inner join pessoa on pessoa.codigo = solicitacao.pessoa ");
        str.append(" inner join planoOrcamentario on planoOrcamentario.codigo = solicitacao.planoOrcamentario ");
        str.append(" inner join departamento on departamento.codigo = solicitacao.departamento ");
        str.append(" inner join unidadeEnsino on unidadeEnsino.codigo = solicitacao.unidadeensino ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("select solicitacao.codigo, solicitacao.situacao, solicitacao.valortotalsolicitado, solicitacao.valorTotalAprovado as \"solicitacao.valorTotalAprovado\",   ");
        //Dados do PlanoOrçamentario
        str.append("planoOrcamentario.codigo AS \"planoOrcamentario.codigo\", planoOrcamentario.nome AS \"planoOrcamentario.nome\",  planoOrcamentario.dataInicio AS \"planoOrcamentario.dataInicio\",   planoOrcamentario.dataFinal AS \"planoOrcamentario.dataFinal\", ");
        //Dados do Funcionario
        str.append("pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
        //Dados da Unidade de Ensino
        str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\",  ");
        //Dados do Departamento
        str.append("departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\"  ");
        
        str.append(" from solicitacaoorcamentoplanoorcamentario solicitacao ");
        str.append(" inner join planoOrcamentario on planoOrcamentario.codigo = solicitacao.planoOrcamentario ");
        str.append(" inner join pessoa on pessoa.codigo = solicitacao.pessoa ");
        str.append(" inner join departamento on departamento.codigo = solicitacao.departamento ");
        str.append(" inner join unidadeEnsino on unidadeEnsino.codigo = solicitacao.unidadeensino ");
        return str;
    }

    private void montarDadosBasico(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da Solicitacao
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNovoObj(false);
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("situacao"))) {
        	obj.setSituacao(SituacaoPlanoOrcamentarioEnum.getSituacaoPlanoOrcamentarioEnum(dadosSQL.getString("situacao")));
        }
        obj.setValorTotalSolicitado(dadosSQL.getDouble("valorTotalSolicitado"));
        obj.setValorTotalAprovado(new Double(dadosSQL.getDouble("solicitacao.valorTotalAprovado")));
        obj.setValorConsumido(getFacadeFactory().getRequisicaoFacade().consultarValorConsumidoPlanoOrcamentario(null, obj.getCodigo(), null, null, null, null, null, null, null));

        //Dados do PlanoOrcamentario
        obj.getPlanoOrcamentario().setCodigo(dadosSQL.getInt("planoOrcamentario.codigo"));
        obj.getPlanoOrcamentario().setNome(dadosSQL.getString("planoOrcamentario.nome"));
        obj.getPlanoOrcamentario().setDataInicio(dadosSQL.getDate("planoOrcamentario.dataInicio"));
        obj.getPlanoOrcamentario().setDataFinal(dadosSQL.getDate("planoOrcamentario.dataFinal"));

        //Dados do Funcionario
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));

        //Dados Departamento
        obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento.codigo"));
        obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));

        //Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
    }

	private void montarDadosCompleto(SolicitacaoOrcamentoPlanoOrcamentarioVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        // Dados da Solicitacao
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNovoObj(false);
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("situacao"))) {
        	obj.setSituacao(SituacaoPlanoOrcamentarioEnum.getSituacaoPlanoOrcamentarioEnum(dadosSQL.getString("situacao")));
        }
        obj.setValorTotalSolicitado(dadosSQL.getDouble("valorTotalSolicitado"));
        obj.setValorTotalAprovado(new Double(dadosSQL.getDouble("solicitacao.valorTotalAprovado")));
        obj.setValorConsumido(getFacadeFactory().getRequisicaoFacade().consultarValorConsumidoPlanoOrcamentario(null, obj.getCodigo(), null, null, null, null, null, null, null));

        //Dados do PlanoOrcamentario
        obj.getPlanoOrcamentario().setCodigo(dadosSQL.getInt("planoOrcamentario.codigo"));
        obj.getPlanoOrcamentario().setNome(dadosSQL.getString("planoOrcamentario.nome"));
        obj.getPlanoOrcamentario().setDataInicio(dadosSQL.getDate("planoOrcamentario.dataInicio"));
        obj.getPlanoOrcamentario().setDataFinal(dadosSQL.getDate("planoOrcamentario.dataFinal"));

        //Dados do Funcionario
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));

        //Dados Departamento
        obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento.codigo"));
        obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));

        //Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

        obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarItemSolicitacaoOrcamentoPlanoOrcamentario(obj.getCodigo(), false, usuario));
        
        realizarCriacaoDetalhamentoPeriodoGeral(obj);
    }

	@Override
	public void realizarCriacaoDetalhamentoPeriodoGeral(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception {
		solicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs().clear();
		Map<String, DetalhamentoPeriodoOrcamentoVO> mapDetPer =  new HashMap<String, DetalhamentoPeriodoOrcamentoVO>();
		DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO2 = null;

		for(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO : solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			if(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs().isEmpty()) {
				itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarDistribuicaoValorItemMensal(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorBaseUtilizar(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataInicio(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataFinal()));	
			}
			for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
				if(!mapDetPer.containsKey(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes())) {
					detalhamentoPeriodoOrcamentoVO2 = new DetalhamentoPeriodoOrcamentoVO();
					detalhamentoPeriodoOrcamentoVO2.setAno(detalhamentoPeriodoOrcamentoVO.getAno());
					
					detalhamentoPeriodoOrcamentoVO2.setMes(MesAnoEnum.valueOf(String.valueOf(detalhamentoPeriodoOrcamentoVO.getMes())));
					
					detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCategoriaDespesa(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
					mapDetPer.put(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes(), detalhamentoPeriodoOrcamentoVO2);
				}

				detalhamentoPeriodoOrcamentoVO2 = mapDetPer.get(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes());
				detalhamentoPeriodoOrcamentoVO2.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO2.getOrcamentoRequeridoGestor()+detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor());
   			    detalhamentoPeriodoOrcamentoVO2.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVO2.getOrcamentoTotal()+detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal());
				detalhamentoPeriodoOrcamentoVO2.setValorConsumido(detalhamentoPeriodoOrcamentoVO.getValorConsumido());
				detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCategoriaDespesa(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
				if (detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0) {
					detalhamentoPeriodoOrcamentoVO2.getDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs().add((DetalhamentoPeriodoOrcamentoVO)detalhamentoPeriodoOrcamentoVO.clone());
				}
			}
		}

		solicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs().addAll(mapDetPer.values());
		Ordenacao.ordenarLista(solicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs(), "ordenacao");
	}

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorPlanoOrcamentario(Integer planoOrcamentario, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE planoOrcamentario.codigo = ");
        sqlStr.append(planoOrcamentario);
        sqlStr.append(" AND solicitacao.situacao = 'AT' ");

        if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
            sqlStr.append(" and unidadeEnsino.codigo in (");
            int x = 0;
            for (UnidadesPlanoOrcamentarioVO unidadePlanoOrcamentarioVO : unidadesPlanoOrcamentarioVOs) {
                if (x > 0) {
                    sqlStr.append(", ");
                }
                sqlStr.append(unidadePlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
                x++;
            }
            sqlStr.append(") ");
        }

        sqlStr.append(" ORDER BY unidadeEnsino.nome, departamento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

   public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, 
		   boolean permiteConsultarTodos) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE solicitacao.situacao ilike(?) ");
        if (!permiteConsultarTodos) {
        	sqlStr.append(" and pessoa.codigo = ").append(usuario.getPessoa().getCodigo());        	
        }
        sqlStr.append(" ORDER BY solicitacao.situacao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE lower(sem_acentos(departamento.nome)) ilike(sem_acentos(?))");
        if (!permiteConsultarTodos) {
        	sqlStr.append(" and pessoa.codigo = ").append(usuario.getPessoa().getCodigo());        	
        }
        sqlStr.append(" ORDER BY departamento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta.toLowerCase() + PERCENT);
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorNomePlanoOrcamentario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario,
    		boolean permiteConsultarTodos) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE lower(planoOrcamentario.nome) like(sem_acentos(?))");
        if (!permiteConsultarTodos) {
        	sqlStr.append(" and pessoa.codigo = ").append(usuario.getPessoa().getCodigo());        	
        }
        sqlStr.append(" ORDER BY planoOrcamentario.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta.toLowerCase() + PERCENT);
        return montarDadosConsultaRapida(tabelaResultado);
    }
    
    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorFuncionario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos(?))");
        if (!permiteConsultarTodos) {
        	sqlStr.append(" and pessoa.codigo = ").append(usuario.getPessoa().getCodigo());        	
        }
        sqlStr.append(" ORDER BY pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta.toLowerCase() + PERCENT);
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, boolean permiteConsultarTodos) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE solicitacao.codigo = ?");
        if (!permiteConsultarTodos) {
        	sqlStr.append(" and pessoa.codigo = ").append(usuario.getPessoa().getCodigo());        	
        }
        sqlStr.append(" ORDER BY departamento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.intValue());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<SolicitacaoOrcamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<SolicitacaoOrcamentoPlanoOrcamentarioVO>(0);
        SolicitacaoOrcamentoPlanoOrcamentarioVO obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
        while (tabelaResultado.next()) {
            obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
            montarDadosCompleto(obj, tabelaResultado, usuario);
            vetResultado.add(obj);            
        }
        return vetResultado;
    }
    
    public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<SolicitacaoOrcamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<SolicitacaoOrcamentoPlanoOrcamentarioVO>(0);
        while (tabelaResultado.next()) {
            SolicitacaoOrcamentoPlanoOrcamentarioVO obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public SolicitacaoOrcamentoPlanoOrcamentarioVO consultarDados(RequisicaoVO requisicaoVO, DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	List<Object> filtros = new ArrayList<>();
    	sql.append(" select itemsolicitacaoorcamentoplanoorcamentario.codigo, itemsolicitacaoorcamentoplanoorcamentario.valor,");
    	sql.append(" planoorcamentario.codigo as planoorcamentario, itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa, solicitacaoorcamentoplanoorcamentario.departamento as departamento,");
    	sql.append(" solicitacaoorcamentoplanoorcamentario.unidadeensino, sum(requisicaoitem.quantidadeautorizada::numeric(20,2)  * requisicaoitem.valorunitario::numeric(20,2) )::numeric(20,2) as totalConsumido,");
    	sql.append(" itemsolicitacaoorcamentoplanoorcamentario.valor - coalesce (sum(requisicaoitem.quantidadeautorizada::numeric(20,2)  * requisicaoitem.valorunitario::numeric(20,2) )::numeric(20,2), 0.0) saldoRestante,");
    	sql.append(" array_to_string(array_agg(distinct requisicao.codigo), ';') as requisicoes,");
    	sql.append(" array_to_string(array_agg(distinct requisicaoitem.codigo), ';') as requisicaoitens");
    	sql.append(" from solicitacaoorcamentoplanoorcamentario");

    	sql.append(" inner join planoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");
    	sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.codigo = itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario");
    	sql.append(" left join requisicao on requisicao.categoriadespesa = itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa");
    	sql.append(" and requisicao.unidadeensino = solicitacaoorcamentoplanoorcamentario.unidadeensino");
    	sql.append(" and requisicao.situacaoautorizacao = 'AU' and planoorcamentario.datainicio <= requisicao.datarequisicao ");
    	sql.append(" and  planoorcamentario.datafinal >= requisicao.datarequisicao");
    	sql.append(" left join funcionariocargo on funcionariocargo.codigo = requisicao.funcionariocargo");
    	sql.append(" left join cargo on funcionariocargo.cargo = cargo.codigo");
    	sql.append(" left join departamento on ((requisicao.departamento is not null and departamento.codigo = requisicao.departamento)");
    	sql.append(" or  (requisicao.departamento is null and funcionariocargo.departamento is not null and departamento.codigo = funcionariocargo.departamento)");
    	sql.append(" or (requisicao.departamento is null and funcionariocargo.departamento is null and departamento.codigo = cargo.departamento))");
    	sql.append(" and departamento.codigo = solicitacaoorcamentoplanoorcamentario.departamento");
    	sql.append(" left join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo and requisicaoitem.quantidadeautorizada > 0");
    	sql.append(" and requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo");
    	sql.append(" where 1 = 1 ");
    	sql.append(" and planoorcamentario.situacao = 'AT' ");
    	sql.append(" and planoorcamentario.datainicio <= current_date ");
    	sql.append(" and  planoorcamentario.datafinal >= current_date");
    	sql.append(" and  itemsolicitacaoorcamentoplanoorcamentario.valorAutorizado  > 0 ");

    	if (Uteis.isAtributoPreenchido(requisicaoVO.getCategoriaDespesa().getCodigo())) {
    		sql.append(" and itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa = ?");
    		filtros.add(requisicaoVO.getCategoriaDespesa().getCodigo());
    	}

    	if (Uteis.isAtributoPreenchido(departamentoVO)) {
    		sql.append(" and solicitacaoorcamentoplanoorcamentario.departamento = ?");
    		filtros.add(departamentoVO.getCodigo());
    	}

    	if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
    		sql.append(" and solicitacaoorcamentoplanoorcamentario.unidadeensino = ?");
    		filtros.add(unidadeEnsinoVO.getCodigo());
    	}

    	sql.append(" group by itemsolicitacaoorcamentoplanoorcamentario.codigo, itemsolicitacaoorcamentoplanoorcamentario.valor,");
    	sql.append(" planoorcamentario.codigo , itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa, solicitacaoorcamentoplanoorcamentario.departamento ,");
    	sql.append(" solicitacaoorcamentoplanoorcamentario.unidadeensino");
    	sql.append(" having itemsolicitacaoorcamentoplanoorcamentario.valorAutorizado - coalesce (sum(requisicaoitem.quantidadeautorizada::numeric(20,2)  * requisicaoitem.valorunitario::numeric(20,2) )::numeric(20,2), 0.0) >= ?");
    	filtros.add(requisicaoVO.getValorTotalRequisicao());
    	sql.append(" order by planoorcamentario.codigo desc limit 1");

    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

    	SolicitacaoOrcamentoPlanoOrcamentarioVO obj = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
    	ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
    	if (tabelaResultado.next()) {
    		itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setCodigo(tabelaResultado.getInt("codigo"));
    		itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setValor(tabelaResultado.getDouble("valor"));
    		obj.getPlanoOrcamentario().setCodigo(tabelaResultado.getInt("planoorcamentario"));
    		
    		itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa().setCodigo(tabelaResultado.getInt("categoriadespesa"));
    		obj.getDepartamento().setCodigo(tabelaResultado.getInt("departamento"));
    		obj.setRequisicoes(tabelaResultado.getString("requisicoes"));
    		obj.setRequisicaoitens(tabelaResultado.getString("requisicaoitens"));
    		
    		obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().add(itemSolicitacaoOrcamentoPlanoOrcamentarioVO);
    	}

    	return obj;
    }

    public Double consultarTotalRestantePlanoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select SUM(detalhamentoplanoorcamentario.orcamentototaldepartamento - detalhamentoplanoorcamentario.valorconsumido) as restante from itemsolicitacaoorcamentoplanoorcamentario as item ");
        sql.append(" inner join solicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
        sql.append(" inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario");
        sql.append(" inner join detalhamentoplanoorcamentario on detalhamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");
        sql.append("		and detalhamentoplanoorcamentario.departamento = solicitacaoorcamentoplanoorcamentario.departamento ");
        sql.append(" WHERE 1=1 AND solicitacaoorcamentoplanoorcamentario.situacao = 'AT' ");
        sql.append(" and item.codigo = ? ;");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo());

        return (Double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "restante", TipoCampoEnum.DOUBLE);
	}

	@Override
	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> consultaPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append("WHERE planoOrcamentario.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), planoOrcamentarioVO.getCodigo());
        return montarDadosConsultaCompleta(tabelaResultado, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAguardandoAprovacao(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String situacao,  UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(solicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo())) {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		} else {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().incluir(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		}
		this.alterarSituacao(solicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo(), situacao, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(int codigo, String situacao, UsuarioVO usuarioVO) {
		try {
			final String sql = "UPDATE SolicitacaoOrcamentoPlanoOrcamentario set situacao=? WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					
					sqlAlterar.setInt(2, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoValorAprovado(int codigo, String situacao, double valorAprovado, UsuarioVO usuarioVO) {
		try {
			final String sql = "UPDATE SolicitacaoOrcamentoPlanoOrcamentario set situacao=?, valortotalAprovado =? WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setDouble(2, valorAprovado);
					
					sqlAlterar.setInt(3, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoSolicitacaoPlanoOrcamentario(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String situacao,
			UsuarioVO usuarioVO) throws Exception {
		try {
			solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalAprovado(0.0);
			this.alterarValorAprovado(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
			this.alterarSituacao(solicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo(), situacao, usuarioVO);

			if (situacao.equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO.getValor()) 
					|| situacao.equals(SituacaoPlanoOrcamentarioEnum.REVISAO.getValor())
					|| situacao.equals(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO.getValor())) {
				for (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO : 
						solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
					itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setValorAutorizado(BigDecimal.ZERO);
					
					for (DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
						detalhamentoPeriodoOrcamentoVO.setOrcamentoTotal(0.0);
					}
				}
				getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarValorAprovado(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<CategoriaDespesaVO> consultarCategoriaDespesaPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct categoriadespesa.codigo, categoriadespesa.identificadorcategoriadespesa, categoriadespesa.descricao from solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario item on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = item.categoriadespesa");
		sql.append(" inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario");

		String[] arrayString = montarArrayStringPlanoOrcamentario(planoOrcamentariosVO);
		sql.append(" where planoorcamentario  ").append(realizarGeracaoInValor(planoOrcamentariosVO.size(), arrayString));

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CategoriaDespesaVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			CategoriaDespesaVO obj = new CategoriaDespesaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setIdentificadorCategoriaDespesa(tabelaResultado.getString("identificadorcategoriadespesa"));
			lista.add(obj);
		}
		return lista;
	}

	public List<CategoriaProdutoVO> consultarCategoriaProdutoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct categoriaproduto.codigo, categoriaproduto.nome from solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario item on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario");
		sql.append(" inner join requisicaoitem on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join requisicao on requisicao.codigo = requisicaoitem.requisicao");
		sql.append(" inner join categoriaproduto on categoriaproduto.codigo = requisicao.categoriaproduto");

		String[] arrayString = montarArrayStringPlanoOrcamentario(planoOrcamentariosVO);
		sql.append(" where planoorcamentario  ").append(realizarGeracaoInValor(planoOrcamentariosVO.size(), arrayString));
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CategoriaProdutoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			CategoriaProdutoVO obj = new CategoriaProdutoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			lista.add(obj);
		}
		return lista;
	}

	public List<CentroResultadoVO> consultarCentroResultadoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct centroresultado.codigo, centroresultado.descricao from solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario item on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario");
		sql.append(" inner join requisicaoitem on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario");
		sql.append(" inner join requisicao on requisicao.codigo = requisicaoitem.requisicao");
		sql.append(" inner join centroresultado on centroresultado.codigo = requisicao.centroresultadoadministrativo");

		String[] arrayString = montarArrayStringPlanoOrcamentario(planoOrcamentariosVO);
		sql.append(" where planoorcamentario  ").append(realizarGeracaoInValor(planoOrcamentariosVO.size(), arrayString));

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CentroResultadoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			CentroResultadoVO obj = new CentroResultadoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			lista.add(obj);
		}
		return lista;
	}

	private String[] montarArrayStringPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO) {
		String[] arrayString = new String[planoOrcamentariosVO.size()];
		int contador = 0;
		for (PlanoOrcamentarioVO obj : planoOrcamentariosVO) {
			arrayString[contador] = obj.getCodigo().toString();
			contador++;
		}
		return arrayString;
	}

	@Override
	public void adicionarObjItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, ItemSolicitacaoOrcamentoPlanoOrcamentarioVO  itemSolicitacaoOrcamentoPlanoOrcamentario, boolean remanejamento) throws Exception {
		if (!remanejamento) {
			ItemSolicitacaoOrcamentoPlanoOrcamentario.validarDados( itemSolicitacaoOrcamentoPlanoOrcamentario);
		}
		int index = 0;
		Iterator<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> i = solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().iterator();
		while (i.hasNext()) {
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO objExistente = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) i.next();
			if (objExistente.getCategoriaDespesa().getCodigo().equals( itemSolicitacaoOrcamentoPlanoOrcamentario.getCategoriaDespesa().getCodigo())) {
				if(!objExistente.getValorAutorizado().equals( itemSolicitacaoOrcamentoPlanoOrcamentario.getValorAutorizado())) {
					solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado().doubleValue() - objExistente.getValor());
					solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado().doubleValue() +  itemSolicitacaoOrcamentoPlanoOrcamentario.getValor());
					if(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado() < 0.0) {
						solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(0.0);
					}
					 itemSolicitacaoOrcamentoPlanoOrcamentario.setUnidadeEnsino(solicitacaoOrcamentoPlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
					if(objExistente.getDetalhamentoPeriodoOrcamentoVOs().isEmpty()) {
						 itemSolicitacaoOrcamentoPlanoOrcamentario.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarDistribuicaoValorItemMensal( itemSolicitacaoOrcamentoPlanoOrcamentario.getValorBaseUtilizar(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataInicio(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataFinal()));
					}else {
						 itemSolicitacaoOrcamentoPlanoOrcamentario.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarRedistribuicaoValorItemMensal(objExistente.getDetalhamentoPeriodoOrcamentoVOs(),  itemSolicitacaoOrcamentoPlanoOrcamentario.getValorBaseUtilizar()));
					}
					solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().set(index,  itemSolicitacaoOrcamentoPlanoOrcamentario);
				}
				return;
			}
			index++;
		}
		
		solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado().doubleValue() +  itemSolicitacaoOrcamentoPlanoOrcamentario.getValor());
		 itemSolicitacaoOrcamentoPlanoOrcamentario.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarDistribuicaoValorItemMensal( itemSolicitacaoOrcamentoPlanoOrcamentario.getValorBaseUtilizar(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataInicio(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataFinal()));
		solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().add( itemSolicitacaoOrcamentoPlanoOrcamentario);
	}

	@Override
	public void excluirObjItensProvisaoVOs(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, Integer codCategoriaDespesa) throws Exception {
		int index = 0;
		Iterator<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> i = solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()
				.iterator();
		while (i.hasNext()) {
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO objExistente = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) i.next();
			if (objExistente.getCategoriaDespesa().getCodigo().equals(codCategoriaDespesa)) {
				solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado().doubleValue()
						- (solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().get(index)).getValor());
				if(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado() < 0.0) {
					solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalSolicitado(0.0);
				}
				solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().remove(index);
				return;
			}
			index++;
		}
	}
	
	@Override
	public void enviarEmailResponsavelDepartamento(DepartamentoVO departamento, UnidadeEnsinoVO unidadeEnsinoVO, Boolean enviarComunicadoPorEmail, String mensagemPadraoNotificacao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		enviarEmailResponsavelDepartamento(departamento, unidadeEnsinoVO, enviarComunicadoPorEmail, mensagemPadraoNotificacao, usuarioLogado,
				configuracaoGeralSistemaVO, 0, usuarioLogado.getPessoa(), departamento.getResponsavel());
    }

	@Override
    public void enviarEmailResponsavelDepartamento(DepartamentoVO departamento, UnidadeEnsinoVO unidadeEnsinoVO, Boolean enviarComunicadoPorEmail,
    		String mensagemPadraoNotificacao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
    		Integer codigoTipoOrigemComunicacaoInterna, PessoaVO remetente, PessoaVO destinatario) throws Exception {
        ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();

        if (usuarioLogado.getPessoa() == null || usuarioLogado.getPessoa().getCodigo().equals(0)) {
            throw new Exception("Este usuário não pode enviar Comunicação Interna, pois não possui nenhuma pessoa vinculada a ele.");
        }
        comunicacaoInternaVO.setResponsavel(usuarioLogado.getPessoa());
        comunicacaoInternaVO.setUnidadeEnsino(unidadeEnsinoVO);
        comunicacaoInternaVO.setAssunto("Notificação Plano Orçamentário");
        comunicacaoInternaVO.setEnviarEmail(enviarComunicadoPorEmail);
        comunicacaoInternaVO.setTipoDestinatario("FU");
        comunicacaoInternaVO.setTipoMarketing(false);
        comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
        comunicacaoInternaVO.setDigitarMensagem(true);
        comunicacaoInternaVO.setRemoverCaixaSaida(false);
        comunicacaoInternaVO.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO);
        comunicacaoInternaVO.getFuncionario().setPessoa(remetente);
        comunicacaoInternaVO.setFuncionarioNome(remetente.getNome());
        comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(mensagemPadraoNotificacao));
        comunicacaoInternaVO.setCodigoTipoOrigemComunicacaoInterna(codigoTipoOrigemComunicacaoInterna);
        adicionarDestinatarioResponsavelDepartamento(comunicacaoInternaVO, destinatario);
        getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, usuarioLogado, configuracaoGeralSistemaVO, null);
    }

	@Override
    public void adicionarDestinatarioResponsavelDepartamento(ComunicacaoInternaVO comunicacaoInternaVO, PessoaVO responsavel) throws Exception {
        ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
        if (responsavel != null && !responsavel.getCodigo().equals(0)) {
            comunicadoInternoDestinatarioVO.setDestinatario(responsavel);
            comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
            comunicadoInternoDestinatarioVO.setDataLeitura(null);
            comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
            comunicadoInternoDestinatarioVO.setCiJaLida(false);
            comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
            comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
            comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);

        }
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorAprovado(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception{
		 for(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO: planoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			 this.alterarValorAprovado(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
			 getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarValorAprovado(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		 }
	}

	private void alterarValorAprovado(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception {
		 getConexao().getJdbcTemplate().update("update SolicitacaoOrcamentoPlanoOrcamentario set valorTotalAprovado = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado(), solicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCalculoValorAprovadoPorCategoriaDespesa(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception{
		 solicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs().clear();
		 Double valorTotalAprovadoAplicado = 0.0;
		 for(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO: solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			 if(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado() > 0) {
				 Double percentual = (itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValor() * 100.00) /  solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalSolicitado();			 
				 itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setValorAutorizado(BigDecimal.valueOf(Uteis.arrendondarForcando2CadasDecimais((solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado() * percentual) /100.00)));
				 valorTotalAprovadoAplicado += itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue();
			 }else {
				 itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setValorAutorizado(BigDecimal.ZERO);	 
			 }
		 }
		 if(valorTotalAprovadoAplicado < solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado()) {
			 solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().get(0).setValorAutorizado(solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().get(0).getValorAutorizado().add(BigDecimal.valueOf(solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado() - valorTotalAprovadoAplicado)));
		 }else if(valorTotalAprovadoAplicado > solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado()) {
			 solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().get(0).setValorAutorizado(solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().get(0).getValorAutorizado().subtract(BigDecimal.valueOf(valorTotalAprovadoAplicado - solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado())));
		 }
		 realizarCalculoValorAprovadoPorDetalhamentoPeriodo(solicitacaoOrcamentoPlanoOrcamentarioVO);
		 realizarCriacaoDetalhamentoPeriodoGeral(solicitacaoOrcamentoPlanoOrcamentarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCalculoValorAprovadoPorDetalhamentoPeriodo(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) throws Exception {
		for (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO : solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			Double valorTotalAprovadoAplicado = 0.0;

			for (DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
				if (detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0.0) {
					if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().equals(BigDecimal.ZERO)) { 
						detalhamentoPeriodoOrcamentoVO.setOrcamentoTotal(0.0);
					} else {
						if ( !(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue() == 0.0)) {
							Double percentual = (detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() * 100.00) / itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue();
							
							detalhamentoPeriodoOrcamentoVO.setOrcamentoTotal(Uteis.arrendondarForcando2CadasDecimais(
									(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue() * percentual) / 100.00));
							valorTotalAprovadoAplicado += detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal();
						}
					}
				}
			}

			if (Uteis.arrendondarForcando2CadasDecimais(valorTotalAprovadoAplicado) < itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue()) {
				Optional<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO
						.getDetalhamentoPeriodoOrcamentoVOs().stream().filter(dp -> dp.getOrcamentoTotal() > 0.0).findFirst();
				if (detalhamentoPeriodoOrcamentoVO.isPresent()) {
					detalhamentoPeriodoOrcamentoVO.get().setOrcamentoTotal(Uteis
							.arrendondarForcando2CadasDecimais(detalhamentoPeriodoOrcamentoVO.get().getOrcamentoTotal() + (itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue() - valorTotalAprovadoAplicado)));
				}

			}else if (Uteis.arrendondarForcando2CadasDecimais(valorTotalAprovadoAplicado) > itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue()) {
				Optional<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO
						.getDetalhamentoPeriodoOrcamentoVOs().stream().filter(dp -> dp.getOrcamentoTotal() > 0.0).findFirst();
				if (detalhamentoPeriodoOrcamentoVO.isPresent()) {
					detalhamentoPeriodoOrcamentoVO.get().setOrcamentoTotal(Uteis.arrendondarForcando2CadasDecimais(
							detalhamentoPeriodoOrcamentoVO.get().getOrcamentoTotal() - (valorTotalAprovadoAplicado - itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue())));
				}
			}
		}
	}

	@Override
	public void alterarSolicitacaoOrcamentoPlanoOrcamentarioPorSituacao(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, 
			SituacaoPlanoOrcamentarioEnum situacaoPlanoOrcamentarioEnum, UsuarioVO usuarioVO) throws Exception {

		solicitacaoOrcamentoPlanoOrcamentarioVO.setSituacao(situacaoPlanoOrcamentarioEnum);
		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarSituacao(solicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo(), 
				situacaoPlanoOrcamentarioEnum.getValor(), usuarioVO);
	}
}
