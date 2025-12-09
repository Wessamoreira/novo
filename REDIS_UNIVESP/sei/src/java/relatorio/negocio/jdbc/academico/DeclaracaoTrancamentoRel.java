package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.interfaces.academico.DeclaracaoTrancamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoTrancamentoRel extends SuperRelatorio implements DeclaracaoTrancamentoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public DeclaracaoTrancamentoRel() throws Exception {
	}

	public List<MatriculaPeriodoVO> montarListaObjetos(TrancamentoVO trancamentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoVO> listaObjetos = new ArrayList<MatriculaPeriodoVO>(0);
		List<MatriculaPeriodoVO> listaAuxiliar = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(trancamentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		MatriculaPeriodoVO matriculaPeriodoVO = listaAuxiliar.get(0);
		matriculaPeriodoVO.setMatriculaVO(trancamentoVO.getMatricula());
		matriculaPeriodoVO.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(trancamentoVO.getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuarioVO));
		matriculaPeriodoVO.setJustificativaTrancamento(trancamentoVO.getJustificativa());
		if (matriculaPeriodoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("BA") || matriculaPeriodoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("ME") || matriculaPeriodoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("EX") || matriculaPeriodoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("PR")) {
			matriculaPeriodoVO.setTitulacaoInstituicao("Instituto de Ensino");
		} else {
			matriculaPeriodoVO.setTitulacaoInstituicao("Instituto de Ensino Superior");
		}
		matriculaPeriodoVO.setDataExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCidade().getNome(), trancamentoVO.getData(), false) + ".");
		listaObjetos.add(matriculaPeriodoVO);
		return listaObjetos;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoTrancamento");
	}

}
