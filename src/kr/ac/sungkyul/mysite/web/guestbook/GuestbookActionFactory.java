package kr.ac.sungkyul.mysite.web.guestbook;

import kr.ac.sungkyul.web.Action;
import kr.ac.sungkyul.web.ActionFactory;

public class GuestbookActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		Action action = null;
		if( "deleteform".equals( actionName ) ) {
			action = new DeleteFormAction();
		} else if( "delete".equals( actionName ) ) {
			action = new DeleteAction();
		} else if( "insert".equals( actionName ) ) {
			action = new InsertAction();
		} else {
			action = new ListAction();
		}		
		
		return action;
	}

}
