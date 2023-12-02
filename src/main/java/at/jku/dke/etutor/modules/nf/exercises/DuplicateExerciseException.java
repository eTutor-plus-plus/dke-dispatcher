package at.jku.dke.etutor.modules.nf.exercises;

public class DuplicateExerciseException extends Exception {

	public DuplicateExerciseException() {
		super();
	}

	public DuplicateExerciseException(String arg0) {
		super(arg0);
	}

	public DuplicateExerciseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateExerciseException(Throwable arg0) {
		super(arg0);
	}

	public String getMessage(){
		if ((super.getMessage() == null) || (super.getMessage().isEmpty())){
			return "An exercise with the same specification already exists.";
		} else {
			return "Exercise " + super.getMessage() + " has the same specification.";
		}
	}
}
