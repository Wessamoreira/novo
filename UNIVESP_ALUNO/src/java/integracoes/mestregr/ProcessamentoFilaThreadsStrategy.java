package integracoes.mestregr;

import negocio.comuns.utilitarias.ProgressBarVO;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * @author Rodrigo Ribeiro
 * Classe ProcessamentoFilaStrategy.
 * Esta classe implementa o padrão Strategy para gerenciar o processamento de uma fila de tarefas
 * de integração, utilizando um pool fixo de threads.
 * @ Gerenciar uma fila encadeada de tarefas (objetos do tipo {@code Runnable}), garantindo que
 * as tarefas sejam executadas na ordem de entrada.
 * @ Iniciar, quando necessário, um consumidor que retira as tarefas da fila e as executa.
 * @ Controlar o número de threads ativas por meio de um pool fixo que realiza a verificacao de threads disponiveis no servidor,
 * na variavel THREADS_UTILIZADAS_ATUALMENTE.
 * @ Implementar um tempo máximo de espera (timeout) para a obtenção de uma tarefa da fila (Integracao API), permitindo o
 * encerramento seguro do processamento quando a fila estiver ociosa.
 * @ Pausar por 2 segundos entre a execução de cada tarefa, para dar tempo a outros processos e evitar
 * sobrecarga.
 */
@Service
public class ProcessamentoFilaThreadsStrategy {
    private ExecutorService executorConsumidor;
    private ExecutorService executorTarefas;
    private final BlockingQueue<Runnable> filaDeTrabalho = new LinkedBlockingQueue<>();
    // Utiliza volatile para garantir a visibilidade entre threads
    private volatile boolean ativo = false;
    private final Object lock = new Object();
    private static final Integer THREADS_UTILIZADAS_ATUALMENTE = Runtime.getRuntime().availableProcessors();
    private static final Integer MINIMO_THREADS = 2;
    // Tempo de espera para o poll (por quanto tempo ele espera ate adicionar o proximo item da fila), em segundos
    private static final long TEMPO_AGUARDANDO_NOVO_ITEM = 1L;
    // Tempo de espera para atualização da progressão (em segundos)
    private static final long TEMPO_PROGRESSAO_TRANSMISSAO = 2L;
    private static final Integer DELAY_ENTRE_REQUISICOES = 2000;

    /**
     * Inicia o consumidor que processa a fila, se ainda não estiver ativo.
     */
    public void iniciarConsumidor() {
        synchronized (lock) {
            if (!ativo) {
                ativo = true;
                int numThreads = Math.max(MINIMO_THREADS, THREADS_UTILIZADAS_ATUALMENTE);

                executorConsumidor = Executors.newSingleThreadExecutor();
                executorTarefas = Executors.newFixedThreadPool(numThreads);
                executorConsumidor.submit(this::processarFila);
            }
        }
    }

    /**
     * Adiciona uma tarefa à fila de integração.
     * @param integrationTask tarefa a ser processada
     */
    public void adicionarNaFila(Runnable integrationTask) {
        iniciarConsumidor();
        filaDeTrabalho.add(integrationTask);
    }

    /**
     * Encerra completamente o processamento: consumidor, tarefas e fila.
     */
    public void encerrar() {
        synchronized (lock) {
            if (!ativo) return;
            ativo = false;
            filaDeTrabalho.clear();

            if (executorConsumidor != null && !executorConsumidor.isShutdown()) {
                executorConsumidor.shutdown();
            }
            if (executorTarefas != null && !executorTarefas.isShutdown()) {
                executorTarefas.shutdown();
            }
        }
    }

    /**
     * Loop do consumidor que retira tarefas da fila e as delega ao pool de execução.
     */
    private void processarFila() {
        try {
            while (ativo && !Thread.currentThread().isInterrupted()) {
                Runnable task = filaDeTrabalho.poll(TEMPO_AGUARDANDO_NOVO_ITEM, TimeUnit.SECONDS);
                if (task == null) {
                    encerrar();
                    break;
                }
                executorTarefas.submit(task);
                Thread.sleep(DELAY_ENTRE_REQUISICOES);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Aguarda a conclusão de todas as tarefas e atualiza o progresso.
     * @param progressBarVO barra de progresso
     * @param mensagemNaProgressBar mensagem a ser exibida
     */
    public void aguardarProcessamentoFila(ProgressBarVO progressBarVO, String mensagemNaProgressBar) {
        if (executorTarefas != null) {
            executorTarefas.shutdown();
            try {
                while (!executorTarefas.awaitTermination(TEMPO_PROGRESSAO_TRANSMISSAO, TimeUnit.SECONDS)) {
                    progressBarVO.setStatus(mensagemNaProgressBar);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                iniciarConsumidor();
            }
        }
    }

    /**
     * mantém o mecanismo de fila atual e, ao mesmo tempo, consegue retornar um
     * Future com o resultado da tarefa. Essa abordagem é bastante utilizada e
     * considerada uma boa prática em ambientes corporativos quando se deseja
     * mesclar execução assíncrona com retorno de valores.
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submitTask(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        adicionarNaFila(futureTask);
        return futureTask;
    }

    /**
     * Encerra a fila de forma segura sem forçar interrupções.
     */
    public void encerrarFila() {
        synchronized (lock) {
            ativo = false;
            if (executorConsumidor != null) executorConsumidor.shutdown();
            if (executorTarefas != null) executorTarefas.shutdown();
        }
    }

    /**
     * Encerra a fila com atualização da barra de progresso.
     * @param progressBarVO barra de progresso
     */
    public void encerrarFilaAndProgressBar(ProgressBarVO progressBarVO) {
        synchronized (lock) {
            ativo = false;
            if (executorConsumidor != null) executorConsumidor.shutdown();
            if (executorTarefas != null) {
                executorTarefas.shutdown();
                try {
                    while (!executorTarefas.awaitTermination(TEMPO_PROGRESSAO_TRANSMISSAO, TimeUnit.SECONDS)) {
                        progressBarVO.setStatus("Encerrando processamento...");
                        progressBarVO.encerrarForcado();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    progressBarVO.setStatus("Erro ao encerrar processamento: " + e.getMessage());
                } finally {
                    progressBarVO.setStatus("Processamento finalizado");
                }
            }
        }
    }

    /**
     * Executa uma tarefa com acesso ao contexto necessário (ex: chamadas de métodos não-anônimas).
     * Útil quando se quer evitar lambdas anônimos com escopo perdido.
     */
    public void executarComContexto(Runnable runnableComContexto) {
        adicionarNaFila(runnableComContexto);
    }

}
