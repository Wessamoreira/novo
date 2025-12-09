package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface OpcaoRespostaQuestaoInterfaceFacade {
    
    void incluirOpcaoRespostaQuestao(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception;
    
    void alterarOpcaoRespostaQuestao(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception;    
    
    void validarDados(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws ConsistirException;
    
    List<OpcaoRespostaQuestaoVO> consultarPorQuestao(Integer questao) throws Exception;

    OpcaoRespostaQuestaoVO montarDados(SqlRowSet rs, String prefixo);

	OpcaoRespostaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    
    

}
