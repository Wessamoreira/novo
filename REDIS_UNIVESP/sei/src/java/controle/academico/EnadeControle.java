package controle.academico;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas enadeForm.jsp enadeCons.jsp) com
 * as funcionalidades da classe <code>Enade</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Enade
 * @see EnadeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("EnadeControle")
@Scope("viewScope")
@Lazy
public class EnadeControle extends SuperControleRelatorio implements Serializable {

	private EnadeVO enadeVO;
	private Date valorConsultaData;
	private TextoEnadeVO textoEnadeVO;
	private EnadeCursoVO enadeCursoVO;
	private List<SelectItem> listaSelectItemTipoTextoEnade;
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private ArquivoVO arquivoVO;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtIngressantes;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtConcluintes;
	public List<MapaConvocacaoEnadeVO> listaSituacaoEnade;
	public Boolean validarExistenciaMapaConvocacaoEnade;
	public List<MapaConvocacaoEnadeVO> listaExportarMapaConvocacaoEnadeVOs;
	private String onCompleteDownloadMapaconvocacaoEnade;
	public EnadeControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Enade</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setEnadeVO(new EnadeVO());
		try {
			getEnadeVO().setTextoEnadeVOs(getFacadeFactory().getTextoEnadeFacade().consultarUltimosTextosCadastrados());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Enade</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 * @throws Exception 
	 */
	public String editar() throws Exception {
		EnadeVO obj = (EnadeVO) context().getExternalContext().getRequestMap().get("enadeItens");
		obj.setNovoObj(Boolean.FALSE);
		setEnadeVO(obj);
		setListaExportarMapaConvocacaoEnadeVOs(getFacadeFactory().getEnadeFacade().consultarPorCodigoEnade("enade", getEnadeVO().getCodigo(), getUsuarioLogado(), NivelMontarDados.TODOS));
		if (!obj.getNovoObj() && Uteis.isAtributoPreenchido(getListaExportarMapaConvocacaoEnadeVOs())) {
			setValidarExistenciaMapaConvocacaoEnade(Boolean.TRUE);
		} else {
			setValidarExistenciaMapaConvocacaoEnade(Boolean.FALSE);
		}
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Enade</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (enadeVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getEnadeFacade().incluir(enadeVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getEnadeFacade().alterar(enadeVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * EnadeCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List objs = new ArrayList(0);
			if (getControleConsultaOtimizado().getCampoConsulta().equals("codigo")) {
				if (getControleConsultaOtimizado().getValorConsulta().equals("")) {
					getControleConsultaOtimizado().setValorConsulta("0");
				}
				if (getControleConsultaOtimizado().getValorConsulta().trim() != null || !getControleConsultaOtimizado().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsultaOtimizado().getValorConsulta().trim());
				}
				int valorInt = Integer.parseInt(getControleConsultaOtimizado().getValorConsulta());
				getControleConsultaOtimizado().setValorConsulta(new Integer(valorInt) + "");
			} else if (getControleConsultaOtimizado().getCampoConsulta().equals("dataPublicacaoPortariaDOU")) {
				getControleConsultaOtimizado().setDataIni(Uteis.getDateTime(getControleConsultaOtimizado().getDataIni(), 0, 0, 0));
				getControleConsultaOtimizado().setDataFim(Uteis.getDateTime(getControleConsultaOtimizado().getDataIni(), 23, 59, 59));
			}
			objs = getFacadeFactory().getEnadeFacade().consultarEnade(getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeCons.xhtml");
		}
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>EnadeVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getEnadeFacade().excluir(enadeVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("enadeForm.xhtml");
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("tituloEnade", "Título Enade"));
		itens.add(new SelectItem("dataPublicacaoPortariaDOU", "Data Publicação no Diário Oficial"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("enadeCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		enadeVO = null;

	}

	public EnadeVO getEnadeVO() {
		if (enadeVO == null) {
			enadeVO = new EnadeVO();
		}
		return enadeVO;
	}

	public void setEnadeVO(EnadeVO enadeVO) {
		this.enadeVO = enadeVO;
	}

	/**
	 * @return the valorConsultaData
	 */
	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	/**
	 * @param valorConsultaData
	 *            the valorConsultaData to set
	 */
	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Boolean getApresentarCampoData() {
		if (getControleConsultaOtimizado().getCampoConsulta().equals("dataPublicacaoPortariaDOU")) {
			return true;
		}
		return false;
	}

	public void adicionarTextoEnadeVOs() {
		try {
			getFacadeFactory().getEnadeFacade().adicionarTextoEnadeVOs(getEnadeVO(), getTextoEnadeVO());
			setTextoEnadeVO(new TextoEnadeVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirTextoEnadeVOs() {
		try {
			getFacadeFactory().getEnadeFacade().excluirTextoEnadeVOs(getEnadeVO(), (TextoEnadeVO) getRequestMap().get("textoEnadeItens"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarEnadeCursoVOs() {
		try {
			getFacadeFactory().getEnadeFacade().adicionarEnadeCursoVOs(getEnadeVO(), getEnadeCursoVO());
			setEnadeCursoVO(new EnadeCursoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirEnadeCursoVOs() {
		try {
			getFacadeFactory().getEnadeFacade().excluirEnadeCursoVOs(getEnadeVO(), (EnadeCursoVO) getRequestMap().get("enadeCursoItens"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarTextoEnadeVOs() {
		try {
			setTextoEnadeVO(new TextoEnadeVO());
			setTextoEnadeVO((TextoEnadeVO) getRequestMap().get("textoEnadeItens"));
			getTextoEnadeVO().setTextoEdicao(getTextoEnadeVO().getTexto());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarEnadeCurso() {
		try {
			setEnadeCursoVO(new EnadeCursoVO());
			setEnadeCursoVO((EnadeCursoVO) getRequestMap().get("enadeCursoItens"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getEnadeCursoVO().setCursoVO(obj);
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			getEnadeCursoVO().setCursoVO(null);
		} catch (Exception e) {
		}
	}
	
	List<SelectItem> tipoConsultaComboCurso;
	
    public List getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }


	public TextoEnadeVO getTextoEnadeVO() {
		if (textoEnadeVO == null) {
			textoEnadeVO = new TextoEnadeVO();
		}
		return textoEnadeVO;
	}

	public void setTextoEnadeVO(TextoEnadeVO textoEnadeVO) {
		this.textoEnadeVO = textoEnadeVO;
	}

	public List<SelectItem> getListaSelectItemTipoTextoEnade() {
		if (listaSelectItemTipoTextoEnade == null) {
			listaSelectItemTipoTextoEnade = new ArrayList<SelectItem>(0);
			for (TipoTextoEnadeEnum textoEnadeEnum : TipoTextoEnadeEnum.values()) {
				listaSelectItemTipoTextoEnade.add(new SelectItem(textoEnadeEnum, textoEnadeEnum.getValorApresentar()));
			}
		}
		return listaSelectItemTipoTextoEnade;
	}

	public void setListaSelectItemTipoTextoEnade(List<SelectItem> listaSelectItemTipoTextoEnade) {
		this.listaSelectItemTipoTextoEnade = listaSelectItemTipoTextoEnade;
	}

	public EnadeCursoVO getEnadeCursoVO() {
		if (enadeCursoVO == null) {
			enadeCursoVO = new EnadeCursoVO();
		}
		return enadeCursoVO;
	}

	public void setEnadeCursoVO(EnadeCursoVO enadeCursoVO) {
		this.enadeCursoVO = enadeCursoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	
	public void obterCaminhoServidorDownloadArquivoAlunoIngressante() {
		try {
			MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO = (MapaConvocacaoEnadeVO) context().getExternalContext().getRequestMap().get("mapaConvocacaoEnadeVOItens");
			setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(getArquivoVO())) {
				fazerDownloadArquivo(getArquivoVO());
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	
	
	public void obterCaminhoServidorDownloadArquivoAlunoConcluinte() {
		try {
			MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO = (MapaConvocacaoEnadeVO) context().getExternalContext().getRequestMap().get("mapaConvocacaoEnadeVOItens");
			setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(getArquivoVO())) {
				fazerDownloadArquivo(getArquivoVO());
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	private void fazerDownloadArquivo(ArquivoVO arquivoVO) throws Exception {
		context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		context().getExternalContext().dispatch("/DownloadSV");
		FacesContext.getCurrentInstance().responseComplete();
	}	
	
	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}
	
	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}	
	
	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVORelTxtIngressantes() {
		if (mapaConvocacaoEnadeVORelTxtIngressantes == null) {
			mapaConvocacaoEnadeVORelTxtIngressantes = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVORelTxtIngressantes;
	}
	
	public void setMapaConvocacaoEnadeVORelTxtIngressantes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtIngressantes) {
		this.mapaConvocacaoEnadeVORelTxtIngressantes = mapaConvocacaoEnadeVORelTxtIngressantes;
	}
	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVORelTxtConcluintes() {
		if (mapaConvocacaoEnadeVORelTxtConcluintes == null) {
			mapaConvocacaoEnadeVORelTxtConcluintes = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVORelTxtConcluintes;
	}
	
	public void setMapaConvocacaoEnadeVORelTxtConcluintes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtConcluintes) {
		this.mapaConvocacaoEnadeVORelTxtConcluintes = mapaConvocacaoEnadeVORelTxtConcluintes;
	}
	
	
	public Boolean getValidarExistenciaMapaConvocacaoEnade() {
		if(validarExistenciaMapaConvocacaoEnade == null) {
			validarExistenciaMapaConvocacaoEnade = false;
		} 
		return validarExistenciaMapaConvocacaoEnade;
	}
	
	public void setValidarExistenciaMapaConvocacaoEnade(Boolean validarExistenciaMapaConvocacaoEnade) {
		this.validarExistenciaMapaConvocacaoEnade = validarExistenciaMapaConvocacaoEnade;
	}

	public List<MapaConvocacaoEnadeVO> getListaExportarMapaConvocacaoEnadeVOs() {
		if (listaExportarMapaConvocacaoEnadeVOs == null) {
			listaExportarMapaConvocacaoEnadeVOs = new ArrayList<MapaConvocacaoEnadeVO>();
		}
		return listaExportarMapaConvocacaoEnadeVOs;
	}

	public void setListaExportarMapaConvocacaoEnadeVOs(List<MapaConvocacaoEnadeVO> listaExportarMapaConvocacaoEnadeVOs) {
		this.listaExportarMapaConvocacaoEnadeVOs = listaExportarMapaConvocacaoEnadeVOs;
	}

	//ROTINA RESPONSAVEL POR COMPACTAR OS ARQUIVOS
	public String obterCaminhoServidorDownloadArquivoAlunoIngressanteConcluinte() {
		try {
			List<File> files = new ArrayList<File>();
			List<MapaConvocacaoEnadeVO> mapaConvocacaoEnadeVOs  = getListaExportarMapaConvocacaoEnadeVOs();
			for(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO : mapaConvocacaoEnadeVOs) {
				ArquivoVO arquivoIngressanteVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado());
				ArquivoVO arquivoConcluinteVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado());
				File fileIngressante = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + arquivoIngressanteVO.getPastaBaseArquivo() + File.separator + arquivoIngressanteVO.getNome());
				files.add(fileIngressante);
				File fileConcluinte = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + arquivoConcluinteVO.getPastaBaseArquivo() + File.separator + arquivoConcluinteVO.getNome());
				files.add(fileConcluinte);
				if(files.size() >= 1) {
				setCaminhoRelatorio("");
					String nomeNovoArquivo = "arquivoIngressantesConcluintes"+new Date().getTime()+".zip";
					File[] filesArray = new File[files.size()];
					getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File( getCaminhoPastaWeb() +  "relatorio" + File.separator + nomeNovoArquivo));
					setCaminhoRelatorio(nomeNovoArquivo);				
					setFazerDownload(true);				
				}else {
					setFazerDownload(false);	
				}
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}
	
	public String getOnCompleteDownloadMapaconvocacaoEnade() {
		if(onCompleteDownloadMapaconvocacaoEnade == null ) {
			onCompleteDownloadMapaconvocacaoEnade ="";
		}
		return onCompleteDownloadMapaconvocacaoEnade;
	}

	public void setOnCompleteDownloadMapaconvocacaoEnade(String onCompleteDownloadMapaconvocacaoEnade) {
		this.onCompleteDownloadMapaconvocacaoEnade = onCompleteDownloadMapaconvocacaoEnade;
	}
	
	
//ROTINA RESPONSAVEL POR FAZER O DOWNLOAD DOS ARQUIVOS COMPACTADOS REFERENTE AO ENADE QUE FORAM GERADOS NO MAPA CONVOCACAO ENADE. 	
	public String getDownloadMapaconvocacaoEnade() {
		if (getFazerDownload()) {
			String download = super.getDownload();
			setOnCompleteDownloadMapaconvocacaoEnade(download);
			return download;
		}
		try {
			realizarDownloadArquivo(getArquivoVO());
			return "";
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return "";
	}
}
