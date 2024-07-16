package ua.foxminded.universitycms.repository;

@SuppressWarnings("serial")
public class RepositoryException extends Exception {

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
