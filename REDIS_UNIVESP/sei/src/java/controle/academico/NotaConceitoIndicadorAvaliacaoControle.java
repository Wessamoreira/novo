package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.NotaConceitoIndicadorAvaliacaoVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("NotaConceitoIndicadorAvaliacaoControle")
@Lazy
@Scope("viewScope")
public class NotaConceitoIndicadorAvaliacaoControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4050472187943622894L;
	private NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO;
	private List<SelectItem> listaSelectItemOpcaoConsulta;

	public void persistir() {
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().persistir(getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		setNotaConceitoIndicadorAvaliacaoVO((NotaConceitoIndicadorAvaliacaoVO) getRequestMap().get("notaConceitoIndicadorAvaliacaoItens"));
		setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaConceitoIndicadorAvaliacaoForm.xhtml");
	}

	public void excluir() {
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().excluir(getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			setNotaConceitoIndicadorAvaliacaoVO(null);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativar() {
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().ativar(getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			setMensagemID("msg_dados_ativados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativar() {
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().inativar(getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getUrlImagemCriterioNotaConceito(){
		NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO = ((NotaConceitoIndicadorAvaliacaoVO) getRequestMap().get("notaConceitoIndicadorAvaliacaoItens"));
		 if(notaConceitoIndicadorAvaliacaoVO.getUrlImagem().trim().isEmpty() && !notaConceitoIndicadorAvaliacaoVO.getNomeArquivo().trim().isEmpty()){			 
			 notaConceitoIndicadorAvaliacaoVO.setUrlImagem(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()+"/"+notaConceitoIndicadorAvaliacaoVO.getPastaBaseArquivo().getValue()+"/"+notaConceitoIndicadorAvaliacaoVO.getNomeArquivo());
		 }
		 return notaConceitoIndicadorAvaliacaoVO.getUrlImagem();
	}	

	public String novo() {
		try {
			setNotaConceitoIndicadorAvaliacaoVO(null);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaConceitoIndicadorAvaliacaoForm.xhtml");
	}

	public String consultar() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaConceitoIndicadorAvaliacaoCons.xhtml");
	}
	
	public void uploadImagem(FileUploadEvent uploadEvent){
		try {	
			
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().uploadArquivo(uploadEvent, getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String irPaginaConsulta() {
		setControleConsulta(null);
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("notaConceitoIndicadorAvaliacaoCons.xhtml");
	}

	public NotaConceitoIndicadorAvaliacaoVO getNotaConceitoIndicadorAvaliacaoVO() {
		if (notaConceitoIndicadorAvaliacaoVO == null) {
			notaConceitoIndicadorAvaliacaoVO = new NotaConceitoIndicadorAvaliacaoVO();
		}
		return notaConceitoIndicadorAvaliacaoVO;
	}

	public void setNotaConceitoIndicadorAvaliacaoVO(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO) {
		this.notaConceitoIndicadorAvaliacaoVO = notaConceitoIndicadorAvaliacaoVO;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("descricao", "Descrição"));
			getControleConsulta().setCampoConsulta("descricao");
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

}
