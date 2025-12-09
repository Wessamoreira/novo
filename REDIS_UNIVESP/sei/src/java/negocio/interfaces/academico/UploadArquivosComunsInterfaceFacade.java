/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;

/**
 *
 * @author Carlos
 */
public interface UploadArquivosComunsInterfaceFacade {
	public void persistir(ArquivoVO arquivoVO, UsuarioVO usuarioVO, List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public Boolean verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(UsuarioVO usuarioLogado);
    public void inicializarDadosArquivoPersistencia(ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws ConsistirException;
    public void removerArquivoVO(ArquivoVO arquivoVO, List<ArquivoVO> listaArquivos, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void validarDados(ArquivoVO arquivoVO) throws Exception;
    void adicionarProfessorListaAssinaturaDigital(List<FuncionarioVO> listaFuncionarioVOs, List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, UsuarioVO usuarioVO);
	void removerProfessorListaAssinaturaDigital(List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, FuncionarioVO objRemoverVO, UsuarioVO usuarioVO);
	void realizarInvativacaoArquivo(ArquivoVO arquivoVO, SituacaoArquivo situacaoArquivo, UsuarioVO usuarioVO) throws Exception;
}
