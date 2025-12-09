package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.AlteracaoPlanoFinanceiroAlunoInterfaceFacade;

/**
 * @author Wellington - 4 de jan de 2016
 *
 */
@Repository
public class AlteracaoPlanoFinanceiroAluno extends ControleAcesso implements AlteracaoPlanoFinanceiroAlunoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	@Override
	public List<MatriculaPeriodoVO> consultar(String campoConsulta, String valorConsulta, String ano, String semestre, String situacaoMatricula, UnidadeEnsinoVO unidadeEnsinoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		AlteracaoPlanoFinanceiroAluno.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		validarDadosConsulta(campoConsulta, valorConsulta);
		if (campoConsulta.equals("matricula")) {
			return getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(valorConsulta, unidadeEnsinoVO.getCodigo(), situacaoMatricula, ano, semestre, verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("aluno")) {
			return getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorNomePessoaAnoSemestre(valorConsulta, unidadeEnsinoVO.getCodigo(), ano, semestre, verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("curso")) {
			return getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorUnidadeEnsinoCursoAnoSemestre(Integer.valueOf(valorConsulta), situacaoMatricula, ano, semestre, verificarAcesso, usuarioVO);
		}
		return getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorIdentificadorTurmaSituacaoMatriculaPeriodoAnoSemestre(valorConsulta, unidadeEnsinoVO.getCodigo(), situacaoMatricula, ano, semestre, verificarAcesso, usuarioVO);
	}

	private void validarDadosConsulta(String campoConsulta, String valorConsulta) throws Exception {
		if (campoConsulta.equals("matricula") || campoConsulta.equals("aluno")) {
			if (valorConsulta.trim().length() < 2) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
			}
		} else if (campoConsulta.equals("curso") && !Uteis.isAtributoPreenchido(valorConsulta)) {
			throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsinoCurso"));
		} else {
			if (valorConsulta.trim().length() < 2) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_turmaVazio"));
			}
		}
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = AlteracaoPlanoFinanceiroAluno.class.getSimpleName();
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AlteracaoPlanoFinanceiroAluno.idEntidade = idEntidade;
	}
}
