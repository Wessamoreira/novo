package negocio.facade.jdbc.blackboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import jobs.ThreadHistoricoNotaBlackboardApurar;
import jobs.enumeradores.TipoUsoNotaEnum;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.OperacaoImportacaoSalaBlackboardEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardGrupoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardRelatorioExcelVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.SugestaoFacilitadorBlackboardVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoDisciplinaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.blackboard.SalaAulaBlackboardInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.boletoonline.itau.comuns.TokenVO;

@Repository
@Scope("singleton")
@Lazy
public class SalaAulaBlackboard  extends ControleAcesso implements SalaAulaBlackboardInterfaceFacade{

	/**
	 *
	 */
	private static final long serialVersionUID = 2136759888071979078L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public StringBuilder getSqlConsultaDeVerificacaoSeMatriculaAptaEstagio(String ano, String semestre) {
		StringBuilder sql = new StringBuilder("select * from ( ");
		
		sql.append(" 	select  ");
		sql.append(" 	matricula.matricula,  ");
		sql.append(" 	pessoa.codigo as codigo_pessoa, ");
		sql.append(" 	pessoa.nome as nome_pessoa, ");
		sql.append(" 	curso.codigo as codigo_curso, ");
		sql.append(" 	curso.nome as nome_curso, ");
		sql.append(" 	curso.abreviatura as abreviatura_curso, ");
		sql.append(" 	curso.periodicidade, ");
		sql.append(" 	matriculaperiodo.codigo as codigo_matriculaperiodo, ");
		sql.append(" 	matriculaperiodo.ano, ");
		sql.append(" 	matriculaperiodo.semestre,  ");
		sql.append(" 	coalesce(gradecurricular.percentualpermitiriniciarestagio,0) as percentualpermitiriniciarestagio,  ");
		sql.append(" 	gradecurricular.cargahoraria as totalCargaHoraria,   ");
		sql.append("    gradecurricular.totalCargaHorariaEstagio ");		
		sql.append(" 	from matricula 	 ");
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sql.append("    inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			sql.append("    and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}else {
			sql.append("    inner join lateral ( ");
			sql.append("	 	 select mp.* from matriculaPeriodo mp where mp.matricula = matricula.matricula ");  
			sql.append("			order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ");
			sql.append("	) as matriculaPeriodo on 1=1 ");	
		}
		sql.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sql.append(" 	inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" 	inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" 	inner join matriculaintegralizacaocurricular(matricula.matricula) as integralizacao on integralizacao.percentualcumpridoliberarestagio >= 100 ");
		sql.append(" 	where matricula.situacao = 'AT' ");		
		sql.append(" 	and matriculaperiodo.situacaomatriculaperiodo in ('AT','FI') ");
		sql.append("    and gradecurricular.percentualPermitirIniciarEstagio > 0  ");
		sql.append(" 	and not exists (SELECT salaaulablackboardpessoa.codigo  FROM salaaulablackboardpessoa   ");
		sql.append(" 				  inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'ESTAGIO'  ");
		sql.append(" 				  WHERE salaaulablackboardpessoa.matricula = matricula.matricula   ");
		sql.append(" 				  and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
		sql.append(" 				  and salaaulablackboard.curso = matricula.curso) ");
		sql.append(" 	AND CASE WHEN integralizacao.cargahorariaestagioexigido > 0 THEN COALESCE(integralizacao.cargahorariaestagiocumprido, 0) < integralizacao.cargahorariaestagioexigido ELSE TRUE END ");		
		sql.append(" 	group by 	matricula.matricula,  ");		
		sql.append(" 				pessoa.codigo, ");
		sql.append(" 				pessoa.nome, ");
		sql.append(" 				curso.codigo, ");
		sql.append(" 				curso.nome, ");
		sql.append(" 				curso.abreviatura, ");
		sql.append(" 				curso.periodicidade, ");
		sql.append(" 				matriculaperiodo.codigo, ");
		sql.append(" 				matriculaperiodo.ano, ");
		sql.append(" 				matriculaperiodo.semestre,  ");
		sql.append(" 				gradecurricular.cargahoraria, ");
		sql.append(" 				gradecurricular.percentualpermitiriniciarestagio, ");
		sql.append(" 				gradecurricular.totalCargaHorariaEstagio, ");
		sql.append(" 				integralizacao.cargahorariaestagioexigido, ");
		sql.append(" 				integralizacao.cargahorariaestagiocumprido ");
		sql.append(" ) as t where 1 = 1 ");
		
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public StringBuilder getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(String ano, String semestre, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, boolean isAlunoNaoEnsalado) {
		StringBuilder sql = new StringBuilder(" select * from ( ");		
		sql.append(" 	select  ");
		sql.append(" 	matricula.matricula,  ");
		sql.append(" 	matricula.gradecurricularatual,  ");
		sql.append(" 	unidadeensino.nome as unidadeensino_nome, ");
		sql.append(" 	pessoa.codigo as codigo_pessoa, ");
		sql.append(" 	pessoa.nome as nome_pessoa, ");
		sql.append(" 	curso.codigo as codigo_curso, ");
		sql.append(" 	curso.nome as nome_curso, ");
		sql.append(" 	curso.abreviatura as abreviatura_curso, ");
		sql.append(" 	curso.periodicidade, ");
		sql.append(" 	matriculaperiodo.codigo as codigo_matriculaperiodo, ");
		sql.append(" 	matriculaperiodo.ano, ");
		sql.append(" 	matriculaperiodo.semestre,  ");
		sql.append(" 	coalesce(gradecurricular.percentualPermitirIniciarTcc,0) as percentualPermitirIniciarTcc,  ");
		sql.append(" 	gradecurricular.disciplinapadraotcc as gc_disciplinapadraotcc  ");		
		sql.append(" 	from matricula 	 ");
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sql.append("    inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			sql.append("    and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}else {
			sql.append("    inner join lateral ( ");
			sql.append("	 	 select mp.* from matriculaPeriodo mp where mp.matricula = matricula.matricula ");  
			sql.append("			order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ");
			sql.append("	) as matriculaPeriodo on 1=1 ");	
		}
		sql.append(" 	inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sql.append(" 	inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" 	inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" 	inner join matriculaintegralizacaocurricular(matricula.matricula) as integralizacao on integralizacao.percentualcumpridoliberartcc >= 100 ");
		sql.append(" 	where matricula.situacao = 'AT' ");		
		sql.append(" 	and matriculaperiodo.situacaomatriculaperiodo in ('AT','FI') ");
		sql.append("    and gradecurricular.percentualPermitirIniciarTcc > 0  ");
		sql.append("    and gradecurricular.disciplinapadraotcc is not null and gradecurricular.disciplinapadraotcc != 0 ");
		sql.append("    and  exists ( ");
		sql.append("    		select g.codigo from gradedisciplina g  ");
		sql.append("    		inner join periodoletivo p on p.codigo =  g.periodoletivo  ");
		sql.append("    		inner join disciplina on disciplina.codigo  = g.disciplina  ");
		sql.append("    		where disciplina.classificacaodisciplina = 'TCC' ");
		sql.append("    		and p.gradecurricular = matricula.gradecurricularatual  ");
		sql.append("    		and not exists ( ");
		sql.append("    			select historico.codigo from historico where historico.gradedisciplina = g.codigo ");
		sql.append("    			and historico.matricula  = matricula.matricula  ");
		sql.append("    			and historico.matrizcurricular = matricula.gradecurricularatual  ");
		sql.append("    			and historico.situacao in ('AP', 'AA', 'CC', 'CH', 'IS', 'AB') ");
		sql.append("    		) ");
		sql.append("    ) ");
		if(isAlunoNaoEnsalado) {
			sql.append(" 	and not exists (SELECT salaaulablackboardpessoa.codigo  FROM salaaulablackboardpessoa   ");
			sql.append(" 				inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard");
			sql.append(" 				WHERE salaaulablackboardpessoa.matricula = matricula.matricula   ");
			sql.append(" 				and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' "); 
			sql.append(" 				and salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
			sql.append("				and salaaulablackboard.curso = curso.codigo ");
			sql.append("				and salaaulablackboard.disciplina = gradecurricular.disciplinapadraotcc  ");
			sql.append(" )");
		}
		sql.append(" 	group by 	matricula.matricula,  ");
		sql.append(" 				matricula.gradecurricularatual,  ");
		sql.append(" 				unidadeensino.nome, ");
		sql.append(" 				gradecurricular.totalCargaHorariaEstagio, ");
		sql.append(" 				pessoa.codigo, ");
		sql.append(" 				pessoa.nome, ");
		sql.append(" 				curso.codigo, ");
		sql.append(" 				curso.nome, ");
		sql.append(" 				curso.abreviatura, ");
		sql.append(" 				curso.periodicidade, ");
		sql.append(" 				matriculaperiodo.codigo, ");
		sql.append(" 				matriculaperiodo.ano, ");
		sql.append(" 				matriculaperiodo.semestre,  ");
		sql.append(" 				gradecurricular.cargahoraria, ");
		sql.append(" 				gradecurricular.percentualPermitirIniciarTcc, ");
		sql.append(" 				gradecurricular.disciplinapadraotcc ");
		sql.append(" ) as t  where 1 = 1 ");
		
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaDeVerificacaoSeMatriculaAptaEstagio(ano, semestre);
		if(Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" and matricula ='").append(matricula).append("'");	
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaDesensalarEstagio() throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select matricula.matricula, matricula.gradecurricularatual,");
		sql.append(" pessoa.codigo as codigo_pessoa, pessoa.nome as nome_pessoa,");
		sql.append(" pessoaemailinstitucional.email as pessoaemailinstitucional_email,");
		sql.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.idsalaaulablackboard as salaaulablackboard_idsalaaulablackboard ");
		sql.append(" from matricula	");		
		sql.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual");
		sql.append(" inner join salaaulablackboardpessoa on  salaaulablackboardpessoa.matricula = matricula.matricula and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO'");
		sql.append(" inner join salaaulablackboard on  salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'ESTAGIO' and salaaulablackboard.curso = matricula.curso ");
		sql.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional");
		sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa");
		sql.append(" inner join lateral (");
		sql.append(" 			select sum(coalesce(estagio.cargahorariadeferida , 0)) as totalCargaHorariaDeferido ");
		sql.append("			from estagio  where estagio.matricula = matricula.matricula ");
		sql.append("			and estagio.situacaoestagioenum = 'DEFERIDO' ");
		sql.append(" ) as estagio on  estagio.totalCargaHorariaDeferido > 0 ");
		sql.append(" where  matricula.situacao in('AT', 'FI') ");
		sql.append(" and gradecurricular.percentualPermitirIniciarEstagio > 0 ");
		sql.append(" and estagio.totalCargaHorariaDeferido >= gradecurricular.totalCargaHorariaEstagio  ");
		sql.append(" group by ");
		sql.append(" matricula.matricula, ");
		sql.append(" matricula.gradecurricularatual, ");
		sql.append(" pessoa.codigo, ");
		sql.append(" pessoa.nome, ");
		sql.append(" pessoaemailinstitucional_email, ");
		sql.append(" salaaulablackboard_codigo, ");
		sql.append(" salaaulablackboard_idsalaaulablackboard ");
		
		sql.append(" union all ");
		
		sql.append(" select matricula.matricula, matricula.gradecurricularatual, ");
		sql.append(" pessoa.codigo as codigo_pessoa, pessoa.nome as nome_pessoa, ");
		sql.append(" pessoaemailinstitucional.email as pessoaemailinstitucional_email, ");
		sql.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.idsalaaulablackboard as salaaulablackboard_idsalaaulablackboard ");
		sql.append(" from matricula	");
		sql.append(" inner join salaaulablackboardpessoa on  salaaulablackboardpessoa.matricula = matricula.matricula and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
		sql.append(" inner join salaaulablackboard on  salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'ESTAGIO' and salaaulablackboard.curso = matricula.curso ");
		sql.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
		sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa ");
		sql.append(" where matricula.situacao in ('AC','DE','IN', 'CA', 'CF', 'JU', 'TS', 'FO', 'TI') ");		
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaDesensalarTccAmbientacao() throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select matricula.matricula, ");
		sql.append(" pessoa.codigo as codigo_pessoa, pessoa.nome as nome_pessoa,");
		sql.append(" pessoaemailinstitucional.email as pessoaemailinstitucional_email,");
		sql.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.idsalaaulablackboard as salaaulablackboard_idsalaaulablackboard ");
		sql.append(" from matricula		");		
		sql.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual");
		sql.append(" inner join salaaulablackboardpessoa on  salaaulablackboardpessoa.matricula = matricula.matricula and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO'");
		sql.append(" inner join salaaulablackboard on  salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'TCC_AMBIENTACAO' and salaaulablackboard.curso = matricula.curso ");
		sql.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional");
		sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa");
		sql.append(" where matricula.situacao in ('AT', 'FI') ");
		sql.append(" and gradecurricular.percentualpermitiriniciartcc > 0 ");
		sql.append(" and gradecurricular.disciplinapadraotcc > 0 ");
		sql.append(" and not exists (	");
		sql.append(" 		select g.codigo from gradedisciplina g 	");
		sql.append(" 		inner join periodoletivo p on p.codigo =  g.periodoletivo 	");
		sql.append(" 		inner join disciplina on disciplina.codigo  =g.disciplina 	");
		sql.append(" 		where disciplina.classificacaodisciplina = 'TCC'	");
		sql.append(" 		and p.gradecurricular = matricula.gradecurricularatual 	");
		sql.append(" 		and not exists (	");
		sql.append(" 			select historico.codigo from historico where historico.gradedisciplina = g.codigo and	");
		sql.append(" 			historico.matricula  = matricula.matricula 	");
		sql.append(" 			and historico.matrizcurricular = matricula.gradecurricularatual 	");
		sql.append(" 			and historico.situacao in ('AP', 'AA', 'CC', 'CH', 'IS', 'AB')	");
		sql.append(" 		)	");
		sql.append(" 	) ");
		sql.append(" group by");
		sql.append(" matricula.matricula, ");
		sql.append(" pessoa.codigo, ");
		sql.append(" pessoa.nome, ");
		sql.append(" pessoaemailinstitucional_email, ");
		sql.append(" salaaulablackboard_codigo, ");
		sql.append(" salaaulablackboard_idsalaaulablackboard ");
		
		sql.append(" union all ");
		
		sql.append(" select matricula.matricula, ");
		sql.append(" pessoa.codigo as codigo_pessoa, pessoa.nome as nome_pessoa,");
		sql.append(" pessoaemailinstitucional.email as pessoaemailinstitucional_email,");
		sql.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.idsalaaulablackboard as salaaulablackboard_idsalaaulablackboard ");
		sql.append(" from matricula		");
		sql.append(" inner join salaaulablackboardpessoa on  salaaulablackboardpessoa.matricula = matricula.matricula and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO'");
		sql.append(" inner join salaaulablackboard on  salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'TCC_AMBIENTACAO' and salaaulablackboard.curso = matricula.curso ");
		sql.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional");
		sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa");
		sql.append(" where matricula.situacao in ('AC','DE','IN', 'CA', 'CF', 'JU', 'TS', 'FO', 'TI') ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardTccAmbientacao(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(ano, semestre, TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, true);
		if(Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" and matricula ='").append(matricula).append("'");	
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<MatriculaPeriodoVO> realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio(List<Integer> listaCurso, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaDeVerificacaoSeMatriculaAptaEstagio(ano, semestre);
		if(Uteis.isAtributoPreenchido(listaCurso)) {
			sql.append(" and codigo_curso  in (").append(UteisTexto.converteListaInteiroParaString(listaCurso)).append(")");	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaPeriodoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
			matriculaPeriodoVO.setCodigo(tabelaResultado.getInt("codigo_matriculaperiodo"));
			matriculaPeriodoVO.setAno(tabelaResultado.getString("ano"));
			matriculaPeriodoVO.setSemestre(tabelaResultado.getString("semestre"));
			matriculaPeriodoVO.setMatriculaVO(new MatriculaVO());
			matriculaPeriodoVO.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			matriculaPeriodoVO.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			matriculaPeriodoVO.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("nome_pessoa"));
			matriculaPeriodoVO.getMatriculaVO().getCurso().setCodigo(tabelaResultado.getInt("codigo_curso"));
			matriculaPeriodoVO.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("nome_curso"));
			matriculaPeriodoVO.getMatriculaVO().getCurso().setAbreviatura(tabelaResultado.getString("abreviatura_curso"));
			matriculaPeriodoVO.getMatriculaVO().getCurso().setPeriodicidade(tabelaResultado.getString("periodicidade"));
			lista.add(matriculaPeriodoVO);
		}
		return lista;
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoSalaAulaBlackboardPorGradeCurricularEstagio(EstagioVO estagioVO, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		Uteis.checkState(!Uteis.isAtributoPreenchido(configEstagio), "O cadastro da Configuração de Estágio Obrigatório deve ser informado.");
		getFacadeFactory().getMatriculaFacade().carregarDados(estagioVO.getMatriculaVO(), NivelMontarDados.TODOS, usuarioVO);
		MatriculaPeriodoVO mp = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(estagioVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		SalaAulaBlackboardVO salaAulaBlackboardEstagio = consultarSalaAulaBlackboardPorGradeCurricularEstagioVO(estagioVO.getGradeCurricularEstagioVO(), TipoSalaAulaBlackboardEnum.COMPONENTE_ESTAGIO, usuarioVO);
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardEstagio)) {
			Integer qtdeAlunosMatriculados = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarQuantidadeMatriculaPorSalaAulaBlackboard(salaAulaBlackboardEstagio, TipoSalaAulaBlackboardPessoaEnum.ALUNO, usuarioVO);
			if( qtdeAlunosMatriculados < configEstagio.getQtdMinimaMantidoPorFacilitador()) {
				SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(estagioVO.getMatriculaVO().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				sabAluno.setMatricula(estagioVO.getMatriculaVO().getMatricula());
				realizarEnvioConviteAlunoSalaAulaBlackboard(salaAulaBlackboardEstagio, sabAluno, usuarioVO);
			}else {
				realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(estagioVO.getMatriculaVO(), mp, salaAulaBlackboardEstagio.getNrSala() + 1,  TipoSalaAulaBlackboardEnum.COMPONENTE_ESTAGIO,  estagioVO.getGradeCurricularEstagioVO(), configEstagio, usuarioVO);
			}
		}else {
			realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(estagioVO.getMatriculaVO(), mp, 1, TipoSalaAulaBlackboardEnum.COMPONENTE_ESTAGIO, estagioVO.getGradeCurricularEstagioVO(), configEstagio, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoSalaAulaBlackboardPorModuloTccAmbientacao(MatriculaVO matriculaVO , MatriculaPeriodoVO ultimaMatriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		SalaAulaBlackboardVO salaAulaBlackboardTcc = consultarSalaAulaBlackboardTccAmbientacaoPorGradeCurricularPorImportadoFalsoPorAnoSemestreNulos(matriculaVO.getGradeCurricularAtual().getCodigo(), usuarioVO);
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardTcc)) {
//			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_TCC_LIBERADO, false, null);
			String msgNotificacao = null;
			TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = null;
//			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
//				msgNotificacao = getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().obterMensagemFormatadaMensagemAlunosTccLiberado(matriculaVO, mensagemTemplate.getMensagem());
//				templateMensagemAutomaticaEnum = mensagemTemplate.getTemplateMensagemAutomaticaEnum();
//			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(salaAulaBlackboardTcc,  matriculaVO.getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, matriculaVO.getMatricula(), null, null, msgNotificacao, templateMensagemAutomaticaEnum, null, usuarioVO);
		}else {
			realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(matriculaVO, ultimaMatriculaPeriodoVO, 1, TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, null, null, usuarioVO);
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoSalaAulaBlackboardPorModuloEstagio(MatriculaVO matriculaVO , MatriculaPeriodoVO ultimaMatriculaPeriodoVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuarioVO) throws Exception {
		SalaAulaBlackboardVO salaAulaBlackboardEstagio = consultarSalaAulaBlackboardPorTipoSalaAulaBlackboardPorImportadoFalsoPorAnoSemestreNulos(matriculaVO.getCurso(), TipoSalaAulaBlackboardEnum.ESTAGIO, usuarioVO);
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardEstagio)) {
			Integer qtdeAlunosMatriculados = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarQuantidadeMatriculaPorSalaAulaBlackboard(salaAulaBlackboardEstagio, TipoSalaAulaBlackboardPessoaEnum.ALUNO, usuarioVO);
			Integer qtdeOperacaoAlunos = getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarQuantidadeOperacaoRestantesPorSalaAulaBlackboard(salaAulaBlackboardEstagio, usuarioVO);
//			if(configEstagio.getQtdVagasPorSalaEstagio().equals(0) ||  (qtdeAlunosMatriculados + qtdeOperacaoAlunos) < configEstagio.getQtdVagasPorSalaEstagio()) {
//				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_LIBERADO, false, null);
//				String msgNotificacao = null;
//				TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = null;
//				if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
//					msgNotificacao = getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().obterMensagemFormatadaMensagemAlunosEstagioLiberado(matriculaVO, mensagemTemplate.getMensagem());
//					templateMensagemAutomaticaEnum = mensagemTemplate.getTemplateMensagemAutomaticaEnum();
//				}
//				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(salaAulaBlackboardEstagio,  matriculaVO.getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, matriculaVO.getMatricula(), null, null, msgNotificacao, templateMensagemAutomaticaEnum, null, usuarioVO);
//			}
//			else {
//				realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(matriculaVO, ultimaMatriculaPeriodoVO, salaAulaBlackboardEstagio.getNrSala() + 1,  TipoSalaAulaBlackboardEnum.ESTAGIO, null, configEstagio, usuarioVO);
//			}
		}else {
			realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(matriculaVO, ultimaMatriculaPeriodoVO, 1, TipoSalaAulaBlackboardEnum.ESTAGIO, null, configEstagio, usuarioVO);
		}
		validarHistoricosDeEstagioCriados(matriculaVO, ultimaMatriculaPeriodoVO, TipoSalaAulaBlackboardEnum.ESTAGIO, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private SalaAulaBlackboardVO realizarGeracaoSalaAulaBlackboardPorTipoSalaBlackboard(MatriculaVO matriculaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoVO, Integer nrSala, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, GradeCurricularEstagioVO gradeCurricularEstagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuarioVO) throws Exception {
		SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
		sab.setTipoSalaAulaBlackboardEnum(tipoSalaAulaBlackboardEnum);
		sab.setCursoVO(matriculaVO.getCurso());
		sab.setNrSala(nrSala);
		if(tipoSalaAulaBlackboardEnum.isTccAmbientacao()) {
			sab.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorGradeCurricularDisciplinaPadraoTcc(matriculaVO.getGradeCurricularAtual().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			sab.setDataSourceId(sab.getDisciplinaVO().getFonteDeDadosBlackboard());
		}
		if(tipoSalaAulaBlackboardEnum.isEstagio()) {
			sab.setGradeCurricularEstagioVO(gradeCurricularEstagioVO);
			sab.setDataSourceId(configEstagio.getFonteDeDadosBlackboardEstagio());
		}
		if(tipoSalaAulaBlackboardEnum.isComponenteEstagio()) {
			sab.setGradeCurricularEstagioVO(gradeCurricularEstagioVO);
			sab.setDataSourceId(configEstagio.getFonteDeDadosBlackboardComponenteEstagio());
		}
		if(!tipoSalaAulaBlackboardEnum.isTccAmbientacao() && !tipoSalaAulaBlackboardEnum.isEstagio() && !matriculaVO.getCurso().getIntegral() && matriculaVO.getCurso().getAnual() &&  Uteis.isAtributoPreenchido(ultimaMatriculaPeriodoVO)) {
			sab.setAno(ultimaMatriculaPeriodoVO.getAno());
		}
		if(!tipoSalaAulaBlackboardEnum.isTccAmbientacao() && !tipoSalaAulaBlackboardEnum.isEstagio() && !matriculaVO.getCurso().getIntegral() && matriculaVO.getCurso().getSemestral() &&  Uteis.isAtributoPreenchido(ultimaMatriculaPeriodoVO)) {
			sab.setAno(ultimaMatriculaPeriodoVO.getAno());
			sab.setSemestre(ultimaMatriculaPeriodoVO.getSemestre());
		}
		SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
		sabAluno.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.ALUNO);
		sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		sabAluno.setMatricula(matriculaVO.getMatricula());
		sab.getListaAlunos().add(sabAluno);
		if(tipoSalaAulaBlackboardEnum.isEstagio() || tipoSalaAulaBlackboardEnum.isComponenteEstagio()) {
			//professor
			configEstagio.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().stream().forEach(p->{
				if(Uteis.isAtributoPreenchido(p.getFuncionarioVO().getPessoa().getListaPessoaEmailInstitucionalVO()) && p.getFuncionarioVO().getPessoa().getListaPessoaEmailInstitucionalVO().stream().anyMatch(e -> e.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO))) {
					SalaAulaBlackboardPessoaVO sabProfessor = new SalaAulaBlackboardPessoaVO();
					sabProfessor.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.PROFESSOR);
					sabProfessor.setPessoaEmailInstitucionalVO(p.getFuncionarioVO().getPessoa().getListaPessoaEmailInstitucionalVO().stream().filter(e -> e.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)).findFirst().get());
					sab.getListaProfessores().add(sabProfessor);
				}
			});
		}
		realizarValidacaoGrupoPessoaItem(matriculaVO, tipoSalaAulaBlackboardEnum, gradeCurricularEstagioVO, usuarioVO, sab);
		//getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirSalaBlack(sab, "SALA_AULA_BLACKBOARD", null, usuarioVO);
		return realizarGeracaoSalaAulaBlackboard(sab, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarValidacaoGrupoPessoaItem(MatriculaVO matriculaVO, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, GradeCurricularEstagioVO gradeCurricularEstagioVO, UsuarioVO usuarioVO, SalaAulaBlackboardVO sab) throws Exception {
		if(tipoSalaAulaBlackboardEnum.isEstagio() || tipoSalaAulaBlackboardEnum.isComponenteEstagio()) {
			// FACILITADOR
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
			GrupoPessoaVO grupoPessoaFacilitadores = new GrupoPessoaVO();
			if(tipoSalaAulaBlackboardEnum.isComponenteEstagio() && gradeCurricularEstagioVO.getGradeCurricularEstagioQuestionarioEnum().isAproveitamento()) {
				grupoPessoaFacilitadores = curso.getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO();
			}else if(tipoSalaAulaBlackboardEnum.isComponenteEstagio() && gradeCurricularEstagioVO.getGradeCurricularEstagioQuestionarioEnum().isEquivalencia()) {
				grupoPessoaFacilitadores = curso.getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO();
			}else if(tipoSalaAulaBlackboardEnum.isEstagio() || tipoSalaAulaBlackboardEnum.isComponenteEstagio()) {
				grupoPessoaFacilitadores = curso.getGrupoPessoaAnaliseRelatorioFinalEstagioVO();
			}
			if (Uteis.isAtributoPreenchido(grupoPessoaFacilitadores)) {
				grupoPessoaFacilitadores = getFacadeFactory().getGrupoPessoaFacade().consultarPorChavePrimaria(grupoPessoaFacilitadores.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
			//listaGrupoPessoaItemFacilitadores = getFacadeFactory().getGrupoPessoaItemFacade().consultarPorGrupoPessoaVO(grupoPessoaFacilitadores, StatusAtivoInativoEnum.ATIVO, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(grupoPessoaFacilitadores.getPessoaSupervisorGrupo())) {
				SalaAulaBlackboardPessoaVO sabpFacilitador = new SalaAulaBlackboardPessoaVO();
				sabpFacilitador.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.FACILITADOR);
				sabpFacilitador.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(grupoPessoaFacilitadores.getPessoaSupervisorGrupo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				if(Uteis.isAtributoPreenchido(sabpFacilitador.getPessoaEmailInstitucionalVO())) {
					sab.getListaFacilitadores().add(sabpFacilitador);
				}
			}
			for (GrupoPessoaItemVO gpi : grupoPessoaFacilitadores.getListaGrupoPessoaItemVO()) {
				if(gpi.getStatusAtivoInativoEnum().isAtivo()) {
					SalaAulaBlackboardPessoaVO sabpFacilitador = new SalaAulaBlackboardPessoaVO();
					sabpFacilitador.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.FACILITADOR);
					sabpFacilitador.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(gpi.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					if(Uteis.isAtributoPreenchido(sabpFacilitador.getPessoaEmailInstitucionalVO()) && sab.getListaFacilitadores().stream().noneMatch(p->p.getPessoaEmailInstitucionalVO().getCodigo().equals(sabpFacilitador.getPessoaEmailInstitucionalVO().getCodigo()))) {
						sab.getListaFacilitadores().add(sabpFacilitador);
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO realizarGeracaoSalaAulaBlackboardPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO pto, SalaAulaBlackboardVO obj , UsuarioVO usuarioVO) throws Exception {
		obj.setProgramacaoTutoriaOnlineVO(pto);
//		Uteis.checkState(pto.getDisciplinaVO().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.CURSO_DISCIPLINA)  && !Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso deve ser informado.");
//		Uteis.checkState(pto.getDisciplinaVO().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.TURMA_DISCIPLINA) && !Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma deve ser informado.");
//		boolean turmaDisciplinaEad = getFacadeFactory().getTurmaDisciplinaFacade().consultarSeTurmaDisciplinaSaoEadPorUnidadeEnsinoPorCursoPorNivelEducacionalPorTurmaPorDisciplina(pto.getUnidadeEnsinoVO().getCodigo(), pto.getCursoVO().getCodigo(), pto.getNivelEducacional(), obj.getTurmaVO().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuarioVO);
//		Uteis.checkState((obj.getTurmaVO().getAnual() || obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getAno()), "O campo Ano (Sala de Aula Blackboard) deve ser informado.");
//		Uteis.checkState((obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getSemestre()), "O campo Semestre (Sala de Aula Blackboard) deve ser informado.");
//		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getUnidadeEnsinoVO().getCodigo()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada na Unidade Ensino "+ pto.getUnidadeEnsinoVO().getNome()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
//		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getCursoVO().getCodigo()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada no Curso "+ pto.getCursoVO().getNome()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
//		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getCursoVO().getNivelEducacional()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada no Curso "+ pto.getCursoVO().getNome()+" com o Nível Educacional "+pto.getCursoVO().getNivelEducacional_Apresentar()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
//		Uteis.checkState(!turmaDisciplinaEad, "Não existe para a TURMA informada a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getTurmaVO()) && !obj.getTurmaVO().getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL), "Não é possível gerar Sala de Aula Blackboard de uma Subturma.");
		return realizarGeracaoSalaAulaBlackboard(obj, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO realizarGeracaoSalaAulaBlackboard(SalaAulaBlackboardVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//		Uteis.checkState(obj.getTipoSalaAulaBlackboardEnum().isDisciplina() && obj.getDisciplinaVO().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.CURSO_DISCIPLINA) && !Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso deve ser informado.");
//		Uteis.checkState(obj.getTipoSalaAulaBlackboardEnum().isDisciplina() && obj.getDisciplinaVO().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.TURMA_DISCIPLINA) && !Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma deve ser informado.");
		Uteis.checkState(obj.getTipoSalaAulaBlackboardEnum().isDisciplina() && !Uteis.isAtributoPreenchido(obj.getDisciplinaVO()), "O campo Disciplina deve ser informado.");
		Uteis.checkState((obj.getTipoSalaAulaBlackboardEnum().isTccAmbientacao() ||  obj.getTipoSalaAulaBlackboardEnum().isEstagio()) && !Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso deve ser informado.");
		Uteis.checkState(obj.getTipoSalaAulaBlackboardEnum().isComponenteEstagio() && !Uteis.isAtributoPreenchido(obj.getGradeCurricularEstagioVO()), "O campo Grade Currigular Estágio deve ser informado.");
		Uteis.checkState(!obj.getTipoSalaAulaBlackboardEnum().isTccAmbientacao() && !obj.getTipoSalaAulaBlackboardEnum().isEstagio() && (obj.getCursoVO().getAnual() || obj.getCursoVO().getSemestral() || obj.getTurmaVO().getAnual() || obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getAno()), "O campo Ano deve ser informado.");
		Uteis.checkState(!obj.getTipoSalaAulaBlackboardEnum().isTccAmbientacao() && !obj.getTipoSalaAulaBlackboardEnum().isEstagio() && (obj.getCursoVO().getSemestral() || obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getSemestre()), "O campo Semestre deve ser informado.");
		obj.realizarValidacaoEmailExistente();
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse = unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_PERSISTIR, json, RequestMethod.POST, null, usuarioVO);
		return gson.fromJson(jsonResponse.getBody().toString(), SalaAulaBlackboardVO.class);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO realizarVerificacaoDadosSeiComDadosBlackboard(Integer codigoSalaAula , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.codigo, codigoSalaAula.toString());
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_REVISAO_SEI, RequestMethod.PUT, headers, usuarioVO);
		Gson gson = inicializaGson();
		return gson.fromJson(jsonResponse.getBody().toString(), SalaAulaBlackboardVO.class);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO realizarRevisaoSalaAulaBlackboard(SalaAulaBlackboardVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);		
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse = unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_REALIZAR_REVISAO, json, RequestMethod.PUT, null, usuarioVO);
		return gson.fromJson(jsonResponse.getBody().toString(), SalaAulaBlackboardVO.class);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarRevisaoSalaAulaBlackboardPorTurma(TurmaVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
			Gson gson = inicializaGson();
			String json = gson.toJson(obj);
			TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
			unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_REALIZAR_REVISAO_TURMA, json, RequestMethod.PUT, null, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarRevisaoSalaAulaBlackboardPorTurmaPorDisciplinaPorAnoPorSemestrePorProfessor(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, CursoVO curso, TurmaVO turma, DisciplinaVO displina , String ano, String semestre, Integer bimestre, Integer nrSala, Integer professor, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
			SalaAulaBlackboardVO sab = consultarSeExisteSalaAulaBlackboard(tipoSalaAulaBlackboardEnum, curso.getCodigo(), turma.getCodigo(), displina.getCodigo(), ano, semestre, bimestre, nrSala, professor, usuarioVO);
			if(Uteis.isAtributoPreenchido(sab)) {
				sab.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(sab.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
				Gson gson = inicializaGson();
				String json = gson.toJson(sab);
				TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
				unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_REALIZAR_REVISAO, json, RequestMethod.PUT, null, usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoSalaAulaBlackboard(SalaAulaBlackboardVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoSalaAulaBlackboardEad(SalaAulaBlackboardVO objFiltro , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(objFiltro);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_EAD_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarBuscaAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj,  ConfiguracaoGeralSistemaVO confGeral, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdSalaAulaBlackboard());
		headers.put(UteisWebServiceUrl.id, obj.getId());
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONSULTAR_ALUNOS, RequestMethod.GET, headers, usuarioVO);
		Gson gson = inicializaGson();
		Type listaType = new TypeToken<ArrayList<SalaAulaBlackboardPessoaVO>>(){}.getType();
		List<SalaAulaBlackboardPessoaVO> listaEstudante =  gson.fromJson(jsonResponse.getBody().toString(), listaType);
		if(Uteis.isAtributoPreenchido(obj.getTurmaVO())) {
			obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
		}
		List<SalaAulaBlackboardPessoaVO> listaEstudanteHistorico = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(obj.getProgramacaoTutoriaOnlineVO())) {
			listaEstudanteHistorico =  getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosDoEadTurmaDisciplinaDisponivel(obj.getCursoVO(), obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getAno(), obj.getSemestre(), obj.getBimestre(), obj.getProgramacaoTutoriaOnlineVO(), true,  obj.getCodigo(), usuarioVO);
		}else {
			listaEstudanteHistorico =  getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivel(0, obj.getCursoVO().getCodigo(), obj.getTurmaVO(), obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), 0, true, obj.getCodigo(), confGeral, usuarioVO);
		}
		listaEstudanteHistorico.stream()
		.filter(p-> Uteis.isAtributoPreenchido(p.getPessoaEmailInstitucionalVO().getEmail()) && listaEstudante.stream().anyMatch(pp-> pp.getPessoaEmailInstitucionalVO().getEmail().equals(p.getPessoaEmailInstitucionalVO().getEmail())))
		.forEach(p-> p.getPessoaEmailInstitucionalVO().setPessoaEmailInstitucional(true));
		obj.setListaAlunos(listaEstudanteHistorico);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarEnvioConviteAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj,  SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()), "O Conta de Email no Blackboard deve ser informado para o Aluno "+salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.codigo, obj.getCodigo().toString());
		headers.put(UteisWebServiceUrl.emailpessoainstitucional, salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
		headers.put(UteisWebServiceUrl.matricula, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatricula()) ? null : salaAulaBlackboardPessoaVO.getMatricula());
		headers.put(UteisWebServiceUrl.matriculaperiodoturmadisciplina, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString()) ? null : salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString() );
		unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_ADICIONAR_ALUNOS, RequestMethod.PUT, headers, usuarioVO);
		obj.getListaAlunos().stream().filter(p-> p.getPessoaEmailInstitucionalVO().getEmail().equals(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail())).forEach(p-> p.getPessoaEmailInstitucionalVO().setPessoaEmailInstitucional(true));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(SalaAulaBlackboardVO obj,  SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, OperacaoEnsalacaoBlackboardEnum operacao, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()), "O Conta de Email no Blackboard deve ser informado para o Aluno "+salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.codigo, obj.getCodigo().toString());
		headers.put(UteisWebServiceUrl.emailpessoainstitucional, salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
		headers.put(UteisWebServiceUrl.matricula, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatricula()) ? null : salaAulaBlackboardPessoaVO.getMatricula());
		headers.put(UteisWebServiceUrl.matriculaperiodoturmadisciplina, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina()) ? null : salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString());
		headers.put(UteisWebServiceUrl.operacao, operacao.name());
		unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_GRUPO_OPERACAO_ALUNOS, RequestMethod.PUT, headers, usuarioVO);		
		
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO()) 
				&& !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome())) {
			salaAulaBlackboardPessoaVO.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorChavePrimaria(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
//		if(Uteis.isAtributoPreenchido(operacao) && operacao.equals(OperacaoEnsalacaoBlackboardEnum.EXCLUIR) && obj.getTipoSalaAulaBlackboardEnum().isProjetoIntegradorGrupo()) {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROJETO_INTEGRADOR_EXCLUSAO_SALA_GRUPO, obj, salaAulaBlackboardPessoaVO, usuarioVO);
//		}else if(Uteis.isAtributoPreenchido(operacao) && operacao.equals(OperacaoEnsalacaoBlackboardEnum.INCLUIR) && obj.getTipoSalaAulaBlackboardEnum().isProjetoIntegradorGrupo()) {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROJETO_INTEGRADOR_INCLUSAO_SALA_GRUPO, obj, salaAulaBlackboardPessoaVO, usuarioVO);
//		}else if(Uteis.isAtributoPreenchido(operacao) && operacao.equals(OperacaoEnsalacaoBlackboardEnum.INCLUIR) && obj.getTipoSalaAulaBlackboardEnum().isTccGrupo()) {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_TCC_INCLUSAO_SALA_GRUPO, obj, salaAulaBlackboardPessoaVO, usuarioVO);
//		}else if(Uteis.isAtributoPreenchido(operacao) && operacao.equals(OperacaoEnsalacaoBlackboardEnum.EXCLUIR) && obj.getTipoSalaAulaBlackboardEnum().isTccGrupo()) {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_TCC_EXCLUSAO_SALA_GRUPO, obj, salaAulaBlackboardPessoaVO, usuarioVO);
//		}		
	}	
	
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO realizarConsultaSalaAulaBlackboardConteudoMaster(String idSalaAulaBlackboard,  UsuarioVO usuarioVO) throws Exception {
		SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
		try {
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.id, idSalaAulaBlackboard);
			TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
			HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONSULTAR, RequestMethod.GET, headers, usuarioVO);	
			Gson gson = inicializaGson();
			sab =  gson.fromJson(jsonResponse.getBody().toString(), SalaAulaBlackboardVO.class);
		} catch (Exception e) {
			if(!e.getMessage().contains("Connection refused")) {
				throw new StreamSeiException(e);	
			}
		}
		return sab;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardVO> realizarConsultaSalaAulaBlackboardConteudoMaster(UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(configSeiBlackboardVO.getFonteDeDadosConteudoMasterBlackboard())) {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(UteisWebServiceUrl.descricao, configSeiBlackboardVO.getFonteDeDadosConteudoMasterBlackboard());
				TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
				HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONSULTAR_CONTEUDO, RequestMethod.GET, headers, usuarioVO);	
				Gson gson = inicializaGson();
				Type listaType = new TypeToken<ArrayList<SalaAulaBlackboardVO>>(){}.getType();
				return gson.fromJson(jsonResponse.getBody().toString(), listaType);	
			}	
		} catch (Exception e) {
			if(!e.getMessage().contains("Connection refused")) {
				throw new StreamSeiException(e);	
			}
		}
		return new ArrayList<>();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(SalaAulaBlackboardVO obj,  SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getId()), "O campo Id da Sala Aula Blackboard deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getIdSalaAulaBlackboard()), "O campo Id Sala Aula Blackboard  deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()), "O campo Email deve ser informado.");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdSalaAulaBlackboard());
		headers.put(UteisWebServiceUrl.emailpessoainstitucional, salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONSULTAR_ALUNO, RequestMethod.GET, headers, usuarioVO);
		Gson gson = inicializaGson();
		PessoaEmailInstitucionalVO pg =  gson.fromJson(jsonResponse.getBody().toString(), PessoaEmailInstitucionalVO.class);
		if(pg == null || !Uteis.isAtributoPreenchido(pg.getEmail())) {
			realizarEnvioConviteAlunoSalaAulaBlackboard(obj, salaAulaBlackboardPessoaVO, usuarioVO);
		}
	}

//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
//	public void realizarGeracaoSalaAulaBlackboardPorHorarioTurma( UsuarioVO usuarioVO) throws Exception {
//		CursoVO curso = obj.getDisciplina().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.CURSO_DISCIPLINA) ? obj.getTurma().getCurso() : null;
//		TurmaVO turma = obj.getDisciplina().getModeloGeracaoSalaBlackboard().equals(ModeloGeracaoSalaBlackboardEnum.TURMA_DISCIPLINA) ? obj.getTurma() : null;
//		SalaAulaBlackboardVO c = consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum.DISCIPLINA, obj.getTurma().getCurso().getCodigo() , obj.getTurma().getCodigo(),  obj.getDisciplina().getCodigo(), obj.getAnoVigente(), obj.getSemestreVigente(), null, null, null, usuarioVO);
//		if(!Uteis.isAtributoPreenchido(c)) {
//			c = new SalaAulaBlackboardVO(obj.getTurma().getCurso() , obj.getTurma(), obj.getDisciplina(), obj.getAnoVigente(), obj.getSemestreVigente());
//			if(Uteis.isAtributoPreenchido(obj.getTurma().getCurso())){
//				c.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getTurma().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioVO));
//			}
//			realizarGeracaoSalaAulaBlackboard(c, usuarioVO);
//		}
//	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarProcessamentoLoteSalaAulaBlackboard(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		return unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONTA_LOTE, RequestMethod.POST, null, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarProcessamentoLoteSalaAulaBlackboardPorHorarioTurma( UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
//		headers.put(UteisWebServiceUrl.horarioTurma, obj.getCodigo().toString());
		return unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_CONTA_LOTE_HORARIO_TURMA, RequestMethod.POST, headers, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarAtualizacaoAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj,  UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		return unirestBody(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_ATUALIZAR_ALUNOS, json, RequestMethod.PUT, null, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarSalaAulaBlackboardOperacao(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_OPERACAO_ALUNO, RequestMethod.PUT, null, usuarioVO);
	}


//	@Override
//	public void consultarClassroomPorDataModelo(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
//		dataModeloGoogleMeet.setListaConsulta(consultarClassroom(dataModeloGoogleMeet,turmaVO, ano, semestre, usuarioLogado));
//
//	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarSeExisteSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(String matricula, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(distinct salaaulablackboard.codigo) AS QTDE FROM salaaulablackboard ");
		sql.append("inner join salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" WHERE salaaulablackboardpessoa.matricula = ? ");
		sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		sql.append(" and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {matricula, tipoSalaAulaBlackboardEnum.name(), TipoSalaAulaBlackboardPessoaEnum.ALUNO.name()});
		return (Boolean) Uteis.isAtributoPreenchido(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(String matricula, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) {
		StringBuilder sql = getSQLPadraoConsultaBasica(false);
		sql.append(" INNER JOIN salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" WHERE salaaulablackboardpessoa.matricula = ? ");
		sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		sql.append(" and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {matricula, tipoSalaAulaBlackboardEnum.name(), TipoSalaAulaBlackboardPessoaEnum.ALUNO.name()});
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;
	}	

	public SalaAulaBlackboardVO consultarSalaAulaBlackboardPorTipoSalaAulaBlackboardPorImportadoFalsoPorAnoSemestreNulos(CursoVO cursoVO, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica(false);
		sql.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum.name()).append("' ");
		sql.append(" and salaaulablackboard.curso = ").append(cursoVO.getCodigo());
		sql.append(" and salaaulablackboard.importado = false ");
		sql.append(" and coalesce (salaaulablackboard.ano, '') = '' ");
		sql.append(" and coalesce (salaaulablackboard.semestre, '') = '' ");
		sql.append(" and salaaulablackboard.nrsala = (select MAX(sab.nrsala) from salaaulablackboard as sab ");
		sql.append(" where sab.tipoSalaAulaBlackboardEnum='").append(tipoSalaAulaBlackboardEnum.name()).append("' ");
		sql.append(" and sab.curso = ").append(cursoVO.getCodigo());
		sql.append(" and sab.importado = false ");
		sql.append(" and coalesce (sab.ano, '') = '' ");
		sql.append(" and coalesce (sab.semestre, '') = '' ");
		sql.append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;

	}

	
	public SalaAulaBlackboardVO consultarSalaAulaBlackboardPorGradeCurricularEstagioVO(GradeCurricularEstagioVO gce, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica(false);
		sql.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum.name()).append("' ");
		sql.append(" and salaaulablackboard.gradecurricularestagio = ").append(gce.getCodigo());
		sql.append(" and salaaulablackboard.nrsala = (select MAX(sab.nrsala) from salaaulablackboard as sab ");
		sql.append(" where sab.tipoSalaAulaBlackboardEnum='").append(tipoSalaAulaBlackboardEnum.name()).append("' ");
		sql.append(" and sab.gradecurricularestagio = ").append(gce.getCodigo());
		sql.append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;

	}
	
	public SalaAulaBlackboardVO consultarSalaAulaBlackboardTccAmbientacaoPorGradeCurricularPorImportadoFalsoPorAnoSemestreNulos(Integer codigoGradeCurricular, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica(false);
		sql.append(" inner join gradecurricular on salaaulablackboard.curso = gradecurricular.curso  and salaaulablackboard.disciplina = gradecurricular.disciplinapadraotcc ");
		sql.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO.name()).append("' ");
		sql.append(" and gradecurricular.codigo = ? ");
		sql.append(" and salaaulablackboard.importado = false ");
		sql.append(" and coalesce (salaaulablackboard.ano, '') = '' ");
		sql.append(" and coalesce (salaaulablackboard.semestre, '') = '' ");
		sql.append(" and salaaulablackboard.nrsala = (select MAX(sab.nrsala) from salaaulablackboard as sab ");
		sql.append(" inner join gradecurricular on sab.curso = gradecurricular.curso  and sab.disciplina = gradecurricular.disciplinapadraotcc ");
		sql.append(" where sab.tipoSalaAulaBlackboardEnum='").append(TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO.name()).append("' ");
		sql.append(" and gradecurricular.codigo = ? ");
		sql.append(" and sab.importado = false ");
		sql.append(" and coalesce (sab.ano, '') = '' ");
		sql.append(" and coalesce (sab.semestre, '') = '' ");
		sql.append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoGradeCurricular, codigoGradeCurricular);
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return obj;
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarSalaAulaBlackboardGrupoTcc(String matricula, GradeCurricularVO gradeCurricular,  String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ( ");
		sql.append(getSQLPadraoConsultaBasica(true));
		sql.append(" WHERE salaaulablackboard.id in ( ");
		sql.append(" 	select salaaulablackboard.id from  gradecurricular ");
		sql.append(" 	inner join salaaulablackboard on gradecurricular.curso = salaaulablackboard.curso  and gradecurricular.disciplinapadraotcc = salaaulablackboard.disciplina ");
		sql.append(" 	where gradecurricular.codigo = ? and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? and salaaulablackboard.ano = ? and salaaulablackboard.semestre = ?  ");
		sql.append("  ) ");
		sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		sql.append(" ) as t ");
		sql.append(" left join lateral ( ");
		sql.append(" 	select (case when salaaulablackboardpessoa.codigo is not null then 1 else 0 end) as alunoExiste from salaaulablackboardpessoa");
		sql.append(" 	where salaaulablackboardpessoa.salaaulablackboard = t.\"salaaulablackboard.codigo\" ");
		sql.append(" 	and salaaulablackboardpessoa.matricula = ? ");
		sql.append(" 	and salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum = 'ALUNO' ");
		sql.append(" ) as salaAulaAlunoExistente on  1=1 ");
		sql.append(" order by ");
		sql.append(" coalesce(alunoExiste, 0) desc, ");
		sql.append(" (case when t.qtdeAlunos < t.\"disciplina.nrMaximoAulosPorGrupo\" then 0 else 1 end), ");
		sql.append(" t.\"salaaulablackboard.nome\", t.\"salaaulablackboard.nomeGrupo\" ");
		sql.append(Uteis.limitOffset(controleConsulta.getLimitePorPagina(), controleConsulta.getOffset()));
		FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
		fra.realizarDesmarcarTodasSituacoes();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {gradeCurricular.getCodigo(), TipoSalaAulaBlackboardEnum.TCC.name(), ano , semestre, TipoSalaAulaBlackboardEnum.TCC_GRUPO.name(), matricula});
        montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
        controleConsulta.setListaConsulta(montarDadosConsultaOtimizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, fra,usuarioVO));

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarSalaAulaBlackboardGrupoProjetoIntegrador(String matricula, Integer unidadeEnsino, Integer disciplina,  String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ( ");
		sql.append(getSQLPadraoConsultaBasica(true));
		sql.append(" WHERE salaaulablackboard.disciplina = ? ");
		sql.append(" and salaaulablackboard.ano = ? and salaaulablackboard.semestre = ?  ");
		sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		sql.append(" and exists ( ");
		sql.append(" 	select agrupamentounidadeensinoitem.codigo  from agrupamentounidadeensinoitem");
		sql.append(" 	where agrupamentounidadeensinoitem.unidadeensino = ?");
		sql.append(" 	and agrupamentounidadeensinoitem.agrupamentounidadeensino = salaaulablackboard.agrupamentounidadeensino");
		sql.append(" )) as t ");
		sql.append(" left join lateral ( ");
		sql.append(" 	select (case when salaaulablackboardpessoa.codigo is not null then 1 else 0 end) as alunoExiste from salaaulablackboardpessoa");
		sql.append(" 	where salaaulablackboardpessoa.salaaulablackboard = t.\"salaaulablackboard.codigo\" ");
		sql.append(" 	and salaaulablackboardpessoa.matricula = ? ");
		sql.append(" 	and salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum = 'ALUNO' ");
		sql.append(" ) as salaAulaAlunoExistente on  1=1 ");
		sql.append(" order by ");
		sql.append(" coalesce(alunoExiste, 0) desc, ");
		sql.append(" (case when t.qtdeAlunos < t.\"disciplina.nrMaximoAulosPorGrupo\" then 0 else 1 end), ");
		sql.append(" t.\"salaaulablackboard.nome\", t.\"salaaulablackboard.nomeGrupo\" ");
		sql.append(Uteis.limitOffset(controleConsulta.getLimitePorPagina(), controleConsulta.getOffset()));
		FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
		fra.realizarDesmarcarTodasSituacoes();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {disciplina, ano , semestre, TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO.name(), unidadeEnsino, matricula});
		montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
		controleConsulta.setListaConsulta(montarDadosConsultaOtimizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, fra, usuarioVO));
		
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarSalaAulaBlackboardGrupoPorMatriculaPorTipoSalaAulaBlackboardEnum(MatriculaVO matricula, Integer disciplina,  TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception {
		FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
		fra.realizarDesmarcarTodasSituacoes();
		List<Object> listaFiltros = new ArrayList<>();
		StringBuilder sql = getSQLPadraoConsultaBasica(true);
		sql.append(" inner join salaaulablackboardpessoa on salaaulablackboard.codigo =  salaaulablackboardpessoa.salaaulablackboard ");
		sql.append(" where  salaaulablackboardpessoa.matricula = ? ");
		listaFiltros.add(matricula.getMatricula());
		sql.append(" and  salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = ? ");
		listaFiltros.add(TipoSalaAulaBlackboardPessoaEnum.ALUNO.name());
		sql.append(" and  salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		listaFiltros.add(tipoSalaAulaBlackboardEnum.name());
		sql.append(" and  salaaulablackboard.ano = ? ");
		listaFiltros.add(ano);	
		sql.append(" and  salaaulablackboard.semestre = ? ");
		listaFiltros.add(semestre);
		if(tipoSalaAulaBlackboardEnum.isProjetoIntegrador()) {
			sql.append(" and  salaaulablackboard.disicplina = ? ");
			listaFiltros.add(disciplina);
			sql.append(" and exists ( ");
			sql.append(" 	select agrupamentounidadeensinoitem.codigo  from agrupamentounidadeensinoitem");
			sql.append(" 	where agrupamentounidadeensinoitem.unidadeensino = ?");
			sql.append(" 	and agrupamentounidadeensinoitem.agrupamentounidadeensino = salaaulablackboard.agrupamentounidadeensino");
			sql.append(" )");	
			listaFiltros.add(matricula.getUnidadeEnsino().getCodigo());
		}
		sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nomegrupo ");
		sql.append(Uteis.limitOffset(controleConsulta.getLimitePorPagina(), controleConsulta.getOffset()));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltros.toArray());
		montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
		controleConsulta.setListaConsulta(montarDadosConsultaOtimizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, fra, usuarioVO));

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarPorChavePrimaria(Integer codigo) {
		return consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(true);
		sqlStr.append(" where salaaulablackboard.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, null);
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarPorIdSalaAulaBlackboardPorTipoSalaAulaBlackboardEnum(String idSalaAulaBlackboard, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append(" where salaaulablackboard.idSalaAulaBlackboard = ? ");
		sqlStr.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
		sqlStr.append(" and (salaaulablackboard.idgrupo is null or salaaulablackboard.idgrupo = '') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), idSalaAulaBlackboard, tipoSalaAulaBlackboardEnum.name());
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		}
		return obj;
	}



//	public List<SalaAulaBlackboardVO> consultarPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date data) throws Exception {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select   ");
//		sql.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", salaaulablackboard.ano as \"salaaulablackboard.ano\", ");
//		sql.append(" salaaulablackboard.semestre as \"salaaulablackboard.semestre\",  salaaulablackboard.linkClassroom as \"salaaulablackboard.linkClassroom\", ");
//		sql.append(" salaaulablackboard.idClassRoomGoogle as \"salaaulablackboard.idClassRoomGoogle\",  salaaulablackboard.id as \"salaaulablackboard.id\", ");
//		sql.append(" salaaulablackboard.professoread as \"salaaulablackboard.professoread\",   ");
//		sql.append(" salaaulablackboard.termId as \"salaaulablackboard.termId\",   ");
//
//		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
//
//		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
//		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
//		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", unidadeensino.razaosocial as \"unidadeensino.razaosocial\" ");
//		sql.append(" from salaaulablackboard ");
//		sql.append(" inner join turma on turma.codigo = salaaulablackboard.turma ");
//		sql.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
//		sql.append(" inner join disciplina on disciplina.codigo = salaaulablackboard.disciplina ");
//		sql.append(" left join pessoa on pessoa.codigo = salaaulablackboard.professor ");
//		sql.append(" ");
//		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = turma.codigo and matriculaperiodoturmadisciplina.disciplina = disciplina.codigo and matriculaperiodoturmadisciplina.professor = pessoa.codigo  ");
//		sql.append(" where and matriculaperiodoturmadisciplina.codigo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(matriculaPeriodoTurmaDisciplinaVOs)).append(")");
//		sql.append(" and diaEvento ='").append(Uteis.getDataJDBC(data)).append("'");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
//	}

	@Override
	public List<SalaAulaBlackboardVO> consultarSalaAulaBlackboard(SalaAulaBlackboardVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica(true);
		List<Object> listaFiltros = new ArrayList<>();
		montarFiltrosConsultaPadrao(listaFiltros, obj, sql);
		sql.append(" order by salaaulablackboard.codigo desc");
		//UteisTexto.addLimitAndOffset(sql, dataModeloGoogleMeet.getLimitePorPagina(), dataModeloGoogleMeet.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltros.toArray());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}


	private void montarFiltrosConsultaPadrao(List<Object> listaFiltros, SalaAulaBlackboardVO obj, StringBuilder sql) {
		sql.append(" WHERE 1 = 1");
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO().getIdentificadorTurma())) {
			sql.append(" and turma.identificadorTurma = ? ");
			listaFiltros.add(obj.getTurmaVO().getIdentificadorTurma());
		}

		if (Uteis.isAtributoPreenchido(obj.getAno())) {
			sql.append(" and salaaulablackboard.ano = ? ");
			listaFiltros.add(obj.getAno());
		}

		if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
			sql.append(" and salaaulablackboard.semestre = ? ");
			listaFiltros.add(obj.getSemestre());
		}
		if (Uteis.isAtributoPreenchido(obj.getTipoSalaAulaBlackboardEnum())) {
			sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = ? ");
			listaFiltros.add(obj.getTipoSalaAulaBlackboardEnum().name());
		}
	}


	public List<SalaAulaBlackboardVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}


	private StringBuilder getSQLPadraoConsultaBasica(boolean trazColunaQuantidade) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) over() as totalRegistroConsulta,  salaaulablackboard.codigo as \"salaaulablackboard.codigo\", salaaulablackboard.ano as \"salaaulablackboard.ano\", ");
		sql.append(" salaaulablackboard.semestre as \"salaaulablackboard.semestre\",   ");
		sql.append(" salaaulablackboard.bimestre as \"salaaulablackboard.bimestre\",   ");
		sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  ");
		sql.append(" salaaulablackboard.id as \"salaaulablackboard.id\",  ");
		sql.append(" salaaulablackboard.linkSalaAulaBlackboard as \"salaaulablackboard.linkSalaAulaBlackboard\",  ");
		sql.append(" salaaulablackboard.termId as \"salaaulablackboard.termId\",  ");
		sql.append(" salaaulablackboard.tipoSalaAulaBlackboardEnum as \"salaaulablackboard.tipoSalaAulaBlackboardEnum\",  ");
		sql.append(" salaaulablackboard.nrSala as \"salaaulablackboard.nrSala\",  ");
		sql.append(" salaaulablackboard.nome as \"salaaulablackboard.nome\", ");
		sql.append(" salaaulablackboard.nomeGrupo as \"salaaulablackboard.nomeGrupo\", ");
		sql.append(" salaaulablackboard.grupoExternalId as \"salaaulablackboard.grupoExternalId\", ");
		sql.append(" salaaulablackboard.grupoSetId as \"salaaulablackboard.grupoSetId\", ");
		sql.append(" salaaulablackboard.idGrupo as \"salaaulablackboard.idGrupo\", ");
		sql.append(" salaaulablackboard.nrGrupo as \"salaaulablackboard.nrGrupo\", ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
		sql.append(" curso.abreviatura as \"curso.abreviatura\", curso.idConteudoMasterBlackboardEstagio as \"curso.idConteudoMasterBlackboardEstagio\",  ");
		sql.append(" agrupamentounidadeensino.codigo as \"agrupamentounidadeensino.codigo\", agrupamentounidadeensino.descricao as \"agrupamentounidadeensino.descricao\", ");
		sql.append(" agrupamentounidadeensino.abreviatura as \"agrupamentounidadeensino.abreviatura\",  ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\", ");
		sql.append(" disciplina.nrMinimoAlunosPorSala as \"disciplina.nrMinimoAlunosPorSala\", disciplina.nrMaximoAulosPorSala as \"disciplina.nrMaximoAulosPorSala\", ");
		sql.append(" disciplina.nrMinimoAlunosPorGrupo as \"disciplina.nrMinimoAlunosPorGrupo\", disciplina.nrMaximoAulosPorGrupo as \"disciplina.nrMaximoAulosPorGrupo\", ");
		sql.append(" disciplina.classificacaoDisciplina as \"disciplina.classificacaoDisciplina\", disciplina.idConteudoMasterBlackboard as \"disciplina.idConteudoMasterBlackboard\", ");
		sql.append(" gradecurricularestagio.codigo as \"gradecurricularestagio.codigo\", gradecurricularestagio.nome as \"gradecurricularestagio.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.razaosocial as \"unidadeensino.razaosocial\", ");
		sql.append(" salaaulablackboard.programacaoTutoriaOnline as \"salaaulablackboard.programacaoTutoriaOnline\",  ");		
		
		if(trazColunaQuantidade) {
			sql.append(" (select count(distinct coalesce(salaaulablackboardpessoa.matriculaperiodoturmadisciplina,0) || salaaulablackboardpessoa.matricula) ");
			sql.append(" from salaaulablackboardpessoa  ");
			sql.append(" left join matriculaperiodoturmadisciplina mptd on mptd.codigo = salaaulablackboardpessoa.matriculaperiodoturmadisciplina  ");
			sql.append(" left join historico his on his.matriculaperiodoturmadisciplina =  mptd.codigo  ");
			sql.append(" inner join lateral ( ");
			sql.append("	select mp.codigo, mp.situacaomatriculaperiodo from matriculaPeriodo mp where mp.matricula = salaaulablackboardpessoa.matricula ");  
			sql.append("	and case when salaaulablackboardpessoa.matriculaperiodoturmadisciplina is null then true else mptd.matriculaperiodo = mp.codigo end ");  
			sql.append("	order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ");
			sql.append(" ) as matriculaPeriodo on matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI') ");
			sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
			sql.append(" and case when salaaulablackboardpessoa.matriculaperiodoturmadisciplina is null  then true else his.codigo is not null and his.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') end ");
			sql.append(" ) as qtdeAlunos,  ");
		
			sql.append(" (select count(historiconotablackboard.codigo) from historiconotablackboard where historiconotablackboard.salaaulablackboard = salaaulablackboard.codigo and historiconotablackboard.situacaohistoriconotablackboardenum = 'NAO_LOCALIZADO') as qtdeNotaNaoLocalizada  ");	
		}else {
			sql.append(" 0 as qtdeAlunos, 0 as qtdeNotaNaoLocalizada");
		}
		sql.append(" from salaaulablackboard ");
		sql.append(" left join curso on curso.codigo =  salaaulablackboard.curso ");
		sql.append(" left join agrupamentounidadeensino on agrupamentounidadeensino.codigo =  salaaulablackboard.agrupamentounidadeensino ");
		sql.append(" left join turma on turma.codigo =  salaaulablackboard.turma ");
		sql.append(" left join unidadeensino on unidadeensino.codigo =  turma.unidadeensino ");
		sql.append(" left join disciplina on disciplina.codigo =  salaaulablackboard.disciplina ");
		sql.append(" left join gradecurricularestagio on gradecurricularestagio.codigo =  salaaulablackboard.gradecurricularestagio ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardVO> consultarSeExisteSalaAulaBlackboardComVagaGrupo(Integer curso, Integer agrupamentoUnidadeEnsino, Integer disciplina, String ano, String semestre, Integer bimestre, Integer vagaPorSala, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		List<Object> listaFiltros = new ArrayList<>();
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(true);
		sqlStr.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = ?");
		listaFiltros.add(tipoSalaAulaBlackboardEnum.name());
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ? ");
			listaFiltros.add(disciplina);
		}		
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo =  ? ");
			listaFiltros.add(curso);
		}
		if (Uteis.isAtributoPreenchido(agrupamentoUnidadeEnsino)) {
			sqlStr.append(" and agrupamentounidadeensino.codigo =  ? ");
			listaFiltros.add(agrupamentoUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano = ? ");
			listaFiltros.add(ano);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre = ? ");
			listaFiltros.add(semestre);
		}
		if (Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and salaaulablackboard.bimestre = ? ");
			listaFiltros.add(bimestre);
		}		
		if(Uteis.isAtributoPreenchido(vagaPorSala)) {
			sqlStr.append(" and (select count(sabGrupo.codigo) from salaaulablackboard as sabGrupo ");
			sqlStr.append(" where sabGrupo.id = salaaulablackboard.id and sabGrupo.tipoSalaAulaBlackboardEnum = ? ) < ? ");
			listaFiltros.add(tipoSalaAulaBlackboardEnum.isTcc() ? TipoSalaAulaBlackboardEnum.TCC_GRUPO.name() : TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO.name());
			listaFiltros.add(vagaPorSala);
		}
		sqlStr.append(" order by salaaulablackboard.nrsala ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardEad(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer bimestre, Integer programacaoTutoriaOnline,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(true);
		//sqlStr.append(" left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1 ) ) )");
		sqlStr.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum.name()).append("' ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso).append(" ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo = ").append(turma).append(" ");
		}
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina).append(" ");
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and salaaulablackboard.bimestre = ").append(bimestre).append(" ");
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnline)) {
			sqlStr.append(" and salaaulablackboard.programacaoTutoriaOnline =  ").append(programacaoTutoriaOnline).append(" ");
		}
		sqlStr.append(" order by salaaulablackboard.ano desc, salaaulablackboard.semestre desc, salaaulablackboard.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, Integer bimestre, Integer nrSala, Integer programacaoTutoriaOnline, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo =  ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo =  ").append(turma);
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre ='").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and salaaulablackboard.bimestre = ").append(bimestre).append(" ");
		}
		if(Uteis.isAtributoPreenchido(nrSala)) {
			sqlStr.append(" and salaaulablackboard.nrSala =  ").append(nrSala).append(" ");
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnline)) {
			sqlStr.append(" and salaaulablackboard.programacaoTutoriaOnline =  ").append(programacaoTutoriaOnline).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		return obj;
	}

	@Override
	public Boolean consultarSeExisteAlgumSalaAulaBlackboardTutoriaOnline(PessoaVO pessoa, String ano, String semestre,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaSalaAulaBlackboardTutoriaOnline(pessoa, ano, semestre);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return dadosSQL.next();
	}
	
	@Override
	public Boolean consultarSeExisteAgrupamentoUnidadeEnsino(Integer agrupamentoUnidadeEnsino, boolean isLevantarExcecao,  UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select codigo, nome from salaaulablackboard where agrupamentounidadeensino =? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), agrupamentoUnidadeEnsino);
		boolean existeUnicidade = tabelaResultado.next();
		if (existeUnicidade && !isLevantarExcecao ) {
			return true;
		}else if(existeUnicidade && isLevantarExcecao ) {
			ConsistirException ce = new ConsistirException();
			tabelaResultado.beforeFirst();
			while (tabelaResultado.next()) {
				SalaAulaBlackboardVO sb = new SalaAulaBlackboardVO();
				sb.setCodigo(tabelaResultado.getInt("codigo"));
				sb.setNome(tabelaResultado.getString("nome"));
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SalaAulaBlackboard_existeVinculoAgrupamentoUnidadeEnsino").replace("{0}", sb.getNome().toUpperCase()));
			}
			throw ce;
		}
		return false ;
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultarProximoNrGrupoSalaAulaBlackboard(String id, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardGrupoEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select  max(nrGrupo) as nrGrupo from salaaulablackboard ");
		sqlStr.append(" where id = ? and tipoSalaAulaBlackboardEnum = ?   ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), id, tipoSalaAulaBlackboardGrupoEnum.name());
		if(rs.next()) {
			return rs.getInt("nrGrupo")+1;
		}
		return 1;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultarQtdeGrupoExistenteSalaAulaBlackboard(String id, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardGrupoEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select  count(nrGrupo) as QTDE from salaaulablackboard  ");
		sqlStr.append(" where id = ? and tipoSalaAulaBlackboardEnum = ?   ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), id, tipoSalaAulaBlackboardGrupoEnum.name());
		return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultarProximoNrSalaAulaBlackboard(Integer curso, Integer agrupamentoUnidadeEnsino, Integer disciplina, String ano, String semestre, Integer bimestre, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) throws Exception {

		List<Object> listaFiltros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("select max(salaaulablackboard.nrsala) as nrsala from salaaulablackboard");
		sqlStr.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum = ?");
		listaFiltros.add(tipoSalaAulaBlackboardEnum.name());
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and salaaulablackboard.disciplina = ? ");
			listaFiltros.add(disciplina);
		}
		if (Uteis.isAtributoPreenchido(agrupamentoUnidadeEnsino)) {
			sqlStr.append(" and salaaulablackboard.agrupamentoUnidadeEnsino =  ? ");
			listaFiltros.add(agrupamentoUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and salaaulablackboard.curso =  ? ");
			listaFiltros.add(curso);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano = ? ");
			listaFiltros.add(ano);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre = ? ");
			listaFiltros.add(semestre);
		}
		if (Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and salaaulablackboard.bimestre = ? ");
			listaFiltros.add(bimestre);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("nrsala")+1;
		}
		return 1;
	}
	
//	public Integer consultarQtdAlunosNaoEnsaladosTcc(Integer disciplina, String ano, String semestre) throws Exception {
//		StringBuilder sql = new StringBuilder("select distinct ");
//		sql.append(" 	(select count(matricula) from ( ");
//		sql.append(getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(ano, semestre, TipoSalaAulaBlackboardEnum.TCC_GRUPO, true).toString());
//		sql.append(" 	and gc_disciplinapadraotcc = disciplina.codigo");
//		sql.append(" 	)as tt ");
//		sql.append("  ) as qtdAlunosNaoEnsalados ");
//		sql.append(" 	from disciplina  ");
//		sql.append(" 	inner join gradecurricular gc on gc.disciplinapadraotcc = disciplina.codigo and gc.percentualPermitirIniciarTcc > 0  ");
//		sql.append("    where  disciplina.codigo = ? ");
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina);
//		if(rs.next()) {
//			return rs.getInt("qtdAlunosNaoEnsalados"); 
//		}
//		return 0;
//	}
	
	@Override
	public List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardTutoriaOnline(PessoaVO pessoa, String ano, String semestre,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaSalaAulaBlackboardTutoriaOnline(pessoa, ano, semestre);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
		while (dadosSQL.next()) {
			SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
			obj.setNovoObj(false);
			obj.setCodigo(dadosSQL.getInt("codigoSalaaulablackboard"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setNomeGrupo(dadosSQL.getString("nomeGrupo"));
			obj.setGrupoExternalId(dadosSQL.getString("grupoExternalId"));
			obj.setGrupoSetId(dadosSQL.getString("grupoSetId"));
			obj.setIdGrupo(dadosSQL.getString("idGrupo"));
			obj.setTermId(dadosSQL.getString("termId"));
			obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
			obj.getGradeCurricularEstagioVO().setCodigo(dadosSQL.getInt("gradeCurricularEstagio.codigo"));
			obj.getGradeCurricularEstagioVO().setNome(dadosSQL.getString("gradeCurricularEstagio.nome"));
			obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
			obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			obj.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
			obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaoSocial"));
			obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setSemestre(dadosSQL.getString("semestre"));
			obj.setBimestre(dadosSQL.getInt("bimestre"));
			obj.setId(dadosSQL.getString("id"));
			obj.setNrSala(dadosSQL.getInt("nrSala"));
			obj.setLinkSalaAulaBlackboard(dadosSQL.getString("linkSalaAulaBlackboard"));
			obj.getProgramacaoTutoriaOnlineVO().setCodigo(dadosSQL.getInt("programacaoTutoriaOnline"));
			obj.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(dadosSQL.getString("tipoSalaAulaBlackboardEnum")));

			vetResultado.add(obj);
		}
		return vetResultado;
	}


	private StringBuilder getSqlConsultaSalaAulaBlackboardTutoriaOnline(PessoaVO pessoa, String ano, String semestre) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select ");
		sqlStr.append(" salaaulablackboard.codigo as codigoSalaaulablackboard, salaaulablackboard.idSalaAulaBlackboard,  ");
		sqlStr.append(" salaaulablackboard.id, salaaulablackboard.linkSalaAulaBlackboard, ");
		sqlStr.append(" salaaulablackboard.ano, salaaulablackboard.semestre,  salaaulablackboard.bimestre, ");
		sqlStr.append(" salaaulablackboard.nome, ");
		sqlStr.append(" salaaulablackboard.nomeGrupo, ");
		sqlStr.append(" salaaulablackboard.grupoExternalId, ");
		sqlStr.append(" salaaulablackboard.grupoSetId, ");
		sqlStr.append(" salaaulablackboard.idGrupo, ");
		sqlStr.append(" salaaulablackboard.nrSala, ");
		sqlStr.append(" salaaulablackboard.termId, ");
		sqlStr.append(" salaaulablackboard.tipoSalaAulaBlackboardEnum, ");
		sqlStr.append(" salaaulablackboard.programacaoTutoriaOnline, ");
		sqlStr.append(" gradeCurricularEstagio.codigo as \"gradeCurricularEstagio.codigo\", gradeCurricularEstagio.nome as \"gradeCurricularEstagio.nome\", ");
		sqlStr.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\",   disciplina.abreviatura as \"disciplina.abreviatura\", ");
		sqlStr.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\"  ");
		sqlStr.append(" from salaaulablackboard ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = salaaulablackboard.disciplina ");
		sqlStr.append(" inner join salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum != 'ALUNO' ");
		sqlStr.append(" inner join pessoaEmailInstitucional on salaaulablackboardpessoa.pessoaEmailInstitucional = pessoaEmailInstitucional.codigo ");
		sqlStr.append(" left join turma on turma.codigo = salaaulablackboard.turma  ");
		sqlStr.append(" left join curso on curso.codigo = salaaulablackboard.curso  ");
		sqlStr.append(" left join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" left join gradeCurricularEstagio on gradeCurricularEstagio.codigo = salaaulablackboard.gradeCurricularEstagio  ");
		sqlStr.append(" where pessoaEmailInstitucional.pessoa = ").append(pessoa.getCodigo());
		if(Uteis.isAtributoPreenchido(ano) ) {
			sqlStr.append(" and salaaulablackboard.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre = '").append(semestre).append("'");
		}
		sqlStr.append(" order by \"turma.identificadorTurma\", \"disciplina.nome\" ") ;
		return sqlStr;
	}

	private SalaAulaBlackboardVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("salaaulablackboard.codigo"));
		obj.setId(dadosSQL.getString("salaaulablackboard.id"));
		obj.setIdSalaAulaBlackboard(dadosSQL.getString("salaaulablackboard.idsalaaulablackboard"));
		obj.setLinkSalaAulaBlackboard(dadosSQL.getString("salaaulablackboard.linkSalaAulaBlackboard"));
		obj.setTermId(dadosSQL.getString("salaaulablackboard.termId"));
		obj.setAno(dadosSQL.getString("salaaulablackboard.ano"));
		obj.setSemestre(dadosSQL.getString("salaaulablackboard.semestre"));
		obj.setBimestre(dadosSQL.getInt("salaaulablackboard.bimestre"));
		obj.setNrSala(dadosSQL.getInt("salaaulablackboard.nrsala"));
		obj.setNome(dadosSQL.getString("salaaulablackboard.nome"));
		obj.setNomeGrupo(dadosSQL.getString("salaaulablackboard.nomeGrupo"));
		obj.setGrupoExternalId(dadosSQL.getString("salaaulablackboard.grupoExternalId"));
		obj.setGrupoSetId(dadosSQL.getString("salaaulablackboard.grupoSetId"));
		obj.setIdGrupo(dadosSQL.getString("salaaulablackboard.idGrupo"));
		obj.setNrGrupo(dadosSQL.getInt("salaaulablackboard.nrGrupo"));
		obj.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(dadosSQL.getString("salaaulablackboard.tipoSalaAulaBlackboardEnum")));
		obj.getProgramacaoTutoriaOnlineVO().setCodigo(dadosSQL.getInt("salaaulablackboard.programacaoTutoriaOnline"));
		obj.setQtdeAlunos(dadosSQL.getInt("qtdeAlunos"));
		obj.setQtdeNotaNaoLocalizada(dadosSQL.getInt("qtdeNotaNaoLocalizada"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}

		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("curso.codigo"))) {
			obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
			obj.getCursoVO().setAbreviatura(dadosSQL.getString("curso.abreviatura"));
			obj.getCursoVO().setIdConteudoMasterBlackboardEstagio(dadosSQL.getString("curso.idConteudoMasterBlackboardEstagio"));

		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("agrupamentounidadeensino.codigo"))) {
			obj.getAgrupamentoUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("agrupamentounidadeensino.codigo"));
			obj.getAgrupamentoUnidadeEnsinoVO().setDescricao(dadosSQL.getString("agrupamentounidadeensino.descricao"));
			obj.getAgrupamentoUnidadeEnsinoVO().setAbreviatura(dadosSQL.getString("agrupamentounidadeensino.abreviatura"));
			
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("turma.codigo"))) {
			obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
			obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaosocial"));
		}

		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("disciplina.codigo"))) {
			obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getDisciplinaVO().setAbreviatura(dadosSQL.getString("disciplina.abreviatura"));
			obj.getDisciplinaVO().setNrMinimoAlunosPorSala(dadosSQL.getInt("disciplina.nrMinimoAlunosPorSala"));
			obj.getDisciplinaVO().setNrMaximoAulosPorSala(dadosSQL.getInt("disciplina.nrMaximoAulosPorSala"));
			obj.getDisciplinaVO().setNrMinimoAlunosPorGrupo(dadosSQL.getInt("disciplina.nrMinimoAlunosPorGrupo"));
			obj.getDisciplinaVO().setNrMaximoAulosPorGrupo(dadosSQL.getInt("disciplina.nrMaximoAulosPorGrupo"));
			obj.getDisciplinaVO().setIdConteudoMasterBlackboard(dadosSQL.getString("disciplina.idConteudoMasterBlackboard"));
			if(dadosSQL.getString("disciplina.classificacaoDisciplina") != null) {
				obj.getDisciplinaVO().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(dadosSQL.getString("disciplina.classificacaoDisciplina")));
			}
