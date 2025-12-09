/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author PEDRO
 */
public class EtapaWorkflowAntecedenteVO extends SuperVO {

    protected Integer codigo;
    private EtapaWorkflowVO etapaWorkflow;
    private EtapaWorkflowVO etapaAntecedente;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public EtapaWorkflowVO getEtapaWorkflow() {
        if (etapaWorkflow == null) {
            etapaWorkflow = new EtapaWorkflowVO();
        }
        return etapaWorkflow;
    }

    public void setEtapaWorkflow(EtapaWorkflowVO etapaWorkflow) {
        this.etapaWorkflow = etapaWorkflow;
    }

    public EtapaWorkflowVO getEtapaAntecedente() {
        if (etapaAntecedente == null) {
            etapaAntecedente = new EtapaWorkflowVO();
        }
        return etapaAntecedente;
    }

    public void setEtapaAntecedente(EtapaWorkflowVO etapaAntecedente) {
        this.etapaAntecedente = etapaAntecedente;
    }
}
