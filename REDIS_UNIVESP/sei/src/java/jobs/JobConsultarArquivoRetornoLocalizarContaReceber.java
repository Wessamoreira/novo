package jobs;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import controle.financeiro.ControleCobrancaControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobConsultarArquivoRetornoLocalizarContaReceber extends SuperControle  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1145789418718507416L;
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 3;
	private static final long EXECUTAR_HORA = HORA * 24;
	
	@Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO) 
    public void executarJobConsultarArquivoRetornoLocalizarContaReceber() {
        try {
        	System.out.println("JobConsultarArquivoRetornoLocalizarContaReceber iniciou - "+Uteis.getDataComHoraCompleta(new Date()));
        	if(!Uteis.isVersaoDev()) {
        		List<RegistroExecucaoJobVO> lista = getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobInterrompidasPorNomeOrigem(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO);
        		for (RegistroExecucaoJobVO rej : lista) {
        			if (!getAplicacaoControle().getMapThreadControleCobranca().containsKey(rej.getCodigoOrigem())) {
        				ControleCobrancaVO cc = getFacadeFactory().getControleCobrancaFacade().consultarBasicaPorCodigo(rej.getCodigoOrigem(), getUsuarioLogado());
        				if(Uteis.isAtributoPreenchido(cc)) {
        					cc.setEdicaoManual(true);
        					cc.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(cc.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
        					cc.setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(cc.getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, cc.getResponsavel()));
                			ControleCobrancaControle controle = new ControleCobrancaControle();
                			controle.setControleCobrancaVO(cc);
                			controle.setProgressBarVO(new ProgressBarVO());
                			controle.getProgressBarVO().setUsuarioVO(cc.getResponsavel());
                			controle.getProgressBarVO().resetar();
                			controle.getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
                			controle.getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWebForaContext() + File.separator + "arquivos"+  File.separator +cc.getNomeArquivo());
                			controle.getProgressBarVO().iniciar(0l, (cc.getRegistroArquivoVO().getRegistroDetalheVOs().size()+1), "Iniciando Processamento da(s) operações.", true, controle, "processamentoArquivoProgressBar");
                			controle.getProgressBarVO().iniciarAssincrono();
                			getAplicacaoControle().adicionarMapThreadControleCobranca(cc.getCodigo(), controle.getProgressBarVO());        					
        				}	
        			}
				}		
        	}
        	System.out.println("JobConsultarArquivoRetornoLocalizarContaReceber terminou - "+Uteis.getDataComHoraCompleta(new Date()));
        } catch (Exception e) {
        	System.out.println("JobConsultarArquivoRetornoLocalizarContaReceber erro - "+Uteis.getDataComHoraCompleta(new Date()));
            e.printStackTrace();
        }
    }

}
