package controle.basico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.ArtefatoAjudaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.basico.ArtefatoAjuda;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * artefatoAjudaForm.jsp artefatoAjudaCons.jsp) com as funcionalidades da classe <code>ArtefatoAjuda</code>.
 * Implemtação da camada controle (Backing Bean).
 * @author Paulo Taucci
 * @see SuperControle
 * @see ArtefatoAjuda
 * @see ArtefatoAjudaVO
*/
@Controller("ArtefatoAjudaControle") 
@Scope("session")
@Lazy
public class ArtefatoAjudaControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8910416853660242399L;
	private ArtefatoAjudaVO artefatoAjudaVO;
	private String modulo;
	private String submodulo;
	private String recurso;
	private String filtroIndice;
    private List<ArtefatoAjudaVO> listaConsultaIndice;

    public ArtefatoAjudaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        getControleConsultaOtimizado().setLimitePorPagina(12);
        getControleConsultaOtimizado().setPaginaAtual(0);
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>ArtefatoAjuda</code>
    * para edição pelo usuário da aplicação.
    * @author Paulo Taucci
    */
    @SuppressWarnings("finally")
	public String novo() {         
    	removerObjetoMemoria(this);
        setArtefatoAjudaVO(new ArtefatoAjudaVO());
        try {
			getArtefatoAjudaVO().setResponsavelCadastro(getUsuarioLogadoClone());
        	setMensagemID("msg_entre_dados");
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoAjudaForm");
        }
    }
    
    public String novoVisaoProfessor() {
    	setArtefatoAjudaVO(new ArtefatoAjudaVO());
        try {
			getArtefatoAjudaVO().setResponsavelCadastro(getUsuarioLogadoClone());
        	setMensagemID("msg_entre_dados");
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	return "editar";
        }
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ArtefatoAjuda</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    * @author Paulo Taucci
    */
    @SuppressWarnings("finally")
	public String editar() {
        ArtefatoAjudaVO obj = (ArtefatoAjudaVO)context().getExternalContext().getRequestMap().get("artefatoAjudaItem");
        try {
			obj.setResponsavelCadastro(getUsuarioLogadoClone());
	        obj.setNovoObj(Boolean.FALSE);
	        setArtefatoAjudaVO(obj);
	        setMensagemID("msg_dados_editar");
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoAjudaForm");
        }
        
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ArtefatoAjuda</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    * @author Paulo Taucci
    */
    @SuppressWarnings("finally")
	public String persistir() {
        try {
            getFacadeFactory().getArtefatoAjudaFacade().persistir(artefatoAjudaVO, getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
//            return "editar";
        	return "";
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP artefatoAjudaCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    * @author Paulo Taucci
    */
    @PostConstruct
	public String consultar() {
        try {            
            getFacadeFactory().getArtefatoAjudaFacade().consultarArtefatos(getControleConsultaOtimizado(), getControleConsultaOtimizado().getValorConsulta(), getModulo(), getSubmodulo(), getRecurso());
            setMensagemID("msg_dados_consultados");
        }catch (Exception e) {        	
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoAjudaCons");
        }
    }
    
    public void scrollListener(DataScrollEvent event) {
    	getControleConsultaOtimizado().setPage(event.getPage());
    	getControleConsultaOtimizado().setPaginaAtual(event.getPage());
    	consultar();
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ArtefatoAjudaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     * @author Paulo Taucci
     */
    @SuppressWarnings("finally")
	public String excluir() {
        try {
            getFacadeFactory().getArtefatoAjudaFacade().excluir(artefatoAjudaVO);
            setArtefatoAjudaVO( new ArtefatoAjudaVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
//            return "editar";
            return "";
        }
    }


    public ArtefatoAjudaVO getArtefatoAjudaVO() {
    	if (artefatoAjudaVO == null) {
    		artefatoAjudaVO = new ArtefatoAjudaVO();
    	}
        return artefatoAjudaVO;
    }
     
    public void setArtefatoAjudaVO(ArtefatoAjudaVO artefatoAjudaVO) {
        this.artefatoAjudaVO = artefatoAjudaVO;
    }
    
    /**
     * Método criado para atribuir ao responsável assinalar desatualizado o  usuário logado para que seja mantido um
     * registro. Esse campo não é alterado na tela.
     * @author Paulo Taucci
     */
    public void realizarAtribuicaoResponsavelAssinalarDesatualizado() {
    	try {
			getArtefatoAjudaVO().setResponsavelAssinalarDesatualizado(getUsuarioLogadoClone());
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public List<ArtefatoAjudaVO> getListaConsultaIndice() {
		if(listaConsultaIndice == null) {
			listaConsultaIndice =  new ArrayList<ArtefatoAjudaVO>(0);
			try {				
				listaConsultaIndice = getFacadeFactory().getArtefatoAjudaFacade().consultarArtefatoIndice("");				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaConsultaIndice;
	}

	public void setListaConsultaIndice(List<ArtefatoAjudaVO> listaConsultaIndice) {
		this.listaConsultaIndice = listaConsultaIndice;
	}

	public String getModulo() {
		if(modulo == null) {
			modulo = "";
		}
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getSubmodulo() {
		if(submodulo == null) {
			submodulo = "";
		}
		return submodulo;
	}

	public void setSubmodulo(String submodulo) {
		this.submodulo = submodulo;
	}

	public String getRecurso() {
		if(recurso == null) {
			recurso = "";
		}
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
    
	
	
	public String getFiltroIndice() {
		if(filtroIndice == null) {
			filtroIndice =  "";
		}
		return filtroIndice;
	}

	public void setFiltroIndice(String filtroIndice) {
		this.filtroIndice = filtroIndice;
	}

	public Boolean filtrarIndice(ArtefatoAjudaVO artefatoAjudaVO) {
		return getFiltroIndice().trim().isEmpty() || Uteis.removerAcentos(artefatoAjudaVO.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(getFiltroIndice().toUpperCase()))
				|| artefatoAjudaVO.getArtefatoAjudaVOs().stream().anyMatch(a -> { return Uteis.removerAcentos(a.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(getFiltroIndice().toUpperCase()))
					|| a.getArtefatoAjudaVOs().stream().anyMatch(b -> Uteis.removerAcentos(b.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(getFiltroIndice().toUpperCase())));});
		
	}
    
}