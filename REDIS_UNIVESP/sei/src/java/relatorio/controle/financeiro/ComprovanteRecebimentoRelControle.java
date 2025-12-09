package relatorio.controle.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ComprovanteRecebimentoRelVO;

@Controller("ComprovanteRecebimentoRelControle")
@Scope("request")
@Lazy
public class ComprovanteRecebimentoRelControle extends SuperControleRelatorio {

	protected List<ComprovanteRecebimentoRelVO> listaComprovanteRecebimentoRelVOs;
	protected NegociacaoRecebimentoVO negociacaoRecebimentoVO;

	public ComprovanteRecebimentoRelControle() throws Exception {
	}

	public void imprimirPDF() {
		String titulo = null;
		String design = null;

		try {
			titulo = "COMPROVANTE DE RECEBIMENTO";
			design = getFacadeFactory().getComprovanteRecebimentoRelFacade().getDesignIReportRelatorio();
			getNegociacaoRecebimentoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			setListaComprovanteRecebimentoRelVOs(getFacadeFactory().getComprovanteRecebimentoRelFacade().criarObjeto(getNegociacaoRecebimentoVO(), getUsuarioLogado()));
			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0) && !getListaComprovanteRecebimentoRelVOs().isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getNome());
				if (!getNegociacaoRecebimentoVO().getMatricula().equals("")) {
					getSuperParametroRelVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(negociacaoRecebimentoVO.getMatricula().toString(), getUsuarioLogado()).getIdentificadorTurma());
					getSuperParametroRelVO().setTurno(getFacadeFactory().getTurnoFacade().consultaRapidaPorMatricula(negociacaoRecebimentoVO.getMatricula(), getUsuarioLogado()).getNome());
					getSuperParametroRelVO().setMatricula(getNegociacaoRecebimentoVO().getMatricula());
				}
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setListaObjetos(getListaComprovanteRecebimentoRelVOs());
				getSuperParametroRelVO().adicionarParametro("razaoSocialUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getRazaoSocial());
				getSuperParametroRelVO().adicionarParametro("enderecoUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getEnderecoCompleto());
				getSuperParametroRelVO().adicionarParametro("cnpjUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getCNPJ());
				getSuperParametroRelVO().adicionarParametro("telefonesUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getTelefones());
				getSuperParametroRelVO().adicionarParametro("observacaoComprovanteRecebimento", getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getObservacaoComprovanteRecebimento());				

				UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				if (ue.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoUsuario", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoUsuario", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}

				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaComprovanteRecebimentoRelVOs());
		}
	}

	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
		if (negociacaoRecebimentoVO == null) {
			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		}
		return negociacaoRecebimentoVO;
	}

	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
	}

	public List<ComprovanteRecebimentoRelVO> getListaComprovanteRecebimentoRelVOs() {
		if (listaComprovanteRecebimentoRelVOs == null) {
			listaComprovanteRecebimentoRelVOs = new ArrayList<ComprovanteRecebimentoRelVO>(0);
		}
		return listaComprovanteRecebimentoRelVOs;
	}

	public void setListaComprovanteRecebimentoRelVOs(List<ComprovanteRecebimentoRelVO> listaComprovanteRecebimentoRelVOs) {
		this.listaComprovanteRecebimentoRelVOs = listaComprovanteRecebimentoRelVOs;
	}
	
	public void imprimirPDFRecebimentoCartaoCredito(MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		String titulo = null;
		String design = null;
		titulo = "COMPROVANTE DE RECEBIMENTO";
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		getNegociacaoRecebimentoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), false, usuario));
		setListaComprovanteRecebimentoRelVOs(getFacadeFactory().getComprovanteRecebimentoRelFacade().montarObjetoComprovanteReciboCartaoCredito(getNegociacaoRecebimentoVO(), usuario));
		design = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ComprovanteRecebimentoCartaoCreditoRel.jrxml";
		if (!getNegociacaoRecebimentoVO().getCodigo().equals(0) && !getListaComprovanteRecebimentoRelVOs().isEmpty()) {
			getSuperParametroRelVO().limparParametros();
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(usuario != null? usuario.getNome(): "");
			getSuperParametroRelVO().setUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getNome());
			if (!getNegociacaoRecebimentoVO().getMatricula().equals("")) {
				getSuperParametroRelVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario).getNome());
				getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(getNegociacaoRecebimentoVO().getMatricula().toString(), usuario).getIdentificadorTurma());
				getSuperParametroRelVO().setTurno(getFacadeFactory().getTurnoFacade().consultaRapidaPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), usuario).getNome());
				getSuperParametroRelVO().setMatricula(getNegociacaoRecebimentoVO().getMatricula());
			}
			getSuperParametroRelVO().setUsuarioVO(usuario);
			getSuperParametroRelVO().setListaObjetos(getListaComprovanteRecebimentoRelVOs());
			getSuperParametroRelVO().adicionarParametro("razaoSocialUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getRazaoSocial());
			getSuperParametroRelVO().adicionarParametro("enderecoUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getEnderecoCompleto());
			getSuperParametroRelVO().adicionarParametro("cnpjUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getCNPJ());
			getSuperParametroRelVO().adicionarParametro("telefonesUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getTelefones());
			getFacadeFactory().getComprovanteRecebimentoRelFacade().validaLogoComprovanteRecibo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCaminhoBaseLogo(), getNegociacaoRecebimentoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio(), getSuperParametroRelVO(), configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
			realizarImpressaoRelatorio();
		}	
	}
	
	
	public void imprimirPDFRecebimentoCartaoCredito() throws Exception {
		String titulo = null;
		String design = null;
		titulo = "COMPROVANTE DE RECEBIMENTO";
		getNegociacaoRecebimentoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
		setListaComprovanteRecebimentoRelVOs(getFacadeFactory().getComprovanteRecebimentoRelFacade().montarObjetoComprovanteReciboCartaoCredito(getNegociacaoRecebimentoVO(), getUsuarioLogado()));
		design = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ComprovanteRecebimentoCartaoCreditoRel.jrxml";
		if (!getNegociacaoRecebimentoVO().getCodigo().equals(0) && !getListaComprovanteRecebimentoRelVOs().isEmpty()) {
			getSuperParametroRelVO().limparParametros();
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getNome());
			if (!getNegociacaoRecebimentoVO().getMatricula().equals("")) {
				getSuperParametroRelVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(getNegociacaoRecebimentoVO().getMatricula().toString(), getUsuarioLogado()).getIdentificadorTurma());
				getSuperParametroRelVO().setTurno(getFacadeFactory().getTurnoFacade().consultaRapidaPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), getUsuarioLogado()).getNome());
				getSuperParametroRelVO().setMatricula(getNegociacaoRecebimentoVO().getMatricula());
			}
			getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
			getSuperParametroRelVO().setListaObjetos(getListaComprovanteRecebimentoRelVOs());
			getSuperParametroRelVO().adicionarParametro("razaoSocialUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getRazaoSocial());
			getSuperParametroRelVO().adicionarParametro("enderecoUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getEnderecoCompleto());
			getSuperParametroRelVO().adicionarParametro("cnpjUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getCNPJ());
			getSuperParametroRelVO().adicionarParametro("telefonesUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getTelefones());
			getFacadeFactory().getComprovanteRecebimentoRelFacade().validaLogoComprovanteRecibo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCaminhoBaseLogo(), getNegociacaoRecebimentoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio(), getSuperParametroRelVO(), getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo());
			realizarImpressaoRelatorio();
		}	
	}
	
	public void imprimirPDFDCC() {
		String titulo = null;
		String design = null;

		try {
			titulo = "COMPROVANTE DE PREVISÃO DE RECEBIMENTO";
			design = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ComprovanteRecebimentoRelDCC.jrxml";			
			getNegociacaoRecebimentoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			setListaComprovanteRecebimentoRelVOs(getFacadeFactory().getComprovanteRecebimentoRelFacade().criarObjetoDCC(getNegociacaoRecebimentoVO(), getUsuarioLogado()));
			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0) && !getListaComprovanteRecebimentoRelVOs().isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovanteRecebimentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getNome());
				if (!getNegociacaoRecebimentoVO().getMatricula().equals("")) {
					getSuperParametroRelVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(negociacaoRecebimentoVO.getMatricula().toString(), getUsuarioLogado()).getIdentificadorTurma());
					getSuperParametroRelVO().setTurno(getFacadeFactory().getTurnoFacade().consultaRapidaPorMatricula(negociacaoRecebimentoVO.getMatricula(), getUsuarioLogado()).getNome());
					getSuperParametroRelVO().setMatricula(getNegociacaoRecebimentoVO().getMatricula());
				}
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setListaObjetos(getListaComprovanteRecebimentoRelVOs());
				getSuperParametroRelVO().adicionarParametro("razaoSocialUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getRazaoSocial());
				getSuperParametroRelVO().adicionarParametro("enderecoUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getEnderecoCompleto());
				getSuperParametroRelVO().adicionarParametro("cnpjUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getCNPJ());
				getSuperParametroRelVO().adicionarParametro("telefonesUnidade", getNegociacaoRecebimentoVO().getUnidadeEnsino().getTelefones());

				getFacadeFactory().getComprovanteRecebimentoRelFacade().validaLogoComprovanteRecibo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCaminhoBaseLogo(), getNegociacaoRecebimentoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio(), getSuperParametroRelVO(), getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo());

				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaComprovanteRecebimentoRelVOs());
		}
	}
}
