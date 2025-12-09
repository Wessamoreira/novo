package negocio.interfaces.basico;
import java.util.List;

import negocio.comuns.academico.EstagioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.EnderecoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.estagio.ConcedenteVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface EnderecoInterfaceFacade {
	
	public EnderecoVO novo() throws Exception;

	public void incluir(EnderecoVO obj) throws Exception;

	public void alterar(EnderecoVO obj) throws Exception;

	public void excluir(EnderecoVO obj) throws Exception;

	public EnderecoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDescricaoBairro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCep(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorLogradouro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
	
        public void incluirNovoCep(PossuiEndereco vo,UsuarioVO usuario) throws Exception;
 
	public void setIdEntidade(String aIdEntidade);

        public void carregarEndereco(PossuiEndereco vo,UsuarioVO usuario) throws Exception;
        
        public void carregarEnderecoEmpresa(DadosComerciaisVO dc, UsuarioVO usuarioLogado) throws Exception;

		void carregarEnderecoFiador(PessoaVO pessoa, UsuarioVO usuario) throws Exception;
		
		void carregarEnderecoEstagio(ConcedenteVO vo, UsuarioVO usuario) throws Exception;
		
		void carregarEnderecoEstagioBeneficiario(EstagioVO vo, UsuarioVO usuario) throws Exception;

		public void carregarEnderecoRegistradora(UnidadeEnsinoVO vo, Boolean mantenedoraRegistradora, Boolean mantenedora, UsuarioVO usuario) throws Exception;
    
}