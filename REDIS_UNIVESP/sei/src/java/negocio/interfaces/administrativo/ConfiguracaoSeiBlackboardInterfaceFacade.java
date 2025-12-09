package negocio.interfaces.administrativo;

import java.util.List;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardDominioVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.FonteDeDadosBlackboardVO;
import negocio.comuns.blackboard.PermissaoBlackboardVO;
import webservice.boletoonline.itau.comuns.TokenVO;

public interface ConfiguracaoSeiBlackboardInterfaceFacade {

	void validarDados(ConfiguracaoSeiBlackboardVO obj);

	void excluir(ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void persitir(ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void consultar(DataModelo dataModelo, ConfiguracaoSeiBlackboardVO obj);

	boolean consultarSeExisteConfiguracaoSeiBlackboardPadrao(UsuarioVO usuario);

	ConfiguracaoSeiBlackboardVO consultarConfiguracaoSeiBlackboardPadrao(int nivelMontarDados, UsuarioVO usuario);

	ConfiguracaoSeiBlackboardVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	TokenVO consultarTokenVO(ConfiguracaoSeiBlackboardVO obj);

	HttpResponse<String> consultarStatusSeiBlackboard(ConfiguracaoSeiBlackboardVO obj, UsuarioVO usuario);
	
	HttpResponse<String> consultarStatusBlackboard(ConfiguracaoSeiBlackboardVO obj, UsuarioVO usuario);
	
	List<FonteDeDadosBlackboardVO> realizarConsultaFonteDeDadosBlackboardVO(UsuarioVO usuario);
	
	List<FonteDeDadosBlackboardVO> realizarConsultaFonteDeDadosBlackboardPorDescricao(ConfiguracaoSeiBlackboardVO obj, String descricao, UsuarioVO usuario);
	
	List<PermissaoBlackboardVO> realizarConsultaPermissaoBlackboard(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csgue, UsuarioVO usuario);

	HttpResponse<JsonNode> realizarGeracaoDaContaBlackboardPorPessoa(ConfiguracaoSeiBlackboardVO obj, PessoaVO pessoa, UsuarioVO usuario);	

	void adicionarConfiguracaoSeiBlackboardDominioVO(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csgue, UsuarioVO usuario);

	void removerConfiguracaoSeiBlackboardDominioVO(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csgue, UsuarioVO usuario);

	void realizarSalaAulaOperacaoPorMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario);

	void persistirImportacaoEmRealizacao(Integer conf, Boolean emRealizacao);

}
