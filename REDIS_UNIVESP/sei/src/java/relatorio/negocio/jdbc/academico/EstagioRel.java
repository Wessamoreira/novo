package relatorio.negocio.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.negocio.comuns.academico.EstagioRelVO;
import relatorio.negocio.interfaces.academico.EstagioRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EstagioRel extends SuperRelatorio implements EstagioRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5568769729204713936L;

	@Override
	public List<EstagioRelVO> consultarListaEstagioSintetico(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, List<DisciplinaVO> disciplinaVOs, ParceiroVO empresaVO, List<AreaProfissionalVO> areaProfissionalVOs, MatriculaVO matricula, String situacao, TipoEstagioEnum tipoEstagio, String periodicidade, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean trazerTodosEstagiosAluno, String tipoLayout, Integer periodoLetivoDe, Integer periodoLetivoAte, UsuarioVO usuarioLogado,Date dataInicio,Date dataFim) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append("  SELECT matricula.matricula, unidadeensino.nome as unidadeensino, ");
			sql.append("  (select sum(estagio.cargahoraria) from estagio where estagio.matricula = matricula.matricula ");
			if (!trazerTodosEstagiosAluno) {
				if (!ano.trim().isEmpty()) {
					sql.append(" and estagio.ano = '").append(ano).append("'");
				}
				if (!semestre.trim().isEmpty()) {
					sql.append(" and estagio.semestre = '").append(semestre).append("'");
				}				
			}	
		
			sql.append("  ) as cargahoraria,   ");
			sql.append(" (select sum(gradedisciplina.cargahoraria) from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual) as cargahorariaestagiodisciplina, ");
			sql.append("  pessoa.nome as pessoa, pessoa.sexo, pessoa.cpf, pessoa.dataNasc, curso.nome as curso, turno.nome as turno, gradecurricular.totalcargahorariaestagio , turma.identificadorturma ");
			sql.append(" from matricula  ");
			sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
			sql.append(" inner join curso on matricula.curso = curso.codigo  ");
			sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo  ");
			sql.append(" inner join turno on matricula.turno = turno.codigo ");			
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula    and ");
			sql.append(" matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp ");
			sql.append("  where mp.matricula = matricula.matricula  ");
			if (!ano.equals("")) {
				sql.append(" and mp.ano = '").append(ano).append("'");
			}
			if (!semestre.equals("")) {
				sql.append(" and mp.semestre = '").append(semestre).append("'");
			}			
			sql.append(" order by (mp.ano||'/'||mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1)  ");
			sql.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
			sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula");
			sql.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");			
			sql.append(" left join estagio on estagio.matricula =  matricula.matricula ");
			sql.append(" left join parceiro on estagio.empresaestagio =  parceiro.codigo ");
			sql.append(" left join disciplina on estagio.disciplina =  disciplina.codigo ");
			sql.append(" left join areaprofissional on estagio.areaprofissional =  areaprofissional.codigo ");
			sql.append(realizarGeracaoClausulaWhere(unidadeEnsinoVOs, cursoVOs, turnoVOs, turma, disciplinaVOs, empresaVO, areaProfissionalVOs, matricula, situacao, tipoEstagio, periodicidade, ano, semestre, filtroRelatorioAcademicoVO, false, periodoLetivoDe, periodoLetivoAte, null, false, null, null,dataInicio,dataFim));
			sql.append(" group by  matricula.matricula, unidadeensino.nome, pessoa.nome, curso.nome, turno.nome, gradecurricular.totalcargahorariaestagio , turma.identificadorturma, pessoa.sexo, pessoa.cpf, pessoa.dataNasc ");
			sql.append(" order by unidadeensino.nome, curso.nome, pessoa.nome, matricula ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<EstagioRelVO> listaRetorno = new ArrayList<EstagioRelVO>(0);
			while (rs.next()) {
				EstagioRelVO obj = new EstagioRelVO();				
				obj.getMatriculaVO().setMatricula(rs.getString("matricula"));
				obj.setCargaHoraria(rs.getInt("cargahoraria"));
				obj.getMatriculaVO().getAluno().setSexo(rs.getString("sexo"));
				obj.getMatriculaVO().getAluno().setCPF(rs.getString("cpf"));
				obj.getMatriculaVO().getAluno().setDataNasc(rs.getDate("dataNasc"));
				obj.getMatriculaVO().getAluno().setNome(rs.getString("pessoa"));
				obj.getMatriculaVO().getCurso().setNome(rs.getString("curso"));
				obj.getMatriculaVO().getTurno().setNome(rs.getString("turno"));				
				obj.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino"));				
				obj.setHorasObrigatorias(rs.getInt("totalcargahorariaestagio")+rs.getInt("cargahorariaestagiodisciplina"));
				listaRetorno.add(obj);
			}
			return listaRetorno;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<EstagioRelVO> consultarListaEstagioAnalitico(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, List<DisciplinaVO> disciplinaVOs, ParceiroVO empresaVO, List<AreaProfissionalVO> areaProfissionalVOs, MatriculaVO matricula, String situacao, TipoEstagioEnum tipoEstagio, String periodicidade, String ano, String semestre,  FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean apresentarNotaDisciplina, Boolean trazerTodosEstagiosAluno, String tipoLayout, Integer periodoLetivoDe, Integer periodoLetivoAte, String estagioRegistradoSeguradora, Boolean filtrarEstagioRegitradoSeguradora, Date dataInicioEnvioSeguradora, Date dataTerminoEnvioSeguradora, UsuarioVO usuarioLogado,Date dataInicio,Date dataFim) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" SELECT estagio.codigo, estagio.enviadoSeguradora, matricula.matricula, estagio.ano, estagio.semestre, disciplina.nome as disciplina, unidadeensino.nome as unidadeensino, ");
			sql.append(" estagio.cargahoraria, case when parceiro.codigo is not null then parceiro.nome else estagio.empresa end as empresa, usuario.nome as responsavel,  ");
			sql.append(" pessoa.nome as pessoa, pessoa.cpf as cpf, pessoa.sexo as sexo,  pessoa.datanasc as datanasc, curso.nome as curso, turno.nome as turno, gradecurricular.totalcargahorariaestagio, estagio.tipoestagio, areaprofissional.descricaoareaprofissional as areaprofissional,  ");
			sql.append(" estagio.datainiciovigencia, estagio.datafinalvigencia,  ");
			sql.append(" (select sum(gradedisciplina.cargahoraria) from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual) as cargahorariaestagiodisciplina ");
			if(apresentarNotaDisciplina){
				sql.append(", historico.mediafinal as nota, configuracaoacademiconotaconceito.conceitonota, configuracaoacademico.quantidadecasasdecimaispermitiraposvirgula ");
			}else{
				sql.append(", null as nota, '' as conceitonota, 0 as quantidadecasasdecimaispermitiraposvirgula");
			}
			sql.append(" from matricula  ");
			sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
			sql.append(" inner join curso on matricula.curso = curso.codigo  ");
			sql.append(" inner join turno on matricula.turno = turno.codigo ");
			sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo  ");
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and ");
			sql.append(" matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp ");
			sql.append(" where mp.matricula = matricula.matricula  ");
			if (!ano.trim().isEmpty()) {
				sql.append(" and mp.ano = '").append(ano).append("'");
			}
			if (!semestre.trim().isEmpty()) {
				sql.append(" and mp.semestre = '").append(semestre).append("'");
			}
			if(periodicidade.equals(PeriodicidadeEnum.INTEGRAL)){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "mp.data", false));
			}
			sql.append(" order by (mp.ano||'/'||mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
			sql.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual");
			sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula");
			sql.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
			sql.append(" left join estagio on estagio.matricula =  matricula.matricula ");
			if (!trazerTodosEstagiosAluno) {
				if (!ano.trim().isEmpty()) {
					sql.append(" and estagio.ano = '").append(ano).append("'");
				}
				if (!semestre.trim().isEmpty()) {
					sql.append(" and estagio.semestre = '").append(semestre).append("'");
				}
			}
			sql.append(" left join usuario on estagio.responsavel =  usuario.codigo ");
			sql.append(" left join parceiro on estagio.empresaestagio =  parceiro.codigo ");
			sql.append(" left join disciplina on estagio.disciplina =  disciplina.codigo ");
			sql.append(" left join areaprofissional on estagio.areaprofissional =  areaprofissional.codigo ");
			if(apresentarNotaDisciplina){
				sql.append(" left join historico on historico.matricula =  matricula.matricula ");
				sql.append(" and historico.matrizcurricular =  matricula.gradecurricularatual ");
				sql.append(" and historico.disciplina =  disciplina.codigo ");
				sql.append(" and historico.codigo = (select his.codigo from historico his ");
				sql.append(" where his.matricula =  matricula.matricula ");
				sql.append(" and his.matrizcurricular =  matricula.gradecurricularatual ");
				sql.append(" and his.disciplina =  disciplina.codigo order by anohistorico||semestrehistorico desc, codigo desc limit 1) ");
				sql.append(" left join configuracaoacademico on historico.configuracaoacademico =  configuracaoacademico.codigo ");
				sql.append(" left join configuracaoacademiconotaconceito on historico.mediafinalconceito =  configuracaoacademiconotaconceito.codigo ");
			}
			sql.append(realizarGeracaoClausulaWhere(unidadeEnsinoVOs, cursoVOs, turnoVOs, turma, disciplinaVOs, empresaVO, areaProfissionalVOs, matricula, situacao, tipoEstagio, periodicidade, ano, semestre, filtroRelatorioAcademicoVO, apresentarNotaDisciplina, periodoLetivoDe, periodoLetivoAte, estagioRegistradoSeguradora, filtrarEstagioRegitradoSeguradora, dataInicioEnvioSeguradora, dataTerminoEnvioSeguradora,dataInicio,dataFim));			
			if(tipoLayout.equals("EstagioAnaliticoUnidadeCursoTipoEstagioRel")){
				sql.append(" ORDER by unidadeensino, curso, tipoEstagio, pessoa, matricula, estagio.ano, estagio.semestre ");
			}else if(tipoLayout.equals("EstagioAnaliticoUnidadeCursoEmpresaRel")){
				sql.append(" ORDER by unidadeensino, curso, empresa, pessoa, matricula, estagio.ano, estagio.semestre ");
			}else if(tipoLayout.equals("EstagioAnaliticoUnidadeAreaProfissionalRel")){
				sql.append(" ORDER by unidadeensino, curso, areaProfissional, pessoa, matricula, estagio.ano, estagio.semestre ");
			}else{
				sql.append(" ORDER by unidadeensino, curso, pessoa, matricula, estagio.ano, estagio.semestre ");
			}				
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<EstagioRelVO> listaRetorno = new ArrayList<EstagioRelVO>(0);
			while (rs.next()) {
				EstagioRelVO obj = new EstagioRelVO();				
				obj.getEstagioVO().setCodigo(rs.getInt("codigo"));				
				obj.getMatriculaVO().setMatricula(rs.getString("matricula"));
				obj.setAno(rs.getString("ano"));
				obj.setSemestre(rs.getString("semestre"));
				obj.setCargaHoraria(rs.getInt("cargaHoraria"));
				obj.getEmpresaVO().setNome(rs.getString("empresa"));
				obj.getMatriculaVO().getAluno().setNome(rs.getString("pessoa"));
				obj.getMatriculaVO().getAluno().setSexo(rs.getString("sexo"));
				obj.getMatriculaVO().getAluno().setCPF(rs.getString("cpf"));
				obj.getMatriculaVO().getAluno().setDataNasc(rs.getDate("dataNasc"));
				obj.getMatriculaVO().getCurso().setNome(rs.getString("curso"));
				obj.getMatriculaVO().getTurno().setNome(rs.getString("turno"));
				obj.getAreaProfissionalVO().setDescricaoAreaProfissional(rs.getString("areaprofissional"));
				obj.setDataInicioVigencia(rs.getDate("datainiciovigencia"));
				obj.setDataFimVigencia(rs.getDate("datafinalvigencia"));
				if(rs.getString("tipoEstagio") != null){
					obj.setTipoEstagio(TipoEstagioEnum.valueOf(rs.getString("tipoEstagio")));
				}
				obj.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino"));
				obj.getDisciplinaVO().setNome(rs.getString("disciplina"));
				obj.setResponsavel(rs.getString("responsavel"));				
				obj.setHorasObrigatorias(rs.getInt("totalcargahorariaestagio")+rs.getInt("cargahorariaestagiodisciplina"));
				if(rs.getString("conceitonota") != null && rs.getString("conceitonota").trim().isEmpty()){	
					obj.setNota(rs.getString("conceitonota"));					
				}else{
					if(rs.getObject("nota") == null){
						obj.setNota("");
					}else{
						obj.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(rs.getDouble("nota"), rs.getInt("quantidadecasasdecimaispermitiraposvirgula")));
					}
				}
				listaRetorno.add(obj);
			}
			return listaRetorno;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	private String realizarGeracaoClausulaWhere(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, List<DisciplinaVO> disciplinaVOs, ParceiroVO empresaVO, List<AreaProfissionalVO> areaProfissionalVOs, MatriculaVO matricula, String situacao, TipoEstagioEnum tipoEstagio, String periodicidade, String ano, String semestre,  FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean apresentarNotaDisciplina, Integer periodoLetivoDe, Integer periodoLetivoAte, String estagioRegistradoSeguradora, Boolean filtrarEstagioRegitradoSeguradora, Date dataInicioEnvioSeguradora, Date dataTerminoEnvioSeguradora,Date dataInicioCadastro,Date dataFimCadastro){
		StringBuilder sql = new StringBuilder(" where ");
		sql.append(" curso.periodicidade = '").append(periodicidade).append("' ");
		if (Uteis.isAtributoPreenchido(dataInicioCadastro) && Uteis.isAtributoPreenchido(dataFimCadastro)) {
			sql.append(" AND estagio.datacadastro::date between '").append(UteisData.getDataAplicandoFormatacao(dataInicioCadastro, "yyyy-MM-dd")).append("' AND '").append(UteisData.getDataAplicandoFormatacao(dataFimCadastro,"yyyy-MM-dd")).append("' ");
		}
		
		if (situacao.equals("pendencia")) {
			sql.append(" and (totalcargahorariaestagio > 0 and totalcargahorariaestagio > (select sum(estagio.cargahoraria) from estagio where estagio.matricula = matricula.matricula)  ");
			sql.append(" or exists (select gradedisciplina.disciplina from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sql.append(" and not exists (select his.codigo from historico his where his.matricula = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual ");
			sql.append(" and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE', 'CO')  and his.gradedisciplina = gradedisciplina.codigo)");
			sql.append(" limit 1 )) ");
		}
		if (situacao.equals("concluido")) {
			sql.append(" and (totalcargahorariaestagio > 0 and totalcargahorariaestagio <= (select sum(estagio.cargahoraria) from estagio where estagio.matricula = matricula.matricula) ");
			sql.append(" or (exists (select gradedisciplina.disciplina from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual) ");
			sql.append(" and not exists (select gradedisciplina.disciplina from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sql.append(" and not exists (select his.codigo from historico his where his.matricula = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual ");
			sql.append(" and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE', 'CO')  and his.gradedisciplina = gradedisciplina.codigo)");
			sql.append(" limit 1 ))) ");
		}
		if (situacao.equals("estagioRegistrado")) {
			sql.append(" and estagio.codigo is not null ");
		}
		if (situacao.equals("estagioNaoRegistrado")) {
			sql.append(" and (totalcargahorariaestagio> 0 and totalcargahorariaestagio > (select sum(estagio.cargahoraria) from estagio where estagio.matricula = matricula.matricula) ");
			sql.append(" or exists (select gradedisciplina.disciplina from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sql.append(" and not exists (select his.codigo from historico his where his.matricula = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual ");
			sql.append(" and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE', 'CO') and his.gradedisciplina = gradedisciplina.codigo )");
			sql.append(" limit 1 )) ");
			sql.append(" and estagio.codigo is null ");
		}
		if (situacao.equals("todos")) {
			sql.append(" and (totalcargahorariaestagio > 0 or exists (select disciplina from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo where gradedisciplina.disciplinaEstagio and gradedisciplina.tipoDisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = matricula.gradecurricularatual limit 1 )) ");
		}
		if(tipoEstagio != null){
			sql.append(" and estagio.tipoestagio = '").append(tipoEstagio.name()).append("' ");
		}
		if (!matricula.getMatricula().equals("")) {
			sql.append(" and matricula.matricula = '").append(matricula.getMatricula()).append("'");
		}
		if (Uteis.isAtributoPreenchido(periodoLetivoDe)) {
			sql.append(" and periodoLetivo.periodoLetivo >= ").append(periodoLetivoDe);
		}
		if (Uteis.isAtributoPreenchido(periodoLetivoAte)) {
			sql.append(" and periodoLetivo.periodoLetivo <= ").append(periodoLetivoAte);
		}
		if (!ano.trim().isEmpty()) {
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("'");
		}
		if (!semestre.trim().isEmpty()) {
			sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("'");
		}
		if(periodicidade.equals(PeriodicidadeEnum.INTEGRAL)){
			sql.append(" and ").append(realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "matriculaperiodo.data", false));
		}
		if(filtrarEstagioRegitradoSeguradora && !estagioRegistradoSeguradora.trim().isEmpty()){
			sql.append(" and estagio.enviadoSeguradora = ").append(estagioRegistradoSeguradora.equals("Sim"));
			if(estagioRegistradoSeguradora.equals("Sim")){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicioEnvioSeguradora, dataTerminoEnvioSeguradora, "estagio.dataEnvioSeguradora", false));
			}
		}
		StringBuilder filtroIn = new StringBuilder();
		if (!Uteis.isAtributoPreenchido(turma.getCodigo())){
			for(CursoVO cursoVO: cursoVOs){
				if(cursoVO.getFiltrarCursoVO()){
					filtroIn.append(filtroIn.toString().isEmpty()?"":", ").append(cursoVO.getCodigo());
				}
			}
			if(!filtroIn.toString().isEmpty()){
				sql.append(" and curso.codigo in (").append(filtroIn).append(")");
			}
			filtroIn = new StringBuilder();
			for(UnidadeEnsinoVO unidadeEnsinoVO: unidadeEnsinoVOs){
				if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
					filtroIn.append(filtroIn.toString().isEmpty()?"":", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			if(!filtroIn.toString().isEmpty()){
				sql.append(" and unidadeEnsino.codigo in (").append(filtroIn).append(")");
			}
			
			filtroIn = new StringBuilder();
			for(TurnoVO turnoVO: turnoVOs){
				if(turnoVO.getFiltrarTurnoVO()){
					filtroIn.append(filtroIn.toString().isEmpty()?"":", ").append(turnoVO.getCodigo());
				}
			}
			if(!filtroIn.toString().isEmpty()){
				sql.append(" and turno.codigo in (").append(filtroIn).append(")");
			}
		}
		filtroIn = new StringBuilder();
		for(AreaProfissionalVO areaProfissionalVO: areaProfissionalVOs){
			if(areaProfissionalVO.getSelecionado()){
				filtroIn.append(filtroIn.toString().isEmpty()?"":", ").append(areaProfissionalVO.getCodigo());
			}
		}
		if(!filtroIn.toString().isEmpty()){
			sql.append(" and areaProfissional.codigo in (").append(filtroIn).append(")");
		}
		if (turma.getCodigo() != null && turma.getCodigo() != 0) {		
			sql.append(" and turma.codigo = ").append(turma.getCodigo());
		}
		if (empresaVO.getCodigo() != null && empresaVO.getCodigo() != 0) {
			sql.append(" and parceiro.codigo = ").append(empresaVO.getCodigo());
		}
		StringBuilder inDisciplina = new StringBuilder("");
		for(DisciplinaVO disciplinaVO: disciplinaVOs){
			if(disciplinaVO.getSelecionado()){
				if(inDisciplina.length() > 0){
					inDisciplina.append(", ");	
				}
				inDisciplina.append(disciplinaVO.getCodigo());
			}
		}
		if (inDisciplina.length() > 0) {
			sql.append(" and ( ");
			if (situacao.equals("pendencia")  || situacao.equals("estagioNaoRegistrado") || situacao.trim().isEmpty() ||  situacao.equals("todos")) {
				sql.append(" (exists ( ");
				sql.append(" select mptd.codigo from matriculaperiodoturmadisciplina mptd  ");
				sql.append(" inner join gradedisciplina on gradedisciplina.codigo = mptd.gradedisciplina ");
				sql.append(" where gradedisciplina.disciplinaestagio and mptd.disciplina in (").append(inDisciplina).append(") ");
				sql.append(" and mptd.matricula = matricula.matricula and mptd.matriculaperiodo = matriculaperiodo.codigo and gradedisciplina.disciplinacomposta = false ");
				sql.append(" and not exists (select est.codigo from estagio as est where est.matricula = matricula.matricula ");
				if(!ano.trim().isEmpty()){
					sql.append(" and est.ano = '").append(ano).append("' ");
				}
				if(!semestre.trim().isEmpty()){
					sql.append(" and est.semestre = '").append(semestre).append("' ");
				}
				sql.append(" and est.disciplina =  mptd.disciplina ");
				sql.append(" ) ");
				sql.append(" union all ");
				sql.append(" select mptd.codigo from matriculaperiodoturmadisciplina mptd  ");
				sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = mptd.gradedisciplinacomposta ");
				sql.append(" inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
				sql.append(" where gradedisciplina.disciplinaestagio and mptd.disciplina in (").append(inDisciplina).append(") ");
				sql.append(" and mptd.matricula = matricula.matricula and mptd.matriculaperiodo = matriculaperiodo.codigo ");
				sql.append(" and not exists (select est.codigo from estagio as est where est.matricula = matricula.matricula ");
				if(!ano.trim().isEmpty()){
					sql.append(" and est.ano = '").append(ano).append("' ");
				}
				if(!semestre.trim().isEmpty()){
					sql.append(" and est.semestre = '").append(semestre).append("' ");
				}
				sql.append(" and est.disciplina =  mptd.disciplina ");
				sql.append(" ) ");
				sql.append(" union all ");
				sql.append(" select mptd.codigo from matriculaperiodoturmadisciplina mptd  ");
				sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = mptd.gradedisciplinacomposta ");
				sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
				sql.append(" where gradecurriculargrupooptativadisciplina.disciplinaestagio and mptd.disciplina in (").append(inDisciplina).append(") ");
				sql.append(" and mptd.matricula = matricula.matricula and mptd.matriculaperiodo = matriculaperiodo.codigo ");
				sql.append(" and not exists (select est.codigo from estagio as est where est.matricula = matricula.matricula ");
				if(!ano.trim().isEmpty()){
					sql.append(" and est.ano = '").append(ano).append("' ");
				}
				if(!semestre.trim().isEmpty()){
					sql.append(" and est.semestre = '").append(semestre).append("' ");
				}
				sql.append(" and est.disciplina =  mptd.disciplina ");
				sql.append(" ) ");
				sql.append(" union all ");
				sql.append(" select mptd.codigo from matriculaperiodoturmadisciplina mptd  ");
				sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = mptd.gradecurriculargrupooptativadisciplina ");
				sql.append(" where gradecurriculargrupooptativadisciplina.disciplinaestagio and mptd.disciplina in (").append(inDisciplina).append(") ");
				sql.append(" and mptd.matricula = matricula.matricula and mptd.matriculaperiodo = matriculaperiodo.codigo  and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
				sql.append(" and not exists (select est.codigo from estagio as est where est.matricula = matricula.matricula ");
				if(!ano.trim().isEmpty()){
					sql.append(" and est.ano = '").append(ano).append("' ");
				}
				if(!semestre.trim().isEmpty()){
					sql.append(" and est.semestre = '").append(semestre).append("' ");
				}
				sql.append(" and est.disciplina = mptd.disciplina ");
				sql.append(" ) ");				
				sql.append(")) ");
			}
			if (situacao.trim().isEmpty() || situacao.equals("todos")) {
				sql.append(" or ");
			}
			if (situacao.equals("concluido") || situacao.equals("estagioRegistrado") || situacao.trim().isEmpty() || situacao.equals("todos")) {
				sql.append(" (disciplina.codigo in (").append(inDisciplina).append(")) ");				
			}
			sql.append(" ) ");
		}
		sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		return sql.toString();
	}

	
}