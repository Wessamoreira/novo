package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;

public interface ArquivoMarc21InterfaceFacade {
	
	public void executarProcessarArquivoMarc21(ArquivoMarc21VO obj, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioVO) throws Exception;
	public void incluir(final ArquivoMarc21VO obj, UsuarioVO usuarioVO) throws Exception;
	public void validarDados(ArquivoMarc21VO obj) throws Exception;
	public List<ArquivoMarc21VO> consultarPorResponsavel(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	public void alterar(final ArquivoMarc21VO obj, UsuarioVO usuarioVO) throws Exception;
	public List<ArquivoMarc21VO> consultarPorDataProcessamento(Date dataInicial, Date dataFinal, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	public void executarExportarArquivoMarc21(ArquivoMarc21VO arquivoMarc21VO, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioVO) throws Exception;
	public List<ArquivoMarc21VO> montarArquivoMarc21DadosCatalogo(Integer codigoCatalogo ,UsuarioVO usuario)throws Exception;
	public void executarExportarArquivoMarc21XML(ArquivoMarc21VO arquivoMarc21VO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO config, String recordStatus ) throws Exception;

}
