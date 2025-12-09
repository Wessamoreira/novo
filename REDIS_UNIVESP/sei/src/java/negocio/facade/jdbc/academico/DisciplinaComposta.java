package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaCompostaVO;
import negocio.comuns.academico.DisciplinaEquivalenteVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaCompostaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DisciplinaEquivalenteVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>DisciplinaEquivalenteVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DisciplinaEquivalenteVO
 * @see ControleAcesso
 * @see Disciplina
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DisciplinaComposta extends ControleAcesso implements DisciplinaCompostaInterfaceFacade {

    protected static String idEntidade;

    public DisciplinaComposta() throws Exception {
        super();
        setIdEntidade("Disciplina");
    }

    public void validarDados(DisciplinaCompostaVO obj) {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DisciplinaCompostaVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO DisciplinaComposta( disciplina, composta, ordem ) VALUES ( ?, ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getDisciplinaVO().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getCompostaVO().getCodigo().intValue());
                sqlInserir.setInt(3, obj.getOrdem());
                return sqlInserir;
            }
        });

        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DisciplinaCompostaVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE DisciplinaComposta set disciplina=?, composta=?, ordem=?  WHERE ((disciplina = ?) and (composta = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getDisciplinaVO().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getCompostaVO().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getOrdem().intValue());
                sqlAlterar.setInt(4, obj.getDisciplinaVO().getCodigo().intValue());
                sqlAlterar.setInt(5, obj.getCompostaVO().getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DisciplinaCompostaVO obj, UsuarioVO usuario) throws Exception {
        DisciplinaEquivalente.excluir(getIdEntidade());
        String sql = "DELETE FROM DisciplinaComposta WHERE ((disciplina = ?) and (composta = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getDisciplinaVO().getCodigo(), obj.getCompostaVO().getCodigo()});
    }

    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DisciplinaComposta.* FROM DisciplinaComposta, Disciplina WHERE DisciplinaComposta.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta
                + "%') ORDER BY Disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>DisciplinaEquivalenteVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DisciplinaEquivalenteVO</code>.
     *
     * @return O objeto da classe <code>DisciplinaEquivalenteVO</code> com os dados devidamente montados.
     */
    public static DisciplinaCompostaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        DisciplinaCompostaVO obj = new DisciplinaCompostaVO();
        obj.getDisciplinaVO().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.getCompostaVO().setCodigo(new Integer(dadosSQL.getInt("composta")));
        obj.setOrdem(dadosSQL.getInt("ordem"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosDisciplinaComposta(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto
     * <code>DisciplinaEquivalenteVO</code>. Faz uso da chave primária da classe <code>DisciplinaVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDisciplinaComposta(DisciplinaCompostaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCompostaVO().getCodigo().intValue() == 0) {
            obj.setCompostaVO(new DisciplinaVO());
            return;
        }
        obj.setCompostaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDisciplinaComposta(Integer disciplina, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM DisciplinaComposta WHERE (disciplina = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{disciplina});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinaCompostas(Integer disciplina, List objetos, UsuarioVO usuario) throws Exception {
        excluirDisciplinaComposta(disciplina, usuario);
        incluirDisciplinaComposta(disciplina, objetos, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDisciplinaComposta(Integer disciplinaPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            DisciplinaCompostaVO obj = (DisciplinaCompostaVO) e.next();
            obj.getDisciplinaVO().setCodigo(disciplinaPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>DisciplinaEquivalenteVO</code> relacionados a um objeto da
     * classe <code>academico.Disciplina</code>.
     *
     * @param disciplina
     *            Atributo de <code>academico.Disciplina</code> a ser utilizado para localizar os objetos da classe
     *            <code>DisciplinaEquivalenteVO</code>.
     * @return List Contendo todos os objetos da classe <code>DisciplinaEquivalenteVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarDisciplinaComposta(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DisciplinaComposta WHERE disciplina = ? ORDER BY ordem ";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{disciplina});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario));
        }
        return objetos;
    }

    public DisciplinaCompostaVO consultarPorChavePrimaria(Integer disciplinaPrm, Integer equivalentePrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DisciplinaComposta WHERE disciplina = ?, composta = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{disciplinaPrm, equivalentePrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    public DisciplinaCompostaVO consultarDisciplinaCompostaPorCompostaGradeCurricularPeriodoLetivo(Integer gradeCurricular, Integer composta, Integer periodoLetivo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append("select disciplinaComposta.* from disciplinaComposta ");
        sb.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplinacomposta.disciplina ");
        sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
        sb.append(" where periodoletivo.gradecurricular = ");
        sb.append(gradeCurricular);
        sb.append(" and disciplinacomposta.composta = ");
        sb.append(composta);
        sb.append(" and periodoLetivo.codigo = ");
        sb.append(periodoLetivo);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return new DisciplinaCompostaVO();
        }
        return (montarDados(tabelaResultado, usuario));
    }

    public Integer consultarQtdeDisciplinaCompostaPorDisciplinaMae(Integer disciplina, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(composta) from disciplinaComposta where disciplina = ").append(disciplina);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getInt("count");
        }
        return 0;
    }


    public String executarVerificacaoNotaAtualizarHistoricoDisciplinaComposta(DisciplinaCompostaVO obj) {
        if (obj.getOrdem().equals(0)) {
            return "NOTA1";
        }
        if (obj.getOrdem().equals(1)) {
            return "NOTA2";
        }
        if (obj.getOrdem().equals(2)) {
            return "NOTA3";
        }
        if (obj.getOrdem().equals(3)) {
            return "NOTA4";
        }
        if (obj.getOrdem().equals(4)) {
            return "NOTA5";
        }
        if (obj.getOrdem().equals(5)) {
            return "NOTA6";
        }
        if (obj.getOrdem().equals(6)) {
            return "NOTA7";
        }
        if (obj.getOrdem().equals(7)) {
            return "NOTA8";
        }
        if (obj.getOrdem().equals(8)) {
            return "NOTA9";
        }
        if (obj.getOrdem().equals(9)) {
            return "NOTA10";
        }
        if (obj.getOrdem().equals(10)) {
            return "NOTA11";
        }
        if (obj.getOrdem().equals(11)) {
            return "NOTA12";
        }
        if (obj.getOrdem().equals(12)) {
            return "NOTA13";
        }
        return "";
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DisciplinaEquivalente.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        DisciplinaEquivalente.idEntidade = idEntidade;
    }
    
    @Override
	public List<DisciplinaCompostaVO> consultarCompostaPorDisciplina(Integer disciplina, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select disciplinaComposta.* from disciplinaComposta ");
		sb.append("where disciplinaComposta.disciplina = ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuario);
	}
}
