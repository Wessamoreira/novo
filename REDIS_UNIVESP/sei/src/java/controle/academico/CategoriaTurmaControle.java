
package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * categoriaTurmaForm.jsp categoriaTurmaCons.jsp) com as funcionalidades da classe <code>CategoriaTurma</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see CategoriaTurma
 * @see CategoriaTurmaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CategoriaTurmaVO;
import negocio.comuns.academico.TipoCategoriaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("CategoriaTurmaControle")
@Scope("viewScope")
@Lazy
public class CategoriaTurmaControle extends SuperControle implements Serializable {

    private CategoriaTurmaVO categoriaTurmaVO;
	private List listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;    
	private List<SelectItem> listaSelectItemTipoCategoria;
    
    public CategoriaTurmaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>CategoriaTurma</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         
    	removerObjetoMemoria(this);
        setCategoriaTurmaVO(new CategoriaTurmaVO());
        montarListaSelectItemTipoCategoria();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CategoriaTurma</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        CategoriaTurmaVO obj = (CategoriaTurmaVO) context().getExternalContext().getRequestMap().get("categoriaTurmaItens");
        obj = montarDadosCategoriaTurmaVOCompleto(obj);
        montarListaSelectItemTipoCategoria();
        obj.setNovoObj(Boolean.FALSE);
        setCategoriaTurmaVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
    }

    /**
     * Metodo responsavel por montar os dados do VO
     * @param obj
     * @return CategoriaTurmaVO
     */
    public CategoriaTurmaVO montarDadosCategoriaTurmaVOCompleto(CategoriaTurmaVO obj){
        try {
            return getFacadeFactory().getCategoriaTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new CategoriaTurmaVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CategoriaTurma</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getCategoriaTurmaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getCategoriaTurmaFacade().incluir(getCategoriaTurmaVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getCategoriaTurmaFacade().alterar(getCategoriaTurmaVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CategoriaTurmaCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("turma")) {
                objs = getFacadeFactory().getCategoriaTurmaFacade().consultarPorTurma(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoCategoria")) {
            	objs = getFacadeFactory().getCategoriaTurmaFacade().consultarPorTipoCategoria(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CategoriaTurmaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getCategoriaTurmaFacade().excluir(categoriaTurmaVO, getUsuarioLogado());
            setCategoriaTurmaVO(new CategoriaTurmaVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaForm.xhtml");
        }
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
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), this.getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), this.getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), this.getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		this.getCategoriaTurmaVO().getTurma().setCodigo(obj.getCodigo());
		this.getCategoriaTurmaVO().getTurma().setIdentificadorTurma(obj.getIdentificadorTurma());
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
	}
	
	public void limparDadosTurma() {
		this.getCategoriaTurmaVO().getTurma().setCodigo(0);
		this.getCategoriaTurmaVO().getTurma().setIdentificadorTurma("");
	}
	
	public void montarListaSelectItemTipoCategoria() {
		try {
			List<TipoCategoriaVO> lista = new ArrayList<TipoCategoriaVO>(0);
			lista = getFacadeFactory().getTipoCategoriaFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> selectItemTipoCategoria = new ArrayList<SelectItem>();
			selectItemTipoCategoria.add(new SelectItem("", ""));
			if (lista != null && !lista.isEmpty()) {
				for (TipoCategoriaVO tipoCat: lista) {
					selectItemTipoCategoria.add(new SelectItem(tipoCat.getCodigo(), tipoCat.getNome()));
				}
				setListaSelectItemTipoCategoria(selectItemTipoCategoria);
				setMensagemDetalhada("");
			} else {
				setListaSelectItemTipoCategoria(selectItemTipoCategoria);
				throw new ConsistirException("Não há nenhum tipo de categoria cadastro!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem>itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("tipoCategoria", "Tipo Categoria"));
        return itens;
    }

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
    
    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaTurmaCons.xhtml");
    }

    public CategoriaTurmaVO getCategoriaTurmaVO() {
        if (categoriaTurmaVO == null) {
            categoriaTurmaVO = new CategoriaTurmaVO();
        }
        return categoriaTurmaVO;
    }

    public void setCategoriaTurmaVO(CategoriaTurmaVO categoriaTurmaVO) {
        this.categoriaTurmaVO = categoriaTurmaVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        categoriaTurmaVO = null;
    }
    
	/**
	 * @return the listaConsultaTurma
	 */
	public List getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List getListaSelectItemTipoCategoria() {
		if (listaSelectItemTipoCategoria == null) {
			listaSelectItemTipoCategoria = new ArrayList(0);
		}
		return listaSelectItemTipoCategoria;
	}

	public void setListaSelectItemTipoCategoria(List listaSelectItemTipoCategoria) {
		this.listaSelectItemTipoCategoria = listaSelectItemTipoCategoria;
	}
}
