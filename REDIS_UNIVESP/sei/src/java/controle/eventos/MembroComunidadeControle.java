package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas membroComunidadeForm.jsp
 * membroComunidadeCons.jsp) com as funcionalidades da classe <code>MembroComunidade</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MembroComunidade
 * @see MembroComunidadeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.MembroComunidadeVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

@Controller("MembroComunidadeControle")
@Scope("request")
@Lazy
public class MembroComunidadeControle extends SuperControle implements Serializable {

    private MembroComunidadeVO membroComunidadeVO;
    private PessoaVO pessoaVO;
    private String pessoa_Erro;
    public Boolean verificarCpf;
    public Boolean consultarPessoa;
    public Boolean editarDados;
    private List listaSelectItemCidade;
    private List listaSelectItemNaturalidade;
    private List listaSelectItemNacionalidade;
    private FormacaoAcademicaVO formacaoAcademicaVO;
    private String disciplina_Erro;
    private FiliacaoVO filiacaoVO;
    private String turno_Erro;
    public String tipoPessoa = "MC";

    public MembroComunidadeControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>MembroComunidade</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setMembroComunidadeVO(new MembroComunidadeVO());
        setPessoaVO(new PessoaVO());
        inicializarListasSelectItemTodosComboBox();
        setFiliacaoVO(new FiliacaoVO());
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        setVerificarCpf(this.validarCadastroPorCpf());
        setConsultarPessoa(Boolean.TRUE);
        setEditarDados(Boolean.FALSE);
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>MembroComunidade</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            PessoaVO pessoaVO = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoa");
            MembroComunidadeVO obj = getFacadeFactory().getMembroComunidadeFacade().consultarPorCodigoPessoa(pessoaVO.getCodigo(), false, getUsuarioLogado());
            obj.setNovoObj(Boolean.FALSE);
            setMembroComunidadeVO(obj);
            inicializarListasSelectItemTodosComboBox();
            setFiliacaoVO(new FiliacaoVO());
            setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            pessoaVO.setNovoObj(Boolean.FALSE);
            setPessoaVO(pessoaVO);
            setConsultarPessoa(Boolean.FALSE);
            setMensagemID("msg_dados_editar");
            return "editar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>MembroComunidade</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("MembroComunidade");
            getPessoaVO().setMembroComunidade(Boolean.TRUE);
            membroComunidadeVO.setPessoa(pessoaVO);
            if (membroComunidadeVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getMembroComunidadeFacade().incluir(membroComunidadeVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            } else {
                getFacadeFactory().getMembroComunidadeFacade().alterar(membroComunidadeVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MembroComunidadeCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            getFacadeFactory().getPessoaFacade().setIdEntidade("MembroComunidade");
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getControleConsulta().getValorConsulta(), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getControleConsulta().getValorConsulta(), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>MembroComunidadeVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("MembroComunidade");
            getFacadeFactory().getMembroComunidadeFacade().excluir(membroComunidadeVO);
            setMembroComunidadeVO(new MembroComunidadeVO());
            // getFacadeFactory().getPessoaFacade().excluir(pessoaVO);
            setPessoaVO(new PessoaVO());
            setFiliacaoVO(new FiliacaoVO());
            setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setVerificarCpf(this.validarCadastroPorCpf());
            setConsultarPessoa(Boolean.TRUE);
            setEditarDados(Boolean.FALSE);
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public String editarDadosPessoa() throws Exception {
        setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        MembroComunidadeVO obj = getFacadeFactory().getMembroComunidadeFacade().consultarPorCodigoPessoa(pessoaVO.getCodigo(), false, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setMembroComunidadeVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setFiliacaoVO(new FiliacaoVO());
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        getPessoaVO().setNovoObj(Boolean.FALSE);
        setConsultarPessoa(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return "editar";

    }

    public String fechar() {
        setMensagemDetalhada("", "");
        return "consultar";
    }

    public void validarPessoaCadastrada() {
        try {
            PessoaVO pessoaExistente = new PessoaVO();
            String mensagem = "";
            if (verificarCpf.equals(Boolean.TRUE)) {
                if (getPessoaVO().getCPF().length() == 14) {
                    String cpf = getPessoaVO().getCPF();
                    // getPessoaVO().verificaCPF(cpf);
                    pessoaExistente = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                } else {
                    throw new Exception("CPF inválido.");
                }
            } else {
                if (getPessoaVO().getNome().equals("")) {
                    throw new Exception("O campo NOME (Dados Pessoais) deve ser informado.");
                }
                if (getFiliacaoVO().getNome().equals("")) {
                    throw new Exception("O campo NOME DA MÃE (Dados Pessoais) deve ser informado.");
                }
                if (getPessoaVO().getDataNasc() == null) {
                    throw new Exception("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
                }
                pessoaExistente = getFacadeFactory().getPessoaFacade().consultarPessoaPorNomeDataNascNomeMae(getPessoaVO().getNome(), getFiliacaoVO().getNome(), getPessoaVO().getDataNasc(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            if (pessoaExistente.getCodigo().intValue() != 0) {
                mensagem = "Já existe um ";
                if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
                    mensagem += "Professor cadastrado";
                } else if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
                    mensagem += "Funcionário cadastrado";
                    setPessoaVO(pessoaExistente);
                    setEditarDados(Boolean.TRUE);
                } else if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
                    mensagem += "Aluno cadastrado";
                } else if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {

                    mensagem += "Candidato cadastrado";
                } else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
                    setPessoaVO(pessoaExistente);
                    setEditarDados(Boolean.TRUE);
                    mensagem += "Membro da Comunidade cadastrado";
                }

                if (verificarCpf.equals(Boolean.TRUE)) {
                    mensagem += " com este CPF.";
                } else {
                    mensagem += " com este NOME.";
                }
                throw new Exception(mensagem);
            }
            setConsultarPessoa(Boolean.FALSE);

        } catch (Exception e) {
            pessoaVO.setCPF("");
            setConsultarPessoa(Boolean.TRUE);
            setMensagemDetalhada(e.getMessage());
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>Filiacao</code> para o objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public String adicionarFiliacao() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                filiacaoVO.setAluno(getPessoaVO().getCodigo());
            }
            getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
            this.setFiliacaoVO(new FiliacaoVO());
            setMensagemID("msg_dados_adicionados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>Filiacao</code> para edição pelo
     * usuário.
     */
    public String editarFiliacao()  {
    	try {
	        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacao");
	        setFiliacaoVO(obj.getClone());
	    	setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
        return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>Filiacao</code> do objeto <code>pessoaVO</code> da
     * classe <code>Pessoa</code>
     */
    public String removerFiliacao() throws Exception {
        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacao");
        getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
        setMensagemID("msg_dados_excluidos");
        return "editar";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>FormacaoAcademica</code> para o objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public String adicionarFormacaoAcademica() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
            }
            getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
            this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setMensagemID("msg_dados_adicionados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>FormacaoAcademica</code> para edição
     * pelo usuário.
     */
    public String editarFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
        setFormacaoAcademicaVO(obj);
        return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public String removerFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
        getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
        setMensagemID("msg_dados_excluidos");
        return "editar";
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
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = membroComunidadeVO.getPessoa().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            membroComunidadeVO.getPessoa().setNome(pessoa.getNome());
            this.setPessoa_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            membroComunidadeVO.getPessoa().setNome("");
            membroComunidadeVO.getPessoa().setCodigo(0);
            this.setPessoa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCidadePorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCidadeFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Cidade</code>.
     */
    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCidade() {
        try {
            montarListaSelectItemCidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Naturalidade</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemNaturalidade() {
        try {
            montarListaSelectItemNaturalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Nacionalidade</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem de dados,
     * isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemNacionalidade() {
        try {
            montarListaSelectItemNacionalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Naturalidade</code>.
     */
    public void montarListaSelectItemNaturalidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemNaturalidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Nacionalidade</code>.
     */
    public void montarListaSelectItemNacionalidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPaizPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PaizVO obj = (PaizVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemNacionalidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCidade();
        montarListaSelectItemNaturalidade();
        montarListaSelectItemNacionalidade();
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>estadoEmissaoRG</code>
     */
    public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable estados = (Hashtable) Dominios.getEstado();
        Enumeration keys = estados.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estados.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>estadoCivil</code>
     */
    public List getListaSelectItemEstadoCivilPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
        Enumeration keys = estadoCivils.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estadoCivils.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>sexo</code>
     */
    public List getListaSelectItemSexoPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable sexos = (Hashtable) Dominios.getSexo();
        Enumeration keys = sexos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) sexos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>escolaridade</code>
     */
    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
//        List objs = new ArrayList(0);
//        objs.add(new SelectItem("", ""));
//        Hashtable escolaridadeFormacaoAcademicas = (Hashtable) Dominios.getEscolaridadeFormacaoAcademica();
//        Enumeration keys = escolaridadeFormacaoAcademicas.keys();
//        while (keys.hasMoreElements()) {
//            String value = (String) keys.nextElement();
//            String label = (String) escolaridadeFormacaoAcademicas.get(value);
//            objs.add(new SelectItem(value, label));
//        }
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
        Enumeration keys = situacaoFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipo</code>
     */
    public List getListaSelectItemTipoFiliacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
        Enumeration keys = tipoFiliacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoFiliacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    public String getPessoa_Erro() {
        return pessoa_Erro;
    }

    public void setPessoa_Erro(String pessoa_Erro) {
        this.pessoa_Erro = pessoa_Erro;
    }

    public MembroComunidadeVO getMembroComunidadeVO() {
        return membroComunidadeVO;
    }

    public void setMembroComunidadeVO(MembroComunidadeVO membroComunidadeVO) {
        this.membroComunidadeVO = membroComunidadeVO;
    }

    public List getListaSelectItemCidade() {
        return listaSelectItemCidade;
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public List getListaSelectItemNaturalidade() {
        return listaSelectItemNaturalidade;
    }

    public void setListaSelectItemNaturalidade(List listaSelectItemNaturalidade) {
        this.listaSelectItemNaturalidade = listaSelectItemNaturalidade;
    }

    public List getListaSelectItemNacionalidade() {
        return listaSelectItemNacionalidade;
    }

    public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
        this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
    }

    public PessoaVO getPessoaVO() {
        return pessoaVO;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public FormacaoAcademicaVO getFormacaoAcademicaVO() {
        return formacaoAcademicaVO;
    }

    public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
        this.formacaoAcademicaVO = formacaoAcademicaVO;
    }

    public FiliacaoVO getFiliacaoVO() {
        return filiacaoVO;
    }

    public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
        this.filiacaoVO = filiacaoVO;
    }

    public Boolean getConsultarPessoa() {
        return consultarPessoa;
    }

    public void setConsultarPessoa(Boolean consultarPessoa) {
        this.consultarPessoa = consultarPessoa;
    }

    public Boolean getEditarDados() {
        return editarDados;
    }

    public void setEditarDados(Boolean editarDados) {
        this.editarDados = editarDados;
    }

    public Boolean getVerificarCpf() {
        return verificarCpf;
    }

    public void setVerificarCpf(Boolean verificarCpf) {
        this.verificarCpf = verificarCpf;
    }
}
