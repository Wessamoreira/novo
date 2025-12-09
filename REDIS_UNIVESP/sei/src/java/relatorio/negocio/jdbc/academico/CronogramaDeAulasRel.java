package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.interfaces.academico.CronogramaDeAulasRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class CronogramaDeAulasRel extends SuperRelatorio implements CronogramaDeAulasRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public CronogramaDeAulasRel() {
	}

	public void validarDados(Date dataInicio, Date dataFim, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre) throws Exception {
		if (periodicidadeEnum==null) {
			throw new Exception("O campo PERIODICIDADE deve ser informado.");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
			if (dataInicio == null) {
				throw new Exception("O campo DATA INICIAL deve ser informado.");
			}
			if (dataFim == null) {
				throw new Exception("O campo DATA FINAL deve ser informado.");
			}
		}
		if (Uteis.isAtributoPreenchido(dataFim) && Uteis.isAtributoPreenchido(dataInicio) && dataFim.compareTo(dataInicio) < 0) {
			throw new Exception("O campo DATA INICIAL dever ser menor ou igual a DATA FINAL.");
		}
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && (!Uteis.isAtributoPreenchido(ano) || ano.trim().length() != 4)) {
			throw new Exception("O campo ANO deve ser informado (Ex: "+Uteis.getAnoDataAtual4Digitos()+").");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && (!Uteis.isAtributoPreenchido(semestre))) {
			throw new Exception("O campo SEMESTRE deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(dataFim) && Uteis.isAtributoPreenchido(dataInicio) && Uteis.getObterDiferencaDiasEntreDuasData(dataFim, dataInicio) > 731) {
			throw new Exception(UteisJSF.internacionalizar("Atenção! O período máximo de consulta por DATA deve ser de no máximo DOIS ANOS!"));
		}

	}

	@Override
	public List<CronogramaDeAulasRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, String tipoTurma, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String tipoLayout, boolean visaoAluno, UsuarioVO usuario, String ordenacao, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		validarDados(dataInicio, dataFim, periodicidadeEnum, ano, semestre);
		boolean realizarProcessamentoComParalelismo = true;
		if (tipoLayout.equals("periodoAula") || (tipoLayout.equals("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel") && periodicidadeEnum.isIntegral())) {
			List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs =  criarObjetoPeriodoAula(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
			if (realizarProcessamentoComParalelismo) {
				this.consultarDadosEstatisticoMatriculaComParalelismo(cronogramaDeAulasRelVOs, true, true, true, true);
			} else {
				consultarDadosEstatisticoMatricula(cronogramaDeAulasRelVOs, true, true, true, true);
			}
			return cronogramaDeAulasRelVOs;
		} else if (tipoLayout.equals("dataAulaHorario")) {
			return criarObjetoPeriodoDataAulaComHorario(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
		} else if (tipoLayout.equals("dataAula")) {
			List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = criarObjetoDataAula(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
			if (realizarProcessamentoComParalelismo) {
				this.consultarDadosEstatisticoMatriculaComParalelismo(cronogramaDeAulasRelVOs, true, false, true, true);
			} else {
				consultarDadosEstatisticoMatricula(cronogramaDeAulasRelVOs, true, true, true, true);
			}
			return cronogramaDeAulasRelVOs;
		} else if (tipoLayout.equals("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel") && !periodicidadeEnum.isIntegral()) {
			List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = criarObjetoDataAula(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
			if (realizarProcessamentoComParalelismo) {
				this.consultarDadosEstatisticoMatriculaComParalelismo(cronogramaDeAulasRelVOs, true, false, true, true);
			} else {
				consultarDadosEstatisticoMatricula(cronogramaDeAulasRelVOs, true, false, true, true);
			}
			return cronogramaDeAulasRelVOs;
		} else if (tipoLayout.equals("horarioPorSala")) {
			return criarObjetoHorarioPorSala(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
		} else if (tipoLayout.equals("horarioPorCurso")) {
			List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = criarObjetoHorarioPorCurso(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
			if (realizarProcessamentoComParalelismo) {
				this.consultarDadosEstatisticoMatriculaComParalelismo(cronogramaDeAulasRelVOs, true, true, true, true);
			} else {
				consultarDadosEstatisticoMatricula(cronogramaDeAulasRelVOs, true, true, true, true);
			}
			return cronogramaDeAulasRelVOs;
		} else if (tipoLayout.equals("frequenciaProfessor")) {
			return criarObjetoFrequenciaProfessor(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
		} else if (tipoLayout.equals("frequenciaTurmaDisciplinaProfessor")) {
			return criarObjetoFrequenciaTurmaDisciplinaProfessor(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
		} else if (tipoLayout.equals("frequenciaTurmaDisciplinaDataHoraProfessor")) {
			return criarObjetoFrequenciaTurmaDisciplinaDataHoraProfessor(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
		} else {
			return new ArrayList<CronogramaDeAulasRelVO>(0);
		}
	}

	public String getSqlWhere(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, PeriodicidadeEnum periodicidadeEnum, boolean ead, UsuarioVO usuarioVO){
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" where 1 = 1 ");

		if(!ead) {
			if (dataInicio != null && dataFim != null) {
				sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "horarioturmadia.data", false));
			}
			if (Uteis.isAtributoPreenchido(ano) && !periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
				sqlStr.append(" and horarioturma.anovigente = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre) && periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
			}
		}
		else {
			sqlStr.append(" and programacaotutoriaonlineprofessor.situacaoprogramacaotutoriaonline  = 'ATIVO' ");
			
			if(Uteis.isAtributoPreenchido(dataInicio) && Uteis.isAtributoPreenchido(dataFim)) {
				sqlStr.append(" and programacaotutoriaonline.datainicioaula >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
				sqlStr.append(" and programacaotutoriaonline.datainicioaula <= '").append(Uteis.getDataJDBC(dataFim)).append("'");
			}
			//sqlStr.append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "horarioturmadia.data", false));
			if (Uteis.isAtributoPreenchido(ano) && !periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
				sqlStr.append(" and programacaotutoriaonline.ano = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre) && periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and programacaotutoriaonline.semestre = '").append(semestre).append("' ");
			}
		}

		if (unidadeEnsino > 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sqlStr.append(" and turma.semestral = true ");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
			sqlStr.append(" and turma.anual = true ");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
			sqlStr.append(" and turma.anual = false and turma.semestral = false ");
		}
		if (curso > 0  && !Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and ((turma.turmaagrupada = false and turma.curso = ").append(curso).append(") ");
			sqlStr.append(" or (turma.turmaagrupada = true and turma.codigo in (select ta.turmaorigem from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo and t.curso =  ").append(curso).append("))) ");
		}
		if (turma > 0) {
			sqlStr.append(" and turma.codigo = ").append(turma);
		} else {
			if (tipoTurma.equals("NORMAL")) {
				sqlStr.append(" and turma.turmaagrupada is not true and turma.subturma is not true ");
			} else if (tipoTurma.equals("TEORICA") || tipoTurma.equals("PRATICA") || tipoTurma.equals("GERAL")) {
				sqlStr.append(" and turma.subturma is true and turma.tiposubturma = '").append(tipoTurma).append("' ");
			} else if (tipoTurma.equals("AGRUPADA")) {
				sqlStr.append(" and turma.turmaagrupada is true ");
			}
		}
		if (disciplina > 0) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (turno.getCodigo() != 0) {
			sqlStr.append(" and turma.turno = ").append(turno.getCodigo());
		}
		if (funcionario.getCodigo() != 0) {
			sqlStr.append(" and pessoa.codigo = ").append(funcionario.getPessoa().getCodigo());
		}
		if(!ead) {
			if (salaLocalAula.getCodigo() != 0) {
				sqlStr.append(" and salalocalaula.codigo = ").append(salaLocalAula.getCodigo());
			}
			if (localAula.getCodigo() != 0) {
				sqlStr.append(" and localaula.codigo = ").append(localAula.getCodigo());
		}
		}
		if (Uteis.isAtributoPreenchido(usuarioVO) && Uteis.isAtributoPreenchido(usuarioVO.getPessoa()) && usuarioVO.getIsApresentarVisaoCoordenador()) {
			sqlStr.append(" and EXISTS (select distinct pessoa.codigo from cursocoordenador c  ");
			sqlStr.append(" inner join funcionario on funcionario.codigo = c.funcionario ");
			sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
			sqlStr.append(" where (((turma.turmaagrupada = false and c.curso = turma.curso and c.unidadeensino = turma.unidadeensino and c.turma is null) or ");
			sqlStr.append(" (turma.turmaAgrupada and c.curso IN (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo))) ");
			sqlStr.append(" or (c.turma = turma.codigo)) ");
			sqlStr.append(" and pessoa.codigo = ").append(usuarioVO.getPessoa().getCodigo()).append(") ");
		}
		sqlStr.append(" ");
		return sqlStr.toString();
	}

	private List<CronogramaDeAulasRelVO> criarObjetoPeriodoAula(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula, ");
		
		sqlStr.append(" (select array_to_string(array_agg(distinct pessoa.nome order by pessoa.nome), ', ') as coordenadores from cursocoordenador c  ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = c.funcionario ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where (((turma.turmaagrupada = false and c.curso = turma.curso and c.unidadeensino = turma.unidadeensino and c.turma is null) or ");
		sqlStr.append(" (turma.turmaAgrupada and c.curso IN (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.turma))) ");
		sqlStr.append(" or (c.turma = turma.turma)) ");
		sqlStr.append("  ) as nomeCoordenador, ");
		
		sqlStr.append(" ( select array_to_string(array_agg(distinct pessoa.email), ', ') as coordenadores from cursocoordenador c  ");
		sqlStr.append("  inner join funcionario on funcionario.codigo = c.funcionario ");
		sqlStr.append("  inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where (((turma.turmaagrupada = false and c.curso = turma.curso and c.unidadeensino = turma.unidadeensino and c.turma is null) or ");
		sqlStr.append(" (turma.turmaAgrupada and c.curso IN (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.turma))) ");
		sqlStr.append(" or (c.turma = turma.turma)) ");
		sqlStr.append("  and pessoa.email != '' ");
		sqlStr.append("  ) as emailCoordenador ");
		
		sqlStr.append(" from ( ");
		sqlStr.append(" (select min(horarioturmadia.data) as dataInicio, max(horarioturmadia.data) as dataFim, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.curso as curso, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as nomeCurso,");
		sqlStr.append(" pessoa.telefoneRes as telefoneProfessor, ");
		sqlStr.append(" pessoa.email as emailProfessor, pessoa.celular as celularProfessor, turma.codigo as codigoturma, disciplina.codigo as codigodisciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, ");
		sqlStr.append(" (select count(*) as qtdeMaterialPostado from ( select distinct j.qtde from (    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina ");
		sqlStr.append(" AND professor = pessoa.codigo AND turma is null  union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND turma = horarioturma.turma  ");
		sqlStr.append(" and professor is null union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND professor = pessoa.codigo AND turma = horarioturma.turma  ");
		sqlStr.append(" union all    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND professor is null AND turma is null ) as j) as t  ) as qtdeMaterialPostado, ");
		sqlStr.append(" turno.codigo as turno, turno.nome as nomeTurno, disciplina.descricaoComplementar as descricaoComplementarDisciplina, ");
		sqlStr.append(" periodoletivo.periodoletivo, turma.nrVagas, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, ");

		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplina, ");


		sqlStr.append(" (select sum(vagaturmadisciplina.nrVagasMatriculaReposicao) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplinareposicao, ");

		sqlStr.append(" ((select count(distinct disciplina) from  horarioturma ht ");
		sqlStr.append(" inner join horarioturmadia htd on htd.horarioturma = ht.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo ");
		sqlStr.append(" inner join disciplina dis on htdi.disciplina = dis.codigo ");
		sqlStr.append(" where ht.codigo = horarioturma.codigo and dis.codigo != disciplina.codigo and htd.data < max(horarioturmadia.data)) + 1) as numeroDisciplina, ");


		sqlStr.append(" (select numeromodulo from (select row_number() OVER (ORDER BY min(htd.data)) AS numeromodulo, htdi.disciplina from horarioturmadiaitem htdi");
		sqlStr.append(" inner join horarioturmadia as htd on htd.codigo = htdi.horarioturmadia");
		sqlStr.append(" and htd.horarioturma = horarioturma.codigo and htdi.disciplina is not null ");
		sqlStr.append(" group by  htdi.disciplina) as ordem where ordem.disciplina = horarioturmadiaitem.disciplina) as numeromodulo, 0 as programacaotutoriaonline, salalocalaula.sala as sala, localaula.local as local, ");
		sqlStr.append("	(");
		sqlStr.append("select turmaabertura.data from turmaabertura where turmaabertura.situacao = 'IN' and turmaabertura.turma = turma.codigo order by turmaabertura.data desc limit 1 ");
		sqlStr.append(") as dataabertura ");

		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno ");
		sqlStr.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
//		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino  ");
//		sqlStr.append(" and cursocoordenador.codigo = (select cc.codigo from cursocoordenador as cc where cc.curso = curso.codigo and (unidadeensino.codigo = cc.unidadeensino or cc.unidadeensino is null) and (cc.turma = turma.codigo or cc.turma is null) order by case when cc.unidadeensino is not null then 0 else 1 end, case when cc.turma is not null then 0 else 1 end limit 1 ) ");
//		sqlStr.append(" left join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
//		sqlStr.append(" left join pessoa as coordenador on funcionario.pessoa = coordenador.codigo  ");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");



		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		/*sqlStr.append(" group by cidade.codigo, cidade.nome, unidadeensino.codigo, unidadeensino.nome, turma.codigo, pessoa.telefoneRes, pessoa.email, pessoa.celular, turma.identificadorturma,  ");
		sqlStr.append(" disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, turma.curso, horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.codigo,  ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");*/

		sqlStr.append(" group by cidade.codigo , cidade.nome, unidadeensino.codigo , unidadeensino.nome, turma.codigo , pessoa.telefoneRes, pessoa.email , pessoa.celular, turma.identificadorturma, disciplina.codigo , disciplina.nome, pessoa.codigo , pessoa.nome, turma.curso , horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente,  ");
		sqlStr.append(" horarioturma.codigo, turno.codigo, turno.nome, curso.nome, periodoletivo.periodoletivo, turma.codigo, disciplina.codigo , horarioturma.anovigente, horarioturma.semestrevigente, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, salalocalaula.sala, localaula.local ");


		sqlStr.append(" )");

		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select programacaotutoriaonline.datainicioaula as dataInicio, programacaotutoriaonline.dataterminoaula as dataFim, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.curso as curso, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as nomeCurso,");
		sqlStr.append(" pessoa.telefoneRes as telefoneProfessor, ");
		sqlStr.append(" pessoa.email as emailProfessor, pessoa.celular as celularProfessor, turma.codigo as codigoturma, disciplina.codigo as codigodisciplina, programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, ");
		sqlStr.append(" (select count(*) as qtdeMaterialPostado from ( select distinct j.qtde from (    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina ");
		sqlStr.append(" AND professor = pessoa.codigo AND turma is null  union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" and professor is null union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor = pessoa.codigo AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" union all    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor is null AND turma is null ) as j) as t  ) as qtdeMaterialPostado, ");
		sqlStr.append(" turno.codigo as turno, turno.nome as nomeTurno, disciplina.descricaoComplementar as descricaoComplementarDisciplina, ");
		sqlStr.append(" periodoletivo.periodoletivo, turma.nrVagas, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, ");

		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplina, ");


		sqlStr.append(" (select sum(vagaturmadisciplina.nrVagasMatriculaReposicao) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplinareposicao, ");

		sqlStr.append(" ((select count(distinct disciplina) from programacaotutoriaonline pto ");
		sqlStr.append(" inner join disciplina dis on pto.disciplina = dis.codigo ");
		sqlStr.append(" where pto.codigo = programacaotutoriaonline.codigo and dis.codigo != disciplina.codigo and pto.dataterminoaula < programacaotutoriaonline.dataterminoaula) + 1) as numeroDisciplina, ");


		sqlStr.append(" (select numeromodulo from (select row_number() OVER (ORDER BY min(pto.datainicioaula)) AS numeromodulo, pto.disciplina from programacaotutoriaonline pto");
		sqlStr.append(" where pto.disciplina is not null ");
		sqlStr.append(" group by  pto.disciplina) as ordem where ordem.disciplina = programacaotutoriaonline.disciplina) as numeromodulo, programacaotutoriaonline.codigo as  programacaotutoriaonline, '' as sala, '' as local,");
		sqlStr.append("	(");
		sqlStr.append("select turmaabertura.data from turmaabertura where turmaabertura.situacao = 'IN' and turmaabertura.turma = turma.codigo order by turmaabertura.data desc limit 1 ");
		sqlStr.append(") as dataabertura ");

		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno ");
		sqlStr.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
//		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino  ");
//		sqlStr.append(" and cursocoordenador.codigo = (select cc.codigo from cursocoordenador as cc where cc.curso = curso.codigo and (unidadeensino.codigo = cc.unidadeensino or cc.unidadeensino is null) and (cc.turma = turma.codigo or cc.turma is null) order by case when cc.unidadeensino is not null then 0 else 1 end, case when cc.turma is not null then 0 else 1 end limit 1 ) ");
//		sqlStr.append(" left join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
//		sqlStr.append(" left join pessoa as coordenador on funcionario.pessoa = coordenador.codigo  ");


		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		/*sqlStr.append(" group by cidade.codigo, cidade.nome, unidadeensino.codigo, unidadeensino.nome, turma.codigo, pessoa.telefoneRes, pessoa.email, pessoa.celular, turma.identificadorturma,  ");
		sqlStr.append(" disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, turma.curso, horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.codigo,  ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");*/

		sqlStr.append(" group by cidade.codigo , cidade.nome, unidadeensino.codigo , unidadeensino.nome, turma.codigo , pessoa.telefoneRes, pessoa.email , pessoa.celular, turma.identificadorturma, disciplina.codigo , disciplina.nome, pessoa.codigo , pessoa.nome, turma.curso , programacaotutoriaonline.turma, programacaotutoriaonline.disciplina, curso.nome, programacaotutoriaonline.ano, programacaotutoriaonline.semestre,  ");
		sqlStr.append(" programacaotutoriaonline.codigo, turno.codigo, turno.nome, curso.nome, periodoletivo.periodoletivo, turma.codigo, disciplina.codigo, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");


		sqlStr.append(" )");
		sqlStr.append(") as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		if (ordenacao.equals("data")) {
			sqlStr.append("ORDER BY dataInicio, nomeUnidadeEnsino, identificadorturma, nomeDisciplina ");
		} else if (ordenacao.equals("disciplina")) {
			sqlStr.append("ORDER BY nomeDisciplina, dataInicio, nomeUnidadeEnsino, identificadorturma ");
		} else if (ordenacao.equals("turma")) {
			sqlStr.append("ORDER BY identificadorturma, dataInicio, nomeDisciplina ");
		} else {
			sqlStr.append("ORDER BY nomeUnidadeEnsino, dataInicio, identificadorturma, nomeDisciplina ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosPeriodoAula(tabelaResultado, visaoAluno, dataInicio, dataFim, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> montarDadosPeriodoAula(SqlRowSet tabelaResultado, boolean visaoAluno, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setDataModulo(Uteis.getData(tabelaResultado.getDate("dataInicio")) + " à " + Uteis.getData(tabelaResultado.getDate("dataFim")));
			carVO.setModulo(tabelaResultado.getString("numeromodulo") + "º");
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setCodigoDisciplina(tabelaResultado.getInt("codigodisciplina"));
			carVO.setCodigoTurma(tabelaResultado.getInt("codigoTurma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			if (!visaoAluno) {
				carVO.setCodigoProfessor(tabelaResultado.getInt("pessoa"));
				carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
				carVO.setTelefoneProfessor(tabelaResultado.getString("telefoneProfessor"));
				carVO.setCelularProfessor(tabelaResultado.getString("celularProfessor"));
				carVO.setEmailProfessor(tabelaResultado.getString("emailProfessor"));
				FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(tabelaResultado.getInt("pessoa"), usuarioVO);
				if (Uteis.isAtributoPreenchido(formacaoAcademicaVO)) {
					carVO.setTitulacaoProfessor(formacaoAcademicaVO.getEscolaridade_Apresentar());
				}
			}

			carVO.setDataInicio(dataInicio);
			carVO.setDataFim(dataFim);
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCidade(tabelaResultado.getString("nomeCidade"));
			carVO.setTurma(tabelaResultado.getString("identificadorTurma"));
			carVO.setPeriodo(tabelaResultado.getString("periodoLetivo"));
			carVO.setDataUnidadeOrdenacao(carVO.getDataModulo() + " - " + carVO.getUnidadeEnsino());
			carVO.setQtdeMaterialPostado(tabelaResultado.getInt("qtdeMaterialPostado"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));

			carVO.setCurso(tabelaResultado.getString("nomeCurso"));
			carVO.setTurno(tabelaResultado.getString("nomeTurno"));
			carVO.setDescricaoComplementarDisciplina(tabelaResultado.getString("descricaoComplementarDisciplina"));
			carVO.setNomeCoordenador(tabelaResultado.getString("nomeCoordenador"));
			carVO.setEmailCoordenador(tabelaResultado.getString("emailCoordenador"));
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(tabelaResultado.getObject("vagadisciplina") != null) {
				carVO.setNumeroVaga(tabelaResultado.getInt("vagadisciplina"));
			}else {
				carVO.setNumeroVaga(tabelaResultado.getInt("nrVagas"));
			}
			if(tabelaResultado.getObject("vagadisciplinareposicao") != null) {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("vagadisciplinareposicao"));
			}else {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("nrVagasInclusaoReposicao"));
			}
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(carVO.getNumeroDisciplina().equals(0)) {
				carVO.setNumeroDisciplina(1);
			}

			carVO.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			carVO.setSubTurma(tabelaResultado.getBoolean("subturma"));
			carVO.setAnual(tabelaResultado.getBoolean("anual"));
			carVO.setSemestral(tabelaResultado.getBoolean("semestral"));
			if(tabelaResultado.getObject("tiposubturma") != null) {
				carVO.setTipoSubTurmaEnum(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tiposubturma")));
			}
			if(tabelaResultado.getInt("programacaotutoriaonline") > 0) {
				carVO.setEad(true);
			}
			else {
				carVO.setEad(false);
			}
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setDataAbertura(tabelaResultado.getDate("dataabertura"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}
	
	private List<CronogramaDeAulasRelVO> montarDadosPeriodoAulaTutoriaOnline(SqlRowSet tabelaResultado, boolean visaoAluno, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setDataModulo(Uteis.getData(tabelaResultado.getDate("dataInicio")) + " à " + Uteis.getData(tabelaResultado.getDate("dataFim")));
			carVO.setModulo(tabelaResultado.getString("numeromodulo") + "º");
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setCodigoDisciplina(tabelaResultado.getInt("codigodisciplina"));
			carVO.setCodigoTurma(tabelaResultado.getInt("codigoTurma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			if (!visaoAluno) {
				carVO.setCodigoProfessor(tabelaResultado.getInt("pessoa"));
				carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
				carVO.setTelefoneProfessor(tabelaResultado.getString("telefoneProfessor"));
				carVO.setCelularProfessor(tabelaResultado.getString("celularProfessor"));
				carVO.setEmailProfessor(tabelaResultado.getString("emailProfessor"));
				FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(tabelaResultado.getInt("pessoa"), usuarioVO);
				if (Uteis.isAtributoPreenchido(formacaoAcademicaVO)) {
					carVO.setTitulacaoProfessor(formacaoAcademicaVO.getEscolaridade_Apresentar());
				}
			}

			carVO.setDataInicio(dataInicio);
			carVO.setDataFim(dataFim);
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCidade(tabelaResultado.getString("nomeCidade"));
			carVO.setTurma(tabelaResultado.getString("identificadorTurma"));
			carVO.setPeriodo(tabelaResultado.getString("periodoLetivo"));
			carVO.setDataUnidadeOrdenacao(carVO.getDataModulo() + " - " + carVO.getUnidadeEnsino());
			carVO.setQtdeMaterialPostado(tabelaResultado.getInt("qtdeMaterialPostado"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));

			carVO.setCurso(tabelaResultado.getString("nomeCurso"));
			carVO.setTurno(tabelaResultado.getString("nomeTurno"));
			carVO.setDescricaoComplementarDisciplina(tabelaResultado.getString("descricaoComplementarDisciplina"));
			carVO.setNomeCoordenador(tabelaResultado.getString("nomeCoordenador"));
			carVO.setEmailCoordenador(tabelaResultado.getString("emailCoordenador"));
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(tabelaResultado.getObject("vagadisciplina") != null) {
				carVO.setNumeroVaga(tabelaResultado.getInt("vagadisciplina"));
			}else {
				carVO.setNumeroVaga(tabelaResultado.getInt("nrVagas"));
			}
			if(tabelaResultado.getObject("vagadisciplinareposicao") != null) {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("vagadisciplinareposicao"));
			}else {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("nrVagasInclusaoReposicao"));
			}
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(carVO.getNumeroDisciplina().equals(0)) {
				carVO.setNumeroDisciplina(1);
			}

			carVO.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			carVO.setSubTurma(tabelaResultado.getBoolean("subturma"));
			carVO.setAnual(tabelaResultado.getBoolean("anual"));
			carVO.setSemestral(tabelaResultado.getBoolean("semestral"));
			if(tabelaResultado.getObject("tiposubturma") != null) {
				carVO.setTipoSubTurmaEnum(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tiposubturma")));
			}
			if(tabelaResultado.getInt("programacaotutoriaonline") > 0) {
				carVO.setEad(true);
			}
			else {
				carVO.setEad(false);
			}
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> criarObjetoDataAula(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula, ");
		
		
		sqlStr.append(" (select array_to_string(array_agg(distinct pessoa.nome order by pessoa.nome), ', ') as coordenadores from cursocoordenador c  ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = c.funcionario ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" where ((c.curso = turma.curso and c.unidadeensino = turma.unidadeensino and c.turma is null) or (c.turma = turma.turma)) ");
		sqlStr.append("  ) as nomeCoordenador, ");
		
		sqlStr.append(" ( select array_to_string(array_agg(distinct pessoa.email), ', ') as coordenadores from cursocoordenador c  ");
		sqlStr.append("  inner join funcionario on funcionario.codigo = c.funcionario ");
		sqlStr.append("  inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("  where ((c.curso = turma.curso and c.unidadeensino = turma.unidadeensino and c.turma is null) or (c.turma = turma.turma)) ");
		sqlStr.append("  and pessoa.email != '' ");
		sqlStr.append("  ) as emailCoordenador ");

		sqlStr.append(" from ( ");
		sqlStr.append("select horarioturmadia.data, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma, ");
		sqlStr.append("turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.curso as curso, curso.nome as nomeCurso, ");
		sqlStr.append("pessoa.telefoneRes as telefoneProfessor, pessoa.email as emailProfessor, pessoa.celular as celularProfessor,  unidadeensino.nome as unidadeensinoTurma, horarioturmadia.data as dataturma, ");
		sqlStr.append("(select count(*) as qtdeMaterialPostado from ( select distinct j.qtde from ( ");
		sqlStr.append("SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND professor = pessoa.codigo AND turma is null ");
		sqlStr.append("union all  SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND professor = pessoa.codigo AND turma = horarioturma.turma ");
		sqlStr.append("union all  SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND turma = horarioturma.turma  and professor is null ");
		sqlStr.append("union all  SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = horarioturmadiaitem.disciplina AND professor is null AND turma is null ");
		sqlStr.append(") as j) as t  ) as qtdeMaterialPostado, ");
		sqlStr.append(" turno.codigo as turno, turno.nome as nomeTurno, disciplina.descricaoComplementar as descricaoComplementarDisciplina, ");
		sqlStr.append(" periodoletivo.periodoletivo, turma.nrVagas, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, ");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplina, ");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrVagasMatriculaReposicao) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplinareposicao, ");
		sqlStr.append(" (select count(distinct disciplina) from  horarioturma ht ");
		sqlStr.append(" inner join horarioturmadia htd on htd.horarioturma = ht.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo ");
		sqlStr.append(" inner join disciplina dis on htdi.disciplina = dis.codigo ");
		sqlStr.append(" where ht.codigo = horarioturma.codigo and dis.codigo != disciplina.codigo and htd.data < horarioturmadia.data) + 1 as numeroDisciplina, ");
		sqlStr.append(" turma.codigo as codigoturma, disciplina.codigo as codigodisciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, salalocalaula.sala as sala, localaula.local as local, ");
		sqlStr.append("	(");
		sqlStr.append("select turmaabertura.data from turmaabertura where turmaabertura.situacao = 'IN' and turmaabertura.turma = turma.codigo order by turmaabertura.data desc limit 1 ");
		sqlStr.append(") as dataabertura ");
		sqlStr.append("from horarioturma ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append("inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append("inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append("inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append("inner join turno on turno.codigo = turma.turno ");
		sqlStr.append("left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
//		sqlStr.append("left join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino  ");
//		sqlStr.append(" and cursocoordenador.codigo = (select cc.codigo from cursocoordenador as cc where cc.curso = curso.codigo and (unidadeensino.codigo = cc.unidadeensino or cc.unidadeensino is null) and (cc.turma = turma.codigo or cc.turma is null) order by case when cc.unidadeensino is not null then 0 else 1 end, case when cc.turma is not null then 0 else 1 end limit 1 ) ");
//		sqlStr.append("left join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
//		sqlStr.append("left join pessoa as coordenador on funcionario.pessoa = coordenador.codigo  ");
		//sqlStr.append("left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala ");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append("left join localaula on salalocalaula.localaula = localaula.codigo ");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" group by horarioturmadia.data, cidade.codigo , cidade.nome, unidadeensino.codigo , unidadeensino.nome, turma.codigo , pessoa.telefoneRes, pessoa.email , pessoa.celular, turma.identificadorturma, disciplina.codigo , disciplina.nome, pessoa.codigo , pessoa.nome, turma.curso , horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente,  ");
		sqlStr.append(" horarioturma.codigo, turno.codigo, turno.nome, curso.nome, periodoletivo.periodoletivo, turma.codigo, disciplina.codigo , horarioturma.anovigente, horarioturma.semestrevigente, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, salalocalaula.sala, localaula.local ");
		
		sqlStr.append(" )");
		
		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select programacaotutoriaonline.datainicioaula as dataInicio, programacaotutoriaonline.dataterminoaula as dataFim, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as codigoDisciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.curso as curso, curso.nome as nomeCurso,  pessoa.telefoneRes as telefoneProfessor, ");
		sqlStr.append(" pessoa.email as emailProfessor, pessoa.celular as celularProfessor, programacaotutoriaonline.semestre as semestre,programacaotutoriaonline.datainicioaula as dataIniciotutoriaonline, ");
		sqlStr.append(" (select count(*) as qtdeMaterialPostado from ( select distinct j.qtde from (    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina ");
		sqlStr.append(" AND professor = pessoa.codigo AND turma is null  union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" and professor is null union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor = pessoa.codigo AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" union all    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor is null AND turma is null ) as j) as t  ) as qtdeMaterialPostado, ");
		sqlStr.append(" turno.codigo as turno, turno.nome as nomeTurno, disciplina.descricaoComplementar as descricaoComplementarDisciplina, ");
		sqlStr.append(" periodoletivo.periodoletivo, turma.nrVagas, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, ");

		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplina, ");


		sqlStr.append(" (select sum(vagaturmadisciplina.nrVagasMatriculaReposicao) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = vagaturma.turma ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplinareposicao, ");

		sqlStr.append(" ((select count(distinct disciplina) from programacaotutoriaonline pto ");
		sqlStr.append(" inner join disciplina dis on pto.disciplina = dis.codigo ");
		sqlStr.append(" where pto.codigo = programacaotutoriaonline.codigo and dis.codigo != disciplina.codigo and pto.dataterminoaula < programacaotutoriaonline.dataterminoaula) + 1) as numeroDisciplina, ");


		sqlStr.append(" (select numeromodulo from (select row_number() OVER (ORDER BY min(pto.datainicioaula)) AS numeromodulo, pto.disciplina from programacaotutoriaonline pto");
		sqlStr.append(" where pto.disciplina is not null ");
		sqlStr.append(" group by  pto.disciplina) as ordem where ordem.disciplina = programacaotutoriaonline.disciplina) as numeromodulo, programacaotutoriaonline.codigo as  programacaotutoriaonline , programacaotutoriaonline.ano as ano,programacaotutoriaonline.semestre as semestre, '' as sala, '' as local, ");
		sqlStr.append("	(");
		sqlStr.append("select turmaabertura.data from turmaabertura where turmaabertura.situacao = 'IN' and turmaabertura.turma = turma.codigo order by turmaabertura.data desc limit 1 ");
		sqlStr.append(") as dataabertura ");
		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno ");
		sqlStr.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");

		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		sqlStr.append(" group by cidade.codigo , cidade.nome, unidadeensino.codigo , unidadeensino.nome, turma.codigo , pessoa.telefoneRes, pessoa.email , pessoa.celular, turma.identificadorturma, disciplina.codigo , disciplina.nome, pessoa.codigo , pessoa.nome, turma.curso , programacaotutoriaonline.turma, programacaotutoriaonline.disciplina, curso.nome, programacaotutoriaonline.ano, programacaotutoriaonline.semestre,  ");
		sqlStr.append(" programacaotutoriaonline.codigo, turno.codigo, turno.nome, curso.nome, periodoletivo.periodoletivo, turma.codigo, disciplina.codigo, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");
		sqlStr.append(" )");
		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		if (ordenacao.equals("data")) {
			sqlStr.append("ORDER BY dataturma, unidadeEnsinoturma, turma.identificadorturma");
		} else if (ordenacao.equals("disciplina")) {
			sqlStr.append("ORDER BY nomeDisciplina, dataturma, nomeUnidadeEnsino, identificadorturma ");
		} else if (ordenacao.equals("turma")) {
			sqlStr.append("ORDER BY identificadorturma,dataturma, nomeDisciplina ");
		}else {
			sqlStr.append("ORDER BY unidadeEnsinoturma, dataturma, turma.identificadorturma");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosDataAula(tabelaResultado, visaoAluno, dataInicio, dataFim, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> montarDadosDataAula(SqlRowSet tabelaResultado, boolean visaoAluno, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setData(tabelaResultado.getDate("data"));
			carVO.setDataModulo(Uteis.getData(carVO.getData()) + " ");
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setCodigoDisciplina(tabelaResultado.getInt("codigoDisciplina"));
			carVO.setCodigoTurma(tabelaResultado.getInt("codigoTurma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			if (!visaoAluno) {
				carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
				carVO.setTelefoneProfessor(tabelaResultado.getString("telefoneProfessor"));
				carVO.setCelularProfessor(tabelaResultado.getString("celularProfessor"));
				carVO.setEmailProfessor(tabelaResultado.getString("emailProfessor"));
				FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(tabelaResultado.getInt("pessoa"), usuarioVO);
				if (Uteis.isAtributoPreenchido(formacaoAcademicaVO)) {
					carVO.setTitulacaoProfessor(formacaoAcademicaVO.getEscolaridade_Apresentar());
				}
			}
			carVO.setDataInicio(dataInicio);
			carVO.setDataFim(dataFim);
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCidade(tabelaResultado.getString("nomeCidade"));
			carVO.setTurma(tabelaResultado.getString("identificadorTurma"));
			carVO.setPeriodo(tabelaResultado.getString("periodoLetivo"));
			carVO.setDataUnidadeOrdenacao(carVO.getDataModulo() + " - " + carVO.getUnidadeEnsino());
			carVO.setQtdeMaterialPostado(tabelaResultado.getInt("qtdeMaterialPostado"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			carVO.setCurso(tabelaResultado.getString("nomeCurso"));
			carVO.setTurno(tabelaResultado.getString("nomeTurno"));
			carVO.setDescricaoComplementarDisciplina(tabelaResultado.getString("descricaoComplementarDisciplina"));
			carVO.setNomeCoordenador(tabelaResultado.getString("nomeCoordenador"));
			carVO.setEmailCoordenador(tabelaResultado.getString("emailCoordenador"));
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(tabelaResultado.getObject("vagadisciplina") != null) {
				carVO.setNumeroVaga(tabelaResultado.getInt("vagadisciplina"));
			}else {
				carVO.setNumeroVaga(tabelaResultado.getInt("nrVagas"));
			}
			if(tabelaResultado.getObject("vagadisciplinareposicao") != null) {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("vagadisciplinareposicao"));
			}else {
				carVO.setNumeroVagaReposicao(tabelaResultado.getInt("nrVagasInclusaoReposicao"));
			}
			carVO.setNumeroDisciplina(tabelaResultado.getInt("numeroDisciplina"));
			if(carVO.getNumeroDisciplina().equals(0)) {
				carVO.setNumeroDisciplina(1);
			}
			carVO.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			carVO.setSubTurma(tabelaResultado.getBoolean("subturma"));
			carVO.setAnual(tabelaResultado.getBoolean("anual"));
			carVO.setSemestral(tabelaResultado.getBoolean("semestral"));
			if(tabelaResultado.getObject("tiposubturma") != null) {
				carVO.setTipoSubTurmaEnum(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tiposubturma")));
			}
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setDataAbertura(tabelaResultado.getDate("dataabertura"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	public static String getDesignIReportRelatorio(String layout) {
		if (layout.equals("dataAulaHorario")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioRel.jrxml");
		} else if (layout.equals("horarioPorSala")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioPorSalaRel.jrxml");
		} else if (layout.equals("horarioPorCurso")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioPorCursoRel.jrxml");
		} else if (layout.equals("frequenciaProfessor")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaProfessorRel.jrxml");
		} else if (layout.equals("frequenciaTurmaDisciplinaProfessor")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaTurmaDisciplinaProfessorRel.jrxml");
		} else if (layout.equals("frequenciaTurmaDisciplinaDataHoraProfessor")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaTurmaDisciplinaDataHoraProfessorRel.jrxml");
		} else if (layout.equals("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasComProfessorCoordenadorEstatisticaMatriculaRel.jrxml");
		}
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeRel() + ".jrxml");
	}

	public static String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcelRel() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidadeRel() {
		return ("CronogramaDeAulasRel");
	}

	public static String getIdEntidadeExcelRel() {
		return ("CronogramaDeAulasExcelRel");
	}

	private List<CronogramaDeAulasRelVO> criarObjetoPeriodoDataAulaComHorario(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula, ");
		sqlStr.append(" case when turma.subturma then turma.turmaprincipal || turma.identificadorturma else turma.identificadorturma end ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select horarioturmadia.data as data, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, pessoa.telefoneRes as telefoneProfessor, ");
		sqlStr.append(" pessoa.email as emailProfessor, pessoa.celular as celularProfessor, ");
		sqlStr.append(" salalocalaula.sala as sala, localaula.local as local, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino,  ");
		sqlStr.append(" case when turma.turmaagrupada then (select sum(t.nrmaximomatricula) from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo) else turma.nrmaximomatricula end as vagaturma,  ");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplina, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" periodoLetivo.periodoLetivo as periodoLetivo, gradeCurricular.nome as gradeCurricular, ");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, turma.turmaagrupada, turma.subturma, turmaprincipal.identificadorturma as turmaprincipal ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala ");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" group by horarioturmadia.data, cidade.codigo, cidade.nome, unidadeensino.codigo, unidadeensino.nome, turma.codigo, pessoa.telefoneRes, pessoa.email, pessoa.celular, turma.identificadorturma,  ");
		sqlStr.append(" disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, turma.curso, horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.codigo, ");
		sqlStr.append(" periodoLetivo.periodoLetivo, gradeCurricular.nome, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, salalocalaula.sala, localaula.local, turma.nrmaximomatricula, turmaprincipal.identificadorturma, disciplina.codigo, turmaprincipal.codigo ");
		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		if (ordenacao.equals("data")) {
			sqlStr.append(" ORDER BY data, horarioinicio, unidadeensino, curso, turma.periodoletivo, case when turma.subturma then turma.turmaprincipal || turma.identificadorturma else turma.identificadorturma end ");
		} else if (ordenacao.equals("disciplina")) {
			sqlStr.append("ORDER BY nomeDisciplina, data, nomeUnidadeEnsino, turma.identificadorturma ");
		} else if (ordenacao.equals("turma")) {
			sqlStr.append("ORDER BY turma.identificadorturma, data, nomeDisciplina ");
		} else {
			sqlStr.append(" ORDER BY unidadeensino, curso, turma.periodoletivo, case when turma.subturma then turma.turmaprincipal || turma.identificadorturma else turma.identificadorturma end, data, horarioinicio ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosPeriodoAulaHorario(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> criarObjetoFrequenciaProfessor(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");
		sqlStr.append(" (select distinct unidadeensino.nome as nomeUnidadeEnsino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, pessoa.nome as nomeProfessor, funcionario.matricula as matriculaProfessor, turma.codigo as turma, disciplina.codigo as disciplina, turma.turmaagrupada ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" group by unidadeensino.nome, turma.codigo, turma.turmaagrupada, funcionario.matricula, ");
		sqlStr.append(" pessoa.nome, turma.curso, curso.nome, horarioturma.anovigente, disciplina.codigo, horarioturma.semestrevigente ");
		sqlStr.append(" ) ");

		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select distinct unidadeensino.nome as nomeUnidadeEnsino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, pessoa.nome as nomeProfessor, funcionario.matricula as matriculaProfessor, turma.codigo as turma, disciplina.codigo as disciplina, turma.turmaagrupada ");
		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");

		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		sqlStr.append(" group by unidadeensino.nome, turma.codigo, turma.turmaagrupada, funcionario.matricula, ");
		sqlStr.append(" pessoa.nome, turma.curso, curso.nome, programacaotutoriaonline.ano, disciplina.codigo, programacaotutoriaonline.semestre ");
		sqlStr.append(" ) ");

		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY nomeUnidadeEnsino, curso, ano, semestre, nomeProfessor");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFrequenciaProfessor(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> criarObjetoFrequenciaTurmaDisciplinaProfessor(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");
		sqlStr.append(" (select distinct unidadeensino.nome as nomeUnidadeEnsino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, pessoa.nome as nomeProfessor, disciplina.nome as nomeDisciplina, turma.identificadorturma ,turma.codigo as turma, disciplina.codigo as disciplina, turma.turmaagrupada ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" group by unidadeensino.nome, turma.codigo, turma.turmaagrupada, disciplina.nome, turma.identificadorturma, ");
		sqlStr.append(" pessoa.nome, turma.curso, curso.nome, horarioturma.anovigente, disciplina.codigo, horarioturma.semestrevigente ");
		sqlStr.append(" ) ");

		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select distinct unidadeensino.nome as nomeUnidadeEnsino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, pessoa.nome as nomeProfessor, disciplina.nome as nomeDisciplina, turma.identificadorturma ,turma.codigo as turma, disciplina.codigo as disciplina, turma.turmaagrupada ");
		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");

		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		sqlStr.append(" group by unidadeensino.nome, turma.codigo, turma.turmaagrupada, disciplina.nome, turma.identificadorturma, ");
		sqlStr.append(" pessoa.nome, turma.curso, curso.nome, programacaotutoriaonline.ano, disciplina.codigo, programacaotutoriaonline.semestre ");
		sqlStr.append(" ) ");

		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY nomeUnidadeEnsino, curso, ano, semestre, nomeDisciplina, nomeProfessor, identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFrequenciaTurmaDisciplinaProfessor(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> criarObjetoFrequenciaTurmaDisciplinaDataHoraProfessor(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select distinct horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, horarioturmadia.data, unidadeensino.nome as nomeUnidadeEnsino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join curso as c on c.codigo = t.curso");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso, localaula.local, pessoa.nome as nomeProfessor, ");
		sqlStr.append(" horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, disciplina.nome as nomeDisciplina, turma.identificadorturma, salalocalaula.sala, turma.codigo as turma, disciplina.codigo as disciplina, turma.turmaagrupada ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append(" or (turma.turmaagrupada and periodoLetivo.codigo = (select pl.codigo from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join periodoLetivo pl on pl.codigo = t.periodoletivo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo order by pl.periodoletivo limit 1 ");
		sqlStr.append(" ))) ");
		sqlStr.append(" left join gradeCurricular on turma.turmaagrupada =  false and gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" group by unidadeensino.nome, turma.codigo, turma.turmaagrupada, disciplina.nome, turma.identificadorturma, horarioturmadia.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, ");
		sqlStr.append(" pessoa.nome, turma.curso, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente, salalocalaula.sala, localaula.local, disciplina.codigo ");
		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY ano, semestre, data, nomeUnidadeEnsino, curso, local, nomeProfessor, horarioinicio, nomeDisciplina, identificadorturma, sala");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFrequenciaTurmaDisciplinaDataHoraProfessor(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> criarObjetoHorarioPorSala(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, pessoa.codigo as pessoa, pessoa.nome as nomeProfessor,");
		sqlStr.append(" salalocalaula.sala as sala, localaula.local as local, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, ");
		sqlStr.append(" case when turma.turmaagrupada then (select sum(t.nrmaximomatricula) from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo) else turma.nrmaximomatricula end as vagaturma, turma.turmaagrupada,");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append("  inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo");
		sqlStr.append("  where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente");
		sqlStr.append("  and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplina,");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada");
		sqlStr.append("  inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append("  inner join curso as c on c.codigo = t.curso");
		sqlStr.append("  where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, periodoLetivo.periodoLetivo as periodoLetivo, min(horarioturmadia.data) as data,");
		sqlStr.append(" (SELECT COUNT(matriculaPeriodo.codigo) AS qtde FROM matriculaPeriodo   INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula   ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaPeriodo = matriculaPeriodo.codigo WHERE matricula.situacao = 'AT' ");
		sqlStr.append(" and (matriculaPeriodo.situacaoMatriculaPeriodo = 'PR') AND matriculaPeriodo.turma = horarioturma.turma AND mptd.disciplina = horarioturmadiaitem.disciplina");
		sqlStr.append(" and ((turma.semestral and mptd.ano = horarioturma.anovigente and mptd.semestre = horarioturma.semestrevigente)");
		sqlStr.append(" or (turma.anual and mptd.ano = horarioturma.anovigente) or (turma.semestral = false and turma.anual = false ))) as qtdeAlunoPreMatricula, ");
		sqlStr.append(" (SELECT COUNT(matriculaPeriodo.codigo) AS qtde FROM matriculaPeriodo INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula INNER JOIN curso ON curso.codigo = matricula.curso INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append(" WHERE matricula.situacao = 'AT' and (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') AND matricula.unidadeEnsino = unidadeensino.codigo AND (mptd.turma = turma.codigo or mptd.turmateorica = turma.codigo or mptd.turmapratica = turma.codigo) AND mptd.disciplina = horarioturmadiaitem.disciplina ");
		sqlStr.append("  and ((turma.semestral and mptd.ano = horarioturma.anovigente and mptd.semestre = horarioturma.semestrevigente) ");
		sqlStr.append("  or (turma.anual and mptd.ano = horarioturma.anovigente) or (turma.semestral = false and turma.anual = false )) ");
		sqlStr.append(" ) as qtdAlunoAtivo, ");
		sqlStr.append(" (SELECT COUNT(matriculaPeriodo.codigo) AS qtde FROM matriculaPeriodo INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula INNER JOIN curso ON curso.codigo = matricula.curso INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append(" WHERE matricula.situacao = 'AT' and (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') AND matricula.unidadeEnsino = unidadeensino.codigo AND (mptd.turma = turma.codigo or mptd.turmateorica = turma.codigo or mptd.turmapratica = turma.codigo) AND mptd.disciplina = horarioturmadiaitem.disciplina ");
		sqlStr.append("  and ((turma.semestral and mptd.ano = horarioturma.anovigente and mptd.semestre = horarioturma.semestrevigente) ");
		sqlStr.append("  or (turma.anual and mptd.ano = horarioturma.anovigente) or (turma.semestral = false and turma.anual = false )) and matriculaperiodo.turma != mptd.turma ");
		sqlStr.append(" ) as qtdAlunoReposicao ");
		
		sqlStr.append(" from horarioturma");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo))");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" GROUP BY unidadeensino.codigo, horarioturma.anovigente, horarioturma.semestrevigente, horarioturmadiaitem.disciplina, horarioturma.turma, curso.nome, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, salalocalaula.sala, localaula.local, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, periodoLetivo.periodoLetivo ");
		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY sala, local, curso, nomeDisciplina, horarioinicio, identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosHorarioPorSala(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> criarObjetoHorarioPorCurso(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula,  ");
		
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahorariapratica else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahorariapratica ");
		sqlStr.append(" else gradedisciplinacomposta.cargahorariapratica end end as cargaHorariaPratica,  ");
		
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria - gradecurriculargrupooptativadisciplina.cargahorariapratica ");
		sqlStr.append(" else gradedisciplinacomposta.cargahorariateorica end end as cargaHorariaTeorica ");
		
		sqlStr.append(" from ( ");

		sqlStr.append(" (select turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, pessoa.codigo as pessoa, pessoa.nome as nomeProfessor,");
		sqlStr.append(" salalocalaula.sala as sala, localaula.local as local, ");
		sqlStr.append(" case when turma.turmaagrupada then (select sum(t.nrmaximomatricula) from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo) else turma.nrmaximomatricula end as vagaturma, ");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append("  inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo");
		sqlStr.append("  where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = horarioturma.anovigente and vagaturma.semestre = horarioturma.semestrevigente");
		sqlStr.append("  and vagaturmadisciplina.disciplina = horarioturmadiaitem.disciplina) vagadisciplina,");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada");
		sqlStr.append("  inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append("  inner join curso as c on c.codigo = t.curso");
		sqlStr.append("  where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, periodoLetivo.periodoLetivo as periodoLetivo, min(horarioturmadia.data) as data,");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, 0 as programacaotutoriaonline ");
		sqlStr.append(" from horarioturma");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo))");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, false, usuarioVO));
		sqlStr.append(" GROUP BY unidadeensino.codigo, horarioturma.anovigente, horarioturma.semestrevigente, horarioturmadiaitem.disciplina, horarioturma.turma, curso.nome, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, salalocalaula.sala, localaula.local, periodoLetivo.periodoLetivo, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");
		sqlStr.append(" ) ");

		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, pessoa.codigo as pessoa, pessoa.nome as nomeProfessor,");
		sqlStr.append(" '' as sala, '' as local, ");
		sqlStr.append(" case when turma.turmaagrupada then (select sum(t.nrmaximomatricula) from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo) else turma.nrmaximomatricula end as vagaturma, ");
		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append("  inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo");
		sqlStr.append("  where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre");
		sqlStr.append("  and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplina,");
		sqlStr.append(" case when turma.turmaagrupada then (select array_to_string(array_agg(distinct c.nome order by c.nome), ', ') from turmaagrupada");
		sqlStr.append("  inner join turma as t on t.codigo = turmaagrupada.turma");
		sqlStr.append("  inner join curso as c on c.codigo = t.curso");
		sqlStr.append("  where turmaagrupada.turmaorigem = turma.codigo) else curso.nome end as curso,");
		sqlStr.append(" programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, periodoLetivo.periodoLetivo as periodoLetivo, min(programacaotutoriaonline.datainicioaula) as data,");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, programacaotutoriaonline.codigo as programacaotutoriaonline ");
		sqlStr.append(" from programacaotutoriaonline");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor");
		sqlStr.append(" left join turma as turmaprincipal on turma.turmaprincipal = turmaprincipal.codigo");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on ((turma.turmaagrupada =  false and periodoLetivo.codigo = turma.periodoLetivo))");

		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		sqlStr.append(" GROUP BY unidadeensino.codigo, programacaotutoriaonline.ano, programacaotutoriaonline.semestre, programacaotutoriaonline.disciplina, programacaotutoriaonline.turma, curso.nome, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, periodoLetivo.periodoLetivo, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, programacaotutoriaonline.codigo ");
		sqlStr.append(" ) ");

		sqlStr.append(") as turma");

		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY curso, periodoLetivo, nomeDisciplina, identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosHorarioPorCurso(tabelaResultado, visaoAluno, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> montarDadosPeriodoAulaHorario(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setData(tabelaResultado.getDate("data"));
			carVO.setDataModulo(Uteis.getData(tabelaResultado.getDate("data")));
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			if (!visaoAluno) {
				carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
				carVO.setTelefoneProfessor(tabelaResultado.getString("telefoneProfessor"));
				carVO.setCelularProfessor(tabelaResultado.getString("celularProfessor"));
				carVO.setEmailProfessor(tabelaResultado.getString("emailProfessor"));
			}
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCidade(tabelaResultado.getString("nomeCidade"));
			carVO.setTurma(tabelaResultado.getString("identificadorTurma"));
			carVO.setPeriodo(String.valueOf(tabelaResultado.getInt("periodoLetivo")));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setGradeCurricular(tabelaResultado.getString("gradeCurricular"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setDiaSemana(Uteis.getDiaSemanaEnum(tabelaResultado.getDate("data")).getDescricao());
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setHorarioInicio(tabelaResultado.getString("horarioInicio"));
			carVO.setHorarioTermino(tabelaResultado.getString("horarioTermino"));
			carVO.setNumeroVaga(tabelaResultado.getInt("vagadisciplina")>0?tabelaResultado.getInt("vagadisciplina"):tabelaResultado.getInt("vagaturma"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> montarDadosFrequenciaProfessor(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setMatriculaProfessor(tabelaResultado.getString("matriculaProfessor"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> montarDadosFrequenciaTurmaDisciplinaProfessor(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setTurma(tabelaResultado.getString("identificadorturma"));
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> montarDadosFrequenciaTurmaDisciplinaDataHoraProfessor(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setData(tabelaResultado.getDate("data"));
			carVO.setHorarioInicio(tabelaResultado.getString("horarioInicio"));
			carVO.setHorarioTermino(tabelaResultado.getString("horarioTermino"));
			carVO.setTurma(tabelaResultado.getString("identificadorturma"));
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setDiaSemana(Uteis.getDiaSemanaEnum(tabelaResultado.getDate("data")).getDescricao());
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> montarDadosHorarioPorSala(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setPeriodo(tabelaResultado.getString("periodoLetivo"));
			carVO.setTurma(tabelaResultado.getString("identificadorturma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setDiaSemana(Uteis.getDiaSemanaEnum(tabelaResultado.getDate("data")).getDescricao());
			carVO.setHorarioInicio(tabelaResultado.getString("horarioInicio"));
			carVO.setHorarioTermino(tabelaResultado.getString("horarioTermino"));
			carVO.setQtdeAlunoAtivo(tabelaResultado.getInt("qtdAlunoAtivo"));
			carVO.setQtdeAlunoPreMatricula(tabelaResultado.getInt("qtdeAlunoPreMatricula"));
			carVO.setQtdAlunoReposicao(tabelaResultado.getInt("qtdAlunoReposicao"));
			carVO.setNumeroVaga(tabelaResultado.getInt("vagadisciplina")>0?tabelaResultado.getInt("vagadisciplina"):tabelaResultado.getInt("vagaturma"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> montarDadosHorarioPorCurso(SqlRowSet tabelaResultado, boolean visaoAluno, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setCodigoProfessor(tabelaResultado.getInt("pessoa"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setPeriodo(tabelaResultado.getString("periodoLetivo"));
			carVO.setTurma(tabelaResultado.getString("identificadorturma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setDiaSemana(Uteis.getDiaSemanaEnum(tabelaResultado.getDate("data")).getDescricao());
			carVO.setNumeroVaga(tabelaResultado.getObject("vagadisciplina") != null ?tabelaResultado.getInt("vagadisciplina"):tabelaResultado.getInt("vagaturma"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			carVO.setCodigoTurma(tabelaResultado.getInt("turma"));
			carVO.setCodigoDisciplina(tabelaResultado.getInt("disciplina"));
			carVO.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			carVO.setSubTurma(tabelaResultado.getBoolean("subturma"));
			carVO.setAnual(tabelaResultado.getBoolean("anual"));
			carVO.setSemestral(tabelaResultado.getBoolean("semestral"));
			carVO.setCargaHorariaPratica(tabelaResultado.getInt("cargahorariapratica"));
			carVO.setCargaHorariaTeorica(tabelaResultado.getInt("cargahorariateorica"));
			if(tabelaResultado.getObject("tiposubturma") != null) {
				carVO.setTipoSubTurmaEnum(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tiposubturma")));
			}
			if(tabelaResultado.getInt("programacaotutoriaonline") > 0) {
				carVO.setEad(true);
			}
			else {
				carVO.setEad(false);
			}
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}


	private List<CronogramaDeAulasRelVO> consultarDadosImpressaoEtiqueta(Integer unidadeEnsino, Integer turma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao,  UsuarioVO usuarioVO, PeriodicidadeEnum periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");
		sqlStr.append(" (select distinct  turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.turmaagrupada, ");
		sqlStr.append(" salalocalaula.sala as sala, localaula.local as local, ");
		sqlStr.append(" case when turma.turmaagrupada then turma.abreviaturacurso else curso.nome end curso, unidadeensino.nome as unidadeensino, turno.nome as turno,  gradeCurricular.nome as gradeCurricular, ");
		sqlStr.append(" horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, periodoLetivo.periodoLetivo as periodoLetivo ");
		sqlStr.append(" from horarioturma");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno");
		sqlStr.append(" inner JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner JOIN horarioturmadiaitem ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		//sqlStr.append(" left join salalocalaula on salalocalaula.codigo = horarioturmadiaitem.sala");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo and  horarioturmadiaitem.disciplina  = turmadisciplina.disciplina ");
		sqlStr.append(" left join salalocalaula on ((horarioturmadiaitem.sala is not null and salalocalaula.codigo = horarioturmadiaitem.sala) or (turmadisciplina.salalocalaula is not null and horarioturmadiaitem.sala  is null and salalocalaula.codigo = turmadisciplina.salalocalaula)) ");
		sqlStr.append(" left join localaula on salalocalaula.localaula = localaula.codigo");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo");
		sqlStr.append(" left join gradeCurricular on gradeCurricular.codigo = turma.gradeCurricular ");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, "", curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidade, false, usuarioVO));
		sqlStr.append(" ) ");

		sqlStr.append(" UNION ALL ");

		sqlStr.append(" (select distinct  turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.turmaagrupada, ");
		sqlStr.append(" '' as sala, '' as local, ");
		sqlStr.append(" case when turma.turmaagrupada then turma.abreviaturacurso else curso.nome end curso, unidadeensino.nome as unidadeensino, turno.nome as turno,  gradeCurricular.nome as gradeCurricular, ");
		sqlStr.append(" programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, periodoLetivo.periodoLetivo as periodoLetivo ");
		sqlStr.append(" from programacaotutoriaonline");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo");
		sqlStr.append(" left join gradeCurricular on gradeCurricular.codigo = turma.gradeCurricular ");
		sqlStr.append(getSqlWhere(unidadeEnsino, turma, "", curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidade, true, usuarioVO));
		sqlStr.append(" ) ");

		sqlStr.append(" ) as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		sqlStr.append(" ORDER BY unidadeensino, curso, identificadorTurma, nomeDisciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosImpressaoEtiqueta(tabelaResultado, usuarioVO);
	}

	private List<CronogramaDeAulasRelVO> montarDadosImpressaoEtiqueta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>();
		while (tabelaResultado.next()) {
			CronogramaDeAulasRelVO carVO = new CronogramaDeAulasRelVO();
			carVO.setDisciplina(tabelaResultado.getString("nomeDisciplina"));
			carVO.setProfessor(tabelaResultado.getString("nomeProfessor"));
			carVO.setSala(tabelaResultado.getString("sala"));
			carVO.setLocal(tabelaResultado.getString("local"));
			carVO.setCurso(tabelaResultado.getString("curso"));
			carVO.setPeriodo(Uteis.isAtributoPreenchido(tabelaResultado.getString("periodoLetivo"))?tabelaResultado.getString("periodoLetivo")+"º":"");
			carVO.setTurma(tabelaResultado.getString("identificadorturma"));
			carVO.setAno(tabelaResultado.getString("ano"));
			carVO.setSemestre(tabelaResultado.getString("semestre"));
			carVO.setUnidadeEnsino(tabelaResultado.getString("unidadeEnsino"));
			carVO.setTurno(tabelaResultado.getString("turno"));
			carVO.setGradeCurricular(tabelaResultado.getString("gradeCurricular"));
			carVO.setCargaHorariaDisciplina(tabelaResultado.getInt("cargaHoraria"));
			carVO.setHoraAulaDisciplina(tabelaResultado.getInt("horaAula"));
			carVO.setCodigoBarra(Uteis.preencherComZerosLimitandoTamanho(tabelaResultado.getInt("turma"), 5)+Uteis.preencherComZerosLimitandoTamanho(tabelaResultado.getInt("disciplina"), 6)+
					Uteis.preencherComZerosLimitandoTamanho(tabelaResultado.getInt("pessoa"), 5)+
					Uteis.preencherComZerosLimitandoTamanho(Uteis.isAtributoPreenchido(tabelaResultado.getString("ano"))?Integer.valueOf(tabelaResultado.getString("ano")):0, 4)+
					Uteis.preencherComZerosLimitandoTamanho(Uteis.isAtributoPreenchido(tabelaResultado.getString("semestre"))? Integer.valueOf(tabelaResultado.getString("semestre")):0, 1));
			cronogramaDeAulasRelVOs.add(carVO);
		}
		return cronogramaDeAulasRelVOs;
	}

	@Override
	public String realizarGeracaoImpressaoEtiquetaPorCronogramaAula(LayoutEtiquetaVO layoutEtiqueta,  Integer unidadeEnsino, Integer turma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim,
	String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, UsuarioVO usuarioVO, Integer numeroCopias,
	Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PeriodicidadeEnum periodicidade) throws Exception{
		validarDados(dataInicio, dataFim, periodicidade, ano, semestre);
		if (layoutEtiqueta.getCodigo() == 0) {
			throw new Exception("Informe o LAYOUT de impressão da etiqueta.");
		}
		List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs = consultarDadosImpressaoEtiqueta(unidadeEnsino, turma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, usuarioVO, periodicidade);
		return realizarImpressaoEtiquetaCronogramaAula(layoutEtiqueta, cronogramaDeAulasRelVOs, numeroCopias, linha, coluna, removerEspacoTAGVazia, configuracaoGeralSistemaVO, usuarioVO);
	}

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta.
	 * Tem como passagem por parâmetros a <code>LayoutEtiquetaVO</code> e
	 * <code>OrigemImagemVO</code>
	 */
	@Override
	public String realizarImpressaoEtiquetaCronogramaAula(LayoutEtiquetaVO layoutEtiqueta, List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs,  Integer numeroCopias,
			Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,UsuarioVO usuarioVO ) throws Exception {
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getCodigo() == 0) {
				throw new Exception("Informe o LAYOUT de impressão da etiqueta.");
			}

			if (cronogramaDeAulasRelVOs.isEmpty()) {
				throw new Exception("Não foi encontrado nenhum dado para a geração da etiqueta.");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioVO));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemEtiqueta(layoutEtiqueta, writer, pdf, cronogramaDeAulasRelVOs, numeroCopias, linha, coluna, removerEspacoTAGVazia);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.close();
			}
		}

	}

	private Boolean getUtilizarTagEtiqueta(CronogramaDeAulasRelVO cronogramaDeAulasRelVO, TagEtiquetaEnum tag, String label) {
		switch (tag) {
		case NOME_UNIDADE_ENSINO:
			return !cronogramaDeAulasRelVO.getUnidadeEnsino().isEmpty();
		case NOME_CURSO:
			return !cronogramaDeAulasRelVO.getCurso().trim().isEmpty();
		case NOME_TURNO:
			return !cronogramaDeAulasRelVO.getTurno().trim().isEmpty();
		case TUR_DIS_PROF_COD_BARRA:
			return !cronogramaDeAulasRelVO.getCodigoBarra().trim().isEmpty();
		case GRADE_CURRICULAR:
			return !cronogramaDeAulasRelVO.getGradeCurricular().trim().isEmpty();
		case TURMA:
			return !cronogramaDeAulasRelVO.getTurma().isEmpty();
		case PERIODO_LETIVO:
			return !cronogramaDeAulasRelVO.getPeriodo().trim().isEmpty();
		case DISCIPLINA:
			return !cronogramaDeAulasRelVO.getDisciplina().trim().isEmpty();
		case CARGA_HORARIA_DISC:
			return !(cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().equals("0"));
		case HORA_AULA_DISC:
			return !(cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().equals("0"));
		case ANO:
			return !cronogramaDeAulasRelVO.getAno().trim().isEmpty();
		case SEMESTRE:
			return !cronogramaDeAulasRelVO.getSemestre().trim().isEmpty();
		case PROFESSOR:
			return !cronogramaDeAulasRelVO.getProfessor().trim().isEmpty();
		case SALA:
			return !cronogramaDeAulasRelVO.getSala().trim().isEmpty();
		case LOCAL:
			return !cronogramaDeAulasRelVO.getLocal().trim().isEmpty();
		default:
			return false;
		}
	}

	/**
	 * Método responsável por atualizar a Posicao da tag quando o valor da mesma
	 * estiver em branco
	 *
	 * @param layoutEtiqueta
	 * @param etiquetaLivroRelVOs
	 * @throws Exception
	 */
	public void realizarAtualizacaoPosicaoTagQuandoValorBranco(List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs, CronogramaDeAulasRelVO cronogramaDeAulasRelVO) throws Exception {
		Ordenacao.ordenarLista(listaLayoutEtiquetaTagVOs, "margemTopo");
		HashMap<Integer, LayoutEtiquetaTagVO> mapTagLayoutOriginalVOs = new HashMap<Integer, LayoutEtiquetaTagVO>(0);
		HashMap<Integer, LayoutEtiquetaTagVO> mapMargemTopoLayoutPreenchidoVOs = new HashMap<Integer, LayoutEtiquetaTagVO>(0);
		HashMap<Integer, Integer> mapTagAtualizarValorTopoVOs = new HashMap<Integer, Integer>(0);
		Integer ultimoVazio = null;
		for (LayoutEtiquetaTagVO layoutOriginalTagVO : listaLayoutEtiquetaTagVOs) {

			// Verifica se o valor da etiqueta está preenchido
			if (!getUtilizarTagEtiqueta(cronogramaDeAulasRelVO, layoutOriginalTagVO.getTagEtiqueta(), layoutOriginalTagVO.getLabelTag())) {
				if (!mapMargemTopoLayoutPreenchidoVOs.containsKey(layoutOriginalTagVO.getMargemTopo())) {
					if (ultimoVazio == null || ultimoVazio.equals(layoutOriginalTagVO.getOrdem())) {
						ultimoVazio = layoutOriginalTagVO.getOrdem();
						mapTagLayoutOriginalVOs.put(layoutOriginalTagVO.getOrdem(), layoutOriginalTagVO);
					}
				}
			} else {

				if (!mapTagLayoutOriginalVOs.isEmpty()) {
					for (LayoutEtiquetaTagVO tagValorAtualizar : mapTagLayoutOriginalVOs.values()) {
						LayoutEtiquetaTagVO layoutEtiquetaClonado = (LayoutEtiquetaTagVO) layoutOriginalTagVO.clone();
						mapTagLayoutOriginalVOs.put(layoutEtiquetaClonado.getOrdem(), layoutEtiquetaClonado);

						if (mapTagAtualizarValorTopoVOs.containsKey(layoutOriginalTagVO.getMargemTopo())) {
							layoutOriginalTagVO.setMargemTopo(mapTagAtualizarValorTopoVOs.get(layoutOriginalTagVO.getMargemTopo()));
							mapTagLayoutOriginalVOs.remove(tagValorAtualizar.getOrdem());
							break;
						} else {
							mapTagAtualizarValorTopoVOs.put(layoutOriginalTagVO.getMargemTopo(), tagValorAtualizar.getMargemTopo());
							layoutOriginalTagVO.setMargemTopo(tagValorAtualizar.getMargemTopo());
							mapTagLayoutOriginalVOs.remove(tagValorAtualizar.getOrdem());
							break;
						}
					}
				}
				mapMargemTopoLayoutPreenchidoVOs.put(layoutOriginalTagVO.getMargemTopo(), layoutOriginalTagVO);
			}
		}
	}

	public static final float PONTO = 2.83f;

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta
	 * para o tipo de impressora Laser/Tinta. Tem como passagem por parâmetros a
	 * <code>LayoutEtiquetaVO</code>>
	 */

	public void realizarMontagemEtiqueta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer, Document pdf, List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia) throws Exception {
		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;

		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;

		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;

		PdfContentByte canvas = writer.getDirectContent();

		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();

		for (CronogramaDeAulasRelVO cronogramaDeAulasRelVO: cronogramaDeAulasRelVOs) {

			// Essa regra só será utilizada caso o booleano para remover espaços
			// esteja marcado.
			// A Lista Original precisa ser clonada porque cada etiqueta pode
			// ser de um catalogo diferente
			// podendo ou não conter tags nulas, por isso para cada etiqueta é
			// necessário passar a lista iriginal e realizar
			// a verificação novamente.
			// Carlos 20/02/2014
			List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs = null;
			if (removerEspacoTAGVazia) {
				listaLayoutEtiquetaTagVOs = getFacadeFactory().getLayoutEtiquetaTagFacade().realizarCloneListaOriginalLayout(layoutEtiqueta.getLayoutEtiquetaTagVOs());
				realizarAtualizacaoPosicaoTagQuandoValorBranco(listaLayoutEtiquetaTagVOs, cronogramaDeAulasRelVO);
				Ordenacao.ordenarLista(listaLayoutEtiquetaTagVOs, "ordem");
			} else {
				listaLayoutEtiquetaTagVOs = layoutEtiqueta.getLayoutEtiquetaTagVOs();
			}
			for (int copia = 1; copia <= numeroCopias; copia++) {

				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);

				for (LayoutEtiquetaTagVO tag : listaLayoutEtiquetaTagVOs) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;

					PdfTemplate tmp = null;
					if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.TUR_DIS_PROF_COD_BARRA)) {
						tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
						tmp.beginText();
						Barcode128 barcode128 = new Barcode128();
						barcode128.setCodeType(Barcode128.CODE128);
						barcode128.setCode(getValorImprimirEtiqueta(cronogramaDeAulasRelVO, tag.getTagEtiqueta(), "", tag.getApresentarLabelEtiquetaAposTagEtiqueta()));
						barcode128.setBarHeight(tag.getAlturaCodigoBarra());
						barcode128.setFont(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true));
						barcode128.setSize(tag.getTamanhoFonte().floatValue());
						if (!tag.getImprimirNumeroAbaixo()) {
							barcode128.setAltText("");
							barcode128.setStartStopText(tag.getImprimirNumeroAbaixo());
						}
						tmp.addTemplate(barcode128.createTemplateWithBarcode(canvas, null, null), margemEsquerdaLabel, 0f);
						tmp.endText();
						canvas.addTemplate(tmp, margemEsquerdaColuna, (margemSuperiorColuna - (margemSuperiorLabel)));

					} else {
						tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
						tmp.beginText();
						tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
						tmp.showTextAligned(Element.ALIGN_LEFT, (getValorImprimirEtiqueta(cronogramaDeAulasRelVO, tag.getTagEtiqueta(), tag.getLabelTag(), tag.getApresentarLabelEtiquetaAposTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
						tmp.endText();
						canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
					}

				}
				if (coluna + 1 > colunas) {
					coluna = 1;
					if (linha + 1 > linhas) {
						pdf.newPage();
						linha = 1;
					} else {
						linha++;
					}
				} else {
					coluna++;
				}
			}
		}
	}

	private String getValorImprimirEtiqueta(CronogramaDeAulasRelVO cronogramaDeAulasRelVO, TagEtiquetaEnum tag, String label, Boolean apresentarLabelEtiquetaAposTagEtiqueta) {
		switch (tag) {
		case NOME_UNIDADE_ENSINO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getUnidadeEnsino().isEmpty() ? "" : cronogramaDeAulasRelVO.getUnidadeEnsino().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getUnidadeEnsino().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getUnidadeEnsino().trim();
			}
		case NOME_CURSO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getCurso().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getCurso().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getCurso().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getCurso().trim();
			}
		case NOME_TURNO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getTurno().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getTurno().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getTurno().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getTurno().trim();
			}
		case TUR_DIS_PROF_COD_BARRA:
			return cronogramaDeAulasRelVO.getCodigoBarra().trim();
		case GRADE_CURRICULAR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getGradeCurricular().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getGradeCurricular().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getGradeCurricular().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getGradeCurricular().trim();
			}
		case TURMA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getTurma().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getTurma().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getTurma().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getTurma().trim();
			}
		case PERIODO_LETIVO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getPeriodo().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getPeriodo().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getPeriodo().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getPeriodo().trim();
			}
		case DISCIPLINA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getDisciplina().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getDisciplina().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getDisciplina().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getDisciplina().trim();
			}
		case CARGA_HORARIA_DISC:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().equals("0") ? "" : cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim().equals("0") ? "" : label + " " + cronogramaDeAulasRelVO.getCargaHorariaDisciplina().toString().trim();
			}
		case HORA_AULA_DISC:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().equals("0") ? "" : cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().isEmpty() || cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim().equals("0") ? "" : label + " " + cronogramaDeAulasRelVO.getHoraAulaDisciplina().toString().trim();
			}
		case ANO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getAno().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getAno().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getAno().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getAno().trim();
			}
		case SEMESTRE:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getSemestre().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getSemestre().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getSemestre().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getSemestre().trim();
			}
		case PROFESSOR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getProfessor().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getProfessor().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getProfessor().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getProfessor().trim();
			}
		case SALA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getSala().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getSala().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getSala().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getSala().trim();
			}
		case LOCAL:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return cronogramaDeAulasRelVO.getLocal().trim().isEmpty() ? "" : cronogramaDeAulasRelVO.getLocal().trim() + " " + label;
			} else {
				return cronogramaDeAulasRelVO.getLocal().trim().isEmpty() ? "" : label + " " + cronogramaDeAulasRelVO.getLocal().trim();
			}
		default:
			return "";
		}
	}

	public void consultarDadosEstatisticoMatricula(List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs, boolean trazerAlunosAtivos, boolean trazerAlunosReposicao, boolean trazerAlunosPreMatriculados, boolean trazerMediaNotas) throws Exception {
		if(trazerAlunosAtivos || trazerAlunosPreMatriculados || trazerAlunosReposicao || trazerMediaNotas) {
			Map<String, Map<String, Integer>> mapEstatisticaMatricula = new HashMap<String, Map<String, Integer>>(0);
			StringBuilder sqlStr = new StringBuilder("");
			for(CronogramaDeAulasRelVO cronogramaDeAulasRelVO: cronogramaDeAulasRelVOs) {
				StringBuilder key = new StringBuilder("T");
				key.append(cronogramaDeAulasRelVO.getCodigoTurma()).append("D").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append("A").append(cronogramaDeAulasRelVO.getAno()).append("S").append(cronogramaDeAulasRelVO.getSemestre()).append("P").append(cronogramaDeAulasRelVO.getCodigoProfessor());
				if(!mapEstatisticaMatricula.containsKey(key.toString())) {
					if(sqlStr.length() > 0) {
						sqlStr.append(" union all ");
					}
					sqlStr.append(" SELECT '").append(key.toString()).append("' as key, ");
					sqlStr.append(" sum(case when (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') then 1 else 0 end) AS qtdeAtivo, ");
					sqlStr.append(" sum(case when (matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' and matricula.situacao = 'AT') then 1 else 0 end) AS qtdePreMatriculado, ");
					sqlStr.append(" sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" and matriculaperiodo.turma != matriculaPeriodoTurmaDisciplina.turma) then 1 else 0 end) AS qtdeReposicao, ");
//					alteracao inicia aqui
							sqlStr.append("     sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' or matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
							sqlStr.append("     and matriculaperiodo.turma = matriculaPeriodoTurmaDisciplina.turma) then 1 else 0 end) as qtdeAtivosSemReposicao, ");
//					alteracao finaliza aqui	
				    sqlStr.append(" sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" and historico.mediafinal is not null) then 1 else 0 end) AS qtdeMediaFinal ");
					sqlStr.append(" FROM matriculaPeriodo ");
					sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula   ");
					sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina ON matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo  ");
					if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.PRATICA)) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.turmapratica = ").append(cronogramaDeAulasRelVO.getCodigoTurma());
					}else if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.TEORICA)) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.turmateorica = ").append(cronogramaDeAulasRelVO.getCodigoTurma());
					}else if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.GERAL)) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.turma = ").append(cronogramaDeAulasRelVO.getCodigoTurma());;
					}else if(!cronogramaDeAulasRelVO.getSubTurma() && !cronogramaDeAulasRelVO.getTurmaAgrupada()) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.turma =").append(cronogramaDeAulasRelVO.getCodigoTurma());;
					}else if(cronogramaDeAulasRelVO.getTurmaAgrupada() && !cronogramaDeAulasRelVO.getSubTurma()) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.turma in ( select ta.turma from turmaagrupada ta where ta.turmaorigem = ").append(cronogramaDeAulasRelVO.getCodigoTurma()).append(") ");
					}
					sqlStr.append(" INNER JOIN historico ON historico.matricula = matricula.matricula and historico.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina.codigo  ");
					sqlStr.append(" WHERE ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" or (matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' and matricula.situacao = 'AT') )");
					if(cronogramaDeAulasRelVO.getEad()) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.professor = ").append(cronogramaDeAulasRelVO.getCodigoProfessor());
					}
					if(cronogramaDeAulasRelVO.getTurmaAgrupada()) {
						sqlStr.append(" and exists ");
						sqlStr.append(" (SELECT disciplinaequivalente.equivalente FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplinaequivalente.equivalente ");
						sqlStr.append(" UNION ALL  ");
						sqlStr.append(" SELECT disciplinaequivalente.disciplina FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplinaequivalente.disciplina ");
						sqlStr.append(" UNION ALL  ");
						sqlStr.append(" SELECT disciplina.codigo FROM disciplina WHERE disciplina.codigo = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplina.codigo ");
						sqlStr.append(" ) ");
					}else {
						sqlStr.append(" AND historico.disciplina = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina());
					}
					if(cronogramaDeAulasRelVO.getAnual() || cronogramaDeAulasRelVO.getSemestral()) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.ano = '").append(cronogramaDeAulasRelVO.getAno()).append("' ");
					}
					if(cronogramaDeAulasRelVO.getSemestral()) {
						sqlStr.append(" and matriculaPeriodoTurmaDisciplina.semestre = '").append(cronogramaDeAulasRelVO.getSemestre()).append("' ");
					}
					sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
					mapEstatisticaMatricula.put(key.toString(), new HashMap<String, Integer>(0));
				}
			}

			if(!sqlStr.toString().isEmpty()){
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				while(rs.next()) {
					Map<String, Integer> mapResultado = new HashMap<String, Integer>(0);
					mapResultado.put("ATIVO", rs.getInt("qtdeAtivo"));
					mapResultado.put("REPOSICAO", rs.getInt("qtdeReposicao"));
					mapResultado.put("PRE_MATRICULA", rs.getInt("qtdePreMatriculado"));
					mapResultado.put("MEDIA_NOTAS", rs.getInt("qtdeMediaFinal"));
					mapResultado.put("ATIVOS_SEM_REPOSICAO", rs.getInt("qtdeAtivosSemReposicao"));
					mapEstatisticaMatricula.get(rs.getString("key")).putAll(mapResultado);
				}

				for(CronogramaDeAulasRelVO cronogramaDeAulasRelVO: cronogramaDeAulasRelVOs) {
					StringBuilder key = new StringBuilder("T");
					key.append(cronogramaDeAulasRelVO.getCodigoTurma()).append("D").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append("A").append(cronogramaDeAulasRelVO.getAno()).append("S").append(cronogramaDeAulasRelVO.getSemestre()).append("P").append(cronogramaDeAulasRelVO.getCodigoProfessor());
					if(mapEstatisticaMatricula.containsKey(key.toString())) {
						if(mapEstatisticaMatricula.get(key.toString()).containsKey("ATIVO")){
							cronogramaDeAulasRelVO.setQtdeAlunoAtivo(mapEstatisticaMatricula.get(key.toString()).get("ATIVO"));
						}
						if(mapEstatisticaMatricula.get(key.toString()).containsKey("REPOSICAO")){
							cronogramaDeAulasRelVO.setQtdAlunoReposicao(mapEstatisticaMatricula.get(key.toString()).get("REPOSICAO"));
						}
						if(mapEstatisticaMatricula.get(key.toString()).containsKey("PRE_MATRICULA")){
							cronogramaDeAulasRelVO.setQtdeAlunoPreMatricula(mapEstatisticaMatricula.get(key.toString()).get("PRE_MATRICULA"));
						}
						if(mapEstatisticaMatricula.get(key.toString()).containsKey("MEDIA_NOTAS")){
							cronogramaDeAulasRelVO.setQtdeMediaCalculada(mapEstatisticaMatricula.get(key.toString()).get("MEDIA_NOTAS"));
						}
						if(mapEstatisticaMatricula.get(key.toString()).containsKey("ATIVOS_SEM_REPOSICAO")) {
							cronogramaDeAulasRelVO.setQtdeAtivosSemReposicao(mapEstatisticaMatricula.get(key.toString()).get("ATIVOS_SEM_REPOSICAO"));
						}
					}
				}
			}
		}
	}

	@Override
	public List<CronogramaDeAulasRelVO> criarObjetoProgramacaoTutoriaOnline(Integer unidadeEnsino, Integer curso, Integer turma, String tipoTurma, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String tipoLayout, boolean visaoAluno, UsuarioVO usuario, String ordenacao, PeriodicidadeEnum periodicidadeEnum) throws Exception {
			List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs =  criarObjetoPeriodoAulaProgramacaoTutoriaOnline(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, ordenacao, visaoAluno, usuario, periodicidadeEnum);
			consultarDadosEstatisticoMatricula(cronogramaDeAulasRelVOs, true, true, true, true);
			return cronogramaDeAulasRelVOs;
	}

	private List<CronogramaDeAulasRelVO> criarObjetoPeriodoAulaProgramacaoTutoriaOnline(Integer unidadeEnsino, Integer turma, String tipoTurma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, boolean visaoAluno, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select turma.*, ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sqlStr.append(" else gradedisciplinacomposta.cargahoraria end end as cargaHoraria,  ");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.horaaula else  ");
		sqlStr.append(" case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
		sqlStr.append(" else gradedisciplinacomposta.horaaula end end as horaAula ");
		sqlStr.append(" from ( ");

		sqlStr.append(" select programacaotutoriaonline.datainicioaula as dataInicio, programacaotutoriaonline.dataterminoaula as dataFim, cidade.codigo as cidade, cidade.nome as nomeCidade, unidadeensino.codigo as unidadeensino, ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append(" pessoa.codigo as pessoa, pessoa.nome as nomeProfessor, turma.curso as curso, curso.nome as nomeCurso,  pessoa.telefoneRes as telefoneProfessor, ");
		sqlStr.append(" pessoa.email as emailProfessor, pessoa.celular as celularProfessor, turma.codigo as codigoturma, disciplina.codigo as codigodisciplina, programacaotutoriaonline.ano as ano, programacaotutoriaonline.semestre as semestre, ");
		sqlStr.append(" (select count(*) as qtdeMaterialPostado from ( select distinct j.qtde from (    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina ");
		sqlStr.append(" AND professor = pessoa.codigo AND turma is null  union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" and professor is null union all SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor = pessoa.codigo AND turma = programacaotutoriaonline.turma  ");
		sqlStr.append(" union all    SELECT arquivo.codigo AS qtde FROM arquivo WHERE disciplina = programacaotutoriaonline.disciplina AND professor is null AND turma is null ) as j) as t  ) as qtdeMaterialPostado, ");
		sqlStr.append(" turno.codigo as turno, turno.nome as nomeTurno, coordenador.nome as nomeCoordenador, coordenador.email as emailCoordenador, disciplina.descricaoComplementar as descricaoComplementarDisciplina, ");
		sqlStr.append(" periodoletivo.periodoletivo, turma.nrVagas, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma, ");

		sqlStr.append(" (select sum(vagaturmadisciplina.nrvagasmatricula) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplina, ");


		sqlStr.append(" (select sum(vagaturmadisciplina.nrVagasMatriculaReposicao) from vagaturma ");
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where ((turma.turmaagrupada= false and vagaturma.turma = turma.codigo) or (turma.turmaagrupada and vagaturma.turma in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ))) and vagaturma.ano = programacaotutoriaonline.ano and vagaturma.semestre = programacaotutoriaonline.semestre ");
		sqlStr.append(" and vagaturmadisciplina.disciplina = programacaotutoriaonline.disciplina) vagadisciplinareposicao, ");

		sqlStr.append(" ((select count(distinct disciplina) from programacaotutoriaonline pto ");
		sqlStr.append(" inner join disciplina dis on pto.disciplina = dis.codigo ");
		sqlStr.append(" where pto.codigo = programacaotutoriaonline.codigo and dis.codigo != disciplina.codigo and pto.dataterminoaula < programacaotutoriaonline.dataterminoaula) + 1) as numeroDisciplina, ");


		sqlStr.append(" (select numeromodulo from (select row_number() OVER (ORDER BY min(pto.datainicioaula)) AS numeromodulo, pto.disciplina from programacaotutoriaonline pto");
		sqlStr.append(" where pto.disciplina is not null ");
		sqlStr.append(" group by  pto.disciplina) as ordem where ordem.disciplina = programacaotutoriaonline.disciplina) as numeromodulo, programacaotutoriaonline.codigo as programacaotutoriaonline ");

		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" inner JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno ");
		sqlStr.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino  ");
		sqlStr.append(" and cursocoordenador.codigo = (select cc.codigo from cursocoordenador as cc where cc.curso = curso.codigo and (unidadeensino.codigo = cc.unidadeensino or cc.unidadeensino is null) and (cc.turma = turma.codigo or cc.turma is null) order by case when cc.unidadeensino is not null then 0 else 1 end, case when cc.turma is not null then 0 else 1 end limit 1 ) ");
		sqlStr.append(" left join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
		sqlStr.append(" left join pessoa as coordenador on funcionario.pessoa = coordenador.codigo  ");


		sqlStr.append(getSqlWhere(unidadeEnsino, turma, tipoTurma, curso, disciplina, dataInicio, dataFim, ano, semestre, funcionario, turno, salaLocalAula, localAula, periodicidadeEnum, true, usuarioVO));

		/*sqlStr.append(" group by cidade.codigo, cidade.nome, unidadeensino.codigo, unidadeensino.nome, turma.codigo, pessoa.telefoneRes, pessoa.email, pessoa.celular, turma.identificadorturma,  ");
		sqlStr.append(" disciplina.codigo, disciplina.nome, pessoa.codigo, pessoa.nome, turma.curso, horarioturma.turma, horarioturmadiaitem.disciplina, curso.nome, horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.codigo,  ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");*/

		sqlStr.append(" group by cidade.codigo , cidade.nome, unidadeensino.codigo , unidadeensino.nome, turma.codigo , pessoa.telefoneRes, pessoa.email , pessoa.celular, turma.identificadorturma, disciplina.codigo , disciplina.nome, pessoa.codigo , pessoa.nome, turma.curso , programacaotutoriaonline.turma, programacaotutoriaonline.disciplina, curso.nome, programacaotutoriaonline.ano, programacaotutoriaonline.semestre,  ");
		sqlStr.append(" programacaotutoriaonline.codigo, turno.codigo, turno.nome, curso.nome, coordenador.nome, coordenador.email, periodoletivo.periodoletivo, turma.codigo, disciplina.codigo, ");
		sqlStr.append(" turma.anual, turma.semestral, turma.turmaagrupada,  turma.subturma, turma.tiposubturma ");

		sqlStr.append(") as turma");
		sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.turma and turmadisciplina.disciplina = turma.disciplina");
		sqlStr.append(" 		 left join gradedisciplina on ((turma.turmaagrupada = true and gradedisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradedisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradedisciplina = gradedisciplina.codigo))");
		sqlStr.append(" 		 left join gradecurriculargrupooptativadisciplina on gradedisciplina is null and ");
		sqlStr.append(" 		         ((turma.turmaagrupada = true and gradecurriculargrupooptativadisciplina.codigo = ");
		sqlStr.append(" 			(select max(td.gradecurriculargrupooptativadisciplina) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and td.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo))");
		sqlStr.append(" 		left join gradedisciplinacomposta on gradedisciplina.codigo is null and  gradecurriculargrupooptativadisciplina.codigo is null and");
		sqlStr.append(" 		        ((turma.turmaagrupada = true and gradedisciplinacomposta.codigo = ");
		sqlStr.append(" 			(select max(gdc.codigo) from turmaagrupada");
		sqlStr.append(" 			inner join turmadisciplina as td on td.turma = turmaagrupada.turma		");
		sqlStr.append(" 			inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			where turmaagrupada.turmaorigem = turma.turma");
		sqlStr.append(" 			and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )) or (gradedisciplinacomposta.codigo = (");
		sqlStr.append(" 			   select gdc.codigo from turmadisciplina td");
		sqlStr.append(" 			   inner join gradedisciplinacomposta gdc on ((gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina) or (gdc.gradedisciplina = td.gradedisciplina))");
		sqlStr.append(" 			   where td.turma = turma.turma and gdc.disciplina = turma.disciplina");
		sqlStr.append(" 			 )))");
		if (ordenacao.equals("data")) {
			sqlStr.append("ORDER BY dataInicio, nomeUnidadeEnsino, identificadorturma, nomeDisciplina ");
		} else if (ordenacao.equals("disciplina")) {
			sqlStr.append("ORDER BY nomeDisciplina, dataInicio, nomeUnidadeEnsino, identificadorturma ");
		} else if (ordenacao.equals("turma")) {
			sqlStr.append("ORDER BY identificadorturma, dataInicio, nomeDisciplina ");
		} else {
			sqlStr.append("ORDER BY nomeUnidadeEnsino, dataInicio, identificadorturma, nomeDisciplina ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosPeriodoAulaTutoriaOnline(tabelaResultado, visaoAluno, dataInicio, dataFim, usuarioVO);
	}


	public void consultarDadosEstatisticoMatriculaComParalelismo(List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs, boolean trazerAlunosAtivos, boolean trazerAlunosReposicao, boolean trazerAlunosPreMatriculados, boolean trazerMediaNotas) throws Exception {

		if(trazerAlunosAtivos || trazerAlunosPreMatriculados || trazerAlunosReposicao || trazerMediaNotas) {
			final List<String> sqls = new ArrayList<String>();
			final Map<String, Map<String, Integer>> mapEstatisticaMatricula = new HashMap<String, Map<String, Integer>>(0);
			StringBuilder sqlStr = new StringBuilder("");
			for(CronogramaDeAulasRelVO cronogramaDeAulasRelVO: cronogramaDeAulasRelVOs) {
				StringBuilder key = new StringBuilder("T");
				key.append(cronogramaDeAulasRelVO.getCodigoTurma()).append("D").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append("A").append(cronogramaDeAulasRelVO.getAno()).append("S").append(cronogramaDeAulasRelVO.getSemestre());
				if(!mapEstatisticaMatricula.containsKey(key.toString())) {
					sqlStr.append(" SELECT '").append(key.toString()).append("' as key, ");
					sqlStr.append(" 	sum(case when (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') then 1 else 0 end) AS qtdeAtivo, ");
					sqlStr.append(" 	sum(case when (matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' and matricula.situacao = 'AT') then 1 else 0 end) AS qtdePreMatriculado, ");
					sqlStr.append(" 	sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" 	and matriculaperiodo.turma != matriculaPeriodoTurmaDisciplina.turma) then 1 else 0 end) AS qtdeReposicao, ");
		//			alteracao inicia aqui
					sqlStr.append("     sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' or matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append("     and matriculaperiodo.turma = matriculaPeriodoTurmaDisciplina.turma) then 1 else 0 end) as qtdeAtivosSemReposicao, ");
		//			alteracao finaliza aqui	
					sqlStr.append(" 	sum(case when ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" 	and historico.mediafinal is not null) then 1 else 0 end) AS qtdeMediaFinal ");
					sqlStr.append(" FROM matriculaPeriodo ");
					sqlStr.append(" INNER JOIN matricula 						ON matricula.matricula = matriculaPeriodo.matricula ");
					sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina  ON matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo ");
					if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.PRATICA)) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.turmapratica = ").append(cronogramaDeAulasRelVO.getCodigoTurma());
					}else if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.TEORICA)) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.turmateorica = ").append(cronogramaDeAulasRelVO.getCodigoTurma());
					}else if(cronogramaDeAulasRelVO.getSubTurma() && cronogramaDeAulasRelVO.getTipoSubTurmaEnum().equals(TipoSubTurmaEnum.GERAL)) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.turma = ").append(cronogramaDeAulasRelVO.getCodigoTurma());;
					}else if(!cronogramaDeAulasRelVO.getSubTurma() && !cronogramaDeAulasRelVO.getTurmaAgrupada()) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.turma =").append(cronogramaDeAulasRelVO.getCodigoTurma());;
					}else if(cronogramaDeAulasRelVO.getTurmaAgrupada() && !cronogramaDeAulasRelVO.getSubTurma()) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.turma IN ( select ta.turma from turmaagrupada ta where ta.turmaorigem = ").append(cronogramaDeAulasRelVO.getCodigoTurma()).append(") ");
					}
					sqlStr.append(" INNER JOIN historico ON historico.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina.codigo ");
					sqlStr.append(" WHERE ((matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'FI') ");
					sqlStr.append(" or (matriculaPeriodo.situacaoMatriculaPeriodo = 'PR' and matricula.situacao = 'AT') )");
					if(cronogramaDeAulasRelVO.getTurmaAgrupada()) {
						sqlStr.append(" and exists ");
						sqlStr.append(" (SELECT disciplinaequivalente.equivalente FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplinaequivalente.equivalente ");
						sqlStr.append(" UNION ALL ");
						sqlStr.append(" SELECT disciplinaequivalente.disciplina FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplinaequivalente.disciplina ");
						sqlStr.append(" UNION ALL ");
						sqlStr.append(" SELECT disciplina.codigo FROM disciplina WHERE disciplina.codigo = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append(" and historico.disciplina = disciplina.codigo ");
						sqlStr.append(" ) ");
					}else {
						sqlStr.append(" AND historico.disciplina = ").append(cronogramaDeAulasRelVO.getCodigoDisciplina());
					}
					if(cronogramaDeAulasRelVO.getAnual() || cronogramaDeAulasRelVO.getSemestral()) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.ano = '").append(cronogramaDeAulasRelVO.getAno()).append("' ");
					}
					if(cronogramaDeAulasRelVO.getSemestral()) {
						sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.semestre = '").append(cronogramaDeAulasRelVO.getSemestre()).append("' ");
					}
					sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" AND "));
					sqls.add(sqlStr.toString());
					sqlStr.setLength(0);
					mapEstatisticaMatricula.put(key.toString(), new HashMap<String, Integer>(0));
				}
			}


			ConsistirException consistirException = new ConsistirException();
			ProcessarParalelismo.executar(0, sqls.size(), consistirException, new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
 						SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqls.get(i));
 						if (rs.next()) {
							Map<String, Integer> mapResultado = new HashMap<String, Integer>(0);
							mapResultado.put("ATIVO", rs.getInt("qtdeAtivo"));
							mapResultado.put("REPOSICAO", rs.getInt("qtdeReposicao"));
							mapResultado.put("PRE_MATRICULA", rs.getInt("qtdePreMatriculado"));
							mapResultado.put("MEDIA_NOTAS", rs.getInt("qtdeMediaFinal"));
							mapResultado.put("ATIVOS_SEM_REPOSICAO", rs.getInt("qtdeAtivosSemReposicao"));
							mapEstatisticaMatricula.get(rs.getString("key")).putAll(mapResultado);
						}
			  }
		  });

		for (CronogramaDeAulasRelVO cronogramaDeAulasRelVO : cronogramaDeAulasRelVOs) {
				StringBuilder key = new StringBuilder("T");
				key.append(cronogramaDeAulasRelVO.getCodigoTurma()).append("D").append(cronogramaDeAulasRelVO.getCodigoDisciplina()).append("A").append(cronogramaDeAulasRelVO.getAno()).append("S").append(cronogramaDeAulasRelVO.getSemestre());
				if (mapEstatisticaMatricula.containsKey(key.toString())) {
					if (mapEstatisticaMatricula.get(key.toString()).containsKey("ATIVO")) {
						cronogramaDeAulasRelVO.setQtdeAlunoAtivo(mapEstatisticaMatricula.get(key.toString()).get("ATIVO"));
					}
					if (mapEstatisticaMatricula.get(key.toString()).containsKey("REPOSICAO")) {
						cronogramaDeAulasRelVO.setQtdAlunoReposicao(mapEstatisticaMatricula.get(key.toString()).get("REPOSICAO"));
					}
					if (mapEstatisticaMatricula.get(key.toString()).containsKey("PRE_MATRICULA")) {
						cronogramaDeAulasRelVO.setQtdeAlunoPreMatricula(
								mapEstatisticaMatricula.get(key.toString()).get("PRE_MATRICULA"));
					}
					if (mapEstatisticaMatricula.get(key.toString()).containsKey("MEDIA_NOTAS")) {
						cronogramaDeAulasRelVO.setQtdeMediaCalculada(mapEstatisticaMatricula.get(key.toString()).get("MEDIA_NOTAS"));
					}
					if(mapEstatisticaMatricula.get(key.toString()).containsKey("ATIVOS_SEM_REPOSICAO")) {
						cronogramaDeAulasRelVO.setQtdeAtivosSemReposicao(mapEstatisticaMatricula.get(key.toString()).get("ATIVOS_SEM_REPOSICAO"));
					}
				}
		 }
    }
   }

}