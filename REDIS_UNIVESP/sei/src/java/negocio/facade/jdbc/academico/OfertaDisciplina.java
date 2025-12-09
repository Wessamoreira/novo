package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.OfertaDisciplinaInterfaceFacade;

@Scope("singleton")
@Service
@Lazy
public class OfertaDisciplina extends ControleAcesso implements OfertaDisciplinaInterfaceFacade {

	private static final String idEntidade = "OfertaDisciplina";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1636997499061360263L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<OfertaDisciplinaVO> ofertaDisciplinaVOs, UsuarioVO usuarioVO) throws Exception {
		for(OfertaDisciplinaVO ofertaDisciplinaVO: ofertaDisciplinaVOs) {
			validarDados(ofertaDisciplinaVO);
			if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getCodigo())) {
				incluir(OfertaDisciplina.idEntidade, true, usuarioVO);
				incluir(ofertaDisciplinaVO, idEntidade, new AtributoPersistencia().add("disciplina", ofertaDisciplinaVO.getDisciplinaVO())
						.add("configuracaoAcademico", ofertaDisciplinaVO.getConfiguracaoAcademicoVO())
						.add("ano", ofertaDisciplinaVO.getAno())
						.add("semestre", ofertaDisciplinaVO.getSemestre())
						.add("periodo", ofertaDisciplinaVO.getPeriodo()), usuarioVO);
			}else {
				alterar(OfertaDisciplina.idEntidade, true, usuarioVO);
				alterar(ofertaDisciplinaVO, idEntidade, new AtributoPersistencia().add("configuracaoAcademico", ofertaDisciplinaVO.getConfiguracaoAcademicoVO())
						.add("periodo", ofertaDisciplinaVO.getPeriodo()), new AtributoPersistencia().add("disciplina", ofertaDisciplinaVO.getDisciplinaVO())
						.add("ano", ofertaDisciplinaVO.getAno())
						.add("semestre", ofertaDisciplinaVO.getSemestre()), usuarioVO);
			}
			if(ofertaDisciplinaVO.getQtdeAlunoMatriculados() > 0) {
				    Integer bimestre = 0;
					switch (ofertaDisciplinaVO.getPeriodo()) {
					case BIMESTRE_01:
						bimestre = 1;
						break;
					case BIMESTRE_02:
						bimestre = 2;						
						break;
					case BIMESTRE_03:
						bimestre = 1;						
						break;
					case BIMESTRE_04:
						bimestre = 2;
						break;
					default:
						bimestre = 0;
						break;
					}									
				getConexao().getJdbcTemplate().update("update matriculaperiodoturmadisciplina set bimestre = "+bimestre+" where  matriculaperiodoturmadisciplina.disciplina = ? and matriculaperiodoturmadisciplina.ano = ? and matriculaperiodoturmadisciplina.semestre = ? and (matriculaperiodoturmadisciplina.bimestre is null or matriculaperiodoturmadisciplina.bimestre != ?) ", ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre(), bimestre);
				getConexao().getJdbcTemplate().update("update historico set configuracaoacademico = "+ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo()+" from matriculaperiodoturmadisciplina where matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina and  matriculaperiodoturmadisciplina.disciplina = ? and matriculaperiodoturmadisciplina.ano = ? and matriculaperiodoturmadisciplina.semestre = ? and historico.configuracaoacademico != ? ", ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre(), ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo());
			}
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(OfertaDisciplinaVO ofertaDisciplinaVO, Boolean excluirDisciplinaIncluidaAluno, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("delete from ofertadisciplina where disciplina = ? and ano = ? and semestre = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre());	
		if(excluirDisciplinaIncluidaAluno) {
			sql =  new StringBuilder("delete from historico where codigo in ( ");
			sql.append("  select h.codigo from matriculaperiodoturmadisciplina m ");
			sql.append("  inner join historico h2 on h2.matriculaperiodoturmadisciplina = m.codigo");
			sql.append("  inner join mapaequivalenciadisciplina m2 on m2.codigo = m.mapaequivalenciadisciplina and h2.mapaequivalenciadisciplina = m2.codigo and h2.mapaequivalenciadisciplinacursada is not null  ");
			sql.append("  inner join historico h on h.matricula = m.matricula ");
			sql.append("  and h.mapaequivalenciadisciplina = m2.codigo  ");
			sql.append("  and h.anohistorico = m.ano ");
			sql.append("  and h.semestrehistorico = h.semestrehistorico ");
			sql.append("  and h.matriculaperiodo = m.matriculaperiodo");
			sql.append("  and h.mapaequivalenciadisciplina is not null");
			sql.append("  and h.mapaequivalenciadisciplina = m.mapaequivalenciadisciplina ");
			sql.append("  and h.mapaequivalenciadisciplinamatrizcurricular is not null");
			sql.append("  and h.matriculaperiodoturmadisciplina is null");
			sql.append("  and h.numeroagrupamentoequivalenciadisciplina = h2.numeroagrupamentoequivalenciadisciplina ");
			sql.append("  where m.ano = ? and m.semestre = ? and m.disciplina = ? ");
			sql.append(" ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo() );
			
			sql =  new StringBuilder(" delete from matriculaperiodoturmadisciplina where ano = ? and semestre = ? and disciplina = ? ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo() );
		
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoDisciplinaAluno(OfertaDisciplinaVO ofertaDisciplinaVO, UsuarioVO usuarioVO) throws Exception{
		realizarInclusaoDisciplinaRegularDPAluno(ofertaDisciplinaVO, usuarioVO);
		realizarInclusaoDisciplinaEquivalenteAluno(ofertaDisciplinaVO, usuarioVO);
		consultarQtdeAlunoVinculadoDisciplina(ofertaDisciplinaVO);	
	}
	
	@Override
	public void consultarQtdeAlunoVinculadoDisciplina(OfertaDisciplinaVO ofertaDisciplinaVO) {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select count(mptd.codigo) as qtdeAlunos from matriculaperiodoturmadisciplina as mptd inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI', 'PR') and mptd.disciplina = ? and mptd.ano = ? and mptd.semestre = ? ", ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre());
		if(rs.next()) {
			ofertaDisciplinaVO.setQtdeAlunoMatriculados(rs.getInt("qtdeAlunos"));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoDisciplinaRegularDPAluno(OfertaDisciplinaVO ofertaDisciplinaVO, UsuarioVO usuarioVO) {
		StringBuilder sql  = new StringBuilder("");
		sql.append("  INSERT INTO public.matriculaperiodoturmadisciplina");
		sql.append("  (disciplina, turma, matriculaperiodo, semestre, ano, matricula, disciplinaincluida, disciplinaequivale, disciplinaequivalente,  ");
		sql.append("  disciplinafazpartecomposicao, reposicao, inclusaoforaprazo, notificacaodownloadmaterialenviado, modalidadedisciplina, ");
		sql.append("  permiteescolhermodalidade, conteudo, datanotificacaofrequenciabaixa, disciplinaporequivalencia, disciplinaemregimeespecial, ");
		sql.append("  disciplinaoptativa, disciplinareferenteaumgrupooptativa, gradecurriculargrupooptativadisciplina, gradedisciplinacomposta, gradedisciplina, ");
		sql.append("  mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, disciplinacomposta, transferenciamatrizcurricularmatricula, ");
		sql.append("  disciplinacursandoporcorrespondenciaapostransferencia, professor, programacaotutoriaonlineprofessor, inclusaoviasql, incluidaparacorrecaohistoricoforagrade, ");
		sql.append("  turmateorica, turmapratica, turmacorrigidaimportacao, liberadasemdisponibilidadevagas, usuarioliberadasemdisponibilidadevagas, ");
		sql.append("  dataliberadasemdisponibilidadevagas, dataultimaalteracao, logalteracao, created, codigocreated, nomecreated, updated, codigoupdated, nomeupdated, ");
		sql.append("  basehomologacao, importacaofinal)");
		sql.append("  (");
		sql.append("  select g.disciplina, turma.codigo,  m.codigo, m.semestre, m.ano, m.matricula, false, false, null, false, m.periodoletivomatricula != p.codigo, ");
		sql.append("  false, false, td.modalidadedisciplina, false, null, null,  false, false, false, false, null, null, g.codigo, null, null, null, null, ");
		sql.append("  false, null, null, true, false, null, null, null, null, null,  null, null, null, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', null, null, null, false, false");
		sql.append("  from matriculaperiodo m ");
		sql.append("  inner join matricula m2 on m2.matricula = m.matricula ");
		sql.append("  inner join periodoletivo pla on pla.codigo = m.periodoletivomatricula and pla.gradecurricular = m2.gradecurricularatual ");
		sql.append("  inner join periodoletivo p on p.gradecurricular = m.gradecurricular and p.periodoletivo <= pla.periodoletivo ");		
		sql.append("  inner join gradedisciplina g on g.periodoletivo = p.codigo");
		sql.append("  inner join disciplina d on d.codigo = g.disciplina ");
		sql.append("  inner join turma on turma.unidadeensino = m2.unidadeensino and turma.curso = m2.curso and turma.turno = m2.turno");
		sql.append("  and turma.gradecurricular = m2.gradecurricularatual and turma.periodoletivo = p.codigo and turma.situacao = 'AB' ");
		sql.append("  and turma.codigo =  ( ");
		sql.append("  	select t.codigo from turma as t inner join turmadisciplina td on td.turma = t.codigo and td.gradedisciplina = g.codigo where t.unidadeensino = m2.unidadeensino and t.curso = m2.curso and t.turno = m2.turno");
		sql.append("  	and t.gradecurricular = m2.gradecurricularatual and t.periodoletivo = p.codigo and t.situacao = 'AB'  order by t.codigo limit 1 ");
		sql.append("  ) ");
		sql.append("  inner join turmadisciplina td on td.turma = turma.codigo and td.gradedisciplina = g.codigo ");
		sql.append("  where m.situacaomatriculaperiodo in ('AT', 'PR') and d.codigo = ? and m.ano = ? and m.semestre = ? ");		
		sql.append("  and not exists (");
		sql.append("  	select h.codigo from historico h where h.matriculaperiodo = m.codigo and h.matrizcurricular = m2.gradecurricularatual  and h.disciplina = g.disciplina ");
		sql.append("  )");
		sql.append("  and not exists (");
		sql.append("  	select h.codigo from historico h where h.matricula  = m.matricula and h.matrizcurricular = m2.gradecurricularatual   and h.disciplina = g.disciplina  and h.situacao in ('AA', 'AP', 'AE', 'IS', 'AB', 'CC', 'CH', 'CS', 'CE')");
		sql.append("  ) ");
		sql.append("  and (coalesce(pla.numeroMaximoCargaHorariaAlunoPodeCursar,0) = 0 or (( pla.numeroMaximoCargaHorariaAlunoPodeCursar - coalesce((");
		sql.append("  	select sum(h.cargahorariadisciplina) from historico h where h.matriculaperiodo  = m.codigo  and h.matrizcurricular = m2.gradecurricularatual ");
		sql.append("  	and h.matriculaperiodoturmadisciplina is not null and h.situacao not in ('AA', 'AP', 'AE', 'IS', 'AB', 'CC', 'CH') ");
		sql.append("  ), 0)) >=  g.cargahoraria)) ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		if(getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre()) > 0) {
		
		
		sql  = new StringBuilder("");
		sql.append("  INSERT INTO public.historico ");
		sql.append("  (matriculaperiodo, dataregistro, responsavel, situacao, tipohistorico, matricula, disciplina, matriculaperiodoturmadisciplina, ");
		sql.append("  configuracaoacademico,  historicodisciplinafazpartecomposicao, ");
		sql.append("  anohistorico, semestrehistorico, cargahorariacursada, cargahorariadisciplina, historicodisciplinaoptativa, historicodisciplinaforagrade, nomedisciplina, ");
		sql.append("  historicoporequivalencia, historicoequivalente, mapaequivalenciadisciplina,  periodoletivomatrizcurricular, ");
		sql.append("  periodoletivocursada, matrizcurricular,  creditodisciplina,  historicodisciplinacomposta, disciplinareferenteaumgrupooptativa, ");
		sql.append("  historicoincluidoporsql, gradedisciplina, created, codigocreated, nomecreated, freguencia)");
		sql.append("  (");
		sql.append("  select m.codigo, m.data, ").append(usuarioVO.getCodigo()).append(",'CS',  case when periodoletivo.periodoletivo < p.periodoletivo");
		sql.append("  or exists (select h.codigo from historico h where h.matricula  = m.matricula  and h.matrizcurricular = m2.gradecurricularatual ");
		sql.append("  	and h.disciplina = g.disciplina  and h.situacao = 'RE' limit 1)");
		sql.append("  then 'DE' else case when periodoletivo.periodoletivo > p.periodoletivo then 'AD' else 'NO' end end,");
		sql.append("  m2.matricula, disciplina.codigo,   matriculaperiodoturmadisciplina.codigo,");
		sql.append(ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo()).append(", ");
		sql.append("   false, m.ano, m.semestre , g.cargahoraria,g.cargahoraria, false, false, disciplina.nome,");
		sql.append("   false, false, null, p.codigo,  periodoletivo.codigo, m2.gradecurricularatual, g.nrcreditos, false, false, true, g.codigo, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', ");
		sql.append("   100");
		sql.append("  from matriculaperiodo m ");
		sql.append("  inner join matricula m2 on m2.matricula = m.matricula ");
		sql.append("  inner join periodoletivo  on m.periodoletivomatricula = periodoletivo.codigo");
		sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = m.codigo ");
		sql.append("  inner join periodoletivo p on p.gradecurricular = m.gradecurricular ");
		sql.append("  inner join gradedisciplina g on g.periodoletivo = p.codigo and g.codigo = matriculaperiodoturmadisciplina.gradedisciplina ");
		sql.append("  inner join disciplina  on g.disciplina = disciplina.codigo and disciplina.codigo = ? ");
		sql.append("  where m.situacaomatriculaperiodo IN ('AT', 'PR') and m.ano = ? and m.semestre = ? ");		
		sql.append("  and not exists (");
		sql.append("  	select h.codigo from historico h where h.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sql.append("  )");
	
		sql.append("  ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre());
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoDisciplinaEquivalenteAluno(OfertaDisciplinaVO ofertaDisciplinaVO, UsuarioVO usuarioVO) {
		StringBuilder sql  = new StringBuilder("");
		sql.append("  INSERT INTO public.matriculaperiodoturmadisciplina");
		sql.append("  (disciplina, turma, matriculaperiodo, semestre, ano, matricula, disciplinaincluida, disciplinaequivale, disciplinaequivalente,  ");
		sql.append("  disciplinafazpartecomposicao, reposicao, inclusaoforaprazo, notificacaodownloadmaterialenviado, modalidadedisciplina, ");
		sql.append("  permiteescolhermodalidade, conteudo, datanotificacaofrequenciabaixa, disciplinaporequivalencia, disciplinaemregimeespecial, ");
		sql.append("  disciplinaoptativa, disciplinareferenteaumgrupooptativa, gradecurriculargrupooptativadisciplina, gradedisciplinacomposta, gradedisciplina, ");
		sql.append("  mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, disciplinacomposta, transferenciamatrizcurricularmatricula, ");
		sql.append("  disciplinacursandoporcorrespondenciaapostransferencia, professor, programacaotutoriaonlineprofessor, inclusaoviasql, incluidaparacorrecaohistoricoforagrade, ");
		sql.append("  turmateorica, turmapratica, turmacorrigidaimportacao, liberadasemdisponibilidadevagas, usuarioliberadasemdisponibilidadevagas, ");
		sql.append("  dataliberadasemdisponibilidadevagas, dataultimaalteracao, logalteracao, created, codigocreated, nomecreated, updated, codigoupdated, nomeupdated, ");
		sql.append("  basehomologacao, importacaofinal)");
		sql.append("  WITH cte_matriculadisciplinas AS (");
		sql.append("  select m.codigo as matriculaperiodocodigo, m.semestre, m.ano, m.matricula, g.disciplina, g.codigo as \"gradedisciplina\", m2.gradecurricularatual,");
		sql.append("  m2.curso, m2.turno, m2.unidadeensino, m.periodoletivomatricula as periodoletivomatricula, p.codigo as periodoletivo_codigo");
		sql.append("  from matriculaperiodo m ");
		sql.append("  inner join matricula m2 on m2.matricula = m.matricula ");
		sql.append("  inner join periodoletivo pla on pla.codigo = m.periodoletivomatricula and pla.gradecurricular = m2.gradecurricularatual ");
		sql.append("  inner join periodoletivo p on p.gradecurricular = m.gradecurricular and p.periodoletivo <= pla.periodoletivo ");		
		sql.append("  inner join gradedisciplina g on g.periodoletivo = p.codigo");
		sql.append("  inner join disciplina d on d.codigo = g.disciplina ");
		sql.append("  WHERE TRUE");
		sql.append("  AND m.situacaomatriculaperiodo IN ('AT', 'PR') ");
		sql.append("  AND m.ano = ? AND m.semestre = ? ");
		sql.append("  AND NOT EXISTS (SELECT h.codigo FROM historico h WHERE h.matriculaperiodo = m.codigo AND h.matrizcurricular = m2.gradecurricularatual AND h.disciplina = g.disciplina )");
		sql.append("  AND NOT EXISTS (SELECT h.codigo FROM historico h WHERE h.matricula = m.matricula AND h.matrizcurricular = m2.gradecurricularatual AND h.disciplina = g.disciplina");
		sql.append("  AND h.situacao IN ('AA', 'AP', 'AE', 'IS', 'AB', 'CC', 'CH', 'CS', 'CE') )");
		sql.append("  ), cte_insert_historico AS ( ");
		sql.append("  SELECT cte_matriculadisciplinas.*, mapaequivalenciadisciplina.*, mapaequivalenciadisciplina.disciplina as mapaequivalenciadisciplina_disciplina, turma.codigo as turma_codigo, turma.modalidadedisciplina as turma_modalidadedisciplina,");
		sql.append("  ROW_NUMBER () OVER(PARTITION BY cte_matriculadisciplinas.matricula, mapaequivalenciadisciplina.disciplina, cte_matriculadisciplinas.ano, cte_matriculadisciplinas.semestre) indice");		
		sql.append("  FROM cte_matriculadisciplinas");
		sql.append("  INNER JOIN LATERAL (SELECT mapaequivalenciadisciplina.codigo AS mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada.codigo AS mapaequivalenciadisciplinacursada,");
		sql.append("  mapaequivalenciadisciplinacursada.cargahoraria, mapaequivalenciadisciplinacursada.disciplina FROM mapaequivalenciamatrizcurricular");
		sql.append("  INNER JOIN mapaequivalenciadisciplina ON mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
		sql.append("  INNER JOIN mapaequivalenciadisciplinacursada ON mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sql.append("  INNER JOIN mapaequivalenciadisciplinamatrizcurricular ON mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sql.append("  WHERE mapaequivalenciamatrizcurricular.gradecurricular = cte_matriculadisciplinas.gradecurricularatual");
		sql.append("  AND mapaequivalenciamatrizcurricular.situacao = 'ATIVO' AND mapaequivalenciadisciplina.situacao = 'ATIVO'");
		sql.append("  AND mapaequivalenciadisciplinamatrizcurricular.disciplina = cte_matriculadisciplinas.disciplina AND mapaequivalenciadisciplinacursada.disciplina = ?");
		sql.append("  AND 1 = (SELECT count(medc.codigo) FROM mapaequivalenciadisciplinacursada medc WHERE medc.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo)");
		sql.append("  AND 1 = (SELECT count(medmc.codigo) FROM mapaequivalenciadisciplinamatrizcurricular medmc WHERE medmc.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo) ORDER BY");
		sql.append("  mapaequivalenciadisciplina.codigo DESC LIMIT 1 ) AS mapaequivalenciadisciplina ON mapaequivalenciadisciplina.mapaequivalenciadisciplina IS NOT NULL");
		sql.append("  INNER JOIN LATERAL (");
		sql.append("  SELECT 1 AS ordem, turma.codigo, turmadisciplina.gradedisciplina, gd.cargahoraria, turmadisciplina.modalidadedisciplina");
		sql.append("  FROM turma");
		sql.append("  INNER JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo");
		sql.append("  INNER JOIN gradedisciplina gd ON gd.codigo = turmadisciplina.gradedisciplina");
		sql.append("  WHERE turma.unidadeensino = cte_matriculadisciplinas.unidadeensino");
		sql.append("  AND turma.curso = cte_matriculadisciplinas.curso AND turma.turno = cte_matriculadisciplinas.turno");
		sql.append("  AND turma.gradecurricular != cte_matriculadisciplinas.gradecurricularatual AND turma.situacao = 'AB'");
		sql.append("  AND gd.disciplina = mapaequivalenciadisciplina.disciplina AND gd.cargahoraria = mapaequivalenciadisciplina.cargahoraria");
		sql.append("  UNION ALL");
		sql.append("  SELECT 2 AS ordem, turma.codigo, turmadisciplina.gradedisciplina, gd.cargahoraria, turmadisciplina.modalidadedisciplina");
		sql.append("  FROM turma");
		sql.append("  INNER JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo INNER JOIN gradedisciplina gd ON gd.codigo = turmadisciplina.gradedisciplina");
		sql.append("  WHERE turma.unidadeensino = cte_matriculadisciplinas.unidadeensino");
		sql.append("  AND turma.curso != cte_matriculadisciplinas.curso AND turma.situacao = 'AB'");
		sql.append("  AND gd.disciplina = mapaequivalenciadisciplina.disciplina AND gd.cargahoraria = mapaequivalenciadisciplina.cargahoraria");
		sql.append("  UNION ALL");
		sql.append("  SELECT 3 AS ordem, turma.codigo, turmadisciplina.gradedisciplina,gd.cargahoraria, turmadisciplina.modalidadedisciplina");
		sql.append("  FROM turma");
		sql.append("  INNER JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo");
		sql.append("  INNER JOIN gradedisciplina gd ON gd.codigo = turmadisciplina.gradedisciplina");
		sql.append("  WHERE turma.unidadeensino != cte_matriculadisciplinas.unidadeensino");
		sql.append("  AND turma.curso = cte_matriculadisciplinas.curso AND turma.situacao = 'AB'");
		sql.append("  AND gd.disciplina = mapaequivalenciadisciplina.disciplina AND gd.cargahoraria = mapaequivalenciadisciplina.cargahoraria");
		sql.append("  UNION ALL");
		sql.append("  SELECT 4 AS ordem, turma.codigo, turmadisciplina.gradedisciplina, gd.cargahoraria, turmadisciplina.modalidadedisciplina");
		sql.append("  FROM turma");
		sql.append("  INNER JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo");
		sql.append("  INNER JOIN gradedisciplina gd ON gd.codigo = turmadisciplina.gradedisciplina");
		sql.append("  WHERE turma.unidadeensino != cte_matriculadisciplinas.unidadeensino");
		sql.append("  AND turma.curso != cte_matriculadisciplinas.curso AND turma.situacao = 'AB'");
		sql.append("  AND gd.disciplina = mapaequivalenciadisciplina.disciplina AND gd.cargahoraria = mapaequivalenciadisciplina.cargahoraria");
		sql.append("  ORDER BY ordem, codigo");
		sql.append("  LIMIT 1 ) AS turma ON");
		sql.append("  turma.codigo IS NOT NULL");
		sql.append("  )");
		sql.append("  select mapaequivalenciadisciplina_disciplina, turma_codigo, matriculaperiodocodigo, semestre, ano, matricula, false, false, null, false, periodoletivomatricula != periodoletivo_codigo, ");
		sql.append("  false, false, turma_modalidadedisciplina, false, null, null,  true, false, false, false, null, null, gradedisciplina, mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, null, null, ");
		sql.append("  false, null, null, true, false, null, null, null, null, null,  null, null, null, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', null, null, null, false, false");
		sql.append("  FROM cte_insert_historico");
		sql.append("  WHERE cte_insert_historico.indice = 1	").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		if(getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo()) > 0) {
		
		
		sql  = new StringBuilder("");
		sql.append("  INSERT INTO public.historico ");
		sql.append("  (matriculaperiodo, dataregistro, responsavel, situacao, tipohistorico, matricula, disciplina, matriculaperiodoturmadisciplina, ");
		sql.append("  configuracaoacademico,  historicodisciplinafazpartecomposicao, ");
		sql.append("  anohistorico, semestrehistorico, cargahorariacursada, cargahorariadisciplina, historicodisciplinaoptativa, historicodisciplinaforagrade, nomedisciplina, ");
		sql.append("  historicoporequivalencia, historicoequivalente, mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, periodoletivomatrizcurricular, ");
		sql.append("  periodoletivocursada, matrizcurricular,  creditodisciplina,  historicodisciplinacomposta, disciplinareferenteaumgrupooptativa, ");
		sql.append("  historicoincluidoporsql, gradedisciplina, created, codigocreated, nomecreated, freguencia)");
		sql.append("  (");
		sql.append("  select m.codigo, m.data, ").append(usuarioVO.getCodigo()).append(",'CS',  case when periodoletivo.periodoletivo < p.periodoletivo");
		sql.append("  or exists (select h.codigo from historico h where h.matricula  = m.matricula  and h.matrizcurricular = m2.gradecurricularatual ");
		sql.append("  	and h.disciplina = gd.disciplina  and h.situacao = 'RE' limit 1)");
		sql.append("  then 'DE' else case when periodoletivo.periodoletivo > p.periodoletivo then 'AD' else 'NO' end end,");
		sql.append("  m2.matricula, disciplina.codigo,   matriculaperiodoturmadisciplina.codigo,");
		sql.append(ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo()).append(", ");
		sql.append("   false, m.ano, m.semestre , g.cargahoraria,g.cargahoraria, false, false, disciplina.nome,");
		sql.append("   false, true, mapaequivalenciadisciplina.codigo, mapaequivalenciadisciplinacursada.codigo, p.codigo,  periodoletivo.codigo, m2.gradecurricularatual, g.nrcreditos, false, false, true, g.codigo, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', ");
		sql.append("   100");
		sql.append("  from matriculaperiodo m ");
		sql.append("  inner join matricula m2 on m2.matricula = m.matricula ");
		sql.append("  inner join periodoletivo  on m.periodoletivomatricula = periodoletivo.codigo");
		sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = m.codigo ");
		sql.append("  inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.codigo = matriculaperiodoturmadisciplina.mapaequivalenciadisciplina ");
		sql.append("  inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.codigo = matriculaperiodoturmadisciplina.mapaequivalenciadisciplinacursada ");
		sql.append("  inner join mapaequivalenciadisciplinamatrizcurricular on mapaequivalenciadisciplina.codigo = mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina ");
		sql.append("  inner join periodoletivo p on p.gradecurricular = m.gradecurricular ");
		sql.append("  inner join gradedisciplina gd on gd.periodoletivo = p.codigo and gd.disciplina = mapaequivalenciadisciplinamatrizcurricular.disciplina ");
		sql.append("  inner join gradedisciplina g on g.codigo = matriculaperiodoturmadisciplina.gradedisciplina ");		
		sql.append("  inner join disciplina  on g.disciplina = disciplina.codigo and disciplina.codigo = ? ");
		sql.append("  where m.situacaomatriculaperiodo IN ('AT', 'PR') and m.ano = ? and m.semestre = ?   ");		
		sql.append("  and not exists (");
		sql.append("  	select h.codigo from historico h where h.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and h.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo and  coalesce(h.mapaequivalenciadisciplinacursada, 0) = mapaequivalenciadisciplinacursada.codigo ");
		sql.append("  )");
	
		sql.append("  ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre());
		
		sql  = new StringBuilder("");
		sql.append("  INSERT INTO public.historico ");
		sql.append("  (matriculaperiodo, dataregistro, responsavel, situacao, tipohistorico, matricula, disciplina, matriculaperiodoturmadisciplina, ");
		sql.append("  configuracaoacademico,  historicodisciplinafazpartecomposicao, ");
		sql.append("  anohistorico, semestrehistorico, cargahorariacursada, cargahorariadisciplina, historicodisciplinaoptativa, historicodisciplinaforagrade, nomedisciplina, ");
		sql.append("  historicoporequivalencia, historicoequivalente, mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, mapaequivalenciadisciplinamatrizcurricular, periodoletivomatrizcurricular, ");
		sql.append("  periodoletivocursada, matrizcurricular,  creditodisciplina,  historicodisciplinacomposta, disciplinareferenteaumgrupooptativa, ");
		sql.append("  historicoincluidoporsql, gradedisciplina, created, codigocreated, nomecreated, freguencia)");
		sql.append("  (");
		sql.append("  select m.codigo, m.data, ").append(usuarioVO.getCodigo()).append(",'CS',  case when periodoletivo.periodoletivo < p.periodoletivo");
		sql.append("  or exists (select h.codigo from historico h where h.matricula  = m.matricula  and h.matrizcurricular = m2.gradecurricularatual ");
		sql.append("  	and h.disciplina = gd.disciplina  and h.situacao = 'RE' limit 1)");
		sql.append("  then 'DE' else case when periodoletivo.periodoletivo > p.periodoletivo then 'AD' else 'NO' end end,");
		sql.append("  m2.matricula, disciplina.codigo,   null, ");
		sql.append(ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo()).append(", ");
		sql.append("   false, m.ano, m.semestre , gd.cargahoraria, gd.cargahoraria, false, false, disciplina.nome,");
		sql.append("   true, false, mapaequivalenciadisciplina.codigo, null, mapaequivalenciadisciplinamatrizcurricular.codigo, p.codigo,  periodoletivo.codigo, m2.gradecurricularatual, gd.nrcreditos, false, false, true, gd.codigo, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', ");
		sql.append("   100");
		sql.append("  from matriculaperiodo m ");
		sql.append("  inner join matricula m2 on m2.matricula = m.matricula ");
		sql.append("  inner join periodoletivo  on m.periodoletivomatricula = periodoletivo.codigo");
		sql.append("  inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = m.codigo ");
		sql.append("  inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.codigo = matriculaperiodoturmadisciplina.mapaequivalenciadisciplina ");
		sql.append("  inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.codigo = matriculaperiodoturmadisciplina.mapaequivalenciadisciplinacursada ");
		sql.append("  inner join mapaequivalenciadisciplinamatrizcurricular on mapaequivalenciadisciplina.codigo = mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina ");
		sql.append("  inner join periodoletivo p on p.gradecurricular = m.gradecurricular ");
		sql.append("  inner join gradedisciplina gd on gd.periodoletivo = p.codigo and gd.disciplina = mapaequivalenciadisciplinamatrizcurricular.disciplina ");
		sql.append("  inner join disciplina  on gd.disciplina = disciplina.codigo and disciplina.codigo = mapaequivalenciadisciplinamatrizcurricular.disciplina ");
		sql.append("  where m.situacaomatriculaperiodo IN ('AT', 'PR') and matriculaperiodoturmadisciplina.disciplina = ? and m.ano = ? and m.semestre = ?   ");		
		sql.append("  and not exists (");
		sql.append("  	select h.codigo from historico h where h.matricula = m.matricula AND h.anohistorico = m.ano AND h.semestrehistorico = m.semestre ");
		sql.append("  	and h.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and h.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo and  coalesce(h.mapaequivalenciadisciplinamatrizcurricular, 0) = mapaequivalenciadisciplinamatrizcurricular.codigo ");
		sql.append("  )");
		sql.append("  ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), ofertaDisciplinaVO.getDisciplinaVO().getCodigo(), ofertaDisciplinaVO.getAno(), ofertaDisciplinaVO.getSemestre());
		}
	}

	@Override
	public List<OfertaDisciplinaVO> consultarPorAnoSemestre(String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		consultar(OfertaDisciplina.idEntidade, true, usuarioVO);
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet("select ofertadisciplina.*, configuracaoAcademico.nome as configuracaoAcademico_nome, disciplina.nome as disciplina_nome, disciplina.abreviatura as  disciplina_abreviatura, (select count(mptd.codigo) from matriculaperiodoturmadisciplina as mptd inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI', 'PR') and mptd.disciplina = disciplina.codigo and mptd.ano = ofertadisciplina.ano and mptd.semestre = ofertadisciplina.semestre ) as qtdeAlunoMatriculados  from ofertadisciplina inner join disciplina on disciplina.codigo = ofertadisciplina.disciplina left join configuracaoAcademico on configuracaoAcademico.codigo = ofertadisciplina.configuracaoAcademico where ofertadisciplina.ano = ? and ofertadisciplina.semestre = ? order by  disciplina_nome ", ano, semestre);
		return montarDadosConsulta(rs);
	}
	
	@Override
	public synchronized OfertaDisciplinaVO consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(Integer disciplina, String ano, String semestre) throws Exception {
		String key  = ano+semestre+disciplina;
		OfertaDisciplinaVO oferta = getAplicacaoControle().ofertaDisciplina(key, null, Uteis.CONSULTAR);
		if(oferta == null) {
			SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet("select ofertadisciplina.*, configuracaoAcademico.nome as configuracaoAcademico_nome, disciplina.nome as disciplina_nome, disciplina.abreviatura as  disciplina_abreviatura, 0 as qtdeAlunoMatriculados  from ofertadisciplina inner join disciplina on disciplina.codigo = ofertadisciplina.disciplina left join configuracaoAcademico on configuracaoAcademico.codigo = ofertadisciplina.configuracaoAcademico where ofertadisciplina.ano = ? and ofertadisciplina.semestre = ? and ofertadisciplina.disciplina = ? ", ano, semestre, disciplina);		
			List<OfertaDisciplinaVO> ofertaDisciplinaVOs = montarDadosConsulta(rs);
			if(!ofertaDisciplinaVOs.isEmpty()) {
				getAplicacaoControle().ofertaDisciplina(key, ofertaDisciplinaVOs.get(0), Uteis.INCLUIR);
				oferta = ofertaDisciplinaVOs.get(0);
				return (OfertaDisciplinaVO) Uteis.clonar(oferta);
			}
			getAplicacaoControle().ofertaDisciplina(key, new OfertaDisciplinaVO(), Uteis.INCLUIR);
			return null;
		}
		return Uteis.isAtributoPreenchido(oferta.getCodigo()) ? (OfertaDisciplinaVO)  Uteis.clonar(oferta) : null;
	}
	
	private List<OfertaDisciplinaVO> montarDadosConsulta(SqlRowSet rs){
		List<OfertaDisciplinaVO> ofertaDisciplinaVOs =  new ArrayList<OfertaDisciplinaVO>(0);
		while(rs.next()) {
			OfertaDisciplinaVO ofertaDisciplinaVO =  new OfertaDisciplinaVO();
			ofertaDisciplinaVO.setNovoObj(!Uteis.isAtributoPreenchido(rs.getInt("codigo")));
			ofertaDisciplinaVO.setCodigo(rs.getInt("codigo"));
			ofertaDisciplinaVO.getConfiguracaoAcademicoVO().setCodigo(rs.getInt("configuracaoAcademico"));
			ofertaDisciplinaVO.getConfiguracaoAcademicoVO().setNome(rs.getString("configuracaoAcademico_nome"));
			ofertaDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
			ofertaDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
			ofertaDisciplinaVO.getDisciplinaVO().setAbreviatura(rs.getString("disciplina_abreviatura"));
			ofertaDisciplinaVO.setAno(rs.getString("ano"));
			ofertaDisciplinaVO.setSemestre(rs.getString("semestre"));
			ofertaDisciplinaVO.setCreated(rs.getDate("created"));
			ofertaDisciplinaVO.setCodigoCreated(rs.getInt("codigocreated"));
			ofertaDisciplinaVO.setNomeCreated(rs.getString("nomecreated"));
			ofertaDisciplinaVO.setUpdated(rs.getDate("updated"));
			ofertaDisciplinaVO.setCodigoUpdated(rs.getInt("codigoupdated"));
			ofertaDisciplinaVO.setNomeUpdated(rs.getString("nomeupdated"));
			ofertaDisciplinaVO.setQtdeAlunoMatriculados(rs.getInt("qtdeAlunoMatriculados"));
			if(rs.getObject("periodo") != null) {
				ofertaDisciplinaVO.setPeriodo(BimestreEnum.valueOf(rs.getString("periodo")));
			}
			ofertaDisciplinaVOs.add(ofertaDisciplinaVO);
		}
		return ofertaDisciplinaVOs;
	}
	
	@Override
	public void adicionarDisciplina(OfertaDisciplinaVO ofertaDisciplinaVO, List<OfertaDisciplinaVO> ofertaDisciplinaVOs) throws Exception {
		validarDados(ofertaDisciplinaVO);
		if(ofertaDisciplinaVOs.stream().anyMatch(o -> o.getDisciplinaVO().getCodigo().equals(ofertaDisciplinaVO.getDisciplinaVO().getCodigo()))) {
			throw new ConsistirException("A disciplina "+ofertaDisciplinaVO.getDisciplinaVO().getNome()+" já adicionada na lista.");
		}
		consultarQtdeAlunoVinculadoDisciplina(ofertaDisciplinaVO);
		ofertaDisciplinaVOs.add(ofertaDisciplinaVO);
		Ordenacao.ordenarLista(ofertaDisciplinaVOs, "disciplinaVO.nome");
	}
	
	private void validarDados(OfertaDisciplinaVO ofertaDisciplinaVO) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getDisciplinaVO())) {
			throw new ConsistirException("O campo DISCIPLINA deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getAno())) {
			throw new ConsistirException("O campo ANO da disciplina "+ofertaDisciplinaVO.getDisciplinaVO().getNome()+" deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getSemestre())) {
			throw new ConsistirException("O campo SEMESTRE da disciplina "+ofertaDisciplinaVO.getDisciplinaVO().getNome()+" deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getConfiguracaoAcademicoVO())) {
			throw new ConsistirException("O campo CONFIGURAÇÃO ACADÊMICO da disciplina "+ofertaDisciplinaVO.getDisciplinaVO().getNome()+" deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getPeriodo())) {
			throw new ConsistirException("O campo PERÍODO da disciplina "+ofertaDisciplinaVO.getDisciplinaVO().getNome()+" deve ser informado.");
		}
		
	}
	
	@Override
	public List<OfertaDisciplinaVO> consultarDisciplina(String ano, String semestre, String campoConsulta, String valorConsulta) throws Exception{
		StringBuilder sql = new StringBuilder("select null as codigo, null as  created, null as codigocreated, null as nomecreated,  null as  updated, null as codigoupdated, null as nomeupdated,  '");
		sql.append(ano).append("' as ano, '").append(semestre).append("' as semestre, CASE disciplina.classificacaodisciplina WHEN 'PROJETO_INTEGRADOR' then 4  WHEN 'ESTAGIO' then 5 WHEN 'TCC' then 3 else 2 end  as configuracaoAcademico, '' as configuracaoAcademico_nome, 'SEMESTRE_1' as periodo, disciplina.codigo as disciplina, disciplina.nome as disciplina_nome, disciplina.abreviatura as  disciplina_abreviatura, ");
		sql.append(" (select count(mptd.codigo) from matriculaperiodoturmadisciplina as mptd inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI', 'PR') and mptd.disciplina = disciplina.codigo and mptd.ano = ? and mptd.semestre = ? ) as qtdeAlunoMatriculados ");  
		sql.append(" from disciplina ");
		sql.append(" where not exists (select ofertadisciplina.codigo from ofertadisciplina where disciplina.codigo = ofertadisciplina.disciplina and ofertadisciplina.ano = ? and ofertadisciplina.semestre = ?) ");
		if(campoConsulta.equals("codigo")) {
			sql.append(" and disciplina.").append(campoConsulta).append(" >= ? ");
		}else {
			sql.append(" and sem_acentos(disciplina.").append(campoConsulta).append(") ilike sem_acentos(?) ");			
		}
		if(campoConsulta.equals("codigo")) {
			sql.append(" order by  disciplina ");
		}else if(campoConsulta.equals("nome")) {
			sql.append(" order by  disciplina_nome ");
		}else if(campoConsulta.equals("abreviatura")) {
			sql.append(" order by  disciplina_abreviatura ");
		}
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), ano, semestre, ano, semestre, (campoConsulta.equals("codigo") ? Uteis.getIsValorNumerico(valorConsulta) ? Integer.valueOf(valorConsulta) : 0 : valorConsulta+PERCENT));
		return montarDadosConsulta(rs);
	}
	
	@Override
	public void upload(FileUploadEvent uploadEvent, List<OfertaDisciplinaVO> ofertaDisciplinaVOs, List<OfertaDisciplinaVO> listaErro, String ano, String semestre) throws Exception {
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
		InputStream is = uploadEvent.getUploadedFile().getInputStream();
		int rowMax = 0;
		XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
		if (extensao.equals("xlsx")) {
//			PARA XLSX UTILIZA XSSFWorkbook
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			mySheetXlsx = workbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();

		} else {
//			PARA XLS UTILIZA HSSFWorkbook
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			mySheetXls = workbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
		}
		Row row = null;
		if (extensao.equals("xlsx")) {
			row = mySheetXlsx.getRow(0);
		} else {
			row = mySheetXls.getRow(0);
		}
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarTodasConfiguracaoAcademica(Uteis.NIVELMONTARDADOS_COMBOBOX, false, null);  
		int linha = 1;
		boolean existeRegistro = false;
		while (linha <= rowMax) {
			if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
			if(row != null && (row.getCell(0) != null || row.getCell(1) != null || row.getCell(2) != null)) {
				existeRegistro = true;
			OfertaDisciplinaVO ofertaDisciplinaVO =  new OfertaDisciplinaVO();			
			if(row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK && row.getCell(0).getCellType() != Cell.CELL_TYPE_STRING) {
				ofertaDisciplinaVO.setErroImportacao("O valor informado na coluna DISCIPLINA não é do tipo texto.");
			}else if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && !row.getCell(0).getStringCellValue().trim().isEmpty()) {
				ofertaDisciplinaVO.getDisciplinaVO().setAbreviatura(row.getCell(0).getStringCellValue());
				ofertaDisciplinaVO.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorAbreviaturaUnica(ofertaDisciplinaVO.getDisciplinaVO().getAbreviatura(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				if(!Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getDisciplinaVO())) {
					ofertaDisciplinaVO.getDisciplinaVO().setAbreviatura(row.getCell(0).getStringCellValue());
					ofertaDisciplinaVO.setErroImportacao("Não foi encontrado a disciplina de abreviatura '"+ofertaDisciplinaVO.getDisciplinaVO().getAbreviatura()+"'.");
				}
			}else {
				ofertaDisciplinaVO.setErroImportacao("Não foi informado a abreviatura da disciplina.");
			}
			if(row.getCell(1) != null && row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK && row.getCell(1).getCellType() != Cell.CELL_TYPE_STRING) {
				if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
					ofertaDisciplinaVO.setErroImportacao("O valor informado na coluna PERÍODO não é do tipo texto.");
				}else{
					ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>O valor informado na coluna PERÍODO não é do tipo texto.");
				}				
			}else if(row.getCell(1) != null &&  row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING && !row.getCell(1).getStringCellValue().trim().isEmpty()) {
				ofertaDisciplinaVO.setPeriodoImportacao(row.getCell(1).getStringCellValue());
				ofertaDisciplinaVO.setPeriodo(BimestreEnum.getBimestreEnumPorSigla(ofertaDisciplinaVO.getPeriodoImportacao().trim()));
				if(ofertaDisciplinaVO.getPeriodo().equals(BimestreEnum.NAO_CONTROLA)) {
					if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
						ofertaDisciplinaVO.setErroImportacao("O valor informado para o período é inválido informe as opções: B1, B2, B3, B4, S1 ou S2.");
					}else {
						ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>O valor informado para o período é inválido informe as opções: B1, B2, B3, B4, S1 ou S2.");
					}
				}
			}else {
				if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
					ofertaDisciplinaVO.setErroImportacao("Não foi informado o período da disciplina.");
				}else {
					ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>Não foi informado o período da disciplina.");
				}
			}
			if(row.getCell(2) != null && row.getCell(2).getCellType() != Cell.CELL_TYPE_BLANK && row.getCell(2).getCellType() != Cell.CELL_TYPE_STRING) {
				if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
					ofertaDisciplinaVO.setErroImportacao("O valor informado na coluna CONFIGURAÇÃO ACADÊMICO não é do tipo texto.");
				}else{
					ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>O valor informado na coluna CONFIGURAÇÃO ACADÊMICO não é do tipo texto.");
				}
			}else if(row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING && !row.getCell(2).getStringCellValue().trim().isEmpty()) {
				ofertaDisciplinaVO.setConfiguracaoAcademicoImportacao(row.getCell(2).getStringCellValue());
				if(configuracaoAcademicoVOs.stream().anyMatch(c -> Uteis.removerAcentuacao(c.getNome()).equalsIgnoreCase(Uteis.removerAcentuacao(ofertaDisciplinaVO.getConfiguracaoAcademicoImportacao())))){
					ConfiguracaoAcademicoVO c2 = configuracaoAcademicoVOs.stream().filter(c -> Uteis.removerAcentuacao(c.getNome()).equalsIgnoreCase(Uteis.removerAcentuacao(ofertaDisciplinaVO.getConfiguracaoAcademicoImportacao()))).findFirst().get();
					ofertaDisciplinaVO.getConfiguracaoAcademicoVO().setCodigo(c2.getCodigo());
					ofertaDisciplinaVO.getConfiguracaoAcademicoVO().setNome(c2.getNome());
				}else {
					if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
						ofertaDisciplinaVO.setErroImportacao("A configuração academica '"+ofertaDisciplinaVO.getConfiguracaoAcademicoImportacao()+"' não foi encontrada.");
					}else {
						ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>A configuração academica '"+ofertaDisciplinaVO.getConfiguracaoAcademicoImportacao()+"' não foi encontrada.");
					}
				}
			}else {
				if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
					ofertaDisciplinaVO.setErroImportacao("Não foi informado a configuração academica.");
				}else {
					ofertaDisciplinaVO.setErroImportacao(ofertaDisciplinaVO.getErroImportacao()+"<br/>Não foi informado a configuração academica.");
				}
			}
			if(ofertaDisciplinaVO.getErroImportacao().isEmpty()) {
				ofertaDisciplinaVO.setAno(ano);
				ofertaDisciplinaVO.setSemestre(semestre);
				if(!ofertaDisciplinaVOs.stream().anyMatch(o -> o.getDisciplinaVO().getCodigo().equals(ofertaDisciplinaVO.getDisciplinaVO().getCodigo()))) {
					consultarQtdeAlunoVinculadoDisciplina(ofertaDisciplinaVO);
					ofertaDisciplinaVOs.add(ofertaDisciplinaVO);					
				}
			}else {
				ofertaDisciplinaVO.setCodigo(linha);
				listaErro.add(ofertaDisciplinaVO);
			}
			}
			linha++;
			
		}
		if(!existeRegistro) {
			throw new Exception("Nenhum registro encontrado neste arquivo para ser importado, veja se o arquivo segue o modelo padrão.");
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoTurmaOfertaDisciplina(String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		realizarFechamentoTurmaDesnecessaria(usuarioVO);
		realizarFechamentoTurmaAbertaEmDuplicidade(usuarioVO);
		realizarReaberturaTurma(usuarioVO);
		realizarCriacaoTurmaPendente(ano, semestre, usuarioVO);
		realizarCriacaoTurmaDisciplinaPendente(ano, semestre, usuarioVO);
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarFechamentoTurmaDesnecessaria(UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	update turma set situacao = 'FE', apresentarrenovacaoonline = false where codigo in ( ");
		sql.append(" 	select turma.codigo ");
		sql.append(" 	from gradecurricular  ");
		sql.append(" 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo  ");
		sql.append(" 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");
		sql.append(" 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 	inner join turma on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 	and turma.curso =  curso.codigo	");
		sql.append(" 	and turma.turno =  turno.codigo");
		sql.append(" 	and turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" 	and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 	and turma.situacao = 'AB'");
		sql.append(" 	and (gradecurricular.situacao !=  'AT' and not exists (select m.matricula from matricula m where m.gradecurricularatual = gradecurricular.codigo and m.unidadeensino = unidadeensino.codigo and m.curso = curso.codigo and m.turno = turno.codigo and m.situacao in ('AT', 'TR') limit 1))");
		
		sql.append(" 	) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarFechamentoTurmaAbertaEmDuplicidade(UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	update turma set situacao = 'FE', apresentarrenovacaoonline = false from ( ");
		sql.append(" 	select max(turma.codigo) as turma, unidadeensino.codigo as unidadeensino , curso.codigo as curso, turno.codigo as turno, gradecurricular.codigo as gradecurricular, periodoletivo.codigo as periodoletivo ");
		sql.append(" 	from gradecurricular  ");
		sql.append(" 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sql.append(" 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");
		sql.append(" 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 	inner join turma on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 	and turma.curso =  curso.codigo	");
		sql.append(" 	and turma.turno =  turno.codigo");
		sql.append(" 	and turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" 	and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 	and turma.situacao = 'AB'");
		sql.append(" 	and ((gradecurricular.situacao =  'AT'  and unidadeensinocurso.situacaoCurso = 'AT') or exists (select m.matricula from matricula m where m.gradecurricularatual = gradecurricular.codigo and m.unidadeensino = unidadeensino.codigo  and m.curso = curso.codigo and m.turno = turno.codigo and m.situacao in ('AT', 'TR') limit 1))");
		sql.append(" 	group by unidadeensino.codigo , curso.codigo , turno.codigo , gradecurricular.codigo, periodoletivo.codigo having count(turma.codigo) > 1 ");
		sql.append(" 	) as t where t.turma != turma.codigo and t.unidadeensino = turma.unidadeensino and t.curso = turma.curso and t.turno = turma.turno and t.gradecurricular = turma.gradecurricular and t.periodoletivo = turma.periodoletivo and turma.situacao =  'AB' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarReaberturaTurma( UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	update turma set situacao = 'AB', apresentarrenovacaoonline = true where turma.codigo in ( ");
		sql.append(" 	select turma.codigo from ");
		sql.append(" 	(");		
		sql.append(" 	select distinct unidadeensino.codigo as unidadeensino, curso.codigo as curso, turno.codigo as turno, gradecurricular.codigo  as gradecurricular,");
		sql.append(" 	periodoletivo.codigo as codigo_periodoletivo, ");
		sql.append(" 	sem_acentos(coalesce(unidadeensino.abreviatura, unidadeensino.nome) ||'-'||curso.abreviatura||'-'||gradecurricular.nome||'-'||periodoletivo.periodoletivo||'P-'||(case when turno.nome = 'NOTURNO' then 'NOT' else case when turno.nome = 'MATUTINO' then 'MAT' else  case when turno.nome = 'VESPERTINO' then 'VEP' else '' end end end )) as turma");
		sql.append(" 	from gradecurricular  ");
		sql.append(" 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sql.append(" 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");
		sql.append(" 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 	where not exists (");
		sql.append(" 		select turma.codigo from turma where turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 		and turma.curso =  curso.codigo	");
		sql.append(" 		and turma.turno =  turno.codigo");
		sql.append(" 		and turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" 		and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 		and turma.situacao = 'AB' and apresentarrenovacaoonline = true ");
		sql.append(" 	)");
		sql.append(" 	and ((gradecurricular.situacao =  'AT'  and unidadeensinocurso.situacaoCurso = 'AT') or exists(select m.matricula from matricula m where m.gradecurricularatual = gradecurricular.codigo and m.unidadeensino = unidadeensino.codigo  and m.curso = curso.codigo and m.turno = turno.codigo and m.situacao in ('AT', 'TR') limit 1))");
		sql.append(" 	) as t ");
		sql.append("    inner join lateral (");
		sql.append(" 		select turma.codigo from turma where turma.unidadeensino = t.unidadeensino ");
		sql.append(" 		and turma.curso =  t.curso	");
		sql.append(" 		and turma.turno =  t.turno ");
		sql.append(" 		and turma.gradecurricular = t.gradecurricular ");
		sql.append(" 		and turma.periodoletivo = t.codigo_periodoletivo ");
		sql.append(" 		order by case when turma.situacao = 'AB' then 1 else 2 end, case when turma.identificadorturma = t.turma then 1 else 2 end, turma.codigo desc limit 1) as turma on turma.codigo is not null ");		
		sql.append(" 	) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarCriacaoTurmaPendente(String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	INSERT INTO turma");
		sql.append(" 	(situacao, curso, periodoletivo, turno, unidadeensino, gradecurricular, nrminimomatricula, nrmaximomatricula, nrvagas, identificadorturma,   semestral, anual,  ");
		sql.append(" 	planofinanceirocurso, subturma, turmaprincipal, configuracaoead, avaliacaoonline, tiposubturma, apresentarrenovacaoonline, abreviaturacurso, nrvagasinclusaoreposicao, datacriacao, ");
		sql.append(" 	categoriacondicaopagamento, dataultimaalteracao, codigoturnoapresentarcenso, considerarturmaavaliacaoinstitucional, created, codigocreated, nomecreated, incluidosql, turmaagrupada, ano, semestre, tabelaant)");
		sql.append(" 	(");		
		sql.append(" 	select 'AB', curso, codigo_periodoletivo, turno, unidadeensino, gradecurricular, 0, 10000, 10000, turma,  true, false, planofinanceirocurso, false, null, null, null, 'GERAL', true, '', 1000, now(), '', now(), ");
		sql.append(" 	turnocenso, true, now(),").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("', true, false, '").append(ano).append("', '").append(semestre).append("', 'OfertaDisciplina' from (");
		sql.append(" 	select distinct unidadeensino.codigo as unidadeensino, curso.codigo as curso, turno.codigo as turno, gradecurricular.codigo  as gradecurricular,");
		sql.append(" 	periodoletivo.codigo as codigo_periodoletivo, periodoletivo.periodoletivo, ");
		sql.append(" 	case when curso.niveleducacional = 'SU' then case when periodoletivo.periodoletivo = 1 then 2 else  5 end else 3 end as planofinanceirocurso,");
		sql.append(" 	(case when turno.nome ilike '%NOTURNO%' then 3 else case when turno.nome ilike '%MATUTINO%' then 1 else  case when turno.nome ilike '%VESPERTINO%' then 2 else 0 end end end ) as turnocenso,");
		sql.append(" 	sem_acentos(coalesce(unidadeensino.abreviatura, unidadeensino.nome) ||'-'||curso.abreviatura||'-'||gradecurricular.nome||'-'||periodoletivo.periodoletivo||'P-'||(case when turno.nome = 'NOTURNO' then 'NOT' else case when turno.nome = 'MATUTINO' then 'MAT' else  case when turno.nome = 'VESPERTINO' then 'VEP' else '' end end end )) as turma");		
		sql.append(" 	from gradecurricular  ");
		sql.append(" 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sql.append(" 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");
		sql.append(" 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 	where not exists (");
		sql.append(" 		select turma.codigo from turma where turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 		and turma.curso =  curso.codigo	");
		sql.append(" 		and turma.turno =  turno.codigo");
		sql.append(" 		and turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" 		and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 		and turma.situacao = 'AB'");
		sql.append(" 	)");
		sql.append(" 	and ((gradecurricular.situacao =  'AT' and unidadeensinocurso.situacaoCurso = 'AT') or exists(select m.matricula from matricula m where m.gradecurricularatual = gradecurricular.codigo and m.unidadeensino = unidadeensino.codigo and m.curso = curso.codigo and m.turno = turno.codigo and m.situacao in ('AT', 'TR') limit 1))");
		sql.append(" 	order by turma");
		sql.append(" ");
		sql.append(" 	) as t");
		sql.append(" 	) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarCriacaoTurmaDisciplinaPendente(String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	INSERT INTO public.turmadisciplina");
		sql.append(" 	(nralunosmatriculados, nrmaximomatricula, nrvagasmatricula, disciplina, turma, modalidadedisciplina,  configuracaoacademico, disciplinareferenteaumgrupooptativa, ");
		sql.append(" 	gradecurriculargrupooptativadisciplina, gradedisciplina, definicoestutoriaonline, ordemestudoonline, permitereposicao, permiteapoiopresencial, created, codigocreated, nomecreated) (");
		sql.append(" 	select 10000, 10000, 10000, disciplina.codigo, turma.codigo as turma, 'ON_LINE', ");
		sql.append(" 				gradedisciplina.configuracaoacademico as configuracaoacademico, ");
		sql.append(" 				false, null, gradedisciplina.codigo, 'DINAMICA', gradedisciplina.bimestre, true, false, now(), ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("' ");
		sql.append(" 			 	from gradecurricular  ");
		sql.append(" 			 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 			 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sql.append(" 			 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" 			 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 			 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");		
		sql.append(" 			 	inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
		sql.append(" 			 	inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
		sql.append(" 			 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 			 	inner join turma on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 			 	and turma.curso =  curso.codigo	");
		sql.append(" 			 	and turma.turno =  turno.codigo");
		sql.append(" 			 	and turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" 			 	and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 			 	and turma.situacao = 'AB' and turma.apresentarrenovacaoonline 		 	");
		sql.append(" 			 	where not  exists (");		
		sql.append(" 			 		select td.codigo from turmadisciplina as td where td.turma = turma.codigo and td.disciplina = gradedisciplina.disciplina");
		sql.append(" 			 	)");
		sql.append(" 			 	union all");
		sql.append(" 			 	select 10000, 10000, 10000, disciplina.codigo, turma.codigo as turma, 'ON_LINE', ");
		sql.append(" 				gradecurriculargrupooptativadisciplina.configuracaoacademico as configuracaoacademico, ");
		sql.append(" 				true, gradecurriculargrupooptativadisciplina.codigo, null, 'DINAMICA', gradecurriculargrupooptativadisciplina.bimestre, true, false, now(),  ").append(usuarioVO.getCodigo()).append(", '").append(usuarioVO.getNome()).append("' ");
		sql.append(" 			 	from gradecurricular  ");
		sql.append(" 			 	inner join curso on curso.codigo = gradecurricular.curso ");
		sql.append(" 			 	inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.situacaocurso  =  'AT' ");
		sql.append(" 			 	inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");		
		sql.append(" 			 	inner join turno on turno.codigo = unidadeensinocurso.turno");
		sql.append(" 			 	inner join periodoletivo  on periodoletivo.gradecurricular = gradecurricular.codigo  ");
		sql.append(" 			 	inner join gradecurriculargrupooptativa  on gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa");
		sql.append(" 			 	inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa");
		sql.append(" 			 	inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
		sql.append(" 			 	inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico and configuracaoacademico.matricularapenasdisciplinaaulaprogramada  ");
		sql.append(" 			 	inner join turma on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 			 	and turma.curso =  curso.codigo	");
		sql.append(" 			 	and turma.turno =  turno.codigo");
		sql.append(" 			 	and turma.gradecurricular = gradecurricular.codigo ");		
		sql.append(" 			 	and turma.periodoletivo = periodoletivo.codigo");
		sql.append(" 			 	and turma.situacao = 'AB' and turma.apresentarrenovacaoonline 		 	");
		sql.append(" 			 	where not  exists (");
		sql.append(" 			 		select td.codigo from turmadisciplina as td where td.turma = turma.codigo and td.disciplina = gradecurriculargrupooptativadisciplina.disciplina");
		sql.append(" 			 	)");
		sql.append(" 	) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

}
