package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO.EnumCampoConsultaPeriodoAquisitivoFuncionario;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.Uteis;

@Controller("PeriodoAquisitivoFeriasControle")
@Scope("viewScope")
@Lazy
public class PeriodoAquisitivoFeriasControle extends SuperControle {

	private static final long serialVersionUID = 2208053835267809665L;

	private static final String TELA_FORM = "periodoAquisitivoFeriasForm";
	private static final String TELA_CONS = "periodoAquisitivoFeriasCons";
	private static final String CONTEXT_PARA_EDICAO = "itemPeriodoAquisitivoFerias";

	private PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO;
	private FuncionarioCargoVO funcionarioCargoVO;

	private List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivoFerias;

	private List<SelectItem> listaSelectItemSituacaoPeriodo;

	private String valorConsultaSituacao;

	public PeriodoAquisitivoFeriasControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	@PostConstruct
	public void carregarFichaFinanceiraVindoDeOutraTela () {
		
		FuncionarioCargoVO funcionarioCargo = (FuncionarioCargoVO) context().getExternalContext().getSessionMap().get("funcionarioCargo");
		try {
			if (Uteis.isAtributoPreenchido(funcionarioCargo)) {
				funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargo.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getPeriodoAquisitivoFeriasVO().setFuncionarioCargo(funcionarioCargo);
				
				setFuncionarioCargoVO(funcionarioCargo);
				prepararDadosParaEdicao(funcionarioCargo);
			}
		} catch (Exception e) {
			setMensagemID(e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("funcionarioCargo");
		}
	}

	public String editar() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setFuncionarioCargoVO(obj);
		return prepararDadosParaEdicao(obj);
	}

