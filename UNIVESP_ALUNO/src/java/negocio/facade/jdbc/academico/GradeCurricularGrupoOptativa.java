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

import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.GradeCurricularGrupoOptativaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class GradeCurricularGrupoOptativa extends ControleAcesso implements GradeCurricularGrupoOptativaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7921189397274856525L;
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception{
		if(gradeCurricularGrupoOptativaVO.isNovoObj()){
			incluir(gradeCurricularGrupoOptativaVO, situacaoGradeCurricular, usuario);
		}else{
			alterar(gradeCurricularGrupoOptativaVO, situacaoGradeCurricular, usuario);
		}
	}
	
	@Override
	public void validarDados(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws ConsistirException{
		if(gradeCurricularGrupoOptativaVO.getDescricao() == null || gradeCurricularGrupoOptativaVO.getDescricao().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeCurricularGrupoOptativa_descricao"));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final GradeCurricularGrupoOptativaVO obj, String situacaoGradeCurricular, final UsuarioVO usuario) throws Exception{
		validarDados(obj);
		obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO GradeCurricularGrupoOptativa ");
				sql.append(" ( gradeCurricular, descricao) VALUES (?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setInt(1, obj.getGradeCurricular().getCodigo());
				ps.setString(2, obj.getDescricao());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					obj.setNovoObj(false);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().incluirGradeCurricularGrupoOptativaDisciplinaVOs(obj, situacaoGradeCurricular, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final GradeCurricularGrupoOptativaVO obj, String situacaoGradeCurricular, final UsuarioVO usuario) throws Exception{
		validarDados(obj);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE GradeCurricularGrupoOptativa ");
				sql.append(" set gradeCurricular = ?,  descricao = ? WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setInt(1, obj.getGradeCurricular().getCodigo());
				ps.setString(2, obj.getDescricao());
				ps.setInt(3, obj.getCodigo());
				return ps;
			}
		}) == 0){
			incluir(obj, situacaoGradeCurricular, usuario);
			return;
		}
		getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().alterarGradeCurricularGrupoOptativaDisciplinaVOs(obj, situacaoGradeCurricular, usuario);
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		for(GradeCurricularGrupoOptativaVO obj:gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()){
			obj.setGradeCurricular(gradeCurricularVO);
			incluir(obj, situacaoGradeCurricular, usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		excluirGradeCurricularGrupoOptativaVOs(gradeCurricularVO, usuario);
		for(GradeCurricularGrupoOptativaVO obj:gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()){
			obj.setGradeCurricular(gradeCurricularVO);			
			persistir(obj, situacaoGradeCurricular, usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeCurricularGrupoOptativaVOs(GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM GradeCurricularGrupoOptativa WHERE gradeCurricular =  ").append(gradeCurricularVO.getCodigo()).append(" and codigo not in (0 ");
		for(GradeCurricularGrupoOptativaVO obj:gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()){
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}
	
	/**
	 * Toda vez que alterar aqui alterar o metodo montarDadosCompleto
	 * @return
	 */
	private StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("SELECT GradeCurricularGrupoOptativa.codigo, GradeCurricularGrupoOptativa.gradeCurricular, GradeCurricularGrupoOptativa.descricao, ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.codigo as \"GradeCurricularGrupoOptativaDisciplina.codigo\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.bimestre as \"GradeCurricularGrupoOptativaDisciplina.bimestre\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplina as \"GradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.cargaHoraria as \"GradeCurricularGrupoOptativaDisciplina.cargaHoraria\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.cargaHorariaPratica as \"GradeCurricularGrupoOptativaDisciplina.cargaHorariaPratica\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.horaAula as \"GradeCurricularGrupoOptativaDisciplina.horaAula\", ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina as \"GradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.nrCreditos as \"GradeCurricularGrupoOptativaDisciplina.nrCreditos\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro as \"GradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.configuracaoAcademico as \"GradeCurricularGrupoOptativaDisciplina.configuracaoAcademico\", ");
		sql.append(" configuracaoacademico.nome as \"configuracaoacademico.nome\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.diversificada as \"GradeCurricularGrupoOptativaDisciplina.diversificada\", ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplinaComposta as \"GradeCurricularGrupoOptativaDisciplina.disciplinaComposta\", ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculoNota as \"GradeCurricularGrupoOptativaDisciplina.formulaCalculoNota\", ");		
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculo as \"GradeCurricularGrupoOptativaDisciplina.formulaCalculo\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.disciplinaEstagio as \"GradeCurricularGrupoOptativaDisciplina.disciplinaEstagio\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.controlarRecuperacaoPelaDisciplinaPrincipal as \"gcgod.controlarRecuperacaoPelaDisciplinaPrincipal\",  ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.condicaoUsoRecuperacao as \"gcgod.condicaoUsoRecuperacao\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaCondicaoUsoRecuperacao as \"gcgod.variavelNotaCondicaoUsoRecuperacao\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.formulaCalculoNotaRecuperada as \"gcgod.formulaCalculoNotaRecuperada\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaFormulaCalculoNotaRecuperada as \"gcgod.variavelNotaFormulaCalculoNotaRecuperada\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.variavelNotaRecuperacao as \"gcgod.variavelNotaRecuperacao\", ");
		sql.append(" GradeCurricularGrupoOptativaDisciplina.utilizarEmissaoXmlDiploma as \"gcgod.utilizarEmissaoXmlDiploma\",  ");
		sql.append(" Disciplina.nome as \"Disciplina.nome\", disciplina.abreviatura \"disciplina.abreviatura\" ");
		sql.append(" FROM GradeCurricularGrupoOptativa ");
		sql.append(" left join GradeCurricularGrupoOptativaDisciplina on GradeCurricularGrupoOptativaDisciplina.gradeCurricularGrupoOptativa = GradeCurricularGrupoOptativa.codigo ");
		sql.append(" left join Disciplina on GradeCurricularGrupoOptativaDisciplina.disciplina = Disciplina.codigo ");
		sql.append(" left join configuracaoacademico on GradeCurricularGrupoOptativaDisciplina.configuracaoacademico = configuracaoacademico.codigo ");

		
		return sql;
	}

	@Override
	public List<GradeCurricularGrupoOptativaVO> consultarPorGradeCurricular(Integer gradeCurricular, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql;
		if(nivelMontarDados.equals(NivelMontarDados.TODOS)){
			sql = getSqlConsultaCompleta();
			sql.append(" WHERE GradeCurricularGrupoOptativa.gradeCurricular = ").append(gradeCurricular).append(" ORDER BY GradeCurricularGrupoOptativa.codigo, Disciplina.nome ");
		}else{
			sql = new StringBuilder("SELECT * FROM GradeCurricularGrupoOptativa WHERE gradeCurricular =  ").append(gradeCurricular);
		}				
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}
	
	@Override
	public GradeCurricularGrupoOptativaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql;
		if(nivelMontarDados.equals(NivelMontarDados.TODOS)){
			sql = getSqlConsultaCompleta();
			sql.append(" WHERE GradeCurricularGrupoOptativa.codigo = ").append(codigo).append(" ORDER BY GradeCurricularGrupoOptativa.codigo, Disciplina.nome");
		}else{
			sql = new StringBuilder("SELECT * FROM GradeCurricularGrupoOptativa WHERE gradeCurricular =  ").append(codigo);
		}				
		List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs = montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
		if(gradeCurricularGrupoOptativaVOs.isEmpty()){
			return null;
		}
		return gradeCurricularGrupoOptativaVOs.get(0);
	}
	
	private List<GradeCurricularGrupoOptativaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuario ) throws Exception{
		
		if(nivelMontarDados.equals(NivelMontarDados.TODOS)){
			return montarDadosCompleto(rs, usuario);
		}else{
			List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs = new ArrayList<GradeCurricularGrupoOptativaVO>(0);
			while(rs.next()){			
				gradeCurricularGrupoOptativaVOs.add(montarDados(rs, usuario));		
			}
			return gradeCurricularGrupoOptativaVOs;
		}
		
	}
	
	private List<GradeCurricularGrupoOptativaVO> montarDadosCompleto(SqlRowSet rs, UsuarioVO usuario) throws Exception{
		List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs = new ArrayList<GradeCurricularGrupoOptativaVO>(0);
		GradeCurricularGrupoOptativaVO curricularGrupoOptativaVO = null;
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
		Integer gradeCurricularGrupoOptativa = 0;
		while(rs.next()){			
			if(gradeCurricularGrupoOptativa == 0 || gradeCurricularGrupoOptativa != rs.getInt("codigo")){
				
				gradeCurricularGrupoOptativa = rs.getInt("codigo");
				curricularGrupoOptativaVO = montarDados(rs, usuario);
				gradeCurricularGrupoOptativaVOs.add(curricularGrupoOptativaVO);
			}
			if(rs.getInt("GradeCurricularGrupoOptativaDisciplina.codigo") != 0){
				gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
				gradeCurricularGrupoOptativaDisciplinaVO.setNovoObj(false);
				gradeCurricularGrupoOptativaDisciplinaVO.setCodigo(rs.getInt("GradeCurricularGrupoOptativaDisciplina.codigo"));
				gradeCurricularGrupoOptativaDisciplinaVO.setBimestre(rs.getInt("GradeCurricularGrupoOptativaDisciplina.bimestre"));
				gradeCurricularGrupoOptativaDisciplinaVO.setCargaHoraria(rs.getInt("GradeCurricularGrupoOptativaDisciplina.cargaHoraria"));
				gradeCurricularGrupoOptativaDisciplinaVO.setCargaHorariaPratica(rs.getInt("GradeCurricularGrupoOptativaDisciplina.cargaHorariaPratica"));
				gradeCurricularGrupoOptativaDisciplinaVO.setHoraAula(rs.getInt("GradeCurricularGrupoOptativaDisciplina.horaAula"));				
				gradeCurricularGrupoOptativaDisciplinaVO.setNrCreditos(rs.getInt("GradeCurricularGrupoOptativaDisciplina.nrCreditos"));
				gradeCurricularGrupoOptativaDisciplinaVO.setNrCreditoFinanceiro(rs.getDouble("GradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro"));
				gradeCurricularGrupoOptativaDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(rs.getString("GradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina")));
				gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().setNovoObj(false);
				gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().setCodigo(rs.getInt("GradeCurricularGrupoOptativaDisciplina.configuracaoAcademico"));
				gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().setNome(rs.getString("configuracaoacademico.nome"));
				gradeCurricularGrupoOptativaDisciplinaVO.setGradeCurricularGrupoOptativa(curricularGrupoOptativaVO);
				gradeCurricularGrupoOptativaDisciplinaVO.setDiversificada(rs.getBoolean("GradeCurricularGrupoOptativaDisciplina.diversificada"));
				gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaComposta(rs.getBoolean("GradeCurricularGrupoOptativaDisciplina.disciplinaComposta"));
				gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setNovoObj(false);
				gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("GradeCurricularGrupoOptativaDisciplina.disciplina"));
				gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setNome(rs.getString("Disciplina.nome"));
				gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setAbreviatura(rs.getString("disciplina.abreviatura"));
				gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaEstagio(rs.getBoolean("GradeCurricularGrupoOptativaDisciplina.disciplinaEstagio"));
				gradeCurricularGrupoOptativaDisciplinaVO.setControlarRecuperacaoPelaDisciplinaPrincipal(rs.getBoolean("gcgod.controlarRecuperacaoPelaDisciplinaPrincipal"));
				gradeCurricularGrupoOptativaDisciplinaVO.setCondicaoUsoRecuperacao(rs.getString("gcgod.condicaoUsoRecuperacao"));
				gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaCondicaoUsoRecuperacao(rs.getString("gcgod.variavelNotaCondicaoUsoRecuperacao"));
				gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculoNotaRecuperada(rs.getString("gcgod.formulaCalculoNotaRecuperada"));
				gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaFormulaCalculoNotaRecuperada(rs.getString("gcgod.variavelNotaFormulaCalculoNotaRecuperada"));
				gradeCurricularGrupoOptativaDisciplinaVO.setVariavelNotaRecuperacao(rs.getString("gcgod.variavelNotaRecuperacao"));
				gradeCurricularGrupoOptativaDisciplinaVO.setUtilizarEmissaoXmlDiploma(rs.getBoolean("gcgod.utilizarEmissaoXmlDiploma"));
				if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaComposta()){
					gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculoNota(FormulaCalculoNotaEnum.getEnum(rs.getString("GradeCurricularGrupoOptativaDisciplina.formulaCalculoNota")));
					gradeCurricularGrupoOptativaDisciplinaVO.setFormulaCalculo(rs.getString("GradeCurricularGrupoOptativaDisciplina.formulaCalculo"));
                    gradeCurricularGrupoOptativaDisciplinaVO.setGradeDisciplinaCompostaVOs(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				}
				gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaRequisitoVOs(getFacadeFactory().getDisciplinaPreRequisitoFacade().consultarGrupoOptativaPreRequisitos(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo(), false, usuario));

				curricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaVO);
			}					
		}
		return gradeCurricularGrupoOptativaVOs;
	}

	
	private GradeCurricularGrupoOptativaVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = new GradeCurricularGrupoOptativaVO();
		gradeCurricularGrupoOptativaVO.setNovoObj(false);
		gradeCurricularGrupoOptativaVO.setCodigo(rs.getInt("codigo"));
		gradeCurricularGrupoOptativaVO.getGradeCurricular().setCodigo(rs.getInt("gradeCurricular"));
		gradeCurricularGrupoOptativaVO.setDescricao(rs.getString("descricao"));
		return gradeCurricularGrupoOptativaVO;
	}
	
	@Override
	public void adicionarGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) throws Exception{
		getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().validarDados(gradeCurricularGrupoOptativaDisciplinaVO);
		for(GradeCurricularGrupoOptativaDisciplinaVO objExistente:gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
			if(objExistente.getDisciplina().getCodigo().intValue() == gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeCurricularGrupoOptativa_disciplinaExistente"));
			}
		}		
		gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaVO);
	}
	
	@Override
	public void removerGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) throws Exception{
		int index = 0;
		for(GradeCurricularGrupoOptativaDisciplinaVO objExistente:gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
			if(objExistente.getDisciplina().getCodigo().intValue() == gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()){
				gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().remove(index);
				return;
			}
			index++;
		}
		
	}
	
	@Override
	public GradeCurricularGrupoOptativaVO consultarPorPeriodoLetivo(Integer periodoLetivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select gradecurriculargrupooptativa.* from gradecurriculargrupooptativa  ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sb.append(" where periodoletivo.codigo = ").append(periodoLetivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = new GradeCurricularGrupoOptativaVO();
		if (tabelaResultado.next()) {
			gradeCurricularGrupoOptativaVO.setCodigo(tabelaResultado.getInt("codigo"));
			gradeCurricularGrupoOptativaVO.getGradeCurricular().setCodigo(tabelaResultado.getInt("gradeCurricular"));
			gradeCurricularGrupoOptativaVO.setDescricao(tabelaResultado.getString("descricao"));
		}
		return gradeCurricularGrupoOptativaVO;
				
	}

}
