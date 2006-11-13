package csm;

/**
 * definiert alle Tag-Namen für das XML-Dateiformat
 */
public interface FileTagNames {

	// für das Positions-Attribut aller Komponenten
	String TAG_POSITION = "position";
	String ATTR_X = "x";
	String ATTR_Y = "y";

	// für den Typ der ExitStates
	String ATTR_KIND = "kind";

	// für die eindeutige Nummer jedes States
	String ATTR_UNIQUE_ID = "uniqueId";

	// die konkreten States und Komponenten
	String TAG_OUTERMOSTREGION = "outermostregion";
	String TAG_REGION = "region";
	String TAG_ENTRYSTATE = "entrystate";
	String TAG_EXITSTATE = "exitstate";
	String TAG_COMPOSITESTATE = "compositestate";
	String TAG_FINALSTATE = "finalstate";
	String TAG_CHOICESTATE = "choicestate";

}
