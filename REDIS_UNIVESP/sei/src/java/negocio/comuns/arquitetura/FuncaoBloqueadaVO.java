package negocio.comuns.arquitetura;

/**
 *
 * @author Kennedy
 */
import java.io.Serializable;

public class FuncaoBloqueadaVO implements Serializable {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	private String descricao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}	

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
