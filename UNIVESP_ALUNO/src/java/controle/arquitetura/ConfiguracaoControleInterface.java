package controle.arquitetura;

import controle.basico.ConfiguracoesControle;
import negocio.comuns.basico.ConfiguracoesVO;

/**
 * Interface que deve ser implementada pelo controlador ao adicionar nova
 * configuracao do sistema.
 * 
 * @author Diego
 */
public interface ConfiguracaoControleInterface {

	public void iniciarControleConfiguracao(ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception;

	public void gravar(ConfiguracoesVO configuracoesVO) throws Exception;

	public String excluir() throws Exception;

	public void limparCamposParaClone();

}
