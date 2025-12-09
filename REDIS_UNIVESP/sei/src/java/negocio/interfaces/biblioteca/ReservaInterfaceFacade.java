package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.ReservaVO;

public interface ReservaInterfaceFacade {

	public ReservaVO novo() throws Exception;

	public void incluirReservas(List<ReservaVO> reservaVOs, UsuarioVO usuario) throws Exception;

	public void incluir(ReservaVO reservaVO, UsuarioVO usuario) throws Exception;

	public void excluir(ReservaVO obj) throws Exception;

	public ReservaVO consultarPorChavePrimaria(Integer codigoReserva, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	public void incluir(ReservaVO reservaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public Boolean verificarExistenciaReservaCatalogoParaDeterminadaPessoa(Integer codigoCatalogo, Integer codigoPessoa, Integer biblioteca) throws Exception;

	public Integer consultarNumeroDeCatalogosReservadosParaDeterminadaPessoa(Integer codigoPessoa) throws Exception;

	public ReservaVO montarReserva(CatalogoVO catalogo, PessoaVO pessoa, Date prazoValidadeReserva, String tipoPessoa, BibliotecaVO biblioteca, String matricula, UsuarioVO usuario) throws Exception;

	public List<ReservaVO> consultarReservasPorCodigoPessoa(Integer codigoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarReservasPorCodigoCatalogo(Integer codigoCatalogo) throws Exception;

	public void verificarPossibilidadeEmprestimoCatalogoComBaseNoNumeroDeReservas(Integer codigoCatalogo, Integer codigoPessoa, Integer biblioteca)
			throws Exception;

	public void executarVinculoDaReservaParaUmEmprestimo(Integer codigoEmprestimo, Integer codigoPessoa, Integer codigoCatalogo) throws Exception;
	
	public String gerarStringParaTicket(List<ReservaVO> reservas, BibliotecaVO bibliotecaVO, ImpressoraVO impressoraVO,  ConfiguracaoBibliotecaVO confBibVO, UsuarioVO usuario) throws Exception;
	
	public Integer consultarNumeroDeExemplaresDisponiveisPorCatalogo(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confBibVO, Boolean validarFeriado) throws Exception;
	public Integer consultarQuantidadeDeReservasValidasPorCatalogo(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO) throws Exception;
	public List<ReservaVO> consultarReservasPorCatalogoPessoa(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO, PessoaVO pessoaVO, UsuarioVO usuario, Integer limit) throws Exception;
	void executarAlterarDataTerminoReservaDataReservaMaisAntigaPorCatalogoEEnviaMensagemReservaDisponivel(final CatalogoVO catalogoVO, ReservaVO reservaAnterior, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception;
	public List<ReservaVO> consultarReservasVencidasPorCatalogoPessoa(CatalogoVO catalogoVO, PessoaVO pessoaVO, ConfiguracaoBibliotecaVO conBibliotecaVO, UsuarioVO usuario) throws Exception;
	public void alterarSituacaoReserva(ReservaVO reservaVO, String situacao, UsuarioVO usuarioVO) throws Exception;
	public ReservaVO consultarReservaPorEmprestimoCatalogoPessoa(Integer emprestimo, Integer catalogo, Integer pessoa, UsuarioVO usuarioVO) throws Exception;
	public Integer consultarQuantidadeReservaEmAbertoPorCatalogoPessoa(Integer catalogo, Integer pessoa, Integer reserva) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

	void realizarExclusaoReservaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
}
