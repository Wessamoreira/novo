package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.PlanoFinanceiroReposicaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.DisciplinaAbono;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.PlanoFinanceiroReposicaoInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PlanoFinanceiroReposicao extends ControleAcesso implements PlanoFinanceiroReposicaoInterfaceFacade {

    protected static String idEntidade;

    public PlanoFinanceiroReposicao() throws Exception {
        super();
        setIdEntidade("PlanoFinanceiroReposicao");
    }

    public void validarDados(PlanoFinanceiroReposicaoVO obj) throws Exception {
        if (obj.getDescricao().equals("")) {
            throw new Exception("O campo DESCRIÇÃO deve ser informado!");
        }
        if (obj.getQtdeParcela().equals(0)) {
            throw new Exception("O campo QUANTIDADE PARCELA deve ser informado!");
        }
        if (obj.getValor().equals(0.0)) {
            throw new Exception("O campo VALOR deve ser informado!");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            PlanoFinanceiroReposicao.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO PlanoFinanceiroReposicao( descricao, qtdeParcela, valor, textoPadraoContrato, situacao, dataAtivacao, dataInativacao, responsavelAtivacao, responsavelInativacao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao().toString());
                    sqlInserir.setInt(2, obj.getQtdeParcela());
                    sqlInserir.setDouble(3, obj.getValor());
                    if (obj.getTextoPadraoContratoVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getTextoPadraoContratoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getSituacao());
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getResponsavelAtivacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getResponsavelInativacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
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
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            PlanoFinanceiroReposicao.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE PlanoFinanceiroReposicao set descricao=?, qtdeParcela=?, valor=?, textoPadraoContrato=?, situacao=?, dataAtivacao=?, dataInativacao=?, responsavelAtivacao=?, responsavelInativacao=?  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao().toString());
                    sqlAlterar.setInt(2, obj.getQtdeParcela());
                    sqlAlterar.setDouble(3, obj.getValor());
                    if (obj.getTextoPadraoContratoVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getTextoPadraoContratoVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, obj.getSituacao());
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getResponsavelAtivacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getResponsavelInativacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setInt(10, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception {
        try {
            DisciplinaAbono.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM PlanoFinanceiroReposicao WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void carregarDados(PlanoFinanceiroReposicaoVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((PlanoFinanceiroReposicaoVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(PlanoFinanceiroReposicaoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((PlanoFinanceiroReposicaoVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((PlanoFinanceiroReposicaoVO) obj, resultado);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codPlanoFinanceiroCurso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (PlanoFinanceiroReposicao.codigo= ").append(codPlanoFinanceiroCurso).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codPlanoFinanceiroCurso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (PlanoFinanceiroReposicao.codigo= ").append(codPlanoFinanceiroCurso).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT DISTINCT codigo, descricao, situacao FROM planofinanceiroreposicao ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT DISTINCT planofinanceiroreposicao.codigo, planofinanceiroreposicao.descricao, planofinanceiroreposicao.situacao, qtdeParcela, valor, dataAtivacao, dataInativacao, ");
        str.append(" textoPadrao.codigo AS \"textoPadrao.codigo\", textoPadrao.descricao AS \"textoPadrao.descricao\", textoPadrao.tipo AS \"textoPadrao.tipo\", ");
        str.append(" responsavelativacao.codigo AS \"responsavelativacao.codigo\", responsavelativacao.nome AS \"responsavelativacao.nome\", ");
        str.append(" responsavelInativacao.codigo AS \"responsavelInativacao.codigo\", responsavelInativacao.nome AS \"responsavelInativacao.nome\" ");
        str.append(" FROM planofinanceiroreposicao ");
        str.append(" LEFT JOIN textoPadrao ON textoPadrao.codigo = planofinanceiroreposicao.textoPadraoContrato ");
        str.append(" LEFT JOIN usuario responsavelativacao ON responsavelativacao.codigo = planofinanceiroreposicao.responsavelativacao ");
        str.append(" LEFT JOIN usuario responsavelInativacao ON responsavelInativacao.codigo = planofinanceiroreposicao.responsavelInativacao ");
        return str;
    }

    private void montarDadosBasico(PlanoFinanceiroReposicaoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
    }

    private void montarDadosCompleto(PlanoFinanceiroReposicaoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setQtdeParcela(dadosSQL.getInt("qtdeParcela"));
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        obj.setDataInativacao(dadosSQL.getDate("dataInativacao"));
        //Dados do Contrato
        obj.getTextoPadraoContratoVO().setCodigo(dadosSQL.getInt("textoPadrao.codigo"));
        obj.getTextoPadraoContratoVO().setDescricao(dadosSQL.getString("textoPadrao.descricao"));
        obj.getTextoPadraoContratoVO().setTipo(dadosSQL.getString("textoPadrao.tipo"));
        //Dados do Responsavel
        obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelAtivacao.codigo"));
        obj.getResponsavelAtivacao().setNome(dadosSQL.getString("responsavelAtivacao.nome"));
        obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelInativacao.codigo"));
        obj.getResponsavelInativacao().setNome(dadosSQL.getString("responsavelInativacao.nome"));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
    }

    public List<PlanoFinanceiroReposicaoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<PlanoFinanceiroReposicaoVO> vetResultado = new ArrayList<PlanoFinanceiroReposicaoVO>(0);
        while (tabelaResultado.next()) {
            PlanoFinanceiroReposicaoVO obj = new PlanoFinanceiroReposicaoVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorDescricao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(planoFinanceiroReposicao.descricao)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY planoFinanceiroReposicao.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(planoFinanceiroReposicao.situacao)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY planoFinanceiroReposicao.situacao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoFinanceiroReposicaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE planoFinanceiroReposicao.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append(" ORDER BY planoFinanceiroReposicao.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoFinanceiroReposicao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PlanoFinanceiroReposicao.idEntidade = idEntidade;
    }

    public void inicializarDadosAtivacao(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, UsuarioVO usuarioVO) throws Exception {
        planoFinaceiroReposicaoVO.setSituacao("AT");
        planoFinaceiroReposicaoVO.setDataAtivacao(new Date());
        planoFinaceiroReposicaoVO.setResponsavelAtivacao(usuarioVO);
    }

    public void inicializarDadosInativacao(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, UsuarioVO usuarioVO) throws Exception {
        planoFinaceiroReposicaoVO.setSituacao("IN");
        planoFinaceiroReposicaoVO.setDataInativacao(new Date());
        planoFinaceiroReposicaoVO.setResponsavelInativacao(usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacaoCondicaoPagamento(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        inicializarDadosAtivacao(planoFinaceiroReposicaoVO, usuarioVO);
        alterarSituacaoAtivacao(planoFinaceiroReposicaoVO.getResponsavelAtivacao().getCodigo(), planoFinaceiroReposicaoVO.getCodigo(), planoFinaceiroReposicaoVO.getDataAtivacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoAtivacao(Integer responsavelAtivacao, Integer planoFinanceiroReposicao, Date dataAtivacao) throws Exception {
        PlanoFinanceiroReposicao.alterar(getIdEntidade());
        String sqlStr = "UPDATE PlanoFinanceiroReposicao set situacao = 'AT', responsavelAtivacao = ?, dataAtivacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelAtivacao, dataAtivacao, planoFinanceiroReposicao});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacaoCondicaoPagamento(PlanoFinanceiroReposicaoVO planoFinaceiroReposicaoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        inicializarDadosInativacao(planoFinaceiroReposicaoVO, usuarioVO);
        alterarSituacaoInativacao(planoFinaceiroReposicaoVO.getResponsavelInativacao().getCodigo(), planoFinaceiroReposicaoVO.getCodigo(), planoFinaceiroReposicaoVO.getDataInativacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoInativacao(Integer responsavelInativacao, Integer planoFinanceiroReposicao, Date dataInativacao) throws Exception {
        PlanoFinanceiroReposicao.alterar(getIdEntidade());
        String sqlStr = "UPDATE PlanoFinanceiroReposicao set situacao = 'IN', responsavelInativacao = ?, dataInativacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelInativacao, dataInativacao, planoFinanceiroReposicao});
    }
}
