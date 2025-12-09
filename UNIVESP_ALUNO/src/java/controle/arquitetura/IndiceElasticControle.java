package controle.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.enumeradores.PesquisaPadraoEnum;
import negocio.comuns.elastic.ExcecaoElasticVO;
import negocio.comuns.elastic.IndiceElasticVO;
import negocio.comuns.elastic.IndiceVersaoElasticVO;
import negocio.comuns.elastic.SincronismoElasticVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("IndiceElasticControle")
@Scope("session")
@Lazy
public class IndiceElasticControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private IndiceElasticVO indiceElasticVO;
	private IndiceVersaoElasticVO indiceVersaoElasticVO;
	private String indiceElastic;
	private List<SincronismoElasticVO> sincronismos;
	private List<ExcecaoElasticVO> excecoesPesquisa;
	private List<ExcecaoElasticVO> excecoesAgendamento;
	private List<ExcecaoElasticVO> excecoesSincronismo;

    public IndiceElasticControle() throws Exception {
    	atualizarInformacoesElastic();
    }
    
    public Integer getTotalExcecoesPesquisa() {
    	try {
    		return getFacadeFactory().getIndiceElasticFacade().consultarTotalExcecoesPesquisa();
    	} catch (Exception e) {
    		return 0;
    	}
    }
    
    public Integer getTotalExcecoesAgendamento() {
    	try {
    		return getFacadeFactory().getIndiceElasticFacade().consultarTotalExcecoesAgendamento();
    	} catch (Exception e) {
    		return 0;
    	}
    }
    
    public Integer getTotalExcecoesSincronismo() {
    	try {
    		return getFacadeFactory().getIndiceElasticFacade().consultarTotalExcecoesSincronismo();
    	} catch (Exception e) {
    		return 0;
    	}
    }

    public void incluirIndice() {
        try {
        	getIndiceElasticVO().setNome(getIndiceElastic());
            getFacadeFactory().getIndiceElasticFacade().executarCriacaoIndiceElastic(getIndiceElasticVO(), getUsuarioLogado());
            realizarConsultaIndiceElastic();
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void excluirIndice() {
        try {
            getFacadeFactory().getIndiceElasticFacade().executarExclusaoIndiceElastic(getIndiceElasticVO(), getUsuarioLogado());
            realizarConsultaIndiceElastic();
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void incluirVersao() {
        try {
        	getIndiceVersaoElasticVO().getIndice().setNome(getIndiceElasticVO().getNome());
        	getIndiceVersaoElasticVO().getIndice().setUrl(getIndiceElasticVO().getUrl());
            getFacadeFactory().getIndiceElasticFacade().executarCriacaoIndiceVersaoElastic(getIndiceVersaoElasticVO(), getUsuarioLogado());
            realizarConsultaIndiceElastic();
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void excluirVersao() {
        try {
        	IndiceVersaoElasticVO obj = (IndiceVersaoElasticVO) context().getExternalContext().getRequestMap().get("versao");
            getFacadeFactory().getIndiceElasticFacade().executarExclusaoIndiceVersaoElastic(obj, getUsuarioLogado());
            realizarConsultaIndiceElastic();
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void ativarVersao() {
        try {
        	IndiceVersaoElasticVO obj = (IndiceVersaoElasticVO) context().getExternalContext().getRequestMap().get("versao");
            getFacadeFactory().getIndiceElasticFacade().executarAtivacaoIndiceVersaoElastic(obj, getUsuarioLogado());
            realizarConsultaIndiceElastic();
            setMensagemID("msg_dados_indice_ativado");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void agendarVersao() {
        try {
        	IndiceVersaoElasticVO obj = (IndiceVersaoElasticVO) context().getExternalContext().getRequestMap().get("versao");
            getFacadeFactory().getIndiceElasticFacade().executarAgendamentoIndexacaoVersaoElastic(obj, getUsuarioLogado());
            setMensagemID("msg_dados_indice_agendado");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public void atualizarInformacoesElastic() {
    	realizarConsultaIndiceElastic();
    	realizarConsultaSincronismosElastic();
    	realizarConsultaExcecoesAgendamentoElastic();
    	realizarConsultaExcecoesSincronismoElastic();
    	realizarConsultaExcecoesPesquisaElastic();
    }

	public IndiceElasticVO getIndiceElasticVO() {
		if (indiceElasticVO == null) {
			indiceElasticVO = new IndiceElasticVO();
		}
		return indiceElasticVO;
	}

	public void setIndiceElasticVO(IndiceElasticVO indiceElasticVO) {
		this.indiceElasticVO = indiceElasticVO;
	}

	public IndiceVersaoElasticVO getIndiceVersaoElasticVO() {
		if (indiceVersaoElasticVO == null) {
			indiceVersaoElasticVO = new IndiceVersaoElasticVO();
		}
		return indiceVersaoElasticVO;
	}

	public void setIndiceVersaoElasticVO(IndiceVersaoElasticVO indiceVersaoElasticVO) {
		this.indiceVersaoElasticVO = indiceVersaoElasticVO;
	}
	
	public String getIndiceElastic() {
		if (indiceElastic == null) {
			indiceElastic = PesquisaPadraoEnum.ALUNO.getName();
		}
		return indiceElastic;
	}

	public void setIndiceElastic(String indiceElastic) {
		this.indiceElastic = indiceElastic;
	}
	
	public void realizarConsultaIndiceElastic() {
		try {
			setIndiceElasticVO(getFacadeFactory().getIndiceElasticFacade().consultarPorNome(getIndiceElastic().toLowerCase(), getUsuarioLogado()));
			setIndiceVersaoElasticVO(new IndiceVersaoElasticVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarConsultaSincronismosElastic() {
		try {
			setSincronismos(getFacadeFactory().getIndiceElasticFacade().consultarSincronismos());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarConsultaExcecoesSincronismoElastic() {
		try {
			setExcecoesSincronismo(getFacadeFactory().getIndiceElasticFacade().consultarExcecoesSincronismo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarConsultaExcecoesAgendamentoElastic() {
		try {
			setExcecoesAgendamento(getFacadeFactory().getIndiceElasticFacade().consultarExcecoesAgendamento());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarConsultaExcecoesPesquisaElastic() {
		try {
			setExcecoesPesquisa(getFacadeFactory().getIndiceElasticFacade().consultarExcecoesPesquisa());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemIndiceElastic() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (PesquisaPadraoEnum obj : PesquisaPadraoEnum.values()) {
			lista.add(new SelectItem(obj.getName(), obj.getDescricao()));
		}
		return lista;
	}

	public List<SincronismoElasticVO> getSincronismos() {
		if (sincronismos == null) {
			sincronismos = new ArrayList<SincronismoElasticVO>();
		}
		return sincronismos;
	}

	public void setSincronismos(List<SincronismoElasticVO> sincronismos) {
		this.sincronismos = sincronismos;
	}

	public List<ExcecaoElasticVO> getExcecoesPesquisa() {
		if (excecoesPesquisa == null) {
			excecoesPesquisa = new ArrayList<ExcecaoElasticVO>();
		}
		return excecoesPesquisa;
	}

	public void setExcecoesPesquisa(List<ExcecaoElasticVO> excecoesPesquisa) {
		this.excecoesPesquisa = excecoesPesquisa;
	}

	public List<ExcecaoElasticVO> getExcecoesAgendamento() {
		if (excecoesAgendamento == null) {
			excecoesAgendamento = new ArrayList<ExcecaoElasticVO>();
		}
		return excecoesAgendamento;
	}

	public void setExcecoesAgendamento(List<ExcecaoElasticVO> excecoesAgendamento) {
		this.excecoesAgendamento = excecoesAgendamento;
	}

	public List<ExcecaoElasticVO> getExcecoesSincronismo() {
		if (excecoesSincronismo == null) {
			excecoesSincronismo = new ArrayList<ExcecaoElasticVO>();
		}
		return excecoesSincronismo;
	}

	public void setExcecoesSincronismo(List<ExcecaoElasticVO> excecoesSincronismo) {
		this.excecoesSincronismo = excecoesSincronismo;
	}

}
