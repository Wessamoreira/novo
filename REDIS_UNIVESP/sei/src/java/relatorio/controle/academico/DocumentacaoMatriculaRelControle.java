package relatorio.controle.academico;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.LogImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRel;
import relatorio.negocio.jdbc.academico.DadosMatriculaRel;
import webservice.servicos.MatriculaRSVO;

@SuppressWarnings("unchecked")
@Controller("DocumentacaoMatriculaRelControle")
@Scope("request")
@Lazy
public class DocumentacaoMatriculaRelControle extends SuperControleRelatorio {

    public void imprimirPlanoEstudoPDF(Integer matriculaPeriodo, Integer configuracaoGeralSistema, String nomeUnidadeEnsino) {
        String titulo = null;
        String design = null;
        List listaObjetos = null;
        try {
            titulo = " PLANO DE ESTUDO ";
            design = DadosMatriculaRel.getDesignIReportRelatorio();
            ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
            configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracaoGeralSistema(configuracaoGeralSistema, false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            listaObjetos = getFacadeFactory().getDadosMatriculaRelFacade().criarObjeto(matriculaPeriodo, getConfiguracaoFinanceiroPadraoSistema(), configuracaoGeralSistemaVO, getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DadosMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DadosMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeUnidadeEnsino);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
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

    public void imprimirComprovanteMatriculaPDF(MatriculaVO matriculaVO, String nivelEducacional, Integer matriculaPeriodo, Integer tipoLayout) {
        String titulo = null;
        String design = null;
        String nomeRelatorio = null;
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        List listaObjetos = null;
        try {
            if (tipoLayout == 2) {
                titulo = "FICHA DE INSCRIÇÃO";
                design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().substring(0, ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().lastIndexOf(".")) + tipoLayout + ".jrxml";
                nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade() + tipoLayout;
            } else {
                if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX")) {
                    titulo = "FICHA DE INSCRIÇÃO";
                    design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio();
                    nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade();
                } else {
                    titulo = "COMPROVANTE DE RENOVAÇÃO DE MATRÍCULA";
                    ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, super.getUnidadeEnsinoLogado().getCodigo());
                    if (configuracoesVO == null || configuracoesVO.getLayoutPadraoComprovanteMatricula() == null || configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_01)) {
                        design = ComprovanteRenovacaoMatriculaRel.getDesignComprovanteMatriculaIReportRelatorio();
                        nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidadeComprovanteMatricula();
                    } else if (configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)) {
                        design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ComprovanteRenovacaoMatriculaRelLayout2" + ".jrxml");
                        nomeRelatorio = "ComprovanteRenovacaoMatriculaRelLayout2";
                    }

                }
            }

            listaObjetos = getFacadeFactory().getComprovanteRenovacaoMatriculaRelFacade().criarObjeto(matriculaPeriodo, matriculaVO.getMatricula(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            persistirLayoutPadrao(tipoLayout.toString());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    private void persistirLayoutPadrao(String valor) throws Exception {
        getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "comprovanteMatricula", "tipoDesignRelatorioComprovanteMatricula", null);
    }
    
    public void imprimirComprovanteMatriculaPDFMatriculaExterna(MatriculaVO matriculaVO, String nivelEducacional, Integer matriculaPeriodo, Integer tipoLayout, String unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) {
        String titulo = null;
        String design = null;
        String nomeRelatorio = null;
        String nomeEntidade = unidadeEnsino;
        List listaObjetos = null;
        try {
            if (tipoLayout == 2) {
                titulo = "FICHA DE INSCRIÇÃO";
                design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().substring(0, ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().lastIndexOf(".")) + tipoLayout + ".jrxml";
                nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade() + tipoLayout;
            } else {
                if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX")) {
                    titulo = "FICHA DE INSCRIÇÃO";
                    design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio();
                    nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade();
                } else {
                    titulo = "COMPROVANTE DE RENOVAÇÃO DE MATRÍCULA";
                    ConfiguracoesVO configuracoesVO = configuracaoGeralSistemaVO.getConfiguracoesVO();
                    if (configuracoesVO == null || configuracoesVO.getLayoutPadraoComprovanteMatricula() == null || configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_01)) {
                        design = ComprovanteRenovacaoMatriculaRel.getDesignComprovanteMatriculaIReportRelatorio();
                        nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidadeComprovanteMatricula();
                    } else if (configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)) {
                        design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ComprovanteRenovacaoMatriculaRelLayout2" + ".jrxml");
                        nomeRelatorio = "ComprovanteRenovacaoMatriculaRelLayout2";
                    }

                }
            }

            listaObjetos = getFacadeFactory().getComprovanteRenovacaoMatriculaRelFacade().criarObjeto(matriculaPeriodo, matriculaVO.getMatricula(), configuracaoFinanceiroVO, usuarioVO);
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(usuarioVO.getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(usuarioVO);
                if (matriculaVO.getUnidadeEnsino().getNomeArquivoLogoRelatorio().equals("")) {
	                UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
	    			if (ue.getExisteLogoRelatorio()) {
	    				String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
	    				String urlLogo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
	    				getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
	    			}
                }
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            persistirLayoutPadrao(tipoLayout.toString());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
        
        
        
    }
    
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public String gerarContratoPDFMatriculaExterna(MatriculaVO matriculaVO,MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, MatriculaRSVO matriculaRSVO)  throws Exception {
    	ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
    	impressaoContratoVO.setMatriculaVO(matriculaVO);
    	impressaoContratoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		impressaoContratoVO.setImpressaoContratoMatriculaExterna(true);
       
		try {
			return getFacadeFactory().getImpressaoContratoFacade().imprimirContrato("MA", impressaoContratoVO, getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()), usuarioVO);
		} catch (Exception e) {
			 String mensagemErro = e.getMessage();
			if(mensagemErro != null && e.getMessage().contains("SocketTimeoutException")) {
				mensagemErro ="O tempo limite de geração de contrtato esgotou.";
			}else {
				mensagemErro ="detalhes do erro não encontrado.";
			}
			matriculaRSVO.setAssinarDigitalmenteContrato(false);
			matriculaRSVO.setMensagemErroContrato("Não foi possivel gerar o contrato de Matrícula para o aluno. Procure a instituição pra realizar a geração do contrato. (" + mensagemErro + ")");
			throw new Exception(matriculaRSVO.getMensagemErroContrato()); 
		}
    }
        
    
    
}
