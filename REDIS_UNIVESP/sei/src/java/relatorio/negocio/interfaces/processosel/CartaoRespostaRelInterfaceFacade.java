/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import relatorio.negocio.comuns.processosel.CartaoRespostaRelVO;

/**
 *
 * @author Philippe
 */
public interface CartaoRespostaRelInterfaceFacade {

    public List<CartaoRespostaRelVO> criarObjeto(Boolean trazerSomenteCandidatosComInscricaoPaga, Integer candidato, Integer processoSeletivo, Integer itemProcSeletivoDataProva, Integer sala, Integer inscricao) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseRelatorio();

    public List<InscricaoVO> consultarTodosCandidatos(String valorPesquisa, String campoPesquisa, Integer itemProcSeletivoDataProva);

    String realizarImpressaoCartaoRespostaLC3000(List<CartaoRespostaRelVO> inscricaoVOs, Integer numeroCopias, Integer iniciarColuna, boolean preenchimento, boolean impressarTeste, UsuarioVO usuarioLogado) throws Exception;
}
