package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface QuestaoTCCInterfaceFacade {
	
	public void incluirQuestaoFormatacao(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception;
	
	public void incluirQuestaoConteudo(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception ;
	
	void alterarQuestaoConteudo(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception;
	
	void alterarQuestaoFormatacao(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception;
	
	public List<QuestaoTCCVO> consultarPorConfiguracao(Integer configuracao, String origemQuestao) throws Exception;
	
	//void alterarOrdemOpcaoRespostaQuestao(QuestaoTCCVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, OpcaoRespostaVagaQuestaoVO opc2) throws Exception;

	//void adicionarOrdemOpcaoRespostaQuestao(QuestaoTCCVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, Boolean validarDados) throws Exception;

	//void removerOrdemOpcaoRespostaQuestao(QuestaoTCCVO questaoVO, OpcaoRespostaVagaQuestaoVO opc1)  throws Exception;

	void validarDados(QuestaoTCCVO questaoVO) throws ConsistirException;

	QuestaoTCCVO novo() throws Exception;

}
