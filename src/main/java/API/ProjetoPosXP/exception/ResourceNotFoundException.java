package API.ProjetoPosXP.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Long resourceId;

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " não encontrado com ID: " + resourceId);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Long getResourceId() {
        return resourceId;
    }
}
