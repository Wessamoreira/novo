package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoCancelamentoMatriculaVO;
import relatorio.negocio.interfaces.academico.DeclaracaoCancelamentoMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoCancelamentoMatriculaRel extends SuperRelatorio implements DeclaracaoCancelamentoMatriculaRelInterfaceFacade {

	public DeclaracaoCancelamentoMatriculaRel() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoCancelamentoMatriculaRelInterfaceFacade#consultarPorCodigoAluno(negocio
	 * .comuns.academico.MatriculaVO, java.lang.Integer)
	 */
	public DeclaracaoCancelamentoMatriculaVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		PessoaVO obj = new PessoaVO();
		obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
		List lista = new ArrayList(0);
		lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		matPeriodo = (MatriculaPeriodoVO) lista.get(0);
		return montarDados(obj, matPeriodo, matricula);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoCancelamentoMatriculaRelInterfaceFacade#montarDados(negocio.comuns
	 * .basico.PessoaVO, negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
	 */
	public DeclaracaoCancelamentoMatriculaVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception {
		DeclaracaoCancelamentoMatriculaVO obj = new DeclaracaoCancelamentoMatriculaVO();
		obj.setMatricula(matPeriodo.getMatricula());
		obj.setNome(pessoa.getNome());
		obj.setPeriodoLetivo(matPeriodo.getPeridoLetivo().getDescricao());
		obj.setCurso(matricula.getCurso().getNome());
		obj.setUnidadeEnsino(matricula.getUnidadeEnsino().getNome());
		obj.setCpf(pessoa.getCPF());
		obj.setRg(pessoa.getRG());
		obj.setDataExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		obj.setDataSituacao(Uteis.getDataAno4Digitos(new Date()));
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("DeclaracaoCancelamentoMatriculaRel");
	}
}
