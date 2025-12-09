package controle.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("IntegracaoGinfesCursoControle")
@Scope("viewScope")
@Lazy
public class IntegracaoGinfesCursoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntegracaoGinfesCursoVO integracaoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Integer codigoUnidadeEnsino;
	private Boolean naoImportados;
	private Boolean fazerDownload;
	private String nomeArquivoImportado;

	public IntegracaoGinfesCursoControle() throws Exception {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta("nome");
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		setIntegracaoVO(new IntegracaoGinfesCursoVO());
		getIntegracaoVO().setDataImportacao(null);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesCursoForm");
	}

	public String editar() {
		try {
			IntegracaoGinfesCursoVO obj = (IntegracaoGinfesCursoVO) context().getExternalContext().getRequestMap().get("item");
			setIntegracaoVO(getFacadeFactory().getIntegracaoGinfesCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesCursoForm");
	}

	public void persistir() {
		try {
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			getFacadeFactory().getIntegracaoGinfesCursoFacade().persistir(getIntegracaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void executarDefinicaoComoImportado() {
		try {
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			if (getIntegracaoVO().getCursos().size() < 1) {
				throw new Exception("Importação não possui nenhum curso!");
			}
			getIntegracaoVO().setImportado(true);
			getIntegracaoVO().setDataImportacao(new Date());
			getFacadeFactory().getIntegracaoGinfesCursoFacade().importar(getIntegracaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getIntegracaoVO().setImportado(false);
			getIntegracaoVO().setDataImportacao(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void executarGeracaoCursos() {
		try {
			if (getIntegracaoVO().getUnidadeEnsino().getCodigo().intValue() == 0) {
				throw new Exception("Informe a Unidade de Ensino!");
			}
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			getIntegracaoVO().setCursos(getFacadeFactory().getIntegracaoGinfesCursoItemFacade().gerarCursos(getIntegracaoVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getDownload() {
		if (getFazerDownload()) {
			setFazerDownload(false);
			try {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + getNomeArquivoImportado() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return "";
	}
	
	public void executarDownload() {
		try {
			setNomeArquivoImportado(getFacadeFactory().getIntegracaoGinfesCursoFacade().executarGeracaoArquivoImportacao(getIntegracaoVO(), 50, getUsuarioLogado()));
			setFazerDownload(true);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getIntegracaoGinfesCursoFacade().excluir(getIntegracaoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesCursoForm");
	}
	
	public void removerCurso() throws Exception {
        IntegracaoGinfesCursoItemVO obj = (IntegracaoGinfesCursoItemVO) context().getExternalContext().getRequestMap().get("c");
        getIntegracaoVO().getCursos().remove(obj);
        setMensagemID("msg_dados_excluidos");
    }
	
	public String consultar() {
        try {
            super.consultar();
            List<IntegracaoGinfesCursoVO> objs = getFacadeFactory().getIntegracaoGinfesCursoFacade()
            		.consultar(getCodigoUnidadeEnsino(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getNaoImportados(), true, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<IntegracaoGinfesCursoVO>());
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
	
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesCursoCons");
    }
	
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public void irPaginaInicial() throws Exception {
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

	public IntegracaoGinfesCursoVO getIntegracaoVO() {
		if (integracaoVO == null) {
			integracaoVO = new IntegracaoGinfesCursoVO();
		}
		return integracaoVO;
	}

	public void setIntegracaoVO(IntegracaoGinfesCursoVO integracaoVO) {
		this.integracaoVO = integracaoVO;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			montarListaSelectItemUnidadeEnsino();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		try {
			for (UnidadeEnsinoVO obj : consultarUnidadeEnsinoPorNome("")) {
				objs.add(new SelectItem(obj.getCodigo().toString(), obj.getNome() + " - " + obj.getAbreviatura()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setListaSelectItemUnidadeEnsino(objs);
	}
	
	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	public Boolean getNaoImportados() {
		if (naoImportados == null) {
			naoImportados = false;
		}
		return naoImportados;
	}

	public void setNaoImportados(Boolean naoImportados) {
		this.naoImportados = naoImportados;
	}

	public Boolean getFazerDownload() {
		if (fazerDownload == null) {
			fazerDownload = false;
		}
		return fazerDownload;
	}

	public void setFazerDownload(Boolean fazerDownload) {
		this.fazerDownload = fazerDownload;
	}

	public String getNomeArquivoImportado() {
		if (nomeArquivoImportado == null) {
			nomeArquivoImportado = "";
		}
		return nomeArquivoImportado;
	}

	public void setNomeArquivoImportado(String nomeArquivoImportado) {
		this.nomeArquivoImportado = nomeArquivoImportado;
	}

}
