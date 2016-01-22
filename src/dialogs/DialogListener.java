package dialogs;

import extras.PointcutContainer;

public interface DialogListener {

	public void saveAdvice(String input);
	
	public void savePointcut(PointcutContainer p);
	
	public void saveManualInput(String code);
}
