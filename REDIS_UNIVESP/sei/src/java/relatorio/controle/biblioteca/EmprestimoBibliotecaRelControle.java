package relatorio.controle.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoEmprestimo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.biblioteca.Emprestimo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.EmprestimoFiltroRelVO;
import relatorio.negocio.jdbc.biblioteca.EmprestimoRel;
import controle.arquitetura.SuperControle;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas emprestimoBibliotecaRel.jsp
 * emprestimoBibliotecaRel.jsp) com as funcionalidades da classe <code>EmprestimoBibliotecaRelControle</code>. 
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Emprestimo
 * @see Emprestimo
 */
@SuppressWarnings("serial")
@Controller("EmprestimoBibliotecaRelControle")
@Scope("viewScope")
@Lazy
public class EmprestimoBibliotecaRelControle extends SuperControleRelatorio {
	
	private EmprestimoFiltroRelVO emprestimoFiltroRel;

    private List<SelectItem> listaSelectItemUnidadeEnsino;

    private List<SelectItem> listaSelectItemBiblioteca;

    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;

    private String campoConsultaCatalogo;
    private String valorConsultaCatalogo;
    private List listaConsultaCatalogo;
    
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;

	private List listaConsultaMembroComunidade;
	private String valorConsultaMembroComunidade;
	private String campoConsultaMembroComunidade;
    
    private List listaSelectItemTipoEmprestimo;
    private List listaSelectItemTipoPessoa;
    private List listaSelectItemSituacaoEmprestimo;
    
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;

	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List listaConsultaFuncionario;
    private String tipoRelatorio;
	private Boolean apresentarFiltroBiblioteca;
	private Boolean apresentarFiltroCurso;
	private Boolean apresentarFiltroTurma;
	private List<SelectItem> listaSelectItemOrdenarPor;
	private List<SelectItem> listaSelectItemTipoCatalogo;
	private Boolean apresentarMotivoIsencao;
	

    public EmprestimoBibliotecaRelControle() throws Exception {
        novo();
     	inicializarListasSelectItemTodosComboBox();
    }
    
    @PostConstruct
   	public void realizarCarregamentoRelatorioEmprestimoVindoTelaFichaAluno() {
   		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaImpressaoEmprestimoFichaAluno");
   		if (obj != null && !obj.getMatricula().equals(0)) {
   			try {
   				novo();
   		     	inicializarListasSelectItemTodosComboBox();
   				getEmprestimoFiltroRel().setUnidadeEnsinoVO(obj.getUnidadeEnsino());
   				getEmprestimoFiltroRel().setTipoPessoa(TipoPessoa.ALUNO.getValor());
   				getEmprestimoFiltroRel().setMatriculaVO(obj);
   				setApresentarFiltroBiblioteca(Boolean.TRUE);
				montarListaSelectItemBiblioteca();
   				
   			} catch (Exception e) {
   				setMensagemDetalhada("msg_erro", e.getMessage());
   			} finally {
   				context().getExternalContext().getSessionMap().remove("matriculaImpressaoEmprestimoFichaAluno");
   			}
   		}
   	}
    
    public void novo() {
        try {
        	setEmprestimoFiltroRel(new EmprestimoFiltroRelVO());
//        	inicializarListasSelectItemTodosComboBox();
            setMensagemID("msg_entre_prmconsulta");
        } catch (Exception e) {
            setMensagemID(e.getMessage());
        }
    }
    
