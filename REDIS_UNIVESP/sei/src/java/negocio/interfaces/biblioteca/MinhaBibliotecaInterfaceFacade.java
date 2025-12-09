package negocio.interfaces.biblioteca;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.TimeLineVO;
import negocio.comuns.utilitarias.TipoFiltroConsulta;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public interface MinhaBibliotecaInterfaceFacade {

	
	public List<CatalogoVO> consultarPorTitulo(String urlAcessoExternoAplicacao ,String titulo, boolean controlarAcesso, Integer biblioteca,UsuarioVO usuario) throws Exception;
	
	public List<CatalogoVO> consultarCatalogos(String codigoCatalogo ,DataModelo dataModelo, BibliotecaVO bibliotecaVO, List<TipoFiltroConsulta> tipoFiltroConsultas, String filtroTopo , String ordenarPor, String urlAcessoExternoAplicacao,String codigoTombo, String titulo, String assunto, String palavrasChave, String autores, String classificacao, String isbn, String issn, UsuarioVO usuario) throws Exception;
	
	public List<BibliotecaVO>consultarBiblioteca() throws Exception;
	
	public List<TimeLineVO>listarDadosTimeLine(Integer codigoPessoa , UsuarioVO usuarioVO) throws Exception;
	
	public List<TimeLineVO>listarDadosTimeLineReservas(Integer codigoPessoa , UsuarioVO usuarioVO) throws Exception;
	
	public Double realizarCalculoMultaParcial(TimeLineVO timeLineVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO) throws Exception;
	
	 public void criarCadastroVisitante(PessoaVO pessoaVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema , String usernameMembroComunidade , String senhaMembroComunidade) throws Exception;
	 
	 public List<TimeLineVO> montarDadosTimeLine(UsuarioVO usuario) throws Exception;
	
}
