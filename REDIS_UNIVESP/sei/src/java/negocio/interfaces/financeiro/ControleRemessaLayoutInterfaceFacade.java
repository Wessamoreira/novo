package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.DadosRemessaVO;
import negocio.comuns.utilitarias.EditorOC;

/**
 *
 * @author Carlos
 */
public interface ControleRemessaLayoutInterfaceFacade {
    public EditorOC executarGeracaoDadosArquivoRemessaGerandoNossoNumero(List<DadosRemessaVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
    public EditorOC executarGeracaoDadosArquivoRemessaUtilizandoNossoNumeroBanco(List<DadosRemessaVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
}
