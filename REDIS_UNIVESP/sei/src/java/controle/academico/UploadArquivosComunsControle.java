package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ModuloDisponibilizarMaterialEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.DepartamentoVO.EnumCampoConsultaDepartamento;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 *
 * @author Carlos
 */
@Controller("UploadArquivosComunsControle")
@Scope("viewScope")
@Lazy
public class UploadArquivosComunsControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7840537074484807285L;
	private ArquivoVO arquivoVO;
    private List<ArquivoVO> listaArquivos;
    private Boolean apresentarDataDisponibilizacaoMaterial;
    private List<SelectItem> listaSelectItemNivelEducacional;    
    private Boolean permitirUsuarioRealizarUpload;
    private List<CursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List<PessoaVO> listaConsultaProfessor;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String campoConsultaDepartamento;
    private String valorConsultaDepartamento;
    protected List<DepartamentoVO> listaConsultaDepartamento;
    private List<SelectItem> listaSelectItemTipoDocumentoVOs;
    private Boolean apresentarDocumentoPortalTransparencia;
    
    
    private List<SelectItem> tipoConsultaComboFuncionario;
    private String campoConsultaFuncionario;
    private String valorConsultaFuncionario;
    private List<FuncionarioVO> listaConsultaFuncionario;
    private List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<ArquivoVO> listaArquivosInativosVOs;

    public UploadArquivosComunsControle() {
    	if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){    		
    		verificarUsuarioPossuiPermissaoRealizarUpload();
    		montarListaSelectItemTipoDocumento();
    		getListaSelectItemNivelEducacional().stream().findFirst().map(SelectItem::getValue).map(String::valueOf)
    			.ifPresent(getArquivoVO()::setNivelEducacional);
    		getListaSelectItemUnidadeEnsino().stream().findFirst().map(SelectItem::getValue).map(String::valueOf)
    			.map(Integer::new).ifPresent(getArquivoVO().getUnidadeEnsinoVO()::setCodigo);
    	}
    }

    public final void inicializarDadosComboBoxNivelEducacional() {
        setListaSelectItemNivelEducacional(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true));
    }

    public void upLoadArquivo(FileUploadEvent uploadEvent) {
        try {
//            getFacadeFactory().getUploadArquivosComunsFacade().validarDados(getArquivoVO());
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.COMUM_TMP, getUsuarioLogado());
            setArquivoVO(getArquivoVO());
//            getArquivoVO().getDescricao();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public void verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial() {
        setApresentarDataDisponibilizacaoMaterial(getFacadeFactory().getUploadArquivosComunsFacade().verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(getUsuarioLogadoClone()));
    }

    public void persistir() {
        try {
        	getArquivoVO().setProvedorDeAssinaturaEnum(getProvedorDeAssinaturaEnum());
            getFacadeFactory().getUploadArquivosComunsFacade().persistir(getArquivoVO(), getUsuarioLogado(), getListaFuncionarioAssinarDigitalmenteVOs(), getConfiguracaoGeralPadraoSistema());
            getArquivoVO().setDescricao("");
            getArquivoVO().setNome("");
            getArquivoVO().setExtensao("");
            getArquivoVO().setPastaBaseArquivo("");
            getArquivoVO().setPastaBaseArquivoEnum(null);
            getArquivoVO().setProvedorDeAssinaturaEnum(null);
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
    	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getArquivoVO().getUnidadeEnsinoVO().getCodigo()) ? getArquivoVO().getUnidadeEnsinoVO().getCodigo() : 0;
    	if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) || !getArquivoVO().getAssinarCertificadoDigitalUnidadeEnsino() ){
    		return new ArrayList<SelectItem>();
    	}
    	return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL);
    }

    public void adicionarArquivoLista() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "UploadArquivosComuns", "Iniciando Adicionar Lista Download Arquivo", "Downloading");
            getFacadeFactory().getUploadArquivosComunsFacade().inicializarDadosArquivoPersistencia(getArquivoVO(), getUsuarioLogadoClone());
            getFacadeFactory().getUploadArquivosComunsFacade().validarDados(getArquivoVO());
            persistir();
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo", "Downloading");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerArquivoLista() throws Exception {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download ", "Downloading - Removendo");
            ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
            getFacadeFactory().getUploadArquivosComunsFacade().removerArquivoVO(obj, getListaArquivos(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            consultarArquivoPorNivelEducacional();
            setMensagemID("msg_dados_excluidos");
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download ", "Downloading - Removendo");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void realizarInativacaoArquivo() {
    	try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download ", "Downloading - Removendo");
            ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
            getFacadeFactory().getUploadArquivosComunsFacade().realizarInvativacaoArquivo(obj, SituacaoArquivo.INATIVO, getUsuarioLogado());
            consultarArquivos();
            setMensagemID("msg_dados_inativado");
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download ", "Downloading - Removendo");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void realizarAtivacaoArquivo() {
    	try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download ", "Downloading - Removendo");
            ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoInativoItens");
            getFacadeFactory().getUploadArquivosComunsFacade().realizarInvativacaoArquivo(obj, SituacaoArquivo.ATIVO, getUsuarioLogado());
            consultarArquivosInativos();
            setMensagemID("msg_ativar_dados");
            registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download ", "Downloading - Removendo");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarDadosMensagens() throws Exception {
        setMensagem("");
        setMensagemDetalhada("");
    }

    public void consultarArquivoPorNivelEducacional() {
        try {
            setListaArquivos(getFacadeFactory().getArquivoFacade().consultarArquivosPorSituacaoPorNivelEducacional(getArquivoVO().getNivelEducacional(), SituacaoArquivo.ATIVO, getUsuarioLogado()));
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }
    
    public void consultarArquivos() {
    	try {
    		consultarArquivosAtivos();
    		consultarArquivosInativos();
    		setMensagemID("msg_dados_consultados", Uteis.SUCESSO, true);
    	} catch (Exception ex) {
    		setMensagemDetalhada("msg_erro", ex.getMessage());
    	}
    }
    
    public void consultarArquivosAtivos() {
    	try {
    		setListaArquivos(getFacadeFactory().getArquivoFacade().consultarArquivosPorSituacaoInstitucional(getArquivoVO(), SituacaoArquivo.ATIVO, getArquivoVO().getApresentarPortalAluno(), getArquivoVO().getApresentarPortalProfessor(), getArquivoVO().getApresentarPortalCoordenador()));
    		setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
    	} catch (Exception ex) {
    		setMensagemDetalhada("msg_erro", ex.getMessage());
    	}
    }
    
    public void consultarArquivosInativos() {
    	try {
    		setListaArquivosInativosVOs(getFacadeFactory().getArquivoFacade().consultarArquivosPorSituacaoInstitucional(getArquivoVO(), SituacaoArquivo.INATIVO, getArquivoVO().getApresentarPortalAluno(), getArquivoVO().getApresentarPortalProfessor(), getArquivoVO().getApresentarPortalCoordenador()));
    		setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
    	} catch (Exception ex) {
    		setMensagemDetalhada("msg_erro", ex.getMessage());
    	}
    }
    
    public String getCaminhoServidorDownload() {
        try {
            ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
            return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj, PastaBaseArquivoEnum.COMUM, getConfiguracaoGeralPadraoSistema());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public static void verificarPermissaoUsuarioRealizarUpload(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public void verificarUsuarioPossuiPermissaoRealizarUpload() {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioRealizarUpload(getUsuarioLogado(), "PermitirUsuarioRealizarUpload");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPermitirUsuarioRealizarUpload(liberar);
    }

    
    public boolean getApresentarDescricaoNivelEducacional() {
        return !getArquivoVO().getNivelEducacional().equals("");
    }
    
    
    private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void consultarDisciplina() {
		try {
			getListaConsultaDisciplina().clear();
			setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getArquivoVO().getCurso().getCodigo(), getArquivoVO().getTurma().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		getArquivoVO().setDisciplina((DisciplinaVO) getRequestMap().get("disciplinaItens"));
		limparCampoProfessor();
		//getArquivoVO().setNivelEducacional("");
		getListaArquivos().clear();
	}

	public void limparDisciplina() {
		getArquivoVO().setDisciplina(null);
	}
	public void limparNivelEducacional() {
		limparCurso();
		limparTurma();
		limparDisciplina();
	}

	public void consultarTurma() {
		try {

			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			getArquivoVO().setTurma((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));	
			limparDisciplina();			
			limparCurso();		
			limparCampoProfessor();
			getArquivoVO().setCurso(getArquivoVO().getTurma().getCurso());
			getArquivoVO().setNivelEducacional(getArquivoVO().getTurma().getCurso().getNivelEducacional());
			getListaArquivos().clear();
			limparMensagem();
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}
	
	public void consultarTurmaPorIdentificador() {
		try {
			getArquivoVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getArquivoVO().getTurma(), getArquivoVO().getTurma().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			limparDisciplina();
			limparCurso();
			limparCampoProfessor();
			getArquivoVO().setNivelEducacional("");
			getListaArquivos().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparTurma() {
		getArquivoVO().setTurma(null);		
	}

	public void limparCurso() {
		getArquivoVO().setCurso(null);		
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}
	
	public void realizarDownload() {
		ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		try {
			realizarDownloadArquivo(obj);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void realizarDownloadArquivoInativo() {
		ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoInativoItens");
		try {
			realizarDownloadArquivo(obj);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (!getArquivoVO().getNivelEducacional().equals("")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), getArquivoVO().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
				if (getCampoConsultaCurso().equals("codigo")) {
					if (getValorConsultaCurso().equals("")) {
						setValorConsultaCurso("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaCurso());
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				if (getCampoConsultaCurso().equals("nome")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getArquivoVO().setCurso(curso);
			getArquivoVO().setNivelEducacional(getArquivoVO().getCurso().getNivelEducacional());
			limparDisciplina();
			limparCampoProfessor();
			getListaArquivos().clear();
			if (!getArquivoVO().getTurma().getCurso().getCodigo().equals(getArquivoVO().getCurso().getCodigo())) {
				limparTurma();
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarProfessor() {
        try {
            super.consultar();
            List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("cpf")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarProfessor() {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
        getArquivoVO().setProfessor(obj);
        limparCurso();
        limparTurma();
        limparDisciplina();
        //getArquivoVO().setNivelEducacional("");
        getArquivoVO().setApresentarPortalAluno(false);
        getArquivoVO().setApresentarPortalCoordenador(false);
        getArquivoVO().setApresentarPortalProfessor(false);
        getListaArquivos().clear();
    }
    
    public void limparCampoProfessor() {
        getArquivoVO().setProfessor(new PessoaVO());        
    }

    public Boolean getApresentarCampoCpf() {
        if (getCampoConsultaProfessor().equals("cpf")) {
            return true;
        }
        return false;
    }

    /**
     * @return the campoConsultaProfessor
     */
    public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
        return campoConsultaProfessor;
    }

    /**
     * @param campoConsultaProfessor the campoConsultaProfessor to set
     */
    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    /**
     * @return the valorConsultaProfessor
     */
    public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
        return valorConsultaProfessor;
    }

    /**
     * @param valorConsultaProfessor the valorConsultaProfessor to set
     */
    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    /**
     * @return the listaConsultaProfessor
     */
    public List<PessoaVO> getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList<PessoaVO>(0);
        }
        return listaConsultaProfessor;
    }

    /**
     * @param listaConsultaProfessor the listaConsultaProfessor to set
     */
    public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }

    public List<SelectItem> getTipoConsultaComboProfessor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("cpf", "CPF"));
        return itens;
    }


	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}
	
	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

    public Boolean getApresentarDataDisponibilizacaoMaterial() {
        if (apresentarDataDisponibilizacaoMaterial == null) {
            apresentarDataDisponibilizacaoMaterial = Boolean.FALSE;
        }
        return apresentarDataDisponibilizacaoMaterial;
    }

    public void setApresentarDataDisponibilizacaoMaterial(Boolean apresentarDataDisponibilizacaoMaterial) {
        this.apresentarDataDisponibilizacaoMaterial = apresentarDataDisponibilizacaoMaterial;
    }

    public List<ArquivoVO> getListaArquivos() {
        if (listaArquivos == null) {
            listaArquivos = new ArrayList<ArquivoVO>(0);
        }
        return listaArquivos;
    }

    public void setListaArquivos(List<ArquivoVO> listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    public List<SelectItem> getListaSelectItemNivelEducacional() {
        if (listaSelectItemNivelEducacional == null) {
            listaSelectItemNivelEducacional = new ArrayList<SelectItem>(0);
            listaSelectItemNivelEducacional = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
        }
        return listaSelectItemNivelEducacional;
    }

    public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
        this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
    }

    public Boolean getPermitirUsuarioRealizarUpload() {
        if (permitirUsuarioRealizarUpload == null) {
            permitirUsuarioRealizarUpload = Boolean.FALSE;
        }
        return permitirUsuarioRealizarUpload;
    }

    public void setPermitirUsuarioRealizarUpload(Boolean permitirUsuarioRealizarUpload) {
        this.permitirUsuarioRealizarUpload = permitirUsuarioRealizarUpload;
    }
    
    public Boolean getApresentarFiltroNivelEducacional(){
    	return Boolean.TRUE; 
    }
    
    public Boolean getApresentarFiltroProfessor(){
    	return (getArquivoVO().getNivelEducacional().trim().isEmpty() || getArquivoVO().getNivelEducacional().trim().equals("0"))  && getArquivoVO().getCurso().getCodigo() == 0 && getArquivoVO().getDisciplina().getCodigo()  == 0 && getArquivoVO().getTurma().getCodigo() == 0; 
    }
    
    public Boolean getApresentarFiltroDisciplina(){
    	return Boolean.TRUE;
    	//return (getArquivoVO().getNivelEducacional().trim().isEmpty() || getArquivoVO().getNivelEducacional().trim().equals("0")) && getArquivoVO().getProfessor().getCodigo() == 0; 
    }
    
    public Boolean getApresentarFiltroTurma(){
    	return Boolean.TRUE;
    	//return (getArquivoVO().getNivelEducacional().trim().isEmpty() || getArquivoVO().getNivelEducacional().trim().equals("0")) && getArquivoVO().getProfessor().getCodigo() == 0 && getArquivoVO().getCurso().getCodigo() == 0; 
    }
    
    public Boolean getApresentarFiltroCurso(){
    	return Boolean.TRUE;
    	//return (getArquivoVO().getNivelEducacional().trim().isEmpty() || getArquivoVO().getNivelEducacional().trim().equals("0")) && getArquivoVO().getProfessor().getCodigo()  == 0  && getArquivoVO().getTurma().getCodigo() == 0; 
    }
    
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);	
			try {
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.addAll(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public void inicializarDadosPertinenteUnidadeEnsino() {
		
	}
	
	public List<SelectItem> getTipoConsultaComboDisponibilizarDocumento() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(ModuloDisponibilizarMaterialEnum.ACADEMICO, ModuloDisponibilizarMaterialEnum.ACADEMICO.getDescricao()));
        itens.add(new SelectItem(ModuloDisponibilizarMaterialEnum.ADMINISTRATIVO, ModuloDisponibilizarMaterialEnum.ADMINISTRATIVO.getDescricao()));
        return itens;
    }

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}
	
	public void montarListaSelectItemTipoDocumento() {
		try {
			List<TipoDocumentoVO> listaTipoDocumentoVOs = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorTipoExigenciaDocumento(TipoExigenciaDocumentoEnum.EXIGENCIA_INSTITUCIONAL, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem("", ""));
			for (TipoDocumentoVO tipoDocumentoVO : listaTipoDocumentoVOs) {
				itens.add(new SelectItem(tipoDocumentoVO.getCodigo(), tipoDocumentoVO.getNome()));
			}
			setListaSelectItemTipoDocumentoVOs(itens);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoDocumentoVOs() {
		if (listaSelectItemTipoDocumentoVOs == null) {
			listaSelectItemTipoDocumentoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoDocumentoVOs;
	}

	public void setListaSelectItemTipoDocumentoVOs(List<SelectItem> listaSelectItemTipoDocumentoVOs) {
		this.listaSelectItemTipoDocumentoVOs = listaSelectItemTipoDocumentoVOs;
	}
	
	public Boolean getApresentarDadosAcademicos() {
		return getArquivoVO().getModuloDisponibilizarMaterial().equals(ModuloDisponibilizarMaterialEnum.ACADEMICO);
	}
	
	public Boolean getApresentarDadosAdministrativo() {
		return getArquivoVO().getModuloDisponibilizarMaterial().equals(ModuloDisponibilizarMaterialEnum.ADMINISTRATIVO);
	}

	public Boolean getApresentarDocumentoPortalTransparencia() {
		if (apresentarDocumentoPortalTransparencia == null) {
			apresentarDocumentoPortalTransparencia = false;
		}
		return apresentarDocumentoPortalTransparencia;
	}

	public void setApresentarDocumentoPortalTransparencia(Boolean apresentarDocumentoPortalTransparencia) {
		this.apresentarDocumentoPortalTransparencia = apresentarDocumentoPortalTransparencia;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("NOME", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("MATRICULA", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("CARGO", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		}
		return tipoConsultaComboFuncionario;
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
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarFuncionario() throws Exception {
		getFacadeFactory().getUploadArquivosComunsFacade().adicionarProfessorListaAssinaturaDigital(getListaConsultaFuncionario(), getListaFuncionarioAssinarDigitalmenteVOs(), getUsuarioLogado());
	}
	
	public void removerFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getFacadeFactory().getUploadArquivosComunsFacade().removerProfessorListaAssinaturaDigital(getListaFuncionarioAssinarDigitalmenteVOs(), obj, getUsuarioLogado());
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaFuncionarioAssinarDigitalmenteVOs() {
		if (listaFuncionarioAssinarDigitalmenteVOs == null) {
			listaFuncionarioAssinarDigitalmenteVOs = new ArrayList<FuncionarioVO>(0);
		}
		return listaFuncionarioAssinarDigitalmenteVOs;
	}

	public void setListaFuncionarioAssinarDigitalmenteVOs(List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs) {
		this.listaFuncionarioAssinarDigitalmenteVOs = listaFuncionarioAssinarDigitalmenteVOs;
	}
		
	public void consultarDepartamento() {
		try {
			super.consultar();
			List<DepartamentoVO> objs = new ArrayList<DepartamentoVO>(0);
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.CODIGO.toString())) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME_PESSOA.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePessoa(getValorConsultaDepartamento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados"); 
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarDepartamento() {
    	DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("itemDepartamento");
    	getArquivoVO().setDepartamentoVO(obj);
    	this.getListaConsultaDepartamento().clear();
    }
	
	public void limparDadosDepartamento() {
		getArquivoVO().setDepartamentoVO(null);
	}
	
	public boolean getApresentarResultadoConsultaDepartamento() {
		return getListaConsultaDepartamento().size() > 0;
	}

	public List<ArquivoVO> getListaArquivosInativosVOs() {
		if (listaArquivosInativosVOs == null) {
			listaArquivosInativosVOs = new ArrayList<ArquivoVO>(0);
		}
		return listaArquivosInativosVOs;
	}

	public void setListaArquivosInativosVOs(List<ArquivoVO> listaArquivosInativosVOs) {
		this.listaArquivosInativosVOs = listaArquivosInativosVOs;
	}
	
	public void inicializarCheckBoxApresentarPortalCoordenador() {
		if (getArquivoVO().getApresentarPortalCoordenador()) {
			getArquivoVO().setApresentarPortalCoordenador(false);
		} else {
			getArquivoVO().setApresentarPortalCoordenador(true);
		}
	}
	
	public void inicializarCheckBoxApresentarPortalProfessor() {
		if (getArquivoVO().getApresentarPortalProfessor()) {
			getArquivoVO().setApresentarPortalProfessor(false);
		} else {
			getArquivoVO().setApresentarPortalProfessor(true);
		}
	}
	
	public void inicializarCheckBoxApresentarPortalAluno() {
		if (getArquivoVO().getApresentarPortalAluno()) {
			getArquivoVO().setApresentarPortalAluno(false);
		} else {
			getArquivoVO().setApresentarPortalAluno(true);
		}
	}
	
	public void inicializarCheckBoxAssinarCertificadoDigitalUnidadeEnsino() {
		if (getArquivoVO().getAssinarCertificadoDigitalUnidadeEnsino()) {
			getArquivoVO().setAssinarCertificadoDigitalUnidadeEnsino(false);
		} else {
			getArquivoVO().setAssinarCertificadoDigitalUnidadeEnsino(true);
		}
	}
	
//	public void inicializarCheckBoxAssinarCertificadoDigitalUnidadeCertificadora() {
//		if (getArquivoVO().getAssinarCertificadoDigitalUnidadeCertificadora()) {
//			getArquivoVO().setAssinarCertificadoDigitalUnidadeCertificadora(false);
//		} else {
//			getArquivoVO().setAssinarCertificadoDigitalUnidadeCertificadora(true);
//		}
//	}
	
	public void inicializarCheckBoxApresentarDocumentoPortalTransparencia() {
		if (getArquivoVO().getApresentarDocumentoPortalTransparencia()) {
			getArquivoVO().setApresentarDocumentoPortalTransparencia(false);
		} else {
			getArquivoVO().setApresentarDocumentoPortalTransparencia(true);
		}
	}

	public void limparDadosConsultaCurso() {
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList<>());
	}

	public void limparDadosConsultaTurma() {
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<>());
	}

	public void limparDadosConsultaDisciplina() {
		setValorConsultaDisciplina("");
		setListaConsultaDisciplina(new ArrayList<>());
	}

	public void limparDadosConsultaProfessor() {
		setValorConsultaProfessor("");
		setListaConsultaProfessor(new ArrayList<>());
	}
}
