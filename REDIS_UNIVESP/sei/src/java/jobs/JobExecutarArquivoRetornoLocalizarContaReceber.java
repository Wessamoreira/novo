package jobs;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public class JobExecutarArquivoRetornoLocalizarContaReceber extends SuperControle   {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4149440992223245894L;
	private ProgressBarVO progressBarVO;
	private ControleCobrancaVO controleCobrancaVO;
	private UsuarioVO usuarioVO;

	public JobExecutarArquivoRetornoLocalizarContaReceber(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) {
		this.progressBarVO = progressBarVO;
		this.controleCobrancaVO = controleCobrancaVO;
		this.usuarioVO = usuarioVO;
	}
	
	public void executarLocalizacaoContaReceber() throws Exception {
		try {
			if(!getFacadeFactory().getRegistroExecucaoJobFacade().consultarSeExisterRegistroExecucaoJobPorCodigoOrigem(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO, controleCobrancaVO.getCodigo())) {
				RegistroExecucaoJobVO rej = new RegistroExecucaoJobVO();
				rej.setNome(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO.getName());
				rej.setCodigoOrigem(controleCobrancaVO.getCodigo());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(rej);	
			}
			getFacadeFactory().getControleCobrancaFacade().processarArquivoProgressBarVO(controleCobrancaVO, progressBarVO, usuarioVO);			
		} catch (Exception e) {
			throw e;
		}
	}

}
