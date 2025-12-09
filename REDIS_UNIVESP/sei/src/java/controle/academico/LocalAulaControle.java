package controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.academico.MaterialProfessorVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("LocalAulaControle")
@Scope("viewScope")
@Lazy
public class LocalAulaControle extends SuperControle {

    /**
     *
     */
    private static final long serialVersionUID = 4800130956433195393L;
    private LocalAulaVO localAulaVO;
    private SalaLocalAulaVO salaLocalAulaVO;
    private List<SelectItem> opcaoConsulta;
    private MaterialAlunoVO materialAlunoVO;
    private MaterialProfessorVO materialProfessorVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String erroUpload;
    private String msgErroUpload;
    private UnidadeEnsinoVO unidadeEnsino;

    public void persistir() {
        try {
            getFacadeFactory().getLocalAulaFacade().persistir(getLocalAulaVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException ce) {
            setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void ativar() {
        try {
            getFacadeFactory().getLocalAulaFacade().ativar(getLocalAulaVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException ce) {
            setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void inativar() {
        try {
            getFacadeFactory().getLocalAulaFacade().inativar(getLocalAulaVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException ce) {
            setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String novo() {
        setLocalAulaVO(null);
        setSalaLocalAulaVO(null);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("localSalaForm");
    }

    public String editar() {
        try {
            LocalAulaVO localAulaVO = (LocalAulaVO) getRequestMap().get("localAulaItem");
            setLocalAulaVO(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(localAulaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            //getLocalAulaVO().setSalaLocalAulaVOs(getFacadeFactory().getSalaLocalAulaFacade().consultarPorLocalAula(getLocalAulaVO().getCodigo()));
            setSalaLocalAulaVO(null);
            setMensagemID("msg_entre_dados", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("localSalaForm");
    }

    public String consultar() {
        try {
            getControleConsulta().setListaConsulta(getFacadeFactory().getLocalAulaFacade().consulta(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),getUnidadeEnsino().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_entre_dados", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

        return Uteis.getCaminhoRedirecionamentoNavegacao("localSalaCons");
    }

    public String irPaginaConsulta() {
        setControleConsulta(null);
        limparMensagem();
        return Uteis.getCaminhoRedirecionamentoNavegacao("localSalaCons");
    }

    public void adicionarSalaLocaAula() {
        try {
        	SalaLocalAulaVO salaLocalAula = new SalaLocalAulaVO();
        	salaLocalAula.setOrdem(getLocalAulaVO().getSalaLocalAulaVOs().size()+1);
        	salaLocalAula.setSala(getSalaLocalAulaVO().getSala());
        	salaLocalAula.setCapacidade(getSalaLocalAulaVO().getCapacidade());
        	salaLocalAula.setNaoControlarChoqueSala(getSalaLocalAulaVO().getNaoControlarChoqueSala());
            getFacadeFactory().getLocalAulaFacade().adicionarSalaLocalAulaVO(getLocalAulaVO(), salaLocalAula);
            setSalaLocalAulaVO(new SalaLocalAulaVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException ce) {
            setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerSalaLocaAula() {
        try {
            getFacadeFactory().getLocalAulaFacade().removerSalaLocalAulaVO(getLocalAulaVO(), (SalaLocalAulaVO) getRequestMap().get("salaLocalAulaItem"));
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException ce) {
            setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getCaminhoServidorDownloadProfessor() {
        try {
            MaterialProfessorVO obj = (MaterialProfessorVO) context().getExternalContext().getRequestMap().get("materialProfessorVOItem");
        	context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public String getCaminhoServidorDownloadAluno() {
        try {
            MaterialAlunoVO obj = (MaterialAlunoVO) context().getExternalContext().getRequestMap().get("materialAlunoVOItem");
        	context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public void upLoadArquivoAluno(FileUploadEvent uploadEvent) {
        try {

            if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
                if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
                    setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
                    setMsgErroUpload("Prezado, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
                } else {
                    setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
                    getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialAlunoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
                }
            } else {
                getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialAlunoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public void upLoadArquivoProfessor(FileUploadEvent uploadEvent) {
        try {

            if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
                if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
                    setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
                    setMsgErroUpload("Prezado professor/coordenador, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
                } else {
                    setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
                    getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialProfessorVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
                }
            } else {
                getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialProfessorVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public void removerMaterialAluno() {
        try {
            MaterialAlunoVO obj = (MaterialAlunoVO) context().getExternalContext().getRequestMap().get("materialAlunoVOItem");
            realizarExcluirMaterialAluno(obj, 0);
            if (getMaterialAlunoVO().getCodigo().equals(obj.getCodigo())) {
                setMaterialAlunoVO(new MaterialAlunoVO());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarExcluirMaterialAluno(MaterialAlunoVO obj, Integer aux) throws Exception {
        Iterator<MaterialAlunoVO> i = getLocalAulaVO().getMaterialAlunoVOs().iterator();
        while (i.hasNext()) {
            MaterialAlunoVO vo = (MaterialAlunoVO) i.next();
            if (vo.equals(obj)) {
                //caso o vo escolhido para remover tenha codigo, entao deve ser tbm deletado do banco
                if (!vo.isNovoObj() && aux.intValue() == 0) {
                    getFacadeFactory().getMaterialAlunoFacade().excluir(vo);
                }
                i.remove();
                break;
            }
        }
    }

    public void adicionarMaterialAluno() throws Exception {
        try {
            //getFacadeFactory().getCursoFacade().validarReconhecimentoCurso(getAutorizacaoCurso(), getCursoVO());
            realizarExcluirMaterialAluno(getMaterialAlunoVO(), 1);
            getFacadeFactory().getLocalAulaFacade().validarMaterialAluno(getMaterialAlunoVO());
            getLocalAulaVO().getMaterialAlunoVOs().add(getMaterialAlunoVO());
            setMaterialAlunoVO(new MaterialAlunoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerMaterialProfessor() {
        try {
            MaterialProfessorVO obj = (MaterialProfessorVO) context().getExternalContext().getRequestMap().get("materialProfessorVOItem");
            realizarExcluirMaterialProfessor(obj, 0);
            if (getMaterialProfessorVO().getCodigo().equals(obj.getCodigo())) {
                setMaterialProfessorVO(new MaterialProfessorVO());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarExcluirMaterialProfessor(MaterialProfessorVO obj, Integer aux) throws Exception {
        Iterator<MaterialProfessorVO> i = getLocalAulaVO().getMaterialProfessorVOs().iterator();
        while (i.hasNext()) {
            MaterialProfessorVO vo = (MaterialProfessorVO) i.next();
            if (vo.equals(obj)) {
                //caso o vo escolhido para remover tenha codigo, entao deve ser tbm deletado do banco
                if (!vo.isNovoObj() && aux.intValue() == 0) {
                    getFacadeFactory().getMaterialProfessorFacade().excluir(vo);
                }
                i.remove();
                break;
            }
        }
    }

    public void adicionarMaterialProfessor() throws Exception {
        try {
            realizarExcluirMaterialProfessor(getMaterialProfessorVO(), 1);
            getFacadeFactory().getLocalAulaFacade().validarMaterialProfessor(getMaterialProfessorVO());
            getLocalAulaVO().getMaterialProfessorVOs().add(getMaterialProfessorVO());
            setMaterialProfessorVO(new MaterialProfessorVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public void realizarSubirOrdem(){
		getFacadeFactory().getLocalAulaFacade().realizarAlteracaoOrdem(getLocalAulaVO(),  (SalaLocalAulaVO) context().getExternalContext().getRequestMap().get("salaLocalAulaItem"), true);
	}
	
	public void realizarDescerOrdem(){
		getFacadeFactory().getLocalAulaFacade().realizarAlteracaoOrdem(getLocalAulaVO(), (SalaLocalAulaVO) context().getExternalContext().getRequestMap().get("salaLocalAulaItem"), false);
	}

    public LocalAulaVO getLocalAulaVO() {
        if (localAulaVO == null) {
            localAulaVO = new LocalAulaVO();
        }
        return localAulaVO;
    }

    public void setLocalAulaVO(LocalAulaVO localAulaVO) {
        this.localAulaVO = localAulaVO;
    }

    public SalaLocalAulaVO getSalaLocalAulaVO() {
        if (salaLocalAulaVO == null) {
            salaLocalAulaVO = new SalaLocalAulaVO();
        }
        return salaLocalAulaVO;
    }

    public void setSalaLocalAulaVO(SalaLocalAulaVO salaLocalAulaVO) {
        this.salaLocalAulaVO = salaLocalAulaVO;
    }

    public List<SelectItem> getOpcaoConsulta() {
        if (opcaoConsulta == null) {
            opcaoConsulta = new ArrayList<SelectItem>(0);
            opcaoConsulta.add(new SelectItem("local", "Local"));
            opcaoConsulta.add(new SelectItem("endereco", "Endereço"));
        }
        return opcaoConsulta;
    }

    public void setOpcaoConsulta(List<SelectItem> opcaoConsulta) {
        this.opcaoConsulta = opcaoConsulta;
    }

    /**
     * @return the materialAlunoVO
     */
    public MaterialAlunoVO getMaterialAlunoVO() {
        if (materialAlunoVO == null) {
            materialAlunoVO = new MaterialAlunoVO();
        }
        return materialAlunoVO;
    }

    /**
     * @param materialAlunoVO the materialAlunoVO to set
     */
    public void setMaterialAlunoVO(MaterialAlunoVO materialAlunoVO) {
        this.materialAlunoVO = materialAlunoVO;
    }

    /**
     * @return the materialProfessorVO
     */
    public MaterialProfessorVO getMaterialProfessorVO() {
        if (materialProfessorVO == null) {
            materialProfessorVO = new MaterialProfessorVO();
        }
        return materialProfessorVO;
    }

    /**
     * @param materialProfessorVO the materialProfessorVO to set
     */
    public void setMaterialProfessorVO(MaterialProfessorVO materialProfessorVO) {
        this.materialProfessorVO = materialProfessorVO;
    }

    public String getErroUpload() {
        if (erroUpload == null) {
            erroUpload = "";
        }
        return erroUpload;
    }

    public void setErroUpload(String erroUpload) {
        this.erroUpload = erroUpload;
    }

    public String getMsgErroUpload() {
        if (msgErroUpload == null) {
            msgErroUpload = "";
        }
        return msgErroUpload;
    }

    public void setMsgErroUpload(String msgErroUpload) {
        this.msgErroUpload = msgErroUpload;
    }

    public String getVerificarUltrapassouTamanhoMaximoUpload() {
        try {
            return "Arquivo não Enviado. Tamanho Máximo Permitido " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
        } catch (Exception e) {
            return "";
        }

    }

    public String getTamanhoMaximoUpload() {
        try {
            return "Tamanho Máximo Permitido: " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
        } catch (Exception e) {
            return "Tamanho Máximo Não Configurado";
        }
    }
    
	public Integer getTamanhoListaSalaLocalAula(){
		return getLocalAulaVO().getSalaLocalAulaVOs().size();
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}	

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if(unidadeEnsino == null){
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
		
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
}