//			obj.getDisciplinaVO().setModeloGeracaoSalaBlackboard(ModeloGeracaoSalaBlackboardEnum.valueOf(dadosSQL.getString("modeloGeracaoSalaBlackboard")));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("gradecurricularestagio.codigo"))) {
			obj.getGradeCurricularEstagioVO().setCodigo(dadosSQL.getInt("gradecurricularestagio.codigo"));
			obj.getGradeCurricularEstagioVO().setNome(dadosSQL.getString("gradecurricularestagio.nome"));

		}

		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		List<SalaAulaBlackboardPessoaVO> salaAulaBlackboardPessoaVOs = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarPorSalaAulaBlackboard(obj, nivelMontarDados, usuario);
		for (SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO : salaAulaBlackboardPessoaVOs) {
			switch (salaAulaBlackboardPessoaVO.getTipoSalaAulaBlackboardPessoaEnum()) {
				case ALUNO:
					obj.getListaAlunos().add(salaAulaBlackboardPessoaVO);
					break;
				case PROFESSOR:
					obj.getListaProfessores().add(salaAulaBlackboardPessoaVO);
					break;
				case FACILITADOR:
					obj.getListaFacilitadores().add(salaAulaBlackboardPessoaVO);
					break;
				case ORIENTADOR:
					obj.getListaOrientadores().add(salaAulaBlackboardPessoaVO);
					break;
				default:
					break;
			}
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarImportacaoSalaAulaBlackboard(ConfiguracaoSeiBlackboardVO configSeiBlackboardVO, UsuarioVO usuarioVO) throws Exception {
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_IMPORTAR_SALAAULA, RequestMethod.GET, null,  usuarioVO);
		if(jsonResponse.isSuccess()) {
			configSeiBlackboardVO.setImportacaoEmRealizacao(jsonResponse.isSuccess());
		}else {
			throw new Exception(jsonResponse.getStatusText());
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void visualizarTelaFechamentoGrupoSalaAulaBlackboard(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, List<SalaAulaBlackboardGrupoVO> listaFechamento, ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum, DisciplinaVO discipinaVO, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(ano), "O campo ANO deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(semestre), "O campo SEMESTRE deve ser informado.");
		listaFechamento.clear();
		List<Object> listaFiltros = new ArrayList<>();
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(true);
		sqlStr.append(" where salaaulablackboard.tipoSalaAulaBlackboardEnum in ('").append(classificacaoDisciplinaEnum.isTcc() ?  TipoSalaAulaBlackboardEnum.TCC_GRUPO : TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO).append("' )");
		if (Uteis.isAtributoPreenchido(discipinaVO.getCodigo())) {
			sqlStr.append(" and disciplina.codigo = ? ");
			listaFiltros.add(discipinaVO.getCodigo());
		}else if(Uteis.isAtributoPreenchido(calendarioAgrupamentoTccVO)  && !calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs().isEmpty()) {
			sqlStr.append(" and disciplina.codigo in (0 ");
			for(CalendarioAgrupamentoDisciplinaVO calendarioAgrupamentoDisciplinaVO: calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs()) {
				sqlStr.append(", ").append(calendarioAgrupamentoDisciplinaVO.getDisciplinaVO().getCodigo());
			}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano = ? ");
			listaFiltros.add(ano);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre = ? ");
			listaFiltros.add(semestre);
		}
		sqlStr.append(" order by salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
		List<SalaAulaBlackboardVO> lista =  montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, List<SalaAulaBlackboardVO>> map = lista.stream().collect(Collectors.groupingBy(p -> p.getIdSalaAulaBlackboard()));
		for (Map.Entry<String, List<SalaAulaBlackboardVO>> mapa : map.entrySet()) {
			SalaAulaBlackboardGrupoVO sabGrupoFechamento = new SalaAulaBlackboardGrupoVO();
			sabGrupoFechamento.setAno(ano);
			sabGrupoFechamento.setSemestre(semestre);
			sabGrupoFechamento.setIdSalaAulaBlackboard(mapa.getKey());
			sabGrupoFechamento.setListaGrupoSalaAulaBlackboardVO(mapa.getValue());
			if(!sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().isEmpty()) {
				sabGrupoFechamento.setDisciplinaVO(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().get(0).getDisciplinaVO());
				sabGrupoFechamento.setCursoVO(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().get(0).getCursoVO());
				sabGrupoFechamento.setAgrupamentoUnidadeEnsinoVO(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().get(0).getAgrupamentoUnidadeEnsinoVO());
				sabGrupoFechamento.setQtdAlunosEnsalados(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().stream().mapToInt(SalaAulaBlackboardVO::getQtdeAlunos).sum());
				sabGrupoFechamento.setQtdAlunos(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().stream().mapToInt(SalaAulaBlackboardVO::getQtdeAlunos).sum());
			}
			if(classificacaoDisciplinaEnum.isProjetoIntegrador()) {
				sabGrupoFechamento.setVagasSalaAulaBlackboard(sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().isEmpty() ? null : ((sabGrupoFechamento.getListaGrupoSalaAulaBlackboardVO().size() * sabGrupoFechamento.getDisciplinaVO().getNrMaximoAulosPorGrupo()) - sabGrupoFechamento.getQtdAlunosEnsalados()));
				sabGrupoFechamento.setQtdAlunosNaoEnsalados(getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarQtdAlunosNaoEnsaladosProjetoIntegrador(sabGrupoFechamento));
			}
			listaFechamento.add(sabGrupoFechamento);
		}
		if(classificacaoDisciplinaEnum.isTcc()) {
			realizarVerificacaoRemanejamentoPorGrupo(listaFechamento);
			realizarVerificacaoRemanejamentoPorSala(listaFechamento);	
		}
		Ordenacao.ordenarLista(listaFechamento, "idSalaAulaBlackboard");
	}

	private void realizarVerificacaoRemanejamentoPorSala(List<SalaAulaBlackboardGrupoVO> listaFechamento) {
		listaFechamento.stream().forEach(sala-> sala.setQtdAlunos(sala.getListaGrupoSalaAulaBlackboardVO().stream().mapToInt(SalaAulaBlackboardVO::getQtdeAlunos).sum()));
		boolean existeSalaAbaixoMinino = false;
		do {
			//remanejar o grupo abaixo do minino
			Optional<SalaAulaBlackboardGrupoVO> findFirst = listaFechamento.stream()
					.filter(sala-> !sala.getSelecionado() && sala.getQtdAlunos() > 0 && sala.getQtdAlunos() < sala.getDisciplinaVO().getNrMinimoAlunosPorSala())
					.sorted(Comparator.comparing(SalaAulaBlackboardGrupoVO::getQtdAlunos))
					.findFirst();
			existeSalaAbaixoMinino = false;
			if(findFirst.isPresent() && !findFirst.get().getIdSalaAulaBlackboard().isEmpty()) {
				existeSalaAbaixoMinino = true;
				findFirst.get().setSelecionado(true);
				verificarRemanejamentoSalaAdere100PorcentoGrupo100Porcento(listaFechamento,findFirst.get());
//				if(findFirst.get().getQtdAlunos() > 0) {
//					verificarRemanejamentoSalaAdere100PorcentoGrupoNao100Porcento(listaFechamento, findFirst.get());
//				}
//				if(findFirst.get().getQtdAlunos() > 0) {
//					verificarRemanejamentoSalaNaoAdere100PorcentoGrupo100Porcento(listaFechamento, findFirst.get());
//				}
//				if(findFirst.get().getQtdAlunos() > 0) {
//					verificarRemanejamentoSalaNaoAdere100PorcentoGrupoNao100Porcento(listaFechamento, findFirst.get());
//				}
			}
		}while(existeSalaAbaixoMinino);
	}

	private void verificarRemanejamentoSalaAdere100PorcentoGrupo100Porcento(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardGrupoVO sabGrupoVO) {
		listaFechamento.stream()
		.filter(sala-> !sala.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
				&& sala.getDisciplinaVO().getCodigo().equals(sabGrupoVO.getDisciplinaVO().getCodigo())
				&& sala.getCursoVO().getCodigo().equals(sabGrupoVO.getCursoVO().getCodigo())
				&& sala.getAgrupamentoUnidadeEnsinoVO().getCodigo().equals(sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().getCodigo())
				&& sala.getQtdAlunos() > 0
				&& sala.getDisciplinaVO().getNrMaximoAulosPorSala() >= (sala.getQtdAlunos() + sabGrupoVO.getQtdAlunos()))
		.sorted(Comparator.comparing(SalaAulaBlackboardGrupoVO::getQtdAlunos).reversed())
		.forEach(sala -> {
				sabGrupoVO.getListaGrupoSalaAulaBlackboardVO().stream()
				.filter(grupoRemanejar-> grupoRemanejar.getQtdeAlunos() > 0)
				.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos))
				.forEach(grupoRemanejar->{
					sala.getListaGrupoSalaAulaBlackboardVO()
					.stream()
					.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos).reversed())
					.forEach(grupo -> {
						if((grupo.getQtdeAlunos() > 0 && grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= (grupo.getQtdeAlunos() + grupoRemanejar.getQtdeAlunos()))
								|| (grupo.getQtdeAlunos() == 0 && grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= grupoRemanejar.getQtdeAlunos())) {
							realizarTrocaSalaGrupoRemanecente100Porcento(sabGrupoVO, sala, grupoRemanejar, grupo);
						}
					});
				});
		});
	}

