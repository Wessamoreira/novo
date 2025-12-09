package jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaAberturaInterfaceFacade;


@Service
@Lazy
public class JobAlterarSituacaoTurmaAberturaInaugurada extends ControleAcesso implements Runnable {

    private static final long serialVersionUID = 1L;
	private TurmaAberturaInterfaceFacade turmaAberturaFacade;
    private List<Integer> listaCodigoTurmaAberturasConfirmadas;

    @PostConstruct
    public void inicializarJobAlterarSituacaoTurmaAberturaInaugurada() {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 2);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            ScheduledExecutorService agendador = Executors.newScheduledThreadPool(1);
            agendador.scheduleAtFixedRate(this, (calendar.getTimeInMillis() - new Date().getTime()), 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
             //System.out.println("JobAlterarSituacao Erro:" + e.getMessage());
        } finally {
            calendar = null;
        }
    }

    @Override
    public void run() {
        try {
            setListaCodigoTurmaAberturasConfirmadas(getTurmaAberturaFacade().consultarPorSituacaoDataJob("CO", new Date()));
            if (!getListaCodigoTurmaAberturasConfirmadas().isEmpty()) {
                alterarSituacaoTurmaAbertura();
            }
        } catch (Exception e) {
             //System.out.println("JobAlterarSituacao Erro:" + e.getMessage());
        } finally {
            setListaCodigoTurmaAberturasConfirmadas(null);
        }
    }

    public void alterarSituacaoTurmaAbertura() {
        try {
            for (Integer codigo : getListaCodigoTurmaAberturasConfirmadas()) {
                getTurmaAberturaFacade().alterarSituacao(codigo, "IN");
            }
        } catch (Exception e) {
            //System.out.println("Erro jobAlterarSituacaoTurmaAberturaInaugurada: " + e.getMessage());
        }
    }

    public List<Integer> getListaCodigoTurmaAberturasConfirmadas() {
        if (listaCodigoTurmaAberturasConfirmadas == null) {
            listaCodigoTurmaAberturasConfirmadas = new ArrayList<Integer>(0);
        }
        return listaCodigoTurmaAberturasConfirmadas;
    }

    public void setListaCodigoTurmaAberturasConfirmadas(List<Integer> listaCodigoTurmaAberturasConfirmadas) {
        this.listaCodigoTurmaAberturasConfirmadas = listaCodigoTurmaAberturasConfirmadas;
    }

    public TurmaAberturaInterfaceFacade getTurmaAberturaFacade() {
        return turmaAberturaFacade;
    }

    public void setTurmaAberturaFacade(TurmaAberturaInterfaceFacade turmaAberturaFacade) {
        this.turmaAberturaFacade = turmaAberturaFacade;
    }
}
