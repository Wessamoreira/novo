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
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;

@Component
public class JobTccEnsalamento extends SuperControle {

	/**
		 * 
		 */
	private static final long serialVersionUID = -7744361667422363589L;

//  A: Segundos (0 - 59).
//  B: Minutos (0 - 59).
//  C: Horas (0 - 23).
//  D: Dia (1 - 31).
//  E: Mês (1 - 12).
//  F: Dia da semana (0 - 6).

	@Scheduled(cron = "00 00 02 * * *")
	public void run() {
		try {
			System.out.println("JobTccEnsalamento TCC inicio - " + Uteis.getDataComHora(new Date()));
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(configSeiBlackboardVO.isAtivarOperacaoEnsalamentoTCC()) {
				UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				SqlRowSet tabelaResultado = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardTccAmbientacao("", Uteis.getAnoDataAtual(), Uteis.getSemestreAtual(), usuarioOperacaoExterna);
				while (tabelaResultado.next()) {
					executarEnsalamentoSalaAulaTcc(usuarioOperacaoExterna, tabelaResultado);
				}
			}
			System.out.println("JobTccEnsalamento TCC termino - " + Uteis.getDataComHora(new Date()));
		} catch (Exception e) {
			System.out.println("JobTccEnsalamento TCC erro - " + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}

	private void executarEnsalamentoSalaAulaTcc(UsuarioVO usuarioOperacaoExterna, SqlRowSet tabelaResultado) throws Exception {
		try {
			MatriculaVO matricula = new MatriculaVO();
			matricula.setMatricula(tabelaResultado.getString("matricula"));
			matricula.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradecurricularatual"));
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
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSalaAulaBlackboardPorModuloTccAmbientacao(matricula, matriculaPeriodoVO, usuarioOperacaoExterna);
			//getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTccLiberado(matricula, matriculaPeriodoVO, usuarioOperacaoExterna);
		} catch (Exception e) {
			try {
				SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
				sab.getCursoVO().setCodigo(tabelaResultado.getInt("codigo_curso"));
				sab.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorGradeCurricularDisciplinaPadraoTcc(tabelaResultado.getInt("gradecurricularatual"), true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioOperacaoExterna));
				PessoaEmailInstitucionalVO pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(tabelaResultado.getInt("codigo_pessoa"), Uteis.NIVELMONTARDADOS_TODOS, usuarioOperacaoExterna);
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirLogErro(sab, TipoSalaAulaBlackboardPessoaEnum.ALUNO, pei.getPessoaVO().getCodigo(), pei.getEmail(), tabelaResultado.getString("matricula"), null, tabelaResultado.getString("ano"), tabelaResultado.getString("semestre"),  null, SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, SalaAulaBlackboardOperacao.OPERACAO_INCLUIR, "JobTccEnsalamento msg:" + e.getMessage(), getUsuarioLogado());
				if(e instanceof UnirestException) {
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