//	private void verificarRemanejamentoSalaAdere100PorcentoGrupoNao100Porcento(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardGrupoVO sabGrupoVO) {
//		listaFechamento.stream()
//		.filter(sala-> !sala.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
//				&& sala.getDisciplinaVO().getCodigo().equals(sabGrupoVO.getDisciplinaVO().getCodigo())
//				&& sala.getCursoVO().getCodigo().equals(sabGrupoVO.getCursoVO().getCodigo())
//				&& sala.getAgrupamentoUnidadeEnsinoVO().getCodigo().equals(sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().getCodigo())
//				&& sala.getQtdAlunos() > 0
//				&& sala.getDisciplinaVO().getNrMaximoAulosPorSala() >= (sala.getQtdAlunos() + sabGrupoVO.getQtdAlunos()))
//		.forEach(sala -> {
//			sabGrupoVO.getListaGrupoSalaAulaBlackboardVO().stream().filter(grupoRemanejar-> grupoRemanejar.getQtdeAlunos() > 0).forEach(grupoRemanejar->{
//				sala.getListaGrupoSalaAulaBlackboardVO().stream().forEach(grupo -> {
//					if(grupo.getQtdeAlunos() > 0 && (grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() - grupo.getQtdeAlunos()) > 0) {
//						realizarTrocaSalaGrupoRemanecenteNao100Porcento(sabGrupoVO, sala, grupoRemanejar, grupo);
//					}
//				});
//			});
//		});
//	}


