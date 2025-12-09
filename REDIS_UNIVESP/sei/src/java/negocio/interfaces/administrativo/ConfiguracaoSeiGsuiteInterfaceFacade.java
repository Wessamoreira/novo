package negocio.interfaces.administrativo;

import java.util.List;

import javax.faces.model.SelectItem;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import webservice.boletoonline.itau.comuns.TokenVO;

public interface ConfiguracaoSeiGsuiteInterfaceFacade {

	void consultar(DataModelo dataModelo, ConfiguracaoSeiGsuiteVO obj);

	void persitir(ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso , ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	ConfiguracaoSeiGsuiteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	ConfiguracaoSeiGsuiteVO consultarConfiguracaoSeiGsuitePadrao(int nivelMontarDados, UsuarioVO usuario);

	void validarDados(ConfiguracaoSeiGsuiteVO obj);

	void validarDadosSeiGSuite(ConfiguracaoSeiGsuiteVO obj);

	void validarDadosApiGoogle(ConfiguracaoSeiGsuiteVO obj);

	HttpResponse<String> consultarStatusSeiGsuite(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	HttpResponse<String> realizarRequestUrlCodeGoogle(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	HttpResponse<String> realizarProcessoUrlCodeGoogle(String code, UsuarioVO usuario);
	
	HttpResponse<String> realizarExclusaoCredencialGoogle(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	void adicionarTodasUnidadeEnsinoParaConfiguracaoSeiGsuiteUnidadeEnsino(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, List<SelectItem> listaSelectItemUnidadeEnsino, UsuarioVO usuario);
	
	void adicionarConfiguracaoSeiGsuiteUnidadeEnsinoVO(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, UsuarioVO usuario);

	void removerConfiguracaoSeiGsuiteUnidadeEnsinoVO(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, UsuarioVO usuario);

	void excluir(ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void realizarAlteracaoDominioEmailPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	void realizarAlteracaoUnidadeOrganizacionalAlunoPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	void realizarAlteracaoUnidadeOrganizacionalFuncionarioPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);
	
	void realizarDesvinculacaoPessoaGsuite(PessoaGsuiteVO pessoaGsuite, ConfiguracaoSeiGsuiteVO confSeiGsuiteVO, UsuarioVO usuario) throws Exception;
	
	void realizarVinculoPessoaComPessoaGsuiteImportada(PessoaGsuiteVO pessoaGsuite, ConfiguracaoSeiGsuiteVO confSeiGsuiteVO, UsuarioVO usuario)  throws Exception;
	
	void realizarProcessamentoLoteGoogleMeet(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) throws Exception;

	HttpResponse<JsonNode> realizarGeracaoDaContaGsuitePorPessoa(ConfiguracaoSeiGsuiteVO obj, PessoaVO pessoa, UsuarioVO usuario);

	HttpResponse<JsonNode> realizarExclusaoPorPessoaGsuite(ConfiguracaoSeiGsuiteVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario);

	HttpResponse<JsonNode> realizarAlteracaoPorPessoaGsuite(ConfiguracaoSeiGsuiteVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario);

	HttpResponse<JsonNode> realizarGeracaoUsuarioGsuiteLote(ConfiguracaoSeiGsuiteVO obj, AdminSdkIntegracaoVO adminSdkVO, UsuarioVO usuario);
	
	HttpResponse<JsonNode> realizarImportacaoUsuarioGsuiteLote(ConfiguracaoSeiGsuiteVO obj, AdminSdkIntegracaoVO adminSdkVO, UsuarioVO usuario);
	
	public HttpResponse<JsonNode> realizarProcessamentoGoogleMeetLote(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);
	
	ConfiguracaoSeiGsuiteVO consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	TokenVO consultarTokenVO(ConfiguracaoSeiGsuiteVO obj) throws Exception;
	
	void realizarGeracaoDaContaGsuitePorMatricula(MatriculaVO matricula, UsuarioVO usuario);

	void realizarExclusaoDaContaGsuitePorMatricula(MatriculaVO matricula, UsuarioVO usuario);

	void realizarAlteracaoDominioEmailFuncionarioPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario);

	boolean consultarSeExisteConfiguracaoSeiGsuitePadrao(UsuarioVO usuario);	

	

	

			

	

}
