package negocio.facade.jdbc.academico;



import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import controle.academico.AssinarDocumentoEntregueControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.AssinarDocumentoEntregueInterfaceFacade;


/**
 * 
 * @see DocumetacaoMatriculaVO
 * @see ControleAcesso
 * @see Matricula
 */
@Repository
public class AssinarDocumentoEntregue extends ControleAcesso implements AssinarDocumentoEntregueInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public AssinarDocumentoEntregue() throws Exception {
		super();
	}

	@Override
	public synchronized void assinarDocumentoEntregue(ProgressBarVO progressBarVO, AssinarDocumentoEntregueVO assinarDocumentoEntregueVO ,Integer nivelMontarDados ,ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
			validarDados(assinarDocumentoEntregueVO,usuarioVO);			
			new AssinarDocumentacaoMatriculaExecucao(progressBarVO,assinarDocumentoEntregueVO, nivelMontarDados, configuracaoGeralSistema, usuarioVO).run();			
	}
	
	
	@Override
	public ProgressBarVO consultarProgressBarEmExecucao() {
		if(getAplicacaoControle().getMapThreadIndiceReajuste().containsKey("AssinarDocumentoEntregue")){
			ProgressBarVO progressBarVO = getAplicacaoControle().getMapThreadIndiceReajuste().get("AssinarDocumentoEntregue");
			if(progressBarVO.getAtivado()) {
				if(!progressBarVO.getProgressBarThread().isAlive()) {
					progressBarVO.setForcarEncerramento(true);
					progressBarVO.encerrar();
					return progressBarVO;
				}
				return progressBarVO;
			}
			 getAplicacaoControle().getMapThreadIndiceReajuste().remove("AssinarDocumentoEntregue");
		}
		return null;
	}
	
	
	private void validarDados(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO,UsuarioVO usuarioVO) throws Exception {
		 if(usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
			 throw new Exception("Este usuário não pode assinar a Documentação da Matrícula, pois não possui nenhuma pessoa vinculada a ele.");
			 
		 }else if(!Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getOrigemArquivo())) {
			 throw new Exception("O Tipo Arquivo deve ser informado para Assinar o Documento Entregue.");
			 
		 }else if(!Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getServidorArquivo())) {
			 throw new Exception("O Servidor deve ser informado para Assinar o Documento Entregue.");
			 
		 }else if (!Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataInicioEntrega())) {
			 throw new Exception("A Data Inicial deve ser informada para Assinar o Documento Entregue.");
			 
		 }else if(!Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataFimEntrega())) {
			 throw new Exception("A Data Final deve ser informada para Assinar o Documento Entregue.");
			 
		 }else if(Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataInicioEntrega()) && Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getDataFimEntrega())) {
			 	ZoneId defaultZoneId = ZoneId.systemDefault();	
				LocalDate ldDataEntregaInicio = assinarDocumentoEntregueVO.getDataInicioEntrega().toInstant().atZone(defaultZoneId).toLocalDate();
				LocalDate ldDataEntregaFim = assinarDocumentoEntregueVO.getDataFimEntrega().toInstant().atZone(defaultZoneId).toLocalDate();
				if (ldDataEntregaInicio.isAfter(ldDataEntregaFim)) {
					throw new Exception("A Data Inicial  não pode ser maior que a data Final de Entrega do Documento.");
			}	
		}
	}


	class AssinarDocumentacaoMatriculaExecucao implements Runnable{
		private AssinarDocumentoEntregueVO assinarDocumentoEntregueVO;
		private Integer nivelMontarDados;
		private ConfiguracaoGeralSistemaVO configuracaoGeralSistema;
		private ProgressBarVO progressBarVO;
		private UsuarioVO usuarioVO;
		
		public  AssinarDocumentacaoMatriculaExecucao (ProgressBarVO progressBarVO,AssinarDocumentoEntregueVO assinarDocumentoEntregueVO, Integer nivelMontarDados,ConfiguracaoGeralSistemaVO configuracaoGeralSistema,UsuarioVO usuarioVO) {
			this.assinarDocumentoEntregueVO = assinarDocumentoEntregueVO;
			this.nivelMontarDados = nivelMontarDados;
			this.configuracaoGeralSistema = configuracaoGeralSistema;
			this.progressBarVO = progressBarVO;
			this.usuarioVO = usuarioVO;
		}
		

		@Override
		public void run() {
			RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
			try {
				progressBarVO.getAplicacaoControle().getMapThreadIndiceReajuste().put("AssinarDocumentoEntregue", progressBarVO);
				registroExecucaoJobVO.setDataInicio(new Date());
				registroExecucaoJobVO.setNome("EXECUÇÃO ASSINATURA DE DOCUMENTO ENTREGUE");
				if(assinarDocumentoEntregueVO.getOrigemArquivo().equals("DM")) {
					assinarDocumentacaoMatricula(assinarDocumentoEntregueVO, configuracaoGeralSistema, progressBarVO, nivelMontarDados,usuarioVO);
				}else if(assinarDocumentoEntregueVO.getOrigemArquivo().equals("DC")) {
					assinarDocumentacaoGED(assinarDocumentoEntregueVO, progressBarVO, configuracaoGeralSistema, nivelMontarDados, usuarioVO);
				}
				progressBarVO.getSuperControle().setMensagemID("msg_arquivos_assinados_processados_com_sucesso", Uteis.SUCESSO);
			} catch (Exception e) {				
				registroExecucaoJobVO.setErro(e.getMessage());
				progressBarVO.getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}finally {
				registroExecucaoJobVO.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
				progressBarVO.setProgresso(progressBarVO.getMaxValue().longValue());
				progressBarVO.setForcarEncerramento(true);
//				progressBarVO.encerrar();
				progressBarVO.getAplicacaoControle().getMapThreadIndiceReajuste().remove("AssinarDocumentoEntregue");				
			}
		}
	}

	/**
	 * @param listaDocumetoMatricula
	 * @param configuracaoGeralSistema
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void assinarDocumentacaoMatricula(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ProgressBarVO progressBarVO,Integer nivelMontarDados,UsuarioVO usuarioVO) throws Exception {
				progressBarVO.setStatus("Consultando Documentação Matricula");
				List<DocumetacaoMatriculaVO> listaDocumetoMatricula =  getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentoMatriculaEntregue(assinarDocumentoEntregueVO, nivelMontarDados, usuarioVO);
				if(!Uteis.isAtributoPreenchido(listaDocumetoMatricula)) {
					progressBarVO.setStatus("Não foi encontrado Documentação Matrícula para Assinatura");
					throw new Exception("Não foi encontrado Documentação Matrícula para Assinatura");					
				}
			progressBarVO.setMaxValue(listaDocumetoMatricula.size()+1);
			Map<Integer, ConfiguracaoGEDVO> mapaConfigurcaoGed = getFacadeFactory().getConfiguracaoGEDFacade().consultarConfiguracaoGED(usuarioVO);			
			ConsistirException consistirException = new ConsistirException();
			int tamanhoLista = listaDocumetoMatricula.size();
			boolean processarEmParalelo =  false;
			if(processarEmParalelo) {
				ProcessarParalelismo.executar(0, listaDocumetoMatricula.size(), consistirException, new ProcessarParalelismo.Processo() {
					int cont = 1;
					@Override
					public void run(int i) {
						realizarProcessamentoDocumentoMatricula(listaDocumetoMatricula.get(i), progressBarVO, cont, tamanhoLista, mapaConfigurcaoGed, configuracaoGeralSistema, usuarioVO);
						cont ++;
					}
				});
			}else {
				int cont = 1;
				for(DocumetacaoMatriculaVO obj: listaDocumetoMatricula) {
					if(!progressBarVO.getForcarEncerramento().booleanValue() && progressBarVO.getAtivado().booleanValue()) {
						realizarProcessamentoDocumentoMatricula(obj, progressBarVO, cont, tamanhoLista, mapaConfigurcaoGed, configuracaoGeralSistema, usuarioVO);
					}else {
						break;
					}
					cont++;
				};
				progressBarVO.incrementar();
			}
		}
	
	private void realizarProcessamentoDocumentoMatricula(DocumetacaoMatriculaVO obj, ProgressBarVO progressBarVO, int cont, int tamanhoLista, Map<Integer, ConfiguracaoGEDVO> mapaConfigurcaoGed, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) {		
		ConfiguracaoGEDVO configuracaoGEDVO;
		try {
			progressBarVO.setStatus("Assinando Documento "+obj.getTipoDeDocumentoVO().getNome()+" da matrícula "+ obj.getMatricula()+" "+cont+"/"+tamanhoLista);			
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula());
			configuracaoGEDVO = mapaConfigurcaoGed.get(matricula.getUnidadeEnsino().getCodigo());
			if(configuracaoGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento().booleanValue()){
				obj.setFileAssinar(getFacadeFactory().getDocumetacaoMatriculaFacade().unificarFrenteVersoDocumentoMatricula(obj, configuracaoGeralSistema, usuarioVO));
				UsuarioVO usuario = selecionarUsuarioAssinatura(obj.getRespAprovacaoDocDep(), obj.getUsuario(), usuarioVO);
				getFacadeFactory().getDocumetacaoMatriculaFacade().realizarAssinaturaDocumentoJOB(obj, usuario);
			}else{
				getFacadeFactory().getDocumetacaoMatriculaFacade().atualizarStatusDocumentacaoMatricula(obj, "Não existe Configuração GED para esta unidade de ensino",Boolean.TRUE, usuarioVO);
			}
		} catch (Exception e) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().atualizarStatusDocumentacaoMatricula(obj, e.getMessage(), Boolean.TRUE,usuarioVO);
		}finally {			
			progressBarVO.incrementar();
		}
	} 
	
	private UsuarioVO selecionarUsuarioAssinatura(UsuarioVO usuarioAprovacaoDocumento,  UsuarioVO usuarioDocumentoMatricula, UsuarioVO usuarioVOLogado) throws Exception {
		boolean isExisteFuncionarioVinculado = false;
		if(Uteis.isAtributoPreenchido(usuarioAprovacaoDocumento.getPessoa())){
			isExisteFuncionarioVinculado = getFacadeFactory().getFuncionarioFacade().realizarVerificacaoPessoaVinculadaFuncionario(usuarioAprovacaoDocumento.getPessoa().getCodigo(), false, usuarioVOLogado);
			if(isExisteFuncionarioVinculado) {
				 return usuarioAprovacaoDocumento;
			}
		}
		if(Uteis.isAtributoPreenchido(usuarioDocumentoMatricula.getPessoa())){
			isExisteFuncionarioVinculado = getFacadeFactory().getFuncionarioFacade().realizarVerificacaoPessoaVinculadaFuncionario(usuarioDocumentoMatricula.getPessoa().getCodigo(), false, usuarioVOLogado);
			if(isExisteFuncionarioVinculado) {
				 return usuarioDocumentoMatricula;
			}
		}
		if(!isExisteFuncionarioVinculado){
			isExisteFuncionarioVinculado = getFacadeFactory().getFuncionarioFacade().realizarVerificacaoPessoaVinculadaFuncionario(usuarioVOLogado.getPessoa().getCodigo(), false, usuarioVOLogado);
			if(!isExisteFuncionarioVinculado) {
				throw new Exception("Dados Não Encontrados (Funcionário).");
			}
		}
		return usuarioVOLogado;

	}

	/**
	 * @param listaDocumentacao
	 * @param configuracaoGeralSistema
	 * @param usuario
	 * @throws Exception
	 */
	private void assinarDocumentacaoGED(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		progressBarVO.setStatus("Consultando Documentação GED");
		List<DocumentacaoGEDVO> listaDocumentacao =  getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarDocumentoGEDEntregue(assinarDocumentoEntregueVO, nivelMontarDados, usuarioVO);
		if(!Uteis.isAtributoPreenchido(listaDocumentacao)) {
			progressBarVO.setStatus("Não foi encontrado Documentação GED para Assinatura");
			Thread.sleep(5000);
			progressBarVO.setMaxValue(listaDocumentacao.size());
			Thread.sleep(500);
			return;
		}
		progressBarVO.setMaxValue(listaDocumentacao.size());
		Map<Integer, ConfiguracaoGEDVO> mapaConfigurcaoGed = getFacadeFactory().getConfiguracaoGEDFacade().consultarConfiguracaoGED(usuarioVO);
		ConsistirException consistirException = new ConsistirException();
		int tamanhoLista = listaDocumentacao.size();
		ProcessarParalelismo.executar(0, listaDocumentacao.size(), consistirException, new ProcessarParalelismo.Processo() {
			int cont = 1;
			@Override
			public void run(int i) {
				DocumentacaoGEDVO obj = listaDocumentacao.get(i);
				ConfiguracaoGEDVO configuracaoGEDVO;
				try {
					progressBarVO.setStatus("Assinando Documento GED da matrícula "+ obj.getMatricula().getMatricula() +" "+cont+"/"+tamanhoLista);
					
					MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula());
					configuracaoGEDVO = mapaConfigurcaoGed.get(matricula.getUnidadeEnsino().getCodigo());
					if(configuracaoGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento()){
						UsuarioVO usuario = Uteis.isAtributoPreenchido(obj.getUsuario().getPessoa().getCodigo()) ? obj.getUsuario() : usuarioVO;
						File fileAssinar = carregarArquivo(obj.getArquivo(),configuracaoGeralSistema);
						getFacadeFactory().getDocumentacaoGEDInterfaceFacade().realizarAssinaturaDocumentoGEDJOB(obj,matricula.getUnidadeEnsino(), configuracaoGEDVO, fileAssinar,  configuracaoGeralSistema,usuario);
					}else{
						getFacadeFactory().getDocumentacaoGEDInterfaceFacade().atualizarStatusDocumentacaoGED(obj, "Não existe Configuração GED para esta unidade de ensino",Boolean.TRUE, usuarioVO);
					}
				} catch (Exception e) {
					getFacadeFactory().getDocumentacaoGEDInterfaceFacade().atualizarStatusDocumentacaoGED(obj, e.getMessage(), Boolean.TRUE, usuarioVO);
				}finally {
					cont ++;
					progressBarVO.incrementar();
				}
			}

			private File carregarArquivo(ArquivoVO arquivo, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws IOException, ConsistirException {
				if(arquivo.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					String caminhoTemporario = getFacadeFactory().getArquivoHelper().criarCaminhoPastaAteDiretorio(arquivo, PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP.getValue() , configuracaoGeralSistema.getLocalUploadArquivoFixo());
					File file = new File(getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(arquivo, caminhoTemporario, configuracaoGeralSistema));
					return  file;
				}else {
					return new File(configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + arquivo.getPastaBaseArquivo() + File.separator +  arquivo.getNome());
				}
			}
		});
	}

}
