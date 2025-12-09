package negocio.interfaces.academico;

import org.json.simple.JSONObject;

import negocio.comuns.academico.LogRegistroOperacoesVO;
import negocio.comuns.academico.enumeradores.OperacaoLogRegistroOperacoesEnum;
import negocio.comuns.academico.enumeradores.TabelaLogRegistroOperacoesEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogRegistroOperacoesInterfaceFacade {

	public LogRegistroOperacoesVO novo() throws Exception;

	public void incluir(LogRegistroOperacoesVO obj, UsuarioVO usuario) throws Exception;

	LogRegistroOperacoesVO inicializarDadosLogRegistroOperacoes(TabelaLogRegistroOperacoesEnum origemLogRegistroOperacoes, OperacaoLogRegistroOperacoesEnum operacaoLogRegistroOperacoes, JSONObject jsonObjectRowData, JSONObject jsonObjectChangedFields, String observacao, UsuarioVO responsavelVO);

	
}