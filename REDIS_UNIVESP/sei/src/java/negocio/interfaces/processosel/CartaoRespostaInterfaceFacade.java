package negocio.interfaces.processosel;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.CartaoRespostaVO;

public interface CartaoRespostaInterfaceFacade {

	public void incluir(CartaoRespostaVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public CartaoRespostaVO consultarCartaoResposta(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterar(CartaoRespostaVO obj,  UsuarioVO usuario) throws Exception;

	public int validaDados(CartaoRespostaVO obj) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/09/2015
	 * @param cartaoRespostaVO
	 * @param inscricaoVOs
	 * @param imprimirCartao1
	 * @param imprimirCartao2
	 * @param linhaInicio
	 * @param colunaInicio
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	String realizarImpressaoTesteCartaoRespostaLC3000(CartaoRespostaVO cartaoRespostaVO, String numeroTeste, Boolean imprimirCartao1, Boolean imprimirCartao2, Integer linhaInicio, Integer colunaInicio) throws Exception;


}
