package negocio.facade.jdbc.sad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
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
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.sad.DespesaDWVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.sad.NivelGraficoDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.sad.DespesaDWInterfaceFacade;
//import net.sf.json.JSONArray;

/**
 * Classe de persistï¿½ncia que encapsula todas as operaï¿½ï¿½es de manipulaï¿½ï¿½o dos dados da classe
 * <code>DespesaDWVO</code>. Responsï¿½vel por implementar operaï¿½ï¿½es como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DespesaDWVO</code>. Encapsula toda a interaï¿½ï¿½o com o banco de dados.
 * 
 * @see DespesaDWVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class DespesaDW extends ControleAcesso implements DespesaDWInterfaceFacade {

    protected static String idEntidade;

    public DespesaDW() throws Exception {
        super();
        setIdEntidade("Despesa");
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar um novo objeto da classe <code>DespesaDWVO</code>.
     */
    public DespesaDWVO novo(UsuarioVO usuario) throws Exception {
        DespesaDW.incluir(getIdEntidade());
        DespesaDWVO obj = new DespesaDWVO("PA");
        return obj;
    }

    public void montarDadosPendentes(DespesaDWVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() != 0
                && (obj.getCurso().getCodigo().intValue() == 0 || obj.getTurno().getCodigo().intValue() == 0 || obj.getAreaConhecimento().getCodigo().intValue() == 0)) {
            SqlRowSet tabelaResultado = consultarDadosTurma(obj.getTurma().getCodigo(), false, usuario);

            if (tabelaResultado.next()) {
                obj.getCurso().setCodigo(tabelaResultado.getInt("curso"));
                obj.getAreaConhecimento().setCodigo(tabelaResultado.getInt("areaConhecimento"));
                obj.getTurno().setCodigo(tabelaResultado.getInt("turno"));
                obj.setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
            }

        }
    }

    public SqlRowSet consultarDadosTurma(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "Select curso.codigo as curso,curso.nivelEducacional as nivelEducacional, curso.areaConhecimento as areaConhecimento, turma.turno, as turno from Turma, Curso "
                + " where Turma.curso = Curso.codigo and turma.codigo = " + turma.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (SqlRowSet) (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por incluir no banco de dados um objeto da classe <code>DespesaDWVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexï¿½o com o banco de dados e
     * a permissï¿½o do usuï¿½rio para realizar esta operacï¿½o na entidade. Isto, atravï¿½s da operaï¿½ï¿½o
     * <code>incluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>DespesaDWVO</code> que serï¿½ gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexï¿½o, restriï¿½ï¿½o de acesso ou validaï¿½ï¿½o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DespesaDWVO obj, UsuarioVO usuario) throws Exception {

        try {
            final String sql = "INSERT INTO DespesaDW( data, mes, valor, ano, categoriaDespesa, unidadeEnsino, curso, turno, fornecedor, "
                    + "areaConhecimento, nivelEducacional, tipoSacado, funcionario, banco, departamento, "
                    + "funcionarioCentroCusto, turma, origem ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);

                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setInt(2, obj.getMes().intValue());
                    sqlInserir.setDouble(3, obj.getValor().doubleValue());
                    sqlInserir.setInt(4, obj.getAno().intValue());
                    sqlInserir.setInt(5, obj.getCategoriaDespesa().getCodigo().intValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getTurno().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getTurno().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(10, obj.getAreaConhecimento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(10, 0);
                    }
                    sqlInserir.setString(11, obj.getNivelEducacional());
                    sqlInserir.setString(12, obj.getTipoSacado());
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(13, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(13, 0);
                    }
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(14, 0);
                    }

                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(15, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(15, 0);
                    }
                    if (obj.getFuncionarioCentroCusto().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(16, obj.getFuncionarioCentroCusto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(16, 0);
                    }
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(17, obj.getTurma().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(17, 0);
                    }
                    sqlInserir.setString(18, obj.getOrigem());
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
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por alterar no BD os dados de um objeto da classe <code>DespesaDWVO</code>. Sempre
     * utiliza a chave primï¿½ria da classe como atributo para localizaï¿½ï¿½o do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexï¿½o com o banco de dados e a permissï¿½o
     * do usuï¿½rio para realizar esta operacï¿½o na entidade. Isto, atravï¿½s da operaï¿½ï¿½o <code>alterar</code> da
     * superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>DespesaDWVO</code> que serï¿½ alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexï¿½o, restriï¿½ï¿½o de acesso ou validaï¿½ï¿½o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DespesaDWVO obj) throws Exception {
        try {
            final String sql = "UPDATE DespesaDW set data=?, mes=?, valor=?, ano=?, categoriaDespesa=?, unidadeEnsino=?, curso=?, "
                    + "turno=?, fornecedor=?, areaConhecimento=?, nivelEducacional=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setInt(2, obj.getMes().intValue());
                    sqlAlterar.setInt(3, obj.getValor().intValue());
                    sqlAlterar.setInt(4, obj.getAno().intValue());
                    sqlAlterar.setInt(5, obj.getCategoriaDespesa().getCodigo().intValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getTurno().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getTurno().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(10, obj.getAreaConhecimento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(10, 0);
                    }
                    sqlAlterar.setString(11, obj.getNivelEducacional());
                    sqlAlterar.setInt(12, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por excluir no BD um objeto da classe <code>DespesaDWVO</code>. Sempre localiza o
     * registro a ser excluï¿½do atravï¿½s da chave primï¿½ria da entidade. Primeiramente verifica a conexï¿½o com o
     * banco de dados e a permissï¿½o do usuï¿½rio para realizar esta operacï¿½o na entidade. Isto, atravï¿½s da
     * operaï¿½ï¿½o <code>excluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>DespesaDWVO</code> que serï¿½ removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DespesaDWVO obj) throws Exception {
        try {
            String sql = "DELETE FROM DespesaDW WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void gerarRelatorioGrafico(NivelGraficoDWVO obj, NivelGraficoDWVO obj2, String nivel, Date dataInicio, Date dataFim, String origem, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
    	dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataFim = Uteis.getDataUltimoDiaMes(dataFim);
        SqlRowSet rs = null;
        if (nivel.equals("")) {
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj.alimentarGrafico(rs, "UE");
            rs.beforeFirst();
            obj2.alimentarGrafico(rs, "UE");
        } else if (nivel.equals("UE")) {
            if (origem.equals("PA")) {
                rs = gerarRelatorioNivelFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FA");
            } else {
                rs = gerarRelatorioNivelDepartamento(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "DE");
            }
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("DE")) {
            if (origem.equals("RE")) {
                rs = gerarRelatorioNivelFuncionario(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FU");
            } else if (origem.equals("PA")) {
                rs = gerarRelatorioNivelFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FA");
            }
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("FU")) {
            rs = gerarRelatorioNivelFuncionario(obj, dataInicio, dataFim, origem, usuario);
            obj.alimentarGrafico(rs, "FU");
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("FA")) {
            if (obj.getTipoFavorecido().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
                rs = gerarRelatorioNivelFuncionarioFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FUF");
            } else if (obj.getTipoFavorecido().equals(TipoSacado.FORNECEDOR.getValor())) {
                rs = gerarRelatorioNivelFornecedorFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FOF");
            } else if (obj.getTipoFavorecido().equals(TipoSacado.BANCO.getValor())) {
                rs = gerarRelatorioNivelBancoFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "BAF");
            }
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("FUF")) {
            rs = gerarRelatorioNivelFuncionarioFavorecido(obj, dataInicio, dataFim, origem, usuario);
            obj.alimentarGrafico(rs, "FUF");
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("FOF")) {
            rs = gerarRelatorioNivelFornecedorFavorecido(obj, dataInicio, dataFim, origem, usuario);
            obj.alimentarGrafico(rs, "FOF");
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("BAF")) {
            rs = gerarRelatorioNivelBancoFavorecido(obj, dataInicio, dataFim, origem, usuario);
            obj.alimentarGrafico(rs, "BAF");
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        } else if (nivel.equals("CD")) {
            if (obj.getNivelEntidade().equals("DE")) {
                rs = gerarRelatorioNivelDepartamento(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "DE");
            } else if (obj.getNivelEntidade().equals("FU")) {
                rs = gerarRelatorioNivelFuncionario(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FU");
            } else if (obj.getNivelEntidade().equals("FA")) {
                rs = gerarRelatorioNivelFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FA");
            } else if (obj.getNivelEntidade().equals("FUF")) {
                rs = gerarRelatorioNivelFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FUF");
            } else if (obj.getNivelEntidade().equals("FOF")) {
                rs = gerarRelatorioNivelFuncionarioFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "FOF");
            } else if (obj.getNivelEntidade().equals("BAF")) {
                rs = gerarRelatorioNivelBancoFavorecido(obj, dataInicio, dataFim, origem, usuario);
                obj.alimentarGrafico(rs, "BAF");
            }
            rs = gerarRelatorioNivelCategoriaDespesa(obj2, dataInicio, dataFim, origem, usuario);
            obj2.alimentarGrafico(rs, "CD");
        }

    }

    private SqlRowSet gerarRelatorioNivelUnidade(List<UnidadeEnsinoVO> unidadeEnsinoVOs, NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        StringBuilder sqlStr = new StringBuilder(" Select sum(DespesaDW.valor) as valor, case when (UnidadeEnsino.abreviatura is null or UnidadeEnsino.abreviatura = '') then UnidadeEnsino.nome else UnidadeEnsino.abreviatura end as entidade, UnidadeEnsino.codigo as codigoEntidade  ");
        sqlStr.append(" from DespesaDW inner join UnidadeEnsino  on DespesaDW.unidadeEnsino = UnidadeEnsino.codigo ");
        sqlStr.append(" left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ");
        sqlStr.append(montarClausulaWhere(obj, dataInicio, dataFim, origem, rs));
        if (unidadeEnsinoVOs != null && unidadeEnsinoVOs.isEmpty()) {
            sqlStr.append(" and unidadeensino.codigo in (");
            int x = 0;
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                if (x > 0) {
                    sqlStr.append(", ");
                }
                sqlStr.append(unidadeEnsinoVO.getCodigo());
                x++;
            }
            sqlStr.append(" ) ");
        }
        sqlStr.append(" group by  unidadeEnsino.nome, unidadeEnsino.abreviatura, UnidadeEnsino.codigo order by unidadeEnsino.nome, unidadeEnsino.abreviatura");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelDepartamento(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        SqlRowSet rs = (SqlRowSet) consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        String sqlStr = " Select sum(DespesaDW.valor) as valor, Departamento.nome as entidade, Departamento.codigo as codigoEntidade  "
                + " from DespesaDW inner join Departamento  on DespesaDW.departamento = Departamento.codigo " + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += " group by  departamento.nome, departamento.codigo order by departamento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;

    }

    private SqlRowSet gerarRelatorioNivelFuncionario(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        String sqlStr = "Select sum(DespesaDW.valor) as valor, Pessoa.nome as entidade, Funcionario.codigo as codigoEntidade  "
                + " from DespesaDW inner join Funcionario  on DespesaDW.funcionarioCentroCusto = Funcionario.codigo " + " left join Pessoa  on Funcionario.pessoa = Pessoa.codigo "
                + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += " group by  Pessoa.nome, Funcionario.codigo order by Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelFavorecido(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        String sqlStr = "Select sum(DespesaDW.valor) as valor, DespesaDW.tipoSacado as entidade, 0 as codigoEntidade " + " from DespesaDW  "
                + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += "  and ((DespesaDW.funcionario is not null or DespesaDW.funcionario > 0)" + " or (DespesaDW.banco is not null or DespesaDW.banco > 0)"
                + " or (DespesaDW.fornecedor is not null or DespesaDW.fornecedor > 0))";
        sqlStr += " group by  DespesaDW.tipoSacado order by DespesaDW.tipoSacado";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelFuncionarioFavorecido(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        String sqlStr = "Select sum(DespesaDW.valor) as valor,Pessoa.nome as entidade, Funcionario.codigo as codigoEntidade  "
                + " from DespesaDW inner join Funcionario  on DespesaDW.funcionario = Funcionario.codigo " + " left join Pessoa  on Funcionario.pessoa = Pessoa.codigo "
                + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);

        sqlStr += " group by  Pessoa.nome, Funcionario.codigo order by Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelBancoFavorecido(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        String sqlStr = "Select sum(DespesaDW.valor) as valor,Banco.nome as entidade, Banco.codigo as codigoEntidade  " + " from DespesaDW inner join Banco  on DespesaDW.banco = Banco.codigo "
                + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += " group by  Banco.nome, Banco.codigo order by Banco.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelFornecedorFavorecido(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        String sqlStr = "Select sum(DespesaDW.valor) as valor, Fornecedor.nome as entidade, Fornecedor.codigo as codigoEntidade  "
                + " from DespesaDW inner join Fornecedor  on DespesaDW.fornecedor = Fornecedor.codigo " + " left join CategoriaDespesa on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += " group by  Fornecedor.nome, Fornecedor.codigo order by Fornecedor.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private SqlRowSet gerarRelatorioNivelCategoriaDespesa(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, UsuarioVO usuario) throws Exception {
        SqlRowSet rs = consultarIdentificadorCategoriaDespesa(obj.getCategoriaDespesa(), false, usuario);

        String sqlStr = " Select sum(valor) as valor, entidade, codigoEntidade from  (" + " Select sum(DespesaDW.valor) as valor, " + montarSelectCategoriaDespesa(rs)
                + " from DespesaDW inner join CategoriaDespesa  on DespesaDW.CategoriaDespesa = CategoriaDespesa.codigo ";
        rs.beforeFirst();
        sqlStr += montarClausulaWhere(obj, dataInicio, dataFim, origem, rs);
        sqlStr += " group by  CategoriaDespesa.codigo, CategoriaDespesa.descricao, identificadorCategoriaDespesa order by CategoriaDespesa.descricao ) as tabela "
                + " where tabela.valor > 0 group by entidade, codigoEntidade order by entidade";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    private String montarSelectCategoriaDespesa(SqlRowSet rs) throws Exception {
        String sql = "";
        String sqlElse = " ";
        int i = 0;
        if (rs.next()) {
            rs.beforeFirst();
            // adiciona condiçao select entidade
            while (rs.next()) {
                sql += sqlElse + " case when (  CategoriaDespesa.identificadorCategoriaDespesa like('" + rs.getString("identificadorCategoriaDespesa") + "%')) "
                        + " then (select descricao from CategoriaDespesa where identificadorCategoriaDespesa = '" + rs.getString("identificadorCategoriaDespesa") + "') ";
                sqlElse = " else ";
                i++;
            }
            for (int j = 1; j <= i; j++) {
                sql += " end ";
            }
            sql += " as entidade, ";
            rs.beforeFirst();
            sqlElse = " ";

            // adiciona condiçao select codigoEntidade
            while (rs.next()) {
                sql += sqlElse + " case when (  CategoriaDespesa.identificadorCategoriaDespesa like('" + rs.getString("identificadorCategoriaDespesa") + "%')) "
                        + " then (select codigo from CategoriaDespesa where identificadorCategoriaDespesa = '" + rs.getString("identificadorCategoriaDespesa") + "') ";
                sqlElse = " else ";

            }
            for (int j = 1; j <= i; j++) {
                sql += " end ";
            }
            sql += " as codigoEntidade ";
            rs.beforeFirst();
        } else {
            sql += " CategoriaDespesa.descricao as entidade, CategoriaDespesa.codigo as codigoEntidade ";
        }
        return sql;

    }

    // public SqlRowSet consultaGeracaoRelatorioPizzaBarra(DespesaDWVO obj) throws Exception {
    // String sql =
    // "Select sum(DespesaDW.valor) as valor, UnidadeEnsino.nome as unidadeEnsino from DespesaDW inner join UnidadeEnsino on DespesaDW.unidadeEnsino = UnidadeEnsino.codigo ";
    // //sql += montarClausulaWhere(obj);
    // sql += " group by  unidadeEnsino.nome";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sql);
    // return tabelaResultado;
    // }
    private String montarClausulaWhere(NivelGraficoDWVO obj, Date dataInicio, Date dataFim, String origem, SqlRowSet rs) throws Exception {
        String sql = " ";
        sql += " where (( DespesaDW.data >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00')" + " and (DespesaDW.data <= '" + Uteis.getDataJDBC(dataFim) + " 23:59:59'))";

        if (obj.getFuncionario().intValue() != 0) {
            sql += " and DespesaDW.funcionarioCentroCusto = " + obj.getFuncionario().intValue() + " ";
        }
        if (obj.getUnidadeEnsino().intValue() != 0) {
            sql += " and DespesaDW.UnidadeEnsino = " + obj.getUnidadeEnsino().intValue() + " " + " and DespesaDW.departamento is not null and DespesaDW.departamento > 0 ";
        }

        if (obj.getDepartamento().intValue() != 0) {
            sql += " and DespesaDW.Departamento = " + obj.getDepartamento().intValue() + " ";
            if (origem.equals("RE")) {
                sql += " and DespesaDW.funcionarioCentroCusto is not null";
            }
        }

        if (obj.getTipoFavorecido() != null && !obj.getTipoFavorecido().equals("")) {
            sql += " and DespesaDW.tipoSacado = '" + obj.getTipoFavorecido() + "' ";
            if (obj.getTipoFavorecido().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
                sql += " and DespesaDW.funcionario is not null  ";
                if (obj.getFuncionarioFavorecido().intValue() != 0) {
                    sql += " and DespesaDW.funcionario = " + obj.getFuncionarioFavorecido().intValue();
                } else {
                    sql += " and DespesaDW.funcionario > 0 ";
                }
            }
            if (obj.getTipoFavorecido().equals(TipoSacado.BANCO.getValor())) {
                sql += " and DespesaDW.banco is not null  ";
                if (obj.getBancoFavorecido().intValue() != 0) {
                    sql += " and DespesaDW.banco = " + obj.getBancoFavorecido().intValue();
                } else {
                    sql += " and DespesaDW.banco > 0 ";
                }
            }
            if (obj.getTipoFavorecido().equals(TipoSacado.FORNECEDOR.getValor())) {
                sql += " and DespesaDW.fornecedor is not null  ";
                if (obj.getFornecedorFavorecido().intValue() != 0) {
                    sql += " and DespesaDW.fornecedor = " + obj.getFornecedorFavorecido().intValue();
                } else {
                    sql += " and DespesaDW.fornecedor > 0 ";
                }
            }
        }

        if (origem == null) {
            origem = "";
        }

        sql += " and DespesaDW.origem = '" + origem + "'";

        if (rs.next()) {
            rs.beforeFirst();
            String andOr = " and ( ";
            while (rs.next()) {
                sql += andOr + " CategoriaDespesa.identificadorCategoriaDespesa like ('" + rs.getString("identificadorCategoriaDespesa") + "%')";
                andOr = " or ";
            }
            if (andOr.equals(" or ")) {
                sql += " )";
            }
        } else {
            if (obj.getCategoriaDespesa().intValue() != 0) {
                sql += " and CategoriaDespesa.codigo = " + obj.getCategoriaDespesa() + " ";
            }
        }

        return sql;
    }

    private SqlRowSet consultarIdentificadorCategoriaDespesa(Integer categoriaDespesa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        if (categoriaDespesa.intValue() == 0) {
            sqlStr = "select identificadorCategoriaDespesa, descricao, codigo from categoriaDespesa where (categoriaDespesaPrincipal = 0  or categoriaDespesaPrincipal is null) ";
        } else {
            sqlStr = "select identificadorCategoriaDespesa, descricao, codigo from categoriaDespesa where categoriaDespesaPrincipal = " + categoriaDespesa;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado;
    }

    //
    // public SqlRowSet consultaGeracaoRelatorioLinhaTempo(DespesaDWVO obj) throws Exception {
    // String sql =
    // "Select sum(DespesaDW.valor) as valor, UnidadeEnsino.nome as unidadeEnsino, DespesaDW.data as data from DespesaDW left join UnidadeEnsino  on DespesaDW.unidadeEnsino = UnidadeEnsino.codigo ";
    // //sql += montarClausulaWhere(obj);
    // sql += " group by  unidadeEnsino.nome, data  order by data";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sql);
    // return tabelaResultado;
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>nome</code> da classe <code>AreaConhecimento</code>
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Execption Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorNomeAreaConhecimento(String valorConsulta, int nivelMontarDados) throws Exception {
    // ControleAcesso.consultar(getIdEntidade(), true);
    // String sqlStr =
    // "SELECT DespesaDW.* FROM DespesaDW, AreaConhecimento WHERE DespesaDW.areaConhecimento = AreaConhecimento.codigo and upper( AreaConhecimento.nome ) like('"
    // + valorConsulta.toUpperCase() + "%') ORDER BY AreaConhecimento.nome";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>nome</code> da classe <code>Turno</code>
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Execption Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorNomeTurno(String valorConsulta, int nivelMontarDados) throws Exception {
    // ControleAcesso.consultar(getIdEntidade(), true);
    // String sqlStr =
    // "SELECT DespesaDW.* FROM DespesaDW, Turno WHERE DespesaDW.turno = Turno.codigo and upper( Turno.nome ) like('" +
    // valorConsulta.toUpperCase() + "%') ORDER BY Turno.nome";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>nome</code> da classe <code>Curso</code>
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Execption Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorNomeCurso(String valorConsulta, int nivelMontarDados) throws Exception {
    // ControleAcesso.consultar(getIdEntidade(), true);
    // String sqlStr =
    // "SELECT DespesaDW.* FROM DespesaDW, Curso WHERE DespesaDW.curso = Curso.codigo and upper( Curso.nome ) like('" +
    // valorConsulta.toUpperCase() + "%') ORDER BY Curso.nome";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>nome</code> da classe <code>UnidadeEnsino</code>
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Execption Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados) throws Exception {
    // ControleAcesso.consultar(getIdEntidade(), true);
    // String sqlStr =
    // "SELECT DespesaDW.* FROM DespesaDW, UnidadeEnsino WHERE DespesaDW.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('"
    // + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>Integer centroDespesa</code>. Retorna os objetos com valores iguais ou superiores ao parï¿½metro
    // fornecido.
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @param controlarAcesso Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui permissï¿½o para esta
    // consulta ou nï¿½o.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Exception Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorCentroDespesa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados)
    // throws Exception {
    // ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    // String sqlStr = "SELECT * FROM DespesaDW WHERE centroDespesa >= " + valorConsulta.intValue() +
    // " ORDER BY centroDespesa";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>Date data</code>. Retorna os objetos com valores pertecentes ao perï¿½odo informado por parï¿½metro.
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @param controlarAcesso Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui permissï¿½o para esta
    // consulta ou nï¿½o.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Exception Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados) throws
    // Exception {
    // ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    // String sqlStr = "SELECT * FROM DespesaDW WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" +
    // Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    // }
    //
    // /**
    // * Responsï¿½vel por realizar uma consulta de <code>DespesaDW</code> atravï¿½s do valor do atributo
    // * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parï¿½metro fornecido.
    // * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
    // * @param controlarAcesso Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui permissï¿½o para esta
    // consulta ou nï¿½o.
    // * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
    // * @exception Exception Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
    // */
    // public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws
    // Exception {
    // ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    // String sqlStr = "SELECT * FROM DespesaDW WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
    // Statement stm = con.createStatement();
    // SqlRowSet tabelaResultado = stm.executeQuery(sqlStr);
    // return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    // }
    /**
     * Responsï¿½vel por montar os dados de vï¿½rios objetos, resultantes de uma consulta ao banco de dados (
     * <code>SqlRowSet</code>). Faz uso da operaï¿½ï¿½o <code>montarDados</code> que realiza o trabalho para um objeto
     * por vez.
     * 
     * @return List Contendo vï¿½rios objetos da classe <code>DespesaDWVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
        return vetResultado;
    }

    /**
     * Responsï¿½vel por montar os dados resultantes de uma consulta ao banco de dados (<code>SqlRowSet</code>) em um
     * objeto da classe <code>DespesaDWVO</code>.
     * 
     * @return O objeto da classe <code>DespesaDWVO</code> com os dados devidamente montados.
     */
    public static DespesaDWVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        DespesaDWVO obj = new DespesaDWVO("");
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setMes(new Integer(dadosSQL.getInt("mes")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.getCategoriaDespesa().setCodigo(new Integer(dadosSQL.getInt("categoriaDespesa")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
        obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
        obj.getFuncionarioCentroCusto().setCodigo(new Integer(dadosSQL.getInt("funcionarioCentroCusto")));
        obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento")));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
        obj.setOrigem(dadosSQL.getString("origem"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        // montarDadosUnidadeEnsino(obj, nivelMontarDados);
        // montarDadosCurso(obj, nivelMontarDados);
        // montarDadosTurno(obj, nivelMontarDados);
        // montarDadosAreaConhecimento(obj, nivelMontarDados);
        return obj;
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado
     * ao objeto <code>DespesaDWVO</code>. Faz uso da chave primï¿½ria da classe <code>AreaConhecimentoVO</code> para
     * realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual serï¿½ montado os dados consultados.
     */
    public static void montarDadosAreaConhecimento(DespesaDWVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto
     * <code>DespesaDWVO</code>. Faz uso da chave primï¿½ria da classe <code>TurnoVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual serï¿½ montado os dados consultados.
     */
    public static void montarDadosTurno(DespesaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getTurno().getCodigo().intValue() == 0) {
            obj.setTurno(new TurnoVO());
            return;
        }
        obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto
     * <code>DespesaDWVO</code>. Faz uso da chave primï¿½ria da classe <code>CursoVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual serï¿½ montado os dados consultados.
     */
    public static void montarDadosCurso(DespesaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao
     * objeto <code>DespesaDWVO</code>. Faz uso da chave primï¿½ria da classe <code>UnidadeEnsinoVO</code> para realizar
     * a consulta.
     * 
     * @param obj
     *            Objeto no qual serï¿½ montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(DespesaDWVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por localizar um objeto da classe <code>DespesaDWVO</code> atravï¿½s de sua chave
     * primï¿½ria.
     * 
     * @exception Exception
     *                Caso haja problemas de conexï¿½o ou localizaï¿½ï¿½o do objeto procurado.
     */
    public DespesaDWVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM DespesaDW WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DespesaDW ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por retornar o identificador desta classe. Este identificar ï¿½ utilizado para
     * verificar as permissï¿½es de acesso as operaï¿½ï¿½es desta classe.
     */
    public static String getIdEntidade() {
        return DespesaDW.idEntidade;
    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por definir um novo valor para o identificador desta classe. Esta alteraï¿½ï¿½o deve
     * ser possï¿½vel, pois, uma mesma classe de negï¿½cio pode ser utilizada com objetivos distintos. Assim ao se
     * verificar que Como o controle de acesso ï¿½ realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DespesaDW.idEntidade = idEntidade;
    }
    
    @Override
    public Object realizarMontagemGraficoPizzaJSON(List<LegendaGraficoVO> listaDadosAcesso) throws Exception {
        
        JSONArray dados = new JSONArray();
        JSONArray jsonArray = null;
        try {
            double total = 0.0;
            for (LegendaGraficoVO obj : listaDadosAcesso) {
                total += obj.getValor();
            }
            for (LegendaGraficoVO obj : listaDadosAcesso) {
                jsonArray = new JSONArray();                
                jsonArray.add(Uteis.removerAcentos(obj.getNome()).replaceAll("ª", "").replaceAll("º", ""));                
                jsonArray.add(Uteis.truncar(obj.getValor() * 100.0 / total, 2));
                jsonArray.add("R$"+Uteis.getDoubleFormatado(obj.getValor()));
                jsonArray.add(obj.getCor());
                jsonArray.add(obj.getNivel());
                jsonArray.add(obj.getCodigo());                
                dados.add(jsonArray);
                jsonArray = null;
            }
            return dados;
        }catch (Exception e) {
            throw e;        
        } finally {
            dados = null;
        }
        
    }
}
