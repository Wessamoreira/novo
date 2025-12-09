package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.SituacaoCriterioAvaliacaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoInterfaceFacade;

@Repository
@Lazy
public class CriterioAvaliacao extends ControleAcesso implements CriterioAvaliacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -226366039543440238L;
	private static String idEntidade = "CriterioAvaliacao";

	@Override
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		criterioAvaliacaoVO.getUsuarioCadastro().setCodigo(usuario.getCodigo());
		criterioAvaliacaoVO.getUsuarioCadastro().setNome(usuario.getNome());
		if(criterioAvaliacaoVO.isNovoObj()){
			criterioAvaliacaoVO.setDataCadastro(new Date());
			incluir(criterioAvaliacaoVO, verificarAcesso, usuario);
		}else{
			alterar(criterioAvaliacaoVO, verificarAcesso, usuario);
		}		
	}
	
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		incluir(getIdEntidade(), verificarAcesso, usuario);
		validarDados(criterioAvaliacaoVO);
		try{
			criterioAvaliacaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO CriterioAvaliacao (");
					sql.append(" unidadeEnsino, curso, gradecurricular, anovigencia, situacao, dataCadastro, usuarioCadastro, "); 
					sql.append(" dataAtivacao, usuarioAtivacao, dataInativacao, usuarioInativacao ) "); 
					sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?) returning codigo "); 
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoVO.getUnidadeEnsino().getCodigo());
					ps.setInt(x++, criterioAvaliacaoVO.getCurso().getCodigo());
					ps.setInt(x++, criterioAvaliacaoVO.getGradeCurricularVO().getCodigo());
					ps.setString(x++, criterioAvaliacaoVO.getAnoVigencia());
					ps.setString(x++, criterioAvaliacaoVO.getSituacao().name());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataCadastro()));
					ps.setInt(x++, criterioAvaliacaoVO.getUsuarioCadastro().getCodigo());
					if(criterioAvaliacaoVO.getDataAtivacao() != null){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataAtivacao()));
					}else{
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getUsuarioAtivacao() != null && criterioAvaliacaoVO.getUsuarioAtivacao().getCodigo() > 0){
						ps.setInt(x++, criterioAvaliacaoVO.getUsuarioAtivacao().getCodigo());
					}else{						
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getDataInativacao() != null){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataInativacao()));
					}else{
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getUsuarioInativacao() != null && criterioAvaliacaoVO.getUsuarioInativacao().getCodigo() > 0){
						ps.setInt(x++, criterioAvaliacaoVO.getUsuarioInativacao().getCodigo());
					}else{						
						ps.setNull(x++, 0);
					}
					
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if(arg0.next()){
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().incluirCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoVO);
			criterioAvaliacaoVO.setNovoObj(false);
		}catch(Exception e){
			if(e.getCause().getMessage().contains("unique_criterioAvaliacao_ue_curso_grade_ano")){
				throw new Exception(UteisJSF.internacionalizar("msg_criterioAvaliacao_unico"));
			}
			criterioAvaliacaoVO.setNovoObj(true);
			throw e;
		}
				
	}
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		alterar(getIdEntidade(), verificarAcesso, usuario);
		validarDados(criterioAvaliacaoVO);
		try{
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE CriterioAvaliacao SET ");
					sql.append(" unidadeEnsino = ?, curso = ?, gradecurricular = ?, anovigencia = ?, situacao = ?, dataCadastro = ?, usuarioCadastro = ?, "); 
					sql.append(" dataAtivacao = ?, usuarioAtivacao = ?, dataInativacao = ?, usuarioInativacao = ?  ");
					sql.append(" WHERE codigo = ? "); 
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoVO.getUnidadeEnsino().getCodigo());
					ps.setInt(x++, criterioAvaliacaoVO.getCurso().getCodigo());
					ps.setInt(x++, criterioAvaliacaoVO.getGradeCurricularVO().getCodigo());
					ps.setString(x++, criterioAvaliacaoVO.getAnoVigencia());
					ps.setString(x++, criterioAvaliacaoVO.getSituacao().name());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataCadastro()));
					ps.setInt(x++, criterioAvaliacaoVO.getUsuarioCadastro().getCodigo());
					if(criterioAvaliacaoVO.getDataAtivacao() != null){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataAtivacao()));
					}else{
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getUsuarioAtivacao() != null && criterioAvaliacaoVO.getUsuarioAtivacao().getCodigo() > 0){
						ps.setInt(x++, criterioAvaliacaoVO.getUsuarioAtivacao().getCodigo());
					}else{						
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getDataInativacao() != null){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoVO.getDataInativacao()));
					}else{
						ps.setNull(x++, 0);
					}
					if(criterioAvaliacaoVO.getUsuarioInativacao() != null && criterioAvaliacaoVO.getUsuarioInativacao().getCodigo() > 0){
						ps.setInt(x++, criterioAvaliacaoVO.getUsuarioInativacao().getCodigo());
					}else{						
						ps.setNull(x++, 0);
					}
					ps.setInt(x++, criterioAvaliacaoVO.getCodigo());
					return ps;
				}
			});		
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoVO);
		}catch(Exception e){
			throw e;
		}
		
	}

	@Override
	public void ativar(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		criterioAvaliacaoVO.setSituacao(SituacaoCriterioAvaliacaoEnum.ATIVO);
		criterioAvaliacaoVO.setDataAtivacao(new Date());
		criterioAvaliacaoVO.getUsuarioAtivacao().setCodigo(usuario.getCodigo());
		criterioAvaliacaoVO.getUsuarioAtivacao().setNome(usuario.getNome());
		try{
			persistir(criterioAvaliacaoVO, verificarAcesso, usuario);
		}catch(Exception e){
			criterioAvaliacaoVO.setSituacao(SituacaoCriterioAvaliacaoEnum.EM_CONSTRUCAO);
			throw e;
		}
		
	}

	@Override
	public void inativar(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		criterioAvaliacaoVO.setSituacao(SituacaoCriterioAvaliacaoEnum.INATIVO);
		criterioAvaliacaoVO.setDataInativacao(new Date());
		criterioAvaliacaoVO.getUsuarioInativacao().setCodigo(usuario.getCodigo());
		criterioAvaliacaoVO.getUsuarioInativacao().setNome(usuario.getNome());
		try{
			persistir(criterioAvaliacaoVO, verificarAcesso, usuario);
		}catch(Exception e){
			criterioAvaliacaoVO.setSituacao(SituacaoCriterioAvaliacaoEnum.ATIVO);
			throw e;
		}
		
	}
	@Override
	public void excluir(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuario);
		getConexao().getJdbcTemplate().update("DELETE FROM CriterioAvaliacao where codigo = ?", criterioAvaliacaoVO.getCodigo());
		
	}
	
	private StringBuilder getSqlBasico(){
		StringBuilder sql = new StringBuilder("SELECT CriterioAvaliacao.*, curso.nome as \"curso.nome\", ");
		sql.append(" unidadeEnsino.nome as \"unidadeEnsino.nome\", gradeCurricular.nome as \"gradeCurricular.nome\", ");
		sql.append(" responsavelAtivacao.nome as \"responsavelAtivacao.nome\", responsavelInativacao.nome as \"responsavelInativacao.nome\", ");
		sql.append(" responsavelCadastro.nome as \"responsavelCadastro.nome\" ");
		sql.append(" from CriterioAvaliacao ");
		sql.append(" inner join curso on curso.codigo = CriterioAvaliacao.curso ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CriterioAvaliacao.unidadeEnsino ");
		sql.append(" inner join gradeCurricular on gradeCurricular.codigo = CriterioAvaliacao.gradeCurricular ");
		sql.append(" left join Usuario as responsavelCadastro on responsavelCadastro.codigo = CriterioAvaliacao.usuarioCadastro ");
		sql.append(" left join Usuario as responsavelAtivacao on responsavelAtivacao.codigo = CriterioAvaliacao.usuarioAtivacao ");
		sql.append(" left join Usuario as responsavelInativacao on responsavelInativacao.codigo = CriterioAvaliacao.usuarioInativacao ");
		return sql;
	}

	@Override
	public CriterioAvaliacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" WHERE CriterioAvaliacao.codigo = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if(rs.next()){
			return montarDados(rs, nivelMontarDados);
		}
		throw new Exception("Dados não encontrados Critério de Avaliação.");
	}
	
	private List<CriterioAvaliacaoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception{
		List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = new ArrayList<CriterioAvaliacaoVO>(0);
		while(rs.next()){
			criterioAvaliacaoVOs.add(montarDados(rs, nivelMontarDados));
		}
		return criterioAvaliacaoVOs;
	}
	private CriterioAvaliacaoVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception{
		CriterioAvaliacaoVO criterioAvaliacaoVO = new CriterioAvaliacaoVO();
		criterioAvaliacaoVO.setNovoObj(false);
		criterioAvaliacaoVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		criterioAvaliacaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		criterioAvaliacaoVO.getGradeCurricularVO().setCodigo(rs.getInt("gradeCurricular"));
		criterioAvaliacaoVO.getGradeCurricularVO().setNome(rs.getString("gradeCurricular.nome"));
		criterioAvaliacaoVO.getCurso().setCodigo(rs.getInt("curso"));
		criterioAvaliacaoVO.getCurso().setNome(rs.getString("curso.nome"));
		criterioAvaliacaoVO.getUsuarioCadastro().setCodigo(rs.getInt("usuarioCadastro"));
		criterioAvaliacaoVO.getUsuarioAtivacao().setCodigo(rs.getInt("usuarioAtivacao"));
		criterioAvaliacaoVO.getUsuarioInativacao().setCodigo(rs.getInt("usuarioInativacao"));
		criterioAvaliacaoVO.getUsuarioInativacao().setNome(rs.getString("responsavelInativacao.nome"));
		criterioAvaliacaoVO.getUsuarioAtivacao().setNome(rs.getString("responsavelAtivacao.nome"));
		criterioAvaliacaoVO.getUsuarioCadastro().setNome(rs.getString("responsavelCadastro.nome"));
		criterioAvaliacaoVO.setAnoVigencia(rs.getString("anoVigencia"));
		criterioAvaliacaoVO.setSituacao(SituacaoCriterioAvaliacaoEnum.valueOf(rs.getString("situacao")));
		criterioAvaliacaoVO.setDataCadastro(rs.getDate("dataCadastro"));
		criterioAvaliacaoVO.setDataAtivacao(rs.getDate("dataAtivacao"));
		criterioAvaliacaoVO.setDataInativacao(rs.getDate("dataInativacao"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
			return criterioAvaliacaoVO;
		}
		criterioAvaliacaoVO.setCriterioAvaliacaoPeriodoLetivoVOs(getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().consultarPorCriterioAvaliacao(criterioAvaliacaoVO.getCodigo(), 0, nivelMontarDados));
		return criterioAvaliacaoVO;
	}

	@Override
	public List<CriterioAvaliacaoVO> consultar(String opcaoConsulta, String valorConsulta, Integer unidadeEnsino, boolean validarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" WHERE 1 = 1 ");
		getFiltroWhereConsultar(opcaoConsulta, unidadeEnsino, sql);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT), nivelMontarDados);
	}

	@Override
	public Integer consultarTotalRegistro(String opcaoConsulta, String valorConsulta, Integer unidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(CriterioAvaliacao.codigo) as qtde ");
		sql.append(" from CriterioAvaliacao ");
		sql.append(" inner join curso on curso.codigo = CriterioAvaliacao.curso ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CriterioAvaliacao.unidadeEnsino ");
		sql.append(" inner join gradeCurricular on gradeCurricular.codigo = CriterioAvaliacao.gradeCurricular ");
		sql.append(" WHERE 1 = 1 ");
		getFiltroWhereConsultar(opcaoConsulta, unidadeEnsino, sql);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void adicionarCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, Integer periodoLetivo , UsuarioVO usuarioVO) throws Exception {
		CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO = new CriterioAvaliacaoPeriodoLetivoVO();
		criterioAvaliacaoPeriodoLetivoVO.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(periodoLetivo, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().validarDados(criterioAvaliacaoPeriodoLetivoVO);		
		for(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO2:criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()){
			if(criterioAvaliacaoPeriodoLetivoVO2.getPeriodoLetivoVO().getCodigo().intValue() == criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().getCodigo().intValue()){
				return;
			}
		}
		@SuppressWarnings("unchecked")
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorPeriodoLetivo(periodoLetivo, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		for(DisciplinaVO disciplinaVO:disciplinaVOs){
			CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO = new CriterioAvaliacaoDisciplinaVO();
			criterioAvaliacaoDisciplinaVO.setDisciplina(disciplinaVO);
			criterioAvaliacaoDisciplinaVO.setOrdem(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().size()+1);
			criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().add(criterioAvaliacaoDisciplinaVO);
		}
		criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().add(criterioAvaliacaoPeriodoLetivoVO);
		Ordenacao.ordenarLista(criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs(), "ordenacao");
	}
	
	@Override
	public void excluirCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO , UsuarioVO usuarioVO) throws Exception {		
		int x = 0;		
		for(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO2:criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()){
			if(criterioAvaliacaoPeriodoLetivoVO2.getPeriodoLetivoVO().getCodigo().intValue() == criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().getCodigo().intValue()){
				criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().remove(x);
				return;
			}
			x++;
		}		
	}
	
	@Override
	public void adicionarTodosCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, UsuarioVO usuarioVO) throws Exception {		
		validarDados(criterioAvaliacaoVO);
		criterioAvaliacaoVO.setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		List<PeriodoLetivoVO> periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		for(PeriodoLetivoVO periodoLetivoVO:periodoLetivoVOs){
			adicionarCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoVO, periodoLetivoVO.getCodigo(), usuarioVO);	
		}
	}

	@Override
	public void validarDados(CriterioAvaliacaoVO criterioAvaliacaoVO) throws ConsistirException {
		if(criterioAvaliacaoVO.getUnidadeEnsino().getCodigo() == 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacao_unidadeEnsino"));
		}
		if(criterioAvaliacaoVO.getCurso().getCodigo() == 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacao_curso"));
		}
		if(criterioAvaliacaoVO.getGradeCurricularVO().getCodigo() == 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacao_gradeCurricular"));
		}
		if(criterioAvaliacaoVO.getAnoVigencia().trim().equals("")){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacao_anoVigencia"));
		}
		if(criterioAvaliacaoVO.getAnoVigencia().trim().length() != 4){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacao_anoVigenciaInvalido"));
		}
		
	}

	public static String getIdEntidade() {
		if (CriterioAvaliacao.idEntidade == null) {
			CriterioAvaliacao.idEntidade = "CriterioAvaliacao";
		}
		return CriterioAvaliacao.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CriterioAvaliacao.idEntidade = idEntidade;
	}
	
	private void getFiltroWhereConsultar(String opcaoConsulta, int unidadeEnsino, StringBuilder sql) {
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and criterioavaliacao.unidadeensino =  ").append(unidadeEnsino);
		}
		if (opcaoConsulta.equals("curso")) {
			sql.append(" and sem_acentos(curso.nome) ilike sem_acentos(?)");
		} else if (opcaoConsulta.equals("gradeCurricular")) {
			sql.append(" and sem_acentos(gradeCurricular.nome) ilike sem_acentos(?)");
		} else if (opcaoConsulta.equals("anoVigencia")) {
			sql.append(" and sem_acentos(anoVigencia) ilike sem_acentos(?)");
		}
	}
}
