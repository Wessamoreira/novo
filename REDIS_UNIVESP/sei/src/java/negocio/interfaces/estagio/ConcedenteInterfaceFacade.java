package negocio.interfaces.estagio;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConcedenteVO;

public interface ConcedenteInterfaceFacade {

	void persistir(ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, ConcedenteVO obj) throws Exception;

	ConcedenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	ConcedenteVO consultarPorCnpj(String cnpj, Integer TipoConcedente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	public void atualizarEmailConcedente(ConcedenteVO obj) throws Exception;

	 List<ConcedenteVO> consultarPorNome(String nomeConcedente, Integer TipoConcedente, Integer limite, Integer pagina, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

}
