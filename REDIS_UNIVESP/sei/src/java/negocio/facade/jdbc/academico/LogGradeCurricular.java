package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogGradeCurricularInterfaceFacade;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.LogGradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.arquitetura.UsuarioVO;

@Repository
@Scope("singleton")
@Lazy
public class LogGradeCurricular extends ControleAcesso implements LogGradeCurricularInterfaceFacade {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogGradeCurricularVO obj, final UsuarioVO usuario){
        try {
            final String sql = "INSERT INTO logGradeCurricular(usuarioResponsavel, data, operacao, gradeCurricular, periodoLetivo, gradeDisciplina, logAlteracao) VALUES (?,?,?,?,?,?,?) returning codigo";
            
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                  PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                  sqlInserir.setInt(1, usuario.getCodigo());
                  sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
                  sqlInserir.setString(3, obj.getOperacao().toString());
                  if (Uteis.isAtributoPreenchido(obj.getGradeCurricularVO())) {
                	  sqlInserir.setInt(4, obj.getGradeCurricularVO().getCodigo());
                  } else {
                	  sqlInserir.setNull(4, 0);
                  }
                  if (Uteis.isAtributoPreenchido(obj.getPeriodoLetivoVO())) {
                	  sqlInserir.setInt(5, obj.getPeriodoLetivoVO().getCodigo());
                  } else {
                	  sqlInserir.setNull(5, 0);
                  }
                  if (Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO())) {
                	  sqlInserir.setInt(6, obj.getGradeDisciplinaVO().getCodigo());
                  } else {
                	  sqlInserir.setNull(6, 0);
                  }
                  sqlInserir.setString(7, obj.getLogAlteracao());
                    return sqlInserir;
                }
         }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
              }
            }));
            getFacadeFactory().getLogImpactoMatrizCurricularFacade().incluirLogImpactosGradeDisciplina(obj.getCodigo(), obj.getListaLogImpactoGradeDisciplinaVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	public List<LogGradeCurricularVO> consultarPorGradeCurricular(Integer codigo, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT * FROM logGradeCurricular where gradecurricular = ").append(codigo).append(" order by data desc");
		List<LogGradeCurricularVO> vetResultado = new ArrayList<LogGradeCurricularVO>();

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			LogGradeCurricularVO obj = new LogGradeCurricularVO();
			try {
				montarLogGradeCurricularVO(obj, tabelaResultado, usuario);
			} catch (InvalidResultSetAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarLogGradeCurricularVO(LogGradeCurricularVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws InvalidResultSetAccessException, Exception {
		obj.setData(dadosSQL.getDate("data"));
		obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(new Integer(dadosSQL.getInt("usuarioresponsavel")), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setLogAlteracao(dadosSQL.getString("logAlteracao"));
		obj.setOperacao(TipoAlteracaoMatrizCurricularEnum.valueOf((dadosSQL.getString("operacao"))));

		JSONParser jsonParser=new JSONParser();
		Object object=null;
		object=jsonParser.parse(dadosSQL.getString("impacto"));
//		obj.setImpactos((JSONArray) object);		
	}
	
	@Override
	public void realizarCriacaoLogMatrizCurricularAlteracaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaVO, String logAlteracao, UsuarioVO usuarioVO) {
		LogGradeCurricularVO log = new LogGradeCurricularVO();
		String alteracoes = periodoLetivoVO.getDescricao() + " - " + gradeDisciplinaVO.getDescricaoCodigoNomeDisciplina() + " - " + logAlteracao;
		getFacadeFactory().getLogImpactoMatrizCurricularFacade().inicializarDadosJsonImpactos(gradeDisciplinaVO.getListaLogImpactoGradeDisciplinaVOs());
		log.inicializarDadosLogGradeCurricular(alteracoes, gradeDisciplinaVO.getListaLogImpactoGradeDisciplinaVOs(), usuarioVO, TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA, gradeCurricularVO.getCodigo(), periodoLetivoVO.getCodigo(), gradeDisciplinaVO.getCodigo());
		getFacadeFactory().getLogGradeCurricularInterfaceFacade().incluir(log, usuarioVO);
	}
	
	@Override
	public void realizarCriacaoLogMatrizCurricularInclusaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaVO, String logInclusao, UsuarioVO usuarioVO) {
		String alteracoes = periodoLetivoVO.getDescricao() + " - Inclusão de disciplina: " + logInclusao;
		LogGradeCurricularVO obj = new LogGradeCurricularVO();
		getFacadeFactory().getLogImpactoMatrizCurricularFacade().inicializarDadosJsonImpactos(gradeDisciplinaVO.getListaLogImpactoGradeDisciplinaVOs());	
		obj.montarDadosPorMatricula(alteracoes, usuarioVO, TipoAlteracaoMatrizCurricularEnum.ADICIONAR_GRADE_DISCIPLINA, gradeCurricularVO.getCodigo(), periodoLetivoVO.getCodigo(), gradeDisciplinaVO.getCodigo());
		getFacadeFactory().getLogGradeCurricularInterfaceFacade().incluir(obj, usuarioVO);
	}
	
	@Override
	public void realizarCriacaoLogExclusaoGradeDisciplina(Integer gradeCurricular, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO) {
		LogGradeCurricularVO log = new LogGradeCurricularVO();
		String alteracoes = periodoLetivoVO.getDescricao() + " - " + gradeDisciplinaExcluirVO.getDescricaoCodigoNomeDisciplina();
		getFacadeFactory().getLogImpactoMatrizCurricularFacade().inicializarDadosJsonImpactos(gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs());
		log.inicializarDadosLogGradeCurricular(alteracoes, gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs(), usuarioVO, TipoAlteracaoMatrizCurricularEnum.EXCLUIR_GRADE_DISCIPLINA, gradeCurricular, periodoLetivoVO.getCodigo(), gradeDisciplinaExcluirVO.getCodigo());
		getFacadeFactory().getLogGradeCurricularInterfaceFacade().incluir(log, usuarioVO);
	}
	
}