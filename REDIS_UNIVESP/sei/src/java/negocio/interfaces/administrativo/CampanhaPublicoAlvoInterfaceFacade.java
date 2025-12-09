package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ProgressBarVO;


public interface CampanhaPublicoAlvoInterfaceFacade {

    public List<CampanhaPublicoAlvoVO> montarListaCampanhaPublicoAlvo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirCampanhaPublicoAlvo(Integer meta, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception;
    
    public void alterarCampanhaPublicoAlvo(Integer campanha, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception;

    public void excluirCampanhaPublicoAlvo(Integer meta, UsuarioVO usuarioVO) throws Exception;

    public Integer montarTotalProspectsSelecionadosCampanhaRegistroEntrada(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception;

    public Integer montarTotalProspectsSelecionadosCampanhaRevisitacaoCarteira(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception;

    public CampanhaPublicoAlvoVO montarCampanhaPublicoAlvo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ProspectsVO> montarProspectsGeracaoAgenda(CampanhaPublicoAlvoVO obj) throws Exception;
    
    public void adicionarObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, Boolean revisitacaoCarteira, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception;
    
    public void excluirObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception;

    public Double montarQuantidadeTotalContatosProspectsDia(Integer codigoCampanha);
    
    public List<CampanhaPublicoAlvoVO> consultarCampanhaPublicoAlvoPorCampanha(Integer campanha, UsuarioVO usuarioVO);
    
    public void adicionarObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, Boolean revisitacaoCarteira, ProgressBarVO progressBar, UsuarioVO usuarioVO, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception;

	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param campanha
	 * @param objetos
	 * @throws Exception
	 */
	void alterarCampanhaPublicoAlvoSemSubordinada(Integer campanha, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarCampanhaPublicoAlvoRegeracaoAgenda(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 29/12/2016
	 * @param campanhaVO
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	List<CampanhaPublicoAlvoProspectVO> consultarProspectsSelecionadosCampanhaRevisitacaoCarteira(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception;
	
	List<CampanhaPublicoAlvoProspectVO> consultarProspectsSelecionadosCampanhaRevisitacaoCarteiraResponsavelFinanceiro(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception;

	void incluirSemSubordinada(CampanhaPublicoAlvoVO obj, UsuarioVO usuarioVO) throws Exception;
}
