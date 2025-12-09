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

import relatorio.negocio.comuns.academico.DeclaracaoSetranspVO;
import relatorio.negocio.interfaces.academico.DeclaracaoSetranspRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoSetranspRel extends SuperRelatorio implements DeclaracaoSetranspRelInterfaceFacade {

	/**
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoSetranspRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns.
	 * academico.MatriculaVO, java.lang.Integer)
	 */
	public DeclaracaoSetranspVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		PessoaVO obj = new PessoaVO();
		obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
		List lista = new ArrayList(0);
		lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		matPeriodo = (MatriculaPeriodoVO) lista.get(0);
		return montarDados(obj, matPeriodo, matricula);
	}

	/**
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoSetranspRelInterfaceFacade#montarDados(negocio.comuns.basico.PessoaVO,
	 * negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
	 */
	public DeclaracaoSetranspVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception {
		DeclaracaoSetranspVO obj = new DeclaracaoSetranspVO();
		obj.setMatricula(matPeriodo.getMatricula());
		obj.setNome(pessoa.getNome());
		obj.setPeriodoLetivo(matPeriodo.getPeridoLetivo().getDescricao());
		obj.setCurso(matricula.getCurso().getNome());
		obj.setCpf(pessoa.getCPF());
		obj.setRg(pessoa.getRG());
		obj.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoSetranspRel");
	}
	
	public static void validarDados(DeclaracaoSetranspVO declaracaoSetranspVO) throws Exception {
		if (declaracaoSetranspVO.getNome().equals("")
				|| declaracaoSetranspVO.getMatricula().equals("")) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
	}
}
