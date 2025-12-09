package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface ConfiguracaoTCCInterfaceFacade {
	
	void persistir(ConfiguracaoTCCVO configuracaoTCCVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoTCCVO> consultarPorDescricao(String descricao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void adicionarConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO, ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO)  throws Exception;
	
	void removerConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO, ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO)  throws Exception;
	
	void validarDados(ConfiguracaoTCCVO configuracaoTCCVO) throws ConsistirException;

	ConfiguracaoTCCVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

	ConfiguracaoTCCVO consultarPorCurso(int curso, int nivelMontarDados) throws Exception;

	public void removerQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoVO) throws Exception;
	
	public void adicionarQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO) throws Exception;
	
	public void removerQuestaoConteudo(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoVO) throws Exception;

	public void adicionarQuestaoConteudo(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO) throws Exception;
	
	public void alterarOrdemQuestao(ConfiguracaoTCCVO configuracaoVO, QuestaoTCCVO questaoTCCVO, QuestaoTCCVO questaoTCCVO2) throws Exception;
}