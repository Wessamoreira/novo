package negocio.comuns.arquitetura;

import java.util.Objects;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.google.common.base.Preconditions;

import negocio.facade.jdbc.arquitetura.ControleAcesso;


public abstract class SuperFacade<T extends SuperVO> extends ControleAcesso   {

	private static final long serialVersionUID = 605125272835549552L;

	public void persistir(T t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		this.validarDadosSuperVO(t);
		if (isNovoObjeto(t)) {
			incluir(t, validarAcesso, usuarioVO);
		} else {
			alterar(t, validarAcesso, usuarioVO);
		}
	}
	
	

	public void validarDadosSuperVO(T t) throws Exception {
		Preconditions.checkState(Objects.nonNull(t.isNovoObj()), "Campo novo objeto do VO não pode ser nulo.");
		
	}

	public abstract void incluir(T t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract void alterar(T t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract void excluir(T t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract T consultarPorChavePrimaria(Long id) throws Exception;

	public abstract T montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception;

	private boolean isNovoObjeto(T t) {
		return Objects.isNull(t.isNovoObj());
	}
}