package jobs;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import kong.unirest.UnirestException;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;

@Component
public class JobTccDesensalamento extends SuperControle {
	
private static final long serialVersionUID = -4753454510172703105L;

//  A: Segundos (0 - 59).
//  B: Minutos (0 - 59).
//  C: Horas (0 - 23).
//  D: Dia (1 - 31).
//  E: Mês (1 - 12).
//  F: Dia da semana (0 - 6).
	
	@Scheduled(cron = "00 30 02 * * *")
	public void run() {
		try {
			System.out.println("JobTccDesensalamento inicio - " + Uteis.getDataComHora(new Date()));
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(configSeiBlackboardVO.isAtivarOperacaoEnsalamentoTCC()) {
				UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				SqlRowSet tabelaResultado = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaDeVerificacaoSeMatriculaAptaDesensalarTccAmbientacao();
				while (tabelaResultado.next()) {
					executarDesensalamentoSalaAulaTcc(usuarioOperacaoExterna, tabelaResultado);
				}
			}
			System.out.println("JobTccDesensalamento termino - " + Uteis.getDataComHora(new Date()));
		} catch (Exception e) {
				System.out.println("JobTccDesensalamento erro - " + Uteis.getDataComHora(new Date()));
				e.printStackTrace();
		}
	}
	
	private void executarDesensalamentoSalaAulaTcc(UsuarioVO usuarioOperacaoExterna, SqlRowSet tabelaResultado) throws Exception {
		try {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(tabelaResultado.getInt("salaaulablackboard_codigo"), tabelaResultado.getInt("codigo_pessoa"), TipoSalaAulaBlackboardPessoaEnum.ALUNO,"",tabelaResultado.getString("pessoaemailinstitucional_email"), usuarioOperacaoExterna);
		} catch (Exception e) {
			SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
			sab.setCodigo(tabelaResultado.getInt("salaaulablackboard_codigo"));
			sab.getCursoVO().setCodigo(tabelaResultado.getInt("codigo_curso"));
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirLogErro(sab, TipoSalaAulaBlackboardPessoaEnum.ALUNO, tabelaResultado.getInt("codigo_pessoa"), tabelaResultado.getString("pessoaemailinstitucional_email"), tabelaResultado.getString("matricula"), null, "", "",  null, SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, SalaAulaBlackboardOperacao.OPERACAO_DELETAR, "JobTccDesensalamento msg:" + e.getMessage(), getUsuarioLogado());
			if(e instanceof UnirestException) {
				return;
			}
		}
	}

}
