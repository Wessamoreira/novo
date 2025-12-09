package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class QuadroComissaoConsultoresRelVO {

    private TurmaVO turmaVO;
    private Boolean tipoConsultor;
    private String nomeConsultor;
    private Integer totalMatriculados;
    private Integer totalNaoAtivos;
    private Integer totalBolsista;
    private Integer totalPagantes;
    private Boolean possuiConsultor;
    private List<FuncionarioVO> listaConsultor;

    public QuadroComissaoConsultoresRelVO() {
    }

    public JRDataSource getListaConsultorVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaConsultor().toArray());
        return jr;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the tipoConsultor
     */
    public Boolean getTipoConsultor() {
        if (tipoConsultor == null) {
            tipoConsultor = Boolean.FALSE;
        }
        return tipoConsultor;
    }

    /**
     * @param tipoConsultor the tipoConsultor to set
     */
    public void setTipoConsultor(Boolean tipoConsultor) {
        this.tipoConsultor = tipoConsultor;
    }

    /**
     * @return the nomeConsultor
     */
    public String getNomeConsultor() {
        if (nomeConsultor == null) {
            nomeConsultor = "";
        }
        return nomeConsultor;
    }

    /**
     * @param nomeConsultor the nomeConsultor to set
     */
    public void setNomeConsultor(String nomeConsultor) {
        this.nomeConsultor = nomeConsultor;
    }

    /**
     * @return the totalMatriculados
     */
    public Integer getTotalMatriculados() {
        if (totalMatriculados == null) {
            totalMatriculados = 0;
        }
        return totalMatriculados;
    }

    /**
     * @param totalMatriculados the totalMatriculados to set
     */
    public void setTotalMatriculados(Integer totalMatriculados) {
        this.totalMatriculados = totalMatriculados;
    }

    /**
     * @return the totalNaoAtivos
     */
    public Integer getTotalNaoAtivos() {
        if (totalNaoAtivos == null) {
            totalNaoAtivos = 0;
        }
        return totalNaoAtivos;
    }

    /**
     * @param totalNaoAtivos the totalNaoAtivos to set
     */
    public void setTotalNaoAtivos(Integer totalNaoAtivos) {
        this.totalNaoAtivos = totalNaoAtivos;
    }

    /**
     * @return the totalBolsista
     */
    public Integer getTotalBolsista() {
        if (totalBolsista == null) {
            totalBolsista = 0;
        }
        return totalBolsista;
    }

    /**
     * @param totalBolsista the totalBolsista to set
     */
    public void setTotalBolsista(Integer totalBolsista) {
        this.totalBolsista = totalBolsista;
    }

    /**
     * @return the totalPagantes
     */
    public Integer getTotalPagantes() {
        if (totalPagantes == null) {
            totalPagantes = 0;
        }
        return totalPagantes;
    }

    /**
     * @param totalPagantes the totalPagantes to set
     */
    public void setTotalPagantes(Integer totalPagantes) {
        this.totalPagantes = totalPagantes;
    }

    /**
     * @return the possuiConsultor
     */
    public Boolean getPossuiConsultor() {
        if (possuiConsultor == null) {
            possuiConsultor = Boolean.FALSE;
        }
        return possuiConsultor;
    }

    /**
     * @param possuiConsultor the possuiConsultor to set
     */
    public void setPossuiConsultor(Boolean possuiConsultor) {
        this.possuiConsultor = possuiConsultor;
    }

    /**
     * @return the listaConsultores
     */
    public List<FuncionarioVO> getListaConsultor() {
        if (listaConsultor == null) {
            listaConsultor = new ArrayList(0);
        }
        return listaConsultor;
    }

    /**
     * @param listaConsultores the listaConsultores to set
     */
    public void setListaConsultor(List<FuncionarioVO> listaConsultor) {
        this.listaConsultor = listaConsultor;
    }

}
