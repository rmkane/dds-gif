package dds.gif.lib;

/**
 * Additional enumeration of supported disposal types.
 * 
 * @see https://docs.oracle.com/javase/8/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
 */
public enum DisposalMode {
	NONE("none"),
	NO_DISPOSE("doNotDispose"),
	RESTORE_BACKGROUND("restoreToBackgroundColor"),
	RESTORE_PREVIOUS("restoreToPrevious");

	private String disposalMethod;

	private DisposalMode(String disposalMethod) {
		this.disposalMethod = disposalMethod;
	}

	public String getDisposalMethod() {
		return disposalMethod;
	}
}