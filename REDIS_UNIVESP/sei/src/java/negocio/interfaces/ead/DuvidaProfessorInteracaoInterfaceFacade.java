package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.ead.DuvidaProfessorInteracaoVO;

public interface DuvidaProfessorInteracaoInterfaceFacade {

    void incluir(DuvidaProfessorInteracaoVO duvidaProfessorInteracaoVO) throws Exception;
    
    List<DuvidaProfessorInteracaoVO> consultarPorDuvidaProfessor(Integer duvidaProfessor) throws Exception;
    
    
}
