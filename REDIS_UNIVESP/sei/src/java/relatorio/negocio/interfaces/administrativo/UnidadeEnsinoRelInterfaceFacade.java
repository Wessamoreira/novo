package relatorio.negocio.interfaces.administrativo;

import negocio.comuns.arquitetura.UsuarioVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface UnidadeEnsinoRelInterfaceFacade {

	public String emitirRelatorio(UsuarioVO usuarioVO) throws Exception;

	public void inicializarParametros();

	public void inicializarOrdenacoesRelatorio();

	public SqlRowSet executarConsultaParametrizada() throws Exception;

	public Integer getCurso();

	public void setCurso(Integer curso);

	public Integer getUnidadeEnsino();

	public void setUnidadeEnsino(Integer unidadeEnsino);

	public String getUnidadeEnsinonome();

	public void setUnidadeEnsinonome(String unidadeEnsinonome);

	public String getCursonome();

	public void setCursonome(String cursonome);

}