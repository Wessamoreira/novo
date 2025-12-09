package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class OrganizationTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("identifier")
    private String identifier;

    // Pode ser mapeado para um objeto mais específico se desejar
    @SerializedName("owner")
    private Object owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }
}
