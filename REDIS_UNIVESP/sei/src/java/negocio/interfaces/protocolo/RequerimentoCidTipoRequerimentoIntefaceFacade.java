package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoCidTipoRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoVO;

public interface RequerimentoCidTipoRequerimentoIntefaceFacade {

public void incluirRequerimentoCidTipoRequerimentoVOs(RequerimentoVO requerimento, UsuarioVO usuario) throws Exception;
	
public void incluir(final RequerimentoCidTipoRequerimentoVO obj, RequerimentoVO requerimento, CidTipoRequerimentoVO cidTipoRequerimentoVO, UsuarioVO usuario) throws Exception;
}
