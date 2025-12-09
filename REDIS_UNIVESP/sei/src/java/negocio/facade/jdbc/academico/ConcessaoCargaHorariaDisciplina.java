package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.ConcessaoCargaHorariaDisciplinaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConcessaoCargaHorariaDisciplinaInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class ConcessaoCargaHorariaDisciplina extends ControleAcesso implements ConcessaoCargaHorariaDisciplinaInterfaceFacade {

    public ConcessaoCargaHorariaDisciplina() {
    }

    public void validarDados(ConcessaoCargaHorariaDisciplinaVO obj) throws Exception {
        if (obj.getDisciplinaVO() == null || obj.getDisciplinaVO().getCodigo() == 0) {
            throw new Exception("O campo DISCIPLINA deve ser informado");
        }
        if (obj.getAproveitamentoDisciplinaVO() == null || obj.getAproveitamentoDisciplinaVO().getCodigo() == 0) {
            throw new Exception("O campo APROVEITAMENTO DE DISCIPLINA deve ser informado");
        }
        if (obj.getQtdeCargaHorariaConcedido() == 0) {
            throw new Exception("O campo QTDE DE CARGA HORÁRIA A CONCEDER deve ser informado");
        }
        if (obj.getDescricaoComplementacaoCH().equals("")) {
            throw new Exception("O campo COMPLEMENTAÇÃO CARGA HORÁRIA deve ser informado");
        }
    }

    public void validarDadosAdicionarAproveitamento(ConcessaoCargaHorariaDisciplinaVO obj) throws Exception {
        if (obj.getDisciplinaVO() == null || obj.getDisciplinaVO().getCodigo() == 0) {
            throw new Exception("O campo DISCIPLINA deve ser informado");
        }
        if (obj.getQtdeCargaHorariaConcedido() == 0) {
            throw new Exception("O campo QTDE DE CARGA HORÁRIA A CONCEDER deve ser informado");
        }
        if (obj.getDescricaoComplementacaoCH().equals("")) {
            throw new Exception("O campo COMPLEMENTAÇÃO CARGA HORÁRIA deve ser informado");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.
     * #incluir(negocio
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO ConcessaoCargaHorariaDisciplina( disciplina, qtdeCargaHorariaConcedido, descricaoComplementacaoCH, aproveitamentoDisciplina, ano, semestre ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getDisciplinaVO().getCodigo().intValue());
                    sqlInserir.setInt(2, obj.getQtdeCargaHorariaConcedido().intValue());
                    sqlInserir.setString(3, obj.getDescricaoComplementacaoCH());
                    if (obj.getAproveitamentoDisciplinaVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getAproveitamentoDisciplinaVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getAno());
                    sqlInserir.setString(6, obj.getSemestre());
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

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #alterar(negocio
     * .comuns.academico.AproveitamentoDisciplinaDisciplinasAproveitadasVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "UPDATE ConcessaoCargaHorariaDisciplina set disciplina=?, qtdeCargaHorariaConcedido=?, descricaoComplementacaoCH=?, aproveitamentoDisciplina=?, ano=?, semestre=? "
                    + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getDisciplinaVO().getCodigo().intValue());
                    sqlAlterar.setInt(2, obj.getQtdeCargaHorariaConcedido());
                    sqlAlterar.setString(3, obj.getDescricaoComplementacaoCH());
                    if (obj.getAproveitamentoDisciplinaVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getAproveitamentoDisciplinaVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, obj.getAno());
                    sqlAlterar.setString(6, obj.getSemestre());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #excluir(negocio
     * .comuns.academico.AproveitamentoDisciplinaDisciplinasAproveitadasVO)
     */
    public void excluir(ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
        	ConcessaoCargaHorariaDisciplina.excluir(getIdEntidade());
            String sql = "DELETE FROM ConcessaoCargaHorariaDisciplina WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void excluirPorAproveitamentoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, UsuarioVO usuario) throws Exception {
		try {
			ConcessaoCargaHorariaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM ConcessaoCargaHorariaDisciplina WHERE (aproveitamentoDisciplina = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { aproveitamentoDisciplina });
		} catch (Exception e) {
			throw e;
		}
	}

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorDescricaoAproveitamentoDisciplina(java.lang.String, int)
     */
    public List<ConcessaoCargaHorariaDisciplinaVO> consultarPorDescricaoAproveitamentoDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ConcessaoCargaHorariaDisciplina.* FROM AproveitamentoDisciplinaDisciplinasAproveitadas, AproveitamentoDisciplina WHERE AproveitamentoDisciplinaDisciplinasAproveitadas.AproveitamentoDisciplina = AproveitamentoDisciplina.codigo and upper( AproveitamentoDisciplina.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY AproveitamentoDisciplina.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<ConcessaoCargaHorariaDisciplinaVO> consultarConcessaoCargaHorariaDisciplinaPorAproveitamento(Integer aproveitamentoDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ConcessaoCargaHorariaDisciplina.consultar(getIdEntidade());
        String sql = "SELECT * FROM ConcessaoCargaHorariaDisciplina WHERE aproveitamentoDisciplina = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{aproveitamentoDisciplina});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public static List<ConcessaoCargaHorariaDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ConcessaoCargaHorariaDisciplinaVO obj = new ConcessaoCargaHorariaDisciplinaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>.
     *
     * @return O objeto da classe
     *         <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code> com os
     *         dados devidamente montados.
     */
    public static ConcessaoCargaHorariaDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ConcessaoCargaHorariaDisciplinaVO obj = new ConcessaoCargaHorariaDisciplinaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setQtdeCargaHorariaConcedido(dadosSQL.getInt("qtdeCargaHorariaConcedido"));
        obj.setDescricaoComplementacaoCH(dadosSQL.getString("descricaoComplementacaoCH"));
        obj.getDisciplinaVO().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.getAproveitamentoDisciplinaVO().setCodigo((new Integer(dadosSQL.getInt("AproveitamentoDisciplina"))));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosDisciplina(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosDisciplina(ConcessaoCargaHorariaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplinaVO().getCodigo().intValue() == 0) {
            obj.setDisciplinaVO(new DisciplinaVO());
            return;
        }
        obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosAproveitamentoDisciplina(ConcessaoCargaHorariaDisciplinaVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        if (obj.getAproveitamentoDisciplinaVO().getCodigo().intValue() == 0) {
            obj.setAproveitamentoDisciplinaVO(new AproveitamentoDisciplinaVO());
            return;
        }
        obj.setAproveitamentoDisciplinaVO(getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorChavePrimaria(obj.getAproveitamentoDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirConcessaoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ConcessaoCargaHorariaDisciplinaVO obj = (ConcessaoCargaHorariaDisciplinaVO) e.next();
            obj.getAproveitamentoDisciplinaVO().setCodigo(aproveitamentoDisciplina);
            incluir(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarConcessaoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM ConcessaoCargaHorariaDisciplina WHERE (aproveitamentoDisciplina = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            Iterator i = objetos.iterator();
            while (i.hasNext()) {
                ConcessaoCargaHorariaDisciplinaVO obj = (ConcessaoCargaHorariaDisciplinaVO) i.next();
                if (obj.getCodigo().intValue() != 0) {
                    sql += " and codigo != " + obj.getCodigo().intValue();
                }
            }
            getConexao().getJdbcTemplate().update(sql, new Object[]{aproveitamentoDisciplina});
            Iterator e = objetos.iterator();
            while (e.hasNext()) {
                ConcessaoCargaHorariaDisciplinaVO obj = (ConcessaoCargaHorariaDisciplinaVO) e.next();
                obj.getAproveitamentoDisciplinaVO().setCodigo(aproveitamentoDisciplina);
                if (obj.getCodigo().intValue() == 0) {
                    incluir(obj, usuario);
                } else {
                    alterar(obj, usuario);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
