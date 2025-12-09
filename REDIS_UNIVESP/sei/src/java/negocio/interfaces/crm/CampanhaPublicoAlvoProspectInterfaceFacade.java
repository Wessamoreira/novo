/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;

/**
 *
 * @author Philippe
 */
public interface CampanhaPublicoAlvoProspectInterfaceFacade {
    public void incluirCampanhaPublicoAlvoProspect(Integer campanhaPublicoAlvo, List<CampanhaPublicoAlvoProspectVO> objetos, UsuarioVO usuarioLogado) throws Exception;
    public void excluirCampanhaPublicoAlvoProspectPorCodigoCampanhaPublicoAlvo(Integer codigo, UsuarioVO usuarioLogado) throws Exception;
    public void excluirCampanhaPublicoAlvoProspectPorCodigoProspect(Integer codigo, UsuarioVO usuarioLogado) throws Exception;
    public List<CampanhaPublicoAlvoProspectVO> consultarPorCampanhaPublicoAlvo(Integer campanhaPublicAlvo) throws Exception;
    
    /**
	 * @author Carlos Eugênio - 01/12/2016
	 * @param campanhaPublicoAlvo
	 * @param listaCampanhaPublicoAlvoVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarCampanhaPublicoAlvoProspectPorCampanhaPublicoAlvo(Integer campanha, Integer campanhaPublicoAlvo, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoVOs, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param listaCompromissoIniciouAgendaVOs
	 * @param listaCampanhaPublicoAlvoProspectVOs
	 * @return
	 */
	public List<CampanhaPublicoAlvoProspectVO> realizarCarregamentoProspectIniciouAgendaParaVisualizacao(CampanhaVO campanhaVO, List<CompromissoAgendaPessoaHorarioVO> listaCompromissoIniciouAgendaVOs, Boolean publicoAlvoEspecifico, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs);
	void alterarConsultorDistribuicao(Integer campanhaPublicoAlvoPropect, Integer funcionarioConsultor)
			throws Exception;
	void realizarAlteracaoConsultorAgendaCampanhaPublicoAlvoProspect(
			CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, UsuarioVO usuarioVO) throws Exception;
	List<CampanhaPublicoAlvoProspectVO> consultarProspectsVinculadoCampanhaSemVinculoPublicoAlvo(CampanhaVO campanhaVO,
			UsuarioVO usuarioVO) throws Exception;
	public void alterarCampanhaPublicoAlvoProspectUnificacaoFuncionario(Integer funcManter, Integer funcRemover , UsuarioVO usuarioVO) throws Exception;
}
