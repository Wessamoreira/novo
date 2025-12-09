package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("CalendarioRelatorioFinalFacilitadorControle")
@Scope("viewScope")
@Lazy
public class CalendarioRelatorioFinalFacilitadorControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4835656321242590046L;
	private CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemSituacao;
	private List<SelectItem> listaSelectItemVariavelNota;
	private List<SelectItem> listaSelectItemQuestionario;
	private List<SelectItem> listaSelectItemMes;
	private Boolean carregarListaVariavelTipoNota;


	public String novo() {
		limparMensagem();
		removerObjetoMemoria(this);
		setCarregarListaVariavelTipoNota(true);
		montarListaSelectItemQuestionario();
		getListaSelectItemMes();
		getCalendarioRelatorioFinalFacilitadorVO().setAno(Uteis.getAnoDataAtual());
		getCalendarioRelatorioFinalFacilitadorVO().setSemestre(Uteis.getSemestreAtual());
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRelatorioFinalFacilitadorForm.xhtml");
	}

	public String consultar() {
		try {
			if(Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina()) || getControleConsultaOtimizado().getLimitePorPagina() == 0) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultar(getCalendarioRelatorioFinalFacilitadorVO().getDisciplinaVO().getCodigo(), 
					getCalendarioRelatorioFinalFacilitadorVO().getSituacao(), getCalendarioRelatorioFinalFacilitadorVO().getAno(),getCalendarioRelatorioFinalFacilitadorVO().getSemestre(), getCalendarioRelatorioFinalFacilitadorVO().getMes(),
						getCalendarioRelatorioFinalFacilitadorVO().getDataInicio(), getCalendarioRelatorioFinalFacilitadorVO().getDataFim(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultarTotalRegistro(getCalendarioRelatorioFinalFacilitadorVO().getDisciplinaVO().getCodigo(),
					getCalendarioRelatorioFinalFacilitadorVO().getSituacao(), getCalendarioRelatorioFinalFacilitadorVO().getAno(),getCalendarioRelatorioFinalFacilitadorVO().getSemestre(), getCalendarioRelatorioFinalFacilitadorVO().getMes(),
						getCalendarioRelatorioFinalFacilitadorVO().getDataInicio(), getCalendarioRelatorioFinalFacilitadorVO().getDataFim()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRelatorioFinalFacilitadorCons.xhtml");
	}
	
	public void realizarConsulta() {
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		consultar();
	}
	
	public void paginarConsulta(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}
	
	public String excluir() {
		try {
			getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().excluir(getCalendarioRelatorioFinalFacilitadorVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRelatorioFinalFacilitadorForm.xhtml");
	}
	
	public String editar() {
		try {
			setCalendarioRelatorioFinalFacilitadorVO((CalendarioRelatorioFinalFacilitadorVO) getRequestMap().get("calendarioItens"));
			setCarregarListaVariavelTipoNota(true);
			montarListaSelectItemQuestionario();
			montarListaSelectItemVariavelNota();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRelatorioFinalFacilitadorForm.xhtml");
	}

	public void persitir() {
		try {
			getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().persistir(getCalendarioRelatorioFinalFacilitadorVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@PostConstruct
	public String inicializarConsulta() {
		removerObjetoMemoria(this);
		setCarregarListaVariavelTipoNota(false);
		setControleConsultaOtimizado(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRelatorioFinalFacilitadorCons.xhtml");
	}
	
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaConsulta() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getCalendarioRelatorioFinalFacilitadorVO().setDisciplinaVO(obj);
			setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
			getListaConsultaDisciplina().clear();
			if(getCarregarListaVariavelTipoNota()) {
				montarListaSelectItemVariavelNota();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDadosDisciplina() {
		getCalendarioRelatorioFinalFacilitadorVO().setDisciplinaVO(new DisciplinaVO());
		getCalendarioRelatorioFinalFacilitadorVO().setVariavelTipoNota("");
		if(getCarregarListaVariavelTipoNota()) {
			montarListaSelectItemVariavelNota();
		}
	}
	
	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}
	
	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem("", "Todos"));
			listaSelectItemSituacao.add(new SelectItem("Encerrado", "Encerrado"));
			listaSelectItemSituacao.add(new SelectItem("Em aberto", "Em aberto"));
			listaSelectItemSituacao.add(new SelectItem("Aguardando Prazo", "Aguardando Prazo"));

		}
		return listaSelectItemSituacao;
	}

	public void montarListaSelectItemVariavelNota() {
		try {
			setListaSelectItemVariavelNota(getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarConfiguracaoAcademicaNotaPorDisciplina(getCalendarioRelatorioFinalFacilitadorVO().getDisciplinaVO().getCodigo(), getCalendarioRelatorioFinalFacilitadorVO().getAno(), getCalendarioRelatorioFinalFacilitadorVO().getSemestre()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	public void montarListaSelectItemQuestionario() {
        List<QuestionarioVO> resultadoConsulta = null;
        try {
            resultadoConsulta = getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum.RELATORIO_FACILITADOR, "AT", false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setListaSelectItemQuestionario(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
	}

	public CalendarioRelatorioFinalFacilitadorVO getCalendarioRelatorioFinalFacilitadorVO() {
		if(calendarioRelatorioFinalFacilitadorVO == null) {
			calendarioRelatorioFinalFacilitadorVO = new CalendarioRelatorioFinalFacilitadorVO();
		}
		return calendarioRelatorioFinalFacilitadorVO;
	}


	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public void setCalendarioRelatorioFinalFacilitadorVO(
			CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO) {
		this.calendarioRelatorioFinalFacilitadorVO = calendarioRelatorioFinalFacilitadorVO;
	}

	public List<SelectItem> getListaSelectItemVariavelNota() {
		return listaSelectItemVariavelNota;
	}

	public void setListaSelectItemVariavelNota(List<SelectItem> listaSelectItemVariavelNota) {
		this.listaSelectItemVariavelNota = listaSelectItemVariavelNota;
	}
	
	public List<SelectItem> getListaSelectItemQuestionario() {
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}
	
		
	public List<SelectItem> getListaSelectItemMes() {
		if(listaSelectItemMes == null) {
			listaSelectItemMes = new ArrayList<SelectItem>();
			listaSelectItemMes.add(new SelectItem("", ""));
    		for(MesAnoEnum mesAnoEnum : MesAnoEnum.values()){
    			listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
    		}
		}
		return listaSelectItemMes;
	}

	public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
		this.listaSelectItemMes = listaSelectItemMes;
	}

	public Boolean getCarregarListaVariavelTipoNota() {
		if(carregarListaVariavelTipoNota == null) {
			carregarListaVariavelTipoNota = Boolean.FALSE;
		}
		return carregarListaVariavelTipoNota;
	}

	public void setCarregarListaVariavelTipoNota(Boolean carregarListaVariavelTipoNota) {
		this.carregarListaVariavelTipoNota = carregarListaVariavelTipoNota;
	}
	
}
