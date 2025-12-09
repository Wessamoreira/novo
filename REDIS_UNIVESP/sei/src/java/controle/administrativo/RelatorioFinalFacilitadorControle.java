package controle.administrativo;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.academico.enumeradores.SituacaoRelatorioFacilitadorEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("RelatorioFinalFacilitadorControle")
@Scope("viewScope")
public class RelatorioFinalFacilitadorControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO;
	private CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO;
	private Boolean vizualizarRelatorioFacilitador;
	

	public RelatorioFinalFacilitadorControle(){
		super();
	}

	@PostConstruct
	public void inicializarQuestionarioAluno() {
		try { 
			setVizualizarRelatorioFacilitador(false);
			String situacao = "Em aberto";
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getSessionMap().get("matriculaPeriodoTurmaDisciplina");
			setCalendarioRelatorioFinalFacilitadorVO(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultarPorRelatorioFacilitadoresRegistroUnico(
				obj.getDisciplina().getCodigo(),situacao, obj.getAno(), obj.getSemestre(), "", false, getUsuarioLogado()));
			getRelatorioFinalFacilitadorVO().setMatriculaperiodoturmadisciplinaVO(obj); 
			getRelatorioFinalFacilitadorVO().setMes(getCalendarioRelatorioFinalFacilitadorVO().getMes());
			getRelatorioFinalFacilitadorVO().setAno(getCalendarioRelatorioFinalFacilitadorVO().getAno());
			getRelatorioFinalFacilitadorVO().setSemestre(getCalendarioRelatorioFinalFacilitadorVO().getSemestre());
			if(Uteis.isAtributoPreenchido(getCalendarioRelatorioFinalFacilitadorVO())) {
				QuestionarioVO questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getCalendarioRelatorioFinalFacilitadorVO().getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuario());
				getRelatorioFinalFacilitadorVO().setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, getUsuario()));
				getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().setEscopo(EscopoPerguntaEnum.RELATORIO_FACILITADOR);
				getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.DEFERIDO);
			}
			getRelatorioFinalFacilitadorVO().setSupervisor(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarSupervisorPorFacilitador(obj.getMatricula(), obj.getAno(), obj.getSemestre(), getUsuario()));
			if(!Uteis.isAtributoPreenchido(getRelatorioFinalFacilitadorVO().getSupervisor())){
				throw new Exception("Não foi encontrado um supervisor na sala de aula da graduação");
			}
			getControleConsultaOtimizado().setPage(1);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultar();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String vizualizarRelatorio() {
		try {
			editar();
			setVizualizarRelatorioFacilitador(true);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("relatorioFinalFacilitadorForm.xhtml");
	}
	
	public String editar() {
		try {
			setVizualizarRelatorioFacilitador(false);
			RelatorioFinalFacilitadorVO obj = (RelatorioFinalFacilitadorVO) getRequestMap().get("relatorioFinalFacilitadorItens");
			setRelatorioFinalFacilitadorVO(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarPorChamvePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario()));
			getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().preencherDadosRelatorioFacilitadorQuestionarioRespostaOrigem(getRelatorioFinalFacilitadorVO(), getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().getCodigo(), getUsuarioLogado());
			limparMensagem();
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("relatorioFinalFacilitadorForm.xhtml");
	}
	
	public String gravar() {
		try {
			getRelatorioFinalFacilitadorVO().setSituacao(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR);
			getRelatorioFinalFacilitadorVO().setDataEnvioAnalise(new Date());	
			getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().persistir(getRelatorioFinalFacilitadorVO(), getUsuarioLogado());
			Integer codigoQuestionario = 0;
			codigoQuestionario = getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().getCodigo();
			getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioOrigem(codigoQuestionario, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
			}
			montarDadosEnvioEmail();
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSupervisorRelatorioFacilitador(getRelatorioFinalFacilitadorVO(), getUsuarioLogado());
			removerControleMemoriaFlashTela("VisaoAlunoControle");
			executarMetodoControle("VisaoAlunoControle", "realizarNavegacaoTelaInicial");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("relatorioFinalFacilitadorForm.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoAluno.xhtml");
	}

	private void montarDadosEnvioEmail() throws Exception {
		RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO = new RelatorioFinalFacilitadorVO();
		relatorioFinalFacilitadorVO =getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarDadosEnvioEmail(getRelatorioFinalFacilitadorVO(), getUsuarioLogado());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCodigo(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getCodigo());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getEmail());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setRegistroAcademico(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getRegistroAcademico());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setNome(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getNome());
		getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().setNome(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().getNome());
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultar(getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo(), getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarTotalRegistro(getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo(), getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String inicializarConsultar() {
		try {
			getControleConsultaOtimizado().setPage(1);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultar();
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("relatorioFinalFacilitadorCons.xhtml");
	}
	
	
	public void paginarConsulta(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}
	
	public void validarDadosListaResposta(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO, RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO) {		
		respostaPerguntaRespostaOrigemVO.setSelecionado(!respostaPerguntaRespostaOrigemVO.getSelecionado());
		if(perguntaRespostaOrigemPrincipalVO.getPerguntaVO().getTipoRespostaSimplesEscolha()) {
			for(RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO2: perguntaRespostaOrigemPrincipalVO.getRespostaPerguntaRespostaOrigemVOs()) {
				if(!respostaPerguntaRespostaOrigemVO2.getRespostaPerguntaVO().getCodigo().equals(respostaPerguntaRespostaOrigemVO.getRespostaPerguntaVO().getCodigo())) {
					respostaPerguntaRespostaOrigemVO2.setSelecionado(false);
				}
			}
		}
		else if(perguntaRespostaOrigemPrincipalVO.getPerguntaVO().getTipoRespostaListaCampos()) {
			if(Uteis.isAtributoPreenchido(perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs())) {
				for(PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs()) {
					if(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().getPerguntaVO().getTipoRespostaSimplesEscolha()) {
						if(Uteis.isAtributoPreenchido(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().getRespostaPerguntaRespostaOrigemVOs())) {
							for(RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO2: perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().getRespostaPerguntaRespostaOrigemVOs()) {
								if(!respostaPerguntaRespostaOrigemVO2.getRespostaPerguntaVO().getCodigo().equals(respostaPerguntaRespostaOrigemVO.getRespostaPerguntaVO().getCodigo())) {
									respostaPerguntaRespostaOrigemVO2.setSelecionado(false);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void uploadArquivoRespostaOrigem(FileUploadEvent uploadEvent) {
		try {
			PerguntaRespostaOrigemVO obj = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			obj.getArquivoRespostaVO().setOrigem(OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM.getValor());
			obj.getArquivoRespostaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			obj.getArquivoRespostaVO().setExtensao(ArquivoHelper.getExtensaoArquivo(uploadEvent.getUploadedFile().getName()));
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, obj.getArquivoRespostaVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP, getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void apresentarAnexoArquivoPergunta() {
		try {
			PerguntaRespostaOrigemVO obj = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			if(obj.isApresentarListaArquivoVOs()) {
				obj.setApresentarListaArquivoVOs(false);	
			}else {
				obj.setApresentarListaArquivoVOs(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void uploadAnexoArquivoPergunta(FileUploadEvent uploadEvent) {
		try {
			PerguntaRespostaOrigemVO obj = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			ArquivoVO arquivo = new ArquivoVO();
			arquivo.setOrigem(OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM_ANEXO.getValor());
			arquivo.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			arquivo.setExtensao(ArquivoHelper.getExtensaoArquivo(uploadEvent.getUploadedFile().getName()));
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, arquivo, getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP, getUsuarioLogado());
			String urlExternoDownloadArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo().getUrlExternoDownloadArquivo();
			arquivo.montarCampoDescricao(urlExternoDownloadArquivo);
			obj.getListaArquivoVOs().add(arquivo);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void removerAnexoArquivoPergunta() {
		try {
			PerguntaRespostaOrigemVO obj = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			ArquivoVO arquivo = (ArquivoVO) getRequestMap().get("imagemItens");
			obj.getListaArquivoVOs().removeIf(p-> p.getNome().equals(arquivo.getNome()));
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getUrlDonloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	
	public RelatorioFinalFacilitadorVO getRelatorioFinalFacilitadorVO() {
		if(relatorioFinalFacilitadorVO == null) {
			relatorioFinalFacilitadorVO = new RelatorioFinalFacilitadorVO();
		}
		return relatorioFinalFacilitadorVO;
	}

	public void setRelatorioFinalFacilitadorVO(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO) {
		this.relatorioFinalFacilitadorVO = relatorioFinalFacilitadorVO;
	}

	public CalendarioRelatorioFinalFacilitadorVO getCalendarioRelatorioFinalFacilitadorVO() {
		return calendarioRelatorioFinalFacilitadorVO;
	}

	public void setCalendarioRelatorioFinalFacilitadorVO(
			CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO) {
		this.calendarioRelatorioFinalFacilitadorVO = calendarioRelatorioFinalFacilitadorVO;
	}

	public Boolean getVizualizarRelatorioFacilitador() {
		return vizualizarRelatorioFacilitador;
	}

	public void setVizualizarRelatorioFacilitador(Boolean vizualizarRelatorioFacilitador) {
		this.vizualizarRelatorioFacilitador = vizualizarRelatorioFacilitador;
	}
}
