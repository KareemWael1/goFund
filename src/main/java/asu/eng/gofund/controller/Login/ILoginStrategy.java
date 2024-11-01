package asu.eng.gofund.controller.Login;

import java.util.Map;

public interface ILoginStrategy {
    boolean login(Map<String, String> credentials);
}
