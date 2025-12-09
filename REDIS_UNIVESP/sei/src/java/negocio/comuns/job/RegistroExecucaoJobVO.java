package negocio.comuns.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
 *
 */
public class RegistroExecucaoJobVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
	 */
	private Integer codigo;
	private String nome;
	private Date dataInicio;
	private Date dataTermino;
	private long tempoExecucao;
	private String erro;
	private int total;
	private int totalSucesso;
	private int totalErro;
	private Integer codigoOrigem;		
	//Transiente
	private List<String> listaJobsEnum;
	private Map<String, PessoaVO> mapResponsavelComunicadoInterno;
	
	public RegistroExecucaoJobVO() {
		super();
	}
	
	public RegistroExecucaoJobVO(String nome, String erro) {
		super();
		this.nome = nome;
		this.erro = erro;
		this.tempoExecucao = 0;
		this.total = 0;
		this.totalErro = 0;
		this.totalSucesso = 0;
	}
	
	public RegistroExecucaoJobVO(String nome, String erro, Integer codigoOrigem) {
		super();
		this.nome = nome;
		this.erro = erro;
		this.codigoOrigem = codigoOrigem;
		this.tempoExecucao = 0;
		this.total = 0;
		this.totalErro = 0;
		this.totalSucesso = 0;
	}
	
	public RegistroExecucaoJobVO(String nome, String erro , Date dataInicio) {
		super();
		this.nome = nome;
		this.erro = erro;
		this.dataInicio = dataInicio;
	}
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataInicioApresentar() {
		return Uteis.getDataComHora(getDataInicio());
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataTerminoApresentar() {
		return Uteis.getDataComHora(getDataTermino());
	}

	
	/**
	 * Campo Data Termino nao poder ser inicializado caso o mesmo esteja nulo.
	 * @return
	 */
	public Date getDataTermino() {				
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public long getTempoExecucao() {
		return tempoExecucao;
	}

	public void setTempoExecucao(long tempoExecucao) {
		this.tempoExecucao = tempoExecucao;
	}

	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalSucesso() {
		return totalSucesso;
	}

	public void setTotalSucesso(int totalSucesso) {
		this.totalSucesso = totalSucesso;
	}

	public int getTotalErro() {
		return totalErro;
	}

	public void setTotalErro(int totalErro) {
		this.totalErro = totalErro;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Integer getCodigoOrigem() {		
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}	

	public List<String> getListaJobsEnum() {
		if (listaJobsEnum == null) {
			listaJobsEnum = new ArrayList<>();
		}
		return listaJobsEnum;
	}

	public void setListaJobsEnum(List<String> listaJobsEnum) {
		this.listaJobsEnum = listaJobsEnum;
	}	

	public static synchronized void incrementarTotal(RegistroExecucaoJobVO registroExecucaoJob, int total) {
		if (Uteis.isAtributoPreenchido(total)) {
			registroExecucaoJob.setTotal(registroExecucaoJob.getTotal() + total);
		}
	}
	
	public static synchronized void incrementarTotalErro(RegistroExecucaoJobVO registroExecucaoJob, int total) {
		if (Uteis.isAtributoPreenchido(total)) {
			registroExecucaoJob.setTotalErro(registroExecucaoJob.getTotalErro() + total);
		}
	}
	
	public static synchronized void incrementarTotalSucesso(RegistroExecucaoJobVO registroExecucaoJob, int total) {
		if (Uteis.isAtributoPreenchido(total)) {
			registroExecucaoJob.setTotalSucesso(registroExecucaoJob.getTotalSucesso() + total);
		}
	}
	
	public static synchronized void incrementarErro(RegistroExecucaoJobVO registroExecucaoJob, String erro) {
		if (Uteis.isAtributoPreenchido(erro)) {
			registroExecucaoJob.setErro((Uteis.isAtributoPreenchido(registroExecucaoJob.getErro()) ? registroExecucaoJob.getErro() + ", " : Constantes.EMPTY) + erro);
		}
	}
	
	public Map<String, PessoaVO> getMapResponsavelComunicadoInterno() {
		if (mapResponsavelComunicadoInterno == null) {
			mapResponsavelComunicadoInterno = new HashMap<>(0);
		}
		return mapResponsavelComunicadoInterno;
	}
	
	public void setMapResponsavelComunicadoInterno(Map<String, PessoaVO> mapResponsavelComunicadoInterno) {
		this.mapResponsavelComunicadoInterno = mapResponsavelComunicadoInterno;
	}
}
