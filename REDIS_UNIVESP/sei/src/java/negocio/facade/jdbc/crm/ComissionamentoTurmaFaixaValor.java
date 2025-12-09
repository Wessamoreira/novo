package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.ComissionamentoTurmaFaixaValorInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class ComissionamentoTurmaFaixaValor extends ControleAcesso implements ComissionamentoTurmaFaixaValorInterfaceFacade {

    protected static String idEntidade;

    public ComissionamentoTurmaFaixaValor() {
        super();
        setIdEntidade("ComissionamentoTurmaFaixaValor");
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return ComissionamentoTurmaFaixaValor.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ComissionamentoTurmaFaixaValor.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FuncionarioCargoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FuncionarioCargoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO ComissionamentoTurmaFaixaValor( comissionamentoTurma, qtdeInicialAluno, qtdeFinalAluno, valor, percComissao) VALUES ( ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                if (obj.getComissionamentoTurmaVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getComissionamentoTurmaVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setInt(2, obj.getQtdeInicialAluno());
                sqlInserir.setInt(3, obj.getQtdeFinalAluno());
                sqlInserir.setDouble(4, obj.getValor());
                sqlInserir.setDouble(5, obj.getPercComissao());
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FuncionarioCargoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        ComissionamentoTurmaFaixaValor.alterar(getIdEntidade());
        final String sql = "UPDATE ComissionamentoTurmaFaixaValor set comissionamentoTurma=?, qtdeInicialAluno=?, qtdeFinalAluno=?, valor=?, percComissao=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                if (obj.getComissionamentoTurmaVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getComissionamentoTurmaVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, obj.getQtdeInicialAluno());
                sqlAlterar.setInt(3, obj.getQtdeFinalAluno());
                sqlAlterar.setDouble(4, obj.getValor());
                sqlAlterar.setDouble(5, obj.getPercComissao());
                sqlAlterar.setInt(6, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception {
        ComissionamentoTurmaFaixaValor.excluir(getIdEntidade());
        String sql = "DELETE FROM ComissionamentoTurmaFaixaValor WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Operação responsável por incluir objetos da <code>FuncionarioCargoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>administrativo.funcionario</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirComissionamentoFaixaValorVOs(Integer comissionamento, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ComissionamentoTurmaFaixaValorVO obj = (ComissionamentoTurmaFaixaValorVO) e.next();
            obj.getComissionamentoTurmaVO().setCodigo(comissionamento);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>FuncionarioCargoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFuncionarioCargos</code> e <code>incluirFuncionarioCargos</code> disponíveis na classe <code>FuncionarioCargo</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarComissionamentoFaixaValorVOs(Integer comissionamentoTurma, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM ComissionamentoTurmaFaixaValor WHERE comissionamentoTurma = " + comissionamentoTurma;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ComissionamentoTurmaFaixaValorVO objeto = (ComissionamentoTurmaFaixaValorVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ComissionamentoTurmaFaixaValorVO objeto = (ComissionamentoTurmaFaixaValorVO) e.next();
            if (objeto.getComissionamentoTurmaVO().getCodigo().equals(0)) {
                objeto.getComissionamentoTurmaVO().setCodigo(comissionamentoTurma);
            }
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComissionamentoFaixaValorVOs(Integer comissionamento, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM ComissionamentoTurmaFaixaValor WHERE (comissionamentoTurma = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{comissionamento.intValue()});
    }

    public void validarDados(ComissionamentoTurmaFaixaValorVO obj) throws Exception {


        if (obj.getQtdeInicialAluno() == null || obj.getQtdeInicialAluno().equals(0)) {
            throw new Exception("O campo QTDE INICIAL DE ALUNOS deve ser informado.");
        }
        if (obj.getQtdeFinalAluno() == null || obj.getQtdeFinalAluno().equals(0)) {
            throw new Exception("O campo QTDE FINAL DE ALUNOS deve ser informado.");
        }
//        if (obj.getValor() == null || obj.getValor().equals(0.0)) {
//            throw new Exception("O campo VALOR deve ser informado");
//        }
        if (obj.getQtdeInicialAluno() > obj.getQtdeFinalAluno()) {
            throw new Exception("A Qtde Inicial de Alunos não pode ser inferior à Qtde Final.");
        }
    }

    public List<ComissionamentoTurmaFaixaValorVO> consultaRapidaPorComissionamentoTurma(Integer comissionamentoTurma,  boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append("select * from comissionamentoturmafaixavalor  where comissionamentoturma = ");
        sb.append(comissionamentoTurma.intValue());
        sb.append(" ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaRapida(tabelaResultado, usuario));
    }

    public List<ComissionamentoTurmaFaixaValorVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
        List<ComissionamentoTurmaFaixaValorVO> vetResultado = new ArrayList<ComissionamentoTurmaFaixaValorVO>(0);
        while (tabelaResultado.next()) {
            ComissionamentoTurmaFaixaValorVO obj = new ComissionamentoTurmaFaixaValorVO();
            montarDadosBasico(obj, tabelaResultado, usuarioVO);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosBasico(ComissionamentoTurmaFaixaValorVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getComissionamentoTurmaVO().setCodigo(dadosSQL.getInt("comissionamentoTurma"));
        obj.setQtdeInicialAluno(dadosSQL.getInt("qtdeInicialAluno"));
        obj.setQtdeFinalAluno(dadosSQL.getInt("qtdeFinalAluno"));
        obj.setPercComissao(dadosSQL.getDouble("percComissao"));
        obj.setValor(dadosSQL.getDouble("valor"));
    }

    public void validarDadosIntervalorQtdeAluno(List<ComissionamentoTurmaFaixaValorVO> listaComissionamentoFaixaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception {
        for (ComissionamentoTurmaFaixaValorVO comissionamentoTurmaFaixaValorVO : listaComissionamentoFaixaVO) {
            if (obj.getQtdeInicialAluno() >= comissionamentoTurmaFaixaValorVO.getQtdeInicialAluno() && obj.getQtdeInicialAluno() <= comissionamentoTurmaFaixaValorVO.getQtdeFinalAluno()) {
                throw new Exception("Já possui cadastrada QTDE DE ALUNOS nesse intervalo.");
            }
        }
    }

    public Double consultarValorComissionamentoPorTurma(Integer turma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cfv.valor FROM comissionamentoturmafaixavalor cfv ");
        sb.append(" INNER JOIN comissionamentoTurma ON comissionamentoTurma.codigo = cfv.comissionamentoTurma ");
        sb.append(" WHERE turma = ").append(turma);
        sb.append(" AND ").append(qtdeAlunoTurma).append(" >= cfv.qtdeInicialAluno AND ").append(qtdeAlunoTurma).append(" <= cfv.qtdeFinalAluno");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.0;
    }

    public ComissionamentoTurmaFaixaValorVO consultarComissionamentoPorTurmaFaixa(Integer turma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cfv.* FROM comissionamentoturmafaixavalor cfv ");
        sb.append(" INNER JOIN comissionamentoTurma ON comissionamentoTurma.codigo = cfv.comissionamentoTurma ");
        sb.append(" WHERE turma = ").append(turma);
        sb.append(" AND ").append(qtdeAlunoTurma).append(" >= cfv.qtdeInicialAluno AND ").append(qtdeAlunoTurma).append(" <= cfv.qtdeFinalAluno");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        ComissionamentoTurmaFaixaValorVO obj = new ComissionamentoTurmaFaixaValorVO();
        if (tabelaResultado.next()) {
            montarDadosBasico(obj, tabelaResultado, usuarioVO);
            return obj;
        }
        return obj;
    }
    
    @Override
    public ComissionamentoTurmaFaixaValorVO consultarComissionamentoPorComissinamentoTurmaFaixa(Integer comissionamentoturma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT cfv.* FROM comissionamentoturmafaixavalor cfv ");
    	sb.append(" INNER JOIN comissionamentoTurma ON comissionamentoTurma.codigo = cfv.comissionamentoTurma ");
    	sb.append(" WHERE comissionamentoturma.codigo = ").append(comissionamentoturma);
    	sb.append(" AND ").append(qtdeAlunoTurma).append(" >= cfv.qtdeInicialAluno AND ").append(qtdeAlunoTurma).append(" <= cfv.qtdeFinalAluno");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	ComissionamentoTurmaFaixaValorVO obj = new ComissionamentoTurmaFaixaValorVO();
    	if (tabelaResultado.next()) {
    		montarDadosBasico(obj, tabelaResultado, usuarioVO);
    		return obj;
    	}
    	return obj;
    }


}
