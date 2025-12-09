package controle.estagio;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Controller("GrupoPessoaControle")
@Scope("viewScope")
@Lazy
public class GrupoPessoaControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2992145772205444986L;
	private static final String TELA_FORM = "grupoPessoaForm.xhtml";
	private static final String TELA_CONS = "grupoPessoaCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "grupoPessoaItens";
	private GrupoPessoaVO grupoPessoaVO;
	private GrupoPessoaItemVO grupoPessoaItemVO;
	private List<GrupoPessoaVO> listaGrupoPessoaImportador;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<PessoaVO> listaConsultaPessoa;
	private InputStream inputStream;
	

	public String novo() {
		removerObjetoMemoria(this);
		setGrupoPessoaVO(new GrupoPessoaVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			GrupoPessoaVO obj = (GrupoPessoaVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setGrupoPessoaVO(getFacadeFactory().getGrupoPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getGrupoPessoaFacade().persistir(getGrupoPessoaVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getGrupoPessoaFacade().excluir(getGrupoPessoaVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getGrupoPessoaFacade().consultar(getControleConsultaOtimizado(), getGrupoPessoaVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);

	}
	
	public void consultarPessoa() {
		try {
			setListaConsultaPessoa(new ArrayList<PessoaVO>());
			setListaConsultaPessoa(getFacadeFactory().getPessoaFacade().consultar(getCampoConsultaPessoa(), getValorConsultaPessoa(), TipoPessoa.NENHUM, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarPessoa() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			getGrupoPessoaItemVO().setPessoaVO(obj);
			setValorConsultaPessoa("");
			setCampoConsultaPessoa("");
			getListaConsultaPessoa().clear();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarPessoaSupervisor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			Uteis.checkState(getGrupoPessoaVO().getListaGrupoPessoaItemVO().stream().anyMatch(p-> p.getPessoaVO().getCodigo().equals(obj.getCodigo()) && p.getStatusAtivoInativoEnum().isAtivo()), "A pessoa informada não pode ser um Supervisor, pois o mesmo já é um Avaliador/Facilitador.");
			getGrupoPessoaVO().setPessoaSupervisorGrupo(obj);
			setValorConsultaPessoa("");
			setCampoConsultaPessoa("");
			getListaConsultaPessoa().clear();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void ativarGrupoPessoaItemVO() {
		GrupoPessoaItemVO obj = (GrupoPessoaItemVO) context().getExternalContext().getRequestMap().get("grupoPessoaItens");
		try {
			Uteis.checkState(Uteis.isAtributoPreenchido(getGrupoPessoaVO().getPessoaSupervisorGrupo().getCodigo()) && getGrupoPessoaVO().getPessoaSupervisorGrupo().getCodigo().equals(obj.getPessoaVO().getCodigo()), "O Avaliador/Facilitador não pode ser ativado, pois o mesmo já é o Supervisor.");
			obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
			getFacadeFactory().getGrupoPessoaItemFacade().atualizarCampoStatus(obj, obj.getStatusAtivoInativoEnum(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inativarGrupoPessoaItemVO() {
		try {			
			getFacadeFactory().getGrupoPessoaItemFacade().inativarGrupoPessoaItemVO(getGrupoPessoaItemVO(), getUsuarioLogadoClone());
			setGrupoPessoaItemVO(new GrupoPessoaItemVO());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void adicionarGrupoPessoaItemVO() {
		try {
			getFacadeFactory().getGrupoPessoaFacade().adicionarGrupoPessoaItemVO(getGrupoPessoaVO(), getGrupoPessoaItemVO(), getUsuarioLogadoClone());
			setGrupoPessoaItemVO(new GrupoPessoaItemVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarGrupoPessoaItemParaInativacao() {
		try {
			GrupoPessoaItemVO obj = (GrupoPessoaItemVO) context().getExternalContext().getRequestMap().get("grupoPessoaItens");
			setGrupoPessoaItemVO(obj);
			getGrupoPessoaItemVO().setQtdeEstagioObrigatorio(getFacadeFactory().getGrupoPessoaItemFacade().consultarQuantidadeEstagioObrigatorioPorGrupoPessoaItemVO(getGrupoPessoaItemVO(), getUsuarioLogadoClone()));
			getGrupoPessoaItemVO().setQtdeEstagioNaoObrigatorio(getFacadeFactory().getGrupoPessoaItemFacade().consultarQuantidadeEstagioNaoObrigatorioPorGrupoPessoaItemVO(getGrupoPessoaItemVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparPessoaSupervisor() {
		try {
			getGrupoPessoaVO().setPessoaSupervisorGrupo(new PessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparPessoa() {
		try {
			setGrupoPessoaItemVO(new GrupoPessoaItemVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparArquivoImportado() {
		try {
			setInputStream(null);
			getListaGrupoPessoaImportador().clear();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void upLoadArquivoImportado(FileUploadEvent uploadEvent) {
		try {
			setInputStream(uploadEvent.getUploadedFile().getInputStream());
			getListaGrupoPessoaImportador().clear();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void processarUpLoadArquivoImportado() {
		try {
			getListaGrupoPessoaImportador().clear();
			getFacadeFactory().getGrupoPessoaFacade().upLoadArquivoImportado(getInputStream(), getListaGrupoPessoaImportador(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			
		}
	}
	
	public void realizarImportacaoGrupoPessoa() {
		try {
			getFacadeFactory().getGrupoPessoaFacade().realizarImportacaoGrupoPessoa(getListaGrupoPessoaImportador(), getUsuarioLogadoClone());
			getListaGrupoPessoaImportador().clear();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} 
	}
	
	public void realizarExportacaoLayouGrupoPessoa() {
		try {
			File arquivo = getFacadeFactory().getGrupoPessoaFacade().realizarExportacaoLayouGrupoPessoa(getGrupoPessoaVO(), getUsuarioLogadoClone());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public GrupoPessoaVO getGrupoPessoaVO() {
		if (grupoPessoaVO == null) {
			grupoPessoaVO = new GrupoPessoaVO();
		}
		return grupoPessoaVO;
	}

	public void setGrupoPessoaVO(GrupoPessoaVO grupoPessoaVO) {
		this.grupoPessoaVO = grupoPessoaVO;
	}

	public GrupoPessoaItemVO getGrupoPessoaItemVO() {
		if (grupoPessoaItemVO == null) {
			grupoPessoaItemVO = new GrupoPessoaItemVO();
		}
		return grupoPessoaItemVO;
	}

	public void setGrupoPessoaItemVO(GrupoPessoaItemVO grupoPessoaItemVO) {
		this.grupoPessoaItemVO = grupoPessoaItemVO;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public List<GrupoPessoaVO> getListaGrupoPessoaImportador() {
		if (listaGrupoPessoaImportador == null) {
			listaGrupoPessoaImportador = new ArrayList<>();
		}
		return listaGrupoPessoaImportador;
	}

	public void setListaGrupoPessoaImportador(List<GrupoPessoaVO> listaGrupoPessoaImportador) {
		this.listaGrupoPessoaImportador = listaGrupoPessoaImportador;
	}

	public InputStream getInputStream() {		
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}	
	
	
	

}