//	private void verificarRemanejamentoSalaNaoAdere100PorcentoGrupo100Porcento(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardGrupoVO sabGrupoVO) {
//		listaFechamento.stream()
//		.filter(sala-> !sala.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
//				&& sala.getDisciplinaVO().getCodigo().equals(sabGrupoVO.getDisciplinaVO().getCodigo())
//				&& sala.getCursoVO().getCodigo().equals(sabGrupoVO.getCursoVO().getCodigo())
//				&& sala.getAgrupamentoUnidadeEnsinoVO().getCodigo().equals(sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().getCodigo())
//				&& sala.getQtdAlunos() > 0
//				&& (sala.getDisciplinaVO().getNrMaximoAulosPorSala() - sala.getQtdAlunos()) > 0)
//		.forEach(sala -> {
//			sabGrupoVO.getListaGrupoSalaAulaBlackboardVO().stream()
//			.filter(grupoRemanejar-> grupoRemanejar.getQtdeAlunos() > 0 && (sala.getDisciplinaVO().getNrMaximoAulosPorSala() - sala.getQtdAlunos()) >= grupoRemanejar.getQtdeAlunos())
//			.forEach(grupoRemanejar->{
//					sala.getListaGrupoSalaAulaBlackboardVO().stream().forEach(grupo -> {
//						if((grupo.getQtdeAlunos() > 0 && grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= (grupo.getQtdeAlunos() + grupoRemanejar.getQtdeAlunos()))
//								|| (grupo.getQtdeAlunos() == 0 && grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= grupoRemanejar.getQtdeAlunos())) {
//							realizarTrocaSalaGrupoRemanecente100Porcento(sabGrupoVO, sala, grupoRemanejar, grupo);
//						}
//					});
//				});
//		});
//	}

//	private void verificarRemanejamentoSalaNaoAdere100PorcentoGrupoNao100Porcento(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardGrupoVO sabGrupoVO) {
//		listaFechamento.stream()
//		.filter(sala-> !sala.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
//				&& sala.getDisciplinaVO().getCodigo().equals(sabGrupoVO.getDisciplinaVO().getCodigo())
//				&& sala.getCursoVO().getCodigo().equals(sabGrupoVO.getCursoVO().getCodigo())
//				&& sala.getAgrupamentoUnidadeEnsinoVO().getCodigo().equals(sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().getCodigo())
//				&& sala.getQtdAlunos() > 0
//				&& (sala.getDisciplinaVO().getNrMaximoAulosPorSala() - sala.getQtdAlunos()) > 0)
//		.forEach(sala -> {
//			sabGrupoVO.getListaGrupoSalaAulaBlackboardVO().stream().filter(grupoRemanejar-> grupoRemanejar.getQtdeAlunos() > 0)
//			.forEach(grupoRemanejar->{
//				sala.getListaGrupoSalaAulaBlackboardVO().stream().forEach(grupo -> {
//					if(grupo.getQtdeAlunos() > 0 && (grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() - grupo.getQtdeAlunos()) > 0) {
//						realizarTrocaSalaGrupoRemanecenteNao100Porcento(sabGrupoVO, sala, grupoRemanejar, grupo);
//					}
//				});
//			});
//		});
//	}

	private void realizarTrocaSalaGrupoRemanecente100Porcento(SalaAulaBlackboardGrupoVO sabGrupoVO, SalaAulaBlackboardGrupoVO sala, SalaAulaBlackboardVO grupoRemanejar, SalaAulaBlackboardVO grupo) {
		sabGrupoVO.setQtdAlunos(sabGrupoVO.getQtdAlunos() - grupoRemanejar.getQtdeAlunos());
		sala.setQtdAlunos(sala.getQtdAlunos() + grupoRemanejar.getQtdeAlunos());
		grupo.setQtdeAlunos(grupo.getQtdeAlunos() + grupoRemanejar.getQtdeAlunos());
		grupoRemanejar.setQtdeAlunos(0);
		realizarTrocaGrupoRemanecente100Porcento(grupoRemanejar, grupo);
	}

//	private void realizarTrocaSalaGrupoRemanecenteNao100Porcento(SalaAulaBlackboardGrupoVO sabGrupoVO, SalaAulaBlackboardGrupoVO sala, SalaAulaBlackboardVO grupoRemanejar, SalaAulaBlackboardVO grupo) {
//		Integer qtdGrupoPermitido =  grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() - grupo.getQtdeAlunos();
//		sabGrupoVO.setQtdAlunos(sabGrupoVO.getQtdAlunos() - qtdGrupoPermitido);
//		sala.setQtdAlunos(sala.getQtdAlunos() + qtdGrupoPermitido);
//		grupo.setQtdeAlunos(grupo.getQtdeAlunos() + qtdGrupoPermitido);
//		grupoRemanejar.setQtdeAlunos(grupoRemanejar.getQtdeAlunos() - qtdGrupoPermitido);
//		realizarTrocaGrupoRemanecenteNao100Porcento(grupoRemanejar, grupo,qtdGrupoPermitido);
//	}



	private void realizarVerificacaoRemanejamentoPorGrupo(List<SalaAulaBlackboardGrupoVO> listaFechamento) {
		boolean existeGrupoAbaixoMinino = false;
		do {
			//remanejar o grupo abaixo do minino
			Optional<SalaAulaBlackboardVO> findFirst = listaFechamento.stream()
					.flatMap(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream())					
					.filter(grupo-> !grupo.isSelecionado() && grupo.getQtdeAlunos() > 0 && grupo.getQtdeAlunos() < grupo.getDisciplinaVO().getNrMinimoAlunosPorGrupo())
					.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos))
					.findFirst();
			existeGrupoAbaixoMinino = false;
			if(findFirst.isPresent() && !findFirst.get().getCodigo().equals(0)) {
				existeGrupoAbaixoMinino = true;
				findFirst.get().setSelecionado(true);
				verificarRemanejamentoMesmaSalaEmOutrosGrupo(listaFechamento,findFirst.get());
				if(findFirst.get().getQtdeAlunos() > 0) {
					listaFechamento.stream().forEach(sala-> sala.setQtdAlunos(sala.getListaGrupoSalaAulaBlackboardVO().stream().mapToInt(ppp -> ppp.getQtdeAlunos()).reduce(0, (a, b) -> (a + b))));
					verificarRemanejamentoOutraSala(listaFechamento, findFirst.get());
				}
			}
		}while(existeGrupoAbaixoMinino);
	}

	private void verificarRemanejamentoMesmaSalaEmOutrosGrupo(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardVO sabGrupoVO) {
		listaFechamento.stream()
		.flatMap(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream())
		.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos).reversed())
		.forEach(grupo -> {
			if(grupo.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
					&& !grupo.getCodigo().equals(sabGrupoVO.getCodigo())
					&& grupo.getQtdeAlunos() > 0
					&& grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= (grupo.getQtdeAlunos() + sabGrupoVO.getQtdeAlunos())) {
				grupo.setQtdeAlunos(grupo.getQtdeAlunos() + sabGrupoVO.getQtdeAlunos());
				sabGrupoVO.setQtdeAlunos(0);
				realizarTrocaGrupoRemanecente100Porcento(sabGrupoVO, grupo);
			}
//			else if(grupo.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
//					&& !grupo.getCodigo().equals(sabGrupoVO.getCodigo())
//					&& grupo.getQtdeAlunos() > 0
//					&& (grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() -grupo.getQtdeAlunos()) > 0) {
//				Integer qtdGrupoPermitido =  grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() - grupo.getQtdeAlunos();
//				grupo.setQtdeAlunos(grupo.getQtdeAlunos() + qtdGrupoPermitido);
//				sabGrupoVO.setQtdeAlunos(sabGrupoVO.getQtdeAlunos() - qtdGrupoPermitido);
//				realizarTrocaGrupoRemanecenteNao100Porcento(sabGrupoVO, grupo,qtdGrupoPermitido);
//			}
		});
	}




	private void verificarRemanejamentoOutraSala(List<SalaAulaBlackboardGrupoVO> listaFechamento, SalaAulaBlackboardVO sabGrupoVO) {
		listaFechamento.stream()
		.filter(sala-> !sala.getIdSalaAulaBlackboard().equals(sabGrupoVO.getIdSalaAulaBlackboard())
				&& sala.getDisciplinaVO().getCodigo().equals(sabGrupoVO.getDisciplinaVO().getCodigo())
				&& sala.getCursoVO().getCodigo().equals(sabGrupoVO.getCursoVO().getCodigo())
				&& sala.getAgrupamentoUnidadeEnsinoVO().getCodigo().equals(sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().getCodigo())
				&& sala.getQtdAlunos() > 0
				&& (sala.getDisciplinaVO().getNrMaximoAulosPorSala() >= (sala.getQtdAlunos() + sabGrupoVO.getQtdeAlunos())))
		.sorted(Comparator.comparing(SalaAulaBlackboardGrupoVO::getQtdAlunos).reversed())
		.flatMap(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream())
		.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos).reversed())
		.forEach(grupo -> {
			if(grupo.getQtdeAlunos() > 0 && grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() >= (grupo.getQtdeAlunos() + sabGrupoVO.getQtdeAlunos())) {
				grupo.setQtdeAlunos(grupo.getQtdeAlunos() + sabGrupoVO.getQtdeAlunos());
				sabGrupoVO.setQtdeAlunos(0);
				realizarTrocaGrupoRemanecente100Porcento(sabGrupoVO, grupo);
			}
//			else if(grupo.getQtdeAlunos() > 0 && (grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() -grupo.getQtdeAlunos()) > 0) {
//					Integer qtdGrupoPermitido =  grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo() - grupo.getQtdeAlunos();
//					grupo.setQtdeAlunos(grupo.getQtdeAlunos() + qtdGrupoPermitido);
//					sabGrupoVO.setQtdeAlunos(sabGrupoVO.getQtdeAlunos() - qtdGrupoPermitido);
//					realizarTrocaGrupoRemanecenteNao100Porcento(sabGrupoVO, grupo, qtdGrupoPermitido);
//			}
		});
	}

	private void realizarTrocaGrupoRemanecente100Porcento(SalaAulaBlackboardVO sabGrupoVO, SalaAulaBlackboardVO grupo) {
		for (Iterator<SalaAulaBlackboardPessoaVO> iterator = sabGrupoVO.getListaAlunos().iterator(); iterator.hasNext();) {
			SalaAulaBlackboardPessoaVO sabp =  iterator.next();
			if(!Uteis.isAtributoPreenchido(sabp.getCodigoGrupoOrigem())) {
				sabp.setCodigoGrupoOrigem(sabGrupoVO.getCodigo());
				sabp.setNomeGrupoOrigem(sabGrupoVO.getNomeGrupo());
			}
			sabp.setNomeGrupoRemanescente(sabGrupoVO.getNomeGrupo());
			sabp.setSalaAulaBlackboardVO(grupo);
			grupo.getListaAlunos().add(sabp);
			iterator.remove();
		}
		Ordenacao.ordenarListaDecrescente(grupo.getListaAlunos(), "nomeGrupoOrigem");
	}


