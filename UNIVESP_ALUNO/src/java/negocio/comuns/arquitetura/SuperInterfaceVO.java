package negocio.comuns.arquitetura;

import java.util.Date;

public abstract interface SuperInterfaceVO {

	public Boolean getNovoObj();

	public void setNovoObj(Boolean novoObj);

	public Boolean getMaximixado();

	public void setMaximixado(Boolean maximixado);

	public Date getCreated();

	public void setCreated(Date created);

	public Integer getCodigoCreated();

	public void setCodigoCreated(Integer codigoCreated);

	public String getNomeCreated();

	public void setNomeCreated(String nomeCreated);

	public Integer getCodigoUpdated();

	public void setCodigoUpdated(Integer codigoUpdated);

	public String getNomeUpdated();

	public void setNomeUpdated(String nomeUpdated);

	public void setUpdated(Date updated);

	public Date getUpdated();

	public void setControlarConcorrencia(Boolean controlarConcorrencia);

	public Boolean getControlarConcorrencia();
	

	public void setEdicaoManual(boolean edicaoManual);

	public boolean isEdicaoManual();

	public Boolean isValidarDados();

	public void setValidarDados(Boolean validarDados);
}
