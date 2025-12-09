package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.InclusaoDisciplinasHistoricoForaPrazoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaPeriodoTurmaDisciplinaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class InclusaoDisciplinasHistoricoForaPrazo extends ControleAcesso implements InclusaoDisciplinasHistoricoForaPrazoInterfaceFacade {

    protected static String idEntidade;

    public InclusaoDisciplinasHistoricoForaPrazo() throws Exception {
        super();
        setIdEntidade("InclusaoDisciplinasHistoricoForaPrazo");
    }

    public static String getIdEntidade() {
        return InclusaoDisciplinasHistoricoForaPrazo.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        InclusaoDisciplinasHistoricoForaPrazo.idEntidade = idEntidade;
    }
    
    public void validarDados(InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception {
    	
    }

    public void incluirListaInclusaoDisciplinasHistoricoForaPrazoVO(InclusaoHistoricoForaPrazoVO obj) throws Exception {
        try {
            for (InclusaoDisciplinasHistoricoForaPrazoVO inclusaoDisciplinasHistoricoForaPrazoVO : obj.getListaInclusaoDisciplinasHistoricoForaPrazoVO()) {
            	if (Uteis.isAtributoPreenchido(inclusaoDisciplinasHistoricoForaPrazoVO.getDisciplina().getCodigo())) {
					if (inclusaoDisciplinasHistoricoForaPrazoVO.getCodigo().intValue() == 0) {
						try {
							inclusaoDisciplinasHistoricoForaPrazoVO.setInclusaoHistoricoForaPrazoVO(obj);
							incluir(inclusaoDisciplinasHistoricoForaPrazoVO);
						} catch (Exception e) {
							inclusaoDisciplinasHistoricoForaPrazoVO.setNovoObj(Boolean.TRUE);
							throw e;
						}
					} else {
						alterar(inclusaoDisciplinasHistoricoForaPrazoVO);
					}
				}
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade#incluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception {
        try {
            ////System.out.println("++++++++++++++++++++++ INCLUI MATRICULA PERIODO TURMA DISCIPLINA  +++++++++++++++++++++++++++");
//            InclusaoDisciplinasHistoricoForaPrazo.validarDados(obj);
            final String sql = "INSERT INTO inclusaodisciplinashistoricoforaprazo( turma, disciplina, semestre, ano, inclusaoHistoricoForaPrazo, mapaEquivalenciaDisciplinaCursada, disciplinaForaGrade, periodoLetivoDisciplinaIncluida, gradeCurricularGrupoOptativaDisciplina, usuarioLibercaoChoqueHorario, dataUsuarioLibercaoChoqueHorario ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);

                    sqlInserir.setInt(1, obj.getTurma().getCodigo());
                    sqlInserir.setInt(2, obj.getDisciplina().getCodigo());
                    sqlInserir.setString(3, obj.getSemestre());
                    sqlInserir.setString(4, obj.getAno());
                    sqlInserir.setInt(5, obj.getInclusaoHistoricoForaPrazoVO().getCodigo().intValue());
                    if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
                    sqlInserir.setBoolean(7, obj.getDisciplinaForaGrade());
                    if (obj.getPeriodoLetivoDisciplinaIncluidaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getPeriodoLetivoDisciplinaIncluidaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
                    if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(9, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(9, 0);
                    }
                    if (obj.getUsuarioLibercaoChoqueHorario() != null) {
						sqlInserir.setInt(10, obj.getUsuarioLibercaoChoqueHorario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
                    if (obj.getDataUsuarioLibercaoChoqueHorario() != null) {
                    	sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataUsuarioLibercaoChoqueHorario()));
                    } else {
                    	sqlInserir.setNull(11, 0);
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
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade#alterar(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception {
        final String sql = "UPDATE inclusaodisciplinashistoricoforaprazo( turma=?, disciplina=?, semestre=?, ano=?, inclusaoHistoricoForaPrazo=?, mapaEquivalenciaDisciplinaCursada=?, disciplinaForaGrade=?, periodoLetivoDisciplinaIncluida=?, gradeCurricularGrupoOptativaDisciplina=?, usuarioLibercaoChoqueHorario=?, dataUsuarioLibercaoChoqueHorario=?  WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getTurma().getCodigo());
                sqlAlterar.setInt(2, obj.getDisciplina().getCodigo());
                sqlAlterar.setString(3, obj.getSemestre());
                sqlAlterar.setString(4, obj.getAno());
                sqlAlterar.setInt(5, obj.getInclusaoHistoricoForaPrazoVO().getCodigo().intValue());
                if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(6, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(6, 0);
				}
                sqlAlterar.setBoolean(7, obj.getDisciplinaForaGrade());
                if (obj.getPeriodoLetivoDisciplinaIncluidaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(8, obj.getPeriodoLetivoDisciplinaIncluidaVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(8, 0);
				}
                if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(9, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(9, 0);
                }
                if (obj.getUsuarioLibercaoChoqueHorario() != null) {
                	sqlAlterar.setInt(10, obj.getUsuarioLibercaoChoqueHorario().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(10, 0);
				}
                if (obj.getDataUsuarioLibercaoChoqueHorario() != null) {
                	sqlAlterar.setDate(11, Uteis.getDataJDBC(obj.getDataUsuarioLibercaoChoqueHorario()));
                } else {
                	sqlAlterar.setNull(11, 0);
                }                
                sqlAlterar.setInt(12, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorInclusaoHistoricoForaPrazo(InclusaoHistoricoForaPrazoVO obj) throws Exception {
        InclusaoHistoricoForaPrazo.excluir(getIdEntidade());
        String sql = "DELETE FROM inclusaodisciplinashistoricoforaprazo WHERE (inclusaohistoricoforaprazo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(InclusaoDisciplinasHistoricoForaPrazoVO obj) throws Exception {
        InclusaoHistoricoForaPrazo.excluir(getIdEntidade());
        String sql = "DELETE FROM inclusaodisciplinashistoricoforaprazo WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List<InclusaoDisciplinasHistoricoForaPrazoVO> consultarPorInclusaoHistoricoForaPrazo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT inclusaoDisciplinasHistoricoForaPrazo.*, turma.identificadorturma, disciplina.nome AS \"nomeDisciplina\" ");
        sqlStr.append("FROM inclusaoDisciplinasHistoricoForaPrazo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = inclusaoDisciplinasHistoricoForaPrazo.turma ");
        sqlStr.append("INNER JOIN disciplina ON disciplina.codigo = inclusaoDisciplinasHistoricoForaPrazo.disciplina ");
        sqlStr.append("LEFT JOIN gradedisciplina on gradedisciplina.periodoletivo = turma.periodoletivo and gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("WHERE inclusaoHistoricoForaPrazo = ").append(valorConsulta.intValue()).append(" ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public static List<InclusaoDisciplinasHistoricoForaPrazoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<InclusaoDisciplinasHistoricoForaPrazoVO> vetResultado = new ArrayList<InclusaoDisciplinasHistoricoForaPrazoVO>(0);
        while (tabelaResultado.next()) {
            InclusaoDisciplinasHistoricoForaPrazoVO obj = new InclusaoDisciplinasHistoricoForaPrazoVO();
            obj = montarDados(tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public static InclusaoDisciplinasHistoricoForaPrazoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        InclusaoDisciplinasHistoricoForaPrazoVO obj = new InclusaoDisciplinasHistoricoForaPrazoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
        obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina"));
        obj.getDisciplina().setNome(dadosSQL.getString("nomeDisciplina"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.getInclusaoHistoricoForaPrazoVO().setCodigo(dadosSQL.getInt("inclusaoHistoricoForaPrazo"));
        obj.getMapaEquivalenciaDisciplinaCursada().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
        obj.setDisciplinaForaGrade(dadosSQL.getBoolean("disciplinaForaGrade"));
        obj.getPeriodoLetivoDisciplinaIncluidaVO().setCodigo(dadosSQL.getInt("periodoLetivoDisciplinaIncluida"));
        obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina"));
        obj.setNovoObj(Boolean.FALSE);
             
        return obj;
    }
    
    

}