//	private void realizarTrocaGrupoRemanecenteNao100Porcento(SalaAulaBlackboardVO sabGrupoVO, SalaAulaBlackboardVO grupo, Integer qtdGrupoPermitido) {
//		int i = 0;
//		Iterator<SalaAulaBlackboardPessoaVO> iterator = sabGrupoVO.getListaAlunos().iterator();
//		while (iterator.hasNext() && i < qtdGrupoPermitido) {
//			SalaAulaBlackboardPessoaVO sabp = (SalaAulaBlackboardPessoaVO) iterator.next();
//			if(!Uteis.isAtributoPreenchido(sabp.getCodigoGrupoOrigem())) {
//				sabp.setCodigoGrupoOrigem(sabGrupoVO.getCodigo());
//				sabp.setNomeGrupoOrigem(sabGrupoVO.getNomeGrupo());
//			}
//			sabp.setNomeGrupoRemanescente(sabGrupoVO.getNomeGrupo());
//			sabp.setSalaAulaBlackboardVO(grupo);
//			grupo.getListaAlunos().add(sabp);
//			iterator.remove();
//			i++;
//		}
//		Ordenacao.ordenarListaDecrescente(grupo.getListaAlunos(), "nomeGrupoRemanescente");
//	}

	@Override
	public List<SalaAulaBlackboardGrupoVO> consultarSalaAulaMontagemSalaTccGrupo(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, DisciplinaVO discipinaVO, String ano, String semestre, String nivelEducacional, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	select curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");

		sql.append(" 	disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sql.append(" 	disciplina.abreviatura as \"disciplina.abreviatura\", disciplina.classificacaoDisciplina as \"disciplina.classificacaoDisciplina\",  ");
		sql.append(" 	disciplina.nrminimoalunosporsala as \"disciplina.nrminimoalunosporsala\", disciplina.nrmaximoaulosporsala as \"disciplina.nrmaximoaulosporsala\",  ");
		sql.append(" 	disciplina.nrminimoalunosporgrupo as \"disciplina.nrminimoalunosporgrupo\", disciplina.nrMaximoAulosPorGrupo as \"disciplina.nrMaximoAulosPorGrupo\",  ");
		sql.append(" 	disciplina.fonteDeDadosBlackboard as \"disciplina.fonteDeDadosBlackboard\", ");

//		sql.append(" 	(select count(distinct matricula.matricula) from matricula ");
//		sql.append(" 			inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
//		sql.append(" 			where matricula.curso = curso.codigo and matriculaperiodo.situacaomatriculaperiodo = 'AT'");
//		sql.append(" 			and matriculaperiodo.ano = '").append(ano).append("' ");
//		sql.append(" 			and matriculaperiodo.semestre = '").append(semestre).append("' ");
//		sql.append(" 	) as qtdAlunos, ");
		
//		sql.append(" 	(select count(matricula) from ( ");
//		sql.append(getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(ano, semestre, TipoSalaAulaBlackboardEnum.TCC_GRUPO, false).toString());
//		sql.append(" 	and codigo_curso = curso.codigo ");
//		sql.append(" 	and gc_disciplinapadraotcc = disciplina.codigo");
//		sql.append(" 	)as tt ");
//		sql.append("  ) as qtdAlunos, ");

		sql.append(" 	(select count(distinct salaaulablackboardpessoa.codigo) from salaaulablackboardpessoa  ");
		sql.append(" 			inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo  and salaaulablackboard.curso = curso.codigo ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'TCC_GRUPO' ");
		sql.append(" 			and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
		sql.append(" 			and salaaulablackboard.ano = '").append(ano).append("' ");
		sql.append(" 			and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 	) as qtdEnsalados, ");
		
		sql.append(" 	(select count(matricula) from ( ");
		sql.append(getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(ano, semestre, TipoSalaAulaBlackboardEnum.TCC_GRUPO, true).toString());
		sql.append(" 	and codigo_curso = curso.codigo ");
		sql.append(" 	and gc_disciplinapadraotcc = disciplina.codigo");
		sql.append(" 	)as tt ");
		sql.append("  ) as qtdAlunosNaoEnsalados, ");

		sql.append(" 	(select count(distinct salaaulablackboard.id) from salaaulablackboard");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo  and salaaulablackboard.curso = curso.codigo ");
		sql.append(" 			and salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'TCC' ");
		sql.append(" 	) as qtdSala, ");

		sql.append(" 	(select count(distinct salaaulablackboard.idgrupo) from salaaulablackboard");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo  and salaaulablackboard.curso = curso.codigo ");
		sql.append(" 			and salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'TCC_GRUPO' ");
		sql.append(" 	) as qtdGroup ");

		sql.append(" 	from curso  ");
		sql.append(" 	inner join gradecurricular gc on gc.curso = curso.codigo and gc.percentualPermitirIniciarTcc > 0  ");
		sql.append(" 	inner join disciplina on gc.disciplinapadraotcc = disciplina.codigo ");
		sql.append(" 	where 1=1 ");
		sql.append(" 	");
		if(Uteis.isAtributoPreenchido(discipinaVO)) {
			sql.append(" and disciplina.codigo = ").append(discipinaVO.getCodigo());
		}else if(calendarioAgrupamentoTccVO.getClassificacaoAgrupamento().isTcc() && !calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs().isEmpty()) {
			sql.append(" and disciplina.codigo in (0");
			for(CalendarioAgrupamentoDisciplinaVO obj: calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs()) {
				sql.append(", ").append(obj.getDisciplinaVO().getCodigo()).append("");
			}
			sql.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" and disciplina.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sql.append(" 	group by curso.codigo, curso.nome,  ");
		sql.append(" 	disciplina.codigo, disciplina.nome, ");
		sql.append(" 	disciplina.abreviatura, disciplina.classificacaoDisciplina, ");
		sql.append(" 	disciplina.fonteDeDadosBlackboard, ");
		sql.append(" 	disciplina.nrminimoalunosporsala, disciplina.nrmaximoaulosporsala, ");
		sql.append(" 	disciplina.nrminimoalunosporgrupo, disciplina.nrmaximoaulosporgrupo  ");

		sql.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaAulaBlackboardGrupoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			SalaAulaBlackboardGrupoVO sabGrupoVO = new SalaAulaBlackboardGrupoVO();
			sabGrupoVO.setAno(ano);
			sabGrupoVO.setSemestre(semestre);
			sabGrupoVO.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
			sabGrupoVO.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));


			sabGrupoVO.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			sabGrupoVO.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			sabGrupoVO.getDisciplinaVO().setAbreviatura(tabelaResultado.getString("disciplina.abreviatura"));
			sabGrupoVO.getDisciplinaVO().setFonteDeDadosBlackboard(tabelaResultado.getString("disciplina.fonteDeDadosBlackboard"));
			sabGrupoVO.getDisciplinaVO().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(tabelaResultado.getString("disciplina.classificacaodisciplina")));
			sabGrupoVO.getDisciplinaVO().setNrMinimoAlunosPorSala(tabelaResultado.getInt("disciplina.nrMinimoAlunosPorSala"));
			sabGrupoVO.getDisciplinaVO().setNrMaximoAulosPorSala(tabelaResultado.getInt("disciplina.nrMaximoAulosPorSala"));
			sabGrupoVO.getDisciplinaVO().setNrMinimoAlunosPorGrupo(tabelaResultado.getInt("disciplina.nrminimoalunosporgrupo"));
			sabGrupoVO.getDisciplinaVO().setNrMaximoAulosPorGrupo(tabelaResultado.getInt("disciplina.nrMaximoAulosPorGrupo"));


			
			sabGrupoVO.setQtdAlunosEnsalados(tabelaResultado.getInt("qtdEnsalados"));
			sabGrupoVO.setQtdAlunosNaoEnsalados(tabelaResultado.getInt("qtdAlunosNaoEnsalados"));
			
			sabGrupoVO.setQtdAlunos(sabGrupoVO.getQtdAlunosEnsalados() + sabGrupoVO.getQtdAlunosNaoEnsalados());

			sabGrupoVO.setVagasSalaAulaBlackboardExistentes(tabelaResultado.getInt("qtdSala"));
			sabGrupoVO.setVagasSalaAulaBlackboardGrupoExistentes(tabelaResultado.getInt("qtdGroup"));

			BigDecimal bigCalculo = BigDecimal.ZERO;
			bigCalculo = bigCalculo.add(new BigDecimal(sabGrupoVO.getQtdAlunos().toString()));

			if(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorSala() > 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardNecessario(bigCalculo.divide(new BigDecimal(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorSala().toString()), BigDecimal.ROUND_UP).intValue());
			}
			if(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorGrupo() > 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardGrupoNecessario(bigCalculo.divide(new BigDecimal(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorGrupo().toString()), BigDecimal.ROUND_UP).intValue());
			}

			sabGrupoVO.setVagasSalaAulaBlackboardNovo(sabGrupoVO.getVagasSalaAulaBlackboardNecessario() - sabGrupoVO.getVagasSalaAulaBlackboardExistentes());
			if(sabGrupoVO.getVagasSalaAulaBlackboardNovo() < 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardNovo(0);
			}
			sabGrupoVO.setVagasSalaAulaBlackboardGrupoNovo(sabGrupoVO.getVagasSalaAulaBlackboardGrupoNecessario() - sabGrupoVO.getVagasSalaAulaBlackboardGrupoExistentes());
			if(sabGrupoVO.getVagasSalaAulaBlackboardGrupoNovo() < 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardGrupoNovo(0);
			}
			lista.add(sabGrupoVO);
		}
		return lista;
	}
	
	@Override
	public List<SalaAulaBlackboardGrupoVO> consultarSalaAulaMontagemSalaProjetoIntegrador(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, Integer disciplina, String ano, String semestre, Integer bimestre, String nivelEducacional) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct agrupamentounidadeensino.codigo as \"agrupamentounidadeensino.codigo\", agrupamentounidadeensino.descricao as \"agrupamentounidadeensino.descricao\",  agrupamentounidadeensino.abreviatura as \"agrupamentounidadeensino.abreviatura\", ");
		
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sql.append(" disciplina.abreviatura as \"disciplina.abreviatura\", disciplina.classificacaoDisciplina as \"disciplina.classificacaoDisciplina\",  ");
		sql.append(" disciplina.nrminimoalunosporsala as \"disciplina.nrminimoalunosporsala\", disciplina.nrmaximoaulosporsala as \"disciplina.nrmaximoaulosporsala\",  ");
		sql.append(" disciplina.nrminimoalunosporgrupo as \"disciplina.nrminimoalunosporgrupo\", disciplina.nrMaximoAulosPorGrupo as \"disciplina.nrMaximoAulosPorGrupo\",  ");
		sql.append(" disciplina.fonteDeDadosBlackboard as \"disciplina.fonteDeDadosBlackboard\", ");
		
		sql.append(" count(matriculaperiodoturmadisciplina.codigo)::INT  as qtdAlunos,");
		
		sql.append(" 	sum(case when exists (select salaaulablackboardpessoa.codigo from salaaulablackboardpessoa  ");
		sql.append(" 			inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo  ");
		sql.append(" 			and salaaulablackboard.agrupamentounidadeensino = agrupamentounidadeensino.codigo  ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_GRUPO' ");
		sql.append(" 			and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' and salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" 		    and salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 	) then 1 else 0 end)  as qtdEnsalados, ");
		
		sql.append(" 	(select count(distinct salaaulablackboard.id) from salaaulablackboard");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo and salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 			and salaaulablackboard.agrupamentounidadeensino = agrupamentounidadeensino.codigo ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR' ");
		sql.append(" 	) as qtdSala, ");

		sql.append(" 	(select count(distinct salaaulablackboard.idgrupo) from salaaulablackboard");
		sql.append(" 			where salaaulablackboard.disciplina = disciplina.codigo and salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" 			and salaaulablackboard.agrupamentounidadeensino = agrupamentounidadeensino.codigo ");
		sql.append(" 			and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_GRUPO' ");
		sql.append(" 	) as qtdGroup ");
		sql.append(" from matriculaperiodoturmadisciplina ");
		sql.append(" inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append(" inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" inner join agrupamentounidadeensinoitem on agrupamentounidadeensinoitem.unidadeensino = matricula.unidadeensino ");
		sql.append(" inner join agrupamentounidadeensino on agrupamentounidadeensino.codigo = agrupamentounidadeensinoitem.agrupamentounidadeensino and agrupamentounidadeensino.statusativoinativoenum = 'ATIVO' ");
		sql.append(" where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'CO') ");
		sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		sql.append(" and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') ");
		sql.append(" and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR' ");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo =  ").append(disciplina);	
		}else if(calendarioAgrupamentoTccVO.getClassificacaoAgrupamento().isProjetoIntegrador() && !calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs().isEmpty()) {
			sql.append(" and disciplina.codigo in (0");
			for(CalendarioAgrupamentoDisciplinaVO obj: calendarioAgrupamentoTccVO.getCalendarioAgrupamentoDisciplinaVOs()) {
				sql.append(", ").append(obj.getDisciplinaVO().getCodigo()).append("");
			}
			sql.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" and disciplina.nivelEducacional =  '").append(nivelEducacional).append("' ");	
		}
		sql.append(" group by agrupamentounidadeensino.codigo, agrupamentounidadeensino.descricao, agrupamentounidadeensino.abreviatura, ");
		sql.append(" 	disciplina.codigo, disciplina.nome, disciplina.abreviatura, disciplina.classificacaodisciplina, ");
		sql.append(" 	disciplina.nrminimoalunosporsala , disciplina.nrmaximoaulosporsala , disciplina.nrminimoalunosporgrupo , ");
		sql.append(" 	disciplina.nrMaximoAulosPorGrupo , disciplina.fonteDeDadosBlackboard ");
		sql.append(" order by disciplina.nome	");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaAulaBlackboardGrupoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			SalaAulaBlackboardGrupoVO sabGrupoVO = new SalaAulaBlackboardGrupoVO();
			sabGrupoVO.setAno(ano);
			sabGrupoVO.setSemestre(semestre);
			sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("agrupamentounidadeensino.codigo"));
			sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().setDescricao(tabelaResultado.getString("agrupamentounidadeensino.descricao"));
			sabGrupoVO.getAgrupamentoUnidadeEnsinoVO().setAbreviatura(tabelaResultado.getString("agrupamentounidadeensino.abreviatura"));


			sabGrupoVO.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			sabGrupoVO.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			sabGrupoVO.getDisciplinaVO().setAbreviatura(tabelaResultado.getString("disciplina.abreviatura"));
			sabGrupoVO.getDisciplinaVO().setFonteDeDadosBlackboard(tabelaResultado.getString("disciplina.fonteDeDadosBlackboard"));
			sabGrupoVO.getDisciplinaVO().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(tabelaResultado.getString("disciplina.classificacaodisciplina")));
			sabGrupoVO.getDisciplinaVO().setNrMinimoAlunosPorSala(tabelaResultado.getInt("disciplina.nrMinimoAlunosPorSala"));
			sabGrupoVO.getDisciplinaVO().setNrMaximoAulosPorSala(tabelaResultado.getInt("disciplina.nrMaximoAulosPorSala"));
			sabGrupoVO.getDisciplinaVO().setNrMinimoAlunosPorGrupo(tabelaResultado.getInt("disciplina.nrminimoalunosporgrupo"));
			sabGrupoVO.getDisciplinaVO().setNrMaximoAulosPorGrupo(tabelaResultado.getInt("disciplina.nrMaximoAulosPorGrupo"));


			sabGrupoVO.setQtdAlunos(tabelaResultado.getInt("qtdAlunos"));
			sabGrupoVO.setQtdAlunosEnsalados(tabelaResultado.getInt("qtdEnsalados"));
			sabGrupoVO.setQtdAlunosNaoEnsalados(sabGrupoVO.getQtdAlunos()-sabGrupoVO.getQtdAlunosEnsalados());
			if(sabGrupoVO.getQtdAlunosNaoEnsalados() < 0) {
				sabGrupoVO.setQtdAlunosNaoEnsalados(0);
			}
			sabGrupoVO.setVagasSalaAulaBlackboardExistentes(tabelaResultado.getInt("qtdSala"));
			sabGrupoVO.setVagasSalaAulaBlackboardGrupoExistentes(tabelaResultado.getInt("qtdGroup"));

			BigDecimal bigCalculo = BigDecimal.ZERO;
			bigCalculo = bigCalculo.add(new BigDecimal(sabGrupoVO.getQtdAlunos().toString()));
			
			if(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorSala() > 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardNecessario(bigCalculo.divide(new BigDecimal(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorSala().toString()), BigDecimal.ROUND_UP).intValue());
			}
			if(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorGrupo() > 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardGrupoNecessario(bigCalculo.divide(new BigDecimal(sabGrupoVO.getDisciplinaVO().getNrMaximoAulosPorGrupo().toString()), BigDecimal.ROUND_UP).intValue());
			}

			sabGrupoVO.setVagasSalaAulaBlackboardNovo(sabGrupoVO.getVagasSalaAulaBlackboardNecessario() - sabGrupoVO.getVagasSalaAulaBlackboardExistentes());
			if(sabGrupoVO.getVagasSalaAulaBlackboardNovo() < 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardNovo(0);
			}
			sabGrupoVO.setVagasSalaAulaBlackboardGrupoNovo(sabGrupoVO.getVagasSalaAulaBlackboardGrupoNecessario() - sabGrupoVO.getVagasSalaAulaBlackboardGrupoExistentes());
			if(sabGrupoVO.getVagasSalaAulaBlackboardGrupoNovo() < 0) {
				sabGrupoVO.setVagasSalaAulaBlackboardGrupoNovo(0);
			}
			lista.add(sabGrupoVO);
		}
		return lista;
	}	
	
	
	@Override
	public List<SalaAulaBlackboardVO> consultarSalaAulaMontagemSalaEstagio(CursoVO curso, String ano, String semestre, String nivelEducacional, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" 	select curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.abreviatura as \"curso.abreviatura\", ");

		sql.append(" 	(select count(distinct matricula.matricula) from matricula ");
		sql.append(" 			inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" 			where matricula.curso = curso.codigo and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI')");
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" 		and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" 		and matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		sql.append(" 	) as qtdAlunos,");

		sql.append(" 	( select count(distinct salaaulablackboard.codigo) from salaaulablackboard ");
		sql.append(" 		where salaaulablackboard.curso = curso.codigo and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'ESTAGIO' ");
		sql.append(" 		and ((salaaulablackboard.importado = false ");
		sql.append(" 		and coalesce (salaaulablackboard.ano, '') = '' ");
		sql.append(" 		and coalesce (salaaulablackboard.semestre, '') = '') ");
			if(Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" or salaaulablackboard.ano = '").append(ano).append("' ");
			}
			if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" or (salaaulablackboard.ano = '").append(ano).append("' ");
				sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("') ");
			}	
		sql.append("	)) as qtdSala, ");		
		
		sql.append(" 	(select count(distinct salaaulablackboardpessoa.codigo) from salaaulablackboardpessoa  ");
		sql.append(" 			inner join salaaulablackboard on  salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard and salaaulablackboard.tipoSalaAulaBlackboardEnum = 'ESTAGIO' ");
		sql.append(" 			where salaaulablackboard.curso = curso.codigo ");
		sql.append(" 			and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
		sql.append(" 		    and ((salaaulablackboard.importado = false ");
		sql.append(" 		    and coalesce (salaaulablackboard.ano, '') = '' ");
		sql.append(" 		    and coalesce (salaaulablackboard.semestre, '') = '') ");
			if(Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" or salaaulablackboard.ano = '").append(ano).append("' ");
			}
			if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" or (salaaulablackboard.ano = '").append(ano).append("' ");
				sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("') ");
			}
		sql.append(" 	)) as qtdEnsalados,");
		
		sql.append(" 	(select count(matricula) from ( ");
		sql.append(getSqlConsultaDeVerificacaoSeMatriculaAptaEstagio(ano, semestre).toString());
		sql.append(" 	and codigo_curso = curso.codigo ");
		sql.append(" 	)as tt  ");
		sql.append("  ) as qtdAlunosNaoEnsalados");
		sql.append(" 	from curso  ");
		sql.append(" 	inner join gradecurricular on gradecurricular.curso = curso.codigo ");
		sql.append(" 	where 1=1 ");
		sql.append(" 	and gradecurricular.percentualPermitirIniciarEstagio > 0 ");
		if(Uteis.isAtributoPreenchido(curso)) {
			sql.append(" 	and curso.codigo = ").append(curso.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" 	and curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sql.append(" group by curso.codigo, curso.nome, curso.abreviatura ");
		sql.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaAulaBlackboardVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			SalaAulaBlackboardVO sabGrupoVO = new SalaAulaBlackboardVO();
			sabGrupoVO.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
			sabGrupoVO.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));
			sabGrupoVO.getCursoVO().setAbreviatura(tabelaResultado.getString("curso.abreviatura"));
			sabGrupoVO.setQtdeSalas(tabelaResultado.getInt("qtdSala"));
			sabGrupoVO.setQtdeAlunos(tabelaResultado.getInt("qtdAlunos"));
			sabGrupoVO.setQtdAlunosEnsalados(tabelaResultado.getInt("qtdEnsalados"));
			sabGrupoVO.setQtdAlunosNaoEnsalados(tabelaResultado.getInt("qtdAlunosNaoEnsalados"));
			lista.add(sabGrupoVO);
		}
		return lista;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosTccPorSalaAulaBlackboardGrupo(SalaAulaBlackboardGrupoVO salaAulaBlackboardGrupo, String acao) throws Exception{
		StringBuilder sql =  new StringBuilder("");
		if (acao.equals("AlunoNaoEnsalado") || acao.equals("Qtde_Aluno")){
			sql.append(" select matricula, 0 as codigo, ");
			sql.append(" codigo_pessoa as codigo_aluno, nome_pessoa as nome_aluno,  ");
			sql.append(" 0 as codigo_sala, '' as nome_sala,  ");
			sql.append(" nome_curso, unidadeensino_nome as nome_unidadeEnsino, ");
			sql.append(" pessoaemailinstitucional.email ");
			sql.append(" from ( ");
			sql.append(getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(salaAulaBlackboardGrupo.getAno(), salaAulaBlackboardGrupo.getSemestre(), TipoSalaAulaBlackboardEnum.TCC_GRUPO, true).toString());
			sql.append(" and gc_disciplinapadraotcc = ").append(salaAulaBlackboardGrupo.getDisciplinaVO().getCodigo());
			sql.append(" )as tt ");
			sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = codigo_pessoa and pessoaemailinstitucional.statusAtivoInativoEnum =  'ATIVO'  ");
			sql.append(" and pessoaemailinstitucional.codigo = (select p.codigo from pessoaemailinstitucional as p where p.pessoa = codigo_pessoa and p.statusAtivoInativoEnum =  'ATIVO' order by p.codigo desc limit 1) ");
		}
		if(acao.equals("Qtde_Aluno")){
			sql.append(" union all ");	
		}
		if(acao.equals("AlunoEnsalado") || acao.equals("Qtde_Aluno")){
			sql.append("select distinct matricula.matricula, 0 as codigo, ");
			sql.append(" matricula.aluno as codigo_aluno, pessoa.nome as nome_aluno, ");
			sql.append(" 0 as codigo_sala, '' as nome_sala,  ");
			sql.append(" curso.nome as nome_curso, unidadeEnsino.nome as nome_unidadeEnsino, pessoaemailinstitucional.email  ");
			sql.append(" from matricula ");
			sql.append(" inner join curso on matricula.curso = curso.codigo ");
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  ");
			sql.append(" inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
			sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
			sql.append(" inner join gradecurricular gc on gc.codigo = matricula.gradecurricularatual and gc.percentualPermitirIniciarTcc > 0  ");
			sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.statusAtivoInativoEnum =  'ATIVO'  ");
			sql.append(" and pessoaemailinstitucional.codigo = (select p.codigo from pessoaemailinstitucional as p where p.pessoa = pessoa.codigo and p.statusAtivoInativoEnum =  'ATIVO' order by p.codigo desc limit 1) ");
			sql.append(" where matriculaperiodo.ano = '").append(salaAulaBlackboardGrupo.getAno()).append("' ");
			sql.append(" and matriculaperiodo.semestre = '").append(salaAulaBlackboardGrupo.getSemestre()).append("' ");
			sql.append(" and gc.disciplinapadraotcc = ").append(salaAulaBlackboardGrupo.getDisciplinaVO().getCodigo());
			sql.append(" and exists (");
			sql.append("	select sap.codigo from salaaulablackboardpessoa sap ");
			sql.append("	inner join salaaulablackboard on salaaulablackboard.codigo  = sap.salaaulablackboard ");
			sql.append("	where sap.matricula = matricula.matricula ");
			sql.append(" 	and sap.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
			sql.append("	and salaaulablackboard.curso = curso.codigo ");
			sql.append("	and salaaulablackboard.disciplina = gc.disciplinapadraotcc ");
			sql.append("    and salaaulablackboard.ano = '").append(salaAulaBlackboardGrupo.getAno()).append("' ");
			sql.append("    and salaaulablackboard.semestre = '").append(salaAulaBlackboardGrupo.getSemestre()).append("' ");
			sql.append("	and salaaulablackboard.tiposalaaulablackboardenum = 'TCC_GRUPO' ) ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosMPTD(rs);
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosProjetoIntegradorPorSalaAulaBlackboardGrupo(SalaAulaBlackboardGrupoVO salaAulaBlackboardGrupo, String acao) throws Exception{
		StringBuilder sql =  new StringBuilder("select distinct matriculaperiodoturmadisciplina.matricula, matricula.aluno as codigo_aluno, matriculaperiodoturmadisciplina.codigo, pessoa.nome as nome_aluno, ");
		sql.append(" 0 as codigo_sala, '' as nome_sala,  ");
		sql.append(" curso.nome as nome_curso, unidadeEnsino.nome as nome_unidadeEnsino, pessoaemailinstitucional.email  ");
		sql.append(" from matriculaperiodoturmadisciplina ");		
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo  ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula  ");
		sql.append(" inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join agrupamentounidadeensinoitem on agrupamentounidadeensinoitem.unidadeensino = matricula.unidadeensino ");
		sql.append(" inner join agrupamentounidadeensino on agrupamentounidadeensino.codigo = agrupamentounidadeensinoitem.agrupamentounidadeensino and agrupamentounidadeensino.statusativoinativoenum = 'ATIVO' ");
		sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.statusAtivoInativoEnum =  'ATIVO'  ");
		sql.append(" and pessoaemailinstitucional.codigo = (select p.codigo from pessoaemailinstitucional as p where p.pessoa = pessoa.codigo and p.statusAtivoInativoEnum =  'ATIVO' order by p.codigo desc limit 1) ");		
		sql.append(" where matriculaperiodoturmadisciplina.ano = '").append(salaAulaBlackboardGrupo.getAno()).append("' ");
		sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(salaAulaBlackboardGrupo.getSemestre()).append("' ");
		sql.append(" and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') ");
		sql.append(" and matriculaperiodoturmadisciplina.disciplina =  ").append(salaAulaBlackboardGrupo.getDisciplinaVO().getCodigo());
		sql.append(" and agrupamentounidadeensino.codigo =  ").append(salaAulaBlackboardGrupo.getAgrupamentoUnidadeEnsinoVO().getCodigo());
		if(acao.equals("AlunoNaoEnsalado") || acao.equals("Qtde_Aluno")) {
			sql.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'CO')");
		}
		if(acao.equals("AlunoNaoEnsalado") || acao.equals("AlunoEnsalado")) {
			if(acao.equals("AlunoEnsalado")) {
				sql.append(" and exists  (");
			}else {
				sql.append(" and not exists  (");	
			}
			sql.append("	select sap.codigo from salaaulablackboardpessoa sap ");
			sql.append("	inner join salaaulablackboard on salaaulablackboard.codigo  = sap.salaaulablackboard ");
			sql.append("	where sap.matricula = matriculaperiodoturmadisciplina.matricula ");
			sql.append("	and sap.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
			sql.append("	and salaaulablackboard.disciplina = matriculaperiodoturmadisciplina.disciplina	");
			sql.append(" 	and sap.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
			sql.append("	and salaaulablackboard.agrupamentounidadeensino =  ").append(salaAulaBlackboardGrupo.getAgrupamentoUnidadeEnsinoVO().getCodigo());
			sql.append("	and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_GRUPO' ");
			sql.append("	and salaaulablackboard.ano = matriculaperiodoturmadisciplina.ano ");
			sql.append("	and salaaulablackboard.semestre = matriculaperiodoturmadisciplina.semestre ");
			sql.append(") ");	
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosMPTD(rs);
	}
	
	



	@Override
	public void consultarSalaAulaImportada(DataModelo controleConsulta, String nomeSala, String nomeGrupo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica(true);
		sql.append(" where salaaulablackboard.importado ");
		if(Uteis.isAtributoPreenchido(nomeSala)) {
			sql.append(" and sem_acentos(salaaulablackboard.nome) ilike sem_acentos(?)  ");
		}
		if(Uteis.isAtributoPreenchido(nomeGrupo)) {
			sql.append(" and sem_acentos(salaaulablackboard.nomeGrupo) ilike sem_acentos(?)  ");
		}
		sql.append(" order by salaaulablackboard.nome ");
		sql.append(" limit ").append(controleConsulta.getLimitePorPagina());
		sql.append(" offset ").append(controleConsulta.getOffset());

		SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(nomeSala) && Uteis.isAtributoPreenchido(nomeGrupo)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeSala+"%", nomeGrupo+"%");
		}else if(Uteis.isAtributoPreenchido(nomeSala)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeSala+"%");
		}else if(Uteis.isAtributoPreenchido(nomeGrupo)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeGrupo+"%");
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}

		montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
		controleConsulta.setListaConsulta(montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			alterar(obj, usuarioVO);
		} else {
			incluir(obj, usuarioVO);
		}
		if(Uteis.isAtributoPreenchido(obj.getDisciplinaVO())) {
		for(SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO: obj.getListaAlunos()) {
			if(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina()) ) {
				StringBuilder sql = new StringBuilder("");
				sql.append(" select distinct matriculaperiodoturmadisciplina.codigo, matricula.matricula, matricula.aluno, pessoaemailinstitucional.codigo as pessoaemailinstitucional, pessoaemailinstitucional.email from matricula ");
				sql.append(" inner join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula");
				sql.append(" inner join curso on curso.codigo = matricula.curso");
				sql.append(" inner join historico on historico.matriculaperiodo = matriculaperiodo.codigo");
				sql.append(" left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
				sql.append(" and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
				sql.append(" inner join pessoa  on pessoa.codigo = matricula.aluno ");
				sql.append(" inner join pessoaemailinstitucional on pessoa.codigo = pessoaemailinstitucional.pessoa ");
				sql.append(" where ");
				sql.append(" ((curso.periodicidade = 'SE' and matriculaperiodo.ano = '");
				sql.append(obj.getAno()).append("' and matriculaperiodo.semestre = '");
				sql.append(obj.getSemestre()).append("') ");
				sql.append(" or (curso.periodicidade = 'AN' and matriculaperiodo.ano = '");
				sql	.append(obj.getAno()).append("') ");
				sql.append(" or (curso.periodicidade = 'IN')) ");
				sql.append(" and historico.disciplina = ");
				sql.append(obj.getDisciplinaVO().getCodigo());
				sql.append(" and pessoaemailinstitucional.email = ('").append(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()).append("')");
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				if(rs.next()) {
					salaAulaBlackboardPessoaVO.setMatricula(rs.getString("matricula"));
					salaAulaBlackboardPessoaVO.setMatriculaPeriodoTurmaDisciplina(rs.getInt("codigo"));
					salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().setCodigo(rs.getInt("pessoaemailinstitucional"));
					getFacadeFactory().getSalaAulaBlackboardPessoaFacade().persistir(salaAulaBlackboardPessoaVO, usuarioVO);
				}
			}
		}
		}
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, "salaaulablackboard", new AtributoPersistencia()
				.add("curso", obj.getCursoVO())
				.add("gradeCurricularEstagio", obj.getGradeCurricularEstagioVO())
				.add("turma", obj.getTurmaVO())
				.add("disciplina", obj.getDisciplinaVO())
				.add("programacaoTutoriaOnline", obj.getProgramacaoTutoriaOnlineVO())
				.add("ano", obj.getAno())
				.add("semestre", obj.getSemestre())
				.add("bimestre", obj.getBimestre())
				.add("idSalaAulaBlackboard", obj.getIdSalaAulaBlackboard())
				.add("linkSalaAulaBlackboard", obj.getLinkSalaAulaBlackboard())
				.add("termId", obj.getTermId())
				.add("tipoSalaAulaBlackboardEnum", obj.getTipoSalaAulaBlackboardEnum())
				.add("nrSala", obj.getNrSala())
				.add("id", obj.getId())
				.add("nome", obj.getNome())
				.add("nomeGrupo", obj.getNomeGrupo())
				.add("grupoExternalId", obj.getGrupoExternalId())
				.add("grupoSetId", obj.getGrupoSetId())
				.add("idGrupo", obj.getIdGrupo())
				, usuarioVO);

	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception {
		alterar(obj, "salaaulablackboard", new AtributoPersistencia()
				.add("curso", obj.getCursoVO())
				.add("gradeCurricularEstagio", obj.getGradeCurricularEstagioVO())
				.add("turma", obj.getTurmaVO())
				.add("disciplina", obj.getDisciplinaVO())
				.add("programacaoTutoriaOnline", obj.getProgramacaoTutoriaOnlineVO())
				.add("ano", obj.getAno())
				.add("semestre", obj.getSemestre())
				.add("bimestre", obj.getBimestre())
				.add("idSalaAulaBlackboard", obj.getIdSalaAulaBlackboard())
				.add("linkSalaAulaBlackboard", obj.getLinkSalaAulaBlackboard())
				.add("termId", obj.getTermId())
				.add("tipoSalaAulaBlackboardEnum", obj.getTipoSalaAulaBlackboardEnum())
				.add("nrSala", obj.getNrSala())
				.add("id", obj.getId())
				.add("nome", obj.getNome())
				.add("nomeGrupo", obj.getNomeGrupo())
				.add("grupoExternalId", obj.getGrupoExternalId())
				.add("grupoSetId", obj.getGrupoSetId())
				.add("idGrupo", obj.getIdGrupo()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()),
				usuarioVO);
	}

	private StringBuilder getSQLConsultaPadraoSalaAulaBlackBoard(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String nivelEducacional) {
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica(true));
        sql.append(" where 1=1 ");
        if (Uteis.isAtributoPreenchido(idSalaAulaBlackboard)) {
        	sql.append(" and salaaulablackboard.idSalaAulaBlackboard ilike('%").append(idSalaAulaBlackboard).append("%')");
        }
        if (Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum)) {
        	sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
        }        
        if (Uteis.isAtributoPreenchido(bimestre)) {
        	sql.append(" and salaaulablackboard.bimestre =").append(bimestre).append(" ");
        }
        if (Uteis.isAtributoPreenchido(curso)) {
            sql.append(" and curso.codigo =  ").append(curso);
        }
        if (Uteis.isAtributoPreenchido(disciplina)) {
            sql.append(" and disciplina.codigo = ").append(disciplina);
        }
        if (Uteis.isAtributoPreenchido(nivelEducacional)) {
            sql.append(" and disciplina.niveleducacional = '").append(nivelEducacional).append("'");
        }
        if (Uteis.isAtributoPreenchido(ano) &&  !Uteis.isAtributoPreenchido(semestre)) {
        	sql.append(" and  (salaaulablackboard.ano ='").append(ano).append("' ");
        	if(Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum) && (tipoSalaAulaBlackboardEnum.isTccAmbientacao() || tipoSalaAulaBlackboardEnum.isEstagio())) {
        		sql.append(" or  (coalesce (salaaulablackboard.ano,'') = '' and salaaulablackboard.importado = false) ");
        	}
        	sql.append(" ) ");
        }
        if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
            sql.append(" and  ( (salaaulablackboard.ano ='").append(ano).append("' and salaaulablackboard.semestre ='").append(semestre).append("') ");
        	if(Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum) && (tipoSalaAulaBlackboardEnum.isTccAmbientacao() || tipoSalaAulaBlackboardEnum.isEstagio())) {
        		sql.append(" or (coalesce (salaaulablackboard.ano,'') = '' and coalesce(salaaulablackboard.semestre,'') = '' and salaaulablackboard.importado = false )");
        	}
        	sql.append(" ) ");
        }
        if (Uteis.isAtributoPreenchido(matricula)) {
            sql.append(" and exists ( ");
            sql.append(" select 1 from salaaulablackboardpessoa ");
            sql.append(" where salaaulablackboardpessoa.matricula = '").append(matricula).append("' ");
            sql.append(" and salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
            sql.append(" ) ");
        }
        
        if(!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
        	sql.append(" and exists ( ");
            sql.append(" select 1 from salaaulablackboardpessoa ");
            sql.append(" inner join matricula on matricula.matricula = salaaulablackboardpessoa.matricula ");
        	sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
        	sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
            sql.append(" ) ");
        }
        if (Uteis.isAtributoPreenchido(supervisor)) {
	        sql.append("and exists ( ");
	        sql.append("select from salaaulablackboardpessoa ");
	        sql.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
	        sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa ");
	        sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
	        sql.append(" and salaaulablackboard.ano = '").append(ano).append("' ");
	    	sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("' ");
	    	sql.append(" and pessoa.codigo = ").append(supervisor);
	    	sql.append(" and salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum <> 'ALUNO' ");
	    	sql.append(" ) ");
        }

		adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs, sql);

		
		return sql;
	}

   
	@Override
    public void consultar(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, DataModelo controleConsulta, String nivelEducacionalApresentar) throws Exception {
    	StringBuilder sql = getSQLConsultaPadraoSalaAulaBlackBoard(idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, supervisor, disciplina, matricula, filtroRelatorioAcademicoVO, nivelEducacionalApresentar);
    	sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo ");
    	sql.append(Uteis.limitOffset(controleConsulta.getLimitePorPagina(), controleConsulta.getOffset()));
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
        controleConsulta.setListaConsulta(montarDadosConsultaOtimizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, filtroRelatorioAcademicoVO, usuarioVO));
    }
	
    @Override
    public  List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardCopiaConteudo(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, String nivelEducacionalApresentar) throws Exception {
    	StringBuilder sql = getSQLConsultaPadraoSalaAulaBlackBoard(idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, supervisor, disciplina, matricula, filtroRelatorioAcademicoVO, nivelEducacionalApresentar);
    	sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum in ('ESTAGIO', 'TCC', 'TCC_AMBIENTACAO', 'PROJETO_INTEGRADOR', 'PROJETO_INTEGRADOR_AMBIENTACAO', 'DISCIPLINA') ");
    	sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        }
        return vetResultado;
    }
    
    @Override
    public  List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardApurarNota(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, String nivelEducacionalApresentar) throws Exception {
    	StringBuilder sql = getSQLConsultaPadraoSalaAulaBlackBoard(idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, supervisor, disciplina, matricula, filtroRelatorioAcademicoVO, nivelEducacionalApresentar);
    	sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum not in ('ESTAGIO', 'COMPONENTE_ESTAGIO', 'TCC_GRUPO', 'TCC_AMBIENTACAO', 'PROJETO_INTEGRADOR_GRUPO', 'PROJETO_INTEGRADOR_AMBIENTACAO', 'IMPORTACAO') ");
    	sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
    	while (tabelaResultado.next()) {
    		vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    	}
    	return vetResultado;
    }
    
