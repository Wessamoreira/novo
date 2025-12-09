package relatorio.controle.processosel;

import java.util.List;

import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ProcessarResultadoProcessoSeletivoRelVO;

@Controller("ProcessarResultadoProcessoSeletivoRelControle")
@Scope("request")
@Lazy
public class ProcessarResultadoProcessoSeletivoRelControle extends SuperControleRelatorio {

	private ResultadoProcessamentoArquivoRespostaVO resultadoProcessamentoArquivoResposta;
	private String descricaoProcSeletivo;
	private String dataProva;
	private String sala;
	
	public void imprimirPDF() {
		String titulo = null;
		String design = null;
		List<ProcessarResultadoProcessoSeletivoRelVO> listaObjetos = null;
		try {
			titulo = "PROCESSAR RESULTADO PROCESSO SELETIVO";
			design = getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getDesignIReportRelatorio();
			listaObjetos = getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().criarObjeto(getResultadoProcessamentoArquivoResposta().getInscricaoRespostaNaoProcessadaVOs(), getResultadoProcessamentoArquivoResposta().getResultadoProcessoSeletivoVOs());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProcessarResultadoProcessoSeletivoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().adicionarParametro("descricaoProcSeletivo", getDescricaoProcSeletivo());
				getSuperParametroRelVO().adicionarParametro("dataProva", getDataProva());
				getSuperParametroRelVO().adicionarParametro("sala", getSala());
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public ResultadoProcessamentoArquivoRespostaVO getResultadoProcessamentoArquivoResposta() {
		if (resultadoProcessamentoArquivoResposta == null) {
			resultadoProcessamentoArquivoResposta = new ResultadoProcessamentoArquivoRespostaVO();
		}
		return resultadoProcessamentoArquivoResposta;
	}

	public void setResultadoProcessamentoArquivoResposta(ResultadoProcessamentoArquivoRespostaVO resultadoProcessamentoArquivoResposta) {
		this.resultadoProcessamentoArquivoResposta = resultadoProcessamentoArquivoResposta;
	}

	public String getDescricaoProcSeletivo() {
		if (descricaoProcSeletivo == null) {
			descricaoProcSeletivo = "";
		}
		return descricaoProcSeletivo;
	}

	public void setDescricaoProcSeletivo(String descricaoProcSeletivo) {
		this.descricaoProcSeletivo = descricaoProcSeletivo;
	}

	public String getDataProva() {
		if (dataProva == null) {
			dataProva = "";
		}
		return dataProva;
	}

	public void setDataProva(String dataProva) {
		this.dataProva = dataProva;
	}

	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}
}
