package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoDocumentacaoProgramacaoFormaturaAluno;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.ComunicadoDebitoDocumentosAlunoVO;
import relatorio.negocio.interfaces.academico.ComunicadoDebitoDocumentosAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ComunicadoDebitoDocumentosAlunoRel extends SuperRelatorio implements ComunicadoDebitoDocumentosAlunoRelInterfaceFacade {

	public static void validarDados(MatriculaVO obj) throws ConsistirException {
		if (obj.getMatricula().equals("")) {
			throw new ConsistirException("A MATRÍCULA deve ser informada para a geração do relatório.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.ComunicadoDebitoDocumentosAlunoRelInterfaceFacade#criarObjeto()
	 */
	public List<ComunicadoDebitoDocumentosAlunoVO> criarObjeto(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<ComunicadoDebitoDocumentosAlunoVO> listaResultado = new ArrayList<ComunicadoDebitoDocumentosAlunoVO>(0);
        ComunicadoDebitoDocumentosAlunoVO comunicadoDebitoDocumentosAlunoVO = new ComunicadoDebitoDocumentosAlunoVO();
		matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		comunicadoDebitoDocumentosAlunoVO.setNome(matriculaVO.getAluno().getNome());
		comunicadoDebitoDocumentosAlunoVO.setTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		comunicadoDebitoDocumentosAlunoVO.setMatricula(matriculaVO.getMatricula());
		comunicadoDebitoDocumentosAlunoVO.setUnidadeEnsino(matriculaVO.getUnidadeEnsino().getNome());
		comunicadoDebitoDocumentosAlunoVO.setDataHoje(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		comunicadoDebitoDocumentosAlunoVO.setDocumentacaoMatriculaVOs(consultarDocumentosPendentes(matriculaVO));
		listaResultado.add(comunicadoDebitoDocumentosAlunoVO);
		return listaResultado;
	}

	private List<TipoDocumentoVO> consultarDocumentosPendentes(MatriculaVO matriculaVO) throws Exception {
		List<TipoDocumentoVO> listaResultado = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorSituacaoMatricula(matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS);
		return listaResultado;
	}

//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see relatorio.negocio.jdbc.academico.ComunicadoDebitoDocumentosAlunoRelInterfaceFacade#getApresentarCampos()
//	 */
//	public Boolean getApresentarCampos() {
//		if ((getMatriculaVO().getAluno() != null) && (getMatriculaVO().getAluno().getCodigo() != 0)) {
//			return true;
//		}
//		return false;
//	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ComunicadoDebitoDocumentosAlunoRel");
	}

}
