package controle.academico;

import java.io.Serializable;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turnoForm.jsp turnoCons.jsp) com
 * as funcionalidades da classe <code>Turno</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turno
 * @see TurnoVO
 */
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

@Controller("TurnoControle")
@Scope("viewScope")
public class TurnoControle extends SuperControle implements Serializable{

	private static final long serialVersionUID = 1L;
	private TurnoVO turnoVO;
	private List<SelectItem> listaSelectItemDiaSemana;
	private Integer numeroAula;
	private Integer duracaoAula;
	private DiaSemana diaSemana;
	private TurnoHorarioVO turnoHorarioVO;
	private boolean apresentarBotaoGravar = false;

	public TurnoControle() throws Exception {

		setMensagemID("msg_entre_prmconsulta");
	}

	public void excluirObjTurnoHorarioDomingo() {
		getTurnoVO().getTurnoHorarioDomingo().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioSegunda() {
		getTurnoVO().getTurnoHorarioSegunda().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioTerca() {
		getTurnoVO().getTurnoHorarioTerca().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioQuarta() {
		getTurnoVO().getTurnoHorarioQuarta().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioQuinta() {
		getTurnoVO().getTurnoHorarioQuinta().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioSexta() {
		getTurnoVO().getTurnoHorarioSexta().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirObjTurnoHorarioSabado() {
		getTurnoVO().getTurnoHorarioSabado().clear();
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarObjTurnoHorarioVO() {
		try {
			getFacadeFactory().getTurnoFacade().inicializarDadosListaTurnoHorarioVO(getTurnoVO(), getNumeroAula(), getDiaSemana(), getDuracaoAula(), getApresentarBotaoGravar());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public TurnoHorarioVO getTurnoHorarioVO() {
		if (turnoHorarioVO == null) {
			turnoHorarioVO = new TurnoHorarioVO();
		}
		return turnoHorarioVO;
	}

	public void setTurnoHorarioVO(TurnoHorarioVO turnoHorarioVO) {
		this.turnoHorarioVO = turnoHorarioVO;
	}

	public DiaSemana getDiaSemana() {
		if (diaSemana == null) {
			diaSemana = DiaSemana.DOMINGO;
		}
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Integer getDuracaoAula() {
		if (duracaoAula == null) {
			duracaoAula = 0;
		}
		return duracaoAula;
	}

	public void setDuracaoAula(Integer duracaoAula) {
		this.duracaoAula = duracaoAula;
	}

	public List<SelectItem> getListaSelectItemDiaSemana() {
		if (listaSelectItemDiaSemana == null) {
			listaSelectItemDiaSemana = DiaSemana.getComboDiaSemana();
		}
		return listaSelectItemDiaSemana;
	}

	public void setListaSelectItemDiaSemana(List<SelectItem> listaSelectItemDiaSemana) {
		this.listaSelectItemDiaSemana = listaSelectItemDiaSemana;
	}

	public Integer getNumeroAula() {
		if (numeroAula == null) {
			numeroAula = 0;
		}
		return numeroAula;
	}

	public void setNumeroAula(Integer numeroAula) {
		this.numeroAula = numeroAula;
	}

	public void realizarDefinicaoHorarioFinal(TurnoHorarioVO turnoHorarioVO) {
		try {
			getFacadeFactory().getTurnoHorarioFacade().realizarCalculoHorarioFinal(getTurnoVO(), turnoHorarioVO);
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarNrAulas() {
		if (getTurnoVO().getNrAulas().intValue() < getTurnoVO().getNrAulasAntes().intValue()) {
			getTurnoVO().setNrAulasAntes(getTurnoVO().getNrAulas());
		}
		getFacadeFactory().getTurnoFacade().realizarMontagemHorarios(getTurnoVO());
	}

	public void realizarMontagemHorarios() {
		getFacadeFactory().getTurnoFacade().realizarMontagemHorarios(getTurnoVO());
	}

	public Boolean getExisteHorarioFinal() {
		if (getTurnoVO().getHoraFinal().equals("")) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Turno</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setApresentarBotaoGravar(Boolean.TRUE);
		setTurnoVO(new TurnoVO());
		setNumeroAula(0);
		setDuracaoAula(0);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("turnoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Turno</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoVO");
		try {
			// getFacadeFactory().getTurnoHorarioFacade().consultarTurnoHorarioVOsSeparadoPorDiaSemana(obj);
			obj.setNovoObj(Boolean.FALSE);
			setTurnoVO(obj);
			verificarExisteVinculoProgramacaoAula();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("turnoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";

		}
	}

	public void verificarExisteVinculoProgramacaoAula() throws Exception {
		if (getFacadeFactory().getHorarioProfessorFacade().consultarHorarioProfessorPorTurno(getTurnoVO().getCodigo(), getUsuarioLogado())) {
			setApresentarBotaoGravar(Boolean.FALSE);
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Turno</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */

	public String gravar() {
		try {
			if (turnoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTurnoFacade().incluir(turnoVO, true, getUsuarioLogado());
			} else {
				getFacadeFactory().getTurnoFacade().alterar(turnoVO, true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TurnoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTurnoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getTurnoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TurnoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTurnoFacade().excluir(turnoVO, true, getUsuarioLogado());
			setTurnoVO(new TurnoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
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

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("turnoCons");
	}

	public TurnoVO getTurnoVO() {
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		turnoVO = null;

	}

	public boolean getApresentarBotaoGravar() {
		return apresentarBotaoGravar;
	}

	public void setApresentarBotaoGravar(boolean apresentarBotaoGravar) {
		this.apresentarBotaoGravar = apresentarBotaoGravar;
	}
}
