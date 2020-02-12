public class ListLoggingHandler extends CommandEventHandler {
    public ListLoggingHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    @Override
    protected String execute(String param) {
        return null;
    }
}
