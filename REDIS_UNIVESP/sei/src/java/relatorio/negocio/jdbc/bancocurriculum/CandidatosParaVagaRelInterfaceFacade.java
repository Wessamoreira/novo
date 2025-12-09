/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.bancocurriculum;

import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.bancocurriculum.CandidatosParaVagaRelVO;

/**
 *
 * @author PEDRO
 */
public interface CandidatosParaVagaRelInterfaceFacade {

    String caminhoBaseRelatorio();

    List criarObjeto(List<CandidatosParaVagaRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO) throws Exception;

    String designIReportRelatorio();

    String designIReportRelatorioExcel();

    SqlRowSet executarConsultaParametrizada(ParceiroVO parceiro, VagasVO vagas, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuario) throws Exception;
    
}
