package controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.controle.processosel.ProcessarResultadoProcessoSeletivoRelControle;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ProcessarResultadoProcessoSeletivoRelVO;

@Controller("ProcessarResultadoProcessoSeletivoControle")
@Scope("viewScope")
public class ProcessarResultadoProcessoSeletivoControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected ProcessarResultadoProcessoSeletivoRelControle processarResultadoProcessoSeletivoRelControle;
	private ResultadoProcessamentoArquivoRespostaVO resultadoProcessamentoArquivoResposta;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private ProcSeletivoVO procSeletivoVO;
	private String campoConsultaProcSeletivo;
	private String valorConsultaProcSeletivo;
	private List<ProcSeletivoVO> listaConsultaProcSeletivo;
	private List<SelectItem> listaSelectItemProcSeletivoDataProva;
	private List<SelectItem> listaSelectItemSala;
	private Integer sala;
	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	private DisciplinasProcSeletivoVO disciplinasProcSeletivoVO;
	private List<SelectItem> listaSelectItemDisciplinaProcSeletivo;
	
    
    public void processarArquivoResposta(FileUploadEvent event){
        try{
            setResultadoProcessamentoArquivoResposta(null);
            setResultadoProcessamentoArquivoResposta(getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarProcessamentoArquivoResposta(event, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getUsuarioLogado()));
            if(getResultadoProcessamentoArquivoResposta().getResultadoProcessoSeletivoVOs().isEmpty()){
				getListaSelectItemDisciplinaProcSeletivo().clear();
			}else{
				montarListaSelectItemLancamentoNotaPorDisciplina();
			}
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        }catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }        
    }
    
    public void imprimirPDF() {
		try {
			String titulo = null;
			String design = null;
			List<ProcessarResultadoProcessoSeletivoRelVO> listaObjetos = null;
			try {
				titulo = "PROCESSAR RESULTADO PROCESSO SELETIVO";
				design = getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getDesignIReportRelatorio();
				listaObjetos = getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().criarObjeto(getResultadoProcessamentoArquivoResposta().getInscricaoRespostaNaoProcessadaVOs(), getResultadoProcessamentoArquivoResposta().getResultadoProcessoSeletivoVOs());
				if (!listaObjetos.isEmpty()) {
					getSuperParametroRelVO().setTituloRelatorio(titulo);
					getSuperParametroRelVO().setNomeDesignIreport(design);
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
					getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
					getSuperParametroRelVO().setListaObjetos(listaObjetos);
					if (!getProcSeletivoVO().getCodigo().equals(0)) {
						getSuperParametroRelVO().adicionarParametro("descricaoProcSeletivo", getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(procSeletivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getDescricao());
					}
					if (!getItemProcSeletivoDataProvaVO().getCodigo().equals(0)) {
						getSuperParametroRelVO().adicionarParametro("dataProva",getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()).getDataProva_Apresentar());
					}
					if (!getSala().equals(0)) {
						getSuperParametroRelVO().adicionarParametro("sala", getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala()).getSala());
					} else {
						getSuperParametroRelVO().adicionarParametro("sala", "Todas as Sala");
					}					
					setMensagemID("msg_relatorio_ok");
					realizarImpressaoRelatorio();
				} else {
					setMensagemID("msg_relatorio_sem_dados");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				Uteis.liberarListaMemoria(listaObjetos);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaProcSeletivoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataInicioInternet", "Data Início Internet"));
		itens.add(new SelectItem("dataFimInternet", "Data Fim Internet"));
		// itens.add(new SelectItem("valorInscricao", "Valor Inscrição"));
		// itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}

	public void limparDadosProcSeletivo() {
		setProcSeletivoVO(new ProcSeletivoVO());
		setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
		getListaSelectItemProcSeletivoDataProva().clear();
		getListaSelectItemSala().clear();
	}

	public void limparCamposConsultaProcSeletivo() {
		try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setValorConsultaProcSeletivo(null);
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProcSeletiVOs() throws Exception {
		try {
			super.consultar();
			List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicioInternet")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFimInternet")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProcSeletivo() {
		try {
			ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
			setProcSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaSelectItemDataProva();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemDataProva() throws Exception {
		List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		getListaSelectItemProcSeletivoDataProva().clear();
		getListaSelectItemProcSeletivoDataProva().clear();
		setSala(0);
		getListaSelectItemProcSeletivoDataProva().add(new SelectItem(0, ""));
		for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
			getListaSelectItemProcSeletivoDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
		}

	}

	public void montarListaSelectItemSalaProcSeletivo() throws Exception {
		setSala(0);
		List<SalaLocalAulaVO> resultadoConsulta = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivoEDataAula(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo());
		if (!resultadoConsulta.isEmpty()) {
			getListaSelectItemSala().clear();
			getListaSelectItemSala().add(new SelectItem(0, "Todas as Sala"));
			for (SalaLocalAulaVO sala : resultadoConsulta) {
				getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getLocalAula().getLocal() + " - " + sala.getSala()));
			}
		} else {
			getListaSelectItemSala().clear();
			getListaSelectItemSala().add(new SelectItem(0, "Sem Sala Cadastrada"));
		}
	}

	public ResultadoProcessamentoArquivoRespostaVO getResultadoProcessamentoArquivoResposta() {
		if (resultadoProcessamentoArquivoResposta == null) {
			resultadoProcessamentoArquivoResposta = new ResultadoProcessamentoArquivoRespostaVO();
		}
		return resultadoProcessamentoArquivoResposta;
	}

	public void setResultadoProcessamentoArquivoResposta(ResultadoProcessamentoArquivoRespostaVO resultadoProcessamentoArquivoResposta) {
		this.resultadoProcessamentoArquivoResposta = resultadoProcessamentoArquivoResposta;
	}

	public String getCampoConsultaProcSeletivo() {
		if (campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
		if (listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList<ProcSeletivoVO>(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public List<SelectItem> getListaSelectItemProcSeletivoDataProva() {
		if (listaSelectItemProcSeletivoDataProva == null) {
			listaSelectItemProcSeletivoDataProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProcSeletivoDataProva;
	}

	public void setListaSelectItemProcSeletivoDataProva(List<SelectItem> listaSelectItemProcSeletivoDataProva) {
		this.listaSelectItemProcSeletivoDataProva = listaSelectItemProcSeletivoDataProva;
	}

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	public List<SelectItem> getListaSelectItemSala() {
		if (listaSelectItemSala == null) {
			listaSelectItemSala = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSala;
	}

	public void setListaSelectItemSala(List<SelectItem> listaSelectItemSala) {
		this.listaSelectItemSala = listaSelectItemSala;
	}

	public Integer getSala() {
		if (sala == null) {
			sala = 0;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}

	public ProcessarResultadoProcessoSeletivoRelControle getProcessarResultadoProcessoSeletivoRelControle() {
		return processarResultadoProcessoSeletivoRelControle;
	}

	public void setProcessarResultadoProcessoSeletivoRelControle(ProcessarResultadoProcessoSeletivoRelControle processarResultadoProcessoSeletivoRelControle) {
		this.processarResultadoProcessoSeletivoRelControle = processarResultadoProcessoSeletivoRelControle;
	}
	
	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
		if(resultadoProcessoSeletivoVO == null){
			resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		}
		return resultadoProcessoSeletivoVO;
	}

	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
	}
	
	public void gravarLancamentoNotaPorDisciplinaIndividual(){
		try{
			limparMensagem();
			getFacadeFactory().getResultadoProcessoSeletivoFacade().persistir(getResultadoProcessoSeletivoVO(), getUsuarioLogado(), true);		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	/**
	 * Responsável por executar o cálculo da média, seja ela do tipo Gabarito, seja do tipo Prova, ao ser lançado a nota individualmente.
	 * 
	 * @author Wellington - 16 de mar de 2016
	 */
	public void calcularMediaNotaDisciplinaLancadaManualmente() {
		try {			
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarCalculoMediaNotaLancadaManualmente(getResultadoProcessoSeletivoVO(), getUsuarioLogado());						
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void editarCalcularMediaNotaDisciplinaLancadaManualmente() {
		try {
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarCalculoMediaNotaLancadaManualmente((ResultadoProcessoSeletivoVO)getRequestMap().get("resultadoItens"), getUsuarioLogado());						
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(){
		try{
			limparMensagem();
			if(Uteis.isAtributoPreenchido(getDisciplinasProcSeletivoVO().getCodigo())){				
				setDisciplinasProcSeletivoVO(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(getDisciplinasProcSeletivoVO().getCodigo(), getUsuarioLogado()));			
			}
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarSelecaoLancamentoNotaResultadoProcessoSeletivoPorDisciplina(getResultadoProcessamentoArquivoResposta().getResultadoProcessoSeletivoVOs(), getDisciplinasProcSeletivoVO(), getUsuarioLogado());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public DisciplinasProcSeletivoVO getDisciplinasProcSeletivoVO() {
		if(disciplinasProcSeletivoVO == null){
			disciplinasProcSeletivoVO = new DisciplinasProcSeletivoVO();
		}
		return disciplinasProcSeletivoVO;
	}

	public void setDisciplinasProcSeletivoVO(DisciplinasProcSeletivoVO disciplinasProcSeletivoVO) {
		this.disciplinasProcSeletivoVO = disciplinasProcSeletivoVO;
	}
	
	public String montarListaSelectItemLancamentoNotaPorDisciplina(){
		try{
			setDisciplinasProcSeletivoVO(null);			
			limparMensagem();			
			List<DisciplinasProcSeletivoVO> disciplinasProcSeletivoVOs = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorProcSeletivo(getProcSeletivoVO().getCodigo(), false, getUsuarioLogado());
			getListaSelectItemDisciplinaProcSeletivo().clear();
			setListaSelectItemDisciplinaProcSeletivo(UtilSelectItem.getListaSelectItem(disciplinasProcSeletivoVOs, "codigo", "nome"));
			if(getListaSelectItemDisciplinaProcSeletivo().size() == 1){
				getListaSelectItemDisciplinaProcSeletivo().clear();
			}
			return "editarNotaDisciplina";
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "editar";
		}
	}

	public List<SelectItem> getListaSelectItemDisciplinaProcSeletivo() {
		if(listaSelectItemDisciplinaProcSeletivo == null){
			listaSelectItemDisciplinaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaProcSeletivo;
	}

	public void setListaSelectItemDisciplinaProcSeletivo(List<SelectItem> listaSelectItemDisciplinaProcSeletivo) {
		this.listaSelectItemDisciplinaProcSeletivo = listaSelectItemDisciplinaProcSeletivo;
	}

	public void marcarInscricaoComoNaoCompareceu() {
		try {
			getFacadeFactory().getInscricaoFacade().realizarAlteracaoInscricaoNaoCompareceu(getResultadoProcessamentoArquivoResposta().getListaInscricaoNaoComparecidosVOs(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosListaInscricaoNaoCompareceu() {
		for (InscricaoVO obj : getResultadoProcessamentoArquivoResposta().getListaInscricaoNaoComparecidosVOs()) {
			if (obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				obj.setSelecionar(true);
			}
		}
	}
	
	public void desmarcarTodosListaInscricaoNaoCompareceu() {
		for (InscricaoVO obj : getResultadoProcessamentoArquivoResposta().getListaInscricaoNaoComparecidosVOs()) {
			if (obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				obj.setSelecionar(false);
			}
		}
	}
	

}
