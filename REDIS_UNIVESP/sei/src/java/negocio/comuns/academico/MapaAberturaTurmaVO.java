package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Carlos
 */
public class MapaAberturaTurmaVO extends SuperVO {

    private List<TurmaAberturaVO> turmaAberturaVOs;
    private List<TurmaAberturaVO> turmaAberturaPendenciasVOs;
    private String graficoPizza;
    public static final long serialVersionUID = 1L;

    public List<TurmaAberturaVO> getTurmaAberturaVOs() {
        if (turmaAberturaVOs == null) {
            turmaAberturaVOs = new ArrayList(0);
        }
        return turmaAberturaVOs;
    }

    public void setTurmaAberturaVOs(List<TurmaAberturaVO> turmaAberturaVOs) {
        this.turmaAberturaVOs = turmaAberturaVOs;
    }

    public List<TurmaAberturaVO> getTurmaAberturaPendenciasVOs() {
        if (turmaAberturaPendenciasVOs == null) {
            turmaAberturaPendenciasVOs = new ArrayList(0);
        }
        return turmaAberturaPendenciasVOs;
    }

    public void setTurmaAberturaPendenciasVOs(List<TurmaAberturaVO> turmaAberturaPendenciasVOs) {
        this.turmaAberturaPendenciasVOs = turmaAberturaPendenciasVOs;
    }

    public String getGraficoPizza() {
        if(graficoPizza == null){
            graficoPizza = "";
        }
        return graficoPizza;
    }

    public void setGraficoPizza(String graficoPizza) {
        this.graficoPizza = graficoPizza;
    }

}
