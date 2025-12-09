/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.bancocurriculum;

import java.util.Date;
import java.util.List;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import relatorio.negocio.comuns.bancocurriculum.EmpresaPorVagasRelVO;

/**
 *
 * @author rogerio.gomes
 */
public interface EmpresaPorVagasRelInterfaceFacade {

    public List<EmpresaPorVagasRelVO> criarObjeto(VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, Boolean contratados) throws Exception;

    public String caminhoBaseRelatorio();

    public String designIReportRelatorioExcel();
    
    public String designIReportRelatorio();
}
