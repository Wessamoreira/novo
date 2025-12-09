package negocio.interfaces.processosel;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface OpcaoRespostaQuestaoProcessoSeletivoInterfaceFacade {

 void incluirOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception;
    
    void alterarOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception;    
    
    void validarDados(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws ConsistirException;
    
    List<OpcaoRespostaQuestaoProcessoSeletivoVO> consultarPorQuestao(Integer questao) throws Exception;

    OpcaoRespostaQuestaoProcessoSeletivoVO montarDados(SqlRowSet rs, String prefixo);
    
}
