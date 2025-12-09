package controle.basico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cidadeForm.jsp cidadeCons.jsp) com as funcionalidades da classe <code>Cidade</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cidade
 * @see CidadeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AlterarSenhaControle")
@Scope("viewScope")
@Lazy
public class AlterarSenhaControle extends SuperControle implements Serializable {

    private UsuarioVO usuarioVO;
    private String userName;
    private String senha;
    private String mensagemRedefinicaoSenha;
    private Boolean permitirAlterarUsername;    

    public AlterarSenhaControle() throws Exception {
        setUsuarioVO(getUsuarioLogadoClone());
//        setLoginAnterior(getUsuarioVO().getUsername());
        setLogin(getUsuarioVO().getUsername());
        try {
            setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
        } catch (Exception e) {
        }
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    @PostConstruct
    public void init(){
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void alterarSenha() {
        try {
	    Uteis.validarSenha(getConfiguracaoGeralPadraoSistema(), getSenha());
            getFacadeFactory().getUsuarioFacade().alterarSenha( getUsuarioLogado(), getLoginAnterior(), getSenhaAnterior(), getLogin(), getSenha(), false);
            getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuarioLogado(), getSenha());
            setMensagemID("msg_dados_gravados");
        } catch (ConsistirException  e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception  e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void alterarUserNameSenhaAluno() {
        try {
            getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( getUsuarioVO(), getUserName(), getSenha(),true, getUsuarioLogado());
            getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuarioVO(), getSenha());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void redefinirSenhaPadrao() {
        try {
            if (getConfiguracaoGeralPadraoSistema().getGerarSenhaCpfAluno()) {
                getUsuarioVO().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getUsuarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioVO()));
                setUserName(Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
                setSenha(Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
                if(Uteis.isAtributoPreenchido(getUsuarioVO().getPessoa().getRegistroAcademico())) {
                    setSenha(getUsuarioVO().getPessoa().getRegistroAcademico());
                }
                setMensagemRedefinicaoSenha("O Username e Senha do aluno foram redefinidas com sucesso para => Username: " + Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()) + " Senha: " + Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
            } else {
                List matriculas = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoa(getUsuarioVO().getPessoa().getCodigo(), 0, false, getUsuarioVO());
                if (matriculas.isEmpty()) {
                    getUsuarioVO().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getUsuarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioVO()));
                    setUserName(Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
                    setSenha(Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
                    if(Uteis.isAtributoPreenchido(getUsuarioVO().getPessoa().getRegistroAcademico())) {
                        setSenha(getUsuarioVO().getPessoa().getRegistroAcademico());
                    }
                    setMensagemRedefinicaoSenha("O Username e Senha do aluno foram redefinidas com sucesso para => Username: " + Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()) + " Senha: " + Uteis.removerMascara(getUsuarioVO().getPessoa().getCPF()));
                } else {
                    MatriculaVO mat = (MatriculaVO)matriculas.get(0);
                    setUserName(mat.getMatricula());
                    setSenha(mat.getMatricula());
                    if(Uteis.isAtributoPreenchido(mat.getAluno().getRegistroAcademico())) {
                        setSenha(mat.getAluno().getRegistroAcademico());
                    }
                    setMensagemRedefinicaoSenha("O Username e Senha do aluno foram redefinidas com sucesso para => Username: " + mat.getMatricula() + " Senha: " + mat.getMatricula());
                }
            }
            getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( getUsuarioVO(), getUserName(), getSenha() , true, getUsuarioLogado());
            getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuarioVO(), getSenha());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        getControleConsultaOtimizado().getListaConsulta().clear();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoSenhaAlunoCons.xhtml");
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public String editar() {
        UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
        try {
            setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
        } catch (Exception e) {
        }
        setUsuarioVO(obj);
        setUserName(obj.getUsername());
        setSenha("");
        return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoSenhaAlunoForm.xhtml");
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            getControleConsultaOtimizado().getListaConsulta().clear();
            getControleConsultaOtimizado().setLimitePorPagina(10);
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorNomeAlunoAlteracaoSenha(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),
                        getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeGegistroPorNomeAlunoAlteracaoSenha(getControleConsulta().getValorConsulta(),
                        getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado()));
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorMatriculaAlunoAlteracaoSenha(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),
                        getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeGegistroPorMatriculaAlunoAlteracaoSenha(getControleConsulta().getValorConsulta(),
                        getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado()));
            }
            getControleConsultaOtimizado().setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoSenhaAlunoCons.xhtml");
        } catch (Exception e) {
            getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoSenhaAlunoCons.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();

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

    @Override
    public void anularDataModelo() {
        setControleConsultaOtimizado(null);
    }

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    public String getUserName() {
        if (userName == null) {
            userName = "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSenha() {
        if (senha == null) {
            senha = "";
        }
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the permitirAlterarUsername
     */
    public Boolean getPermitirAlterarUsername() {
        return permitirAlterarUsername;
    }

    /**
     * @param permitirAlterarUsername the permitirAlterarUsername to set
     */
    public void setPermitirAlterarUsername(Boolean permitirAlterarUsername) {
        this.permitirAlterarUsername = permitirAlterarUsername;
    }

    /**
     * @return the mensagemRedefinicaoSenha
     */
    public String getMensagemRedefinicaoSenha() {
        if (mensagemRedefinicaoSenha == null) {
            mensagemRedefinicaoSenha = "";
        }
        return mensagemRedefinicaoSenha;
    }

    /**
     * @param mensagemRedefinicaoSenha the mensagemRedefinicaoSenha to set
     */
    public void setMensagemRedefinicaoSenha(String mensagemRedefinicaoSenha) {
        this.mensagemRedefinicaoSenha = mensagemRedefinicaoSenha;
    }
}
