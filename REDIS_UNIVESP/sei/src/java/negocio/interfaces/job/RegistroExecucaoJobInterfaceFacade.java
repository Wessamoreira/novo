package negocio.interfaces.job;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobTotaisVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.dominios.TipoJobSerasaApiGeo;

/**
 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
 *
 */
public interface RegistroExecucaoJobInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 18 de mai de 2016 
	 * @param registroExecucaoJobVO
	 * @throws Exception 
	 */
	void incluir(RegistroExecucaoJobVO registroExecucaoJobVO) throws Exception;


	/** 
	 * @author Victor Hugo de Paula Costa - 18 de mai de 2016 
	 * @param registroExecucaoJobVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluirRegistroExecucaoJob(RegistroExecucaoJobVO registroExecucaoJobVO, UsuarioVO usuarioVO);
	
	List<RegistroExecucaoJobVO> consultarUltimosRegistros() throws Exception;

	List<RegistroExecucaoJobVO> consultarRegistrosUltimaHora() throws Exception;

	int consultarTotalEmailsAguardandoEnvio();

	void consultarTotaisUltimaHora(RegistroExecucaoJobTotaisVO totaisUltimaHora);

	void consultarTotaisUltimas24Horas(RegistroExecucaoJobTotaisVO totaisUltimas24Horas);

	int consultarTotalEmailsQueNaoForamEnviados();
	
	void consultar(DataModelo dataModelo, RegistroExecucaoJobVO obj);

	Date consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC();

	void excluirRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception;

	List<RegistroExecucaoJobVO> consultarRegistroExecucaoJobInterrompidasPorNomeOrigem(JobsEnum jobsEnum) throws Exception;
	
	void atualizarAgendamentoExecucaoRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem, Date dataInicio) throws Exception;
	
	void atualizarCampoDataTerminoRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception;
	
	void atualizarCampoErroRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem, String erro) throws Exception;
	boolean consultarSeExisterRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception;
	
	RegistroExecucaoJobVO consultarRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum) throws Exception;

	List<RegistroExecucaoJobVO> consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum jobsEnum) throws Exception;


	RegistroExecucaoJobVO consultarRegistroExecucaoJobPorCodigoOrigemSemErro(JobsEnum jobsEnum) throws Exception;


	public void consultarOtimizado(DataModelo dataModelo) throws Exception;
}
