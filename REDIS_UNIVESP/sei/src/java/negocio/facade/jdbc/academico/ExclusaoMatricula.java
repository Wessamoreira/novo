package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ExclusaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ExclusaoMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>MatriculaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings("unchecked")
public class ExclusaoMatricula extends ControleAcesso implements ExclusaoMatriculaInterfaceFacade {

    protected static String idEntidade;

    public ExclusaoMatricula() throws Exception {
        super();
        setIdEntidade("ExclusaoMatricula");
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ExclusaoMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ExclusaoMatricula.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirLogExclusaoMatricula(final MatriculaVO obj, final MatriculaPeriodoVO objMatPer, final String motivoExclusao, final UsuarioVO usuario) throws Exception {
        try {
        	if(!obj.getConsultor().getCodigo().equals(0)){
        		obj.getConsultor().setPessoa(getFacadeFactory().getPessoaFacade().consultarPessoaPorCodigoFuncionario(obj.getConsultor().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        	}
            final String sql = "INSERT INTO logExclusaoMatricula( matricula, aluno, unidadeEnsino, curso, turno, dataMatricula, dataExclusao, nomeResponsavelExclusao, motivoExclusao, codunidadeensino, codturma, codconsultor, nomeconsultor, turma, codcurso )"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getMatricula());
                    sqlInserir.setString(2, obj.getAluno().getNome());
                    sqlInserir.setString(3, obj.getUnidadeEnsino().getNome());
                    sqlInserir.setString(4, obj.getCurso().getNome());
                    sqlInserir.setString(5, obj.getTurno().getNome());
                    sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getData()));
                    sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setString(8, usuario.getNome());
                    sqlInserir.setString(9, motivoExclusao);
                    sqlInserir.setInt(10, obj.getUnidadeEnsino().getCodigo());
                    sqlInserir.setInt(11, objMatPer.getTurma().getCodigo());
                    if(!obj.getConsultor().getCodigo().equals(0)){
                    	sqlInserir.setInt(12, obj.getConsultor().getCodigo());
                    }else{
                    	sqlInserir.setInt(12, 0);
                    }
                    if(!obj.getConsultor().getPessoa().getNome().isEmpty()){
                    	sqlInserir.setString(13, obj.getConsultor().getPessoa().getNome());
                    }else{
                    	sqlInserir.setString(13, null);
                    }
                    sqlInserir.setString(14, objMatPer.getTurma().getIdentificadorTurma());
                    sqlInserir.setInt(15, obj.getCurso().getCodigo());
                    return sqlInserir;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT codigo, matricula, aluno, unidadeEnsino, curso, turno, dataMatricula, dataExclusao, nomeResponsavelExclusao, motivoExclusao, codunidadeensino, codturma, codconsultor, consultor, nomeconsultor, turma FROM logexclusaomatricula ");
        return str;
    }

    public List consultarPorNomeAluno(String nomeAluno, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE UPPER(aluno) LIKE ('").append(nomeAluno.toUpperCase()).append("%')");
        sqlStr.append(" ORDER BY aluno ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorMatricula(String nomeAluno, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE UPPER(matricula) LIKE ('").append(nomeAluno.toUpperCase()).append("%')");
        sqlStr.append(" ORDER BY aluno ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorNomeCurso(String nomeCurso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE UPPER(curso) LIKE ('").append(nomeCurso.toUpperCase()).append("%')");
        sqlStr.append(" ORDER BY curso ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorDataExclusao(Date prmIni, Date prmFim, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (dataExclusao >= '").append(Uteis.getDataJDBC(prmIni)).append(" 00:00:00' AND dataExclusao <= '").append(Uteis.getDataJDBC(prmFim)).append(" 23:59:59')");
        sqlStr.append(" ORDER BY dataExclusao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorDataMatricula(Date prmIni, Date prmFim, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (dataMatricula >= '").append(Uteis.getDataJDBC(prmIni)).append(" 00:00:00' AND dataMatricula <= '").append(Uteis.getDataJDBC(prmFim)).append(" 23:59:59')");
        sqlStr.append(" ORDER BY dataMatricula ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public ExclusaoMatriculaVO consultarPorCodigo(Integer codigo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE codigo = ").append(codigo);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new ExclusaoMatriculaVO();
        }
        return (montarDados(tabelaResultado, usuario));
    }

    public static List<ExclusaoMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<ExclusaoMatriculaVO> vetResultado = new ArrayList<ExclusaoMatriculaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public static ExclusaoMatriculaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        ExclusaoMatriculaVO obj = new ExclusaoMatriculaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("aluno"));
        obj.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino"));
        obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso"));
        obj.getMatriculaVO().getTurno().setNome(dadosSQL.getString("turno"));
        obj.getMatriculaVO().setData(dadosSQL.getDate("dataMatricula"));
        obj.setDataExclusao(dadosSQL.getDate("dataExclusao"));
        obj.getResponsavelExclusao().setNome(dadosSQL.getString("nomeResponsavelExclusao"));
        obj.setMotivoExclusao(dadosSQL.getString("motivoExclusao"));
        obj.getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("codunidadeensino"));
        //obj.getMatriculaVO().getTurma().setCodigo(dadosSQL.getInt("codturma"));
        obj.getMatriculaVO().getConsultor().setCodigo(dadosSQL.getInt("codconsultor"));
        obj.getMatriculaVO().getConsultor().getPessoa().setNome(dadosSQL.getString("nomeconsultor"));
        //obj.getMatriculaVO().getTurma().setIdentificadorTurma(dadosSQL.getString("turma"));
        
        return obj;
    }
}
