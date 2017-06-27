package mipsy.types;

/**
 * Created by prg01 on 6/27/2017.
 */
public class FailedToParseInstructionException extends Exception {
    public int line;
    public String lineText;

    public Exception innerException;

    public FailedToParseInstructionException(int line, String lineText, Exception inner) {
        this.line = line;
        this.lineText = lineText;
        this.innerException = inner;
    }
}
