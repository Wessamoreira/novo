package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;

public interface EtiquetaLivroRelInterfaceFacade {

	// public EtiquetaLivroRelVO realizarCriacaoOjbRel(CatalogoVO catalogo,
	// Integer inicialExemplar, TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta)
	// throws Exception;

	public void validarDados(CatalogoVO catalogo, Integer biblioteca, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, Integer unidadeEnsino, LayoutEtiquetaVO layoutEtiquetaVO, List<ExemplarVO> exemplarVOs) throws Exception;

	public String realizarImpressaoEtiquetaBiblioteca(LayoutEtiquetaVO layoutEtiqueta, CatalogoVO catalogo, Integer unidadeEnsino, Integer biblioteca, List<ExemplarVO> listaExemplarVOs, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado, String ordenacao, String nomeBiblioteca) throws Exception;

	void preencherTodosListaExemplar(List<ExemplarVO> exemplarVOs);

	void desmarcarTodosListaExemplar(List<ExemplarVO> exemplarVOs);
}
