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

import negocio.comuns.academico.HistoricoEquivalenteVO;
import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HistoricoEquivalenteInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class HistoricoEquivalente extends ControleAcesso implements HistoricoEquivalenteInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public HistoricoEquivalente() {
		super();
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception {

        final String sql = "INSERT INTO HistoricoEquivalente( mapaEquivalenciaDisciplina) VALUES ( ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getMapaEquivalenciaDisciplinaVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getMapaEquivalenciaDisciplinaVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }                
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
    }
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE HistoricoEquivalente set mapaEquivalenciaDisciplina=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
	                if (obj.getMapaEquivalenciaDisciplinaVO().getCodigo().intValue() != 0) {
	                	sqlAlterar.setInt(1, obj.getMapaEquivalenciaDisciplinaVO().getCodigo().intValue());
	                } else {
	                	sqlAlterar.setNull(1, 0);
	                }       
	                return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception {
//        MatriculaPeriodo.excluir(getIdEntidade());
        String sql = "DELETE FROM HistoricoEquivalente WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }
    
	public List<HistoricoGradeAnteriorAlteradaVO> consultarHistoricoGradePorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct historicograde.codigo AS \"historicograde.codigo\", disciplina.nome AS \"disciplina.nome\", disciplina.codigo AS \"disciplina.codigo\", ");
		sb.append(" historicograde.situacao, mediafinal, frequencia, anohistorico, semestrehistorico, instituicao, isentarMediaFinal, cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", ");
		sb.append(" cargahorariacursada, matriculaperiodo.codigo AS \"matriculaperiodo.codigo\", transferenciamatrizcurricular.codigo AS \"transferenciamatrizcurricular.codigo\" ");
		sb.append(" from historicograde ");
		sb.append(" inner join disciplina on disciplina.codigo = historicograde.disciplina ");
		sb.append(" inner join transferenciamatrizcurricular on transferenciamatrizcurricular.codigo = historicograde.transferenciamatrizcurricular ");
		sb.append(" left join cidade on cidade.codigo = historicograde.cidade ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historicograde.matriculaperiodoapresentarhistorico ");
		sb.append(" where transferenciamatrizcurricular.matricula = '").append(matricula).append("' ");
		sb.append(" and gradeorigem = ").append(gradeCurricular);
		sb.append(" order by disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs = new ArrayList<HistoricoGradeAnteriorAlteradaVO>(0);
		while (tabelaResultado.next()) {
			HistoricoGradeAnteriorAlteradaVO obj = new HistoricoGradeAnteriorAlteradaVO();
			montarDados(tabelaResultado, obj, usuarioVO);
			historicoGradeAnteriorAlteradaVOs.add(obj);
		}
		return historicoGradeAnteriorAlteradaVOs;
	}
	
	public void montarDados(SqlRowSet dadoosSQL, HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuarioVO) {
		obj.getHistoricoGradeVO().setCodigo(dadoosSQL.getInt("historicoGrade.codigo"));
		obj.getDisciplinaVO().setNome(dadoosSQL.getString("disciplina.nome"));
		obj.getDisciplinaVO().setCodigo(dadoosSQL.getInt("disciplina.codigo"));
		obj.setSituacao(dadoosSQL.getString("situacao"));
		obj.setMediaFinal(dadoosSQL.getDouble("mediaFinal"));
		obj.setFrequencia(dadoosSQL.getDouble("frequencia"));
		obj.setAno(dadoosSQL.getString("anoHistorico"));
		obj.setSemestre(dadoosSQL.getString("semestreHistorico"));
		obj.setInstituicao(dadoosSQL.getString("instituicao"));
		obj.getCidadeVO().setCodigo(dadoosSQL.getInt("cidade.codigo"));
		obj.getCidadeVO().setNome(dadoosSQL.getString("cidade.nome"));
		obj.setCargaHorariaCursada(dadoosSQL.getInt("cargaHorariaCursada"));
		obj.getMatriculaPeriodoVO().setCodigo(dadoosSQL.getInt("matriculaPeriodo.codigo"));
		obj.getTransferenciaMatrizCurricularVO().setCodigo(dadoosSQL.getInt("transferenciamatrizcurricular.codigo"));
		obj.setIsentarMediaFinal(dadoosSQL.getBoolean("isentarMediaFinal"));
	}
	
    public void adicionarHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
    	validarDadosInclusaoHistoricoGrade(obj);
        int index = 0;
        Iterator i = listaHistoricoGradeAnteriorVOs.iterator();
        while (i.hasNext()) {
        	HistoricoGradeAnteriorAlteradaVO objExistente = (HistoricoGradeAnteriorAlteradaVO) i.next();
            if (objExistente.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo())
            		&& objExistente.getMatriculaPeriodoVO().getCodigo().equals(obj.getMatriculaPeriodoVO().getCodigo())) {
            	listaHistoricoGradeAnteriorVOs.set(index, obj);
                return;
            }
            index++;
        }
        obj.setAcao("INCLUSAO");
        listaHistoricoGradeAnteriorVOs.add(obj);
        Ordenacao.ordenarLista(listaHistoricoGradeAnteriorVOs, "nomeDisciplina");
    }

    public void removerHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
    	int index = 0;
    	Iterator i = listaHistoricoGradeAnteriorVOs.iterator();
    	while (i.hasNext()) {
    		HistoricoGradeAnteriorAlteradaVO objExistente = (HistoricoGradeAnteriorAlteradaVO) i.next();
    		if (objExistente.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo())
            		&& objExistente.getMatriculaPeriodoVO().getCodigo().equals(obj.getMatriculaPeriodoVO().getCodigo())) {
    			listaHistoricoGradeAnteriorVOs.remove(index);
    			return;
    		}
    		index++;
    	}
    	Ordenacao.ordenarLista(listaHistoricoGradeAnteriorVOs, "nomeDisciplina");
    }
    
    public void validarDados(HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
    	if (obj.getMatriculaVO().getMatricula().equals("")) {
    		throw new Exception("O campo Matrícula deve ser informado!");
    	}
    	if (obj.getGradeCurricularVO().getCodigo().equals(0)) {
    		throw new Exception("O campo Grade Curricular deve ser informado!");
    	}
    	if (obj.getDisciplinaVO().getCodigo().equals(0)) {
    		throw new Exception("O campo Disciplina deve ser informado!");
    	}
    	if (obj.getSituacao().equals("")) {
    		throw new Exception("O campo Situação deve ser informado!");
    	}
    }
    
    public void validarDadosInclusaoHistoricoGrade(HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
    	if (obj.getDisciplinaVO().getCodigo().equals(0)) {
    		throw new Exception("O campo Disciplina deve ser informado!");
    	}
    	if (obj.getSituacao().equals("")) {
    		throw new Exception("O campo Situação deve ser informado!");
    	}
    }

    
}
