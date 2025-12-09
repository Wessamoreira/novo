package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.TermoCompromissoDocumentacaoPendenteRelVO;

/**
 * 
 * @author Alessandro
 */
public interface TermoCompromissoDocumentacaoPendenteRelInterfaceFacade {

    List<TermoCompromissoDocumentacaoPendenteRelVO> criarObjeto(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

    void ValidarDados(MatriculaVO matriculaVO) throws ConsistirException;

}
