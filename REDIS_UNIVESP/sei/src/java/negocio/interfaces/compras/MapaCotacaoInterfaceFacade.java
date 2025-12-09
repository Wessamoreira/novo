/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.compras;

import java.util.List;

import controle.arquitetura.DataModelo;
import controle.compras.CotacaoControle.EnumSituacaoTramitacao;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoRelVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.EstatisticaCotacaoVO;
import negocio.comuns.compras.MapaCotacaoVO;

/**
 *
 * @author Rodrigo
 */
public interface MapaCotacaoInterfaceFacade {		

	public void aprovarCotacao(MapaCotacaoVO obj, String situacaoCotacaoAnterior, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config) throws Exception;

	public void indeferirCotacao(MapaCotacaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;

	public void revisarCotacao(MapaCotacaoVO obj, UsuarioVO responsavel) throws Exception;

	public EstatisticaCotacaoVO consultarEstatisticaCotacoesAtualizada(UsuarioVO usuario) throws Exception;

	List<CotacaoRelVO> getListaCotacaoRelatorio(MapaCotacaoVO mapaCotacaoVO, UsuarioVO usuario) throws Exception;

	void preencherDadosCompraCotacao(MapaCotacaoVO mapa, UsuarioVO usuario) throws Exception;

	void consultar(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento);

}
