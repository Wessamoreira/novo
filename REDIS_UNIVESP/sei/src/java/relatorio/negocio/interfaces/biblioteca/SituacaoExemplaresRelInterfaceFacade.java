package relatorio.negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.biblioteca.SituacaoExemplaresRelVO;

public interface SituacaoExemplaresRelInterfaceFacade {

    List<SituacaoExemplaresRelVO> criarObjeto(SituacaoExemplaresRelVO situacaoExemplaresRelVO, String tipoSaida) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseRelatorio();

    void validarDados(SituacaoExemplaresRelVO situacaoExemplaresRelVO) throws ConsistirException;

}
