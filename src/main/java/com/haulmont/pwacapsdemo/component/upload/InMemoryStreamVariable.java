package com.haulmont.pwacapsdemo.component.upload;

import com.vaadin.flow.server.StreamVariable;
import com.vaadin.flow.server.VaadinRequest;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple implementation of StreamVariable for custom uploads to server.
 * Stores uploaded content in memory.
 * Does not listen for upload progress.
 */
public class InMemoryStreamVariable implements StreamVariable {

    private final StreamingFinishedListener finishListener;

    private ByteArrayOutputStream baos;

    public InMemoryStreamVariable(StreamingFinishedListener finishListener) {
        this.finishListener = finishListener;
    }

    @Override
    public OutputStream getOutputStream() {
        baos = new ByteArrayOutputStream();
        return baos;
    }

    @Override
    public boolean listenProgress() {
        return false;
    }

    @Override
    public void onProgress(StreamingProgressEvent event) {
        // listening for progress isn't implemented
    }

    @Override
    public void streamingStarted(StreamingStartEvent event) {
        // listening for progress isn't implemented
    }

    @Override
    public void streamingFinished(StreamingEndEvent event) {
        // event.getFileName(), event.getMimeType() have dummy values
        // set here in code: com.vaadin.flow.server.communication.StreamReceiverHandler.doHandleXhrFilePost
        // so we have to pass these values ourselves through http headers
        VaadinRequest vaadinRequest = VaadinRequest.getCurrent();
        String fileName = vaadinRequest.getHeader("File-Name");
        String mimeType = vaadinRequest.getHeader(HttpHeaders.CONTENT_TYPE);
        finishListener.streamingSucceeded(new ByteArrayInputStream(baos.toByteArray()), fileName, mimeType);
        baos = null;
    }

    @Override
    public void streamingFailed(StreamingErrorEvent event) {
        Exception exception = event.getException();
        finishListener.streamingFailed(exception);
    }

    @Override
    public boolean isInterrupted() {
        return false;
    }

    public interface StreamingFinishedListener {

        /**
         * Process uploaded file
         * @param fileContent input stream with file content
         * @param fileName name of the file passed from client
         * @param mimeType content mime type
         */
        void streamingSucceeded(InputStream fileContent, String fileName, String mimeType);

        /**
         * Handle upload failure
         *
         * @param exception exception object
         */
        void streamingFailed(Exception exception);
    }
}
