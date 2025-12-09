/**
 * 
 */
package negocio.comuns.utilitarias;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Rodrigo Wind
 *
 */
public class ProcessarParalelismo {
	/**
	 * @author Rodrigo Wind - 12/08/2015
	 */
	public interface Processo {
		void run(int i);
	}


	public static void executar(int start, int stop, final ConsistirException consistirException, final Processo body) {
		executar(start, stop, 100,  null, consistirException, body);
	}
	
	public static void executar(int start, int stop, int percentualNucleoUtilizar, final ConsistirException consistirException, final Processo body) {
		executar(start, stop, 100, null, consistirException, body);
	}
	
	public static void executar(int start, int stop, int percentualNucleoUtilizar, Long timeOut, final ConsistirException consistirException, final Processo body) {
		if (stop > 0 && start <= stop) {
			int CPUs = Uteis.isAtributoPreenchido(percentualNucleoUtilizar) && Objects.equals(percentualNucleoUtilizar, 100) ? Runtime.getRuntime().availableProcessors() : calcularPorcentualNucleoUtilizar(percentualNucleoUtilizar);
			if(CPUs == 0) {
				CPUs = 1;
			}
			int chunksize = (stop - start + CPUs - 1) / CPUs;
			int loops = (stop - start + chunksize - 1) / chunksize;
			ExecutorService executor = Executors.newFixedThreadPool(CPUs);
			final CountDownLatch latch = new CountDownLatch(loops);
			for (int i = start; i < stop;) {
				final int lo = i;
				i += chunksize;
				final int hi = (i < stop) ? i : stop;
				executor.submit(new Runnable() {
					public void run() {
						for (int i = lo; i < hi; i++)
							body.run(i);
						latch.countDown();
					}
				});
			}
			executor.shutdown();
			try {
				if(timeOut == null || timeOut == 0l || timeOut.equals(0l)) {
					latch.await();
				}else {
					latch.await(timeOut, TimeUnit.MINUTES);					
				}
			} catch (InterruptedException e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
			} catch (Exception e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
			}
		}
	}
	public static void executarProgressBarVO(int start, int stop, int percentualNucleoUtilizar, ProgressBarVO progressBarVO, String status, final ConsistirException consistirException, final Processo body) {
		if (stop > 0 && start <= stop) {
			int CPUs = Uteis.isAtributoPreenchido(percentualNucleoUtilizar) && Objects.equals(percentualNucleoUtilizar, 100) ? Runtime.getRuntime().availableProcessors() : calcularPorcentualNucleoUtilizar(percentualNucleoUtilizar);
			if(CPUs == 0) {
				CPUs = 1;
			}
			int chunksize = (stop - start + CPUs - 1) / CPUs;
			int loops = (stop - start + chunksize - 1) / chunksize;
			ExecutorService executor = Executors.newFixedThreadPool(CPUs);
			final CountDownLatch latch = new CountDownLatch(loops);
			for (int i = start; i < stop;) {
				final int lo = i;
				i += chunksize;
				final int hi = (i < stop) ? i : stop;
				executor.submit(new Runnable() {
					public void run() {
						for (int i = lo; i < hi; i++) {
							try {
								if(progressBarVO != null && progressBarVO.getForcarEncerramento()) {
									break;
								}								
								body.run(i);
								if(progressBarVO != null) {
									progressBarVO.setStatus(status.concat(" - ").concat(progressBarVO.getProgresso().toString()).concat(" /").concat(progressBarVO.getMaxValue().toString()).concat(" ("+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(progressBarVO.getPorcentagem(), 0)+"%)"));	
								}
							} finally {
								if(progressBarVO != null) {
									progressBarVO.incrementarSemStatus();
								}
							}
						}
						latch.countDown();
					}
				});
			}
			try {				
				latch.await();
				if(progressBarVO != null) {
					progressBarVO.setProgresso(progressBarVO.getMaxValue().longValue());
				}
			} catch (InterruptedException e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
			} catch (Exception e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
			}finally {
				if(progressBarVO != null) {
					progressBarVO.setProgresso(progressBarVO.getMaxValue().longValue());
					progressBarVO.setForcarEncerramento(true);
				}
			}
			executor.shutdown();
		}
	}
	
	public static int calcularPorcentualNucleoUtilizar(int percentual) {
		int nucleosParaUsar = Uteis.isAtributoPreenchido(percentual) ? ((int) Math.floor(Runtime.getRuntime().availableProcessors() * (percentual / 100.0))) : Runtime.getRuntime().availableProcessors();
		return nucleosParaUsar;
	}
}
