package csm;

/**
 * definiert alle Tag-Namen fuer das XML-Dateiformat
 */
interface FileTagNames {

	// die gesamte CSM
	String TAG_CSM = "corestatemachine";

	// Atribute fuer Transitionen
	String ATTR_SOURCE = "source";
	String ATTR_TARGET = "target";
	String ATTR_EVENT = "event";
	String ATTR_GUARD = "guard";
	String ATTR_ACTION = "action";

	// Tags und Attribute fuer definierte Events und Variablen
	String TAG_EVENT = "event";
	String TAG_DEFERREDEVENT = "deferredEvent";
	String TAG_VARIABLE = "variable";
	String ATTR_UNIQUENAME = "uniquename";
	String ATTR_INITIAL = "initial";
	String ATTR_MINIMUM = "minimum";
	String ATTR_MAXIMUM = "maximum";

	// Attribute fuer alle Komponenten
	String ATTR_UNIQUE_ID = "uniqueId";
	String ATTR_X = "x";
	String ATTR_Y = "y";
	String ATTR_NAMECOMMENT = "description";

	// fuer den Typ der ExitStates
	String ATTR_KIND = "kind";

	// die konkreten States und Komponenten
	String TAG_REGION = "region";
	String TAG_ENTRYSTATE = "entrystate";
	String TAG_EXITSTATE = "exitstate";
	String TAG_COMPOSITESTATE = "compositestate";
	String TAG_FINALSTATE = "finalstate";
	String TAG_CHOICESTATE = "choicestate";
	String TAG_TRANSITION = "transition";
	
	// GuiMetadata
	String TAG_GUIMETADATA = "guimetadata";
	String ATTR_GUIID = "gui";
	String ATTR_VALUE = "value";
	String TAG_CDATA = "componentdata";
}
