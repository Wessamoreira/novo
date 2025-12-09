package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Victor Hugo de Paula Costa - 13 de out de 2016
 *
 */
@XmlRootElement(name = "url")
public class UrlRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 13 de out de 2016
	 */
	private String url;

	@XmlElement(name = "url")
	public String getUrl() {
		if (url == null) {
			url = "";
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
