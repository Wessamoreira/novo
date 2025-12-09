package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import controle.secretaria.MapaConvocacaoEnadeControle;

@Controller("MapaConvocacaoEnadeRelControle")
@Scope("request")
@Lazy
public class MapaConvocacaoEnadeRelControle extends SuperControleRelatorio {
	
	private static final long serialVersionUID = 1L;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO;

	public void inicializaMapaConvocacaoEnadeVO(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO, String tipoRelatorio, Boolean ingressante, Boolean concluinte, Boolean dispensado, String ordenacao) {
		setMapaConvocacaoEnadeVO(mapaConvocacaoEnadeVO);
		if (tipoRelatorio.equals("sem-assinatura")) {
			imprimirPDFSemAssinatura(ingressante, concluinte, dispensado, ordenacao);
		} else if(tipoRelatorio.equals("com-assinatura")) {
			imprimirPDFComAssinatura(ingressante, concluinte, dispensado, ordenacao);
		}else {
			imprimirExcelListagemDadosCompletosAluno(ingressante, concluinte, dispensado, ordenacao);
		}

	}

	public void imprimirPDFSemAssinatura(Boolean ingressante, Boolean concluinte, Boolean dispensado, String ordenacao) {
		List<MapaConvocacaoEnadeMatriculaVO> listaMapaConvocacaoEnadeMatriculaVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>();
		try {
			if (ingressante) {
				List<MapaConvocacaoEnadeMatriculaVO> listaIngressante = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaIngressante);
			}
			if (concluinte) {
				List<MapaConvocacaoEnadeMatriculaVO> listaConcluinte = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaConcluinte);
			}
			if (dispensado) {
				List<MapaConvocacaoEnadeMatriculaVO> listaDispensado = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaDispensado);
			}
			if (ordenacao.equals("nome")) {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoNome");
			} else {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoMatricula");
			}
			imprimirPDF(listaMapaConvocacaoEnadeMatriculaVOs, getDesignSintetico(), "Relatório Mapa Convocação Enade", ingressante, concluinte, dispensado);
		} catch (Exception e) {
			executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelErro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaMapaConvocacaoEnadeMatriculaVOs);
		}

	}

	public void imprimirPDFComAssinatura(Boolean ingressante, Boolean concluinte, Boolean dispensado, String ordenacao) {
		List<MapaConvocacaoEnadeMatriculaVO> listaMapaConvocacaoEnadeMatriculaVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>();
		try {
			if (ingressante) {
				List<MapaConvocacaoEnadeMatriculaVO> listaIngressante = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaIngressante);
			}
			if (concluinte) {
				List<MapaConvocacaoEnadeMatriculaVO> listaConcluinte = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaConcluinte);
			}
			if (dispensado) {
				List<MapaConvocacaoEnadeMatriculaVO> listaDispensado = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaDispensado);
			}
			if (ordenacao.equals("nome")) {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoNome");
			} else {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoMatricula");
			}
			for (MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO : listaMapaConvocacaoEnadeMatriculaVOs) {
				System.out.println(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getMatricula());
			}
			imprimirPDF(listaMapaConvocacaoEnadeMatriculaVOs, getDesignAnalitico(), "Relatório Mapa Convocação Enade", ingressante, concluinte, dispensado);
		} catch (Exception e) {
			executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelErro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaMapaConvocacaoEnadeMatriculaVOs);
		}

	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getDesignAnalitico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MapaConvocacaoEnadeComAssinatura.jrxml");
	}

	public String getDesignSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MapaConvocacaoEnadeSemAssinatura.jrxml");
	}
	
	public String getDesignListagemDadosCompletosAlunos() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MapaConvocacaoEnadeListagemDadosCompletosAlunoExcel.jrxml");
	}

	public void imprimirPDF(List<MapaConvocacaoEnadeMatriculaVO> lista, String design, String tituloRelatorio, Boolean ingressante, Boolean concluinte, Boolean dispensado) {
		try {
			if (!lista.isEmpty()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaConvocacaoEnadeRelControle", "Inicializando Geração de Relatório Mapa Convocação Enade", "Emitindo Relatório");
				getSuperParametroRelVO().setTituloRelatorio(tituloRelatorio);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setQuantidade(lista.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getMapaConvocacaoEnadeVO().getUnidadeEnsino());
				String situacoes = "";
				if (ingressante && concluinte && dispensado) {
					situacoes = "Todas";
				} else if (ingressante && concluinte) {
					situacoes = "Ingressante e Concluinte";
				} else if (ingressante && dispensado) {
					situacoes = "Ingressante e Dispensado";
				} else if (concluinte && dispensado) {
					situacoes = "Concluinte e dispensado";
				} else if (ingressante) {
					situacoes = "Ingressante";
				} else if (concluinte) {
					situacoes = "Concluinte";
				} else if (dispensado) {
					situacoes = "Dispensado";
				}
				getSuperParametroRelVO().adicionarParametro("situacoes", situacoes);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaConvocacaoEnadeRelControle", "Finalizando Geração de Relatório Mapa Convocação Enade", "Finalizando Relatório");
				getSuperParametroRelVO().adicionarParametro("curso", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getNome());
//				getSuperParametroRelVO().adicionarParametro("turno", getMapaConvocacaoEnadeVO().getUnidadeEnsinoCursoVO().getTurno().getNome());
				getSuperParametroRelVO().adicionarParametro("enade", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getTituloEnade());
				getSuperParametroRelVO().adicionarParametro("portaria", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataPortaria());
				getSuperParametroRelVO().adicionarParametro("publicacao", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataPublicacaoPortariaDOU());
				getSuperParametroRelVO().adicionarParametro("prova", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataProva());
				realizarImpressaoRelatorio();
				executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelComDados");
			} else {
				executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelSemDados");
			}

		} catch (Exception e) {
			executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelErro", e.getMessage());
		} finally {
			design = null;
			Uteis.liberarListaMemoria(lista);
		}
	}

	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVO() {
		if (mapaConvocacaoEnadeVO == null) {
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVO;
	}

	public void setMapaConvocacaoEnadeVO(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO) {
		this.mapaConvocacaoEnadeVO = mapaConvocacaoEnadeVO;
	}
	
	public void imprimirExcelListagemDadosCompletosAluno(Boolean ingressante, Boolean concluinte, Boolean dispensado, String ordenacao) {
		List<MapaConvocacaoEnadeMatriculaVO> listaMapaConvocacaoEnadeMatriculaVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>();
		try {
			if (ingressante) {
				List<MapaConvocacaoEnadeMatriculaVO> listaIngressante = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaIngressante);
			}
			if (concluinte) {
				List<MapaConvocacaoEnadeMatriculaVO> listaConcluinte = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaConcluinte);
			}
			if (dispensado) {
				List<MapaConvocacaoEnadeMatriculaVO> listaDispensado = getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs();
				listaMapaConvocacaoEnadeMatriculaVOs.addAll(listaDispensado);
			}
			if (ordenacao.equals("nome")) {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoNome");
			} else {
				Ordenacao.ordenarLista(listaMapaConvocacaoEnadeMatriculaVOs, "ordenacaoMatricula");
			}
			imprimirExcel(listaMapaConvocacaoEnadeMatriculaVOs, getDesignListagemDadosCompletosAlunos(), "Relatório Mapa Convocação Enade", ingressante, concluinte, dispensado);
		} catch (Exception e) {
			executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelErro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaMapaConvocacaoEnadeMatriculaVOs);
		}

	}
	
	public void imprimirExcel(List<MapaConvocacaoEnadeMatriculaVO> lista, String design, String tituloRelatorio, Boolean ingressante, Boolean concluinte, Boolean dispensado) {
		try {
			if (!lista.isEmpty()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaConvocacaoEnadeRelControle", "Inicializando Geração de Relatório Mapa Convocação Enade", "Emitindo Relatório");
				getSuperParametroRelVO().setTituloRelatorio(tituloRelatorio);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setQuantidade(lista.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getMapaConvocacaoEnadeVO().getUnidadeEnsino());
				String situacoes = "";
				if (ingressante && concluinte && dispensado) {
					situacoes = "Todas";
				} else if (ingressante && concluinte) {
					situacoes = "Ingressante e Concluinte";
				} else if (ingressante && dispensado) {
					situacoes = "Ingressante e Dispensado";
				} else if (concluinte && dispensado) {
					situacoes = "Concluinte e dispensado";
				} else if (ingressante) {
					situacoes = "Ingressante";
				} else if (concluinte) {
					situacoes = "Concluinte";
				} else if (dispensado) {
					situacoes = "Dispensado";
				}
				getSuperParametroRelVO().adicionarParametro("situacoes", situacoes);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaConvocacaoEnadeRelControle", "Finalizando Geração de Relatório Mapa Convocação Enade", "Finalizando Relatório");
				getSuperParametroRelVO().adicionarParametro("curso", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getNome());
//				getSuperParametroRelVO().adicionarParametro("turno", getMapaConvocacaoEnadeVO().getUnidadeEnsinoCursoVO().getTurno().getNome());
				getSuperParametroRelVO().adicionarParametro("enade", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getTituloEnade());
				getSuperParametroRelVO().adicionarParametro("portaria", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataPortaria());
				getSuperParametroRelVO().adicionarParametro("publicacao", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataPublicacaoPortariaDOU());
				getSuperParametroRelVO().adicionarParametro("prova", getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataProva());
				realizarImpressaoRelatorio();
				executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelComDados");
			} else {
				executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelSemDados");
			}

		} catch (Exception e) {
			executarMetodoControle(MapaConvocacaoEnadeControle.class.getSimpleName(), "imprimirMensagemControladorMapaConvocacaoEnadeRelErro", e.getMessage());
		} finally {
			design = null;
			Uteis.liberarListaMemoria(lista);
		}
	}

}
