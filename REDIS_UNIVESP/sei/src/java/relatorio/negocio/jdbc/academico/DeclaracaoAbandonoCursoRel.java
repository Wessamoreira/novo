package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoAbandonoCursoVO;
import relatorio.negocio.interfaces.academico.DeclaracaoAbandonoCursoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoAbandonoCursoRel extends SuperRelatorio implements DeclaracaoAbandonoCursoRelInterfaceFacade {

	/**
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoAbandonoCursoRelInterfaceFacade#criarObjeto(negocio.comuns.academico.MatriculaVO)
	 */
	public List<DeclaracaoAbandonoCursoVO> criarObjeto(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<DeclaracaoAbandonoCursoVO> declaracaoAbandonoCursoVOs = new ArrayList<DeclaracaoAbandonoCursoVO>(0);
        DeclaracaoAbandonoCursoVO declaracaoAbandonoCursoVO = new DeclaracaoAbandonoCursoVO();
		MatriculaPeriodoVO matriculaPeriodoVO = consultarUltimaMatriculaPeriodo(matriculaVO, configuracaoFinanceiroVO, usuarioVO);
		declaracaoAbandonoCursoVO.setMatricula(matriculaVO.getMatricula());
		declaracaoAbandonoCursoVO.setNome(matriculaVO.getAluno().getNome());
		declaracaoAbandonoCursoVO.setPeriodoLetivo(matriculaPeriodoVO.getPeridoLetivo().getDescricao());
		declaracaoAbandonoCursoVO.setCurso(matriculaVO.getCurso().getNome());
		declaracaoAbandonoCursoVO.setUnidadeEnsino(matriculaVO.getUnidadeEnsino().getNome());
		declaracaoAbandonoCursoVO.setCpf(matriculaVO.getAluno().getCPF());
		declaracaoAbandonoCursoVO.setRg(matriculaVO.getAluno().getRG());
		declaracaoAbandonoCursoVO.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		declaracaoAbandonoCursoVOs.add(declaracaoAbandonoCursoVO);
		return declaracaoAbandonoCursoVOs;
	}

	private MatriculaPeriodoVO consultarUltimaMatriculaPeriodo(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		return getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoAbandonoCursoRel");
	}
	
	public static void validarDados(MatriculaVO matricula) throws Exception {
		if (matricula.getMatricula().equals("") || matricula.getAluno().getNome().equals("")) {
			throw new Exception("O Aluno deve ser informado para criação do relatório.");
		}
	}
	
	public static void validarDados(MatriculaVO matricula, Integer textoPadraoDeclaracao) throws Exception {
		if (matricula.getMatricula().equals("") || matricula.getAluno().getNome().equals("")) {
			throw new Exception("O Aluno deve ser informado para criação do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(textoPadraoDeclaracao)) {
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}

	@Override
	public String imprimirDeclaracaoAbandonoCurso(MatriculaVO matriculaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
		if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoStr);
		} else {
			caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
		}
		return caminhoRelatorio;
	}
	
}
