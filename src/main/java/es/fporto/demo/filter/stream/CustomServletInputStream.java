package es.fporto.demo.filter.stream;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class CustomServletInputStream extends ServletInputStream{

	   private final InputStream sourceStream;

	    private boolean finished = false;


	    public CustomServletInputStream(InputStream sourceStream) {
	        this.sourceStream = sourceStream;
	    }

	    public final InputStream getSourceStream() {
	        return this.sourceStream;
	    }


	    @Override
	    public int read() throws IOException {
	        int data = this.sourceStream.read();
	        if (data == -1) {
	            this.finished = true;
	        }
	        return data;
	    }

	    @Override
	    public int available() throws IOException {
	        return this.sourceStream.available();
	    }

	    @Override
	    public void close() throws IOException {
	        super.close();
	        this.sourceStream.close();
	    }

	    @Override
	    public boolean isFinished() {
	        return this.finished;
	    }

	    @Override
	    public boolean isReady() {
	        return true;
	    }

	    @Override
	    public void setReadListener(ReadListener readListener) {
	        throw new UnsupportedOperationException();
	    }
	
}
