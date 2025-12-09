package jobs;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaNotaVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class ThreadHistoricoNotaBlackboardApurar extends SuperFacadeJDBC implements Callable<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6412415514442013529L;
	private String  identificadorTarefa;
	private Integer  codigoOperacao;
	private SalaAulaBlackboardNotaVO obj;
	private SalaAulaBlackboardPessoaNotaVO sabPessoaNota;
	private Map<Integer, ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOMap;
	private HistoricoVO  historicoVO;
	private boolean considerarAuditoria;
	private boolean realizarCalculoMediaApuracaoNotas;
	private UsuarioVO usuarioLogado;
	private String nomeRotinaExecutar;
	
	@Override
	public String call() {
		try {
			switch (nomeRotinaExecutar) {
			case "incluirHistoricoParaApuracaoNota":
				incluirHistoricoParaApuracaoNota();
				break;
			case "consolidarNotas":
				consolidarNotas();
				break;
			case "apurarNotas":
				apurarNotas();
				break;
			case "apurarNotasNaoLocalizado":
				apurarNotasNaoLocalizado();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return identificadorTarefa;
	}

	public ThreadHistoricoNotaBlackboardApurar(String  identificadorTarefa, String nomeRotinaExecutar, Integer  codigoOperacao, SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO sabPessoaNota, Map<Integer, ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOMap, HistoricoVO  historicoVO,  Boolean considerarAuditoria, boolean realizarCalculoMediaApuracaoNotas, UsuarioVO usuarioLogado) {
		this.identificadorTarefa = identificadorTarefa;
		this.nomeRotinaExecutar = nomeRotinaExecutar;
		this.codigoOperacao = codigoOperacao;
		this.obj = obj;
		this.sabPessoaNota = sabPessoaNota;
		this.configuracaoAcademicaNotaVOMap = configuracaoAcademicaNotaVOMap;
		this.considerarAuditoria = considerarAuditoria;
		this.realizarCalculoMediaApuracaoNotas = realizarCalculoMediaApuracaoNotas;
		this.historicoVO = historicoVO;
		this.usuarioLogado = usuarioLogado;
	}
	
	public ThreadHistoricoNotaBlackboardApurar(String  identificadorTarefa, String nomeRotinaExecutar, Integer  codigoOperacao, SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO sabPessoaNota,  UsuarioVO usuarioLogado) {
		this.identificadorTarefa = identificadorTarefa;
		this.nomeRotinaExecutar = nomeRotinaExecutar;
		this.codigoOperacao = codigoOperacao;
		this.obj = obj;
		this.sabPessoaNota = sabPessoaNota;
		this.usuarioLogado = usuarioLogado;
	}
	
	public ThreadHistoricoNotaBlackboardApurar(String  identificadorTarefa, String nomeRotinaExecutar, Integer  codigoOperacao, HistoricoVO  historicoVO,  UsuarioVO usuarioLogado) {
		this.identificadorTarefa = identificadorTarefa;
		this.nomeRotinaExecutar = nomeRotinaExecutar;
		this.codigoOperacao = codigoOperacao;
		this.historicoVO = historicoVO;
		this.usuarioLogado = usuarioLogado;
	}
	
	public ThreadHistoricoNotaBlackboardApurar(String  identificadorTarefa, String nomeRotinaExecutar, Integer  codigoOperacao, SalaAulaBlackboardNotaVO obj,  UsuarioVO usuarioLogado) {
		this.identificadorTarefa = identificadorTarefa;
		this.nomeRotinaExecutar = nomeRotinaExecutar;
		this.codigoOperacao = codigoOperacao;
		this.obj = obj;
		this.usuarioLogado = usuarioLogado;
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHistoricoParaApuracaoNota()  {
		try {
			String listaCodigoSalaAulaBlackboardPessoa = obj.getListaSalaAulaBlackboardPessoaNotaVO()
					.stream()
					.filter(p-> p.getSalaAulaBlackboardPessoaVO().getCodigo() > 0)
					.map(p -> p.getSalaAulaBlackboardPessoaVO().getCodigo().toString())
					.collect(Collectors.joining(","));
			StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append(" INSERT INTO public.historico ");
			sqlStr.append(" (matriculaperiodo, dataregistro, situacao, tipohistorico, matricula, disciplina, nomedisciplina, matriculaperiodoturmadisciplina,  ");
			sqlStr.append("  configuracaoacademico,  historicodisciplinafazpartecomposicao, anohistorico, semestrehistorico, freguencia, ");
			sqlStr.append("  gradedisciplina, cargahorariacursada, cargahorariadisciplina, historicodisciplinaoptativa, historicodisciplinaforagrade,  ");
			sqlStr.append("  historicoporequivalencia, historicoequivalente, mapaequivalenciadisciplina,  periodoletivomatrizcurricular, ");
			sqlStr.append("  periodoletivocursada, matrizcurricular,  creditodisciplina, historicodisciplinacomposta, disciplinareferenteaumgrupooptativa, ");
			sqlStr.append("  historicoincluidoporsql, created, codigocreated, nomecreated, responsavel) ");
			sqlStr.append("  ( ");
			sqlStr.append("   select matriculaperiodo.codigo, matriculaperiodo.\"data\", 'NC',  'NO', matricula.matricula, disciplina.codigo, disciplina.nome, null, ");
			sqlStr.append("   coalesce(gradedisciplina.configuracaoacademico, curso.configuracaoacademico) configuracaoacademico, false, matriculaperiodo.ano, matriculaperiodo.semestre , 100 as freguencia, ");
			sqlStr.append("   gradedisciplina.codigo, gradedisciplina.cargahoraria, gradedisciplina.cargahoraria, false, false,  ");
			sqlStr.append("   false, false, null, periodoletivo.codigo,  ");
			sqlStr.append("   matriculaperiodo.periodoletivomatricula , matricula.gradecurricularatual, gradedisciplina.nrcreditos, false, false,  ");
			sqlStr.append("   true,  now(), ").append(usuarioLogado.getCodigo()).append(",'").append(usuarioLogado.getNome()).append("',").append(usuarioLogado.getCodigo());
			sqlStr.append("   from  salaaulablackboardpessoa ");
			sqlStr.append("   inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
			sqlStr.append("   inner join salaaulablackboard on salaaulablackboard.codigo =  salaaulablackboardpessoa.salaaulablackboard ");
			sqlStr.append("   inner join matricula on matricula.aluno = pessoaemailinstitucional.pessoa and matricula.matricula IN ( ");
			sqlStr.append("   	select m.matricula  from matriculaperiodo mp ");
			sqlStr.append("   	inner join matricula as m on m.matricula = mp.matricula ");
			sqlStr.append("   	where  mp.ano = salaaulablackboard.ano and mp.semestre = salaaulablackboard.semestre and m.aluno = pessoaemailinstitucional.pessoa ");
			sqlStr.append("   	order by case when m.situacao = 'AT' then 1 else 2 end ");
			sqlStr.append("   )");
			sqlStr.append("   inner join curso on matricula.curso = curso.codigo ");
			sqlStr.append("   inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and matriculaperiodo.ano = salaaulablackboard.ano and matriculaperiodo.semestre = salaaulablackboard.semestre ");
			sqlStr.append("   inner join periodoletivo on periodoletivo.gradecurricular  = matricula.gradecurricularatual ");
			sqlStr.append("   inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  ");
			sqlStr.append("   inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
			sqlStr.append("   where salaaulablackboard.codigo = ").append(obj.getSalaAulaBlackboard());
			sqlStr.append("   and salaaulablackboardpessoa.codigo in (").append(listaCodigoSalaAulaBlackboardPessoa).append(")");	
			sqlStr.append("   and ( gradedisciplina.disciplinatcc = true or disciplina.classificacaodisciplina = 'TCC' ) ");
			sqlStr.append("   and matriculaperiodo.codigo is not null ");
			sqlStr.append("   and not exists (");
			sqlStr.append("   	select historico.codigo  from historico where historico.matriculaperiodo = matriculaperiodo.codigo and historico.gradedisciplina = gradedisciplina.codigo ");
			sqlStr.append("   	and historico.disciplina  = disciplina.codigo ");
			sqlStr.append("   ))");
			sqlStr.append(" --ul:").append(usuarioLogado.getCodigo());
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			StringBuilder log = new StringBuilder();
			log.append("-----------------INCLUIR NOTA HistoricoNotaBlackboardApurar ---------------------");
			log.append("salaAulaBlackboard - ").append(obj.getSalaAulaBlackboard());    					
			log.append(System.lineSeparator());
			log.append("erro - ").append(e.getMessage());
			atualizarCampoErroSalaAulaBlackboardOperacao(codigoOperacao, log.toString());
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void consolidarNotas() {
		StringBuilder log = new StringBuilder();
		try {
			getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(Arrays.asList(historicoVO), true, true, usuarioLogado);
		} catch (Exception e) {
			log.append("-----------------CONSOLIDAR NOTA HistoricoNotaBlackboardApurar ---------------------");
			log.append("historico - ").append(historicoVO.getCodigo());
			log.append(System.lineSeparator());
			log.append("nome - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome());
			log.append(System.lineSeparator());
			log.append("email - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());    					
			log.append(System.lineSeparator());
			log.append("erro - ").append(e.getMessage());
			atualizarCampoErroSalaAulaBlackboardOperacao(codigoOperacao, log.toString());
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void apurarNotas() {
		StringBuilder log = new StringBuilder();
		try {			
			getFacadeFactory().getSalaAulaBlackboardFacade().preencherNotaHistoricoSalaAulaBlackboard(obj, sabPessoaNota, configuracaoAcademicaNotaVOMap, historicoVO, considerarAuditoria, usuarioLogado, log);
			getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(Arrays.asList(historicoVO), false, realizarCalculoMediaApuracaoNotas, usuarioLogado);
		} catch (Exception e) {
			log.append("-----------------APURACAO NOTA ---------------------");
			log.append("historico - ").append(historicoVO.getCodigo());
			log.append(System.lineSeparator());
			log.append("nome - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome());
			log.append(System.lineSeparator());
			log.append("email - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());    					
			log.append(System.lineSeparator());
			log.append("erro - ").append(e.getMessage());
			atualizarCampoErroSalaAulaBlackboardOperacao(codigoOperacao, log.toString());
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void apurarNotasNaoLocalizado() {
    	try {
    		getFacadeFactory().getSalaAulaBlackboardFacade().preencherHistoricoNotaBlackboardNaoLocalizado(obj, sabPessoaNota,  usuarioLogado);
    	} catch (Exception e) {
    		StringBuilder log = new StringBuilder();
    		log.append("-----------------APURACAO NOTA NAO LOCALIZADA ---------------------");
			log.append(System.lineSeparator());
			log.append("nome - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getNome());
			log.append(System.lineSeparator());
			log.append("email - ").append(sabPessoaNota.getSalaAulaBlackboardPessoaVO().getPessoaEmailInstitucionalVO().getEmail());
			log.append(System.lineSeparator());
			log.append("erro - ").append(e.getMessage());	
			log.append(System.lineSeparator());
    		atualizarCampoErroSalaAulaBlackboardOperacao(codigoOperacao, log.toString());
    	}
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void atualizarCampoErroSalaAulaBlackboardOperacao(Integer codigo, String erro) {
		StringBuilder sqlStr = new StringBuilder(" UPDATE  salaaulablackboardoperacao set erro = coalesce (erro, '') || ?, executada = true, updated = now() where codigo = ?");
		getConexao().getJdbcTemplate().update(sqlStr.toString(), erro, codigo);
	}    
			
	
	

}
