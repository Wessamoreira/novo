package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas TipoCatalogoForm.jsp
 * TipoCatalogoCons.jsp) com as funcionalidades da classe <code>TipoCatalogo</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see TipoCatalogo
 * @see TipoCatalogoVO
 */
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.biblioteca.enumeradores.TipoImpressaoComprovanteBibliotecaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.biblioteca.Exemplar;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import controle.arquitetura.SuperControle.MSG_TELA;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("BuscaBibliotecaControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("unchecked")
public class BuscaBibliotecaControle extends SuperControleRelatorio implements Serializable {

    private CatalogoVO catalogoVO;
    private ExemplarVO exemplarVO;
    private List listaConsultaBuscaAvancada;
    private String codigoTomboBuscaAvancada;
    private String tituloBuscaAvancada;
    private String assuntoBuscaAvancada;
    private String palavrasChaveBuscaAvancada;
    private String autoresBuscaAvancada;
    private String classificacaoBuscaAvancada;
    private String isbnBuscaAvancada;
    private String issnBuscaAvancada;
    private String cutterPhaBuscaAvancada;	
    private String valorConsultaOrdenacao;
    private List listaSelectItemTipoCatalogo;
    private Boolean reservarExemplar;
    private List<ExemplarVO> listaExemplares;
    private Boolean mostrarModalExemplares;
    private List<ExemplarVO> listaAdicionadosGuia;
    private List<ExemplarVO> listaAdicionadosReserva;
    private List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva;
    private Boolean tipoPessoaAluno;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;
    private Boolean tipoPessoaProfessor;
    private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List listaConsultaProfessor;
    private Boolean tipoPessoaFuncionario;
    private String campoConsultaFuncionario;
    private String valorConsultaFuncionario;
    private List listaConsultaFuncionario;
    private String tipoPessoa;
    private String matricula;
    private String matriculaFuncionario;
    private PessoaVO pessoaVO;
    private String usernameLiberarReserva;
    private String senhaLiberarReserva;
    private Boolean mostrarModalEscolherPessoa;
    private Boolean mostrarModalLoginSenha;
    private Boolean mostrarModalReservadoComSucesso;
    private String mensagemCatalogosReservadosComSucesso;
    private Boolean apenasExemplarDisponivel;
    private Boolean apenasExemplarDisponivelAvancado;
    private List<BibliotecaVO> listaBiblioteca;
    private List listaItemBiblioteca;
    private BibliotecaVO biblioteca;
    private List listaSelectItemAreaDeConhecimento;
    private String textoComprovante;
	private List<ReservaVO> reservaVOs;
	private ImpressoraVO impressoraVO;
	private List<SelectItem> listaSelectItemImpressora;
	private ArquivoMarc21VO arquivoMarc21VO;
	private ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO;
	private Boolean fazerDownloadMarc21;

    public BuscaBibliotecaControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
        carregarListaBiblioteca();
        montarListaSelectItemAreaDeConhecimento();
    }
    
    public void montarListaSelectItemAreaDeConhecimento() {
        try {
        	setListaSelectItemAreaDeConhecimento(UtilSelectItem.getListaSelectItem(getFacadeFactory().getAreaConhecimentoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado()), "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getPossuiSumarioDigital() {
        if (catalogoVO.getArquivoSumarioCapaVOs().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void selecionarExemplarReserva() {
        ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
        try {
            setExemplarVO(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        } finally {
            obj = null;
        }
    }

    public void fecharModal() {
        setMostrarModalReservadoComSucesso(Boolean.FALSE);
        setMostrarModalLoginSenha(Boolean.FALSE);
        setMostrarModalEscolherPessoa(Boolean.FALSE);
        setMostrarModalExemplares(Boolean.FALSE);
    }

    public void fecharModalLimpandoListas() {
        selecionarFiltro();
        limparListas();
        setMensagemCatalogosReservadosComSucesso(null);
        setMostrarModalReservadoComSucesso(Boolean.FALSE);
        setMostrarModalLoginSenha(Boolean.FALSE);
        setMostrarModalEscolherPessoa(Boolean.FALSE);
        setMostrarModalExemplares(Boolean.FALSE);
    }

    public void limparListas() {
        getListaCatalogosAdicionadosNaGuiaReserva().clear();
        getListaConsulta().clear();
        getListaConsultaBuscaAvancada().clear();
        getListaAdicionadosGuia().clear();
        getListaAdicionadosReserva().clear();
        setControleConsultaOtimizado(null);
        getControleConsultaOtimizado().setPage(1);
        getControleConsultaOtimizado().setPaginaAtual(1);
        setMensagemDetalhada("");
    }

    public void consultarExemplares() {
        getListaExemplares().clear();
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
        if (obj == null) {
            obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoBuscaAvancadaItens");
        }
        try {
            setCatalogoVO(obj);
            setListaExemplares(getFacadeFactory().getExemplarFacade().consultarPorCodigoCatalogoNaoInutilizados(obj.getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMostrarModalExemplares(Boolean.TRUE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMostrarModalExemplares(Boolean.FALSE);
        } finally {
            obj = null;
        }
    }

    public void consultarExemplaresParaReserva() {
        getListaExemplares().clear();
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
        if (obj == null) {
            obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoBuscaAvancadaItens");
        }
        try {
            setCatalogoVO(obj);
            setListaExemplares(getFacadeFactory().getExemplarFacade().consultarPorCodigoCatalogoNaoInutilizados(obj.getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMostrarModalExemplares(Boolean.TRUE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMostrarModalExemplares(Boolean.FALSE);
        } finally {
            obj = null;
        }
    }

    public void consultarExemplaresAvancada() {
        getListaExemplares().clear();
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoAvancada");
        try {
            setListaExemplares(getFacadeFactory().getExemplarFacade().consultarPorCodigoCatalogoSituacaoAtual(obj.getCodigo(),
                    SituacaoExemplar.DISPONIVEL.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            obj = null;
        }
    }

    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	public String consultar() {
		getControleConsultaOtimizado().setLimitePorPagina(5);
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCatalogoFacade().consultarTelaBuscaCatalogos(getControleConsulta().getValorConsulta(), getBiblioteca().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), getApenasExemplarDisponivel()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultaTotalDeRegistroRapidaPorBuscaCatalogo(getControleConsulta().getValorConsulta(), getBiblioteca().getCodigo(), getUsuarioLogado(), getApenasExemplarDisponivel()));
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

     public void irPaginaInicial() throws Exception {
    	 limparListas();
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

	public void consultarAvancada() {
		try {
			limparListas();
			setListaConsultaBuscaAvancada(getFacadeFactory().getCatalogoFacade().consultarTelaBuscaCatalogosAvancada(getCodigoTomboBuscaAvancada(), getTituloBuscaAvancada(), getAssuntoBuscaAvancada(), getPalavrasChaveBuscaAvancada(), getAutoresBuscaAvancada(), getClassificacaoBuscaAvancada(), getIsbnBuscaAvancada(), getIssnBuscaAvancada(), getBiblioteca().getCodigo(), getCatalogoVO().getAreaConhecimentoVO().getCodigo(), getCutterPhaBuscaAvancada(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getApenasExemplarDisponivelAvancado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaBuscaAvancada(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void imprimirGuia() {
    	try {
    		if (!getListaAdicionadosGuia().isEmpty()) {
	    		getSuperParametroRelVO().setNomeDesignIreport(Exemplar.designerGuiaReservaExemplar());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(Exemplar.caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("GUIA DE EMPRÉSTIMO");
				getSuperParametroRelVO().setListaObjetos(getListaAdicionadosGuia());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(Exemplar.caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (getUnidadeEnsinoLogado().getCodigo().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino("");
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				}
				if (getBiblioteca().getCodigo().equals(0)) {
					getSuperParametroRelVO().setBiblioteca("");
				} else {
					getSuperParametroRelVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
    		} else {
    			setMensagemID("msg_relatorio_sem_dados");
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("assunto", "Assunto"));
        itens.add(new SelectItem("autor", "Autor"));
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("palavraChave", "Palavra-Chave"));
        itens.add(new SelectItem("isbnIssn", "ISBN/ISSN"));
        itens.add(new SelectItem("tipoPublicacao", "Tipo de Publicação"));
        return itens;
    }

    public List getTipoConsultaComboOrdenacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("assuntoCres", "Assunto - Crescente"));
        itens.add(new SelectItem("assuntoDesc", "Assunto - Decrescente"));
        itens.add(new SelectItem("autorCres", "Autor - Crescente"));
        itens.add(new SelectItem("autorDesc", "Autor - Decrescente"));
        itens.add(new SelectItem("tituloCres", "Titulo - Crescente"));
        itens.add(new SelectItem("tituloDesc", "Titulo - Decrescente"));
        return itens;
    }

    
    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoCatalogo</code>
     */
    public void montarListaSelectItemTipoCatalogo() throws Exception {
        List<TipoCatalogoVO> resultadoConsulta = new ArrayList<TipoCatalogoVO>(0);
        try {
            resultadoConsulta = consultarTipoCatalogoPorNome("");
            setListaSelectItemTipoCatalogo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            resultadoConsulta = null;
        }
    }

    private List<TipoCatalogoVO> consultarTipoCatalogoPorNome(String nomePrm) throws Exception {
        List<TipoCatalogoVO> lista = getFacadeFactory().getTipoCatalogoFacade().consultarPorNome(nomePrm, false,
                Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void adicionarAGuia() {
        try {
            ExemplarVO exemplarVO = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
            getListaAdicionadosGuia().add(exemplarVO);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_adicionados");
        	setMostrarModalExemplares(Boolean.FALSE);
        } catch (Exception e) {
            setMostrarModalExemplares(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            exemplarVO = null;
        }
    }

    public void removerExemplarGuia() {
        ExemplarVO exemplarVO = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarGuiaItens");
        getListaAdicionadosGuia().remove(exemplarVO);
        try {
            setMostrarModalExemplares(Boolean.FALSE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            exemplarVO = null;
        }
    }

    /**
     * Método adicionará o catálogo da lista de consulta da tela de busca para a guia de reserva. Essa operação validará
     * os seguintes casos: - Se o usuário está adicionando o mesmo catálogo, ou se ele já tem uma reserva para esse
     * catálogo. - Se o número de reservas está dentro do limite do número permitido na configuração da biblioteca.
     */
    public void adicionarCatalogoNaGuiaDeReserva() {
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
        if (obj == null) {
            obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoBuscaAvancadaItens");
        }
        try {
        	ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getBiblioteca().getCodigo(), getTipoPessoa(), getMatricula(), getUsuarioLogado());
        	if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()){
				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(getUsuarioLogado().getPessoa().getCodigo(), getBiblioteca().getCodigo(), getTipoPessoa(), getUsuarioLogado());
			}
//            setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        	if (!confBibVO.getPermiteReserva()) {
            	throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRealizarReservaCatalogo").replace("{0}", getBiblioteca().getNome()));
            }
            for (CatalogoVO catalogo : getListaCatalogosAdicionadosNaGuiaReserva()) {
                if (obj.getCodigo().equals(catalogo.getCodigo())) {
                    throw new Exception("Catálogo já adicionado na lista de reserva!");
                }
            }
            if(getVisaoAlunoControle() != null){
				if(getFacadeFactory().getEmprestimoFacade().consultarPessoaPossuiExemplarEmprestadoPorCatalogoBiblioteca(obj, getBiblioteca(), getVisaoAlunoControle().getPessoaVO())){
					throw new Exception("Você possui um EXEMPLAR deste catálogo emprestado, por isto não é possível realizar uma reserva.");
				}
				
			}
            setCatalogoVO(obj);
            getListaCatalogosAdicionadosNaGuiaReserva().add(getCatalogoVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            obj = null;
        }
    }

    /**
     * Método removerá o catálogo da guia de reserva. Não faz nenhuma validação para executar a operação.
     */
    public void removerCatalogoDaGuiaDeReserva() {
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoReservaItens");
        try {
            getListaCatalogosAdicionadosNaGuiaReserva().remove(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            obj = null;
        }
    }

    /**
     * Método fará a verificação do tipo de modal aparecerá quando o usuário decidir executar a reserva. Se não houver
     * usuário logado, quer dizer que estamos tratando de um terminal, onde o usuário precisará informar o Login e Senha
     * do sistema para continuar a reserva. Porém se existe usuário logado, então temos um funcionário da biblioteca
     * fazendo a reserva para uma pessoa no balcão. Nesse caso, um modal de escolha do tipo de pessoa aparecerá, para o
     * funcionário fazer a pesquisa e então continuar a reserva.
     */
    public void verificarTipoModalParaContinuarReserva() {
        try {
        	 executarValidacaoSimulacaoVisaoAluno();
            if (getUsuarioLogado().getCodigo() == 0) {
                setMostrarModalLoginSenha(Boolean.TRUE);
            } else {
                if ((!getUsuarioLogado().getPessoa().getAluno() && !getUsuarioLogado().getPessoa().getProfessor()) || getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
                    setMostrarModalEscolherPessoa(Boolean.TRUE);
                } else {
                    setPessoaVO(getUsuarioLogado().getPessoa());
                    executarReservaCatalogos();
                }
            }
        } catch (Exception e) {
            setMostrarModalLoginSenha(Boolean.FALSE);
            setMostrarModalEscolherPessoa(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método que realiza a execução da reserva do(s) catálogo(s) que estão na guia de reserva para o usuário. Primeiro
     * ele valida os seguintes aspectos: - Verifica se o usuário possui reservas para algum dos catálogos da lista no
     * período corrente. - Verifica se o número de reservas do usuário existentes para o período corrente ultrapassa o
     * limite máximo pela biblioteca. Após essas validações, os dados da reserva são montados, respeitando o prazo da
     * validade da reserva especificado na configuração da biblioteca, e um modal contendo as informações da reserva é
     * mostrado para o usuário.
     */
    public void executarReservaCatalogos() {
        try {
        	if(getPessoaVO().getCodigo() == 0){
				setMensagemDetalhada("O campo PESSOA deve ser informado.");
			}else{
        	ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getBiblioteca().getCodigo(), getTipoPessoa(), getMatricula(), getUsuarioLogado());
        	getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(getPessoaVO().getCodigo(), getBiblioteca().getCodigo(), getTipoPessoa(), getUsuarioLogado());			
        	setReservaVOs(getFacadeFactory().getCatalogoFacade().executarReservaCatalogos(getListaCatalogosAdicionadosNaGuiaReserva(), getBiblioteca(), getPessoaVO(), getTipoPessoa(), getMatricula(), confBibVO, getUsuarioLogado()));
			setMensagemCatalogosReservadosComSucesso(getReservaVOs().get(0).getMensagemListaCatalogoReservado());
			if(getReservaVOs().get(0).getCodigo() == 0){
				setReservaVOs(null);
			}
			setMostrarModalReservadoComSucesso(Boolean.TRUE);
            setMostrarModalEscolherPessoa(Boolean.FALSE);
            setMostrarModalLoginSenha(Boolean.FALSE);
			}
        } catch (Exception e) {
            setMostrarModalReservadoComSucesso(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void reservar() {
        ExemplarVO exemplarVO = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
        try {
//			if (exemplarVO.getSituacaoAtual().equals(SituacaoExemplar.RESERVADO.getValor())) {
//				setMostrarModalExemplares(Boolean.FALSE);
//				throw new Exception("Não é possível reservar um livro com uma reserva existente.");
//			}
        	
            getListaAdicionadosReserva().add(exemplarVO);
            setMostrarModalExemplares(Boolean.FALSE);
        } catch (Exception e) {
            setMostrarModalExemplares(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            exemplarVO = null;
        }
    }

    public void removerExemplarReserva() {
        ExemplarVO exemplarVO = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarReserva");
        getListaAdicionadosReserva().remove(exemplarVO);
        try {
            setMostrarModalExemplares(Boolean.FALSE);
        } catch (Exception e) {
            setMostrarModalExemplares(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            exemplarVO = null;
        }
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("buscaBiblioteca.xhtml");
    }

    public void selecionarFiltro() {
        setMatriculaFuncionario(null);
        setMatricula(null);
        setPessoaVO(null);
        setarFalseNosFiltros();
        if (getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
            setTipoPessoaAluno(true);
        } else if (getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
            setTipoPessoaProfessor(true);
        } else if (getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
            setTipoPessoaFuncionario(true);
        }
    }

    private void setarFalseNosFiltros() {
        setTipoPessoaAluno(false);
        setTipoPessoaProfessor(false);
        setTipoPessoaFuncionario(false);
    }

    public List getTipoConsultaComboTipoPessoa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem(TipoPessoa.ALUNO.getValor(), "Aluno"));
        itens.add(new SelectItem(TipoPessoa.PROFESSOR.getValor(), "Professor"));
        itens.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), "Funcionário"));
        return itens;
    }

    public void consultarAluno() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsultaAluno().equals("nome")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
                        getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(),
                        getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        MatriculaVO objAluno = new MatriculaVO();
        try {
            objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatricula()
                        + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setPessoaVO(objAluno.getAluno());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatricula(null);
            setPessoaVO(null);
        } finally {
            objAluno = null;
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                setMatricula(obj.getMatricula());
                setPessoaVO(obj.getAluno());
            }
        } finally {
            obj = null;
            getListaConsultaAluno().clear();
            setValorConsultaAluno(null);
            setCampoConsultaAluno(null);
        }
    }

    public void limparCampoAluno() {
        setMatricula(null);
        setPessoaVO(null);
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matricula"));
        return itens;
    }

    /**
     * Métodos do rich:modalPanel de Professor.
     * */
    public void consultarProfessor() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(),
                        TipoPessoa.PROFESSOR.getValor(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(),
                        getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProfessor(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarProfessorPorMatricula() throws Exception {
        FuncionarioVO objProfessor = new FuncionarioVO();
        try {
            objProfessor = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(getMatriculaFuncionario(),
                    getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            if (objProfessor.getMatricula().equals("")) {
                throw new Exception("Professor de matrícula " + getMatriculaFuncionario()
                        + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setPessoaVO(objProfessor.getPessoa());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaFuncionario(null);
            setPessoaVO(null);
        } finally {
            objProfessor = null;
        }
    }

    public void selecionarProfessor() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                setMatriculaFuncionario(obj.getMatricula());
                setPessoaVO(obj.getPessoa());
            }
        } finally {
            obj = null;
            getListaConsultaProfessor().clear();
            setValorConsultaProfessor(null);
            setCampoConsultaProfessor(null);
        }
    }

    public void limparCampoProfessor() {
        setMatriculaFuncionario(null);
        setPessoaVO(null);
    }

    public List getTipoConsultaComboProfessor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matricula"));
        return itens;
    }

    /**
     * Métodos do rich:modalPanel de Funcionário.
     * */
    public void consultarFuncionario() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(),
                        getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs.add(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(),
                        getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFuncionario(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarFuncionarioPorMatricula() throws Exception {
        FuncionarioVO objFuncionario = new FuncionarioVO();
        try {
            objFuncionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(getMatriculaFuncionario(),
                    getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            if (objFuncionario.getMatricula().equals("")) {
                throw new Exception("Funcionário de matrícula " + getMatriculaFuncionario()
                        + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setPessoaVO(objFuncionario.getPessoa());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaFuncionario(null);
            setPessoaVO(null);
        } finally {
            objFuncionario = null;
        }
    }

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                setMatriculaFuncionario(obj.getMatricula());
                setPessoaVO(obj.getPessoa());
            }
        } finally {
            obj = null;
            getListaConsultaFuncionario().clear();
            setValorConsultaFuncionario(null);
            setCampoConsultaFuncionario(null);
        }
    }

    public void limparCampoFuncionario() {
        setMatriculaFuncionario(null);
        setPessoaVO(null);
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matricula"));
        return itens;
    }

    public Boolean getIsMostrarBotaoAdicionarAGuia() {
        ExemplarVO exemplar = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
        if (exemplar.getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor())) {
            return true;
        } else {
            return false;
        }
    }

    public CatalogoVO getCatalogoVO() {
        if (catalogoVO == null) {
            catalogoVO = new CatalogoVO();
        }
        return catalogoVO;
    }

    public void setCatalogoVO(CatalogoVO catalogoVO) {
        this.catalogoVO = catalogoVO;
    }

    public List getListaConsultaBuscaAvancada() {
        if (listaConsultaBuscaAvancada == null) {
            listaConsultaBuscaAvancada = new ArrayList(0);
        }
        return listaConsultaBuscaAvancada;
    }

    public void setListaConsultaBuscaAvancada(List listaConsultaBuscaAvancada) {
        this.listaConsultaBuscaAvancada = listaConsultaBuscaAvancada;
    }

    public String getValorConsultaOrdenacao() {
        if (valorConsultaOrdenacao == null) {
            valorConsultaOrdenacao = "";
        }
        return valorConsultaOrdenacao;
    }

    public void setValorConsultaOrdenacao(String valorConsultaOrdenacao) {
        this.valorConsultaOrdenacao = valorConsultaOrdenacao;
    }

    public List getListaSelectItemTipoCatalogo() {
        if (listaSelectItemTipoCatalogo == null) {
            listaSelectItemTipoCatalogo = new ArrayList(0);
        }
        return listaSelectItemTipoCatalogo;
    }

    public void setListaSelectItemTipoCatalogo(List listaSelectItemTipoCatalogo) {
        this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
    }

    public void setReservarExemplar(Boolean reservarExemplar) {
        this.reservarExemplar = reservarExemplar;
    }

    public Boolean getReservarExemplar() {
        if (reservarExemplar == null) {
            reservarExemplar = false;
        }
        return reservarExemplar;
    }

    public List<ExemplarVO> getListaExemplares() {
        if (listaExemplares == null) {
            listaExemplares = new ArrayList<ExemplarVO>(0);
        }
        return listaExemplares;
    }

    public void setListaExemplares(List<ExemplarVO> listaExemplares) {
        this.listaExemplares = listaExemplares;
    }

    public void setExemplarVO(ExemplarVO exemplarVO) {
        this.exemplarVO = exemplarVO;
    }

    public ExemplarVO getExemplarVO() {
        if (exemplarVO == null) {
            exemplarVO = new ExemplarVO();
        }
        return exemplarVO;
    }

    public void setMostrarModalExemplares(Boolean mostrarModalExemplares) {
        this.mostrarModalExemplares = mostrarModalExemplares;
    }

    public Boolean getMostrarModalExemplares() {
        if (mostrarModalExemplares == null) {
            mostrarModalExemplares = Boolean.FALSE;
        }
        return mostrarModalExemplares;
    }

    public void setListaAdicionadosGuia(List<ExemplarVO> listaAdicionadosGuia) {
        this.listaAdicionadosGuia = listaAdicionadosGuia;
    }

    public List<ExemplarVO> getListaAdicionadosGuia() {
        if (listaAdicionadosGuia == null) {
            listaAdicionadosGuia = new ArrayList<ExemplarVO>(0);
        }
        return listaAdicionadosGuia;
    }

    public void setListaAdicionadosReserva(List<ExemplarVO> listaAdicionadosReserva) {
        this.listaAdicionadosReserva = listaAdicionadosReserva;
    }

    public List<ExemplarVO> getListaAdicionadosReserva() {
        if (listaAdicionadosReserva == null) {
            listaAdicionadosReserva = new ArrayList<ExemplarVO>(0);
        }
        return listaAdicionadosReserva;
    }

    public String getTituloBuscaAvancada() {
        if (tituloBuscaAvancada == null) {
            tituloBuscaAvancada = "";
        }
        return tituloBuscaAvancada;
    }

    public void setTituloBuscaAvancada(String tituloBuscaAvancada) {
        this.tituloBuscaAvancada = tituloBuscaAvancada;
    }

    public String getAssuntoBuscaAvancada() {
        if (assuntoBuscaAvancada == null) {
            assuntoBuscaAvancada = "";
        }
        return assuntoBuscaAvancada;
    }

    public void setAssuntoBuscaAvancada(String assuntoBuscaAvancada) {
        this.assuntoBuscaAvancada = assuntoBuscaAvancada;
    }

    public String getPalavrasChaveBuscaAvancada() {
        if (palavrasChaveBuscaAvancada == null) {
            palavrasChaveBuscaAvancada = "";
        }
        return palavrasChaveBuscaAvancada;
    }

    public void setPalavrasChaveBuscaAvancada(String palavrasChaveBuscaAvancada) {
        this.palavrasChaveBuscaAvancada = palavrasChaveBuscaAvancada;
    }

    public String getAutoresBuscaAvancada() {
        if (autoresBuscaAvancada == null) {
            autoresBuscaAvancada = "";
        }
        return autoresBuscaAvancada;
    }

    public void setAutoresBuscaAvancada(String autoresBuscaAvancada) {
        this.autoresBuscaAvancada = autoresBuscaAvancada;
    }

    public String getIsbnBuscaAvancada() {
        if (isbnBuscaAvancada == null) {
            isbnBuscaAvancada = "";
        }
        return isbnBuscaAvancada;
    }

    public void setIsbnBuscaAvancada(String isbnBuscaAvancada) {
        this.isbnBuscaAvancada = isbnBuscaAvancada;
    }

    public String getIssnBuscaAvancada() {
        if (issnBuscaAvancada == null) {
            issnBuscaAvancada = "";
        }
        return issnBuscaAvancada;
    }

    public void setIssnBuscaAvancada(String issnBuscaAvancada) {
        this.issnBuscaAvancada = issnBuscaAvancada;
    }

    public Boolean getTipoPessoaAluno() {
        if (tipoPessoaAluno == null) {
            tipoPessoaAluno = Boolean.FALSE;
        }
        return tipoPessoaAluno;
    }

    public void setTipoPessoaAluno(Boolean tipoPessoaAluno) {
        this.tipoPessoaAluno = tipoPessoaAluno;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public Boolean getTipoPessoaProfessor() {
        if (tipoPessoaProfessor == null) {
            tipoPessoaProfessor = Boolean.FALSE;
        }
        return tipoPessoaProfessor;
    }

    public void setTipoPessoaProfessor(Boolean tipoPessoaProfessor) {
        this.tipoPessoaProfessor = tipoPessoaProfessor;
    }

    public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
        return campoConsultaProfessor;
    }

    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
        return valorConsultaProfessor;
    }

    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    public List getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList(0);
        }
        return listaConsultaProfessor;
    }

    public void setListaConsultaProfessor(List listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }

    public Boolean getTipoPessoaFuncionario() {
        if (tipoPessoaFuncionario == null) {
            tipoPessoaFuncionario = Boolean.FALSE;
        }
        return tipoPessoaFuncionario;
    }

    public void setTipoPessoaFuncionario(Boolean tipoPessoaFuncionario) {
        this.tipoPessoaFuncionario = tipoPessoaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
            ;
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMatriculaFuncionario() {
        if (matriculaFuncionario == null) {
            matriculaFuncionario = "";
        }
        return matriculaFuncionario;
    }

    public void setMatriculaFuncionario(String matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public PessoaVO getPessoaVO() {
        if (pessoaVO == null) {
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "";
        }
        return tipoPessoa;
    }

    public void setUsernameLiberarReserva(String usernameLiberarReserva) {
        this.usernameLiberarReserva = usernameLiberarReserva;
    }

    public String getUsernameLiberarReserva() {
        if (usernameLiberarReserva == null) {
            usernameLiberarReserva = "";
        }
        return usernameLiberarReserva;
    }

    public void setSenhaLiberarReserva(String senhaLiberarReserva) {
        this.senhaLiberarReserva = senhaLiberarReserva;
    }

    public String getSenhaLiberarReserva() {
        if (senhaLiberarReserva == null) {
            senhaLiberarReserva = "";
        }
        return senhaLiberarReserva;
    }

    public void setMostrarModalEscolherPessoa(Boolean mostrarModalEscolherPessoa) {
        this.mostrarModalEscolherPessoa = mostrarModalEscolherPessoa;
    }

    public Boolean getMostrarModalEscolherPessoa() {
        if (mostrarModalEscolherPessoa == null) {
            mostrarModalEscolherPessoa = Boolean.FALSE;
        }
        return mostrarModalEscolherPessoa;
    }

    public void setMostrarModalLoginSenha(Boolean mostrarModalLoginSenha) {
        this.mostrarModalLoginSenha = mostrarModalLoginSenha;
    }

    public Boolean getMostrarModalLoginSenha() {
        if (mostrarModalLoginSenha == null) {
            mostrarModalLoginSenha = Boolean.FALSE;
        }
        return mostrarModalLoginSenha;
    }

    public void setMostrarModalReservadoComSucesso(Boolean mostrarModalReservadoComSucesso) {
        this.mostrarModalReservadoComSucesso = mostrarModalReservadoComSucesso;
    }

    public Boolean getMostrarModalReservadoComSucesso() {
        if (mostrarModalReservadoComSucesso == null) {
            mostrarModalReservadoComSucesso = Boolean.FALSE;
        }
        return mostrarModalReservadoComSucesso;
    }

    public void setListaCatalogosAdicionadosNaGuiaReserva(List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva) {
        this.listaCatalogosAdicionadosNaGuiaReserva = listaCatalogosAdicionadosNaGuiaReserva;
    }

    public List<CatalogoVO> getListaCatalogosAdicionadosNaGuiaReserva() {
        if (listaCatalogosAdicionadosNaGuiaReserva == null) {
            listaCatalogosAdicionadosNaGuiaReserva = new ArrayList<CatalogoVO>(0);
        }
        return listaCatalogosAdicionadosNaGuiaReserva;
    }

    public void setMensagemCatalogosReservadosComSucesso(String mensagemCatalogosReservadosComSucesso) {
        this.mensagemCatalogosReservadosComSucesso = mensagemCatalogosReservadosComSucesso;
    }

    public String getMensagemCatalogosReservadosComSucesso() {
        if (mensagemCatalogosReservadosComSucesso == null) {
            mensagemCatalogosReservadosComSucesso = "";
        }
        return mensagemCatalogosReservadosComSucesso;
    }

    public Boolean getApenasExemplarDisponivel() {
        if (apenasExemplarDisponivel == null) {
            apenasExemplarDisponivel = false;
        }
        return apenasExemplarDisponivel;
    }

    public void setApenasExemplarDisponivel(Boolean apenasExemplarDisponivel) {
        this.apenasExemplarDisponivel = apenasExemplarDisponivel;
    }

    public Boolean getApenasExemplarDisponivelAvancado() {
        if (apenasExemplarDisponivelAvancado == null) {
            apenasExemplarDisponivelAvancado = false;
        }
        return apenasExemplarDisponivelAvancado;
    }

    public void setApenasExemplarDisponivelAvancado(Boolean apenasExemplarDisponivelAvancado) {
        this.apenasExemplarDisponivelAvancado = apenasExemplarDisponivelAvancado;
    }

    public String getCodigoTomboBuscaAvancada() {
        return codigoTomboBuscaAvancada;
    }

    public void setCodigoTomboBuscaAvancada(String codigoTomboBuscaAvancada) {
        this.codigoTomboBuscaAvancada = codigoTomboBuscaAvancada;
    }
     public List<BibliotecaVO> getListaBiblioteca() {
     if(listaBiblioteca == null){
         listaBiblioteca = new ArrayList();
      }
         return listaBiblioteca;
        }

    public void setListaBiblioteca(List<BibliotecaVO> listaBiblioteca) {
        this.listaBiblioteca = listaBiblioteca;
    }

    public BibliotecaVO getBiblioteca() {
        if(biblioteca == null){
            biblioteca = new BibliotecaVO();
        }
        return biblioteca;
    }

    public void setBiblioteca(BibliotecaVO biblioteca) {
        this.biblioteca = biblioteca;
    }

    public List getListaItemBiblioteca() {
        if(listaItemBiblioteca == null){
            listaItemBiblioteca = new ArrayList(0);
        }
        return listaItemBiblioteca;
    }

    public void setListaItemBiblioteca(List listaItemBiblioteca) {
        this.listaItemBiblioteca = listaItemBiblioteca;
    }
    
    public void carregarListaBiblioteca(){
    	try{
    		setControleConsultaOtimizado(null);
    		listaBiblioteca = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsinoNivelComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
    		if(!listaBiblioteca.isEmpty()){
				getBiblioteca().setCodigo(listaBiblioteca.get(0).getCodigo());
				carregarDadosBiblioteca();
			}
    		setListaItemBiblioteca(UtilSelectItem.getListaSelectItem(listaBiblioteca, "codigo", "nome", false));   
    	} catch (Exception e) {
    	}
    }
    
    public void carregarDadosBiblioteca(){
		try {
			setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			limparListas();
		} catch (Exception e) {
			
		}
		
	}

	public List getListaSelectItemAreaDeConhecimento() {
		if (listaSelectItemAreaDeConhecimento == null) {
			listaSelectItemAreaDeConhecimento = new ArrayList(0);
		}
		return listaSelectItemAreaDeConhecimento;
	}

	public void setListaSelectItemAreaDeConhecimento(List listaSelectItemAreaDeConhecimento) {
		this.listaSelectItemAreaDeConhecimento = listaSelectItemAreaDeConhecimento;
	}

	public String getClassificacaoBuscaAvancada() {
		if (classificacaoBuscaAvancada == null) {
			classificacaoBuscaAvancada = "";
		}
		return classificacaoBuscaAvancada;
	}

	public void setClassificacaoBuscaAvancada(String classificacaoBuscaAvancada) {
		this.classificacaoBuscaAvancada = classificacaoBuscaAvancada;
	}

	public String getCutterPhaBuscaAvancada() {
		if (cutterPhaBuscaAvancada == null) {
			cutterPhaBuscaAvancada = "";
		}
		return cutterPhaBuscaAvancada;
	}

	public void setCutterPhaBuscaAvancada(String cutterPhaBuscaAvancada) {
		this.cutterPhaBuscaAvancada = cutterPhaBuscaAvancada;
	}

	public void imprimirTicket() {
		try {
			ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getBiblioteca().getCodigo(), getTipoPessoa(), getMatricula(), getUsuarioLogado());			
			setTextoComprovante(getFacadeFactory().getReservaFacade().gerarStringParaTicket(reservaVOs, getBiblioteca(), getImpressoraVO(), confBibVO ,getUsuarioLogado()));
			if(getBiblioteca().getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)){
				registrarImpressoraPadraoUsuarioBblioteca();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTextoComprovante() {
		if (textoComprovante == null) {
			textoComprovante = "";
		}
		return textoComprovante;
	}

	public void setTextoComprovante(String textoComprovante) {
		this.textoComprovante = textoComprovante;
	}

	public List<ReservaVO> getReservaVOs() {
		if (reservaVOs == null) {
			reservaVOs = new ArrayList<ReservaVO>(0);
		}
		return reservaVOs;
	}

	public void setReservaVOs(List reservaVOs) {
		this.reservaVOs = reservaVOs;
	}
	
	public void imprimirTicketReservaPDF() {
		try {
			if (getReservaVOs() != null && !getReservaVOs().isEmpty()) {
				String design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + "comprovanteReserva.jrxml");
				ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPorBiblioteca(getReservaVOs().get(0).getBibliotecaVO().getCodigo(), 1, getUsuarioLogado());
				getSuperParametroRelVO().setTituloRelatorio("COMPROVANTE DE RESERVA");
				getSuperParametroRelVO().adicionarParametro("nomeBiblioteca", getReservaVOs().get(0).getBibliotecaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("solicitante", getReservaVOs().get(0).getPessoa().getNome());
				getSuperParametroRelVO().adicionarParametro("matricula", getReservaVOs().get(0).getMatricula());
				getSuperParametroRelVO().adicionarParametro("textoReserva", confBibVO.getTextoPadraoReservaCatalogo());
				if(getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("professor")){
					getSuperParametroRelVO().adicionarParametro("assinaturaFuncionario", null);
					getSuperParametroRelVO().adicionarParametro("assinaturaSolicitante", null);
					getSuperParametroRelVO().adicionarParametro("apresentaAssinaturas", false);
				}else{
					getSuperParametroRelVO().adicionarParametro("assinaturaFuncionario", getUsuarioLogado().getNome_Apresentar());
					getSuperParametroRelVO().adicionarParametro("assinaturaSolicitante", getReservaVOs().get(0).getPessoa().getNome());
					getSuperParametroRelVO().adicionarParametro("apresentaAssinaturas", true);
				}
				getSuperParametroRelVO().setListaObjetos(getReservaVOs());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();

				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImpressoraVO getImpressoraVO() {
		if(impressoraVO == null){
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}

	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}
	
	public void montarListaSelectItemImpressora(){
		try{
			if(Uteis.isAtributoPreenchido(getBiblioteca()) && getBiblioteca().getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)){
				List<ImpressoraVO> impressoraVOs = getFacadeFactory().getImpressoraFacade().consultar("codigoBiblioteca", getBiblioteca().getCodigo().toString(), getUnidadeEnsinoLogado(), false, getUsuarioLogado());
				setListaSelectItemImpressora(UtilSelectItem.getListaSelectItem(impressoraVOs, "codigo", "nomeImpressora", false));
				limparMensagem();
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void registrarImpressoraPadraoUsuarioBblioteca(){
		try {
			if(Uteis.isAtributoPreenchido(getBiblioteca()) && getBiblioteca().getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)){
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getImpressoraVO().getCodigo().toString(), "BuscaCatalogo", "ImpressoraU"+getUsuarioLogado().getCodigo()+"B"+getBiblioteca().getCodigo(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarImpressoraPadraoUsuarioBblioteca(){
		try {
			if(Uteis.isAtributoPreenchido(getBiblioteca()) && getBiblioteca().getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)){			
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("BuscaCatalogo", "ImpressoraU"+getUsuarioLogado().getCodigo()+"B"+getBiblioteca().getCodigo(), false, getUsuarioLogado());
				if(layoutPadraoVO != null && !layoutPadraoVO.getValor().trim().isEmpty() && Uteis.getIsValorNumerico(layoutPadraoVO.getValor())){
					getImpressoraVO().setCodigo(Integer.valueOf(layoutPadraoVO.getValor()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemImpressora() {
		if(listaSelectItemImpressora == null){
			listaSelectItemImpressora =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemImpressora;
	}

	public void setListaSelectItemImpressora(List<SelectItem> listaSelectItemImpressora) {
		this.listaSelectItemImpressora = listaSelectItemImpressora;
	}	
	
	
	public void executarExportarArquivoMarc21XML() {
    	try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
			if (obj == null){
				obj = getCatalogoVO();
			}			
			List<ArquivoMarc21VO> arquivoMarc21VOs = getFacadeFactory().getArquivoMarc21Facade().montarArquivoMarc21DadosCatalogo(obj.getCodigo() , getUsuarioLogado());			
			getFacadeFactory().getArquivoMarc21Facade().executarExportarArquivoMarc21XML(arquivoMarc21VOs.get(0),  getUsuarioLogado() , getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade() ,"c");
			setArquivoMarc21VO(arquivoMarc21VOs.get(0));
			setFazerDownloadMarc21(true);
			setMensagemID(MSG_TELA.msg_dados_Enviados.name());
    	} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
    	
    }
	
	
	public ArquivoMarc21VO getArquivoMarc21VO() {
		if (arquivoMarc21VO == null) {
			arquivoMarc21VO = new ArquivoMarc21VO();
		}
		return arquivoMarc21VO;
	}

	public void setArquivoMarc21VO(ArquivoMarc21VO arquivoMarc21VO) {
		this.arquivoMarc21VO = arquivoMarc21VO;
	}
	
	
	public String getDownloadArquivoMarc21Catalogo() {
		try {
			if (!getArquivoMarc21VO().getArquivoVO().getNome().equals("") && getFazerDownloadMarc21() == true) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoMarc21VO().getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getArquivoMarc21VO().getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", true);
				return "location.href='../../DownloadSV'";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	public Boolean getFazerDownloadMarc21() {
		if (fazerDownloadMarc21 == null) {
			fazerDownloadMarc21 = false;
		}
		return fazerDownloadMarc21;
	}

	public void setFazerDownloadMarc21(Boolean fazerDownloadMarc21) {
		this.fazerDownloadMarc21 = fazerDownloadMarc21;
	}

	public ArquivoMarc21CatalogoVO getArquivoMarc21CatalogoVO() {
		return arquivoMarc21CatalogoVO;
	}

	public void setArquivoMarc21CatalogoVO(ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO) {
		this.arquivoMarc21CatalogoVO = arquivoMarc21CatalogoVO;
	}
	

}
