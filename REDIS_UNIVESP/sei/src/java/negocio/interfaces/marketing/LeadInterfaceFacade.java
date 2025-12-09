package negocio.interfaces.marketing;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.crm.ProspectsVO;
import webservice.servicos.objetos.LeadRSVO;

/**
 * @author Gilberto Nery - 29/11/17
 *
 */
public interface LeadInterfaceFacade {

	
    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>PreInscricaoVO</code>.
     */
	public LeadRSVO novo() throws Exception;
	
    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
	public void setIdEntidade(String aIdEntidade);
	

	/**
	 * Cria uma thread e realiza a chamada via WS para incluir uma lista de leads no RD Station com a lista de Prospects
	 * 
	 * @param prospectsVos
	 * @param config TODO
	 * @return http status code
	 * @throws Exception
	 */
	public void incluirListaDeLeadsNoRdStation(List<ProspectsVO> prospectsVos, ConfiguracaoGeralSistemaVO config) ;

	
	/**
	 * Inclui o Prospect na base de leads do RD Station por meio de uma chamada WS
	 * 
	 * @param prospectsVO
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public int incluirLeadNoRdStation(ProspectsVO prospectsVO, ConfiguracaoGeralSistemaVO config) throws Exception;
}