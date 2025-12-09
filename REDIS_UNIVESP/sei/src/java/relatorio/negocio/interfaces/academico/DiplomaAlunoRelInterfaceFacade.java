package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.DiplomaAlunoRelVO;

public interface DiplomaAlunoRelInterfaceFacade {

//	public void popularObjetos(String matricula, Integer codFuncPrincipal, Integer codFuncSecundario, Integer codCargoFuncPrincipal, Integer codCargoFuncSecundario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

//	public void montaNivelEducacional();

	public void validarDados(DiplomaAlunoRelVO obj) throws ConsistirException;

	public String getDesignIReportRelatorio(String tipoLayout, String tipoDiploma);

	public String getCaminhoBaseRelatorio();

	public List<DiplomaAlunoRelVO> criarObjeto(Boolean utilizarUnidadeMatriz, ExpedicaoDiplomaVO expDiplomaVO, FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO, FuncionarioVO funcionarioTerceiroVO, CargoVO cargoFuncPrincipalVO, CargoVO cargoFuncSecundarioVO, CargoVO cargoFuncTerceiroVO, String tituloFuncionarioPrincipal, String tituloFuncionarioSecundario, UsuarioVO usuario, String tipoLayout, CargoVO cargoFuncQuartoVO, CargoVO cargoFuncQuintoVO, FuncionarioVO funcionarioQuartoVO, FuncionarioVO funcionarioQuintoVO, Boolean xmlDiploma) throws Exception;

}