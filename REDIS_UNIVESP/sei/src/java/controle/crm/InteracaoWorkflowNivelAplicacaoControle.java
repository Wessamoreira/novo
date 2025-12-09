/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.crm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.crm.InteracaoWorkflowVO;
/**
 *
 * @author Philippe
 */
@Controller("InteracaoWorkflowNivelAplicacaoControle")
@Scope("application")
@Lazy
public class InteracaoWorkflowNivelAplicacaoControle {

//    private InteracaoWorkflowVO interacaoWorkflowNivelAplicacaoVONovoProspect;
//    private InteracaoWorkflowVO interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda;
    private Map<String, InteracaoWorkflowVO> mapaInteracaoNovoProspectVOs;

//    public InteracaoWorkflowVO getInteracaoWorkflowNivelAplicacaoVONovoProspect() {
//        if (interacaoWorkflowNivelAplicacaoVONovoProspect == null) {
//            interacaoWorkflowNivelAplicacaoVONovoProspect = new InteracaoWorkflowVO();
//        }
//        return interacaoWorkflowNivelAplicacaoVONovoProspect;
//    }
//
//    public void setInteracaoWorkflowNivelAplicacaoVONovoProspect(InteracaoWorkflowVO interacaoWorkflowNivelAplicacaoVONovoProspect) {
//        this.interacaoWorkflowNivelAplicacaoVONovoProspect = interacaoWorkflowNivelAplicacaoVONovoProspect;
//    }
//
//    public InteracaoWorkflowVO getInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda() {
//        if (interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda == null) {
//            interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda = new InteracaoWorkflowVO();
//        }
//        return interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda;
//    }
//
//    public void setInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda(InteracaoWorkflowVO interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda) {
//        this.interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda = interacaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda;
//    }

	public Map<String, InteracaoWorkflowVO> getMapaInteracaoNovoProspectVOs() {
		if (mapaInteracaoNovoProspectVOs == null) {
			mapaInteracaoNovoProspectVOs = new HashMap<String, InteracaoWorkflowVO>(0);
		}
		return mapaInteracaoNovoProspectVOs;
	}

	public void setMapaInteracaoNovoProspectVOs(Map<String, InteracaoWorkflowVO> mapaInteracaoNovoProspectVOs) {
		this.mapaInteracaoNovoProspectVOs = mapaInteracaoNovoProspectVOs;
	}
	
	public InteracaoWorkflowVO obterInteracaoWorkFlowPorTipoCampanhaUnidadeEnsino(String tipoCampanha, Integer unidadeEnsino) {
		return getMapaInteracaoNovoProspectVOs().get(tipoCampanha + "_" + unidadeEnsino.toString());
	}
}
