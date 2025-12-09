package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HistoricoNotaParcialInterfaceFacade;

@Repository
public class HistoricoNotaParcial extends ControleAcesso implements HistoricoNotaParcialInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2540257013526895814L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HistoricoNotaParcialVO historicoNotaParcialVO,UsuarioVO usuarioVO) throws Exception {
		final String sql = "INSERT INTO historicoNotaParcial(historico, tipoNota, turmaDisciplinaNotaParcial, usuario) VALUES ( ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		historicoNotaParcialVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, historicoNotaParcialVO.getHistorico().getCodigo());
				sqlInserir.setString(2, historicoNotaParcialVO.getTipoNota().name());
				sqlInserir.setInt(3, historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getCodigo());
				sqlInserir.setInt(4, usuarioVO.getCodigo());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					historicoNotaParcialVO.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(HistoricoNotaParcialVO historicoNotaParcialVO, UsuarioVO usuarioVO) throws Exception{
		final String sql = "UPDATE historicoNotaParcial set nota = ?, dataAlteracao = ?, usuario = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if(Uteis.isAtributoPreenchido(historicoNotaParcialVO.getNota())) {
					sqlAlterar.setDouble(1, historicoNotaParcialVO.getNota());
				}
				else {
					sqlAlterar.setNull(1, 0);
				}
				
				sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
				sqlAlterar.setInt(3, usuarioVO.getCodigo());
//				Uteis.setValuePreparedStatement(historicoNotaParcialVO.getCodigo(), 4, sqlAlterar);
				return sqlAlterar;

			}
		});
	}
		
	@Override
	public List<HistoricoNotaParcialVO> consultarPorHistorico(HistoricoVO historicoVO, String tipoNota, UsuarioVO usuarioVO, String ano, String semestre, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBaseConsulta());
		
		sql.append(" inner join turma on turmaDisciplinaNotaTitulo.turma = turma.codigo ");
		sql.append(" where historicoNotaParcial.historico = ").append(historicoVO.getCodigo());		
		sql.append(" and historicoNotaParcial.tipoNota = '").append(tipoNota).append("'");		
		sql.append(" and (turmaDisciplinaNotaTitulo.turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
		sql.append(" or turmaDisciplinaNotaTitulo.turma in (select turmaOrigem from turmaAgrupada where turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
		sql.append("))");
		sql.append(" and case when turma.anual then ( turmaDisciplinaNotaTitulo.ano = '").append(ano).append("')");
		sql.append(" else case when turma.semestral then (turmaDisciplinaNotaTitulo.ano = '").append(ano).append("' and turmaDisciplinaNotaTitulo.semestre = '").append(semestre).append("')");
		sql.append(" else turma.anual = false and turma.semestral = false end end");
		if(Uteis.isAtributoPreenchido(historicoVO.getConfiguracaoAcademico().getCodigo())) {
			sql.append(" and turmaDisciplinaNotaTitulo.configuracaoacademico = ").append(historicoVO.getConfiguracaoAcademico().getCodigo());
		}
		sql.append(" order by historicoNotaParcial.codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);			
	}
	
	@Override
	public void carregarHistoricoNotaParcialPorHistorico(HistoricoVO historicoVO, int nivelMontarDados)throws Exception {	
		if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo())) {
		StringBuilder sql = new StringBuilder(getSqlBaseConsulta());
		sql.append(" where historicoNotaParcial.historico = ").append(historicoVO.getCodigo());						
		sql.append(" and (turmaDisciplinaNotaTitulo.turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
		sql.append(" or turmaDisciplinaNotaTitulo.turma in (select turmaOrigem from turmaAgrupada where turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
		sql.append("))");
		if(Uteis.isAtributoPreenchido(historicoVO.getConfiguracaoAcademico().getCodigo())) {
			sql.append(" and turmaDisciplinaNotaTitulo.configuracaoacademico = ").append(historicoVO.getConfiguracaoAcademico().getCodigo());
		}
		sql.append(" order by historicoNotaParcial.codigo");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		for(int x = 1; x < 40; x++) {
			((List<HistoricoNotaParcialVO>) UtilReflexao.invocarMetodoGet(historicoVO, "historicoNotaParcialNota"+x+"VOs")).clear();
		}
		while(rs.next()){			
			HistoricoNotaParcialVO historicoNotaParcialVO = montarDados(rs, nivelMontarDados);
			((List<HistoricoNotaParcialVO>) UtilReflexao.invocarMetodoGet(historicoVO, "historicoNotaParcialNota"+historicoNotaParcialVO.getTipoNota().getNumeroNota()+"VOs")).add(historicoNotaParcialVO);
		}			
		}
	}
	
	
	@Override
	public void carregarHistoricoNotaParcialPorHistorico(List<HistoricoVO> historicoVOs, int nivelMontarDados)throws Exception {		
		StringBuilder sql = new StringBuilder("");
		Map<Integer, HistoricoVO> mapHistoricos =  new HashMap<Integer, HistoricoVO>();
		for(HistoricoVO historicoVO:historicoVOs) { 
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo())) {
				for(int x = 1; x < 40; x++) {
					((List<HistoricoNotaParcialVO>) UtilReflexao.invocarMetodoGet(historicoVO, "historicoNotaParcialNota"+x+"VOs")).clear();
				}
				if(!mapHistoricos.isEmpty()) {
					sql.append(" union all ");
				}
				sql.append(getSqlBaseConsulta());
				sql.append(" where historicoNotaParcial.historico = ").append(historicoVO.getCodigo());							
				sql.append(" and (turmaDisciplinaNotaTitulo.turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
				sql.append(" or turmaDisciplinaNotaTitulo.turma in (select turmaOrigem from turmaAgrupada where turma = ").append(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
				sql.append("))");
				if(Uteis.isAtributoPreenchido(historicoVO.getConfiguracaoAcademico().getCodigo())) {
					sql.append(" and turmaDisciplinaNotaTitulo.configuracaoacademico = ").append(historicoVO.getConfiguracaoAcademico().getCodigo());
				}

				mapHistoricos.put(historicoVO.getCodigo(), historicoVO);
			}
		}
		if (Uteis.isAtributoPreenchido(sql)) {
			sql.append(" order by historico, codigo");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while(rs.next()){			
				HistoricoNotaParcialVO historicoNotaParcialVO = montarDados(rs, nivelMontarDados);
				if(mapHistoricos.containsKey(historicoNotaParcialVO.getHistorico().getCodigo())) {
					((List<HistoricoNotaParcialVO>) UtilReflexao.invocarMetodoGet(mapHistoricos.get(historicoNotaParcialVO.getHistorico().getCodigo()), "historicoNotaParcialNota"+historicoNotaParcialVO.getTipoNota().getNumeroNota()+"VOs")).add(historicoNotaParcialVO);
				}
			}			
		}
	}
	
	

	public List<HistoricoNotaParcialVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception{
		List<HistoricoNotaParcialVO> historicoNotaParcialVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		while(rs.next()){			
			historicoNotaParcialVOs.add(montarDados(rs, nivelMontarDados));
		}
		rs = null;
		return  historicoNotaParcialVOs;
	}
	
	public HistoricoNotaParcialVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception{
		HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();
		historicoNotaParcialVO.setCodigo(rs.getInt("codigo"));
		historicoNotaParcialVO.getHistorico().setCodigo(rs.getInt("historico"));
		historicoNotaParcialVO.setTipoNota(TipoNotaConceitoEnum.valueOf(rs.getString("tipoNota")));
		historicoNotaParcialVO.setNota(rs.getDouble("nota"));
		historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().setCodigo(rs.getInt("turmaDisciplinaNotaParcial"));
		historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().setTituloNota(rs.getString("tituloNota"));
		historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().setPossuiFormula(rs.getBoolean("possuiFormula"));
		historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().setFormula(rs.getString("formula"));
		historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().setVariavel(rs.getString("variavel"));
		return historicoNotaParcialVO;
	}
	
	public StringBuilder getSqlBaseConsulta() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select historicoNotaParcial.*, turmaDisciplinaNotaParcial.tituloNota, turmaDisciplinaNotaParcial.variavel,  turmaDisciplinaNotaTitulo.possuiFormula,  turmaDisciplinaNotaTitulo.formula from historicoNotaParcial ");
		sql.append(" inner join turmaDisciplinaNotaParcial on historicoNotaParcial.turmaDisciplinaNotaParcial = turmaDisciplinaNotaParcial.codigo");
		sql.append(" inner join turmaDisciplinaNotaTitulo on turmaDisciplinaNotaParcial.turmaDisciplinaNotaTitulo = turmaDisciplinaNotaTitulo.codigo");
		return sql;
	}
	
	@Override
	public List<HistoricoNotaParcialVO> consultarPorHistoricoTurmaDisciplinaNotaParcialTurmaDisciplinaNotaTitulo(HistoricoNotaParcialVO historicoNotaParcialVO, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBaseConsulta());
		sql.append(" where historicoNotaParcial.historico = ").append(historicoNotaParcialVO.getHistorico().getCodigo());
		sql.append(" and turmaDisciplinaNotaParcial.codigo = ").append(historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getCodigo());
		sql.append(" and turmaDisciplinaNotaTitulo.codigo = ").append(historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().getCodigo());
		sql.append(" and historicoNotaParcial.tipoNota = '").append(historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().getNota()).append("'");
		sql.append(" order by historicoNotaParcial.codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);			
	}
	
	public void alterarHistoricosNotaParcial(List<HistoricoNotaParcialVO> historicoNotaParcialVOs, HistoricoVO historicoVO, UsuarioVO usuario) throws Exception{

		if(historicoNotaParcialVOs.get(0).getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().getPossuiFormula()) {
			int index = 1;
			String formula = historicoNotaParcialVOs.get(0).getTurmaDisciplinaNotaParcial().getTurmaDisciplinaNotaTituloVO().getFormula();

			for(HistoricoNotaParcialVO historicoNotaParcialVO : historicoNotaParcialVOs) {
				String variavel = "";
				if(index < 10) {
					variavel = "N0";
				}else {
					variavel = "N";
				}
				if((variavel + index).equals(historicoNotaParcialVO.getTurmaDisciplinaNotaParcial().getVariavel())) {
					if(historicoNotaParcialVO.getNota() != null) {
						formula = formula.replace(variavel + index, historicoNotaParcialVO.getNota().toString());
					}
				}	
				index++;
			}
			Double notaFinal = Uteis.realizarCalculoFormula(formula);
			
			if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_1)) {
				historicoVO.setNota1(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_2)) {
				historicoVO.setNota2(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_3)) {
				historicoVO.setNota3(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_4)) {
				historicoVO.setNota4(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_5)) {
				historicoVO.setNota5(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_6)) {
				historicoVO.setNota6(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_7)) {
				historicoVO.setNota7(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_8)) {
				historicoVO.setNota8(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_9)) {
				historicoVO.setNota9(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_10)) {
				historicoVO.setNota10(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_11)) {
				historicoVO.setNota11(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_12)) {
				historicoVO.setNota12(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_13)) {
				historicoVO.setNota13(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_14)) {
				historicoVO.setNota14(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_15)) {
				historicoVO.setNota15(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_16)) {
				historicoVO.setNota16(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_17)) {
				historicoVO.setNota17(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_18)) {
				historicoVO.setNota18(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_19)) {
				historicoVO.setNota19(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_20)) {
				historicoVO.setNota20(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_21)) {
				historicoVO.setNota21(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_22)) {
				historicoVO.setNota22(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_23)) {
				historicoVO.setNota23(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_24)) {
				historicoVO.setNota24(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_25)) {
				historicoVO.setNota25(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_26)) {
				historicoVO.setNota26(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_27)) {
				historicoVO.setNota27(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_28)) {
				historicoVO.setNota28(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_29)) {
				historicoVO.setNota29(notaFinal);
			}	
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_30)) {
				historicoVO.setNota30(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_31)) {
				historicoVO.setNota31(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_32)) {
				historicoVO.setNota32(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_33)) {
				historicoVO.setNota33(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_34)) {
				historicoVO.setNota34(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_35)) {
				historicoVO.setNota35(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_36)) {
				historicoVO.setNota36(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_37)) {
				historicoVO.setNota37(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_38)) {
				historicoVO.setNota38(notaFinal);
			}			
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_39)) {
				historicoVO.setNota39(notaFinal);
			}
			else if(historicoNotaParcialVOs.get(0).getTipoNota().equals(TipoNotaConceitoEnum.NOTA_40)) {
				historicoVO.setNota40(notaFinal);
			}
					
		}	
		
		for (HistoricoNotaParcialVO historicoNotaParcialVO : historicoNotaParcialVOs) {

			if(historicoNotaParcialVO.getNota() != null) {
				getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().alterar(historicoNotaParcialVO, usuario);
			}			
			
		}
		
		historicoVO.setHistoricoAlterado(Boolean.TRUE);
		
	}
	
	@Override
	public List<HistoricoNotaParcialVO> consultarPorCodigoHistorico(Integer codigoHistorico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBaseConsulta());
		
		sql.append(" inner join turma on turmaDisciplinaNotaTitulo.turma = turma.codigo ");
		sql.append(" where historicoNotaParcial.historico = ").append(codigoHistorico);	
		sql.append(" order by historicoNotaParcial.codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);			
	}
		
}
