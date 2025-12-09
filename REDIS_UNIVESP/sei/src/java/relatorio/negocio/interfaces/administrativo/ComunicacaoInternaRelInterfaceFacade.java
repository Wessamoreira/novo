package relatorio.negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.administrativo.ComunicacaoInternaRelVO;

public interface ComunicacaoInternaRelInterfaceFacade {

	public List<ComunicacaoInternaRelVO> criarObjeto(Integer unidadeEnsino, String tipoPessoa, Integer codigoPessoa, Date dataInicio, Date dataFim, Boolean filtroLeituraRegistrada, String tipoOrdenacao, UsuarioVO usuarioVO) throws Exception;

}
