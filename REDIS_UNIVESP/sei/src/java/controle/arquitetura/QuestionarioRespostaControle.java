package controle.arquitetura;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;

public class QuestionarioRespostaControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1998816444349647740L;
	
	public void avancarPaginadorQuestionario(QuestionarioRespostaOrigemVO obj, Integer pageAtual) {
		try {			
			setPage(pageAtual + 1);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void voltarPaginadorQuestionario(QuestionarioRespostaOrigemVO obj, Integer pageAtual) {
		try {
			setPage(pageAtual - 1);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void validarDados(List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs) throws ConsistirException {
		for (PerguntaRespostaOrigemVO perguntaRespostaOrigemVO : perguntaRespostaOrigemVOs) {			
			getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().validarDadosPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
		}
	}

	public void validarDadosListaResposta(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO, RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO) {		
//		PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
//		RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO = (RespostaPerguntaRespostaOrigemVO) getRequestMap().get("respostaItens");
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

	public void adicionarListaPerguntaItemRespostaOrigemVO() {
		try {
			PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().adicionarListaPerguntaItemRespostaOrigemVO(perguntaRespostaOrigemPrincipalVO);
			setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarListaPerguntaItemRespostaOrigemVO() {
		try {
			PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO = (PerguntaRespostaOrigemVO) getRequestMap().get("perguntaItens");
			List<PerguntaItemRespostaOrigemVO> perguntaItemRespostaOrigemVOs = (List<PerguntaItemRespostaOrigemVO>) getRequestMap().get("perguntaAdicionadaItem");
			perguntaRespostaOrigemPrincipalVO.setPerguntaItemRespostaOrigemVOs(perguntaItemRespostaOrigemVOs);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerListaPerguntaItemRespostaOrigemVO(int index) throws Exception {
		try {
			PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO = (PerguntaRespostaOrigemVO) getRequestMap()
					.get("perguntaItens");
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade()
					.removerListaPerguntaItemRespostaOrigemVO(perguntaRespostaOrigemPrincipalVO, index);

			setMensagemID("msg_dados_removidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void uploadArquivoRespostaOrigem( FileUploadEvent uploadEvent) {
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
	
	public void uploadAnexoArquivoPergunta( FileUploadEvent uploadEvent) {
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
	
	
}
