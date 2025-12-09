package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ObservacaoComplementarDiplomaInterfaceFacade {
    
        public ObservacaoComplementarDiplomaVO novo() throws Exception;

	public void incluir(final ObservacaoComplementarDiplomaVO obj) throws Exception;
        
        public void alterar(final ObservacaoComplementarDiplomaVO obj) throws Exception;

        public void excluir(ObservacaoComplementarDiplomaVO obj) throws Exception;
        
        public List consultarPorExpedicaoDiploma(Integer codigoExpedicao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
        
        public void excluirObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma) throws Exception;
        
        public void alterarObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma, List objetos) throws Exception;
        
        public void incluirObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma, List objetos) throws Exception;
        
        public ObservacaoComplementarDiplomaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
}
