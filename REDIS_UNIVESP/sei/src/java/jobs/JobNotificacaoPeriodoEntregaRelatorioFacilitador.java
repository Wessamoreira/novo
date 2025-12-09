package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobNotificacaoPeriodoEntregaRelatorioFacilitador extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2289762925462150291L;

	@Scheduled(cron = "0 0 01 * * *", zone = "America/Sao_Paulo")
	public void run() {
		try {
			List<CalendarioRelatorioFinalFacilitadorVO> calendarioRelatorioFinalFacilitadorVOs = new ArrayList<CalendarioRelatorioFinalFacilitadorVO>();
			String ano = Uteis.getAnoDataAtual();
			String semestre = Uteis.getSemestreAtual();
			String mes = Uteis.getMesAtualDoisDigitos(new Date());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PERIODO_ENTREGA_RELATORIO_FACILITADOR, false, 0, null);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				calendarioRelatorioFinalFacilitadorVOs = getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultarPorAnoSemestreMesData(ano, semestre, mes, new Date(), null, false, getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(calendarioRelatorioFinalFacilitadorVOs)) {
					for(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO : calendarioRelatorioFinalFacilitadorVOs) {
						List<RelatorioFinalFacilitadorVO> relatorioFinalFacilitadorVOs = new ArrayList<RelatorioFinalFacilitadorVO>(); 
						RelatorioFinalFacilitadorVO relatorioFinalFacilitador = new RelatorioFinalFacilitadorVO();
						ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
						relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().setDisciplina(calendarioRelatorioFinalFacilitadorVO.getDisciplinaVO());
						relatorioFinalFacilitadorVOs.addAll(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarDashboradRelatorioFacilitador(relatorioFinalFacilitador, null, null, new Date(), calendarioRelatorioFinalFacilitadorVO, null, getUsuarioLogado()));
						comunicacaoEnviar.setTipoOrigemComunicacaoInternaEnum(null);
						comunicacaoEnviar.setCodigo(0);
						comunicacaoEnviar.getComunicadoInternoDestinatarioVOs().clear();
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setPersonalizacaoMensagemAutomaticaVO(mensagemTemplate);
						comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
						comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
						comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
						comunicacaoEnviar.setData(new Date());
						comunicacaoEnviar.setTipoDestinatario("AL");
						if(Uteis.isAtributoPreenchido(relatorioFinalFacilitadorVOs)) {
							for(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : relatorioFinalFacilitadorVOs) {
								String mensagemEditada = obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, calendarioRelatorioFinalFacilitadorVO, mensagemTemplate.getMensagem());
								String mensagemSMSEditada = obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, calendarioRelatorioFinalFacilitadorVO, mensagemTemplate.getMensagemSMS());
								comunicacaoEnviar.setMensagem(mensagemEditada);
								if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
									comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
									comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
								}
								comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno()));
								getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, getUsuarioLogado(), config,null);
								}
							}
						}
					}
				}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String obterMensagemFormatadaMensagemRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.REGISTRO_ACADEMICO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getRegistroAcademico());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SUPERVISOR.name(), relatorioFinalFacilitadorVO.getSupervisor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO.name(), Uteis.getData(calendarioRelatorioFinalFacilitadorVO.getDataInicio()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_FIM.name(), Uteis.getData(calendarioRelatorioFinalFacilitadorVO.getDataFim()));
		return mensagemTexto;
	}
}