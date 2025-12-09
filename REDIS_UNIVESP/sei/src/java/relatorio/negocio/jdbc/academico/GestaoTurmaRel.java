package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.academico.GestaoTurmaDisciplinaRelVO;
import relatorio.negocio.comuns.academico.GestaoTurmaRelVO;
import relatorio.negocio.interfaces.academico.GestaoTurmaRelInterfaceFacade;

@Service
@Lazy
@Scope("singleton")
public class GestaoTurmaRel extends ControleAcesso implements GestaoTurmaRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5290568659835113744L;

	@Override
	public List<GestaoTurmaRelVO> consultarDadosRelatorio(UnidadeEnsinoVO unidadeEnsinoVO,
			PeriodicidadeEnum periodicidade, String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs,
			List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaAgrupada, String situacaoProgramacaoAula, 
			String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma, String situacaoMatricula, 
			String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, Integer periodoLetivoDe, 
			Integer periodoLetivoAte, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, UsuarioVO usuarioVO, 
			Integer limit, Integer offset)
			throws Exception {
		GestaoTurmaRel.consultar(getIdEntidade(), true, usuarioVO);
		StringBuilder sql = realizarGeracaoSelectGestaoTurma(unidadeEnsinoVO, periodicidade, ano, semestre, filtrarPor, cursoVOs, turnoVOs, turmaVO, disciplinaVO, situacaoTurma, situacaoTurmaSubTurma, situacaoTurmaAgrupada, situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada, periodoLetivoDe, periodoLetivoAte, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, limit, offset);
//		System.out.println(sql.toString());		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDados(rs);
	}
	
	@Override
	public Integer consultarTotalRegistroEncontrado(UnidadeEnsinoVO unidadeEnsinoVO,
			PeriodicidadeEnum periodicidade, String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs,
			List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaAgrupada, String situacaoProgramacaoAula, 
			String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma, String situacaoMatricula, 
			String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, Integer periodoLetivoDe, Integer periodoLetivoAte, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, UsuarioVO usuarioVO)
			throws Exception {
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select count(turma.codigo) as qtde ");		
		sql.append("  from turma");
		sql.append("  inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sql.append("  inner join curso on curso.codigo = turma.curso");
		sql.append("  inner join turno on turno.codigo = turma.turno");
		sql.append("  inner join gradecurricular on gradecurricular.codigo = turma.gradecurricular");
		sql.append("  inner join periodoletivo on periodoletivo.codigo = turma.periodoletivo");	
		sql.append("  left join vagaturma on vagaturma.turma = turma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("'  and vagaturma.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' ");
		}
		sql.append("  left join horarioturma on horarioturma.turma = turma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("'  and horarioturma.semestrevigente = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
		}
		sql.append(realizarGeracaoClausulaWhere(unidadeEnsinoVO, periodicidade, ano, semestre, filtrarPor, cursoVOs, turnoVOs, turmaVO, disciplinaVO, situacaoTurma, situacaoTurmaSubTurma, situacaoTurmaAgrupada, 
				situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada,
				periodoLetivoDe, periodoLetivoAte, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada));
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}
	
	private List<GestaoTurmaRelVO> montarDados(SqlRowSet rs) throws Exception{
		List<GestaoTurmaRelVO> gestaoTurmaRelVOs = new ArrayList<GestaoTurmaRelVO>(0);
		while(rs.next()){
			gestaoTurmaRelVOs.add(montarDadosConsulta(rs));
		}
		realizarConsultaAulaProgramadaERegistrada(gestaoTurmaRelVOs);
		return gestaoTurmaRelVOs;
	}
	
	private GestaoTurmaRelVO montarDadosConsulta(SqlRowSet rs){
		GestaoTurmaRelVO obj = new GestaoTurmaRelVO();
		obj.getTurmaVO().setCodigo(rs.getInt("turma_codigo"));
		obj.getTurmaVO().setIdentificadorTurma(rs.getString("turma_identificadorturma"));
		obj.getTurmaVO().setSituacao(rs.getString("turma_situacao"));
		obj.getTurmaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino_codigo"));
		obj.getTurmaVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino_nome"));
		obj.getTurmaVO().getCurso().setCodigo(rs.getInt("curso_codigo"));
		obj.getTurmaVO().getCurso().setNome(rs.getString("curso_nome"));
		obj.getTurmaVO().getTurno().setCodigo(rs.getInt("turno_codigo"));
		obj.getTurmaVO().getTurno().setNome(rs.getString("turno_nome"));
		obj.getTurmaVO().getGradeCurricularVO().setCodigo(rs.getInt("gradecurricular_codigo"));
		obj.getTurmaVO().getGradeCurricularVO().setNome(rs.getString("gradecurricular_nome"));
		obj.getTurmaVO().getPeridoLetivo().setCodigo(rs.getInt("periodoletivo_codigo"));
		obj.getTurmaVO().getPeridoLetivo().setPeriodoLetivo(rs.getInt("periodoletivo_periodoletivo"));
		obj.getTurmaVO().getPeridoLetivo().setDescricao(rs.getString("periodoletivo_descricao"));
		obj.getTurmaVO().setNrVagas(rs.getInt("nrvagas"));
		obj.getTurmaVO().setNrMaximoMatricula(rs.getInt("nrmaximomatricula"));
		obj.getVagaTurmaDisciplinaVO().setVagaTurma(rs.getInt("vagaturma_codigo"));
		obj.getHorarioTurmaVO().setCodigo(rs.getInt("horarioturma_codigo"));
		montarDadosDisciplina(rs, obj);
		return obj;
	}
	
	private void montarDadosDisciplina(SqlRowSet rs, GestaoTurmaRelVO gestaoTurmaRelVO) {
		String[] disciplinas  = rs.getString("disciplinas").split("<DIS>");
		for(String disciplina: disciplinas){
			if(!disciplina.trim().isEmpty()){
			GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = new GestaoTurmaDisciplinaRelVO();
			try{
			String subTurma = disciplina.contains("\"subturma\":\"\"") ? "\"subturma\":\"\"" : disciplina.substring(disciplina.indexOf("\"subturma\":\"{")+13, disciplina.length());
			if(!subTurma.contains("\"subturma\":\"\"")){
				subTurma = subTurma.substring(0, subTurma.indexOf("\"turmaagrupada\":"));
			}
			disciplina.replace(subTurma, "");
			String turmaAgrupada = disciplina.contains("\"turmaagrupada\":\"\"") ? "\"turmaagrupada\":\"\"" : disciplina.substring(disciplina.indexOf("\"turmaagrupada\":\""), disciplina.length());
			if(!turmaAgrupada.contains("\"turmaagrupada\":\"\"")){
				turmaAgrupada.substring(0, turmaAgrupada.indexOf("}\""));
			}
			disciplina.replace(turmaAgrupada, "");
			String campos[] = disciplina.split(",");
			for(String campo: campos){
				if(!campo.contains("\"subturma\":") && !campo.contains("\"turmaagrupada\":")){
					campo = campo.replace("\"", "");				
					campo = campo.replace("{", "");					
					campo = campo.replace("}", "");				
					if(campo.contains(":") && campo.indexOf(":")+1 < campo.length()){
					String[] keyValue = campo.split(":"); 
					if(!keyValue[1].equals("null") && !keyValue[1].trim().isEmpty()){
						if(keyValue[0].equals("disciplina_codigo")){
							gestaoTurmaDisciplinaRelVO.getDisciplinaVO().setCodigo(Integer.valueOf(keyValue[1]));
						}else if(keyValue[0].equals("disciplina_nome")){
							gestaoTurmaDisciplinaRelVO.getDisciplinaVO().setNome(keyValue[1]);
						}else if(keyValue[0].equals("vagaturmadisciplina_codigo")){
							gestaoTurmaDisciplinaRelVO.getVagaTurmaDisciplinaVO().setCodigo(Integer.valueOf(keyValue[1]));
						}else if(keyValue[0].equals("nrvagas")){
							gestaoTurmaDisciplinaRelVO.setTotalVaga(Integer.valueOf(keyValue[1]));
						}else if(keyValue[0].equals("nrmaximomatricula")){
							gestaoTurmaDisciplinaRelVO.setTotalMaximaVaga(Integer.valueOf(keyValue[1]));
						}else if(keyValue[0].equals("horarioturma_codigo")){
							gestaoTurmaDisciplinaRelVO.getHorarioTurmaVO().setCodigo(Integer.valueOf(keyValue[1]));
						}else if(keyValue[0].equals("totalmatriculados")){
							gestaoTurmaDisciplinaRelVO.setTotalMatriculado(keyValue[1]);
						}else if(keyValue[0].equals("totalprematriculados")){
							gestaoTurmaDisciplinaRelVO.setTotalPreMatriculado(keyValue[1]);
						}
					}
					}					
				}
			}	
			if(!subTurma.contains("\"subturma\":\"\"")){
				subTurma = subTurma.replace("\"subturma\":", "");				
				montarDadosSubTurmaETurmaAgrupada(subTurma.split("<SUB>"), gestaoTurmaDisciplinaRelVO);									
			}
			if(!turmaAgrupada.contains("\"turmaagrupada\":\"\"")){
				turmaAgrupada = turmaAgrupada.replace("\"turmaagrupada\":", "");	
				montarDadosSubTurmaETurmaAgrupada(turmaAgrupada.split("<AGR>"), gestaoTurmaDisciplinaRelVO);									
			}
			gestaoTurmaDisciplinaRelVO.setGestaoTurmaRelVO(gestaoTurmaRelVO);
			gestaoTurmaRelVO.getGestaoTurmaDisciplinaRelVOs().add(gestaoTurmaDisciplinaRelVO);		
			
			}catch(Exception e){
				throw e;
			}
			}
		}
	}
	
	private void montarDadosSubTurmaETurmaAgrupada(String[] turmas, GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO) {
		for(String turma: turmas){
			if(!turma.trim().isEmpty()){
				GestaoTurmaRelVO gestaoTurmaRelVO = new GestaoTurmaRelVO();
			String campos[] = turma.split(",");
			for(String campo:campos){
				campo = campo.replace("\"subturma\":", "");				
				campo = campo.replace("\"", "");
				campo = campo.replace("\\", "");	
				campo = campo.replace("{", "");					
				campo = campo.replace("}", "");
				if(campo.contains(":") && campo.indexOf(":")+1 < campo.length()){
					try{
				String[] keyValue = campo.split(":"); 
				if(!keyValue[1].equals("null") && !keyValue[1].trim().isEmpty()){
					if(keyValue[0].equals("turma_codigo")){
						gestaoTurmaRelVO.getTurmaVO().setCodigo(Integer.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("turma_identificadorturma")){
						gestaoTurmaRelVO.getTurmaVO().setIdentificadorTurma(keyValue[1]);
					}else if(keyValue[0].equals("turma_situacao")){
						gestaoTurmaRelVO.getTurmaVO().setSituacao(keyValue[1]);
					}else if(keyValue[0].equals("turma_subturma")){
						gestaoTurmaRelVO.getTurmaVO().setSubturma(Boolean.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("turma_tiposubturma")){
						gestaoTurmaRelVO.getTurmaVO().setTipoSubTurma(TipoSubTurmaEnum.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("turma_turmaagrupada")){
						gestaoTurmaRelVO.getTurmaVO().setTurmaAgrupada(Boolean.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("vagaturmadisciplina_codigo")){
						gestaoTurmaRelVO.getVagaTurmaDisciplinaVO().setCodigo(Integer.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("nrvagas")){
						gestaoTurmaRelVO.setTotalVaga(Integer.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("nrmaximomatricula")){
						gestaoTurmaRelVO.setTotalMaximaVaga(Integer.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("totalmatriculados")){
						if(keyValue[1].equals("0-0")) {
							gestaoTurmaRelVO.setTotalMatriculado("0");
						}else {
							gestaoTurmaRelVO.setTotalMatriculado(keyValue[1]);
						}						
					}else if(keyValue[0].equals("totalprematriculados")){
						if(keyValue[1].equals("0-0")) {
							gestaoTurmaRelVO.setTotalPreMatriculado("0");
						}else {
							gestaoTurmaRelVO.setTotalPreMatriculado(keyValue[1]);
						}
					}else if(keyValue[0].equals("horarioturma_codigo")){
						gestaoTurmaRelVO.getHorarioTurmaVO().setCodigo(Integer.valueOf(keyValue[1]));										
					}else if(keyValue[0].equals("turmaprincipal_codigo")){
						gestaoTurmaRelVO.getTurmaVO().setTurmaPrincipal(Integer.valueOf(keyValue[1]));
					}else if(keyValue[0].equals("turmaprincipal_identificadorturma")){
						gestaoTurmaRelVO.getTurmaVO().setIdentificadorTurmaPrincipal(keyValue[1]);
					}
				}
					}catch(Exception e){
						throw e;
					}
				}
			}
			gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVOs().add(gestaoTurmaRelVO);
			}			

		}
	}
	
	
	private StringBuilder realizarGeracaoSelectGestaoTurma(UnidadeEnsinoVO unidadeEnsinoVO,
			PeriodicidadeEnum periodicidade, String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs,
			List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaTurmaAgrupada,
			String situacaoProgramacaoAula, String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma, 
			String situacaoMatricula, String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, 
			Integer periodoLetivoDe, Integer periodoLetivoAte, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, Integer limit, Integer offset){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select turma.codigo as turma_codigo, turma.identificadorturma as turma_identificadorturma, ");
		sql.append(" turma.situacao as turma_situacao, ");
		sql.append(" unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome,");
		sql.append(" curso.codigo as curso_codigo, curso.nome as curso_nome,");
		sql.append(" turno.codigo as turno_codigo, turno.nome as turno_nome,");
		sql.append(" gradecurricular.codigo as gradecurricular_codigo, gradecurricular.nome as gradecurricular_nome,");
		sql.append(" periodoletivo.codigo as periodoletivo_codigo, periodoletivo.periodoletivo as periodoletivo_periodoletivo, periodoletivo.descricao as periodoletivo_descricao, ");
		sql.append(" turma.nrvagas,");
		sql.append(" turma.nrmaximomatricula,");
		sql.append(" horarioturma.codigo as horarioturma_codigo,");
		sql.append(" vagaturma.codigo as vagaturma_codigo, "); 
		sql.append(" array_to_string(array( select row_to_json(t) from ( ").append(realizarGeracaoSelectGestaoTurmaDisciplina(periodicidade,  ano, semestre, disciplinaVO, turmaVO,
				situacaoTurma, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma,  situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, 
				situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, false) ).append(" ) as t), '<DIS>') as disciplinas ");
		sql.append("  from turma");
		sql.append("  inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sql.append("  inner join curso on curso.codigo = turma.curso");
		sql.append("  inner join turno on turno.codigo = turma.turno");
		sql.append("  inner join gradecurricular on gradecurricular.codigo = turma.gradecurricular");
		sql.append("  inner join periodoletivo on periodoletivo.codigo = turma.periodoletivo");
		sql.append("  left join vagaturma on vagaturma.turma = turma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("'  and vagaturma.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' ");
		}
		sql.append("  left join horarioturma on horarioturma.turma = turma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("'  and horarioturma.semestrevigente = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
		}
		if(limit != null && limit > 0){
			sql.append(" where turma.codigo in ( ");
			sql.append(" select turma.codigo ");		
			sql.append("  from turma");
			sql.append("  inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
			sql.append("  inner join curso on curso.codigo = turma.curso");
			sql.append("  inner join turno on turno.codigo = turma.turno");
			sql.append("  inner join gradecurricular on gradecurricular.codigo = turma.gradecurricular");
			sql.append("  inner join periodoletivo on periodoletivo.codigo = turma.periodoletivo");
			sql.append("  left join vagaturma on vagaturma.turma = turma.codigo ");
			if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
				sql.append("  and vagaturma.ano = '").append(ano).append("'  and vagaturma.semestre = '").append(semestre).append("'");
			}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
				sql.append("  and vagaturma.ano = '").append(ano).append("' ");
			}
			sql.append("  left join horarioturma on horarioturma.turma = turma.codigo ");
			sql.append("  and exists(select horarioturmadiaitem.codigo from horarioturmadiaitem inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
			sql.append(" where horarioturma.codigo = horarioturmadia.horarioturma and horarioturmadiaitem.disciplina is not null and horarioturmadiaitem.professor is not null limit 1 ) ");
			 
			if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("'  and horarioturma.semestrevigente = '").append(semestre).append("'");
			}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
			}
			sql.append(realizarGeracaoClausulaWhere(unidadeEnsinoVO, periodicidade, ano, semestre, filtrarPor, cursoVOs, turnoVOs, turmaVO, disciplinaVO, situacaoTurma, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, 
					situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada, periodoLetivoDe, periodoLetivoAte, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada));	
			sql.append(" order by unidadeensino.nome, curso.nome, turno.nome, periodoletivo.periodoletivo, turma.identificadorturma ");
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
			sql.append(" )");
		}else{
			sql.append(realizarGeracaoClausulaWhere(unidadeEnsinoVO, periodicidade, ano, semestre, filtrarPor, cursoVOs, turnoVOs, turmaVO, disciplinaVO, situacaoTurma, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, 
					situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada, periodoLetivoDe, periodoLetivoAte, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada));
		}
		sql.append(" order by unidadeensino_nome, curso_nome, turno_nome, periodoletivo_periodoletivo, turma_identificadorturma ");
		
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectGestaoTurmaDisciplina(PeriodicidadeEnum periodicidade, String ano, String semestre, DisciplinaVO disciplinaVO, TurmaVO turmaVO,
			String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaTurmaAgrupada,
			String situacaoProgramacaoAula,
			String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada,
			String situacaoVagaTurmaBase, String situacaoVagaSubTurma, String situacaoMatricula, 
			String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, Boolean usarLimit1){
		StringBuilder sql  = new StringBuilder(" ");
		if(usarLimit1){
			sql.append(" ( select disciplina.codigo ");	
		}else{
			sql.append(" select disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, "); 
			sql.append(" vagaturmadisciplina.codigo as vagaturmadisciplina_codigo,");
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrvagasmatricula else turma.nrvagas end as nrvagas,");		
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrmaximomatricula else turma.nrmaximomatricula   end as nrmaximomatricula, ");	
			sql.append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turma", true, disciplinaVO)).append(", ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'",true, false, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase)).append(" as totalMatriculados, ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'PR'",  true, false, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase)).append(" as totalPreMatriculados, ");
			if(!filtrarDisciplinaComTurmaAgrupada.equals("apenas") && !filtarDisciplinaComSubTurma.equals("naofiltrar")){
				sql.append(" array_to_string(array(select row_to_json(st) from ( ").append(realizarGeracaoSelectDisciplinaSubTurma(periodicidade, ano, semestre, disciplinaVO, null, situacaoTurmaSubTurma, situacaoProgramacaoAulaSubTurma, situacaoVagaSubTurma, situacaoMatriculaSubTurma, false, true) ).append(" ) as st ), '<SUB>') as subturma, ");
			}else{
				sql.append(" '' as subturma, ");
			}
			if(!filtarDisciplinaComSubTurma.equals("apenas") && !filtrarDisciplinaComTurmaAgrupada.equals("naofiltrar")){
				sql.append(" array_to_string(array(select row_to_json(st) from ( ").append(realizarGeracaoSelectDisciplinaTurmaAgrupada(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaTurmaAgrupada, "", false, true, situacaoVagaTurmaBase) ).append(" ) as st ), '<AGR>') as turmaagrupada ");
			}else{
				sql.append(" '' as turmaagrupada ");
			}
		}
		sql.append(" from disciplina ");
		sql.append(" left join vagaturma on vagaturma.turma = turma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("'  and vagaturma.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' ");
		}
		sql.append(" left join vagaturmadisciplina  on  vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina = disciplina.codigo ");
		sql.append(" where disciplina.codigo in ( ").append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false)).append(") ");
		if(!filtrarDisciplinaComTurmaAgrupada.equals("apenas") && !filtarDisciplinaComSubTurma.equals("apenas")){
			String and = "";
			if(!situacaoVagaTurmaBase.trim().isEmpty()  
					|| (situacaoProgramacaoAula.equals("possui") 
					|| situacaoProgramacaoAula.equals("naoPossui")) 
					|| situacaoProgramacaoAula.equals("aulaNaoRegistrada")
					|| situacaoMatricula.equals("possui") 
					|| situacaoMatricula.equals("naoPossui")){
				sql.append(" and ( ( ");
			}else{
				and = " and ";
			}
			if(!situacaoTurma.trim().isEmpty()){
				sql.append(and).append(" turma.situacao = '").append(situacaoTurma).append("' ");
				and = " and ";
			}
			if(situacaoMatricula.equals("possui") || situacaoMatricula.equals("naoPossui")){
				sql.append(" ").append(and).append(situacaoMatricula.equals("naoPossui")?" not  " : "").append(" exists (");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'PR', 'FO'", true, true, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase)); 
				sql.append(" )");
				and = " and ";
			}
			if(!situacaoVagaTurmaBase.trim().isEmpty()){
				if(situacaoVagaTurmaBase.equals("possui")){
					sql.append(" ").append(and).append(" ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 ) ");											
				}else if(situacaoVagaTurmaBase.equals("vagaDisponivel")){
					sql.append(" ").append(and).append(" ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula > ");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase));
					sql.append(" )"); 
				}else if(situacaoVagaTurmaBase.equals("vagaPreenchida")){
					sql.append(" ").append(and).append(" ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula <= ");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase));
					sql.append(" )"); 
				}else if(situacaoVagaTurmaBase.equals("vagaAcima")){
					sql.append(" ").append(and).append(" ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula < ");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoTurmaSubTurma, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVagaTurmaBase));
					sql.append(" )"); 
				}else if(situacaoVagaTurmaBase.equals("naoPossui")){
					sql.append(" ").append(and).append(" (vagaturmadisciplina.codigo is null or vagaturmadisciplina.nrvagasmatricula = 0) ");
				}
				and = " and ";
			}
			
			if((situacaoProgramacaoAula.equals("possui"))){		
				sql.append(" ").append(and).append(" ((select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turma", true, disciplinaVO)).append(") is not null) ");								
				and = " and ";
			}else if(situacaoProgramacaoAula.equals("naoPossui")){
				sql.append(" ").append(and).append(" ");				
				sql.append(" not exists (select horarioturmadiaitem.disciplina from horarioturmadia   ");
				sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
				sql.append(" where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");
				sql.append(" union all ");						
				sql.append(" select horarioturmadiaitem.disciplina from horarioturma ht   ");
				sql.append(" inner join turma as t on ht.turma = t.codigo ");
				sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = ht.codigo ");
				sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
				sql.append(" where t.subturma and t.turmaprincipal = turma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");
				if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
					sql.append("  and ht.anovigente = '").append(ano).append("'  and ht.semestrevigente = '").append(semestre).append("'");
				}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
					sql.append("  and ht.anovigente = '").append(ano).append("' ");
				}
				sql.append(" union all ");						
				sql.append(" select horarioturmadiaitem.disciplina from horarioturma ht   ");
				sql.append(" inner join turma as t on ht.turma = t.codigo ");
				sql.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo ");						
				sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = ht.codigo ");
				sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
				sql.append(" where turmaagrupada.turma = turma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");						
				if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
					sql.append("  and ht.anovigente = '").append(ano).append("'  and ht.semestrevigente = '").append(semestre).append("'");
				}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
					sql.append("  and ht.anovigente = '").append(ano).append("' ");
				}
				
				sql.append(" limit 1) ");						

				and = " and ";
				
				
			}else if(situacaoProgramacaoAula.equals("aulaNaoRegistrada")){			
				sql.append(" ").append(and).append(" ( (exists (select horarioturmadiaitem.codigo from horarioturma ");
				sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
				sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");			
				sql.append("  and horarioturmadiaitem.disciplina = disciplina.codigo");			
				sql.append(" where  horarioturma.turma = turma.codigo ");
				if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
					sql.append("  and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("'");
				}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
					sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
				}	
				sql.append(" and not exists (select registroaula.codigo from registroaula  ");
				sql.append(" where  registroaula.turma = turma.codigo and registroaula.disciplina = horarioturmadiaitem.disciplina ");
				sql.append(" and  registroaula.data = horarioturmadiaitem.data and registroaula.nraula = horarioturmadiaitem.nraula limit 1) limit 1)) ");
				sql.append(" ) ");
				and = " and ";
			}
			
			if(!situacaoVagaTurmaBase.trim().isEmpty()  
					|| (situacaoProgramacaoAula.equals("possui") 
					|| situacaoProgramacaoAula.equals("naoPossui")) 
					|| situacaoProgramacaoAula.equals("aulaNaoRegistrada")
					|| situacaoMatricula.equals("possui") 
					|| situacaoMatricula.equals("naoPossui")){				
				sql.append(" ) ");
			}
			if(!situacaoVagaTurmaBase.trim().isEmpty() 
					|| (situacaoProgramacaoAula.equals("possui") 
					|| situacaoProgramacaoAula.equals("naoPossui")) 
					|| situacaoProgramacaoAula.equals("aulaNaoRegistrada")){
				if(!filtrarDisciplinaComTurmaAgrupada.equals("apenas") && !filtarDisciplinaComSubTurma.equals("naofiltrar")){
					sql.append(" or ( exists (").append(realizarGeracaoSelectDisciplinaSubTurma(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaSubTurma, situacaoProgramacaoAulaSubTurma, situacaoVagaSubTurma, situacaoMatriculaSubTurma, true, true)).append("))");
				}
				if(!filtarDisciplinaComSubTurma.equals("apenas") && !filtrarDisciplinaComTurmaAgrupada.equals("naofiltrar")){
					sql.append(" or ( exists (").append(realizarGeracaoSelectDisciplinaTurmaAgrupada(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaTurmaAgrupada, situacaoMatriculaTurmaAgrupada, true, true, situacaoVagaTurmaBase)).append(")) ");
				}				
			}
			if(!situacaoVagaTurmaBase.trim().isEmpty()  
					|| (situacaoProgramacaoAula.equals("possui") 
					|| situacaoProgramacaoAula.equals("naoPossui")) 
					|| situacaoProgramacaoAula.equals("aulaNaoRegistrada")
					|| situacaoMatricula.equals("possui") 
					|| situacaoMatricula.equals("naoPossui")){				
				sql.append(" ) ");
			}
		}
		if(filtarDisciplinaComSubTurma.equals("apenas")){
			sql.append(" and exists ( ");
			sql.append(realizarGeracaoSelectDisciplinaSubTurma(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaSubTurma, situacaoProgramacaoAulaSubTurma, situacaoVagaSubTurma, situacaoMatriculaSubTurma, true, true));
			sql.append(" ) ");
		}
		if(filtrarDisciplinaComTurmaAgrupada.equals("apenas")){
			sql.append(" and exists ( ");
			sql.append(realizarGeracaoSelectDisciplinaTurmaAgrupada(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaTurmaAgrupada, situacaoProgramacaoAulaTurmaAgrupada, situacaoMatriculaTurmaAgrupada, true, true, situacaoVagaTurmaBase));
			sql.append(" ) ");
		}
		if(!usarLimit1){
			sql.append(" order by disciplina_nome ");
		}
		if(usarLimit1){
			sql.append(" limit 1) ");	
		}
		return sql;
	}
	
	private StringBuilder realizarGeracaoClausulaWhere(UnidadeEnsinoVO unidadeEnsinoVO,
			PeriodicidadeEnum periodicidade, String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs,
			List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoSubTurma, String situacaoTurmaAgrupada,
			String situacaoProgramacaoAula, String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma,  
			String situacaoMatricula, String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, Integer periodoLetivoDe, Integer periodoLetivoAte, 
			String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" where turma.subturma = false and turma.turmaagrupada =  false");
		sql.append(" and curso.periodicidade = '").append(periodicidade.getValor()).append("' ");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)){
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo());
		}
		if(filtrarPor.equals("curso")){
			StringBuilder in = new StringBuilder("");
			for(CursoVO cursoVO:cursoVOs){
				if(cursoVO.getFiltrarCursoVO()){
					if(!in.toString().trim().isEmpty()){
						in.append(", ");
					}
					in.append(cursoVO.getCodigo());
				}
			}
			if(!in.toString().trim().isEmpty()){						
				sql.append(" and curso.codigo in (").append(in).append(") ");
			}
			in = new StringBuilder("");
			for(TurnoVO turnoVO:turnoVOs){
				if(turnoVO.getFiltrarTurnoVO()){
					if(!in.toString().trim().isEmpty()){
						in.append(", ");
					}
					in.append(turnoVO.getCodigo());
				}
			}
			if(!in.toString().trim().isEmpty()){
				sql.append(" and turno.codigo in (").append(in).append(") ");
			}
		}
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append(" and exists (").append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false)).append(") ");
		}
		if(Uteis.isAtributoPreenchido(turmaVO) && filtrarPor.equals("turma")){
			if(!turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma() ){
				sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
			}else if(!turmaVO.getTurmaAgrupada() && turmaVO.getSubturma() ){
				sql.append(" and turma.codigo in ( select t.turmaprincipal from turma as t where t.codigo = ").append(turmaVO.getCodigo()).append(" and  t.turmaprincipal = turma.codigo ) ");
			}else if(turmaVO.getTurmaAgrupada()){
				sql.append(" and turma.codigo in ( select ta.turma from turmaagrupada ta where ta.turmaorigem = ").append(turmaVO.getCodigo()).append(" and  ta.turma = turma.codigo ) ");
			}
		}
		if(!Uteis.isAtributoPreenchido(turmaVO)  || filtrarPor.equals("curso")){
			if(Uteis.isAtributoPreenchido(periodoLetivoDe)){
				sql.append(" and periodoLetivo.periodoLetivo >= ").append(periodoLetivoDe);
			}
			if(Uteis.isAtributoPreenchido(periodoLetivoAte)){
				sql.append(" and periodoLetivo.periodoLetivo <= ").append(periodoLetivoAte);
			}
		}
			if(!filtarDisciplinaComSubTurma.equals("apenas") && !filtrarDisciplinaComTurmaAgrupada.equals("apenas")){
				sql.append(" and (( 1=1 ");
				if(!situacaoTurma.trim().isEmpty()){
					sql.append(" and turma.situacao = '").append(situacaoTurma).append("' ");
				}
				if((situacaoMatricula.equals("possui") || situacaoMatricula.equals("naoPossui"))){
					sql.append(" and ").append(situacaoMatricula.equals("naoPossui")?" not  " : "").append(" exists (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'PR', 'FO'", false, true, disciplinaVO, turmaVO, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, situacaoSubTurma, situacaoTurmaAgrupada, situacaoProgramacaoAulaSubTurma, situacaoProgramacaoAulaTurmaAgrupada, situacaoVagaSubTurma, situacaoMatriculaSubTurma, situacaoVaga)); 
					sql.append(" ) ");						
				}
				
				// Este if so será executado caso não esteja filtrando a programação de aula pois na propria validação da programação de aula a mesma valida as vagas 
				if((!situacaoVaga.trim().isEmpty()) 						
					&& !(situacaoProgramacaoAula.equals("possui") || situacaoProgramacaoAula.equals("naoPossui") || situacaoProgramacaoAula.equals("aulaNaoRegistrada") )){
					
					sql.append(getSqlVagaTurmaBase(periodicidade, ano, semestre, situacaoVaga, disciplinaVO, turmaVO, true));
					
				
				}
				if((situacaoProgramacaoAula.equals("possui") || situacaoProgramacaoAula.equals("naoPossui"))){
					if(situacaoProgramacaoAula.equals("naoPossui")){

						sql.append(" and ( exists (select disciplina.codigo from disciplina where codigo in ( ");
						sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false));
						sql.append(" ) and not exists (select horarioturmadiaitem.disciplina from horarioturmadia   ");
						sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
						sql.append(" where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");
						sql.append(" union all ");						
						sql.append(" select horarioturmadiaitem.disciplina from horarioturma ht   ");
						sql.append(" inner join turma as t on ht.turma = t.codigo ");
						sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = ht.codigo ");
						sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
						sql.append(" where t.subturma and t.turmaprincipal = turma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");
						if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
							sql.append("  and ht.anovigente = '").append(ano).append("'  and ht.semestrevigente = '").append(semestre).append("'");
						}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
							sql.append("  and ht.anovigente = '").append(ano).append("' ");
						}
						sql.append(" union all ");						
						sql.append(" select horarioturmadiaitem.disciplina from horarioturma ht   ");
						sql.append(" inner join turma as t on ht.turma = t.codigo ");
						sql.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo ");						
						sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = ht.codigo ");
						sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
						sql.append(" where turmaagrupada.turma = turma.codigo and horarioturmadiaitem.disciplina = disciplina.codigo ");						
						if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
							sql.append("  and ht.anovigente = '").append(ano).append("'  and ht.semestrevigente = '").append(semestre).append("'");
						}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
							sql.append("  and ht.anovigente = '").append(ano).append("' ");
						}
						
						sql.append(" limit 1) ");						
						if(!situacaoVaga.trim().isEmpty()){
							sql.append(getSqlVagaTurmaBase(periodicidade, ano, semestre, situacaoVaga, disciplinaVO, turmaVO, false));
						}						
						sql.append(" limit 1)) ");
					}else{
						sql.append(" and ( horarioturma.codigo  is not null " );
						if(!situacaoVaga.trim().isEmpty()){							
							sql.append(" and exists (select distinct horarioturmadiaitem.disciplina from horarioturmadia ");
							sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
							sql.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");							
							sql.append(" where horarioturmadia.horarioturma = horarioturma.codigo ");
							if(Uteis.isAtributoPreenchido(disciplinaVO)){
								sql.append(" and  horarioturmadiaitem.disciplina = ").append(disciplinaVO.getCodigo());
							}							
							sql.append(getSqlVagaTurmaBase(periodicidade, ano, semestre, situacaoVaga, disciplinaVO, turmaVO, false));
							sql.append(" ) ");
						}
						sql.append(" ) ");
					}
				}else if(situacaoProgramacaoAula.equals("aulaNaoRegistrada") ){
					sql.append(" and horarioturma.codigo is not null ");
					sql.append(" and exists (select horarioturmadiaitem.codigo from horarioturmadia ");
					sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
					sql.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
					if(Uteis.isAtributoPreenchido(disciplinaVO)){
						sql.append(" and  horarioturmadiaitem.disciplina = ").append(disciplinaVO.getCodigo());
					}else{
						sql.append(" and  horarioturmadiaitem.disciplina is not null  ");
					}
					sql.append(" where  horarioturmadia.horarioturma = horarioturma.codigo and not exists (select registroaula.codigo from registroaula  ");
					sql.append(" where  registroaula.turma = turma.codigo and registroaula.disciplina = horarioturmadiaitem.disciplina ");
					sql.append(" and  registroaula.data = horarioturmadiaitem.data and registroaula.nraula = horarioturmadiaitem.nraula limit 1) ");
					if(!situacaoVaga.trim().isEmpty()){						
						sql.append(getSqlVagaTurmaBase(periodicidade, ano, semestre, situacaoVaga, disciplinaVO, turmaVO, false));
					}
					sql.append(" limit 1) ");
				}
				sql.append(" ) ");
				if(!filtarDisciplinaComSubTurma.equals("apenas") && !filtrarDisciplinaComTurmaAgrupada.equals("apenas") 
						&& (!situacaoTurmaAgrupada.trim().isEmpty() || !situacaoProgramacaoAulaTurmaAgrupada.trim().isEmpty()
								|| !situacaoSubTurma.trim().isEmpty()  ||  !situacaoProgramacaoAulaSubTurma.trim().isEmpty() || !situacaoVagaSubTurma.trim().isEmpty() 
								|| !situacaoMatriculaSubTurma.trim().isEmpty())){
					sql.append(" or exists ( ").append(realizarGeracaoSelectGestaoTurmaDisciplina(periodicidade, ano, semestre, disciplinaVO, turmaVO,
					situacaoTurma, situacaoSubTurma, situacaoTurmaAgrupada, situacaoProgramacaoAula, situacaoProgramacaoAulaSubTurma,  situacaoProgramacaoAulaTurmaAgrupada, situacaoVaga, situacaoVagaSubTurma, situacaoMatricula, 
					situacaoMatriculaSubTurma, situacaoMatriculaTurmaAgrupada, filtarDisciplinaComSubTurma, filtrarDisciplinaComTurmaAgrupada, true));
					sql.append(" ) ");
				}
				sql.append(" ) ");
			}
			if(filtarDisciplinaComSubTurma.equals("apenas")){
					sql.append(" and exists ( ");
					sql.append(realizarGeracaoSelectDisciplinaSubTurma(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoSubTurma, situacaoProgramacaoAulaSubTurma, situacaoVagaSubTurma, situacaoMatriculaSubTurma, true, false));
					sql.append(" ) ");
				}
			if(filtrarDisciplinaComTurmaAgrupada.equals("apenas")){
					sql.append(" and exists ( ");
					sql.append(realizarGeracaoSelectDisciplinaTurmaAgrupada(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurmaAgrupada, situacaoProgramacaoAulaTurmaAgrupada, situacaoMatriculaTurmaAgrupada, true, false, situacaoVaga));
					sql.append(" ) ");
			}						
						
			
		return sql;
	}
	
	private StringBuilder getSqlVagaTurmaBase(PeriodicidadeEnum periodicidade, String ano, String semestre, String situacaoVaga,  DisciplinaVO disciplinaVO, TurmaVO turmaVO, boolean usarDisciplinaIN){
		StringBuilder sql = new StringBuilder("");
		if(usarDisciplinaIN){
			sql.append(" and exists (  ");
			if(situacaoVaga.equals("possui")){
				sql.append(" (select vagaturmadisciplina.disciplina from vagaturmadisciplina inner join disciplina on disciplina.codigo = vagaturmadisciplina.disciplina where  vagaturmadisciplina.vagaturma = vagaturma.codigo ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula   >  0 limit 1) ");
			}else if(situacaoVaga.equals("naoPossui")){
				sql.append(" (select vagaturmadisciplina.disciplina from vagaturmadisciplina inner join disciplina on disciplina.codigo = vagaturmadisciplina.disciplina where  vagaturmadisciplina.vagaturma = vagaturma.codigo ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula  = 0 limit 1) ");
				sql.append(" union all ( select codigo from disciplina where codigo in ( ");
				sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false));
				sql.append(" ) and not exists (select vagaturmadisciplina.codigo from vagaturmadisciplina where vagaturmadisciplina.disciplina = disciplina.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo ) limit 1) ");			
			}else if(situacaoVaga.equals("vagaPreenchida")){
				sql.append(" ( select codigo from disciplina where codigo in ( ");
				sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false));
				sql.append(" ) and exists (select vagaturmadisciplina.codigo from vagaturmadisciplina where vagaturmadisciplina.disciplina = disciplina.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula <= ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" limit 1)"); 
				sql.append(" )"); 				
			}else if(situacaoVaga.equals("vagaAcima")){				
				sql.append(" ( select codigo from disciplina where codigo in ( ");
				sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false));
				sql.append(" ) and exists (select vagaturmadisciplina.codigo from vagaturmadisciplina where vagaturmadisciplina.disciplina = disciplina.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula < ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" limit 1)"); 
				sql.append(" )");
			}else if(situacaoVaga.equals("vagaDisponivel")){				
				sql.append(" ( select codigo from disciplina where codigo in ( ");
				sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", false));
				sql.append(" ) and exists (select vagaturmadisciplina.codigo from vagaturmadisciplina where vagaturmadisciplina.disciplina = disciplina.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula > ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" limit 1)"); 
				sql.append(" )");
			}
			sql.append(" ) ");
		}else{
			sql.append(" and exists ( ");
			if(situacaoVaga.equals("possui")){
				sql.append(" ( ");
				sql.append(" select vagaturmadisciplina.disciplina from vagaturmadisciplina where disciplina.codigo = vagaturmadisciplina.disciplina and  vagaturmadisciplina.vagaturma = vagaturma.codigo ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula   >  0 limit 1) ");
			}else if(situacaoVaga.equals("naoPossui")){
				sql.append(" ( ");
				sql.append(" select vagaturmadisciplina.disciplina from vagaturmadisciplina where disciplina.codigo = vagaturmadisciplina.disciplina and  vagaturmadisciplina.vagaturma = vagaturma.codigo ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula  = 0 limit 1) ");
				sql.append(" union all ( select codigo from disciplina dis where dis.codigo = disciplina.codigo ");				
				sql.append(" and not exists (select vagaturmadisciplina.codigo from vagaturmadisciplina where vagaturmadisciplina.disciplina = dis.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo ) limit 1) ");
			}else if(situacaoVaga.equals("vagaPreenchida")){				
				sql.append(" ( select codigo from disciplina dis where dis.codigo = disciplina.codigo ");				
				sql.append(" and exists (select vagaturmadisciplina.disciplina from vagaturmadisciplina where vagaturmadisciplina.disciplina = dis.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula <= ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" )"); 
				sql.append(" )"); 				
			}else if(situacaoVaga.equals("vagaAcima")){								
				sql.append(" ( select codigo from disciplina dis where dis.codigo = disciplina.codigo ");				
				sql.append(" and exists (select vagaturmadisciplina.disciplina from vagaturmadisciplina where vagaturmadisciplina.disciplina = dis.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula < ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" )"); 
				sql.append(" )");
			}else if(situacaoVaga.equals("vagaDisponivel")){								
				sql.append(" ( select codigo from disciplina dis where dis.codigo = disciplina.codigo ");							
				sql.append(" and exists (select vagaturmadisciplina.disciplina from vagaturmadisciplina where vagaturmadisciplina.disciplina = dis.codigo and vagaturmadisciplina.vagaturma = vagaturma.codigo  ");
				sql.append(" and vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
				sql.append(" and vagaturmadisciplina.nrvagasmatricula > ");
				sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", "", "", "", "", "", "", situacaoVaga));
				sql.append(" )"); 
				sql.append(" )");
			}
			sql.append(" ) ");
		}
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectDisciplinaTurma(DisciplinaVO disciplinaVO, TurmaVO turmaVO, String keyTurmaBase, boolean compararDisciplinaEquivalente) {
		StringBuilder sql  = new StringBuilder("");		
		sql.append("(select d.codigo");
		sql.append("	from turmadisciplina ");
		sql.append("        inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sql.append("        inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
		sql.append("        where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo and gradedisciplina.disciplinacomposta = false ");
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and d.codigo = ").append(disciplinaVO.getCodigo());
		}
		sql.append("	union ");
		sql.append("	select d.codigo");
		sql.append("	from turmadisciplina ");
		sql.append("        inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina");
		sql.append("        inner join disciplina as d  on d.codigo = gradecurriculargrupooptativadisciplina.disciplina");
		sql.append("        where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and d.codigo = ").append(disciplinaVO.getCodigo());
		}
		sql.append("	union ");
		sql.append("	select d.codigo");
		sql.append("	from turmadisciplina ");
		sql.append("        inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sql.append("        inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
		sql.append("        inner join disciplina as d on d.codigo = gradedisciplinacomposta.disciplina");
		sql.append("        where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and d.codigo  = ").append(disciplinaVO.getCodigo());
		}
		sql.append("	and (not exists (select codigo from turmadisciplinacomposta  where turmadisciplina.codigo =  turmadisciplinacomposta.turmadisciplina) or ");
		sql.append("	exists (select codigo from turmadisciplinacomposta  where turmadisciplina.codigo =  turmadisciplinacomposta.turmadisciplina and turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo))");
		sql.append("	union ");
		sql.append("	select d.codigo");
		sql.append("	from turmadisciplina ");
		sql.append("        inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina");
		sql.append("        inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina");
		sql.append("        inner join disciplina as d on d.codigo = gradedisciplinacomposta.disciplina");
		sql.append("        where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and d.codigo = ").append(disciplinaVO.getCodigo());
		}
		sql.append("	and (not exists (select codigo from turmadisciplinacomposta  where turmadisciplina.codigo =  turmadisciplinacomposta.turmadisciplina) or ");
		sql.append("	exists (select codigo from turmadisciplinacomposta  where turmadisciplina.codigo =  turmadisciplinacomposta.turmadisciplina and turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo)) ");
		sql.append("	union  ");
		sql.append("	select d.codigo");
		sql.append("	from turmadisciplina ");
		sql.append("    inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
		sql.append("    where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
		sql.append("    and gradecurriculargrupooptativadisciplina is null and gradedisciplina is null ");
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and d.codigo = ").append(disciplinaVO.getCodigo());
		}
		if(compararDisciplinaEquivalente){
			sql.append("	union  ");
			sql.append("	select disciplinaequivalente.disciplina ");
			sql.append("	from turmadisciplina ");
			sql.append("    inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
			sql.append("    inner join disciplinaequivalente on d.codigo = disciplinaequivalente.equivalente ");
			sql.append("    where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
			if(keyTurmaBase.equals("turma")) {
				sql.append("    and gradecurriculargrupooptativadisciplina is null and gradedisciplina is null ");
			}
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append("  and disciplinaequivalente.disciplina = ").append(disciplinaVO.getCodigo());
			}
			sql.append("	union  ");
			sql.append("	select disciplinaequivalente.equivalente ");
			sql.append("	from turmadisciplina ");
			sql.append("    inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
			sql.append("    inner join disciplinaequivalente on d.codigo = disciplinaequivalente.disciplina ");
			sql.append("    where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
			if(keyTurmaBase.equals("turma")) {
				sql.append("    and gradecurriculargrupooptativadisciplina is null and gradedisciplina is null ");
			}
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append("  and disciplinaequivalente.equivalente = ").append(disciplinaVO.getCodigo());
			}
		}else if(Uteis.isAtributoPreenchido(turmaVO) && turmaVO.getTurmaAgrupada()){
			sql.append("	union  ");
			if(keyTurmaBase.equals("turma")) {
				sql.append("	select turmadisciplina.disciplina ");
			}else {
				sql.append("	select disciplinaequivalente.disciplina ");
			}
			sql.append("	from turmadisciplina ");
			sql.append("    inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
			sql.append("    inner join disciplinaequivalente on d.codigo = disciplinaequivalente.equivalente ");
			sql.append("    inner join turmadisciplina as tdta on tdta.disciplina = disciplinaequivalente.disciplina ");
			sql.append("    where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
			sql.append("    and tdta.turma = ").append(turmaVO.getCodigo());
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append("  and disciplinaequivalente.disciplina = ").append(disciplinaVO.getCodigo());				
			}
			sql.append("	union  ");
			if(keyTurmaBase.equals("turma")) {
				sql.append("	select turmadisciplina.disciplina ");
			}else {
				sql.append("	select disciplinaequivalente.equivalente ");
			}
			sql.append("	from turmadisciplina ");
			sql.append("    inner join disciplina as d on d.codigo = turmadisciplina.disciplina");
			sql.append("    inner join disciplinaequivalente on d.codigo = disciplinaequivalente.disciplina ");
			sql.append("    inner join turmadisciplina as tdta on tdta.disciplina = disciplinaequivalente.equivalente ");
			sql.append("    where turmadisciplina.turma = ").append(keyTurmaBase).append(".codigo ");
			sql.append("    and tdta.turma = ").append(turmaVO.getCodigo());			
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append("  and disciplinaequivalente.equivalente = ").append(disciplinaVO.getCodigo());
			}
		}
		sql.append(" )	");
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectDisciplinaSubTurma(PeriodicidadeEnum periodicidade, String ano, String semestre,  DisciplinaVO disciplinaVO, TurmaVO turmaVO, String situacaoTurma,
			String situacaoProgramacaoAula, String situacaoVaga, String situacaoMatricula, boolean usarLimit1, boolean considerarInDisciplina){
		StringBuilder sql  = new StringBuilder("");	
		if(usarLimit1){
			sql.append(" (( select subturma.codigo ");
		}else{
			sql.append(" select subturma.codigo as turma_codigo, subturma.identificadorturma as turma_identificadorturma, subturma.situacao as turma_situacao, subturma.tiposubturma as turma_tiposubturma, subturma.subturma as turma_subturma, subturma.turmaagrupada as turma_turmaagrupada, ");
			sql.append(" turmaprincipal.codigo as turmaprincipal_codigo, turmaprincipal.identificadorturma as turmaprincipal_identificadorturma,");
			sql.append(" vagaturmadisciplina.codigo as vagaturmadisciplina_codigo,");
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrvagasmatricula else subturma.nrvagas end as nrvagas,");
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrmaximomatricula else subturma.nrmaximomatricula   end as nrmaximomatricula,");
			sql.append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "subturma", true, disciplinaVO)).append(", ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "apenas", "", "", "", "", "", "", "", "")).append(" as totalMatriculados, ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'PR'", true, false, disciplinaVO,  turmaVO, "apenas", "", "", "", "", "", "", "", "")).append(" as totalPreMatriculados ");
		}
		sql.append(" from turma as subturma ");
		sql.append(" inner join turma as turmaprincipal  on turmaprincipal.codigo = subturma.turmaprincipal "); 
		sql.append(" left join vagaturma on vagaturma.turma = subturma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' and vagaturma.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' ");
		}
		if(considerarInDisciplina){
			sql.append(" left join vagaturmadisciplina  on  vagaturmadisciplina.vagaturma = vagaturma.codigo and (vagaturmadisciplina.disciplina = disciplina.codigo "); 
			sql.append("  or (subturma.turmaagrupada "); 
			sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina = disciplina.codigo and de.equivalente = vagaturmadisciplina.disciplina)) "); 
			sql.append("  or (subturma.turmaagrupada ");
			sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente = disciplina.codigo and de.disciplina = vagaturmadisciplina.disciplina)) "); 
			sql.append(" ) ");
			
		}else{
			sql.append(" and exists (select vagaturmadisciplina.codigo from vagaturmadisciplina  where vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina is not null and vagaturmadisciplina.nrvagasmatricula > 0 limit 1) ");			
		}
		sql.append(realizarGeracaoWhereSubTurma(periodicidade, ano, semestre, disciplinaVO,turmaVO, situacaoTurma, situacaoProgramacaoAula, situacaoVaga, situacaoMatricula,considerarInDisciplina));		
		sql.append(" and subturma.turmaprincipal = turma.codigo ");
		if(usarLimit1){
			sql.append(" limit 1) ");
		}
		sql.append(" union ");
		if(usarLimit1){
			sql.append(" ( select subturma.codigo ");
		}else{
			sql.append(" select subturma.codigo as turma_codigo, subturma.identificadorturma as turma_identificadorturma, subturma.situacao as turma_situacao, subturma.tiposubturma as turma_tiposubturma, subturma.subturma as turma_subturma, subturma.turmaagrupada as turma_turmaagrupada, ");
			sql.append(" turmabase.codigo as turmaprincipal_codigo, turmabase.identificadorturma as turmaprincipal_identificadorturma,");
			sql.append(" vagaturmadisciplina.codigo as vagaturmadisciplina_codigo,");
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrvagasmatricula else subturma.nrvagas end as nrvagas,");
			sql.append(" case when vagaturmadisciplina.codigo is not null then vagaturmadisciplina.nrmaximomatricula else subturma.nrmaximomatricula   end as nrmaximomatricula,");
			sql.append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "subturma", true, disciplinaVO)).append(", ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "apenas", "", "", "", "", "", "", "", "")).append(" as totalMatriculados, ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'PR'", true, false, disciplinaVO, turmaVO, "apenas", "", "", "", "", "", "", "", "")).append(" as totalPreMatriculados ");
		}
		sql.append(" from turma as subturma ");
		sql.append(" inner join turma as turmaprincipal  on turmaprincipal.codigo = subturma.turmaprincipal ");
		sql.append(" inner join turmaagrupada as ta on subturma.codigo = ta.turmaorigem ");
		sql.append(" inner join turma as turmabase on turmabase.codigo = ta.turma  ");
		sql.append(" left join vagaturma on vagaturma.turma = subturma.codigo ");
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' and vagaturma.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and vagaturma.ano = '").append(ano).append("' ");
		}	
		if(considerarInDisciplina){
			sql.append(" left join vagaturmadisciplina  on  vagaturmadisciplina.vagaturma = vagaturma.codigo and (vagaturmadisciplina.disciplina = disciplina.codigo");
			sql.append("  or (subturma.turmaagrupada ");
			sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina = disciplina.codigo and de.equivalente = vagaturmadisciplina.disciplina)) ");
			sql.append("  or (subturma.turmaagrupada ");
			sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente = disciplina.codigo and de.disciplina = vagaturmadisciplina.disciplina)) ");
			sql.append(" ) ");
			 
		}else{
			sql.append(" and exists (select vagaturmadisciplina.codigo from vagaturmadisciplina  where vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina is not null  and vagaturmadisciplina.nrvagasmatricula > 0  limit 1) ");
		}
		sql.append(realizarGeracaoWhereSubTurma(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoTurma, situacaoProgramacaoAula, situacaoVaga, situacaoMatricula,considerarInDisciplina));
		sql.append(" and turmabase.codigo = turma.codigo ");
		if(usarLimit1){
			sql.append(" limit 1)) ");
		}else {
			sql.append(" order by turma_identificadorturma ");
		}
		return sql;
	}
	
	private StringBuilder realizarGeracaoWhereSubTurma(PeriodicidadeEnum periodicidade, String ano, String semestre, DisciplinaVO disciplinaVO,  TurmaVO turmaVO, String situacaoTurma, String situacaoProgramacaoAula, String situacaoVaga, String situacaoMatricula, boolean considerarInDisciplina){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" where subturma.subturma and subturma.tiposubturma IN ('PRATICA', 'TEORICA', 'GERAL') ");
		if(!situacaoTurma.trim().isEmpty()){
			sql.append(" and subturma.situacao = '").append(situacaoTurma).append("' ");
		}
		if(considerarInDisciplina){
			sql.append(" and disciplina.codigo in ");		
			sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "subturma", true));
		}
		if(situacaoProgramacaoAula.equals("possui")){
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "subturma", considerarInDisciplina, disciplinaVO)).append(") is not null ");
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turma", considerarInDisciplina, disciplinaVO)).append(") is null ");
		}else if(situacaoProgramacaoAula.equals("naoPossui")){
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "subturma", considerarInDisciplina, disciplinaVO)).append(") is null ");
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turma", considerarInDisciplina, disciplinaVO)).append(") is null ");
		}else if(situacaoProgramacaoAula.equals("aulaNaoRegistrada")){			
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turma", considerarInDisciplina, disciplinaVO)).append(") is null ");
			sql.append(" and exists (select horarioturmadiaitem.codigo from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append(" and  (horarioturmadiaitem.disciplina = ").append(disciplinaVO.getCodigo());
				sql.append("  or (subturma.turmaagrupada ");
				sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina = ").append(disciplinaVO.getCodigo()).append(" and de.equivalente = horarioturmadiaitem.disciplina)) ");
				sql.append("  or (subturma.turmaagrupada ");
				sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente = ").append(disciplinaVO.getCodigo()).append(" and de.disciplina = horarioturmadiaitem.disciplina)) ");
				sql.append(" ) ");
			}else if(considerarInDisciplina){
				sql.append("  and horarioturmadiaitem.disciplina = disciplina.codigo");
			}else{
				sql.append(" and  horarioturmadiaitem.disciplina is not null  ");
			}
			sql.append(" where  horarioturma.turma = subturma.codigo ");
			if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("'");
			}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
			}	
			sql.append(" and not exists (select registroaula.codigo from registroaula  ");
			sql.append(" where  registroaula.turma = subturma.codigo and registroaula.disciplina = horarioturmadiaitem.disciplina ");
			sql.append(" and  registroaula.data = horarioturmadiaitem.data and registroaula.nraula = horarioturmadiaitem.nraula limit 1) limit 1) ");
		}	
		
		if(!situacaoVaga.trim().isEmpty()){
			if(considerarInDisciplina){
				if(situacaoVaga.equals("possui") || situacaoVaga.equals("naoPossui")){
					sql.append(" and (vagaturmadisciplina.codigo ").append(situacaoVaga.equals("possui") ? " is not null and vagaturmadisciplina.nrvagasmatricula > 0 ": " is null or vagaturmadisciplina.nrvagasmatricula = 0  ").append(" )");
				}else if(situacaoVaga.equals("vagaDisponivel")){
					sql.append(" and ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula > (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))"); 
				}else if(situacaoVaga.equals("vagaPreenchida")){
					sql.append(" and ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula <= (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))");
				}else if(situacaoVaga.equals("vagaAcima")){
					sql.append(" and ( vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrvagasmatricula > 0 and  ");
					sql.append(" vagaturmadisciplina.nrvagasmatricula < (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))");
				}
			}else{
				if(situacaoVaga.equals("possui") || situacaoVaga.equals("naoPossui")){
					sql.append(" and vagaturma.codigo ").append(situacaoVaga.equals("possui") ? " is not null ": " is null ");
				}else if(situacaoVaga.equals("vagaDisponivel")){
					sql.append(" and exists ( select vagaturmadisciplina.codigo from vagaturmadisciplina inner join disciplina on disciplina.codigo = vagaturmadisciplina.disciplina  where vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina is not null and vagaturmadisciplina.nrvagasmatricula > 0  ");
					sql.append(" and vagaturmadisciplina.nrvagasmatricula > (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))"); 
				}else if(situacaoVaga.equals("vagaPreenchida")){
					sql.append(" and exists ( select vagaturmadisciplina.codigo from vagaturmadisciplina inner join disciplina on disciplina.codigo = vagaturmadisciplina.disciplina  where vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
					sql.append(" and vagaturmadisciplina.nrvagasmatricula <= (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))");
				}else if(situacaoVaga.equals("vagaAcima")){
					sql.append(" and exists ( select vagaturmadisciplina.codigo from vagaturmadisciplina inner join disciplina on disciplina.codigo = vagaturmadisciplina.disciplina  where vagaturmadisciplina.vagaturma = vagaturma.codigo and vagaturmadisciplina.disciplina is not null and vagaturmadisciplina.nrvagasmatricula > 0   ");
					sql.append(" and vagaturmadisciplina.nrvagasmatricula < (");
					sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'FO'", true, false, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, ""));
					sql.append(" ))");
				}
			}
		}
		if(situacaoMatricula.equals("possui") || situacaoMatricula.equals("naoPossui")){
			sql.append(" and ").append(situacaoMatricula.equals("naoPossui")?" not  " : "").append(" exists (");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'PR', 'FO'", considerarInDisciplina,  true, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, "")); 
			sql.append(" )");				
		}	else if(situacaoMatricula.equals("possuiMatriculaSemDistribuicao")) {
			sql.append(" and exists (");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "subturma", "'AT', 'FI', 'PR', 'FO'", considerarInDisciplina, true, disciplinaVO, turmaVO, "", "", situacaoTurma, "", situacaoProgramacaoAula, "", situacaoVaga, situacaoMatricula, "")); 
			sql.append(" )");			
		}
	
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectDisciplinaTurmaAgrupada(PeriodicidadeEnum periodicidade, String ano, String semestre,  DisciplinaVO disciplinaVO, TurmaVO turmaVO, String situacaoTurma,
			String situacaoProgramacaoAula, String situacaoMatricula, boolean usarLimit, boolean considerarInDisciplina, String situacaoVagaTurmaBase){
		StringBuilder sql  = new StringBuilder("");	
		if(usarLimit){
			sql.append(" ( select turmaagrupada.codigo ");
		}else{			
			sql.append(" select distinct turmaagrupada.codigo as turma_codigo , turmaagrupada.identificadorturma as turma_identificadorturma, turmaagrupada.situacao as turma_situacao, turmaagrupada.tiposubturma as turma_tiposubturma, turmaagrupada.subturma as turma_subturma, turmaagrupada.turmaagrupada as turma_turmaagrupada, ");
			sql.append(" 0 as vagaturmadisciplina_codigo,");
			sql.append(" 0 as nrvagas,");
			sql.append(" 0 as nrmaximomatricula,");
			sql.append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmaagrupada", considerarInDisciplina, disciplinaVO)).append(", ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turmaagrupada", "'AT', 'FI', 'FO'", considerarInDisciplina, false, disciplinaVO, turmaVO, "", "apenas", "", "", "", "", "", "", "")).append(" as totalMatriculados, ");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turmaagrupada", "'PR'", considerarInDisciplina, false, disciplinaVO, turmaVO, "", "apenas", "", "", "", "", "", "", "")).append(" as totalPreMatriculados ");
		}
		sql.append(" from turma as turmaagrupada ");
		sql.append(" inner join turmaagrupada as ta on turmaagrupada.codigo = ta.turmaorigem ");
		sql.append(" inner join turma as turmabase on ta.turma = turmabase.codigo ");
		if(!considerarInDisciplina){
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turmaagrupada.codigo ");
			sql.append(" and turmadisciplina.disciplina in ").append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turma", true));
		}
		sql.append(" where turmaagrupada.subturma = false and turmaagrupada.turmaagrupada ");
		sql.append(" and ((turmabase.subturma and turmabase.turmaprincipal = turma.codigo) or (turmabase.subturma = false and ta.turma = turma.codigo)) ");
		if(!situacaoTurma.trim().isEmpty()){
			sql.append(" and turmaagrupada.situacao = '").append(situacaoTurma).append("' ");
		}
		if(situacaoProgramacaoAula.equals("possui")){
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmaagrupada", considerarInDisciplina, disciplinaVO)).append(") is not null ");
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmabase", considerarInDisciplina, disciplinaVO)).append(") is null ");
		}else if(situacaoProgramacaoAula.equals("naoPossui")){
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmaagrupada", considerarInDisciplina, disciplinaVO)).append(") is null ");
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmabase", considerarInDisciplina, disciplinaVO)).append(") is null ");
		}else if(situacaoProgramacaoAula.equals("aulaNaoRegistrada")){
			sql.append(" and (select * from ").append(realizarGeracaoSelectHorarioTurma(periodicidade, ano, semestre, "turmabase", considerarInDisciplina, disciplinaVO)).append(") is null ");
			sql.append(" and exists (select horarioturmadiaitem.codigo from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			if(Uteis.isAtributoPreenchido(disciplinaVO)){
				sql.append(" and  horarioturmadiaitem.disciplina = ").append(disciplinaVO.getCodigo());
			}else if(considerarInDisciplina){
				sql.append("  and horarioturmadiaitem.disciplina = disciplina.codigo");
			}else{
				sql.append(" and  horarioturmadiaitem.disciplina is not null  ");
			}
			sql.append(" where  horarioturma.turma = turmaagrupada.codigo ");
			if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("'");
			}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
				sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
			}	
			sql.append(" and not exists (select registroaula.codigo from registroaula  ");
			sql.append(" where  registroaula.turma = turmaagrupada.codigo and registroaula.disciplina = horarioturmadiaitem.disciplina ");
			sql.append(" and  registroaula.data = horarioturmadiaitem.data and registroaula.nraula = horarioturmadiaitem.nraula limit 1) limit 1) ");
		}
		if(situacaoMatricula.equals("possui") || situacaoMatricula.equals("naoPossui")){
			sql.append(" and ").append(situacaoMatricula.equals("naoPossui")?" not  " : "").append(" exists (");
			sql.append(realizarGeracaoSelectAlunoMatriculadoDisciplina(periodicidade, ano, semestre, "turmaagrupada", "'AT', 'FI', 'PR', 'FO'", considerarInDisciplina, true, disciplinaVO, turmaVO, "", "apenas", "", situacaoTurma, "", situacaoProgramacaoAula, "", "", situacaoVagaTurmaBase)); 
			sql.append(" )");				
		}
		if(considerarInDisciplina){
			sql.append(" and disciplina.codigo in ");		
			sql.append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO, "turmaagrupada", true));
		}
		if(!situacaoVagaTurmaBase.isEmpty()){
			sql.append(getSqlVagaTurmaBase(periodicidade, ano, semestre, situacaoVagaTurmaBase, disciplinaVO, turmaVO, false));
		}
		if(usarLimit){
			sql.append(" limit 1 ) ");
		}else {
			sql.append(" order by turma_identificadorturma ");
		}
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectHorarioTurma(PeriodicidadeEnum periodicidade, String ano, String semestre, String keyTurmaBase, Boolean considerarDisciplina, DisciplinaVO disciplinaVO){
		StringBuilder sql  = new StringBuilder("");	
		sql.append(" (select horarioturma.codigo from horarioturma ");
		sql.append("  inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");		
		sql.append("  where horarioturma.turma = ").append(keyTurmaBase).append(".codigo ");
		
		if(Uteis.isAtributoPreenchido(disciplinaVO)){
			sql.append("  and ( horarioturmadiaitem.disciplina = ").append(disciplinaVO.getCodigo());
			sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
			sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina = ").append(disciplinaVO.getCodigo()).append(" and de.equivalente = horarioturmadiaitem.disciplina)) ");
			sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
			sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente = ").append(disciplinaVO.getCodigo()).append(" and de.disciplina = horarioturmadiaitem.disciplina)) ");
			sql.append(" ) ");
		}else if(considerarDisciplina){
			sql.append("  and ( horarioturmadiaitem.disciplina = disciplina.codigo");
			if(!keyTurmaBase.equals("turma")) {
			sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
			sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina =  disciplina.codigo and de.equivalente = horarioturmadiaitem.disciplina)) ");
			sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
			sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente =  disciplina.codigo and de.disciplina = horarioturmadiaitem.disciplina)) ");
			}
			sql.append(" ) ");
		}else{
			sql.append("  and horarioturmadiaitem.disciplina is not null ");
		}
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and horarioturma.anovigente = '").append(ano).append("' ");
		}		
		sql.append(" limit 1) as horarioturma_codigo ");
		return sql;
	}
	
	private StringBuilder realizarGeracaoSelectAlunoMatriculadoDisciplina(PeriodicidadeEnum periodicidade, String ano, String semestre, String keyTurmaBase, String situacoes, boolean considerarDisciplina, boolean usaLimit1, DisciplinaVO disciplinaVO, TurmaVO turmaVO, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, String situacaoSubTurma, String situacaoTurmaAgrupada, String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVagaSubTurma, String situacaoMatriculaSubTurma, String situacaoVagaTurmaBase) {
		StringBuilder sql  = new StringBuilder("");	
		sql.append(" (select ");
		if(usaLimit1){
			sql.append("  matriculaperiodoturmadisciplina.matricula ");
		}else{
			if(!keyTurmaBase.equals("turma") && !keyTurmaBase.equals("subturma")) {
				sql.append(" case when ").append(keyTurmaBase).append(".turmaagrupada then ").append(" sum(case when turma.codigo = matriculaperiodoturmadisciplina.turma then 1 else 0 end) else count(distinct matriculaperiodoturmadisciplina.matricula) end ");
			}else {
				sql.append("  count(distinct matriculaperiodoturmadisciplina.matricula) ");
			}
		}
		sql.append(" from  matriculaperiodoturmadisciplina ");
		if(!considerarDisciplina){
			sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		}
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" and situacaomatriculaperiodo in ("+situacoes+")  ");
		sql.append(" inner join matricula on matricula.matricula  = matriculaperiodo.matricula");
		sql.append(" inner join historico on historico.matricula  = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" where ");	
		if(keyTurmaBase.equalsIgnoreCase("turmaagrupada")){
			sql.append(" exists (select ta2.turma from turmaagrupada as ta2 where ta2.turmaorigem = turmaagrupada.codigo and matriculaperiodoturmadisciplina.turma = ta2.turma  ) and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		}else if(keyTurmaBase.equals("subturma")) {
			if(situacaoMatriculaSubTurma.equals("possuiMatriculaSemDistribuicao")){
				sql.append(" (((matriculaperiodoturmadisciplina.turmapratica is null and ").append(keyTurmaBase).append(".tiposubturma = 'PRATICA' and ").append(keyTurmaBase).append(".subturma) ");
				sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is null and ").append(keyTurmaBase).append(".tiposubturma = 'TEORICA'  and ").append(keyTurmaBase).append(".subturma)) ");				
			}else {
				sql.append(" (((matriculaperiodoturmadisciplina.turmapratica = ").append(keyTurmaBase).append(".codigo and ").append(keyTurmaBase).append(".tiposubturma = 'PRATICA' and ").append(keyTurmaBase).append(".subturma) ");
				sql.append(" or (matriculaperiodoturmadisciplina.turmateorica = ").append(keyTurmaBase).append(".codigo and ").append(keyTurmaBase).append(".tiposubturma = 'TEORICA'  and ").append(keyTurmaBase).append(".subturma) ");
				sql.append(" ) ");
			}
			sql.append(" and (matriculaperiodoturmadisciplina.turma =  turma.codigo "); 
			sql.append(" or (").append(keyTurmaBase).append(".turmaagrupada =  false and matriculaperiodoturmadisciplina.turma =  ").append(keyTurmaBase).append(".turmaprincipal) ");
			sql.append(" or (").append(keyTurmaBase).append(".turmaagrupada and exists ( select ta.codigo from turmaagrupada as ta where matriculaperiodoturmadisciplina.turma = ta.turma and ta.turmaorigem = ").append(keyTurmaBase).append(".codigo)) ");
			sql.append(" )) ");				
		}else if(keyTurmaBase.equals("turma")) {
			sql.append("  (matriculaperiodoturmadisciplina.turma = ").append(keyTurmaBase).append(".codigo) ");										
		}
		if(considerarDisciplina){
			sql.append(" and ( matriculaperiodoturmadisciplina.disciplina =  disciplina.codigo ");
			if(!keyTurmaBase.equals("turma")) {
				sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
				sql.append("  and exists (select de.disciplina from disciplinaequivalente as de where de.disciplina = disciplina.codigo and de.equivalente = matriculaperiodoturmadisciplina.disciplina)) ");
				sql.append("  or (").append(keyTurmaBase).append(".turmaagrupada ");
				sql.append("  and exists (select de.equivalente from disciplinaequivalente as de where de.equivalente = disciplina.codigo and de.disciplina = matriculaperiodoturmadisciplina.disciplina)) ");
			}
			sql.append(" ) ");
		}else{
			sql.append(" and matriculaperiodoturmadisciplina.disciplina in ").append(realizarGeracaoSelectDisciplinaTurma(disciplinaVO, turmaVO,  keyTurmaBase, !keyTurmaBase.equals("turma")));
			if(keyTurmaBase.equals("turma") && usaLimit1){
				if(filtarDisciplinaComSubTurma.equals("apenas")){
					sql.append(" and exists ( ");
					sql.append(realizarGeracaoSelectDisciplinaSubTurma(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoSubTurma, situacaoProgramacaoAulaSubTurma, situacaoVagaSubTurma, "", true, true));					
					sql.append(" ) ");
				}else if(filtrarDisciplinaComTurmaAgrupada.equals("apenas")){
					sql.append(" and exists ( ");
					sql.append(realizarGeracaoSelectDisciplinaTurmaAgrupada(periodicidade, ano, semestre, disciplinaVO, turmaVO, situacaoSubTurma, situacaoProgramacaoAulaSubTurma, "", true, true, situacaoVagaTurmaBase));					
					sql.append(" ) ");					
				}			
			}
		}
			

		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)){
			sql.append("  and matriculaperiodo.ano = '").append(ano).append("'  and matriculaperiodo.semestre = '").append(semestre).append("'");
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)){
			sql.append("  and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if(usaLimit1){
			sql.append(" limit 1 ");
		}
		sql.append(" ) ");
		return sql;
	}

	
	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
	} 
	
	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}
	
	public static String getIdEntidade() {
		return ("GestaoTurmaRel");
	}
	
	public void realizarConsultaAulaProgramadaERegistrada(List<GestaoTurmaRelVO> gestaoTurmaRelVOs)  throws Exception{
		for(GestaoTurmaRelVO gestaoTurmaRelVO: gestaoTurmaRelVOs){
			for(GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO: gestaoTurmaRelVO.getGestaoTurmaDisciplinaRelVOs()){
				realizarConsultaAulaProgramadaERegistradaPorGestaoTurmaDisciplina(gestaoTurmaRelVO, gestaoTurmaDisciplinaRelVO);
			}
		}
	}	
	
	private void realizarConsultaAulaProgramadaERegistradaPorGestaoTurmaDisciplina(GestaoTurmaRelVO gestaoTurmaRelVO, GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO)  throws Exception{
		SqlRowSet rs = consultarAulaProgramadaERegistradaPorTurmaDisciplina(gestaoTurmaRelVO.getTurmaVO().getCodigo(), gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo(), gestaoTurmaDisciplinaRelVO.getHorarioTurmaVO().getCodigo());
		montarDadosAulaProgramadaERegistradaPorGestaoTurma(rs, gestaoTurmaDisciplinaRelVO);
		for(GestaoTurmaRelVO gestaoTurmaRelVO2:gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVOs()){
			Integer disciplinaUsar = gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo();
			if(gestaoTurmaRelVO2.getTurmaVO().getTurmaAgrupada()) {				
				StringBuilder sql = new StringBuilder("");
				sql.append("select turmadisciplina.disciplina from turmadisciplina where turma = ").append(gestaoTurmaRelVO2.getTurmaVO().getCodigo());
				sql.append(" and turmadisciplina.disciplina in ( select ").append(disciplinaUsar);
				sql.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(disciplinaUsar);
				sql.append(" union select equivalente from disciplinaequivalente where disciplina = ").append(disciplinaUsar);
				sql.append(" ) limit 1 ");
				SqlRowSet rsDis = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				if(rsDis.next()) {
					disciplinaUsar = rsDis.getInt("disciplina");
				}
			}
			rs = consultarAulaProgramadaERegistradaPorTurmaDisciplina(gestaoTurmaRelVO2.getTurmaVO().getCodigo(), disciplinaUsar , gestaoTurmaRelVO2.getHorarioTurmaVO().getCodigo());
			montarDadosAulaProgramadaERegistradaPorGestaoTurma(rs, gestaoTurmaRelVO2);
		}
	}
	
    private void montarDadosAulaProgramadaERegistradaPorGestaoTurma(SqlRowSet rs, GestaoTurmaRelVO gestaoTurmaRelVO)  throws Exception{
    	if(rs != null && rs.next()){
    		gestaoTurmaRelVO.setCargaHoraria(rs.getInt("cargahoraria"));
    		gestaoTurmaRelVO.setCargaHorariaPratica(rs.getInt("cargahorariapratica"));
    		gestaoTurmaRelVO.setCargaHorariaTeorica(rs.getInt("cargahorariateorica"));
    		gestaoTurmaRelVO.setHoraAula(rs.getInt("horaAula"));
    		gestaoTurmaRelVO.setQtdeAulaRegistrada(rs.getInt("qtdeAulaRegistrada"));
    		gestaoTurmaRelVO.setHoraAulaRegistrada(Uteis.converterMinutosEmHorasStr(rs.getDouble("horaAulaRegistrada")));
    		gestaoTurmaRelVO.setQtdeAulaProgramada(rs.getInt("qtdeAulaProgramada"));
    		gestaoTurmaRelVO.setHoraProgramada(Uteis.converterMinutosEmHorasStr(rs.getDouble("horaAulaProgramada")));
    		gestaoTurmaRelVO.setProfessor(rs.getString("professores"));
    	}
    }
        
	public SqlRowSet consultarAulaProgramadaERegistradaPorTurmaDisciplina(Integer turma, Integer disciplina, Integer programacaoAula) throws Exception{
		if(Uteis.isAtributoPreenchido(programacaoAula)){			
			StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append(" select disciplina.codigo, disciplina.nome, disciplina.cargahoraria, disciplina.cargahorariapratica, disciplina.cargahorariateorica, disciplina.horaaula, ");
			sqlStr.append(" sum(case when registroaula.codigo is not null then 1 else 0 end) as qtdeAulaRegistrada, ");
			sqlStr.append(" sum(case when registroaula.codigo is not null then registroaula.cargahoraria else 0 end) as horaAulaRegistrada, ");
			sqlStr.append(" count(horarioturmadiaitem.codigo) as qtdeAulaProgramada, sum(horarioturmadiaitem.duracaoaula) as horaAulaProgramada, ");
			sqlStr.append(" array_to_string(array_agg(distinct professor.nome order by professor.nome), ', ') as professores  ");
			sqlStr.append(" from ( ");
			// Tras as disciplina compostas da grade disciplina de uma turma normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria,  gradedisciplinacomposta.cargahorariapratica,  gradedisciplinacomposta.cargahorariateorica, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
			sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
			sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria,  gradedisciplinacomposta.cargahorariapratica,  gradedisciplinacomposta.cargahorariateorica, gradedisciplinacomposta.nrCreditos ");
			sqlStr.append(" union ");
			// Tras as disciplina da grade disciplina de uma turma normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.cargahorariapratica, gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica as cargahorariateorica, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, gradedisciplina.cargahorariapratica ");
			sqlStr.append(" union ");
			// Tras as disciplina compostas da grade disciplina de uma turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria,  max(gradedisciplinacomposta.cargahorariapratica) as cargahorariapratica,  min(gradedisciplinacomposta.cargahorariateorica) as cargahorariateorica, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
			sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
			sqlStr.append(" union");
			// Tras as disciplina da grade disciplina de uma turma composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, max(gradedisciplina.cargahorariapratica) as cargahorariapratica, min(gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica) as cargahorariateorica, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos ");
			sqlStr.append(" union ");
			// Tras as disciplina compostas de um grupo de optativas de uma turma
			// normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, max(gradedisciplinacomposta.cargahorariapratica) as cargahorariapratica,  min(gradedisciplinacomposta.cargahorariateorica) as cargahorariateorica,  gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
			sqlStr.append(" union ");
			// Tras as disciplina de um grupo de optativas de uma turma normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria,  gradeCurricularGrupoOptativaDisciplina.cargahorariapratica, gradeCurricularGrupoOptativaDisciplina.cargahoraria - gradeCurricularGrupoOptativaDisciplina.cargahorariapratica as cargahorariateorica, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula, gradeCurricularGrupoOptativaDisciplina.cargahorariapratica ");
			sqlStr.append(" union ");
			// Tras as disciplina compostas de um grupo de optativas de uma turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, max(gradedisciplinacomposta.cargahorariapratica) as cargahorariapratica,  min(gradedisciplinacomposta.cargahorariateorica) as cargahorariateorica,  gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
			sqlStr.append(" union ");
			// Tras as disciplina de um grupo de optativas de uma turma composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, max(gradeCurricularGrupoOptativaDisciplina.cargahorariapratica) as cargahorariapratica, min(gradeCurricularGrupoOptativaDisciplina.cargahoraria - gradeCurricularGrupoOptativaDisciplina.cargahorariapratica) as cargahorariateorica, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma).append(" and disciplina.codigo = ").append(disciplina);
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula ");
			sqlStr.append(" ) as disciplina ");
			sqlStr.append(" inner join horarioturma on horarioturma.codigo = ").append(programacaoAula);
			sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			sqlStr.append(" and horarioturmadiaitem.disciplina = disciplina.codigo ");
			sqlStr.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
			sqlStr.append(" left join registroaula on registroaula.turma = horarioturma.turma and registroaula.disciplina = horarioturmadiaitem.disciplina ");
			sqlStr.append(" and registroaula.ano = horarioturma.anovigente and registroaula.semestre = horarioturma.semestrevigente ");
			sqlStr.append(" and registroaula.data = horarioturmadiaitem.data and registroaula.nraula = horarioturmadiaitem.nraula ");
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, disciplina.cargahoraria, disciplina.cargahorariapratica, disciplina.cargahorariateorica, disciplina.horaaula ");
			return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}
		return null;
	}
	
	
}
