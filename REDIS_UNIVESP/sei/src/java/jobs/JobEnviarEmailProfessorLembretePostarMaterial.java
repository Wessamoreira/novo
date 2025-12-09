package jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.job.NotificacaoProfessorPostarMaterialVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Service
@Lazy
public class JobEnviarEmailProfessorLembretePostarMaterial extends ControleAcesso implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6254264490387157176L;

	@Override
	public void run() {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate1 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PROFESSOR_POSTAGEM_MATERIAL, false, null);
			if ((mensagemTemplate1 != null && !mensagemTemplate1.getDesabilitarEnvioMensagemAutomatica())) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemProfessorPostagemMaterial(consultarNotificacaoProfessorPostarMaterial());
			}
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate2 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COORDENADOR_INICIO_TURMA, false, null);
			if ((mensagemTemplate2 != null && !mensagemTemplate2.getDesabilitarEnvioMensagemAutomatica())) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemCoordenadorInicioTurma(consultarNotificacaoCoordenadorInicioTurma());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public List<NotificacaoProfessorPostarMaterialVO> consultarNotificacaoProfessorPostarMaterial() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct turma.identificadorTurma as turma,  horarioturmadia.data as dataAula, disciplina.nome as disciplina, "); 
		sql.append(" pessoa.codigo as professor_codigo, pessoa.nome as professor_nome, pessoa.email as professor_email, unidadeensino.codigo as unidadeensino_codigo "); 
		sql.append(" from  horarioturma ");
		sql.append(" inner join turma on  turma.codigo =  horarioturma.turma");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
		sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");
		sql.append(" and qtdeDiasNotificarProfessorPostarMaterial  >= 0 ");
		sql.append(" inner join horarioturmadia on  horarioturmadia.horarioturma =  horarioturma.codigo ");
		sql.append(" and current_date =   (horarioturmadia.data::DATE - (qtdeDiasNotificarProfessorPostarMaterial||' day')::interval)");
		sql.append(" inner join horarioturmadiaitem on  horarioturmadia.codigo =  horarioturmadiaitem.horarioturmadia ");
		sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sql.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		sql.append(" where not exists (");
		sql.append(" 	select htd.codigo from horarioturmadia htd");
		sql.append(" 	inner join horarioturmadiaitem htdi on  htd.codigo =  htdi.horarioturmadia ");
		sql.append(" 	where htd.horarioturma = horarioturma.codigo");
		sql.append(" 	and htdi.disciplina = disciplina.codigo");
		sql.append(" 	and htdi.professor = pessoa.codigo");
		sql.append(" 	and htd.data < horarioturmadia.data");
		sql.append(" 	limit 1");
		sql.append(" )");
		sql.append(" and not exists (");
		sql.append(" 	select arquivo.codigo from arquivo");
		sql.append(" 	where ((arquivo.disciplina = disciplina.codigo AND    arquivo.turma = turma.codigo))");
		sql.append(" 	and ((apresentardeterminadoperiodo is null or apresentardeterminadoperiodo=false)");
		sql.append(" 	or  (apresentardeterminadoperiodo =  true and dataindisponibilizacao > horarioturmadia.data))");
		sql.append(" 	union all");
		sql.append(" 	select arquivo.codigo from arquivo");
		sql.append(" 	where ((arquivo.disciplina = disciplina.codigo AND arquivo.professor = pessoa.codigo))");
		sql.append(" 	and ((apresentardeterminadoperiodo is null or apresentardeterminadoperiodo=false)");
		sql.append(" 	or  (apresentardeterminadoperiodo =  true and dataindisponibilizacao > horarioturmadia.data))");
		sql.append(" 	union all");
		sql.append(" 	select arquivo.codigo from arquivo");
		sql.append(" 	where ((arquivo.disciplina = disciplina.codigo and arquivo.turma is null  and arquivo.professor is null))");
		sql.append(" 	and ((apresentardeterminadoperiodo is null or apresentardeterminadoperiodo=false)");
		sql.append(" 	or  (apresentardeterminadoperiodo =  true and dataindisponibilizacao > horarioturmadia.data))");
		sql.append(" 	union all");
		sql.append(" 	select arquivo.codigo from arquivo");
		sql.append(" 	where ((arquivo.disciplina = disciplina.codigo and arquivo.turma is not null and arquivo.turma = turma.codigo and arquivo.professor is null ))");
		sql.append(" 	and ((apresentardeterminadoperiodo is null or apresentardeterminadoperiodo=false)");
		sql.append(" 	or  (apresentardeterminadoperiodo =  true and dataindisponibilizacao > horarioturmadia.data))");
		sql.append(" 	union all");
		sql.append(" 	select arquivo.codigo from arquivo");
		sql.append(" 	where ((arquivo.disciplina = disciplina.codigo and arquivo.turma is not null and arquivo.turma = turma.codigo ");
		sql.append(" 	and arquivo.professor is not null and  arquivo.professor =  pessoa.codigo))");
		sql.append(" 	and ((apresentardeterminadoperiodo is null or apresentardeterminadoperiodo=false)");
		sql.append(" 	or  (apresentardeterminadoperiodo =  true and dataindisponibilizacao > horarioturmadia.data))");
		sql.append(" 	limit 1) ");		
		List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs = new ArrayList<NotificacaoProfessorPostarMaterialVO>(0);
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			NotificacaoProfessorPostarMaterialVO obj = new NotificacaoProfessorPostarMaterialVO();
			obj.setTurma(rs.getString("turma"));    		
    		obj.setDisciplina(rs.getString("disciplina"));
    		obj.setDataAula(rs.getDate("dataAula"));
    		obj.getProfessor().setCodigo(rs.getInt("professor_codigo"));
    		obj.getProfessor().setNome(rs.getString("professor_nome"));
    		obj.getProfessor().setEmail(rs.getString("professor_email"));
    		obj.setUnidadeEnsino(rs.getInt("unidadeensino_codigo"));
    		notificacaoProfessorPostarMaterialVOs.add(obj);
		}
		return notificacaoProfessorPostarMaterialVOs;
	}
	
	
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<NotificacaoProfessorPostarMaterialVO> consultarNotificacaoCoordenadorInicioTurma() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct disciplina.nome as disciplina, turma.identificadorturma, curso.nome as curso, horarioturmadia.data, ");
		sql.append(" coordenador.nome as coordenador, coordenador.email, coordenador.codigo as coordenador_codigo, unidadeensino.codigo as unidadeensino_codigo ");
		sql.append(" from  horarioturma ");
		sql.append(" inner join turma on  turma.codigo =  horarioturma.turma");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
		sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");
		sql.append(" and diasParaNotificarCoordenadorInicioTurma  > 0 ");
		sql.append(" inner join curso on case when turma.turmaagrupada then curso.codigo in (select tu.curso from turmaagrupada ");
		sql.append(" inner join turma tu on tu.codigo = turmaorigem where turmaagrupada.turma = turma.codigo ) else turma.curso = curso.codigo  end");
		sql.append(" inner join cursocoordenador on cursocoordenador.curso = curso.codigo");
		sql.append(" and cursocoordenador.unidadeensino = unidadeensino.codigo");
		sql.append(" and (cursocoordenador.turma is null or cursocoordenador.turma =  turma.codigo)");
		sql.append(" inner join funcionario on cursocoordenador.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa as coordenador on coordenador.codigo = funcionario.pessoa ");
		sql.append(" inner join horarioturmadia on  horarioturmadia.horarioturma =  horarioturma.codigo ");
		sql.append(" and current_date =   (horarioturmadia.data::DATE - (diasParaNotificarCoordenadorInicioTurma||' day')::interval)");
		sql.append(" inner join horarioturmadiaitem on  horarioturmadia.codigo =  horarioturmadiaitem.horarioturmadia ");
		sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
		sql.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		sql.append(" where not exists (");
		sql.append(" 	select htd.codigo from horarioturmadia htd");
		sql.append(" 	inner join horarioturmadiaitem htdi on  htd.codigo =  htdi.horarioturmadia ");
		sql.append(" 	where htd.horarioturma = horarioturma.codigo");
		sql.append(" 	and htdi.disciplina = disciplina.codigo	");
		sql.append(" 	and htd.data < horarioturmadia.data");
		sql.append(" 	limit 1");
		sql.append(" )");	
		
		List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs = new ArrayList<NotificacaoProfessorPostarMaterialVO>(0);
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			NotificacaoProfessorPostarMaterialVO obj = new NotificacaoProfessorPostarMaterialVO();
			obj.setTurma(rs.getString("identificadorturma"));    		
    		obj.setDisciplina(rs.getString("disciplina"));
    		obj.setDataAula(rs.getDate("data"));
    		obj.getProfessor().setCodigo(rs.getInt("coordenador_codigo"));
    		obj.getProfessor().setNome(rs.getString("coordenador"));
    		obj.getProfessor().setEmail(rs.getString("email"));
    		obj.setUnidadeEnsino(rs.getInt("unidadeensino_codigo"));
    		notificacaoProfessorPostarMaterialVOs.add(obj);
		}
		return notificacaoProfessorPostarMaterialVOs;
	}

}
