package handler;

import service.ClearService;

public class ClearHandler {
    private final ClearService service = new ClearService();

    public ClearHandler(){
    }

    public void clear(){
        service.clearAll();
    }
}
