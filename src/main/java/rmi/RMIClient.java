package rmi;



import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private etutor.core.evaluation.Evaluator sqlEvaluator;

    public void startClient() throws RemoteException, NotBoundException {
        //connection to server
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //search server
        sqlEvaluator = (etutor.core.evaluation.Evaluator) registry.lookup("SQLEvaluator");
    }

    public etutor.core.evaluation.Evaluator getSQLEvaluator() {
        return sqlEvaluator;
    }
}
