package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.utilitarias.ConsistirException;

public interface CampanhaColaboradorInterfaceFacade {
    public CampanhaColaboradorVO novo() throws Exception;
    public void incluir(CampanhaColaboradorVO obj) throws Exception;
    public void incluirCampanhaColaborador(Integer campanha, List<CampanhaColaboradorVO> objetos) throws Exception;
    public void alterarCampanhaColaborador(Integer campanha, List<CampanhaColaboradorVO> objetos) throws Exception;
    public void excluir(CampanhaColaboradorVO obj, List<CampanhaColaboradorVO> objetos) throws Exception;
    public List<CampanhaColaboradorVO> montarListaCampanhaColaborador(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public CampanhaColaboradorVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void excluirCampanhaColaborador(Integer campanha) throws Exception;
    public List<CampanhaColaboradorVO> consultarPorParticipante(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaColaboradorVO> consultarPorCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void validarDadosFuncionario(FuncionarioVO funcionarioVO) throws Exception;
    public void adicionarObjCampanhaColaborador(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, CampanhaColaboradorVO obj) throws Exception;
    public void adicionarObjCampanhaColaboradorAlterarCompromisso(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, CampanhaColaboradorVO obj) throws Exception;
    public void excluirObjCampanhaColaborador(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, Integer funcionario) throws Exception;
    public void adicionarObjCampanhaColaboradorCursoVOs(CampanhaColaboradorVO obj,  CampanhaColaboradorCursoVO campanhaColaboradorCursoVO) throws Exception;
    public void excluirObjCampanhaColaboradorCursoVOs(CampanhaColaboradorVO obj,  CampanhaColaboradorCursoVO campanhaColaboradorCursoVO) throws Exception;
    public void realizarExclusaoPorCursoNaCampanha(CampanhaVO campanhaVO) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void adicionarTodosCursosCampanhaColaborador(CampanhaColaboradorVO obj);
    public List<CampanhaColaboradorVO> consultarCampanhaColaboradorPorCampanha(Integer campanha, UsuarioVO usuarioVO) throws Exception;
	    
    public CampanhaColaboradorVO consultarCampanhaAndResponsavelInscritoProcessoSeletivo(Integer codigoProcessoSeletivo, Integer curso, 
           PoliticaGerarAgendaEnum politicaGerarAgenda, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington - 9 de set de 2015 
	 * @param campanhaVO 
	 * @throws ConsistirException 
	 */
	public void validarDadosAlterarCampanhaColaborador(CampanhaVO campanhaVO) throws ConsistirException;
	/**
	 * @author Carlos Eugênio - 05/12/2016
	 * @param campanha
	 * @param campanhaColaboradorVOs
	 * @param usuarioVO
	 */
	void realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultorPublicoAlvoEspecifico(Integer campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 15/12/2016
	 * @param listaCampanhaColaboradorVOs
	 * @param prospect
	 * @param usuarioVO
	 * @return
	 */
	CampanhaColaboradorVO obterConsultorIniciouCampanhaProspect(List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, Integer prospect, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param campanhaVO
	 * @param campanhaColaboradorVOs
	 * @param usuarioVO
	 */
	void realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultor(CampanhaVO campanhaVO, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO);

	CampanhaColaboradorVO consultarColaboradorDeveRealizarProximaAgendaOrdenandoPelaDataDoUltimoCompromissoPorCampanha(Integer campanha, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
