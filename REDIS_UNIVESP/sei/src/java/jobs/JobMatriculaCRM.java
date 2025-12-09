package jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import negocio.comuns.academico.MatriculaCRMVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class JobMatriculaCRM extends SuperFacadeJDBC implements Runnable, Serializable {

    @PostConstruct
    public void iniciarJob() {
        ScheduledThreadPoolExecutor schedule = null;
        try {
            schedule = new ScheduledThreadPoolExecutor(1);
            ////System.out.println("===============================Thread Inicial==============================================");
            schedule.setThreadFactory(new ThreadInicialThreadFactory("Thread Inicial"));
            ////System.out.println("=====================================Thread Inicial========================================");
            schedule.execute(this);
        } finally {
            schedule = null;
        }

    }

    class ThreadInicialThreadFactory implements ThreadFactory {

        private String poolName;

        public ThreadInicialThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, poolName);
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    }

    public void run() {
        Date dataAtual = null;
        try {
            dataAtual = new Date();
            ////System.out.println("=============================================================================");
            ////System.out.println("INICOU A THREAD = " + Uteis.getDataAtual() + " - HORA = " + Uteis.getHoraAtual());
            int i = 0;
            //boolean executarJob = true;
            boolean executarJob = getFacadeFactory().getMatriculaCRMFacade().verificaSeDeveProcessarJob();
            while (executarJob) {
                try {
                    Thread.sleep(300000);
                    if (getFacadeFactory().getMatriculaCRMFacade().verificaSeTemRegistro(Boolean.FALSE, null)) {
                        List<MatriculaCRMVO> listaMatriculaCRM = getFacadeFactory().getMatriculaCRMFacade().consultarMatriculaCRMSituacao(Boolean.FALSE, null);
                        for (MatriculaCRMVO mat : listaMatriculaCRM) {
                            try {
                                UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
                                ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, mat.getUnidadeEnsino().getCodigo(), usuResp);
                                mat.getUnidadeEnsino().getConfiguracoes();
                                getFacadeFactory().getMatriculaCRMFacade().novaMatricula(mat, conf, usuResp);
                                removerObjetoMemoria(mat);
                                removerObjetoMemoria(conf);
                                removerObjetoMemoria(usuResp);
                            } catch (Exception e) {
                                try {
                                    mat.setErro(Boolean.TRUE);
                                    mat.setMatricula("");
                                    mat.setDiretorioBoletoMatricula("");
                                    mat.setHtmlContratoMatricula("");
                                    mat.setLocalArmazenamentoDocumentosMatricula("");
                                    mat.setMensagemErro(e.getMessage());
                                    getFacadeFactory().getMatriculaCRMFacade().alterar(mat);
                                    mat = null;
                                } catch (Exception x) {
                                    //System.out.println(" MENSAGEM ERRO => " + x.getMessage());
                                } finally {
                                    removerObjetoMemoria(mat);
                                }
//                                //System.out.println("=========== ERRO NA NOVA MATRICULA ============");
//                                //System.out.println("....NOME ALUNO => " + mat.getAluno().getNome());
//                                //System.out.println("....CODIGO ALUNO =>" + mat.getAluno().getCodigo());
//                                //System.out.println("....TURMA =>" + mat.getTurma().getIdentificadorTurma());
//                                //System.out.println("....UNIDADE ENSINO =>" + mat.getUnidadeEnsino().getNome());
//                                //System.out.println("....PROCESSO MATRICULA =>" + mat.getProcessoMatricula().getDescricao());
//                                //System.out.println("....PLANO FINANCEIRO CURSO =>" + mat.getPlanoFinanceiroCurso().getDescricao());
//                                //System.out.println("....CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO =>" + mat.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
//                                //System.out.println("....MENSAGEM =>" + e.getMessage());
//                                //System.out.println("===============================================");
                                i++;
                            } finally {
                            }
                        }
                    }
                } catch (Exception e) {
                    //System.out.println("=============================================================================");
                    //System.out.println("ERRO NA THREAD INTERNA = " + Uteis.getDataAtual() + " - HORA = " + Uteis.getHoraAtual());
                    //System.out.println("MENSAGEM = " + e.getMessage());
                    //System.out.println("=============================================================================");
                }
            }
            //dataAtual = null;
        } catch (Exception ex) {
            //System.out.println("=============================================================================");
            //System.out.println("PAROU A THREAD 2 = " + Uteis.getDataAtual() + " - HORA = " + Uteis.getHoraAtual());
            //System.out.println("MENSAGEM = " + ex.getMessage());
            //System.out.println("=============================================================================");
            Logger.getLogger(JobMatriculaCRM.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dataAtual = null;
        }
    }
}
