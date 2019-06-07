package es.fporto.demo.controller.exception;

@SuppressWarnings("serial")
public class CustomSalesException extends RuntimeException{

		public CustomSalesException(String exception) {
			super(exception);
		}

}