//    @Override
//    public  List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardRealizarCalculoMediaApuracaoNotas(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
//    	StringBuilder sql = getSQLConsultaPadraoSalaAulaBlackBoard(idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, turma, disciplina, matricula, filtroRelatorioAcademicoVO);
//    	sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum not in ('ESTAGIO', 'COMPONENTE_ESTAGIO', 'TCC_GRUPO', 'TCC_AMBIENTACAO', 'PROJETO_INTEGRADOR_GRUPO', 'PROJETO_INTEGRADOR_AMBIENTACAO', 'IMPORTACAO') ");
//    	sql.append(" and exists ( ");
//        sql.append(" select 1 from historiconotablackboard ");
//        sql.append(" inner join salaaulablackboardpessoa on salaaulablackboardpessoa.codigo = historiconotablackboard.salaaulablackboardpessoa ");
//    	sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
//        sql.append(" limit 1 ");
//        sql.append(" ) ");
//    	sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo ");
//    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//    	List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
//    	while (tabelaResultado.next()) {
//    		vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//    	}
//    	return vetResultado;
//    }

    public List<SalaAulaBlackboardVO> montarDadosConsultaOtimizado(SqlRowSet tabelaResultado, int nivelMontarDados,  boolean isPaginadorAluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
        List<SalaAulaBlackboardVO> vetResultado = new ArrayList<SalaAulaBlackboardVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosOtimizado(tabelaResultado, nivelMontarDados, isPaginadorAluno, filtroRelatorioAcademicoVO, usuarioVO));
        }
        return vetResultado;
    }

    private SalaAulaBlackboardVO montarDadosOtimizado(SqlRowSet tabelaResultado, int nivelMontarDados, boolean isPaginadorAluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) {
        SalaAulaBlackboardVO obj = montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
        if(!isPaginadorAluno) {
        	obj.getDadosConsultaAlunos().setLimitePorPagina(0);
        	obj.getDadosConsultaAlunos().setOffset(0);
        }
        getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, filtroRelatorioAcademicoVO, nivelMontarDados, usuarioVO);
        getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarProfessoresFacilitadoresESupervisoresPorSalaAulaBlackboardOtimizado(obj, nivelMontarDados, usuarioVO);
        return obj;
    }

    private StringBuilder getSQLConsultaSugestaoFacilitadores() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select disciplina.codigo as \"disciplina.codigo\", ");
		sql.append(" disciplina.nome as \"disciplina.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", ");
		sql.append(" curso.nome as \"curso.nome\", ");
		sql.append(" salaaula.quantidadesalas, ");
		sql.append(" ( ");
		sql.append(" 	select count(distinct idsalaaulablackboard) from salaaulablackboard ");
		sql.append(" 	where exists ( ");
		sql.append(" 		select 1 from salaaulablackboardpessoa ");
		sql.append(" 		where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" 		and tipoSalaAulaBlackboardPessoaEnum = 'FACILITADOR' ");
		sql.append(" 	) ");
		sql.append(" 	and salaaulablackboard.disciplina = salaaula.disciplina ");
		sql.append(" 	and (salaaulablackboard.curso = salaaula.curso or salaaulablackboard.curso is null) ");
		sql.append(" 	and (salaaulablackboard.turma = salaaula.turma or salaaulablackboard.turma is null) ");
		sql.append(" 	and salaaulablackboard.ano = salaaula.ano and salaaulablackboard.semestre = salaaula.semestre ");
		sql.append(" ) as salascomfacilitadores, ");
		sql.append(" ( ");
		sql.append(" 	select count(distinct idsalaaulablackboard) from salaaulablackboard ");
		sql.append(" 	where not exists ( ");
		sql.append(" 		select 1 from salaaulablackboardpessoa ");
		sql.append(" 		where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" 		and tipoSalaAulaBlackboardPessoaEnum = 'FACILITADOR' ");
		sql.append(" 	) ");
		sql.append(" 	and salaaulablackboard.disciplina = salaaula.disciplina ");
		sql.append(" 	and (salaaulablackboard.curso = salaaula.curso or salaaulablackboard.curso is null) ");
		sql.append(" 	and (salaaulablackboard.turma = salaaula.turma or salaaulablackboard.turma is null) ");
		sql.append(" 	and salaaulablackboard.ano = salaaula.ano and salaaulablackboard.semestre = salaaula.semestre ");
		sql.append(" ) as salassemfacilitadores, ");
		sql.append(" ( ");
		sql.append(" 	select count(grupopessoaitem.codigo) from grupopessoaitem ");
		sql.append(" 	inner join grupopessoa on grupopessoa.codigo = grupopessoaitem.grupopessoa ");
		sql.append(" 	inner join pessoa on pessoa.codigo = grupopessoaitem.pessoa ");
		sql.append(" 	where grupopessoa.codigo = disciplina.grupopessoa ");
		sql.append(" 	and not exists ( ");
		sql.append(" 		select 1 from salaaulablackboardpessoa ");
		sql.append(" 		inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
		sql.append(" 		inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
		sql.append(" 		where salaaulablackboard.disciplina = salaaula.disciplina ");
		sql.append(" 		and (salaaulablackboard.curso = salaaula.curso or salaaulablackboard.curso is null) ");
		sql.append(" 		and (salaaulablackboard.turma = salaaula.turma or salaaulablackboard.turma is null) ");
		sql.append(" 		and salaaulablackboard.ano = salaaula.ano and salaaulablackboard.semestre = salaaula.semestre ");
		sql.append(" 		and pessoaemailinstitucional.pessoa = pessoa.codigo ");
		sql.append(" 	) ");
		sql.append(" ) as facilitadoresdisponiveis ");
		sql.append(" from ( ");
		sql.append(" 	select  count(distinct idsalaaulablackboard) quantidadeSalas, disciplina, curso, turma, ano, semestre ");
		sql.append(" 	from salaaulablackboard ");
		sql.append(" 	group by disciplina, curso, turma, ano, semestre ");
		sql.append(" ) as salaaula ");
		sql.append(" inner join disciplina on disciplina.codigo = salaaula.disciplina ");
		sql.append(" left join curso on curso.codigo = salaaula.curso ");
		sql.append(" left join turma on turma.codigo = salaaula.turma ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
        return sql;
    }

	@Override
	public List<SugestaoFacilitadorBlackboardVO> consultarSugestaoFacilitadores(String ano, String semestre, UsuarioVO usuario) {
		StringBuilder sql = getSQLConsultaSugestaoFacilitadores();
		sql.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and salaaula.ano ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and salaaula.semestre ='").append(semestre).append("' ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SugestaoFacilitadorBlackboardVO> vetResultado = new ArrayList<SugestaoFacilitadorBlackboardVO>(0);
        while (tabelaResultado.next()) {
        	SugestaoFacilitadorBlackboardVO obj = new SugestaoFacilitadorBlackboardVO();
            obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
            obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
            obj.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
            obj.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));
            obj.setQuantidadeSalas(tabelaResultado.getInt("quantidadesalas"));
            obj.setSalasComFacilitadores(tabelaResultado.getInt("salascomfacilitadores"));
            obj.setSalasSemFacilitadores(tabelaResultado.getInt("salassemfacilitadores"));
            obj.setFacilitadoresDisponiveis(tabelaResultado.getInt("facilitadoresdisponiveis"));
            vetResultado.add(obj);
        }
        return vetResultado;
	}

	@Override
	public void sugerirFacilitadores(List<SugestaoFacilitadorBlackboardVO> listaSugestaoFacilitadores, Integer quantidadeFacilitadoresPorSala, String ano, String semestre, UsuarioVO usuario) throws Exception {
		listaSugestaoFacilitadores.stream()
		.filter(sugestao -> sugestao.getSalasSemFacilitadores() > 0
				&& (sugestao.getFacilitadoresDisponiveis() - (quantidadeFacilitadoresPorSala * sugestao.getSalasSemFacilitadores()) >= 0))
		.forEach(sugestao -> {
			try {
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(sugestao.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				GrupoPessoaVO grupoPessoa = getFacadeFactory().getGrupoPessoaFacade().consultarPorChavePrimaria(disciplina.getGrupoPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				List<SalaAulaBlackboardVO> listaSalas = consultarPorDisciplinaCurso(disciplina.getCodigo(), sugestao.getCursoVO().getCodigo(), ano, semestre, Uteis.NIVELMONTARDADOS_TODOS, usuario);

				Map<Integer, List<String>> mapFacilitadores = new HashMap<>();
				Map<String, List<Integer>> mapSalas = new HashMap<>();

				grupoPessoa.getListaGrupoPessoaItemVO().forEach(grupoPessoaItem -> obterSalaAulaBlackBoardSugestaoFacilitadorAgrupada(listaSalas)
						.forEach(sala -> {
							try {
								PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(grupoPessoaItem.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
								if (!mapFacilitadores.containsKey(pessoaEmailInstitucionalVO.getCodigo())) {
									List<String> salas = obterSalaAulaBlackBoardSugestaoFacilitadorAgrupada(listaSalas)
											.stream()
											.filter(salaAula -> salaAula.getListaFacilitadores()
													.stream().anyMatch(facilitador -> facilitador.getPessoaEmailInstitucionalVO().getCodigo().equals(pessoaEmailInstitucionalVO.getCodigo())))
											.map(SalaAulaBlackboardVO::getIdSalaAulaBlackboard)
											.collect(Collectors.toList());
									mapFacilitadores.put(pessoaEmailInstitucionalVO.getCodigo(), salas);
								}
								if (!mapSalas.containsKey(sala.getIdSalaAulaBlackboard())) {
									mapSalas.put(sala.getIdSalaAulaBlackboard(), new ArrayList<>());
								}
								if (mapFacilitadores.get(pessoaEmailInstitucionalVO.getCodigo()).size() == 0
										&& mapSalas.get(sala.getIdSalaAulaBlackboard()).size() < quantidadeFacilitadoresPorSala) {
									mapFacilitadores.get(pessoaEmailInstitucionalVO.getCodigo()).add(sala.getIdSalaAulaBlackboard());
									getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(null, pessoaEmailInstitucionalVO.getPessoaVO().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.FACILITADOR, null, null, sala.getIdSalaAulaBlackboard(), usuario);
									mapSalas.get(sala.getIdSalaAulaBlackboard()).add(pessoaEmailInstitucionalVO.getCodigo());
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private Collection<SalaAulaBlackboardVO> obterSalaAulaBlackBoardSugestaoFacilitadorAgrupada(List<SalaAulaBlackboardVO> listaSalas) {
		return listaSalas
				.stream()
				.collect(Collectors.toConcurrentMap(SalaAulaBlackboardVO::getIdSalaAulaBlackboard, Function.identity(), (p, q) -> p))
				.values();
	}

	private List<SalaAulaBlackboardVO> consultarPorDisciplinaCurso(Integer disciplina, Integer curso, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(false);
        sqlStr.append(" where 1=1 ");
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" and curso.codigo = ").append(curso).append(" ");
        }
        if (Uteis.isAtributoPreenchido(disciplina)) {
            sqlStr.append(" and disciplina.codigo = ").append(disciplina).append(" ");
        }
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and salaaulablackboard.semestre ='").append(semestre).append("' ");
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}


	@Override
	public File realizarGeracaoExcelSalas(List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores, Integer qtdLinhasExportacaoPorSalaAulaBlackboard, UsuarioVO usuario) throws Exception {
		File arquivo;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sala");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		montarCabecalhoRelatorioExcel(uteisExcel, sheet, exportarSala, exportarAlunos, exportarNotas, exportarProfessores, exportarFacilitadores, exportarSupervisores);
		List<SalaAulaBlackboardRelatorioExcelVO> salaAulaBlackboardRelatorioExcelVOs = consultarSalaAulaBlackboardGeracaoExcel(unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, curso, turma, disciplina, matricula, filtroRelatorioAcademicoVO, exportarSala, exportarAlunos, exportarNotas, exportarProfessores, exportarFacilitadores, exportarSupervisores);
		montarItensRelatorioExcel(uteisExcel, sheet, salaAulaBlackboardRelatorioExcelVOs, exportarSala, qtdLinhasExportacaoPorSalaAulaBlackboard);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + new Date().getTime() + ".xlsx");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}

	private List<SalaAulaBlackboardRelatorioExcelVO> consultarSalaAulaBlackboardGeracaoExcel(List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores) {
		StringBuilder sql = getSQLConsultaGeracaoExcelSalaAulaBlackBoard(exportarSala, exportarAlunos, exportarNotas, exportarProfessores, exportarFacilitadores, exportarSupervisores);
		sql.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum)) {
			sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and salaaulablackboard.semestre ='").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and curso.codigo =  ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo =  ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" and exists ( ");
			sql.append(" select 1 from salaaulablackboardpessoa ");
			sql.append(" where salaaulablackboardpessoa.matricula = '").append(matricula).append("' ");
			sql.append(" and salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" ) ");
		}
		if(!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
			sql.append(" and exists ( ");
			sql.append(" select 1 from salaaulablackboardpessoa ");
			sql.append(" left join matricula on matricula.matricula = salaaulablackboardpessoa.matricula ");
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
			sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" ) ");
		}

		adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs, sql);

		sql.append(" group by ");
		sql.append(" disciplina.nome ");
		if (exportarSala) {
			sql.append(", salaaulablackboard.idSalaAulaBlackboard, salaaulablackboard.id, salaaulablackboard.nome, salaaulablackboard.nrsala ");
		}
		if (exportarAlunos) {
			sql.append(", pessoaemailinstitucionalaluno.email ");
		}
		if (exportarProfessores) {
			sql.append(", pessoaemailinstitucionalprofessor.email ");
		}
		if (exportarFacilitadores) {
			sql.append(", pessoaemailinstitucionalfacilitador.email ");
		}
		if (exportarSupervisores) {
			sql.append(", pessoaemailinstitucionalorientador.email ");
		}
		sql.append(" order by ");
		sql.append(" disciplina.nome ");
		if (exportarSala) {
			sql.append(",salaaulablackboard.nome, salaaulablackboard.nrsala ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaGeracaoExcel(tabelaResultado, exportarSala, exportarAlunos, exportarNotas, exportarProfessores, exportarFacilitadores, exportarSupervisores);
	}

	private List<SalaAulaBlackboardRelatorioExcelVO> montarDadosConsultaGeracaoExcel(SqlRowSet tabelaResultado, Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores) {
		List<SalaAulaBlackboardRelatorioExcelVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			SalaAulaBlackboardRelatorioExcelVO obj = new SalaAulaBlackboardRelatorioExcelVO();
			obj.setDisciplina(tabelaResultado.getString("disciplina.nome"));
			if (exportarSala) {
				obj.setId(tabelaResultado.getString("salaaulablackboard.id"));
				obj.setIdSalaAulaBlackboard(tabelaResultado.getString("salaaulablackboard.idsalaaulablackboard"));
				obj.setNome(tabelaResultado.getString("salaaulablackboard.nome"));
			}
			if (exportarAlunos) {
				obj.setAluno(tabelaResultado.getString("pessoaemailinstitucionalaluno.email"));
			}
			if (exportarProfessores) {
				obj.setProfessor(tabelaResultado.getString("pessoaemailinstitucionalprofessor.email"));
			}
			if (exportarFacilitadores) {
				obj.setFacilitador(tabelaResultado.getString("pessoaemailinstitucionalfacilitador.email"));
			}
			if (exportarSupervisores) {
				obj.setSupervisor(tabelaResultado.getString("pessoaemailinstitucionalorientador.email"));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private StringBuilder getSQLConsultaGeracaoExcelSalaAulaBlackBoard(Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select disciplina.nome as \"disciplina.nome\" ");
		if (exportarSala) {
			sql.append(", salaaulablackboard.id as \"salaaulablackboard.id\" ");
			sql.append(", salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idsalaaulablackboard\" ");
			sql.append(", salaaulablackboard.nome as \"salaaulablackboard.nome\" ");
		}
		if (exportarAlunos) {
			sql.append(", pessoaemailinstitucionalaluno.email as \"pessoaemailinstitucionalaluno.email\" ");
		}
		if (exportarProfessores) {
			sql.append(", pessoaemailinstitucionalprofessor.email as \"pessoaemailinstitucionalprofessor.email\" ");
		}
		if (exportarFacilitadores) {
			sql.append(", pessoaemailinstitucionalfacilitador.email as \"pessoaemailinstitucionalfacilitador.email\" ");
		}
		if (exportarSupervisores) {
			sql.append(", pessoaemailinstitucionalorientador.email as \"pessoaemailinstitucionalorientador.email\" ");
		}
		sql.append(" from salaaulablackboard ");
		sql.append(" left join curso on curso.codigo = salaaulablackboard.curso ");
		sql.append(" left join turma on turma.codigo = salaaulablackboard.turma ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sql.append(" left join disciplina on disciplina.codigo = salaaulablackboard.disciplina ");
		if (exportarAlunos) {
			sql.append(" left join salaaulablackboardpessoa salaaulablackboardpessoaaluno on salaaulablackboardpessoaaluno.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" 	and salaaulablackboardpessoaaluno.tiposalaaulablackboardpessoaenum = 'ALUNO' ");
			sql.append(" left join pessoaemailinstitucional pessoaemailinstitucionalaluno on pessoaemailinstitucionalaluno.codigo = salaaulablackboardpessoaaluno.pessoaemailinstitucional ");
		}
		if (exportarProfessores) {
			sql.append(" left join salaaulablackboardpessoa salaaulablackboardpessoaprofessor on salaaulablackboardpessoaprofessor.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" 	and salaaulablackboardpessoaprofessor.tiposalaaulablackboardpessoaenum = 'PROFESSOR' ");
			sql.append(" left join pessoaemailinstitucional pessoaemailinstitucionalprofessor on pessoaemailinstitucionalprofessor.codigo = salaaulablackboardpessoaprofessor.pessoaemailinstitucional ");
		}
		if (exportarFacilitadores) {
			sql.append(" left join salaaulablackboardpessoa salaaulablackboardpessoafacilitador on salaaulablackboardpessoafacilitador.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" 	and salaaulablackboardpessoafacilitador.tiposalaaulablackboardpessoaenum = 'FACILITADOR' ");
			sql.append(" left join pessoaemailinstitucional pessoaemailinstitucionalfacilitador on pessoaemailinstitucionalfacilitador.codigo = salaaulablackboardpessoafacilitador.pessoaemailinstitucional ");
		}
		if (exportarSupervisores) {
			sql.append(" left join salaaulablackboardpessoa salaaulablackboardpessoaorientador on salaaulablackboardpessoaorientador.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" 	and salaaulablackboardpessoaorientador.tiposalaaulablackboardpessoaenum = 'ORIENTADOR' ");
			sql.append(" left join pessoaemailinstitucional pessoaemailinstitucionalorientador on pessoaemailinstitucionalorientador.codigo = salaaulablackboardpessoaorientador.pessoaemailinstitucional ");
		}
		return sql;
	}

	private void montarItensRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet, List<SalaAulaBlackboardRelatorioExcelVO> salaAulaBlackboardRelatorioExcelVOs, Boolean exportarSala, Integer qtdLinhasExportacaoPorSalaAulaBlackboard ) {
		if(exportarSala) {
			Map<String, List<SalaAulaBlackboardRelatorioExcelVO>> mapa =salaAulaBlackboardRelatorioExcelVOs.stream().collect(Collectors.groupingBy(SalaAulaBlackboardRelatorioExcelVO::getIdSalaAulaBlackboard));
			mapa.entrySet().forEach(p ->{
				if(p.getValue().size() > 1) {
					p.getValue().stream().forEach(obj -> {realizarPreenchimentoLinhaAndColunaDoRelatorioExcel(uteisExcel, sheet, obj);});
					int qdtPermitido = qtdLinhasExportacaoPorSalaAulaBlackboard - p.getValue().size();
					if(qdtPermitido > 0) {
						SalaAulaBlackboardRelatorioExcelVO temp = new SalaAulaBlackboardRelatorioExcelVO();
						temp.setIdSalaAulaBlackboard(p.getValue().get(0).getIdSalaAulaBlackboard());
						temp.setNome(p.getValue().get(0).getNome());
						temp.setDisciplina(p.getValue().get(0).getDisciplina());
						for (int i = 0; i < qdtPermitido; i++) {
							realizarPreenchimentoLinhaAndColunaDoRelatorioExcel(uteisExcel, sheet, temp);	
						}
					}	
				}
			});
			mapa.entrySet().stream().filter(p-> p.getValue().size() == 1).forEach(p ->{
				p.getValue().stream().forEach(obj -> {
					for (int i = 0; i < qtdLinhasExportacaoPorSalaAulaBlackboard; i++) {
						realizarPreenchimentoLinhaAndColunaDoRelatorioExcel(uteisExcel, sheet, obj);	
					}
				});
			});
		}else {
			salaAulaBlackboardRelatorioExcelVOs.forEach(obj -> {
				realizarPreenchimentoLinhaAndColunaDoRelatorioExcel(uteisExcel, sheet, obj);
			});
		}
		
	}

	private void realizarPreenchimentoLinhaAndColunaDoRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet, SalaAulaBlackboardRelatorioExcelVO obj) {
		int cellnum = 0;			
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelula(row, cellnum++, obj.getIdSalaAulaBlackboard());
		uteisExcel.preencherCelula(row, cellnum++, obj.getNome());
		uteisExcel.preencherCelula(row, cellnum++, obj.getDisciplina());
		uteisExcel.preencherCelula(row, cellnum++, obj.getAluno());

		Cell cell = row.createCell(cellnum++);
		cell.setCellStyle(uteisExcel.preencherLayoutNumerico());
		cell.setCellValue(obj.getNotaAnterior());

		cell = row.createCell(cellnum++);
		cell.setCellStyle(uteisExcel.preencherLayoutNumerico());
		cell.setCellValue(obj.getMediaFinal());

		uteisExcel.preencherCelula(row, cellnum++, obj.getProfessor());
		uteisExcel.preencherCelula(row, cellnum++, obj.getFacilitador());
		uteisExcel.preencherCelula(row, cellnum++, obj.getSupervisor());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardAgrupada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLConsultaAgrupadaSalaAulaBlackBoard();
		sql.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum)) {
			sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and salaaulablackboard.semestre ='").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(bimestre)) {
			sql.append(" and salaaulablackboard.bimestre = ").append(bimestre).append(" ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and curso.codigo =  ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo =  ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" and exists ( ");
			sql.append(" select 1 from salaaulablackboardpessoa ");
			sql.append(" where salaaulablackboardpessoa.matricula = '").append(matricula).append("' ");
			sql.append(" and salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" ) ");
		}
		if(!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
			sql.append(" and exists ( ");
			sql.append(" select 1 from salaaulablackboardpessoa ");
			sql.append(" left join matricula on matricula.matricula = salaaulablackboardpessoa.matricula ");
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
			sql.append(" where salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
			sql.append(" ) ");
		}

		adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs, sql);

		sql.append(" group by salaaulablackboard.id, salaaulablackboard.idSalaAulaBlackboard, salaaulablackboard.tipoSalaAulaBlackboardEnum, ");
		sql.append(" salaaulablackboard.nrSala, salaaulablackboard.nome, curso.codigo, ");
		sql.append(" curso.nome, turma.codigo, turma.identificadorturma, disciplina.codigo, ");
		sql.append(" disciplina.nome, disciplina.abreviatura ");
		sql.append(" order by salaaulablackboard.nome, salaaulablackboard.nrsala ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaAgrupada(tabelaResultado, nivelMontarDados, usuario);
	}

	private void adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, StringBuilder sql) {
		if (unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sql.append(" and unidadeensino.codigo in (")
					.append(unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
							.map(UnidadeEnsinoVO::getCodigo)
							.map(Objects::toString)
							.collect(Collectors.joining(",")))
					.append(") ");
		}
	}

	private List<SalaAulaBlackboardVO> montarDadosConsultaAgrupada(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<SalaAulaBlackboardVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
			obj.setNovoObj(false);
			obj.setId(tabelaResultado.getString("salaaulablackboard.id"));
			obj.setIdSalaAulaBlackboard(tabelaResultado.getString("salaaulablackboard.idsalaaulablackboard"));
			obj.setNrSala(tabelaResultado.getInt("salaaulablackboard.nrsala"));
			obj.setNome(tabelaResultado.getString("salaaulablackboard.nome"));
			obj.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(tabelaResultado.getString("salaaulablackboard.tipoSalaAulaBlackboardEnum")));
			obj.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorturma"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getDisciplinaVO().setAbreviatura(tabelaResultado.getString("disciplina.abreviatura"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private StringBuilder getSQLConsultaAgrupadaSalaAulaBlackBoard() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select salaaulablackboard.id as \"salaaulablackboard.id\",  ");
		sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  ");
		sql.append(" salaaulablackboard.tipoSalaAulaBlackboardEnum as \"salaaulablackboard.tipoSalaAulaBlackboardEnum\",  ");
		sql.append(" salaaulablackboard.nrSala as \"salaaulablackboard.nrSala\",  ");
		sql.append(" salaaulablackboard.nome as \"salaaulablackboard.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\" ");
		sql.append(" from salaaulablackboard ");
		sql.append(" left join curso on curso.codigo =  salaaulablackboard.curso ");
		sql.append(" left join turma on turma.codigo =  salaaulablackboard.turma ");
		sql.append(" left join unidadeensino on unidadeensino.codigo =  turma.unidadeensino ");
		sql.append(" left join disciplina on disciplina.codigo =  salaaulablackboard.disciplina ");
		return sql;
	}

	private void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet, Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores) {
		int cellnum = 0;
		Row row;
		if (sheet.getLastRowNum() > 0) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
		} else {
			row = sheet.createRow(0);
		}
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarSala ? 3500 : 0, "Código");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarSala ? 10000 : 0, "Nome");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarSala ? 10000 : 0, "Disciplina");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarAlunos ? 10000 : 0, "Aluno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarNotas ? 3500 : 0, "Nota Anterior");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarNotas ? 3500 : 0, "M.F.");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarProfessores ? 10000 : 0, "Professor");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarFacilitadores ? 10000 : 0, "Facilitador");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, exportarSupervisores ? 10000 : 0, "Supervisor");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void upLoadArquivoImportado(InputStream inputStrem, List<SalaAulaBlackboardPessoaVO> salaAulaBlackboardPessoaVOs, UsuarioVO usuario) throws Exception {
		List<SalaAulaBlackboardPessoaVO> listaTemp = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(inputStrem);
		XSSFSheet sheet = workbook.getSheetAt(0);
		realizarLeituraLinhasSalasImportada(listaTemp, sheet, usuario);
		salaAulaBlackboardPessoaVOs.clear();
		salaAulaBlackboardPessoaVOs.addAll(listaTemp.parallelStream()
				.sorted(Comparator.comparing((SalaAulaBlackboardPessoaVO p)-> p.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard())
						.thenComparing((SalaAulaBlackboardPessoaVO p)-> p.getTipoSalaAulaBlackboardPessoaEnum())
						.thenComparing((SalaAulaBlackboardPessoaVO p)-> p.getPessoaEmailInstitucionalVO().getEmail()))
				.collect(Collectors.toList()));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarImportacao(List<SalaAulaBlackboardPessoaVO> salaAulaBlackboardPessoaVOs, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoProfessor, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoFacilitador, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoSupervisor, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoNota, UsuarioVO usuario) throws Exception {
		realizarOperacaoImportacao(salaAulaBlackboardPessoaVOs
				.stream()
				.filter(p -> Uteis.isAtributoPreenchido(p.getPessoaEmailInstitucionalVO()) && TipoSalaAulaBlackboardPessoaEnum.PROFESSOR.equals(p.getTipoSalaAulaBlackboardPessoaEnum()))
				.collect(Collectors.toList()), operacaoImportacaoProfessor, TipoSalaAulaBlackboardPessoaEnum.PROFESSOR, usuario);
		realizarOperacaoImportacao(salaAulaBlackboardPessoaVOs
				.stream()
				.filter(p -> Uteis.isAtributoPreenchido(p.getPessoaEmailInstitucionalVO()) && TipoSalaAulaBlackboardPessoaEnum.FACILITADOR.equals(p.getTipoSalaAulaBlackboardPessoaEnum()))
				.collect(Collectors.toList()), operacaoImportacaoFacilitador, TipoSalaAulaBlackboardPessoaEnum.FACILITADOR, usuario);
		realizarOperacaoImportacao(salaAulaBlackboardPessoaVOs
				.stream()
				.filter(p -> Uteis.isAtributoPreenchido(p.getPessoaEmailInstitucionalVO()) && TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR.equals(p.getTipoSalaAulaBlackboardPessoaEnum()))
				.collect(Collectors.toList()), operacaoImportacaoSupervisor, TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR, usuario);
		realizarOperacaoImportacaoNota(salaAulaBlackboardPessoaVOs, operacaoImportacaoNota, usuario);
	}

	private void realizarLeituraLinhasSalasImportada(List<SalaAulaBlackboardPessoaVO> lista, XSSFSheet sheet, UsuarioVO usuario) throws Exception {
		Iterator<?> linhas = sheet.rowIterator();
        while (linhas.hasNext()) {
        	XSSFRow linha = (XSSFRow) linhas.next();
        	if(linha.getRowNum() > 0) {
        		realizarLeituraColunasSalasImportada(lista, linha, usuario);
        	}
        }
	}

	private void realizarLeituraColunasSalasImportada(List<SalaAulaBlackboardPessoaVO> lista, XSSFRow linha, UsuarioVO usuario) throws Exception {
		Iterator<?> celulas = linha.cellIterator();
		SalaAulaBlackboardVO salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		while (celulas.hasNext()) {
			XSSFCell celula = (XSSFCell) celulas.next();
			int index = celula.getColumnIndex();
			switch (index) {
				case 0:
					salaAulaBlackboardVO = new SalaAulaBlackboardVO();
					salaAulaBlackboardVO.setIdSalaAulaBlackboard(celula.toString());
					break;
				case 6:
					realizarVerificacaoSalaAulaBlackboardPessoaExistente(lista, salaAulaBlackboardVO, TipoSalaAulaBlackboardPessoaEnum.PROFESSOR, celula.toString().trim(), usuario);
					break;
				case 7:
					realizarVerificacaoSalaAulaBlackboardPessoaExistente(lista, salaAulaBlackboardVO, TipoSalaAulaBlackboardPessoaEnum.FACILITADOR, celula.toString().trim(), usuario);
					break;
				case 8:
					realizarVerificacaoSalaAulaBlackboardPessoaExistente(lista, salaAulaBlackboardVO, TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR, celula.toString().trim(), usuario);
					break;
			}
		}
	}

	private void realizarVerificacaoSalaAulaBlackboardPessoaExistente(List<SalaAulaBlackboardPessoaVO> lista, SalaAulaBlackboardVO salaAulaBlackboardVO, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String email, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(email)) {
			SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarPorIdSalaAulaBlackboardEmailTipoPessoa(salaAulaBlackboardVO.getIdSalaAulaBlackboard(), email, tipoSalaAulaBlackboardPessoaEnum, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			if (!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO)) {
				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorEmail(email, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				salaAulaBlackboardPessoaVO = new SalaAulaBlackboardPessoaVO();
				salaAulaBlackboardPessoaVO.setSalaAulaBlackboardVO(salaAulaBlackboardVO);
				if(!Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
					pessoaEmailInstitucionalVO.setEmail(email);
					
				}
				salaAulaBlackboardPessoaVO.setPessoaEmailInstitucionalVO(pessoaEmailInstitucionalVO);				
				salaAulaBlackboardPessoaVO.setTipoSalaAulaBlackboardPessoaEnum(tipoSalaAulaBlackboardPessoaEnum);
			}
			if (lista.stream()
					.noneMatch(obj -> obj.getPessoaEmailInstitucionalVO().getEmail().equals(email)
							&& obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard().equals(salaAulaBlackboardVO.getIdSalaAulaBlackboard()))) {
				lista.add(salaAulaBlackboardPessoaVO);
			}
		}
	}

	//TODO
	private void realizarOperacaoImportacaoNota(List<SalaAulaBlackboardPessoaVO> lista, OperacaoImportacaoSalaBlackboardEnum operacao, UsuarioVO usuario) throws Exception {
	}

	private void realizarOperacaoImportacao(List<SalaAulaBlackboardPessoaVO> lista, OperacaoImportacaoSalaBlackboardEnum operacao, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuario) throws Exception {
		switch (operacao) {
			case INCLUIR:
				realizarInclusaoSalaAulaBlackboardPessoa(lista, usuario);
				break;
			case EXCLUIR_INCLUIR:
				realizarExclusaoInclusaoSalaAulaBlackboardPessoa(lista, tipoSalaAulaBlackboardPessoaEnum, usuario);
				break;
			case EXCLUIR:
				realizarExclusaoSalaAulaBlackboardPessoa(lista, usuario);
				break;
			default:
				break;
		}
	}

	private void realizarExclusaoSalaAulaBlackboardPessoa(List<SalaAulaBlackboardPessoaVO> lista, UsuarioVO usuario) throws Exception {
		for (SalaAulaBlackboardPessoaVO obj : lista) {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(null, obj.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), obj.getTipoSalaAulaBlackboardPessoaEnum(), obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard(), obj.getPessoaEmailInstitucionalVO().getEmail(), usuario);
		}
	}

	private void realizarInclusaoSalaAulaBlackboardPessoa(List<SalaAulaBlackboardPessoaVO> lista, UsuarioVO usuario) throws Exception {
		for (SalaAulaBlackboardPessoaVO obj : lista) {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(null, obj.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), obj.getTipoSalaAulaBlackboardPessoaEnum(), obj.getMatricula(), obj.getMatriculaPeriodoTurmaDisciplina(), obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard(), obj.getPessoaEmailInstitucionalVO().getEmail(), usuario);
		}
	}

	// todo
	// metodo para agrupar caso seja sala agrupada, foi removido dos dois metodos acima pois na hora de importar
	// será importado um a um independente da sala ser agrupada
	private List<SalaAulaBlackboardPessoaVO> obterPessoaSalaAulaBlackboardAgrupada(List<SalaAulaBlackboardPessoaVO> lista) {
		return lista.stream()
				.collect(Collectors.groupingBy(obj -> obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard(), Collectors.groupingBy(obj -> obj.getPessoaEmailInstitucionalVO().getEmail())))
				.values()
				.stream()
				.map(map -> map.values().stream().findFirst().get().stream().findFirst().get())
				.collect(Collectors.toList());
	}

	private void realizarExclusaoInclusaoSalaAulaBlackboardPessoa(List<SalaAulaBlackboardPessoaVO> lista, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuario) throws Exception {
		List<SalaAulaBlackboardPessoaVO> listaExcluir = lista.stream()
				.map(SalaAulaBlackboardPessoaVO::getSalaAulaBlackboardVO)
				.map(SalaAulaBlackboardVO::getIdSalaAulaBlackboard)
				.distinct()
				.flatMap(idSalaAulaBlackboard -> getFacadeFactory().getSalaAulaBlackboardPessoaFacade()
						.consultarPorIdSalaAulaBlackboardTipoPessoa(idSalaAulaBlackboard, tipoSalaAulaBlackboardPessoaEnum, usuario).stream())
				.collect(Collectors.toList());
		realizarExclusaoSalaAulaBlackboardPessoa(listaExcluir, usuario);
		realizarInclusaoSalaAulaBlackboardPessoa(lista, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from salaAulaBlackboard where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAdicaoPessoaSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()), "A Conta de Email no Blackboard deve ser informado para "+salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.codigo, obj.getCodigo().toString());
		headers.put(UteisWebServiceUrl.emailpessoainstitucional, salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
		headers.put(UteisWebServiceUrl.tipoSalaAulaBlackboardPessoaEnum, salaAulaBlackboardPessoaVO.getTipoSalaAulaBlackboardPessoaEnum().name());
		headers.put(UteisWebServiceUrl.matricula, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatricula()) ? null : salaAulaBlackboardPessoaVO.getMatricula());
		headers.put(UteisWebServiceUrl.matriculaperiodoturmadisciplina, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString()) ? null : salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString() );
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_ADICIONAR_PESSOAS, RequestMethod.POST, headers, usuarioVO);
		if(!jsonResponse.isSuccess()) {
			throw new Exception(jsonResponse.getStatusText());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarRemocaoPessoaSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail()), "A Conta de Email no Blackboard deve ser informado para "+salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.codigo, obj.getCodigo().toString());
		headers.put(UteisWebServiceUrl.emailpessoainstitucional, salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
		headers.put(UteisWebServiceUrl.tipoSalaAulaBlackboardPessoaEnum, salaAulaBlackboardPessoaVO.getTipoSalaAulaBlackboardPessoaEnum().name());
		headers.put(UteisWebServiceUrl.matricula, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatricula()) ? null : salaAulaBlackboardPessoaVO.getMatricula());
		headers.put(UteisWebServiceUrl.matriculaperiodoturmadisciplina, !Uteis.isAtributoPreenchido(salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString()) ? null : salaAulaBlackboardPessoaVO.getMatriculaPeriodoTurmaDisciplina().toString() );
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_REMOVER_PESSOAS, RequestMethod.POST, headers, usuarioVO);
		if(!jsonResponse.isSuccess()) {
			throw new Exception(jsonResponse.getStatusText());
		}
	}
	
	
	
	

	@Override
	public List<SalaAulaBlackboardVO> consultarGeracaoSalaAulaBlackboard(Integer disciplina, String ano, String semestre, Integer bimestre, Integer situacaoAlunos, String nivelEducacionalApresentar) throws Exception{

		StringBuilder sql = new StringBuilder("select * from consultarSalaGeracaoBlackboard(");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(disciplina).append(",");
		}else {
			sql.append("null, ");
		}
		sql.append("'").append(ano).append("',");
		sql.append("'").append(semestre).append("',");
		sql.append(bimestre).append(", ");
		if(Uteis.isAtributoPreenchido(nivelEducacionalApresentar)) {
			sql.append("'").append(nivelEducacionalApresentar).append("'");
		}else {
			sql.append("null ");
		}
		sql.append(") ");
		if(Uteis.isAtributoPreenchido(situacaoAlunos)) {
			if(situacaoAlunos.equals(1)) {
				sql.append(" where qtdeAlunoEnsalado > 0 ");
			}
			if(situacaoAlunos.equals(2)) {
				sql.append(" where  qtdeAlunos - qtdeAlunoEnsalado > 0 ");
			}
		}
		sql.append(" order by bimestre, disciplina_nome ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaAulaBlackboardVO> salaAulaBlackboardVOs =  new ArrayList<SalaAulaBlackboardVO>(0);
		while(rs.next()) {
			SalaAulaBlackboardVO salaAulaBlackboardVO =  new SalaAulaBlackboardVO();
			salaAulaBlackboardVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina_codigo"));
			salaAulaBlackboardVO.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
			salaAulaBlackboardVO.getDisciplinaVO().setAbreviatura(rs.getString("disciplina_abreviatura"));
			salaAulaBlackboardVO.getDisciplinaVO().setIdConteudoMasterBlackboard(rs.getString("disciplina_idConteudoMasterBlackboard"));
			salaAulaBlackboardVO.getDisciplinaVO().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(rs.getString("classificacaodisciplina")));
			if(salaAulaBlackboardVO.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador()) {
				salaAulaBlackboardVO.getDisciplinaVO().setNrMaximoAlunosPorAmbientacao(rs.getInt("nrmaximoaulosporsala"));
				salaAulaBlackboardVO.getDisciplinaVO().setNrMaximoAulosPorSala(rs.getInt("nrmaximoaulosporsala"));
			}else {
				salaAulaBlackboardVO.getDisciplinaVO().setNrMaximoAulosPorSala(rs.getInt("nrmaximoaulosporsala"));
			}
			salaAulaBlackboardVO.getDisciplinaVO().setNrMinimoAlunosPorSala(rs.getInt("nrminimoalunosporsala"));
			salaAulaBlackboardVO.setAno(rs.getString("ano"));
			salaAulaBlackboardVO.setSemestre(rs.getString("semestre"));
			salaAulaBlackboardVO.setBimestre(rs.getInt("bimestre"));
			salaAulaBlackboardVO.getProgramacaoTutoriaOnlineVO().setCodigo(rs.getInt("programacaotutoriaonline"));
			salaAulaBlackboardVO.setQtdeAlunos(rs.getInt("qtdeAlunos"));
			salaAulaBlackboardVO.setQtdAlunosEnsalados(rs.getInt("qtdeAlunoEnsalado"));
			salaAulaBlackboardVO.setQtdAlunosNaoEnsalados(rs.getInt("qtdeAlunos") - rs.getInt("qtdeAlunoEnsalado"));
			salaAulaBlackboardVO.setQtdeSalasExistentes(rs.getInt("qtdeSalasExistentes"));
			if(rs.getInt("nrmaximoaulosporsala") == 0) {
				salaAulaBlackboardVO.setAlunosPorSala(rs.getInt("qtdeAlunos"));
			}else {
				salaAulaBlackboardVO.setAlunosPorSala(Integer.valueOf(rs.getInt("qtdeAlunos") / (Uteis.arredondarParaMais(Integer.valueOf(rs.getInt("qtdeAlunos")).doubleValue() / Integer.valueOf(rs.getInt("nrmaximoaulosporsala")).doubleValue() ))));
			}
			salaAulaBlackboardVO.setQtdeSalasNecessarias(Uteis.arredondarParaMais(salaAulaBlackboardVO.getQtdeAlunos()/salaAulaBlackboardVO.getAlunosPorSala()) - salaAulaBlackboardVO.getQtdeSalasExistentes());
			salaAulaBlackboardVO.setQtdeSalas(salaAulaBlackboardVO.getQtdeSalasExistentes()+salaAulaBlackboardVO.getQtdeSalasNecessarias());
			salaAulaBlackboardVO.setAlunosEnsalados(rs.getString("alunosEnsalados"));
			salaAulaBlackboardVO.setAlunosNaoEnsalados(rs.getString("alunosNaoEnsalados"));
			salaAulaBlackboardVO.setSalasExistentes(rs.getString("salasExistentes"));
			salaAulaBlackboardVOs.add(salaAulaBlackboardVO);
		}
		return salaAulaBlackboardVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardVO consultarSalaAulaBlackboardPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append(" where exists (select salaaulablackboardpessoa.codigo from salaaulablackboardpessoa where matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(") ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		SalaAulaBlackboardVO obj = new SalaAulaBlackboardVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoSalaAulaBlackboard(ConfiguracaoSeiBlackboardVO configSeiBlackboardVO, UsuarioVO usuarioVO) throws Exception {
		TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
		HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_GERAR_SALAS, RequestMethod.GET, null,  usuarioVO);
		if(!jsonResponse.isSuccess()) {
			throw new Exception(jsonResponse.getStatusText());
		}
	}
	
	@Override
	public void executarConsolidacaoDeNotasApuradasNoSeiPorSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaHistoricoParaConsolidarNotasPorSalaAulaBlackboard(obj, usuario);
		AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LOG_ENSALAMENTO_BLACKBOARD, "Codigo Operacao " +obj.getCodigoOperacao() +" qtd tarefas iniciadas" +historicoVOs.size() +" consolidarNotas, aguardando conclusão -"+ Uteis.getDataComHora(new Date()));
		for (HistoricoVO historicoVO : historicoVOs) {
			ThreadHistoricoNotaBlackboardApurar threadHistoricoNotaBlackboardApurar = new ThreadHistoricoNotaBlackboardApurar("Aluno-"+historicoVO.getMatricula().getMatricula(), "consolidarNotas", obj.getCodigoOperacao(), historicoVO,  usuario);
			threadHistoricoNotaBlackboardApurar.call();
		}
	}
	
	@Override
	public void executarApuracaoDeNotaSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj,  boolean considerarAuditoria, UsuarioVO usuario) throws Exception {		
		try {
			if(obj.getListaSalaAulaBlackboardPessoaNotaVO().stream().anyMatch(p-> !p.getSalaAulaBlackboardPessoaVO().getCodigo().equals(0) )) {				
				Map<Integer, ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOMap = new HashMap<>();				
				List<HistoricoVO> historicoVOs = new ArrayList<>();
				if(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaSeClassificadaComoTcc(obj.getDisciplina(), usuario)) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LOG_ENSALAMENTO_BLACKBOARD, "Codigo Operacao " +obj.getCodigoOperacao() +" qtd tarefas iniciadas 1 incluirHistoricoParaApuracaoNota, aguardando conclusão -"+ Uteis.getDataComHora(new Date()));
					ThreadHistoricoNotaBlackboardApurar tarefa = new ThreadHistoricoNotaBlackboardApurar("incluirHistoricoParaApuracaoNota", "incluirHistoricoParaApuracaoNota", obj.getCodigoOperacao(), obj,  usuario);
					tarefa.call();
					historicoVOs = getFacadeFactory().getHistoricoFacade().consultaHistoricoParaApuracaoNotaBlackboard(obj, true, usuario);
				}else {
					historicoVOs = getFacadeFactory().getHistoricoFacade().consultaHistoricoParaApuracaoNotaBlackboard(obj, false, usuario);	
				}
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LOG_ENSALAMENTO_BLACKBOARD, "Codigo Operacao " +obj.getCodigoOperacao() +" qtd tarefas iniciadas" +historicoVOs.size() +" apurarNotas, aguardando conclusão -"+ Uteis.getDataComHora(new Date()));
				for (HistoricoVO historicoVO : historicoVOs) {
					Optional<SalaAulaBlackboardPessoaNotaVO>  findFirst = obj.getListaSalaAulaBlackboardPessoaNotaVO().stream().filter(p-> p.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo().equals(historicoVO.getMatricula().getAluno().getCodigo())).findFirst();
					if(findFirst.isPresent() && findFirst.get().getSalaAulaBlackboardPessoaVO().getCodigo() > 0) {
						if (!configuracaoAcademicaNotaVOMap.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
							ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(historicoVO.getConfiguracaoAcademico(), TipoUsoNotaEnum.BLACKBOARD);
							if(Uteis.isAtributoPreenchido(configuracaoAcademicaNotaVO)) {
								configuracaoAcademicaNotaVOMap.put(historicoVO.getConfiguracaoAcademico().getCodigo(), configuracaoAcademicaNotaVO);	
							} else {
								getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().atualizarCampoErroSalaAulaBlackboardOperacao(obj.getCodigoOperacao(),"Não foi encontrada uma Configuração Acadêmica Nota para o Tipo de Uso Nota BLACKBOARD. (Configuração Acadêmica = "+historicoVO.getConfiguracaoAcademico().getCodigo()+")");
								continue;
							}
						}
						ThreadHistoricoNotaBlackboardApurar tarefa =new ThreadHistoricoNotaBlackboardApurar("Aluno-"+historicoVO.getMatricula().getMatricula(),"apurarNotas", obj.getCodigoOperacao(), obj, findFirst.get(), configuracaoAcademicaNotaVOMap, historicoVO, considerarAuditoria, obj.getRealizarCalculoMediaApuracaoNotas(), usuario);
						tarefa.call();
					}
				}
			
			}
			if(obj.getListaSalaAulaBlackboardPessoaNotaVO().stream().anyMatch(p-> p.getSalaAulaBlackboardPessoaVO().getCodigo().equals(0))) {
				List<SalaAulaBlackboardPessoaNotaVO> listaSalaAulaBlackboardPessoaNotaVOTemp = obj.getListaSalaAulaBlackboardPessoaNotaVO().stream().filter(p-> p.getSalaAulaBlackboardPessoaVO().getCodigo().equals(0)).collect(Collectors.toList());
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LOG_ENSALAMENTO_BLACKBOARD, "Codigo Operacao " +obj.getCodigoOperacao() +" qtd tarefas iniciadas" +listaSalaAulaBlackboardPessoaNotaVOTemp.size() +" apurarNotasNaoLocalizado, aguardando conclusão -"+ Uteis.getDataComHora(new Date()));
				for (SalaAulaBlackboardPessoaNotaVO sabPessoaNota : listaSalaAulaBlackboardPessoaNotaVOTemp) {
					ThreadHistoricoNotaBlackboardApurar tarefa = new ThreadHistoricoNotaBlackboardApurar("apurarNotasNaoLocalizado-"+sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome(), "apurarNotasNaoLocalizado", obj.getCodigoOperacao(), obj, sabPessoaNota,  usuario);
					tarefa.call();
				}
			}
		} catch (Exception e) {
			throw e;	
		}
	}

