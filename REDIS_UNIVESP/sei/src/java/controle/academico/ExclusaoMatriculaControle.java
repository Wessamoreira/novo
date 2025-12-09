package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ExclusaoMatriculaControle")
@Scope("viewScope")
@Lazy
public class ExclusaoMatriculaControle extends SuperControle implements Serializable {

    private MatriculaVO matriculaVO;
    private String valorConsultaAluno;
    private List<MatriculaVO> listaConsultaAluno;
    private String campoConsultaAluno;
    private UnidadeEnsinoVO unidadeEnsinoAluno;
    private List listaSelectItemUnidadeEnsino;
    private String motivoExclusao;
    private Date valorConsultaData;
    private Date valorConsultaDataFinal;

    public ExclusaoMatriculaControle() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void excluirMatricula() {
        String matricula = getMatriculaVO().getMatricula();
        try {
            if (getMatriculaVO().getMatricula().equals("")) {
                throw new Exception("Favor selecionar uma Matrícula para exclusão.");
            }
            if (getMotivoExclusao().equals("")) {
                throw new Exception("Favor informar o motivo da exclusão.");
            }
            MatriculaPeriodoVO matPer = (MatriculaPeriodoVO)getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getFacadeFactory().getMatriculaFacade().excluirMatriculaERegistrosRelacionados(getMatriculaVO(), matPer, getMotivoExclusao(),
                    getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setMatriculaVO(null);
            setMensagem("Matrícula { " + matricula + " } Excluída com sucesso.");
            setMensagemID("");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            matricula = null;
            setMotivoExclusao(null);
        }
    }

    public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(),
                        getUnidadeEnsinoAluno().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
                        getUnidadeEnsinoAluno().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
                        getUnidadeEnsinoAluno().getCodigo(), false, getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(null);
        setMensagemID("msg_entre_dados");
    }

    public void consultarAlunoPorMatricula() {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(),
                    getUnidadeEnsinoAluno().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
            validarMatriculaParaExclusao(objAluno);
            setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
            MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
                    obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
            validarMatriculaParaExclusao(objCompleto);
            setMatriculaVO(objCompleto);
            obj = null;
            objCompleto = null;
            setValorConsultaAluno("");
            setCampoConsultaAluno("");
            getListaConsultaAluno().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(null);
        }
    }

    private void validarMatriculaParaExclusao(MatriculaVO matriculaVO) throws Exception {
        if (matriculaVO.getSituacao().equals("CA") || matriculaVO.getSituacao().equals("TR")) {
            throw new Exception("Existe um cancelamento ou trancamento para essa matrícula. Exclua esse(s) registro(s) e tente novamente mais tarde.");
        }
        Integer numeroRecebimentos = getFacadeFactory().getNegociacaoRecebimentoFacade().consultarNumeroDeRecebimentosParaUmaMatricula(
                matriculaVO.getMatricula());
        Integer numeroContasNegociadas = getFacadeFactory().getNegociacaoContaReceberFacade().consultarNumeroDeRecebimentosParaUmaMatricula(
                matriculaVO.getMatricula());
        if (numeroRecebimentos > 0 || numeroContasNegociadas > 0) {
            throw new Exception(
                    "A matrícula selecionada possui contas recebidas ou negociadas. O estorno dessas contas deve ser feito se desejar prosseguir com a exclusão. Favor entrar em contato com o Departamento Financeiro.");
        }
        if(getFacadeFactory().getEmprestimoFacade().consultaExistenciaEmprestimosEmAbertoPorMatricula(matriculaVO.getMatricula(), matriculaVO.getAluno().getCodigo())){
        	throw new Exception("Não é possível excluir esta matrícula, pois existem emprestimos pendentes na biblioteca para o aluno "+matriculaVO.getAluno().getNome()+".");
		}
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void realizarLimpezaMatricula() {
        setMatriculaVO(null);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public boolean getApresentarCampoData() {
        if (getControleConsulta().getCampoConsulta().equals("dataExclusao") || getControleConsulta().getCampoConsulta().equals("dataMatricula")) {
            return true;
        }
        return false;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("dataExclusao", "Data Exclusão"));
        itens.add(new SelectItem("dataMatricula", "Data Matrícula"));
        return itens;
    }

    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
        }
        if (getControleConsulta().getCampoConsulta().equals("cpf")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
        }
        return "";
    }

    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ExclusaoMatriculaControle", "Iniciando Consultar Exclusão Matrícula", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getExclusaoMatriculaFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getExclusaoMatriculaFacade().consultarPorNomeAluno(getControleConsulta().getValorConsulta(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getExclusaoMatriculaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataExclusao")) {
                objs = getFacadeFactory().getExclusaoMatriculaFacade().consultarPorDataExclusao(getValorConsultaData(), getValorConsultaDataFinal(), getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataMatricula")) {
                objs = getFacadeFactory().getExclusaoMatriculaFacade().consultarPorDataMatricula(getValorConsultaData(), getValorConsultaDataFinal(), getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            registrarAtividadeUsuario(getUsuarioLogado(), "ExclusaoMatriculaControle", "Finalizando Consultar Exclusão Matrícula", "Consultando");
            return Uteis.getCaminhoRedirecionamentoNavegacao("exclusaoMatriculaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("exclusaoMatriculaCons.xhtml");
        }
    }

    public String novo() {
        removerObjetoMemoria(this);
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("exclusaoMatriculaForm.xhtml");
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("exclusaoMatriculaCons.xhtml");
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<MatriculaVO>(0);
        }
        return listaConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoAluno() {
        if (unidadeEnsinoAluno == null) {
            unidadeEnsinoAluno = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoAluno;
    }

    public void setUnidadeEnsinoAluno(UnidadeEnsinoVO unidadeEnsinoAluno) {
        this.unidadeEnsinoAluno = unidadeEnsinoAluno;
    }

    public String getMotivoExclusao() {
        if (motivoExclusao == null) {
            motivoExclusao = "";
        }
        return motivoExclusao;
    }

    public void setMotivoExclusao(String motivoExclusao) {
        this.motivoExclusao = motivoExclusao;
    }

    public Date getValorConsultaData() {
        if (valorConsultaData == null) {
            valorConsultaData = new Date();
        }
        return valorConsultaData;
    }

    public void setValorConsultaData(Date valorConsultaData) {
        this.valorConsultaData = valorConsultaData;
    }

    public Date getValorConsultaDataFinal() {
        if (valorConsultaDataFinal == null) {
            valorConsultaDataFinal = new Date();
        }
        return valorConsultaDataFinal;
    }

    public void setValorConsultaDataFinal(Date valorConsultaDataFinal) {
        this.valorConsultaDataFinal = valorConsultaDataFinal;
    }
}
