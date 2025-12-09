package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cargoForm.jsp cargoCons.jsp) com as funcionalidades da classe <code>Cargo</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cargo
 * @see CargoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.Cargo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cargoForm.jsp cargoCons.jsp) com as funcionalidades da classe <code>Cargo</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cargo
 * @see CargoVO
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller("CargoControle")
@Scope("viewScope")
@Lazy
public class CargoControle extends SuperControle implements Serializable {

private static final long serialVersionUID = -2005375046301054628L;
	
	private CargoVO cargoVO;
	protected List listaSelectItemDepartamento;
	private String campoConsultaProgressaoSalarial;
	private String valorConsultaProgressaoSalarial;
	private List<SelectItem> listaConsultaProgressao;

    public CargoControle() throws Exception {
        setListaSelectItemDepartamento(new ArrayList(0));
        setControleConsulta(new ControleConsulta());
        montarListaSelectItemDepartamento();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Cargo</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        montarListaSelectItemDepartamento();
        setCargoVO(new CargoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cargoForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Cargo</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        CargoVO obj = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItem");
        montarListaSelectItemDepartamento();
        obj = montarDadosCargoVOCompleto(obj);
        obj.setNovoObj(Boolean.FALSE);
        setCargoVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cargoForm");
    }

    public CargoVO montarDadosCargoVOCompleto(CargoVO obj) {
        try {
            return getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new CargoVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Cargo</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (cargoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getCargoFacade().incluir(cargoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getCargoFacade().alterar(cargoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CargoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getCargoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            } else {
            	objs = getFacadeFactory().getCargoFacade().consultarPorCbo(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CargoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getCargoFacade().excluir(cargoVO, getUsuarioLogado());
            setCargoVO(new CargoVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
        	if (e instanceof DataIntegrityViolationException && e.toString().contains("fk_funcionariocargo_cargo")){
        		setMensagemDetalhada("msg_erro", "Não foi possível excluir este cargo pois ele está vinculado a um funcionário.");
        		return;
        	}
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemDepartamento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDepartamentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DepartamentoVO obj = (DepartamentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemDepartamento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemDepartamento() {
        try {
            montarListaSelectItemDepartamento("");
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
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

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("cbo", "CBO"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("cargoCons.xhtml");
    }

    public CargoVO getCargoVO() {
        return cargoVO;
    }

    public void setCargoVO(CargoVO cargoVO) {
        this.cargoVO = cargoVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        cargoVO = null;
    }

    /**
     * @return the listaSelectItemDepartamento
     */
    public List<SelectItem> getListaSelectItemDepartamento() {
        return listaSelectItemDepartamento;
    }

    /**
     * @param listaSelectItemDepartamento the listaSelectItemDepartamento to set
     */
    public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
        this.listaSelectItemDepartamento = listaSelectItemDepartamento;
    }
    
    public void setValorConsultaCargo(String valorConsultaCargo) {
		this.valorConsultaProgressaoSalarial = valorConsultaCargo;
	}
 
	public List<SelectItem> getTipoConsultaComboCargo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificador", "Identificador"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List<SelectItem> getListaConsultaProgressao() {
		if (listaConsultaProgressao == null)
			listaConsultaProgressao = new ArrayList<>();
		return listaConsultaProgressao;
	}

	public void setListaConsultaProgressao(List<SelectItem> listaConsultaProgressao) {
		this.listaConsultaProgressao = listaConsultaProgressao;
	}
	
	public void selecionarProgressaoSalarial() {
		ProgressaoSalarialVO progressaoSalarialVO = (ProgressaoSalarialVO) context().getExternalContext().getRequestMap().get("progressaoItens");
		getCargoVO().getProgressaoSalarial().setCodigo(progressaoSalarialVO.getCodigo());
		getCargoVO().getProgressaoSalarial().setDescricao(progressaoSalarialVO.getDescricao());
	}
	
	public void consultarProgressaoSalarial() {
		
		try {
			
			if(getCampoConsultaProgressaoSalarial().equals("CODIGO")) {
				Uteis.validarSomenteNumeroString(getValorConsultaProgressaoSalarial());
			} 

			List objs = new ArrayList(0);
			
			objs = getFacadeFactory().getProgressaoSalarialInterfaceFacade().consultarPorFiltro(getCampoConsultaProgressaoSalarial(), getValorConsultaProgressaoSalarial(), true, getUsuarioLogado());
			setListaConsultaProgressao(objs);
            setMensagemID("msg_dados_consultados");
            
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public String getCampoConsultaProgressaoSalarial() {
		if (campoConsultaProgressaoSalarial == null)
			campoConsultaProgressaoSalarial = "";
		return campoConsultaProgressaoSalarial;
	}

	public void setCampoConsultaProgressaoSalarial(String campoConsultaProgressaoSalarial) {
		this.campoConsultaProgressaoSalarial = campoConsultaProgressaoSalarial;
	}

	public String getValorConsultaProgressaoSalarial() {
		if (valorConsultaProgressaoSalarial == null)
			valorConsultaProgressaoSalarial = "";
		return valorConsultaProgressaoSalarial;
	}

	public void setValorConsultaProgressaoSalarial(String valorConsultaProgressaoSalarial) {
		this.valorConsultaProgressaoSalarial = valorConsultaProgressaoSalarial;
	}
	
	public void limparDadosConsulta() {
		getCargoVO().setProgressaoSalarial(null);
	}
}
