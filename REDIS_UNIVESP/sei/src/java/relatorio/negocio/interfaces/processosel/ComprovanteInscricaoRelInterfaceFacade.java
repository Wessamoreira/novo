/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.processosel.ComprovanteInscricaoRelVO;

/**
 *
 * @author Philippe
 */
public interface ComprovanteInscricaoRelInterfaceFacade {
	public List<ComprovanteInscricaoRelVO> preencherDadosComprovanteInscricao(List<InscricaoVO> inscricao, UsuarioVO usuarioVO) throws Exception;

	public void validarDadosPesquisa(InscricaoVO inscricao) throws ConsistirException;

	public String designIReportRelatorio(String layout);

	public String caminhoBaseRelatorio();
}
