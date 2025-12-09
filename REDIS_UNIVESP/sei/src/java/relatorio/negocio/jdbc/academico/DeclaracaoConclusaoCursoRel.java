package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TituloCursoMedio;
import negocio.comuns.utilitarias.dominios.TituloCursoPos;
import negocio.comuns.utilitarias.dominios.TituloCursoSuperior;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoConclusaoCursoVO;
import relatorio.negocio.interfaces.academico.DeclaracaoConclusaoCursoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoConclusaoCursoRel extends SuperRelatorio implements DeclaracaoConclusaoCursoRelInterfaceFacade {

	public static void validarDados(MatriculaVO obj, DeclaracaoConclusaoCursoVO declaracao) throws ConsistirException {
		if (obj.getMatricula().equals("")) {
			throw new ConsistirException("A MATRÍCULA deve ser informada para a geração do relatório.");
		}
		if (declaracao.getTipoLayout().intValue() > 0) {
			if (declaracao.getFuncionarioPrincipalVO().getPessoa().getNome().equals("")) {
				throw new ConsistirException("O RESPONSÁVEL deve ser informado para criação do relatório.");
			}
		}
		if (!Uteis.isAtributoPreenchido(declaracao.getTextoPadraoDeclaracao())) {
			throw new ConsistirException("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DeclaracaoConclusaoCursoRelInterfaceFacade#criarObjeto()
	 */
	public List<DeclaracaoConclusaoCursoVO> criarObjeto(DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<DeclaracaoConclusaoCursoVO> listaResultado = new ArrayList<DeclaracaoConclusaoCursoVO>(0);
		ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarColacaoPorMatricula(matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaConclusaoCurso(
				matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		declaracaoConclusaoCursoVO.setSemestre(matriculaPeriodoVO.getSemestre());
		declaracaoConclusaoCursoVO.setAno(matriculaPeriodoVO.getAno());
		declaracaoConclusaoCursoVO.setDataColacao(Uteis.getDataCidadeDiaMesPorExtensoEAno("", programacaoFormaturaAlunoVO.getColacaoGrau().getData(), false));
		declaracaoConclusaoCursoVO.setNome(matriculaVO.getAluno().getNome());
		declaracaoConclusaoCursoVO.setCpf(matriculaVO.getAluno().getCPF());
		declaracaoConclusaoCursoVO.setRg(matriculaVO.getAluno().getRG());
		declaracaoConclusaoCursoVO.setMatricula(matriculaVO.getMatricula());
		declaracaoConclusaoCursoVO.setCurso(matriculaVO.getCurso().getNome());
		declaracaoConclusaoCursoVO.setDataHoje(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), 0, usuarioVO); 
		if (!unidadeEnsinoCursoVO.getMantida().equals("")) {
			declaracaoConclusaoCursoVO.setNomeUnidadeEnsino(unidadeEnsinoCursoVO.getMantida());
		} else {
			declaracaoConclusaoCursoVO.setNomeUnidadeEnsino(matriculaVO.getUnidadeEnsino().getNome());
		}
        declaracaoConclusaoCursoVO.setCidadeUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCidade().getNome());
        declaracaoConclusaoCursoVO.setTituloCurso(getTituloCurso(matriculaVO));
        declaracaoConclusaoCursoVO.setResolucao(matriculaVO.getCurso().getNrRegistroInterno());
        declaracaoConclusaoCursoVO.setCargaHoraria(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularDaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO).getCargaHoraria().toString());
        montarPeriodoRealizacaoCurso(declaracaoConclusaoCursoVO, matriculaVO);
		listaResultado.add(declaracaoConclusaoCursoVO);
		return listaResultado;
	}


        public void montarPeriodoRealizacaoCurso(DeclaracaoConclusaoCursoVO obj, MatriculaVO matriculaVO) throws Exception {
            try {
                StringBuilder inicio = new StringBuilder();
                StringBuilder termino = new StringBuilder();
                String aux = null;

                    aux = Uteis.getDataAno4Digitos(Uteis.getDataJDBC(matriculaVO.getDataInicioCurso()));
                        inicio = new StringBuilder();
                        inicio.append(aux.subSequence(0,2));
                        inicio.append(" de ").append(Uteis.getMesReferenciaExtenso(aux.substring(3,5))).append(" de ").append(aux.subSequence(6, 10));

                    aux = Uteis.getDataAno4Digitos(Uteis.getDataJDBC(matriculaVO.getDataConclusaoCurso()));
                        termino = new StringBuilder();
                        termino.append(aux.subSequence(0,2));
                        termino.append(" de ").append(Uteis.getMesReferenciaExtenso(aux.substring(3,5))).append(" de ").append(aux.subSequence(6, 10));

                obj.setInicio(inicio.toString());
                obj.setTermino(termino.toString());
            } catch (Exception e) {
                throw e;
            }
        }



    public String getTituloCurso(MatriculaVO obj){
            if (obj.getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())) {
                return TipoNivelEducacional.MEDIO.getDescricao()+" "+ TituloCursoMedio.getDescricao(obj.getCurso().getTitulo());
            }
            else if (obj.getCurso().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())) {
                return TipoNivelEducacional.SUPERIOR.getDescricao()+" "+TituloCursoSuperior.getDescricao(obj.getCurso().getTitulo());
            }
            else if (obj.getCurso().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
                return TipoNivelEducacional.POS_GRADUACAO.getDescricao()+" "+TituloCursoPos.getDescricao(obj.getCurso().getTitulo());
            }
            return "";
    }



    public void executarVerificacaoDeConclusaoCurso(MatriculaVO matricula) throws Exception {
        GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularDaUltimaMatriculaPeriodo(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
        if(!getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matricula, gradeCurricularVO.getCodigo(), null, null)){
            throw new ConsistirException("O Aluno não concluiu a Grade Curricular do curso.");
        }
    }

	public static String getDesignIReportRelatorio(int tipoLayout) {
            if (tipoLayout == 0){
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
            }else {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DeclaracaoConclusaoCursoDisciplinaRel" + ".jrxml");
            }
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoConclusaoCursoRel");
	}

	@Override
	public String imprimirDeclaracaoConclusaoCurso(MatriculaVO matriculaVO, DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		try {
			String caminhoRelatorio = "";
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
			textoStr = textoPadraoDeclaracao.substituirTag(textoStr, "[(250){}Texto_Curso]", declaracaoConclusaoCursoVO.getTexto(), "", 250);
			textoStr = textoPadraoDeclaracao.substituirTag(textoStr, "[(250){}InformacoesAdicionais_Curso]", declaracaoConclusaoCursoVO.getInformacoesAdicionais(), "", 250);
			if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
				impressaoContratoVO.setImpressaoPdf(false);
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoStr);
			} else {
				impressaoContratoVO.setImpressaoPdf(true);
				caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
			}
			return caminhoRelatorio;
		} catch (Exception e) {
			throw e;
		}
	}

}
