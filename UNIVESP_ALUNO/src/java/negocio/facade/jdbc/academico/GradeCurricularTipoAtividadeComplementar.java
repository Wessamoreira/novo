package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

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

import negocio.comuns.academico.GradeCurricularTipoAtividadeComplementarVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.GradeCurricularTipoAtividadeComplementarInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class GradeCurricularTipoAtividadeComplementar extends ControleAcesso implements GradeCurricularTipoAtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public GradeCurricularTipoAtividadeComplementar() {
		super();
		this.setIdEntidade("TipoAtividadeComplementar");
	}

	public GradeCurricularTipoAtividadeComplementarVO novo() throws Exception {
		GradeCurricularTipoAtividadeComplementar.incluir(GradeCurricularTipoAtividadeComplementar.getIdEntidade());
		GradeCurricularTipoAtividadeComplementarVO obj = new GradeCurricularTipoAtividadeComplementarVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GradeCurricularTipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
			 * @author Leonardo Riciolle
			 * Comentado 28/10/2014
			 */
			// GradeCurricularTipoAtividadeComplementar.incluir(GradeCurricularTipoAtividadeComplementar.getIdEntidade());
			final String sql = "INSERT INTO gradeCurricularTipoAtividadeComplementar( cargaHoraria, tipoAtividadeComplementar, gradeCurricular, controlarhoramaximaporperiodoletivo, horamaximaporperiodoletivo, permiteCadastrarAtividadeParaAluno , horasMinimasExigida  ) " + "VALUES (?,?,?,?,?,?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getCargaHoraria());
					sqlInserir.setInt(2, obj.getTipoAtividadeComplementarVO().getCodigo());
					sqlInserir.setInt(3, obj.getGradeCurricularVO().getCodigo());
					sqlInserir.setBoolean(4, obj.isControlarHoraMaximaPorPeriodoLetivo());
					sqlInserir.setInt(5, obj.getHoraMaximaPorPeriodoLetivo());
					sqlInserir.setBoolean(6, obj.isPermiteCadastrarAtividadeParaAluno());
					sqlInserir.setInt(7, obj.getHorasMinimasExigida());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
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
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GradeCurricularTipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle
			 * Comentado 28/10/2014
			 */
			// GradeCurricularTipoAtividadeComplementar.excluir(GradeCurricularTipoAtividadeComplementar.getIdEntidade());
			String sql = "DELETE FROM gradeCurricularTipoAtividadeComplementar where gradeCurricular=?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getGradeCurricularVO().getCodigo() });
		} catch (Exception e) {
			throw e;
		}

	}

	public List<GradeCurricularTipoAtividadeComplementarVO> consultarPorCodigoGrade(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM gradeCurricularTipoAtividadeComplementar WHERE gradeCurricular = (" + valorConsulta + ") ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}
	
	public Integer consultarCargaHorariaObrigatoria(Integer gradecurricular) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select totalCargaHorariaAtividadeComplementar ");
		sqlStr.append(" from gradecurricular ");
		sqlStr.append(" where codigo = ").append(gradecurricular);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalCargaHorariaAtividadeComplementar");
		} 
		
		return 0;
	}

	public GradeCurricularTipoAtividadeComplementarVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		GradeCurricularTipoAtividadeComplementarVO obj = new GradeCurricularTipoAtividadeComplementarVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		obj.setHoraMaximaPorPeriodoLetivo(dadosSQL.getInt("horamaximaporperiodoletivo"));
		obj.setControlarHoraMaximaPorPeriodoLetivo(dadosSQL.getBoolean("controlarhoramaximaporperiodoletivo"));
		obj.setPermiteCadastrarAtividadeParaAluno(dadosSQL.getBoolean("permiteCadastrarAtividadeParaAluno"));
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradeCurricular"));
		obj.getTipoAtividadeComplementarVO().setCodigo(dadosSQL.getInt("tipoAtividadeComplementar"));
		obj.setTipoAtividadeComplementarVO(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(obj.getTipoAtividadeComplementarVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
		obj.setHorasMinimasExigida(dadosSQL.getInt("horasMinimasExigida"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public List<GradeCurricularTipoAtividadeComplementarVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<GradeCurricularTipoAtividadeComplementarVO> vetResultado = new ArrayList<GradeCurricularTipoAtividadeComplementarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static void validarDados(GradeCurricularTipoAtividadeComplementarVO obj) throws ConsistirException {
		if (obj.getTipoAtividadeComplementarVO().getCodigo() < 1) {
			throw new ConsistirException("O campo Tipo Atividade complementar deve ser selecionado.");
		}
		if (obj.getCargaHoraria() < 0) {
			throw new ConsistirException("O campo CARGA HORÁRIA (Atividade complementar - "+obj.getTipoAtividadeComplementarVO().getNome()+") deve ser informado");
		}
		if(obj.isControlarHoraMaximaPorPeriodoLetivo() && obj.getHoraMaximaPorPeriodoLetivo() == 0){
			throw new ConsistirException("O campo HORAS MÁXIMA POR PERÍODO LETIVO deve ser informado");
		}else if(!obj.isControlarHoraMaximaPorPeriodoLetivo()){
			obj.setHoraMaximaPorPeriodoLetivo(0);
		}
	}

	public void validarUnicidade(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO) throws Exception, ConsistirException {
		for (GradeCurricularTipoAtividadeComplementarVO vo : listaGradeCurricularTipoAtividadeComplementarVO) {
			if (vo.getTipoAtividadeComplementarVO().equals(obj.getTipoAtividadeComplementarVO())) {
				throw new ConsistirException("Já existe um Tipo Atividade Complementar.");
			}
		}
	}

	public void adicionarGradeCurricularTipoAtividadeComplementarVOs(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO,Integer TotalCargaHorariaAtividadeComplementargradeCurricular, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		int index = 0;
		validarDados(obj);
		obj.setTipoAtividadeComplementarVO(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(obj.getTipoAtividadeComplementarVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, controlarAcesso, usuario));
		realizarValidacoesQuantidadeMinimaAtividadeComplementar(obj, listaGradeCurricularTipoAtividadeComplementarVO , TotalCargaHorariaAtividadeComplementargradeCurricular);
		for (GradeCurricularTipoAtividadeComplementarVO vo : listaGradeCurricularTipoAtividadeComplementarVO) {
			if (vo.getTipoAtividadeComplementarVO().getCodigo().equals(obj.getTipoAtividadeComplementarVO().getCodigo())) {
				listaGradeCurricularTipoAtividadeComplementarVO.set(index, obj);
				return;
			}
			index++;
		}
		listaGradeCurricularTipoAtividadeComplementarVO.add(obj);
	}

	public void removerGradeCurricularTipoAtividadeComplementarVOs(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO) throws Exception {
		int index = 0;
		for (GradeCurricularTipoAtividadeComplementarVO vo : listaGradeCurricularTipoAtividadeComplementarVO) {
			if (vo.getTipoAtividadeComplementarVO().getCodigo().equals(obj.getTipoAtividadeComplementarVO().getCodigo())) {
				listaGradeCurricularTipoAtividadeComplementarVO.remove(index);
				break;
			}
			index++;
		}
	}

	public void incluirGradeCurricularTipoAtividadeComplementarVOs(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception {
		for (GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO : listaGradeCurricularTipoAtividadeComplementarVOs) {
			if (gradeCurricularTipoAtividadeComplementarVO.getCodigo().equals(0)) {
				gradeCurricularTipoAtividadeComplementarVO.getGradeCurricularVO().setCodigo(gradeCurricular);
				incluir(gradeCurricularTipoAtividadeComplementarVO, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeCurricularTipoAtividadeComplementar(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM gradeCurricularTipoAtividadeComplementar ");
		sb.append(" where gradecurricular = ").append(gradeCurricular);
		if (!listaGradeCurricularTipoAtividadeComplementarVOs.isEmpty()) {
			sb.append(" and codigo not in(");
			boolean virgula = false;
			for (GradeCurricularTipoAtividadeComplementarVO obj : listaGradeCurricularTipoAtividadeComplementarVOs) {
				if (!virgula) {
					sb.append(obj.getCodigo());
					virgula = true;
				} else {
					sb.append(", ").append(obj.getCodigo());
				}
				
			}
			sb.append(") ");
		}
		getConexao().getJdbcTemplate().update(sb.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}

	public void alterarGradeCurricularTipoAtividadeComplementarVOs(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception {
		excluirGradeCurricularTipoAtividadeComplementar(listaGradeCurricularTipoAtividadeComplementarVOs, gradeCurricular, usuarioVO);
		for (GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO : listaGradeCurricularTipoAtividadeComplementarVOs) {
			if (gradeCurricularTipoAtividadeComplementarVO.getCodigo().equals(0)) {
				gradeCurricularTipoAtividadeComplementarVO.getGradeCurricularVO().setCodigo(gradeCurricular);
				incluir(gradeCurricularTipoAtividadeComplementarVO, usuarioVO);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TipoAtividadeComplementarVO consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(TipoAtividadeComplementarVO obj, Integer curso, String matricula, PeriodicidadeEnum periodicidadeEnum, Date dataBase, Integer registroAtividadeComplementarMatriculaDesconsiderar) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select \"gctac.horamaximaporperiodoletivo\",  \"gctacSuperior.horamaximaporperiodoletivo\", ");
		sqlStr.append(" sum(cargahorariaconsiderada) as qtdcargahorariaconsiderada ");
		sqlStr.append(" from (  ");		
		sqlStr.append(" select  gctac.horamaximaporperiodoletivo as \"gctac.horamaximaporperiodoletivo\", ");
		sqlStr.append("  (select gctacSuperior.horamaximaporperiodoletivo from GradeCurricularTipoAtividadeComplementar as gctacSuperior   ");
		sqlStr.append("  inner join gradecurricular on gctacSuperior.gradecurricular = gradecurricular.codigo and gradecurricular.curso = ").append(curso);
		sqlStr.append("  inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append("  where gctacSuperior.tipoatividadecomplementar  = tipoatividadecomplementar.tipoatividadecomplementarsuperior   ) as \"gctacSuperior.horamaximaporperiodoletivo\", ");
		sqlStr.append(" (case when tipoatividadecomplementar.tipoatividadecomplementarsuperior is null then tipoatividadecomplementar.codigo else tipoatividadecomplementar.tipoatividadecomplementarsuperior end) tipoatividadecomplementar ");
		sqlStr.append(" from GradeCurricularTipoAtividadeComplementar as gctac ");
		sqlStr.append(" inner join gradecurricular on gctac.gradecurricular = gradecurricular.codigo and gradecurricular.curso = ").append(curso);
		sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" inner join tipoatividadecomplementar on tipoatividadecomplementar.codigo = gctac.tipoatividadecomplementar ");		
		sqlStr.append(" where gctac.tipoatividadecomplementar = ").append(obj.getCodigo());
		sqlStr.append("  ) as t ");
		sqlStr.append(" inner join tipoatividadecomplementar on tipoatividadecomplementar.tipoatividadecomplementarsuperior = t.tipoatividadecomplementar or tipoatividadecomplementar.codigo = t.tipoatividadecomplementar   ");
		sqlStr.append(" left join registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.tipoatividadecomplementar = tipoatividadecomplementar.codigo and registroatividadecomplementarmatricula.matricula ='").append(matricula).append("' ");
		sqlStr.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		if(Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaDesconsiderar)) {
			sqlStr.append(" and registroatividadecomplementarmatricula.codigo != ").append(registroAtividadeComplementarMatriculaDesconsiderar);
		}
		if (periodicidadeEnum != null && dataBase != null) {
			sqlStr.append(" and exists (select codigo from registroatividadecomplementar where registroatividadecomplementarmatricula.registroatividadecomplementar = registroatividadecomplementar.codigo ");
			if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
				sqlStr.append(" and extract(year from registroatividadecomplementar.data) = ").append(Uteis.getAno(dataBase));
			} else if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and extract(year from registroatividadecomplementar.data) = ").append(Uteis.getAno(dataBase));
				if (Uteis.getSemestreData(dataBase).equals("1")) {
					sqlStr.append(" and extract(month from registroatividadecomplementar.data) >= 1 and extract(month from registroatividadecomplementar.data) <= 7 ");
				} else {
					sqlStr.append(" and extract(month from registroatividadecomplementar.data) >= 8 and extract(month from registroatividadecomplementar.data) <= 12 ");
				}
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append("  group by \"gctac.horamaximaporperiodoletivo\", \"gctacSuperior.horamaximaporperiodoletivo\" ");
		
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			obj.setCargaHorasPermitidasPeriodoLetivo(rs.getInt("gctac.horamaximaporperiodoletivo"));
			obj.getTipoAtividadeComplementarSuperior().setCargaHorasPermitidasPeriodoLetivo(rs.getInt("gctacSuperior.horamaximaporperiodoletivo"));
			obj.setCargaHorasJaRealizadaPeriodoLetivo(rs.getInt("qtdcargahorariaconsiderada"));
		}else{
			obj.setCargaHorasPermitidasPeriodoLetivo(0);
			obj.getTipoAtividadeComplementarSuperior().setCargaHorasPermitidasPeriodoLetivo(0);
			obj.setCargaHorasJaRealizadaPeriodoLetivo(0);
		}
		return obj;
	}

	public static String getIdEntidade() {
		return TipoAtividadeComplementar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		TipoAtividadeComplementar.idEntidade = idEntidade;
	}

	@Override
	public void realizarCopiaTipoAtividadeComplementarOutraGrade(Integer gradeCurricularClonar, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception{
		if(!Uteis.isAtributoPreenchido(gradeCurricularClonar)) {
			throw new Exception("O campo MATRIZ ORIGEM deve ser informado.");
		}
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM gradeCurricularTipoAtividadeComplementar ");
		sqlStr.append(" WHERE gradeCurricular = ").append(gradeCurricularClonar);
		sqlStr.append(" and tipoatividadecomplementar not in (0 ");
		for(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO: gradeCurricularVO.getListaGradeCurricularTipoAtividadeComplementarVOs()) {
			sqlStr.append(", ").append(gradeCurricularTipoAtividadeComplementarVO.getTipoAtividadeComplementarVO().getCodigo());
		}
		sqlStr.append(" ) order by gradeCurricularTipoAtividadeComplementar.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<GradeCurricularTipoAtividadeComplementarVO> gradeCurricularTipoAtividadeComplementarVOs = montarDadosConsulta(tabelaResultado, usuario);
		if(gradeCurricularTipoAtividadeComplementarVOs.isEmpty()) {
			throw new Exception("Não foi encontrado nenhum TIPO ATIVIDADE na MATRIZ ORIGEM para clonar que não tenha na MATRIZ DESTINO.");
		}
		for(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO: gradeCurricularTipoAtividadeComplementarVOs) {
			gradeCurricularTipoAtividadeComplementarVO.setCodigo(0);
			gradeCurricularTipoAtividadeComplementarVO.setNovoObj(true);
			gradeCurricularTipoAtividadeComplementarVO.setGradeCurricularVO(gradeCurricularVO);			
			gradeCurricularVO.getListaGradeCurricularTipoAtividadeComplementarVOs().add(gradeCurricularTipoAtividadeComplementarVO);
		}		
	}
	
	
	public void realizarValidacoesQuantidadeMinimaAtividadeComplementar(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO, Integer TotalCargaHorariaAtividadeComplementargradeCurricular) throws ConsistirException{
		
		int valorTotalHorasMinimas = (listaGradeCurricularTipoAtividadeComplementarVO.stream().
				mapToInt(a-> a.getHorasMinimasExigida()).sum() + obj.getHorasMinimasExigida());
		
		if (obj.getHorasMinimasExigida() > obj.getCargaHoraria()) {
			throw new ConsistirException("O Valor Das Horas Minimas Exigidas Do Tipo Atividade Complementar Deve ser Menor Que o Total Carga Horária Exigida Para Atividade.");
		}
		if (valorTotalHorasMinimas > TotalCargaHorariaAtividadeComplementargradeCurricular) {
			throw new ConsistirException("A Soma Das Horas Minimas Exigidas Do Tipo Atividade Complementar Deve ser Menor Que o Total Carga Horária Exigida Informada Na Matriz.");
		}
		 
	}
	
	@Override
	public void validarTotalCargaHorariaAtividadeComplementar(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO, Integer TotalCargaHorariaAtividadeComplementargradeCurricular) throws ConsistirException{
		
		int valorTotalHorasMinimas = (listaGradeCurricularTipoAtividadeComplementarVO.stream().
				mapToInt(a-> a.getHorasMinimasExigida()).sum());		
		
		if (valorTotalHorasMinimas > TotalCargaHorariaAtividadeComplementargradeCurricular) {
			throw new ConsistirException("O Total Carga Horária Exigida Informada Na Matriz Deve Ser Maior Que A Soma Das Horas Minimas Exigidas.");
		}
		 
	}
	
}
