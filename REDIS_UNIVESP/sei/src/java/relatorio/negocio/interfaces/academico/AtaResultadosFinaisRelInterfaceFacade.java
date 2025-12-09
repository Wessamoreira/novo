package relatorio.negocio.interfaces.academico;

import java.util.Date;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.AtaResultadosFinaisRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AtaResultadosFinaisRelInterfaceFacade {

	public void validarDados(Integer turma, boolean apresentarCampoAno, String ano, boolean apresentarCampoSemestre, String semestre) throws Exception;

	public AtaResultadosFinaisRelVO criarObjeto(TurmaVO turmaVO, String ano, String semestre, String layout, Boolean apresentarDisciplinaComposta, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno, Boolean apresentarDataTransferencia, FuncionarioVO funcionarioPrincipal, FuncionarioVO funcionarioSecundario, Date dataApuracao) throws Exception;

	public void validarFuncionarios(String layout, FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO) throws Exception;

}
