package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.RegistroEntradaProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface RegistroEntradaProspectsInterfaceFacade {
	

    public void validarDados(RegistroEntradaProspectsVO obj) throws ConsistirException;
    public RegistroEntradaProspectsVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorDescricaoRegistroEntrada(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void incluirRegistroEntradaProspectss(final RegistroEntradaVO registroEntrada,final List<RegistroEntradaProspectsVO> objetos,final UsuarioVO usuarioLogado ) throws Exception;
    public void incluir(final RegistroEntradaProspectsVO obj, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public void excluirRegistroEntradaProspectss( Integer registroEntrada, UsuarioVO usuario ) throws Exception;
    public List consultarRegistroEntradaProspects(Integer registroEntrada,  int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuarioLogado) throws Exception;
    public Integer consultarTotalDeRegistroRegistroEntradaProspects(Integer registroEntrada, UsuarioVO usuarioLogado) throws Exception;
    public void alterarRegistroEntradaProspectss( RegistroEntradaVO registroEntrada, List objetos, UsuarioVO usuarioLogado) throws Exception;
    
    /**
     * Envia os Prospects do registro de entrada para a base de leads no RD Station
     * 
     * @param registroEntrada
     * @param objetos
     * @param usuarioLogado
     * @throws Exception
     */
	public void enviarProspectsParaRdStation(List<RegistroEntradaProspectsVO> objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO config);
	
	/**
	 * 
	 * Realiza a chamada ao WS do RD Station para incluir o prospect na base de leads
	 * 
	 * @param entradaProspectsVO
	 * @return HTTP status code <br>
	 * 							200 and 300 - OK <br>
	 * 							Outros codigos indicam que o prospect nao foi salvo na base de leads
	 * 
	 */
	public Integer enviarProspectParaRdStation(RegistroEntradaProspectsVO entradaProspectsVO);
	
}