//	private void executarThreadHistoricoNotaBlackboardApurar(Integer codigoOperacao, List<ThreadHistoricoNotaBlackboardApurar> listaThreadHistoricoNotaBlackboardApurar) {
//		//System.out.println("Codigo Operacao" +codigoOperacao +" qtd tarefas iniciadas" +listaThreadHistoricoNotaBlackboardApurar.size() +", aguardando conclusão -"+ Uteis.getDataComHora(new Date()));
////		final ConsistirException ce = new ConsistirException();
////		ProcessarParalelismo.executar(0, listaThreadHistoricoNotaBlackboardApurar.size(), ce, new ProcessarParalelismo.Processo() {
////			@Override
////			public void run(int i) {
////				listaThreadHistoricoNotaBlackboardApurar.get(i).call();
////			}
////		});
//
//// estou de conexao com banco de dados
////		ExecutorService threadPool = Executors.newFixedThreadPool(listaThreadHistoricoNotaBlackboardApurar.size());
////		ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(threadPool);
////		//executa as tarefas
////		for (ThreadHistoricoNotaBlackboardApurar tarefa : listaThreadHistoricoNotaBlackboardApurar) {
////		    completionService.submit(tarefa);
////		}
////		for (int i = 0; i < listaThreadHistoricoNotaBlackboardApurar.size(); i++) {
////		    try {
////		    	completionService.take().get();//retorno da chamada do call
////		    } catch (InterruptedException | ExecutionException ex) {
////		        ex.printStackTrace();
////		    }
////		}
////		threadPool.shutdown();
//	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void preencherHistoricoNotaBlackboardMotivoConfiguracaoAcademica(SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO sabPessoaNota,HistoricoVO historicoVO, UsuarioVO usuarioLogado) throws Exception {
	
			HistoricoNotaBlackboardVO historicoNotaBlackboardVO = getFacadeFactory().getHistoricoNotaBlackboardFacade().consultarPorHistoricoESituacaoHistoricoNotaBlackboardEnum(historicoVO);			
			historicoNotaBlackboardVO.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarPorChavePrimaria(obj.getSalaAulaBlackboard()));
			historicoNotaBlackboardVO.getSalaAulaBlackboardVO().setCodigo(obj.getSalaAulaBlackboard());
			historicoNotaBlackboardVO.setMotivo("A nota do aluno no AVA não pode ser maior que a Faixa de Nota informada na Configuração Acadêmica.");
			historicoNotaBlackboardVO.setSituacaoHistoricoNotaBlackboardEnum(SituacaoHistoricoNotaBlackboardEnum.ERRO);
			historicoNotaBlackboardVO.setEmailPessoaBlackboard(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());
			historicoNotaBlackboardVO.setNota(sabPessoaNota.getNota());
			historicoNotaBlackboardVO.setNomePessoaBlackboard(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome());		
