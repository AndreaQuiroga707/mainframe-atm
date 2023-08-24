package bo.edu.ucb.sis213.bl.util;

public class ATMException extends Exception {
    
    int ExitStatus = -1;

    public ATMException(String message) {
        super(message);
    }

    public ATMException(String message, int exitStatus) {
        super(message);
        this.ExitStatus = exitStatus;
    }

    public int getExitStatus(){
        return this.ExitStatus;
    }
}