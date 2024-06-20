package src;

public class NodeNameExistsException extends Exception {
    public NodeNameExistsException() {
        super("Node name already exists");
    }

    public NodeNameExistsException(String label) {
        super("Node name " + label + " already exists");
    }
}