    public void realizarImpressaoPDF() {
    	String titulo = "";
        String nomeRelatorio = "";
        String design = "";
        String caminhoBase = "";
        String descricaoFiltros = "";
        String urlLogoUnidadeEnsinoRelatorio = "";
        String urlLogo = "";
        List lista = new ArrayList(0);
        try {
        	
        	getFacadeFactory().getEmprestimoRelFacade().validarDados(getEmprestimoFiltroRel());
            titulo = "Empréstimo Biblioteca";
            nomeRelatorio = EmprestimoRel.getIdEntidade();
            design = getFacadeFactory().getEmprestimoRelFacade().designIReportRelatorio();
            caminhoBase = getFacadeFactory().getEmprestimoRelFacade().caminhoBaseRelatorio();
            descricaoFiltros = "";
            
            lista = getFacadeFactory().getEmprestimoRelFacade().consultar(getEmprestimoFiltroRel());
            
            if (!lista.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(caminhoBase);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(caminhoBase);
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().adicionarParametro("ordenarPor", getEmprestimoFiltroRel().getOrdenarPor());
                getSuperParametroRelVO().adicionarParametro("apresentarMotivoIsensao", getApresentarMotivoIsencao());
                if (Uteis.isAtributoPreenchido(getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo())) {
                	UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
                	if (ue.getExisteLogoRelatorio()) {
                		urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
                		urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
                		getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
                	} else {
                		getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
                	}
					
				}else {
            		getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
            	}
                if (Uteis.isAtributoPreenchido(getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo())) {
					TipoCatalogoVO tipoCatalogoVO = getFacadeFactory().getTipoCatalogoFacade().consultarPorChavePrimaria(getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					
					if(!getEmprestimoFiltroRel().getConsiderarSubTiposCatalogo()) {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", tipoCatalogoVO.getNome()); 
					}else {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", tipoCatalogoVO.getNome()+" e Subdivisões"); 
					}
					
				} else {
					getSuperParametroRelVO().adicionarParametro("tipoCatalogo", "Todos");
				}
                getSuperParametroRelVO().setListaObjetos(lista);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
//            apresentarRelatorioObjetos(nomeRelatorio, titulo, getEmprestimoFiltroRel().getUnidadeEnsinoVO().getRazaoSocial(), "", "PDF",
//                   "/" + EmprestimoRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), descricaoFiltros, lista, caminhoBase);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	titulo = null;
            design = null;
            caminhoBase = null;
            nomeRelatorio = null;
            descricaoFiltros = null;
            lista = null;
        }
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
    	setListaSelectItemBiblioteca(new ArrayList(0));
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemBiblioteca();
        montarListaSelectItemTipoEmprestimo();
        montarListaSelectItemSituacaoEmprestimo();
        montarListaSelectItemTipoPessoa();
        montarListaSelectItemTipoCatalogo();
    }

    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        limparDadosCurso();
        getEmprestimoFiltroRel().getUnidadeEnsinoVO().setCodigo(0);
        setListaSelectItemUnidadeEnsino(getFacadeFactory().getAcervoRelFacade().consultarUnidadeEnsinoPorBiblioteca(getEmprestimoFiltroRel().getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
    }

    public void montarListaSelectItemBiblioteca() throws Exception {        
		setListaSelectItemBiblioteca(getFacadeFactory().getAcervoRelFacade().consultarBiblioteca(getUnidadeEnsinoLogado().getCodigo(), Obrigatorio.SIM, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		if (Uteis.isAtributoPreenchido(getEmprestimoFiltroRel().getBibliotecaVO()) && !getListaSelectItemBiblioteca().isEmpty()) {
			getEmprestimoFiltroRel().getBibliotecaVO().setCodigo((Integer) getListaSelectItemBiblioteca().get(0).getValue());
		}       
    }
    
    public void montarListaSelectItemTipoEmprestimo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todos"));
        itens.add(new SelectItem("emDia", "Em Dia"));
        itens.add(new SelectItem("atrasados", "Atrasados"));
        setListaSelectItemTipoEmprestimo(itens);
    }
    
    public void montarListaSelectItemSituacaoEmprestimo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todas"));
        itens.add(new SelectItem(SituacaoEmprestimo.EMPRESTADO.getValor(), "Emprestado"));
        itens.add(new SelectItem(SituacaoEmprestimo.EMPRESTADO_COM_ATRASO.getValor(), "Emprestado com atraso"));
        itens.add(new SelectItem(SituacaoEmprestimo.EMPRESTADO_SEM_ATRASO.getValor(), "Emprestado sem atraso"));
        itens.add(new SelectItem(SituacaoEmprestimo.DEVOLVIDO.getValor(), "Devolvido"));
        itens.add(new SelectItem(SituacaoEmprestimo.DEVOLVIDO_COM_ATRASO.getValor(), "Devolvido com atraso"));
        itens.add(new SelectItem(SituacaoEmprestimo.DEVOLVIDO_SEM_ATRASO.getValor(), "Devolvido sem atraso"));
        setListaSelectItemSituacaoEmprestimo(itens);
    }
    
    public void montarListaSelectItemTipoPessoa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todos"));
        itens.add(new SelectItem(TipoPessoa.ALUNO.getValor(), "Aluno"));
		itens.add(new SelectItem(TipoPessoa.PROFESSOR.getValor(), "Professor"));
		itens.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), "Funcionário"));
		itens.add(new SelectItem(TipoPessoa.MEMBRO_COMUNIDADE.getValor(), "Visitante"));
        setListaSelectItemTipoPessoa(itens);
    }
    
    public void montarListaSelectItemTipoCatalogo() {
		try {
			montarListaSelectItemTipoCatalogo("");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
    
    /**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Tipo Catalago</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoCatalogo(String valorConsulta) throws Exception {
		try {
			List<TipoCatalogoVO> resultadoConsulta = getFacadeFactory().getTipoCatalogoFacade().consultarPorNome(valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTipoCatalogo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
		}
	}
    
    public boolean getTipoPessoaAluno() {
    	if (getEmprestimoFiltroRel().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean getTipoPessoaFuncionario() {
    	if (getEmprestimoFiltroRel().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean getTipoPessoaProfessor() {
    	if (getEmprestimoFiltroRel().getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public boolean getTipoPessoaMembroComunidade() {
    	if (getEmprestimoFiltroRel().getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {
    		return true;
    	} else {
    		return false;
    	}
    }

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void limparDadosCurso() {
    	getEmprestimoFiltroRel().setCursoVO(new CursoVO());
        getEmprestimoFiltroRel().setTurnoVO(new TurnoVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
    }

    public void selecionarCurso() {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = ((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens"));
            getEmprestimoFiltroRel().setCursoVO(unidadeEnsinoCurso.getCurso());
            getEmprestimoFiltroRel().setTurnoVO(unidadeEnsinoCurso.getTurno());
            getListaConsultaCurso().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public String consultarCatalogo() {
        try {
            super.consultar();
            List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
            if (getCampoConsultaCatalogo().equals("codigo")) {
                if (getValorConsultaCatalogo().equals("")) {
                        setValorConsultaCatalogo("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCatalogo());
                objs = getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false,
                                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getEmprestimoFiltroRel().getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaCatalogo().equals("tituloTitulo")) {
                objs = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(getValorConsultaCatalogo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, 
                				getEmprestimoFiltroRel().getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaCatalogo().equals("nomeEditora")) {
                objs = getFacadeFactory().getCatalogoFacade().consultarPorNomeEditora(getValorConsultaCatalogo(), true,
                                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getEmprestimoFiltroRel().getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
            }
            setListaConsultaCatalogo(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsultaCatalogo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void limparDadosCatalogo() {
        setValorConsultaCatalogo("");
        getEmprestimoFiltroRel().setCatalogoVO(new CatalogoVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarCatalogo() {
        try {
            CatalogoVO catalogo = ((CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens"));
            getEmprestimoFiltroRel().setCatalogoVO(catalogo);
            getListaConsultaCatalogo().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCatalogo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("tituloTitulo", "Título"));
        itens.add(new SelectItem("nomeEditora", "Editora"));
        return itens;
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Integer getAnoAtual() {
        return Uteis.getAnoData(new Date());
    }

    public void consultarTurmaPorIdentificador() {
        try {
                TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getEmprestimoFiltroRel().getTurmaVO().getIdentificadorTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                getEmprestimoFiltroRel().setTurmaVO(turmaVO);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            getEmprestimoFiltroRel().setTurmaVO(obj);
        } catch (Exception ex) {
            setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>(0));
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }
    
    public void limparDadosTurma() {
    	getEmprestimoFiltroRel().setTurmaVO(new TurmaVO());
    }
    
    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

                if (getValorConsultaAluno().equals("")) {
                    throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
                }
                if (getCampoConsultaAluno().equals("matricula")) {
                    objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                }
                if (getCampoConsultaAluno().equals("nomePessoa")) {
                    objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                }
                if (getCampoConsultaAluno().equals("nomeCurso")) {
                    objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                }
                setListaConsultaAluno(objs);

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
        getEmprestimoFiltroRel().setMatriculaVO(obj);
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
    }
    
    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getEmprestimoFiltroRel().getMatriculaVO().getMatricula(), getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getEmprestimoFiltroRel().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            getEmprestimoFiltroRel().setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            getEmprestimoFiltroRel().setMatriculaVO(new MatriculaVO());
        }
    }
    
    public void limparDadosAluno() throws Exception {
    	getEmprestimoFiltroRel().setMatriculaVO(new MatriculaVO());
    }
    
    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }
    
    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }
    
    public void consultarProfessor() {
		try {
			List objs = new ArrayList(0);
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
		}
	}

	public void consultarProfessorPorMatricula() throws Exception {
		try {
			FuncionarioVO objProfessor = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(
					getEmprestimoFiltroRel().getProfessorVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objProfessor.getMatricula().equals("")) {
				throw new Exception("Professor de matrícula " + getEmprestimoFiltroRel().getProfessorVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getEmprestimoFiltroRel().setProfessorVO(objProfessor);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getEmprestimoFiltroRel().setProfessorVO(new FuncionarioVO());
		}
	}

	public void selecionarProfessor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		if (getMensagemDetalhada().equals("")) {
			getEmprestimoFiltroRel().setProfessorVO(obj);
		}
		getListaConsultaProfessor().clear();
		setValorConsultaProfessor(null);
		setCampoConsultaProfessor(null);
	}

	public void limparCampoProfessor() {
		getEmprestimoFiltroRel().setProfessorVO(new FuncionarioVO());
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
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade()
						.consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(),
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
		}
	}

	public void consultarFuncionarioPorMatricula() throws Exception {
		try {
			FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(
					getEmprestimoFiltroRel().getFuncionarioVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objFuncionario.getMatricula().equals("")) {
				throw new Exception("Funcionario de matrícula " + getEmprestimoFiltroRel().getFuncionarioVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getEmprestimoFiltroRel().setFuncionarioVO(objFuncionario);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getEmprestimoFiltroRel().setFuncionarioVO(new FuncionarioVO());
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		if (getMensagemDetalhada().equals("")) {
			getEmprestimoFiltroRel().setFuncionarioVO(obj);
		}
		getListaConsultaFuncionario().clear();
		setValorConsultaFuncionario(null);
		setCampoConsultaFuncionario(null);
	}

	public void limparCampoFuncionario() {
		getEmprestimoFiltroRel().setFuncionarioVO(new FuncionarioVO());
	}
	
	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		return itens;
	}
	
	public void consultarMembroComunidade() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaMembroComunidade().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaMembroComunidade().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorNome(getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaMembroComunidade().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorCPF(getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaMembroComunidade().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorRG(getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaMembroComunidade(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			getListaConsultaMembroComunidade().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void consultarVisitantePorCPF() {
		try {
			String campoConsulta = getEmprestimoFiltroRel().getPessoa().getCPF();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getEmprestimoFiltroRel().setPessoa(pessoa);
			if (pessoa.getCodigo().equals(0)) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getEmprestimoFiltroRel().getPessoa().setNome("");
			getEmprestimoFiltroRel().getPessoa().setCodigo(0);
		}
	}

	public void limparCampoMembroComunidade() {
		getEmprestimoFiltroRel().setPessoa(new PessoaVO());
		setValorConsultaMembroComunidade("");
	}

	public void selecionarMembroComunidade() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("membroComunidadeItens");
			obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getEmprestimoFiltroRel().setPessoa(obj);
			getListaConsultaMembroComunidade().clear();
			setValorConsultaMembroComunidade(null);
			setCampoConsultaMembroComunidade(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarFiltroBiblioteca() {
		if (getEmprestimoFiltroRel().getBibliotecaVO().getCodigo() != 0) {
			setApresentarFiltroBiblioteca(Boolean.TRUE);
			try {
				montarListaSelectItemUnidadeEnsino();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setApresentarFiltroBiblioteca(Boolean.FALSE);
		}
	}
	

	public void validarFiltrosInsuburdinados() {
		try {
			limparDadosAluno();
			limparCampoFuncionario();
			limparCampoProfessor();
			limparDadosCurso();
			limparDadosTurma();
			if (getEmprestimoFiltroRel().getTipoPessoa().equals("AL")) {
				setApresentarFiltroCurso(Boolean.FALSE);
				setApresentarFiltroTurma(Boolean.FALSE);
			}
			if (getEmprestimoFiltroRel().getTipoPessoa().equals("PR")) {
				setApresentarFiltroCurso(Boolean.TRUE);
				setApresentarFiltroTurma(Boolean.TRUE);
			}
			if (getEmprestimoFiltroRel().getTipoPessoa().equals("FU")) {
				setApresentarFiltroCurso(Boolean.TRUE);
				setApresentarFiltroTurma(Boolean.TRUE);
			}
			if (getEmprestimoFiltroRel().getTipoPessoa().equals("")) {
				setApresentarFiltroCurso(Boolean.FALSE);
				setApresentarFiltroTurma(Boolean.FALSE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
// ------------------------------- Getter and Setters ----------------------------------------- //
	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemBiblioteca;
	}

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaCatalogo() {
		if (campoConsultaCatalogo == null) {
			campoConsultaCatalogo = "";
		}
		return campoConsultaCatalogo;
	}

	public void setCampoConsultaCatalogo(String campoConsultaCatalogo) {
		this.campoConsultaCatalogo = campoConsultaCatalogo;
	}

	public String getValorConsultaCatalogo() {
		if (valorConsultaCatalogo == null) {
			valorConsultaCatalogo = "";
		}
		return valorConsultaCatalogo;
	}

	public void setValorConsultaCatalogo(String valorConsultaCatalogo) {
		this.valorConsultaCatalogo = valorConsultaCatalogo;
	}

	public List getListaConsultaCatalogo() {
		if (listaConsultaCatalogo == null) {
			listaConsultaCatalogo = new ArrayList(0);
		}
		return listaConsultaCatalogo;
	}

	public void setListaConsultaCatalogo(List listaConsultaCatalogo) {
		this.listaConsultaCatalogo = listaConsultaCatalogo;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public EmprestimoFiltroRelVO getEmprestimoFiltroRel() {
		if (emprestimoFiltroRel == null) {
			emprestimoFiltroRel = new EmprestimoFiltroRelVO();
		}
		return emprestimoFiltroRel;
	}

	public void setEmprestimoFiltroRel(EmprestimoFiltroRelVO filtro) {
		this.emprestimoFiltroRel = filtro;
	}

	public List getListaSelectItemTipoEmprestimo() {
		if (listaSelectItemTipoEmprestimo == null) {
			listaSelectItemTipoEmprestimo = new ArrayList(0);
		}
		return listaSelectItemTipoEmprestimo;
	}

	public void setListaSelectItemTipoEmprestimo(List listaSelectItemTipoEmprestimo) {
		this.listaSelectItemTipoEmprestimo = listaSelectItemTipoEmprestimo;
	}

	public List getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList(0);
		}

		return listaSelectItemTipoPessoa;
	}

	public void setListaSelectItemTipoPessoa(List listaSelectItemTipoPessoa) {
		this.listaSelectItemTipoPessoa = listaSelectItemTipoPessoa;
	}

	public List getListaSelectItemSituacaoEmprestimo() {
		if (listaSelectItemSituacaoEmprestimo == null) {
			listaSelectItemSituacaoEmprestimo = new ArrayList(0);
		}

		return listaSelectItemSituacaoEmprestimo;
	}

	public void setListaSelectItemSituacaoEmprestimo(List listaSelectItemSituacaoEmprestimo) {
		this.listaSelectItemSituacaoEmprestimo = listaSelectItemSituacaoEmprestimo;
	}

	public String getCampoConsultaProfessor() {
		if(campoConsultaProfessor == null){
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if(valorConsultaProfessor == null){
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

	public String getCampoConsultaFuncionario() {
		if(campoConsultaFuncionario == null){
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if(valorConsultaFuncionario == null){
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
		}

		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public Boolean getApresentarFiltroBiblioteca() {
		return apresentarFiltroBiblioteca;
	}

	public void setApresentarFiltroBiblioteca(Boolean apresentarFiltroBiblioteca) {
		this.apresentarFiltroBiblioteca = apresentarFiltroBiblioteca;
	}

	public Boolean getApresentarFiltroCurso() {
		return apresentarFiltroCurso;
	}

	public void setApresentarFiltroCurso(Boolean apresentarFiltroCurso) {
		this.apresentarFiltroCurso = apresentarFiltroCurso;
	}

	public Boolean getApresentarFiltroTurma() {
		return apresentarFiltroTurma;
	}

	public void setApresentarFiltroTurma(Boolean apresentarFiltroTurma) {
		this.apresentarFiltroTurma = apresentarFiltroTurma;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		if (listaSelectItemOrdenarPor == null) {
			listaSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
			listaSelectItemOrdenarPor.add(new SelectItem("catalogo", UteisJSF.internacionalizar("prt_Emprestimo_catalogo")));
			listaSelectItemOrdenarPor.add(new SelectItem("dataEmprestimo", UteisJSF.internacionalizar("prt_Emprestimo_dataEmprestimo")));
			listaSelectItemOrdenarPor.add(new SelectItem("dataPrevisao", UteisJSF.internacionalizar("prt_Emprestimo_dataPrevistaDevolucao")));
			listaSelectItemOrdenarPor.add(new SelectItem("dataDevolucao", UteisJSF.internacionalizar("prt_Emprestimo_dataDevolucao")));
			listaSelectItemOrdenarPor.add(new SelectItem("pessoa", UteisJSF.internacionalizar("prt_Emprestimo_pessoa")));
			listaSelectItemOrdenarPor.add(new SelectItem("tombo", UteisJSF.internacionalizar("Tombo")));						
		}
		return listaSelectItemOrdenarPor;
	}

	public void setListaSelectItemOrdenarPor(List<SelectItem> listaSelectItemOrdenarPor) {
		this.listaSelectItemOrdenarPor = listaSelectItemOrdenarPor;
	}

	public List<SelectItem> getListaSelectItemTipoCatalogo() {
		if (listaSelectItemTipoCatalogo == null) {
			listaSelectItemTipoCatalogo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoCatalogo;
	}

	public void setListaSelectItemTipoCatalogo(List<SelectItem> listaSelectItemTipoCatalogo) {
		this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
	}
	

	public List getListaConsultaMembroComunidade() {
		if (listaConsultaMembroComunidade == null) {
			listaConsultaMembroComunidade = new ArrayList(0);
		}
		return listaConsultaMembroComunidade;
	}

	public void setListaConsultaMembroComunidade(List listaConsultaMembroComunidade) {
		this.listaConsultaMembroComunidade = listaConsultaMembroComunidade;
	}

	public String getValorConsultaMembroComunidade() {
		if (valorConsultaMembroComunidade == null) {
			valorConsultaMembroComunidade = "";
		}
		return valorConsultaMembroComunidade;
	}

	public void setValorConsultaMembroComunidade(String valorConsultaMembroComunidade) {
		this.valorConsultaMembroComunidade = valorConsultaMembroComunidade;
	}

	public String getCampoConsultaMembroComunidade() {
		if (campoConsultaMembroComunidade == null) {
			campoConsultaMembroComunidade = "";
		}
		return campoConsultaMembroComunidade;
	}

	public void setCampoConsultaMembroComunidade(String campoConsultaMembroComunidade) {
		this.campoConsultaMembroComunidade = campoConsultaMembroComunidade;
	}

	public List getTipoConsultaComboMembroComunidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public String getMascaraConsultaMembroComunidade() {
		if (getCampoConsultaMembroComunidade().equals("CPF")) {
			return "return mascara(this.form,'formRequerente:valorConsultaRequerente','999.999.999-99',event);";
		}
		return "";
	}	
	public Boolean getApresentarMotivoIsencao() {
		if (apresentarMotivoIsencao == null) {
			apresentarMotivoIsencao = false;
		}
		return apresentarMotivoIsencao;
	}

	public void setApresentarMotivoIsencao(Boolean apresentarMotivoIsencao) {
		this.apresentarMotivoIsencao = apresentarMotivoIsencao;
	}

	public Boolean getApresentarFiltroSubTiposCatalogo() {
		if(getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo() != null 
				&& getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo().getCodigo() != null 
				&& getEmprestimoFiltroRel().getCatalogoVO().getTipoCatalogo().getCodigo() != 0) {
			return true;
		}
		return false;
	}

}
