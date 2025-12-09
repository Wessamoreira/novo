package webservice.mundipagg.v2.ResourceClients;

import webservice.mundipagg.v2.DataContracts.PostNotification.StatusNotification;
import webservice.mundipagg.v2.EnumTypes.HttpContentTypeEnum;
import webservice.mundipagg.v2.Utility.SerializeUtility;

/**
 * Recurso para post de notificação
 */
public class PostNotificationResource {
    
    /**
     * Construtor da Classe
     */
    public PostNotificationResource() {}
    
    /**
     * Converte XML de Post de Notificação em Objeto 
     * @param xml
     * @return 
     */
    public StatusNotification parseFromXML(String xml)
    {
        // Inicia objeto de serialização e deserialização para efetuar parse
        SerializeUtility<StatusNotification> serializerResponse = new SerializeUtility();
        StatusNotification statusNotification = serializerResponse.Deserializer(StatusNotification.class, xml, HttpContentTypeEnum.Xml);
        return statusNotification;
    }
}
