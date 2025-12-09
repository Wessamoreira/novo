package controle.administrativo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DropEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.academico.ConfiguracaoTCCMembroBancaVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoTCCEnum;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

@Controller("ConfiguracaoTCCControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoTCCControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9040052693325459249L;
	private ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO;
	private ConfiguracaoTCCVO configuracaoTCCVO;
	private ConfiguracaoTCCMembroBancaVO membroBancaPadrao;
	private List<SelectItem> listaSelectItemTipoTCC;
	private List<SelectItem> listaOpcaoConsulta;
	private List<SelectItem> listaPoliticaApresentarTCCAluno;
	private List<FuncionarioVO> listaConsultaOrientador;
	private String valorConsultaOrientador;
	private String campoConsultaOrientador;
	private String valorExtensao;
	private List<SelectItem> opcaoConsultaOrientador;
	private List<SelectItem> opcaoExtensao;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private QuestaoTCCVO questaoConteudo;
	private QuestaoTCCVO questaoFormatacao;
	
	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoTCCFacade().persistir(getConfiguracaoTCCVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		try {
			setConfiguracaoTCCArtefatoVO(null);
			setConfiguracaoTCCVO(null);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoTCCForm");
	}

	public String editar() {
		try {
			setConfiguracaoTCCArtefatoVO(null);
			setConfiguracaoTCCVO((ConfiguracaoTCCVO) getRequestMap().get("item"));
			getConfiguracaoTCCVO().setConfiguracaoTCCArtefatoVOs(getFacadeFactory().getConfiguracaoTCCArtefatoFacade().consultarPorConfiguracaoTCC(getConfiguracaoTCCVO().getCodigo()));
			getConfiguracaoTCCVO().setOrientadorPadrao(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getConfiguracaoTCCVO().getOrientadorPadrao().getCodigo(), true,false, false, getUsuarioLogado()));
			getConfiguracaoTCCVO().setMembroBancaPadraoVOs(getFacadeFactory().getConfiguracaoTCCMembroBancaFacade().consultarPorTCC(getConfiguracaoTCCVO().getCodigo()));
			getConfiguracaoTCCVO().setQuestaoConteudoVOs(getFacadeFactory().getQuestaoTCCFacade().consultarPorConfiguracao(getConfiguracaoTCCVO().getCodigo(), "conteudo"));
			getConfiguracaoTCCVO().setQuestaoFormatacaoVOs(getFacadeFactory().getQuestaoTCCFacade().consultarPorConfiguracao(getConfiguracaoTCCVO().getCodigo(), "formatacao"));
			Ordenacao.ordenarLista(getConfiguracaoTCCVO().getMembroBancaPadraoVOs(), "ordem");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoTCCForm");
	}

	public String consultar() {
		try {
			setListaConsulta(getFacadeFactory().getConfiguracaoTCCFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoTCCCons");
	}
	
	public void adicionarConfiguracaoTCCArtefatoVOs(){
		try {
			getFacadeFactory().getConfiguracaoTCCFacade().adicionarConfiguracaoTCCArtefatoVOs(getConfiguracaoTCCVO(), getConfiguracaoTCCArtefatoVO());
			setConfiguracaoTCCArtefatoVO(new ConfiguracaoTCCArtefatoVO());
			setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerConfiguracaoTCCArtefatoVOs(){
		try {
			getFacadeFactory().getConfiguracaoTCCFacade().removerConfiguracaoTCCArtefatoVOs(getConfiguracaoTCCVO(),(ConfiguracaoTCCArtefatoVO)getRequestMap().get("configuracaoTCCArtefatoItens"));
			setConfiguracaoTCCArtefatoVO(new ConfiguracaoTCCArtefatoVO());
			setMensagemID("msg_dados_excluidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String inicializarConsulta() {
		getListaConsulta().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoTCCCons");
	}

    public void alterarOrdemQuestao(DropEvent dropEvent) {
        try {
            if (dropEvent.getDragValue() instanceof VagaQuestaoVO && dropEvent.getDropValue() instanceof VagaQuestaoVO) {
                getFacadeFactory().getConfiguracaoTCCFacade().alterarOrdemQuestao(getConfiguracaoTCCVO(), (QuestaoTCCVO) dropEvent.getDragValue(), (QuestaoTCCVO) dropEvent.getDropValue());
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void adicionarQuestao() {
        try {
            getFacadeFactory().getConfiguracaoTCCFacade().adicionarQuestao(getConfiguracaoTCCVO(), getQuestaoFormatacao());
            setQuestaoFormatacao(getFacadeFactory().getQuestaoTCCFacade().novo());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerQuestao() {
        try {
        	setQuestaoFormatacao((QuestaoTCCVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoFormatacao"));
            getFacadeFactory().getConfiguracaoTCCFacade().removerQuestao(getConfiguracaoTCCVO(), getQuestaoFormatacao());
            setQuestaoFormatacao(getFacadeFactory().getQuestaoTCCFacade().novo());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarQuestao() {
        try {
            setQuestaoFormatacao(new QuestaoTCCVO());
            setQuestaoFormatacao((QuestaoTCCVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoFormatacao"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void adicionarQuestaoConteudo() {
        try {
            getFacadeFactory().getConfiguracaoTCCFacade().adicionarQuestaoConteudo(getConfiguracaoTCCVO(), getQuestaoConteudo());
            setQuestaoConteudo(getFacadeFactory().getQuestaoTCCFacade().novo());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerQuestaoConteudo() {
        try {
        	setQuestaoConteudo((QuestaoTCCVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoConteudo"));
            getFacadeFactory().getConfiguracaoTCCFacade().removerQuestaoConteudo(getConfiguracaoTCCVO(), getQuestaoConteudo());
            setQuestaoConteudo(getFacadeFactory().getQuestaoTCCFacade().novo());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarQuestaoConteudo() {
        try {
            setQuestaoConteudo(new QuestaoTCCVO());
            setQuestaoConteudo((QuestaoTCCVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoConteudo"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }    

    public void alterarOrdemOpcaoRespostaQuestao(DropEvent dropEvent) {
        try {
            if (dropEvent.getDragValue() instanceof OpcaoRespostaVagaQuestaoVO && dropEvent.getDropValue() instanceof OpcaoRespostaVagaQuestaoVO) {
                //getFacadeFactory().getQuestaoTCCFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoConteudo(), (OpcaoRespostaVagaQuestaoVO) dropEvent.getDragValue(), (OpcaoRespostaVagaQuestaoVO) dropEvent.getDropValue());
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void adicionarOpcaoRespostaQuestao() {
        try {
            //getFacadeFactory().getQuestaoTCCFacade().adicionarOrdemOpcaoRespostaQuestao(getQuestaoConteudo(), new OpcaoRespostaVagaQuestaoVO(), false);
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerOpcaoRespostaQuestao() {
        try {
            //getFacadeFactory().getQuestaoTCCFacade().removerOrdemOpcaoRespostaQuestao(getQuestaoConteudo(), (OpcaoRespostaVagaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaVagaQuestao"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarOpcaoRespostaQuestao() {
        try {
            ((OpcaoRespostaVagaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostavagaQuestao")).setEditar(true);
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
	public ConfiguracaoTCCArtefatoVO getConfiguracaoTCCArtefatoVO() {
		if (configuracaoTCCArtefatoVO == null) {
			configuracaoTCCArtefatoVO = new ConfiguracaoTCCArtefatoVO();
		}
		return configuracaoTCCArtefatoVO;
	}

	public void setConfiguracaoTCCArtefatoVO(ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) {
		this.configuracaoTCCArtefatoVO = configuracaoTCCArtefatoVO;
	}

	public ConfiguracaoTCCVO getConfiguracaoTCCVO() {
		if (configuracaoTCCVO == null) {
			configuracaoTCCVO = new ConfiguracaoTCCVO();
		}
		return configuracaoTCCVO;
	}

	public void setConfiguracaoTCCVO(ConfiguracaoTCCVO configuracaoTCCVO) {
		this.configuracaoTCCVO = configuracaoTCCVO;
	}

	public List<SelectItem> getListaSelectItemTipoTCC() {
		if (listaSelectItemTipoTCC == null) {
			listaSelectItemTipoTCC = new ArrayList<SelectItem>(0);
			listaSelectItemTipoTCC.add(new SelectItem(TipoTCCEnum.AMBOS, TipoTCCEnum.AMBOS.getValorApresentar()));
			listaSelectItemTipoTCC.add(new SelectItem(TipoTCCEnum.ARTIGO, TipoTCCEnum.ARTIGO.getValorApresentar()));
			listaSelectItemTipoTCC.add(new SelectItem(TipoTCCEnum.MONOGRAFIA, TipoTCCEnum.MONOGRAFIA.getValorApresentar()));
		}
		return listaSelectItemTipoTCC;
	}

	public void setListaSelectItemTipoTCC(List<SelectItem> listaSelectItemTipoTCC) {
		this.listaSelectItemTipoTCC = listaSelectItemTipoTCC;
	}

	public List<SelectItem> getListaOpcaoConsulta() {
		if (listaOpcaoConsulta == null) {
			listaOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaOpcaoConsulta.add(new SelectItem("descricao", "Descrição"));
		}
		return listaOpcaoConsulta;
	}

	public void setListaOpcaoConsulta(List<SelectItem> listaOpcaoConsulta) {
		this.listaOpcaoConsulta = listaOpcaoConsulta;
	}

	public List<SelectItem> getListaPoliticaApresentarTCCAluno() {
		if (listaPoliticaApresentarTCCAluno == null) {
			listaPoliticaApresentarTCCAluno = new ArrayList<SelectItem>(0);
			listaPoliticaApresentarTCCAluno.add(new SelectItem("UL", "Após último dia de Aula"));
			listaPoliticaApresentarTCCAluno.add(new SelectItem("DI", "Configurar dias antecedéncia aula TCC"));
		}
		return listaPoliticaApresentarTCCAluno;
	}

	public void setListaPoliticaApresentarTCCAluno(List<SelectItem> listaPoliticaApresentarTCCAluno) {
		this.listaPoliticaApresentarTCCAluno = listaPoliticaApresentarTCCAluno;
	}

	public void adicionarMembroBanca() {
		try {
			getMembroBancaPadrao().setOrdem(getQtdeFuncionario());
			getConfiguracaoTCCVO().adicionarObjMembroBancaVOs(getMembroBancaPadrao());
			this.setMembroBancaPadrao(new ConfiguracaoTCCMembroBancaVO());
			Ordenacao.ordenarLista(getConfiguracaoTCCVO().getMembroBancaPadraoVOs(), "ordem");
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Integer getQtdeFuncionario(){
		return getConfiguracaoTCCVO().getMembroBancaPadraoVOs().size();
	}
	
	public void descerMembroBanca() {
		try {
			ConfiguracaoTCCMembroBancaVO obj = (ConfiguracaoTCCMembroBancaVO) context().getExternalContext().getRequestMap().get("membroBanca");
			getConfiguracaoTCCVO().alterarOrdemMembroBanca(configuracaoTCCVO.getMembroBancaPadraoVOs(), obj, false);
			this.setMembroBancaPadrao(new ConfiguracaoTCCMembroBancaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirMembroBanca() {
		try {
			ConfiguracaoTCCMembroBancaVO obj = (ConfiguracaoTCCMembroBancaVO) context().getExternalContext().getRequestMap().get("membroBanca");
			getConfiguracaoTCCVO().alterarOrdemMembroBanca(configuracaoTCCVO.getMembroBancaPadraoVOs(), obj, true);
			this.setMembroBancaPadrao(new ConfiguracaoTCCMembroBancaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerMembroBanca() {
		try {
			ConfiguracaoTCCMembroBancaVO obj = (ConfiguracaoTCCMembroBancaVO) context().getExternalContext().getRequestMap().get("membroBanca");
			getConfiguracaoTCCVO().excluirObjMembroBancaVOs(obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarExtensao() {
		try {
			if (!getValorExtensao().equals("")) {
				getConfiguracaoTCCVO().adicionarExtensao(getValorExtensao());
				setValorExtensao("");
				setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			} else {
				throw new Exception("Selecione a Extens?o que deseja adicionar!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerExtensao() {
		try {
			if (!getValorExtensao().equals("")) {
				getConfiguracaoTCCVO().removerExtensao(getValorExtensao());
				setValorExtensao("");
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			} else {
				throw new Exception("Selecione a Extens?o que deseja remover!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public ConfiguracaoTCCMembroBancaVO getMembroBancaPadrao() {
		if (membroBancaPadrao == null) {
			membroBancaPadrao = new ConfiguracaoTCCMembroBancaVO();
		}
		return membroBancaPadrao;
	}

	public void setMembroBancaPadrao(ConfiguracaoTCCMembroBancaVO membroBancaPadrao) {
		this.membroBancaPadrao = membroBancaPadrao;
	}

	public void consultarOrientador() {
		try {
            setListaConsultaOrientador(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaOrientador(), "FU", 
            		0, 
            		false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaOrientador().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}
	
	public void selecionarOrientador() {
		FuncionarioVO funcionarioVO = (FuncionarioVO) getRequestMap().get("professorItens");
		try {
			getConfiguracaoTCCVO().setOrientadorPadrao(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(funcionarioVO.getPessoa().getCodigo(), true,false, false, getUsuarioLogado()));			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<FuncionarioVO> getListaConsultaOrientador() {
		if (listaConsultaOrientador == null) {
			listaConsultaOrientador = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaOrientador;
	}

	public void setListaConsultaOrientador(List<FuncionarioVO> listaConsultaOrintador) {
		this.listaConsultaOrientador = listaConsultaOrintador;
	}

	public String getValorConsultaOrientador() {
		if (valorConsultaOrientador == null) {
			valorConsultaOrientador = "";
		}
		return valorConsultaOrientador;
	}

	public void setValorConsultaOrientador(String valorConsultaOrientador) {
		this.valorConsultaOrientador = valorConsultaOrientador;
	}

	public String getCampoConsultaOrientador() {
		if (campoConsultaOrientador == null) {
			campoConsultaOrientador = "nome";
		}
		return campoConsultaOrientador;
	}

	public void setCampoConsultaOrientador(String campoConsultaOrientador) {
		this.campoConsultaOrientador = campoConsultaOrientador;
	}

	public List<SelectItem> getOpcaoConsultaOrientador() {
		if (opcaoConsultaOrientador == null) {
			opcaoConsultaOrientador = new ArrayList<SelectItem>(0);
			opcaoConsultaOrientador.add(new SelectItem("nome", "Nome"));
		}
		return opcaoConsultaOrientador;
	}

	public void setOpcaoConsultaOrientador(List<SelectItem> opcaoConsultaOrientador) {
		this.opcaoConsultaOrientador = opcaoConsultaOrientador;
	}

	public List<SelectItem> getOpcaoExtensao() {
		
		if (opcaoExtensao == null) {
			opcaoExtensao = new ArrayList<SelectItem>(0);
			opcaoExtensao.add(new SelectItem("", ""));			
			opcaoExtensao.add(new SelectItem("xls;xlsx", "EXCEL"));			
			opcaoExtensao.add(new SelectItem("jpeg;jpg", "JPEG"));
			opcaoExtensao.add(new SelectItem("pdf", "PDF"));			
			opcaoExtensao.add(new SelectItem("png", "PNG"));
			opcaoExtensao.add(new SelectItem("ppt;pptx", "PPT"));
			opcaoExtensao.add(new SelectItem("doc;docx", "DOC"));
		}
		return opcaoExtensao;
	}
	
	public void setOpcaoExtensao(List<SelectItem> opcaoExtensao) {
		this.opcaoExtensao = opcaoExtensao;
	}

	public String getValorExtensao() {
		if (valorExtensao == null) {
			valorExtensao = "";
		}
		return valorExtensao;
	}

	public void setValorExtensao(String valorExtensao) {
		this.valorExtensao = valorExtensao;
	}

	public List<SelectItem> getTipoConsultaFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matr?cula"));
		return itens;
	}
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getMembroBancaPadrao().setMembro(obj);
		getMembroBancaPadrao().setNome(obj.getPessoa().getNome());
		adicionarMembroBanca();
		listaConsultaFuncionario.clear();
		this.setValorConsultaFuncionario("");
		this.setCampoConsultaFuncionario("");
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(
			List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}
	
	public QuestaoTCCVO getQuestaoConteudo() {
		if (questaoConteudo == null) {
			questaoConteudo = new QuestaoTCCVO();
			questaoFormatacao.setOrigemQuestao("conteudo");
		}
		return questaoConteudo;
	}

	public void setQuestaoConteudo(QuestaoTCCVO questaoConteudo) {
		this.questaoConteudo = questaoConteudo;
	}

	public QuestaoTCCVO getQuestaoFormatacao() {
		if (questaoFormatacao == null) {
			questaoFormatacao = new QuestaoTCCVO();
			questaoFormatacao.setOrigemQuestao("formatacao");
		}
		return questaoFormatacao;
	}

	public void setQuestaoFormatacao(QuestaoTCCVO questaoFormatacao) {
		this.questaoFormatacao = questaoFormatacao;
	}

}