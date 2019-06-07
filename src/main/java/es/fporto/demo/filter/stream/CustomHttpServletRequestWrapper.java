package es.fporto.demo.filter.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	private byte[] body;

	public CustomHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			body = IOUtils.toByteArray(request.getInputStream());
		} catch (IOException ex) {
			body = new byte[0];
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		return new CustomServletInputStream(new ByteArrayInputStream(body));

	}

}