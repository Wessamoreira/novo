package controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

@Controller("NotificacaoProcessoSeletivoControle")
@Scope("viewScope")
@Lazy
public class NotificacaoProcessoSeletivoControle extends SuperControle {

	private static final long serialVersionUID = 1L;

	private ProcSeletivoVO procSeletivoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private List<ProcSeletivoVO> procSeletivoVOs;
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
	private List<SelectItem> listaSelectItemProcessoSeletivo;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemDataProva;
	private List<SelectItem> listaSelectItemSala;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> tipoNotificacaoProcessoSeletivo;
	private TipoRelatorioEstatisticoProcessoSeletivoEnum tipoNotificacao;
	private String valorConsultaProcSeletivo;
	private String campoConsultaProcSeletivo;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private Integer sala;

	public NotificacaoProcessoSeletivoControle() throws Exception {
		super();
		inicializarListasSelectItemTodosComboBox();
	}

	public void gravar() throws Exception {
		try {
			if (getProcSeletivoVO().getCodigo().equals(0)) {
				throw new Exception("O Campo PROCESSO SELETIVO deve ser selecionado.");
			}
			if (getTipoNotificacao() == null) {
				throw new Exception("O Campo TIPO NOTIFICAÇÃO deve ser selecionado.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "NotificacaoProcessoSeletivoControle", "Inicializando Incluir Notificacao Processo Seletivo", "Incluindo");
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoProcessoSeletivo(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getTipoNotificacao(), getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "NotificacaoProcessoSeletivoControle", "Inicializando Incluir Notificacao Processo Seletivo", "Incluindo");
			setMensagemID("msg_msg_enviados");
			limparDados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProcSeletivo() {
		try {
			List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setProcSeletivoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setProcSeletivoVOs(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void montarListaUltimosProcSeletivos() {
		try {
			setProcSeletivoVOs(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setProcSeletivoVOs(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDataProva() {
		try {
			List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemDataProva().clear();
			getListaSelectItemDataProva().add(new SelectItem(0, ""));
			for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
				getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoProcSeletivo(getValorConsultaCurso(), getProcSeletivoVO().getCodigo(), "", "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setUnidadeEnsinoCursoVOs(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'this.id','99/99/9999',event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboProcSeletivo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}

	public void selecionarProcSeletivo() {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		setProcSeletivoVO(obj);
		montarListaSelectItemSala();
		montarListaSelectItemDataProva();
	}

	public void montarListaSelectItemSala() {
		setSala(0);
		List<SalaLocalAulaVO> salaVOs = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivoEDataAula(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo());
		if (!salaVOs.isEmpty()) {
			getListaSelectItemSala().clear();
			getListaSelectItemSala().add(new SelectItem(0, "Todas"));
			for (SalaLocalAulaVO sala : salaVOs) {
				getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getSala()));
			}
		} else {
			getListaSelectItemSala().clear();
			getListaSelectItemSala().add(new SelectItem(0, "Sem Sala"));
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setUnidadeEnsinoCursoVO(obj);
			montarListaSelectItemSala();
			getUnidadeEnsinoCursoVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDataProva();
	}

	public void limparDados() {
		getProcSeletivoVO().setCodigo(0);
		getProcSeletivoVO().setDescricao("");
		getListaSelectItemDataProva().clear();
		getListaSelectItemSala().clear();
		getListaSelectItemProcessoSeletivo().clear();
	}

	public void limparDadosCurso() {
		getUnidadeEnsinoCursoVO().setCodigo(0);
		getUnidadeEnsinoCursoVO().setNomeUnidadeEnsino("");
		getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getUnidadeEnsinoCursoVO().getTurno().setNome("");
		montarListaSelectItemSala();
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public List<ProcSeletivoVO> getProcSeletivoVOs() {
		if (procSeletivoVOs == null) {
			procSeletivoVOs = new ArrayList<ProcSeletivoVO>();
		}
		return procSeletivoVOs;
	}

	public void setProcSeletivoVOs(List<ProcSeletivoVO> procSeletivoVOs) {
		this.procSeletivoVOs = procSeletivoVOs;
	}

	public List<SelectItem> getListaSelectItemProcessoSeletivo() {
		if (listaSelectItemProcessoSeletivo == null) {
			listaSelectItemProcessoSeletivo = new ArrayList<SelectItem>();
		}
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List<SelectItem> listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<SelectItem>();
			listaSelectItemCurso.add(new SelectItem("curso", "Curso"));
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List<SelectItem> getListaSelectItemDataProva() {
		if (listaSelectItemDataProva == null) {
			listaSelectItemDataProva = new ArrayList<SelectItem>();
		}
		return listaSelectItemDataProva;
	}

	public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
		this.listaSelectItemDataProva = listaSelectItemDataProva;
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

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
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

	public Integer getSala() {
		if (sala == null) {
			sala = 0;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}

	public boolean getIsApresentraUnidadeEnsinoCurso() {
		return getProcSeletivoVO().getCodigo() != 0;
	}

	public List<SelectItem> getTipoNotificacaoProcessoSeletivo() {
		if (tipoNotificacaoProcessoSeletivo == null) {
			tipoNotificacaoProcessoSeletivo = new ArrayList<SelectItem>();
			tipoNotificacaoProcessoSeletivo.add(new SelectItem("", ""));
			tipoNotificacaoProcessoSeletivo.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES, "Ausentes"));
			tipoNotificacaoProcessoSeletivo.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LEMBRETE_DATA_PROVA, "Lembrete Data Prova"));
			tipoNotificacaoProcessoSeletivo.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS, "Não Matriculados"));
			tipoNotificacaoProcessoSeletivo.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTES, "Presentes"));
		}
		return tipoNotificacaoProcessoSeletivo;
	}

	public TipoRelatorioEstatisticoProcessoSeletivoEnum getTipoNotificacao() {
		return tipoNotificacao;
	}

	public void setTipoNotificacao(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoNotificacao) {
		this.tipoNotificacao = tipoNotificacao;
	}

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>();
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

}
