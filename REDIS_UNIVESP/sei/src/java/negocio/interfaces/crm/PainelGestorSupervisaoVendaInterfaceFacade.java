/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.Date;
import java.util.List;

import negocio.comuns.crm.DadosGeraisCampanhaPorFuncionarioVO;
import negocio.comuns.crm.PainelGestorSupervisaoVendaVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;

/**
 *
 * @author RODRIGO
 */
public interface PainelGestorSupervisaoVendaInterfaceFacade {
    public PainelGestorSupervisaoVendaVO executarGeracaoDadosEstatisticaCampanha(Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino)throws Exception;
    
    public void consultarEstatisticaoPerfilProspect(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO, Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino, SituacaoProspectPipelineControleEnum situacaoProspectPipelineControleEnum) throws Exception;
    public List<DadosGeraisCampanhaPorFuncionarioVO> consultarDadosEstatisticoMetaPorVendedor(Integer campanha, Integer vendedor) throws Exception ;
}
