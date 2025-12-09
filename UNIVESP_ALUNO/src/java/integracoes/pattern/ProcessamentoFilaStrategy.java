package integracoes.pattern;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rodrigo Ribeiro
 * Classe ProcessamentoFilaStrategy.
 * Esta classe implementa o processamento de uma fila comum de tarefas (objetos do tipo Runnable),
 * executando-as na ordem de entrada (FIFO).
 */
public class ProcessamentoFilaStrategy implements AutoCloseable {

    private final BlockingQueue<Runnable> filaIntegracoes;
    private final AtomicBoolean ativo = new AtomicBoolean(true);
    private final Thread threadProcessadora;
    private final long delayEntreTarefasMillis;
    private final int retryMaximo = 3;

    public ProcessamentoFilaStrategy() {
        this(2000L, Integer.MAX_VALUE);
    }

    public ProcessamentoFilaStrategy(long delayEntreTarefasMillis, int capacidadeFila) {
        this.filaIntegracoes = new LinkedBlockingQueue<>(capacidadeFila);
        this.delayEntreTarefasMillis = delayEntreTarefasMillis;

        this.threadProcessadora = new Thread(this::processarFila);
        this.threadProcessadora.setName("FilaProcessadora-" + threadProcessadora.getId());
        this.threadProcessadora.start();
    }

    /**
     * TEMPLATE METHOD:
     * Não deve ser sobrescrito ? define o fluxo fixo de execução da fila.
     */
    private void processarFila() {
        while (ativo.get() && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = filaIntegracoes.take(); // FIFO garantido (take é bloqueante)

                try {
                    beforeTaskExecution(task);

                    // Retry simples
                    int tentativas = 0;
                    boolean sucesso = false;
                    while (tentativas < retryMaximo && !sucesso) {
                        try {
                            task.run();
                            sucesso = true;
                        } catch (Exception e) {
                            tentativas++;
                            onTaskFailure(task, e);
                        }
                    }

                    afterTaskExecution(task);

                    Thread.sleep(delayEntreTarefasMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    onTaskFailure(task, e);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                // erro inesperado ? log se necessário
            }
        }
    }

    /**
     * Adiciona uma tarefa na fila.
     */
    public void adicionarNaFila(final Runnable integrationTask) {
        if (!ativo.get() || integrationTask == null) return;

        try {
            filaIntegracoes.put(() -> {
                try {
                    integrationTask.run();
                } catch (Exception e) {
                    onTaskFailure(integrationTask, e);
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Submete uma tarefa com retorno futuro.
     *
     * @param task            tarefa que retorna valor
     * @param timeoutSegundos timeout
     * @param withRetry       se quer retry
     * @param <T>             tipo retorno
     * @return Future
     */
    public <T> Future<T> submitTask(final Callable<T> task, final long timeoutSegundos, final boolean withRetry) {
        if (!ativo.get() || task == null) return null;

        FutureTask<T> futureTask = new FutureTask<>(() -> {
            int tentativas = 0;
            while (tentativas < retryMaximo) {
                try {
                    T result = task.call();
                    return result;
                } catch (Exception e) {
                    tentativas++;
                    if (!withRetry) break;
                    onTaskFailure(() -> {
                    }, e);
                }
            }
            return null;
        });

        adicionarNaFila(futureTask);
        return futureTask;
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
     * Encerra a fila com shutdown seguro.
     */
    public void encerrarFila() {
        ativo.set(false);
        threadProcessadora.interrupt();
        filaIntegracoes.clear();
    }

    /**
     * Verifica se a fila ainda está ativa.
     */
    public boolean isAtivo() {
        return ativo.get();
    }

    /**
     * Tamanho atual da fila.
     */
    public int getTamanhoFila() {
        return filaIntegracoes.size();
    }

    @Override
    public void close() {
        encerrarFila();
    }


    // ===== HOOKS para Template Method =====

    /**
     * HOOK: executado antes da tarefa.
     * Pode ser sobrescrito por subclasses.
     */
    public void beforeTaskExecution(Runnable task) {
        // default: não faz nada
    }

    /**
     * HOOK: executado após a tarefa.
     * Pode ser sobrescrito por subclasses.
     */
    public void afterTaskExecution(Runnable task) {
        // default: não faz nada
    }

    /**
     * HOOK: executado se a tarefa falhar.
     * Pode ser sobrescrito por subclasses.
     */
    public void onTaskFailure(Runnable task, Exception e) {
        // default: não faz nada
    }
}