package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface ConfiguracaoLdapInterfaceFacade {

    ConfiguracaoLdapVO novo() throws Exception;

    void incluir(ConfiguracaoLdapVO obj) throws Exception;

    void alterar(ConfiguracaoLdapVO obj) throws Exception;

    void excluir(ConfiguracaoLdapVO obj) throws Exception;

    List consultarPorConfiguracaoGeralSistema(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void excluirConfiguracaoLdap(Integer configuracaoGeralSistema) throws Exception;

    void alterarConfiguracaoLdaps(Integer configuracaoGeralSistema, List objetos) throws Exception;

    void incluirConfiguracaoLdaps(Integer configuracaoGeralSistemaPrm, List objetos) throws Exception;

    ConfiguracaoLdapVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void setIdEntidade(String idEntidade);

    ConfiguracaoLdapVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    ConfiguracaoLdapVO consultarPorCodigoDepartamento(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<ConfiguracaoLdapVO> consultarConfiguracaoLdaps() throws Exception;

    void validarDados(ConfiguracaoLdapVO obj) throws ConsistirException;

	ConfiguracaoLdapVO consultarConfiguracaoLdapPorPessoa(Integer pessoa) throws Exception;
	
	ConfiguracaoLdapVO consultarConfiguracaoLdapPorPessoaEmailInstitucional(Integer pessoaEmailInstitucional) throws Exception;

	ConfiguracaoLdapVO consultarConfiguracaoLdapPorCurso(Integer curso) throws Exception;

}