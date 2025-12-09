package controle.basico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * feriadoForm.jsp feriadoCons.jsp) com as funcionalidades da classe <code>Feriado</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Feriado
 * @see FeriadoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; 

@Controller("FeriadoControle")
@Scope("viewScope")
@Lazy
public class FeriadoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 6457680074236118037L;
	private FeriadoVO feriadoVO;
    protected List<SelectItem> listaSelectItemCidade;
    private Boolean consultarTodosOsAnos;
    private Boolean consultarNacional;
    private String considerarFeriado;
    private String filtroCidade;
    
    private Date dataConsulta;
    protected String campoConsultaCidade;
    protected String valorConsultaCidade;
    protected List<CidadeVO> listaConsultaCidade;
    private List<TurmaVO> listaTurmaHorarioAula;
    private Boolean excluirAulaProgramada;
    /**
     * Interface <code>FeriadoInterfaceFacade</code> responsável pela interconexão da camada de controle com a camada de negócio.
     * Criando uma independência da camada de controle com relação a tenologia de persistência dos dados (DesignPatter: Façade).
     */
    public FeriadoControle() throws Exception {
        //obterUsuarioLogado();
        setConsultarTodosOsAnos(false);
        setConsultarNacional(false);        
        setDataConsulta(new Date());
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("periodo");
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Feriado</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         
    	removerObjetoMemoria(this);
        setFeriadoVO(new FeriadoVO());
        setListaTurmaHorarioAula(new ArrayList<TurmaVO>(0));
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return  Uteis.getCaminhoRedirecionamentoNavegacao("feriadoForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Feriado</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        FeriadoVO obj = (FeriadoVO) context().getExternalContext().getRequestMap().get("feriados");
        getListaTurmaHorarioAula().clear();
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(Boolean.FALSE);
        setFeriadoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return  Uteis.getCaminhoRedirecionamentoNavegacao("feriadoForm");
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>FeriadoVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(FeriadoVO obj) {
        if (obj.getCidade() == null) {
            obj.setCidade(new CidadeVO());
        }
    }
    
    public void limparAulaProgramada() {
    	getListaTurmaHorarioAula().clear();
    }

    public void realizarValidacaoHorarioTurma() throws Exception{
       
    	try{
    		limparMensagem();
    		FeriadoVO.validarDados(feriadoVO);
    		if(feriadoVO.getConsiderarFeriadoAcademico()) {
    		setListaTurmaHorarioAula(getFacadeFactory().getFeriadoFacade().executarValidarNaoPossuiAulaProgramada(feriadoVO, getUsuarioLogado()));
    		if(getListaTurmaHorarioAula().isEmpty()){
    			if (feriadoVO.isNovoObj().booleanValue()) {
    	                getFacadeFactory().getFeriadoFacade().incluir(feriadoVO, getUsuarioLogado(), false);
   	            } else {
    	                getFacadeFactory().getFeriadoFacade().alterar(feriadoVO, getUsuarioLogado(), false);
   	            }
    			setMensagemID("msg_dados_gravados");
    		}else{
    			setTam(0);
    			for(TurmaVO turmaVO: getListaTurmaHorarioAula()){
    				setTam(getTam()+turmaVO.getNrVagas());
    			}
    		}
    		}else {
    			getListaTurmaHorarioAula().isEmpty();
    			persistir();
    		}
    	}catch(Exception e){
    		getListaTurmaHorarioAula().clear();
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Feriado</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void persistir() {
        try {            
            if (feriadoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getFeriadoFacade().incluir(feriadoVO, getUsuarioLogado(), getExcluirAulaProgramada());
            } else {
                getFacadeFactory().getFeriadoFacade().alterar(feriadoVO, getUsuarioLogado(), getExcluirAulaProgramada());
            }
            setMensagemID("msg_dados_gravados");            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro",e.getMessage());            
        }finally{
        	getListaTurmaHorarioAula().clear();
        }
    }

    public void limparCampoConsulta() {
        getControleConsulta().setValorConsulta("");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP FeriadoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    
    public String consultar() {
        try {
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("ano")) {
                objs = getFacadeFactory().getFeriadoFacade().consultarPorAno(getControleConsulta().getValorConsulta(),  getConsiderarFeriado(), getFiltroCidade(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodo")) {
            	if(!Uteis.isAtributoPreenchido(getControleConsulta().getDataIni()) || !Uteis.isAtributoPreenchido(getControleConsulta().getDataFim())){
            		throw new Exception("O período deve ser informado.");
            	}
            	objs = getFacadeFactory().getFeriadoFacade().consultarPorPeriodo(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getConsiderarFeriado(), getFiltroCidade(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
            	objs = getFacadeFactory().getFeriadoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(),  getFiltroCidade(), getConsiderarFeriado(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "feriadoCons";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro",e.getMessage());
            return  Uteis.getCaminhoRedirecionamentoNavegacao("feriadoCons");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>FeriadoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getFeriadoFacade().excluir(feriadoVO, getUsuarioLogado());
            setFeriadoVO(new FeriadoVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro",e.getMessage());
            return  Uteis.getCaminhoRedirecionamentoNavegacao("feriadoForm");
        }
    }

    /**
	 * Método responsável por consultar Cidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorEstado(getValorConsultaCidade(), false, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */
	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getFeriadoVO().setCidade(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

        /**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
	 */
	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("estado", "Estado"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public String getCampoConsultaCidade() {
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public List getListaConsultaCidade() {
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    public String getValorConsultaCidade() {
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }


    public void irPaginaInicial() throws Exception {

        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {

        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {

        this.consultar();
    }

    public void irPaginaFinal() throws Exception {

        this.consultar();
    }

    
    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
    
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("ano", "Ano"));
        itens.add(new SelectItem("periodo", "Período"));
        return itens;
    }
    
    public List<SelectItem> getTipoConsiderarFeriado() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.NENHUM.name(), "Todos"));    	
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.FINANCEIRO.name(), "Financeiro"));
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.BIBLIOTECA.name(), "Biblioteca"));
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.ACADEMICO.name(), "Acadêmico"));
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.FINANCEIRO_BIBLIOTECA.name(), "Financeiro e Biblioteca"));
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.FINANCEIRO_ACADEMICO.name(), "Financeiro e Acadêmico"));
    	itens.add(new SelectItem(ConsiderarFeriadoEnum.BIBLIOTECA_ACADEMICO.name(), "Biblioteca e Acadêmico"));
    	return itens;
    }
    
    

    public String getConsiderarFeriado() {
    	if(considerarFeriado == null){
    		considerarFeriado = "TO";
    	}
		return considerarFeriado;
	}

	public void setConsiderarFeriado(String considerarFeriado) {
		this.considerarFeriado = considerarFeriado;
	}

	/**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);

        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return  Uteis.getCaminhoRedirecionamentoNavegacao("feriadoCons");
         
    }

    /**
     * Operação que inicializa as Interfaces Façades com os respectivos objetos de
     * persistência dos dados no banco de dados.
     */
    public List<SelectItem> getListaSelectItemCidade() {
        return (listaSelectItemCidade);
    }

    public void setListaSelectItemCidade(List<SelectItem> listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public FeriadoVO getFeriadoVO() {
    	if(this.feriadoVO == null){
    		feriadoVO = new FeriadoVO();
    	}
        return feriadoVO;
    }

    public void setFeriadoVO(FeriadoVO feriadoVO) {
        this.feriadoVO = feriadoVO;
    }

    /**
     * @return the consultarTodosOsAnos
     */
    public Boolean getConsultarTodosOsAnos() {
        return consultarTodosOsAnos;
    }

    /**
     * @param consultarTodosOsAnos the consultarTodosOsAnos to set
     */
    public void setConsultarTodosOsAnos(Boolean consultarTodosOsAnos) {
        this.consultarTodosOsAnos = consultarTodosOsAnos;
    }

    /**
     * @return the consultarNacional
     */
    public Boolean getConsultarNacional() {
        return consultarNacional;
    }

    /**
     * @param consultarNacional the consultarNacional to set
     */
    public void setConsultarNacional(Boolean consultarNacional) {
        this.consultarNacional = consultarNacional;
    }

    /**
     * @return the dataConsulta
     */
    public Date getDataConsulta() {
        return dataConsulta;
    }

    /**
     * @param dataConsulta the dataConsulta to set
     */
    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public List<TurmaVO> getListaTurmaHorarioAula() {
        if(listaTurmaHorarioAula == null){
            listaTurmaHorarioAula = new ArrayList<TurmaVO>(0);
        }
        return listaTurmaHorarioAula;
    }

    public void setListaTurmaHorarioAula(List<TurmaVO> listaTurmaHorarioAula) {
        this.listaTurmaHorarioAula = listaTurmaHorarioAula;
    }

	public Boolean getExcluirAulaProgramada() {
		if(excluirAulaProgramada == null){
			excluirAulaProgramada = true;
		}
		return excluirAulaProgramada;
	}

	public void setExcluirAulaProgramada(Boolean excluirAulaProgramada) {
		this.excluirAulaProgramada = excluirAulaProgramada;
	}
    
    public void verificarEventoFeriadoNacional(ValueChangeEvent event){
    	this.getFeriadoVO().setNacional((Boolean)event.getNewValue());
    }

	public String getFiltroCidade() {
		if(filtroCidade == null){
			filtroCidade = "";
		}
		return filtroCidade;
	}

	public void setFiltroCidade(String filtroCidade) {
		this.filtroCidade = filtroCidade;
	}
    
    

	
    
    
    
    

    
}
