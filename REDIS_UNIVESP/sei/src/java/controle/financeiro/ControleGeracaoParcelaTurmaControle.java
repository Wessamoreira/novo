package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas ControleGeracaoParcelaTurmaForm.jsp
 * ControleGeracaoParcelaTurmaCons.jsp) com as funcionalidades da classe <code>ControleGeracaoParcelaTurma</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ControleGeracaoParcelaTurma
 * @see ControleGeracaoParcelaTurmaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ControleGeracaoParcelaTurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("ControleGeracaoParcelaTurmaControle")
@Scope("viewScope")
@Lazy
public class ControleGeracaoParcelaTurmaControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ControleGeracaoParcelaTurmaVO controleGeracaoParcelaTurmaVO;

    public ControleGeracaoParcelaTurmaControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ControleGeracaoParcelaTurma</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setControleGeracaoParcelaTurmaVO(new ControleGeracaoParcelaTurmaVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ControleGeracaoParcelaTurma</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        ControleGeracaoParcelaTurmaVO obj = (ControleGeracaoParcelaTurmaVO) context().getExternalContext().getRequestMap().get("controleGeracaoParcelaTurmaItem");
        obj.setNovoObj(Boolean.FALSE);
        setControleGeracaoParcelaTurmaVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ControleGeracaoParcelaTurma</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (this.getControleGeracaoParcelaTurmaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getControleGeracaoParcelaTurmaFacade().incluir(getControleGeracaoParcelaTurmaVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getControleGeracaoParcelaTurmaFacade().alterar(getControleGeracaoParcelaTurmaVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ControleGeracaoParcelaTurmaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getControleGeracaoParcelaTurmaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaCons");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaCons");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getControleGeracaoParcelaTurmaFacade().excluir(this.getControleGeracaoParcelaTurmaVO(), getUsuarioLogado());
            setControleGeracaoParcelaTurmaVO(new ControleGeracaoParcelaTurmaVO());
            setMensagemID("msg_dados_excluidos");
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
//            return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaForm");
        }
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
        this.consultar();
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
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
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleGeracaoParcelaTurmaCons");
    }

    public ControleGeracaoParcelaTurmaVO getControleGeracaoParcelaTurmaVO() {
    	if(controleGeracaoParcelaTurmaVO == null){
    		controleGeracaoParcelaTurmaVO = new ControleGeracaoParcelaTurmaVO();
    	}
        return controleGeracaoParcelaTurmaVO;
    }

    public void setControleGeracaoParcelaTurmaVO(ControleGeracaoParcelaTurmaVO controleGeracaoParcelaTurmaVO) {
        this.controleGeracaoParcelaTurmaVO = controleGeracaoParcelaTurmaVO;
    }

    public void setarNullNaDataDeVencimento() {
        if (getControleGeracaoParcelaTurmaVO().getUsarDataVencimentoDataMatricula()) {
            getControleGeracaoParcelaTurmaVO().setDataVencimentoMatricula(null);
        }
    }

    public void setarNullNoMesEAnoDasMensalidades() {
        if (getControleGeracaoParcelaTurmaVO().getMesSubsequenteMatricula()) {
            getControleGeracaoParcelaTurmaVO().setMesVencimentoPrimeiraMensalidade(null);
            getControleGeracaoParcelaTurmaVO().setAnoVencimentoPrimeiraMensalidade(null);
        } else if (getControleGeracaoParcelaTurmaVO().getMesDataBaseGeracaoParcelas()) {
            getControleGeracaoParcelaTurmaVO().setDiaVencimentoPrimeiraMensalidade(null);
            getControleGeracaoParcelaTurmaVO().setMesVencimentoPrimeiraMensalidade(null);
            getControleGeracaoParcelaTurmaVO().setAnoVencimentoPrimeiraMensalidade(null);
        }
    }

    public void selecionarOpcaoMesSubSequenteAMatricula() {
        if (getControleGeracaoParcelaTurmaVO().getMesSubsequenteMatricula()) {
            getControleGeracaoParcelaTurmaVO().setMesDataBaseGeracaoParcelas(false);
            setarNullNoMesEAnoDasMensalidades();
        }
    }

    public void selecionarOpcaoMesDataBaseGeracaoParcelas() {
        if (getControleGeracaoParcelaTurmaVO().getMesDataBaseGeracaoParcelas()) {
            getControleGeracaoParcelaTurmaVO().setMesSubsequenteMatricula(false);
            setarNullNoMesEAnoDasMensalidades();
        }
    }
    
    public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoAcrescentarPlanoFinanCursoRenovacaoAlemExistentePlanoFinanceiroAluno() {
		if (getControleGeracaoParcelaTurmaVO().getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno()) {
			getControleGeracaoParcelaTurmaVO().setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(Boolean.FALSE);
			getControleGeracaoParcelaTurmaVO().setZerarValorDescontoPlanoFinanceiroAluno(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoZerarValorDoDesconto() {
		if (getControleGeracaoParcelaTurmaVO().getZerarValorDescontoPlanoFinanceiroAluno()) {
//			getProcessoMatriculaCalendarioVO().setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(Boolean.FALSE);
			getControleGeracaoParcelaTurmaVO().setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoUtilizarDescontoConfiguracaoAtual() {
		if (getControleGeracaoParcelaTurmaVO().getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual()) {
//			getProcessoMatriculaCalendarioVO().setZerarValorDescontoPlanoFinanceiroAluno(Boolean.FALSE);
			getControleGeracaoParcelaTurmaVO().setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean.FALSE);
		}
	}
    
}
