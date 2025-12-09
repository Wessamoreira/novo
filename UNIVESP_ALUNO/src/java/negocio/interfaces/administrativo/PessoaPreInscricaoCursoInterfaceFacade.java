package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.PessoaPreInscricaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PessoaPreInscricaoCursoInterfaceFacade {

	public PessoaPreInscricaoCursoVO novo() throws Exception;

	public void incluir(PessoaPreInscricaoCursoVO obj) throws Exception;

	public void incluir(PessoaPreInscricaoCursoVO obj, boolean verificarAcesso) throws Exception;

	public void alterar(PessoaPreInscricaoCursoVO obj) throws Exception;

	public void alterar(PessoaPreInscricaoCursoVO obj, boolean verificarAcesso) throws Exception;

	public void excluir(PessoaPreInscricaoCursoVO obj) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
        
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void excluirPessoaPreInscricaoCursos(Integer pessoa, List objetos) throws Exception;

	public void excluirPessoaPreInscricaoCursos(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception;

	public void alterarPessoaPreInscricaoCursos(Integer pessoa, List objetos) throws Exception;

	public void alterarPessoaPreInscricaoCursos(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception;

	public void incluirPessoaPreInscricaoCursos(Integer pessoaPrm, List objetos) throws Exception;

	public void incluirPessoaPreInscricaoCursos(Integer pessoaPrm, List objetos, boolean verificarAcesso) throws Exception;

	public PessoaPreInscricaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

        public PessoaPreInscricaoCursoVO consultarFuncionarioResponsavel(Integer codCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
        
	public void setIdEntidade(String idEntidade);

}