package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaCobrancaVO;

public interface CartaCobrancaInterfaceFacade {

	public void incluir(CartaCobrancaVO obj) throws Exception;

	public List<CartaCobrancaVO> consultarPorDataGeracao(Date dataGeracaoInicio, Date dataGeracaoFim, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorAluno(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorMatricula(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public List<CartaCobrancaVO> consultarPorUsuario(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

}
