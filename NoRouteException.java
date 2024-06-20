package src;

public class NoRouteException extends Exception {
    public NoRouteException() {
        super("No route found");
    }

    public NoRouteException(String fromNode, String toNode) {
        super("No route found from \"" +
                fromNode +
                "\" to \"" +
                toNode + "\"");
    }
}
