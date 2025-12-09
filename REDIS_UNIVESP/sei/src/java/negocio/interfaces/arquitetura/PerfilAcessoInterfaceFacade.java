package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OpcaoPermissaoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível
 * substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de
 * sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PerfilAcessoInterfaceFacade {

	public PerfilAcessoVO novo() throws Exception;

	public void incluir(PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception;

	public PerfilAcessoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	public List<PerfilAcessoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PerfilAcessoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<PerfilAcessoVO> consultarPorUsuarioEUnidadeEnsino(Integer codigoUsuario, Integer codigoUnidadeEnsino) throws Exception;

	public PerfilAcessoVO definirPerfilAcessoParaAlunoProfessorECandidato(String visao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public PerfilAcessoVO definirPerfilAcessoParaAdminEFuncionario(Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public PermissaoAcessoMenuVO montarPermissoesMenu(PerfilAcessoVO perfilAcessoVO);
	
	public PerfilAcessoVO consultarPerfilAcessoDiretorMultiCampus(UsuarioVO usuarioVO);
	
	public PerfilAcessoVO consultarPerfilParaFuncionarioAdministrador(Integer codigoUnidadeEnsino, UsuarioVO usuarioVO);

        public Integer consultarQtdePerfilParaMesmaUnidade(Integer usuario, UsuarioVO usuarioLogado);

        public PermissaoAcessoMenuVO montarPermissoesMenuComMaisDeUmPerfil(UsuarioVO usuarioVO);

        public PerfilAcessoVO consultarPorChavePrimariaSemPermissao(Integer codigoPrm, UsuarioVO usuario) throws Exception;		

		PerfilAcessoVO consultarPerfilAcessoPadraoOuvidoria(Integer unidadeEnsino, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

		/** 
		 * @author Otimize - 24 de fev de 2016 
		 * @param permissaoVO
		 * @param permissaoBase 
		 */
		void realizarMarcacaoPermissao(PermissaoVO permissaoVO, OpcaoPermissaoEnum opcao, UsuarioVO usuarioVO);

		/** 
		 * @author Otimize - 24 de fev de 2016 
		 * @param permissaoVO 
		 */
		void realizarDefinicaoInformacaoPermissao(PermissaoVO permissaoVO);
		/** 
		 * @author Otimize - 26 de fev de 2016 
		 * @param permissaoVO 
		 */
		public void realizarMarcacaoFuncionalidades(PermissaoVO permissaoVO, String marcarFuncionalidades, UsuarioVO usuarioVO );

		/** 
		 * @author Otimize - 9 de mar de 2016 
		 * @param perfilAcessoVO
		 * @param modulo
		 * @param subModulo
		 * @param permissaoBase
		 * @param filtroCadastro
		 * @param filtroFuncionalidade
		 * @param usuarioVO 
		 */
		void realizarReplicacaoPermissao(PerfilAcessoVO perfilAcessoVO, PerfilAcessoModuloEnum modulo, PerfilAcessoSubModuloEnum subModulo, PermissaoVO permissaoBase, String filtroCadastro, String filtroFuncionalidade, Cliente cliente, UsuarioVO usuarioVO);
		/** 
		 * @author Otimize - 10 de mar de 2016 
		 * @param permissaoVO 
		 */
		public void realizarMarcacaoFuncionalidadesPorEntidade(PermissaoVO permissaoVO, Boolean marcarFuncionalidadesPorEntidade, UsuarioVO usuarioVO );

		/** 
		 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
		 * @param configuracaoGeralSistemaVO
		 * @param tipoNivelEducacional
		 * @return
		 * @throws Exception 
		 */
		public PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, MatriculaVO matricula, Boolean alunoNaoAssinouContratoMatricula, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
		 * @param configuracaoGeralSistemaVO
		 * @param tipoNivelEducacional
		 * @return
		 * @throws Exception 
		 */
		PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoPais(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, TipoNivelEducacional tipoNivelEducacional) throws Exception;

		PerfilAcessoVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		PerfilAcessoVO consultarPorChavePrimariaUnica(Integer valorConsulta, boolean controlarAcesso,
				int nivelMontarDados, UsuarioVO usuario) throws Exception;

		PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoPais(
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, MatriculaVO matriculaVO)
				throws Exception;
		
}