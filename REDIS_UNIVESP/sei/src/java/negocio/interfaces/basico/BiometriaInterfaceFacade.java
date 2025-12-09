package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.BiometriaVO;
import negocio.comuns.basico.PessoaVO;
import webservice.servicos.PessoaObject;

public interface BiometriaInterfaceFacade {

	public void persistir(BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(final BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public BiometriaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<BiometriaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public BiometriaVO consultarPorCodigoPessoa(Integer pessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	public Integer consultarTotalPorCodigoPessoa(Integer pessoa) throws Exception;

	public List<BiometriaVO> consultarPorNomePessoa(String nomePessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	public Integer consultarTotalPorNomePessoa(String nomePessoa) throws Exception;

	public void realizarPreenchimentoIdDigital(BiometriaVO biometriaVO, Integer idDigital, Integer idDedo) throws Exception;

	public BiometriaVO realizarPreenchimentoBiometria(PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/07/2015
	 * @param matricula
	 * @return
	 */
	PessoaObject consultarPessoaAcessarCatracaPelaMatricula(String matricula);

	/**
	 * @author Rodrigo Wind - 28/07/2015
	 * @param codigo
	 * @return
	 */
	PessoaObject consultarPessoaAcessarCatracaPeloCodigo(Integer codigo);

	/**
	 * @author Rodrigo Wind - 27/08/2015
	 */
	void gravarStatusBiometria();

	/**
	 * @author Rodrigo Wind - 27/08/2015
	 * @return
	 */
	Integer consultarTotalPessoaSincronizarCatraca();

	/**
	 * @author Rodrigo Wind - 27/08/2015
	 * @param pessoa
	 */
	void gravarSincronismoRegistradoPorPessoa(String pessoas);

	/**
	 * @author Rodrigo Wind - 27/08/2015
	 * @return
	 */
	List<PessoaObject> consultarPessoaSincronizarCatraca(Integer quantidade);

}
