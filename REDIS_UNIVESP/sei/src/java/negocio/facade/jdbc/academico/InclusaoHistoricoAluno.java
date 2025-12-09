package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoDisciplinaVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.InclusaoHistoricoAlunoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class InclusaoHistoricoAluno extends ControleAcesso implements InclusaoHistoricoAlunoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3199378891411394512L;
	private static final String idEntidade = "InclusaoHistoricoAluno";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, boolean controlarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		inclusaoHistoricoAlunoVO.getResponsavelInclusao().setCodigo(usuarioVO.getCodigo());
		inclusaoHistoricoAlunoVO.getResponsavelInclusao().setNome(usuarioVO.getNome());
		incluir(inclusaoHistoricoAlunoVO, controlarAcesso, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, boolean controlarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		try {
			InclusaoHistoricoAluno.incluir(idEntidade, controlarAcesso, usuarioVO);
			validarDados(inclusaoHistoricoAlunoVO);
			final String sql = "INSERT INTO InclusaoHistoricoAluno( matricula, gradeCurricular, dataInclusao, responsavelInclusao, observacao ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			inclusaoHistoricoAlunoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

						public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
							PreparedStatement sqlInserir = arg0.prepareStatement(sql);
							sqlInserir.setString(1, inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula());
							sqlInserir.setInt(2, inclusaoHistoricoAlunoVO.getGradeCurricular().getCodigo());
							sqlInserir.setDate(3, Uteis.getDataJDBC(inclusaoHistoricoAlunoVO.getDataInclusao()));
							sqlInserir.setInt(4, inclusaoHistoricoAlunoVO.getResponsavelInclusao().getCodigo());
							sqlInserir.setString(5, inclusaoHistoricoAlunoVO.getObservacao());
							return sqlInserir;
						}
					}, new ResultSetExtractor<Integer>() {

						public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
							if (arg0.next()) {
								inclusaoHistoricoAlunoVO.setNovoObj(Boolean.FALSE);
								inclusaoHistoricoAlunoVO.setCodigo(arg0.getInt("codigo"));
								return arg0.getInt("codigo");
							}
							return null;
						}
					}));
			getFacadeFactory().getInclusaoHistoricoAlunoDisciplinaFacade().incluirInclusaoHistoricoAlunoDisciplinaVOs(inclusaoHistoricoAlunoVO, usuarioVO);
			inclusaoHistoricoAlunoVO.setNovoObj(Boolean.FALSE);
			
		} catch (Exception e) {
			inclusaoHistoricoAlunoVO.setNovoObj(true);
			inclusaoHistoricoAlunoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		InclusaoHistoricoAluno.excluir(idEntidade, usuarioVO);
		for(InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO: inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs()){
			getFacadeFactory().getHistoricoFacade().excluir(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO(), false, usuarioVO);
		}
		getConexao().getJdbcTemplate().update("DELETE FROM InclusaoHistoricoAluno WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), inclusaoHistoricoAlunoVO.getCodigo());
	}

	public String getSqlConsultaBasica(){
		StringBuilder sql = new StringBuilder("select InclusaoHistoricoAluno.*,  pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.periodicidade as \"curso.periodicidade\", curso.configuracaoacademico as \"curso.configuracaoacademico\", ");
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\", ");
		sql.append(" usuario.nome as \"usuario.nome\", gradecurricular.nome as \"gradecurricular.nome\" ");
		sql.append(" from InclusaoHistoricoAluno ");
		sql.append(" inner join matricula on inclusaoHistoricoAluno.matricula = matricula.matricula ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join gradecurricular on gradecurricular.codigo = InclusaoHistoricoAluno.gradecurricular ");
		sql.append(" left join usuario on usuario.codigo = InclusaoHistoricoAluno.responsavelInclusao ");
		return sql.toString();
	}

	@Override
	public List<InclusaoHistoricoAlunoVO> consultar(String campoConsulta, String valorConsulta, Integer unidadeEnsino,
			NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaBasica());
		sql.append(" where ");
		if(campoConsulta.equals("matricula")){
			sql.append(" matricula.matricula ilike '").append(valorConsulta).append("'");	
		}
		if(campoConsulta.equals("aluno")){
			sql.append(" sem_acentos(pessoa.nome) ilike sem_acentos('").append(valorConsulta).append("%')");	
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		if(campoConsulta.equals("aluno")){
			sql.append(" order by pessoa.nome, dataInclusao desc ");
		}else{		
			sql.append(" order by matricula.matricula, dataInclusao desc ");
		}
		if(Uteis.isAtributoPreenchido(limite)){
			sql.append(" limit ").append(limite).append(" offset ").append(offset);	
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}
	
	@Override
	public Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, Integer unidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("select count(distinct InclusaoHistoricoAluno.codigo) as qtde from InclusaoHistoricoAluno ");
		sql.append(" inner join matricula on matricula.matricula = InclusaoHistoricoAluno.matricula ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sql.append(" where ");
		if(campoConsulta.equals("matricula")){
			sql.append(" matricula.matricula ilike '").append(valorConsulta).append("'");	
		}
		if(campoConsulta.equals("aluno")){
			sql.append(" sem_acentos(pessoa.nome) ilike sem_acentos('").append(valorConsulta).append("%')");	
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}		
		return getConexao().getJdbcTemplate().queryForInt(sql.toString());
	}

	@Override
	public InclusaoHistoricoAlunoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados,
			boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaBasica());
		sql.append(" where InclusaoHistoricoAluno.codigo =  ").append(codigo);
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			if(nivelMontarDados.equals(NivelMontarDados.TODOS)){
				return montarDadosCompletos(rs, usuarioVO);
			}
			return montarDadosBasicos(rs);
		}
		throw new Exception("Dados não encontrados Inclusão Histórico Aluno("+codigo+").");
	}
	
	private List<InclusaoHistoricoAlunoVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
		List<InclusaoHistoricoAlunoVO> inclusaoHistoricoAlunoVOs = new ArrayList<InclusaoHistoricoAlunoVO>(0);
		while(rs.next()){
			if(!nivelMontarDados.equals(NivelMontarDados.TODOS)){
				inclusaoHistoricoAlunoVOs.add(montarDadosBasicos(rs));
			}else{
				inclusaoHistoricoAlunoVOs.add(montarDadosCompletos(rs, usuarioVO));
			}
		}
		return inclusaoHistoricoAlunoVOs;
	}
	
	private InclusaoHistoricoAlunoVO montarDadosBasicos(SqlRowSet rs) throws Exception{
		InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO = new InclusaoHistoricoAlunoVO();
		inclusaoHistoricoAlunoVO.setNivelMontarDados(NivelMontarDados.BASICO);
		inclusaoHistoricoAlunoVO.setNovoObj(false);
		inclusaoHistoricoAlunoVO.setCodigo(rs.getInt("codigo"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().setMatricula(rs.getString("matricula"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getAluno().setCodigo(rs.getInt("pessoa.codigo"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getAluno().setNome(rs.getString("pessoa.nome"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().setCodigo(rs.getInt("curso.codigo"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().setNome(rs.getString("curso.nome"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().setPeriodicidade(rs.getString("curso.periodicidade"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().setCodigo(rs.getInt("curso.configuracaoacademico"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getTurno().setCodigo(rs.getInt("turno.codigo"));
		inclusaoHistoricoAlunoVO.getMatriculaVO().getTurno().setNome(rs.getString("turno.nome"));
		inclusaoHistoricoAlunoVO.getGradeCurricular().setCodigo(rs.getInt("gradecurricular"));		
		inclusaoHistoricoAlunoVO.getGradeCurricular().setNome(rs.getString("gradecurricular.nome"));		
		inclusaoHistoricoAlunoVO.setObservacao(rs.getString("observacao"));		
		inclusaoHistoricoAlunoVO.setDataInclusao(rs.getDate("dataInclusao"));		
		inclusaoHistoricoAlunoVO.getResponsavelInclusao().setCodigo(rs.getInt("responsavelInclusao"));		
		inclusaoHistoricoAlunoVO.getResponsavelInclusao().setNome(rs.getString("usuario.nome"));		
		return inclusaoHistoricoAlunoVO;
	}
	
	private InclusaoHistoricoAlunoVO montarDadosCompletos(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO = montarDadosBasicos(rs);
		inclusaoHistoricoAlunoVO.setInclusaoHistoricoAlunoDisciplinaVOs(getFacadeFactory().getInclusaoHistoricoAlunoDisciplinaFacade().consultarPorInclusaoHistoricoAluno(inclusaoHistoricoAlunoVO.getCodigo(), usuarioVO));
		return inclusaoHistoricoAlunoVO;
	}

	@Override
	public void realizarMontagemMatriculaComHistoricoAlunoVO(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,
			UsuarioVO usuarioVO) throws Exception {
		if (!inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo().equals(0)) {
			ConfiguracaoAcademicoVO cfgCurso = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo(), usuarioVO);
			inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().setConfiguracaoAcademico(cfgCurso);
		}

		if (inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula().equals("")) {
			if (inclusaoHistoricoAlunoVO.getGradeCurricular().getCodigo().equals(0)) {
				throw new Exception("Não é possível iniciar o Aproveitamento de Disciplinas sem que uma matriz curricular seja informada");
			}
		}
		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(inclusaoHistoricoAlunoVO.getMatriculaVO(),inclusaoHistoricoAlunoVO.getGradeCurricular().getCodigo(), false,inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico(), usuarioVO);
	    inclusaoHistoricoAlunoVO.getMatriculaVO().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		inclusaoHistoricoAlunoVO.setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
		q:
		for (Iterator<GradeCurricularGrupoOptativaVO> iterator = matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs().iterator(); iterator.hasNext();) {
			GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = iterator.next();
			for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()){
				if(periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo().equals(gradeCurricularGrupoOptativaVO.getCodigo())){
					aqui:
						for(GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO: periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()){
							for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
								if(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo())){
									gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs().addAll(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricosAluno());
									continue aqui;
								}
							}
						}
					continue q;
				}
			}
			iterator.remove();
		}
		
	}
	
	private boolean validarExistenciaHistoricoDisciplinaAnoSemestreInformado(MatriculaVO matriculaVO, Integer disciplina, String ano, String semestre) throws Exception{
		for(HistoricoVO historicoVO:matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTodosHistoricosAlunoGradeCurricular()){
			if(historicoVO.getDisciplina().getCodigo().equals(disciplina) && historicoVO.getAnoHistorico().equals(ano) && historicoVO.getSemestreHistorico().equals(semestre)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Este médoto obtem os histórico com a situação Reprovado, Reprovado Por Falta e Reprovado no Periodo Letivo e da mesma grade curricular da matricula selecionada, 
	 * depois é feita uma varedura na grade da matrícula na qual deve ser incluido os históricos, em busca da mesma disciplina, caso encontre é validado se não 
	 * existe este histórico no mesmo ano/semestre, caso não exista é criado o histórico, se encontrar o mesmo não é considerado.
	 * A definição da matrícula periodo segue a seguinte regra, caso encontre uma matriculperiodo no ano/semestre do histórico então é vinculado a ele, 
	 * caso contrario é vinculado a primeira matricula periodo.   
	 */
	@Override
	public void realizarInclusaoDisciplinaOutraMatriculaAluno(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,  Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO) throws Exception {
		inclusaoHistoricoAlunoVO.getHistoricoNaoAproveitadoVOs().clear();
		inclusaoHistoricoAlunoVO.getHistoricoAproveitadoVOs().clear();
		inclusaoHistoricoAlunoVO.getHistoricoJaAdicionadoVOs().clear();
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaSituacaoHistorico(null, inclusaoHistoricoAlunoVO.getMatriculaAproveitarDisciplinaVO().getMatricula(), 
				inclusaoHistoricoAlunoVO.getMatriculaAproveitarDisciplinaVO().getGradeCurricularAtual().getCodigo(), new String[]{"'RE'", "'RF'", "'RP'"}, 
				1, false, false, NivelMontarDados.BASICO, usuarioVO, inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico(), true);
		if(historicoVOs.isEmpty()){
			throw new Exception("Não foi encontrado nenhum histórico reprovado para ser aproveitado.");
		}
		realizarAproveitamentoHistoricoOutraDisciplinaPorCorrespondendencia(inclusaoHistoricoAlunoVO, mapConfAcad, historicoVOs, usuarioVO);
		realizarAproveitamentoHistoricoOutraDisciplinaPorEquivalencia(inclusaoHistoricoAlunoVO, mapConfAcad, historicoVOs, usuarioVO);
	}
	
	private void realizarAproveitamentoHistoricoOutraDisciplinaPorEquivalencia(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,  Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, List<HistoricoVO> historicoVOs, UsuarioVO usuarioVO) throws Exception{
		if(Uteis.isAtributoPreenchido(inclusaoHistoricoAlunoVO.getMapaEquivalenciaMatrizCurricularVO().getCodigo())){
			//if(!inclusaoHistoricoAlunoVO.getMapaEquivalenciaMatrizCurricularVO().getNivelMontarDados().equals(NivelMontarDados.TODOS)){
				inclusaoHistoricoAlunoVO.setMapaEquivalenciaMatrizCurricularVO(getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorChavePrimaria(inclusaoHistoricoAlunoVO.getMapaEquivalenciaMatrizCurricularVO().getCodigo(), NivelMontarDados.TODOS, false, usuarioVO));
				inclusaoHistoricoAlunoVO.getMapaEquivalenciaMatrizCurricularVO().setNivelMontarDados(NivelMontarDados.TODOS);
			//}
			his:
			for(Iterator<HistoricoVO> iterator= historicoVOs.iterator();iterator.hasNext();){
				HistoricoVO historicoVO = iterator.next();
				eq:
				for(Iterator<MapaEquivalenciaDisciplinaVO> iteratorEq = inclusaoHistoricoAlunoVO.getMapaEquivalenciaMatrizCurricularVO().getMapaEquivalenciaDisciplinaVOs().iterator();iteratorEq.hasNext();){
					MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = iteratorEq.next();
					if(mapaEquivalenciaDisciplinaVO.getIsAtivo() && mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1  && mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size() == 1){
						MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().get(0);					
						if(historicoVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo()) && 
								historicoVO.getCargaHorariaDisciplina().equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria())){
							Integer disciplina = historicoVO.getDisciplina().getCodigo();
							String nomeDisciplina = historicoVO.getDisciplina().getNome();
							Integer cargaHoraria = historicoVO.getCargaHorariaDisciplina();
							MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO = mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().get(0);
							historicoVO.getDisciplina().setCodigo(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo());
							historicoVO.getDisciplina().setNome(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getNome());
							historicoVO.setCargaHorariaDisciplina(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria());
							boolean aproveitado = realizarAproveitamentoHistoricoOutraDisciplina(inclusaoHistoricoAlunoVO, mapConfAcad, historicoVO, mapaEquivalenciaDisciplinaVO, usuarioVO);
							historicoVO.getDisciplina().setCodigo(disciplina);
							historicoVO.getDisciplina().setNome(nomeDisciplina);
							historicoVO.setCargaHorariaDisciplina(cargaHoraria);
							if(aproveitado){
								removerHistoricoListaNaoAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
								adicionarHistoricoListaAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
								iteratorEq.remove();
								iterator.remove();
								continue his;
							}else{
								if(historicoVO.getHistoricoDisciplinaAproveitada()){
									removerHistoricoListaNaoAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
									adicionarHistoricoListaJaAdicionado(inclusaoHistoricoAlunoVO, historicoVO);
									historicoVO.setObservacao("Histórico Já Adicionado");
									iteratorEq.remove();
									iterator.remove();
									continue his;
								}else{
									historicoVO.setObservacao("Sem Correspondência");

								}
							}
							iteratorEq.remove();
							continue eq;							
						}
					}else{
						iteratorEq.remove();
					}
				}
			}
		}
	}
	
	private boolean realizarAproveitamentoHistoricoOutraDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,  Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, HistoricoVO historicoVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception{ 
		if(!validarExistenciaHistoricoDisciplinaAnoSemestreInformado(inclusaoHistoricoAlunoVO.getMatriculaVO(), historicoVO.getDisciplina().getCodigo(), historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico())){
		for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : inclusaoHistoricoAlunoVO.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()){
			for(GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()){
				if(historicoVO.getDisciplina().getCodigo().equals(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina().getCodigo())  && historicoVO.getCargaHorariaDisciplina().equals(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getCargaHoraria())){						
					gradeDisciplinaComHistoricoAlunoVO.setInclusaoHistoricoAlunoDisciplinaVO(new InclusaoHistoricoAlunoDisciplinaVO());
					gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().setMatriculaAproveitarDisciplinaVO(inclusaoHistoricoAlunoVO.getMatriculaAproveitarDisciplinaVO());
					gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().setMapaEquivalenciaDisciplinaVO(mapaEquivalenciaDisciplinaVO);
					realizarCopiaInformacoesBasicaHistoricoOutraMatricula(historicoVO, gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
							periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO(), gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO(), null, inclusaoHistoricoAlunoVO.getMatriculaVO(), inclusaoHistoricoAlunoVO.getGradeCurricular(),  usuarioVO, mapConfAcad);
					if(adicionarDisciplinaHistoricoAlunoPorGradeDisciplina(inclusaoHistoricoAlunoVO, gradeDisciplinaComHistoricoAlunoVO, mapConfAcad, usuarioVO, false)){
						return true;
					}else{
						historicoVO.setHistoricoDisciplinaAproveitada(true);
						return false;
					}
				}	
			}
		}
		for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO : inclusaoHistoricoAlunoVO.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs()){
			for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
				if(historicoVO.getDisciplina().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()) && historicoVO.getCargaHorariaDisciplina().equals(gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria())){	
					PeriodoLetivoVO periodoLetivoVO =  null;
					for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : inclusaoHistoricoAlunoVO.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()){
						if(periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo().equals(gradeCurricularGrupoOptativaVO.getCodigo())){
							for(GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()){
								if(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo())){
									gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs().addAll(gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricosAluno());
								}
							}
							if(periodoLetivoVO == null){
								periodoLetivoVO = periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO();
							}
						}
					}
					if(periodoLetivoVO != null){
						gradeCurricularGrupoOptativaDisciplinaVO.setInclusaoHistoricoAlunoDisciplinaVO(new InclusaoHistoricoAlunoDisciplinaVO());
						gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().setMatriculaAproveitarDisciplinaVO(inclusaoHistoricoAlunoVO.getMatriculaAproveitarDisciplinaVO());
						gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().setMapaEquivalenciaDisciplinaVO(mapaEquivalenciaDisciplinaVO);
						realizarCopiaInformacoesBasicaHistoricoOutraMatricula(historicoVO, gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
							periodoLetivoVO, null, gradeCurricularGrupoOptativaDisciplinaVO, inclusaoHistoricoAlunoVO.getMatriculaVO(), inclusaoHistoricoAlunoVO.getGradeCurricular(), usuarioVO, mapConfAcad);
						if(adicionarDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(inclusaoHistoricoAlunoVO, gradeCurricularGrupoOptativaDisciplinaVO, mapConfAcad, usuarioVO, false)){
							return true;
						}else{
							historicoVO.setHistoricoDisciplinaAproveitada(true);
							return false;
						}
					}
				}
			}
		}
		}else{
			historicoVO.setHistoricoDisciplinaAproveitada(true);
		}
		return false;
	}
	
	private void realizarAproveitamentoHistoricoOutraDisciplinaPorCorrespondendencia(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,  Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, List<HistoricoVO> historicoVOs, UsuarioVO usuarioVO) throws Exception{	
		for(Iterator<HistoricoVO> iterator= historicoVOs.iterator();iterator.hasNext();){
			HistoricoVO historicoVO = iterator.next();			
			if(Uteis.isAtributoPreenchido(historicoVO.getMediaFinalConceito().getCodigo())){
				historicoVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(historicoVO.getMediaFinalConceito().getCodigo()));
				historicoVO.setUtilizaNotaFinalConceito(true);
			}
			if(realizarAproveitamentoHistoricoOutraDisciplina(inclusaoHistoricoAlunoVO, mapConfAcad, historicoVO, null, usuarioVO)){
				adicionarHistoricoListaAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
				iterator.remove();
			}else{
				if(historicoVO.getHistoricoDisciplinaAproveitada()){
					removerHistoricoListaNaoAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
					adicionarHistoricoListaJaAdicionado(inclusaoHistoricoAlunoVO, historicoVO);
					historicoVO.setObservacao("Histórico Já Adicionado");
					iterator.remove();
				}else{
					historicoVO.setObservacao("Sem Correspondência");
					adicionarHistoricoListaNaoAproveitados(inclusaoHistoricoAlunoVO, historicoVO);
				}
			}			
		}
	}
	
	private void removerHistoricoListaNaoAproveitados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, HistoricoVO historicoVO){
		for(Iterator<HistoricoVO> iterator=inclusaoHistoricoAlunoVO.getHistoricoNaoAproveitadoVOs().iterator(); iterator.hasNext();){
			HistoricoVO historicoVO2 = iterator.next();
			if(historicoVO2.getCodigo().equals(historicoVO.getCodigo())){
				iterator.remove();
				return;
			}
		}
	}
	
	private void adicionarHistoricoListaAproveitados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, HistoricoVO historicoVO){
		for(HistoricoVO historicoVO2:inclusaoHistoricoAlunoVO.getHistoricoAproveitadoVOs()){
			if(historicoVO2.getCodigo().equals(historicoVO.getCodigo())){
				return;
			}
		}
		inclusaoHistoricoAlunoVO.getHistoricoAproveitadoVOs().add(historicoVO);
	}
	
	private void adicionarHistoricoListaJaAdicionado(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, HistoricoVO historicoVO){
		for(HistoricoVO historicoVO2:inclusaoHistoricoAlunoVO.getHistoricoJaAdicionadoVOs()){
			if(historicoVO2.getCodigo().equals(historicoVO.getCodigo())){
				return;
			}
		}
		inclusaoHistoricoAlunoVO.getHistoricoJaAdicionadoVOs().add(historicoVO);
	}
	
	private void adicionarHistoricoListaNaoAproveitados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, HistoricoVO historicoVO){
		for(HistoricoVO historicoVO2:inclusaoHistoricoAlunoVO.getHistoricoNaoAproveitadoVOs()){
			if(historicoVO2.getCodigo().equals(historicoVO.getCodigo())){
				return;
			}
		}
		inclusaoHistoricoAlunoVO.getHistoricoNaoAproveitadoVOs().add(historicoVO);
	}
	
	/**
	 * Este método tem a finalidade de definir qual será a matrícula periodo que será vinculado ao histórico, seguindo a seguinte regra:
	 *  caso encontre uma matriculperiodo no ano/semestre do histórico então é vinculado a ele, caso contrario é vinculado a primeira matricula periodo.   
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private MatriculaPeriodoVO realizarDefinicaoMatriculaPeriodoVincularHistorico(MatriculaVO matriculaVO, String ano, String semestre, UsuarioVO usuario) throws Exception{
		if(matriculaVO.getMatriculaPeriodoVOs().isEmpty()){
			matriculaVO.setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), 0, false, new DataModelo(), usuario));
		}
		MatriculaPeriodoVO matriculaPeriodoUsar = null;
		for(MatriculaPeriodoVO matriculaPeriodoVO: matriculaVO.getMatriculaPeriodoVOs()){
			if(matriculaPeriodoVO.getAno().equals(ano) && matriculaPeriodoVO.getSemestre().equals(semestre)){
				if(matriculaPeriodoUsar == null){
					matriculaPeriodoUsar = matriculaPeriodoVO;
				}else if(matriculaPeriodoVO.getSituacao().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
						|| matriculaPeriodoVO.getSituacao().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
						|| matriculaPeriodoVO.getSituacao().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){
					matriculaPeriodoUsar = matriculaPeriodoVO;		
				}
			}
		}
		if(matriculaPeriodoUsar == null && !matriculaVO.getMatriculaPeriodoVOs().isEmpty()){
			matriculaPeriodoUsar = matriculaVO.getMatriculaPeriodoVOs().get(matriculaVO.getMatriculaPeriodoVOs().size()-1);
		}
		return matriculaPeriodoUsar;
	}
	
	/**
	 * Este método tem a vinalidade de copiar os dados do histórico de outra matricula do aluno para o histórico da matricula nova, 
	 * os dados preservados os vinculado ao parámetro historicoOutraMatricula.
	 * @param historicoOutraMatricula
	 * @param historicoAtual
	 * @param periodoLetivoVO
	 * @param gradeDisciplinaVO
	 * @param gradeCurricularGrupoOptativaDisciplinaVO
	 * @param matriculaVO
	 * @param gradeCurricularVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void realizarCopiaInformacoesBasicaHistoricoOutraMatricula(HistoricoVO historicoOutraMatricula, HistoricoVO historicoAtual, PeriodoLetivoVO periodoLetivoVO, GradeDisciplinaVO gradeDisciplinaVO, 
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad) throws Exception{
		historicoAtual.setAnoHistorico(historicoOutraMatricula.getAnoHistorico());
		historicoAtual.setSemestreHistorico(historicoOutraMatricula.getSemestreHistorico());
		historicoAtual.setPeriodoLetivoCursada(periodoLetivoVO);
		historicoAtual.setPeriodoLetivoMatrizCurricular(periodoLetivoVO);
		historicoAtual.setMatricula(matriculaVO);
		historicoAtual.setMatrizCurricular(gradeCurricularVO);		
		historicoAtual.setTipoHistorico("NO");		
		if(Uteis.isAtributoPreenchido(gradeDisciplinaVO)){
			historicoAtual.setDisciplina(gradeDisciplinaVO.getDisciplina());
			historicoAtual.setGradeDisciplinaVO(gradeDisciplinaVO);
			historicoAtual.setCargaHorariaDisciplina(gradeDisciplinaVO.getCargaHoraria());
			historicoAtual.setCreditoDisciplina(gradeDisciplinaVO.getNrCreditos());
			historicoAtual.setHistoricoDisciplinaComposta(gradeDisciplinaVO.getDisciplinaComposta());
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO)){
			historicoAtual.setDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina());
			historicoAtual.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
			historicoAtual.setCargaHorariaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
			historicoAtual.setCreditoDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getNrCreditos());
			historicoAtual.setDisciplinaReferenteAUmGrupoOptativa(true);
		}
		historicoAtual.setMediaFinal(historicoOutraMatricula.getMediaFinal());
		historicoAtual.setSituacao(historicoOutraMatricula.getSituacao());
		historicoAtual.setNotaFinalConceito(historicoOutraMatricula.getNotaFinalConceito());
		historicoAtual.setMediaFinalConceito(historicoOutraMatricula.getMediaFinalConceito());
		historicoAtual.setUtilizaNotaFinalConceito(historicoOutraMatricula.getUtilizaNotaFinalConceito());
		if(Uteis.isAtributoPreenchido(historicoOutraMatricula.getMediaFinalConceito())){
			historicoAtual.setUtilizaNotaFinalConceito(true);			
		}
		historicoAtual.setConfiguracaoAcademico(historicoOutraMatricula.getConfiguracaoAcademico());					
		historicoAtual.setFreguencia(historicoOutraMatricula.getFreguencia());	
		for(int x = 40;x<=40;x++){
			Object nota = UtilReflexao.invocarMetodoGet(historicoOutraMatricula, "nota"+x);
			if(nota == null){
				UtilReflexao.invocarMetodoSetParametroNull(historicoAtual, "nota"+x);
			}else{
				UtilReflexao.invocarMetodo(historicoAtual, "nota"+x, nota);
			}
			UtilReflexao.invocarMetodo(historicoAtual, "nota"+x+"Lancada", UtilReflexao.invocarMetodoGet(historicoOutraMatricula, "nota"+x+"Lancada"));
			UtilReflexao.invocarMetodo(historicoAtual, "nota"+x+"Conceito", UtilReflexao.invocarMetodoGet(historicoOutraMatricula, "nota"+x+"Conceito"));
		}
		if(!mapConfAcad.containsKey(historicoAtual.getConfiguracaoAcademico().getCodigo())){
			mapConfAcad.put(historicoAtual.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoAtual.getConfiguracaoAcademico().getCodigo(), usuarioVO));
		}
		historicoAtual.setConfiguracaoAcademico(mapConfAcad.get(historicoAtual.getConfiguracaoAcademico().getCodigo()));
	}

	@Override
	public void validarDados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO) throws ConsistirException {
		if(inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_InclusaoHistoricoAluno_matricula"));
		}
		realizaSeparacaoInclusaoHistoricoAlunoDisciplinaSeremIncluidos(inclusaoHistoricoAlunoVO);
		if(inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_InclusaoHistoricoAluno_disciplina"));
		}
	}	
	
	private void realizaSeparacaoInclusaoHistoricoAlunoDisciplinaSeremIncluidos(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO){
		inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().clear();
		for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : inclusaoHistoricoAlunoVO.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()){
			for(GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()){
				inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().addAll(gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs());	
			}
		}
		for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO : inclusaoHistoricoAlunoVO.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs()){
			for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
				inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().addAll(gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVOs());	
			}
		}		
	}
	
	/**
	 * Este carrega os dados basicos de um histórico a ser incluido manualmente, definindo o periodo letivo, configuração acadêmica e outros.
	 * @param historicoAtual
	 * @param gradeDisciplinaVO
	 * @param gradeCurricularGrupoOptativaDisciplinaVO
	 * @param matriculaVO
	 * @param gradeCurricularVO
	 * @param usuarioVO
	 */
	@Override
	public void realizarPreparacaoDadosInclusaoHistorico(HistoricoVO historicoAtual, GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO, 
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO) throws Exception{
		historicoAtual.setMatricula(matriculaVO);	
		historicoAtual.setMatrizCurricular(gradeCurricularVO);
		historicoAtual.setTipoHistorico("NO");
		if(gradeDisciplinaComHistoricoAlunoVO != null && Uteis.isAtributoPreenchido(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO())){
			historicoAtual.setPeriodoLetivoCursada(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getPeriodoLetivoVO());
			historicoAtual.setPeriodoLetivoMatrizCurricular(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getPeriodoLetivoVO());
			historicoAtual.setDisciplina(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina());
			historicoAtual.setGradeDisciplinaVO(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO());
			historicoAtual.setCargaHorariaDisciplina(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getCargaHoraria());
			historicoAtual.setCreditoDisciplina(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getNrCreditos());
			historicoAtual.setHistoricoDisciplinaComposta(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplinaComposta());
			historicoAtual.setConfiguracaoAcademico(gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getConfiguracaoAcademico());			
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO)){
			PeriodoLetivoVO periodoLetivoVO =  null;
			for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()){
				if(periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO.getGradeCurricularGrupoOptativa().getCodigo())){
					periodoLetivoVO = periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO();
					break;
				}
			}
			historicoAtual.setPeriodoLetivoCursada(periodoLetivoVO);
			historicoAtual.setPeriodoLetivoMatrizCurricular(periodoLetivoVO);
			historicoAtual.setDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina());
			historicoAtual.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
			historicoAtual.setCargaHorariaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
			historicoAtual.setCreditoDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getNrCreditos());
			historicoAtual.setDisciplinaReferenteAUmGrupoOptativa(true);
			historicoAtual.setConfiguracaoAcademico(gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico());				
		}
		historicoAtual.setCargaHorariaCursada(historicoAtual.getCargaHorariaDisciplina());
		historicoAtual.setFreguencia(100.0);
		if(!Uteis.isAtributoPreenchido(historicoAtual.getConfiguracaoAcademico())){
			historicoAtual.setConfiguracaoAcademico(matriculaVO.getCurso().getConfiguracaoAcademico());
		}
		if(!mapConfAcad.containsKey(historicoAtual.getConfiguracaoAcademico().getCodigo())){
			mapConfAcad.put(historicoAtual.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoAtual.getConfiguracaoAcademico().getCodigo(), usuarioVO));
		}
		historicoAtual.setConfiguracaoAcademico(mapConfAcad.get(historicoAtual.getConfiguracaoAcademico().getCodigo()));		
		historicoAtual.setUtilizaNotaFinalConceito(!historicoAtual.getConfiguracaoAcademico().getConfiguracaoAcademicaMediaFinalConceito().isEmpty());		
	}
	
	public boolean validarDisciplinaJaAdicionada(MatriculaVO matriculaVO,
			List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs, Integer disciplina,
			String ano, String semestre, boolean retornarExcecao) throws Exception {
		for (InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO : inclusaoHistoricoAlunoDisciplinaVOs) {
			if (inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getDisciplina().getCodigo().equals(disciplina)
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getAnoHistorico().equals(ano)
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getSemestreHistorico().equals(semestre)) {
				if(retornarExcecao){
				if (matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
					throw new Exception("Esta disciplinas já está adicionada no ano e semestre informado.");
				} else if (matriculaVO.getCurso().getPeriodicidade().equals("AN")) {
					throw new Exception("Esta disciplinas já está adicionada no ano informado.");
				} else {
					throw new Exception("Esta disciplinas já está adicionada.");
				}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean adicionarDisciplinaHistoricoAlunoPorGradeDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO, boolean retornarExcecao) throws Exception {
		gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().setMatriculaPeriodo(realizarDefinicaoMatriculaPeriodoVincularHistorico(inclusaoHistoricoAlunoVO.getMatriculaVO(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(),  gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico(), usuarioVO));
		getFacadeFactory().getInclusaoHistoricoAlunoDisciplinaFacade().validarDados(inclusaoHistoricoAlunoVO, gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO());
		boolean retorno = validarExistenciaHistoricoDisciplinaAnoSemestreInformado(inclusaoHistoricoAlunoVO.getMatriculaVO(),  gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina().getCodigo(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico());
		if(retorno){
			if(retornarExcecao){
				if (inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
					throw new Exception("Esta disciplinas já está adicionada no ano e semestre informado.");
				} else if (inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("AN")) {
					throw new Exception("Esta disciplinas já está adicionada no ano informado.");
				} else {
					throw new Exception("Esta disciplinas já está adicionada.");
				}
			}
			return false;
		}
		retorno = validarDisciplinaJaAdicionada(inclusaoHistoricoAlunoVO.getMatriculaVO(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs(), gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina().getCodigo(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(), gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico(), retornarExcecao);
		if(!retorno){
			gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().setInclusaoHistoricoAlunoVO(inclusaoHistoricoAlunoVO);
			gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().add(gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO());
			
			gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno().add(gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO());
			Ordenacao.ordenarLista(gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno(), "anoSemestreOrdenacao");
			gradeDisciplinaComHistoricoAlunoVO.setInclusaoHistoricoAlunoDisciplinaVO(new InclusaoHistoricoAlunoDisciplinaVO());
			realizarPreparacaoDadosInclusaoHistorico(gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
					gradeDisciplinaComHistoricoAlunoVO, null, inclusaoHistoricoAlunoVO.getMatriculaVO(), inclusaoHistoricoAlunoVO.getGradeCurricular(), mapConfAcad, usuarioVO);
			return true;
		}
		return false;
	}

	@Override
	public Boolean adicionarDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad, UsuarioVO usuarioVO, boolean retornarExcecao) throws Exception {
		gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().setMatriculaPeriodo(realizarDefinicaoMatriculaPeriodoVincularHistorico(inclusaoHistoricoAlunoVO.getMatriculaVO(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(),  gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico(), usuarioVO));
		getFacadeFactory().getInclusaoHistoricoAlunoDisciplinaFacade().validarDados(inclusaoHistoricoAlunoVO, gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO());
		boolean retorno = validarExistenciaHistoricoDisciplinaAnoSemestreInformado(inclusaoHistoricoAlunoVO.getMatriculaVO(),  gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico());
		if(retorno){
			if(retornarExcecao){
				if (inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
					throw new Exception("Esta disciplinas já está adicionada no ano e semestre informado.");
				} else if (inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("AN")) {
					throw new Exception("Esta disciplinas já está adicionada no ano informado.");
				} else {
					throw new Exception("Esta disciplinas já está adicionada.");
				}
			}
			return false;
		}
		retorno = validarDisciplinaJaAdicionada(inclusaoHistoricoAlunoVO.getMatriculaVO(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVOs(), gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getAnoHistorico(), gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO().getSemestreHistorico(), retornarExcecao);
		if(!retorno){
			gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().setInclusaoHistoricoAlunoVO(inclusaoHistoricoAlunoVO);
			gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO());
			gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs().add(gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO());
			Ordenacao.ordenarLista(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs(), "anoSemestreOrdenacao");
			gradeCurricularGrupoOptativaDisciplinaVO.setInclusaoHistoricoAlunoDisciplinaVO(new InclusaoHistoricoAlunoDisciplinaVO());
			realizarPreparacaoDadosInclusaoHistorico(gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
					null, gradeCurricularGrupoOptativaDisciplinaVO, inclusaoHistoricoAlunoVO.getMatriculaVO(), inclusaoHistoricoAlunoVO.getGradeCurricular(), mapConfAcad, usuarioVO);
			return true;
		}
		return false;
	}

	@Override
	public void removerDisciplinaHistoricoAlunoPorGradeDisciplina(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,
			GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO, HistoricoVO historicoRemoverVO) throws Exception {		
		int x = 0;
		for(InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO : gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs()){
			if(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getDisciplina().getCodigo().equals(historicoRemoverVO.getDisciplina().getCodigo())
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getAnoHistorico().equals(historicoRemoverVO.getAnoHistorico())
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getSemestreHistorico().equals(historicoRemoverVO.getSemestreHistorico())){	
				gradeDisciplinaComHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().remove(x);
				break;
			}
		}
		x = 0;
		for(HistoricoVO historicoVO: gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno()){
			if(historicoVO.getDisciplina().getCodigo().equals(historicoRemoverVO.getDisciplina().getCodigo())
					&& historicoVO.getAnoHistorico().equals(historicoRemoverVO.getAnoHistorico())
					&& historicoVO.getSemestreHistorico().equals(historicoRemoverVO.getSemestreHistorico())){				
				gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno().remove(x);
				break;
			}
			x++;
		}

	}

	@Override
	public void removerDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(
			InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, HistoricoVO historicoRemoverVO) throws Exception {
		int x = 0;
		for(InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO : gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVOs()){
			if(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getDisciplina().getCodigo().equals(historicoRemoverVO.getDisciplina().getCodigo())
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getAnoHistorico().equals(historicoRemoverVO.getAnoHistorico())
					&& inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getSemestreHistorico().equals(historicoRemoverVO.getSemestreHistorico())){	
				gradeCurricularGrupoOptativaDisciplinaVO.getInclusaoHistoricoAlunoDisciplinaVOs().remove(x);
				break;
			}
		}
		x = 0;
		for(HistoricoVO historicoVO: gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs()){
			if(historicoVO.getDisciplina().getCodigo().equals(historicoRemoverVO.getDisciplina().getCodigo())
					&& historicoVO.getAnoHistorico().equals(historicoRemoverVO.getAnoHistorico())
					&& historicoVO.getSemestreHistorico().equals(historicoRemoverVO.getSemestreHistorico())){
				
				gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoVOs().remove(x);
				break;
			}
			x++;
		}
	}

}
