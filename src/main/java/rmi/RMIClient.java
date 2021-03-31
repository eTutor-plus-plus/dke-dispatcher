package rmi;

import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private Evaluator sqlEvaluator;

    public void startClient() throws RemoteException, NotBoundException {
        //connection to server
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //search server
        sqlEvaluator = (Evaluator) registry.lookup("SQLEvaluator");
    }

    public Evaluator getSQLEvaluator() {
        return sqlEvaluator;
    }
}
