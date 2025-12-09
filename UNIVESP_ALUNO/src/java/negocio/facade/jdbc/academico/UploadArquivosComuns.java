/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.UploadArquivosComunsInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class UploadArquivosComuns extends ControleAcesso implements UploadArquivosComunsInterfaceFacade {
    
    public UploadArquivosComuns() {
        
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    public void persistir(ArquivoVO arquivoVO, UsuarioVO usuarioVO, List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
    	try {
    		getFacadeFactory().getArquivoFacade().incluir(arquivoVO, false, usuarioVO, configuracaoGeralSistemaVO);
    		
    		if (possuiAssinaturaDigitalUpload(arquivoVO, listaFuncionarioAssinarDigitalmenteVOs, usuarioVO)) {
    			getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaUploadArquivoInstitucional(arquivoVO, listaFuncionarioAssinarDigitalmenteVOs, configuracaoGeralSistemaVO, usuarioVO);
    		}
		} catch (Exception e) {
			if (arquivoVO.getPastaBaseArquivoEnum() == null) {
				throw new Exception("Arquivo não encontrado! Erro na descrição do caminho do diretório base de uploads (Configurações Gerais do Sistema).");
			}
			throw e;
		}
    }
    
    public Boolean possuiAssinaturaDigitalUpload(ArquivoVO arquivoVO, List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, UsuarioVO usuarioVO) {
    	if (!listaFuncionarioAssinarDigitalmenteVOs.isEmpty() || arquivoVO.getAssinarCertificadoDigitalUnidadeEnsino()) {
    		return true;
    	}
    	return false;
    }

    public Boolean verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(UsuarioVO usuarioLogado) {
        return verificarPermissaoAlteracaoVisualizacaoDataDisponibilizacaoMaterial(usuarioLogado);
    }

    public  void verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(UsuarioVO usuario, String nomeEntidade) throws Exception {
        getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public Boolean verificarPermissaoAlteracaoVisualizacaoDataDisponibilizacaoMaterial(UsuarioVO usuarioLogado) {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(usuarioLogado, "ApresentarDataDisponibilizacaoMaterial");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        return liberar;
    }

    public void inicializarDadosArquivoPersistencia(ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws ConsistirException {
        arquivoVO.setResponsavelUpload(usuarioVO);
        arquivoVO.setDataUpload(new Date());
        if (arquivoVO.getManterDisponibilizacao()) {
            arquivoVO.setDataDisponibilizacao(arquivoVO.getDataUpload());
            arquivoVO.setDataIndisponibilizacao(null);
        }
        arquivoVO.setSituacao(SituacaoArquivo.ATIVO.getValor());
        arquivoVO.setOrigem(OrigemArquivo.INSTITUICAO.getValor());
    }
    
    public void validarDados(ArquivoVO arquivoVO) throws Exception {
    	
        if(!arquivoVO.getApresentarPortalAluno() && !arquivoVO.getApresentarPortalProfessor() && !arquivoVO.getApresentarPortalCoordenador()){
        	throw new Exception("Deve ser informado pelo menos um PORTAL a ser apresentado o arquivo.");
        }
        if(arquivoVO.getNome().trim().isEmpty()){
        	throw new Exception("O campo ARQUIVO deve ser informado.");
        }
    }
    
    public void removerArquivoVO(ArquivoVO arquivoVO, List<ArquivoVO> listaArquivos, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        int index = 0;
        Iterator i = listaArquivos.iterator();
        while (i.hasNext()) {
            ArquivoVO objExistente = (ArquivoVO) i.next();
            if (objExistente.getDescricao().equals(arquivoVO.getDescricao())) {
                listaArquivos.remove(index);
                getFacadeFactory().getArquivoFacade().excluir(arquivoVO, true, "Upload", usuarioVO, configuracaoGeralSistemaVO);
                return;
            }
            index++;
        }
    }
    
    @Override
    public void adicionarProfessorListaAssinaturaDigital(List<FuncionarioVO> listaFuncionarioVOs, List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, UsuarioVO usuarioVO) {
    	for (FuncionarioVO funcionarioAdicionarVO : listaFuncionarioVOs) {
			if (funcionarioAdicionarVO.getSelecionado()) {
				int index = 0;
				boolean adicionar = true;
				for (FuncionarioVO funcionarioAssinaturaVO : listaFuncionarioAssinarDigitalmenteVOs) {
					if (funcionarioAdicionarVO.getCodigo().equals(funcionarioAssinaturaVO.getCodigo())) {
						listaFuncionarioAssinarDigitalmenteVOs.set(index, funcionarioAdicionarVO);
						adicionar = false;
						break;
					}
					index++;
				}
				if (adicionar) {
					listaFuncionarioAssinarDigitalmenteVOs.add(funcionarioAdicionarVO);
				}
			}
		}
    }
    
    @Override
    public void removerProfessorListaAssinaturaDigital(List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs, FuncionarioVO objRemoverVO, UsuarioVO usuarioVO) {
    	int index = 0;
    	for (FuncionarioVO funcionarioVO : listaFuncionarioAssinarDigitalmenteVOs) {
			if (funcionarioVO.getCodigo().equals(objRemoverVO.getCodigo())) {
				listaFuncionarioAssinarDigitalmenteVOs.remove(index);
				break;
			}
			index++;
		}
    }
    
    @Override
    public void realizarInvativacaoArquivo(ArquivoVO arquivoVO, SituacaoArquivo situacaoArquivo, UsuarioVO usuarioVO) throws Exception {
    	getFacadeFactory().getArquivoFacade().alterarSituacaoArquivo(arquivoVO, situacaoArquivo, false, usuarioVO);
    	arquivoVO.setSituacao(situacaoArquivo.getValor());
    }
}
