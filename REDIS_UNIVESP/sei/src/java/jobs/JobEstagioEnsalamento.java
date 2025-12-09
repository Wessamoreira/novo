package jobs;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import kong.unirest.UnirestException;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;

@Component
public class JobEstagioEnsalamento extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5319014612082970L;
	
//    A: Segundos (0 - 59).
//    B: Minutos (0 - 59).
//    C: Horas (0 - 23).
//    D: Dia (1 - 31).
//    E: Mês (1 - 12).
//    F: Dia da semana (0 - 6).

	@Scheduled(cron = "00 00 01 * * *")
	public void run() {
		try {
			System.out.println("JobEstagioEnsalamento inicio - " + Uteis.getDataComHora(new Date()));
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(configSeiBlackboardVO.isAtivarOperacaoEnsalamentoEstagio()) {
				UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioOperacaoExterna);
				SqlRowSet tabelaResultado = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio("", Uteis.getAnoDataAtual(), Uteis.getSemestreAtual(), usuarioOperacaoExterna);
				while (tabelaResultado.next()) {
					executarEnsalamentoSalaAulaEstagio(usuarioOperacaoExterna, configEstagio, tabelaResultado);
				}
			}
			System.out.println("JobEstagioEnsalamento termino - " + Uteis.getDataComHora(new Date()));
		} catch (Exception e) {
			System.out.println("JobEstagioEnsalamento erro - " + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}
	
	

	private void executarEnsalamentoSalaAulaEstagio(UsuarioVO usuarioOperacaoExterna, ConfiguracaoEstagioObrigatorioVO configEstagio, SqlRowSet tabelaResultado) throws Exception {
		try {
			MatriculaVO matricula = new MatriculaVO();
			matricula.setMatricula(tabelaResultado.getString("matricula"));
			matricula.getAluno().setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			matricula.getAluno().setNome(tabelaResultado.getString("nome_pessoa"));
			matricula.getCurso().setCodigo(tabelaResultado.getInt("codigo_curso"));
			matricula.getCurso().setNome(tabelaResultado.getString("nome_curso"));
			matricula.getCurso().setAbreviatura(tabelaResultado.getString("abreviatura_curso"));
			matricula.getCurso().setPeriodicidade(tabelaResultado.getString("periodicidade"));
			
			MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
			matriculaPeriodoVO.setCodigo(tabelaResultado.getInt("codigo_matriculaperiodo"));
			matriculaPeriodoVO.setAno(tabelaResultado.getString("ano"));
			matriculaPeriodoVO.setSemestre(tabelaResultado.getString("semestre"));
			
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSalaAulaBlackboardPorModuloEstagio(matricula, matriculaPeriodoVO, configEstagio, usuarioOperacaoExterna);
			//getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioLiberado(matricula, matriculaPeriodoVO, usuarioOperacaoExterna);
		} catch (Exception e) {
			try {
				SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
				sab.getCursoVO().setCodigo(tabelaResultado.getInt("codigo_curso"));
				PessoaEmailInstitucionalVO pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(tabelaResultado.getInt("codigo_pessoa"), Uteis.NIVELMONTARDADOS_TODOS, usuarioOperacaoExterna);
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirLogErro(sab, TipoSalaAulaBlackboardPessoaEnum.ALUNO, pei.getPessoaVO().getCodigo(), pei.getEmail(), tabelaResultado.getString("matricula"), null, tabelaResultado.getString("ano"), tabelaResultado.getString("semestre"),  null, SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, SalaAulaBlackboardOperacao.OPERACAO_INCLUIR, "JobEstagioEnsalamento msg:" + e.getMessage(), getUsuarioLogado());
				if(e instanceof UnirestException) {
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	

}
