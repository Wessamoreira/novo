/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

/**
 * 
 * @author otimize-ti
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AbonoFaltaVO;
import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoJustificativaFaltaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AbonoFaltaControle")
@Scope("viewScope")
@Lazy
public class AbonoFaltaControle extends SuperControle implements Serializable {

    protected AbonoFaltaVO abonoFaltaVO;
    protected String campoConsultaMatricula;
    protected String valorConsultaMatricula;
    protected List listaConsultaMatricula;
    protected String mensagemConsultaFalta;
    private List<SelectItem> listaSelectItemTipoJustificativa;
	//private MatriculaVO matriculaVO;

    public AbonoFaltaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setAbonoFaltaVO(new AbonoFaltaVO());
        setCampoConsultaMatricula("");
        setValorConsultaMatricula("");
        setListaConsultaMatricula(new ArrayList(0));
        inicializarListasSelectItemTodosComboBox();
        setMensagemConsultaFalta("");
        setMensagemID("msg_entre_prmconsulta");

    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Turma</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setAbonoFaltaVO(new AbonoFaltaVO());
        setCampoConsultaMatricula("");
        setValorConsultaMatricula("");
        setMensagemConsultaFalta("");
        setListaConsultaMatricula(new ArrayList(0));
        inicializarListasSelectItemTodosComboBox();
        montarListaSelectItemTipoJustificativa();
        getAbonoFaltaVO().setTipoAbono("AB");
        getAbonoFaltaVO().setResponsavel(getUsuarioLogadoClone());		
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Turma</code> para alteração. O objeto
     * desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
    	try {
        AbonoFaltaVO obj = (AbonoFaltaVO) context().getExternalContext().getRequestMap().get("abonoItens");
        getFacadeFactory().getAbonoFaltaFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setAbonoFaltaVO(obj);        
        Ordenacao.ordenarLista(obj.getDisciplinaAbonoVOs(), "registroAula.data");
        inicializarListasSelectItemTodosComboBox();
        montarListaSelectItemTipoJustificativa();
        setMensagemConsultaFalta("");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaForm.xhtml");
    	} catch (Exception e) {
    		e.getMessage();
    		return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaForm.xhtml");	
    	}
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Turma</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
	public void persistir() {
		try {
			getFacadeFactory().getPessoaFacade().carregarDados(getAbonoFaltaVO().getPessoa(), NivelMontarDados.TODOS, getUsuarioLogado());
			getAbonoFaltaVO().setResponsavel(getUsuarioLogadoClone());
			getAbonoFaltaVO().setTipoJustificativaFaltaVO(getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorChavePrimeira(getAbonoFaltaVO().getTipoJustificativaFaltaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFacadeFactory().getAbonoFaltaFacade().persistir(getAbonoFaltaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                objs = getFacadeFactory().getAbonoFaltaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }

            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getAbonoFaltaFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaCons.xhtml");
        }
    }

    public void consultarMatricula() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaMatricula().equals("matricula")) {
                if (!getValorConsultaMatricula().isEmpty()) {
                    MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
                    if (!obj.getMatricula().equals("")) {
                        objs.add(obj);
                    }
                } else {
                    setMensagemID("msg_entre_dados");
                }
            }
            if (getCampoConsultaMatricula().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("cpf")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCPF(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultaMatricula());
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("situacao")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorSituacao(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("situacaoFinanceira")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorSituacaoFinanceira(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("nomeResponsavel")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeResponsavel(getValorConsultaMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaMatricula(objs);
            if (!objs.isEmpty()) {
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            setListaConsultaMatricula(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaComboMatricula() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("cpf", "CPF"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
        return itens;
    }

    public List getTipoConsultaComboTipoAbono() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("abonar", "ABONAR "));
        itens.add(new SelectItem("justificar", "JUSTIFICAR "));
		if (Uteis.isAtributoPreenchido(getAbonoFaltaVO()) && getAbonoFaltaVO().getTipoAbono().equals("TT")) {
			itens.add(new SelectItem("TT", "TRANSFERÊNCIA DE TURMA"));
		}
        return itens;
    }

/*    public List getTipoConsultaComboTipoJustificativa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("DO", "Doença Infectocontagiosa"));
        itens.add(new SelectItem("GR", "Gravidez"));
        itens.add(new SelectItem("OU", "Outro"));
        return itens;
    }*/

    @SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoJustificativa() throws Exception {
		List<TipoJustificativaFaltaVO> tipoJustVOs = getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		for (TipoJustificativaFaltaVO item : tipoJustVOs) {
			objs.add(new SelectItem(item.getCodigo(), item.getDescricao()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		setListaSelectItemTipoJustificativa(objs);
	}
    
    public Boolean getApresentarDadosJustificativa() {
        return getAbonoFaltaVO().getTipoAbono().equals("justificar");
    }

    public Boolean getApresentarComboBoxSituacao() {
        if (getCampoConsultaMatricula().equals("situacao")) {
            return true;
        }
        return false;
    }

    public Boolean getSituacaoFinanceira() {
        if (getCampoConsultaMatricula().equals("situacaoFinanceira")) {
            return true;
        }
        return false;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoMatricula();
        Enumeration keys = situacaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemSituacaoFinanceiraMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoFinanceiraMatricula();
        Enumeration keys = situacaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public void selecionarMatricula() throws Exception {
        try {
            MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
            getAbonoFaltaVO().setMatricula(matriculaVO);
            getAbonoFaltaVO().setPessoa(matriculaVO.getAluno());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarDadosAbonoFalta() throws Exception {
        try {
            MatriculaVO obj = new MatriculaVO();
            obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(abonoFaltaVO.getMatricula().getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
            getAbonoFaltaVO().setPessoa(obj.getAluno());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String getMascaraConsulta() {
        if (getCampoConsultaMatricula().equals("data")) {
            return "return mascara(this.form,'formMatricula:valorConsulta','99/99/9999',event);";
        }
        if (getCampoConsultaMatricula().equals("cpf")) {
            return "return mascara(this.form,'formMatricula:valorConsulta','999.999.999-99',event);";
        }
        return "";
    }

    public void verificarFaltas() throws Exception {
        try {
        	getAbonoFaltaVO().getDisciplinaAbonoVOs().clear();
        	if(abonoFaltaVO.getMatricula().getMatricula().trim().isEmpty()){
        		throw new Exception("O campo MATRÍCULA deve ser informado.");
        	}
            List<DisciplinaAbonoVO> disciplinaAbonoVOs = getFacadeFactory().getAbonoFaltaFacade().consultarDisciplinaComFalta(abonoFaltaVO.getMatricula().getMatricula(), abonoFaltaVO.getDataInicio(), abonoFaltaVO.getDataFim(), abonoFaltaVO.getSituacaoAula(), false, getUsuarioLogado());
            if (!disciplinaAbonoVOs.isEmpty()) {
            	getAbonoFaltaVO().setDisciplinaAbonoVOs(disciplinaAbonoVOs);
                setMensagemConsultaFalta("");
            } else {
                if (!getAbonoFaltaVO().getAbonarFaltaFuturosRegistrosAula()) {
                    setMensagemConsultaFalta(UteisJSF.internacionalizar("msg_AbonoFalta_nenhumRegistroEncontrado"));
                }
            }
            setMensagemID("msg_dados_consultados");
            setMensagemConsultaFalta("");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAbonarTodasFaltas() {
        List<DisciplinaAbonoVO> lista = abonoFaltaVO.getDisciplinaAbonoVOs();
        if (lista != null && !lista.isEmpty()) {
            for (DisciplinaAbonoVO disc : lista) {
                disc.setFaltaAbonada(Boolean.TRUE);
            }
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TurmaVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getAbonoFaltaFacade().excluir(abonoFaltaVO, getUsuarioLogado());
            setAbonoFaltaVO(new AbonoFaltaVO());
            novo();			
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaForm.xhtml");
        }

    }

    public String irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
        return "";
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

    public void inicializarListasSelectItemTodosComboBox() {
    }

    public List getListaSelectItemSituacaoAula() {
    	List itens = new ArrayList(0);
    	itens.add(new SelectItem("AF", "Apenas Faltas"));
    	itens.add(new SelectItem("AP", "Apenas Presente"));
    	itens.add(new SelectItem("", "Ambos"));
    	return itens;
    }
	
    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("abonoFaltaCons.xhtml");
    }

    /**
     * @return the abonoFaltaVO
     */
    public AbonoFaltaVO getAbonoFaltaVO() {
        if (abonoFaltaVO == null) {
            abonoFaltaVO = new AbonoFaltaVO();
        }
        return abonoFaltaVO;
    }

    /**
     * @param abonoFaltaVO
     *            the abonoFaltaVO to set
     */
    public void setAbonoFaltaVO(AbonoFaltaVO abonoFaltaVO) {
        this.abonoFaltaVO = abonoFaltaVO;
    }

    /**
     * @return the campoConsultaMatricula
     */
    public String getCampoConsultaMatricula() {
        if (campoConsultaMatricula == null) {
            campoConsultaMatricula = "";
        }
        return campoConsultaMatricula;
    }

    /**
     * @param campoConsultaMatricula
     *            the campoConsultaMatricula to set
     */
    public void setCampoConsultaMatricula(String campoConsultaMatricula) {
        this.campoConsultaMatricula = campoConsultaMatricula;
    }

    /**
     * @return the valorConsultaMatricula
     */
    public String getValorConsultaMatricula() {
        if (valorConsultaMatricula == null) {
            valorConsultaMatricula = "";
        }
        return valorConsultaMatricula;
    }

    /**
     * @param valorConsultaMatricula
     *            the valorConsultaMatricula to set
     */
    public void setValorConsultaMatricula(String valorConsultaMatricula) {
        this.valorConsultaMatricula = valorConsultaMatricula;
    }
    

    /**
     * @return the listaConsultaMatricula
     */
    public List getListaConsultaMatricula() {
        if (listaConsultaMatricula == null) {
            listaConsultaMatricula = new ArrayList(0);
        }
        return listaConsultaMatricula;
    }

    /**
     * @param listaConsultaMatricula
     *            the listaConsultaMatricula to set
     */
    public void setListaConsultaMatricula(List listaConsultaMatricula) {
        this.listaConsultaMatricula = listaConsultaMatricula;
    }

    /**
     * @return the mensagemConsultaFalta
     */
    public String getMensagemConsultaFalta() {
        if (mensagemConsultaFalta == null) {
            mensagemConsultaFalta = "";
        }
        return mensagemConsultaFalta;
    }

    /**
     * @param mensagemConsultaFalta
     *            the mensagemConsultaFalta to set
     */
    public void setMensagemConsultaFalta(String mensagemConsultaFalta) {
        this.mensagemConsultaFalta = mensagemConsultaFalta;
    }
    
    public List<SelectItem> getListaSelectItemTipoJustificativa() {
		if(listaSelectItemTipoJustificativa == null){
			listaSelectItemTipoJustificativa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoJustificativa;
	}

	public void setListaSelectItemTipoJustificativa(List<SelectItem> listaSelectItemTipoJustificativa) {
		this.listaSelectItemTipoJustificativa = listaSelectItemTipoJustificativa;
	}
	
	public void limparDadosMatricula() {
		getAbonoFaltaVO().setMatricula(new MatriculaVO());
		getAbonoFaltaVO().setPessoa(new PessoaVO());
	}
}
