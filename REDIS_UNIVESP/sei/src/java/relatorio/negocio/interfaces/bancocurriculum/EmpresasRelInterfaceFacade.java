/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.bancocurriculum;

import java.util.Date;
import java.util.List;

import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.ParceiroVO;

/**
 *
 * @author Philippe
 */
public interface EmpresasRelInterfaceFacade {

    public String caminhoBaseRelatorio();

    public String designIReportRelatorio();

    public String designIReportRelatorioExcel();

	public List criarObjeto(ParceiroVO parceiroVO, CidadeVO cidadeVO, EstadoVO estadoVO, String ordenarPor, String tipoConsulta, Date dataInicio, Date dataFim) throws Exception;
}
