package relatorio.negocio.interfaces.financeiro;

import java.util.List;
import java.util.Vector;

import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.ContaPagarPorTurmaRelVO;

public interface ContaPagarPorTurmaRelInterfaceFacade {

    String designIReportRelatorio();

    String caminhoBaseRelatorio();

    List<ContaPagarPorTurmaRelVO> criarObjeto(ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaPagaCancelado) throws Exception;

    void validarDados(ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO) throws ConsistirException;

    public Vector getOrdenacoesRelatorio();

}