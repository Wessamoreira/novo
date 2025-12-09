package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;

public interface TransferenciaMatrizCurricularMatriculaInterfaceFacade {
    
    public void persistir(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception;
	
    public void incluir(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception;
    
    public void alterar(final TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception;
	
    public void excluir(TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception;
    
    public TransferenciaMatrizCurricularMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TransferenciaMatrizCurricularMatriculaVO> consultaRapidaPorMatricula(String matriculaPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        
    public List<TransferenciaMatrizCurricularMatriculaVO> consultaRapidaPorTransferenciaMatrizCurricular(Integer codigoTransferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void persistirTransferenciaMatrizCurricularMatriculaVOs(TransferenciaMatrizCurricularVO transferencia, List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatriculaVOs, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues
	 * 05/03/2015
	 * @param matriculaPeriodo
	 * @param usuario
	 * @throws Exception
	 */
    void excluirPorMatriculaPeriodo(Integer matriculaPeriodo, String periodoMatricula, String matricula,  UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 13/03/2015
	 * @param matricula
	 * @param usuario
	 * @throws Exception
	 */
	void excluirPorMatricula(String matricula, UsuarioVO usuario) throws Exception;
	
	public void alterarSituacao(final Integer codigo, final String situacao, final String alertas, UsuarioVO usuario) throws Exception;
	
	boolean executarBloqueioTransferenciaMatrizCurricularMatriculaValidandoGradeMigrarGradeDestino(int matriculaPeriodo, int gradeMigrar, int gradeDestino) throws Exception;
}