//		getFacadeFactory().getHistoricoNotaBlackboardFacade().persistir(historicoNotaBlackboardVO, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherNotaHistoricoSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO sabPessoaNota, Map<Integer, ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOMap, HistoricoVO historicoVO, boolean considerarAuditoria, UsuarioVO usuarioLogado, StringBuilder log )  throws Exception{
		try {			
			Uteis.checkState(!configuracaoAcademicaNotaVOMap.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo()), "Não foi encontrado uma CONFIGURAÇÃO ACADEMICA NOTA BLACKBOARD parametrizada.");
			
			if(Uteis.isAtributoPreenchido(configuracaoAcademicaNotaVOMap) && configuracaoAcademicaNotaVOMap.get(historicoVO.getConfiguracaoAcademico().getCodigo()).getTipoUsoNota().equals(TipoUsoNotaEnum.BLACKBOARD)) {
				if(sabPessoaNota.getNota() != null && configuracaoAcademicaNotaVOMap.get(historicoVO.getConfiguracaoAcademico().getCodigo()).getFaixaNotaMaior() < sabPessoaNota.getNota()) {
					preencherHistoricoNotaBlackboardMotivoConfiguracaoAcademica(obj, sabPessoaNota, historicoVO, usuarioLogado);
					return;
				}	
			}
			
			Field field = HistoricoVO.class.getDeclaredField(StringUtils.join("nota", configuracaoAcademicaNotaVOMap.get(historicoVO.getConfiguracaoAcademico().getCodigo()).getNota().getNumeroNota()));
			field.setAccessible(true);
			Double nota = (Double) FieldUtils.readField(field, historicoVO);
			boolean alterarNota = true;
			considerarAuditoria =  false;
			if(considerarAuditoria) {
				alterarNota = alterarNota && validarSeAlunoColouGrau(historicoVO, nota, sabPessoaNota, obj, usuarioLogado);
				alterarNota = alterarNota && validarDataLimiteApuracaoNota(historicoVO, nota, sabPessoaNota, obj, usuarioLogado);
			}
			alterarNota = alterarNota && Uteis.isAtributoPreenchido(sabPessoaNota.getNota(), true);
			if (alterarNota) {
				FieldUtils.writeField(field, historicoVO, sabPessoaNota.getNota());
				preencherHistoricoNotaBlackboardVO(historicoVO, nota, null, SituacaoHistoricoNotaBlackboardEnum.APURADO, sabPessoaNota, obj, usuarioLogado);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private boolean validarDataLimiteApuracaoNota(HistoricoVO historicoVO, Double nota, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO, SalaAulaBlackboardNotaVO obj, UsuarioVO usuarioLogado) throws Exception {
		CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getUnidadeEnsino().getCodigo(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getTurmaAgrupada(), 0, historicoVO.getDisciplina().getCodigo(), historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getPeriodicidade(), historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico(), false, usuarioLogado);
		if (Uteis.isAtributoPreenchido(nota)
				&& !salaAulaBlackboardPessoaNotaVO.getNota().equals(nota)
				&& (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO)
				&& (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal()) && calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now()))
				|| (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal()) && calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now())))) {
			preencherHistoricoNotaBlackboardVO(historicoVO, nota, "Fora do período do calendário de apuração de nota", SituacaoHistoricoNotaBlackboardEnum.EM_AUDITORIA, salaAulaBlackboardPessoaNotaVO, obj, usuarioLogado);
			return false;
		}
		return true;
	}

	private boolean validarSeHouveMudancaNota(HistoricoVO historicoVO, Double nota, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO, UsuarioVO usuarioLogado) throws Exception {
//		if (Uteis.isAtributoPreenchido(nota) && !salaAulaBlackboardPessoaNotaVO.getNota().equals(nota)) {
//			preencherHistoricoNotaBlackboardVO(historicoVO, nota, "Nota anterior divergente da nota apurada", SituacaoHistoricoNotaBlackboardEnum.EM_AUDITORIA, salaAulaBlackboardPessoaNotaVO, usuarioLogado);
//			return false;
//		}
		return true;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private boolean validarSeAlunoColouGrau(HistoricoVO historicoVO, Double nota, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO, SalaAulaBlackboardNotaVO obj, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(nota) && !salaAulaBlackboardPessoaNotaVO.getNota().equals(nota) && Uteis.isAtributoPreenchido(historicoVO.getMatricula().getDataColacaoGrau())) {
			preencherHistoricoNotaBlackboardVO(historicoVO, nota, "Aluno com colação de grau", SituacaoHistoricoNotaBlackboardEnum.EM_AUDITORIA, salaAulaBlackboardPessoaNotaVO, obj, usuarioLogado);
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void preencherHistoricoNotaBlackboardVO(HistoricoVO historicoVO, Double nota, String motivo, SituacaoHistoricoNotaBlackboardEnum situacaoHistoricoNotaBlackboardEnum, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO, SalaAulaBlackboardNotaVO obj, UsuarioVO usuario) throws Exception {
		HistoricoNotaBlackboardVO historicoNotaBlackboardVO = getFacadeFactory().getHistoricoNotaBlackboardFacade().consultarPorHistoricoESituacaoHistoricoNotaBlackboardEnum(historicoVO);
		historicoNotaBlackboardVO.setHistoricoVO(historicoVO);
		historicoNotaBlackboardVO.setSituacaoHistoricoNotaBlackboardEnum(situacaoHistoricoNotaBlackboardEnum);
		historicoNotaBlackboardVO.setMotivo(motivo);
		historicoNotaBlackboardVO.setSalaAulaBlackboardPessoaVO(salaAulaBlackboardPessoaNotaVO.getSalaAulaBlackboardPessoaVO());
		historicoNotaBlackboardVO.getSalaAulaBlackboardVO().setCodigo(obj.getSalaAulaBlackboard());
		historicoNotaBlackboardVO.setNotaAnterior(nota);
		historicoNotaBlackboardVO.setNota(salaAulaBlackboardPessoaNotaVO.getNota());
//		getFacadeFactory().getHistoricoNotaBlackboardFacade().persistir(historicoNotaBlackboardVO, false, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherHistoricoNotaBlackboardNaoLocalizado(SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO,  UsuarioVO usuario) throws Exception {
		try {
			HistoricoNotaBlackboardVO historicoNotaBlackboardVO = getFacadeFactory().getHistoricoNotaBlackboardFacade().consultarPorSalaAulaBlacboardNotaNaoLocalizada(obj.getSalaAulaBlackboard(), salaAulaBlackboardPessoaNotaVO.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());
			historicoNotaBlackboardVO.setSituacaoHistoricoNotaBlackboardEnum(SituacaoHistoricoNotaBlackboardEnum.NAO_LOCALIZADO);
			historicoNotaBlackboardVO.setMotivo(" Aluno Existente no AVA porém não encontrado no SEI.");
			historicoNotaBlackboardVO.getSalaAulaBlackboardVO().setCodigo(obj.getSalaAulaBlackboard());
			historicoNotaBlackboardVO.setNomePessoaBlackboard(salaAulaBlackboardPessoaNotaVO.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome());
			historicoNotaBlackboardVO.setEmailPessoaBlackboard(salaAulaBlackboardPessoaNotaVO.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());
			historicoNotaBlackboardVO.setNota(salaAulaBlackboardPessoaNotaVO.getNota());
//			getFacadeFactory().getHistoricoNotaBlackboardFacade().persistir(historicoNotaBlackboardVO, false, usuario);	
		} catch (Exception e) {
			throw e;
		}
	}	
	
	
	
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(String listaAlunos, DisciplinaVO disciplinaVO, String ano, String semestre) throws Exception{
		String[] arrayMPTDVOs = listaAlunos.split(",");
		StringBuilder sql =  new StringBuilder("select distinct matriculaperiodoturmadisciplina.matricula, matricula.aluno as codigo_aluno, matriculaperiodoturmadisciplina.codigo, pessoa.nome as nome_aluno, ");
		sql.append(" salaAulaLatera.codigo as codigo_sala, salaAulaLatera.nome as nome_sala, curso.nome as nome_curso, unidadeEnsino.nome as nome_unidadeEnsino, pessoaemailinstitucional.email ");
		sql.append(" from matriculaperiodoturmadisciplina inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.statusAtivoInativoEnum =  'ATIVO'  ");
		sql.append(" and pessoaemailinstitucional.codigo = (select p.codigo from pessoaemailinstitucional as p where p.pessoa = pessoa.codigo and p.statusAtivoInativoEnum =  'ATIVO' order by p.codigo desc limit 1) ");		
		sql.append(" left join lateral(");
		sql.append(" select salaaulablackboard.codigo, salaaulablackboard.nome, salaaulablackboardpessoa.matriculaperiodoturmadisciplina from salaaulablackboardpessoa ");  
		sql.append(" inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");		
		sql.append(" and salaaulablackboard.disciplina = ").append(disciplinaVO.getCodigo());
		sql.append(" and salaaulablackboard.ano = '").append(ano).append("'");
		sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("'");
		if(Uteis.isAtributoPreenchido(disciplinaVO.getClassificacaoDisciplina()) && disciplinaVO.getClassificacaoDisciplina().isProjetoIntegrador()) {
			sql.append(" and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_AMBIENTACAO' ");
		}else {
			sql.append(" and salaaulablackboard.tiposalaaulablackboardenum = 'DISCIPLINA' ");
		}
		sql.append(") as salaAulaLatera on salaAulaLatera.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");		
		sql.append(" where matriculaperiodoturmadisciplina.codigo in (?) order by pessoa.nome ");
		StringBuilder in = new StringBuilder("");
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		int x = 0;
		for(String mptd : arrayMPTDVOs ) {
			x++;
			if(in.length() > 0) {
				in.append(",");
			}
			in.append(mptd);
			if(x > 100) {	
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString().replace("?", in.toString()));
				matriculaPeriodoTurmaDisciplinaVOs.addAll(montarDadosMPTD(rs));
				x = 0;
				in = new StringBuilder("");				
			}
		}
		if(x > 0) {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString().replace("?", in.toString()));
			matriculaPeriodoTurmaDisciplinaVOs.addAll(montarDadosMPTD(rs));
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	private List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosMPTD(SqlRowSet rs){
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while(rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO mptd =  new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			mptd.getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("codigo_aluno"));
			mptd.getMatriculaObjetoVO().getAluno().setNome(rs.getString("nome_aluno"));
			mptd.getMatriculaObjetoVO().getAluno().setEmail(rs.getString("email"));
			mptd.getMatriculaObjetoVO().getCurso().setNome(rs.getString("nome_curso"));
			mptd.getMatriculaObjetoVO().getUnidadeEnsino().setNome(rs.getString("nome_unidadeEnsino"));
			mptd.getSalaAulaBlackboardVO().setCodigo(rs.getInt("codigo_sala"));
			mptd.getSalaAulaBlackboardVO().setNome(rs.getString("nome_sala"));
			mptd.setCodigo(rs.getInt("codigo"));
			matriculaPeriodoTurmaDisciplinaVOs.add(mptd);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardVO> consultarSalaAulasExistentes(String salaAulaExistentes, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(true);
		sqlStr.append(" where salaaulablackboard.codigo in (?) order by salaaulablackboard.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString().replace("?", salaAulaExistentes));		
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
	}

	public File realizarGeracaoExcelLogNotas(DataModelo controleConsultaHistoricoNotaBlackboard, UsuarioVO usuario) throws Exception {
		File arquivo = null;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("LogNota");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		montarCabecalhoRelatorioExcel(uteisExcel, sheet);
		montarItensRelatorioExcel(uteisExcel, sheet, controleConsultaHistoricoNotaBlackboard);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + new Date().getTime() + ".xlsx");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}


	@Override
	public List<SalaAulaBlackboardVO> consultarGruposPorFacilitador(Integer codigoPessoaFacilitador) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT salaaulablackboard.codigo, salaaulablackboard.nomegrupo ");
		sql.append("FROM salaaulablackboard ");
		sql.append("INNER JOIN salaaulablackboardpessoa ON salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append("INNER JOIN pessoaemailinstitucional ON pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = pessoaemailinstitucional.pessoa ");
		sql.append("WHERE salaaulablackboardpessoa. tiposalaaulablackboardpessoaenum = 'FACILITADOR' ");
		sql.append("AND salaaulablackboard.tiposalaaulablackboardenum = 'TCC_GRUPO' ");
		sql.append("AND pessoa.codigo = ? ");
		sql.append("GROUP BY salaaulablackboard.codigo ORDER BY salaaulablackboard.codigo DESC, salaaulablackboard.nomegrupo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPessoaFacilitador);
		List<SalaAulaBlackboardVO> salaAulaBlackboardVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			SalaAulaBlackboardVO salaAulaBlackboardVO = new SalaAulaBlackboardVO();
			salaAulaBlackboardVO.setCodigo(tabelaResultado.getInt("codigo"));
			salaAulaBlackboardVO.setNomeGrupo(tabelaResultado.getString("nomegrupo"));
			salaAulaBlackboardVOs.add(salaAulaBlackboardVO);
		}
		return salaAulaBlackboardVOs;
	}
	private void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet) {
		int cellnum = 0;
		Row row = sheet.createRow(sheet.getLastRowNum());
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5400 , "Sala");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 12000, "Usuário Responsável");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4500 , "Data");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 4500, "Disciplina");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 12000, "Nome");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000, "Email");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500, "Nota Ava");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 22000, "Motivo");
	}

	@SuppressWarnings("unchecked")
	private void montarItensRelatorioExcel(UteisExcel uteisExcel, XSSFSheet sheet,  DataModelo controleConsultaHistoricoNotaBlackboard) {
		controleConsultaHistoricoNotaBlackboard.getListaConsulta().forEach(obj -> {
			int cellnum = 0;			
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getSalaAulaBlackboardVO().getIdSalaAulaBlackboard());
			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getUsuarioResponsavel().getNome());

			Cell cell = row.createCell(cellnum++);
			cell.setCellStyle(uteisExcel.preencherLayoutData());
			cell.setCellValue(((HistoricoNotaBlackboardVO) obj).getDataDeferimentoIndeferimentoFiltro());

			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getSalaAulaBlackboardVO().getDisciplinaVO().getAbreviatura());
			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getNomePessoaBlackboard());
			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getEmailPessoaBlackboard());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(uteisExcel.preencherLayoutNumerico());
			cell.setCellValue(((HistoricoNotaBlackboardVO) obj).getNota());

			uteisExcel.preencherCelula(row, cellnum++, ((HistoricoNotaBlackboardVO) obj).getMotivo());
		});
	}
	
	/**
	 * Método que tem por sua finalidade validar se existem pendências para criações
	 * de históricos vinculadas a disciplinas de estágios
	 * 
	 * @param ultimaMatriculaPeriodoVO
	 * @param tipoSalaAulaBlackboardEnum
	 * @param usuarioVO
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarHistoricosDeEstagioCriados(MatriculaVO matriculaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoVO, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) {
		if (Uteis.isAtributoPreenchido(ultimaMatriculaPeriodoVO) && Objects.nonNull(tipoSalaAulaBlackboardEnum) && tipoSalaAulaBlackboardEnum.isEstagio()) {
			getFacadeFactory().getHistoricoFacade().incluirHistoricosDeEstagioNaoCriados(ultimaMatriculaPeriodoVO, usuarioVO);
			getFacadeFactory().getHistoricoFacade().atualizarHistoricosEstagioCursandoParaAprovado(matriculaVO);
		}
	}
	
	@Override
	public List<PessoaVO> consultarFacilitadorProfessorSupervisorPorNome(String valorConsultaPessoa, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.codigo, pessoa.nome, pessoa.email, pessoa.cpf "); 
		sql.append(" FROM salaaulablackboard ");
		sql.append(" INNER JOIN salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" INNER JOIN pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional "); 
		sql.append(" INNER JOIN pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa "); 
		sql.append(" WHERE salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum <> 'ALUNO' ");  
		sql.append(" AND salaaulablackboard.ano = '").append(ano).append("' "); 
		sql.append(" AND salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" AND sem_acentos((pessoa.nome)) ILIKE (trim(sem_acentos(?)))");
		sql.append(" ORDER BY pessoa.nome "); 
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsultaPessoa);
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			obj.setCPF(rs.getString("cpf"));
			obj.setEmail(rs.getString("email"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	public List<PessoaVO> consultarFacilitadorProfessorSupervisorPorCPF(String valorConsultaPessoa, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.codigo, pessoa.nome, pessoa.email, pessoa.cpf "); 
		sql.append(" FROM salaaulablackboard ");
		sql.append(" INNER JOIN salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sql.append(" INNER JOIN pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional "); 
		sql.append(" INNER JOIN pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa "); 
		sql.append(" WHERE salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum <> 'ALUNO' ");  
		sql.append(" AND salaaulablackboard.ano = '").append(ano).append("' "); 
		sql.append(" AND salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" AND (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Uteis.retirarMascaraCPF(valorConsultaPessoa) + PERCENT);
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			obj.setCPF(rs.getString("cpf"));
			obj.setEmail(rs.getString("email"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
}