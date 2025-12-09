package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface QuestaoProvaProcessoSeletivoInterfaceFacade {

    
    void incluirQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception;
    
    void alterarQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception;
    
    List<QuestaoProvaProcessoSeletivoVO> consultarPorProvaProcessoSeletivo(Integer provaProcessoSeletivo, NivelMontarDados nivelMontarDados);
    
    void validarDados(QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws ConsistirException;
    
    

    
}
