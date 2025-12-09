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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaDisciplinaNotaParcialInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TurmaDisciplinaNotaParcial extends ControleAcesso implements TurmaDisciplinaNotaParcialInterfaceFacade {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1218700830073209787L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, final UsuarioVO usuarioVO) throws Exception {
		final String sql = "INSERT INTO turmaDisciplinaNotaParcial( tituloNota, variavel, turmaDisciplinaNotaTitulo) VALUES ( ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		turmaDisciplinaNotaParcialVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, turmaDisciplinaNotaParcialVO.getTituloNota());
				sqlInserir.setString(2, turmaDisciplinaNotaParcialVO.getVariavel());
				sqlInserir.setInt(3, turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getCodigo());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					turmaDisciplinaNotaParcialVO.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO,UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE turmaDisciplinaNotaParcial set tituloNota = ?, variavel = ?, turmaDisciplinaNotaTitulo = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, turmaDisciplinaNotaParcialVO.getTituloNota());
				sqlAlterar.setString(2, turmaDisciplinaNotaParcialVO.getVariavel());
				sqlAlterar.setInt(3, turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getCodigo());
				Uteis.setValuePreparedStatement(turmaDisciplinaNotaParcialVO.getCodigo(), 4, sqlAlterar);
				return sqlAlterar;

			}
		});

	}
	
	@Override
	public List<TurmaDisciplinaNotaParcialVO> consultarPorTurmaDisciplinaNotaTitulo(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select * from turmaDisciplinaNotaParcial ");
		sql.append(" where turmaDisciplinaNotaTitulo = ").append(turmaDisciplinaNotaTituloVO.getCodigo());
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);			
	}

	public List<TurmaDisciplinaNotaParcialVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
		List<TurmaDisciplinaNotaParcialVO> turmaDisciplinaNotaParcialVOs = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
		while(rs.next()){			
			turmaDisciplinaNotaParcialVOs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		rs = null;
		return  turmaDisciplinaNotaParcialVOs;
	}
	
	public TurmaDisciplinaNotaParcialVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
		TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO = new TurmaDisciplinaNotaParcialVO();
		turmaDisciplinaNotaParcialVO.setCodigo(rs.getInt("codigo"));
		turmaDisciplinaNotaParcialVO.setTituloNota(rs.getString("tituloNota"));
		turmaDisciplinaNotaParcialVO.setVariavel(rs.getString("variavel"));
		turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().setCodigo(rs.getInt("turmaDisciplinaNotaTitulo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return turmaDisciplinaNotaParcialVO;
		}
		turmaDisciplinaNotaParcialVO.setTurmaDisciplinaNotaTituloVO(getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().consultarPorChavePrimaria(turmaDisciplinaNotaParcialVO.getTurmaDisciplinaNotaTituloVO().getCodigo(), usuarioVO));
		return turmaDisciplinaNotaParcialVO;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql =  new StringBuilder("delete from turmaDisciplinaNotaParcial where codigo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), turmaDisciplinaNotaParcialVO.getCodigo());
	}
	
	@Override
	public List<TurmaDisciplinaNotaParcialVO> consultarPorTurmaDisciplina(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select turmadisciplinanotaparcial.*, turmadisciplinanotatitulo.nota    from turmadisciplinanotatitulo ");
		sql.append(" inner join turmadisciplinanotaparcial ");
		sql.append(" on turmadisciplinanotaparcial.turmadisciplinanotatitulo=turmadisciplinanotatitulo.codigo ");
		sql.append(" where turma = ").append(turma.getCodigo());
		sql.append(" and disciplina = ").append(disciplina.getCodigo());
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and turmaDisciplinaNotaTitulo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and turmaDisciplinaNotaTitulo.semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sql.append(" and turmaDisciplinaNotaTitulo.configuracaoAcademico = '").append(configuracaoAcademico).append("' ");
		}
		sql.append(" order by turmadisciplinanotaparcial.codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);			
	}

	@Override
	public List<TurmaDisciplinaNotaParcialVO> consultarPorTurmaDisciplinaTipoNota(TurmaVO turma, DisciplinaVO disciplina, String tipoNota, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select * from turmaDisciplinaNotaParcial ");
		sql.append(" inner join turmadisciplinanotatitulo  on turmaDisciplinaNotaParcial.turmadisciplinanotatitulo=turmadisciplinanotatitulo.codigo");
		if(!turma.getSubturma()) {
			sql.append(" where turmaDisciplinaNotaTitulo.turma = ").append(turma.getCodigo());
		}
		else {
			sql.append(" where turmaDisciplinaNotaTitulo.turma = ").append(turma.getTurmaPrincipal());
		}		
		sql.append(" and turmaDisciplinaNotaTitulo.disciplina = ").append(disciplina.getCodigo());
		sql.append(" and turmaDisciplinaNotaTitulo.nota = '").append(tipoNota).append("'");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and turmaDisciplinaNotaTitulo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and turmaDisciplinaNotaTitulo.semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sql.append(" and turmaDisciplinaNotaTitulo.configuracaoAcademico = '").append(configuracaoAcademico).append("' ");
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);		
	}
	
	public void adicionarTurmaDisciplinaNotaParcialItem(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo)throws Exception{
		
		if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaParcialVO.getTituloNota())) {
			throw new Exception("O campo Título Nota Parcial deve ser informado!");
		}
		turmaDisciplinaNotaTitulo.getTurmaDisciplinaNotaParcialVOs().add(turmaDisciplinaNotaParcialVO);
		int count = 1;
		for (TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial : turmaDisciplinaNotaTitulo.getTurmaDisciplinaNotaParcialVOs()) {
			if(count < 10) {
				turmaDisciplinaNotaParcialVO.setVariavel("N0"+ count);
			}else {
				turmaDisciplinaNotaParcialVO.setVariavel("N"+ count);
			}
			count ++;
		}

	}
	
	public void removerTurmaDisciplinaNotaParcialItem(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo, UsuarioVO usuarioVO) throws Exception {
		
		if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaParcialVO.getCodigo())) {
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().excluir(turmaDisciplinaNotaParcialVO, usuarioVO);
			turmaDisciplinaNotaTitulo.getTurmaDisciplinaNotaParcialVOs().remove(turmaDisciplinaNotaParcialVO);
		}else {
			turmaDisciplinaNotaTitulo.getTurmaDisciplinaNotaParcialVOs().remove(turmaDisciplinaNotaParcialVO);
		}
		int count = 1;
		for (TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial : turmaDisciplinaNotaTitulo.getTurmaDisciplinaNotaParcialVOs()) {
			turmaDisciplinaNotaParcial.setVariavel("N"+ count);
			count ++;
		}
	}

}
