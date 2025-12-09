package controle.administrativo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FollowMeGrupoDestinatarioVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.administrativo.enumeradores.FrequenciaEnvioFollowMeEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

@Controller("FollowMeControle")
@Scope("viewScope")
@Lazy
public class FollowMeControle extends SuperControle {

	/**
     * 
     */
	private static final long serialVersionUID = -1869857620672591993L;
	private FollowMeVO followMeVO;
	private FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO;
	private List<SelectItem> listaSelectItemDiaSemana;
	private List<SelectItem> listaSelectItemFrequenciaEnvioFollowMe;
	private List<SelectItem> listaSelectItemGrupoDestinatario;
	private String descricao;
	private DataModelo dadosConsultaRelatorio;

	public String novo() {
		try {
			setFollowMeVO(null);
			setFollowMeGrupoDestinatarioVO(null);
			setDadosConsultaRelatorio(null);
			getFollowMeVO().setFollowMeUnidadeEnsinoVOs(getFacadeFactory().getFollowMeUnidadeEnsinoFacade().consultarPorFollowMeTrazendoTodasUnidadesEnsino(getFollowMeVO().getCodigo()));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("followMeForm");
	}

	public void paginarRelatorios(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarRelatorios();
	}

	public void consultarRelatorios() {
		try {
			getDadosConsultaRelatorio().setLimitePorPagina(10);
			getDadosConsultaRelatorio().setListaConsulta(getFacadeFactory().getFollowMeFacade().consultarFollowMeRelPorFollowMe(getFollowMeVO().getCodigo(), getDadosConsultaRelatorio().getLimitePorPagina(), getDadosConsultaRelatorio().getOffset()));
			getDadosConsultaRelatorio().setTotalRegistrosEncontrados(getFacadeFactory().getFollowMeFacade().consultarTotalRegistroFollowMeRelPorFollowMe(getFollowMeVO().getCodigo()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public DataModelo getDadosConsultaRelatorio() {
		if (dadosConsultaRelatorio == null) {
			dadosConsultaRelatorio = new DataModelo();
		}
		return dadosConsultaRelatorio;
	}

	public void setDadosConsultaRelatorio(DataModelo dadosConsultaRelatorio) {
		this.dadosConsultaRelatorio = dadosConsultaRelatorio;
	}

	public void clonar() {
		try {
			setFollowMeVO(getFollowMeVO().clone());
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			setFollowMeVO((FollowMeVO) context().getExternalContext().getRequestMap().get("followMeItem"));
			getFollowMeVO().setFollowMeGrupoDestinatarioVOs(getFacadeFactory().getFollowMeGrupoDestinatarioFacade().consultarPorFollowMe(getFollowMeVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
			getFollowMeVO().setFollowMeUnidadeEnsinoVOs(getFacadeFactory().getFollowMeUnidadeEnsinoFacade().consultarPorFollowMeTrazendoTodasUnidadesEnsino(getFollowMeVO().getCodigo()));
			getFollowMeVO().setFollowMeCategoriaDespesaVOs(getFacadeFactory().getFollowMeCategoriaDespesaFacade().consultarPorFollowMeTrazendoTodasCategoriaDespesa(getFollowMeVO().getCodigo()));
			getFollowMeVO().setFollowMeDepartamentoVOs(getFacadeFactory().getFollowMeDepartamentoFacade().consultarPorFollowMeTrazendoTodosDepartamento(getFollowMeVO().getCodigo()));
			setFollowMeGrupoDestinatarioVO(null);
			getDadosConsultaRelatorio().setPage(1);
			getDadosConsultaRelatorio().setPaginaAtual(1);
			consultarRelatorios();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("followMeForm");
	}

	public void persistir() {
		try {
			getFacadeFactory().getFollowMeFacade().persistir(getFollowMeVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarGeracaoRelatorio() {
		try {
			getFacadeFactory().getFollowMeFacade().realizarEnvioDadosFollowMeAgora(getFollowMeVO(), null);
			getDadosConsultaRelatorio().setPage(1);
			getDadosConsultaRelatorio().setPaginaAtual(1);
			consultarRelatorios();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getFollowMeFacade().excluir(getFollowMeVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@Override
	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getFollowMeFacade().consultar(getDescricao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFollowMeFacade().consultarTotalRegistros(getDescricao()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			getControleConsultaOtimizado().getListaConsulta();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void paginarConsulta(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultar();
	}

	public String inicializarConsultar() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setPaginaAtual(1);
		setDescricao("");
		consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("followMeCons");
	}

	public void adicionarFollowMeGrupoDestinatario() {
		try {
			for (SelectItem selectItem : getListaSelectItemGrupoDestinatario()) {
				if (((Integer) selectItem.getValue()).equals(getFollowMeGrupoDestinatarioVO().getGrupoDestinatario().getCodigo())) {
					getFollowMeGrupoDestinatarioVO().getGrupoDestinatario().setNomeGrupo(selectItem.getLabel());
					break;
				}
			}
			getFacadeFactory().getFollowMeFacade().adicionarFollowMeGrupoDestinatarioVO(getFollowMeVO(), getFollowMeGrupoDestinatarioVO());
			setFollowMeGrupoDestinatarioVO(new FollowMeGrupoDestinatarioVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerFollowMeGrupoDestinatario() {
		try {
			getFacadeFactory().getFollowMeFacade().removerFollowMeGrupoDestinatarioVO(getFollowMeVO(), (FollowMeGrupoDestinatarioVO) context().getExternalContext().getRequestMap().get("followMeGrupoDestinatarioItem"));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarFollowMeGrupoDestinatario() {
		try {
			setFollowMeGrupoDestinatarioVO((FollowMeGrupoDestinatarioVO) context().getExternalContext().getRequestMap().get("followMeGrupoDestinatarioItem"));
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public FollowMeVO getFollowMeVO() {
		if (followMeVO == null) {
			followMeVO = new FollowMeVO();
		}
		return followMeVO;
	}

	public void setFollowMeVO(FollowMeVO followMeVO) {
		this.followMeVO = followMeVO;
	}

	public FollowMeGrupoDestinatarioVO getFollowMeGrupoDestinatarioVO() {
		if (followMeGrupoDestinatarioVO == null) {
			followMeGrupoDestinatarioVO = new FollowMeGrupoDestinatarioVO();
		}
		return followMeGrupoDestinatarioVO;
	}

	public void setFollowMeGrupoDestinatarioVO(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) {
		this.followMeGrupoDestinatarioVO = followMeGrupoDestinatarioVO;
	}

	public List<SelectItem> getListaSelectItemDiaSemana() {
		if (listaSelectItemDiaSemana == null) {
			listaSelectItemDiaSemana = new ArrayList<SelectItem>(0);
			for (DiaSemana diaSemana : DiaSemana.values()) {
				if (!diaSemana.equals(DiaSemana.NENHUM)) {
					listaSelectItemDiaSemana.add(new SelectItem(diaSemana, diaSemana.getDescricao()));
				}
			}
		}
		return listaSelectItemDiaSemana;
	}

	public void setListaSelectItemDiaSemana(List<SelectItem> listaSelectItemDiaSemana) {
		this.listaSelectItemDiaSemana = listaSelectItemDiaSemana;
	}

	public List<SelectItem> getListaSelectItemFrequenciaEnvioFollowMe() {
		if (listaSelectItemFrequenciaEnvioFollowMe == null) {
			listaSelectItemFrequenciaEnvioFollowMe = FrequenciaEnvioFollowMeEnum.getListaSelectItemFrequenciaEnvioFollowMeEnum();
		}
		return listaSelectItemFrequenciaEnvioFollowMe;
	}

	public void setListaSelectItemFrequenciaEnvioFollowMe(List<SelectItem> listaSelectItemFrequenciaEnvioFollowMe) {
		this.listaSelectItemFrequenciaEnvioFollowMe = listaSelectItemFrequenciaEnvioFollowMe;
	}

	public List<SelectItem> getListaSelectItemGrupoDestinatario() {
		if (listaSelectItemGrupoDestinatario == null) {
			listaSelectItemGrupoDestinatario = getFacadeFactory().getGrupoDestinatariosFacade().consultarDadosListaSelectItem(Obrigatorio.SIM);
		}
		return listaSelectItemGrupoDestinatario;
	}

	public void setListaSelectItemGrupoDestinatario(List<SelectItem> listaSelectItemGrupoDestinatario) {
		this.listaSelectItemGrupoDestinatario = listaSelectItemGrupoDestinatario;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getColumnFollowMeUnidadeEnsinoVOs() {
		if (getFollowMeVO().getFollowMeUnidadeEnsinoVOs().size() > 5) {
			return 5;
		}
		return getFollowMeVO().getFollowMeUnidadeEnsinoVOs().size();
	}

	public Integer getElementFollowMeUnidadeEnsinoVOs() {
		return getFollowMeVO().getFollowMeUnidadeEnsinoVOs().size();
	}

	public Integer getColumnFollowMeDepartamentoVOs() {
		if (getFollowMeVO().getFollowMeDepartamentoVOs().size() > 4) {
			return 4;
		}
		return getFollowMeVO().getFollowMeDepartamentoVOs().size();
	}

	public Integer getElementFollowMeDepartamentoVOs() {
		return getFollowMeVO().getFollowMeDepartamentoVOs().size();
	}

	public Integer getColumnFollowMeCategoriaDespesaVOs() {
		if (getFollowMeVO().getFollowMeCategoriaDespesaVOs().size() > 4) {
			return 4;
		}
		return getFollowMeVO().getFollowMeCategoriaDespesaVOs().size();
	}

	public Integer getElementFollowMeCategoriaDespesaVOs() {
		if (getFollowMeVO().getFollowMeCategoriaDespesaVOs().size() > 20) {
			return 20;
		}
		return getFollowMeVO().getFollowMeCategoriaDespesaVOs().size();
	}
}