	public String prepararDadosParaEdicao(FuncionarioCargoVO obj) {
		try {
			getPeriodoAquisitivoFeriasVO().setFuncionarioCargo(obj);
			setListaPeriodoAquisitivoFerias(getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPorFuncionarioCargo(funcionarioCargoVO, false, getUsuarioLogado()));
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		
	}

	public void persistir() {
		try {
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCodigo())) {
				getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().persistirTodos(getListaPeriodoAquisitivoFerias(), getFuncionarioCargoVO(), true, getUsuarioLogado());
				prepararDadosParaEdicao(getFuncionarioCargoVO());
				getPeriodoAquisitivoFeriasVO().setFuncionarioCargo(getFuncionarioCargoVO());
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			} else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Nenhum Funcionário selecionado");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao());
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarFuncionarioPelaMatriculaCargo() {
		try {
			setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getPeriodoAquisitivoFeriasVO().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(getFuncionarioCargoVO().getCodigo() > 0) {
			setPeriodoAquisitivoFeriasVO(new PeriodoAquisitivoFeriasVO());
			setListaPeriodoAquisitivoFerias(new ArrayList<>());
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			prepararDadosParaEdicao(getFuncionarioCargoVO());
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setFuncionarioCargoVO(new FuncionarioCargoVO());
			setPeriodoAquisitivoFeriasVO(new PeriodoAquisitivoFeriasVO());
			setListaPeriodoAquisitivoFerias(new ArrayList<>());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().excluirPorFuncionarioCargo(getFuncionarioCargoVO(), false, getUsuario());
			setListaPeriodoAquisitivoFerias(new ArrayList<>());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public void visualizarMarcacaoFerias(PeriodoAquisitivoFeriasVO periodoAquisitivoFerias) {
		context().getExternalContext().getSessionMap().put("funcionarioCargo", getFuncionarioCargoVO());
		context().getExternalContext().getSessionMap().put("marcacaoFeriasChamadoDoPeriodoArquisitivo", periodoAquisitivoFerias);
		removerControleMemoriaFlashTela(MarcacaoFeriasControle.class.getSimpleName());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaPeriodoAquisitivoFuncionario.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	/**
	 * Adicionar o {@link PeriodoAquisitivoFeriasVO} ne lista de periodos aquisitivos.
	 * 
	 */
	public void adicionarPeriodoAquisitivoFerias() {
		try {

			getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDadosAdicionarPeriodoAquisitivo(getPeriodoAquisitivoFeriasVO(), getListaPeriodoAquisitivoFerias());

			if (getPeriodoAquisitivoFeriasVO().getItemEmEdicao()) {
				
				Iterator<PeriodoAquisitivoFeriasVO> i = getListaPeriodoAquisitivoFerias().iterator();
				int index = 0;
				int aux = -1;
				PeriodoAquisitivoFeriasVO objAux = new PeriodoAquisitivoFeriasVO();
				while(i.hasNext()) {
					PeriodoAquisitivoFeriasVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getPeriodoAquisitivoFeriasVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getPeriodoAquisitivoFeriasVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getPeriodoAquisitivoFeriasVO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getListaPeriodoAquisitivoFerias().set(aux, objAux);
				}
			} else {		
				getListaPeriodoAquisitivoFerias().add(0, getPeriodoAquisitivoFeriasVO());
			}

			limparDadosPeriodoAquisitivoFerias();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void cancelarEdicao() {
		setPeriodoAquisitivoFeriasVO(new PeriodoAquisitivoFeriasVO());
		getPeriodoAquisitivoFeriasVO().setFuncionarioCargo(getFuncionarioCargoVO());
		getPeriodoAquisitivoFeriasVO().setItemEmEdicao(false);
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}
	
	public void limparDadosPeriodoAquisitivoFerias() {
		this.cancelarEdicao();
	}
	
	public void removerItem(PeriodoAquisitivoFeriasVO obj) {
		getListaPeriodoAquisitivoFerias().removeIf(p -> p.getInicioPeriodo().equals(obj.getInicioPeriodo()) && p.getCodigo().equals(obj.getCodigo()));
		limparDadosPeriodoAquisitivoFerias();
	}

	public String editarPeriodoAquisitivoFerias()  {
		limparDadosPeriodoAquisitivoFerias();
        try {
        	PeriodoAquisitivoFeriasVO obj =(PeriodoAquisitivoFeriasVO) context().getExternalContext().getRequestMap().get("periodoAquisitivo");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setPeriodoAquisitivoFeriasVO((PeriodoAquisitivoFeriasVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	/**
	 * Calcula e preenche o final do periodo aquisitivo a partir da data de inicio do periodo.
	 */
	public void preencherFinalPeriodoAquisitivo() {
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().preencherFinalPeriodoAquisitivo(getPeriodoAquisitivoFeriasVO());
	}

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "TODOS";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public PeriodoAquisitivoFeriasVO getPeriodoAquisitivoFeriasVO() {
		if(periodoAquisitivoFeriasVO == null)
			periodoAquisitivoFeriasVO = new PeriodoAquisitivoFeriasVO();
		return periodoAquisitivoFeriasVO;
	}

	public void setPeriodoAquisitivoFeriasVO(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO) {
		this.periodoAquisitivoFeriasVO = periodoAquisitivoFeriasVO;
	}

	public List<PeriodoAquisitivoFeriasVO> getListaPeriodoAquisitivoFerias() {
		if(listaPeriodoAquisitivoFerias == null)
			listaPeriodoAquisitivoFerias = new ArrayList<>();
		return listaPeriodoAquisitivoFerias;
	}

	public void setListaPeriodoAquisitivoFerias(List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivoFerias) {
		this.listaPeriodoAquisitivoFerias = listaPeriodoAquisitivoFerias;
	}

	public List<SelectItem> getListaSelectItemSituacaoPeriodo() {
		if(listaSelectItemSituacaoPeriodo == null)
			listaSelectItemSituacaoPeriodo = SituacaoPeriodoAquisitivoEnum.getValorSituacaoPeriodoAquisitivoEnum();
		return listaSelectItemSituacaoPeriodo;
	}

	public void setListaSelectItemSituacaoPeriodo(List<SelectItem> listaSelectItemSituacaoPeriodo) {
		this.listaSelectItemSituacaoPeriodo = listaSelectItemSituacaoPeriodo;
	}
	
	public void atualizarPeriodoAquisitivo() {
		try {
			CompetenciaFolhaPagamentoVO competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
			getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().realizarAtualizacaoDoPeriodoAquisitivoDoFuncionario(competencia, funcionarioCargoVO);
			prepararDadosParaEdicao(getFuncionarioCargoVO());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}