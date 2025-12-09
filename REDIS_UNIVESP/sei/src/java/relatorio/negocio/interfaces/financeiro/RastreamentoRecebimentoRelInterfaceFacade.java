package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.RastreamentoRecebimentoRelVO;

public interface RastreamentoRecebimentoRelInterfaceFacade {

    List<RastreamentoRecebimentoRelVO> consultarRastreamentoRecebimento(Integer codUnidadeEnsino, Integer codCurso, Integer codTurno, String mes, String ano) throws Exception;

    void validarDados(Integer codigoUnidadeEnsino, String mes, String ano) throws ConsistirException;

    Date montarData(Integer qtdDias, int mes, int ano) throws Exception;

    Date atualizarData(Date data, int indice);

    String nomeMesExtenso(int indice) throws Exception;

}
